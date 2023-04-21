package com.richclientgui.toolbox.samples.propagate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.richclientgui.toolbox.propagate.PropagateComposite;

/**
 * @author Carien van Zyl
 */
public class PropagateBackgroundColorSample {

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setText("Propagate Background Colour Sample");
        shell.setLayout(new GridLayout(4, false));
        final PropagateComposite mainComposite = new PropagateComposite(shell, SWT.BORDER);
        mainComposite.setLayout(new GridLayout());
        final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 4;
        mainComposite.setLayoutData(gd);
        new Label(mainComposite, SWT.NONE).setText("Main Composite");
        final Text txtOne = new Text(mainComposite, SWT.BORDER);
        txtOne.setText("Default 'Text' on Main");
        txtOne.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final Text txtTwo = new Text(mainComposite, SWT.BORDER);
        txtTwo.setText("Set to  be excluded from propagate background on Main");
        mainComposite.excludeFromPropagateBackground(txtTwo);
        txtTwo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final PropagateComposite composite1 = new PropagateComposite(mainComposite, SWT.BORDER);
        composite1.setLayout(new GridLayout());
        composite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        new Label(composite1, SWT.NONE).setText("Composite 1 - set to propagate background");
        composite1.setPropagateBackground(true);
        final Text txtInclude = new Text(composite1, SWT.BORDER);
        txtInclude.setText("Default 'Text' on Composite 1");
        txtInclude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final Text txtExclude = new Text(composite1, SWT.BORDER);
        txtExclude.setText("Set to  be excluded from propagate background on Composite 1");
        composite1.excludeFromPropagateBackground(txtExclude);
        txtExclude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final PropagateComposite composite2 = new PropagateComposite(mainComposite, SWT.BORDER);
        composite2.setLayout(new GridLayout());
        composite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        mainComposite.excludeFromPropagateBackground(composite2);
        new Label(composite2, SWT.NONE).setText("Composite 2 - set to not propagate background");
        final Text txtIllustrate = new Text(composite2, SWT.BORDER);
        txtIllustrate.setText("Default 'Text' on Composite 2");
        txtIllustrate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final Button btnBlue = new Button(shell, SWT.PUSH);
        btnBlue.setText("Blue");
        btnBlue.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                mainComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
            }
        });
        btnBlue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final Button btnRed = new Button(shell, SWT.PUSH);
        btnRed.setText("Red");
        btnRed.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                mainComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
            }
        });
        btnRed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final Button btnToggle = new Button(shell, SWT.TOGGLE);
        btnToggle.setText("Propagate Background");
        btnToggle.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                mainComposite.setPropagateBackground(btnToggle.getSelection());
            }
        });
        mainComposite.setPropagateBackground(true);
        btnToggle.setSelection(true);
        btnToggle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        final Button btnToggleComposite2 = new Button(shell, SWT.TOGGLE);
        btnToggleComposite2.setText("Include Composite 2");
        btnToggleComposite2.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                final boolean include = btnToggleComposite2.getSelection();
                if (include) {
                    mainComposite.includePropagateBackground(composite2);
                } else {
                    mainComposite.excludeFromPropagateBackground(composite2);
                }
            }
        });
        btnToggleComposite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        shell.open();
        shell.pack();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
