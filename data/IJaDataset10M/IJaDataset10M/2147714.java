package org.pubcurator.core.dialogs;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.pubcurator.core.managers.AnalyzersManager;
import org.pubcurator.core.ui.RevisedTitleAreaDialog;
import org.pubcurator.core.ui.RevisedTitleAreaDialogSupport;
import org.pubcurator.core.ui.TextFieldValidator;
import org.pubcurator.model.projectstore.PubAnalyzerDelegate;
import org.pubcurator.model.projectstore.PubCategoryDelegate;
import org.pubcurator.model.projectstore.PubColor;
import org.pubcurator.model.projectstore.PubGraphElement;
import org.pubcurator.model.projectstore.PubProject;
import org.pubcurator.model.projectstore.PubProjectStoreFactory;
import org.pubcurator.model.projectstore.PubProjectStorePackage;
import org.pubcurator.model.projectstore.PubTopic;
import org.pubcurator.model.projectstore.PubTopicRelation;
import org.pubcurator.uima.config.Category;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public class EditGraphElementDialog extends RevisedTitleAreaDialog {

    private PubGraphElement graphElementToEdit;

    private PubProject project;

    private List<PubCategoryDelegate> notAvailableCategoryDelegates;

    private DataBindingContext bindingContext;

    private TableViewer categoryDelegatesTableViewer;

    private WritableList includedCategoryDelegates;

    private Button addButton;

    private Button removeButton;

    /**
	 * @param parentShell
	 */
    public EditGraphElementDialog(Shell parentShell, PubGraphElement graphElementToEdit, PubProject project) {
        super(parentShell);
        this.graphElementToEdit = graphElementToEdit;
        this.project = project;
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected Control createContents(Composite parent) {
        Control control = super.createContents(parent);
        if (graphElementToEdit instanceof PubTopic) {
            setTitle("Edit Topic");
            setDescription("Edit the parameters of this topic.");
        } else {
            setTitle("Edit Relation");
            setDescription("Edit the parameters of this relation.");
        }
        return control;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite comp = new Composite((Composite) super.createDialogArea(parent), SWT.NONE);
        comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        comp.setLayout(new GridLayout(2, false));
        bindingContext = new DataBindingContext();
        TabFolder tabFolder = new TabFolder(comp, SWT.TOP);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        TabItem settingsTabItem = new TabItem(tabFolder, SWT.NONE);
        settingsTabItem.setText("Settings");
        Composite settingsComp = new Composite(tabFolder, SWT.NONE);
        settingsComp.setLayout(new GridLayout(2, false));
        settingsTabItem.setControl(settingsComp);
        Label label = new Label(settingsComp, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("Name");
        Text nameText = new Text(settingsComp, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        GraphElementIdValidator idValidator = new GraphElementIdValidator(label.getText(), true);
        bindTextToModel(nameText, idValidator, graphElementToEdit, PubProjectStorePackage.Literals.PUB_GRAPH_ELEMENT__NAME);
        label = new Label(settingsComp, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("Description");
        Text descriptionText = new Text(settingsComp, SWT.BORDER | SWT.MULTI);
        GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gd.heightHint = 50;
        descriptionText.setLayoutData(gd);
        TextFieldValidator descriptionValidator = new TextFieldValidator("Description", false);
        bindTextToModel(descriptionText, descriptionValidator, graphElementToEdit, PubProjectStorePackage.Literals.PUB_GRAPH_ELEMENT__DESCRIPTION);
        label = new Label(settingsComp, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("Color");
        ColorSelector colorSelector = new ColorSelector(settingsComp);
        colorSelector.getButton().setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        bindColorSelectorToModel(colorSelector, graphElementToEdit, PubProjectStorePackage.Literals.PUB_GRAPH_ELEMENT__COLOR);
        Button requiredButton = new Button(settingsComp, SWT.CHECK);
        requiredButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 2, 1));
        requiredButton.setText("Required");
        bindRequiredButtonToModel(requiredButton, graphElementToEdit, PubProjectStorePackage.Literals.PUB_GRAPH_ELEMENT__REQUIRED);
        label = new Label(settingsComp, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
        label.setText("Included Categories");
        categoryDelegatesTableViewer = new TableViewer(settingsComp, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd.heightHint = categoryDelegatesTableViewer.getTable().getItemHeight() * 5;
        categoryDelegatesTableViewer.getTable().setLayoutData(gd);
        setupCategoriesTableViewer();
        Composite buttonComp = new Composite(settingsComp, SWT.NONE);
        buttonComp.setLayoutData(new GridData(SWT.BEGINNING, SWT.BOTTOM, true, false, 2, 1));
        buttonComp.setLayout(new RowLayout(SWT.HORIZONTAL));
        addButton = new Button(buttonComp, SWT.PUSH);
        addButton.setText("Add");
        removeButton = new Button(buttonComp, SWT.PUSH);
        removeButton.setText("Remove");
        setupListeners();
        bindingContext.updateTargets();
        RevisedTitleAreaDialogSupport.create(this, bindingContext);
        return comp;
    }

    private void checkForNotAvailableCategories() {
        notAvailableCategoryDelegates = new ArrayList<PubCategoryDelegate>();
        for (PubCategoryDelegate categoryDelegate : graphElementToEdit.getCategoryDelegates()) {
            Category category = AnalyzersManager.INSTANCE.getCategory(categoryDelegate);
            if (category == null) {
                notAvailableCategoryDelegates.add(categoryDelegate);
            }
        }
        if (notAvailableCategoryDelegates.size() > 0) setMessage("One or more selected categories are not available anymore (marked yellow).", IMessageProvider.WARNING); else setMessage(null);
    }

    private void setupListeners() {
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addCategories();
            }
        });
        removeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelectedCategories();
            }
        });
    }

    private void addCategories() {
        List<PubCategoryDelegate> categoryDelegatesToSelectFrom = new ArrayList<PubCategoryDelegate>();
        for (PubAnalyzerDelegate analyzerDelegate : project.getAnalyzerDelegates()) {
            for (Category category : AnalyzersManager.INSTANCE.getCategoriesForDelegate(analyzerDelegate)) {
                if ((category.isRelation() && graphElementToEdit instanceof PubTopicRelation) || (!category.isRelation() && graphElementToEdit instanceof PubTopic)) {
                    PubCategoryDelegate categoryDelegate = PubProjectStoreFactory.eINSTANCE.createPubCategoryDelegate();
                    categoryDelegate.setCategoryName(category.getName());
                    categoryDelegate.setAnalyzerDelegate(analyzerDelegate);
                    categoryDelegatesToSelectFrom.add(categoryDelegate);
                }
            }
        }
        categoryDelegatesToSelectFrom = filterOutAlreadyIncludedCategoryDelegates(categoryDelegatesToSelectFrom);
        if (categoryDelegatesToSelectFrom.size() == 0) {
            MessageDialog.openInformation(getShell(), "Categories", "No more catagories to add are available");
            return;
        }
        SelectCategoriesDialog dialog = new SelectCategoriesDialog(getShell(), categoryDelegatesToSelectFrom);
        dialog.setTitle("Include Categories");
        dialog.open();
        if (dialog.getReturnCode() == SelectCategoriesDialog.OK) {
            Object result[] = dialog.getResult();
            for (Object obj : result) {
                PubCategoryDelegate categoryDelegate = (PubCategoryDelegate) obj;
                includedCategoryDelegates.add(categoryDelegate);
            }
        }
    }

    private List<PubCategoryDelegate> filterOutAlreadyIncludedCategoryDelegates(List<PubCategoryDelegate> categoryDelegatesToFilter) {
        List<PubCategoryDelegate> filteredCategoryDelegates = new ArrayList<PubCategoryDelegate>();
        for (PubCategoryDelegate categoryDelegateToFilter : categoryDelegatesToFilter) {
            boolean alreadyAdded = false;
            for (Object obj : includedCategoryDelegates) {
                PubCategoryDelegate categoryDelegate = (PubCategoryDelegate) obj;
                if (categoryDelegateToFilter.getCategoryName().equals(categoryDelegate.getCategoryName()) && categoryDelegateToFilter.getAnalyzerDelegate() == categoryDelegate.getAnalyzerDelegate()) {
                    alreadyAdded = true;
                    break;
                }
            }
            if (!alreadyAdded) {
                filteredCategoryDelegates.add(categoryDelegateToFilter);
            }
        }
        return filteredCategoryDelegates;
    }

    @SuppressWarnings("unchecked")
    private void removeSelectedCategories() {
        IStructuredSelection selection = (IStructuredSelection) categoryDelegatesTableViewer.getSelection();
        List<PubCategoryDelegate> categoryDelegates = selection.toList();
        includedCategoryDelegates.removeAll(categoryDelegates);
        checkForNotAvailableCategories();
    }

    private Binding bindColorSelectorToModel(ColorSelector colorSelector, EObject eObject, EStructuralFeature eStructuralFeature) {
        return bindingContext.bindValue(PojoObservables.observeValue(colorSelector, "colorValue"), EMFObservables.observeValue(eObject, eStructuralFeature), new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST).setConverter(new RgbToPubColorConverter()), new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST).setConverter(new PubColorToRgbConverter()));
    }

    private Binding bindLists(WritableList targetList, EObject eObject, EStructuralFeature eStructuralFeature) {
        return bindingContext.bindList(targetList, EMFObservables.observeList(eObject, eStructuralFeature), new UpdateListStrategy(UpdateListStrategy.POLICY_ON_REQUEST), new UpdateListStrategy(UpdateListStrategy.POLICY_ON_REQUEST));
    }

    private void setupCategoriesTableViewer() {
        checkForNotAvailableCategories();
        categoryDelegatesTableViewer.setContentProvider(new ObservableListContentProvider());
        categoryDelegatesTableViewer.setLabelProvider(new PubCategoryReferenceLabelProvider());
        includedCategoryDelegates = new WritableList();
        includedCategoryDelegates.addAll(graphElementToEdit.getCategoryDelegates());
        categoryDelegatesTableViewer.setInput(includedCategoryDelegates);
        bindLists(includedCategoryDelegates, graphElementToEdit, PubProjectStorePackage.Literals.PUB_GRAPH_ELEMENT__CATEGORY_DELEGATES);
    }

    private Binding bindTextToModel(Text text, TextFieldValidator validator, EObject eObject, EStructuralFeature eStructuralFeature) {
        return bindingContext.bindValue(SWTObservables.observeText(text, SWT.Modify), EMFObservables.observeValue(eObject, eStructuralFeature), new UpdateValueStrategy(UpdateValueStrategy.POLICY_CONVERT).setAfterConvertValidator(validator), new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST).setAfterConvertValidator(validator));
    }

    private Binding bindRequiredButtonToModel(Button button, EObject eObject, EStructuralFeature eStructuralFeature) {
        return bindingContext.bindValue(SWTObservables.observeSelection(button), EMFObservables.observeValue(eObject, eStructuralFeature), new UpdateValueStrategy(UpdateValueStrategy.POLICY_CONVERT).setAfterConvertValidator(new RequiredValidator()), new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST));
    }

    @Override
    protected void okPressed() {
        bindingContext.updateModels();
        super.okPressed();
    }

    private class PubCategoryReferenceLabelProvider extends ColumnLabelProvider {

        @Override
        public Color getBackground(Object element) {
            PubCategoryDelegate categoryDelegate = (PubCategoryDelegate) element;
            if (notAvailableCategoryDelegates.contains(categoryDelegate)) {
                return ColorConstants.yellow;
            }
            return super.getBackground(element);
        }

        @Override
        public String getText(Object element) {
            PubCategoryDelegate categoryDelegate = (PubCategoryDelegate) element;
            return categoryDelegate.getCategoryName() + " [" + categoryDelegate.getAnalyzerDelegate().getDelegateName() + "]";
        }
    }

    private class RgbToPubColorConverter extends Converter {

        public RgbToPubColorConverter() {
            super(RGB.class, PubColor.class);
        }

        @Override
        public Object convert(Object fromObject) {
            RGB rgb = (RGB) fromObject;
            PubColor color = PubProjectStoreFactory.eINSTANCE.createPubColor();
            color.setRed(rgb.red);
            color.setGreen(rgb.green);
            color.setBlue(rgb.blue);
            return color;
        }
    }

    private class PubColorToRgbConverter extends Converter {

        public PubColorToRgbConverter() {
            super(PubColor.class, RGB.class);
        }

        @Override
        public Object convert(Object fromObject) {
            PubColor color = (PubColor) fromObject;
            RGB rgb = new RGB(color.getRed(), color.getGreen(), color.getBlue());
            return rgb;
        }
    }

    private class GraphElementIdValidator extends TextFieldValidator {

        /**
		 * @param fieldName
		 * @param controlDecoration
		 * @param required
		 * @param maximumStringLength
		 */
        public GraphElementIdValidator(String fieldName, boolean required) {
            super(fieldName, required);
        }

        @Override
        public IStatus validate(Object value) {
            IStatus status = super.validate(value);
            if (!status.equals(ValidationStatus.ok())) {
                return status;
            } else {
                String text = (String) value;
                List<PubGraphElement> allGraphElements = new ArrayList<PubGraphElement>();
                allGraphElements.addAll(project.getSearchGraph().getTopics());
                allGraphElements.addAll(project.getSearchGraph().getRelations());
                for (PubGraphElement graphElement : allGraphElements) {
                    if (graphElement != graphElementToEdit) {
                        if (graphElement.getName().equals(text)) {
                            return ValidationStatus.error("This name is already used by another topic or topic relation.");
                        }
                    }
                }
                return ValidationStatus.ok();
            }
        }
    }

    private class RequiredValidator implements IValidator {

        @Override
        public IStatus validate(Object value) {
            boolean required = (Boolean) value;
            if (required) {
                if (graphElementToEdit instanceof PubTopicRelation) {
                    PubTopicRelation topicRelation = (PubTopicRelation) graphElementToEdit;
                    if (!topicRelation.getSource().isRequired() || !topicRelation.getDestination().isRequired()) {
                        return ValidationStatus.error("An relation of non required topics can't be required.");
                    }
                }
            }
            return ValidationStatus.ok();
        }
    }
}
