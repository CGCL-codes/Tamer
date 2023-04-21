package jdiff.component;

import javax.swing.JComponent;
import jdiff.DualDiff;
import jdiff.util.Diff;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.JEditTextArea;

public abstract class DiffOverview extends JComponent {

    protected DualDiff dualDiff;

    private DiffTextAreaModel model = null;

    protected Diff.Change edits;

    protected int lineCount0;

    protected int lineCount1;

    protected JEditTextArea textArea0;

    protected JEditTextArea textArea1;

    public DiffOverview() {
        this(null);
    }

    public DiffOverview(DualDiff dualDiff) {
        this.dualDiff = dualDiff;
        setModel(new DiffTextAreaModel(dualDiff));
    }

    public void setModel(DiffTextAreaModel model) {
        this.model = model;
        edits = model.getEdits();
        lineCount0 = model.getLeftLineCount();
        lineCount1 = model.getRightLineCount();
        textArea0 = model.getLeftTextArea();
        textArea1 = model.getRightTextArea();
    }

    public DiffTextAreaModel getModel() {
        return model;
    }

    public void synchroScrollRight() {
        if (!jEdit.getBooleanProperty("jdiff.synchroscroll-on", true)) {
            return;
        }
        Diff.Change hunk = edits;
        int leftFirstLine = textArea0.getFirstLine();
        int rightFirstLine = textArea1.getFirstLine();
        int rightMaxFirstLine = textArea1.getLineCount() - textArea1.getVisibleLines() + 1;
        if (hunk == null) {
            textArea1.setFirstLine(Math.min(leftFirstLine, rightMaxFirstLine));
            return;
        }
        for (; hunk != null; hunk = hunk.next) {
            if (leftFirstLine < hunk.first0 && hunk.prev == null) {
                textArea1.setFirstLine(leftFirstLine);
                return;
            }
            if (leftFirstLine >= hunk.first0 && leftFirstLine < hunk.first0 + hunk.lines0) {
                int distance = 0;
                if (hunk.lines0 == 0 && hunk.lines1 > 0) {
                    distance = rightFirstLine - hunk.first1;
                } else if (hunk.lines0 > 0 && hunk.lines1 == 0) {
                    distance = 0;
                } else if (hunk.lines0 == hunk.lines1) {
                    distance = leftFirstLine - hunk.first0;
                } else if (hunk.lines0 > hunk.lines1) {
                    int left_increment = hunk.lines0 / hunk.lines1;
                    int right_increment = (int) Math.round((float) (hunk.lines1 * left_increment) / (float) hunk.lines0);
                    distance = ((leftFirstLine - hunk.first0) / left_increment) * right_increment;
                } else if (hunk.lines0 < hunk.lines1) {
                    int right_increment = hunk.lines1 / hunk.lines0;
                    int left_increment = (int) Math.round((float) (hunk.lines0 * right_increment) / (float) hunk.lines1);
                    distance = ((leftFirstLine - hunk.first0) / left_increment) * right_increment;
                } else {
                    return;
                }
                textArea1.setFirstLine(hunk.first1 + distance);
                return;
            }
            if (leftFirstLine > hunk.last0 && (hunk.next != null && leftFirstLine < hunk.next.first0)) {
                int distance = leftFirstLine - hunk.last0;
                textArea1.setFirstLine(hunk.last1 + distance);
                return;
            }
            if (leftFirstLine > hunk.last0 && hunk.next == null) {
                int distance = leftFirstLine - hunk.last0;
                textArea1.setFirstLine(hunk.last1 + distance);
                return;
            }
        }
    }

    public void synchroScrollLeft() {
        if (!jEdit.getBooleanProperty("jdiff.synchroscroll-on", true)) {
            return;
        }
        Diff.Change hunk = edits;
        int leftFirstLine = textArea0.getFirstLine();
        int rightFirstLine = textArea1.getFirstLine();
        int leftMaxFirstLine = textArea0.getLineCount() - textArea0.getVisibleLines() + 1;
        if (hunk == null) {
            textArea0.setFirstLine(Math.min(rightFirstLine, leftMaxFirstLine));
            return;
        }
        for (; hunk != null; hunk = hunk.next) {
            if (rightFirstLine < hunk.first1 && hunk.prev == null) {
                textArea0.setFirstLine(rightFirstLine);
                return;
            }
            if (rightFirstLine >= hunk.first1 && rightFirstLine < hunk.first1 + hunk.lines1) {
                int distance = 0;
                if (hunk.lines1 == 0 && hunk.lines0 > 0) {
                    distance = leftFirstLine - hunk.first0;
                } else if (hunk.lines1 > 0 && hunk.lines0 == 0) {
                    distance = 0;
                } else if (hunk.lines1 == hunk.lines0) {
                    distance = rightFirstLine - hunk.first1;
                } else if (hunk.lines1 > hunk.lines0) {
                    int right_increment = hunk.lines1 / hunk.lines0;
                    int left_increment = (int) Math.round((float) (hunk.lines0 * right_increment) / (float) hunk.lines1);
                    distance = ((rightFirstLine - hunk.first1) / right_increment) * left_increment;
                } else if (hunk.lines1 < hunk.lines0) {
                    int left_increment = hunk.lines0 / hunk.lines1;
                    int right_increment = (int) Math.round((float) (hunk.lines1 * left_increment) / (float) hunk.lines0);
                    distance = ((rightFirstLine - hunk.first1) / right_increment) * left_increment;
                } else {
                    return;
                }
                textArea0.setFirstLine(hunk.first0 + distance);
                return;
            }
            if (rightFirstLine > hunk.last1 && (hunk.next != null && rightFirstLine < hunk.next.first1)) {
                int distance = rightFirstLine - hunk.last1;
                textArea0.setFirstLine(hunk.last0 + distance);
                return;
            }
            if (rightFirstLine > hunk.last1 && hunk.next == null) {
                int distance = rightFirstLine - hunk.last1;
                textArea0.setFirstLine(hunk.last0 + distance);
                return;
            }
        }
    }

    /**
     * Default implementation does nothing, this is for subclasses to override.
     */
    public void moveRight(int line_number) {
    }

    /**
     * Default implementation does nothing, this is for subclasses to override.
     */
    public void moveLeft(int line_number) {
    }
}
