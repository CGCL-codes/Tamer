package com.michaelzanussi.genalyze.genesys.statemachine;

import com.michaelzanussi.genalyze.loggers.Genesys;

/**
 * T-Server header state. Implements marker interface, <code>Header</code>.
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (28 September 2006) 
 */
public class TServerHeaderState extends AbstractTServerState implements Header {

    private boolean firstScan = true;

    /**
	 * Single-arg constructor.
	 * 
	 * @param log the log.
	 */
    public TServerHeaderState(Genesys log) {
        this.log = log;
    }

    public void exec(String line) throws StateException {
        if (isEmpty(line)) {
            log.dump(line, "skipping");
            if (firstScan) {
                log.getStateMachine().setState(log.getStateMachine().getTServerPostHeaderState());
                firstScan = false;
            } else {
                log.getStateMachine().setState(log.getStateMachine().getTServerMessageState());
            }
        } else if (isMessage(line)) {
            throw new StateException("Message encountered waiting for end of header.");
        } else if (isAttribute(line)) {
            throw new StateException("Attribute encountered waiting for end of header.");
        } else if (isSubattribute(line)) {
            throw new StateException("Subattribute encountered waiting for end of header.");
        } else {
            log.dump(line, "skipping");
        }
    }
}
