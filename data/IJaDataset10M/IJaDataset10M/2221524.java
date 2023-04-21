package com.jrandrews.statemachine.demo;

import com.jrandrews.statemachine.BasicAction;
import com.jrandrews.statemachine.Context;

/**
 *
 * @author JAndrews
 */
public class SnoozeAction extends BasicAction {

    public String execute(Context ctx) {
        super.execute(ctx);
        Model m = Model.getInstance();
        m.setAlarm(true);
        return null;
    }
}
