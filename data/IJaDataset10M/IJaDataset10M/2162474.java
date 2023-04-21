package org.knopflerfish.bundle.desktop.event;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Date;
import javax.swing.JFrame;
import org.osgi.service.event.*;

public class JDetailFrame extends JFrame {

    int w = 500;

    int h = 300;

    JEventEntryDetail eventEntryDetail;

    public JDetailFrame(JEventTable parent, Event entry) {
        super(new Date(Util.getTime(entry)).toString() + ": " + Util.getMessage(entry));
        getContentPane().setLayout(new BorderLayout());
        eventEntryDetail = new JEventEntryDetail(parent, entry);
        Container contentPane = getContentPane();
        contentPane.add(eventEntryDetail, BorderLayout.CENTER);
        pack();
        invalidate();
        setSize(new Dimension(w, h));
        setVisible(true);
    }

    public void setEntry(Event entry) {
        eventEntryDetail.setEntry(entry);
    }
}
