package org.perfectday.logicengine.brain.simple;

import org.perfectday.logicengine.brain.SelectedActionFunction;
import org.perfectday.logicengine.model.minis.Mini;
import org.perfectday.logicengine.model.minis.action.ActionMini;

/**
 * Dummy
 * @author Miguel Angel Lopez Montellano ( alakat@gmail.com )
 */
public class SimpleActionSelectedFunction implements SelectedActionFunction {

    public double getCualityOfAction(ActionMini actionMini, Mini mini) {
        return mini.getPrimaryAction().equals(actionMini) ? Double.MAX_VALUE : Double.MIN_VALUE;
    }
}
