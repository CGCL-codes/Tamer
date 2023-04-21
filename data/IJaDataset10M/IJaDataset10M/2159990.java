package org.perfectday.logicengine.brain;

import org.perfectday.logicengine.model.minis.Mini;
import org.perfectday.logicengine.model.minis.action.ActionMini;

/**
 *
 * @author Miguel Angel Lopez Montellano ( alakat@gmail.com )
 */
public interface SelectedActionFunction {

    public double getCualityOfAction(ActionMini actionMini, Mini mini);
}
