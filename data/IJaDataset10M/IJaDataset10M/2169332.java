package com.iplayawriter.novelizer.view.info;

import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * The view for displaying novel element information.
 * 
 * The information is displayed as formatted HTML.
 * 
 * @author Erik
 */
public class InfoView extends JPanel implements IInfoView {

    /** Text to display when the InfoView has no information to show */
    private static final String NO_INFORMATION = "<html><h1>No Element Selected</h1></html>";

    /** The editor pane used to show the html passed in */
    private JEditorPane displayPane;

    /**
    * Initializes the controls in this panel.
    */
    public void init() {
        setLayout(new BorderLayout());
        displayPane = new JEditorPane("text/html", NO_INFORMATION);
        displayPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayPane);
        add(scrollPane);
    }

    /** {@inheritDoc} */
    @Override
    public void update(String data) {
        if (data == null || data.isEmpty()) {
            displayPane.setText(NO_INFORMATION);
        } else {
            displayPane.setText(data);
        }
    }
}
