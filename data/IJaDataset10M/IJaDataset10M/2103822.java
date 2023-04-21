package jdiff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import jdiff.util.Diff;
import org.gjt.sp.jedit.textarea.DisplayManager;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.util.Log;

public class DiffGlobalVirtualOverview extends DiffOverview {

    public DiffGlobalVirtualOverview(Diff.change edits, int lineCount0, int lineCount1, JEditTextArea textArea0, JEditTextArea textArea1) {
        super(edits, lineCount0, lineCount1, textArea0, textArea1);
    }

    public void paint(Graphics gfx) {
    }

    public void paintCursor(Graphics gfx) {
    }
}
