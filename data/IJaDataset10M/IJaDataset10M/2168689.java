package hu.sztaki.lpds.wfs.service.angie.workflowxml;

import hu.sztaki.lpds.wfs.com.ComDataBean;
import hu.sztaki.lpds.wfs.com.JobPropertyBean;
import hu.sztaki.lpds.wfs.com.StorageWorkflowNamesBean;
import hu.sztaki.lpds.wfs.com.WorkflowConfigErrorBean;
import hu.sztaki.lpds.wfs.net.wsaxis13.WfsPortalServiceImpl;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Összeallit es visszaad egy adatbazisban letarolt workflow leiro xml-t.
 *
 * (es ha kell a beagyazott workflow-kat is osszegyujti)
 *
 * @author lpds
 */
public class WorkflowXMLBuilder {

    private WorkflowXMLService workflowXMLService;

    private WfsPortalServiceImpl wfsPortalService;

    private BuilderUtils builderUtils;

    private Hashtable grafList;

    private Hashtable abstList;

    private Hashtable realList;

    public WorkflowXMLBuilder() throws Exception {
        this.workflowXMLService = new WorkflowXMLServiceImpl();
        this.wfsPortalService = new WfsPortalServiceImpl();
        this.builderUtils = new BuilderUtils(workflowXMLService, wfsPortalService);
        grafList = new Hashtable();
        abstList = new Hashtable();
        realList = new Hashtable();
    }

    /**
     * A parameterek-ben (bean) megadott azonositok
     * alapjan összeallitja a workflow xml-t.
     *
     * @param StorageWorkflowNamesBean bean
     *         - portalid portal azonosito
     *         - userid felhasznalo azonosito
     *         - workflowid main workflow neve
     *         - downloadType letoltes tipusa
     *         - instanceType instance tipusa
     *         - exportType letoltes tipusa
     * @return String workflow xml
     */
    public String buildXMLStr(StorageWorkflowNamesBean bean) throws Exception {
        try {
            String retStr = realBuildXMLStr(bean);
            workflowXMLService.closeConnection();
            return retStr;
        } catch (Exception e) {
            workflowXMLService.closeConnection();
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * A parameterek-ben (bean) megadott azonositok
     * alapjan összeallitja a workflow xml-t.
     *
     * @param StorageWorkflowNamesBean bean
     *         - portalid portal azonosito
     *         - userid felhasznalo azonosito
     *         - workflowid main workflow neve
     *         - downloadType letoltes tipusa
     *         - instanceType instance tipusa
     *         - exportType letoltes tipusa
     * @return String workflow xml
     */
    private String realBuildXMLStr(StorageWorkflowNamesBean bean) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element eworkflowlist = doc.createElement("workflow");
        eworkflowlist.setAttribute("name", bean.getWorkflowID());
        eworkflowlist.setAttribute("download", bean.getDownloadType());
        eworkflowlist.setAttribute("export", bean.getExportType());
        String grafName = new String("");
        String abstName = new String("");
        String realName = new String("");
        String mainGrafName = new String("");
        String mainAbstName = new String("");
        String mainRealName = new String("");
        if (bean.isWork()) {
            if (bean.isGraf()) {
                grafName = bean.getWorkflowID();
                addGraf(grafName);
                builderUtils.buildGraf(eworkflowlist, doc, bean, grafName);
                mainGrafName = grafName;
            }
            if (bean.isAbst()) {
                abstName = bean.getWorkflowID();
                addAbst(abstName);
                grafName = workflowXMLService.getAWorkflowName(bean.getPortalID(), bean.getUserID(), abstName);
                addGraf(grafName);
                builderUtils.buildGraf(eworkflowlist, doc, bean, grafName);
                builderUtils.buildAbst(eworkflowlist, doc, bean, abstName, grafName);
                mainGrafName = grafName;
                mainAbstName = abstName;
            }
            if ((bean.isReal()) || (bean.isAll()) || (bean.getDownloadType().startsWith(bean.downloadType_inputs_rtID))) {
                realName = bean.getWorkflowID();
                addReal(realName);
                grafName = workflowXMLService.getAWorkflowName(bean.getPortalID(), bean.getUserID(), realName);
                addGraf(grafName);
                abstName = workflowXMLService.getAbstractWorkflowName(bean.getPortalID(), bean.getUserID(), realName);
                if (!"".equals(abstName)) {
                    addAbst(abstName);
                }
                builderUtils.buildGraf(eworkflowlist, doc, bean, grafName);
                if (!"".equals(abstName)) {
                    builderUtils.buildAbst(eworkflowlist, doc, bean, abstName, grafName);
                    mainAbstName = abstName;
                }
                builderUtils.buildReal(eworkflowlist, doc, bean, realName, grafName, abstName);
                mainGrafName = grafName;
                mainRealName = realName;
            }
        }
        if ((bean.isAppl()) || (bean.isProj())) {
            addReal(bean.getWorkflowID());
            mainRealName = bean.getWorkflowID();
            ComDataBean comDataBean = new ComDataBean();
            comDataBean.setPortalID(bean.getPortalID());
            comDataBean.setUserID(bean.getUserID());
            Hashtable tempList = new Hashtable();
            boolean working = true;
            while (working) {
                boolean newWf = false;
                tempList.clear();
                tempList.putAll(abstList);
                tempList.putAll(realList);
                Enumeration tempenum = tempList.keys();
                while (tempenum.hasMoreElements()) {
                    String wfname = (String) tempenum.nextElement();
                    String wfvalue = (String) tempList.get(wfname);
                    if (wfvalue.length() <= 1) {
                        if (("R".equals(wfvalue)) || ("T".equals(wfvalue))) {
                            grafName = workflowXMLService.getAWorkflowName(bean.getPortalID(), bean.getUserID(), wfname);
                            if (!isOldGraf(grafName)) {
                                newWf = true;
                                addGraf(grafName);
                                builderUtils.buildGraf(eworkflowlist, doc, bean, grafName);
                                if ("".equals(mainGrafName)) {
                                    if (wfname.equals(mainRealName)) {
                                        mainGrafName = grafName;
                                    }
                                }
                            }
                            comDataBean.setWorkflowID(wfname);
                            Vector jobList = wfsPortalService.getEmbedWorkflowConfigDataReal(comDataBean);
                            for (int jobPos = 0; jobPos < jobList.size(); jobPos++) {
                                JobPropertyBean tmpjob = (JobPropertyBean) jobList.get(jobPos);
                                if (tmpjob.getExe().containsKey("iworkflow")) {
                                    String newwfname = (String) tmpjob.getExe().get("iworkflow");
                                    if (!isOldReal(newwfname)) {
                                        newWf = true;
                                        addReal(newwfname);
                                    }
                                }
                            }
                            if ("R".equals(wfvalue)) {
                                abstName = workflowXMLService.getAbstractWorkflowName(bean.getPortalID(), bean.getUserID(), wfname);
                                if (!"".equals(abstName)) {
                                    if (!isOldAbst(abstName)) {
                                        newWf = true;
                                        addAbst(abstName);
                                        if ("".equals(mainAbstName)) {
                                            if (wfname.equals(mainRealName)) {
                                                mainAbstName = abstName;
                                            }
                                        }
                                    }
                                }
                                if (!isParsedReal(wfname)) {
                                    builderUtils.buildReal(eworkflowlist, doc, bean, wfname, grafName, abstName);
                                    addReal(wfname);
                                }
                            }
                            if ("T".equals(wfvalue)) {
                                if (!isParsedAbst(wfname)) {
                                    builderUtils.buildAbst(eworkflowlist, doc, bean, wfname, grafName);
                                    addAbst(wfname);
                                }
                            }
                        }
                    }
                }
                if (!newWf) {
                    working = false;
                }
            }
            if (bean.isAppl()) {
                isModuleWorkflowList(bean, realList);
            }
        }
        eworkflowlist.setAttribute("maingraf", mainGrafName);
        eworkflowlist.setAttribute("mainabst", mainAbstName);
        eworkflowlist.setAttribute("mainreal", mainRealName);
        return builderUtils.transformWorkflowListToString(doc, eworkflowlist);
    }

    private void addGraf(String name) throws Exception {
        addList(grafList, name, "G");
        addList(grafList, name, "G");
    }

    private void addAbst(String name) throws Exception {
        addList(abstList, name, "T");
    }

    private void addReal(String name) throws Exception {
        addList(realList, name, "R");
    }

    private void addList(Hashtable hash, String name, String value) throws Exception {
        if (!"".equals(name)) {
            if (!hash.containsKey(name)) {
                hash.put(name, value);
            } else {
                hash.put(name, value + "_parsed");
            }
        }
    }

    private boolean isOldGraf(String name) throws Exception {
        return isOld(grafList, name);
    }

    private boolean isOldReal(String name) throws Exception {
        return isOld(realList, name);
    }

    private boolean isOldAbst(String name) throws Exception {
        return isOld(abstList, name);
    }

    private boolean isOld(Hashtable hash, String name) throws Exception {
        if (!"".equals(name)) {
            if (hash.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isParsedGraf(String name) throws Exception {
        return isParsed(grafList, name);
    }

    private boolean isParsedReal(String name) throws Exception {
        return isParsed(realList, name);
    }

    private boolean isParsedAbst(String name) throws Exception {
        return isParsed(abstList, name);
    }

    private boolean isParsed(Hashtable hash, String name) throws Exception {
        if (!"".equals(name)) {
            if (hash.containsKey(name)) {
                if ("parsed".equals((String) hash.get(name))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void isModuleWorkflowList(StorageWorkflowNamesBean bean, Hashtable tempList) throws Exception {
        if (bean.isAppl()) {
            ComDataBean comBean = new ComDataBean();
            comBean.setPortalID(bean.getPortalID());
            comBean.setUserID(bean.getUserID());
            Enumeration tempenum = tempList.keys();
            while (tempenum.hasMoreElements()) {
                String wfname = (String) tempenum.nextElement();
                comBean.setWorkflowID(wfname);
                Vector errorVector = wfsPortalService.getWorkflowConfigDataErrorReal(comBean);
                for (int vPos = 0; vPos < errorVector.size(); vPos++) {
                    WorkflowConfigErrorBean eBean = (WorkflowConfigErrorBean) errorVector.get(vPos);
                    if (!"certtype".equals(eBean.getErrorID())) {
                        throw new Exception(bean.getWorkflowID() + " workflow is not module !");
                    }
                }
            }
        }
    }
}
