package com.holzer.fusedoc.dialogs.widgets;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import com.holzer.fusedoc.dialogs.AbstractElementWizard;
import com.holzer.fusedoc.dialogs.PropertyWizard;
import com.holzer.fusedoc.elements.Property;

public class PropertyElementDisplay extends AbstractElementDisplay {

    /**
	 * @see AbstractElementDisplay
	 * @param parent
	 */
    public PropertyElementDisplay(Composite parent) {
        super(parent);
        int actualWidth = 0;
        for (int i = 0; i < this.getTableViewer().getTable().getColumnCount(); i++) actualWidth += this.getTableViewer().getTable().getColumn(i).getWidth();
    }

    @Override
    protected LabelProvider getDefaultLabelProvider() {
        return new PropertyElementLabelProvider();
    }

    @Override
    protected AbstractElementWizard getElementWizard(Object selectedElement) {
        return new PropertyWizard(this.getShell(), (Property) selectedElement);
    }

    @Override
    protected String[] getColumnHeaders() {
        return new String[] { "Name", "Value" };
    }
}

class PropertyElementLabelProvider extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        Property record = (Property) element;
        switch(columnIndex) {
            case 0:
                return record.getName();
            case 1:
                return record.getValue();
            default:
                return "";
        }
    }
}
