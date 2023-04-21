package com.dragoniade.deviantart.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpDialog extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4444282683407354837L;

    private final JTextPane textPane;

    private final JFrame owner;

    public HelpDialog(JFrame frame) {
        super(frame);
        setTitle("Help");
        this.owner = frame;
        textPane = new JTextPane();
        JScrollPane scroll = new JScrollPane(textPane);
        this.add(scroll);
        this.pack();
        this.setSize(800, 600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
        this.setVisible(true);
        textPane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent ev) {
                if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String uri = ev.getURL().toString();
                    if (uri.startsWith("http")) {
                        if (!NavigatorLauncher.launch(uri)) {
                            JOptionPane.showMessageDialog(owner, "Unable to start web browser.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        int pound = uri.indexOf('#');
                        String url, mark;
                        if (pound > -1) {
                            url = uri.substring(0, pound);
                            if (pound == uri.length() - 1) {
                                mark = null;
                            } else {
                                mark = uri.substring(pound + 1);
                            }
                        } else {
                            url = uri;
                            mark = null;
                        }
                        if (url.length() > 0) {
                            try {
                                textPane.setPage(ev.getURL());
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        if (mark != null) {
                            textPane.scrollToReference(mark);
                        }
                    }
                }
            }
        });
        textPane.setEditable(false);
        URL page = getClass().getResource("/help/Index.html");
        setPage(page);
    }

    public void setPage(URL page) {
        try {
            textPane.setPage(page);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(owner, "Unable to load page.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
