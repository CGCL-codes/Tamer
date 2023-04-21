package emulator.GUI.keyboard;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class VicMouseEvent extends MouseAdapter {

    private JButton button;

    private VicKey key;

    private boolean key_state;

    private Color default_color;

    private Color selected_color;

    VicMouseEvent(JButton button, VicKey key) {
        this.button = button;
        this.key = key;
        this.key_state = false;
        default_color = button.getForeground();
        selected_color = Color.RED;
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON1) pressKey(); else if (arg0.getButton() == MouseEvent.BUTTON3) {
            if (key_state) releaseKey(); else pressKey();
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON1) releaseKey();
    }

    private void pressKey() {
        key.press();
        key_state = true;
        button.setForeground(selected_color);
    }

    private void releaseKey() {
        key.release();
        key_state = false;
        button.setForeground(default_color);
    }
}
