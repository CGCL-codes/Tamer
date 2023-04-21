package game.gui.sdraw;

import javax.media.opengl.GL;
import java.awt.*;

/**
 * User: honza
 * Date: Aug 27, 2006
 * Time: 11:02:38 PM
 */
class GOB {

    int id;

    Color col;

    public void draw(GL ogl) {
        ogl.glLoadName(id);
        ogl.glColor3d(((float) col.getRed() / 0xFF), ((float) col.getGreen() / 0xFF), ((float) col.getBlue() / 0xFF));
    }
}
