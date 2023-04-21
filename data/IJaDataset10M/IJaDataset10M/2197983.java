package org.eyrene.jplayer.ui;

import java.awt.*;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eyrene.jplayer.JPlayerSettings;
import org.gui4j.*;

/**
 * <p>Title: JPlayerGUI4jBuilder.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 */
public class JPlayerGUI4jBuilder implements JPlayerUIBuilder {

    private final Log log;

    private JPlayerSettings jPlayerSettings;

    private JPlayerGUI4j jPlayerGUI4j;

    private Gui4j gui4j;

    private JPlayerGUI4jController jPlayerGUI4jController;

    private JPlayerGUI4jView jPlayerGUI4jView;

    private JPlaylistGUI4jController jPlaylistGUI4jController;

    private JPlaylistGUI4jView jPlaylistGUI4jView;

    public JPlayerGUI4jBuilder(JPlayerSettings jPlayerSettings) {
        this.log = LogFactory.getLog(JPlayerGUI4jBuilder.class);
        this.jPlayerSettings = jPlayerSettings;
        URL url = JPlayerGUI4jBuilder.class.getResource("gui4jComponents.properties");
        this.gui4j = Gui4jFactory.createGui4j(true, false, -1, url);
    }

    public JPlayerUIBuilder buildJPlayerUI() {
        String name = jPlayerSettings.getName();
        String version = jPlayerSettings.getVersion();
        Dimension size = jPlayerSettings.getSize();
        log.debug("Creating JPlayerGUI4jView...");
        this.jPlayerGUI4j = new JPlayerGUI4j(gui4j, name, version, size);
        this.jPlayerGUI4j.setJPlayerGUI4jView(jPlayerGUI4jView);
        this.jPlayerGUI4j.setJPlayerGUI4jController(jPlayerGUI4jController);
        this.jPlayerGUI4j.setJPlaylistGUI4jView(jPlaylistGUI4jView);
        this.jPlayerGUI4j.setJPlaylistGUI4jController(jPlaylistGUI4jController);
        return this;
    }

    public JPlayerUI getJPlayerUI() {
        return jPlayerGUI4j;
    }

    public JPlayerUIBuilder buildJPlayerUIController() {
        log.debug("Creating JPlayerGUI4jController...");
        this.jPlayerGUI4jController = new JPlayerGUI4jController(gui4j);
        return this;
    }

    public UIController getJPlayerUIController() {
        return this.jPlayerGUI4jController;
    }

    public JPlayerUIBuilder buildJPlayerUIView() {
        log.debug("Creating JPlayerGUI4jView...");
        this.jPlayerGUI4jView = new JPlayerGUI4jView(gui4j, "JPlayerGUI4jView.xml");
        return this;
    }

    public UIView getJPlayerUIView() {
        return jPlayerGUI4jView;
    }

    public JPlayerUIBuilder buildJPlaylistUIController() {
        log.debug("Creating JPlaylistGUI4jController...");
        this.jPlaylistGUI4jController = new JPlaylistGUI4jController(gui4j);
        return this;
    }

    public UIController getJPlaylistUIController() {
        return this.jPlaylistGUI4jController;
    }

    public JPlayerUIBuilder buildJPlaylistUIView() {
        log.debug("Creating JPlaylistGUI4jView...");
        this.jPlaylistGUI4jView = new JPlaylistGUI4jView(gui4j, "JPlaylistGUI4jView.xml");
        return this;
    }

    public UIView getJPlaylistUIView() {
        return this.jPlaylistGUI4jView;
    }
}
