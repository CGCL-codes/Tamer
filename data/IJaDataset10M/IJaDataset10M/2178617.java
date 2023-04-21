package guse;

import hu.sztaki.lpds.information.com.ServiceType;
import hu.sztaki.lpds.information.local.InformationBase;
import hu.sztaki.lpds.information.local.PropertyLoader;
import hu.sztaki.lpds.pgportal.service.base.PortalCacheService;
import hu.sztaki.lpds.pgportal.service.base.data.WorkflowData;
import hu.sztaki.lpds.repository.inf.PortalRepositoryClient;
import hu.sztaki.lpds.storage.inf.PortalStorageClient;
import hu.sztaki.lpds.wfi.com.WorkflowRuntimeBean;
import hu.sztaki.lpds.wfi.inf.PortalWfiClient;
import hu.sztaki.lpds.wfs.com.ComDataBean;
import hu.sztaki.lpds.wfs.com.JobPropertyBean;
import hu.sztaki.lpds.wfs.com.RepositoryWorkflowBean;
import hu.sztaki.lpds.wfs.inf.PortalWfsClient;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author krisztian
 */
public class Util {

    public static String STORAGE = PropertyLoader.getInstance().getProperty("storage.url");

    public static String WFS = PropertyLoader.getInstance().getProperty("wfs.url");

    public static String STORAGE_SEC = PropertyLoader.getInstance().getProperty("storagesec.url");

    public static Vector<RepositoryWorkflowBean> getRepoApps() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ServiceType st = InformationBase.getI().getService("wfs", "portal", new Hashtable(), new Vector());
        PortalWfsClient cl = (PortalWfsClient) Class.forName(st.getClientObject()).newInstance();
        cl.setServiceURL(st.getServiceUrl());
        cl.setServiceID(st.getServiceID());
        RepositoryWorkflowBean tmp = new RepositoryWorkflowBean();
        tmp.setId(new Long(0));
        tmp.setWorkflowType("appl");
        return cl.getRepositoryItems(tmp);
    }

    /**
 * Alkalmazas importalasa
 * @param pUID felhasznalao azonositoja
 * @param pApp Alkalmazas leiro
 */
    public static void importApp(String pUID, RepositoryWorkflowBean pApp) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        pApp.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
        pApp.setStorageID(STORAGE);
        pApp.setWfsID(WFS);
        pApp.setUserID(pUID);
        ServiceType st = InformationBase.getI().getService("repository", "portal", new Hashtable(), new Vector());
        PortalRepositoryClient cli = (PortalRepositoryClient) Class.forName(st.getClientObject()).newInstance();
        cli.setServiceURL(st.getServiceUrl());
        cli.setServiceID(st.getServiceID());
        cli.importWorkflow(pApp);
    }

    public static Vector<ComDataBean> getWorkflows(String pUSER) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ServiceType st = InformationBase.getI().getService("wfs", "portal", new Hashtable(), new Vector());
        PortalWfsClient pc = (PortalWfsClient) Class.forName(st.getClientObject()).newInstance();
        pc.setServiceURL(st.getServiceUrl());
        pc.setServiceID(st.getServiceID());
        ComDataBean cmd = new ComDataBean();
        cmd.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
        cmd.setUserID(pUSER);
        return pc.getRealWorkflows(cmd);
    }

    /**
 * Workflow konfiguracio lekerdezese
 * @param pUID felhasznalo
 * @param pWfID workflow neve
 * @return WF konfiguracioja(jobonkent listaban)
 * @throws java.lang.ClassNotFoundException nicns kommunikacios osztaly
 * @throws java.lang.InstantiationException nem hoszjato letre com.class.
 * @throws java.lang.IllegalAccessException nincs jogosultsag a com.class-hoz
 */
    public static Vector<JobPropertyBean> getWorkflowConfig(String pUID, String pWfID) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Hashtable hsh = new Hashtable();
        hsh.put("url", WFS);
        ServiceType st = InformationBase.getI().getService("wfs", "portal", hsh, new Vector());
        PortalWfsClient pc = (PortalWfsClient) Class.forName(st.getClientObject()).newInstance();
        pc.setServiceURL(st.getServiceUrl());
        pc.setServiceID(st.getServiceID());
        ComDataBean cmd = new ComDataBean();
        cmd.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
        cmd.setUserID(pUID);
        cmd.setWorkflowID(pWfID);
        return pc.getWorkflowConfigData(cmd);
    }

    /**
 * Workflow konfigracio mentese
 * @param pUID konfiguralo felhasznalo
 * @param pWF konfiguralt wf
 * @param pJobs konfiguracio jobonkent
 * @throws java.lang.ClassNotFoundException nicns kommunikacios osztaly
 * @throws java.lang.InstantiationException nem hoszjato letre com.class.
 * @throws java.lang.IllegalAccessException nincs jogosultsag a com.class-hoz
 */
    public static void saveConfigData(String pUID, String pWF, Vector pJobs) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Hashtable hsh = new Hashtable();
        hsh.put("url", WFS);
        ServiceType st = InformationBase.getI().getService("wfs", "portal", hsh, new Vector());
        PortalWfsClient pc = (PortalWfsClient) Class.forName(st.getClientObject()).newInstance();
        pc.setServiceURL(st.getServiceUrl());
        pc.setServiceID(st.getServiceID());
        ComDataBean cmd = new ComDataBean();
        cmd.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
        cmd.setUserID(pUID);
        cmd.setWorkflowID(pWF);
        pc.setWorkflowConfigData(cmd, pJobs);
    }

    /**
 * Workflow peldany submit
 * @param pUID user
 * @param pWF wf
 * @throws java.lang.ClassNotFoundException nicns kommunikacios osztaly
 * @throws java.lang.InstantiationException nem hoszjato letre com.class.
 * @throws java.lang.IllegalAccessException nincs jogosultsag a com.class-h
 */
    public static void submit(String pUID, String pWF) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ServiceType st = InformationBase.getI().getService("wfi", "portal", new Hashtable(), new Vector());
        PortalWfiClient pc = (PortalWfiClient) Class.forName(st.getClientObject()).newInstance();
        pc.setServiceURL(st.getServiceUrl());
        pc.setServiceID(st.getServiceID());
        pc.submitWorkflow(new WorkflowRuntimeBean(PropertyLoader.getInstance().getProperty("service.url"), STORAGE, WFS, pUID, pWF, "INSTACE", "zen"));
    }

    /**
 * Workflow peldany submit
 * @param pUID user
 * @param pWF wf
 * @param  pWFRID wf runtimeID
 * @throws java.lang.ClassNotFoundException nicns kommunikacios osztaly
 * @throws java.lang.InstantiationException nem hoszjato letre com.class.
 * @throws java.lang.IllegalAccessException nincs jogosultsag a com.class-h
 */
    public static void rescue(String pUID, String pWF, String pWFRID) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ServiceType st = InformationBase.getI().getService("wfi", "portal", new Hashtable(), new Vector());
        PortalWfiClient pc = (PortalWfiClient) Class.forName(st.getClientObject()).newInstance();
        pc.setServiceURL(st.getServiceUrl());
        pc.setServiceID(st.getServiceID());
        pc.rescueWorkflow(new WorkflowRuntimeBean(PropertyLoader.getInstance().getProperty("service.url"), STORAGE, WFS, pUID, pWF, "INSTANCE", pWFRID, "zen", new Vector()));
    }

    public static void delete(String pUser, String pWorkflow) {
        WorkflowData wData = PortalCacheService.getInstance().getUser(pUser).getWorkflow(pWorkflow);
        Hashtable hsh = new Hashtable();
        hsh.put("url", wData.getWfsID());
        try {
            PortalWfsClient pc = (PortalWfsClient) InformationBase.getI().getServiceClient("wfs", "portal", hsh);
            ComDataBean tmp = new ComDataBean();
            tmp.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
            tmp.setUserID(pUser);
            tmp.setWorkflowID(wData.getWorkflowID());
            pc.deleteWorkflow(tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            hsh = new Hashtable();
            hsh.put("url", wData.getStorageID());
            PortalStorageClient ps = (PortalStorageClient) InformationBase.getI().getServiceClient("storage", "portal", hsh);
            ComDataBean tmp = new ComDataBean();
            tmp.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
            tmp.setUserID(pUser);
            tmp.setWorkflowID(wData.getWorkflowID());
            ps.deleteWorkflow(tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PortalCacheService.getInstance().getUser(pUser).deleteWorkflow(wData.getWorkflowID());
    }
}
