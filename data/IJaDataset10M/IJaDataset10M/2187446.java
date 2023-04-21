package de.haumacher.timecollect.ui.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import de.haumacher.timecollect.ui.Style;

public class DefaultStyle implements Style {

    public static final Style INSTANCE = new DefaultStyle();

    private DefaultStyle() {
    }

    public void inputLarge(Control control) {
        GridData inputDataLarge = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1);
        inputDataLarge.horizontalIndent = 5;
        control.setLayoutData(inputDataLarge);
    }

    public void input(Control control) {
        GridData inputData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        inputData.horizontalIndent = 5;
        control.setLayoutData(inputData);
    }

    public void container(Composite composite) {
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(1, false));
    }

    public void buttonBar(Composite composite) {
        composite.setLayoutData(new GridData(SWT.END, SWT.BEGINNING, true, false));
        composite.setLayout(new FillLayout());
    }

    public void form(Composite composite) {
        composite.setLayout(new GridLayout(2, false));
    }
}
