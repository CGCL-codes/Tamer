package org.opencms.setup;

import org.opencms.configuration.CmsConfigurationManager;
import org.opencms.configuration.CmsModuleConfiguration;
import org.opencms.file.CmsProject;
import org.opencms.file.CmsResource;
import org.opencms.file.types.I_CmsResourceType;
import org.opencms.i18n.CmsEncoder;
import org.opencms.importexport.CmsImportParameters;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.CmsSystemInfo;
import org.opencms.main.OpenCms;
import org.opencms.module.CmsModule;
import org.opencms.module.CmsModuleVersion;
import org.opencms.module.CmsModuleXmlHandler;
import org.opencms.relations.I_CmsLinkParseable;
import org.opencms.report.CmsShellReport;
import org.opencms.report.I_CmsReport;
import org.opencms.setup.update6to7.CmsUpdateDBThread;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.threads.CmsXmlContentRepairSettings;
import org.opencms.workplace.threads.CmsXmlContentRepairThread;
import org.opencms.xml.CmsXmlException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.jsp.JspWriter;
import org.apache.commons.logging.Log;

/**
 * A java bean as a controller for the OpenCms update wizard.<p>
 * 
 * @author  Michael Moossen
 * 
 * @version $Revision: 1.6 $ 
 * 
 * @since 6.0.0 
 */
public class CmsUpdateBean extends CmsSetupBean {

    /** name of the update folder. */
    public static final String FOLDER_UPDATE = "update" + File.separatorChar;

    /** replace pattern constant for the cms script. */
    private static final String C_ADMIN_GROUP = "@ADMIN_GROUP@";

    /** replace pattern constant for the cms script. */
    private static final String C_ADMIN_PWD = "@ADMIN_PWD@";

    /** replace pattern constant for the cms script. */
    private static final String C_ADMIN_USER = "@ADMIN_USER@";

    /** replace pattern constant for the cms script. */
    private static final String C_UPDATE_PROJECT = "@UPDATE_PROJECT@";

    /** replace pattern constant for the cms script. */
    private static final String C_UPDATE_SITE = "@UPDATE_SITE@";

    /** The static log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsUpdateBean.class);

    /** Static flag to indicate if all modules should be updated regardless of their version number. */
    private static final boolean UPDATE_ALL_MODULES = false;

    /** The new logging offset in the database update thread. */
    protected int m_newLoggingDBOffset;

    /** The old logging offset in the database update thread. */
    protected int m_oldLoggingDBOffset;

    /** The used admin user name. */
    private String m_adminGroup = "_tmpUpdateGroup" + (System.currentTimeMillis() % 1000);

    /** the admin user password. */
    private String m_adminPwd = "admin";

    /** The used admin user name. */
    private String m_adminUser = "Admin";

    /** The update database thread. */
    private CmsUpdateDBThread m_dbUpdateThread;

    /** Parameter for keeping the history. */
    private boolean m_keepHistory;

    /** List of module to be updated. */
    private List m_modulesToUpdate;

    /** Signalizes if a DB update is needed. */
    private boolean m_needDbUpdate = false;

    /** the update project. */
    private String m_updateProject = "_tmpUpdateProject" + (System.currentTimeMillis() % 1000);

    /** the site for update. */
    private String m_updateSite = CmsResource.VFS_FOLDER_SITES + "/default/";

    /** Cache for the up-to-date module names. */
    private List m_uptodateModules;

    /** The workplace import thread. */
    private CmsUpdateThread m_workplaceUpdateThread;

    /** 
     * Default constructor.<p>
     */
    public CmsUpdateBean() {
        super();
        m_modulesFolder = FOLDER_UPDATE + CmsSystemInfo.FOLDER_MODULES;
        m_logFile = CmsSystemInfo.FOLDER_WEBINF + CmsLog.FOLDER_LOGS + "update.log";
    }

    /**
     * Returns html code to display an error.<p> 
     * 
     * @param pathPrefix to adjust the path
     * 
     * @return html code
     */
    public String displayError(String pathPrefix) {
        if (pathPrefix == null) {
            pathPrefix = "";
        }
        StringBuffer html = new StringBuffer(512);
        html.append("<table border='0' cellpadding='5' cellspacing='0' style='width: 100%; height: 100%;'>");
        html.append("\t<tr>");
        html.append("\t\t<td style='vertical-align: middle; height: 100%;'>");
        html.append(getHtmlPart("C_BLOCK_START", "Error"));
        html.append("\t\t\t<table border='0' cellpadding='0' cellspacing='0' style='width: 100%;'>");
        html.append("\t\t\t\t<tr>");
        html.append("\t\t\t\t\t<td><img src='").append(pathPrefix).append("resources/error.png' border='0'></td>");
        html.append("\t\t\t\t\t<td>&nbsp;&nbsp;</td>");
        html.append("\t\t\t\t\t<td style='width: 100%;'>");
        html.append("\t\t\t\t\t\tThe Alkacon OpenCms update wizard has not been started correctly!<br>");
        html.append("\t\t\t\t\t\tPlease click <a href='").append(pathPrefix);
        html.append("index.jsp'>here</a> to restart the wizard.");
        html.append("\t\t\t\t\t</td>");
        html.append("\t\t\t\t</tr>");
        html.append("\t\t\t</table>");
        html.append(getHtmlPart("C_BLOCK_END"));
        html.append("\t\t</td>");
        html.append("\t</tr>");
        html.append("</table>");
        return html.toString();
    }

    /**
     * Returns the admin Pwd.<p>
     *
     * @return the admin Pwd
     */
    public String getAdminPwd() {
        return m_adminPwd;
    }

    /**
     * Returns the admin User.<p>
     *
     * @return the admin User
     */
    public String getAdminUser() {
        return m_adminUser;
    }

    /**
     * Returns a map of all previously installed modules.<p>
     * 
     * @return a map of <code>[String, {@link org.opencms.module.CmsModuleVersion}]</code> objects
     * 
     * @see org.opencms.module.CmsModuleManager#getAllInstalledModules()
     */
    public Map getInstalledModules() {
        String file = CmsModuleConfiguration.DEFAULT_XML_FILE_NAME;
        String basePath = new StringBuffer("/").append(CmsConfigurationManager.N_ROOT).append("/").append(CmsModuleConfiguration.N_MODULES).append("/").append(CmsModuleXmlHandler.N_MODULE).append("[?]/").toString();
        Map modules = new HashMap();
        String name = "";
        for (int i = 1; name != null; i++) {
            if (i > 1) {
                String ver = CmsModuleVersion.DEFAULT_VERSION;
                try {
                    ver = getXmlHelper().getValue(file, CmsStringUtil.substitute(basePath, "?", "" + (i - 1)) + CmsModuleXmlHandler.N_VERSION);
                } catch (CmsXmlException e) {
                }
                modules.put(name, new CmsModuleVersion(ver));
            }
            try {
                name = getXmlHelper().getValue(file, CmsStringUtil.substitute(basePath, "?", "" + i) + CmsModuleXmlHandler.N_NAME);
            } catch (CmsXmlException e) {
            }
        }
        return modules;
    }

    /**
     * List of modules to be updated.<p>
     * 
     * @return a list of module names
     */
    public List getModulesToUpdate() {
        if (m_modulesToUpdate == null) {
            getUptodateModules();
        }
        return m_modulesToUpdate;
    }

    /**
     * Returns the update database thread.<p>
     * 
     * @return the update database thread
     */
    public CmsUpdateDBThread getUpdateDBThread() {
        return m_dbUpdateThread;
    }

    /**
     * Returns the update Project.<p>
     *
     * @return the update Project
     */
    public String getUpdateProject() {
        return m_updateProject;
    }

    /**
     * Returns the update site.<p>
     *
     * @return the update site
     */
    public String getUpdateSite() {
        return m_updateSite;
    }

    /**
     * Returns the modules that does not need to be updated.<p>
     * 
     * @return a list of module names
     */
    public List getUptodateModules() {
        if (m_uptodateModules == null) {
            m_uptodateModules = new ArrayList();
            m_modulesToUpdate = new ArrayList();
            Map installedModules = getInstalledModules();
            Map availableModules = getAvailableModules();
            Iterator itMods = availableModules.entrySet().iterator();
            while (itMods.hasNext()) {
                Map.Entry entry = (Map.Entry) itMods.next();
                String name = (String) entry.getKey();
                CmsModuleVersion instVer = (CmsModuleVersion) installedModules.get(name);
                CmsModuleVersion availVer = ((CmsModule) entry.getValue()).getVersion();
                boolean uptodate = (!UPDATE_ALL_MODULES) && ((instVer != null) && (instVer.compareTo(availVer) >= 0));
                if (uptodate) {
                    m_uptodateModules.add(name);
                } else {
                    m_modulesToUpdate.add(name);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug(name + " --- installed: " + instVer + " available: " + availVer + " --- uptodate: " + uptodate);
                }
            }
        }
        return m_uptodateModules;
    }

    /**
     * Returns the workplace update thread.<p>
     * 
     * @return the workplace update thread
     */
    public CmsUpdateThread getWorkplaceUpdateThread() {
        return m_workplaceUpdateThread;
    }

    /**
     * @see org.opencms.setup.CmsSetupBean#htmlModules()
     */
    public String htmlModules() {
        StringBuffer html = new StringBuffer(1024);
        Set uptodate = new HashSet(getUptodateModules());
        Iterator itModules = sortModules(getAvailableModules().values()).iterator();
        boolean hasModules = false;
        for (int i = 0; itModules.hasNext(); i++) {
            String moduleName = (String) itModules.next();
            CmsModule module = (CmsModule) getAvailableModules().get(moduleName);
            if (UPDATE_ALL_MODULES || !uptodate.contains(moduleName)) {
                html.append(htmlModule(module, i));
                hasModules = true;
            } else {
                html.append("<input type='hidden' name='availableModules' value='");
                html.append(moduleName);
                html.append("'>\n");
            }
        }
        if (!hasModules) {
            html.append("\t<tr>\n");
            html.append("\t\t<td style='vertical-align: middle;'>\n");
            html.append(Messages.get().getBundle().key(Messages.GUI_WARNING_ALL_MODULES_UPTODATE_0));
            html.append("\t\t</td>\n");
            html.append("\t</tr>\n");
        }
        return html.toString();
    }

    /** 
     * Creates a new instance of the setup Bean.<p>
     * 
     * @param webAppRfsPath path to the OpenCms web application
     * @param servletMapping the OpenCms servlet mapping
     * @param defaultWebApplication the name of the default web application
     */
    public void init(String webAppRfsPath, String servletMapping, String defaultWebApplication) {
        try {
            super.init(webAppRfsPath, servletMapping, defaultWebApplication);
            if (m_workplaceUpdateThread != null) {
                if (m_workplaceUpdateThread.isAlive()) {
                    m_workplaceUpdateThread.kill();
                }
                m_workplaceUpdateThread = null;
            }
            if (m_dbUpdateThread != null) {
                if (m_dbUpdateThread.isAlive()) {
                    m_dbUpdateThread.kill();
                }
                m_dbUpdateThread = null;
                m_newLoggingOffset = 0;
                m_oldLoggingOffset = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the keep History parameter value.<p>
     *
     * @return the keep History parameter value
     */
    public boolean isKeepHistory() {
        return m_keepHistory;
    }

    /**
     * Returns <code>true</code> if a DB update is needed.<p>
     *
     * @return <code>true</code> if a DB update is needed
     */
    public boolean isNeedDbUpdate() {
        return m_needDbUpdate;
    }

    /**
     * Prepares step 1 of the update wizard.<p>
     */
    public void prepareUpdateStep1() {
    }

    /**
     * Prepares step 1 of the update wizard.<p>
     */
    public void prepareUpdateStep1b() {
        if (!isInitialized()) {
            return;
        }
        if ((m_dbUpdateThread != null) && (m_dbUpdateThread.isFinished())) {
            return;
        }
        if (m_dbUpdateThread == null) {
            m_dbUpdateThread = new CmsUpdateDBThread(this);
        }
        if (!m_dbUpdateThread.isAlive()) {
            m_dbUpdateThread.start();
        }
    }

    /**
     * Generates the output for step 1 of the setup wizard.<p>
     * 
     * @param out the JSP print stream
     * @throws IOException in case errors occur while writing to "out"
     */
    public void prepareUpdateStep1bOutput(JspWriter out) throws IOException {
        m_oldLoggingDBOffset = m_newLoggingDBOffset;
        m_newLoggingDBOffset = m_dbUpdateThread.getLoggingThread().getMessages().size();
        if (isInitialized()) {
            for (int i = m_oldLoggingDBOffset; i < m_newLoggingDBOffset; i++) {
                String str = m_dbUpdateThread.getLoggingThread().getMessages().get(i).toString();
                str = CmsEncoder.escapeWBlanks(str, CmsEncoder.ENCODING_UTF_8);
                out.println("output[" + (i - m_oldLoggingDBOffset) + "] = \"" + str + "\";");
            }
        } else {
            out.println("output[0] = 'ERROR';");
        }
        boolean threadFinished = m_dbUpdateThread.isFinished();
        boolean allWritten = m_oldLoggingDBOffset >= m_dbUpdateThread.getLoggingThread().getMessages().size();
        out.println("function initThread() {");
        if (isInitialized()) {
            out.print("send();");
            if (threadFinished && allWritten) {
                out.println("setTimeout('top.display.finish()', 1000);");
            } else {
                int timeout = 5000;
                if (getUpdateDBThread().getLoggingThread().getMessages().size() < 20) {
                    timeout = 2000;
                }
                out.println("setTimeout('location.reload()', " + timeout + ");");
            }
        }
        out.println("}");
    }

    /**
     * Prepares step 5 of the update wizard.<p>
     */
    public void prepareUpdateStep5() {
        if (isInitialized()) {
            try {
                String fileName = getWebAppRfsPath() + FOLDER_UPDATE + "cmsupdate";
                FileInputStream fis = new FileInputStream(fileName + CmsConfigurationManager.POSTFIX_ORI);
                String script = "";
                int readChar = fis.read();
                while (readChar > -1) {
                    script += (char) readChar;
                    readChar = fis.read();
                }
                fis.close();
                script = CmsStringUtil.substitute(script, C_ADMIN_USER, getAdminUser());
                script = CmsStringUtil.substitute(script, C_ADMIN_PWD, getAdminPwd());
                script = CmsStringUtil.substitute(script, C_UPDATE_PROJECT, getUpdateProject());
                script = CmsStringUtil.substitute(script, C_UPDATE_SITE, getUpdateSite());
                script = CmsStringUtil.substitute(script, C_ADMIN_GROUP, getAdminGroup());
                FileOutputStream fos = new FileOutputStream(fileName + ".txt");
                fos.write(script.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Prepares step 5 of the update wizard.<p>
     */
    public void prepareUpdateStep5b() {
        if (!isInitialized()) {
            return;
        }
        if ((m_workplaceUpdateThread != null) && (m_workplaceUpdateThread.isFinished())) {
            return;
        }
        if (m_workplaceUpdateThread == null) {
            m_workplaceUpdateThread = new CmsUpdateThread(this);
        }
        if (!m_workplaceUpdateThread.isAlive()) {
            m_workplaceUpdateThread.start();
        }
    }

    /**
     * Generates the output for the update wizard.<p>
     * 
     * @param out the JSP print stream
     * 
     * @throws IOException in case errors occur while writing to "out"
     */
    public void prepareUpdateStep5bOutput(JspWriter out) throws IOException {
        if ((m_workplaceUpdateThread == null) || (m_workplaceUpdateThread.getLoggingThread() == null)) {
            return;
        }
        m_oldLoggingOffset = m_newLoggingOffset;
        m_newLoggingOffset = m_workplaceUpdateThread.getLoggingThread().getMessages().size();
        if (isInitialized()) {
            for (int i = m_oldLoggingOffset; i < m_newLoggingOffset; i++) {
                String str = m_workplaceUpdateThread.getLoggingThread().getMessages().get(i).toString();
                str = CmsEncoder.escapeWBlanks(str, CmsEncoder.ENCODING_UTF_8);
                out.println("output[" + (i - m_oldLoggingOffset) + "] = \"" + str + "\";");
            }
        } else {
            out.println("output[0] = 'ERROR';");
        }
        boolean threadFinished = m_workplaceUpdateThread.isFinished();
        boolean allWritten = m_oldLoggingOffset >= m_workplaceUpdateThread.getLoggingThread().getMessages().size();
        out.println("function initThread() {");
        if (isInitialized()) {
            out.print("send();");
            if (threadFinished && allWritten) {
                out.println("setTimeout('top.display.finish()', 500);");
            } else {
                int timeout = 5000;
                if (getWorkplaceUpdateThread().getLoggingThread().getMessages().size() < 20) {
                    timeout = 1000;
                }
                out.println("setTimeout('location.reload()', " + timeout + ");");
            }
        }
        out.println("}");
    }

    /**
     * Prepares step 6 of the update wizard.<p>
     */
    public void prepareUpdateStep6() {
        if (isInitialized()) {
            lockWizard();
            saveProperties(getProperties(), CmsSystemInfo.FILE_PROPERTIES, false);
        }
    }

    /**
     * Sets the admin Pwd.<p>
     *
     * @param adminPwd the admin Pwd to set
     */
    public void setAdminPwd(String adminPwd) {
        m_adminPwd = adminPwd;
    }

    /**
     * Sets the admin User.<p>
     *
     * @param adminUser the admin User to set
     */
    public void setAdminUser(String adminUser) {
        m_adminUser = adminUser;
    }

    /**
     * Sets the keep History parameter value.<p>
     *
     * @param keepHistory the keep History parameter value to set
     */
    public void setKeepHistory(boolean keepHistory) {
        m_keepHistory = keepHistory;
    }

    /**
     * Sets the DB update flag.<p>
     *
     * @param needDbUpdate the value to set
     */
    public void setNeedDbUpdate(boolean needDbUpdate) {
        m_needDbUpdate = needDbUpdate;
    }

    /**
     * Sets the update Project.<p>
     *
     * @param updateProject the update Project to set
     */
    public void setUpdateProject(String updateProject) {
        m_updateProject = updateProject;
    }

    /**
     * Sets the update site.<p>
     *
     * @param site the update site to set
     */
    public void setUpdateSite(String site) {
        m_updateSite = site;
    }

    /**
     * @see org.opencms.main.I_CmsShellCommands#shellExit()
     */
    public void shellExit() {
        System.out.println();
        System.out.println();
        System.out.println("The update is finished!\nThe OpenCms system used for the update will now shut down.");
    }

    /**
     * @see org.opencms.main.I_CmsShellCommands#shellStart()
     */
    public void shellStart() {
        System.out.println();
        System.out.println("Starting Workplace update for OpenCms!");
        String[] copy = org.opencms.main.Messages.COPYRIGHT_BY_ALKACON;
        for (int i = copy.length - 1; i >= 0; i--) {
            System.out.println(copy[i]);
        }
        System.out.println("This is OpenCms " + OpenCms.getSystemInfo().getVersionNumber());
        System.out.println();
        System.out.println();
    }

    /**
     * Installed all modules that have been set using {@link #setInstallModules(String)}.<p>
     * 
     * This method is invoked as a shell command.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void updateModulesFromUpdateBean() throws Exception {
        if ((m_cms != null) && (m_installModules != null)) {
            I_CmsReport report = new CmsShellReport(m_cms.getRequestContext().getLocale());
            Set utdModules = new HashSet(getUptodateModules());
            for (int i = 0; i < m_installModules.size(); i++) {
                String name = (String) m_installModules.get(i);
                if (!utdModules.contains(name)) {
                    String filename = (String) m_moduleFilenames.get(name);
                    try {
                        updateModule(name, filename, report);
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                } else {
                    report.println(Messages.get().container(Messages.RPT_MODULE_UPTODATE_1, name), I_CmsReport.FORMAT_HEADLINE);
                }
            }
        }
    }

    /**
     * Fills the relations db tables during the update.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void updateRelations() throws Exception {
        if (!m_needDbUpdate) {
            return;
        }
        I_CmsReport report = new CmsShellReport(m_cms.getRequestContext().getLocale());
        report.println(Messages.get().container(Messages.RPT_START_UPDATE_RELATIONS_0), I_CmsReport.FORMAT_HEADLINE);
        String storedSite = m_cms.getRequestContext().getSiteRoot();
        CmsProject project = null;
        try {
            String projectName = "Update relations project";
            try {
                project = m_cms.readProject(projectName);
            } catch (CmsException e) {
                project = m_cms.createProject(projectName, projectName, OpenCms.getDefaultUsers().getGroupAdministrators(), OpenCms.getDefaultUsers().getGroupAdministrators(), CmsProject.PROJECT_TYPE_TEMPORARY);
            }
            m_cms.getRequestContext().setSiteRoot("");
            m_cms.getRequestContext().setCurrentProject(project);
            List types = OpenCms.getResourceManager().getResourceTypes();
            int n = types.size();
            int m = 0;
            Iterator itTypes = types.iterator();
            while (itTypes.hasNext()) {
                I_CmsResourceType type = (I_CmsResourceType) itTypes.next();
                m++;
                report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_SUCCESSION_2, String.valueOf(m), String.valueOf(n)), I_CmsReport.FORMAT_NOTE);
                report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_ARGUMENT_1, type.getTypeName()));
                report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_DOTS_0));
                if (type instanceof I_CmsLinkParseable) {
                    try {
                        CmsXmlContentRepairSettings settings = new CmsXmlContentRepairSettings(m_cms);
                        settings.setIncludeSubFolders(true);
                        settings.setVfsFolder("/");
                        settings.setForce(true);
                        settings.setResourceType(type.getTypeName());
                        CmsXmlContentRepairThread t = new CmsXmlContentRepairThread(m_cms, settings);
                        t.start();
                        synchronized (this) {
                            t.join();
                        }
                        report.println(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_OK_0), I_CmsReport.FORMAT_OK);
                    } catch (Exception e) {
                        report.println(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_ERROR_0), I_CmsReport.FORMAT_ERROR);
                        report.addError(e);
                        e.printStackTrace(System.err);
                    }
                } else {
                    report.println(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_SKIPPED_0), I_CmsReport.FORMAT_WARNING);
                }
            }
        } finally {
            try {
                if (project != null) {
                    try {
                        m_cms.unlockProject(project.getUuid());
                        OpenCms.getPublishManager().publishProject(m_cms, report, OpenCms.getPublishManager().getPublishList(m_cms));
                        OpenCms.getPublishManager().waitWhileRunning();
                    } finally {
                        m_cms.getRequestContext().setCurrentProject(m_cms.readProject(CmsProject.ONLINE_PROJECT_ID));
                    }
                }
            } finally {
                m_cms.getRequestContext().setSiteRoot(storedSite);
            }
            report.println(Messages.get().container(Messages.RPT_FINISH_UPDATE_RELATIONS_0), I_CmsReport.FORMAT_HEADLINE);
        }
    }

    /**
     * Returns the admin Group.<p>
     *
     * @return the admin Group
     */
    protected String getAdminGroup() {
        return m_adminGroup;
    }

    /**
     * Sets the admin Group.<p>
     *
     * @param adminGroup the admin Group to set
     */
    protected void setAdminGroup(String adminGroup) {
        m_adminGroup = adminGroup;
    }

    /**
     * Imports a module (zipfile) from the default module directory, 
     * creating a temporary project for this.<p>
     * 
     * @param moduleName the name of the module to replace
     * @param importFile the name of the import .zip file located in the update module directory
     * @param report the shell report to write the output
     * 
     * @throws Exception if something goes wrong
     * 
     * @see org.opencms.importexport.CmsImportExportManager#importData(org.opencms.file.CmsObject, I_CmsReport, org.opencms.importexport.CmsImportParameters)
     */
    protected void updateModule(String moduleName, String importFile, I_CmsReport report) throws Exception {
        String fileName = getModuleFolder() + importFile;
        report.println(Messages.get().container(Messages.RPT_BEGIN_UPDATE_MODULE_1, moduleName), I_CmsReport.FORMAT_HEADLINE);
        if (OpenCms.getModuleManager().getModule(moduleName) != null) {
            OpenCms.getModuleManager().deleteModule(m_cms, moduleName, true, report);
        }
        OpenCms.getPublishManager().stopPublishing();
        OpenCms.getPublishManager().startPublishing();
        OpenCms.getPublishManager().waitWhileRunning();
        OpenCms.getImportExportManager().importData(m_cms, report, new CmsImportParameters(fileName, "/", true));
        report.println(Messages.get().container(Messages.RPT_END_UPDATE_MODULE_1, moduleName), I_CmsReport.FORMAT_HEADLINE);
        OpenCms.getPublishManager().stopPublishing();
        OpenCms.getPublishManager().startPublishing();
        OpenCms.getPublishManager().waitWhileRunning();
    }
}
