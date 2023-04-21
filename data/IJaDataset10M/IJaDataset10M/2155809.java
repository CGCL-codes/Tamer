package org.uoa.eolus.vm;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.persistence.EntityManager;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.xmlrpc.XmlRpcException;
import org.opennebula.bridge.CloudClient;
import org.opennebula.bridge.CloudConstants;
import org.opennebula.bridge.VMInfo;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachinePool;
import org.uoa.eolus.InternalErrorException;
import org.uoa.eolus.config.ConfigurationsManager;
import org.uoa.eolus.config.ONEInfo;
import org.uoa.eolus.host.Host;
import org.uoa.eolus.host.Site;
import org.uoa.eolus.template.UnknownTemplateException;
import org.uoa.eolus.user.UnknownUserException;
import org.uoa.eolus.user.User;
import org.xml.sax.InputSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author jackal
 */
public class VirtualMachineManager {

    CloudClient cloud = null;

    EntityManager em = null;

    public VirtualMachineManager(EntityManager em) throws InternalErrorException {
        this.em = em;
        Init();
    }

    private void Init() throws InternalErrorException {
        try {
            cloud = ConfigurationsManager.getCloudConnector(em);
        } catch (Exception ex) {
            cloud = null;
            throw new InternalErrorException(ex.getMessage(), ex);
        }
        syncVMs();
    }

    private void syncVMs() throws InternalErrorException {
        System.out.println("Initializing...");
        try {
            User privateUser = em.find(User.class, "private");
            if (privateUser == null) {
                System.out.println("Private user does not exist. Creating him...");
                privateUser = new User();
                privateUser.setId("private");
                em.persist(privateUser);
            }
            CloudClient cloudClinet = new CloudClient();
            List<VMInfo> cloudvms = cloudClinet.getAllVMs();
            System.out.println("Check vms against DB");
            Iterator<VMInfo> cloudvmsit = cloudvms.iterator();
            while (cloudvmsit.hasNext()) {
                VMInfo info = (VMInfo) cloudvmsit.next();
                List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + info.name + "'").getResultList();
                if (ts.size() == 0) {
                    VirtualMachine vm = new VirtualMachine();
                    vm.setName(info.name);
                    vm.setUser(privateUser);
                    vm.setOneID(new Long(info.id));
                    em.persist(vm);
                } else {
                    VirtualMachine vm = (VirtualMachine) ts.get(0);
                    vm.setOneID(new Long(info.id));
                }
            }
            System.out.println("Check DB against vms");
            List<VirtualMachine> vms = em.createQuery("select vm from VirtualMachine vm ").getResultList();
            for (Object m : vms) {
                VirtualMachine vm = (VirtualMachine) m;
                String name = vm.getName();
                boolean found = false;
                Iterator<VMInfo> cvmsit = cloudvms.iterator();
                while (cvmsit.hasNext()) {
                    VMInfo info = (VMInfo) cvmsit.next();
                    if (info.name.equals(name)) {
                        found = true;
                    }
                }
                if (!found) {
                    em.remove(vm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalErrorException(e.getMessage(), e);
        }
    }

    public void Create(String username, String VMname, File desc) throws VMExistsException, UnknownTemplateException, IOException, InternalErrorException {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts.size() != 0) {
            throw new VMExistsException("VM with name " + VMname + " already exists");
        }
        User user = em.find(User.class, username);
        if (user == null) {
            System.out.println("User with name " + username + " not found");
            user = em.find(User.class, "private");
        }
        if (desc == null) {
            throw new UnknownTemplateException("Template description file is null.");
        }
        VMInfo vminfo = cloud.vmAllocate(desc);
        VirtualMachine vm = new VirtualMachine();
        vm.setName(VMname);
        vm.setOneID(Long.parseLong(vminfo.id));
        vm.setUser(user);
        em.persist(vm);
    }

    public boolean userOwnsVM(String user, String VMname) {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'" + " and vm.user.id='" + user + "'").getResultList();
        if (ts.size() != 0) return true; else return false;
    }

    public synchronized void Kill(String username, String VMname, Boolean force) throws UnknownVMException, InternalErrorException, UnknownUserException {
        if (!userOwnsVM(username, VMname)) throw new UnknownUserException("Permission denied."); else Kill(VMname, force);
    }

    public synchronized void Kill(String VMname, Boolean force) throws UnknownVMException, InternalErrorException {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new UnknownVMException("VM " + VMname + " not found");
        }
        VirtualMachine vm = ts.get(0);
        int VMID = vm.getOneID().intValue();
        if (force) cloud.vmAction(CloudConstants.VMAction.poweroff, VMID); else cloud.vmAction(CloudConstants.VMAction.shutdown, VMID);
        em.remove(vm);
    }

    public synchronized void Pause(String username, String VMname) throws UnknownVMException, InternalErrorException, UnknownUserException {
        if (!userOwnsVM(username, VMname)) throw new UnknownUserException("Permission denied."); else Pause(VMname);
    }

    public synchronized void Pause(String VMname) throws UnknownVMException, InternalErrorException {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new UnknownVMException("VM " + VMname + " not found");
        }
        VirtualMachine vm = ts.get(0);
        int VMID = vm.getOneID().intValue();
        cloud.vmAction(CloudConstants.VMAction.suspend, VMID);
    }

    public synchronized int Stop(String username, String VMname) throws UnknownVMException, InternalErrorException, UnknownUserException {
        if (!userOwnsVM(username, VMname)) throw new UnknownUserException("Permission denied."); else return Stop(VMname);
    }

    public synchronized int Stop(String VMname) throws UnknownVMException, InternalErrorException {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new UnknownVMException("VM " + VMname + " not found");
        }
        VirtualMachine vm = ts.get(0);
        int VMID = vm.getOneID().intValue();
        cloud.vmAction(CloudConstants.VMAction.suspend, VMID);
        return VMID;
    }

    public void WaitForFullStop(String VMname) {
        try {
            List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
            if (ts == null || ts.size() == 0) {
                throw new UnknownVMException("VM " + VMname + " not found");
            }
            VirtualMachine vm = ts.get(0);
            int VMID = vm.getOneID().intValue();
            VMInfo info = cloud.getVMInfo(VMID);
            while (info.state != CloudConstants.VMState.STOPPED) {
                System.out.println("Sleeep 30 secs");
                Thread.sleep(30000);
                try {
                    info = cloud.getVMInfo(VMID);
                } catch (Exception x) {
                    System.out.println("Getting info failed");
                }
            }
            System.out.println("Sleeep 15 secs");
            Thread.sleep(15000);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public synchronized void Resume(String username, String VMname) throws UnknownVMException, UnknownUserException, InternalErrorException {
        if (!userOwnsVM(username, VMname)) throw new UnknownUserException("Permission denied."); else Resume(VMname);
    }

    public synchronized void Resume(String VMname) throws UnknownVMException, InternalErrorException {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new UnknownVMException("VM " + VMname + " not found");
        }
        VirtualMachine vm = ts.get(0);
        int VMID = vm.getOneID().intValue();
        cloud.vmAction(CloudConstants.VMAction.resume, VMID);
    }

    public synchronized String getIP(String username, String VMname) throws InternalErrorException, UnknownVMException, UnknownUserException {
        if (!userOwnsVM(username, VMname)) throw new UnknownUserException("Permission denied.");
        return getIP(VMname);
    }

    public synchronized String getIP(String VMname) throws InternalErrorException, UnknownVMException {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new UnknownVMException("VM " + VMname + " not found");
        }
        VirtualMachine vm = ts.get(0);
        int VMID = vm.getOneID().intValue();
        VMInfo info = cloud.getVMInfo(VMID);
        return info.IPs.get(0);
    }

    public synchronized String getInfo(String username, String VMname) throws InternalErrorException, UnknownVMException, UnknownUserException {
        if (!userOwnsVM(username, VMname)) throw new UnknownUserException("Permission denied.");
        return getInfo(VMname);
    }

    public synchronized String getInfo(String VMname) throws InternalErrorException, UnknownVMException {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new UnknownVMException("VM " + VMname + " not found");
        }
        VirtualMachine vm = ts.get(0);
        int VMID = vm.getOneID().intValue();
        VMInfo info = cloud.getVMInfo(VMID);
        return info.toXML();
    }

    public synchronized String getStatus(String username, String VMname) throws UnknownVMException, InternalErrorException, UnknownUserException {
        if (!userOwnsVM(username, VMname)) throw new UnknownUserException("Permission denied.");
        return getStatus(VMname);
    }

    public synchronized String getStatus(String VMname) throws UnknownVMException, InternalErrorException {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new UnknownVMException("VM " + VMname + " not found");
        }
        VirtualMachine vm = ts.get(0);
        int VMID = vm.getOneID().intValue();
        VMInfo info = cloud.getVMInfo(VMID);
        return info.state.toString();
    }

    public synchronized List<String> getVMlist() {
        List<String> vms = em.createQuery("select vm.name from VirtualMachine vm").getResultList();
        return vms;
    }

    public synchronized List<String> getVMlist(String user) {
        List<String> vms = em.createQuery("select vm.name from VirtualMachine vm where vm.user.id='" + user + "'").getResultList();
        return vms;
    }

    public Boolean clearVMEntry(String name) {
        em.createQuery("delete vm from VirtualMachine vm where vm.name='" + name + "'").getResultList();
        return true;
    }

    public Boolean clearVMEntry(Long oneID) {
        em.createQuery("delete vm from VirtualMachine vm where vm.oneID='" + oneID + "'").getResultList();
        return true;
    }

    public Boolean clearAllVMEntries() {
        List<VirtualMachine> vms = em.createQuery("select vm from VirtualMachine vm").getResultList();
        for (VirtualMachine vm : vms) {
            em.remove(vm);
        }
        return true;
    }

    public void assignVMtoUser(String username, String VMname) throws UnknownUserException, UnknownVMException {
        User user = em.find(User.class, username);
        if (user == null) throw new UnknownUserException("User " + username + " not found.");
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new UnknownVMException("VM " + VMname + " not found");
        }
        VirtualMachine vm = ts.get(0);
        vm.setUser(user);
    }

    public void migrate(String VMname, int hostID) throws UnknownVMException, InternalErrorException {
        List<VirtualMachine> ts = em.createQuery("select vm from VirtualMachine vm where vm.name='" + VMname + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new UnknownVMException("VM " + VMname + " not found");
        }
        VirtualMachine vm = ts.get(0);
        int VMID = vm.getOneID().intValue();
        String hostname = cloud.getVMInfo(VMID).hostname;
        Host hostsource = em.find(Host.class, hostname);
        if (hostsource == null) throw new InternalErrorException("Host " + hostname + " does not exist!");
        List<Host> hosttargets = em.createQuery("select h from Host h where h.cloudid='" + hostID + "'").getResultList();
        if (ts == null || ts.size() == 0) {
            throw new InternalErrorException("Target host invalid!");
        }
        Host hosttarget = hosttargets.get(0);
        boolean livemigration = false;
        if (hostsource.getSitename().equals(hosttarget.getSitename())) {
            Site s = em.find(Site.class, hostsource.getSitename());
            if (s == null) throw new InternalErrorException("Site " + hostsource.getSitename() + " does not exist!");
            livemigration = s.isLiveMigrationCapable();
        } else {
            throw new InternalErrorException("Cross site migration not supported");
        }
        System.out.println("Live migration: " + livemigration);
        cloud.VMMigrate(VMID, hostID, livemigration);
    }

    private ONEInfo getONEInfo() throws InternalErrorException {
        ConfigurationsManager configs = new ConfigurationsManager(em);
        ONEInfo oneinfo = configs.getONEInfo();
        if (oneinfo == null) {
            oneinfo = new ONEInfo();
        }
        return oneinfo;
    }

    public String getONEDir() throws InternalErrorException {
        ONEInfo oneinfo = getONEInfo();
        return oneinfo.getONEdir();
    }
}
