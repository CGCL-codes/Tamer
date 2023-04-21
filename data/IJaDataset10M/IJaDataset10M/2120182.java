package org.argeproje.resim;

import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argeproje.resim.proc.view.ViewerPR;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class ViewerPage extends FormPage {

    private ViewerPR _viewerPR;

    private int _panelIndex;

    public ViewerPage(FormEditor editor, ViewerPR viewerPR, int panelIndex, String id, String title) {
        super(editor, id, title);
        _viewerPR = viewerPR;
        _panelIndex = panelIndex;
    }

    public int getPanelIndex() {
        return _panelIndex;
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        ScrolledForm form = managedForm.getForm();
        FormToolkit toolkit = managedForm.getToolkit();
        toolkit.decorateFormHeading(form.getForm());
        Composite body = form.getBody();
        FillLayout layout = new FillLayout();
        body.setLayout(layout);
        Composite parent = new Composite(body, SWT.EMBEDDED);
        Frame frame = org.eclipse.swt.awt.SWT_AWT.new_Frame(parent);
        frame.add(new JScrollPane(_viewerPR.getViewPanel(_panelIndex)));
    }

    public boolean isEditor() {
        return false;
    }

    public ViewerPR getViewerProcessor() {
        return _viewerPR;
    }
}
