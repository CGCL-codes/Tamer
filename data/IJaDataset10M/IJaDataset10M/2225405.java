package pl.rzarajczyk.utils.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Rafal
 */
public class ExtendedMouseAdapter extends MouseAdapter {

    private int interval;

    private Timer timer;

    private TimerTask task;

    public ExtendedMouseAdapter(int interval) {
        this.timer = new Timer("ExtendedMouseAdaper timer", true);
        this.interval = interval;
    }

    public void mouseSingleClicked(MouseEvent e) {
    }

    public void mouseDoubleClicked(MouseEvent e) {
    }

    public void mouseRightClicked(MouseEvent e) {
    }

    public void mouseMiddleClicked(MouseEvent e) {
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            this.mouseRightClicked(e);
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            this.mouseMiddleClicked(e);
        } else {
            assert e.getButton() == MouseEvent.BUTTON1;
            if (task != null) {
                task.cancel();
                task = null;
                this.mouseDoubleClicked(e);
            } else {
                task = new TimerTask() {

                    @Override
                    public void run() {
                        mouseSingleClicked(e);
                        task.cancel();
                        task = null;
                    }
                };
                timer.cancel();
                timer = new Timer("ExtendedMouseAdaper timer", true);
                timer.schedule(task, interval);
            }
        }
    }
}
