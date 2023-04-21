package org.jmove.zui.graph.layout;

import org.jmove.core.Thing;
import org.jmove.zui.core.InteractionContext;
import org.jmove.zui.graph.Layout;
import org.jmove.zui.graph.Layoutable;
import org.jmove.zui.graph.LinkableNode;
import org.jmove.zui.graph.NodeColoring;

/**
 * Base class for layouts.
 *
 * @author Michael Juergens
 */
public abstract class AbstractLayoutBase implements Layout {

    private InteractionContext myInteractionContext = null;

    public AbstractLayoutBase(InteractionContext aInteractionContext) {
        myInteractionContext = aInteractionContext;
    }

    public InteractionContext getInteractionContext() {
        return myInteractionContext;
    }

    protected void color(Layoutable aLayoutable, LinkableNode aNode) {
        coloringFor(aLayoutable.getThingForNode(aNode)).color(aLayoutable.getThingForNode(aNode), aNode);
    }

    protected NodeColoring coloringFor(Thing aThing) {
        return (NodeColoring) getInteractionContext().broker().behaviorFor(NodeColoring.class, aThing);
    }
}
