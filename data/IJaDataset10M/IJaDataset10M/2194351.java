package no.ugland.utransprod.gui.edit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import no.ugland.utransprod.gui.WindowInterface;
import no.ugland.utransprod.gui.handlers.ProductionUnitViewHandler;
import no.ugland.utransprod.gui.model.ProductionUnitModel;
import no.ugland.utransprod.model.ProductionUnit;
import no.ugland.utransprod.model.validators.ProductionUnitValidator;
import no.ugland.utransprod.util.IconFeedbackPanel;
import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class EditProductionUnitView extends AbstractEditView<ProductionUnitModel, ProductionUnit> {

    private JTextField textFieldProductionUnitName;

    private JComboBox comboBoxArticleType;

    private JList listProductAreaGroup;

    private JButton buttonAddProductAreaGroup;

    private JButton buttonRemoveProductAreaGroup;

    public EditProductionUnitView(boolean searchDialog, ProductionUnitModel object, ProductionUnitViewHandler aViewHandler) {
        super(searchDialog, object, aViewHandler);
    }

    @Override
    protected JComponent buildEditPanel(WindowInterface window) {
        FormLayout layout = new FormLayout("10dlu,p,3dlu,70dlu,3dlu,p,10dlu", "10dlu,p,3dlu,p,3dlu,p,3dlu,50dlu,3dlu,p");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Navn:", cc.xy(2, 2));
        builder.add(textFieldProductionUnitName, cc.xy(4, 2));
        builder.addLabel("Artikkel:", cc.xy(2, 4));
        builder.add(comboBoxArticleType, cc.xy(4, 4));
        builder.addLabel("Produktomr�der:", cc.xy(2, 6));
        builder.add(new JScrollPane(listProductAreaGroup), cc.xywh(4, 6, 1, 3));
        builder.add(buildButtonPanel(), cc.xywh(6, 6, 1, 3));
        builder.add(ButtonBarFactory.buildCenteredBar(buttonSave, buttonCancel), cc.xyw(2, 10, 5));
        return new IconFeedbackPanel(validationResultModel, builder.getPanel());
    }

    private JPanel buildButtonPanel() {
        ButtonStackBuilder builder = new ButtonStackBuilder();
        builder.addGridded(buttonAddProductAreaGroup);
        builder.addRelatedGap();
        builder.addGridded(buttonRemoveProductAreaGroup);
        return builder.getPanel();
    }

    @Override
    protected Validator getValidator(ProductionUnitModel object, boolean search) {
        return new ProductionUnitValidator(object);
    }

    @Override
    protected void initComponentAnnotations() {
        ValidationComponentUtils.setMandatory(textFieldProductionUnitName, true);
        ValidationComponentUtils.setMessageKey(textFieldProductionUnitName, "Produksjonsenhet.navn");
        ValidationComponentUtils.setMandatory(comboBoxArticleType, true);
        ValidationComponentUtils.setMessageKey(comboBoxArticleType, "Produksjonsenhet.artikkel");
        ValidationComponentUtils.setMandatory(listProductAreaGroup, true);
        ValidationComponentUtils.setMessageKey(listProductAreaGroup, "Produksjonsenhet.produktomr�de");
    }

    @Override
    protected void initEditComponents(WindowInterface aWindow) {
        textFieldProductionUnitName = ((ProductionUnitViewHandler) viewHandler).getTextFieldProductionUnitName(presentationModel);
        comboBoxArticleType = ((ProductionUnitViewHandler) viewHandler).getComboBoxArticleType(presentationModel);
        listProductAreaGroup = ((ProductionUnitViewHandler) viewHandler).getListProductAreaGroup(presentationModel);
        buttonAddProductAreaGroup = ((ProductionUnitViewHandler) viewHandler).getButtonAddProductAreaGroup(aWindow, presentationModel);
        buttonRemoveProductAreaGroup = ((ProductionUnitViewHandler) viewHandler).getButtonRemoveProductAreaGroup(presentationModel);
    }

    public String getDialogName() {
        return "EditProductionUnitView";
    }

    public String getHeading() {
        return "Produksjonsenhet";
    }
}
