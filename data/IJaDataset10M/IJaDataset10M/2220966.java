package basic.widget;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class ViewFormExample2 {

    public ViewFormExample2() {
        final Display display = Display.getDefault();
        final Shell shell = new Shell();
        shell.setSize(300, 190);
        shell.setText("ViewFormʵ��");
        shell.setLayout(new FillLayout());
        final ViewForm viewForm = new ViewForm(shell, SWT.BORDER);
        viewForm.setLayout(new FillLayout());
        Text text = new Text(viewForm, SWT.NONE);
        Label labelLeft = new Label(viewForm, SWT.NONE);
        labelLeft.setText("Left");
        Label labelRight = new Label(viewForm, SWT.NONE);
        labelRight.setText("Right");
        Label labelCenter = new Label(viewForm, SWT.NONE);
        labelCenter.setText("Center");
        viewForm.setContent(text);
        viewForm.setTopLeft(labelLeft);
        viewForm.setTopRight(labelRight);
        viewForm.setTopCenter(labelCenter);
        viewForm.setTopCenterSeparate(true);
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    public static void main(String[] args) {
        new ViewFormExample2();
    }
}
