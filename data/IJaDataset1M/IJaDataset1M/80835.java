package net.sf.sail.emf.launch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import net.sf.sail.core.service.LauncherService;
import net.sf.sail.core.service.ServiceContext;
import net.sf.sail.core.uuid.SessionUuid;
import net.sf.sail.emf.sailuserdata.ESessionBundle;
import org.concord.applesupport.AppleApplicationUtil;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.apple.eio.FileManager;

public class SessionBundleHelper {

    private Logger logger = Logger.getLogger(SessionBundleHelper.class.getName());

    private File sessionBackupFolder;

    private File sessionBundleBackupFile;

    public SessionBundleHelper() {
        String versionDirStr = "v0";
        URL eCoreURL = SessionBundleHelper.class.getResource("/net/sf/sail/emf/models/sailuserdata.ecore");
        try {
            if (eCoreURL == null) {
                throw new IOException("eCore url was null.");
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(eCoreURL.openStream(), eCoreURL.toExternalForm());
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = "//eClassifiers[@name='ESessionBundle']/eStructuralFeatures[@name='version']/@defaultValueLiteral";
            String versionString = (String) xpath.evaluate(expression, document, XPathConstants.STRING);
            Pattern p = Pattern.compile("^\\$Rev: ([0-9]+) \\$$");
            Matcher m = p.matcher(versionString);
            if (m.matches()) {
                versionDirStr = "v" + m.group(1);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to load sailuserdata.ecore. Using default version.", e);
        } catch (ParserConfigurationException e) {
            logger.log(Level.WARNING, "Unable to load xml parser. Using default version.", e);
        } catch (SAXException e) {
            logger.log(Level.WARNING, "Unable to load sailuserdata.ecore xml. Using default version.", e);
        } catch (XPathExpressionException e) {
            logger.log(Level.WARNING, "Unable to find version string. Using default version.", e);
        }
        String customDataFolderStr = System.getProperty(PortfolioManagerService.SAVE_DATA_FOLDER_PROP);
        if (customDataFolderStr != null && customDataFolderStr.length() > 0) {
            sessionBackupFolder = new File(customDataFolderStr + File.separator + "SailUserData" + File.separator + versionDirStr);
        } else {
            File applicationDataFolder = null;
            if (AppleApplicationUtil.MAC_OS_X) {
                try {
                    String applicationSupportStr = FileManager.findFolder(AppleApplicationUtil.kUserDomain, AppleApplicationUtil.OSTypeToInt("asup"), true);
                    applicationDataFolder = new File(applicationSupportStr);
                    if (!applicationDataFolder.isDirectory() || !applicationDataFolder.canWrite()) {
                        logger.warning("Can't write to apple folder: " + applicationSupportStr);
                        applicationDataFolder = null;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    applicationDataFolder = null;
                }
            }
            if (applicationDataFolder == null) {
                String homeDirStr = System.getProperty("user.home");
                applicationDataFolder = new File(homeDirStr);
            }
            sessionBackupFolder = new File(applicationDataFolder, "SailUserData" + File.separator + versionDirStr);
        }
        sessionBackupFolder.mkdirs();
        if (!sessionBackupFolder.exists() || !sessionBackupFolder.isDirectory()) {
            String tmpDirStr = System.getProperty("java.io.tmpdir");
            sessionBackupFolder = new File(tmpDirStr + File.separator + "SailUserData" + File.separator + versionDirStr);
            sessionBackupFolder.mkdirs();
        }
    }

    public void setupBackupFile(String sailDataFilePrefix, String userTempFilePrefix) {
        try {
            sessionBundleBackupFile = File.createTempFile(sailDataFilePrefix, ".xml", sessionBackupFolder);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "cannot create session file in backup folder", e);
            try {
                sessionBundleBackupFile = File.createTempFile(userTempFilePrefix, ".xml");
            } catch (IOException e2) {
                logger.log(Level.SEVERE, "cannot create temp backup file", e);
                sessionBundleBackupFile = null;
            }
        }
    }

    public void setupSessionBundle(ESessionBundle sessionBundle, BundlePoster bundlePoster, ServiceContext serviceContext, boolean log) {
        EMap launchProperties = sessionBundle.getLaunchProperties();
        String mavenJnlpVersion = System.getProperty(PortfolioManagerService.MAVEN_JNLP_VERSION_KEY);
        if (mavenJnlpVersion != null) {
            launchProperties.put(PortfolioManagerService.MAVEN_JNLP_VERSION_KEY, mavenJnlpVersion);
            if (log) {
                logger.info("jnlp_version: " + mavenJnlpVersion);
            }
        }
        if (bundlePoster != null) {
            String postUrl = bundlePoster.getPostUrl();
            sessionBundle.getSdsReturnAddresses().add(postUrl);
            if (log) {
                logger.info("sdsReturnAddress: " + postUrl);
            }
        }
        LauncherService launcherService = (LauncherService) serviceContext.getService(LauncherService.class);
        if (launcherService != null) {
            Properties sessionProperties = launcherService.getProperties();
            SessionUuid sessionUuid = launcherService.getSessionUuid();
            sessionBundle.setSessionUUID(sessionUuid);
            if (log) {
                logger.info("sessionId: " + sessionUuid);
            }
            if (sessionProperties != null) {
                Enumeration<Object> keys = sessionProperties.keys();
                for (; keys.hasMoreElements(); ) {
                    String key = (String) keys.nextElement();
                    String value = sessionProperties.getProperty(key);
                    launchProperties.put(key, value);
                }
            }
        } else {
            logger.warning("Service Context does not define a launcherService");
        }
    }

    public File getSessionBundleBackupFile() {
        return sessionBundleBackupFile;
    }

    public File getSessionBackupFolder() {
        return sessionBackupFolder;
    }

    public URI getLocalBundleURI() throws MalformedURLException {
        return URI.createURI(sessionBundleBackupFile.toURL().toExternalForm());
    }
}
