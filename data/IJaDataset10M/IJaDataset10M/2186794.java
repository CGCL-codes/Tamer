package GameThings;

import RootP.Level;

public class Wizard extends Figure {

    public Wizard(int X, int Y, String ROUTE, boolean SAME_WAY_BACK) {
        super(X, Y, 30, 65, "wizard", Level.getPicArray(new String[] { "gameThing_images/wizard_0_0.png", "gameThing_images/wizard_0_1.png", "gameThing_images/wizard_1_0.png", "gameThing_images/wizard_1_1.png", "gameThing_images/wizard_2_0.png", "gameThing_images/wizard_2_1.png", "gameThing_images/wizard_3_0.png", "gameThing_images/wizard_3_1.png" }));
        sameWayBack = SAME_WAY_BACK;
        if (sameWayBack) {
            frontRoute.append(ROUTE);
            backRoute.append(ROUTE.replace('0', 'a').replace('1', '0').replace('a', '1').replace('2', 'a').replace('3', '2').replace('a', '3'));
            backRoute = backRoute.reverse();
            route = frontRoute;
        } else route.append(ROUTE);
        setDirection(0);
        level.eventLoop.figures.add(this);
        level.repaint();
    }
}
