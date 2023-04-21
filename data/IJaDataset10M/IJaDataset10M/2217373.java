package de.sooja.framework.ui.soojasites;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * @author Simon Oetzel <soetzel@sooja.de>
 * 
 * <h2>SoojaTableViewer</h2>
 * 
 * <p>
 * SoojaTableViewer shows a list of available soojaSites.
 * </p>
 */
public class SoojaTableViewer {

    private Label label;

    private Table table;

    private Device device;

    private TableViewer tableViewer;

    private TaskList taskList = new TaskList();

    private Popup popupInput;

    private String[] columnNames = new String[] { "Title", "Vendor", "Homepage", "Short description" };

    private int NUMBER_COLUMNS = columnNames.length;

    private static int[] columnWidth = new int[] { 120, 200, 200, 200 };

    /**
   * the constructor
   * 
   * @param parent
   */
    public SoojaTableViewer(Composite parent) {
        this.addChildControls(parent);
    }

    /**
   * create the layout for the view, add the table and the buttons to the view
   * and fill the table with tasks
   * 
   * @param composite
   */
    private void addChildControls(Composite composite) {
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.FILL_BOTH);
        composite.setLayoutData(gridData);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);
        createTable(composite);
        createTableViewer();
        tableViewer.setContentProvider(new SoojaContentProvider());
        tableViewer.setLabelProvider(new SoojaLabelProvider());
        tableViewer.setInput(taskList);
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                Task task = (Task) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
                popupInput = new Popup();
                if (!tableViewer.getSelection().isEmpty()) {
                    if (Popup.getInstance().isEnabled()) Popup.getInstance().dispose();
                    popupInput.init(task);
                }
            }
        });
        createButtons(composite);
    }

    /**
   * Create the Table
   * 
   * @param parent
   */
    public void createTable(Composite parent) {
        int style = SWT.SINGLE | SWT.H_SCROLL | SWT.BORDER | SWT.FULL_SELECTION;
        table = new Table(parent, style);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setBackground(new Color(device, 255, 255, 255));
        table.setForeground(new Color(device, 15, 15, 15));
        table.setFont(new Font(device, "Tahoma", 8, 0));
        GridData layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.verticalAlignment = GridData.FILL;
        table.setLayoutData(layoutData);
        TableColumn[] columnList = new TableColumn[NUMBER_COLUMNS];
        TableColumn columnTitle = new TableColumn(table, style);
        TableColumn columnVendor = new TableColumn(table, style);
        TableColumn columnHp = new TableColumn(table, style);
        TableColumn columnShort = new TableColumn(table, style);
        columnList[0] = columnTitle;
        columnList[1] = columnVendor;
        columnList[2] = columnHp;
        columnList[3] = columnShort;
        for (int i = 0; i < NUMBER_COLUMNS; i++) {
            columnList[i].setText(columnNames[i]);
            columnList[i].setWidth(columnWidth[i]);
        }
    }

    /**
   * Create the TableViewer
   */
    private void createTableViewer() {
        tableViewer = new TableViewer(table);
        tableViewer.setUseHashlookup(true);
        tableViewer.setColumnProperties(columnNames);
    }

    /**
   * Close the window and dispose of resources
   */
    public void close() {
        Composite parent = table.getParent();
        if (parent != null && !parent.isDisposed()) parent.dispose();
    }

    /**
   * InnerClass that acts as a proxy for the TaskList providing content for the
   * Table. It implements the ITaskListViewer interface since it must register
   * changeListeners with the TaskList
   */
    class SoojaContentProvider implements IStructuredContentProvider, ITaskListViewer {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            if (newInput != null) ((TaskList) newInput).addChangeListener(this);
            if (oldInput != null) ((TaskList) oldInput).removeChangeListener(this);
        }

        public void dispose() {
            taskList.removeChangeListener(this);
        }

        public Object[] getElements(Object parent) {
            return taskList.getTasks().toArray();
        }

        public void addTask(Task task) {
            tableViewer.add(task);
        }

        public void removeTask(Task task) {
            tableViewer.remove(task);
        }

        public void updateTask(Task task) {
            tableViewer.update(task, columnNames);
        }
    }

    /**
   * Add the buttons
   * 
   * @param parent
   *          the parent composite
   */
    private void createButtons(Composite parent) {
        int style = SWT.PUSH;
        Composite c = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 4;
        c.setLayout(layout);
        Button installButton = new Button(c, style);
        installButton.setText("install");
        installButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Task task = (Task) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
                if (task != null) {
                    System.out.println("[ install -> " + task.getTitle() + " ]");
                }
            }
        });
        Button updateButton = new Button(c, style);
        updateButton.setText("refresh");
        updateButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                tableViewer.getContentProvider().dispose();
                taskList = new TaskList();
                tableViewer.setContentProvider(new SoojaContentProvider());
            }
        });
        Button detailsButton = new Button(c, style);
        detailsButton.setText("details");
        detailsButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Task task = (Task) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
                popupInput = new Popup();
                if (!tableViewer.getSelection().isEmpty()) {
                    if (Popup.getInstance().isEnabled()) Popup.getInstance().dispose();
                    popupInput.init(task);
                }
            }
        });
        createTimeStamp(c);
    }

    /**
   * creates a label for the timeStamp
   *  
   * @param composite
   */
    private void createTimeStamp(Composite composite) {
        Task task = (Task) getTaskList().getFirstElement();
        long timeStamp = Long.parseLong(task.getTimeStamp());
        Date myDate = new Date(timeStamp);
        SimpleDateFormat sdfOutput = new SimpleDateFormat("HH.mm - dd.MM.yyyy");
        String date = sdfOutput.format(myDate);
        label = new Label(composite, SWT.NONE);
        label.setFont(new Font(device, "Tahoma", 7, 0));
        label.setText("last update: " + date);
    }

    /**
   * Return the column names in a String-Array
   * 
   * @return columnNames - String-Array that contains the column names
   */
    public String[] getColumnNames() {
        return columnNames;
    }

    /**
   * returns the currently selected Item
   * 
   * @return ISelection - currently selected item
   */
    public ISelection getSelection() {
        return tableViewer.getSelection();
    }

    /**
   * Return the TaskList
   * 
   * @return TaskList - the TaskList
   */
    public TaskList getTaskList() {
        return taskList;
    }

    /**
   * Return the parent composite
   * 
   * @return Control - the parent composite
   */
    public Control getControl() {
        return table.getParent();
    }
}
