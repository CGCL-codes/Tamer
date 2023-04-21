package org.geoforge.appgsi.actioncontroller.perspective.glg;

import java.util.logging.Logger;
import org.geoforge.appgsi.actioncontroller.perspective.actionmanager.glg.AmrPrsManGsiGlg;
import org.geoforge.app.actioncontroller.perspective.AcrPrsManAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.guillc.AppAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 *
 */
public class AcrPrsManGsiGlg extends AcrPrsManAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(AcrPrsManGsiGlg.class.getName());

    static {
        AcrPrsManGsiGlg._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public AcrPrsManGsiGlg(AppAbs app) throws Exception {
        super(app, new AmrPrsManGsiGlg());
    }
}
