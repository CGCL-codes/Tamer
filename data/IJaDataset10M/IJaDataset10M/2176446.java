package gatchan.jedit.hyperlinks;

import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaExtension;
import java.awt.*;

/**
 * @author Matthieu Casanova
 * @version $Id: Server.java,v 1.33 2007/01/05 15:15:17 matthieu Exp $
 */
public class HyperlinkTextAreaPainter extends TextAreaExtension {

    private final JEditTextArea textArea;

    private Hyperlink hyperLink;

    static Color color;

    public HyperlinkTextAreaPainter(JEditTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void paintValidLine(Graphics2D gfx, int screenLine, int physicalLine, int start, int end, int y) {
        Hyperlink link = hyperLink;
        if (link == null) {
            textArea.getPainter().resetCursor();
            return;
        }
        if (link.getStartLine() != physicalLine) return;
        Point startPoint = textArea.offsetToXY(link.getStartOffset());
        if (startPoint == null) {
            textArea.getPainter().resetCursor();
            return;
        }
        int startX = startPoint.x;
        Point endPoint = textArea.offsetToXY(link.getEndOffset());
        if (endPoint == null) {
            textArea.getPainter().resetCursor();
            return;
        }
        int endX = endPoint.x;
        gfx.setColor(color);
        FontMetrics fm = textArea.getPainter().getFontMetrics();
        y += fm.getAscent();
        gfx.drawLine(startX, y + 1, endX, y + 1);
        textArea.getPainter().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public String getToolTipText(int x, int y) {
        Hyperlink link = hyperLink;
        if (link == null) return null;
        int offset = textArea.xyToOffset(x, y);
        if (hyperLink.getStartOffset() <= offset && hyperLink.getEndOffset() >= offset) {
            return hyperLink.getTooltip();
        }
        return null;
    }

    public Hyperlink getHyperLink() {
        return hyperLink;
    }

    public void setHyperLink(Hyperlink hyperLink) {
        if (hyperLink != this.hyperLink) {
            if (hyperLink == null) {
                int line = this.hyperLink.getStartLine();
                textArea.invalidateLine(line);
            } else {
                int lineNew = hyperLink.getStartLine();
                textArea.invalidateLine(lineNew);
                if (this.hyperLink != null) {
                    int lineOld = this.hyperLink.getStartLine();
                    textArea.invalidateLine(lineOld);
                }
            }
            this.hyperLink = hyperLink;
        }
    }
}
