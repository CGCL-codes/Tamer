package org.omwg.ui.dialogs;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.omwg.jface.viewers.TreeLabelProvider;
import org.omwg.ontology.Concept;
import org.omwg.ui.views.*;
import org.omwg.ontology.Instance;
import org.omwg.ontology.RelationInstance;
import org.wsmo.common.*;
import java.util.*;
import org.omwg.ontology.Parameter;
import org.omwg.ui.models.TableModel;

/**
 * @author Jan Henke, jan.henke@deri.org
 */
public class SingleSelectionDialog extends SelectionDialog {

    private int columnIndex;

    private TableView tableView;

    private RelationInstance processedRow;

    public SingleSelectionDialog(Shell parentShell, int i, TableView tv, Entity e) {
        super(parentShell);
        columnIndex = i;
        tableView = tv;
        processedRow = (RelationInstance) e;
        setInitialSelections(getInitialElementSelections().toArray());
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        Label label = new Label(composite, SWT.NONE);
        label.setText("Select the instance to serve as parameter value.");
        Table table = new Table(composite, SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_BOTH);
        table.setLayout(new RowLayout());
        gd.heightHint = 250;
        gd.widthHint = 300;
        table.setLayoutData(gd);
        Object data = tableView.getTable().getColumn(columnIndex).getData();
        final Parameter parameter = (Parameter) data;
        Iterator typesIterator = ((Parameter) data).listTypes().iterator();
        Set temporaryInstances = ((Concept) typesIterator.next()).listInstances();
        final Set selectableInstances = temporaryInstances;
        while (typesIterator.hasNext()) {
            Set furtherInstances = ((Concept) typesIterator.next()).listInstances();
            selectableInstances.retainAll(furtherInstances);
        }
        Iterator iterator = selectableInstances.iterator();
        while (iterator.hasNext()) {
            Instance instance = (Instance) iterator.next();
            String text = (new TreeLabelProvider()).getText(instance);
            Button button = new Button(table, SWT.RADIO);
            button.setLayoutData(new RowData(300, 20));
            button.setBackground(new Color(parent.getShell().getDisplay(), new RGB(255, 255, 255)));
            button.setText(text);
            button.setData(instance);
            List sel = getInitialElementSelections();
            if (!sel.isEmpty()) {
                if (sel.get(0) == instance) {
                    button.setSelection(true);
                }
            }
        }
        return composite;
    }

    protected void okPressed() {
        List oldSelections = this.getInitialElementSelections();
        Control[] controls = ((Composite) this.getDialogArea()).getChildren();
        Table table = (Table) controls[1];
        Control[] items = table.getChildren();
        Object data = null;
        for (int i = 0; i < items.length; i++) {
            Button item = (Button) items[i];
            if (item.getSelection()) {
                data = item.getData();
            }
        }
        Object[] result = null;
        result = new Object[] { data };
        if (data != null) {
            final Integer i = new Integer(columnIndex - 1);
            ((TableModel) tableView.getContentProvider()).setValues(processedRow, i, result);
        }
        close();
    }

    protected List getInitialElementSelections() {
        Vector selections = new Vector();
        RelationInstance ri = (RelationInstance) processedRow;
        byte b = (new Integer(columnIndex - 1)).byteValue();
        try {
            selections.addElement(ri.getParameterValue(b));
            return selections;
        } catch (Exception e) {
            return selections;
        }
    }
}
