package utility;

import java.awt.Color;
import main.Sprite;

public class ContraReticle extends Sprite {

    int Counter = 0;

    public ContraReticle() {
        x = 300;
        y = 750;
        collidable = false;
        drawOrder = 30;
        width = 0;
    }

    public void draw() {
        g.setColor(Color.ORANGE);
        g.drawLine(x + 15 + width, y, x + 5 + width, y);
        g.drawLine(x - 15 - width, y, x - 5 - width, y);
        g.drawLine(x, y + 15 + width, x, y + 5 + width);
        g.drawLine(x, y - 15 - width, x, y - 5 - width);
    }

    public void setWidth(int wid) {
        Counter = 0;
        super.setWidth(wid);
    }

    public void think() {
        if (width != 0) {
            Counter++;
            if (Counter % 10 == 9) {
                Counter = 0;
                width = 0;
            }
        }
    }

    public boolean doesCollide(Sprite s) {
        return false;
    }
}
