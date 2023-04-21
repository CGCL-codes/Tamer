package com.ivis.xprocess.web.client.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

public class EditlinkLabel extends JLabel {

    private static final long serialVersionUID = 1L;

    private IHyperlinkListener linkListener;

    public EditlinkLabel(String label, IHyperlinkListener hyperlinkListener) {
        super(label);
        this.linkListener = hyperlinkListener;
        setForeground(Color.BLUE);
        MouseListener linker = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (linkListener != null) {
                    linkListener.linkClicked();
                }
            }

            public void mouseEntered(MouseEvent e) {
                e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        };
        addMouseListener(linker);
    }
}
