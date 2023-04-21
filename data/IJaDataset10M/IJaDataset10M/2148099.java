package com.peterhi.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.peterhi.FlipPanel;

public class TestFlipPanel {

    public static void main(String[] args) throws Exception {
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setSize(640, 480);
        GridLayout gl = new GridLayout();
        gl.horizontalSpacing = gl.marginBottom = gl.marginHeight = gl.marginLeft = gl.marginRight = gl.marginTop = gl.marginWidth = gl.verticalSpacing = 0;
        shell.setLayout(gl);
        final FlipPanel panel = new FlipPanel(shell, SWT.NONE);
        GridData gd = new GridData();
        gd.heightHint = 22;
        gd.widthHint = 220;
        panel.setLayoutData(gd);
        Composite v = panel.getViewport();
        final Control[] controls = new Control[] { new Button(v, SWT.PUSH), new Text(v, SWT.BORDER), new Button(v, SWT.CHECK), new Button(v, SWT.RADIO), new Scale(v, SWT.NONE), new ProgressBar(v, SWT.INDETERMINATE) };
        ((Button) controls[0]).setText("Button");
        ((Text) controls[1]).setText("TextBox");
        ((Button) controls[2]).setText("CheckBox");
        ((Button) controls[3]).setText("RadioButton");
        ((Scale) controls[4]).setSelection(50);
        ((ProgressBar) controls[5]).setSelection(50);
        panel.setControl(controls[0]);
        Button cmdDown = new Button(shell, SWT.PUSH);
        cmdDown.setText("Down");
        cmdDown.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                panel.scrollDown();
            }
        });
        Button cmdUp = new Button(shell, SWT.PUSH);
        cmdUp.setText("Up");
        cmdUp.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                panel.scrollUp();
            }
        });
        shell.open();
        while (!shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
        display.dispose();
    }
}
