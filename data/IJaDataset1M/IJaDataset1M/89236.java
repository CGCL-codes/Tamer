package game.Entrys;

import java.awt.*;
import game.*;

public class Player extends Entry {

    public Player(int ix, int iy) {
        super(ix, iy);
        image = Res.player;
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public void rerender(Graphics g) {
    }

    public void hit(Entry e) {
    }

    public void move(int ax, int ay) {
    }
}
