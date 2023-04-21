package com.cell.html;

import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import com.cell.CObject;

public class HtmlFrame extends JInternalFrame implements HyperlinkListener {

    JEditorPane html_page;

    public HtmlFrame(String path, int width, int height) {
        try {
            html_page = new JEditorPane();
            html_page.setEditable(false);
            html_page.setPage(path);
            html_page.addHyperlinkListener(this);
            JScrollPane scroll = new JScrollPane(html_page);
            this.add(scroll);
            this.setResizable(true);
            this.setSize(width, height);
            this.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JEditorPane pane = (JEditorPane) e.getSource();
            if (e instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                HTMLDocument doc = (HTMLDocument) pane.getDocument();
                doc.processHTMLFrameHyperlinkEvent(evt);
            } else {
                try {
                    System.out.println("open : " + e.getURL().toString());
                    CObject.getAppBridge().openBrowser(e.getURL().toString());
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.add(new HtmlFrame("http://www.google.com", 800, 600));
        frame.setVisible(true);
    }
}
