package org.merlotxml.merlot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * About screen for merlot with cool animation someday
 */
@SuppressWarnings("serial")
public class MerlotAbout extends JInternalFrame implements Runnable, MerlotConstants {

    XMLEditorFrame _frame;

    AboutScroller _scroller;

    public MerlotAbout(XMLEditorFrame frame) {
        super(MerlotResource.getString(UI, "help.about") + " " + XMLEditorSettings.getSharedInstance().getFrameTitle(), false, true);
        _frame = frame;
        setupPanel();
        this.addInternalFrameListener(new InternalFrameAdapter() {

            public void internalFrameClosing(InternalFrameEvent e) {
                if (_scroller != null) {
                    _scroller.stop();
                }
            }
        });
    }

    protected void setupPanel() {
        try {
            ImageIcon pic = MerlotResource.getImage(UI, "about.background");
            JLabel label = new JLabel(pic);
            JPanel p = new JPanel(new BorderLayout());
            p.setBorder(new EmptyBorder(5, 5, 5, 5));
            p.add(label, BorderLayout.CENTER);
            _scroller = new AboutScroller();
            p.add(_scroller, BorderLayout.SOUTH);
            this.getContentPane().add(p);
            Dimension d = _frame.getSize();
            this.pack();
            Dimension e = this.getSize();
            int x, y;
            x = (d.width / 2) - (e.width / 2);
            y = (d.height / 2) - (e.height / 2) - 25;
            this.setLocation(x, y);
        } catch (Exception ex) {
            MerlotDebug.exception(ex);
        }
    }

    public void run() {
        _frame.addInternalFrame(this, false);
        Thread t = new Thread(_scroller);
        t.start();
    }

    protected class AboutScroller extends JPanel implements Runnable {

        private static final long serialVersionUID = 1L;

        String _stuff[];

        static final long DELAY = 5 * 1000;

        boolean _stop = false;

        JLabel _multiLineLabel;

        public AboutScroller() {
            super();
            _stuff = new String[2];
            _stuff[0] = MerlotResource.getString(UI, "merlot.version.string");
            _stuff[1] = MerlotResource.getString(UI, "merlot.copyright");
            _multiLineLabel = new JLabel(_stuff[0]);
            _multiLineLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
            add(_multiLineLabel, BorderLayout.WEST);
        }

        public void run() {
            int i = 0;
            while (!_stop) {
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException ex) {
                }
                i++;
                _multiLineLabel.setText(_stuff[i % 2]);
                _multiLineLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
                add(_multiLineLabel, BorderLayout.WEST);
            }
        }

        public void stop() {
            _stop = true;
        }
    }
}
