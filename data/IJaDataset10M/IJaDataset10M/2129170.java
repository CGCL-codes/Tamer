package org.argouml.uml.ui.model_management;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLCheckBox2;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.uml.ui.foundation.core.ActionSetElementOwnershipSpecification;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;

/**
 * The properties panel for an ElementImport. <p>
 * 
 * The ElementResidence is not a ModelElement according MDR, 
 * hence this properties panel does not show a name field.
 *
 * @author Michiel
 */
public class PropPanelElementImport extends PropPanelModelElement {

    private JPanel modifiersPanel;

    private JTextField aliasTextField;

    private static UMLElementImportAliasDocument aliasDocument = new UMLElementImportAliasDocument();

    /**
     * Constructor.
     */
    public PropPanelElementImport() {
        super("label.element-import", lookupIcon("ElementImport"));
        addField(Translator.localize("label.alias"), getAliasTextField());
        add(getNamespaceVisibilityPanel());
        add(getModifiersPanel());
        addSeparator();
        addField(Translator.localize("label.imported-element"), getSingleRowScroll(new ElementImportImportedElementListModel()));
        addField(Translator.localize("label.package"), getSingleRowScroll(new ElementImportPackageListModel()));
        addAction(new ActionNavigateContainerElement());
        addAction(getDeleteAction());
    }

    /**
     * @return a textfield for the alias
     */
    protected JComponent getAliasTextField() {
        if (aliasTextField == null) {
            aliasTextField = new UMLTextField2(aliasDocument);
        }
        return aliasTextField;
    }

    public JPanel getModifiersPanel() {
        if (modifiersPanel == null) {
            modifiersPanel = createBorderPanel(Translator.localize("label.modifiers"));
            modifiersPanel.add(new UMLElementImportIsSpecificationCheckbox());
        }
        return modifiersPanel;
    }
}

/**
 * A checkbox for the "isSpecification" of the ElementImport.
 * 
 * @author Michiel
 */
class UMLElementImportIsSpecificationCheckbox extends UMLCheckBox2 {

    /**
     * Constructor for UMLGeneralizableElementRootCheckBox.
     */
    public UMLElementImportIsSpecificationCheckbox() {
        super(Translator.localize("checkbox.is-specification"), ActionSetElementOwnershipSpecification.getInstance(), "isSpecification");
    }

    public void buildModel() {
        if (getTarget() != null) {
            setSelected(Model.getFacade().isSpecification(getTarget()));
        }
    }
}

/**
 * The Document for the Alias of the ElementImport.
 * 
 * @author mvw
 */
class UMLElementImportAliasDocument extends UMLPlainTextDocument {

    /**
     * Constructor for UMLModelElementNameDocument.
     */
    public UMLElementImportAliasDocument() {
        super("alias");
    }

    protected void setProperty(String text) {
        Object t = getTarget();
        if (t != null) {
            Model.getModelManagementHelper().setAlias(getTarget(), text);
        }
    }

    protected String getProperty() {
        return Model.getFacade().getAlias(getTarget());
    }
}

/**
 * The list model for the importedElement of a ElementImport.
 *
 * @author mvw
 */
class ElementImportImportedElementListModel extends UMLModelElementListModel2 {

    /**
     * Constructor.
     */
    public ElementImportImportedElementListModel() {
        super("importedElement");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            removeAllElements();
            addElement(Model.getFacade().getImportedElement(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAElementImport(getTarget());
    }
}

/**
 * The list model for the package of a ElementImport.
 *
 * @author mvw
 */
class ElementImportPackageListModel extends UMLModelElementListModel2 {

    /**
     * Constructor.
     */
    public ElementImportPackageListModel() {
        super("package");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            removeAllElements();
            addElement(Model.getFacade().getPackage(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAElementImport(getTarget());
    }
}
