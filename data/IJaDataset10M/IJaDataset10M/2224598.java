package fmglemmings;

import java.awt.Color;
import java.awt.Graphics;

public class BuilderLemmingView extends ViewObject {

    public void drawme(Graphics g) {
        g.setColor(MyColor);
        if ((dir == 3) || (dir == 6) || (dir == 9) || (dir == 11)) g.fillRect(0, 0, (int) ((CELLX - 1) / 2), CELLY - 1); else g.fillRect((int) ((CELLX - 1) / 2), 0, (int) ((CELLX - 1) / 2), CELLY - 1);
        g.fillOval(0, 0, CELLX - 1, CELLY - 1);
        g.setColor(Color.BLACK);
        g.drawString("É", 8, 16);
    }

    public BuilderLemmingView() {
        MyColor = Color.ORANGE;
    }
}
