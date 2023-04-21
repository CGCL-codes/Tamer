package ua.org.groovy.gs.ui;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.plaf.ComponentUI;

public class GroovyEditorPane extends JTextPane {

    public GroovyEditorPane() {
        setFont(new Font("Monospaced", Font.PLAIN, 13));
        setEditorKit(new GroovyKit());
    }

    public boolean getScrollableTracksViewportWidth() {
        Component parent = getParent();
        ComponentUI ui = getUI();
        return parent != null ? (ui.getPreferredSize(this).width <= parent.getSize().width) : true;
    }
}
