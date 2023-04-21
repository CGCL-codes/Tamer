package visitpc.distjar;

import java.io.File;
import javax.swing.ProgressMonitor;
import visitpc.VisitPCException;
import visitpc.webserver.WebServerConfig;

/**
 * Responsible for holding the config required to create a distributable jar file.
 */
public class DistJarConfig {

    public String serverName;

    public int serverPort = -1;

    public DistJarDialogConfig distJarDialogConfig;

    public File configDir;

    public File jarFile;

    public File tmpDir;

    public File visitPCJarFile;

    public File srvMgrDir;

    public File vncServerDir;

    public String trustStoreFile;

    public String embeddedConfigFile;

    public String startupClass;

    public String defaultEncryptKey;

    public WebServerConfig webServerConfig;

    public String programName;

    public void check(boolean signedJar) throws VisitPCException {
        File f;
        if (serverName == null) {
            throw new VisitPCException("Server name is not defined in the configuration.");
        }
        if (serverPort == -1) {
            throw new VisitPCException("Server port is not defined in the configuration.");
        }
        if (distJarDialogConfig == null) {
            throw new VisitPCException("DistJarConfig.distJarDialogConfig not set");
        }
        if (configDir == null) {
            throw new VisitPCException("DistJarConfig.configDir not set");
        }
        if (jarFile == null) {
            throw new VisitPCException("DistJarConfig.jarFile not set");
        }
        if (tmpDir == null) {
            throw new VisitPCException("DistJarConfig.tmpDir not set");
        }
        if (visitPCJarFile == null) {
            throw new VisitPCException("DistJarConfig.visitPCJarFile not set");
        }
        if (srvMgrDir == null) {
            throw new VisitPCException("DistJarConfig.srvMgrDir not set");
        }
        if (vncServerDir == null) {
            throw new VisitPCException("DistJarConfig.vncServerDir not set");
        }
        if (trustStoreFile == null || trustStoreFile.trim().length() == 0) {
            throw new VisitPCException("Please specify a trust/key store file in the configuration");
        }
        if (!trustStoreFile.equals("dummy")) {
            f = new File(trustStoreFile);
            if (!f.isFile()) {
                throw new VisitPCException(f + " (trust store) file not found.");
            }
        }
        if (embeddedConfigFile == null) {
            throw new VisitPCException("DistJarConfig.embeddedConfigFile not set");
        }
        if (startupClass == null) {
            throw new VisitPCException("DistJarConfig.startupClass not set");
        }
        if (defaultEncryptKey == null) {
            throw new VisitPCException("DistJarConfig.defaultEncryptKey not set");
        }
        if (webServerConfig != null) {
            webServerConfig.checkValid();
        }
        if (programName == null || programName.length() == 0) {
            throw new VisitPCException("DistJarConfig.programName not set");
        }
    }

    public ProgressMonitor pm;
}
