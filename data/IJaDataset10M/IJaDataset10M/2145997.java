package com.testonica.kickelhahn.core.ui.text;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;

public class AntiAliasedEditorPane extends JEditorPane {

    public AntiAliasedEditorPane() {
        super();
    }

    public AntiAliasedEditorPane(String arg0, String arg1) {
        super(arg0, arg1);
    }

    public AntiAliasedEditorPane(String arg0) throws IOException {
        super(arg0);
    }

    public AntiAliasedEditorPane(URL arg0) throws IOException {
        super(arg0);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponent(g2);
    }
}
