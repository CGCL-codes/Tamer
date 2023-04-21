package com.dukesoftware.utils.thread;

import java.util.ArrayList;
import java.util.List;
import com.dukesoftware.utils.data.IQueue;

@Deprecated
public final class Pipeline<T> {

    private final IQueue<T> entranceQueue;

    private final IQueue<T> outranceQueue;

    private final List<Connection<T>> connections;

    public Pipeline(IQueue<T> entranceQueue, IQueue<T> outranceQueue, List<Connection<T>> connections) {
        this.entranceQueue = entranceQueue;
        this.outranceQueue = outranceQueue;
        this.connections = connections;
    }

    public Pipeline<T> put(T request) throws InterruptedException {
        this.entranceQueue.put(request);
        return this;
    }

    public T get() throws InterruptedException {
        return outranceQueue.take();
    }

    public void start() {
        for (Connection<T> connection : connections) {
            connection.start();
        }
    }

    public void shutdown() {
        for (Connection<T> connection : connections) {
            connection.shutdown();
        }
    }

    private static final class Connection<T> extends AbstractTerminationThread {

        private T task;

        private Status status = Status.NONE;

        private final IQueue<T> input;

        private final IQueue<T> output;

        private final Process<T> processor;

        private Connection(final IQueue<T> input, final IQueue<T> output, final Process<T> processor) {
            this.input = input;
            this.output = output;
            this.processor = processor;
        }

        public synchronized void run() {
            try {
                while (running) {
                    task = input.take();
                    status = Status.TAKEN;
                    try {
                        task = processor.exec(task, this);
                    } catch (InterruptedException e) {
                        status = Status.SHUTDOWN_REQUESTED;
                        throw e;
                    }
                    if (isShutdownRequested()) {
                        status = Status.SHUTDOWN_REQUESTED;
                        return;
                    }
                    status = Status.EXECUTED;
                    output.put(task);
                    status = Status.PUT;
                }
            } catch (InterruptedException ex) {
            } finally {
                processor.finalize(task, status);
            }
        }
    }

    public static class PipelineBuilder<T> {

        private final IQueue<T> entranceQueue;

        private final int queueSize;

        private IQueue<T> outranceQueue;

        private final ArrayList<Connection<T>> connections = new ArrayList<Connection<T>>();

        public PipelineBuilder(final int queueSize) {
            this.entranceQueue = new Queue<T>(queueSize);
            this.queueSize = queueSize;
            this.outranceQueue = entranceQueue;
        }

        public PipelineBuilder<T> appendNextProcess(Process<T> processor) {
            final Queue<T> newOutranceQueue = new Queue<T>(queueSize);
            connections.add(new Connection<T>(outranceQueue, newOutranceQueue, processor));
            outranceQueue = newOutranceQueue;
            return this;
        }

        public Pipeline<T> build() {
            return new Pipeline<T>(entranceQueue, outranceQueue, connections);
        }
    }

    public static enum Status {

        NONE, TAKEN, PUT, EXECUTED, SHUTDOWN_REQUESTED
    }

    public static interface Process<T> {

        T exec(T task, IStartShutdownable check) throws InterruptedException;

        void finalize(T task, Status status);
    }
}
