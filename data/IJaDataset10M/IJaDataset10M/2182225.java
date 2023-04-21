package org.kku.gui.remote;

import org.kku.gui.*;
import org.kku.util.remote.*;
import javax.swing.*;

public class RemotePanelChooser implements RemoteControlListenerIF {

    private static String SOURCE_PANEL = "SOURCE_PANEL";

    private static String ARTIST_PANEL = "ARTIST_PANEL";

    private static String ALBUM_PANEL = "ALBUM_PANEL";

    private static String SONG_PANEL = "SONG_PANEL";

    private RemoteUIControl remoteUIControl;

    private String[] panels;

    private int panelIndex;

    public RemotePanelChooser(RemoteUIControl remoteUIControl) {
        this.remoteUIControl = remoteUIControl;
        init();
    }

    private void init() {
        panels = new String[] { SOURCE_PANEL, ARTIST_PANEL, ALBUM_PANEL, SONG_PANEL };
        panelIndex = 0;
    }

    public void execute(RemoteCommand remoteCommand) {
        int oldPanelIndex;
        oldPanelIndex = panelIndex;
        if (remoteCommand.getName().equals("focusNext")) {
            panelIndex++;
            if (panelIndex >= panels.length) {
                panelIndex = 0;
            }
            select(oldPanelIndex, RemoteUIControl.DESELECT);
            select(panelIndex, RemoteUIControl.SELECT);
        } else if (remoteCommand.getName().equals("focusPrevious")) {
            panelIndex--;
            if (panelIndex < 0) {
                panelIndex = panels.length - 1;
            }
            select(oldPanelIndex, RemoteUIControl.DESELECT);
            select(panelIndex, RemoteUIControl.SELECT);
        } else if (remoteCommand.getName().equals("focusUp")) {
            select(panelIndex, RemoteUIControl.SELECT_PREVIOUS);
        } else if (remoteCommand.getName().equals("focusDown")) {
            select(panelIndex, RemoteUIControl.SELECT_NEXT);
        } else {
            return;
        }
    }

    private void select(int index, int select) {
        SwingUtilities.invokeLater(new Selection(index, select));
    }

    class Selection implements Runnable {

        private int panelIndex;

        private int select;

        Selection(int panelIndex, int select) {
            this.panelIndex = panelIndex;
            this.select = select;
        }

        public void run() {
            MusicPanelIF musicPanel;
            musicPanel = getMusicPanel(panels[panelIndex]);
            musicPanel.select(select);
        }
    }

    private MusicPanelIF getMusicPanel(String name) {
        DukeJukeBox dukeJukeBox;
        DukeJukeBoxPanel dukeJukeBoxPanel;
        dukeJukeBox = remoteUIControl.getDukeJukeBox();
        dukeJukeBoxPanel = dukeJukeBox.getDukeJukeBoxPanel();
        return dukeJukeBoxPanel.getMusicPanel(name);
    }
}
