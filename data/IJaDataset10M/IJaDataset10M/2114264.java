package headFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import mp3Player.MP3SoundThread;

public class SlideListener implements ChangeListener, MouseListener, KeyListener {

    SuperFrame mframe;

    public SlideListener(SuperFrame mframe) {
        this.mframe = mframe;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == mframe.jSlider1 && !mframe.jSlider1.getValueIsAdjusting()) {
            try {
                if (PublicFrameActions.soundthread == null) {
                    return;
                }
                if (PublicFrameActions.soundthread.soundplayer == null) {
                    return;
                }
                String path = PublicFrameActions.soundthread.path;
                PublicFrameActions.soundthread.stop();
                synchronized (this) {
                    while (!(PublicFrameActions.soundthread.closed && PublicFrameActions.soundthread.soundplayer.isClosed)) {
                        this.wait(1);
                    }
                }
                PublicFrameActions.soundthread = new MP3SoundThread(path, mframe.jSlider1.getValue(), Integer.MAX_VALUE);
                PublicFrameActions.soundthread.execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == mframe.jSlider1 && mframe.jSlider1.getValueIsAdjusting()) {
            if (PublicFrameActions.soundthread == null) return;
            if (PublicFrameActions.soundthread.soundplayer == null) return;
            PublicFrameActions.soundthread.soundplayer.pause = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public synchronized void mouseReleased(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
}
