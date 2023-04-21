package org.eyrene.jplayer.ui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.eyrene.jplayer.JPlayerSettings;

/**
 * <p>Title: JPlayerGUIBuilder.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 */
public class JPlayerGUIBuilder implements JPlayerUIBuilder {

    private JPlayerSettings jPlayerSettings;

    private JPlayerGUI jPlayerGUI;

    public JPlayerGUIBuilder(JPlayerSettings jPlayerSettings) {
        this.jPlayerSettings = jPlayerSettings;
        this.jPlayerGUI = new JPlayerGUI();
    }

    public JPlayerUIBuilder buildJPlayerUI() {
        jPlayerGUI.setTitle("JPlayer " + jPlayerSettings.getVersion());
        JPanel mainPanel = new JPanel(new BorderLayout());
        jPlayerGUI.getContentPane().add(mainPanel);
        return this;
    }

    public JPlayerUI getJPlayerUI() {
        return jPlayerGUI;
    }

    public JPlayerUIBuilder buildJPlayerUIController() {
        return this;
    }

    public UIController getJPlayerUIController() {
        return null;
    }

    public JPlayerUIBuilder buildJPlayerUIView() {
        return this;
    }

    public UIView getJPlayerUIView() {
        return null;
    }

    public JPlayerUIBuilder buildJPlaylistUIController() {
        return this;
    }

    public UIController getJPlaylistUIController() {
        return null;
    }

    public JPlayerUIBuilder buildJPlaylistUIView() {
        return this;
    }

    public UIView getJPlaylistUIView() {
        return null;
    }
}
