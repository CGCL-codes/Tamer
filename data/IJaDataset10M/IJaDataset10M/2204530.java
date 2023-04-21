package jdiff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import jdiff.util.Diff;
import org.gjt.sp.jedit.EditPane;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaExtension;
import org.gjt.sp.jedit.textarea.TextAreaPainter;

public class DiffHighlight extends TextAreaExtension {

    public static final Position LEFT = new Position();

    public static final Position RIGHT = new Position();

    public static class Position {

        public Position() {
        }
    }

    private static HashMap<EditPane, TextAreaExtension> highlights = new HashMap<EditPane, TextAreaExtension>();

    private JEditTextArea textArea;

    private boolean enabled = false;

    private Diff.Change edits;

    private Position position;

    private DiffHighlight(JEditTextArea textArea, Diff.Change edits, Position position) {
        this.textArea = textArea;
        this.edits = edits;
        this.position = position;
    }

    public void paintValidLine(Graphics2D gfx, final int screenLine, final int physicalLine, final int start, final int end, final int y) {
        if (this.isEnabled()) {
            if (this.textArea.getLineStartOffset(physicalLine) == -1 || this.textArea.getLineEndOffset(physicalLine) == -1) {
                return;
            }
            Diff.Change hunk = this.edits;
            Color color;
            if (this.position == DiffHighlight.LEFT) {
                for (; hunk != null; hunk = hunk.next) {
                    if (hunk.first0 <= physicalLine && physicalLine <= hunk.last0) {
                        TextAreaPainter painter = this.textArea.getPainter();
                        int height = hunk.lines0 == 0 ? y : painter.getLineHeight();
                        if (hunk.lines0 == 0) {
                            if (hunk.first0 != physicalLine) {
                                continue;
                            }
                            color = JDiffPlugin.overviewInvalidColor;
                            gfx.setColor(color);
                            gfx.drawLine(0, y, painter.getWidth() - 1, height);
                            continue;
                        }
                        color = hunk.lines1 == 0 ? JDiffPlugin.overviewDeletedColor : JDiffPlugin.overviewChangedColor;
                        gfx.setColor(color);
                        gfx.fillRect(0, y, painter.getWidth(), height);
                        break;
                    }
                }
            } else {
                for (; hunk != null; hunk = hunk.next) {
                    if (hunk.first1 <= physicalLine && physicalLine <= hunk.last1) {
                        TextAreaPainter painter = this.textArea.getPainter();
                        int height = hunk.lines1 == 0 ? y : painter.getLineHeight();
                        if (hunk.lines1 == 0) {
                            if (hunk.first1 != physicalLine) {
                                continue;
                            }
                            color = JDiffPlugin.overviewInvalidColor;
                            gfx.setColor(color);
                            gfx.drawLine(0, y, painter.getWidth() - 1, height);
                            continue;
                        }
                        color = hunk.lines0 == 0 ? JDiffPlugin.overviewInsertedColor : JDiffPlugin.overviewChangedColor;
                        gfx.setColor(color);
                        gfx.fillRect(0, y, painter.getWidth(), height);
                        break;
                    }
                }
            }
        }
    }

    public void paintInvalidLine(Graphics2D gfx, int screenLine, int y) {
        if (screenLine == textArea.getLineCount()) {
            Diff.Change hunk = this.edits;
            Color color;
            if (this.position == DiffHighlight.LEFT) {
                for (; hunk != null; hunk = hunk.next) {
                    if (hunk.first0 <= screenLine && screenLine <= hunk.last0) {
                        TextAreaPainter painter = this.textArea.getPainter();
                        color = JDiffPlugin.overviewInvalidColor;
                        gfx.setColor(color);
                        gfx.drawLine(0, y, painter.getWidth() - 1, y);
                        break;
                    }
                }
            } else {
                for (; hunk != null; hunk = hunk.next) {
                    if (hunk.first1 <= screenLine && screenLine <= hunk.last1) {
                        TextAreaPainter painter = this.textArea.getPainter();
                        color = JDiffPlugin.overviewInvalidColor;
                        gfx.setColor(color);
                        gfx.drawLine(0, y, painter.getWidth() - 1, y);
                        break;
                    }
                }
            }
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggleEnabled() {
        this.enabled = !this.enabled;
    }

    public Diff.Change getEdits() {
        return this.edits;
    }

    public void setEdits(Diff.Change edits) {
        this.edits = edits;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void updateTextArea() {
        if (this.textArea == null) {
            return;
        }
        int first = this.textArea.getFirstLine();
        int last = first + this.textArea.getVisibleLines();
        this.textArea.invalidateLineRange(first, last);
    }

    /**
     * Tests if the diff highlights are enabled for an editPane
     */
    public static boolean isDiffHighlightEnabledFor(EditPane editPane) {
        DiffHighlight highlight = (DiffHighlight) highlights.get(editPane);
        if (highlight != null) {
            return highlight.isEnabled();
        }
        return false;
    }

    /**
     * Sets diff highlighting to enabled or disabled for an editPane
     */
    public static void setDiffHighlightFor(EditPane editPane, boolean enabled) {
        DiffHighlight highlight = (DiffHighlight) highlights.get(editPane);
        if (highlight != null) {
            highlight.setEnabled(enabled);
            highlight.updateTextArea();
        }
    }

    /**
     * Enables diff highlights for an editPane
     */
    public static void enableDiffHighlightFor(EditPane editPane) {
        DiffHighlight.setDiffHighlightFor(editPane, true);
    }

    /**
     * Disables diff highlights for an editPane
     */
    public static void disableDiffHighlightFor(EditPane editPane) {
        DiffHighlight.setDiffHighlightFor(editPane, false);
    }

    public static TextAreaExtension getHighlightFor(EditPane editPane) {
        return (TextAreaExtension) highlights.get(editPane);
    }

    public static TextAreaExtension addHighlightTo(EditPane editPane, Diff.Change edits, Position position) {
        TextAreaExtension textAreaHighlight = new DiffHighlight(editPane.getTextArea(), edits, position);
        highlights.put(editPane, textAreaHighlight);
        return textAreaHighlight;
    }

    public static void removeHighlightFrom(EditPane editPane) {
        highlights.remove(editPane);
    }
}
