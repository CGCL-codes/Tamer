package org.vizzini.example.geneticprogramming.forger;

import java.util.logging.Logger;
import ec.EvolutionState;
import ec.util.Parameter;

/**
 * @author  Jeffrey M. Thompson
 */
public class EvolutionStateUtilities {

    /** Logger. */
    protected static final Logger LOGGER = Logger.getLogger(EvolutionStateUtilities.class.getName());

    /** Base parameter name for Vizzini. */
    public static final String P_BASE = "vizzini";

    /** Parameter name for image width. */
    public static final String P_WIDTH = "width";

    /** Parameter name for image height. */
    public static final String P_HEIGHT = "height";

    /**
     * @param   state  Evolution state.
     *
     * @return  the image height from the given parameter.
     */
    public int getImageHeight(EvolutionState state) {
        Parameter base = new Parameter(P_BASE);
        Parameter p = base.push(P_HEIGHT);
        return state.parameters.getInt(p, base.push(P_HEIGHT));
    }

    /**
     * @param   state  Evolution state.
     *
     * @return  the image width from the given parameter.
     */
    public int getImageWidth(EvolutionState state) {
        Parameter base = new Parameter(P_BASE);
        Parameter p = base.push(P_WIDTH);
        return state.parameters.getInt(p, base.push(P_WIDTH));
    }

    /**
     * @param  state   Evolution state.
     * @param  height  Image height.
     */
    public void setImageHeight(EvolutionState state, int height) {
        Parameter base = new Parameter(P_BASE);
        Parameter p = base.push(P_HEIGHT);
        state.parameters.set(p, String.valueOf(height));
    }

    /**
     * @param  state  Evolution state.
     * @param  width  Image width.
     */
    public void setImageWidth(EvolutionState state, int width) {
        Parameter base = new Parameter(P_BASE);
        Parameter p = base.push(P_WIDTH);
        state.parameters.set(p, String.valueOf(width));
    }
}
