package tasklist;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.text.Segment;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.syntax.*;
import org.gjt.sp.jedit.textarea.*;
import org.gjt.sp.util.Log;

/**
 * A class extending jEdit's TextAreaExtension class
 * that, when enabled, draws a wavy line underscoring a task item
 * appearing in the text of a buffer.
 */
public class TaskHighlight extends TextAreaExtension {

    /**
	 * Constructs a TextHighlight object
	 * @param textArea The text area
	 */
    public TaskHighlight(JEditTextArea textArea) {
        super();
        this.textArea = textArea;
        this.highlightEnabled = jEdit.getBooleanProperty("tasklist.highlight.tasks");
        this.seg = new Segment();
        this.point = new Point();
    }

    /**
	 * Returns whether highlighting of task items is currently enabled.
	 *
	 * @return the value true or false indicating whther highlighting
	 * is enabled
	 */
    public boolean isEnabled() {
        return highlightEnabled;
    }

    /**
	 * Set whether highlighting of task items is enabled; does not
	 * redraw the text area after a change in state.
	 *
	 * @param the new state for task highlighting
	 */
    public void setEnabled(boolean enabled) {
        highlightEnabled = enabled;
    }

    /**
	 * Called by the text area to paint a highlight on a task line
	 * (when highlighting is enabled)
	 * @param gfx The graphics context
	 * @param screenLine The screen line number
	 * @param physicalLine The physical line number
	 * @param start The offset where the screen line begins, from the start of the buffer
	 * @param end The offset where the screen line ends, from the start of the buffer
	 * @param y The y co-ordinate of the top of the line's bounding box
	 */
    public void paintValidLine(Graphics2D gfx, int screenLine, int physicalLine, int start, int end, int y) {
        Buffer buffer = (Buffer) textArea.getBuffer();
        if (!highlightEnabled || !buffer.isLoaded() || physicalLine >= buffer.getLineCount()) {
            return;
        }
        HashMap<Integer, Task> taskMap = TaskListPlugin.requestTasksForBuffer(buffer);
        if (taskMap != null) {
            Task task = null;
            Integer _line = Integer.valueOf(physicalLine);
            if (!buffer.isDirty()) {
                task = (Task) taskMap.get(_line);
            } else {
                for (Task _task : taskMap.values()) {
                    if (_task.getLineNumber() == _line.intValue()) {
                        task = _task;
                        break;
                    }
                }
            }
            if (task != null) {
                System.out.println(task);
                FontMetrics fm = textArea.getPainter().getFontMetrics();
                underlineTask(task, gfx, physicalLine, start, end, y + fm.getAscent());
            }
        }
    }

    /**
	 * Returns the tool tip to display at the specified location.
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
    public java.lang.String getToolTipText(int x, int y) {
        return super.getToolTipText(x, y);
    }

    /**
	 * The textArea on which the highlight will be drawn.
	 */
    private final JEditTextArea textArea;

    /**
	 * A flag indicating whether highlighting of task items
	 * is currently enabled.
	 */
    private boolean highlightEnabled;

    /**
	 * A portion of text to be highlighted.
	 */
    private final Segment seg;

    /**
	 * A point for anchor the highlighting of taks text
	 */
    private final Point point;

    /**
	 * Implements underlining of task items through a call to
	 * paintWavyLine()
	 *
	 * @param task the Task that is the subject of highlighting
	 * @param gfx The graphics context
	 * @param line The physical line number
	 * @param _start The offset where the line begins
	 * @param _end The offset where the line ends
	 * @param y The y co-ordinate of the line
	 */
    private void underlineTask(Task task, Graphics2D gfx, int line, int _start, int _end, int y) {
        int start = task.getStartOffset();
        int end = task.getEndOffset();
        if (start == 0 && end == 0) {
            textArea.getLineText(line, seg);
            for (int j = 0; j < seg.count; j++) {
                if (Character.isWhitespace(seg.array[seg.offset + j])) start++; else break;
            }
            end = seg.count;
        }
        try {
            if (start + textArea.getLineStartOffset(line) >= _start) start = textArea.offsetToXY(line, start, point).x; else start = 0;
            if (end + textArea.getLineStartOffset(line) >= _end) end = textArea.offsetToXY(line, _end - 1, point).x; else end = textArea.offsetToXY(line, end, point).x;
        } catch (NullPointerException npe) {
            Log.log(Log.ERROR, this, "NullPointerException in TaskHighlight.underlineTask():" + task.getBufferPath() + ":" + task.getLineIndex());
        }
        gfx.setColor(TaskListPlugin.getHighlightColor());
        gfx.drawLine(start, y + 1, end, y + 1);
    }
}
