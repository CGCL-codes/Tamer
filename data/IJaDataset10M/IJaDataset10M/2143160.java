package base;

import java.awt.Point;

public class DefaultStrategie implements Strategie {

    public Move getMove() {
        return (new DefaultMove(new Point(0, 0), 0));
    }
}
