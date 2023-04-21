package goldengate.ftp.exec;

import goldengate.common.file.filesystembased.FilesystemBasedDirImpl;
import goldengate.common.file.filesystembased.FilesystemBasedFileParameterImpl;
import goldengate.common.file.filesystembased.specific.FilesystemBasedDirJdk5;
import goldengate.common.file.filesystembased.specific.FilesystemBasedDirJdk6;
import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;
import goldengate.common.logging.GgSlf4JLoggerFactory;
import goldengate.ftp.core.config.FtpConfiguration;
import goldengate.ftp.core.exception.FtpNoConnectionException;
import goldengate.ftp.exec.config.FileBasedConfiguration;
import goldengate.ftp.exec.control.ExecBusinessHandler;
import goldengate.ftp.exec.data.FileSystemBasedDataBusinessHandler;
import goldengate.ftp.exec.exec.AbstractExecutor;
import openr66.protocol.configuration.Configuration;
import org.jboss.netty.logging.InternalLoggerFactory;

/**
 * Exec FTP Server using simple authentication (XML FileInterface based),
 * and standard Directory and FileInterface implementation (Filesystem based).
 *
 * @author Frederic Bregier
 *
 */
public class ExecGatewayFtpServer {

    /**
     * Internal Logger
     */
    private static GgInternalLogger logger = null;

    /**
     * Take a simple XML file as configuration.
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: " + ExecGatewayFtpServer.class.getName() + " <config-file> [<r66config-file>]");
            return;
        }
        InternalLoggerFactory.setDefaultFactory(new GgSlf4JLoggerFactory(null));
        logger = GgInternalLoggerFactory.getLogger(ExecGatewayFtpServer.class);
        String config = args[0];
        FileBasedConfiguration configuration = new FileBasedConfiguration(ExecGatewayFtpServer.class, ExecBusinessHandler.class, FileSystemBasedDataBusinessHandler.class, new FilesystemBasedFileParameterImpl());
        if (!configuration.setConfigurationServerFromXml(config)) {
            System.err.println("Bad main configuration");
            return;
        }
        Configuration.configuration.useLocalExec = configuration.useLocalExec;
        if (FtpConfiguration.USEJDK6) {
            FilesystemBasedDirImpl.initJdkDependent(new FilesystemBasedDirJdk6());
        } else {
            FilesystemBasedDirImpl.initJdkDependent(new FilesystemBasedDirJdk5());
        }
        if (AbstractExecutor.useDatabase) {
            if (args.length > 1) {
                if (!openr66.configuration.FileBasedConfiguration.setSubmitClientConfigurationFromXml(args[1])) {
                    System.err.println("Bad R66 configuration");
                    return;
                }
            } else {
                System.err.println("No R66PrepareTransfer configuration file");
            }
        }
        FileBasedConfiguration.fileBasedConfiguration = configuration;
        configuration.configureLExec();
        configuration.serverStartup();
        configuration.configureHttps();
        configuration.configureConstraint();
        try {
            configuration.configureSnmp();
        } catch (FtpNoConnectionException e) {
            System.err.println("Cannot start SNMP support: " + e.getMessage());
        }
        logger.warn("FTP started");
    }
}
