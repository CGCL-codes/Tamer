package com.ragstorooks.samples.scenarios;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.bt.aloha.callleg.OutboundCallLegBean;
import com.bt.aloha.callleg.OutboundCallLegListener;
import com.bt.aloha.callleg.event.CallLegAlertingEvent;
import com.bt.aloha.callleg.event.CallLegConnectedEvent;
import com.bt.aloha.callleg.event.CallLegConnectionFailedEvent;
import com.bt.aloha.callleg.event.CallLegDisconnectedEvent;
import com.bt.aloha.callleg.event.CallLegRefreshCompletedEvent;
import com.bt.aloha.callleg.event.CallLegTerminatedEvent;
import com.bt.aloha.callleg.event.CallLegTerminationFailedEvent;
import com.bt.aloha.callleg.event.ReceivedCallLegRefreshEvent;
import com.bt.aloha.dialog.state.TerminationCause;
import com.ragstorooks.testrr.ScenarioBase;

public class BusyCallLeg extends ScenarioBase implements OutboundCallLegListener {

    private Map<String, String> scenarios = new ConcurrentHashMap<String, String>();

    private Object lock = new Object();

    private OutboundCallLegBean outboundCallLegBean;

    @Override
    public void run(String scenarioId) {
        String callLegId = outboundCallLegBean.createCallLeg(URI.create("sip:app"), URI.create("sip:busy@127.0.0.1"));
        outboundCallLegBean.connectCallLeg(callLegId);
        scenarios.put(callLegId, scenarioId);
    }

    public void onCallLegAlerting(CallLegAlertingEvent arg0) {
    }

    public void onCallLegConnected(CallLegConnectedEvent arg0) {
        String callLegId = arg0.getId();
        scenarios.remove(callLegId);
        getScenarioListener().scenarioFailure(callLegId, "Call Leg Connected Event received");
    }

    public void onCallLegConnectionFailed(CallLegConnectionFailedEvent arg0) {
        String callLegId = arg0.getId();
        String scenarioId = scenarios.remove(callLegId);
        if (scenarioId != null) {
            if (arg0.getTerminationCause().equals(TerminationCause.RemotePartyBusy)) getScenarioListener().scenarioSuccess(scenarioId); else getScenarioListener().scenarioFailure(scenarioId, String.format("Termination cause: %s", arg0.getTerminationCause()));
        }
    }

    public void onCallLegDisconnected(CallLegDisconnectedEvent arg0) {
    }

    public void onCallLegRefreshCompleted(CallLegRefreshCompletedEvent arg0) {
    }

    public void onCallLegTerminated(CallLegTerminatedEvent arg0) {
    }

    public void onCallLegTerminationFailed(CallLegTerminationFailedEvent arg0) {
    }

    public void onReceivedCallLegRefresh(ReceivedCallLegRefreshEvent arg0) {
    }

    public void setOutboundCallLegBean(OutboundCallLegBean outboundCallLegBean) {
        this.outboundCallLegBean = outboundCallLegBean;
        this.outboundCallLegBean.addOutboundCallLegListener(this);
    }
}
