package pedro.soa.alerts;

import pedro.mda.model.*;
import pedro.mda.config.SchemaConceptPropertyManager;
import pedro.system.*;
import pedro.util.RecordClassContextUtility;
import pedro.soa.ontology.sources.OntologyContext;
import pedro.soa.ontology.provenance.OntologyTermProvenanceManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MatchingCriteriaView extends JPanel implements ActionListener {

    private PedroUIFactory pedroUIFactory;

    private RecordModelFactory recordModelFactory;

    private SchemaConceptPropertyManager schemaConceptPropertyManager;

    private HashMap viewFromDeleteButton;

    private JComboBox recordTypes;

    private MatchingCriteria matchingCriteria;

    private ArrayList criteriaViews;

    private JButton addEditFieldCriterion;

    private JButton addListFieldCriterion;

    private boolean isEnabled;

    private boolean includeListFieldCriterion;

    private OntologyContext ontologyContext;

    private FieldOperatorProvider fieldOperatorProvider;

    private HashMap fieldsToExclude;

    private PedroFormContext pedroFormContext;

    public MatchingCriteriaView(PedroFormContext pedroFormContext) {
        init(pedroFormContext);
        String[] recordClassContexts = recordModelFactory.getRecordClassNames();
        Arrays.sort(recordClassContexts);
        for (int i = 0; i < recordClassContexts.length; i++) {
            recordTypes.addItem(recordClassContexts[i]);
        }
        recordTypes.addActionListener(this);
        if (recordClassContexts.length > 0) {
            ontologyContext.setCurrentRecordClassName(recordClassContexts[0]);
        }
        buildUI();
    }

    public MatchingCriteriaView(PedroFormContext pedroFormContext, String[] acceptedRecordClassContexts) {
        init(pedroFormContext);
        ArrayList factories = recordModelFactory.getRecordFactories();
        int numberOfFactories = factories.size();
        for (int i = 0; i < acceptedRecordClassContexts.length; i++) {
            recordTypes.addItem(acceptedRecordClassContexts[i]);
        }
        recordTypes.addActionListener(this);
        if (acceptedRecordClassContexts.length > 0) {
            String recordClassName = RecordClassContextUtility.determineRecordClassName(acceptedRecordClassContexts[0]);
            ontologyContext.setCurrentRecordClassName(acceptedRecordClassContexts[0]);
        }
        buildUI();
    }

    private void init(PedroFormContext pedroFormContext) {
        this.pedroFormContext = pedroFormContext;
        recordModelFactory = (RecordModelFactory) pedroFormContext.getApplicationProperty(PedroApplicationContext.RECORD_MODEL_FACTORY);
        schemaConceptPropertyManager = (SchemaConceptPropertyManager) pedroFormContext.getApplicationProperty(PedroApplicationContext.CONFIGURATION_READER);
        this.pedroUIFactory = (PedroUIFactory) pedroFormContext.getApplicationProperty(PedroApplicationContext.USER_INTERFACE_FACTORY);
        pedroUIFactory.setPanelProperties(this);
        viewFromDeleteButton = new HashMap();
        fieldsToExclude = new HashMap();
        isEnabled = true;
        matchingCriteria = new MatchingCriteria();
        criteriaViews = new ArrayList();
        recordTypes = pedroUIFactory.createComboBox();
        recordTypes.setPrototypeDisplayValue("aaaaaaaaaaaaaaaaaaaaa");
        includeListFieldCriterion = true;
        OntologyTermProvenanceManager ontologyTermProvenanceManager = new OntologyTermProvenanceManager();
        ontologyContext = new OntologyContext(ontologyTermProvenanceManager);
        String addListFieldCriterionText = PedroResources.getMessage("pedro.alerts.addListFieldCriterion");
        addListFieldCriterion = pedroUIFactory.createButton(addListFieldCriterionText);
        addListFieldCriterion.addActionListener(this);
        String addEditFieldCriterionText = PedroResources.getMessage("pedro.alerts.addEditFieldCriterion");
        addEditFieldCriterion = pedroUIFactory.createButton(addEditFieldCriterionText);
        addEditFieldCriterion.addActionListener(this);
        this.fieldOperatorProvider = new DefaultFieldOperatorProvider();
    }

    public void setIncludeListFieldCriterion(boolean includeListFieldCriterion) {
        this.includeListFieldCriterion = includeListFieldCriterion;
        buildUI();
    }

    public void buildUI() {
        removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints panelGC = new GridBagConstraints();
        panelGC.gridx = 0;
        panelGC.gridy = 0;
        panelGC.anchor = GridBagConstraints.NORTHWEST;
        panelGC.weightx = 100;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        add(createSelectRecordTypePanel(), panelGC);
        if (includeListFieldCriterion == true) {
            panelGC.gridy++;
            panelGC.fill = GridBagConstraints.NONE;
            panelGC.weightx = 0;
            add(createAddItemPanel(), panelGC);
        }
        checkAddButtonStates();
        panelGC.gridy++;
        panelGC.weightx = 100;
        panelGC.weighty = 100;
        panelGC.fill = GridBagConstraints.BOTH;
        JScrollPane fieldPane = pedroUIFactory.createScrollPane(createFieldPanel());
        add(fieldPane, panelGC);
        updateUI();
    }

    private void checkAddButtonStates() {
        if (isEnabled == false) {
            addEditFieldCriterion.setEnabled(false);
            addListFieldCriterion.setEnabled(false);
            return;
        }
        String selectedRecordClassContext = (String) recordTypes.getSelectedItem();
        String recordClassName = RecordClassContextUtility.determineRecordClassName(selectedRecordClassContext);
        RecordModelUtility recordModelUtility = new RecordModelUtility();
        RecordModel recordModel = recordModelFactory.createRecordModel(recordClassName);
        ArrayList editFields = recordModelUtility.getEditFields(recordModel);
        if (editFields.size() == 0) {
            addEditFieldCriterion.setEnabled(false);
        } else {
            addEditFieldCriterion.setEnabled(true);
        }
        ArrayList listFields = recordModelUtility.getListFields(recordModel);
        if (listFields.size() == 0) {
            addListFieldCriterion.setEnabled(false);
        } else {
            addListFieldCriterion.setEnabled(true);
        }
    }

    private JPanel createFieldPanel() {
        JPanel fieldPanel = pedroUIFactory.createPanel(new BorderLayout());
        JPanel panel = pedroUIFactory.createPanel(new GridBagLayout());
        GridBagConstraints panelGC = new GridBagConstraints();
        panelGC.gridx = 0;
        panelGC.gridy = 0;
        panelGC.anchor = GridBagConstraints.NORTHWEST;
        panelGC.weightx = 100;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        int numberOfViews = criteriaViews.size();
        for (int i = 0; i < numberOfViews; i++) {
            CriterionView currentView = (CriterionView) criteriaViews.get(i);
            panelGC.gridy++;
            panel.add(currentView, panelGC);
        }
        fieldPanel.add(panel, BorderLayout.NORTH);
        return fieldPanel;
    }

    private JPanel createSelectRecordTypePanel() {
        JPanel panel = pedroUIFactory.createPanel(new GridBagLayout());
        GridBagConstraints panelGC = pedroUIFactory.createGridBagConstraints();
        String selectRecordTypeText = PedroResources.getMessage("pedro.alerts.matchingCriteriaView.select");
        JLabel selectRecordTypeLabel = pedroUIFactory.createLabel(selectRecordTypeText);
        panel.add(selectRecordTypeLabel, panelGC);
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.weightx = 100;
        panelGC.gridx++;
        panel.add(recordTypes, panelGC);
        if (includeListFieldCriterion == false) {
            panelGC.gridx++;
            panelGC.anchor = GridBagConstraints.NORTHEAST;
            panelGC.fill = GridBagConstraints.NONE;
            panel.add(addEditFieldCriterion, panelGC);
        }
        return panel;
    }

    private JPanel createAddItemPanel() {
        JPanel panel = pedroUIFactory.createPanel();
        GridBagConstraints panelGC = pedroUIFactory.createGridBagConstraints();
        panel.add(addEditFieldCriterion, panelGC);
        panelGC.gridx++;
        panel.add(addListFieldCriterion, panelGC);
        panelGC.gridx++;
        return panel;
    }

    public ArrayList validateCriteria() {
        ArrayList alerts = new ArrayList();
        StringBuffer errorMessages = new StringBuffer();
        int numberOfViews = criteriaViews.size();
        for (int i = 0; i < numberOfViews; i++) {
            CriterionView currentView = (CriterionView) criteriaViews.get(i);
            alerts.addAll(currentView.validateCriterion());
        }
        return alerts;
    }

    public MatchingCriteria getMatchingCriteria() {
        return matchingCriteria;
    }

    public boolean isDirty() {
        int numberOfViews = criteriaViews.size();
        for (int i = 0; i < numberOfViews; i++) {
            CriterionView currentView = (CriterionView) criteriaViews.get(i);
            if (currentView.isDirty() == true) {
                return true;
            }
        }
        return false;
    }

    public ArrayList getCriteriaViews() {
        return criteriaViews;
    }

    public OntologyContext getOntologyContext() {
        return ontologyContext;
    }

    public void excludeRecordClassName(String recordClassName) {
        recordTypes.removeItem(recordClassName);
    }

    public void excludeField(String recordClassName, String fieldName) {
        ArrayList fields = (ArrayList) fieldsToExclude.get(recordClassName);
        if (fields == null) {
            fields = new ArrayList();
        }
        int index = fields.indexOf(fieldName);
        if (index == -1) {
            fields.add(fieldName);
        }
        fieldsToExclude.put(recordClassName, fields);
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        recordTypes.setEnabled(isEnabled);
        addEditFieldCriterion.setEnabled(isEnabled);
        addListFieldCriterion.setEnabled(isEnabled);
    }

    public void clear() {
        int numberOfViews = criteriaViews.size();
        for (int i = 0; i < numberOfViews; i++) {
            CriterionView currentView = (CriterionView) criteriaViews.get(i);
            currentView.clear();
        }
        if (ontologyContext != null) {
            ontologyContext.clearTerms();
        }
    }

    public void reset() {
        criteriaViews.clear();
        matchingCriteria.clear();
        if (ontologyContext != null) {
            ontologyContext.clearTerms();
        }
        buildUI();
    }

    public void save() {
        String selectedRecordClassContext = (String) recordTypes.getSelectedItem();
        matchingCriteria.setRecordClassContext(selectedRecordClassContext);
        int numberOfViews = criteriaViews.size();
        for (int i = 0; i < numberOfViews; i++) {
            CriterionView currentView = (CriterionView) criteriaViews.get(i);
            currentView.commitChanges();
        }
    }

    public void setMatchingCriteria(MatchingCriteria matchingCriteria) {
        this.matchingCriteria = matchingCriteria;
        String recordClassContext = matchingCriteria.getRecordClassContext();
        String recordClassName = RecordClassContextUtility.determineRecordClassName(recordClassContext);
        criteriaViews.clear();
        if (recordClassName != null) {
            recordTypes.removeActionListener(this);
            recordTypes.setSelectedItem(recordClassContext);
            RecordModel recordModel = recordModelFactory.createRecordModel(recordClassName);
            ArrayList criteria = matchingCriteria.getCriteria();
            int numberOfCriteria = criteria.size();
            for (int i = 0; i < numberOfCriteria; i++) {
                Object currentCriterion = criteria.get(i);
                if (currentCriterion instanceof EditFieldMatchingCriterion) {
                    EditFieldMatchingCriterion criterionModel = (EditFieldMatchingCriterion) currentCriterion;
                    EditFieldCriterionView criterionView = new EditFieldCriterionView(pedroFormContext, ontologyContext, recordModel, fieldOperatorProvider);
                    criterionView.setEditFieldMatchingCriterion(criterionModel);
                    criteriaViews.add(criterionView);
                    viewFromDeleteButton.put(criterionView.getDeleteButton(), criterionView);
                } else {
                    ListFieldMatchingCriterion criterionModel = (ListFieldMatchingCriterion) currentCriterion;
                    ListFieldCriterionView criterionView = new ListFieldCriterionView(pedroFormContext, recordModel, fieldOperatorProvider);
                    criterionView.setListFieldMatchingCriterion(criterionModel);
                    criteriaViews.add(criterionView);
                    viewFromDeleteButton.put(criterionView.getDeleteButton(), criterionView);
                }
            }
            recordTypes.addActionListener(this);
        }
        buildUI();
    }

    /**
	* sets the field operator provider, which provides the operators such as
	* "=", "not contains", etc...  The alerts system just uses the DefaultFieldOperatorProvider
	* although other programs such as Pierre may have their own provider.  for example,
	* the field operator provider may return an operator such as "like" which has a specific
	* meaning in relational database terminology.
	*
	*/
    public void setFieldOperatorProvider(FieldOperatorProvider fieldOperatorProvider) {
        this.fieldOperatorProvider = fieldOperatorProvider;
    }

    /**
	* clears Ontology Context of all ontology terms that may have been used to mark-up
	* the form
	*/
    private void resetOntologyContext() {
        ontologyContext.clearTerms();
        String selectedRecordClassContext = (String) recordTypes.getSelectedItem();
        String recordClassName = RecordClassContextUtility.determineRecordClassName(selectedRecordClassContext);
        ontologyContext.setCurrentRecordClassName(recordClassName);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == addEditFieldCriterion) {
            addEditFieldCriterion();
        } else if (source == addListFieldCriterion) {
            addListFieldCriterion();
        } else if (source == recordTypes) {
            criteriaViews.clear();
            matchingCriteria.clear();
            resetOntologyContext();
            buildUI();
        } else {
            CriterionView currentView = (CriterionView) viewFromDeleteButton.get(source);
            MatchingCriterion matchingCriterion = currentView.getMatchingCriterion();
            String fieldName = matchingCriterion.getFieldName();
            ontologyContext.removeField(fieldName);
            viewFromDeleteButton.remove(source);
            criteriaViews.remove(currentView);
            MatchingCriterion criterionModel = currentView.getMatchingCriterion();
            matchingCriteria.removeMatchingCriterion(criterionModel);
            buildUI();
        }
    }

    private void addEditFieldCriterion() {
        String selectedRecordClassContext = (String) recordTypes.getSelectedItem();
        String recordClassName = RecordClassContextUtility.determineRecordClassName(selectedRecordClassContext);
        RecordModel recordModel = recordModelFactory.createRecordModel(recordClassName);
        EditFieldCriterionView view = new EditFieldCriterionView(pedroFormContext, ontologyContext, recordModel, fieldOperatorProvider);
        ArrayList fields = (ArrayList) fieldsToExclude.get(recordClassName);
        if (fields != null) {
            int numberOfFields = fields.size();
            for (int i = 0; i < numberOfFields; i++) {
                String currentFieldName = (String) fields.get(i);
                view.removeRecordFieldChoice(currentFieldName);
            }
        }
        view.refresh();
        matchingCriteria.addMatchingCriterion(view.getEditFieldMatchingCriterion());
        JButton deleteButton = view.getDeleteButton();
        deleteButton.addActionListener(this);
        viewFromDeleteButton.put(deleteButton, view);
        criteriaViews.add(view);
        buildUI();
    }

    private void addListFieldCriterion() {
        String selectedRecordClassContext = (String) recordTypes.getSelectedItem();
        String recordClassName = RecordClassContextUtility.determineRecordClassName(selectedRecordClassContext);
        RecordModel recordModel = recordModelFactory.createRecordModel(recordClassName);
        ListFieldCriterionView view = new ListFieldCriterionView(pedroFormContext, recordModel, fieldOperatorProvider);
        matchingCriteria.addMatchingCriterion(view.getListFieldMatchingCriterion());
        JButton deleteButton = view.getDeleteButton();
        deleteButton.addActionListener(this);
        viewFromDeleteButton.put(deleteButton, view);
        criteriaViews.add(view);
        buildUI();
    }
}
