package org.eclipse.tptp.test.tools.web.ui.test.editor.form;

import org.eclipse.hyades.models.common.facades.behavioral.ITestCase;
import org.eclipse.hyades.test.ui.editor.extension.BaseEditorExtension;
import org.eclipse.hyades.test.ui.editor.form.util.EditorForm;
import org.eclipse.hyades.test.ui.editor.form.util.WidgetFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tptp.test.tools.web.ui.test.editor.form.section.WebTestNavigationSection;

public abstract class AResponseForm extends EditorForm {

    protected WebTestCasesForm requestsForm;

    protected WebTestNavigationSection testCaseNavigation;

    private int pageNumber;

    public AResponseForm(BaseEditorExtension baseEditorExtension, WidgetFactory widgetFactory) {
        super(baseEditorExtension, widgetFactory);
    }

    protected abstract void createEditorFormContents(Composite parent);

    public abstract void load();

    public WebTestCasesForm getRequestForm() {
        return requestsForm;
    }

    protected void createTestCaseNavigation(Composite parent) {
        this.testCaseNavigation = new WebTestNavigationSection(this);
        registerSection(this.testCaseNavigation);
        Control control = this.testCaseNavigation.createControl(parent, getWidgetFactory());
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        control.setLayoutData(gridData);
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(int page) {
        this.pageNumber = page;
    }

    public abstract IStructuredSelection getSelection();

    public boolean activated() {
        return true;
    }

    public ITestCase getActiveTestCase() {
        return requestsForm.getActTestCase();
    }
}
