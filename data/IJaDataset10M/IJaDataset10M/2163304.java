package ail.semantics.operationalrules;

import ail.semantics.AILAgent;
import ail.semantics.Intention;
import ail.syntax.Structure;
import gov.nasa.jpf.annotation.FilterField;

/**
 * Add a belief to the agent.
 * 
 * @author lad
 *
 */
public class HandleAddContent extends HandleContent {

    @FilterField
    private static final String name = "Handle Add Content";

    public String getName() {
        return name;
    }

    public boolean checkPreconditions(AILAgent a) {
        return (super.checkPreconditions(a) && topdeed.isAddition());
    }

    public void apply(AILAgent a) {
        i.tlI();
        thetahd.compose(thetab);
        i.compose(thetahd);
        Structure content_term = (Structure) topdeed.getContent();
        content_term.apply(thetahd);
        String st = content_term.toString();
        a.addContent(st);
        for (Intention i : a.getIntentions()) {
            i.unsuspend();
        }
    }
}
