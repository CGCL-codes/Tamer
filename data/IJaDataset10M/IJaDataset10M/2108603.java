package com.infomancers.workflow.channels;

import com.infomancers.workflow.Place;
import com.infomancers.workflow.Token;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aviadbd
 */
public abstract class OutputChannel {

    private class ProcessToken implements Runnable {

        private Token token;

        public ProcessToken(Token token) {
            this.token = token;
        }

        public void run() {
            target.addToken(token);
        }
    }

    private class TokenRetriever implements Runnable {

        public void run() {
            while (true) {
                try {
                    Token token = retrieve();
                    exec.execute(new ProcessToken(token));
                } catch (Throwable t) {
                    Logger.getLogger("global").log(Level.SEVERE, "Could not retrieve token", t);
                }
            }
        }
    }

    private Place target;

    private Executor exec;

    public OutputChannel(Executor exec) {
        try {
            this.exec = exec;
            init();
            exec.execute(new com.infomancers.workflow.channels.OutputChannel.TokenRetriever());
        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }

    public final void setTarget(Place target) {
        assert target != null : "Cannot assign target to be null";
        this.target = target;
    }

    public final Place getTarget() {
        return target;
    }

    protected abstract Token retrieve() throws IOException;

    protected abstract void init() throws IOException;
}
