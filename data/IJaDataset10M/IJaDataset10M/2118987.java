package org.berlin.pino.test.functional.jogl;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCanvas;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.berlin.pino.test.jogl.box.GLBox;
import com.sun.opengl.util.Animator;

/**
 * 
 * @author BerlinBrown
 *
 */
public class SmallerGLFrameWin extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 443658669585028L;

    public void buildFrame() {
        final JPanel panel = new JPanel();
        final JTextField text = new JTextField("http://www.botnode.com");
        final JTextField text2 = new JTextField("http://www.google.com");
        final BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        final GLCanvas canvas = (new GLBox.Builder()).buildCanvas();
        final Animator animator = new Animator(canvas);
        panel.add(text);
        panel.add(canvas);
        panel.add(text2);
        panel.setPreferredSize(new Dimension(600, 400));
        this.add(panel);
        this.setLocation(400, 200);
        this.setResizable(false);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        animator.start();
        this.pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                final SmallerGLFrameWin demo = new SmallerGLFrameWin();
                demo.buildFrame();
                demo.setVisible(true);
            }
        });
    }
}
