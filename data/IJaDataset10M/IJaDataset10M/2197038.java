package com.dukesoftware.ongakumusou.player;

import com.dukesoftware.ongakumusou.gui.controll.MusicListener;
import com.dukesoftware.ongakumusou.utils.thread.LoopingTask;
import com.dukesoftware.ongakumusou.utils.thread.LoopingThread;

/**
 * 
 * 
 * 
 * <h5>update history</h5>
 * <p>2007/09/24 File was created.</p>
 * 
 * @author 
 * @since 2007/09/24
 * @version last update 2007/09/24
 */
public class MusicLoopingThread extends LoopingThread implements MusicListener {

    public MusicLoopingThread(LoopingTask process) {
        super(process, 1000);
        pause();
        start();
    }

    public void setPlayState() {
        resume();
    }

    public void setStopState() {
        stop();
    }

    public void setPauseState() {
        pause();
    }
}
