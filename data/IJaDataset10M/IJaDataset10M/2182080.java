package uk.org.ogsadai.service.gt.context;

import java.io.File;
import org.globus.wsrf.config.ContainerConfig;
import org.springframework.context.support.GenericApplicationContext;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.common.msgs.MessageID;
import uk.org.ogsadai.context.ContextInitializationException;
import uk.org.ogsadai.context.OGSADAIConstants;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.context.SpringUtils;

/**
 * Initialization class for GT-compliant presentation layers. Initializes
 * OGSA-DAI using Spring and a configuration file in the CLASSPATH at
 * <code>ogsadai-context.xml</code>.
 *
 * @author The OGSA-DAI Project Team,
 */
public class GTOGSADAIContextInitializer {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2010.";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(GTOGSADAIContextInitializer.class);

    /** Has the initialization taken place? */
    private static boolean mIsInitialized = false;

    /** Relative OGSA-DAI configuration directory path. */
    private static final String CONFIG_PATH = "etc/dai";

    /** Relative schema directory path. */
    private static final String SCHEMA_PATH = "share/schema";

    /**
     * Initialize the OGSA-DAI global context. This assumes
     * that the webapp directory is available within the GT container 
     * configuration. Any problems are logged. This method only initializes the
     * context once - if a call to this method succeeds then subsequent calls 
     * to the method are no-ops.
     *
     * @throws ContextInitializationException
     *     If required configuration parameters cannot be found
     *     or there is some other problem.
     */
    public static synchronized void initialize() throws ContextInitializationException {
        if (mIsInitialized) {
            return;
        }
        try {
            LOG.info(MessageID.MSG_INITIALIZE_GLOBAL_CONTEXT);
            String webAppConfigDir = ContainerConfig.getBaseDirectory();
            String webAppDirStr = webAppConfigDir.substring(0, webAppConfigDir.length() - 8);
            File configDir = new File(webAppConfigDir, CONFIG_PATH);
            File webAppDir = new File(webAppDirStr);
            File schemaDir = new File(webAppDir, SCHEMA_PATH);
            GenericApplicationContext genericCtx = new GenericApplicationContext();
            SpringUtils.registerFileBean(genericCtx, OGSADAIConstants.CONFIG_DIR.toString(), configDir);
            SpringUtils.registerFileBean(genericCtx, OGSADAIConstants.WEB_APP_DIR.toString(), webAppDir);
            SpringUtils.registerFileBean(genericCtx, OGSADAIConstants.SCHEMA_DIR.toString(), schemaDir);
            genericCtx.refresh();
            OGSADAIContext.initialize(genericCtx);
            mIsInitialized = true;
            LOG.info(MessageID.MSG_INITIALIZED_GLOBAL_CONTEXT);
            OGSADAIContext.getInstance().logEntries();
        } catch (Exception e) {
            throw new ContextInitializationException(e);
        }
    }
}
