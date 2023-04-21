package de.erdesignerng.visual;

import de.erdesignerng.visual.common.ERDesignerWorldConnector;
import javax.swing.*;
import java.util.List;

public abstract class LongRunningTask<T> extends Thread {

    private final ERDesignerWorldConnector connector;

    public interface MessagePublisher {

        void publishMessage(String aMessage);
    }

    protected abstract class MySwingWorker<X> extends SwingWorker<X, String> implements MessagePublisher {

        @Override
        public void publishMessage(String aMessage) {
            publish(aMessage);
        }
    }

    public LongRunningTask(ERDesignerWorldConnector aConnector) {
        connector = aConnector;
    }

    @Override
    public void run() {
        SwingWorker<T, String> worker = new MySwingWorker<T>() {

            @Override
            protected T doInBackground() throws Exception {
                return doWork(this);
            }

            @Override
            protected void process(List<String> aChunks) {
                handleProcess(aChunks);
            }
        };
        worker.execute();
        try {
            handleResult(worker.get());
        } catch (Exception e) {
            connector.notifyAboutException(e);
        } finally {
            try {
                cleanup();
            } catch (Exception e) {
                connector.notifyAboutException(e);
            }
        }
    }

    public void handleProcess(List<String> aChunks) {
        for (String theChunk : aChunks) {
            connector.setStatusText(theChunk);
        }
    }

    public abstract T doWork(MessagePublisher aMessagePublisher) throws Exception;

    public abstract void handleResult(T aResult);

    public void cleanup() throws Exception {
    }
}
