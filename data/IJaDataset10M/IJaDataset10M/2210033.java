package eu.redseeds.sc.current.ui.wizards;

import java.util.List;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import eu.redseeds.scl.model.rsl.rsldomainelements.phrases.NounLinkDTO;

public class RenameWizardPageResolveProblems extends WizardPage {

    private static Rectangle tableRect = new Rectangle(20, 205, 540, 600);

    private static String[] columnNames = { "original non-basic form", "new non-basic form" };

    private Composite container;

    private Table table;

    private TableViewer formsViewer;

    private NonBasicFormsContentProvider formsViewerContentProvider;

    protected ISelection selection;

    protected RenameWizardPageResolveProblems(ISelection selection) {
        super(ResourceMessages.Rename_windowTitle);
        this.selection = selection;
        setTitle(ResourceMessages.Rename_title);
        setDescription(ResourceMessages.Rename_forms_description);
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 1;
        setControl(container);
        setPageComplete(true);
        Object input = getSelectedObject(selection);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        table = new Table(container, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setBounds(tableRect);
        table.setLayoutData(gd);
        TableColumn column = new TableColumn(table, SWT.LEFT, 0);
        column.setWidth(140);
        column.setText(columnNames[0]);
        column = new TableColumn(table, SWT.LEFT, 1);
        column.setWidth(140);
        column.setText(columnNames[1]);
        formsViewer = new TableViewer(table);
        formsViewer.setUseHashlookup(true);
        formsViewer.setColumnProperties(columnNames);
        CellEditor[] editors = new CellEditor[2];
        editors[0] = null;
        editors[1] = new TextCellEditor(table);
        formsViewer.setCellEditors(editors);
        formsViewer.setCellModifier(new NonBasicFormsCellModifier(input, formsViewer, container));
        formsViewerContentProvider = new NonBasicFormsContentProvider(input, ((RenameWizard) getWizard()).getNewElementName());
        formsViewer.setContentProvider(formsViewerContentProvider);
        formsViewer.setLabelProvider(new NonBasicFormsLabelProvider(input));
        formsViewer.setInput(input);
    }

    @Override
    public Control getControl() {
        return container;
    }

    protected Object getSelectedObject(ISelection selection) {
        if (selection != null) {
            ITreeSelection treeSelection = (ITreeSelection) selection;
            TreePath[] tr = treeSelection.getPaths();
            if (tr[0] != null) {
                Object selectedObj = tr[0].getLastSegment();
                return selectedObj;
            } else return null;
        } else return null;
    }

    protected void refreshViewer() {
        if (formsViewer != null && formsViewerContentProvider != null) {
            formsViewerContentProvider.setDefaultNewValue(((RenameWizard) getWizard()).getNewElementName());
            formsViewer.refresh();
        }
    }

    public List<NounLinkDTO> getNonBasicForms() {
        return formsViewerContentProvider.getLinks();
    }
}
