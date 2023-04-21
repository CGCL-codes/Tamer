package net.sf.ulmac.ui.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ExternalProgramsPreferencePage extends AbstractPreferencePage implements IWorkbenchPreferencePage {

    public static final String ID = "net.sf.ulmac.ui.preferences.encodingPreferencePage";

    public ExternalProgramsPreferencePage() {
        super();
    }

    @Override
    public Control createContents(Composite parent) {
        final Composite container = new Composite(parent, SWT.NULL);
        container.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        GridLayout layout = new GridLayout(1, false);
        container.setLayout(layout);
        Label lblInfo = new Label(container, SWT.NONE);
        lblInfo.setText("Expand the tree to edit preferences for a specific program.");
        lblInfo.pack();
        return container;
    }

    @Override
    protected String getPageID() {
        return ID;
    }

    @Override
    public void init(IWorkbench workbench) {
    }
}
