package org.webstrips.gui;

import java.awt.Insets;
import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLDocument;
import org.coffeeshop.swing.SwingDefaultListeners;

public class HTMLBox extends JEditorPane {

    private static final Insets MARGIN = new Insets(10, 10, 10, 10);

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public HTMLBox(String text) {
        super("text/html", "");
        setText("<html>" + text + "</html>");
        setEditable(false);
        ((HTMLDocument) getDocument()).getStyleSheet().importStyleSheet(ComicList.class.getResource("style.css"));
        addHyperlinkListener(SwingDefaultListeners.getDefaultHyperlinkListener());
        setMargin(MARGIN);
        setTransferHandler(null);
    }
}
