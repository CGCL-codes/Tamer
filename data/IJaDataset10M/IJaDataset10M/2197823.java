package client;

import java.io.IOException;
import java.io.StringReader;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class ChatOutput extends JEditorPane {

    private static final long serialVersionUID = -2679554061763208444L;

    public static String ColorPLAYER = "00AA00";

    public static String ColorENEMY = "AA0000";

    public static String ColorITEM = "0000AA";

    HTMLDocument doc;

    HTMLEditorKit editor;

    public ChatOutput() {
        this.setEditorKit(new HTMLEditorKit());
        doc = (HTMLDocument) this.getDocument();
        editor = (HTMLEditorKit) this.getEditorKit();
        try {
            editor.read(new StringReader("Client v08.08.27 gestartet..."), doc, 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void append(String text) {
        text = text.replaceAll(Player.name, "<font color=" + ChatOutput.ColorPLAYER + ">" + Player.name + "</font>");
        try {
            editor.read(new StringReader(text), doc, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
