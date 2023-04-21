package org.geoforge.io.util.property;

import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PropMgrInitializerAppli {

    private PropMgrInitializerAppli() {
    }

    public static PropMgrInitializerAppli s_getInstance() {
        if (PropMgrInitializerAppli._INSTANCE_ == null) PropMgrInitializerAppli._INSTANCE_ = new PropMgrInitializerAppli();
        return PropMgrInitializerAppli._INSTANCE_;
    }

    public void doJob() throws Exception {
        PrpNamingGfr.s_init();
        FileHandlerLogger.s_init(PrpNamingGfr._STRS_LOGS_PARENT_FOLDERS, PrpNamingGfr._STR_APPLI_VERSION_TRANSFORMED);
        PrpMgrPrivatePropertiesUser.s_init();
    }

    private static PropMgrInitializerAppli _INSTANCE_ = null;
}
