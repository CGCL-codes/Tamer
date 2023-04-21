package de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.wizards;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.AbstractArchitectureModel;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.HorizontalConnection;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.DelegationDown;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.DelegationUp;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.XORComponent;
import de.fraunhofer.isst.axbench.axlang.elements.featuremodel.Feature;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLException;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLangElementUtilities;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Attributes;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Cardinality;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Direction;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.EditableViewConstants;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.wizards.pages.EditableViewMainWizardPage;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.wizards.pages.EditableViewNextWizardPage;
import de.fraunhofer.isst.axbench.editors.axlmultipage.treeeditor.wizards.pages.EditableViewReferenceWizardPage;

/**
 * @brief The main wizard class where all pages are added and the finish button is handled.
 * @author skaegebein
 * @version 0.8.0
 * @since 0.8.0
 */
public class EditableViewWizard extends Wizard implements INewWizard {

    private Role myrole = null;

    private String action = null;

    private EditableViewMainWizardPage mainpage = null;

    private EditableViewReferenceWizardPage referencepage = null;

    private LinkedHashMap<String, EditableViewNextWizardPage> nextpages = null;

    private LinkedHashMap<String, AtomicBoolean> attributechoosen = null;

    private LinkedHashMap<String, LinkedHashMap<String, Boolean>> possibleattributes = null;

    private LinkedHashMap<Role, ArrayList<String>> neededattributes = null;

    private LinkedHashMap<Role, ArrayList<Role>> referenceconstraints = null;

    private IAXLangElement element = null;

    private IAXLangElement actualeditedelement = null;

    private EditableViewWizardDialog myDialog = null;

    private ArrayList<String> pageorder = null;

    private Vector<Role> possiblereferences = null;

    private String oldwindowtitle = null;

    private boolean referenceaction = false;

    /**
     * @param myrole
     * @param action
     * @param element
     * @param actualeditedelement
     */
    public EditableViewWizard(Role myrole, String action, IAXLangElement element, IAXLangElement actualeditedelement) {
        this.myrole = myrole;
        this.action = action;
        this.element = element;
        this.actualeditedelement = actualeditedelement;
        nextpages = new LinkedHashMap<String, EditableViewNextWizardPage>();
        attributechoosen = new LinkedHashMap<String, AtomicBoolean>();
        pageorder = new ArrayList<String>();
        possibleattributes = new LinkedHashMap<String, LinkedHashMap<String, Boolean>>();
        neededattributes = new LinkedHashMap<Role, ArrayList<String>>();
        referenceconstraints = new LinkedHashMap<Role, ArrayList<Role>>();
    }

    /**
     * @brief changes the actual window title.
     * @param title
     */
    public void changeWindowTitle(String title) {
        oldwindowtitle = getWindowTitle();
        setWindowTitle(title);
    }

    /**
     * @brief resets the window title
     */
    public void resetWindowTitle() {
        setWindowTitle(oldwindowtitle);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

    @Override
    public void addPages() {
        String windowtitle = "";
        if (element != null) {
            windowtitle = action + myrole;
            if (EditableViewConstants.getInstance().getDeepcounter() != 0) {
                windowtitle = windowtitle + " - ChildDeep:" + EditableViewConstants.getInstance().getDeepcounter();
            } else {
                windowtitle = windowtitle + "...";
            }
        } else {
            windowtitle = action;
        }
        setWindowTitle(windowtitle);
        if (element != null) {
            addPage(mainpage);
            if (this.possiblereferences != null) {
                addPage(referencepage);
            }
            Set<Entry<String, EditableViewNextWizardPage>> entries = nextpages.entrySet();
            for (Entry<String, EditableViewNextWizardPage> entry : entries) {
                addPage(entry.getValue());
            }
        } else {
            addPage(referencepage);
        }
    }

    /**
     * @brief via this method the mainpage can be configured.
     * @param title
     * @param startstring
     * @param description
     * @param usedIdentifiers
     */
    public void configureMainPage(String title, String startstring, String description, Vector<String> usedIdentifiers) {
        mainpage = new EditableViewMainWizardPage("mainpage", myrole, title, startstring, description, this, usedIdentifiers, false);
    }

    /**
     * @brief via this method all pages to the corresponding attribute can be configured.
     * @param title
     * @param description
     * @param done
     * @param attribute
     */
    public void configureNextPage(String title, String description, boolean done, String attribute) {
        nextpages.put(attribute, new EditableViewNextWizardPage("secondpage" + attribute, myrole, title, description, this, done, attribute));
        attributechoosen.put(attribute, new AtomicBoolean(false));
        pageorder.add(attribute);
    }

    /**
     * @brief via this method the reference page can be configured.
     * @param title
     * @param possiblereferences
     * @param description
     * @param done
     */
    public void configureReferencePage(String title, Vector<Role> possiblereferences, String description, boolean done, boolean referenceaction) {
        this.possiblereferences = possiblereferences;
        this.referenceaction = referenceaction;
        if (referenceaction) {
            if (myrole.equals(Role.FEATUREMODEL)) {
                myrole = Role.CONFIGURATION;
            }
        }
        referencepage = new EditableViewReferenceWizardPage("referencepage", myrole, title, this, description, done, referenceaction);
    }

    /**
     * @brief realize the add of a needed attribute to the element.
     * @param myRole
     */
    public void performNeededAttributes(Role myRole) {
        if (neededattributes.get(myRole) != null) {
            if (neededattributes.get(myRole).size() > 0) {
                ArrayList<String> attributes = neededattributes.get(myRole);
                for (int i = 0; i < attributes.size(); i++) {
                    Combo c = getMainpage().getNeededattributescombos().get(attributes.get(i));
                    if (c != null) {
                        getElement().setAttributeValue(attributes.get(i), Attributes.valueOf(attributes.get(i).toUpperCase()).getType().getEnumConstants()[c.getSelectionIndex()].toString());
                    }
                }
            }
        }
    }

    /**
     * @brief performs the Optional attribute.
     */
    public void performOptionalAttribute() {
        if (getMainpage().getButtonspecifications().get("isOptional") != null) {
            getElement().setOptional(getMainpage().getButtonspecifications().get("isOptional").getSelection());
        }
    }

    /**
     * @brief performs the description.
     */
    public void performDescription() {
        if (getMainpage().getButtonspecifications().get("description").getSelection()) {
            StyledText text = (StyledText) getNextpages().get("description").getValues().get("@description@StyledText");
            if (!text.getText().equals("")) {
                String toadd = text.getText();
                toadd = toadd.replace("/*", "");
                toadd = toadd.replace("*/", "");
                toadd = toadd.replace("//", "");
                getElement().setDescription(toadd);
            }
        }
    }

    /**
     * @brief performs the XOR Attribute.
     * @param role
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean performXORAttribute(Role role) {
        XORComponent buffersubcomponent = null;
        if (getMainpage().getButtonspecifications().get("isXOR") != null && getMainpage().getButtonspecifications().get("isXOR").getSelection()) {
            if (role.equals(Role.FEATURE)) {
                getElement().setXOR(getMainpage().getButtonspecifications().get("isXOR").getSelection());
                ((Feature) getElement()).setCardinality(new Cardinality(1, 1));
            } else if (role.equals(Role.SUBCOMPONENT)) {
                buffersubcomponent = performXORSubComponent();
            } else if (role.equals(Role.FUNCTION)) {
                getElement().setXOR(getMainpage().getButtonspecifications().get("isXOR").getSelection());
            }
            Vector<IAXLangElement> childs = (Vector<IAXLangElement>) getNextpages().get("isXOR").getValues().get("@isXOR@Vector<IAXLangElement>");
            for (int i = 0; i < childs.size(); i++) {
                try {
                    if (buffersubcomponent != null) {
                        buffersubcomponent.addChild(childs.get(i), role);
                    }
                } catch (AXLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (buffersubcomponent != null && role.equals(Role.SUBCOMPONENT)) {
            return true;
        }
        return false;
    }

    /**
     * @brief performs the OR Attribute.
     * @param role
     */
    @SuppressWarnings("unchecked")
    public void performORAttribute(Role role) {
        if (getMainpage().getButtonspecifications().get("isOR") != null && getMainpage().getButtonspecifications().get("isOR").getSelection()) {
            int lowerbounds = 0;
            int upperbounds = Cardinality.INFINITY;
            Text car = (Text) getNextpages().get("isOR").getValues().get("@isOR@Text");
            String cardi = car.getText();
            lowerbounds = Integer.parseInt(cardi.substring(0, cardi.indexOf(".")));
            if (!cardi.substring(cardi.lastIndexOf(".") + 1, cardi.length()).equals("*")) {
                upperbounds = Integer.parseInt(cardi.substring(cardi.lastIndexOf(".") + 1, cardi.length()));
            }
            ((Feature) getElement()).setCardinality(new Cardinality(lowerbounds, upperbounds));
            Vector<IAXLangElement> childs = (Vector<IAXLangElement>) getNextpages().get("isOR").getValues().get("@isOR@Vector<IAXLangElement>");
            for (int i = 0; i < childs.size(); i++) {
                try {
                    getElement().addChild(childs.get(i), role);
                } catch (AXLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @brief performs the ActivityAttribute.
     */
    public void performActivityAttribute() {
        IAXLangElement element = getTheRightElement();
        for (int i = 0; i < getPossiblereferences().size(); i++) {
            int index = getReferencepage().getReferencegroupcombos().get(getPossiblereferences().get(i)).getSelectionIndex();
            if (index > 0) {
                performAddSingleReference(element, getPossiblereferences().get(i));
            }
        }
    }

    /**
     * @brief performs a Activity.
     */
    public void performActivity() {
        IAXLangElement element = getTheRightElement();
        for (int i = 0; i < getPossiblereferences().size(); i++) {
            int index = getReferencepage().getReferencegroupcombos().get(getPossiblereferences().get(i)).getSelectionIndex();
            if (index > 0) {
                performAddSingleReference(element, getPossiblereferences().get(i));
            }
        }
    }

    /**
     * @brief performs a Activation.
     */
    public void performActivation() {
        IAXLangElement element = getTheRightElement();
        for (int i = 0; i < getPossiblereferences().size(); i++) {
            int index = getReferencepage().getReferencegroupcombos().get(getPossiblereferences().get(i)).getSelectionIndex();
            if (index > 0 || getPossiblereferences().get(i).equals(Role.TRIGGER)) {
                performAddSingleReference(element, getPossiblereferences().get(i));
            }
        }
    }

    /**
     * @brief performs a ChildMapping.
     */
    public void performChildMapping() {
        IAXLangElement element = getTheRightElement();
        for (int i = 0; i < getPossiblereferences().size(); i++) {
            performAddSingleReference(element, getPossiblereferences().get(i));
        }
    }

    /**
     * @brief performs a XORSubComponent.
     */
    private XORComponent performXORSubComponent() {
        XORComponent buffersubcomponent = new XORComponent();
        buffersubcomponent.setIdentifier(myDialog.getIdent());
        IAXLangElement applicationmodel = AXLangElementUtilities.getInstance().searchForElementClass(getActualeditedelement(), AbstractArchitectureModel.class, getReferencepage().getApplicationmodelbuffer());
        try {
            applicationmodel.addChild(buffersubcomponent, Role.COMPONENT);
        } catch (AXLException e) {
            e.printStackTrace();
        }
        try {
            getElement().addReference(buffersubcomponent, Role.COMPONENTTYPE);
        } catch (AXLException e1) {
            e1.printStackTrace();
        }
        return buffersubcomponent;
    }

    /**
     * @brief performs a Configuration.
     */
    public void performConfiguration(boolean featuremodelladd) {
        IAXLangElement element = getTheRightElement();
        if (featuremodelladd) {
            try {
                element.addReference(getReferencepage().getCurrentchosenelement(), Role.FEATUREMODEL);
            } catch (AXLException e) {
                e.printStackTrace();
            }
        }
        performAddMultipleReferencesFromTable(element, Role.SELECTEDFEATURE, getReferencepage().getCurrentchosenelement());
        performAddMultipleReferencesFromTable(element, Role.DESELECTEDFEATURE, getReferencepage().getCurrentchosenelement());
    }

    /**
     * @brief performs a Connection.
     */
    public void performConnection() {
        IAXLangElement sourcesubcomponent = null;
        IAXLangElement sourcesubcomponentbuf = null;
        IAXLangElement sourceport = null;
        IAXLangElement targetsubcomponent = null;
        IAXLangElement targetsubcomponentbuf = null;
        IAXLangElement targetport = null;
        boolean delegation = false;
        IAXLangElement connection = null;
        if (!referenceaction) {
            delegation = Boolean.valueOf(getMainpage().getButtonspecifications().get("isDelegation").getSelection());
            connection = getElement();
        } else {
            delegation = Boolean.valueOf(getActualeditedelement().getAttributeValue(Attributes.ISDELEGATION.getID()));
            connection = getActualeditedelement();
        }
        int index = getReferencepage().getReferencegroupcombos().get(Role.SOURCESUBCOMPONENT).getSelectionIndex();
        sourcesubcomponent = getReferencepage().getRoleelementreferences().get(Role.SOURCESUBCOMPONENT).get(index);
        index = getReferencepage().getReferencegroupcombos().get(Role.SOURCEPORT).getSelectionIndex();
        String actualsourceportdirection = "";
        if (!sourcesubcomponent.equals(getReferencepage().getCurrentchosenelement()) && !sourcesubcomponent.isXOR()) {
            sourcesubcomponentbuf = sourcesubcomponent.getReference(Role.COMPONENTTYPE);
        } else {
            sourcesubcomponentbuf = sourcesubcomponent;
        }
        if (delegation) {
            sourceport = getReferencepage().getDependedroleselementmap().get(sourcesubcomponentbuf).get(Role.PORT).get(index);
        } else {
            ArrayList<IAXLangElement> ports = getReferencepage().getDependedroleselementmap().get(sourcesubcomponentbuf).get(Role.PORT);
            int count = 0;
            for (int i = 0; i < ports.size(); i++) {
                if (ports.get(i).getAttributeValue(Attributes.DIRECTION.getID()).equals(Direction.OUT.toString().toLowerCase())) {
                    if (count == index) {
                        sourceport = ports.get(i);
                        break;
                    }
                    count++;
                }
            }
        }
        if (sourceport == null) {
            try {
                throw new InvalidParameterException("NOT EXPECTED!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        actualsourceportdirection = sourceport.getAttributeValue(Attributes.DIRECTION.getID());
        index = getReferencepage().getReferencegroupcombos().get(Role.TARGETSUBCOMPONENT).getSelectionIndex();
        targetsubcomponent = getReferencepage().getRoleelementreferences().get(Role.SOURCESUBCOMPONENT).get(index);
        index = getReferencepage().getReferencegroupcombos().get(Role.TARGETPORT).getSelectionIndex();
        if (!targetsubcomponent.equals(getReferencepage().getCurrentchosenelement()) && !targetsubcomponent.isXOR()) {
            targetsubcomponentbuf = targetsubcomponent.getReference(Role.COMPONENTTYPE);
        } else {
            targetsubcomponentbuf = targetsubcomponent;
        }
        if (delegation) {
            ArrayList<IAXLangElement> ports = getReferencepage().getDependedroleselementmap().get(targetsubcomponentbuf).get(Role.PORT);
            int count = 0;
            for (int i = 0; i < ports.size(); i++) {
                if (ports.get(i).getAttributeValue(Attributes.DIRECTION.getID()).equals(actualsourceportdirection)) {
                    if (count == index) {
                        targetport = ports.get(i);
                        break;
                    }
                    count++;
                }
            }
        } else {
            ArrayList<IAXLangElement> ports = getReferencepage().getDependedroleselementmap().get(targetsubcomponentbuf).get(Role.PORT);
            int count = 0;
            for (int i = 0; i < ports.size(); i++) {
                if (ports.get(i).getAttributeValue(Attributes.DIRECTION.getID()).equals(Direction.IN.toString().toLowerCase())) {
                    if (count == index) {
                        targetport = ports.get(i);
                        break;
                    }
                    count++;
                }
            }
        }
        try {
            if (sourcesubcomponent.isXOR()) {
                sourcesubcomponent = AXLangElementUtilities.getInstance().searchForComponentOrXORSubComponent(sourcesubcomponent, AbstractArchitectureModel.class, XORComponent.class, Role.COMPONENT, Role.SUBCOMPONENT, Role.COMPONENTTYPE);
            }
            if (targetsubcomponent.isXOR()) {
                targetsubcomponent = AXLangElementUtilities.getInstance().searchForComponentOrXORSubComponent(targetsubcomponent, AbstractArchitectureModel.class, XORComponent.class, Role.COMPONENT, Role.SUBCOMPONENT, Role.COMPONENTTYPE);
            }
            if (delegation) {
                if (this.getElement().getClass().equals(HorizontalConnection.class)) {
                    try {
                        if (!sourcesubcomponent.equals(getReferencepage().getCurrentchosenelement())) {
                            element = DelegationUp.class.newInstance();
                            connection = getElement();
                        }
                        if (!targetsubcomponent.equals(getReferencepage().getCurrentchosenelement())) {
                            element = DelegationDown.class.newInstance();
                            connection = getElement();
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (!sourcesubcomponent.equals(getReferencepage().getCurrentchosenelement())) {
                    connection.addReference(sourcesubcomponent, Role.SOURCESUBCOMPONENT);
                }
                connection.addReference(sourceport, Role.SOURCEPORT);
                if (!targetsubcomponent.equals(getReferencepage().getCurrentchosenelement())) {
                    connection.addReference(targetsubcomponent, Role.TARGETSUBCOMPONENT);
                }
                connection.addReference(targetport, Role.TARGETPORT);
                connection.setAttributeValue(Attributes.ISDELEGATION.getID(), "true");
            } else {
                connection.addReference(sourcesubcomponent, Role.SOURCESUBCOMPONENT);
                connection.addReference(sourceport, Role.SOURCEPORT);
                connection.addReference(targetsubcomponent, Role.TARGETSUBCOMPONENT);
                connection.addReference(targetport, Role.TARGETPORT);
            }
        } catch (AXLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief performs a HWConnection.
     */
    public void performHWConnection() {
        IAXLangElement sourcesubcomponent = null;
        IAXLangElement sourcesubcomponentbuf = null;
        IAXLangElement sourceport = null;
        IAXLangElement targetsubcomponent = null;
        IAXLangElement targetsubcomponentbuf = null;
        IAXLangElement targetport = null;
        IAXLangElement hwbus = null;
        boolean delegation = false;
        IAXLangElement connection = null;
        if (!referenceaction) {
            delegation = Boolean.valueOf(getMainpage().getButtonspecifications().get("isDelegation").getSelection());
            connection = getElement();
        } else {
            delegation = Boolean.valueOf(getActualeditedelement().getAttributeValue(Attributes.ISDELEGATION.getID()));
            connection = getActualeditedelement();
        }
        int index = getReferencepage().getReferencegroupcombos().get(Role.BUS).getSelectionIndex();
        if (index > 0) {
        } else {
        }
        if (sourcesubcomponent == null && hwbus == null) {
            try {
                throw new InvalidParameterException("NOT EXPECTED!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }

    /**
     * @brief performs a Mapping.
     * @param source
     * @param target
     */
    public void performMapping(Role source, Role target) {
        IAXLangElement element = getTheRightElement();
        this.performAddSingleReference(element, source);
        this.performAddSingleReference(element, target);
    }

    /**
     * @brief performs a DataElementLink.
     */
    public void performDataElementLink() {
        IAXLangElement element = getTheRightElement();
        performAddSingleReference(element, Role.SOURCEDATAELEMENT);
        performAddSingleReference(element, Role.TARGETDATAELEMENT);
    }

    /**
     * @brief performs a SubService.
     */
    public void performSubService() {
        IAXLangElement element = getTheRightElement();
        performAddSingleReference(element, Role.SUBCOMPONENT);
        int parentindex = getReferencepage().getReferencegroupcombos().get(Role.SUBCOMPONENT).getSelectionIndex();
        IAXLangElement parentelement = getReferencepage().getRoleelementreferences().get(Role.SUBCOMPONENT).get(parentindex);
        IAXLangElement dataelement = getReferencepage().getElementreferencemap().get(parentelement).get(getReferencepage().getReferencegroupcombos().get(Role.FUNCTION).getSelectionIndex());
        try {
            element.addReference(dataelement, Role.FUNCTION);
        } catch (AXLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief performs a Trigger.
     */
    public void performTrigger() {
        IAXLangElement element = getTheRightElement();
        performAddSingleReference(element, Role.PORT);
        int parentindex = getReferencepage().getReferencegroupcombos().get(Role.PORT).getSelectionIndex();
        IAXLangElement parentelement = getReferencepage().getRoleelementreferences().get(Role.PORT).get(parentindex);
        IAXLangElement dataelement = getReferencepage().getElementreferencemap().get(parentelement).get(getReferencepage().getReferencegroupcombos().get(Role.DATAELEMENT).getSelectionIndex());
        try {
            element.addReference(dataelement, Role.DATAELEMENT);
        } catch (AXLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief performs a TransactionModel.
     */
    public void performTransactionModel() {
        IAXLangElement element = getTheRightElement();
        performAddSingleReference(element, Role.A2RMAPPING);
    }

    /**
     * @brief performs a RWAccess.
     */
    public void performRWAccess() {
        IAXLangElement element = getTheRightElement();
        int variableindex = getReferencepage().getReferencegroupcombos().get(Role.STORAGE).getSelectionIndex();
        if (variableindex > 0) {
            performAddSingleReference(element, Role.STORAGE);
        } else {
            performAddSingleReference(element, Role.PORT);
            int parentindex = getReferencepage().getReferencegroupcombos().get(Role.PORT).getSelectionIndex();
            parentindex = parentindex - 1;
            IAXLangElement parentelement = getReferencepage().getRoleelementreferences().get(Role.PORT).get(parentindex);
            IAXLangElement dataelement = getReferencepage().getElementreferencemap().get(parentelement).get(getReferencepage().getReferencegroupcombos().get(Role.DATAELEMENT).getSelectionIndex());
            try {
                element.addReference(dataelement, Role.DATAELEMENT);
            } catch (AXLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @brief performs a Behaviour.
     */
    public void performBehaviour() {
        IAXLangElement element = getTheRightElement();
        Set<Entry<String, LinkedHashMap<String, Boolean>>> entries = possibleattributes.entrySet();
        for (Entry<String, LinkedHashMap<String, Boolean>> entry : entries) {
            if (!entry.getKey().equals("description")) {
                if (getMainpage().getButtonspecifications().get(entry.getKey()).getSelection()) {
                    StyledText text = (StyledText) getNextpages().get(entry.getKey()).getValues().get("@" + entry.getKey() + "@StyledText");
                    if (!text.getText().equals("") && !entry.getKey().equals("interaction")) {
                        element.setAttributeValue(entry.getKey(), text.getText());
                    } else {
                        element.setAttributeValue(entry.getKey(), "default");
                    }
                }
            }
        }
    }

    /**
     * @brief adds a single reference to the element.
     * @param element
     * @param role
     */
    public void performAddSingleReference(IAXLangElement element, Role role) {
        int parentindex = getReferencepage().getReferencegroupcombos().get(role).getSelectionIndex();
        if (myrole.equals(Role.RWACCESS) || myrole.equals(Role.ACTIVITY) || myrole.equals(Role.ACTIVATION) && !role.equals(Role.TRIGGER) || myrole.equals(Role.ACTIVITYATTRIBUTE)) {
            parentindex = parentindex - 1;
        }
        IAXLangElement parentelement = null;
        parentelement = getReferencepage().getRoleelementreferences().get(role).get(parentindex);
        if (!(parentelement instanceof AbstractArchitectureModel) || getReferencepage().isMapping() || getReferencepage().isChildmapping()) {
            if (!EditableViewConstants.getInstance().getPathrolerelation().containsKey(role)) {
                try {
                    element.addReference(parentelement, role);
                } catch (AXLException e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }

    /**
     * @brief adds a single reference to the element via the depended role element map.
     * @param element
     * @param parentrole
     * @param role
     */
    public void performAddSingleReferenceFromComboViaDependedRole(IAXLangElement element, Role parentrole, Role role) {
        IAXLangElement parentelement = null;
        if (getReferencepage().getReferencegroupcombos().get(parentrole) == null) {
            parentelement = getReferencepage().getCurrentchosenelement();
        } else {
            int parentindex = getReferencepage().getReferencegroupcombos().get(parentrole).getSelectionIndex();
            parentelement = getReferencepage().getRoleelementreferences().get(parentrole).get(parentindex);
        }
        int index = getReferencepage().getReferencegroupcombos().get(role).getSelectionIndex();
        int sub = 0;
        if (getReferenceconstraints().get(myrole) == null) {
            if (index == 0) {
                return;
            }
            sub = 1;
        }
        IAXLangElement reference = null;
        reference = getReferencepage().getDependedroleselementmap().get(parentelement).get(role).get(index - sub);
        try {
            element.addReference(reference, role);
        } catch (AXLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief adds multiple references to the element from a table.
     * @param element
     * @param role
     * @param referencedelement
     */
    public void performAddMultipleReferencesFromTable(IAXLangElement element, Role role, IAXLangElement referencedelement) {
        if (getReferencepage().getDynamicdependedroleselementmap().get(referencedelement) != null && getReferencepage().getDynamicdependedroleselementmap().get(referencedelement).get(role) != null) {
            ArrayList<IAXLangElement> chosenelements = getReferencepage().getDynamicdependedroleselementmap().get(referencedelement).get(role);
            if (chosenelements != null) {
                for (int i = 0; i < chosenelements.size(); i++) {
                    if (role.equals(Role.SELECTEDFEATURE) || role.equals(Role.DESELECTEDFEATURE)) {
                        if (!element.getReferences(role).contains(chosenelements.get(i))) {
                            if (role.equals(Role.SELECTEDFEATURE)) {
                                if (element.getReferences(Role.DESELECTEDFEATURE).contains(chosenelements.get(i))) {
                                    element.removeReference(chosenelements.get(i), Role.DESELECTEDFEATURE);
                                }
                            } else if (role.equals(Role.DESELECTEDFEATURE)) {
                                if (element.getReferences(Role.SELECTEDFEATURE).contains(chosenelements.get(i))) {
                                    element.removeReference(chosenelements.get(i), Role.SELECTEDFEATURE);
                                }
                            }
                            try {
                                element.addReference(chosenelements.get(i), role);
                            } catch (AXLException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            element.addReference(chosenelements.get(i), role);
                        } catch (AXLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean performFinish() {
        if (element != null) {
            getMainpage().performFinish();
        } else {
            referencepage.performFinish();
        }
        return true;
    }

    /**
     * @return the actualeditedelement
     */
    public IAXLangElement getActualeditedelement() {
        return actualeditedelement;
    }

    /**
     * @return the myDialog
     */
    public EditableViewWizardDialog getMyDialog() {
        return myDialog;
    }

    /**
     * @return the myDialog
     */
    public void setMyDialog(EditableViewWizardDialog dialog) {
        myDialog = dialog;
    }

    /**
     * @return the mainpage
     */
    public EditableViewMainWizardPage getMainpage() {
        return mainpage;
    }

    @Override
    public boolean canFinish() {
        for (int i = 0; i < getPages().length; i++) {
            if (!getPages()[i].isPageComplete()) {
                return false;
            }
        }
        if (getMainpage() != null) {
            if (getMainpage().isNofinishallowed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @brief looks for the right element for adding the new child or reference.
     * @return
     */
    private IAXLangElement getTheRightElement() {
        if (referenceaction) {
            return getActualeditedelement();
        }
        return getElement();
    }

    /**
     * @return the neededattributes
     */
    public LinkedHashMap<Role, ArrayList<String>> getNeededattributes() {
        return neededattributes;
    }

    /**
     * @return the referenceconstraints
     */
    public LinkedHashMap<Role, ArrayList<Role>> getReferenceconstraints() {
        return referenceconstraints;
    }

    /**
     * @return the nextpages
     */
    public LinkedHashMap<String, EditableViewNextWizardPage> getNextpages() {
        return nextpages;
    }

    /**
     * @return the attributechoosen
     */
    public LinkedHashMap<String, AtomicBoolean> getAttributechoosen() {
        return attributechoosen;
    }

    /**
     * @return the pageorder
     */
    public ArrayList<String> getPageorder() {
        return pageorder;
    }

    /**
     * @return the possibleattributes
     */
    public LinkedHashMap<String, LinkedHashMap<String, Boolean>> getPossibleattributes() {
        return possibleattributes;
    }

    /**
     * @return the referencepage
     */
    public EditableViewReferenceWizardPage getReferencepage() {
        return referencepage;
    }

    /**
     * @return the possiblereferences
     */
    public Vector<Role> getPossiblereferences() {
        return possiblereferences;
    }

    /**
     * @return the element
     */
    public IAXLangElement getElement() {
        return element;
    }
}
