package com.safi.workshop.sheet.actionstep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.safi.core.actionstep.DBResultSetId;
import com.safi.core.actionstep.SetColMapping;
import com.safi.core.actionstep.SetColValues;

public class SetColValuesEditorPage extends AbstractActionstepEditorPage {

    private Combo combo;

    private ComboViewer comboViewer;

    private SetColMappingEditorWidget setColMappingEditorWidget;

    private Label colMappingsLabel;

    private Label resultSetLabel;

    private Text text;

    private Label nameLabel;

    public SetColValuesEditorPage(ActionstepEditorDialog parent) {
        super(parent);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        nameLabel = new Label(this, SWT.NONE);
        nameLabel.setText("Name:");
        text = new Text(this, SWT.BORDER);
        final GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_text.widthHint = 50;
        text.setLayoutData(gd_text);
        resultSetLabel = new Label(this, SWT.NONE);
        resultSetLabel.setText("Result Set:");
        comboViewer = new ComboViewer(this, SWT.BORDER);
        combo = comboViewer.getCombo();
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        SetColValues setColValues = (SetColValues) parent.getEditPart().getActionStep();
        TransactionalEditingDomain editingDomain = parent.getEditPart().getEditingDomain();
        IObservableValue ob = ActionstepEditObservables.observeValue(editingDomain, setColValues, setColValues.eClass().getEStructuralFeature("name"));
        ISWTObservableValue uiElement = SWTObservables.observeText(text, SWT.FocusOut);
        uiElement = SWTObservables.observeDelayedValue(400, uiElement);
        bindingContext.bindValue(uiElement, ob, null, null);
        colMappingsLabel = new Label(this, SWT.NONE);
        final GridData gd_colMappingsLabel = new GridData(SWT.LEFT, SWT.TOP, false, false);
        colMappingsLabel.setLayoutData(gd_colMappingsLabel);
        colMappingsLabel.setText("Col Mappings");
        setColMappingEditorWidget = new SetColMappingEditorWidget(this, SWT.NONE);
        setColMappingEditorWidget.setEditingDomain(parent.getEditPart().getEditingDomain());
        setColMappingEditorWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        IObservableList modelList = ActionstepEditObservables.observeList(editingDomain, setColValues, setColValues.eClass().getEStructuralFeature("columnMappings"));
        IObservableList uiList = new WritableList(new ArrayList<SetColMapping>(setColValues.getColumnMappings()), SetColMapping.class);
        bindingContext.bindList(uiList, modelList, null, null);
        setColMappingEditorWidget.setItemList(uiList);
        setColMappingEditorWidget.setActionstepEditorDialog(parent);
        List<DBResultSetId> resultSets = new ArrayList<DBResultSetId>();
        for (Iterator<EObject> iter = setColValues.eResource().getAllContents(); iter.hasNext(); ) {
            EObject obj = iter.next();
            if (obj instanceof DBResultSetId) resultSets.add((DBResultSetId) obj);
        }
        comboViewer.setContentProvider(new IStructuredContentProvider() {

            @Override
            public void dispose() {
            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return ((List<DBResultSetId>) inputElement).toArray();
            }
        });
        comboViewer.setLabelProvider(new ILabelProvider() {

            @Override
            public void addListener(ILabelProviderListener listener) {
            }

            @Override
            public void dispose() {
            }

            @Override
            public boolean isLabelProperty(Object element, String property) {
                return true;
            }

            @Override
            public void removeListener(ILabelProviderListener listener) {
            }

            @Override
            public Image getImage(Object element) {
                return null;
            }

            @Override
            public String getText(Object element) {
                return ((DBResultSetId) element).getName();
            }
        });
        comboViewer.setInput(resultSets);
        ob = ActionstepEditObservables.observeValue(parent.getEditPart().getEditingDomain(), setColValues, setColValues.eClass().getEStructuralFeature("resultSet"));
        ISWTObservableValue comboElement = SWTObservables.observeSelection(combo);
        comboElement = SWTObservables.observeDelayedValue(400, comboElement);
        ResultSetUpdateStrategy cus = new ResultSetUpdateStrategy(resultSets);
        bindingContext.bindValue(comboElement, ob, cus, cus);
    }

    @Override
    public void operationsComplete() {
        new CaseItemReorderCommand(editPart.getEditingDomain(), editPart).execute();
    }

    @Override
    public void operationsUndone() {
        new CaseItemReorderCommand(editPart.getEditingDomain(), editPart).execute();
    }

    @Override
    public String getTabText() {
        return "Basic";
    }

    @Override
    public boolean validate() {
        IObservableList list = bindingContext.getBindings();
        for (Binding b : (List<Binding>) list) {
            b.validateTargetToModel();
        }
        return true;
    }
}
