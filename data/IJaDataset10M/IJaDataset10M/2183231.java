package cafe;

import javax.swing.JFrame;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Sterling
 */
public class Window {

    private static Window windowSingleton;

    public static Window getWindow() {
        if (windowSingleton == null) {
            return new Window();
        } else {
            return windowSingleton;
        }
    }

    private JFrame frame;

    private JPanel canvas;

    private Window() {
        frame = new JFrame();
        canvas = new JPanel();
    }

    public void init() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("CafeGame");
        frame.setSize(640, 480);
        frame.setResizable(false);
        frame.add(canvas);
        canvas.setDoubleBuffered(true);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
    }
}
