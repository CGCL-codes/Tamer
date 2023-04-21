package emap;

import java.util.ArrayList;

public class HumanController extends Controller {

    private ArrayList<InputKey> mycontrols;

    public HumanController(Controllable newToCon) {
        super(newToCon);
    }

    public void doControls() {
        boolean moving = false;
        if (mycontrols.get(0).is()) {
            toCon.moveUp();
            moving = true;
        }
        if (mycontrols.get(1).is()) {
            toCon.moveLeft();
            moving = true;
        }
        if (mycontrols.get(2).is()) {
            toCon.moveDown();
            moving = true;
        }
        if (mycontrols.get(3).is()) {
            toCon.moveRight();
            moving = true;
        }
        if (moving == false) toCon.idle();
    }

    public void setControls(ArrayList<InputKey> newControls) {
        mycontrols = newControls;
    }
}
