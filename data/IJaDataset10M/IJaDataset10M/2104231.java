package net.emotivecloud.scheduler.goiri.scheduling.bdim;

import java.util.Iterator;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import net.emotivecloud.scheduler.goiri.SimulatorScheduler;
import net.emotivecloud.scheduler.goiri.VM;
import net.emotivecloud.scheduler.goiri.scheduling.Provider;
import net.emotivecloud.scheduler.goiri.scheduling.State;

/**
 * Profit-Efficient Business-driven policy
 * Outsource services with a revenue of GOLD or SILVER quality
 * @author fito
 *
 */
public class ProfEfficient extends SimulatorScheduler {

    private static final double GOLD = 1.0;

    private static final double SILVER = 0.5;

    private static final double BRONZE = 0.25;

    protected boolean outsourcing;

    protected ListOrderedMap providers;

    public ProfEfficient() {
        super();
        providers = new ListOrderedMap();
        try {
            PropertiesConfiguration conf = new PropertiesConfiguration("/etc/scheduler.properties");
            outsourcing = conf.getBoolean("outsourcing", false);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        System.out.println("Virtualization scheduler parameters:");
        System.out.println("     Outsourcing " + outsourcing);
    }

    protected void addNode(Node node, boolean on) {
        super.addNode(node, on);
    }

    public void schedule() {
        int numLocalNodes = onNodes.size();
        int numNodes = numLocalNodes + providers.size();
        int aux = 0;
        int posProvider = 0;
        boolean found = false;
        boolean outsource = false;
        float revenue = 0.0F;
        Iterator<String> itVM = vmsQueue.iterator();
        while (itVM.hasNext()) {
            String vmId = itVM.next();
            int posVM = this.vms.indexOf(vmId);
            VM vm = (VM) this.vms.getValue(posVM);
            float price_min = Float.MAX_VALUE;
            for (int i = 0; i < this.providers.size(); i++) {
                Provider p = (Provider) this.providers.getValue(i);
                if (vm.revenue == GOLD || vm.revenue == SILVER) {
                    if (p.price < price_min) {
                        price_min = p.price;
                        posProvider = i;
                    }
                    outsource = true;
                }
            }
            if (!outsource) {
                for (String auxHostId : onNodes) {
                    Node host = (Node) nodes.get(auxHostId);
                    if (vm.cpu / host.getCapacityCPU() <= host.getAvailableCPU() * 100) {
                        this.assignNode(posVM, onNodes.indexOf(auxHostId), State.RUN);
                        found = true;
                        break;
                    }
                }
            }
            if (!found || outsource) this.assignProvider(posVM, posProvider);
            itVM.remove();
        }
    }

    public void addProvider(String id, float boot, float price) {
        if (outsourcing) {
            Provider p = new Provider(this, id, boot, price);
            providers.put(id, p);
        }
    }

    public void sendTime(int t) {
        super.sendTime(t);
        for (int i = 0; i < this.providers.size(); i++) {
            Provider p = (Provider) this.providers.getValue(i);
            p.sendTime(t);
        }
    }

    protected void assignProvider(int posVM, int posProvider) {
        VM vm = (VM) this.vms.getValue(posVM);
        Provider prov = (Provider) providers.getValue(posProvider);
        prov.submitVM(vm);
        vmNode.put(vm.id, prov.id);
        this.addOutsourceEvent(vm.id);
    }

    protected void addOutsourceEvent(String vmId) {
        Iterator<String> it = scheduleQueue.iterator();
        while (it.hasNext()) {
            String event = it.next();
            if (event.startsWith("outsource") && event.split(" ")[1].equals(vmId)) {
                it.remove();
            }
        }
        scheduleQueue.addLast("outsource " + vmId);
    }

    /**
	 * Assign a VM to a given node.
	 * @param posVM Position of the VM.
	 * @param posNode Position of the Host.
	 * @param state State of the VM.
	 */
    protected Node assignNode(int posVM, int posNode, short state) {
        Node node = super.assignNode(posVM, posNode, state);
        return node;
    }
}
