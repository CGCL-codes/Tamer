package org.butu.gui.filters;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * ��������� ��� ���������� ��������.
 * @author kbakaras
 *
 */
public class BVFilters {

    private Group top;

    private GridData gridData;

    public BVFilters(Composite parent, int style) {
        top = new Group(parent, style);
        top.setLayout(new GridLayout(2, false));
        top.setText("�������");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
    }

    public void setLayoutData(Object layoutData) {
        top.setLayoutData(layoutData);
    }

    public void addFilter(IVFilter filter) {
        filter.createPartControl(top, gridData);
    }

    public Control getControl() {
        return top;
    }
}
