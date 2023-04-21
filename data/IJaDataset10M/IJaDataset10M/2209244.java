package org.openmobster.core.common.bus;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.hornetq.core.client.ClientConsumer;
import org.hornetq.core.client.ClientMessage;
import org.hornetq.core.client.ClientProducer;
import org.hornetq.core.client.ClientSession;
import org.hornetq.core.client.ClientSessionFactory;
import org.hornetq.core.client.impl.ClientSessionFactoryImpl;
import org.hornetq.core.config.TransportConfiguration;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.exception.HornetQException;
import org.hornetq.utils.SimpleString;
import org.openmobster.core.common.errors.ErrorHandler;
import org.openmobster.core.common.errors.SystemException;
import org.openmobster.core.common.XMLUtilities;
import org.openmobster.core.common.transaction.TransactionHelper;

/**
 * @author openmobster@gmail.com
 */
public final class Bus {

    private static Logger log = Logger.getLogger(Bus.class);

    private static Map<String, Bus> activeBuses;

    private static ClientSessionFactory sessionFactory;

    static {
        activeBuses = new HashMap<String, Bus>();
    }

    private String uri;

    private List<BusListener> busListeners;

    private Thread busReader;

    private BusReader threadRunner;

    private Bus(String uri) {
        if (uri == null || uri.trim().length() == 0) {
            throw new IllegalArgumentException("Bus Uri must be specified!!");
        }
        this.uri = uri;
        this.busListeners = new ArrayList<BusListener>();
    }

    private void start() {
        ClientSession coreSession = null;
        try {
            Bus.activeBuses.put(this.uri, this);
            synchronized (Bus.class) {
                if (sessionFactory == null) {
                    sessionFactory = new ClientSessionFactoryImpl(new TransportConfiguration(InVMConnectorFactory.class.getName()));
                }
            }
            coreSession = sessionFactory.createSession(false, false, false);
            if (!coreSession.queueQuery(new SimpleString(this.uri)).isExists()) {
                coreSession.createQueue(this.uri, this.uri, true);
            }
            coreSession.close();
            this.threadRunner = new BusReader();
            this.busReader = new Thread(this.threadRunner);
            this.busReader.start();
        } catch (HornetQException hqe) {
            log.error(this, hqe);
            ErrorHandler.getInstance().handle(hqe);
            throw new SystemException(hqe.getMessage(), hqe);
        } finally {
            if (coreSession != null && !coreSession.isClosed()) {
                try {
                    coreSession.close();
                } catch (HornetQException hqe) {
                    ErrorHandler.getInstance().handle(hqe);
                    throw new SystemException(hqe.getMessage(), hqe);
                }
            }
        }
    }

    private void stop() {
        try {
            Bus.activeBuses.remove(uri);
            if (this.busReader != null) {
                this.threadRunner.exit = true;
            }
        } catch (Exception e) {
            ErrorHandler.getInstance().handle(e);
        }
    }

    private void restart() {
        try {
            if (this.busReader != null) {
                this.threadRunner.exit = true;
                this.busReader.join();
                this.threadRunner = new BusReader();
                this.busReader = new Thread(this.threadRunner);
                this.busReader.start();
            }
        } catch (Exception e) {
            log.error(this, e);
            ErrorHandler.getInstance().handle(e);
        }
    }

    private void addBusListener(BusListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("BusListener must be specified!!");
        }
        this.busListeners.add(listener);
    }

    private void removeBusListener(BusListener listener) {
        this.busListeners.remove(listener);
    }

    private void sendMessageOnQueue(BusMessage busMessage) {
        ClientSession session = null;
        ClientProducer producer = null;
        try {
            session = sessionFactory.createSession();
            producer = session.createProducer(uri);
            session.start();
            String busMessageXml = XMLUtilities.marshal(busMessage);
            ClientMessage message = session.createClientMessage(true);
            message.putStringProperty("message", busMessageXml);
            producer.send(message);
            producer.close();
            session.stop();
            session.close();
        } catch (HornetQException hqe) {
            ErrorHandler.getInstance().handle(hqe);
            throw new SystemException(hqe.getMessage(), hqe);
        } finally {
            if (producer != null && !producer.isClosed()) {
                try {
                    producer.close();
                } catch (HornetQException hqe) {
                    ErrorHandler.getInstance().handle(hqe);
                    throw new SystemException(hqe.getMessage(), hqe);
                }
            }
            if (session != null && !session.isClosed()) {
                try {
                    session.stop();
                    session.close();
                } catch (HornetQException hqe) {
                    ErrorHandler.getInstance().handle(hqe);
                    throw new SystemException(hqe.getMessage(), hqe);
                }
            }
        }
    }

    public static void startBus(String uri) {
        Bus bus = new Bus(uri);
        bus.start();
    }

    public static void stopBus(String uri) {
        Bus bus = Bus.activeBuses.get(uri);
        bus.stop();
    }

    public static void restartBus(String uri) {
        Bus bus = Bus.activeBuses.get(uri);
        bus.restart();
    }

    public static void addBusListener(String uri, BusListener listener) {
        Bus bus = Bus.activeBuses.get(uri);
        if (bus == null) {
            throw new IllegalStateException(uri + " is not active!!");
        }
        if (listener == null) {
            throw new IllegalArgumentException("BusListener must be specified!!");
        }
        bus.addBusListener(listener);
    }

    public static void removeBusListener(String uri, BusListener listener) {
        Bus bus = Bus.activeBuses.get(uri);
        if (bus != null) {
            bus.removeBusListener(listener);
        }
    }

    public static void sendMessage(BusMessage message) {
        if (message == null) {
            return;
        }
        if (message.getSenderUri() == null || message.getSenderUri().trim().length() == 0) {
            throw new IllegalStateException("Sender URI is mandatory!!");
        }
        if (message.getBusUri() == null || message.getBusUri().trim().length() == 0) {
            throw new IllegalStateException("Bus URI is mandatory!!");
        }
        Bus bus = Bus.activeBuses.get(message.getBusUri());
        bus.sendMessageOnQueue(message);
    }

    private class BusReader implements Runnable {

        private ClientSession session;

        private boolean exit;

        public void run() {
            ClientConsumer messageConsumer = null;
            try {
                session = sessionFactory.createSession();
                session.start();
                messageConsumer = session.createConsumer(uri);
                do {
                    ClientMessage message = messageConsumer.receive(20000);
                    if (exit) {
                        break;
                    }
                    if (message != null) {
                        boolean isStartedHere = TransactionHelper.startTx();
                        try {
                            SimpleString msg = (SimpleString) message.getProperty("message");
                            BusMessage busMessage = (BusMessage) XMLUtilities.unmarshal(msg.toString());
                            busMessage.setAttribute("hornetq-message", message);
                            this.sendBusListenerEvent(busMessage);
                            if (isStartedHere) {
                                TransactionHelper.commitTx();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (isStartedHere) {
                                TransactionHelper.rollbackTx();
                            }
                        }
                    }
                } while (true);
            } catch (HornetQException hqe) {
                ErrorHandler.getInstance().handle(hqe);
                throw new SystemException(hqe.getMessage(), hqe);
            } finally {
                if (messageConsumer != null && !messageConsumer.isClosed()) {
                    try {
                        messageConsumer.close();
                    } catch (HornetQException hqe) {
                        ErrorHandler.getInstance().handle(hqe);
                        throw new SystemException(hqe.getMessage(), hqe);
                    }
                }
                if (session != null && !session.isClosed()) {
                    try {
                        session.stop();
                        session.close();
                    } catch (HornetQException hqe) {
                        ErrorHandler.getInstance().handle(hqe);
                        throw new SystemException(hqe.getMessage(), hqe);
                    }
                }
            }
        }

        private void sendBusListenerEvent(BusMessage busMessage) {
            for (BusListener busListener : busListeners) {
                try {
                    busListener.messageIncoming(busMessage);
                } catch (Exception e) {
                    try {
                        ErrorHandler.getInstance().handle(e);
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }
}
