package org.roussev.hiena.player;

import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import org.roussev.hiena.gui.*;
import org.roussev.hiena.player.playlist.*;
import org.roussev.hiena.*;
import org.roussev.hiena.util.*;

abstract class BaseArea extends JPanel implements ActionListener {

    protected BaseArea(MainListener mainListener) {
        this.mainListener = mainListener;
        WindowUtilities.manageScrollbarUI(false);
        setLayout(null);
        area = new AreaPanel(mainListener.isApplet());
        add(area);
        playlist = new TWindow(mainListener.getOwnerWindow());
        addActionListeners();
        playlist.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                _propertyChange(evt);
            }
        });
    }

    public final void addActionListeners() {
        getPlay().addActionListener(this);
        getPause().addActionListener(this);
        getStop().addActionListener(this);
        getPrev().addActionListener(this);
        getNext().addActionListener(this);
        getPl().addActionListener(this);
        getEject().addActionListener(this);
        getShuffle().addActionListener(this);
        getRepeat().addActionListener(this);
        getClose().addActionListener(this);
        getMinimize().addActionListener(this);
        getGainSlider().addActionListener(this);
    }

    protected void setInfo(String txt) {
        area.setInfo(txt);
    }

    protected void setTime(String txt) {
        area.setTime(txt);
    }

    protected void setKbps(String txt) {
        area.setKbps(txt);
    }

    protected void setKhz(String txt) {
        area.setKhz(txt);
    }

    public final void _propertyChange(PropertyChangeEvent e) {
        if (Constants.EVT_PLAY.equals(e.getPropertyName())) {
            playActionEvent();
        } else if (Constants.EVT_STOP.equals(e.getPropertyName())) {
            stopActionEvent();
        } else if (Constants.EVT_PL_CLOSE.equals(e.getPropertyName())) {
            plActionEvent();
        } else if (Constants.EVT_PL_SHADE.equals(e.getPropertyName())) {
            doShadePlaylist();
        }
    }

    public final void actionPerformed(ActionEvent e) {
        final String cmd = e.getActionCommand();
        if (cmd.equals("play")) {
            playActionEvent();
        } else if (cmd.equals("stop")) {
            stopActionEvent();
        } else if (cmd.equals("pause")) {
            pauseActionEvent();
        } else if (cmd.equals("pl")) {
            plActionEvent();
        } else if (cmd.equals("eject")) {
            playlist.eject();
        } else if (cmd.equals("next")) {
            nextActionEvent();
        } else if (cmd.equals("prev")) {
            prevActionEvent();
        } else if (cmd.equals("close")) {
            mainListener.destroy();
        } else if (cmd.equals("minimize")) {
            mainListener.iconify();
        } else if (cmd.equals("gain")) {
            gainActionEvent();
        } else if (cmd.equals("shade")) {
            doShade();
            mainListener.shade();
        }
    }

    private final void doShade() {
        final AbsArea tmp;
        if (area instanceof AreaPanel) tmp = new AreaShadePanel(mainListener.isApplet()); else tmp = new AreaPanel(mainListener.isApplet());
        this.remove(area);
        this.add(tmp);
        area = tmp;
        area.repaint();
        addActionListeners();
    }

    private final void doShadePlaylist() {
        playlist.shade();
    }

    protected abstract void gainActionEvent();

    protected abstract void playActionEvent();

    protected abstract void pauseActionEvent();

    protected abstract void stopActionEvent();

    protected abstract void prevActionEvent();

    protected abstract void nextActionEvent();

    protected abstract void destroy();

    protected final void plActionEvent() {
        if (!isPlOpen) {
            isPlOpen = true;
            playlist.show();
        } else {
            isPlOpen = false;
            playlist.dispose();
        }
    }

    protected final XTransitionButton getPlay() {
        return area.getPlay();
    }

    protected final XTransitionButton getPause() {
        return area.getPause();
    }

    protected final XTransitionButton getStop() {
        return area.getStop();
    }

    protected final XTransitionButton getPrev() {
        return area.getPrev();
    }

    protected final XTransitionButton getNext() {
        return area.getNext();
    }

    protected final XTransitionButton getPl() {
        return area.getPl();
    }

    protected final XTransitionButton getEject() {
        return area.getEject();
    }

    protected final XTransitionButton getShuffle() {
        return area.getShuffle();
    }

    protected final XTransitionButton getRepeat() {
        return area.getRepeat();
    }

    protected final XTransitionButton getClose() {
        return area.getClose();
    }

    protected final XTransitionButton getMinimize() {
        return area.getMinimize();
    }

    protected final XTransitionButton getShade() {
        return area.getShade();
    }

    protected final XProgressSlider getProgressSlider() {
        return area.getProgressSlider();
    }

    protected final XSlider getGainSlider() {
        return area.getGainSlider();
    }

    protected Playlist playlist;

    private AbsArea area;

    protected MainListener mainListener;

    private boolean isPlOpen = false;
}
