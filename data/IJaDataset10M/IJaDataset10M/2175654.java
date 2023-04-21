package hu.sztaki.lpds.pgportal.servlet.ajaxactions.zen;

import hu.sztaki.lpds.information.com.ServiceType;
import hu.sztaki.lpds.information.local.InformationBase;
import hu.sztaki.lpds.information.local.PropertyLoader;
import hu.sztaki.lpds.pgportal.service.base.GemlcaCacheService;
import hu.sztaki.lpds.pgportal.service.workflow.RealWorkflowUtils;
import hu.sztaki.lpds.pgportal.service.base.PortalCacheService;
import hu.sztaki.lpds.pgportal.servlet.ajaxactions.BASEActions;
import hu.sztaki.lpds.storage.com.UploadWorkflowBean;
import hu.sztaki.lpds.storage.inf.PortalStorageClient;
import hu.sztaki.lpds.storage.service.carmen.commons.FileUtils;
import hu.sztaki.lpds.wfs.com.*;
import hu.sztaki.lpds.wfs.inf.PortalWfsClient;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Raw saving of workflow configiguration data on the server side ( WFS) 
 *
 * @author krisztian karoczkai
 */
public class SendSavedData extends BASEActions {

    public SendSavedData() {
    }

    @Override
    public String getDispacher(Hashtable pParams) {
        return "/jsp/msg.jsp";
    }

    @Override
    public String getOutput(Hashtable pParams) {
        return null;
    }

    @Override
    public Hashtable getParameters(Hashtable pParams) {
        Enumeration<String> enmParams = pParams.keys();
        String keyParams;
        while (enmParams.hasMoreElements()) {
            keyParams = enmParams.nextElement();
            System.out.println("---" + keyParams + "=" + pParams.get(keyParams));
        }
        String user = "" + pParams.get("user");
        PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow();
        String key = "";
        String ovalue = "", nvalue = "";
        for (int i = 0; i < PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().size(); i++) {
            ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().clear();
            System.out.println("*****" + ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getName());
            HashMap jhash = ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe();
            String jobistype = null;
            if (jhash.get("iworkflow") != null) jobistype = "workflow"; else if ((jhash.get("servicetype") != null) || (jhash.get("serviceurl") != null) || (jhash.get("servicemethod") != null)) jobistype = "service"; else if ((jhash.get("cloudtype") != null) || (jhash.get("gaeurl") != null)) jobistype = "cloud"; else if (jhash.get("gridtype") != null) jobistype = "binary";
            Iterator<String> enmJ = jhash.keySet().iterator();
            String kesJ;
            while (enmJ.hasNext()) {
                kesJ = enmJ.next();
                System.out.println(kesJ + "=" + jhash.get(kesJ));
            }
            if (jobistype != null) {
                if (!"binary".equals(jobistype)) {
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("binary");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("grid");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("type");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("gridtype");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("jobmanager");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("nodenumber");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("resource");
                }
                if (!"service".equals(jobistype)) {
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("servicetype");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("serviceurl");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("servicemethod");
                }
                if (!"workflow".equals(jobistype)) {
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().remove("iworkflow");
                }
                if (!"cloud".equals(jobistype)) {
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getEditingJobData()).getExe().remove("cloudtype");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getEditingJobData()).getExe().remove("gaeurl");
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getEditingJobData()).getExe().remove("defaultgaeservice");
                }
                ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().put("jobistype", jobistype);
            }
            Iterator itl = ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getExe().keySet().iterator();
            while (itl.hasNext()) {
                key = "" + itl.next();
                if (((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().get(key) == null) {
                    ovalue = "" + ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getExe().get(key);
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "-", user, "delete.job." + key, ovalue, ""));
                } else if (!((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().get(key).equals(((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getExe().get(key))) {
                    ovalue = "" + ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getExe().get(key);
                    nvalue = "" + ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().get(key);
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "-", user, "modify.job." + key, ovalue, nvalue));
                }
            }
            itl = ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().keySet().iterator();
            while (itl.hasNext()) {
                key = "" + itl.next();
                if (((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getExe().get(key) == null) {
                    nvalue = "" + ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().get(key);
                    ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "-", user, "new.job." + key, "", nvalue));
                }
            }
            for (int k = 0; k < ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getInputs().size(); k++) {
                PortDataBean tmp2 = (PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getInputs().get(k);
                PortDataBean tmp1 = (PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getInputs().get(k);
                itl = tmp1.getData().keySet().iterator();
                while (itl.hasNext()) {
                    key = "" + itl.next();
                    if (tmp2.getData().get(key) == null) {
                        ovalue = "" + tmp1.getData().get(key);
                        ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "" + tmp2.getSeq(), user, "delete.input." + key, ovalue, ""));
                    } else if (!tmp1.getData().get(key).equals(tmp2.getData().get(key))) {
                        ovalue = "" + tmp1.getData().get(key);
                        nvalue = "" + tmp2.getData().get(key);
                        ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "" + tmp2.getSeq(), user, "modify.input." + key, ovalue, nvalue));
                    }
                }
                itl = tmp2.getData().keySet().iterator();
                while (itl.hasNext()) {
                    key = "" + itl.next();
                    if (tmp1.getData().get(key) == null) {
                        nvalue = "" + tmp2.getData().get(key);
                        ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "" + tmp2.getSeq(), user, "new.input." + key, "", nvalue));
                    }
                }
            }
            for (int k = 0; k < ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getOutputs().size(); k++) {
                PortDataBean tmp2 = (PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getOutputs().get(k);
                PortDataBean tmp1 = (PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getOutputs().get(k);
                itl = tmp1.getData().keySet().iterator();
                while (itl.hasNext()) {
                    key = "" + itl.next();
                    if (tmp2.getData().get(key) == null) {
                        ovalue = "" + tmp1.getData().get(key);
                        ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "" + tmp1.getSeq(), user, "delete.output." + key, ovalue, ""));
                    } else if (!tmp1.getData().get(key).equals(tmp2.getData().get(key))) {
                        ovalue = "" + tmp1.getData().get(key);
                        nvalue = "" + tmp2.getData().get(key);
                        ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "" + tmp2.getSeq(), user, "modify.output." + key, ovalue, nvalue));
                    }
                }
                itl = tmp2.getData().keySet().iterator();
                while (itl.hasNext()) {
                    key = "" + itl.next();
                    if (tmp1.getData().get(key) == null) {
                        nvalue = "" + tmp2.getData().get(key);
                        ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "" + tmp2.getSeq(), user, "new.output." + key, "", nvalue));
                    }
                }
            }
            HashMap ndesc = ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getDesc();
            HashMap odesc = ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(i)).getDesc();
            Iterator it0 = odesc.keySet().iterator();
            key = "";
            while (it0.hasNext()) {
                key = "" + it0.next();
                if (ndesc.get(key) != null) {
                    if (ndesc.get(key).equals("")) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "-", user, "delete.desc." + key, "" + odesc.get(key), "")); else {
                        if (!ndesc.get(key).equals(odesc.get(key))) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "-", user, "modify.desc." + key, "" + odesc.get(key), "" + ndesc.get(key)));
                    }
                }
            }
            Iterator it = ndesc.keySet().iterator();
            while (it.hasNext()) {
                key = "" + it.next();
                if (odesc.get(key) == null) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getHistory().add(new HistoryBean("", "-", user, "new.desc." + key, "", "" + ndesc.get(key)));
            }
        }
        HashMap tGelmca;
        for (int i = 0; i < PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().size(); i++) {
            tGelmca = ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe();
            String gridtype = "" + tGelmca.get("gridtype");
            if (gridtype != null) if (gridtype.equalsIgnoreCase("gemlca")) {
                String grid = (String) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().get("grid");
                String resource = (String) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().get("resource");
                for (int k = 0; k < ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getInputs().size(); k++) {
                    PortDataBean tmp = (PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getInputs().get(k);
                    String intname = (String) tmp.getData().get("intname");
                    String gparams = (String) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().get("params");
                    if (!GemlcaCacheService.getInstance().isValidPortIName(grid, resource, intname, gparams, true)) {
                        String newiname = GemlcaCacheService.getInstance().getFirstValidPortIName(grid, resource, gparams, true);
                        if (((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getInputs().size() == 1 && !newiname.equals("")) {
                            ((PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getInputs().get(k)).getData().put("intname", newiname);
                        } else ((PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getInputs().get(k)).getData().remove("intname");
                    }
                }
                for (int k = 0; k < ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getOutputs().size(); k++) {
                    PortDataBean tmp = (PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getOutputs().get(k);
                    String intname = (String) tmp.getData().get("intname");
                    String gparams = (String) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getExe().get("params");
                    if (!GemlcaCacheService.getInstance().isValidPortIName(grid, resource, intname, gparams, false)) {
                        String newiname = GemlcaCacheService.getInstance().getFirstValidPortIName(grid, resource, gparams, false);
                        if (((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getOutputs().size() == 1 && !newiname.equals("")) {
                            ((PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getOutputs().get(k)).getData().put("intname", newiname);
                        } else {
                            ((PortDataBean) ((JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(i)).getOutputs().get(k)).getData().remove("intname");
                        }
                    }
                }
            }
        }
        Hashtable deletedFiles = new Hashtable();
        String sep = FileUtils.getInstance().getSeparator();
        for (int jPos = 0; jPos < PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().size(); jPos++) {
            try {
                JobPropertyBean oldJob = (JobPropertyBean) PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().get(jPos);
                if (oldJob.getExe().containsKey("jobistype")) {
                    String oldJobType = (String) oldJob.getExe().get("jobistype");
                    JobPropertyBean newJob = (JobPropertyBean) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow().get(jPos);
                    String newJobType = (String) newJob.getExe().get("jobistype");
                    String newJobName = (String) newJob.getName();
                    if ("binary".equals(oldJobType)) {
                        if (!oldJobType.equals(newJobType)) {
                            String keyPath = newJobName + sep + FileUtils.getInstance().getDefaultBinaryName();
                            deletedFiles.put(keyPath, "");
                        }
                    }
                    for (int pPos = 0; pPos < oldJob.getInputs().size(); pPos++) {
                        try {
                            PortDataBean oldPort = (PortDataBean) oldJob.getInputs().get(pPos);
                            PortDataBean newPort = (PortDataBean) newJob.getInputs().get(pPos);
                            String oldPortType = "";
                            if (oldPort.getData().containsKey("file")) {
                                oldPortType = "file";
                            }
                            String newPortType = "";
                            if (newPort.getData().containsKey("file")) {
                                newPortType = "file";
                            }
                            if ("file".equals(oldPortType)) {
                                if (!oldPortType.equals(newPortType)) {
                                    String keyPath = newJobName + sep + "inputs" + sep + new String().valueOf(newPort.getSeq()) + sep;
                                    deletedFiles.put(keyPath, "");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String workflow = "" + pParams.get("workflow");
        String wfsID = PortalCacheService.getInstance().getUser(user).getWorkflow(workflow).getWfsID();
        Hashtable hsh = new Hashtable();
        hsh.put("url", wfsID);
        ServiceType st = InformationBase.getI().getService("wfs", "portal", hsh, new Vector());
        try {
            PortalWfsClient pc = (PortalWfsClient) Class.forName(st.getClientObject()).newInstance();
            pc.setServiceURL(wfsID);
            pc.setServiceID(st.getServiceID());
            ComDataBean tmp = new ComDataBean();
            tmp.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
            tmp.setUserID(user);
            tmp.setWorkflowID(workflow);
            tmp.setWorkflowtype(PortalCacheService.getInstance().getUser(user).getWorkflow(workflow).getWorkflowType());
            pc.setWorkflowConfigData(tmp, PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow());
            System.out.println("SEND");
            Vector<JobPropertyBean> tl = (Vector<JobPropertyBean>) PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow();
            HashMap<String, String> ttll;
            String keyll;
            Iterator<String> enmll;
            for (JobPropertyBean tll : tl) {
                ttll = tll.getDesc();
                enmll = ttll.keySet().iterator();
                while (enmll.hasNext()) {
                    keyll = enmll.next();
                    System.out.println(tll.getName() + ":" + keyll + "=" + ttll.get(keyll));
                }
            }
            PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().clear();
            PortalCacheService.getInstance().getUser(user).getOConfiguringWorkflow().addAll(PortalCacheService.getInstance().getUser(user).getConfiguringWorkflow());
            String radioDeleteYes = (String) pParams.get("radioDeleteYes");
            if ("true".equalsIgnoreCase(radioDeleteYes)) {
                DeleteOldWorkflowInstance backThread = new DeleteOldWorkflowInstance(user, workflow);
                backThread.start();
            }
        } catch (Exception e) {
            System.out.println("------------------");
            e.printStackTrace();
        }
        Hashtable res = new Hashtable();
        res.put("msg", "workflow.config.save.data");
        boolean retUploadFiles = true;
        try {
            String storageID = PortalCacheService.getInstance().getUser(user).getWorkflow(workflow).getStorageID();
            String confID = (String) pParams.get("confIDparam");
            Hashtable hshsto = new Hashtable();
            hshsto.put("url", storageID);
            ServiceType sts = InformationBase.getI().getService("storage", "portal", hshsto, new Vector());
            PortalStorageClient psc = (PortalStorageClient) Class.forName(sts.getClientObject()).newInstance();
            psc.setServiceURL(storageID);
            psc.setServiceID(sts.getServiceID());
            UploadWorkflowBean uwb = new UploadWorkflowBean();
            uwb.setPortalID(PropertyLoader.getInstance().getProperty("service.url"));
            uwb.setUserID(user);
            uwb.setWorkflowID(workflow);
            uwb.setConfID(confID);
            uwb.setDeletedFiles(deletedFiles);
            retUploadFiles = psc.uploadWorkflowFiles(uwb);
            if (!retUploadFiles) {
                res.put("msg", "workflow.config.files.notuploaded");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}

class DeleteOldWorkflowInstance extends Thread {

    private String user, workflow;

    public DeleteOldWorkflowInstance(String user, String workflow) {
        this.user = user;
        this.workflow = workflow;
    }

    @Override
    public void run() {
        setPriority(MIN_PRIORITY);
        String runtimeID;
        Hashtable rtiHash = PortalCacheService.getInstance().getUser(user).getWorkflow(workflow).getAllRuntimeInstance();
        PortalCacheService.getInstance().getUser(user).getWorkflow(workflow).getAllRuntimeInstance().clear();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!rtiHash.isEmpty()) {
            Iterator rit = rtiHash.keySet().iterator();
            while (rit.hasNext()) {
                runtimeID = (String) rit.next();
                RealWorkflowUtils.getInstance().deleteWorkflowInstance(user, workflow, runtimeID);
            }
        }
    }
}
