package org.dengues.designer.ui.query.sections;

import org.dengues.designer.ui.query.AbstractFormPage;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2007-12-8 qiang.zhang $
 * 
 */
public class SortsSection extends TableSectionPart {

    /**
     * Qiang.Zhang.Adolf@gmail.com ColumnsSection constructor comment.
     * 
     * @param parent
     * @param toolkit
     * @param style
     */
    public SortsSection(AbstractFormPage page, Composite parent) {
        super(page, parent, new String[] { "Add computed column", "Edit computed column", "Remove", "Up", "Down" });
        getSection().setText("Sort");
        getSection().setDescription("Specify the order of fields sorted by.");
    }

    @Override
    protected void buttonSelected(Button button, int index) {
        handleButtonSelected(index, TYPE_SORTS);
    }

    @Override
    public void createTableColumns(TableViewer viewerPart) {
        Table table = viewerPart.getTable();
        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setText("Order by");
        column.pack();
        column = new TableColumn(table, SWT.NONE);
        column.setText("Operator");
        column.pack();
        column = new TableColumn(table, SWT.NONE);
        column.setText("Expression");
        column.pack();
        ViewerPartProvider provider = new ViewerPartProvider(TYPE_SORTS);
        viewerPart.setContentProvider(provider);
        viewerPart.setLabelProvider(provider);
        viewerPart.setInput(dbDiagram);
        handleCCombo(viewerPart, table, 1, TYPE_SORTS);
    }
}
