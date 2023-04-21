package org.mobicents.servlet.sip.catalina;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.catalina.Context;
import org.apache.catalina.startup.HostConfig;
import org.apache.log4j.Logger;
import org.mobicents.servlet.sip.catalina.annotations.SipApplicationAnnotationUtils;
import org.mobicents.servlet.sip.core.SipContext;

/**
 * @author Jean Deruelle
 *
 */
public class SipHostConfig extends HostConfig {

    private static final String WAR_EXTENSION = ".war";

    private static final String SAR_EXTENSION = ".sar";

    public static final String SIP_CONTEXT_CLASS = "org.mobicents.servlet.sip.startup.SipStandardContext";

    public static final String SIP_CONTEXT_CONFIG_CLASS = "org.mobicents.servlet.sip.startup.SipContextConfig";

    private static final Logger logger = Logger.getLogger(SipHostConfig.class);

    /**
	 * 
	 */
    public SipHostConfig() {
        super();
    }

    @Override
    protected void deployApps() {
        super.deployApps();
    }

    @Override
    protected String getDocBase(String path) {
        String basename = null;
        if (path.equals("")) {
            basename = "ROOT";
        } else {
            basename = path.substring(1).replace('/', '#');
        }
        return (basename);
    }

    @Override
    protected void deployApps(String name) {
        File appBase = appBase();
        File configBase = configBase();
        String baseName = getConfigFile(name);
        String docBase = getDocBase(name);
        File xml = new File(configBase, baseName + ".xml");
        if (xml.exists()) deployDescriptor(name, xml, baseName + ".xml");
        File war = new File(appBase, docBase + ".war");
        if (war.exists()) {
            boolean isSipServletApplication = isSipServletArchive(war);
            if (isSipServletApplication) {
                deploySAR(name, war, docBase + ".war");
            } else {
                deployWAR(name, war, docBase + ".war");
            }
        }
        File dir = new File(appBase, docBase);
        if (dir.exists()) {
            deployDirectory(name, dir, docBase);
        }
        File sar = new File(appBase, docBase + SAR_EXTENSION);
        if (sar.exists()) {
            deploySAR(name, sar, docBase + SAR_EXTENSION);
        }
    }

    /**
	 * 
	 * @param NAME
	 * @param sar
	 * @param string
	 */
    private void deploySAR(String contextPath, File sar, String file) {
        if (deploymentExists(contextPath)) return;
        if (logger.isDebugEnabled()) {
            logger.debug(SipContext.APPLICATION_SIP_XML + " found in " + sar + ". Enabling sip servlet archive deployment");
        }
        String initialConfigClass = configClass;
        String initialContextClass = contextClass;
        host.setConfigClass(SIP_CONTEXT_CONFIG_CLASS);
        setConfigClass(SIP_CONTEXT_CONFIG_CLASS);
        setContextClass(SIP_CONTEXT_CLASS);
        deployWAR(contextPath, sar, file);
        host.setConfigClass(initialConfigClass);
        configClass = initialConfigClass;
        contextClass = initialContextClass;
    }

    @Override
    protected void deployWAR(String contextPath, File dir, String file) {
        if (logger.isTraceEnabled()) {
            logger.trace("Context class used to deploy the WAR : " + contextClass);
            logger.trace("Context config class used to deploy the WAR : " + configClass);
        }
        super.deployWAR(contextPath, dir, file);
    }

    @Override
    protected void deployDirectory(String contextPath, File dir, String file) {
        if (deploymentExists(contextPath)) return;
        boolean isSipServletApplication = isSipServletDirectory(dir);
        if (isSipServletApplication) {
            if (logger.isDebugEnabled()) {
                logger.debug(SipContext.APPLICATION_SIP_XML + " found in " + dir + ". Enabling sip servlet archive deployment");
            }
            String initialConfigClass = configClass;
            String initialContextClass = contextClass;
            host.setConfigClass(SIP_CONTEXT_CONFIG_CLASS);
            setConfigClass(SIP_CONTEXT_CONFIG_CLASS);
            setContextClass(SIP_CONTEXT_CLASS);
            super.deployDirectory(contextPath, dir, file);
            host.setConfigClass(initialConfigClass);
            configClass = initialConfigClass;
            contextClass = initialContextClass;
        } else {
            super.deployDirectory(contextPath, dir, file);
        }
    }

    @Override
    protected void deployDescriptor(String contextPath, File contextXml, String file) {
        super.deployDescriptor(contextPath, contextXml, file);
    }

    @Override
    protected void deployWARs(File appBase, String[] files) {
        if (files == null) return;
        for (int i = 0; i < files.length; i++) {
            if (files[i].equalsIgnoreCase("META-INF")) continue;
            if (files[i].equalsIgnoreCase("WEB-INF")) continue;
            File dir = new File(appBase, files[i]);
            boolean isSipServletApplication = isSipServletArchive(dir);
            if (isSipServletApplication) {
                String contextPath = "/" + files[i];
                int period = contextPath.lastIndexOf(".");
                if (period >= 0) contextPath = contextPath.substring(0, period);
                if (contextPath.equals("/ROOT")) contextPath = "";
                if (isServiced(contextPath)) continue;
                String file = files[i];
                String initialConfigClass = configClass;
                String initialContextClass = contextClass;
                host.setConfigClass(SIP_CONTEXT_CONFIG_CLASS);
                setConfigClass(SIP_CONTEXT_CONFIG_CLASS);
                setContextClass(SIP_CONTEXT_CLASS);
                deploySAR(contextPath, dir, file);
                host.setConfigClass(initialConfigClass);
                configClass = initialConfigClass;
                contextClass = initialContextClass;
            }
        }
        super.deployWARs(appBase, files);
    }

    /**
	 * Check if the file given in parameter match a sip servlet application, i.e.
	 * if it contains a sip.xml in its WEB-INF directory
	 * @param file the file to check (war or sar)
	 * @return true if the file is a sip servlet application, false otherwise
	 */
    private boolean isSipServletArchive(File file) {
        if (file.getName().toLowerCase().endsWith(SAR_EXTENSION)) {
            return true;
        } else if (file.getName().toLowerCase().endsWith(WAR_EXTENSION)) {
            try {
                JarFile jar = new JarFile(file);
                JarEntry entry = jar.getJarEntry(SipContext.APPLICATION_SIP_XML);
                if (entry != null) {
                    return true;
                }
            } catch (IOException e) {
                if (logger.isInfoEnabled()) {
                    logger.info("couldn't find WEB-INF/sip.xml in " + file + " checking for package-info.class");
                }
            }
            return SipApplicationAnnotationUtils.findPackageInfoInArchive(file);
        }
        return false;
    }

    /**
	 * Check if the file given in parameter match a sip servlet application, i.e.
	 * if it contains a sip.xml in its WEB-INF directory
	 * @param file the file to check (war or sar)
	 * @return true if the file is a sip servlet application, false otherwise
	 */
    private boolean isSipServletDirectory(File dir) {
        if (dir.isDirectory()) {
            File sipXmlFile = new File(dir.getAbsoluteFile(), SipContext.APPLICATION_SIP_XML);
            if (sipXmlFile.exists()) {
                return true;
            }
            if (SipApplicationAnnotationUtils.findPackageInfoinDirectory(dir)) return true;
        }
        return false;
    }

    @Override
    public void manageApp(Context arg0) {
        super.manageApp(arg0);
    }
}
