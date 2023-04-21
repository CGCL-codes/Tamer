package org.geoforge.guillc.splitpane;

import java.awt.Component;
import java.util.logging.Logger;
import javax.swing.JSplitPane;
import org.geoforge.io.serial.IGfrSerializer;
import org.geoforge.lang.IShrObj;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class SplAbs extends JSplitPane implements IShrObj, IGfrSerializer {

    private static final Logger _LOGGER_ = Logger.getLogger(SplAbs.class.getName());

    static {
        SplAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected SplAbs() {
        super();
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void loadUnserialized() throws Exception {
        for (int i = 0; i < super.getComponentCount(); i++) {
            Component cmpCur = super.getComponent(i);
            if (cmpCur instanceof IGfrSerializer) {
                IGfrSerializer serCur = (IGfrSerializer) cmpCur;
                serCur.loadUnserialized();
            }
        }
    }

    @Override
    public void releaseUnserialized() throws Exception {
        for (int i = 0; i < super.getComponentCount(); i++) {
            Component cmpCur = super.getComponent(i);
            if (cmpCur instanceof IGfrSerializer) {
                IGfrSerializer serCur = (IGfrSerializer) cmpCur;
                serCur.releaseUnserialized();
            }
        }
    }
}
