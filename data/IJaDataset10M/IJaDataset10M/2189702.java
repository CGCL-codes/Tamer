package hu.sztaki.lpds.USGIME.services;

import hu.sztaki.lpds.USGIME.beans.PortalTestbean;
import hu.sztaki.lpds.USGIME.beans.USGIMEAllresourcebean;
import hu.sztaki.lpds.pgportal.portlets.credential.SZGCredentialBean;
import hu.sztaki.lpds.pgportal.services.credential.SZGCredential;
import hu.sztaki.lpds.pgportal.services.credential.SZGCredentialManager;
import hu.sztaki.lpds.pgportal.services.credential.SZGStoreKey;
import hu.sztaki.lpds.pgportal.services.pgrade.PersistenceManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import org.gridlab.gridsphere.portlet.User;
import org.gridlab.gridsphere.portlet.service.PortletServiceUnavailableException;
import org.gridlab.gridsphere.portlet.service.spi.PortletServiceConfig;
import org.gridlab.gridsphere.portlet.service.spi.PortletServiceProvider;
import hu.sztaki.lpds.pgportal.services.pgrade.SZGWorkflowList;
import hu.sztaki.lpds.pgportal.services.utils.PropertyLoader;
import hu.sztaki.lpds.pgportal.services.utils.MiscUtils;
import hu.sztaki.lpds.USGIME.services.USGIME_ResourceManager;
import hu.sztaki.lpds.USGIME.exceptions.USGIME_StartException;

/**
 *
 * @author balasko
 */
public class Admin_TestServiceImpl implements PortletServiceProvider, Admin_TestService {

    private HashMap<String, HashMap<String, PortalTestbean>> tests;

    private static String PortalTest = "Portal";

    private static USGIME_ResourceManager urm;

    private static Admin_TestServiceImpl portaltestservice;

    private static String newline = System.getProperty("line.separator");

    private SZGCredentialManager cm = null;

    private SZGCredentialBean cb = null;

    public Admin_TestServiceImpl() {
        try {
            urm = new USGIME_ResourceManager();
            cm = SZGCredentialManager.getInstance();
        } catch (Exception e) {
            System.out.println("Admin_TestServiceImpl - PropertyLoader failed!");
        }
        tests = new HashMap<String, HashMap<String, PortalTestbean>>();
    }

    /**
	 * Get the instance. Creates an instance of the USGIMEOutputManager class if
	 * none exists. If exists, returns the instance.
	 * @return USGIMEOutputManager object
	 */
    public static synchronized Admin_TestServiceImpl getInstance() throws Exception {
        if (portaltestservice == null) portaltestservice = new Admin_TestServiceImpl();
        return portaltestservice;
    }

    /**
	 * Disable cloning.
	 */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public HashMap<String, PortalTestbean> getTests(String userId) {
        return tests.get(userId);
    }

    private boolean addTest(String userId, PortalTestbean bean) {
        boolean isExists = false;
        boolean success = false;
        if (tests.containsKey(userId)) {
            HashMap<String, PortalTestbean> storedtests = tests.get(userId);
            if (storedtests.containsKey(new String(bean.getWorkflowname()))) {
                isExists = true;
            }
            if (!isExists) {
                storedtests.put(bean.getWorkflowname(), bean);
                tests.put(userId, storedtests);
                success = true;
            }
        } else {
            HashMap<String, PortalTestbean> newmap = new HashMap<String, PortalTestbean>();
            newmap.put(bean.getWorkflowname(), bean);
            tests.put(userId, newmap);
            success = true;
        }
        return success;
    }

    public boolean is_in_progress(String userId, String wfname) {
        if (tests.get(userId) != null) {
            if (tests.get(userId).get(wfname) != null) {
                if (tests.get(userId).get(wfname).getTask() == null) {
                    return false;
                } else {
                    return tests.get(userId).get(wfname).getTask().isIn_progress();
                }
            } else return false;
        } else return false;
    }

    public void startTest(String userId, PortalTestbean bean) throws USGIME_StartException, USGIMEException {
        String gridname = SZGWorkflowList.getInstance(userId).getGridName(bean.getWorkflowname());
        this.cm = SZGCredentialManager.getInstance();
        this.cb = new SZGCredentialBean(userId);
        if (this.cm != null) {
            this.cb.setCredentials(this.cm.getCredentials(userId));
            try {
                this.cb.setUseThis(this.cm.getUseThis(userId));
            } catch (Exception ex) {
            }
        }
        try {
            loadUsrCert(userId);
        } catch (Exception e) {
        }
        if (!this.userCertCheckMG(userId, gridname)) {
            USGIMEException e = new USGIMEException("No valid certificate for " + gridname);
            throw e;
        } else {
            addTest(userId, bean);
            if (!is_in_progress(userId, bean.getWorkflowname())) {
                PortalTest_Init(userId, bean.getWorkflowname());
                String exec = this.getExecutable(userId, bean.getWorkflowname());
                String parameters = this.getParameters(userId, bean.getWorkflowname());
                TestTimerTask newtimer = new TestTimerTask(userId, bean.getWorkflowname(), exec, parameters, bean.getCycl_time());
                tests.get(userId).get(bean.getWorkflowname()).setTask(newtimer);
                tests.get(userId).get(bean.getWorkflowname()).setTimer(new Timer());
                tests.get(userId).get(bean.getWorkflowname()).getTimer().schedule(newtimer, 0);
                tests.get(userId).get(bean.getWorkflowname()).setStopped(false);
            } else {
                throw new USGIME_StartException("Operation is still in Progress...");
            }
        }
    }

    public void stopTest(String userId, PortalTestbean bean) {
        if (tests.containsKey(userId)) {
            HashMap<String, PortalTestbean> storedtests = tests.get(userId);
            if (storedtests.containsKey((bean.getWorkflowname()))) {
                storedtests.get(bean.getWorkflowname()).getTask().setStop(true);
                storedtests.get(bean.getWorkflowname()).getTimer().cancel();
                storedtests.get(bean.getWorkflowname()).setStopped(true);
            }
        }
    }

    public PortalTestbean getTest(String userId, String workflowname) {
        if (tests.containsKey(userId)) {
            if (tests.get(userId).containsKey(workflowname)) {
                return tests.get(userId).get(workflowname);
            }
        }
        return new PortalTestbean("", "", 0);
    }

    public String getExecutable(String userId, String wfname) {
        return PropertyLoader.getPrefixDir() + "tmp/Portaltests/" + userId + "/" + wfname + "/portaltest.sh";
    }

    public String getParameters(String userId, String wfname) {
        return userId + " Portal " + wfname + " " + getGridName(userId, wfname);
    }

    private String getGridName(String userId, String workflowname) {
        PersistenceManager.initPM(userId);
        SZGWorkflowList wfl = SZGWorkflowList.getInstance(userId);
        return wfl.getGridName(workflowname);
    }

    public boolean isRunning(String userId, String workflowname) {
        if (tests.containsKey(userId)) {
            if (tests.get(userId).containsKey(workflowname)) {
                return !(tests.get(userId).get(workflowname).isStopped());
            } else {
                System.out.println("ADMINTEST :User Workflowname - false");
                return false;
            }
        } else {
            System.out.println("ADMINTEST :User Containskey - false");
            return false;
        }
    }

    public void PortalTest_Init(String userId, String workflowname) {
        File todir = new File(PropertyLoader.getPrefixDir() + "/tmp/Portaltests/");
        boolean success = false;
        if (!todir.exists()) {
            success = todir.mkdir();
        }
        File todir_user = new File(PropertyLoader.getPrefixDir() + "/tmp/Portaltests/" + userId);
        if (!todir_user.exists()) {
            success = todir_user.mkdir();
        }
        File todir_user_workflow = new File(PropertyLoader.getPrefixDir() + "/tmp/Portaltests/" + userId + "/" + workflowname);
        if (!todir_user_workflow.exists()) {
            success = todir_user_workflow.mkdir();
        }
        String from = PropertyLoader.getPrefixDir() + "/users/" + userId + "/" + workflowname + "_files/" + PortalTest + "/portaltest.sh";
        String to = PropertyLoader.getPrefixDir() + "/tmp/Portaltests/" + userId + "/" + workflowname + "/portaltest.sh";
        try {
            MiscUtils.copy(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> grids = new ArrayList<String>();
        grids.add(getGridName(userId, workflowname));
        USGIMEAllresourcebean resources = urm.get_All_Resources_script(userId, grids);
        String storagelist = "";
        for (String s : resources.getVoresources().get(getGridName(userId, workflowname)).getStorages()) {
            storagelist = storagelist + s + newline;
        }
        MiscUtils.deleteFileRecursively(new File(PropertyLoader.getPrefixDir() + "/tmp/Portaltests/" + userId + "/" + workflowname + "/inputses.txt"));
        MiscUtils.writeStrToFile(PropertyLoader.getPrefixDir() + "/tmp/Portaltests/" + userId + "/" + workflowname + "/inputses.txt", storagelist);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(PortletServiceConfig arg0) throws PortletServiceUnavailableException {
    }

    private boolean userCertCheckMG(String username, String gridName) {
        SZGCredential c, c2, c3;
        SZGCredentialManager cm = SZGCredentialManager.getInstance();
        c = cm.getCredentialForGrid(username, gridName);
        c2 = cm.getCredentialForGrid(username, gridName + "_LCG_2_BROKER");
        c3 = cm.getCredentialForGrid(username, gridName + "_GLITE_BROKER");
        if (c == null) {
            if (c2 == null) {
                if (c3 == null) return false;
                try {
                    return c3.getTimeLeftInSeconds() > 0L;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
            try {
                return c2.getTimeLeftInSeconds() > 0L;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        try {
            return c.getTimeLeftInSeconds() > 0L;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String loadUsersDirPath() throws Exception {
        return PropertyLoader.getPrefixDir() + "/users/";
    }

    private String loadUsrCert(String usr) throws Exception {
        String usrDir = this.loadUsersDirPath() + usr + "/";
        File uDir = new File(usrDir);
        if (!uDir.exists()) {
            if (!uDir.mkdirs()) {
                return null;
            }
        }
        FileReader fin = new FileReader(usrDir + usr);
        BufferedReader in = new BufferedReader(fin);
        SZGCredential[] creds = this.cb.getCredentials();
        if (creds == null) {
            try {
                String sor = new String(" ");
                while ((sor = in.readLine()) != null) {
                    int indx = 0;
                    int indv = sor.indexOf(";", indx);
                    String Id = new String(sor.substring(indx, indv));
                    indx = indv + 1;
                    indv = sor.indexOf(";", indx);
                    String DownloadedFrom = new String(sor.substring(indx, indv));
                    indx = indv + 1;
                    indv = sor.indexOf(";", indx);
                    String TimeLeft = new String(sor.substring(indx, indv));
                    indx = indv + 1;
                    indv = sor.indexOf(";#", indx);
                    String Description = new String(sor.substring(indx, indv));
                    indx = indv + 2;
                    indv = sor.indexOf(";", indx);
                    String gsVal = new String(sor.substring(indx, indv));
                    SZGStoreKey key = new SZGStoreKey(usr, Id);
                    String cfp;
                    if (0 == gsVal.compareTo(" ")) {
                        cfp = new String(usrDir + "x509up");
                        InputStream crinstr = new FileInputStream(cfp);
                        this.cm.loadFromFile(crinstr, DownloadedFrom, Integer.parseInt(TimeLeft), key, Description);
                    } else {
                        cfp = new String(usrDir + "x509up." + gsVal.trim());
                        InputStream crinstr = new FileInputStream(cfp);
                        this.cm.loadFromFile(crinstr, DownloadedFrom, Integer.parseInt(TimeLeft), key, Description);
                        while (indv != -1) {
                            this.cm.setCredentialForGrid(usr, Id, gsVal.trim());
                            indx = indv + 1;
                            indv = sor.indexOf(";", indx);
                            if (indv != -1) {
                                gsVal = new String(sor.substring(indx, indv));
                            }
                        }
                    }
                }
                in.close();
            } catch (Exception e) {
                System.out.println("loadUsrCert ERROR:" + e);
            }
        } else {
            System.out.println("loadUsrCert: - mar betoltve");
        }
        return " ";
    }
}
