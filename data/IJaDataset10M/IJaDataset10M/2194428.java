package org.geoforge.guitlc.frame.main.tabbedpane;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import org.geoforge.io.util.property.PrpNamingGfr;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class TabSpcClsPrjAbs extends TabSpcAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(TabSpcClsPrjAbs.class.getName());

    static {
        TabSpcClsPrjAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected boolean _blnIsOpen = false;

    protected TabSpcClsPrjAbs() throws Exception {
        super(PrpNamingGfr.F_STR_NAME_SPACEPROJECT_DISPLAY);
    }

    public void open(ActionListener alrControllerSpcPrj) throws Exception {
        if (this._blnIsOpen) {
            String str = "this._blnIsOpen";
            TabSpcClsPrjAbs._LOGGER_.severe(str);
            throw new Exception(str);
        }
        this._blnIsOpen = true;
    }

    public void close() throws Exception {
        if (!this._blnIsOpen) {
            String str = "! this._blnIsOpen";
            TabSpcClsPrjAbs._LOGGER_.severe(str);
            throw new Exception(str);
        }
        this._blnIsOpen = false;
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
