package com.safi.workshop.sheet.actionstep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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
import com.safi.core.actionstep.MoveToRow;
import com.safi.workshop.sheet.DynamicValueEditorUtils;

public class MoveToRowEditorPage extends AbstractActionstepEditorPage {

    private DynamicValueEditorWidget rowNumDVEWidget;

    private Label rowNumLabel;

    private Combo combo;

    private ComboViewer comboViewer;

    private Label resultSetLabel;

    private Text text;

    private Label nameLabel;

    public MoveToRowEditorPage(ActionstepEditorDialog parent) {
        super(parent);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        nameLabel = new Label(this, SWT.NONE);
        nameLabel.setText("Name*:");
        text = new Text(this, SWT.BORDER);
        final GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_text.widthHint = 50;
        text.setLayoutData(gd_text);
        resultSetLabel = new Label(this, SWT.NONE);
        resultSetLabel.setText("Result Set*:");
        comboViewer = new ComboViewer(this, SWT.BORDER);
        combo = comboViewer.getCombo();
        final GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_combo.widthHint = 50;
        combo.setLayoutData(gd_combo);
        final TransactionalEditingDomain editingDomain = parent.getEditPart().getEditingDomain();
        final MoveToRow moveToRow = (MoveToRow) parent.getEditPart().getActionStep();
        IObservableValue ob = ActionstepEditObservables.observeValue(editingDomain, moveToRow, moveToRow.eClass().getEStructuralFeature("name"));
        ISWTObservableValue uiElement = SWTObservables.observeText(text, SWT.FocusOut);
        bindingContext.bindValue(uiElement, ob, null, null);
        List<DBResultSetId> resultSets = new ArrayList<DBResultSetId>();
        for (Iterator<EObject> iter = moveToRow.eResource().getAllContents(); iter.hasNext(); ) {
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
                String id = ((DBResultSetId) element).getName();
                String[] names = id.split(".");
                if (names.length > 0) {
                    return names[names.length - 1];
                } else return id;
            }
        });
        comboViewer.setInput(resultSets);
        ob = ActionstepEditObservables.observeValue(editingDomain, moveToRow, moveToRow.eClass().getEStructuralFeature("resultSet"));
        ISWTObservableValue comboElement = SWTObservables.observeSelection(combo);
        rowNumLabel = new Label(this, SWT.NONE);
        rowNumLabel.setText("Row Num:");
        rowNumDVEWidget = new DynamicValueEditorWidget(this, SWT.NONE);
        rowNumDVEWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboElement = SWTObservables.observeDelayedValue(400, comboElement);
        ResultSetUpdateStrategy rus = new ResultSetUpdateStrategy(resultSets);
        bindingContext.bindValue(comboElement, ob, rus, rus);
        rowNumDVEWidget.setDynamicValue(DynamicValueEditorUtils.copyDynamicValue(moveToRow.getRowNum()));
        rowNumDVEWidget.setEditingDomain(editingDomain);
        rowNumDVEWidget.setObject(moveToRow);
        EStructuralFeature rowNumFeature = moveToRow.eClass().getEStructuralFeature("rowNum");
        rowNumDVEWidget.setFeature(rowNumFeature);
        DynamicValueWidgetObservableValue rowNumVal = new DynamicValueWidgetObservableValue(rowNumDVEWidget, SWT.Modify);
        IObservableValue ob3 = ActionstepEditObservables.observeValue(editingDomain, moveToRow, rowNumFeature);
        bindingContext.bindValue(rowNumVal, ob3, null, null);
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
