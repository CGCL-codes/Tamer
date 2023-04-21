package tk.bot;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.text.html.*;

public class BotHelp extends JInternalFrame {

    BotJarReader reader = new BotJarReader("TKBot.jar");

    public BotHelp() {
        super("Help", true, true, true, true);
        setPreferredSize(new Dimension(200, 400));
        HtmlPane html = new HtmlPane();
        setContentPane(html);
    }

    public void showInternalFrame(JDesktopPane pane, int level) {
        pack();
        pane.add(this, level);
        show();
    }

    class HtmlPane extends JScrollPane implements HyperlinkListener {

        JEditorPane html;

        public HtmlPane() {
            html = new JEditorPane();
            html.setContentType("text/html");
            html.setEditable(false);
            html.addHyperlinkListener(this);
            JViewport vp = getViewport();
            vp.add(html);
            linkActivated("index.html");
        }

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (TKBot.DEBUG > 3) System.out.println(e.getDescription());
                linkActivated(e.getDescription());
            }
        }

        protected void linkActivated(String file) {
            (new PageLoader(file)).start();
        }

        class PageLoader extends Thread implements Runnable {

            String file;

            Cursor cursor;

            PageLoader(String file) {
                setPriority(MIN_PRIORITY);
                this.file = file;
            }

            public void run() {
                if (file == null) {
                } else {
                    Document doc = html.getDocument();
                    html.setText(new String(reader.getBytes(file)));
                }
            }
        }
    }
}
