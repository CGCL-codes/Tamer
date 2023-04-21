package org.jmik.asterisk.model.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.MonitorAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.jmik.asterisk.model.CallListener;
import org.jmik.asterisk.model.Provider;
import org.jmik.asterisk.model.ProviderListener;

/**
 * This class represents the implementation of a Provider.
 * It holds instances of active (attached) Call.
 * 
 * @author Michele La Porta
 *
 */
public class AsteriskProvider implements Provider, ManagerEventListener, CallListener {

    private static Logger logger = Logger.getLogger(AsteriskProvider.class);

    private Set<Call> attachedCalls;

    private Set<CallCostruction> callConstrutions;

    private List<ProviderListener> listeners;

    private ManagerConnection managerConnection;

    /**
     * 
     * @param managerConnection
     */
    public AsteriskProvider(ManagerConnection managerConnection) {
        this.managerConnection = managerConnection;
        attachedCalls = new LinkedHashSet<Call>();
        callConstrutions = new LinkedHashSet<CallCostruction>();
        listeners = new ArrayList<ProviderListener>();
    }

    public void onManagerEvent(ManagerEvent managerEvent) {
        if (logger.isDebugEnabled()) logger.debug("RECEIVED " + managerEvent);
        boolean processed = false;
        for (Call call : this.attachedCalls) {
            processed = call.process(managerEvent);
            if (logger.isDebugEnabled()) logger.debug("" + call.getClass().getSimpleName() + "[state=" + call.getState() + "] processEvent " + managerEvent.getClass().getSimpleName() + " processed " + processed);
            if (processed) break;
        }
        if (!processed) processEvent(managerEvent);
    }

    /**
	 * 
	 * @param managerEvent
	 */
    private void processEvent(ManagerEvent event) {
        boolean processed = false;
        for (CallCostruction callCostruction : callConstrutions) {
            processed = callCostruction.processEvent(event);
            if (logger.isDebugEnabled()) logger.debug("processEvent " + event.getClass().getSimpleName() + " cc state " + callCostruction.getWaitState() + " processed " + processed);
            if (processed) break;
        }
        if (!processed) {
            if (event instanceof NewChannelEvent) {
                NewChannelEvent newChannelEvent = (NewChannelEvent) event;
                if ("Down".equals(newChannelEvent.getState())) {
                    CallCostruction callCostruction = new CallCostruction(this, newChannelEvent);
                    callConstrutions.add(callCostruction);
                }
            } else {
                if (logger.isDebugEnabled()) logger.debug("NOT processed " + event);
            }
        }
    }

    public void removeCallConstruction(CallCostruction callConstruction) {
        callConstrutions.remove(callConstruction);
        if (logger.isDebugEnabled()) logger.debug("removeCallConstruction " + callConstruction);
    }

    public void addListener(ProviderListener providerListener) {
        listeners.add(providerListener);
        if (logger.isDebugEnabled()) logger.debug("addListener " + listeners);
    }

    public void removeListener(ProviderListener providerListener) {
        listeners.remove(providerListener);
        if (logger.isDebugEnabled()) logger.debug("removeListener " + listeners);
    }

    public void attachCall(Call call) {
        this.attachedCalls.add(call);
        for (ProviderListener providerListener : listeners) {
            providerListener.callAttached(call);
            if (logger.isDebugEnabled()) logger.debug("attachCall notify " + providerListener);
        }
    }

    public void detachCall(Call call) {
        this.attachedCalls.remove(call);
        for (ProviderListener providerListener : listeners) {
            providerListener.callDetached(call);
            if (logger.isDebugEnabled()) logger.debug("detachCall notify " + providerListener);
        }
    }

    public Set<Call> getAttachedCalls() {
        return this.attachedCalls;
    }

    public void stateChanged(int oldState, Call call) {
        if (call.getState() == Call.INVALID_STATE) {
            if (logger.isDebugEnabled()) logger.debug("call detached: " + call);
            attachedCalls.remove(call);
            for (ProviderListener listener : listeners) {
                listener.callDetached(call);
            }
        }
    }

    public void channelAdded(ConferenceCall conferenceCall, Channel channel) {
        if (logger.isDebugEnabled()) logger.debug("channelAdded: " + channel);
    }

    public void channelRemoved(ConferenceCall conferenceCall, Channel channel) {
        if (logger.isDebugEnabled()) logger.debug("channelRemoved: " + channel);
    }

    public void drop(Call call) {
        if (call instanceof SinglePartyCall) {
            SinglePartyCall spc = (SinglePartyCall) call;
            HangupAction hangupAction = new HangupAction(spc.getChannel().getDescriptor().getId());
            try {
                managerConnection.sendAction(hangupAction);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (call instanceof TwoPartiesCall) {
            TwoPartiesCall tpc = (TwoPartiesCall) call;
            HangupAction hangupAction = new HangupAction(tpc.getCallerChannel().getDescriptor().getId());
            try {
                managerConnection.sendAction(hangupAction);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (call instanceof ConferenceCall) {
            ConferenceCall cc = (ConferenceCall) call;
            for (Channel channel : cc.getChannels()) {
                HangupAction hangupAction = new HangupAction(channel.getDescriptor().getId());
                try {
                    managerConnection.sendAction(hangupAction);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void monitor(Call call) {
        if (logger.isDebugEnabled()) logger.debug("monitor " + call);
        String monitoredChannel = null;
        if (call instanceof SinglePartyCall) {
            SinglePartyCall spc = (SinglePartyCall) call;
            monitoredChannel = spc.getChannel().getDescriptor().getId();
            if (logger.isDebugEnabled()) logger.debug("spc monitoredChannel " + monitoredChannel);
        } else if (call instanceof TwoPartiesCall) {
            TwoPartiesCall tpc = (TwoPartiesCall) call;
            monitoredChannel = tpc.getCallerChannel().getDescriptor().getId();
            if (logger.isDebugEnabled()) logger.debug("tpc monitoredChannel " + monitoredChannel);
        } else if (call instanceof ConferenceCall) {
            ConferenceCall cc = (ConferenceCall) call;
            for (Channel channel : cc.getChannels()) {
                String channelId = channel.getDescriptor().getId();
                if (logger.isDebugEnabled()) logger.debug("cc channel " + channelId);
                monitoredChannel = channelId;
            }
        }
        if (logger.isDebugEnabled()) logger.debug("monitoredChannel:" + monitoredChannel);
        StringBuffer fileName = new StringBuffer();
        if (call instanceof SinglePartyCall) {
            SinglePartyCall spc = (SinglePartyCall) call;
            fileName.append("singleparty_").append(spc.getId());
        } else if (call instanceof TwoPartiesCall) {
            TwoPartiesCall tpc = (TwoPartiesCall) call;
            fileName.append("twoparties_").append(tpc.getId());
        } else if (call instanceof ConferenceCall) {
            ConferenceCall cc = (ConferenceCall) call;
            fileName.append("conference_").append(cc.getId());
        }
        MonitorAction monitorAction = new MonitorAction(monitoredChannel, "/tmp/" + fileName.toString());
        try {
            managerConnection.sendAction(monitorAction);
            if (logger.isDebugEnabled()) logger.debug("send " + monitorAction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<CallCostruction> getCallConstrutions() {
        return callConstrutions;
    }

    public List<ProviderListener> getListeners() {
        return listeners;
    }

    public ManagerConnection getManagerConnection() {
        return managerConnection;
    }
}
