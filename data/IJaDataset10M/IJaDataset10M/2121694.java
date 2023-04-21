package misc;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 */
public class HtmlViewer {

    private JComponent mSwingComponent;

    private String mTitle;

    private String mHomeUrlString;

    private JEditorPane mHtmlPane;

    private JTextField mUrlField;

    private Exception mLastPageException;

    public static void main(String[] args) throws Exception {
        new HtmlViewer("http://www.google.com");
    }

    /**
     */
    public HtmlViewer(String anUrl) throws IOException {
        if (anUrl.equals("ERROR")) {
            throw new IOException("ERROR TEST");
        }
        mHomeUrlString = anUrl;
        mTitle = anUrl;
        mHtmlPane = new JEditorPane(anUrl);
        mHtmlPane.addHyperlinkListener(new Hyperactive());
        mHtmlPane.setEditable(false);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(mHtmlPane, BorderLayout.CENTER);
        mUrlField = new JTextField(anUrl);
        mUrlField.addActionListener(new ChangeUrlAction());
        topPanel.add(mUrlField, BorderLayout.NORTH);
        mSwingComponent = topPanel;
        JFrame mFrame = new JFrame("Web Browser");
        mFrame.getContentPane().add(topPanel, BorderLayout.CENTER);
        mFrame.pack();
        mFrame.setVisible(true);
    }

    private void setUrlString(String aUrlString) {
        mLastPageException = null;
        openPage(aUrlString);
        finishPageOpen(aUrlString);
    }

    private void openPage(Object aParam) {
        String aUrlString = (String) aParam;
        if (aUrlString == null || mHtmlPane == null || mUrlField == null) {
            return;
        }
        try {
            mHtmlPane.setPage(aUrlString);
            mUrlField.setText(mHtmlPane.getPage().toString());
        } catch (Exception e) {
            mLastPageException = e;
        }
    }

    private void finishPageOpen(Object aParam) {
        String aUrlString = (String) aParam;
        if (mLastPageException != null) {
            JOptionPane.showMessageDialog(mSwingComponent, "Error with URL " + aUrlString + ": " + mLastPageException);
            mLastPageException.printStackTrace();
        }
    }

    private class ChangeUrlAction implements ActionListener {

        public void actionPerformed(ActionEvent anEvent) {
            JTextField urlField = (JTextField) anEvent.getSource();
            setUrlString(urlField.getText());
        }
    }

    /**
     * Listens for hyperlink events
     */
    private class Hyperactive implements HyperlinkListener {

        Hyperactive() {
        }

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                JEditorPane pane = (JEditorPane) e.getSource();
                if (e instanceof HTMLFrameHyperlinkEvent) {
                    HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                    HTMLDocument doc = (HTMLDocument) pane.getDocument();
                    doc.processHTMLFrameHyperlinkEvent(evt);
                } else {
                    java.net.URL url = e.getURL();
                    if (url == null) {
                        setUrlString(e.getDescription());
                    } else {
                        setUrlString(url.toString());
                    }
                }
            }
        }
    }
}
