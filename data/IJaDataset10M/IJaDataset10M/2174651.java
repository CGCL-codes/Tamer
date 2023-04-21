package saapl;

import ail.semantics.AILAgent;
import ail.semantics.Message;
import ail.semantics.operationalrules.HandleActionwProblem;
import ail.syntax.Action;
import ail.syntax.SendAction;
import gov.nasa.jpf.annotation.FilterField;
import gov.nasa.jpf.jvm.Verify;

/**
 * Sending a message in SAAPL.
 * 
 * @author lad
 *
 */
public class SendMessage extends HandleActionwProblem {

    @FilterField
    private static final String name = "Send Message";

    public String getName() {
        return name;
    }

    public void apply(AILAgent a) {
        Action act = (Action) topdeed.getTerm();
        SAAPLAgent a1 = (SAAPLAgent) a;
        Verify.beginAtomic();
        SendAction send = (SendAction) act;
        Message msg = send.getMessage(a1.getAgName());
        Verify.endAtomic();
        act = send;
        a1.getSAAPLEnv().addtoQueue(msg);
        i.tlI();
    }
}
