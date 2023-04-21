package org.eclipse.tptp.test.tools.web.ui.test.editor.form.provider.impl;

import org.eclipse.hyades.test.ui.editor.form.util.WidgetFactory;
import org.eclipse.tptp.models.web.common.test.data.rep.IRepositoryFactory;
import org.eclipse.tptp.test.tools.web.ui.test.editor.WebTestEditorExtension;
import org.eclipse.tptp.test.tools.web.ui.test.editor.form.AResponseForm;
import org.eclipse.tptp.test.tools.web.ui.test.editor.form.WebTestBrowserForm;
import org.eclipse.tptp.test.tools.web.ui.test.editor.form.WebTestCasesForm;
import org.eclipse.tptp.test.tools.web.ui.test.editor.form.provider.IResponseFormProvider;

public class BrowserResponseFormProvider implements IResponseFormProvider {

    public AResponseForm create(WebTestEditorExtension editorExtension, WidgetFactory factory, WebTestCasesForm testCaseForm, IRepositoryFactory repositoryFactory) {
        return new WebTestBrowserForm(editorExtension, factory, testCaseForm);
    }

    public String getTabText() {
        return "HTML Browser";
    }
}
