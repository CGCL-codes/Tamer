package org.baljinder.presenter.testing.support;

import org.baljinder.presenter.dataacess.ITransitionAction;

/**
 * @author Baljinder Randhawa
 *
 */
public class DoNothingTransitionAction implements ITransitionAction {

    public ActionResult performTransitionAction(TransitionContext transitionContext) {
        return ActionResult.DONOTHING;
    }
}
