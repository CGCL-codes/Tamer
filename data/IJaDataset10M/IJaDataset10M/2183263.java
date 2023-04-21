package eolus.server;

import java.util.ArrayList;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.security.SecurityAssociation;
import org.uoa.eolus.EolusRemote;
import org.uoa.eolus.InternalErrorException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import eolus.client.ContactServerService;

public class ContactServerServiceImpl extends RemoteServiceServlet implements ContactServerService {

    private Boolean isAdmin = null;

    public void workAround(String str) {
    }

    public InternalErrorException dummy(InternalErrorException e) {
        return null;
    }

    private static final long serialVersionUID = 1L;

    private EolusRemote eolus = null;

    public String foo() {
        return "foo";
    }

    public String adminCreateScript(String user, String name, String content, String description) throws Exception {
        init();
        eolus.adminAddScript(user, name, content, description);
        return "ok";
    }

    public String userCreateScript(String name, String content, String description) throws Exception {
        init();
        eolus.addScript(name, content, description);
        return "ok";
    }

    public String[] adminGetClusterInfo(String site) throws Exception {
        init();
        String xml = eolus.adminGetSiteInfo(site);
        String[] result = Parser.parseClusterInfo(xml);
        return result;
    }

    public String adminSyncAllTemplates() throws Exception {
        init();
        String[] users = eolus.getUsers();
        for (String usr : users) {
            eolus.adminSyncUserTemplates(usr);
        }
        return "ok";
    }

    public String adminSyncAllScripts() throws Exception {
        init();
        System.out.println("Syncing ALL scripts");
        String[] users = eolus.getUsers();
        for (String usr : users) {
            eolus.adminSyncUserScripts(usr);
        }
        return "ok";
    }

    public String[] adminGetAllVms() throws Exception {
        init();
        ArrayList<String> vms = new ArrayList<String>();
        String[] users = eolus.getUsers();
        for (String usr : users) {
            String[] myvms = eolus.adminGetUserVMlist(usr);
            for (String vm : myvms) {
                vms.add(vm);
            }
        }
        String[] vmString = new String[vms.size()];
        int i = 0, size = vms.size();
        while (i < size) {
            vmString[i] = vms.get(i);
            i++;
        }
        return vmString;
    }

    public String[] adminGetHostsOfCluster(String cluster) throws Exception {
        init();
        return eolus.adminGetHostsofSite(cluster);
    }

    public String adminAddHostToCluster(String hostname, String cluster) throws Exception {
        init();
        eolus.adminAssignHostToSite(hostname, cluster);
        return "ok";
    }

    public String adminRemoveHostfromCluster(String hostname, String cluster) throws Exception {
        init();
        eolus.adminMoveHostToDefaultSite(hostname);
        return "ok";
    }

    public String adminNewCluster(String name, String[] hosts) throws Exception {
        init();
        eolus.adminCreateSite(name);
        for (String host : hosts) {
            eolus.adminAssignHostToSite(host, name);
        }
        return "ok";
    }

    public String[] adminGetClusters() throws Exception {
        init();
        return eolus.adminGetSites();
    }

    public String adminRemoveCluster(String name) throws Exception {
        init();
        eolus.adminDeleteSite(name);
        return "ok";
    }

    public String removeNetwork(String network) throws Exception {
        init();
        eolus.adminRemoveVNet(network);
        return "ok";
    }

    public String removeNetworkUser(String network) throws Exception {
        init();
        eolus.removeVNet(network);
        return "ok";
    }

    public String assignNetwork(String network, String user) throws Exception {
        init();
        System.out.println("user:" + user + ",network:" + network);
        eolus.adminAssignVNettoUser(user, network);
        return "ok";
    }

    public String getNetworkInfo(String network) throws Exception {
        init();
        String xml;
        if (isAdmin()) xml = eolus.adminGetVNetInfo(network); else {
            xml = eolus.getVNetInfo(network);
        }
        return xml;
    }

    public String[] adminGetAllNetworks() throws Exception {
        init();
        String[] all = null;
        all = eolus.adminGetAllVNetList();
        return all;
    }

    public String[] getStrayNetworks() throws Exception {
        init();
        String[] all = eolus.adminGetAllVNetList();
        String[] users = getUsers();
        String[] nonstray = {};
        String[] stray = {};
        for (String s : users) {
            String[] usern = getNetworksbyUser(s);
            for (String net : usern) {
                nonstray[nonstray.length] = net;
            }
        }
        for (String net1 : all) {
            boolean is_stray = true;
            for (String net2 : nonstray) {
                if (net1.equals(net2)) {
                    is_stray = false;
                    break;
                }
            }
            if (is_stray) {
                stray[stray.length] = net1;
            }
        }
        return stray;
    }

    public String[] getNetworks() throws Exception {
        init();
        String[] s = eolus.getVNetList();
        return s;
    }

    public String[] getNetworksbyUser(String user) throws Exception {
        init();
        String[] s;
        if (isAdmin()) s = eolus.adminGetVNetList(user); else if (user.equals("public")) s = eolus.getVNetPublicList(); else s = eolus.getVNetList();
        return s;
    }

    public String AdminCreateNetwork(String name, String type, String info) throws Exception {
        init();
        int typeNum = -1;
        if (type.equalsIgnoreCase("subnet")) {
            info = "192.168." + info + ".0";
            typeNum = 1;
        }
        if (type.equalsIgnoreCase("list")) typeNum = 0;
        eolus.adminCreateVNet(name, typeNum, info);
        return "ok";
    }

    public String UserCreateNetwork(String name, String type, String info) throws Exception {
        init();
        int typeNum = -1;
        if (type.equalsIgnoreCase("subnet")) {
            info = "192.168." + info + ".0";
            typeNum = 1;
        }
        if (type.equalsIgnoreCase("list")) typeNum = 0;
        eolus.createVNet(name, typeNum, info);
        return "ok";
    }

    public String[] getUsers() throws Exception {
        init();
        String[] all = eolus.getUsers();
        return all;
    }

    public void init() {
        if (eolus == null) {
            InitialContext ctx;
            try {
                ctx = new InitialContext();
                eolus = (EolusRemote) ctx.lookup("Eolus/remote-org.uoa.eolus.EolusRemote");
            } catch (NamingException e) {
                System.out.println("failed to find Eolus EJB through JNDI lookup:  " + e.getMessage());
            } catch (Exception e) {
                System.out.println("initialization failure  " + e.getMessage());
            }
        }
    }

    public String getUsername() throws Exception {
        init();
        return SecurityAssociation.getCallerPrincipal().getName();
    }

    public boolean isAdmin() throws Exception {
        init();
        if (isAdmin == null) {
            if (this.getThreadLocalRequest().isUserInRole("admin")) {
                isAdmin = true;
                System.out.println("I am admin");
                return true;
            }
            if (this.getThreadLocalRequest().isUserInRole("user")) {
                System.out.println("I am user");
                isAdmin = false;
                return false;
            }
            throw new Exception("Invalid user role");
        }
        System.out.println("I am admin?:" + isAdmin);
        return isAdmin;
    }

    public String getRemoteIp() throws Exception {
        init();
        return getThreadLocalRequest().getRemoteHost();
    }

    public String debug(String mes) throws Exception {
        init();
        System.out.println(mes);
        return null;
    }

    public String addUser(String data) throws Exception {
        System.out.print("addUser");
        init();
        eolus.addUser(data);
        return "ok";
    }

    public String[][] getUsersList() throws Exception {
        System.out.println("getUsersList");
        init();
        String[] names = eolus.getUsers();
        String[][] data = new String[names.length + 1][5];
        String[] temp2 = { "Username", "Real Name", "Date Registered", "Last IP", "Role" };
        for (int j = 0; j < 5; j++) data[0][j] = temp2[j];
        for (int i = 1; i < names.length + 1; i++) {
            String[] temp = { names[i - 1], " -- ", " -- ", " -- ", " -- " };
            for (int j = 0; j < 5; j++) data[i][j] = temp[j];
        }
        return data;
    }

    public String[] getUserInfo(String username) throws Exception {
        System.out.println("getUserInfo   username=\"" + username + "\"");
        init();
        String[] reply = { username, "", "", "", "admin" };
        return reply;
    }

    public String[] getUsernamesList() throws Exception {
        System.out.println("getUsernamesList");
        init();
        return eolus.getUsers();
    }

    public String deleteUser(String name) throws Exception {
        System.out.println("deleteUser   name=\"" + name + "\"");
        init();
        eolus.deleteUser(name);
        return "ok";
    }

    public String[][] getUserTemplates(String user, boolean sync) throws Exception {
        System.out.println("getUserTemplates   user=\"" + user + "\"   sync=\"" + sync + "\"");
        init();
        if (sync) {
            if (isAdmin()) eolus.adminSyncUserTemplates(user); else eolus.syncTemplates();
        }
        String[] templates;
        if (isAdmin()) templates = eolus.adminGetTemplates(user); else {
            if (user.equals("public")) templates = eolus.getPublicTemplates(); else templates = eolus.getTemplates();
        }
        String[][] result = new String[templates.length + 1][3];
        result[0][0] = "Name";
        result[0][1] = "Status";
        result[0][2] = "Actions";
        int i = 1;
        for (String templ : templates) {
            String status;
            if (isAdmin.equals(true)) status = eolus.adminGetTemplateStatus(user, templ); else {
                if (user.equals("public")) status = eolus.getPublicTemplateStatus(templ); else status = eolus.getTemplateStatus(templ);
            }
            String[] s = { templ, status };
            result[i] = s;
            i++;
        }
        return result;
    }

    public String getUserTemplateStatus(String user, String template, boolean sync) throws Exception {
        System.out.println("getUserTemplates   user=\"" + user + "\"   sync=\"" + sync + "\"");
        init();
        if (sync) {
            eolus.adminSyncUserTemplates(user);
        }
        return eolus.adminGetTemplateStatus(user, template);
    }

    public String deleteTemplate(String user, String template) throws Exception {
        System.out.println("deleteTemplate   user=\"" + user + "\"     template=\"" + template + "\"");
        init();
        if (isAdmin()) eolus.adminRemoveTemplate(user, template); else eolus.removeTemplate(template);
        return "ok";
    }

    public String moveTemplate(String user, String tempName, String newTempName) throws Exception {
        System.out.println("moveTemplate   user=\"" + user + "\"    tempName=\"" + tempName + "\"     newTempName=\"" + newTempName + "\"");
        if (templateNameExists(user, newTempName)) return "The selected template name already exists";
        if (isAdmin()) eolus.adminTransferTemplate(user, tempName, newTempName, true); else eolus.transferTemplate(tempName, newTempName, true);
        return "ok";
    }

    private boolean templateNameExists(String user, String template) throws Exception {
        init();
        String[] templates;
        if (isAdmin()) templates = eolus.adminGetTemplates(user); else templates = eolus.getTemplates();
        int i = 0, size = templates.length;
        while (i < size) {
            if (templates[i].equals(template)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public String copyTemplate(String user, String tempName, String newTempName) throws Exception {
        System.out.println("copyTemplate   user=\"" + user + "\"    tempName=\"" + tempName + "\"     newTempName=\"" + newTempName + "\"");
        init();
        try {
            if (templateNameExists(user, newTempName)) return "The selected template name already exists";
        } catch (Exception e) {
            throw e;
        }
        if (isAdmin()) eolus.adminTransferTemplate(user, tempName, newTempName, false); else eolus.transferTemplate(tempName, newTempName, false);
        return "ok";
    }

    public String makeTemplatePublic(String user, String tempName) throws Exception {
        System.out.println("makeTemplatePublic   user=\"" + user + "\"     tempName=\"" + tempName + "\"");
        init();
        Vector<String> pub_temps = new Vector<String>();
        for (String name : eolus.getPublicTemplates()) pub_temps.add(name);
        String newName = tempName;
        if (pub_temps.contains(tempName)) {
            int i = 1;
            while (pub_temps.contains(tempName + "_" + i)) i++;
            newName = tempName + "_" + i;
        }
        eolus.makeTemplatePublic(tempName, newName);
        return "ok";
    }

    public String[] getVMinfo(String vm) throws Exception {
        init();
        String xml;
        if (isAdmin.equals(true)) xml = eolus.adminGetVMInfo(vm); else xml = eolus.getVMInfo(vm);
        String[] s = Parser.parseVMInfo(xml);
        return s;
    }

    public String[] getUserVmList(String user) throws Exception {
        String[] vms;
        init();
        if (isAdmin()) vms = eolus.adminGetUserVMlist(user); else vms = eolus.getVMlist();
        return vms;
    }

    public String[][] getUserVms(String user) throws Exception {
        System.out.println("getUserVms   user=\"" + user + "\"");
        init();
        String[] vms;
        if (isAdmin()) vms = eolus.adminGetUserVMlist(user); else vms = eolus.getVMlist();
        String[] headers = { "Name", "Status", "Memory", "CPU", "IP", "Hostname", "Actions" };
        String[][] s = new String[vms.length + 1][headers.length];
        s[0] = headers;
        int i = 0, size = vms.length;
        while (i < size) {
            s[i + 1] = getVMinfo(vms[i]);
            i++;
        }
        return s;
    }

    public String actionOnVm(String user, String vm, String action) throws Exception {
        System.out.println("actionOnVm   user=\"" + user + "\"    vm=\"" + vm + "\"     action=\"" + action + "\"");
        init();
        if ("Kill".equalsIgnoreCase(action)) {
            if (isAdmin.equals(true)) eolus.adminShutdownVM(vm, false); else eolus.shutdownVM(vm, false);
            return "ok";
        } else if ("Poweroff".equalsIgnoreCase(action)) {
            if (isAdmin.equals(true)) eolus.adminShutdownVM(vm, true); else eolus.shutdownVM(vm, true);
            return "ok";
        } else if ("Resume".equalsIgnoreCase(action)) {
            if (isAdmin.equals(true)) eolus.adminResumeVM(vm); else eolus.resumeVM(vm);
            return "ok";
        } else if ("Pause".equalsIgnoreCase(action)) {
            if (isAdmin.equals(true)) eolus.adminSuspendVM(vm); else eolus.suspendVM(vm);
            return "ok";
        } else return "Action not recognised";
    }

    public String createVm(String user, String template, String VmName, String net, int mem, int cores) throws Exception {
        System.out.println("createVm   user=\"" + user + "\"    template=\"" + template + "\"    VmName=\"" + VmName + "\"" + "    network=\"" + net + "\"    mem=\"" + mem + "\"    cores=\"" + cores + "\"");
        init();
        String[] nets = { net };
        if (isAdmin.equals(true)) eolus.adminCreateVM(user, template, VmName, null, cores, mem, nets); else eolus.createVM(template, VmName, cores, mem, nets);
        return "ok";
    }

    public String createTemplFromVm(String user, String Vm) throws Exception {
        System.out.println("createTemplFromVm   user=\"" + user + "\"    Vm=\"" + Vm + "\"");
        init();
        String[] templates;
        if (isAdmin.equals(true)) templates = eolus.adminGetTemplates(user); else templates = eolus.getTemplates();
        Vector<String> temps = new Vector<String>();
        for (String name : templates) temps.add(name);
        String temp_name = Vm;
        if (temps.contains(Vm)) {
            int i = 1;
            while (temps.contains(Vm + "_" + i)) i++;
            temp_name = Vm + "_" + i;
        }
        if (isAdmin.equals(true)) {
            eolus.adminVMtoTemplate(user, Vm, temp_name);
            return "ok";
        } else {
            eolus.VMtoTemplate(Vm, temp_name);
            return "ok";
        }
    }

    public String[][] getUserScripts(String user, boolean sync) throws Exception {
        System.out.println("getUserScripts   user=\"" + user + "\"   sync=\"" + sync + "\"");
        init();
        if (sync) {
            if (isAdmin()) eolus.adminSyncUserScripts(user); else eolus.syncScripts();
        }
        String[] scripts;
        if (isAdmin.equals(true)) scripts = eolus.adminGetScriptList(user); else {
            scripts = eolus.getScriptList();
        }
        String[][] res = new String[scripts.length + 1][3];
        res[0][0] = "Name";
        res[0][1] = "Description";
        res[0][2] = "Actions";
        int i = 1;
        for (String script : scripts) {
            try {
                String[] s = { script, getScriptDescription(user, script) };
                res[i] = s;
                i++;
            } catch (Exception e) {
                throw e;
            }
        }
        return res;
    }

    public String getScriptDescription(String user, String script) throws Exception {
        System.out.println("getScriptDescription   user=\"" + user + "\"    script=\"" + script + "\"");
        init();
        if (isAdmin()) return eolus.adminGetDescription(user, script); else return eolus.getDescription(script);
    }

    public String userRemoveScript(String script) throws Exception {
        eolus.removeScript(script);
        return "ok";
    }

    public String adminRemoveScript(String usr, String script) throws Exception {
        eolus.adminRemoveScript(usr, script);
        return "ok";
    }

    public String runScript(String user, String script, String vm) throws Exception {
        System.out.println("runScript   user=\"" + user + "\"    script=\"" + script + "\"     vm=\"" + vm + "\"");
        init();
        if (isAdmin()) eolus.adminApplyScript(user, script, vm); else eolus.applyScript(script, vm);
        System.out.println("runScript ok");
        return "ok";
    }

    public String[] runCommand(String user, String vm, String command) throws Exception {
        System.out.println("runCommand    user=\"" + user + "\"    vm=\"" + vm + "\"     command=\"" + command + "\"");
        init();
        if (isAdmin()) return eolus.adminExecCMD(command, vm); else return eolus.execCMD(command, vm);
    }

    public boolean logout() throws Exception {
        this.getThreadLocalRequest().getSession().invalidate();
        eolus = null;
        isAdmin = null;
        System.out.println("Session invalidated");
        return true;
    }

    public String syncTemplates(String user) throws Exception {
        System.out.println("syncTemplates    user=\"" + user + "\"");
        init();
        if (isAdmin()) eolus.adminSyncUserTemplates(user); else eolus.syncTemplates();
        return "ok";
    }

    public String syncScripts(String user) throws Exception {
        System.out.println("syncScripts   user=\"" + user + "\"");
        init();
        if (isAdmin()) eolus.adminSyncUserScripts(user); else eolus.syncScripts();
        return "ok";
    }

    public String assignVm(String user, String vmName) throws Exception {
        System.out.println("assignVm   user=\"" + user + "\"    vmName=\"" + vmName + "\"");
        init();
        eolus.adminAssignVMtoUser(user, vmName);
        return "ok";
    }

    public String createHost(String hostName) throws Exception {
        System.out.println("createHost   hostName=\"" + hostName + "\"");
        init();
        eolus.createHost(hostName);
        return "ok";
    }

    public String deleteHost(String hostID) throws Exception {
        System.out.println("deleteHost   hostID=\"" + hostID + "\"");
        init();
        eolus.deleteHost(hostID);
        return "ok";
    }

    public String[] getHosts() throws Exception {
        String[] hosts = null;
        hosts = eolus.getHostList();
        return hosts;
    }

    public String[][] getHostsList() throws Exception {
        System.out.println("getHostsList");
        init();
        String[] hosts = null;
        hosts = eolus.getHostList();
        String[] headers = { "Name", "Running VMs", "Total CPU", "Free CPU", "Total Memory", "Free Memory", "Status", "Actions" };
        String[][] s = new String[hosts.length + 1][headers.length];
        s[0] = headers;
        int i = 0, size = hosts.length;
        while (i < size) {
            try {
                s[i + 1] = this.getHostInfo(hosts[i]);
                i++;
            } catch (Exception e) {
                throw e;
            }
        }
        return s;
    }

    public String[] getHostInfo(String host) throws Exception {
        String xml = eolus.getHostInfo(host);
        String[] s = Parser.parseHostInfo(xml);
        System.out.println("HOST Naem:" + s[0]);
        return s;
    }

    public String enableHost(String hostID, boolean enable) throws Exception {
        System.out.println("enableHost   hostID=\"" + hostID + "\"   enable=\"" + enable + "\"");
        init();
        eolus.enableHost(hostID, enable);
        return "ok";
    }

    public String[] getHostNames() throws Exception {
        System.out.println("getHostsList");
        init();
        return eolus.getHostList();
    }

    public String migrateVM(String vm, String migrate_to) throws Exception {
        System.out.println("migrateVM   VM=\"" + vm + "\"");
        init();
        eolus.migrateVM(vm, migrate_to);
        return "ok";
    }
}
