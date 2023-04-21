package com.gid.ui;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ResultObserver implements Observer {

    private static Log logger = LogFactory.getLog(ResultObserver.class.getName());

    ProcessResult processResult;

    JTextField statusArea;

    public ResultObserver(ProcessResult extractionResult, JTextField statusArea) {
        this.processResult = extractionResult;
        this.statusArea = statusArea;
    }

    public void update(Observable o, Object arg) {
        if (o != null) {
            final String msg = processResult.getStatusMessage().toString();
            statusArea.setText(msg);
            statusArea.setCaretPosition(statusArea.getDocument().getLength());
            if (logger.isDebugEnabled()) {
                logger.debug("observer " + msg);
            }
        }
    }
}
