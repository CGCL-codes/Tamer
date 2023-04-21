package org.archive.modules.deciderules;

import org.archive.modules.ProcessorURI;
import org.archive.state.KeyManager;

public class AcceptDecideRule extends DecideRule {

    /**
     * 
     */
    private static final long serialVersionUID = 3L;

    static {
        KeyManager.addKeys(AcceptDecideRule.class);
    }

    @Override
    protected DecideResult innerDecide(ProcessorURI uri) {
        return DecideResult.ACCEPT;
    }

    @Override
    public DecideResult onlyDecision(ProcessorURI uri) {
        return DecideResult.ACCEPT;
    }
}
