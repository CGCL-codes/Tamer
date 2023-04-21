package org.silicolife.gui.actionlistener;

import org.apache.log4j.Logger;
import org.silicolife.gui.SLFrame;

public class DefaultEditActionListener extends SLEditActionListener {

    private static Logger logger = Logger.getLogger(DefaultEditActionListener.class);

    public DefaultEditActionListener(SLFrame frame) {
        super(frame);
    }

    public static DefaultEditActionListener createEditActionListener(SLFrame frame) {
        return new DefaultEditActionListener(frame);
    }

    public void callCut() {
    }

    public void callCopy() {
    }

    public void callPaste() {
    }

    public void callClear() {
    }

    public void callSelectAll() {
    }
}
