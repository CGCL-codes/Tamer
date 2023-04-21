package no.ugland.utransprod.gui.edit;

import javax.swing.JComponent;
import javax.swing.JTextField;
import no.ugland.utransprod.gui.WindowInterface;
import no.ugland.utransprod.gui.handlers.CustomersViewHandler;
import no.ugland.utransprod.gui.model.CustomerModel;
import no.ugland.utransprod.model.Customer;
import no.ugland.utransprod.model.validators.CustomerValidator;
import no.ugland.utransprod.util.IconFeedbackPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * Klase som h�ndterer vining av panel for innlegging, editering og s�k av
 * kunder
 * 
 * @author atle.brekka
 */
public class EditCustomerView extends AbstractEditView<CustomerModel, Customer> {

    private JTextField textFieldCustomerNr;

    private JTextField textFieldFirstName;

    private JTextField textFieldLastName;

    public EditCustomerView(final CustomersViewHandler handler, final Customer customer, final boolean searchDialog) {
        super(searchDialog, new CustomerModel(customer), handler);
    }

    /**
	 * Initierer komponenter for visning av kunde
	 * 
	 * @param window1
	 */
    @Override
    protected final void initEditComponents(final WindowInterface window1) {
        textFieldCustomerNr = ((CustomersViewHandler) viewHandler).getTextFieldCustomerNr(presentationModel);
        textFieldFirstName = ((CustomersViewHandler) viewHandler).getTextFieldFirstName(presentationModel);
        textFieldLastName = ((CustomersViewHandler) viewHandler).getTextFieldLastName(presentationModel);
    }

    /**
	 * @param object
	 * @return Validator
	 * @see no.ugland.utransprod.gui.edit.AbstractEditView#getValidator(java.lang.Object)
	 */
    @Override
    protected final Validator getValidator(final CustomerModel object, boolean search) {
        return new CustomerValidator(object);
    }

    /**
	 * Initierer komponenter som skal vise feilmeldinger
	 */
    @Override
    protected final void initComponentAnnotations() {
        ValidationComponentUtils.setMandatory(textFieldCustomerNr, true);
        ValidationComponentUtils.setMessageKey(textFieldCustomerNr, "Kunde.nummer");
        ValidationComponentUtils.setMandatory(textFieldFirstName, true);
        ValidationComponentUtils.setMessageKey(textFieldFirstName, "Kunde.fornavn");
        ValidationComponentUtils.setMandatory(textFieldLastName, true);
        ValidationComponentUtils.setMessageKey(textFieldLastName, "Kunde.etternavn");
    }

    @Override
    protected final JComponent buildEditPanel(final WindowInterface window) {
        FormLayout layout = new FormLayout("10dlu,p,p,20dlu,30dlu,50dlu", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.appendRow(new RowSpec("bottom:20dlu"));
        builder.setLeadingColumnOffset(1);
        builder.nextColumn();
        builder.append("Kundenr:", textFieldCustomerNr);
        builder.nextLine();
        builder.append("Fornavn:", textFieldFirstName, 2);
        builder.append("Etternavn:", textFieldLastName, 2);
        builder.nextLine();
        builder.append(ButtonBarFactory.buildCenteredBar(buttonSave, buttonCancel), 5);
        builder.appendRow(new RowSpec("5dlu"));
        return new IconFeedbackPanel(validationResultModel, builder.getPanel());
    }

    public final String getDialogName() {
        return "EditCustomerView";
    }

    public final String getHeading() {
        return "Kunde";
    }
}
