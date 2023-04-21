package org.lateralgm.components.visual;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javax.swing.Icon;

/**
 VTextIcon is an Icon implementation which draws a short string vertically.
 It's useful for JTabbedPanes with LEFT or RIGHT tabs but can be used in any
 component which supports Icons, such as JLabel or JButton 
 
 You can provide a hint to indicate whether to rotate the string 
 to the left or right, or not at all, and it checks to make sure 
 that the rotation is legal for the given string 
 (for example, Chinese/Japanese/Korean scripts have special rules when 
 drawn vertically and should never be rotated)
 */
public class VTextIcon implements Icon, PropertyChangeListener {

    String fLabel;

    String[] fCharStrings;

    int[] fCharWidths;

    int[] fPosition;

    int fWidth, fHeight, fCharHeight, fDescent;

    int fRotation;

    Component fComponent;

    static final int POSITION_NORMAL = 0;

    static final int POSITION_TOP_RIGHT = 1;

    static final int POSITION_FAR_TOP_RIGHT = 2;

    public static final int ROTATE_DEFAULT = 0x00;

    public static final int ROTATE_NONE = 0x01;

    public static final int ROTATE_LEFT = 0x02;

    public static final int ROTATE_RIGHT = 0x04;

    /**
	 * Creates a <code>VTextIcon</code> for the specified <code>component</code>
	 * with the specified <code>label</code>.
	 * It sets the orientation to the default for the string
	 * @see #verifyRotation
	 */
    public VTextIcon(Component component, String label) {
        this(component, label, ROTATE_DEFAULT);
    }

    /**
	 * Creates a <code>VTextIcon</code> for the specified <code>component</code>
	 * with the specified <code>label</code>.
	 * It sets the orientation to the provided value if it's legal for the string
	 * @see #verifyRotation
	 */
    public VTextIcon(Component component, String label, int rotateHint) {
        fComponent = component;
        fLabel = label;
        fRotation = verifyRotation(label, rotateHint);
        calcDimensions();
        fComponent.addPropertyChangeListener(this);
    }

    /**
	 * sets the label to the given string, updating the orientation as needed
	 * and invalidating the layout if the size changes
	 * @see #verifyRotation
	 */
    public void setLabel(String label) {
        fLabel = label;
        fRotation = verifyRotation(label, fRotation);
        recalcDimensions();
    }

    /**
	 * Checks for changes to the font on the fComponent
	 * so that it can invalidate the layout if the size changes
	 */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if ("font".equals(prop)) {
            recalcDimensions();
        }
    }

    /** 
	 * Calculates the dimensions.  If they've changed,
	 * invalidates the component
	 */
    void recalcDimensions() {
        int wOld = getIconWidth();
        int hOld = getIconHeight();
        calcDimensions();
        if (wOld != getIconWidth() || hOld != getIconHeight()) fComponent.invalidate();
    }

    void calcDimensions() {
        FontMetrics fm = fComponent.getFontMetrics(fComponent.getFont());
        fCharHeight = fm.getAscent() + fm.getDescent();
        fDescent = fm.getDescent();
        if (fRotation == ROTATE_NONE) {
            int len = fLabel.length();
            char data[] = new char[len];
            fLabel.getChars(0, len, data, 0);
            fWidth = 0;
            fCharStrings = new String[len];
            fCharWidths = new int[len];
            fPosition = new int[len];
            char ch;
            for (int i = 0; i < len; i++) {
                ch = data[i];
                fCharWidths[i] = fm.charWidth(ch);
                if (fCharWidths[i] > fWidth) fWidth = fCharWidths[i];
                fCharStrings[i] = new String(data, i, 1);
                if (IN_TOP_RIGHT.indexOf(ch) >= 0) fPosition[i] = POSITION_TOP_RIGHT; else if (IN_FAR_TOP_RIGHT.indexOf(ch) >= 0) fPosition[i] = POSITION_FAR_TOP_RIGHT; else fPosition[i] = POSITION_NORMAL;
            }
            fHeight = fCharHeight * len + fDescent;
        } else {
            fWidth = fCharHeight;
            fHeight = fm.stringWidth(fLabel) + 2 * K_BUFFER_SPACE;
        }
    }

    /**
	 * Draw the icon at the specified location.  Icon implementations
	 * may use the Component argument to get properties useful for 
	 * painting, e.g. the foreground or background color.
	 */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        Toolkit tk = Toolkit.getDefaultToolkit();
        Map<?, ?> map = (Map<?, ?>) (tk.getDesktopProperty("awt.font.desktophints"));
        if (map != null) {
            g2.addRenderingHints(map);
        }
        g2.setColor(c.getForeground());
        g2.setFont(c.getFont());
        if (fRotation == ROTATE_NONE) {
            int yPos = y + fCharHeight;
            for (int i = 0; i < fCharStrings.length; i++) {
                int tweak;
                switch(fPosition[i]) {
                    case POSITION_NORMAL:
                        g2.drawString(fCharStrings[i], x + ((fWidth - fCharWidths[i]) / 2), yPos);
                        break;
                    case POSITION_TOP_RIGHT:
                        tweak = fCharHeight / 3;
                        g2.drawString(fCharStrings[i], x + (tweak / 2), yPos - tweak);
                        break;
                    case POSITION_FAR_TOP_RIGHT:
                        tweak = fCharHeight - fCharHeight / 3;
                        g2.drawString(fCharStrings[i], x + (tweak / 2), yPos - tweak);
                        break;
                    default:
                        throw new Error();
                }
                yPos += fCharHeight;
            }
        } else if (fRotation == ROTATE_LEFT) {
            g2.translate(x + fWidth, y + fHeight);
            g2.rotate(-NINETY_DEGREES);
            g2.drawString(fLabel, K_BUFFER_SPACE, -fDescent);
            g2.rotate(NINETY_DEGREES);
            g2.translate(-(x + fWidth), -(y + fHeight));
        } else if (fRotation == ROTATE_RIGHT) {
            g2.translate(x, y);
            g2.rotate(NINETY_DEGREES);
            g2.drawString(fLabel, K_BUFFER_SPACE, -fDescent);
            g2.rotate(-NINETY_DEGREES);
            g2.translate(-x, -y);
        }
    }

    /**
	 * Returns the icon's width.
	 *
	 * @return an int specifying the fixed width of the icon.
	 */
    public int getIconWidth() {
        return fWidth;
    }

    /**
	 * Returns the icon's height.
	 *
	 * @return an int specifying the fixed height of the icon.
	 */
    public int getIconHeight() {
        return fHeight;
    }

    /** 
	 verifyRotation
	 
	 returns the best rotation for the string (ROTATE_NONE, ROTATE_LEFT, ROTATE_RIGHT)
	 
	 This is public static so you can use it to test a string without creating a VTextIcon
	 
	 from http://www.unicode.org/unicode/reports/tr9/tr9-3.html
	 When setting text using the Arabic script in vertical lines, 
	 it is more common to employ a horizontal baseline that 
	 is rotated by 90 degrees counterclockwise so that the characters 
	 are ordered from top to bottom. Latin text and numbers 
	 may be rotated 90 degrees clockwise so that the characters 
	 are also ordered from top to bottom.
	 
	 Rotation rules
	 - Roman can rotate left, right, or none - default right (counterclockwise)
	 - CJK can't rotate
	 - Arabic must rotate - default left (clockwise)
	 
	 from the online edition of _The Unicode Standard, Version 3.0_, file ch10.pdf page 4
	 Ideographs are found in three blocks of the Unicode Standard...
	 U+4E00-U+9FFF, U+3400-U+4DFF, U+F900-U+FAFF
	 
	 Hiragana is U+3040-U+309F, katakana is U+30A0-U+30FF
	 
	 from http://www.unicode.org/unicode/faq/writingdirections.html
	 East Asian scripts are frequently written in vertical lines 
	 which run from top-to-bottom and are arrange columns either 
	 from left-to-right (Mongolian) or right-to-left (other scripts). 
	 Most characters use the same shape and orientation when displayed 
	 horizontally or vertically, but many punctuation characters 
	 will change their shape when displayed vertically.

	 Letters and words from other scripts are generally rotated through 
	 ninety degree angles so that they, too, will read from top to bottom. 
	 That is, letters from left-to-right scripts will be rotated clockwise 
	 and letters from right-to-left scripts counterclockwise, both 
	 through ninety degree angles.

	 Unlike the bidirectional case, the choice of vertical layout 
	 is usually treated as a formatting style; therefore, 
	 the Unicode Standard does not define default rendering behavior 
	 for vertical text nor provide directionality controls designed to override such behavior

	 */
    public static int verifyRotation(String label, int rotateHint) {
        boolean hasCJK = false;
        boolean hasMustRotate = false;
        int len = label.length();
        char data[] = new char[len];
        char ch;
        label.getChars(0, len, data, 0);
        for (int i = 0; i < len; i++) {
            ch = data[i];
            if ((ch >= '一' && ch <= '鿿') || (ch >= '㐀' && ch <= '䷿') || (ch >= '豈' && ch <= '﫿') || (ch >= '぀' && ch <= 'ゟ') || (ch >= '゠' && ch <= 'ヿ')) hasCJK = true;
            if ((ch >= '֐' && ch <= '׿') || (ch >= '؀' && ch <= 'ۿ') || (ch >= '܀' && ch <= 'ݏ')) hasMustRotate = true;
        }
        if (hasCJK) return DEFAULT_CJK;
        int legal = hasMustRotate ? LEGAL_MUST_ROTATE : LEGAL_ROMAN;
        if ((rotateHint & legal) > 0) return rotateHint;
        return hasMustRotate ? DEFAULT_MUST_ROTATE : DEFAULT_ROMAN;
    }

    static final String IN_TOP_RIGHT = "ぁぃぅぇぉっゃゅょゎ" + "ァィゥェォッャュョヮヵヶ";

    static final String IN_FAR_TOP_RIGHT = "、。";

    static final int DEFAULT_CJK = ROTATE_NONE;

    static final int LEGAL_ROMAN = ROTATE_NONE | ROTATE_LEFT | ROTATE_RIGHT;

    static final int DEFAULT_ROMAN = ROTATE_RIGHT;

    static final int LEGAL_MUST_ROTATE = ROTATE_LEFT | ROTATE_RIGHT;

    static final int DEFAULT_MUST_ROTATE = ROTATE_LEFT;

    static final double NINETY_DEGREES = Math.toRadians(90.0);

    static final int K_BUFFER_SPACE = 5;
}
