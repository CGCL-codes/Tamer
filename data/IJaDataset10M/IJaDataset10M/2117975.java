package com.quikj.application.web.talk.feature.proactive.server;

import java.io.IOException;
import java.util.Date;
import com.quikj.server.framework.AceLogger;
import com.quikj.server.framework.AceMessageInterface;
import com.quikj.server.framework.AceSignalMessage;
import com.quikj.server.framework.AceTimer;
import com.quikj.server.framework.AceTimerMessage;

/**
 * 
 * @author amit
 */
public class ProactiveServiceController extends com.quikj.server.framework.AceThread {

    private static ProactiveServiceController instance = null;

    private static final long SESSION_TIMEOUT = 30 * 60 * 1000L;

    private int timerId = -1;

    /** Creates a new instance of ProactiveServiceController */
    public ProactiveServiceController() throws IOException {
        super("ProactiveServiceController");
        instance = this;
    }

    public static ProactiveServiceController getInstance() {
        return instance;
    }

    public void dispose() {
        if (instance != null) {
            interruptWait(AceSignalMessage.SIGNAL_TERM, "disposed");
            if (timerId != -1) {
                try {
                    AceTimer.Instance().cancelTimer(timerId);
                } catch (IOException ex) {
                }
                timerId = -1;
            }
            super.dispose();
            instance = null;
        }
    }

    private void removeOldSessions() {
        long d = (new Date()).getTime() - SESSION_TIMEOUT;
        ProactiveUserData.getInstance().removeSessions(new Date(d));
    }

    public void run() {
        try {
            timerId = AceTimer.Instance().startTimer(SESSION_TIMEOUT, 0L);
            if (timerId == -1) {
                AceLogger.Instance().log(AceLogger.ERROR, AceLogger.SYSTEM_LOG, getName() + "- ProactiveServiceController.run() -- Could not start timer - " + getErrorMessage());
            }
        } catch (IOException ex) {
        }
        while (true) {
            AceMessageInterface message = waitMessage();
            if (message == null) {
                AceLogger.Instance().log(AceLogger.ERROR, AceLogger.SYSTEM_LOG, getName() + "- ProactiveServiceController.run() -- A null message was received while waiting for a message - " + getErrorMessage());
                break;
            }
            if ((message instanceof AceSignalMessage) == true) {
                AceLogger.Instance().log(AceLogger.INFORMATIONAL, AceLogger.SYSTEM_LOG, getName() + " - ProactiveServiceController.run() --  A signal " + ((AceSignalMessage) message).getSignalId() + " is received : " + ((AceSignalMessage) message).getMessage());
                break;
            } else if ((message instanceof AceTimerMessage) == true) {
                timerId = -1;
                removeOldSessions();
                try {
                    timerId = AceTimer.Instance().startTimer(SESSION_TIMEOUT, 0L);
                    if (timerId == -1) {
                        AceLogger.Instance().log(AceLogger.ERROR, AceLogger.SYSTEM_LOG, getName() + "- ProactiveServiceController.run() -- Could not re-start timer - " + getErrorMessage());
                    }
                } catch (IOException ex) {
                }
            }
        }
        dispose();
    }
}
