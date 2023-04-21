package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Relay.Direction;

public class Minibot {

    private Relay extend = null;

    private Relay release = null;

    private long nextStageStartTime = 0;

    private boolean extended = false;

    private boolean released = false;

    public Minibot() {
        extend = new Relay(Ports.kDigitalModule, Ports.kMinibotExtend, Direction.kForward);
        release = new Relay(Ports.kDigitalModule, Ports.kMinibotDeploy, Direction.kForward);
        release.set(Relay.Value.kOff);
        extend.set(Relay.Value.kOff);
    }

    /** @brief Called periodically to deploy the minibot.
	 *
	 * The deployment process uses the following stages:
	 * 1) Extend the slide
	 * 2) Release the minibot
	 * 3) Retract the slide
	 *
	 * Currently stages 1 and 2 and manually controlled, and stage 3 happens
	 * automatically after stage 2 is completed.  Note stage 2 cannot commence
	 * until after stage 1.
	 *
	 * @param now
	 * @param deployExtend
	 * @param deployRelease
	 */
    public void deploy(long now, boolean extendSlide, boolean releaseMinbot) {
        if (extendSlide) {
            extend.set(Relay.Value.kOn);
            RobotTemplate.LogMessage("Extending Minibot");
            nextStageStartTime = now + Configuration.kMinibotReleaseDelay;
            extended = true;
        }
        if (releaseMinbot && extended && now >= nextStageStartTime) {
            release.set(Relay.Value.kOn);
            RobotTemplate.LogMessage("Releasing Minibot");
            nextStageStartTime = now + Configuration.kMinibotRetractDelay;
            released = true;
        }
        if (released && now >= nextStageStartTime) {
            extend.set(Relay.Value.kOff);
            extended = false;
        }
    }

    /** @brief Called to reset the deployment system
	 *
	 */
    public void init() {
        release.set(Relay.Value.kOff);
        extend.set(Relay.Value.kOff);
        released = false;
        extended = false;
        nextStageStartTime = 0;
    }
}
