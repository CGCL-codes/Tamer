package clock;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author Administrator
 */
public class Clock extends MIDlet {

    private Display display;

    private ClockCanvas canvas;

    public Clock() {
        display = Display.getDisplay(this);
        canvas = new ClockCanvas();
    }

    public void startApp() {
        display.setCurrent(canvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}

class ClockCanvas extends Canvas {

    private Timer timer;

    private ClockCanvasTimerTask task;

    public ClockCanvas() {
        timer = new Timer();
        task = new ClockCanvasTimerTask(this);
        timer.schedule(task, 100, 100);
    }

    protected void paint(Graphics g) {
        Calendar cal = Calendar.getInstance();
        int hour = (int) cal.get(Calendar.HOUR);
        int hour_of_day = (int) cal.get(Calendar.HOUR_OF_DAY);
        int minute = (int) cal.get(Calendar.MINUTE);
        int second = (int) cal.get(Calendar.SECOND);
        int year = (int) cal.get(Calendar.YEAR);
        int month = (int) cal.get(Calendar.MONTH) + 1;
        int day = (int) cal.get(Calendar.DAY_OF_MONTH);
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0, 0, 0);
        g.drawArc(0, 0, 200, 200, 0, 360);
        String timestr = year + "/" + month + "/" + day + "  " + hour_of_day + ":" + minute + ":" + second;
        g.drawString(timestr, 0, 210, Graphics.LEFT | Graphics.TOP);
    }
}

class ClockCanvasTimerTask extends TimerTask {

    private ClockCanvas canvas;

    public ClockCanvasTimerTask(ClockCanvas canvas) {
        this.canvas = canvas;
    }

    public void run() {
        canvas.repaint();
    }
}
