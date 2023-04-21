package org.plazmaforge.studio.reportdesigner.dialogs;

import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.model.data.Entry;
import org.plazmaforge.studio.reportdesigner.model.data.Variable;
import org.plazmaforge.studio.reportdesigner.model.dataset.DatasetRun;
import org.plazmaforge.studio.reportdesigner.model.dataset.ElementDataset;
import org.plazmaforge.studio.reportdesigner.model.dataset.IElementDatasetHolder;
import org.plazmaforge.studio.reportdesigner.util.ReportUtils;

public class ElementDatasetProvider extends AbstractPanelProvider {

    private Combo resetTypeField;

    private Combo incrementTypeField;

    private Combo resetGroupField;

    private Combo incrementGroupField;

    private ExpressionArea incrementWhenExpressionField;

    private DatasetRunPanelProvider datasetRunPanelProvider;

    private List<Entry> resetTypes;

    private boolean supportEnabledMode = true;

    public ElementDatasetProvider() {
        super();
    }

    public Composite createPanel(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        panel.setLayout(layout);
        GridData data = null;
        resetTypeField = new Combo(panel, SWT.READ_ONLY);
        data = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        data.widthHint = 100;
        resetTypeField.setLayoutData(data);
        resetGroupField = new Combo(panel, SWT.READ_ONLY);
        data = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        data.widthHint = 100;
        resetGroupField.setLayoutData(data);
        Label incrementTypeLabel = new Label(panel, SWT.WRAP);
        incrementTypeLabel.setText(ReportDesignerResources.Increment_Type);
        Label incrementGroupLabel = new Label(panel, SWT.WRAP);
        incrementGroupLabel.setText(ReportDesignerResources.Increment_Group);
        incrementTypeField = new Combo(panel, SWT.READ_ONLY);
        data = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        data.widthHint = 100;
        incrementTypeField.setLayoutData(data);
        incrementGroupField = new Combo(panel, SWT.READ_ONLY);
        data = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        data.widthHint = 100;
        incrementGroupField.setLayoutData(data);
        Label expressionLabel = new Label(panel, SWT.WRAP);
        expressionLabel.setText(ReportDesignerResources.Increment_when_expression);
        incrementWhenExpressionField = new ExpressionArea(panel, SWT.BORDER);
        data = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        data.heightHint = 100;
        data.widthHint = 300;
        incrementWhenExpressionField.setLayoutData(data);
        datasetRunPanelProvider = new DatasetRunPanelProvider(this);
        datasetRunPanelProvider.setDesignElement(getDesignElement());
        datasetRunPanelProvider.setDatasetRun(getDatasetRun());
        datasetRunPanelProvider.setReportEditor(getReportEditor());
        datasetRunPanelProvider.setForceChange(isForceChange());
        Composite group = datasetRunPanelProvider.createPanel(panel);
        data = new GridData(SWT.LEFT, SWT.FILL, false, true, 2, 1);
        data.widthHint = 300;
        group.setLayoutData(data);
        initControls();
        initListeners();
        return panel;
    }

    private void enableGroupControls(boolean enabled) {
        enableResetGroupControl(enabled);
        enableIncrementGroupControl(enabled);
    }

    private void enableResetGroupControl() {
        enableResetGroupControl(true);
    }

    private void enableResetGroupControl(boolean enabled) {
        if (!enabled) {
            resetGroupField.setEnabled(false);
            return;
        }
        byte key = getByteEntryKey(resetTypeField, resetTypes);
        resetGroupField.setEnabled(key == Variable.RESET_TYPE_GROUP);
        if (isLoad && !isEmptyGroups() && resetGroupField.isEnabled() && resetGroupField.getSelectionIndex() < 0) {
            resetGroupField.select(0);
            if (isForceChange()) {
                fireSelectionListener(resetGroupField);
            }
        }
    }

    private void enableIncrementGroupControl() {
        enableIncrementGroupControl(true);
    }

    private void enableIncrementGroupControl(boolean enabled) {
        if (!enabled) {
            incrementGroupField.setEnabled(false);
            return;
        }
        byte key = getByteEntryKey(incrementTypeField, resetTypes);
        incrementGroupField.setEnabled(key == Variable.RESET_TYPE_GROUP);
        if (isLoad && !isEmptyGroups() && incrementGroupField.isEnabled() && incrementGroupField.getSelectionIndex() < 0) {
            incrementGroupField.select(0);
            if (isForceChange()) {
                fireSelectionListener(incrementGroupField);
            }
        }
    }

    private void initControls() {
        resetTypes = ReportUtils.getVariableResetTypes();
        populateEntries(resetTypeField, resetTypes);
        populateEntries(incrementTypeField, resetTypes);
    }

    private void initGroupFields() {
        groups = getReport().getGroups();
        populateGroups(resetGroupField, groups);
        populateGroups(incrementGroupField, groups);
    }

    public void loadData() {
        ElementDataset elementDataset = getElementDataset();
        if (elementDataset == null) {
            return;
        }
        isLoad = false;
        initGroupFields();
        setEntryKey(resetTypeField, resetTypes, elementDataset.getResetType());
        setEntryKey(incrementTypeField, resetTypes, elementDataset.getIncrementType());
        setGroup(resetGroupField, groups, elementDataset.getResetGroup());
        setGroup(incrementGroupField, groups, elementDataset.getIncrementGroup());
        setText(incrementWhenExpressionField, elementDataset.getIncrementWhenExpressionText());
        if (datasetRunPanelProvider != null) {
            datasetRunPanelProvider.loadData();
        }
        updateState();
        isLoad = true;
    }

    private boolean isUseDataset() {
        return datasetRunPanelProvider == null ? false : datasetRunPanelProvider.isUseDataset();
    }

    public void storeData() {
        ElementDataset elementDataset = getElementDataset();
        if (elementDataset == null) {
            return;
        }
        elementDataset.setResetType(getByteEntryKey(resetTypeField, resetTypes));
        elementDataset.setIncrementType(getByteEntryKey(incrementTypeField, resetTypes));
        elementDataset.setResetGroup(getGroup(resetGroupField, groups));
        elementDataset.setIncrementGroup(getGroup(incrementGroupField, groups));
        elementDataset.setIncrementWhenExpressionText(getText(incrementWhenExpressionField));
        if (datasetRunPanelProvider != null) {
            datasetRunPanelProvider.storeData();
        }
    }

    protected void setEnabled(boolean enabled) {
        resetTypeField.setEnabled(enabled);
        incrementTypeField.setEnabled(enabled);
        incrementWhenExpressionField.setEnabled(enabled);
        enableGroupControls(enabled);
    }

    public void updateState() {
        setEnabled(isUseDataset());
    }

    protected IElementDatasetHolder getElementDatasetHolder() {
        return (IElementDatasetHolder) getDesignElement(IElementDatasetHolder.class);
    }

    protected ElementDataset getElementDataset() {
        IElementDatasetHolder holder = getElementDatasetHolder();
        if (holder == null) {
            return null;
        }
        return holder.getElementDataset();
    }

    protected DatasetRun getDatasetRun() {
        ElementDataset dataset = getElementDataset();
        return dataset == null ? null : dataset.getDatasetRun();
    }

    protected void initListeners() {
        super.initListeners();
        resetTypeField.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                enableResetGroupControl();
            }
        });
        incrementTypeField.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                enableIncrementGroupControl();
            }
        });
    }

    protected void initChangeListeners() {
        addChangeListeners(resetTypeField, ElementDataset.PROPERTY_RESET_TYPE);
        addChangeListeners(incrementTypeField, ElementDataset.PROPERTY_INCREMENT_TYPE);
        addChangeListeners(resetGroupField, ElementDataset.PROPERTY_RESET_GROUP);
        addChangeListeners(incrementGroupField, ElementDataset.PROPERTY_INCREMENT_GROUP);
        addChangeListeners(incrementWhenExpressionField, ElementDataset.PROPERTY_INCREMENT_WHEN_EXPRESSION_TEXT);
    }

    protected Object getControlValue(Object control) {
        if (control == resetTypeField) {
            return getByteEntryKey(resetTypeField, resetTypes);
        } else if (control == incrementTypeField) {
            return getByteEntryKey(incrementTypeField, resetTypes);
        } else if (control == resetGroupField) {
            return getGroup(resetGroupField, groups);
        } else if (control == incrementGroupField) {
            return getGroup(incrementGroupField, groups);
        } else if (control == incrementWhenExpressionField) {
            return getText(incrementWhenExpressionField);
        }
        return null;
    }

    protected void setPropertyValue(String propertyName, Object value) {
        setPropertyValue(getElementDataset(), propertyName, value);
    }

    /**
     * Return property value form Element
     * @param propertyName
     * @return
     */
    protected Object getPropertyValue(String propertyName) {
        return getPropertyValue(getElementDataset(), propertyName);
    }

    public boolean isSupportEnabledMode() {
        return supportEnabledMode;
    }
}
