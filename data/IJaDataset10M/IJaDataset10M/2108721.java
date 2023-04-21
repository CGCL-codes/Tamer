package ow.routing.impl;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ow.id.ID;
import ow.id.IDAddressPair;
import ow.messaging.ExtendedMessageHandler;
import ow.messaging.Message;
import ow.messaging.MessageHandler;
import ow.messaging.MessageReceiver;
import ow.messaging.MessageSender;
import ow.messaging.MessagingAddress;
import ow.messaging.MessagingConfiguration;
import ow.messaging.MessagingProvider;
import ow.routing.CallbackOnNodeFailure;
import ow.routing.CallbackOnRoute;
import ow.routing.RoutingAlgorithm;
import ow.routing.RoutingAlgorithmConfiguration;
import ow.routing.RoutingAlgorithmProvider;
import ow.routing.RoutingContext;
import ow.routing.RoutingException;
import ow.routing.RoutingHop;
import ow.routing.RoutingResult;
import ow.routing.RoutingRuntime;
import ow.routing.RoutingService;
import ow.routing.RoutingServiceConfiguration;
import ow.routing.impl.message.AckMessage;
import ow.routing.impl.message.PingMessage;
import ow.routing.impl.message.RepNeighborsMessage;
import ow.routing.impl.message.ReqNeighborsMessage;
import ow.stat.MessagingReporter;

/**
 * The super class of all routing drivers.
 * Two types of routing drivers corresponding to iterative and recursive lookup are provided
 * as a subclass of this class.
 *
 * @see ow.routing.impl.IterativeRoutingDriver
 * @see ow.routing.impl.RecursiveRoutingDriver
 */
public abstract class AbstractRoutingDriver implements RoutingRuntime, RoutingService {

    protected static final Logger logger = Logger.getLogger("routing");

    protected RoutingServiceConfiguration config;

    private MessagingProvider msgProvider;

    protected MessageReceiver receiver;

    protected MessageSender sender;

    private final RoutingAlgorithmProvider algoProvider;

    private final RoutingAlgorithmConfiguration algoConfig;

    protected final boolean queryToAllContacts;

    protected RoutingAlgorithm algorithm;

    protected MessagingAddress statCollectorAddress;

    private final IDAddressPair selfIDAddressPair;

    private int selfAddressHashCode;

    private Map<Class<? extends Message>, List<MessageHandler>> handlerTable;

    private Map<Class<? extends Message>, List<ExtendedMessageHandler>> extendedHandlerTable;

    private RoutingDrvMessageHandler msgHandler;

    protected AbstractRoutingDriver(RoutingServiceConfiguration conf, MessagingProvider msgProvider, MessagingConfiguration msgConfig, int port, int portRange, RoutingAlgorithmProvider algoProvider, RoutingAlgorithmConfiguration algoConf, ID selfID) throws IOException {
        this.msgProvider = msgProvider;
        this.receiver = msgProvider.getReceiver(msgConfig, port, portRange);
        this.sender = this.receiver.getSender();
        this.algoProvider = algoProvider;
        this.algoConfig = algoConf;
        this.queryToAllContacts = algoConf.queryToAllContacts();
        this.config = conf;
        MessagingAddress selfAddr = this.receiver.getSelfAddress();
        int idSizeInByte = algoConf.getIDSizeInByte();
        if (selfID != null) {
            selfID = selfID.copy(idSizeInByte);
            this.selfIDAddressPair = IDAddressPair.getIDAddressPair(selfID, selfAddr);
        } else {
            this.selfIDAddressPair = IDAddressPair.getIDAddressPair(idSizeInByte, selfAddr);
        }
        this.selfAddressHashCode = selfAddr.hashCode();
        this.receiver.setSelfAddress(this.selfIDAddressPair);
        prepareHandlers();
    }

    public RoutingResult route(ID target, int numResponsibleNodeCandidates) throws RoutingException {
        return this.route(target, null, numResponsibleNodeCandidates);
    }

    public RoutingResult route(ID target, RoutingContext initialRoutingContext, int numResponsibleNodeCandidates) throws RoutingException {
        ID[] tgts = { target };
        RoutingContext[] cxts = null;
        if (initialRoutingContext != null) {
            cxts = new RoutingContext[1];
            cxts[0] = initialRoutingContext;
        }
        RoutingResult[] res = this.route(tgts, cxts, numResponsibleNodeCandidates);
        if (res == null || res[0] == null) throw new RoutingException();
        return res[0];
    }

    public RoutingResult invokeCallbacksOnRoute(ID target, int numResponsibleNodeCandidates, Serializable[] returnedValue, int tag, Serializable[] args) throws RoutingException {
        ID[] tgts = { target };
        Serializable[][] returnedValues = { returnedValue };
        Serializable[][] argss = { args };
        RoutingResult[] res = this.invokeCallbacksOnRoute(tgts, numResponsibleNodeCandidates, returnedValues, tag, argss);
        if (res == null || res[0] == null) throw new RoutingException();
        return res[0];
    }

    public void leave() {
        this.algorithm.reset();
    }

    public synchronized void stop() {
        logger.log(Level.INFO, "RoutingDriver#stop() called.");
        if (this.receiver != null) {
            this.receiver.stop();
            this.receiver = null;
            this.sender = null;
        }
        if (this.algorithm != null) {
            this.algorithm.stop();
            this.algorithm = null;
        }
    }

    public synchronized void suspend() {
        if (this.receiver != null) {
            this.receiver.stop();
        }
        if (this.algorithm != null) {
            this.algorithm.suspend();
        }
    }

    public synchronized void resume() {
        if (this.receiver != null) {
            this.receiver.start();
        }
        if (this.algorithm != null) {
            this.algorithm.resume();
        }
    }

    public RoutingServiceConfiguration getConfiguration() {
        return this.config;
    }

    public MessagingProvider getMessagingProvider() {
        return this.msgProvider;
    }

    /**
	 * Returns a {@link MessageReceiver MessageReceiver}.
	 */
    public MessageReceiver getMessageReceiver() {
        return this.receiver;
    }

    /**
	 * Returns a {@link MessageSender MessageSender}.
	 */
    public MessageSender getMessageSender() {
        return this.receiver.getSender();
    }

    public MessagingReporter getMessagingReporter() {
        return this.receiver.getMessagingReporter();
    }

    /**
	 * Returns the {@link RoutingAlgorithm RoutingAlgorithm} object.
	 */
    public RoutingAlgorithm getRoutingAlgorithm() {
        return this.algorithm;
    }

    /**
	 * Sets a {@link RoutingAlgorithm RoutingAlgorithm} object to this instance.
	 * Should be called by AbstractRoutingAlgorithm().
	 */
    public RoutingAlgorithm setRoutingAlgorithm(RoutingAlgorithm algo) {
        RoutingAlgorithm old = this.algorithm;
        if (this.algorithm != null) {
            this.algorithm.stop();
        }
        this.algorithm = algo;
        return old;
    }

    public void setStatCollectorAddress(MessagingAddress address) {
        this.statCollectorAddress = address;
        this.msgProvider.setMessagingCollectorAddress(address);
    }

    public String routeToString(RoutingHop[] route) {
        if (route == null) return "";
        long timeBase = -1L;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (RoutingHop hop : route) {
            if (timeBase < 0L) {
                timeBase = hop.getTime();
            }
            sb.append("\n ");
            sb.append(hop.getIDAddressPair());
            sb.append(" (");
            sb.append(hop.getTime() - timeBase);
            sb.append(")");
        }
        sb.append("\n]");
        return sb.toString();
    }

    /**
	 * An utility method for {@link RoutingRuntime RoutingRuntime} users.
	 * Confirm whether the target is alive or not by sending a PING message.
	 */
    public boolean ping(MessageSender sender, IDAddressPair target) throws IOException {
        Message ret = sender.sendAndReceive(target.getAddress(), new PingMessage());
        if (!(ret instanceof AckMessage)) {
            logger.log(Level.WARNING, "Received message should be ACK, but it is: " + ret.getName());
            return false;
        }
        this.algorithm.touch(target);
        return true;
    }

    public IDAddressPair getSelfIDAddressPair() {
        MessagingAddress currentSelfAddress = this.receiver.getSelfAddress();
        if (currentSelfAddress.hashCode() != this.selfAddressHashCode) {
            this.selfIDAddressPair.setAddressAndRecalculateID(currentSelfAddress);
            this.selfAddressHashCode = currentSelfAddress.hashCode();
            try {
                this.algoProvider.initializeAlgorithmInstance(this.algoConfig, this);
            } catch (InvalidAlgorithmParameterException e) {
                logger.log(Level.SEVERE, "Could not create a RoutingAlgorithm instance.");
            }
        }
        return this.selfIDAddressPair;
    }

    public void addMessageHandler(Class<? extends Message> msgClass, MessageHandler handler) {
        {
            if (this.handlerTable == null) {
                this.handlerTable = new HashMap<Class<? extends Message>, List<MessageHandler>>();
                if (this.msgHandler != null) this.receiver.removeHandler(this.msgHandler);
                this.msgHandler = new RoutingDrvMessageHandler();
                this.receiver.addHandler(this.msgHandler);
            }
            List<MessageHandler> handlerList = this.handlerTable.get(msgClass);
            if (handlerList == null) {
                handlerList = new ArrayList<MessageHandler>(1);
                this.handlerTable.put(msgClass, handlerList);
            }
            handlerList.add(handler);
        }
        if (handler instanceof ExtendedMessageHandler) {
            if (this.extendedHandlerTable == null) {
                this.extendedHandlerTable = new HashMap<Class<? extends Message>, List<ExtendedMessageHandler>>();
                if (this.msgHandler != null) this.receiver.removeHandler(this.msgHandler);
                this.msgHandler = new ExtRoutingDrvMessageHandler();
                this.receiver.addHandler(this.msgHandler);
            }
            List<ExtendedMessageHandler> handlerList = this.extendedHandlerTable.get(msgClass);
            if (handlerList == null) {
                handlerList = new ArrayList<ExtendedMessageHandler>(1);
                this.extendedHandlerTable.put(msgClass, handlerList);
            }
            handlerList.add((ExtendedMessageHandler) handler);
        }
    }

    private List<CallbackOnRoute> routeCallbackList = Collections.synchronizedList(new ArrayList<CallbackOnRoute>(1));

    public void addCallbackOnRoute(CallbackOnRoute callback) {
        this.routeCallbackList.add(callback);
    }

    protected Serializable invokeCallbacks(ID target, int tag, Serializable[] args, IDAddressPair lastHop, boolean onResponsibleNode) {
        MessagingAddress lastHopAddress = (lastHop != null ? lastHop.getAddress() : null);
        logger.log(Level.INFO, "Invoke " + routeCallbackList.size() + " callbacks. lastHop: " + lastHopAddress + ", onResponsibleNode: " + onResponsibleNode + ", on " + selfIDAddressPair.getAddress());
        Serializable result = null;
        synchronized (this.routeCallbackList) {
            for (CallbackOnRoute cb : this.routeCallbackList) {
                result = cb.process(target, tag, args, lastHop, onResponsibleNode);
            }
        }
        return result;
    }

    private List<CallbackOnNodeFailure> failureCallbackList = Collections.synchronizedList(new ArrayList<CallbackOnNodeFailure>(1));

    public void addCallbackOnNodeFailure(CallbackOnNodeFailure callback) {
        this.failureCallbackList.add(callback);
    }

    protected void fail(IDAddressPair failedNode) {
        if (this.algorithm != null) {
            this.algorithm.fail(failedNode);
        }
        for (CallbackOnNodeFailure cb : failureCallbackList) {
            cb.fail(failedNode);
        }
    }

    /**
	 * Prepare message handlers.
	 */
    private void prepareHandlers() {
        MessageHandler handler;
        handler = new MessageHandler() {

            public Message process(Message msg) {
                return new AckMessage();
            }
        };
        this.addMessageHandler(PingMessage.class, handler);
        handler = new MessageHandler() {

            public Message process(Message msg) {
                int num = ((ReqNeighborsMessage) msg).num;
                MessagingAddress reqSource = ((IDAddressPair) msg.getSource()).getAddress();
                msgProvider.setMessagingCollectorAddress(reqSource);
                IDAddressPair[] neighbors = algorithm.responsibleNodeCandidates(selfIDAddressPair.getID(), num);
                return new RepNeighborsMessage(neighbors);
            }
        };
        this.addMessageHandler(ReqNeighborsMessage.class, handler);
    }

    private class RoutingDrvMessageHandler implements MessageHandler {

        /**
		 * This method implements {@link MessageHandler#process(Message) MessageHandler#process()}.
		 */
        public Message process(Message msg) {
            Message ret = null;
            List<MessageHandler> handlerList = AbstractRoutingDriver.this.handlerTable.get(msg.getClass());
            if (handlerList != null) {
                for (MessageHandler handler : handlerList) {
                    try {
                        ret = handler.process(msg);
                    } catch (Throwable e) {
                        logger.log(Level.SEVERE, "A MessageHandler#process() threw an Exception.", e);
                    }
                }
            }
            return ret;
        }
    }

    private final class ExtRoutingDrvMessageHandler extends RoutingDrvMessageHandler implements ExtendedMessageHandler {

        public void postProcess(Message msg) {
            List<ExtendedMessageHandler> handlerList = AbstractRoutingDriver.this.extendedHandlerTable.get(msg.getClass());
            if (handlerList != null) {
                for (ExtendedMessageHandler handler : handlerList) {
                    try {
                        handler.postProcess(msg);
                    } catch (Throwable e) {
                        logger.log(Level.SEVERE, "A MessageHandler#postProcess() threw an Exception.", e);
                    }
                }
            }
            if (AbstractRoutingDriver.this.algorithm != null) {
                IDAddressPair src = (IDAddressPair) msg.getSource();
                if (src.getID() != null && src.getAddress() != null) {
                    AbstractRoutingDriver.this.algorithm.touch(src);
                }
            }
        }
    }
}
