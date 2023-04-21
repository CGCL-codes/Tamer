package com.jmbaai.bombsight.sight;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Vec4;
import gumbo.model.state.Animatable;
import java.awt.Frame;
import com.jmbaai.bombsight.tech.math.RotationHPR;

/**
 * Controller (as in MVC) for a bombsight model.
 * @author jonb
 */
public interface SightControl extends Animatable {

    /**
	 * Called by the client to build the control GUI after the host window has
	 * been built.
	 * @param host Shared exposed host window. None if null.
	 */
    public void buildGui(Frame host);

    /**
	 * Starts a new mission, with no wind.
	 */
    public void startWithoutWind();

    /**
	 * Starts a new mission, with wind.
	 */
    public void startWithWind();

    /**
	 * Retry the same mission.
	 */
    public void tryAgain(Angle heading, Vec4 attitude);

    /**
	 * Returns true if the bomber should drop a bomb. Simulate the output from
	 * an intervalometer triggered by the bombsight. Should be called each
	 * frame.
	 * @return The state.
	 */
    public boolean isDropBomb();

    /**
	 * Returns delta flight parameters for flying the plane. Simulates the
	 * output from an autopilot controlled by the bombsight. Should be called
	 * each frame.
	 * @return The state. Null if auto-pilot is disengaged.
	 */
    public RotationHPR getAutoFly();
}
