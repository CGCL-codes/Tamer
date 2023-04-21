package com.amazonaws.eclipse.core.ui.overview;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * Editor displaying the AWS Toolkit for Eclipse Overview page.
 */
public class OverviewEditor extends EditorPart {

    private Composite overviewComposite;

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void dispose() {
        if (overviewComposite != null) overviewComposite.dispose();
        super.dispose();
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());
        overviewComposite = new FormsOverviewComposite(parent);
    }

    @Override
    public void setFocus() {
    }
}
