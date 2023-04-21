package sears.gui;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import sears.tools.Utils;

/**
 * <code>JLabel</code> subclasse with highlight possibility.
 */
public class SubtitleCellComponent extends JLabel {

    private static final long serialVersionUID = 859239543259106083L;

    /** the default highlight color */
    public static final Color DEFAULT_HIGHLIGTH_COLOR = Color.LIGHT_GRAY;

    /** default font */
    public static final Font DEFAULT_FONT = UIManager.getFont("Table.font");

    private static final String EMPTY_STRING = "";

    private static String highligthString;

    private boolean highlighting;

    private int rowHeight;

    private FontRenderContext frc;

    /**
	 * Instantiates a new object
	 * @param rowHeight the height of row defines by the table
	 */
    public SubtitleCellComponent(int rowHeight) {
        super();
        setFont(DEFAULT_FONT);
        frc = new FontRenderContext(null, true, false);
        setHighlightedString(EMPTY_STRING);
        this.rowHeight = rowHeight;
    }

    /**
	 * Sets the highlighted string
	 * @param str	the string to highlight
	 */
    public void setHighlightedString(String str) {
        if (str == null || str.trim().length() == 0) {
            str = EMPTY_STRING;
            highlighting = false;
        } else {
            highlighting = true;
        }
        highligthString = str.toUpperCase();
    }

    /**
	 * Returns the sub string
	 * @return the sub string or null
	 */
    public static String getSubString() {
        return highligthString;
    }

    /**
	 * Returns the bounds of substring glyph, begin at <code>index</code>
	 * @param index		the index of the first char of the searched word
	 * @param str		the word to search, the highlighted word
	 * @return			a <tt>Rectangle</tt> object, the substring bounds
	 */
    public Rectangle getBoundsBeginAtIndex(int index, String str) {
        Rectangle bounds = null;
        String text = formatText(this.getText());
        TextLayout textLayout = new TextLayout(text, getFont(), frc);
        text = text.toUpperCase();
        Shape blackBoxBounds = textLayout.getBlackBoxBounds(index, index + str.length());
        double tyDistance = rowHeight / 2 + (textLayout.getAscent() - textLayout.getDescent()) / 2;
        bounds = new Rectangle(blackBoxBounds.getBounds().width, (int) textLayout.getAscent());
        bounds.translate((int) blackBoxBounds.getBounds2D().getX(), (int) tyDistance);
        return bounds;
    }

    /**
	 * Returns the searched sub string (respect lower and upper case states) in the specified location in subtitle text
	 * @param index	the first index of the first char of the searched text
	 * @param str	the searched text
	 * @return		the searched text in the subtitle text
	 */
    public String getSubStringBeginAtIndex(int index, String str) {
        String text = formatText(getText());
        return text.substring(index, index + str.length());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;
        Color defaultColor = gr.getColor();
        gr.setColor(getBackground());
        gr.fillRect(0, 0, getWidth(), getHeight());
        String text = formatText(getText());
        TextLayout layout = new TextLayout(text, gr.getFont(), gr.getFontRenderContext());
        if (highlighting) {
            paintHighlightShape(gr, layout, text);
        }
        gr.setColor(getForeground());
        float layoutYCentered = getHeight() / 2 + (layout.getAscent() - layout.getDescent()) / 2;
        this.setSize(getWidth(), getHeight());
        layout.draw(gr, 0, layoutYCentered);
        gr.setColor(defaultColor);
    }

    /**
	 * Graphics context could be modified by this method.
	 * @param gr		the graphic context
	 * @param layout	the layout which contain text to highlight
	 * @param text		the text to highlight
	 */
    private void paintHighlightShape(Graphics2D gr, TextLayout layout, String text) {
        Composite defaultComposite = gr.getComposite();
        Rectangle cell = SwingUtilities.getLocalBounds(this);
        if (cell != null) {
            text = text.toUpperCase();
            int start = -1;
            while ((start = text.indexOf(highligthString, start + 1)) != -1) {
                Shape sh = layout.getLogicalHighlightShape(start, start + highligthString.length(), cell);
                gr.setColor(DEFAULT_HIGHLIGTH_COLOR);
                gr.fill(sh);
            }
        }
        gr.setComposite(defaultComposite);
    }

    /**
	 * Format the text given in parameters
	 * @param text 	the text to format
	 * @return		the formatted text
	 */
    private String formatText(String text) {
        if (text.equals("")) {
            text = " ";
        } else {
            text = text.replace(Utils.LINE_SEPARATOR, " ");
        }
        return text;
    }
}
