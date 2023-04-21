package theme;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import java.net.URL;
import java.io.*;
import java.awt.Cursor;

public class HTMLDisplay extends JScrollPane implements HyperlinkListener {

    private JEditorPane edit;

    public HTMLDisplay() {
        edit = new JEditorPane();
        edit.setEditable(false);
        edit.addHyperlinkListener(this);
        getViewport().add(edit);
        File file = new File(Theme.THEME_HELP);
        showURL("file:" + file.getAbsolutePath());
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
            Cursor c = edit.getCursor();
            Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
            edit.setCursor(waitCursor);
            SwingUtilities.invokeLater(new URLLoader(e.getURL(), c));
        }
    }

    public void showURL(String url) {
        Document doc = edit.getDocument();
        try {
            edit.setPage(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Couldn't open page : " + url);
            getToolkit().beep();
        }
    }

    class URLLoader implements Runnable {

        private URL url;

        private Cursor cursor;

        URLLoader(URL u, Cursor c) {
            this.url = u;
            this.cursor = c;
        }

        public void run() {
            if (null == url) {
                edit.setCursor(cursor);
            } else {
                showURL(url.toString());
                url = null;
                SwingUtilities.invokeLater(this);
            }
        }
    }
}
