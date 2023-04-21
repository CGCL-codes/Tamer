package org.vizzini.example.ant;

import org.vizzini.VizziniRuntimeException;
import org.vizzini.ai.geneticalgorithm.IContext;

/**
 * Provides a right terminal for the ant example. Evaluating this terminal has
 * the side effect of turning the ant right on the grid.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.3
 */
public class RightTerminal extends AbstractAntTerminal {

    /**
     * Construct this object.
     *
     * @since  v0.3
     */
    public RightTerminal() {
        super("Right");
    }

    /**
     * Evaluate this function.
     *
     * @param   context0  Context in which to evaluate.
     *
     * @return  the evaluation.
     *
     * @since   v0.3
     */
    public Object evaluate(IContext context0) {
        AntContext context = (AntContext) context0;
        String heading = context.getHeading();
        String newHeading;
        if (Ant.HEADING_EAST.equals(heading)) {
            newHeading = Ant.HEADING_SOUTH;
        } else if (Ant.HEADING_SOUTH.equals(heading)) {
            newHeading = Ant.HEADING_WEST;
        } else if (Ant.HEADING_WEST.equals(heading)) {
            newHeading = Ant.HEADING_NORTH;
        } else if (Ant.HEADING_NORTH.equals(heading)) {
            newHeading = Ant.HEADING_EAST;
        } else {
            throw new VizziniRuntimeException("Unknown heading: " + heading);
        }
        context.setHeading(newHeading);
        context.incrementTime();
        return new Integer(1);
    }
}
