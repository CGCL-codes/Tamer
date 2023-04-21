package com.application.areca.launcher.gui.filters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.application.areca.filter.ArchiveFilter;
import com.application.areca.launcher.gui.FilterEditionWindow;

public abstract class AbstractSimpleParamFilterComposite extends AbstractStringParamFilterComposite {

    public AbstractSimpleParamFilterComposite(Composite composite, int filterIndex, ArchiveFilter filter, FilterEditionWindow window) {
        super(composite, filterIndex, filter, window);
        this.setLayout(new GridLayout(1, false));
        txt = new Text(this, SWT.BORDER);
        GridData dt = new GridData(SWT.FILL, SWT.CENTER, true, false);
        txt.setLayoutData(dt);
        window.monitorControl(txt);
        Label lblExample = new Label(this, SWT.NONE);
        lblExample.setText(getParamExample());
        postInit();
    }

    public abstract String getParamExample();
}
