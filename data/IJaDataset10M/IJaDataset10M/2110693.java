package test.swing;

import env3d.Env;
import env3d.EnvLine;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import org.lwjgl.opengl.Display;

/**
 *
 * @author jmadar
 */
public class Game {

    private Env env;

    public Game() {
        Canvas display_parent = new Canvas();
        display_parent.setSize(640, 480);
        display_parent.setIgnoreRepaint(true);
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());
        f.add(new JLabel("Hello"), BorderLayout.NORTH);
        f.add(display_parent, BorderLayout.CENTER);
        f.add(new JButton(new AbstractAction("test") {

            public void actionPerformed(ActionEvent e) {
                env.addObject(new Doty());
            }
        }), BorderLayout.SOUTH);
        f.pack();
        f.setVisible(true);
        JFrame testFrame = new JFrame();
        testFrame.setLayout(new BorderLayout());
        testFrame.add(new JTextArea("test"));
        testFrame.pack();
        try {
            Display.setParent(display_parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        env = new Env(true);
    }

    public void play() {
        Doty d = new Doty();
        d.setTexture("test");
        d.setModel("box_south");
        d.setRotateX(180);
        TextObject t = new TextObject();
        t.setX(5);
        t.setY(1);
        t.setZ(5);
        t.setScale(1);
        env.addObject(t);
        EnvLine line = new EnvLine();
        line.setPoint1(0, 0, 0);
        line.setPoint2(10, 10, 10);
        line.setColor(1, 0, 0);
        env.addObject(line);
        EnvLine line2 = new EnvLine();
        line2.setPoint1(0, 0, 0);
        line2.setPoint2(10, 5, 10);
        line2.setColor(0, 1, 0);
        env.addObject(line2);
        while (true) {
            env.advanceOneFrame();
        }
    }

    public static void main(String args[]) {
        (new Game()).play();
    }
}
