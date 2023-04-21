package com.infomancers.workflow.channels;

import com.infomancers.collections.Action;
import com.infomancers.workflow.Place;
import com.infomancers.workflow.PlaceListener;
import com.infomancers.workflow.Token;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aviadbd
 */
public abstract class InputChannel {

    private class InputChannelPlaceListener implements PlaceListener {

        public void tokenAdded(Place place, Token token) {
            try {
                assert place == source : "Event source is not the attached place!";
                tokens.put(token);
            } catch (InterruptedException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
        }

        public void tokenRemoved(Place place, Token token) {
            assert place == source : "Event source is not the attached place!";
            tokens.remove(token);
        }
    }

    private class ProcessToken implements Runnable {

        private Token token;

        public ProcessToken(Token token) {
            this.token = token;
        }

        public void run() {
            try {
                send(token);
            } catch (Throwable t) {
                try {
                    errorHandler.accept(token);
                } catch (Exception ex) {
                    Logger.getLogger("global").log(Level.SEVERE, "Error while handling err token", ex);
                }
                Logger.getLogger("global").log(Level.SEVERE, "Error while sending token", t);
            }
        }
    }

    private class ProcessTokensQueue implements Runnable {

        public void run() {
            try {
                Token currentWork = tokens.take();
                exec.execute(new InputChannel.ProcessToken(currentWork));
            } catch (InterruptedException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
        }
    }

    private Place source;

    private BlockingQueue<Token> tokens = new LinkedBlockingQueue<Token>();

    private InputChannelPlaceListener listener = new InputChannelPlaceListener();

    private Executor exec;

    private Action<Token> errorHandler;

    public InputChannel(Executor exec, Action<Token> errorHandler) {
        try {
            this.exec = exec;
            this.errorHandler = errorHandler;
            init();
            exec.execute(new com.infomancers.workflow.channels.InputChannel.ProcessTokensQueue());
        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }

    public final void setSource(Place source) {
        assert source != null : "Place sent is null";
        if (this.source != null) {
            this.source.removePlaceListener(listener);
        }
        this.source = source;
        this.source.addPlaceListener(listener);
    }

    protected final Place getSource() {
        return source;
    }

    protected abstract void send(Token token) throws IOException;

    protected abstract void init() throws IOException;
}
