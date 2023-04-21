package br.furb.inf.tcc.tankcoders.scene.tank.action;

import br.furb.inf.tcc.tankcoders.scene.tank.ITank;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

/**
 * Germano Fronza
 */
public class ActionDirection implements InputActionInterface {

    ITank tank;

    String tankName;

    int direction;

    public ActionDirection(ITank tank, int direction) {
        this.tank = tank;
        this.tankName = tank.getTankName();
        this.direction = direction;
    }

    public void performAction(final InputActionEvent e) {
        if (e.getTriggerPressed()) {
            tank.turnWheel(direction);
        } else {
            tank.stopTurningWheel();
        }
    }
}
