package no.ugland.utransprod.gui.edit;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import no.ugland.utransprod.gui.WindowInterface;
import no.ugland.utransprod.gui.handlers.AbstractViewHandler;
import no.ugland.utransprod.gui.handlers.UserTypeViewHandler;
import no.ugland.utransprod.gui.model.UserTypeModel;
import no.ugland.utransprod.model.UserType;
import no.ugland.utransprod.model.validators.UserTypeValidator;
import no.ugland.utransprod.util.IconFeedbackPanel;
import org.jdesktop.swingx.JXTable;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * Klasse for visning og editering av brukertype
 * 
 * @author atle.brekka
 * 
 */
public class EditUserTypeView extends AbstractEditView<UserTypeModel, UserType> {

    /**
	 * 
	 */
    private JTextField textFieldDescription;

    /**
	 * 
	 */
    private JComboBox comboBoxStartupWindow;

    /**
	 * 
	 */
    private JCheckBox checkBoxAdmin;

    /**
	 * 
	 */
    private JXTable tableWindows;

    /**
	 * @param searchDialog
	 * @param userType
	 * @param aViewHandler
	 */
    public EditUserTypeView(boolean searchDialog, UserType userType, AbstractViewHandler<UserType, UserTypeModel> aViewHandler) {
        super(searchDialog, new UserTypeModel(userType), aViewHandler);
    }

    /**
	 * @see no.ugland.utransprod.gui.edit.AbstractEditView#buildEditPanel(no.ugland.utransprod.gui.WindowInterface)
	 */
    @Override
    protected JComponent buildEditPanel(WindowInterface window) {
        FormLayout layout = new FormLayout("10dlu,p,3dlu,170dlu,10dlu", "10dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,fill:200dlu:grow,3dlu,p,3dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Beskrivelse:", cc.xy(2, 2));
        builder.add(textFieldDescription, cc.xy(4, 2));
        builder.addLabel("Oppstartsvindu:", cc.xy(2, 4));
        builder.add(comboBoxStartupWindow, cc.xy(4, 4));
        builder.add(checkBoxAdmin, cc.xy(2, 6));
        builder.addLabel("Akksess:", cc.xy(2, 8));
        builder.add(new JScrollPane(tableWindows), cc.xyw(2, 10, 3));
        builder.add(ButtonBarFactory.buildCenteredBar(buttonSave, buttonCancel), cc.xyw(2, 12, 3));
        return new IconFeedbackPanel(validationResultModel, builder.getPanel());
    }

    /**
	 * @param object
	 * @return validator
	 * @see no.ugland.utransprod.gui.edit.AbstractEditView#getValidator(java.lang.Object)
	 */
    @Override
    protected Validator getValidator(UserTypeModel object, boolean search) {
        return new UserTypeValidator(object);
    }

    /**
	 * @see no.ugland.utransprod.gui.edit.AbstractEditView#initComponentAnnotations()
	 */
    @Override
    protected void initComponentAnnotations() {
        ValidationComponentUtils.setMandatory(textFieldDescription, true);
        ValidationComponentUtils.setMessageKey(textFieldDescription, "Brukertype.beskrivelse");
        ValidationComponentUtils.setMandatory(comboBoxStartupWindow, true);
        ValidationComponentUtils.setMessageKey(comboBoxStartupWindow, "Brukertype.oppstartsvindu");
    }

    /**
	 * @see no.ugland.utransprod.gui.edit.AbstractEditView#initEditComponents(no.ugland.utransprod.gui.WindowInterface)
	 */
    @Override
    protected void initEditComponents(WindowInterface aWindow) {
        textFieldDescription = ((UserTypeViewHandler) viewHandler).getTextFieldDescription(presentationModel);
        comboBoxStartupWindow = ((UserTypeViewHandler) viewHandler).getComboBoxStartupWindow(presentationModel);
        checkBoxAdmin = ((UserTypeViewHandler) viewHandler).getCheckBoxAdmin(presentationModel);
        tableWindows = ((UserTypeViewHandler) viewHandler).getTableWindows(presentationModel);
    }

    public final String getDialogName() {
        return "EditUserTypeView";
    }

    public final String getHeading() {
        return "Brukertype";
    }
}
