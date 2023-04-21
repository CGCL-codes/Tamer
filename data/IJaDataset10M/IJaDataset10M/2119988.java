package sushmu.sted.ui;

import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;
import sushmu.sted.util.Resources;

/**
 * sets HTMLEditorKit. reads Resources for the text to be displayed. Singleton.
 */
public class AboutText extends JTextPane {

    private static AboutText aboutText;

    private AboutText() {
        super();
        setEditable(false);
        setSize(400, 400);
        setEditorKit(new HTMLEditorKit());
        setText(Resources.getResource("about.dialog.text"));
    }

    public static synchronized AboutText getInstance() {
        if (aboutText == null) {
            aboutText = new AboutText();
        }
        return aboutText;
    }
}
