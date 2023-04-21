package org.vikamine.app.rcp.view;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.prefs.Preferences;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.vikamine.app.DMManager;
import org.vikamine.app.event.OntologyChangeListener;
import org.vikamine.app.event.OntologyChangedEvent;
import org.vikamine.kernel.Describable;
import org.vikamine.kernel.data.Ontology;
import org.vikamine.kernel.data.RDFStatement;
import org.vikamine.kernel.data.RDFTripleStore;
import org.vikamine.kernel.data.TripleStoreListener;
import org.vikamine.kernel.util.RDFExporter;

/**
 * This View gives an tabular overveiw over the background knowledge that is
 * saved in the TripleStore of the current ontology.
 * 
 * @author beczkowiak, lemmerich
 * @date 02/2012
 */
public class TripleViewer extends ViewPart implements ModifyListener, SelectionListener, TripleStoreListener, OntologyChangeListener {

    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "vikamineplugintripleviewer.views.TripleViewer";

    /** The sub list. */
    ArrayList<RDFStatement> subList = new ArrayList<RDFStatement>();

    /** The list all. */
    ArrayList<RDFStatement> listAll = new ArrayList<RDFStatement>();

    /** The use filecheck. */
    org.eclipse.swt.widgets.Button subjectcheck, objectcheck, predicatecheck, useFilecheck;

    /** The filter textbox. */
    org.eclipse.swt.widgets.Text filterTextbox;

    /** The table viewer. */
    TableViewer tableViewer;

    /** The table. */
    Table table;

    /** The added listener. */
    Boolean addedListener = false;

    /** The Predicate from file. */
    public static String PredicateFromFile = "createdByFile";

    @Override
    public void widgetSelected(SelectionEvent e) {
        filterElements(DMManager.getInstance().getOntology());
    }

    /**
     * filters the statements of the @param onto ontology of the triple store by
     * selected checkboxes.
     * 
     * @param onto
     *            the onto
     */
    public void filterElements(Ontology onto) {
        createDatas(onto);
        String text = filterTextbox.getText().toLowerCase();
        subList.clear();
        Boolean addAll = text.equals("") || (!subjectcheck.getSelection() && !objectcheck.getSelection() && !predicatecheck.getSelection());
        for (int i = 0; i < listAll.size(); i++) {
            Boolean add = addAll;
            if (subjectcheck.getSelection() && !add) {
                String val = listAll.get(i).getSubject().toString().toLowerCase();
                if (val.contains(text)) {
                    add = true;
                }
            }
            if (objectcheck.getSelection() && !add) {
                String val = listAll.get(i).getObject().toString().toLowerCase();
                if (val.contains(text)) {
                    add = true;
                }
            }
            if (predicatecheck.getSelection() && !add) {
                String val = listAll.get(i).getPredicate().toString().toLowerCase();
                if (val.contains(text)) {
                    add = true;
                }
            }
            if (add) {
                subList.add(listAll.get(i));
            }
        }
        tableViewer.refresh();
    }

    /**
     * changing text in filter textbox => need to filter elements again.
     * 
     * @param e
     *            the e
     */
    @Override
    public void modifyText(ModifyEvent e) {
        filterElements(DMManager.getInstance().getOntology());
    }

    /**
     * needed for listener @empty.
     * 
     * @param e
     *            the e
     */
    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    /**
     * fills the subset which is filtered and displayed. If "use current file"
     * is selected scans for current editor
     * 
     * @param ontology
     *            the ontology
     */
    public void createDatas(Ontology ontology) {
        if (ontology != null && !addedListener) {
            ontology.getTripleStore().addListener(this);
            addedListener = true;
        }
        listAll.clear();
        Ontology onto = ontology;
        if (onto != null) {
            RDFTripleStore store = onto.getTripleStore();
            Set<RDFStatement> states = store.getStatements();
            Iterator<RDFStatement> stateaa = states.iterator();
            try {
                if (useFilecheck.getSelection() && PlatformUI.getWorkbench() != null && PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null && PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null && PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() != null && PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput() != null) {
                    Object input = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
                    String path = "";
                    if (input instanceof FileEditorInput) {
                        path = ((FileEditorInput) input).getPath().toOSString();
                    } else {
                        return;
                    }
                    String filename = path;
                    while (stateaa.hasNext()) {
                        RDFStatement state = stateaa.next();
                        if (state.getSource() != null && state.getSource().equals(filename)) {
                            listAll.add(state);
                        }
                    }
                } else {
                    while (stateaa.hasNext()) {
                        listAll.add(stateaa.next());
                    }
                }
            } catch (SWTException e) {
                return;
            }
        }
    }

    /**
     * The constructor. empty
     */
    public TripleViewer() {
    }

    /**
     * adds listener for active editor change.
     */
    public void checkListener() {
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPageListener(new IPageListener() {

            @Override
            public void pageOpened(IWorkbenchPage page) {
                filterElements(DMManager.getInstance().getOntology());
            }

            @Override
            public void pageClosed(IWorkbenchPage page) {
            }

            @Override
            public void pageActivated(IWorkbenchPage page) {
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addSelectionListener(new ISelectionListener() {

                    @Override
                    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
                        filterElements(DMManager.getInstance().getOntology());
                    }
                });
            }
        });
        PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {

            @Override
            public void windowOpened(IWorkbenchWindow window) {
                filterElements(DMManager.getInstance().getOntology());
            }

            @Override
            public void windowDeactivated(IWorkbenchWindow window) {
            }

            @Override
            public void windowClosed(IWorkbenchWindow window) {
            }

            @Override
            public void windowActivated(IWorkbenchWindow window) {
                filterElements(DMManager.getInstance().getOntology());
            }
        });
    }

    @Override
    public void createPartControl(Composite parent) {
        checkListener();
        parent.setLayout(new GridLayout(1, true));
        Composite enclosingFilter = new Composite(parent, SWT.None);
        if (DMManager.getInstance().getOntology() != null) {
            DMManager.getInstance().getOntology().getTripleStore().addListener(this);
            addedListener = true;
        }
        enclosingFilter.setLayout(new GridLayout(2, true));
        filterTextbox = new Text(enclosingFilter, SWT.NONE);
        filterTextbox.addModifyListener(this);
        filterTextbox.setSize(500, 150);
        useFilecheck = new Button(enclosingFilter, SWT.CHECK);
        useFilecheck.setText("Show only current file");
        useFilecheck.addSelectionListener(this);
        Composite boxen = new Composite(enclosingFilter, SWT.None);
        boxen.setLayout(new RowLayout());
        subjectcheck = new Button(boxen, SWT.CHECK);
        subjectcheck.setText("Subject");
        subjectcheck.addSelectionListener(this);
        predicatecheck = new Button(boxen, SWT.CHECK);
        predicatecheck.setText("Predicate");
        predicatecheck.addSelectionListener(this);
        objectcheck = new Button(boxen, SWT.CHECK);
        objectcheck.setText("Object");
        objectcheck.addSelectionListener(this);
        Button writeFiles = new Button(boxen, SWT.NONE);
        writeFiles.setText("Export RDF");
        writeFiles.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                org.eclipse.swt.widgets.FileDialog fileChooser = new org.eclipse.swt.widgets.FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                fileChooser.setFilterExtensions(new String[] { "*.xml" });
                String path = Preferences.userNodeForPackage(getClass()).get("saveArffPath", System.getProperty("user.home"));
                fileChooser.setFilterPath(path);
                String status = fileChooser.open();
                if (status != null) {
                    RDFExporter.writeTriples(fileChooser.getFilterPath() + "/" + fileChooser.getFileName(), subList);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.heightHint = 150;
        gridData.horizontalSpan = 2;
        boxen.setLayoutData(gridData);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.heightHint = 20;
        filterTextbox.setLayoutData(gridData);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.heightHint = 20;
        useFilecheck.setLayoutData(gridData);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.heightHint = 60;
        enclosingFilter.setLayoutData(gridData);
        Composite tableComposite = new Composite(parent, SWT.NONE);
        TableColumnLayout tableColumnLayout = new TableColumnLayout();
        tableComposite.setLayout(tableColumnLayout);
        tableViewer = new TableViewer(tableComposite, SWT.NONE | SWT.FULL_SELECTION | SWT.MULTI);
        table = tableViewer.getTable();
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        tableComposite.setLayoutData(gridData);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        table.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == 127) {
                    int[] selected = table.getSelectionIndices();
                    TableItem[] items = table.getSelection();
                    for (int i = selected.length - 1; i >= 0; i--) {
                        DMManager.getInstance().getOntology().getTripleStore().removeStatement((RDFStatement) items[i].getData());
                    }
                    tableViewer.refresh();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        TableViewerColumn viewerNameColumn_Subject = new TableViewerColumn(tableViewer, SWT.NONE);
        viewerNameColumn_Subject.getColumn().setText("Subject");
        tableColumnLayout.setColumnData(viewerNameColumn_Subject.getColumn(), new ColumnWeightData(50, 10, true));
        TableViewerColumn viewerNameColumn_Predicate = new TableViewerColumn(tableViewer, SWT.NONE);
        viewerNameColumn_Predicate.getColumn().setText("Predicate");
        TableViewerColumn viewerNameColumn_Object = new TableViewerColumn(tableViewer, SWT.NONE);
        viewerNameColumn_Object.getColumn().setText("Object");
        tableColumnLayout.setColumnData(viewerNameColumn_Predicate.getColumn(), new ColumnWeightData(25, 10, true));
        tableColumnLayout.setColumnData(viewerNameColumn_Object.getColumn(), new ColumnWeightData(50, 10, true));
        viewerNameColumn_Subject.setLabelProvider(new CellLabelProvider() {

            @Override
            public void update(ViewerCell cell) {
                RDFStatement state = ((RDFStatement) cell.getElement());
                Object obj = state.getSubject();
                if (obj instanceof Describable) {
                    cell.setText(((Describable) obj).getDescription());
                } else if (obj instanceof Ontology) {
                    cell.setText("Ontology");
                } else {
                    cell.setText(state.getSubject().toString());
                }
            }
        });
        viewerNameColumn_Subject.getColumn().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                ((TripleSorter) tableViewer.getSorter()).doSort(0);
                tableViewer.refresh();
            }
        });
        viewerNameColumn_Predicate.getColumn().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                ((TripleSorter) tableViewer.getSorter()).doSort(1);
                tableViewer.refresh();
            }
        });
        viewerNameColumn_Predicate.setLabelProvider(new CellLabelProvider() {

            @Override
            public void update(ViewerCell cell) {
                cell.setText(((RDFStatement) cell.getElement()).getPredicate());
            }
        });
        viewerNameColumn_Object.setLabelProvider(new CellLabelProvider() {

            @Override
            public void update(ViewerCell cell) {
                RDFStatement state = ((RDFStatement) cell.getElement());
                Object obj = state.getObject();
                if (obj instanceof Describable) {
                    cell.setText(((Describable) obj).getDescription());
                } else if (obj instanceof Ontology) {
                    cell.setText("Ontology");
                } else {
                    if (obj == null) {
                        cell.setText("");
                    } else {
                        cell.setText(obj.toString());
                    }
                }
            }
        });
        viewerNameColumn_Object.getColumn().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                ((TripleSorter) tableViewer.getSorter()).doSort(2);
                tableViewer.refresh();
            }
        });
        tableViewer.setSorter(new TripleSorter());
        createDatas(DMManager.getInstance().getOntology());
        DMManager.getInstance().addOntologyChangeListener(this);
        tableViewer.setInput(subList);
    }

    /**
     * The Class TripleSorter.
     */
    class TripleSorter extends ViewerSorter {

        /** The Constant ASCENDING. */
        private static final int ASCENDING = 0;

        /** The Constant DESCENDING. */
        private static final int DESCENDING = 1;

        /** The column. */
        private int column;

        /** The direction. */
        private int direction;

        /**
	 * Does the sort. If it's a different column from the previous sort, do
	 * an ascending sort. If it's the same column as the last sort, toggle
	 * the sort direction.
	 * 
	 * @param column
	 *            the column
	 */
        public void doSort(int column) {
            if (column == this.column) {
                direction = 1 - direction;
            } else {
                this.column = column;
                direction = ASCENDING;
            }
        }

        /**
	 * Compares the object for sorting.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param e1
	 *            the e1
	 * @param e2
	 *            the e2
	 * @return the int
	 */
        @Override
        public int compare(Viewer viewer, Object e1, Object e2) {
            int rc = 0;
            try {
                java.text.Collator collator = Collator.getInstance();
                String first = "";
                String second = "";
                if (column == 0) {
                    first = ((RDFStatement) e1).getSubject().toString();
                    second = ((RDFStatement) e2).getSubject().toString();
                } else if (column == 1) {
                    first = ((RDFStatement) e1).getPredicate().toString();
                    second = ((RDFStatement) e2).getPredicate().toString();
                } else if (column == 2) {
                    first = ((RDFStatement) e1).getObject().toString();
                    second = ((RDFStatement) e2).getObject().toString();
                }
                rc = (collator.compare(first, second));
                if (direction == DESCENDING) rc = -rc;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return rc;
        }
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        table.setFocus();
    }

    @Override
    public Collection<String> getSupportedTypes() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(TripleStoreListener.ANY);
        return list;
    }

    @Override
    public void statementsAdded(Collection<RDFStatement> statements) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                filterElements(DMManager.getInstance().getOntology());
            }
        });
    }

    @Override
    public void statementsRemoved(Collection<RDFStatement> statements) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                filterElements(DMManager.getInstance().getOntology());
            }
        });
    }

    @Override
    public void ontologyChanged(OntologyChangedEvent eve) {
        eve.getNewOntology().getTripleStore().addListener(this);
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                filterElements(DMManager.getInstance().getOntology());
            }
        });
    }
}
