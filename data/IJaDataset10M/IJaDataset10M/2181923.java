package ail.semantics.operationalrules;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import ail.others.AILEnv;
import ail.semantics.AILAgent;
import ail.semantics.Intention;
import ail.semantics.Message;
import ail.syntax.Literal;
import ail.syntax.Structure;
import ail.syntax.Event;
import ail.syntax.GBelief;
import gov.nasa.jpf.annotation.FilterField;
import gov.nasa.jpf.jvm.Verify;

/**
 * Perception rule.  Gets a list of all literals the agent can perceive from the
 * environment.  It all gets a list of things the agent believes it can perceive
 * from the agent and compares the two.  Any discrepancies applied directly to the
 * belief base.  Also gets messages from the environment and adds to inbox.
 * 
 * @author lad
 *
 */
public class DirectPerception implements OSRule {

    @FilterField
    private static final String name = "Direct Perception";

    public String getName() {
        return name;
    }

    public boolean checkPreconditions(AILAgent a) {
        return true;
    }

    public void apply(AILAgent a) {
        AILEnv env = a.getEnv();
        Verify.beginAtomic();
        List<Literal> percepts = env.getPercepts(a.getAgName(), true);
        List<Message> messages = env.getMessages(a.getAgName());
        Verify.endAtomic();
        if (percepts != null) {
            Iterator<Literal> gbi = a.getBB().getPercepts();
            while (gbi.hasNext()) {
                Literal l = gbi.next();
                if (!percepts.contains(l)) {
                    a.delBel(l);
                    a.tellawake();
                } else {
                    percepts.remove(l);
                }
            }
            boolean additions = false;
            for (Literal l : percepts) {
                Literal k = (Literal) l.clone();
                additions = true;
                a.addBel(k, AILAgent.refertopercept());
                a.tellawake();
            }
            if (additions) {
                for (Intention i : a.getIntentions()) {
                    i.unsuspend();
                }
            }
        }
        List<Message> msglist = new LinkedList<Message>();
        msglist.addAll(messages);
        a.newMessages(msglist);
    }
}
