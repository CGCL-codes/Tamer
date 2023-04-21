package visitpc.startupmanager;

import java.io.File;
import java.io.IOException;
import service_manager.*;
import visitpc.destclient.DestClient;
import visitpc.destclient.VNCDestClientConfig;
import visitpc.VisitPCConstants;
import visitpc.distjar.*;

/**
 * Responsible for providing validation of the configuration required for this service.
 */
public class VisitPCDestClientServiceConfiguration extends ServiceConfiguration {

    public VisitPCDestClientServiceConfiguration() throws ServiceManagerException {
    }

    VNCDestClientConfig vncDestClientConfig;

    /**
   * Validate the service configuration. 
   * 
   * @return true if the service configuration is valid
   */
    public boolean isValid() {
        boolean configurationAvailable = false;
        String encryptKey = DestClient.GetDefaultEncryptKey();
        try {
            vncDestClientConfig = new VNCDestClientConfig();
            vncDestClientConfig.load(VisitPCConstants.VNC_DEST_CLIENT_CONFIG_FILE, true, encryptKey);
            if (!vncDestClientConfig.startVNCServer) {
                configurationAvailable = true;
            }
        } catch (Exception e) {
        }
        return configurationAvailable;
    }

    /**
   * Return the message to be displayed in a user dialog if the configuration is invalid. 
   * @return The service invalid message
   */
    public String getServiceInvalidMessage() {
        if (vncDestClientConfig.startVNCServer) {
            return "When installed as a service the VNC Dest cliient is not able to start the VNC server.\nPlease change the VNC Dest Client configuration, not to start the VNC server.\nThen you may start the VNC server in a separate service in the startup manager.";
        }
        return "You must configure the " + getServiceName() + " service from the Launcher\nand get it working before attempting to run it as a service.";
    }

    /**
   * Copy the jar file to the service path
   */
    public void updateWindowsFiles(File path) throws ServiceManagerException, IOException {
        DistJarFactory distJarFactory = new DistJarFactory();
        try {
            distJarFactory.createStartupManagerJar(path, DistJarFactory.JAR_FILE_TYPES.VNC_DEST_CLIENT);
        } catch (Exception e) {
            throw new ServiceManagerException(e.getLocalizedMessage());
        }
    }
}
