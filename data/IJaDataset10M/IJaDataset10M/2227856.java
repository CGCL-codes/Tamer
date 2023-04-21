package de.tud.eclipse.plugins.controlflow.view.details;

import java.util.Set;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import de.tud.eclipse.plugins.controlflow.model.ICFlowNode;
import de.tud.eclipse.plugins.controlflow.util.BorderLayout;

public class DetailsWindow extends Window {

    private ICFlowNode model;

    private Table table;

    protected static Rectangle dimensions = null;

    public DetailsWindow(ICFlowNode model) {
        super((Shell) null);
        this.model = model;
        this.create();
        this.getShell().setText("Details of " + model.getLabel());
        if (dimensions == null) this.getShell().setSize(500, 350); else loadDimensions();
        this.open();
    }

    protected Control createContents(Composite parent) {
        BorderLayout layout = new BorderLayout();
        parent.setLayout(layout);
        Text label = new Text(parent, SWT.SINGLE);
        label.setLayoutData(BorderLayout.NORTH);
        label.setText("Name: " + model.getLabel());
        label.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        label.setEditable(false);
        Text metaText = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        metaText.setLayoutData(BorderLayout.CENTER);
        metaText.setText(model.getMetaText());
        metaText.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        metaText.setEditable(false);
        createAttributesTable(parent, BorderLayout.SOUTH);
        new ConnectionDetailsTree(parent, BorderLayout.EAST, model);
        return parent;
    }

    private void createAttributesTable(Composite parent, Object layoutData) {
        table = new Table(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setLayoutData(layoutData);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        String[] titles = { "Attribute Name", "Attribute Value" };
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(titles[i]);
        }
        Set<String> attrs = model.getAllAttributeKeys();
        for (String key : attrs) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, key);
            item.setText(1, model.getValueForAttributeKey(key));
        }
        for (int i = 0; i < titles.length; i++) {
            table.getColumn(i).pack();
        }
    }

    public void storeDimensions() {
        Point pos = this.getShell().getLocation();
        Point sz = this.getShell().getSize();
        dimensions = new Rectangle(pos.x, pos.y, sz.x, sz.y);
    }

    public void loadDimensions() {
        this.getShell().setLocation(dimensions.x, dimensions.y);
        this.getShell().setSize(dimensions.width, dimensions.height);
    }

    public void handleShellCloseEvent() {
        storeDimensions();
        super.handleShellCloseEvent();
    }
}
