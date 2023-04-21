package org.gamio.processor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.gamio.conf.ProcessorManagerProps;
import org.gamio.conf.ProcessorProps;
import org.gamio.logging.Log;
import org.gamio.logging.Logger;
import org.gamio.system.Context;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 21 $ $Date: 2008-10-02 03:18:05 -0400 (Thu, 02 Oct 2008) $
 */
public class ProcessorManagerImpl implements ProcessorManager {

    private static final Log log = Logger.getLogger(ProcessorManagerImpl.class);

    private ProcessorManagerProps processorManagerProps = null;

    private Map<String, Processor> lhmProcessor = new LinkedHashMap<String, Processor>();

    private final Lock lock = new ReentrantLock();

    private static class Processor {

        private ProcessorProps processorProps = null;

        private ProcessorState processorState = null;

        private ProcessorMqMsgListener processorMqMsgListener = null;

        private final Lock lock = new ReentrantLock();

        private static class ProcessorState {

            public void start(Processor processor) {
            }

            public void stop(Processor processor) {
            }

            protected void changeState(Processor processor, ProcessorState processorState) {
                processor.changeState(processorState);
            }
        }

        private static final class ProcessorStopped extends ProcessorState {

            private static class ProcessorStoppedHolder {

                static final ProcessorStopped processorStopped = new ProcessorStopped();
            }

            private ProcessorStopped() {
            }

            public static ProcessorStopped getInstance() {
                return ProcessorStoppedHolder.processorStopped;
            }

            @Override
            public void start(Processor processor) {
                ProcessorProps processorProps = processor.getProcessorProps();
                processor.processorMqMsgListener = new ProcessorMqMsgListener(processorProps);
                try {
                    processor.processorMqMsgListener.open();
                } catch (Exception e) {
                    processor.processorMqMsgListener.close();
                    log.error(e, "Processor[name<", processorProps.getName(), ">, id<", processorProps.getId(), ">] failed to start");
                    return;
                }
                Context.getInstance().getMessageQueue().registerMessageListener(processorProps.getId(), processor.processorMqMsgListener);
                changeState(processor, ProcessorStarted.getInstance());
                log.info("Processor[name<", processorProps.getName(), ">, id<", processorProps.getId(), ">] started");
            }
        }

        private static final class ProcessorStarted extends ProcessorState {

            private static class ProcessorStartedHolder {

                static final ProcessorStarted processorStarted = new ProcessorStarted();
            }

            private ProcessorStarted() {
            }

            public static ProcessorStarted getInstance() {
                return ProcessorStartedHolder.processorStarted;
            }

            @Override
            public void stop(Processor processor) {
                ProcessorProps processorProps = processor.getProcessorProps();
                Context.getInstance().getMessageQueue().deregisterMessageListener(processorProps.getId());
                processor.processorMqMsgListener.close();
                processor.processorMqMsgListener = null;
                changeState(processor, ProcessorStopped.getInstance());
                log.info("Processor[name<", processorProps.getName(), ">, id<", processorProps.getId(), ">] stopped");
            }
        }

        public Processor(ProcessorProps processorProps) {
            this.processorProps = processorProps;
            changeState(ProcessorStopped.getInstance());
        }

        public void start() {
            lock.lock();
            try {
                processorState.start(this);
            } finally {
                lock.unlock();
            }
        }

        public void stop() {
            lock.lock();
            try {
                processorState.stop(this);
            } finally {
                lock.unlock();
            }
        }

        public final ProcessorProps getProcessorProps() {
            return processorProps;
        }

        private final void changeState(ProcessorState processorState) {
            this.processorState = processorState;
        }
    }

    public ProcessorManagerImpl(ProcessorManagerProps processorManagerProps) {
        setProcessorManagerProps(processorManagerProps);
        ProcessorMqMsgListener.initializeCache(processorManagerProps);
    }

    public boolean registerProcessor(ProcessorProps processorProps) {
        boolean ok = true;
        Processor processor = new Processor(processorProps);
        lock.lock();
        try {
            processor = lhmProcessor.put(processorProps.getId(), processor);
            if (processor != null) {
                ok = false;
                lhmProcessor.put(processorProps.getId(), processor);
            }
        } finally {
            lock.unlock();
        }
        if (ok) log.info("Processor[name<", processorProps.getName(), ">, id<", processorProps.getId(), ">] was registered successfully"); else log.warn("Processor[name<", processorProps.getName(), ">, id<", processorProps.getId(), ">] has already been registered");
        return ok;
    }

    public void startAllProcessors() {
        lock.lock();
        try {
            for (Processor processor : lhmProcessor.values()) processor.start();
        } finally {
            lock.unlock();
        }
    }

    public void startProcessor(String id) {
        Processor processor = lhmProcessor.get(id);
        if (processor != null) processor.start();
    }

    public void stopAllProcessors() {
        lock.lock();
        try {
            for (Processor processor : lhmProcessor.values()) processor.stop();
        } finally {
            lock.unlock();
        }
    }

    public void stopProcessor(String id) {
        Processor processor = lhmProcessor.get(id);
        if (processor != null) processor.stop();
    }

    public void deregisterAllProcessors() {
        Map<String, Processor> lhmTemp = lhmProcessor;
        lock.lock();
        try {
            lhmProcessor = new LinkedHashMap<String, Processor>();
        } finally {
            lock.unlock();
        }
        for (Processor processor : lhmTemp.values()) processor.stop();
    }

    public ProcessorProps deregisterProcessor(String id) {
        Processor processor = null;
        lock.lock();
        try {
            processor = lhmProcessor.remove(id);
        } finally {
            lock.unlock();
        }
        if (processor != null) {
            processor.stop();
            return processor.getProcessorProps();
        }
        return null;
    }

    public void setProcessorManagerProps(ProcessorManagerProps processorManagerProps) {
        this.processorManagerProps = processorManagerProps;
    }

    public ProcessorManagerProps getProcessorManagerProps() {
        return processorManagerProps;
    }
}
