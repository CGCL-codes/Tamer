package uchicago.src.sim.engine.gui.components;

import java.awt.event.ActionListener;

/**
 * @author wes maciorowski
 *
 */
public abstract class GUIControllerAbstract implements ActionListener {

    public static final String INPUT_OUTPUT_CHANGED = "INPUT_OUTPUT_CHANGED";

    public static final String CHANGED_OUTPUT_LOCATION = "CHANGED_OUTPUT_LOCATION";

    public static final String EXIT = "EXIT";

    public static final String RUN_SIMULATION = "RUN_SIMULATION";
}
