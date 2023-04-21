package org.opensecurepay.ui.spring.views;

import javax.swing.JComponent;
import javax.swing.JTextField;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form to handle the properties of a Contract object. It uses a {@link TableFormBuilder} to construct the layout of the
 * form. Contact object properties are easily bound to UI controls using the form builder's
 * {@link TableFormBuilder#add(String)} method. The platform takes care of determining which kind of control to create
 * based on the type of the property in question.
 * @author Larry Streepy
 */
public class ContractForm extends AbstractForm {

    public static final String FORM_NAME = "contract";

    private JComponent nameField;

    /**
	 * @param pageFormModel
	 */
    public ContractForm(FormModel formModel) {
        super(formModel, FORM_NAME);
    }

    protected JComponent createFormControl() {
        TableFormBuilder formBuilder = new TableFormBuilder(getBindingFactory());
        formBuilder.setLabelAttributes("colGrId=label colSpec=right:pref");
        formBuilder.addSeparator("General");
        formBuilder.row();
        nameField = formBuilder.add("name")[1];
        formBuilder.row();
        formBuilder.add("nr", "colSpan=1");
        formBuilder.row();
        formBuilder.add("bank");
        formBuilder.row();
        formBuilder.row();
        return formBuilder.getForm();
    }

    /**
	 * Try to place the focus in the firstNameField whenever the initial focus is being set.
	 * @return
	 */
    public boolean requestFocusInWindow() {
        return nameField.requestFocusInWindow();
    }
}
