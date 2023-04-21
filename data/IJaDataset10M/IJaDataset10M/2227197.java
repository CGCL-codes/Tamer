package werkzeugkasten.editor.text;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPaintPositionManager;
import org.eclipse.jface.text.IPainter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * A painter for drawing visible characters for (invisible) whitespace
 * characters.
 * 
 * @since 3.3
 */
public class WhitespaceCharacterPainter implements IPainter, PaintListener {

    private static final char SPACE_SIGN = '▁';

    private static final char IDEOGRAPHIC_SPACE_SIGN = '□';

    private static final char TAB_SIGN = '»';

    private static final char CARRIAGE_RETURN_SIGN = '¤';

    private static final char CR_LF = '↵';

    private static final char LINE_FEED_SIGN = '↴';

    /** Indicates whether this painter is active. */
    private boolean fIsActive = false;

    /** The source viewer this painter is attached to. */
    private ITextViewer fTextViewer;

    /** The viewer's widget. */
    private StyledText fTextWidget;

    /** Tells whether the advanced graphics sub system is available. */
    private boolean fIsAdvancedGraphicsPresent;

    /**
	 * Creates a new painter for the given text viewer.
	 * 
	 * @param textViewer
	 *            the text viewer the painter should be attached to
	 */
    public WhitespaceCharacterPainter(ITextViewer textViewer) {
        super();
        fTextViewer = textViewer;
        fTextWidget = textViewer.getTextWidget();
        GC gc = new GC(fTextWidget);
        gc.setAdvanced(true);
        fIsAdvancedGraphicsPresent = gc.getAdvanced();
        gc.dispose();
    }

    public void dispose() {
        fTextViewer = null;
        fTextWidget = null;
    }

    public void paint(int reason) {
        IDocument document = fTextViewer.getDocument();
        if (document == null) {
            deactivate(false);
            return;
        }
        if (!fIsActive) {
            fIsActive = true;
            fTextWidget.addPaintListener(this);
            redrawAll(true);
        } else if (reason == CONFIGURATION || reason == INTERNAL) {
            redrawAll(false);
        } else if (reason == TEXT_CHANGE) {
            try {
                IRegion lineRegion = document.getLineInformationOfOffset(getDocumentOffset(fTextWidget.getCaretOffset()));
                int widgetOffset = getWidgetOffset(lineRegion.getOffset());
                int charCount = fTextWidget.getCharCount();
                int redrawLength = Math.min(lineRegion.getLength(), charCount - widgetOffset);
                if (widgetOffset >= 0 && redrawLength > 0) {
                    fTextWidget.redrawRange(widgetOffset, redrawLength, true);
                }
            } catch (BadLocationException e) {
            }
        }
    }

    public void deactivate(boolean redraw) {
        if (fIsActive) {
            fIsActive = false;
            fTextWidget.removePaintListener(this);
            if (redraw) {
                redrawAll(true);
            }
        }
    }

    public void setPositionManager(IPaintPositionManager manager) {
    }

    public void paintControl(PaintEvent event) {
        if (fTextWidget != null) {
            handleDrawRequest(event.gc, event.x, event.y, event.width, event.height);
        }
    }

    /**
	 * Draw characters in view range.
	 * 
	 * @param gc
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
    private void handleDrawRequest(GC gc, int x, int y, int w, int h) {
        int lineCount = fTextWidget.getLineCount();
        int startLine = (y + fTextWidget.getTopPixel()) / fTextWidget.getLineHeight();
        int endLine = (y + h - 1 + fTextWidget.getTopPixel()) / fTextWidget.getLineHeight();
        if (startLine <= endLine && startLine < lineCount) {
            int startOffset = fTextWidget.getOffsetAtLine(startLine);
            int endOffset = endLine < lineCount - 1 ? fTextWidget.getOffsetAtLine(endLine + 1) : fTextWidget.getCharCount();
            if (fIsAdvancedGraphicsPresent) {
                int alpha = gc.getAlpha();
                gc.setAlpha(100);
                handleDrawRequest(gc, startOffset, endOffset);
                gc.setAlpha(alpha);
            } else handleDrawRequest(gc, startOffset, endOffset);
        }
    }

    /**
	 * Draw characters of content range.
	 * 
	 * @param gc
	 *            the GC
	 * @param startOffset
	 *            inclusive start index
	 * @param endOffset
	 *            exclusive end index
	 */
    private void handleDrawRequest(GC gc, int startOffset, int endOffset) {
        StyledTextContent content = fTextWidget.getContent();
        int length = endOffset - startOffset;
        String text = content.getTextRange(startOffset, length);
        StyleRange styleRange = null;
        Color fg = null;
        Point selection = fTextWidget.getSelection();
        StringBuffer visibleChar = new StringBuffer(10);
        for (int textOffset = 0; textOffset <= length; ++textOffset) {
            int delta = 0;
            boolean eol = false;
            if (textOffset < length) {
                delta = 1;
                char c = text.charAt(textOffset);
                switch(c) {
                    case ' ':
                        break;
                    case '　':
                        visibleChar.append(IDEOGRAPHIC_SPACE_SIGN);
                        break;
                    case '\t':
                        visibleChar.append(TAB_SIGN);
                        break;
                    case '\r':
                        if (textOffset >= length - 1 || text.charAt(textOffset + 1) != '\n') {
                            visibleChar.append(CARRIAGE_RETURN_SIGN);
                            eol = true;
                            break;
                        }
                        continue;
                    case '\n':
                        if (1 < text.length() && text.charAt(textOffset - 1) == '\r') {
                            visibleChar.append(CR_LF);
                        } else {
                            visibleChar.append(LINE_FEED_SIGN);
                        }
                        eol = true;
                        break;
                    default:
                        delta = 0;
                        break;
                }
            }
            if (visibleChar.length() > 0) {
                int widgetOffset = startOffset + textOffset - visibleChar.length() + delta;
                if (!eol || !isFoldedLine(content.getLineAtOffset(widgetOffset))) {
                    if (widgetOffset >= selection.x && widgetOffset < selection.y) {
                        fg = fTextWidget.getSelectionForeground();
                    } else if (styleRange == null || styleRange.start + styleRange.length <= widgetOffset) {
                        styleRange = fTextWidget.getStyleRangeAtOffset(widgetOffset);
                        if (styleRange == null || styleRange.foreground == null) {
                            fg = fTextWidget.getForeground();
                        } else {
                            fg = styleRange.foreground;
                        }
                    }
                    draw(gc, widgetOffset, visibleChar.toString(), fg);
                }
                visibleChar.delete(0, visibleChar.length());
            }
        }
    }

    /**
	 * Check if the given widget line is a folded line.
	 * 
	 * @param widgetLine
	 *            the widget line number
	 * @return <code>true</code> if the line is folded
	 */
    private boolean isFoldedLine(int widgetLine) {
        if (fTextViewer instanceof ITextViewerExtension5) {
            ITextViewerExtension5 extension = (ITextViewerExtension5) fTextViewer;
            int modelLine = extension.widgetLine2ModelLine(widgetLine);
            int widgetLine2 = extension.modelLine2WidgetLine(modelLine + 1);
            return widgetLine2 == -1;
        }
        return false;
    }

    /**
	 * Redraw all of the text widgets visible content.
	 * 
	 * @param redrawBackground
	 *            If true, clean background before painting text.
	 */
    private void redrawAll(boolean redrawBackground) {
        int startLine = fTextWidget.getTopPixel() / fTextWidget.getLineHeight();
        int startOffset = fTextWidget.getOffsetAtLine(startLine);
        int endLine = 1 + (fTextWidget.getTopPixel() + fTextWidget.getClientArea().height) / fTextWidget.getLineHeight();
        int endOffset;
        if (endLine >= fTextWidget.getLineCount()) {
            endOffset = fTextWidget.getCharCount();
        } else {
            endOffset = fTextWidget.getOffsetAtLine(endLine);
        }
        if (startOffset < endOffset) {
            endOffset = Math.min(endOffset + 2, fTextWidget.getCharCount());
            int redrawOffset = startOffset;
            int redrawLength = endOffset - redrawOffset;
            fTextWidget.redrawRange(startOffset, redrawLength, redrawBackground);
        }
    }

    /**
	 * Draw string at widget offset.
	 * 
	 * @param gc
	 * @param offset
	 *            the widget offset
	 * @param s
	 *            the string to be drawn
	 * @param fg
	 *            the foreground color
	 */
    private void draw(GC gc, int offset, String s, Color fg) {
        int baseline = fTextWidget.getBaseline(offset);
        FontMetrics fontMetrics = gc.getFontMetrics();
        int fontBaseline = fontMetrics.getAscent() + fontMetrics.getLeading();
        int baslineDelta = baseline - fontBaseline;
        Point pos = fTextWidget.getLocationAtOffset(offset);
        gc.setForeground(fg);
        gc.drawString(s, pos.x, pos.y + baslineDelta, true);
    }

    /**
	 * Convert a document offset to the corresponding widget offset.
	 * 
	 * @param documentOffset
	 * @return widget offset
	 */
    private int getWidgetOffset(int documentOffset) {
        if (fTextViewer instanceof ITextViewerExtension5) {
            ITextViewerExtension5 extension = (ITextViewerExtension5) fTextViewer;
            return extension.modelOffset2WidgetOffset(documentOffset);
        }
        IRegion visible = fTextViewer.getVisibleRegion();
        int widgetOffset = documentOffset - visible.getOffset();
        if (widgetOffset > visible.getLength()) {
            return -1;
        }
        return widgetOffset;
    }

    /**
	 * Convert a widget offset to the corresponding document offset.
	 * 
	 * @param widgetOffset
	 * @return document offset
	 */
    private int getDocumentOffset(int widgetOffset) {
        if (fTextViewer instanceof ITextViewerExtension5) {
            ITextViewerExtension5 extension = (ITextViewerExtension5) fTextViewer;
            return extension.widgetOffset2ModelOffset(widgetOffset);
        }
        IRegion visible = fTextViewer.getVisibleRegion();
        if (widgetOffset > visible.getLength()) {
            return -1;
        }
        return widgetOffset + visible.getOffset();
    }

    public ITextViewer getTextViewer() {
        return this.fTextViewer;
    }
}
