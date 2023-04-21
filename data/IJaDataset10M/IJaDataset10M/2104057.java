package sente;

import java.awt.*;

public class TwoDGameUIFactory extends GameUIFactory {

    public TwoDGameUIFactory(RobotArenaUI e) {
        super(e);
    }

    public GameElementUI getElementUI(GameElement e) {
        GameElementUI retval;
        if (e instanceof Stone) {
            retval = new TwoDStoneUI(theArena);
            Stone s = (Stone) e;
            s.addListener((TwoDStoneUI) retval);
        } else {
            retval = new TwoDRobotUI(theArena);
            Robot s = (Robot) e;
            s.addListener((TwoDRobotUI) retval);
        }
        return retval;
    }
}
