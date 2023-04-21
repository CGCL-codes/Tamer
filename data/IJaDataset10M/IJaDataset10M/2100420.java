package org.desimeter.statemachine.execution;

import org.desimeter.statemachine.core.Transition;

/**
 * @auther: sandeep dixit.<a href="mailto:sandeep.dixit@ugs.com">sandeep.dixit@ugs.com</a>
 * @Date: 08-Apr-2009
 * @Time: 17:53:47
 */
public interface IActionPerformer {

    public boolean performActions(Transition transition, EventExecutionContext executionContext);
}
