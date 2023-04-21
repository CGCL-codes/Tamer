package net.scharlie.gaming.games.worm.afs;

import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.scharlie.gaming.framework.Constants;
import net.scharlie.gaming.framework.animation.AnimationLoop;
import net.scharlie.gaming.framework.animation.impl.AnimationLoopImpl;
import net.scharlie.gaming.framework.statistics.Statistics;
import net.scharlie.gaming.framework.statistics.impl.StatisticsImpl;
import net.scharlie.gaming.games.worm.WormTop;

public class WormChaseFrame extends JFrame implements WindowListener, WormTop {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -7008568511103634014L;

    /**
     * Where the worm is drawn
     */
    private WormPanel mWP = null;

    /**
     * Displays no.of boxes used
     */
    private JTextField mJtfBox;

    /**
     * Displays time spent in game
     */
    private JTextField mJtfTime;

    /**
     * Create a new worm chase.
     * 
     * @param period
     *            Period in nano seconds
     */
    public WormChaseFrame(long period) {
        super("The Worm Chase");
        makeGUI(period);
        addWindowListener(this);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void makeGUI(long period) {
        AnimationLoop aLoop = new AnimationLoopImpl();
        aLoop.setPeriod(period);
        Statistics stats = new StatisticsImpl();
        mWP = new WormPanel(this, aLoop, stats);
        aLoop.setAnimationListener(mWP);
        aLoop.setStatistics(stats);
        Container c = getContentPane();
        c.add(mWP, "Center");
        JPanel ctrls = new JPanel();
        ctrls.setLayout(new BoxLayout(ctrls, BoxLayout.X_AXIS));
        mJtfBox = new JTextField("Boxes used: 0");
        mJtfBox.setEditable(false);
        ctrls.add(mJtfBox);
        mJtfTime = new JTextField("Time Spent: 0 secs");
        mJtfTime.setEditable(false);
        ctrls.add(mJtfTime);
        c.add(ctrls, "South");
    }

    public void setBoxNumber(int no) {
        mJtfBox.setText("Boxes used: " + no);
    }

    public void setTimeSpent(long t) {
        mJtfTime.setText("Time Spent: " + t + " secs");
    }

    public void finishOff() {
        System.exit(0);
    }

    public void windowActivated(WindowEvent e) {
        mWP.resumeGame();
    }

    public void windowDeactivated(WindowEvent e) {
        mWP.pauseGame();
    }

    public void windowDeiconified(WindowEvent e) {
        mWP.resumeGame();
    }

    public void windowIconified(WindowEvent e) {
        mWP.pauseGame();
    }

    public void windowClosing(WindowEvent e) {
        mWP.stopGame();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    /**
     * Build all things together.
     * 
     * @param args
     */
    public static void main(String[] args) {
        int fps = Constants.DEFAULT_FPS;
        if (args.length != 0) {
            fps = Integer.parseInt(args[0]);
        }
        long period = Constants.KILO / fps;
        System.out.println("fps: " + fps + ", period: " + period + "ms");
        new WormChaseFrame(period * Constants.MEGA);
    }
}
