package net.jonbuck.tassoo.ui.dialog;

import java.util.ArrayList;
import java.util.List;
import net.jonbuck.tassoo.ui.view.TasksView;
import net.jonbuck.tassoo.ui.view.model.ColumnData;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * <p>
 * <b>Allows the user to choose which columns are visible and which columns are
 * hidden.</b>
 * </p>
 * 
 * @since 1.0.0
 */
public class TaskPreferencesDialog extends TitleAreaDialog {

    private static int DEFAULTS_BUTTON_ID = 25;

    private ArrayList<ColumnData> hidden;

    /**
	 * 
	 */
    IStructuredContentProvider hiddenColumnContentProvider = new IStructuredContentProvider() {

        public void dispose() {
        }

        public Object[] getElements(Object inputElement) {
            return hidden.toArray();
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    };

    private ListViewer hiddenViewer;

    private TasksView tasksView;

    private ArrayList<ColumnData> visible;

    /**
	 * 
	 */
    IStructuredContentProvider visibleColumnContentProvider = new IStructuredContentProvider() {

        public void dispose() {
        }

        public Object[] getElements(Object inputElement) {
            return visible.toArray();
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    };

    private ListViewer visibleViewer;

    /**
	 * 
	 * @param parentShell
	 */
    public TaskPreferencesDialog(TasksView view) {
        super(view.getSite().getShell());
        this.tasksView = view;
        this.visible = createVisibleList(tasksView.getColumnData());
        this.hidden = creatHiddenList(tasksView.getColumnData());
    }

    /**
	 * 
	 */
    protected void buttonPressed(int buttonId) {
        if (buttonId == DEFAULTS_BUTTON_ID) {
            performDefaults();
        }
        super.buttonPressed(buttonId);
    }

    /**
	 * Return a label provider for fields.
	 * 
	 * @return LabelProvider
	 */
    private LabelProvider columnDataLabelProvider() {
        return new LabelProvider() {

            public String getText(Object element) {
                return ((ColumnData) element).getName();
            }
        };
    }

    /**
	 * 
	 */
    public void create() {
        super.create();
        setTitle("Column Preferences");
        setMessage("Choose which columns or visible and hidden");
    }

    /**
	 * 
	 */
    protected void createButtonsForButtonBar(Composite parent) {
        parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createButton(parent, DEFAULTS_BUTTON_ID, JFaceResources.getString("defaults"), false);
        Label l = new Label(parent, SWT.NONE);
        l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        l = new Label(parent, SWT.NONE);
        l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = (GridLayout) parent.getLayout();
        layout.numColumns += 2;
        layout.makeColumnsEqualWidth = false;
        super.createButtonsForButtonBar(parent);
    }

    /**
	 * <p>
	 * <b>Creates the content of the dialog.</b>
	 * </p>
	 */
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        initializeDialogUnits(container);
        Composite columnComposite = new Composite(container, SWT.NONE);
        GridLayout gl_columnComposite = new GridLayout(3, false);
        gl_columnComposite.marginTop = 10;
        gl_columnComposite.marginRight = 10;
        gl_columnComposite.marginLeft = 10;
        gl_columnComposite.marginBottom = 10;
        columnComposite.setLayout(gl_columnComposite);
        columnComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        Label lblShow = new Label(columnComposite, SWT.NONE);
        lblShow.setText("Visible Columns:");
        new Label(columnComposite, SWT.NONE);
        Label lblHide = new Label(columnComposite, SWT.NONE);
        lblHide.setText("Hidden Columns:");
        visibleViewer = new ListViewer(columnComposite, SWT.BORDER | SWT.MULTI);
        visibleViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        visibleViewer.setContentProvider(visibleColumnContentProvider);
        visibleViewer.setLabelProvider(columnDataLabelProvider());
        visibleViewer.setComparator(new ViewerComparator());
        visibleViewer.setInput(this);
        Composite buttonComposite = new Composite(columnComposite, SWT.NONE);
        buttonComposite.setLayout(new GridLayout(1, false));
        Button btnHideColumn = new Button(buttonComposite, SWT.NONE);
        btnHideColumn.addSelectionListener(new SelectionAdapter() {

            @SuppressWarnings("unchecked")
            public void widgetSelected(SelectionEvent e) {
                List<ColumnData> selection = ((IStructuredSelection) visibleViewer.getSelection()).toList();
                addToHidden(selection);
                removeFromVisible(selection);
                visibleViewer.refresh();
                hiddenViewer.refresh();
            }
        });
        GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_btnNewButton.widthHint = 50;
        btnHideColumn.setLayoutData(gd_btnNewButton);
        btnHideColumn.setText(">>");
        Button btnShowColumn = new Button(buttonComposite, SWT.NONE);
        btnShowColumn.addSelectionListener(new SelectionAdapter() {

            @SuppressWarnings("unchecked")
            public void widgetSelected(SelectionEvent e) {
                List<ColumnData> selection = ((IStructuredSelection) hiddenViewer.getSelection()).toList();
                addToVisible(selection);
                removeFromHidden(selection);
                visibleViewer.refresh();
                hiddenViewer.refresh();
            }
        });
        GridData gd_button = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_button.widthHint = 50;
        btnShowColumn.setLayoutData(gd_button);
        btnShowColumn.setText("<<");
        hiddenViewer = new ListViewer(columnComposite, SWT.BORDER | SWT.MULTI);
        hiddenViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        hiddenViewer.setContentProvider(hiddenColumnContentProvider);
        hiddenViewer.setLabelProvider(columnDataLabelProvider());
        hiddenViewer.setComparator(new ViewerComparator());
        hiddenViewer.setInput(this);
        Dialog.applyDialogFont(columnComposite);
        Label titleBarSeparator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
        titleBarSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        return container;
    }

    /**
	 * 
	 * @param selection
	 */
    private void removeFromHidden(List<ColumnData> selection) {
        for (ColumnData columnToBeRemoved : selection) {
            hidden.remove(columnToBeRemoved);
        }
    }

    /**
	 * 
	 * @param selection
	 */
    protected void addToVisible(List<ColumnData> selection) {
        for (ColumnData columnToBeAdded : selection) {
            columnToBeAdded.setVisible(true);
            visible.add(columnToBeAdded);
        }
    }

    /**
	 * 
	 * @param selection
	 */
    protected void removeFromVisible(List<ColumnData> selection) {
        for (ColumnData columnToBeRemoved : selection) {
            visible.remove(columnToBeRemoved);
        }
    }

    /**
	 * 
	 * @param selection
	 */
    private void addToHidden(List<ColumnData> selection) {
        for (ColumnData columnToBeAdded : selection) {
            columnToBeAdded.setVisible(false);
            hidden.add(columnToBeAdded);
        }
    }

    /**
	 * 
	 * @param columnData
	 * @return
	 */
    private ArrayList<ColumnData> createVisibleList(ColumnData[] columnData) {
        ArrayList<ColumnData> visibleItems = new ArrayList<ColumnData>();
        for (ColumnData currentColumn : columnData) {
            if (currentColumn.isVisible()) {
                visibleItems.add(currentColumn);
            }
        }
        return visibleItems;
    }

    /**
	 * 
	 * @param columnData
	 * @return
	 */
    private ArrayList<ColumnData> creatHiddenList(ColumnData[] columnData) {
        ArrayList<ColumnData> visibleItems = new ArrayList<ColumnData>();
        for (ColumnData currentColumn : columnData) {
            if (!currentColumn.isVisible()) {
                visibleItems.add(currentColumn);
            }
        }
        return visibleItems;
    }

    /**
	 * 
	 */
    protected void okPressed() {
        if (hidden.size() > 0) {
            tasksView.updateColumns(combineVisibleAndHidden());
        }
        super.okPressed();
    }

    /**
	 * 
	 * @return
	 */
    private ArrayList<ColumnData> combineVisibleAndHidden() {
        ArrayList<ColumnData> combinedColumns = new ArrayList<ColumnData>();
        combinedColumns.addAll(hidden);
        combinedColumns.addAll(visible);
        return combinedColumns;
    }

    /**
	 * 
	 */
    protected void performDefaults() {
        visible.clear();
        hidden.clear();
        visibleViewer.refresh();
        hiddenViewer.refresh();
    }
}
