package org.intranet.elevator.model.operate;

import org.intranet.elevator.model.Floor;
import org.intranet.elevator.model.CarRequestPanel;
import org.intranet.elevator.model.operate.controller.Controller;
import org.intranet.elevator.model.operate.controller.Direction;

/**
* Assigns a request from the CarRequestPanel for this Floor to the best
* CarController.
* @author Neil McKellar and Chris Dailey
*/
public final class CarRequest implements CarRequestPanel.ButtonListener {

    private final Controller megaController;

    private final Floor floor;

    public CarRequest(Controller m, Floor floor) {
        super();
        megaController = m;
        this.floor = floor;
    }

    public void pressedUp() {
        megaController.requestCar(floor, Direction.UP);
    }

    public void pressedDown() {
        megaController.requestCar(floor, Direction.DOWN);
    }
}
