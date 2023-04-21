package perun.isle;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import perun.common.UpdateSwitch;
import perun.common.distrib.Contact;
import perun.common.distrib.DistServer;
import perun.common.log.Log;
import perun.isle.event.BiologicalEvent;
import perun.isle.scheduler.SchedulerFactory;
import perun.isle.virtualmachine.VirtualMachineFactory;
import perun.node.NodeRI;

/**
 * NodeAdmin manages connected nodes and handles organism transactions.
 */
public class NodeAdmin extends UnicastRemoteObject implements NodeAdminRI {

    /**
	 * Indicates need of sending new scheduler, vmFactory or environment to nodes.
	 */
    private boolean doCheckOnUpdate = true;

    /**
	 * Num of allocated nodes
	 */
    private int maxNodeID = 0;

    private int lastSent = 0;

    /**
	 * Array to store connected nodes.
	 */
    private final Map<Integer, NodeItem> nodes = new LinkedHashMap<Integer, NodeItem>();

    /**
	 * Organisms in population.
	 */
    private final Map<Integer, OrgItem> organisms;

    private CountDownLatch countDown;

    /**
	 * Lock for locking updating.
	 */
    private UpdateSwitch updateLock;

    /**
	 * Lock for locking nodes array.
	 */
    private ReadWriteLock nodesLock;

    /**
	 * Registered contact in rmi.
	 */
    public Contact contact;

    /**
	 * instance
	 */
    private static NodeAdmin instance;

    private final List<Integer> orgsToRemove = new LinkedList<Integer>();

    /**
	 * Returns instance of node admin.
	 * @return node admin
	 */
    public static NodeAdmin getInstance() {
        return instance;
    }

    /**
	 * Constructs NodeAdmin object and exports it on default port.
	 */
    public NodeAdmin(Map<Integer, OrgItem> orgs) throws RemoteException {
        this(0, orgs);
    }

    /**
	 * Constructs NodeAdmin object and exports it on specified port.
	 * @param port The port for exporting
	 */
    public NodeAdmin(int port, Map<Integer, OrgItem> orgs) throws RemoteException {
        super(port);
        init();
        organisms = orgs;
    }

    /**
	 * Called from constructor - common initialization
	 */
    private void init() {
        instance = this;
        String name = DistServer.RegisterObject("NodeAdmin", (NodeAdminRI) this);
        contact = new Contact(name);
        Log.event(Log.INFO, "Node admin registered on " + name);
        nodesLock = new ReentrantReadWriteLock();
    }

    /**
	 * Sets up update lock from population.
	 * @param lock
	 */
    public void setUpdateLock(UpdateSwitch lock) {
        updateLock = lock;
    }

    /**
	 * Do update on nodes.
	 * @param newStats statistics for scheduler from last update.
	 * @return new statistics
	 */
    public UpdateStatistics update(UpdateStatistics newStats, Environment env) {
        Isle.getPopulation().removeOrganisms(orgsToRemove);
        orgsToRemove.clear();
        updateLock.EnterUpdate();
        nodesLock.readLock().lock();
        int slice = env.getAvgTimeSlice();
        Log.event(Log.VERBOSE, "update in nodeadmin, slice is " + slice);
        countDown = new CountDownLatch(nodes.size());
        for (NodeItem n : nodes.values()) {
            n.start();
            try {
                n.getNode().update(slice, newStats);
            } catch (Exception e) {
                checkNodes(n);
            }
        }
        while (countDown.getCount() > 0) {
            try {
                countDown.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Log.exception(Log.WARNING, "update control thread was interrupted", ex);
            }
        }
        updateLock.ExitUpdate();
        if (Isle.getInstance().nodesdeb && (Isle.getInstance().getUpdateCount() % 100 == 0)) {
            for (NodeItem n : nodes.values()) {
                int id = n.getID();
                int cnt = n.getOrgCnt();
                long time = n.getUpdateLength();
                double tpo = -1;
                if (cnt > 0) tpo = time / (double) cnt;
                System.out.println("node " + id + " orgs " + cnt + " time " + time + " tpo " + tpo);
            }
        }
        UpdateStatistics stats = new UpdateStatistics();
        for (NodeItem n : nodes.values()) {
            n.mergeStats(stats);
        }
        nodesLock.readLock().unlock();
        stats.finalCount();
        return stats;
    }

    private void checkPopulation() {
        if (!doCheckOnUpdate) return;
        doCheckOnUpdate = false;
        NodeRI local = nodes.get(0).getNode();
        for (Map.Entry<Integer, OrgItem> ent : organisms.entrySet()) {
            OrgItem it = ent.getValue();
            it.nodeCrashed(maxNodeID);
        }
    }

    private OrgItem getOrgItem(int id) {
        if (id == -1) return null;
        OrgItem it = organisms.get(id);
        if ((it == null) || it.isDying()) return null;
        return it;
    }

    /**
	 * Put organism to local node.
	 * Needed when leech want to connect to this organism.
	 * @param id id of organism
	 */
    public void putOrganismToLocal(int id) {
        OrgItem it = organisms.get(id);
        int node = it.getNodeID();
        if (node == 0) return;
        nodesLock.readLock().lock();
        NodeItem n = nodes.get(node);
        NodeRI nri = n.getNode();
        DigitalOrganism org = null;
        try {
            org = nri.getOrganism(id);
            it.moving(org);
            nri.removeOrganismsFromNode(Arrays.asList(id));
            nodesLock.readLock().unlock();
        } catch (RemoteException e) {
            nodesLock.readLock().unlock();
            checkNodes(n);
            if (org == null) org = it.getCachedOrganism();
        }
        try {
            nodes.get(0).getNode().setOrganisms(new DigitalOrganism[] { org });
            it.onNode(0);
        } catch (RemoteException e) {
            Log.exception(Log.WARNING, "Failed to put organism to local node", e);
        }
    }

    /**
	 * Clear nodes and sends new organisms to them.
	 * This is done on begining of simulation or when load occurs.
	 * @param orgs organisms to send
	 */
    public void distributeOrganisms(DigitalOrganism orgs[]) {
        updateLock.WaitForFreeAndLock();
        if (nodes.size() == 1) {
            try {
                nodes.get(0).getNode().clear();
                nodes.get(0).getNode().setOrganisms(orgs);
            } catch (RemoteException e) {
                Log.exception(Log.ERROR, "failed to distribute organisms", e);
            }
            for (int i = 0; i < orgs.length; i++) {
                if (orgs[i] != null) organisms.get(orgs[i].getID()).onNode(0);
            }
            updateLock.UnLock();
            return;
        }
        nodesLock.readLock().lock();
        int count = (int) (orgs.length / (double) nodes.size()) + 1;
        DigitalOrganism orgsToNode[] = new DigitalOrganism[count];
        int startOrg = 0;
        for (NodeItem n : nodes.values()) {
            int len = count;
            if ((startOrg + count) >= orgs.length) {
                Arrays.fill(orgsToNode, 0, count, null);
                len = orgs.length - startOrg;
            }
            System.arraycopy(orgs, startOrg, orgsToNode, 0, len);
            startOrg += count;
            int targetNode = n.getID();
            NodeRI nri = n.getNode();
            try {
                nri.clear();
                nri.setOrganisms(orgsToNode);
            } catch (RemoteException e) {
                checkNodes(n);
                try {
                    nodes.get(0).getNode().setOrganisms(orgsToNode);
                } catch (RemoteException re) {
                    Log.exception(Log.WARNING, re);
                }
                targetNode = 0;
            }
            for (int i = 0; i < len; i++) {
                organisms.get(orgsToNode[i].getID()).onNode(targetNode);
            }
        }
        nodesLock.readLock().unlock();
        updateLock.UnLock();
    }

    /**
	 * Gets organisms from nodes.
	 * @param ID id of organisms
	 * @return array of organisms
	 */
    public DigitalOrganism[] getOrganisms(int[] ID) {
        DigitalOrganism orgs[] = new DigitalOrganism[ID.length];
        DigitalOrganism tempOrgs[];
        nodesLock.readLock().lock();
        for (int i = 0; i < ID.length; i++) {
            if (ID[i] == -1) continue;
            if (orgs[i] != null) continue;
            int node = organisms.get(ID[i]).getNodeID();
            NodeItem n = nodes.get(node);
            try {
                tempOrgs = n.getNode().getOrganisms(ID);
            } catch (RemoteException e) {
                checkNodes(n);
                continue;
            }
            for (int j = 0; j < tempOrgs.length; j++) {
                if (tempOrgs[j] != null) {
                    orgs[j] = tempOrgs[j];
                    organisms.get(ID[j]).updateVM(orgs[j]);
                }
            }
        }
        nodesLock.readLock().unlock();
        return orgs;
    }

    public List<DigitalOrganism> getOrganisms(List<Integer> ids) {
        List<DigitalOrganism> orgs = new ArrayList<DigitalOrganism>(ids.size());
        int[] reqIDs = new int[ids.size()];
        Arrays.fill(reqIDs, -1);
        boolean req = false;
        for (int i = 0; i < ids.size(); i++) {
            orgs.add(null);
            int id = ids.get(i);
            OrgItem it = getOrgItem(id);
            if (it == null) continue;
            DigitalOrganism org = it.getOrganism();
            if (org != null) orgs.set(i, org); else {
                reqIDs[i] = id;
                req = true;
            }
        }
        if (req) {
            DigitalOrganism[] newOrgs = getOrganisms(reqIDs);
            for (int i = 0; i < newOrgs.length; i++) {
                if (newOrgs[i] != null) orgs.set(i, newOrgs[i]); else if (reqIDs[i] != -1) orgs.set(i, getOrgItem(reqIDs[i]).getCachedOrganism());
            }
        }
        return orgs;
    }

    /**
	 * Gets organism from node.
	 * @param ID id of organism
	 * @return organism or null if failed
	 */
    public DigitalOrganism getOrganism(int ID) {
        DigitalOrganism org = null;
        nodesLock.readLock().lock();
        int node = organisms.get(ID).getNodeID();
        try {
            org = nodes.get(node).getNode().getOrganism(ID);
            organisms.get(ID).updateVM(org);
        } catch (RemoteException e) {
            checkNodes(nodes.get(node));
            org = null;
        }
        nodesLock.readLock().unlock();
        return org;
    }

    /**
	 * Sends new organism to node.
	 * @param org organism
	 */
    public void sendOrganism(DigitalOrganism org) {
        DigitalOrganism orgs[] = new DigitalOrganism[] { org };
        OrgItem it = organisms.get(org.getID());
        nodesLock.readLock().lock();
        int target = 0;
        for (int id : nodes.keySet()) {
            if (id > lastSent) target = id;
        }
        try {
            nodes.get(target).getNode().setOrganisms(orgs);
            it.onNode(target);
        } catch (RemoteException e) {
            checkNodes(nodes.get(target));
        }
        lastSent = target;
        nodesLock.readLock().unlock();
    }

    public void DiedOrgs(List<Integer> ids) throws RemoteException {
        orgsToRemove.addAll(ids);
    }

    /**
	 * Removes organisms from node and sets up organism cache.
	 * @param ids to be removed
	 */
    public void removeOrganisms(Collection<Integer> ids) {
        nodesLock.readLock().lock();
        for (NodeItem n : nodes.values()) {
            try {
                n.getNode().removeOrganismsFromNode(ids);
            } catch (RemoteException e) {
                checkNodes(n);
            }
        }
        for (int i : ids) {
            organisms.remove(i);
        }
        nodesLock.readLock().unlock();
    }

    public void removeOrganism(int id) {
        int node = organisms.get(id).getNodeID();
        NodeItem n = nodes.get(node);
        try {
            n.getNode().removeOrganismsFromNode(Collections.singletonList(id));
        } catch (RemoteException e) {
            checkNodes(n);
        }
    }

    /**
	 * Inicializes node.
	 * Sends scheduler and environment to node.
	 * @param node NodeRI interface
	 */
    private void initNode(NodeItem n) {
        NodeRI node = n.getNode();
        try {
            node.setEnvData(EnvMgr.env().getProps());
            node.setScheduler(SchedulerFactory.getScheduler());
            node.setVMFactory(VirtualMachineFactory.getInstance());
        } catch (RemoteException e) {
            Log.exception(Log.ERROR, "failed to reset node", e);
            checkNodes(n);
        }
    }

    /**
	 * Registers node to cluster.
	 * Node gets some components like scheduler and environment from isle.
	 * @param node caller node
	 * @return identification of node in cluster
	 * @throws RemoteException
	 */
    public int registerNode(NodeRI node) throws RemoteException {
        updateLock.WaitForFreeAndLock();
        nodesLock.writeLock().lock();
        int id = maxNodeID++;
        NodeItem newNode = new NodeItem(id, node);
        nodes.put(id, newNode);
        initNode(newNode);
        updateLock.UnLock();
        nodesLock.writeLock().unlock();
        Log.event("Registered another node " + newNode.getID());
        return id;
    }

    /**
	 * Node finished update.
	 * @param id id of node
	 * @param stat new organism
	 * @throws RemoteException
	 */
    public void updateFinished(int id, UpdateStatistics stat) throws RemoteException {
        if (!updateLock.IsUpdate()) {
            Log.event(Log.ERROR, "node in update when isle is not in update state");
            return;
        }
        NodeItem n = nodes.get(id);
        if (n == null) {
            Log.event(Log.WARNING, "Finish from unknown node " + id);
            return;
        }
        n.end(stat);
        countDown.countDown();
    }

    /**
	 * Check nodes and organism structure.
	 * @param node where occured exception.
	 */
    private void checkNodes(NodeItem n) {
        int id = n.getID();
        Log.event(Log.WARNING, "Node " + id + " crashed. Restoring population from cache.");
        nodesLock.writeLock().lock();
        nodes.remove(id);
        for (OrgItem it : organisms.values()) {
            it.nodeCrashed(id);
        }
        nodesLock.writeLock().unlock();
    }

    /**
	 * Shuts down all nodes.
	 */
    public void shutDownNodes() {
        updateLock.WaitForFreeAndLock();
        nodesLock.writeLock().lock();
        for (NodeItem n : nodes.values()) {
            try {
                n.getNode().shutDown();
            } catch (RemoteException re) {
                Log.event(Log.WARNING, "failed to shutdown node " + n.getID());
            }
        }
        nodes.clear();
        nodesLock.writeLock().unlock();
        updateLock.UnLock();
    }

    /**
	 * New organisms from @see NodeEnvironment arrived.
	 * @param pe array of new events
	 * @throws RemoteException
	 */
    public List<BiologicalEvent> bioEventPerformed(List<BiologicalEvent> be) throws RemoteException {
        Environment env = EnvMgr.env();
        updateLock.WaitForFreeAndLock();
        for (BiologicalEvent b : be) env.bioEventPerformed(b);
        updateLock.UnLock();
        return be;
    }

    public int readNumbers() {
        int sum = 0;
        for (int i = 0; i < 10000; i++) {
            for (NodeItem it : nodes.values()) {
                if (it.getID() == 0) continue;
                try {
                    sum += it.getNode().getNumber();
                } catch (RemoteException re) {
                }
            }
        }
        return sum;
    }
}
