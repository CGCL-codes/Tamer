package oldjuie;

import java.awt.Canvas;
import java.awt.Event;
import java.awt.Graphics;

class ToolButton extends Canvas {

    public int index;

    public static int ford = -1;

    public boolean handleEvent(Event evt) {
        switch(evt.id) {
            case Event.MOUSE_DOWN:
                this.getGraphics().drawImage(Ed.pres[this.index], 0, 0, this);
                ford = ((this.index < 2) ? this.index : -1);
                if (this.index != Ed.press && this.index >= 2) {
                    Ed.tobut[Ed.press].getGraphics().drawImage(Ed.up[Ed.press], 0, 0, this);
                }
                break;
            case Event.MOUSE_UP:
                ford = -1;
                if (this.index < 2) this.getGraphics().drawImage(Ed.up[this.index], 0, 0, this);
        }
        return false;
    }

    public void paint(Graphics g) {
        if (Ed.press != this.index && this.index != ford) g.drawImage(Ed.up[this.index], 0, 0, this); else {
            g.drawImage(Ed.pres[this.index], 0, 0, this);
        }
    }
}
