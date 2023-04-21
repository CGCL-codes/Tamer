package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Yan Couto
 */
public class CustomFrame extends JFrame {

    Border bor;

    Box b;

    JPanel p;

    JSlider sl;

    JSpinner sp;

    value v;

    Eventer evt;

    boolean choseCustom;

    int tts, ttri;

    public CustomFrame() {
        initialize();
    }

    private void initialize() {
        choseCustom = false;
        bor = BorderFactory.createEtchedBorder();
        bor = BorderFactory.createTitledBorder(bor, "Custom Game Settings");
        b = Box.createVerticalBox();
        p = new JPanel();
        sl = new JSlider(100, 5000, 1000);
        sp = new JSpinner(new SpinnerNumberModel(2, 0, 10, 0.2));
        v = new value();
        evt = new Eventer();
        sp.addChangeListener(evt);
        sl.setMajorTickSpacing(1500);
        sl.setMinorTickSpacing(300);
        sl.setPaintTicks(true);
        sl.setPaintLabels(true);
        sl.addChangeListener(evt);
        Box aux = Box.createHorizontalBox();
        aux.add(new JLabel("Time to Shoot:"));
        aux.add(v);
        b.add(aux);
        b.add(sl);
        b.add(Box.createVerticalStrut(10));
        aux = Box.createHorizontalBox();
        aux.add(Box.createHorizontalGlue());
        aux.add(new JLabel("Radius Rise/second"));
        aux.add(Box.createHorizontalStrut(15));
        aux.add(sp);
        aux.add(Box.createHorizontalGlue());
        b.add(aux);
        p.add(b);
        p.setBorder(bor);
        tts = sl.getValue();
        ttri = spinner(sp);
        this.add(p);
        this.setResizable(false);
        this.pack();
        this.addWindowListener(evt);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        new gC().start();
    }

    private int spinner(JSpinner s) {
        double a = new Double((Double) s.getValue()).doubleValue();
        a = (1 / a) * 1000;
        return (int) a;
    }

    private class gC extends Thread {

        public void run() {
            while (!choseCustom) {
                try {
                    this.sleep(30);
                } catch (InterruptedException ex) {
                }
            }
            BallGame.wf.difficultySet = true;
        }
    }

    private class value extends JComponent {

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawString(sl.getValue() + "", 5, 12);
        }
    }

    private class Eventer extends WindowAdapter implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == sl) {
                v.repaint();
                tts = sl.getValue();
            }
            if (e.getSource() == sp) {
                ttri = spinner(sp);
            }
            if (ttri == 0) System.out.println("Shit");
            BallGame.wf.d = Difficulty.setCustom(tts, ttri, Difficulty.MEDIUM.timeToGoodShot(), Difficulty.MEDIUM.lives());
        }

        @Override
        public void windowClosing(WindowEvent e) {
            choseCustom = true;
        }
    }
}
