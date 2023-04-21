package net.sf.istcontract.acs.protocol;

import net.sf.istcontract.aws.communication.performative.Agree;
import net.sf.istcontract.aws.communication.performative.Failure;
import net.sf.istcontract.aws.communication.performative.Inform;
import net.sf.istcontract.aws.communication.performative.Refuse;

public class ContractRequestProtocolInitiator extends ContractStorerInitiatorCommon {

    /**
	 * This method should be overridden by a subclass to process the messages
	 */
    protected void handleInform(Inform perf) {
    }

    /**
	 * This method should be overridden by a subclass to process the messages
	 */
    protected void handleRefuse(Refuse perf) {
    }

    /**
	 * This method should be overridden by a subclass to process the messages
	 */
    protected void handleAgree(Agree perf) {
    }

    /**
	 * This method should be overridden by a subclass to process the messages
	 */
    protected void handleFailure(Failure failure) {
    }
}
