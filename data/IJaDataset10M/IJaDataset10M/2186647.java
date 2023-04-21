package edu.gsbme.wasabi.UI.Dialog.Decl;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.Factory.ModelMLFactory;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.MMLParser2.Vocabulary.Declaration;
import edu.gsbme.wasabi.UI.UItype;
import edu.gsbme.wasabi.UI.Dialog.MFormDialog;
import edu.gsbme.wasabi.UI.Forms.Declaration.EquationRefForm;

/**
 * Equation reference dialog
 * @author David
 *
 */
public class EquationRefDialog extends MFormDialog {

    public EquationRefDialog(Shell shell) {
        super(shell);
    }

    public EquationRefDialog(Shell shell, Element element, UItype type) {
        super(shell, element, type);
        x = 400;
        y = 300;
        if (type == UItype.NEW) {
            if (!returnElement().getTagName().equals(Declaration.equation_list.toString())) {
                Element listTag = ((ModelMLFactory) returnActiveEditorModel().getFactory()).getDeclWorker().insertEquationListTag(returnElement());
                modTracker.insertNewModification(returnElement(), listTag);
                setElement(listTag);
            }
            Element tag = ((ModelMLFactory) returnActiveEditorModel().getFactory()).getDeclWorker().insertEquationRefTag(returnElement(), "");
            modTracker.insertNewModification(returnElement(), tag);
            setElement(tag);
            insertObserveElement(tag);
        }
    }

    protected void createFormContent(IManagedForm mform) {
        skeleton = new EquationRefForm();
        FormToolkit toolkit = mform.getToolkit();
        ScrolledForm scForm = mform.getForm();
        skeleton.construct_layout(toolkit, scForm);
    }

    public boolean commit_change() {
        EquationRefForm form = (EquationRefForm) skeleton;
        returnElement().setAttribute(Attributes.ref.toString(), form.text.textfield1.getText().trim());
        return true;
    }

    @Override
    public void initial_load_UI() {
        EquationRefForm form = (EquationRefForm) skeleton;
        if (returnElement().hasAttribute(Attributes.ref.toString())) form.text.textfield1.setText(returnElement().getAttribute(Attributes.ref.toString()));
    }

    @Override
    public void refresh_UI() {
        EquationRefForm form = (EquationRefForm) skeleton;
        if (returnElement().hasAttribute(Attributes.ref.toString())) form.text.textfield1.setText(returnElement().getAttribute(Attributes.ref.toString()));
    }
}
