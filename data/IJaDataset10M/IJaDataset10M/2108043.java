package frost.util.gui.textpane;

import java.awt.*;
import java.awt.Graphics;
import javax.swing.JTextArea;
import javax.swing.text.Document;

public class AntialiasedTextArea extends JTextArea {

    private boolean antiAliasEnabled = false;

    /**
	 * 
	 */
    public AntialiasedTextArea() {
        super();
    }

    /**
	 * @param rows
	 * @param columns
	 */
    public AntialiasedTextArea(int rows, int columns) {
        super(rows, columns);
    }

    /**
	 * @param text
	 */
    public AntialiasedTextArea(String text) {
        super(text);
    }

    /**
	 * @param text
	 * @param rows
	 * @param columns
	 */
    public AntialiasedTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
    }

    /**
	 * @param doc
	 */
    public AntialiasedTextArea(Document doc) {
        super(doc);
    }

    /**
	 * @param doc
	 * @param text
	 * @param rows
	 * @param columns
	 */
    public AntialiasedTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
    }

    /**
	 * @return
	 */
    public boolean isAntiAliasEnabled() {
        return antiAliasEnabled;
    }

    /**
	 * @param b
	 */
    public void setAntiAliasEnabled(boolean b) {
        antiAliasEnabled = b;
    }

    public void paint(Graphics g) {
        if (antiAliasEnabled) {
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        super.paint(g);
    }
}
