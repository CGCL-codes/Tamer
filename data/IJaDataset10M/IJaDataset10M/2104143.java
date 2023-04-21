package org.powerfolder.apps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.powerfolder.PFRuntimeException;
import org.powerfolder.config.ConfigManager;
import org.powerfolder.config.ConfigManagerFactory;
import org.powerfolder.utils.misc.MiscHelper;

public class GenericJbossFileWorkflowApplicationLifecycle implements WorkflowApplicationLifecycle {

    protected static final String LOCATIONS_PATH = "/powerfolder-properties[1]/locations[1]";

    private static final String INIT_JSP = "<%\n" + "    org.powerfolder.workflow.web.client.SimpleWebClient client =\n" + "        (org.powerfolder.workflow.web.client.SimpleWebClient)" + "request.getAttribute(\"client\");\n" + "    client.setPageContext(pageContext);\n" + "    String goTo = (\"/WEB-INF/jsps/apps/\"" + " + client.getDestination() + \".jsp\");\n" + "%>\n" + "<jsp:include page=\"<%= goTo%>\"/>\n";

    public void undeployApplication(String inContext) {
        ConfigManager cm = ConfigManagerFactory.getConfigManager();
        String deployLocation = cm.getMandatoryPropertyAttribute(LOCATIONS_PATH, "deploy");
        File finalDeployDir = new File(deployLocation);
        File earFile = new File(finalDeployDir, inContext + ".ear");
        earFile.delete();
        cm.close();
    }

    public void deployApplication(WorkflowApplication inApplication, String inContextPath) {
        ConfigManager cm = ConfigManagerFactory.getConfigManager();
        String deployLocation = cm.getMandatoryPropertyAttribute(LOCATIONS_PATH, "deploy");
        File finalDeployDir = new File(deployLocation);
        File pfDir = getPowerFolderDirectory(cm);
        File assembleDir = new File(pfDir, "assemble");
        if (!assembleDir.exists()) {
            assembleDir.mkdirs();
        }
        MiscHelper.deleteFileOrDirectory(assembleDir);
        if (!assembleDir.exists()) {
            assembleDir.mkdirs();
        }
        File webInfDir = new File(assembleDir, "WEB-INF");
        if (!webInfDir.exists()) {
            webInfDir.mkdirs();
        }
        File jspsDir = new File(webInfDir, "jsps");
        if (!jspsDir.exists()) {
            jspsDir.mkdirs();
        }
        File appsJspsDir = new File(jspsDir, "apps");
        if (!appsJspsDir.exists()) {
            appsJspsDir.mkdirs();
        }
        File coreJspsDir = new File(jspsDir, "core");
        if (!coreJspsDir.exists()) {
            coreJspsDir.mkdirs();
        }
        File parentJspDir = new File(coreJspsDir, "parent.jsp");
        MiscHelper.writeTextFile(parentJspDir, INIT_JSP);
        for (int i = 0; i < inApplication.getWebPageCount(); i++) {
            WorkflowWebPage wwp = inApplication.getWebPage(i);
            String content = wwp.getContent();
            File wwpFile = new File(appsJspsDir, wwp.getName() + ".jsp");
            MiscHelper.writeTextFile(wwpFile, content);
        }
        for (int i = 0; i < inApplication.getWebFileCount(); i++) {
            WorkflowWebFile wwf = inApplication.getWebFile(i);
            byte content[] = wwf.getContent();
            File wwfFile = new File(assembleDir, wwf.getName());
            MiscHelper.writeBinaryFile(wwfFile, content);
        }
        String descriptor = getDeploymentDescriptor(inApplication);
        File webFile = new File(webInfDir, "web.xml");
        MiscHelper.writeTextFile(webFile, descriptor);
        String jbossWebDescriptor = getJbossDeploymentDescriptor();
        File jbossWebFile = new File(webInfDir, "jboss-web.xml");
        MiscHelper.writeTextFile(jbossWebFile, jbossWebDescriptor);
        File libDir = new File(webInfDir, "lib");
        if (!libDir.exists()) {
            libDir.mkdirs();
        }
        File destPowerfolderJarFile = new File(libDir, "powerfolder.jar");
        File sourceLibDir = new File(pfDir, "lib");
        if (!sourceLibDir.exists()) {
            sourceLibDir.mkdirs();
        }
        File sourcePowerfolderJarFile = new File(sourceLibDir, "powerfolder.jar");
        MiscHelper.writeBinaryFile(destPowerfolderJarFile, MiscHelper.readBinaryFile(sourcePowerfolderJarFile));
        File deployDir = new File(pfDir, "deploy");
        if (!deployDir.exists()) {
            deployDir.mkdirs();
        }
        MiscHelper.deleteFileOrDirectory(deployDir);
        if (!deployDir.exists()) {
            deployDir.mkdirs();
        }
        File warFile = new File(deployDir, "deploy.war");
        createWarFile(assembleDir, warFile);
        File metaInfDir = new File(deployDir, "META-INF");
        if (!metaInfDir.exists()) {
            metaInfDir.mkdirs();
        }
        String earDescriptor = getAppDeploymentDescriptor(inContextPath);
        File earFile = new File(metaInfDir, "application.xml");
        MiscHelper.writeTextFile(earFile, earDescriptor);
        File finalEarFile = new File(finalDeployDir, inContextPath + ".ear");
        createWarFile(deployDir, finalEarFile);
        cm.close();
    }

    private static final void createWarFile(File inSource, File inDest) {
        try {
            FileOutputStream fos = new FileOutputStream(inDest);
            MiscHelper.zipDirectory(inSource, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            throw new PFRuntimeException(ioe);
        }
    }

    private static final String getDeploymentDescriptor(WorkflowApplication inApplication) {
        final String START = "<!DOCTYPE web-app\n" + "    PUBLIC \"-//Sun Microsystems, Inc." + "//DTD Web Application 2.3//EN\"\n" + "    \"http://java.sun.com/dtd/web-app_2_3.dtd\">\n" + "\n" + "<web-app>\n";
        final String END = "    <ejb-ref>\n" + "        <ejb-ref-name>ejb/powerfolder/WorkflowLifecycle" + "</ejb-ref-name>\n" + "        <ejb-ref-type>Session</ejb-ref-type>\n" + "        <home>org.powerfolder.workflow.lifecycle." + "WorkflowLifecycleHome</home>\n" + "        <remote>org.powerfolder.workflow.lifecycle." + "WorkflowLifecycleRemote</remote>\n" + "    </ejb-ref>\n" + "    <ejb-ref>\n" + "        <ejb-ref-name>ejb/powerfolder/ConfigManager" + "</ejb-ref-name>\n" + "        <ejb-ref-type>Entity</ejb-ref-type>\n" + "        <home>org.powerfolder.workflow.config." + "ConfigManagerHome</home>\n" + "        <remote>org.powerfolder.workflow.config." + "ConfigManagerRemote</remote>\n" + "    </ejb-ref>\n" + "</web-app>\n";
        StringBuffer outValue = new StringBuffer();
        outValue.append(START);
        outValue.append("    <servlet>\n");
        outValue.append("        <servlet-name>\n");
        outValue.append("            BaseClientServlet\n");
        outValue.append("        </servlet-name>\n");
        outValue.append("        <servlet-class>\n");
        outValue.append("            org.powerfolder.workflow.web.client.BaseClientServlet\n");
        outValue.append("        </servlet-class>\n");
        outValue.append("    </servlet>\n");
        outValue.append("    <servlet-mapping>\n");
        outValue.append("        <servlet-name>\n");
        outValue.append("            BaseClientServlet\n");
        outValue.append("        </servlet-name>\n");
        outValue.append("        <url-pattern>/</url-pattern>\n");
        outValue.append("    </servlet-mapping>\n");
        outValue.append(END);
        return outValue.toString();
    }

    private static final String getJbossDeploymentDescriptor() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!DOCTYPE jboss-web\n" + "    PUBLIC \"-//JBoss//DTD Web Application 2.3//EN\"\n" + "    \"http://www.jboss.org/j2ee/dtds/jboss-web_3_0.dtd\">\n" + "\n" + "<jboss-web>\n" + "    <ejb-ref>\n" + "        <ejb-ref-name>ejb/powerfolder/WorkflowLifecycle" + "</ejb-ref-name>\n" + "        <jndi-name>powerfolder/WorkflowLifecycle</jndi-name>\n" + "    </ejb-ref>\n" + "    <ejb-ref>\n" + "        <ejb-ref-name>ejb/powerfolder/ConfigManager" + "</ejb-ref-name>\n" + "        <jndi-name>powerfolder/ConfigManager</jndi-name>\n" + "    </ejb-ref>\n" + "</jboss-web>\n";
    }

    private static final String getAppDeploymentDescriptor(String inContextPath) {
        StringBuffer outValue = new StringBuffer();
        outValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        outValue.append("<!DOCTYPE application\n");
        outValue.append("    PUBLIC '-//Sun Microsystems, Inc." + "//DTD J2EE Application 1.3//EN' \n");
        outValue.append("    'http://java.sun.com/dtd/application_1_3.dtd'>\n");
        outValue.append("\n");
        outValue.append("<application>\n");
        outValue.append("    <display-name>" + inContextPath + "</display-name>\n");
        outValue.append("    <description>" + inContextPath + "</description>\n");
        outValue.append("\n");
        outValue.append("    <module>\n");
        outValue.append("        <web>\n");
        outValue.append("            <web-uri>deploy.war</web-uri>\n");
        outValue.append("            <context-root>" + inContextPath + "</context-root>\n");
        outValue.append("        </web>\n");
        outValue.append("    </module>\n");
        outValue.append("</application>\n");
        return outValue.toString();
    }

    protected static final File getPowerFolderDirectory(ConfigManager inCm) {
        File outValue = null;
        String baseDirName = inCm.getMandatoryPropertyAttribute(LOCATIONS_PATH, "base");
        String pfDirName = inCm.getMandatoryPropertyAttribute(LOCATIONS_PATH, "pf");
        File baseDir = new File(baseDirName);
        outValue = new File(baseDir, pfDirName);
        if (!outValue.exists()) {
            outValue.mkdirs();
        }
        return outValue;
    }
}
