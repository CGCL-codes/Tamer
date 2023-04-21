package com.googlecode.sarasvati.editor.dialog;

import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.border.BevelBorder;
import com.googlecode.sarasvati.editor.panel.OpenLibraryPanel;

public class OpenLibraryDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public OpenLibraryDialog(final JFrame frame) {
        super(frame, "Open from Library", false);
        Point location = new Point(frame.getLocation());
        location.translate(200, 100);
        setLocation(location);
        setUndecorated(false);
        OpenLibraryPanel panel = new OpenLibraryPanel();
        panel.setup(this);
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        getContentPane().add(panel);
        setResizable(false);
        pack();
    }
}
