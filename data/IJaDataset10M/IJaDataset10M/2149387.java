package net.sf.gridarta.textedit.textarea;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;
import net.sf.gridarta.textedit.textarea.tokenmarker.TokenMarker;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The text area repaint manager. It performs double buffering and paints lines
 * of text.
 * @author Slava Pestov
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public class TextAreaPainter extends JComponent implements TabExpander {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(TextAreaPainter.class);

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The font render context for this instance.
     */
    private final FontRenderContext fontRenderContext = new FontRenderContext(null, false, false);

    /**
     * The associated text area that is painted.
     * @serial
     */
    private final JEditTextArea textArea;

    /**
     * Whether the caret should be wide even in insert mode.
     * @serial
     */
    private final boolean blockCaret;

    /**
     * The syntax styles used to paint colorized text.
     */
    private SyntaxStyles styles;

    /**
     * The number of columns. Use to calculate the component's width.
     * @serial
     */
    private final int cols;

    /**
     * The number of rows. Use to calculate the component's height.
     * @serial
     */
    private final int rows;

    /**
     * The caret color.
     * @serial
     */
    private final Color caretColor;

    /**
     * The selection color.
     * @serial
     */
    private final Color selectionColor;

    /**
     * The color for line highlighting.
     * @serial
     */
    private final Color lineHighlightColor;

    /**
     * Whether line highlighting is enabled.
     * @serial
     */
    private final boolean lineHighlight;

    /**
     * The color for bracket highlighting.
     * @serial
     */
    private final Color bracketHighlightColor;

    /**
     * Whether bracket highlighting is enabled.
     * @serial
     */
    private final boolean bracketHighlight;

    /**
     * Whether invalid lines should be painted as red tildes.
     * @serial
     */
    private boolean paintInvalid;

    /**
     * The color for painting eol markers.
     * @serial
     */
    @NotNull
    private final Color eolMarkerColor;

    /**
     * Whether end of line markers should be painted.
     * @serial
     */
    private final boolean eolMarkers;

    /**
     * The currently painted line.
     * @serial
     */
    private int currentLineIndex = -1;

    /**
     * The tokens of the currently painted line.
     */
    @Nullable
    private List<Token> currentLineTokens = null;

    /**
     * Holds the currently painted line.
     */
    @NotNull
    private final Segment currentLine = new Segment();

    /**
     * The tab size in pixels.
     * @serial
     */
    private int tabSize = 0;

    /**
     * The font metrics for this instance.
     * @serial
     */
    @Nullable
    private FontMetrics fontMetrics = null;

    /**
     * The {@link Font} from which {@link #defaultLineHeight} and {@link
     * #defaultCharWidth} have been calculated. Set to <code>null</code> it not
     * yet calculated.
     * @serial
     */
    @Nullable
    private Font defaultFont = null;

    /**
     * The line height of {@link #defaultFont}. Unset if
     * <code>defaultFont==null</code>.
     * @serial
     */
    private int defaultLineHeight = 0;

    /**
     * The character width of {@link #defaultFont}. Unset if
     * <code>defaultCharWidth==null</code>.
     * @serial
     */
    private int defaultCharWidth = 0;

    /**
     * Creates a new repaint manager. This should be not be called directly.
     * @param textArea the associated text area that is painted
     * @param defaults the text attributes to use
     */
    public TextAreaPainter(@NotNull final JEditTextArea textArea, @NotNull final TextAreaDefaults defaults) {
        this.textArea = textArea;
        setAutoscrolls(true);
        setDoubleBuffered(true);
        setOpaque(true);
        ToolTipManager.sharedInstance().registerComponent(this);
        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        setFont(new Font("Monospaced", Font.PLAIN, 14));
        setForeground(Color.black);
        setBackground(Color.white);
        blockCaret = defaults.getBlockCaret();
        styles = defaults.getStyles();
        cols = defaults.getCols();
        rows = defaults.getRows();
        caretColor = defaults.getCaretColor();
        selectionColor = defaults.getSelectionColor();
        lineHighlightColor = defaults.getLineHighlightColor();
        lineHighlight = defaults.getLineHighlight();
        bracketHighlightColor = defaults.getBracketHighlightColor();
        bracketHighlight = defaults.getBracketHighlight();
        paintInvalid = defaults.getPaintInvalid();
        eolMarkerColor = defaults.getEolMarkerColor();
        eolMarkers = defaults.getEolMarkers();
    }

    /**
     * Make sure {@link #defaultCharWidth} and {@link #defaultLineHeight} are
     * up-to-date.
     */
    private void updateLineInfo() {
        final Font font = getFont();
        if (defaultFont != null && defaultFont.equals(font)) {
            return;
        }
        defaultCharWidth = (int) Math.round((double) new TextLayout("WgGhdJj", font, fontRenderContext).getAdvance() / 7.0);
        defaultLineHeight = Math.round(font.getLineMetrics("WgGhdJj", fontRenderContext).getHeight());
        defaultFont = font;
    }

    /**
     * This works only for fonts with fixed line height. It's sort of hack to
     * avoid the use of deprecated methods.
     * @return default line height in pixels
     */
    public int getDefaultLineHeight() {
        updateLineInfo();
        return defaultLineHeight;
    }

    /**
     * This works only for fonts with fixed line height. It's sort of hack to
     * avoid the use of deprecated methods.
     * @return default line height in pixels
     */
    public int getDefaultCharWidth() {
        updateLineInfo();
        return defaultCharWidth;
    }

    /**
     * Returns the syntax styles used to paint colorized text.
     * @return the styles
     * @see Token
     */
    @NotNull
    public SyntaxStyles getStyles() {
        return styles;
    }

    /**
     * Sets the syntax styles used to paint colorized text.
     * @param styles the syntax styles
     * @see Token
     */
    public void setStyles(@NotNull final SyntaxStyles styles) {
        this.styles = styles;
        repaint();
    }

    /**
     * Returns whether bracket highlighting is enabled. When bracket
     * highlighting is enabled, the bracket matching the one before the caret
     * (if any) is highlighted.
     * @return whether bracket highlighting is enabled
     */
    public boolean isBracketHighlightEnabled() {
        return bracketHighlight;
    }

    /**
     * Returns whether the caret should be drawn as a block.
     * @return whether the caret should be drawn as a block
     */
    public boolean isBlockCaretEnabled() {
        return blockCaret;
    }

    /**
     * Sets whether invalid lines are to be painted as red tildes.
     * @param paintInvalid whether invalid lines should be drawn
     */
    public void setInvalidLinesPainted(final boolean paintInvalid) {
        this.paintInvalid = paintInvalid;
        repaint();
    }

    /**
     * Returns the font metrics used by this component.
     * @return the font metrics
     */
    @Nullable
    public FontMetrics getFontMetrics() {
        return fontMetrics;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setFont(@NotNull final Font font) {
        super.setFont(font);
        fontMetrics = getFontMetrics(font);
        textArea.recalculateVisibleLines();
    }

    /**
     * Repaints the text.
     * @param g the graphics context
     */
    @Override
    public void paint(@NotNull final Graphics g) {
        tabSize = fontMetrics.charWidth(' ') * 4;
        final Rectangle clipRectangle = g.getClipBounds();
        g.setColor(getBackground());
        g.fillRect(clipRectangle.x, clipRectangle.y, clipRectangle.width, clipRectangle.height);
        final int height = fontMetrics.getHeight();
        final int firstLine = textArea.getFirstLine();
        final int firstInvalid = firstLine + clipRectangle.y / height;
        final int lastInvalid = firstLine + (clipRectangle.y + clipRectangle.height - 1) / height;
        try {
            final TokenMarker tokenMarker = textArea.getDocument().getTokenMarker();
            final int x = textArea.getHorizontalOffset();
            for (int line = firstInvalid; line <= lastInvalid; line++) {
                paintLine(g, tokenMarker, line, x);
            }
            if (tokenMarker != null && tokenMarker.isNextLineRequested()) {
                final int h = clipRectangle.y + clipRectangle.height;
                repaint(0, h, getWidth(), getHeight() - h);
            }
        } catch (final Exception e) {
            log.error("Error repainting line range {" + firstInvalid + ", " + lastInvalid + "}", e);
        }
    }

    /**
     * Marks a line as needing a repaint.
     * @param line the line to invalidate
     */
    public void invalidateLine(final int line) {
        repaint(0, textArea.lineToY(line) + fontMetrics.getMaxDescent() + fontMetrics.getLeading(), getWidth(), fontMetrics.getHeight());
    }

    /**
     * Marks a range of lines as needing a repaint.
     * @param firstLine the first line to invalidate
     * @param lastLine the last line to invalidate
     */
    public void invalidateLineRange(final int firstLine, final int lastLine) {
        repaint(0, textArea.lineToY(firstLine) + fontMetrics.getMaxDescent() + fontMetrics.getLeading(), getWidth(), (lastLine - firstLine + 1) * fontMetrics.getHeight());
    }

    /**
     * Repaints the lines containing the selection.
     */
    public void invalidateSelectedLines() {
        invalidateLineRange(textArea.getSelectionStartLine(), textArea.getSelectionEndLine());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float nextTabStop(final float x, final int tabOffset) {
        final int offset = textArea.getHorizontalOffset();
        final int nTabs = ((int) x - offset) / tabSize;
        return (float) ((nTabs + 1) * tabSize + offset);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Dimension getPreferredSize() {
        final Dimension dim = super.getPreferredSize();
        return dim != null ? dim : newDimension(cols, rows);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Dimension getMinimumSize() {
        final Dimension dim = super.getMinimumSize();
        return dim != null ? dim : newDimension(1, 1);
    }

    /**
     * Returns a {@link Dimension} measured in default character sizes.
     * @param columns the width in characters
     * @param rows the height in rows
     * @return the dimension
     */
    @NotNull
    private Dimension newDimension(final int columns, final int rows) {
        return new Dimension(columns * fontMetrics.charWidth('w'), rows * fontMetrics.getHeight());
    }

    /**
     * Paints one line.
     * @param gfx the graphics to paint to
     * @param tokenMarker the token marker to use
     * @param line the line index
     * @param x the x-offset for painting
     */
    private void paintLine(@NotNull final Graphics gfx, @Nullable final TokenMarker tokenMarker, final int line, final int x) {
        final Font font = getFont();
        final Color foreground = getForeground();
        currentLineIndex = line;
        final int y = textArea.lineToY(line);
        if (line < 0 || line >= textArea.getLineCount()) {
            if (paintInvalid) {
                paintHighlight(gfx, line, y);
                styles.getStyle(Token.INVALID).setGraphicsFlags(gfx, font);
                gfx.drawString("~", 0, y + fontMetrics.getHeight());
            }
        } else if (tokenMarker == null) {
            paintPlainLine(gfx, line, font, foreground, x, y);
        } else {
            paintSyntaxLine(gfx, tokenMarker, line, font, foreground, x, y);
        }
    }

    /**
     * Paint a line without token highlighting.
     * @param gfx the graphics to paint to
     * @param line the line index
     * @param defaultFont the font to use
     * @param defaultColor the color to use
     * @param x the x-offset for painting
     * @param y the y-offset for painting
     */
    private void paintPlainLine(@NotNull final Graphics gfx, final int line, @NotNull final Font defaultFont, @NotNull final Color defaultColor, final int x, final int y) {
        paintHighlight(gfx, line, y);
        textArea.getLineText(line, currentLine);
        gfx.setFont(defaultFont);
        gfx.setColor(defaultColor);
        final int yy = y + fontMetrics.getHeight();
        final int xx = Utilities.drawTabbedText(currentLine, x, yy, gfx, this, 0);
        if (eolMarkers) {
            gfx.setColor(eolMarkerColor);
            gfx.drawString(".", xx, yy);
        }
    }

    /**
     * Paint a line with token highlighting.
     * @param gfx the graphics to paint to
     * @param tokenMarker the token marker to use
     * @param line the line index
     * @param defaultFont the font to use
     * @param defaultColor the color to use
     * @param x the x-offset for painting
     * @param y the y-offset for painting
     */
    private void paintSyntaxLine(@NotNull final Graphics gfx, @NotNull final TokenMarker tokenMarker, final int line, @NotNull final Font defaultFont, @NotNull final Color defaultColor, final int x, final int y) {
        textArea.getLineText(currentLineIndex, currentLine);
        currentLineTokens = tokenMarker.markTokens(currentLine, currentLineIndex);
        paintHighlight(gfx, line, y);
        gfx.setFont(defaultFont);
        gfx.setColor(defaultColor);
        final int yy = y + fontMetrics.getHeight();
        final int xx = SyntaxUtilities.paintSyntaxLine(currentLine, currentLineTokens, styles, this, gfx, x, yy);
        if (eolMarkers) {
            gfx.setColor(eolMarkerColor);
            gfx.drawString(".", xx, yy);
        }
    }

    /**
     * Adds highlights for a line: selections, custom highlights from client
     * code, brackets, caret.
     * @param gfx the graphics to paint to
     * @param line the line index
     * @param y the y-offset for painting
     */
    private void paintHighlight(@NotNull final Graphics gfx, final int line, final int y) {
        if (line >= textArea.getSelectionStartLine() && line <= textArea.getSelectionEndLine()) {
            paintLineHighlight(gfx, line, y);
        }
        if (bracketHighlight && line == textArea.getBracketLine()) {
            paintBracketHighlight(gfx, line, y);
        }
        if (line == textArea.getCaretLine()) {
            paintCaret(gfx, line, y);
        }
    }

    /**
     * Paints the selection highlight.
     * @param gfx the graphics to paint to
     * @param line the line index
     * @param y the y-offset for painting
     */
    private void paintLineHighlight(@NotNull final Graphics gfx, final int line, final int y) {
        final int height = fontMetrics.getHeight();
        final int yy = y + fontMetrics.getLeading() + fontMetrics.getMaxDescent();
        final int selectionStart = textArea.getSelectionStart();
        final int selectionEnd = textArea.getSelectionEnd();
        if (selectionStart == selectionEnd) {
            if (lineHighlight) {
                gfx.setColor(lineHighlightColor);
                gfx.fillRect(0, yy, getWidth(), height);
            }
        } else {
            gfx.setColor(selectionColor);
            final int selectionStartLine = textArea.getSelectionStartLine();
            final int selectionEndLine = textArea.getSelectionEndLine();
            final int lineStart = textArea.getLineStartOffset(line);
            final int x1;
            final int x2;
            if (textArea.isSelectionRectangular()) {
                final int lineLen = textArea.getLineLength(line);
                x1 = textArea.offsetToX2(line, Math.min(lineLen, selectionStart - textArea.getLineStartOffset(selectionStartLine)));
                final int x3 = textArea.offsetToX2(line, Math.min(lineLen, selectionEnd - textArea.getLineStartOffset(selectionEndLine)));
                x2 = x3 + (x1 == x3 ? 1 : 0);
            } else if (selectionStartLine == selectionEndLine) {
                x1 = textArea.offsetToX2(line, selectionStart - lineStart);
                x2 = textArea.offsetToX2(line, selectionEnd - lineStart);
            } else if (line == selectionStartLine) {
                x1 = textArea.offsetToX2(line, selectionStart - lineStart);
                x2 = getWidth();
            } else if (line == selectionEndLine) {
                x1 = 0;
                x2 = textArea.offsetToX2(line, selectionEnd - lineStart);
            } else {
                x1 = 0;
                x2 = getWidth();
            }
            gfx.fillRect(Math.min(x1, x2), yy, Math.abs(x1 - x2), height);
        }
    }

    /**
     * Paints the bracket highlight.
     * @param gfx the graphics to paint to
     * @param line the line index
     * @param y the y-offset for painting
     */
    private void paintBracketHighlight(@NotNull final Graphics gfx, final int line, final int y) {
        final int position = textArea.getBracketPosition();
        if (position == -1) {
            return;
        }
        final int yy = y + fontMetrics.getLeading() + fontMetrics.getMaxDescent();
        final int xx = textArea.offsetToX2(line, position);
        gfx.setColor(bracketHighlightColor);
        gfx.drawRect(xx, yy, fontMetrics.charWidth('(') - 1, fontMetrics.getHeight() - 1);
    }

    /**
     * Paints the caret highlight.
     * @param gfx the graphics to paint to
     * @param line the line index
     * @param y the y-offset for painting
     */
    private void paintCaret(@NotNull final Graphics gfx, final int line, final int y) {
        if (textArea.isCaretVisible()) {
            final int offset = textArea.getCaretPosition() - textArea.getLineStartOffset(line);
            final int caretX = textArea.offsetToX2(line, offset);
            final int caretWidth = blockCaret || textArea.isOverwriteEnabled() ? fontMetrics.charWidth('w') : 1;
            final int yy = y + fontMetrics.getLeading() + fontMetrics.getMaxDescent();
            final int height = fontMetrics.getHeight();
            gfx.setColor(caretColor);
            if (textArea.isOverwriteEnabled()) {
                gfx.fillRect(caretX, yy + height - 1, caretWidth, 1);
            } else {
                if (caretWidth <= 1) {
                    gfx.drawLine(caretX, yy, caretX, yy + height - 1);
                } else {
                    gfx.drawRect(caretX, yy, caretWidth - 1, height - 1);
                }
            }
        }
    }

    /**
     * Returns the currently painted line.
     * @return the line index
     */
    public int getCurrentLineIndex() {
        return currentLineIndex;
    }

    /**
     * Sets the currently painted line.
     * @param lineIndex the line index
     */
    public void setCurrentLineIndex(final int lineIndex) {
        currentLineIndex = lineIndex;
    }

    /**
     * Returns the tokens of the currently painted line.
     * @return the tokens
     */
    @Nullable
    public List<Token> getCurrentLineTokens() {
        return currentLineTokens == null ? null : Collections.unmodifiableList(currentLineTokens);
    }

    /**
     * Sets the tokens of the currently painted line.
     * @param tokens the tokens
     */
    public void setCurrentLineTokens(@Nullable final List<Token> tokens) {
        currentLineTokens = tokens == null ? null : new LinkedList<Token>(tokens);
    }
}
