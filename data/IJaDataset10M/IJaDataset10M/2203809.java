package oracle.toplink.essentials.platform.server.sunas;

import oracle.toplink.essentials.internal.sessions.DatabaseSessionImpl;
import oracle.toplink.essentials.transaction.sunas.SunAS9TransactionController;
import oracle.toplink.essentials.platform.server.ServerPlatformBase;
import oracle.toplink.essentials.logging.SessionLog;
import oracle.toplink.essentials.logging.JavaLog;

/**
 * PUBLIC:
 *
 * This is the concrete subclass responsible for representing SunAS9-specific server behaviour.
 *
 * This platform overrides:
 *
 * getExternalTransactionControllerClass(): to use an SunAS9-specific controller class
 *
 */
public class SunAS9ServerPlatform extends ServerPlatformBase {

    /**
     * INTERNAL:
     * Default Constructor: All behaviour for the default constructor is inherited
     */
    public SunAS9ServerPlatform(DatabaseSessionImpl newDatabaseSession) {
        super(newDatabaseSession);
    }

    /**
     * INTERNAL: getExternalTransactionControllerClass(): Answer the class of external transaction controller to use
     * for Oc4j. This is read-only.
     *
     * @return Class externalTransactionControllerClass
     *
     * @see oracle.toplink.essentials.transaction.JTATransactionController
     * @see ServerPlatformBase.isJTAEnabled()
     * @see ServerPlatformBase.disableJTA()
     * @see ServerPlatformBase.initializeExternalTransactionController()
     */
    public Class getExternalTransactionControllerClass() {
        if (externalTransactionControllerClass == null) {
            externalTransactionControllerClass = SunAS9TransactionController.class;
        }
        return externalTransactionControllerClass;
    }

    public SessionLog getServerLog() {
        return new JavaLog();
    }
}
