package net.sf.elbe.ui.widgets.entryeditor;

import net.sf.elbe.ui.widgets.ViewFormWidget;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class EntryEditorWidget extends ViewFormWidget {

    private EntryEditorWidgetConfiguration configuration;

    private EntryEditorWidgetQuickFilterWidget quickFilterWidget;

    private Tree tree;

    private TreeViewer viewer;

    public EntryEditorWidget(EntryEditorWidgetConfiguration configuration) {
        this.configuration = configuration;
    }

    protected Control createContent(Composite parent) {
        this.quickFilterWidget = new EntryEditorWidgetQuickFilterWidget(this.configuration.getFilter(), this);
        this.quickFilterWidget.createComposite(parent);
        this.tree = new Tree(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 450;
        data.heightHint = 250;
        this.tree.setLayoutData(data);
        this.tree.setHeaderVisible(true);
        this.tree.setLinesVisible(true);
        this.viewer = new TreeViewer(this.tree);
        this.viewer.setUseHashlookup(true);
        for (int i = 0; i < EntryEditorWidgetTableMetadata.COLUM_NAMES.length; i++) {
            TreeColumn column = new TreeColumn(this.tree, SWT.LEFT, i);
            column.setText(EntryEditorWidgetTableMetadata.COLUM_NAMES[i]);
            column.setWidth(200);
            column.setResizable(true);
        }
        this.viewer.setColumnProperties(EntryEditorWidgetTableMetadata.COLUM_NAMES);
        this.tree.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                if (tree.getClientArea().width > 0) {
                    int width = tree.getClientArea().width - 2 * tree.getBorderWidth();
                    if (tree.getVerticalBar().isVisible()) {
                        width -= tree.getVerticalBar().getSize().x;
                    }
                    tree.getColumn(EntryEditorWidgetTableMetadata.VALUE_COLUMN_INDEX).setWidth(width - tree.getColumn(EntryEditorWidgetTableMetadata.KEY_COLUMN_INDEX).getWidth());
                }
            }
        });
        this.configuration.getSorter().connect(this.viewer);
        this.configuration.getFilter().connect(this.viewer);
        this.configuration.getPreferences().connect(this.viewer);
        this.viewer.setContentProvider(configuration.getContentProvider(this));
        this.viewer.setLabelProvider(configuration.getLabelProvider(this.viewer));
        this.viewer.setCellModifier(configuration.getCellModifier(this.viewer));
        CellEditor[] editors = new CellEditor[EntryEditorWidgetTableMetadata.COLUM_NAMES.length];
        this.viewer.setCellEditors(editors);
        return this.tree;
    }

    public void setFocus() {
        this.viewer.getTree().setFocus();
    }

    public void dispose() {
        if (this.viewer != null) {
            this.configuration.dispose();
            this.configuration = null;
            if (this.quickFilterWidget != null) {
                this.quickFilterWidget.dispose();
                this.quickFilterWidget = null;
            }
            this.tree.dispose();
            this.tree = null;
            this.viewer = null;
        }
        super.dispose();
    }

    public TreeViewer getViewer() {
        return viewer;
    }

    public EntryEditorWidgetQuickFilterWidget getQuickFilterWidget() {
        return quickFilterWidget;
    }

    public void setEnabled(boolean enabled) {
        this.tree.setEnabled(enabled);
    }
}
