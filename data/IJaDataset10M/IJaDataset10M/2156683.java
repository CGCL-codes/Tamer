package com.dukesoftware.ongakumusou.player;

import com.dukesoftware.ongakumusou.gui.main.IntegratedController;
import com.dukesoftware.ongakumusou.utils.thread.LoopingTask;

/**
 * 
 * <p></p>
 * <h5>update history</h5> 
 * <p>2007/05/31 The file was created.</p>
 * 
 * @author 
 * @since 2007/05/31
 * @version last update 2007/05/31
 */
public class MediaTimeSetterAdvanced implements LoopingTask {

    private final MediaTimeListenerAdvanced subject;

    private final IntegratedController controller;

    public MediaTimeSetterAdvanced(IntegratedController controller, MediaTimeListenerAdvanced subject) {
        this.subject = subject;
        this.controller = controller;
    }

    public void pause() {
    }

    public void stop() {
        subject.resetMediaTime();
    }

    public void shutdown() {
    }

    public void resume() {
    }

    public final void run() {
        subject.updateMediaTime(controller);
    }
}
