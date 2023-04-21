package demos.nehe.lesson37;

import demos.common.GLDisplay;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {

    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Toggle rotation");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "Toggle outline drawing");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "Toggle outline smoothing");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Increase outline width");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Decrease outline width");
    }

    public void keyPressed(KeyEvent e) {
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                renderer.toggleModelRotation();
                break;
            case KeyEvent.VK_1:
                renderer.toggelOutlineDraw();
                break;
            case KeyEvent.VK_2:
                renderer.toggleOutlineSmooth();
                break;
            default:
                processKeyEvent(e, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                renderer.increaseOutlineWidth(pressed);
                break;
            case KeyEvent.VK_DOWN:
                renderer.decreaseOutlineWidth(pressed);
                break;
        }
    }
}
