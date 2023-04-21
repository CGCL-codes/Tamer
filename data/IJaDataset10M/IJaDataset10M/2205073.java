package net.kano.joustsim.text.convbox;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Keymap;
import javax.swing.text.html.StyleSheet;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;

public class ConversationBox extends JPanel {

    private final JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    private final JTextPane textpane = new JTextPane();

    private final ConversationEditorKit editorKit = new ConversationEditorKit();

    private final ConversationDocument document;

    private final StyleSheet stylesheet;

    {
        textpane.setEditorKit(editorKit);
        document = (ConversationDocument) textpane.getDocument();
        document.addConversationDocumentListener(new BottomScroller(scrollPane, textpane, document));
        textpane.setNavigationFilter(new EndAvoider(document));
        textpane.setEditable(false);
        textpane.setDragEnabled(true);
        Keymap keymap = textpane.getKeymap();
        Keymap parent = keymap.getResolveParent();
        System.out.println("keymap: " + keymap);
        System.out.println(Arrays.asList(keymap.getBoundKeyStrokes()));
        System.out.println("parent: " + parent);
        System.out.println(Arrays.asList(parent.getBoundKeyStrokes()));
        System.out.println("up: " + keymap.getAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)));
        stylesheet = document.getStyleSheet();
        textpane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                HyperlinkEvent.EventType eventType = e.getEventType();
                if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
                    URL url = e.getURL();
                    System.out.println("url: " + url);
                }
            }
        });
        loadRulesResource("css/imbox.css");
        scrollPane.setViewport(new BottomScrollingViewport());
        scrollPane.setViewportView(textpane);
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTextPane getTextPane() {
        return textpane;
    }

    public ConversationEditorKit getEditorKit() {
        return editorKit;
    }

    public ConversationDocument getDocument() {
        return document;
    }

    public StyleSheet getStylesheet() {
        return stylesheet;
    }

    private boolean loadRulesResource(String resource) {
        ClassLoader cl = getClass().getClassLoader();
        InputStream stream = cl.getResourceAsStream(resource);
        if (stream == null) return false;
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(stream, "US-ASCII");
        } catch (UnsupportedEncodingException impossible) {
            return false;
        }
        String rules = readIntoString(isr);
        if (rules == null) return false;
        stylesheet.addRule(rules);
        return true;
    }

    public void setEnabled(boolean enabled) {
        textpane.setEnabled(enabled);
    }

    private String readIntoString(InputStreamReader isr) {
        StringBuffer sb = new StringBuffer(500);
        try {
            char[] chars = new char[500];
            while (true) {
                int c = isr.read(chars);
                if (c == -1) break;
                sb.append(chars, 0, c);
            }
        } catch (IOException e) {
            return null;
        }
        return sb.toString();
    }

    private class UpAction extends AbstractAction {

        public UpAction() {
            super("Scroll up");
        }

        public void actionPerformed(ActionEvent e) {
        }
    }
}
