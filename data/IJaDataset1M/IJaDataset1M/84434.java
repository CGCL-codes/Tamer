package com.msimone.j2me;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import java.util.Timer;
import java.util.TimerTask;

public class PreloaderForm extends Form {

    private Gauge progress;

    private Timer timer;

    private WaitTimer task;

    public PreloaderForm(String title) {
        super("Please wait...");
        progress = new Gauge(title, false, 5, 0);
        append(progress);
        timer = new Timer();
        task = new WaitTimer();
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void stop() {
        timer.cancel();
        task.cancel();
    }

    private final class WaitTimer extends TimerTask {

        public void run() {
            if (progress.getValue() < progress.getMaxValue()) progress.setValue(progress.getValue() + 1); else {
                progress.setValue(0);
            }
        }
    }
}
