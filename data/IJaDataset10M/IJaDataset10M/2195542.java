package redrocket.control.test;

import redrocket.control.Controller;
import redrocket.control.KeyCode;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * Testing key events.
 */
public class Vt01KeyEvents {

    private static final int ACTION_A = 0;

    private static final int ACTION_B = 1;

    private static final int ACTION_C = 2;

    private static final int ACTION_UP = 3;

    private static final int ACTION_DN = 4;

    private static final int ACTION_LT = 5;

    private static final int ACTION_RT = 6;

    private Vt01KeyEvents() {
        JPanel pMain = new JPanel();
        JLabel bUp = new JLabel("Up");
        JLabel bDn = new JLabel("Down");
        JLabel bLt = new JLabel("Left");
        JLabel bRt = new JLabel("Right");
        JLabel baA = new JLabel("Action A (Ctrl)");
        JLabel baB = new JLabel("Action B (z)");
        JLabel baC = new JLabel("Action C (x)");
        pMain.setLayout(new GridBagLayout());
        addKeyLabel(pMain, bUp, 2, 0);
        addKeyLabel(pMain, bLt, 1, 1);
        addKeyLabel(pMain, baA, 2, 1);
        addKeyLabel(pMain, bRt, 3, 1);
        addKeyLabel(pMain, bDn, 2, 2);
        addKeyLabel(pMain, baB, 0, 3);
        addKeyLabel(pMain, baC, 3, 3);
        Controller contr = new Controller(7);
        contr.bind(KeyCode.VK_UP, ACTION_UP);
        contr.bind(KeyCode.VK_NUMPAD8, ACTION_UP);
        contr.bind(KeyCode.VK_KP_UP, ACTION_UP);
        contr.bind(KeyCode.VK_DOWN, ACTION_DN);
        contr.bind(KeyCode.VK_NUMPAD2, ACTION_DN);
        contr.bind(KeyCode.VK_KP_DOWN, ACTION_DN);
        contr.bind(KeyCode.VK_LEFT, ACTION_LT);
        contr.bind(KeyCode.VK_NUMPAD4, ACTION_LT);
        contr.bind(KeyCode.VK_KP_LEFT, ACTION_LT);
        contr.bind(KeyCode.VK_RIGHT, ACTION_RT);
        contr.bind(KeyCode.VK_NUMPAD6, ACTION_RT);
        contr.bind(KeyCode.VK_KP_RIGHT, ACTION_RT);
        contr.bind(KeyCode.VK_CONTROL, ACTION_A);
        contr.bind(KeyCode.VK_Z, ACTION_B);
        contr.bind(KeyCode.VK_X, ACTION_C);
        JFrame frame = new JFrame(this.getClass().getName());
        frame.setContentPane(pMain);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
        contr.attachTo(frame);
        frame.pack();
        frame.setVisible(true);
        Font basic = new JLabel().getFont();
        Font plain = basic.deriveFont(Font.PLAIN);
        Font bold = basic.deriveFont(Font.BOLD);
        while (true) {
            bUp.setFont(contr.getAction(ACTION_UP) == 0 ? plain : bold);
            bDn.setFont(contr.getAction(ACTION_DN) == 0 ? plain : bold);
            bLt.setFont(contr.getAction(ACTION_LT) == 0 ? plain : bold);
            bRt.setFont(contr.getAction(ACTION_RT) == 0 ? plain : bold);
            baA.setFont(contr.getAction(ACTION_A) == 0 ? plain : bold);
            baB.setFont(contr.getAction(ACTION_B) == 0 ? plain : bold);
            baC.setFont(contr.getAction(ACTION_C) == 0 ? plain : bold);
            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    /**
	 * GUI building helper method.
	 * 
	 * @param panel panel
	 * @param label key label
	 * @param x X position
	 * @param y Y position
	 */
    private void addKeyLabel(JPanel panel, JLabel label, int x, int y) {
        panel.add(label, new GridBagConstraints(x, y, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 5, 5));
        label.setBorder(new EtchedBorder());
    }

    public static void main(String[] args) {
        new Vt01KeyEvents();
    }
}
