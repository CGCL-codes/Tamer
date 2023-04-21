package de.nava.risotto;

import javax.swing.JTextPane;
import de.nava.informa.core.ItemIF;

public class ItemTextPane extends JTextPane {

    public ItemTextPane() {
        super();
        setContentType("text/html");
        setText("&nbsp; EMPTY ");
        setEditable(false);
    }

    public void setItem(ItemIF item) {
        if (item != null && item.getDescription() != null) setText(item.getDescription()); else setText("sehr leer!");
        setCaretPosition(0);
    }

    public void setText(String text) {
        super.setText("<html><body>" + text + "</body></html>");
    }
}
