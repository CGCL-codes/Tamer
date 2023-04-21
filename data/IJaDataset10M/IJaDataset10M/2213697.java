package sf2.core;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import sf2.io.MessageClient;
import sf2.io.MessageServer;
import sf2.io.event.RecvEvent;
import sf2.io.event.SendFailedEvent;
import sf2.log.Logging;

public class DedicateStage implements Stage {

    protected static final String LOG_NAME = "Stage";

    protected static final String DEFAULT_BROADCAST = "255.255.255.255";

    protected static final String PROP_BROADCAST = "sf2.core.broadcastAddr";

    protected static final String PROP_FAILSTOP = "sf2.core.enableFailStop";

    protected static final boolean DEFAULT_FAILSTOP = true;

    protected Logging logging;

    protected Config config;

    protected InetAddress local;

    protected InetAddress bcast;

    protected StageTask executor;

    protected MessageClient client;

    protected MessageServer server;

    protected boolean enableFailStop;

    protected BlockingQueue<Event> queue = new LinkedBlockingQueue<Event>();

    protected Map<Class, StageHandler> handlers = new HashMap<Class, StageHandler>();

    protected Map<Class, StageRemoteHandler> remoteHandlers = new HashMap<Class, StageRemoteHandler>();

    public void configure(MessageClient client, MessageServer server) throws StageExecption {
        try {
            logging = Logging.getInstance();
            this.config = Config.search();
            bcast = InetAddress.getByName(config.get(PROP_BROADCAST, DEFAULT_BROADCAST));
            logging.config(LOG_NAME, "bcast=" + bcast);
            this.server = server;
            this.client = client;
            local = server.getLocalHost();
            logging.config(LOG_NAME, "local=" + local);
            enableFailStop = config.getBoolean(PROP_FAILSTOP, DEFAULT_FAILSTOP);
        } catch (ConfigException e) {
            throw new StageExecption(e);
        } catch (UnknownHostException e) {
            throw new StageExecption(e);
        }
    }

    public void standby() {
        logging.config(LOG_NAME, "startup()");
        executor = new StageTask();
        if (enableFailStop) executor.setUncaughtExceptionHandler(new FailStopHandler());
        executor.start();
    }

    public void shutdown() {
        logging.config(LOG_NAME, "shutdown()");
        executor.shutdown();
    }

    public void addHandler(Class cls, StageHandler handler) {
        synchronized (handlers) {
            handlers.put(cls, handler);
        }
    }

    public void removeHandler(Class cls) {
        synchronized (handlers) {
            handlers.remove(cls);
        }
    }

    public void addRemoteHandler(Class cls, StageRemoteHandler handler) {
        synchronized (remoteHandlers) {
            remoteHandlers.put(cls, handler);
        }
    }

    public void removeRemoteHandler(Class cls) {
        synchronized (remoteHandlers) {
            remoteHandlers.remove(cls);
        }
    }

    class StageTask extends Thread {

        protected boolean keepRunning = true;

        public void shutdown() {
            keepRunning = false;
        }

        public void run() {
            try {
                while (keepRunning) {
                    Event event = queue.take();
                    if (event instanceof RecvEvent) {
                        RecvEvent recv = (RecvEvent) event;
                        Object msg = recv.getMessage();
                        StageRemoteHandler handler = null;
                        synchronized (remoteHandlers) {
                            handler = remoteHandlers.get(msg.getClass());
                        }
                        if (handler != null) handler.handle(recv.getSender(), recv.getTag(), msg); else {
                            logging.warning(LOG_NAME, "unknown remote message!  from=" + recv.getSender() + ", name=" + recv.getTag() + ", msg=" + msg);
                        }
                    } else {
                        StageHandler handler = null;
                        synchronized (handlers) {
                            handler = handlers.get(event.getClass());
                        }
                        if (handler != null) handler.handle(event); else {
                            logging.warning(LOG_NAME, "unknown event!  event=" + event);
                            if (event instanceof SendFailedEvent) {
                                SendFailedEvent fail = (SendFailedEvent) event;
                                logging.warning(LOG_NAME, "SendFailedEvent - dest=" + fail.getDest() + ", key=" + fail.getKey() + ", msg=" + fail.getMessage());
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }

    class FailStopHandler implements UncaughtExceptionHandler {

        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
