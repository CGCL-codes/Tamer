package mikera.tyrant;

import java.awt.*;
import mikera.engine.Thing;

public class ListItem extends Panel {

    private static final long serialVersionUID = 3978147629882421811L;

    private String text;

    public ListItem(Thing t) {
        super();
        text = t.getName(Game.hero());
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawString(text, 32, getHeight());
    }
}
