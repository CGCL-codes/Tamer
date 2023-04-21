package org.pluginbuilder.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class LabelledComposite extends Composite {

    private final Label label;

    private final int margin;

    public LabelledComposite(FormToolkit toolkit, Composite parent, String labelText, int numColumns) {
        this(toolkit, parent, labelText, numColumns, false);
    }

    public LabelledComposite(FormToolkit toolkit, Composite parent, String labelText, int numColumns, boolean makeColumnsEqualWidth) {
        super(parent, SWT.NULL);
        toolkit.adapt(this);
        setAppropriateLayoutData(parent);
        setLayout(new GridLayout(numColumns, makeColumnsEqualWidth));
        label = createLabel(toolkit, labelText, numColumns);
        margin = getMargin(this);
    }

    private void setAppropriateLayoutData(Composite composite) {
        Layout layout = composite.getLayout();
        if (layout instanceof GridLayout) setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false)); else if (layout instanceof TableWrapLayout) setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP));
    }

    private static int getMargin(Composite composite) {
        Layout layout = composite.getLayout();
        if (layout instanceof GridLayout) {
            GridLayout gridLayout = (GridLayout) layout;
            return 2 * gridLayout.marginWidth + gridLayout.marginLeft + gridLayout.marginRight;
        }
        if (layout instanceof TableWrapLayout) {
            TableWrapLayout tableWrapLayout = (TableWrapLayout) layout;
            int scrollBarWidth = 15;
            return tableWrapLayout.leftMargin + tableWrapLayout.rightMargin + scrollBarWidth;
        }
        return 0;
    }

    private Label createLabel(FormToolkit toolkit, String labelText, int numColumns) {
        if (labelText == null || labelText.length() == 0) return null;
        Label label = toolkit.createLabel(this, labelText, SWT.WRAP);
        label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, numColumns, 1));
        return label;
    }

    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
        if (label != null) {
            int labelHint = computeLabelWidthHint(wHint);
            ((GridData) label.getLayoutData()).widthHint = labelHint;
        }
        return super.computeSize(wHint, hHint, changed);
    }

    private int computeLabelWidthHint(int wHint) {
        if (wHint != SWT.DEFAULT) return wHint - margin;
        Composite parent = getParent();
        Rectangle clientArea = parent.getClientArea();
        int parentMargin = getMargin(parent);
        return clientArea.width - parentMargin - margin;
    }
}
