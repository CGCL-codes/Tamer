package gra;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Klawisze implements KeyListener {

    @Override
    public void keyPressed(KeyEvent arg0) {
        Gra.Instancja().klawisz[arg0.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        Gra.Instancja().klawisz[arg0.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        if (arg0.getKeyChar() == 27) {
            Gra.Instancja().dziala = false;
        }
    }
}
