package org.personalsmartspace.lm.dianne.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.lm.dianne.impl.DIANNEManager.OutcomeListener;
import org.personalsmartspace.lm.dianne.impl.networkElements.groups.OutcomeGroup;
import org.personalsmartspace.lm.dianne.impl.networkElements.nodes.OutcomeNode;
import org.personalsmartspace.lm.dianne.impl.runtime.InputBuffer;
import org.personalsmartspace.lm.dianne.impl.runtime.Network;
import org.personalsmartspace.lm.dianne.impl.runtime.ReinforcementCycle;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.pm.prefmodel.api.platform.IOutcome;
import org.personalsmartspace.pm.prefmodel.api.platform.PreferenceOutcome;
import org.personalsmartspace.pm.prefmodel.api.pss3p.Action;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

public class DIANNE implements Runnable {

    public Network network;

    public InputBuffer buffer;

    private ReinforcementCycle cycle;

    PSSLog logging = new PSSLog(this);

    private Thread dianneRunner;

    private Object pauseMonitor = new Object();

    private boolean paused = false;

    public DIANNE(IDigitalPersonalIdentifier dpi, OutcomeListener listener) {
        network = new Network(dpi, listener);
        buffer = new InputBuffer();
        cycle = new ReinforcementCycle(network, buffer);
        dianneRunner = new Thread(this);
        dianneRunner.setName("Dianne runner");
        dianneRunner.start();
    }

    /**
     * Interface method to retrieve current preference outcome
     */
    public IOutcome getOutcome(IServiceIdentifier serviceId, String prefName) {
        IOutcome outcome = null;
        OutcomeGroup outcomeGroup = network.getOutcomeGroup(serviceId, prefName);
        if (outcomeGroup != null && outcomeGroup.inputAvailable()) {
            OutcomeNode activeNode = (OutcomeNode) outcomeGroup.getActiveNode();
            outcome = new PreferenceOutcome(activeNode.getGroupName(), activeNode.getNodeName());
        }
        return outcome;
    }

    public List<ICtxIdentifier> getConditions(IOutcome outcome) {
        List<ICtxIdentifier> conditions = null;
        IServiceIdentifier serviceId = outcome.getServiceID();
        String parameterName = outcome.getparameterName();
        return conditions;
    }

    public void contextUpdate(Action update) {
        logging.debug("New context update received: " + update.toString() + "...adding to buffer");
        buffer.addContextUpdate(update);
    }

    public void outcomeUpdate(Action update) {
        logging.debug("New outcome update received: " + update.toString() + "...adding to buffer");
        buffer.addOutcomeUpdate(update);
    }

    public void initialiseContext(Map<String, String> contextValues) {
        Iterator<String> contextValues_it = contextValues.keySet().iterator();
        while (contextValues_it.hasNext()) {
            String attrType = (String) contextValues_it.next();
            String value = (String) contextValues.get(attrType);
            this.contextUpdate(new Action(attrType, value));
        }
    }

    public void run() {
        while (true) {
            cycle.execute();
            synchronized (pauseMonitor) {
                if (paused) {
                    try {
                        pauseMonitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void play() {
        synchronized (pauseMonitor) {
            paused = false;
            pauseMonitor.notify();
        }
    }

    public void pause() {
        synchronized (pauseMonitor) {
            paused = true;
        }
    }

    public Network getNetwork() {
        return network;
    }
}
