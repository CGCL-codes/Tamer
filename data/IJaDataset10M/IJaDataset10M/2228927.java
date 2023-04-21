package demo.examples.utils;

import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sepp.config.SeppConfiguration;
import org.sepp.exceptions.SecurityServiceException;
import org.sepp.security.SecurityService;

public class KeyStoreManipulator {

    private SeppConfiguration configuration;

    private SecurityService securityService;

    private Log log;

    public KeyStoreManipulator(String configFile) {
        log = LogFactory.getLog(this.getClass());
        configuration = new SeppConfiguration(configFile);
        try {
            securityService = SecurityService.getInstance(configuration);
            createSharedSecret("SePP");
            SecretKey secretKey = getSharedSecret("SePP");
            log.info(secretKey.toString());
        } catch (SecurityServiceException e) {
            log.error("Couldn't start the security service. Reason: " + e.getMessage());
            System.exit(-1);
        }
    }

    private void createSharedSecret(String alias) {
        try {
            SecretKey sharedSecret = securityService.createSessionKey();
            securityService.addSecretKey(alias, sharedSecret);
        } catch (SecurityServiceException e) {
            log.error("Couldn't create and store shared secret. Reason: " + e.getMessage());
        }
    }

    private SecretKey getSharedSecret(String alias) {
        try {
            return securityService.getSecretKey(alias);
        } catch (SecurityServiceException e) {
            log.error("Couldn't create and store shared secret. Reason: " + e.getMessage());
            return null;
        }
    }

    public static void main(String argv[]) {
        if (argv.length != 2) {
            System.exit(-1);
        } else {
            new KeyStoreManipulator(argv[1]);
        }
    }
}
