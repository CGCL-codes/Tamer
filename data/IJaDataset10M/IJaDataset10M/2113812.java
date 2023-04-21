package com.byterefinery.rmbench.dialogs;

import java.util.List;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import com.byterefinery.rmbench.model.schema.Column;
import com.byterefinery.rmbench.model.schema.UniqueConstraint;

/**
 * wizard page for specifying a unique constraint, aka candidate key
 * 
 * @author sell
 */
public class UniqueConstraintPage extends WizardPage implements TableConstraintWizard.ConstraintPage {

    public static final String PAGE_NAME = UniqueConstraint.CONSTRAINT_TYPE;

    private final List<Column> tableColumns;

    private CheckboxTableHandler viewerHandler;

    private CheckboxTableViewer columnsViewer;

    protected UniqueConstraintPage(List<Column> columns) {
        this(PAGE_NAME, Messages.ConstraintWizard_UniqueTitle, Messages.ConstraintWizard_UniqueDescription, columns);
    }

    protected UniqueConstraintPage(String pageName, String title, String description, List<Column> columns) {
        super(pageName);
        setTitle(title);
        setDescription(description);
        this.tableColumns = columns;
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 30;
        layout.marginHeight = 30;
        composite.setLayout(layout);
        viewerHandler = new CheckboxTableHandler(composite);
        columnsViewer = CheckboxTableViewer.newCheckList(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.minimumWidth = convertWidthInCharsToPixels(30);
        gd.minimumHeight = convertHeightInCharsToPixels(5);
        columnsViewer.getTable().setLayoutData(gd);
        columnsViewer.setLabelProvider(new LabelProvider() {

            public String getText(Object element) {
                return ((Column) element).getName();
            }
        });
        viewerHandler.setViewer(columnsViewer, tableColumns);
        viewerHandler.addListener(new CheckboxTableHandler.Listener() {

            public void checkMoved(Column column, int oldIndex, int newIndex) {
            }

            public void checkCountChanged() {
                setPageComplete(viewerHandler.hasChecked());
            }
        });
        setControl(composite);
        setPageComplete(false);
    }

    public Column[] getColumns() {
        return viewerHandler.getCheckedColumns();
    }

    public IWizardPage getNextPage() {
        return null;
    }

    public Object getConstraintArg() {
        return getColumns();
    }
}
