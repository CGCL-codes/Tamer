package de.rauchhaupt.games.poker.holdem.lib.remoteplayer.server;

import org.apache.log4j.Logger;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.WebTools;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.server.wsstubs.ClientHoldemPlayer;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.server.wsstubs.ClientHoldemPokerResult;

public abstract class ThreadedRemoteAction implements Runnable {

    long actionStarted = 0;

    long actionEnded = 0;

    long TIMEOUT_MS = 5000;

    ClientHoldemPokerResult result = null;

    ClientHoldemPlayer theClientHoldemPlayer = null;

    RemoteHoldemPlayer theRemoteHoldemPlayer = null;

    static Logger stdlog = Logger.getLogger(ThreadedRemoteAction.class);

    public ThreadedRemoteAction(RemoteHoldemPlayer aRemoteHoldemPlayer) {
        theRemoteHoldemPlayer = aRemoteHoldemPlayer;
        theClientHoldemPlayer = aRemoteHoldemPlayer.theClientHoldemPlayer;
    }

    public void send() {
        actionStarted = System.currentTimeMillis();
        new Thread(this).run();
        long finalTimeoutTime = actionStarted + TIMEOUT_MS;
        try {
            while ((result == null) || (conditionIsFullfilled() == false)) {
                if (System.currentTimeMillis() > finalTimeoutTime) {
                    result = new ClientHoldemPokerResult();
                    result.setHoldemPokerException("Timeout exceeded. Took " + (System.currentTimeMillis() - actionStarted) + " ms for '" + getActionName() + "'.");
                    break;
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            stdlog.error("Could not wait until timeout", e);
        }
        WebTools.checkResult(result);
    }

    public String getActionName() {
        return "";
    }

    boolean conditionIsFullfilled() {
        return true;
    }

    /**
    * @see java.lang.Runnable#run()
    */
    @Override
    public void run() {
        if (stdlog.isDebugEnabled()) stdlog.debug("Sending action '" + getActionName() + "'");
        result = sendAction();
        actionEnded = System.currentTimeMillis();
        if (stdlog.isDebugEnabled()) stdlog.debug("Finished action '" + getActionName() + "' in " + (actionEnded - actionStarted) + " ms");
    }

    public abstract ClientHoldemPokerResult sendAction();
}
