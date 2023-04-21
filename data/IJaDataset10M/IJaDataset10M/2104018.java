package sourismobile.server.module;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import sourismobile.server.core.Module;

public class SourisMobileMousse extends Module {

    Robot robot;

    PointerInfo pointer;

    public SourisMobileMousse() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public boolean move(int x_move, int y_move) {
        pointer = MouseInfo.getPointerInfo();
        int x_init = pointer.getLocation().x;
        int y_init = pointer.getLocation().y;
        robot.mouseMove(x_init + x_move, y_init + y_move);
        return true;
    }

    public boolean action(int param) {
        switch(param) {
            case 1:
                return move(0, -10);
            case 5:
                return move(10, 0);
            case 6:
                return move(0, 10);
            case 2:
                return move(-10, 0);
        }
        return false;
    }
}
