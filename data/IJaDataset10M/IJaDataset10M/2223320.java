package View.GUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import Utils.ConstantsImplement;
import Utils.Timers.Timer;
import Utils.Timers.TimerListener;

public class PanelWalk implements MouseInputListener, MouseMotionListener, ConstantsImplement, TimerListener {

    private Timer timer;

    private JPanel p_;

    private JPanel megaPanel;

    private boolean up;

    private boolean right;

    private boolean down;

    private boolean left;

    public PanelWalk(JPanel p, JPanel megaPanel) {
        timer = new Timer(this, 20);
        timer.setRun_(false);
        timer.start();
        p_ = p;
        this.megaPanel = megaPanel;
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        if (e.getX() > megaPanel.getWidth() - megaPanel.getWidth() * 0.1) {
            if (left == true) left = false;
            if (!(timer.isRun_())) timer.setRun_(true);
            right = true;
        } else if (e.getX() < megaPanel.getWidth() * 0.1) {
            if (right == true) right = false;
            if (!(timer.isRun_())) timer.setRun_(true);
            left = true;
        } else if (e.getY() < megaPanel.getWidth() * 0.1) {
            if (down == true) down = false;
            if (!(timer.isRun_())) timer.setRun_(true);
            up = true;
        } else if (e.getY() > megaPanel.getHeight() - megaPanel.getWidth() * 0.1) {
            if (up == true) up = false;
            if (!(timer.isRun_())) timer.setRun_(true);
            down = true;
        } else {
            if (timer.isRun_()) timer.setRun_(false);
            if (down == true) down = false;
            if (right == true) right = false;
            if (left == true) left = false;
            if (up == true) up = false;
        }
    }

    public void update() {
        System.out.println("Ando");
        if (right == true && p_.getX() >= -p_.getWidth() + megaPanel.getWidth() + 5) {
            p_.setLocation(p_.getX() - 5, p_.getY());
        }
        if (left == true && p_.getX() <= 0 - 5) {
            p_.setLocation(p_.getX() + 5, p_.getY());
        }
        if (up == true && p_.getY() <= 0 - 5) {
            p_.setLocation(p_.getX(), p_.getY() + 5);
        }
        if (down == true && p_.getY() >= -p_.getHeight() + megaPanel.getHeight() + 5) {
            p_.setLocation(p_.getX(), p_.getY() - 5);
        }
        megaPanel.repaint();
    }
}
