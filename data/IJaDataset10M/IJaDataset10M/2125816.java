package capitulo4.lowlevel;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class ExercicioCanvasSuporteMIDlet extends MIDlet {

    Display display;

    MeuCanvas canvas;

    TextBox box;

    public ExercicioCanvasSuporteMIDlet() {
        super();
    }

    protected void startApp() throws MIDletStateChangeException {
        display = Display.getDisplay(this);
        canvas = new MeuCanvas();
        box = new TextBox("Resolu��o", "", 50, TextField.ANY);
        String resolucao = "Resolu��o: " + canvas.getWidth() + " x " + canvas.getHeight();
        String cores = "Numero de cores: " + display.numColors();
        box.setString(resolucao + " | " + cores);
        display.setCurrent(box);
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
    }

    private class MeuCanvas extends Canvas {

        protected void paint(Graphics arg0) {
        }
    }
}
