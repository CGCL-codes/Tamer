package org.heartstorming.bada.swingui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.heartstorming.bada.Bada;
import org.heartstorming.bada.playlist.PlayList;
import org.heartstorming.bada.command.CommandFactory;
import org.heartstorming.bada.playlist.command.PlayListCommandFactory;

public class BadaUI extends JPanel {

    Bada bada;

    private PlaybackGUI playbackGUI;

    private TimeBarGUI timeBarGUI;

    private PlayListGUI playListGUI;

    public BadaUI(Bada bada) {
        this.bada = bada;
        PlayList playList = bada.getPlayList();
        CommandFactory.makeCommands(bada);
        PlayListCommandFactory.makeCommands(playList);
        playbackGUI = new PlaybackGUI();
        timeBarGUI = new TimeBarGUI();
        timeBarGUI.setBada(bada);
        bada.getTimeBarObserver().setGUI(timeBarGUI);
        playListGUI = new PlayListGUI(bada);
        setLayout(new BorderLayout());
        add(playbackGUI, BorderLayout.NORTH);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Playlist", playListGUI);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(timeBarGUI);
        panel.add(tabbedPane);
        add(panel, BorderLayout.CENTER);
    }

    public static void createAndShowGUI(Bada bada) {
        JFrame frame = new JFrame("BadaUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.setLayout(new GridLayout(1, 1));
        container.add(new BadaUI(bada));
        frame.pack();
        frame.setVisible(true);
    }
}
