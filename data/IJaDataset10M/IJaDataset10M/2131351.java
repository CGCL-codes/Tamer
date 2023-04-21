package us.wthr.jdem846;

import us.wthr.jdem846.annotations.OnShutdown;
import us.wthr.jdem846.annotations.Service;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;

@Service(name = "us.wthr.jdem846.userPropertiesStorageService", enabled = true)
public class UserPropertiesStorageService implements AppService {

    private static Log log = Logging.getLog(UserPropertiesStorageService.class);

    public UserPropertiesStorageService() {
    }

    @OnShutdown
    public void onShutdown() {
        log.info("Writing user properties to disk...");
        try {
            JDem846Properties.writeUserPropertiesFile();
            log.info("Finished writing user properties.");
        } catch (Exception ex) {
            log.warn("Error writing user properties file: " + ex.getMessage(), ex);
        }
    }
}
