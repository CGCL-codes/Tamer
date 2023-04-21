package com.widen.prima.editor.finance.report;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import com.widen.prima.util.KalendarDialog;

public class DateMouseListener implements MouseListener {

    private Label label;

    private Shell shell;

    public DateMouseListener(Shell shell, Label label) {
        this.shell = shell;
        this.label = label;
    }

    public void mouseDoubleClick(MouseEvent e) {
    }

    public void mouseDown(MouseEvent e) {
        KalendarDialog cd = new KalendarDialog(shell);
        String selectedDate = (String) cd.open(Display.getCurrent().getCursorLocation());
        label.setText(selectedDate);
    }

    public void mouseUp(MouseEvent e) {
    }
}
