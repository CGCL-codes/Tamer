package com.ivis.xprocess.ui.wizards.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import com.ivis.xprocess.core.Artifact;
import com.ivis.xprocess.core.Parameter;
import com.ivis.xprocess.core.ParameterGroup;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.UIConstants;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.PortfolioWrapper;
import com.ivis.xprocess.ui.datawrappers.RootWrapper;
import com.ivis.xprocess.ui.factories.creation.ElementCreationFactory;
import com.ivis.xprocess.ui.listeners.IListenToDataSourceChange;
import com.ivis.xprocess.ui.perspectives.manager.IManagePerspective;
import com.ivis.xprocess.ui.perspectives.manager.PerspectiveFactory;
import com.ivis.xprocess.ui.properties.DialogMessages;
import com.ivis.xprocess.ui.properties.WizardMessages;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory;
import com.ivis.xprocess.ui.util.ElementUtil;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.ui.util.WorkbenchUtil;
import com.ivis.xprocess.ui.view.providers.ExplorerViewLabelProvider;
import com.ivis.xprocess.ui.view.providers.IElementProvider;
import com.ivis.xprocess.ui.widgets.ParametersTableWidget;
import com.ivis.xprocess.ui.wizards.XProcessWizardPage;
import com.ivis.xprocess.ui.wizards.process.InternalParameter;
import com.ivis.xprocess.ui.wizards.tasks.TaskPreviewWizardPage;

public class NewProjectWizardPage extends XProcessWizardPage implements IElementProvider {

    private IElementWrapper portfolioWrapper;

    private Text portfolioText;

    private Button portfolioButton;

    private ParametersTableWidget parametersTableWidget;

    private Label noOfElementsToBeCreatedLabel;

    private java.util.List<InternalParameter> internalParameters = new LinkedList<InternalParameter>();

    private IProvideProjectPattern provideProjectPattern;

    private String separator;

    private Button setAsDefault;

    private boolean useAsDefault;

    private Label patternLabel;

    public NewProjectWizardPage(String pageName, IProvideProjectPattern provideProjectPattern) {
        super(pageName);
        this.setTitle(WizardMessages.project_wizard_title);
        this.setDescription(WizardMessages.project_wizard_description);
        this.provideProjectPattern = provideProjectPattern;
    }

    public NewProjectWizardPage(String pageName, Object selectedObject, IProvideProjectPattern provideProjectPattern) {
        super(pageName, selectedObject);
        this.setTitle(WizardMessages.project_wizard_title);
        this.setDescription(WizardMessages.project_wizard_description);
        this.provideProjectPattern = provideProjectPattern;
    }

    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        container.setLayout(gridLayout);
        patternLabel = new Label(container, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 3;
        patternLabel.setLayoutData(data);
        Label portfolioLabel = new Label(container, SWT.NONE);
        portfolioLabel.setText(WizardMessages.portfolio_field);
        data = new GridData();
        portfolioLabel.setLayoutData(data);
        portfolioText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_HORIZONTAL);
        portfolioText.setLayoutData(data);
        portfolioButton = new Button(container, SWT.PUSH);
        portfolioButton.setText(WizardMessages.browse_button);
        portfolioButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new ExplorerViewLabelProvider());
                dialog.setTitle(WizardMessages.portfolio_selectiondialog_title);
                dialog.setMessage(DialogMessages.select_element_dialog_message);
                dialog.setElements(ElementUtil.getAllElementsOfType(UIType.portfolio));
                if (dialog.open() == Window.OK) {
                    if (dialog.getFirstResult() != null) {
                        portfolioText.setText(((IElementWrapper) dialog.getFirstResult()).getLabel());
                        portfolioWrapper = (PortfolioWrapper) dialog.getFirstResult();
                    }
                }
                checkData();
            }
        });
        setupData();
        parametersTableWidget = new ParametersTableWidget(container, SWT.NONE, this);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 3;
        parametersTableWidget.setLayoutData(data);
        parametersTableWidget.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (multipleCheckBox.getSelection()) {
                    updateElementCountLabel();
                }
            }
        });
        noOfElementsToBeCreatedLabel = new Label(container, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 3;
        noOfElementsToBeCreatedLabel.setLayoutData(data);
        setVisible();
        createSeparatorCombo(container, DialogMessages.pattern_instantiation_multiple_label);
        createOpenEditorCheckBox(container);
        createSetProjectDefaultCheckBox(container);
        updateElementCountLabel();
        setControl(container);
        setupTestHarness();
    }

    private void setVisible() {
        boolean visible = getParameters().length > 0;
        parametersTableWidget.setVisible(visible);
        noOfElementsToBeCreatedLabel.setVisible(visible);
        container.layout(true, true);
    }

    @Override
    public boolean canFlipToNextPage() {
        return false;
    }

    private void createOpenEditorCheckBox(Composite container) {
        Composite openEditorComposite = new Composite(container, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        openEditorComposite.setLayout(gridLayout);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.BEGINNING;
        data.horizontalSpan = 3;
        openEditorComposite.setLayoutData(data);
        openEditorButton = new Button(openEditorComposite, SWT.CHECK);
        openEditorButton.setSelection(true);
        openEditorButton.setText(DialogMessages.open_editor);
    }

    private void createSetProjectDefaultCheckBox(Composite container) {
        Composite openEditorComposite = new Composite(container, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        openEditorComposite.setLayout(gridLayout);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.BEGINNING;
        data.horizontalSpan = 3;
        openEditorComposite.setLayoutData(data);
        setAsDefault = new Button(openEditorComposite, SWT.CHECK);
        setAsDefault.setSelection(true);
        setAsDefault.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                useAsDefault = setAsDefault.getSelection();
            }
        });
        useAsDefault = true;
        setAsDefault.setText(WizardMessages.project_wizard_makefirstdefaultproject);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setupData() {
        internalParameters = new LinkedList<InternalParameter>();
        if (portfolioText != null) {
            if (selectedObject instanceof PortfolioWrapper) {
                portfolioWrapper = (PortfolioWrapper) selectedObject;
                portfolioText.setText(portfolioWrapper.getLabel());
            } else {
                portfolioWrapper = UIPlugin.getRootPortfolioWrapper();
                portfolioText.setText(portfolioWrapper.getLabel());
            }
        }
        if ((provideProjectPattern != null) && (provideProjectPattern.getPattern() != null) && (patternLabel != null)) {
            patternLabel.setText(WizardMessages.project_wizard_chosen_pattern_prefix + " " + provideProjectPattern.getPattern().getLabel());
            Xelement context = RootWrapper.getInstance().getElement();
            for (ParameterGroup parameterGroup : provideProjectPattern.getPattern().getParameterGroups()) {
                for (Parameter parameter : parameterGroup.getParameters()) {
                    InternalParameter internalParameter = new InternalParameter(parameterGroup, parameter, context);
                    internalParameters.add(internalParameter);
                }
            }
            Collections.sort((java.util.List) internalParameters, new Comparator<InternalParameter>() {

                public int compare(InternalParameter a1, InternalParameter a2) {
                    if (a1.getParameterGroup().getRank() == a2.getParameterGroup().getRank()) {
                        if (a1.getRank() >= a2.getRank()) {
                            return 1;
                        }
                        return 0;
                    } else if (a1.getParameterGroup().getRank() > a2.getParameterGroup().getRank()) {
                        return 1;
                    } else if (a1.getParameterGroup().getRank() < a2.getParameterGroup().getRank()) {
                        return 0;
                    }
                    return 0;
                }
            });
        }
        checkData();
    }

    @Override
    public void refresh() {
        if ((provideProjectPattern != null) && (provideProjectPattern.getPattern() != null) && (parametersTableWidget != null)) {
            super.refresh();
            parametersTableWidget.refresh();
            patternLabel.setText(WizardMessages.project_wizard_chosen_pattern_prefix + " " + provideProjectPattern.getPattern().getLabel());
            setVisible();
            checkData();
        }
    }

    @Override
    public void checkData() {
        if ((this.getErrorMessage() != null) && (this.getErrorMessage().length() > 0)) {
            this.setErrorMessage(null);
        }
        if (portfolioWrapper == null) {
            this.setErrorMessage(WizardMessages.error_no_portfolio_name);
            setPageComplete(false);
            return;
        }
        if (provideProjectPattern.getPattern() == null) {
            this.setErrorMessage(WizardMessages.project_wizard_nopattern);
            setPageComplete(false);
            return;
        }
        if (getPreviousPage() instanceof ProjectPatternSelectionWizardPage) {
            ProjectPatternSelectionWizardPage projectPatternSelectionWizardPage = (ProjectPatternSelectionWizardPage) getPreviousPage();
            if (getContainer().getCurrentPage() == this) {
                projectPatternSelectionWizardPage.setPageComplete(true);
            }
        }
        if (getNextPage() instanceof TaskPreviewWizardPage) {
            ((TaskPreviewWizardPage) getNextPage()).setupData();
        }
        setPageComplete(true);
    }

    /**
     * Setting up the wizard page for Abbot
     */
    private void setupTestHarness() {
        TestHarness.name(parametersTableWidget, TestHarness.NEW_PROJECT_WIZARD_PARAMETER_TABLE_WIDGET);
        TestHarness.name(portfolioButton, TestHarness.NEWPROJECT_PORTFOLIO_BUTTON);
        TestHarness.name(multipleCheckBox, TestHarness.MULTIPLE_ELEMENT_BUTTON);
        TestHarness.name(openEditorButton, TestHarness.OPEN_EDITOR_BUTTON);
    }

    @Override
    public boolean save() {
        separator = separatorCombo.getText();
        ArrayList<Artifact> externalArtifacts = new ArrayList<Artifact>();
        createdWrapper = ElementCreationFactory.createMulitplePatterns(this.getNumberOfElementsToCreate(), separator, provideProjectPattern.getPattern(), portfolioWrapper, getParameters(), openEditorButton.getSelection(), externalArtifacts);
        if (useAsDefault()) {
            IManagePerspective managePerspective = PerspectiveFactory.getCurrentProjectManagerPerspective();
            managePerspective.setCurrentElementWrapper(createdWrapper);
            for (IWorkbenchPage workbenchPage : WorkbenchUtil.getActiveWorkbenchWindow().getPages()) {
                for (IViewReference viewReference : workbenchPage.getViewReferences()) {
                    IWorkbenchPart workbenchPart = viewReference.getPart(false);
                    if (workbenchPart != null) {
                        if (workbenchPart instanceof IListenToDataSourceChange) {
                            IListenToDataSourceChange listenToDataSourceChange = (IListenToDataSourceChange) workbenchPart;
                            listenToDataSourceChange.inputHasChanged(createdWrapper);
                        }
                    }
                }
            }
        }
        ChangeEventFactory.saveChanges();
        ChangeEventFactory.stopChangeRecording();
        return true;
    }

    private void createSeparatorCombo(Composite container, String checkBoxText) {
        Composite separatorComposite = new Composite(container, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.numColumns = 2;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        separatorComposite.setLayout(gridLayout);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.BEGINNING;
        data.horizontalSpan = 2;
        separatorComposite.setLayoutData(data);
        multipleCheckBox = new Button(separatorComposite, SWT.CHECK);
        multipleCheckBox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                separatorCombo.setEnabled(multipleCheckBox.getSelection());
                updateElementCountLabel();
            }
        });
        multipleCheckBox.setText(checkBoxText);
        separatorCombo = new Combo(separatorComposite, SWT.NONE);
        separatorCombo.add(",");
        separatorCombo.add(";");
        separatorCombo.select(0);
        separatorCombo.setEnabled(false);
        data = new GridData();
        data.widthHint = UIConstants.separator_combo_width;
        separatorCombo.setLayoutData(data);
    }

    public int getNumberOfElementsToCreate() {
        int max = 1;
        if (!multipleCheckBox.isDisposed() && !multipleCheckBox.getSelection()) {
            return max;
        }
        for (InternalParameter internalParameter : internalParameters) {
            if (separator == null) {
                separator = separatorCombo.getText();
            }
            StringTokenizer stringTokenizer = new StringTokenizer(internalParameter.getValueAsString(), separator);
            int numberOfTokens = stringTokenizer.countTokens();
            if (numberOfTokens > max) {
                max = numberOfTokens;
            }
        }
        return max;
    }

    private void updateElementCountLabel() {
        noOfElementsToBeCreatedLabel.setText(DialogMessages.pattern_instantiation_no_of_elements_label + " " + getNumberOfElementsToCreate());
        if (getNumberOfElementsToCreate() > 1) {
            setAsDefault.setText(WizardMessages.project_wizard_makefirstdefaultproject);
        } else {
            setAsDefault.setText(WizardMessages.project_wizard_makedefaultproject);
        }
        container.layout(true, true);
    }

    public Object[] getContentsByType() {
        return null;
    }

    public String getType() {
        return null;
    }

    public boolean useAsDefault() {
        return useAsDefault;
    }

    public Object[] getElements() {
        return internalParameters.toArray();
    }

    public InternalParameter[] getParameters() {
        return internalParameters.toArray(new InternalParameter[] {});
    }
}
