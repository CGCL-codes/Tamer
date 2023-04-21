package com.csbubbles.mamont.ui;

import com.csbubbles.mamont.App;
import com.csbubbles.mamont.core.AbstractSubscriber;
import com.csbubbles.mamont.msg.Msg;
import com.csbubbles.mamont.msg.MsgStop;
import com.csbubbles.mamont.msg.ui.MsgUIStop;

/**
 * User interface implementation.
 */
public class UI extends AbstractSubscriber {

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Msg>[] getMsgTypes() {
        return MSG_TYPES;
    }

    @Override
    public void run() {
        IMainWindow mainWindow = new MainWindow(this);
        mainWindow.open();
        while (!isInterrupted()) {
            Msg msg = get();
            if (msg == null) {
                continue;
            }
            log.debug(msg + " received");
            if (msg instanceof MsgStop) {
                mainWindow.close();
                break;
            } else if (msg instanceof MsgUIStop) {
                App.dispatch(new MsgStop(this));
                break;
            } else {
                log.warn(msg + " not handled");
            }
        }
    }

    /**
	 * Message types to subscribe and handle.
	 */
    private static final Class[] MSG_TYPES = { MsgStop.class };
}
