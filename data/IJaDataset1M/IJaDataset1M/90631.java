package com.hypermine.ultrasonic.components;

import com.hypermine.ultrasonic.commons.*;

/**
 * A project is a specialized {@link Track} that has an additional
 * beats-per-minute (BPM) controller.
 * 
 * @author wschwitzer
 * @author $Author: wschwitzer $
 * @version $Rev: 150 $
 * @levd.rating GREEN Rev: 150
 */
public class Project extends Track {

    /** The beats-per-minute controller. */
    private final FloatController bpm = new FloatController("BPM", 120, 0, 1000);

    /**
	 * Creates a new project with parent set to <code>null</code>.
	 */
    public Project() {
        super(null);
    }

    /** {@inheritDoc} */
    @Override
    public Controller[] getControllers() {
        return ArrayUtils.append(super.getControllers(), bpm);
    }

    /** Returns the beats-per-minute controller. */
    public FloatController getBpm() {
        return bpm;
    }
}
