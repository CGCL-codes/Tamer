package org.iceinn.iceeditor.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import org.iceinn.iceeditor.document.IceConstants;
import org.iceinn.tools.notification.NotificationCenter;
import org.iceinn.tools.notification.NotificationListener;

/**
 * @see http://www.developer.com/java/other/article.php/3318421
 * @author Lionel FLAHAUT
 * 
 */
public class NumberedParagraphView extends ParagraphView implements NotificationListener {

    private static int NUMBERS_WIDTH = 25;

    int lineToHighlight = -1;

    public NumberedParagraphView(Element elem) {
        super(elem);
        resetInsets();
        NotificationCenter.current().registerNewListener(IceConstants.CARET_OFFSET, this);
    }

    @Override
    public void changedUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
        super.changedUpdate(changes, a, f);
        resetInsets();
    }

    private void resetInsets() {
        short top = 0;
        short left = 0;
        short bottom = 0;
        short right = 0;
        this.setInsets(top, left, bottom, right);
    }

    protected void setInsets(short top, short left, short bottom, short right) {
        super.setInsets(top, (short) (left + NUMBERS_WIDTH), bottom, right);
    }

    /**
	 * Returns the previous line count.
	 * 
	 * @return
	 */
    private int getPreviousLineCount() {
        int lineCount = 0;
        View parent = this.getParent();
        int count = parent.getViewCount();
        for (int i = 0; i < count; i++) {
            if (parent.getView(i) == this) {
                break;
            } else {
                lineCount += parent.getView(i).getViewCount();
            }
        }
        return lineCount;
    }

    public void paintChild(Graphics g, Rectangle r, int n) {
        super.paintChild(g, r, n);
        FontMetrics fontMetrics = g.getFontMetrics();
        int previousLineCount = getPreviousLineCount();
        int lineN = previousLineCount + n + 1;
        String lineNumber = Integer.toString(lineN);
        int stringWidth = fontMetrics.stringWidth(lineNumber);
        int numberX = r.x - stringWidth - 4;
        int numberY = r.y + r.height - fontMetrics.getHeight() / 4;
        Graphics2D g2d = (Graphics2D) g;
        Color backColor = g2d.getColor();
        Font backFont = g2d.getFont();
        g2d.setColor(Color.decode("#e2e2e2"));
        g2d.fillRect(r.x - getLeftInset(), r.y, getLeftInset() - 3, r.height);
        g2d.setColor(backColor);
        if (lineToHighlight == n) {
            g2d.setFont(backFont.deriveFont(Font.BOLD));
        }
        g2d.drawString(lineNumber, numberX, numberY);
        g2d.setFont(backFont);
        g2d.setColor(backColor);
    }

    public void handleNotification(String key, Class notifier, Object newValue) {
        int caretOffset = ((Integer) newValue).intValue();
        int viewIndex = getViewIndexAtPosition(caretOffset);
        lineToHighlight = viewIndex;
    }
}
