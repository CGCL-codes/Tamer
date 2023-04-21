package corina.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Stack;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
   A small help browser.  It's a very simple web browser built using
   JEditorPane for browsing local documentation.

   <h2>Left to do</h2>
   <ul>
      <li>javadoc me
      <li>use special icon for corina-help (which?)
      <li>I18n: "Corina Help", "Back", "Forward", error msgs
      <li>what's the initial size/position?
      <li>change Help.java to use this HelpBrowser instead of JavaHelp
      <li>remove all refs to JavaHelp from the build (libs, manifest, makefile)
      <li>fix build to not re-build HTML version of help if docbook unchanged
      <li>make it a singleton window: don't create a new one, but rather
          showHelp() (or showHelp("id"))
      <li>ability to jump to a section by id -- do i need to make my
          own index for this?  (can i jump to an id in the middle of
	  a section in HTML, anyway?  do i care?) -- i'd need to parse
	  the docbook/xml myself to do this correctly, but i can approximate
	  it by grepping for "id=\"%s\"".  i can even 
      <li>extract TOC into its own frame?
      <li>tabs in left frame: TOC, glossary, index, etc.?
      <li>right-click gives popup menu with back/fwd?
      <li>(long-term open-java-help-clone goal: just give it a docbook tree)
      <li>look at moz help for ideas: glossary, index, search, contents in left pane,
          back/forward/home/print? buttons in toolbar, plus content area
      <li>look at GIMP Help Browser: 4 buttons on top: Home, Index, Back, Forward,
          then a pop-up menu (what for?), and the content
      <li>use stylesheet to make it look nicer
      <li>add searching capabilities?
   </ul>
*/
public class HelpBrowser extends JFrame {

    private Stack back = new Stack(), fwd = new Stack();

    private JButton backButton, fwdButton;

    private static final String HELP_PAGE = "corina/manual/index.html";

    private static class JPrettyEditorPane extends JEditorPane {

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            super.paintComponent(g);
        }
    }

    private static HelpBrowser singleton = null;

    public static void showHelpBrowser() {
        if (singleton == null) {
            singleton = new HelpBrowser();
        } else {
            singleton.toFront();
            singleton.requestFocus();
        }
    }

    public HelpBrowser() {
        super("Corina Help");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        final JEditorPane editorPane = new JPrettyEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new LinkListener());
        URL url = null;
        try {
            editorPane.setPage(getClass().getClassLoader().getResource(HELP_PAGE));
        } catch (Exception e) {
            System.err.println("Attempted to read a bad URL: " + url);
        }
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(600, 500));
        getContentPane().add(editorScrollPane, BorderLayout.CENTER);
        backButton = new JButton("Back");
        backButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                fwd.push(editorPane.getPage());
                try {
                    editorPane.setPage((URL) back.pop());
                } catch (IOException ioe) {
                    System.err.println("Attempted to read a bad URL: " + ioe);
                }
                if (back.isEmpty()) backButton.setEnabled(false);
                fwdButton.setEnabled(true);
            }
        });
        backButton.setEnabled(false);
        fwdButton = new JButton("Forward");
        fwdButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                back.push(editorPane.getPage());
                try {
                    editorPane.setPage((URL) fwd.pop());
                } catch (IOException ioe) {
                    System.err.println("Attempted to read a bad URL: " + ioe);
                }
                if (fwd.isEmpty()) fwdButton.setEnabled(false);
                backButton.setEnabled(true);
            }
        });
        fwdButton.setEnabled(false);
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flow.add(backButton);
        flow.add(fwdButton);
        getContentPane().add(flow, BorderLayout.NORTH);
        pack();
        show();
    }

    private class LinkListener implements HyperlinkListener {

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                JEditorPane pane = (JEditorPane) e.getSource();
                if (e instanceof HTMLFrameHyperlinkEvent) {
                    HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                    HTMLDocument doc = (HTMLDocument) pane.getDocument();
                    doc.processHTMLFrameHyperlinkEvent(evt);
                } else {
                    try {
                        back.push(pane.getPage());
                        fwd = new Stack();
                        pane.setPage(e.getURL());
                        backButton.setEnabled(true);
                        fwdButton.setEnabled(false);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String args[]) {
        new HelpBrowser();
    }
}
