package org.perfectday.logicengine.brain.simple;

import org.perfectday.logicengine.brain.AbstractActionBrain;
import org.perfectday.logicengine.brain.AbstractGameBrain;
import org.perfectday.logicengine.brain.AbstractMotionBrain;

/**
 *
 * @author Miguel Angel Lopez Montellano ( alakat@gmail.com )
 */
public class SimpleGameBrain extends AbstractGameBrain {

    public SimpleGameBrain(AbstractMotionBrain motionBrain, AbstractActionBrain actionBrain) {
        super(motionBrain, actionBrain);
    }

    @Override
    public void setBestFunction() {
        if (this.actionBrain.getActionFunction() == null) this.actionBrain.setActionFunction(new SimpleActionSelectedFunction());
        if (this.motionBrain.getMovedFunction() == null) this.motionBrain.setMovedFunction(new SimpleMovedFunction());
    }
}
