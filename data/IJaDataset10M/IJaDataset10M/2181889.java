package net.solosky.maplefetion.util;

import java.util.Timer;

/**
 *
 * 共享的定时器，可以在多个飞信实例之间共享
 *
 * @author solosky <solosky772@qq.com>
 */
public class SharedTimer extends ThreadTimer {

    @Override
    public synchronized void startTimer() {
        if (this.timer == null) {
            this.timer = new Timer("MapleFetionSharedTimer");
        }
    }

    @Override
    public synchronized void stopTimer() {
    }

    /**
     * 这个才是真正的关闭定时器
     */
    public void reallyStopTimer() {
        super.stopTimer();
    }
}
