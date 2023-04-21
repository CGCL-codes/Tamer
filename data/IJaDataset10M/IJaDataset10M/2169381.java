package net.sourceforge.pmd.eclipse.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.pmd.eclipse.PMDConstants;
import net.sourceforge.pmd.eclipse.PMDPlugin;
import net.sourceforge.pmd.eclipse.model.FileRecord;
import net.sourceforge.pmd.eclipse.model.PMDRecord;
import net.sourceforge.pmd.eclipse.model.PackageRecord;
import net.sourceforge.pmd.eclipse.model.ProjectRecord;
import net.sourceforge.pmd.eclipse.model.RootRecord;
import net.sourceforge.pmd.eclipse.views.actions.CollapseAllAction;
import net.sourceforge.pmd.eclipse.views.actions.PackageSwitchAction;
import net.sourceforge.pmd.eclipse.views.actions.PriorityFilterAction;
import net.sourceforge.pmd.eclipse.views.actions.ProjectFilterAction;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

/**
 * A View for PMD-Violations, provides an Overview
 * as well as statistical Information
 * 
 * @author SebastianRaffel  ( 08.05.2005 )
 */
public class ViolationOverview extends ViewPart implements IDoubleClickListener, ISelectionChangedListener, ISelectionProvider {

    private TableTreeViewer treeViewer;

    private ViolationOverviewContentProvider contentProvider;

    private ViolationOverviewLabelProvider labelProvider;

    private PriorityFilter priorityFilter;

    private ProjectFilter projectFilter;

    private RootRecord root;

    private PMDRecord currentProject;

    private ViewMemento memento;

    private PriorityFilterAction[] priorityActions;

    private boolean isPackageFiltered;

    protected Integer[] columnWidths;

    protected int[] columnSortOrder = { 0, 0, 1, -1, -1, -1, 1 };

    protected int currentSortedColumn;

    protected static final String PACKAGE_SWITCH = "packageSwitch";

    protected static final String PRIORITY_LIST = "priorityFilterList";

    protected static final String PROJECT_LIST = "projectFilterList";

    protected static final String COLUMN_WIDTHS = "tableColumnWidths";

    protected static final String COLUMN_SORTER = "tableColumnSorter";

    public void createPartControl(Composite parent) {
        int treeStyle = SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION;
        treeViewer = new TableTreeViewer(parent, treeStyle);
        treeViewer.setUseHashlookup(true);
        treeViewer.getTableTree().getTable().setHeaderVisible(true);
        treeViewer.getTableTree().getTable().setLinesVisible(true);
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(labelProvider);
        treeViewer.addFilter(priorityFilter);
        treeViewer.addFilter(projectFilter);
        setupActions();
        createColumns(treeViewer.getTableTree().getTable());
        createActionBars();
        createDropDownMenu();
        createContextMenu();
        treeViewer.setInput(root);
        treeViewer.addDoubleClickListener(this);
        treeViewer.addSelectionChangedListener(this);
        getSite().setSelectionProvider(this);
        if (memento != null) {
            ArrayList widthList = memento.getIntegerList(COLUMN_WIDTHS);
            if (widthList != null) {
                columnWidths = new Integer[widthList.size()];
                widthList.toArray(columnWidths);
                setColumnWidths();
            }
            ArrayList sorterList = memento.getIntegerList(COLUMN_SORTER);
            if (sorterList != null) {
                Integer[] sorterProps = new Integer[sorterList.size()];
                sorterList.toArray(sorterProps);
                setSorterProperties(sorterProps);
            }
        }
    }

    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        root = (RootRecord) getInitialInput();
        contentProvider = new ViolationOverviewContentProvider(this);
        labelProvider = new ViolationOverviewLabelProvider(this);
        priorityFilter = new PriorityFilter();
        projectFilter = new ProjectFilter();
        memento = new ViewMemento(PMDPlugin.MEMENTO_OVERVIEW_FILE);
        if (memento != null) {
            ArrayList priorityList = memento.getIntegerList(PRIORITY_LIST);
            if (priorityList != null) priorityFilter.setPriorityFilterList(priorityList);
            ArrayList projectNames = memento.getStringList(PROJECT_LIST);
            if (projectNames != null) {
                ArrayList projectList = new ArrayList();
                for (int k = 0; k < projectNames.size(); k++) {
                    PMDRecord project = root.findResourceByName(projectNames.get(k).toString(), PMDRecord.TYPE_PROJECT);
                    if (project != null) projectList.add(project);
                }
                projectFilter.setProjectFilterList(projectList);
            }
            Integer packageFiltered = memento.getInteger(PACKAGE_SWITCH);
            if ((packageFiltered != null) && (packageFiltered.intValue() == 1)) isPackageFiltered = true;
        }
    }

    public void dispose() {
        memento.putArrayList(PRIORITY_LIST, priorityFilter.getPriorityFilterList());
        ArrayList projects = projectFilter.getProjectFilterList();
        ArrayList projectNames = new ArrayList();
        for (int k = 0; k < projects.size(); k++) {
            PMDRecord project = (PMDRecord) projects.get(k);
            projectNames.add(project.getName());
        }
        memento.putArrayList(PROJECT_LIST, projectNames);
        ArrayList widthList = new ArrayList(Arrays.asList(columnWidths));
        memento.putArrayList(COLUMN_WIDTHS, widthList);
        Integer[] sorterProps = new Integer[] { new Integer(currentSortedColumn), new Integer(columnSortOrder[currentSortedColumn]) };
        ArrayList sorterList = new ArrayList(Arrays.asList(sorterProps));
        memento.putArrayList(COLUMN_SORTER, sorterList);
        if (isPackageFiltered == true) memento.putInteger(PACKAGE_SWITCH, 1); else memento.putInteger(PACKAGE_SWITCH, 0);
        memento.save(PMDPlugin.MEMENTO_OVERVIEW_FILE);
        super.dispose();
    }

    /**
	 * Creates the initial Input
	 * In General this is a RootRecord for the WorkspaceRoot
	 * 
	 * @return an Input-Object
	 */
    private Object getInitialInput() {
        IWorkspaceRoot root = (IWorkspaceRoot) ResourcesPlugin.getWorkspace().getRoot();
        return new RootRecord(root);
    }

    /**
	 * Creates the Table's Columns
	 * 
	 * @param table
	 */
    private void createColumns(Table table) {
        TableColumn plusColumn = new TableColumn(table, SWT.RIGHT);
        plusColumn.setWidth(20);
        plusColumn.setResizable(false);
        TableColumn imageColumn = new TableColumn(table, SWT.CENTER);
        imageColumn.setWidth(20);
        imageColumn.setResizable(false);
        final TableColumn elementColumn = new TableColumn(table, SWT.LEFT);
        elementColumn.setText(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_VIEW_OVERVIEW_COLUMN_ELEMENT));
        elementColumn.setWidth(200);
        final TableColumn vioTotalColumn = new TableColumn(table, SWT.LEFT);
        vioTotalColumn.setText(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_VIEW_OVERVIEW_COLUMN_VIO_TOTAL));
        vioTotalColumn.setWidth(100);
        final TableColumn vioLocColumn = new TableColumn(table, SWT.LEFT);
        vioLocColumn.setText(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_VIEW_OVERVIEW_COLUMN_VIO_LOC));
        vioLocColumn.setWidth(100);
        final TableColumn vioMethodColumn = new TableColumn(table, SWT.LEFT);
        vioMethodColumn.setText(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_VIEW_OVERVIEW_COLUMN_VIO_METHOD));
        vioMethodColumn.setWidth(100);
        final TableColumn projectColumn = new TableColumn(table, SWT.LEFT);
        projectColumn.setText(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_VIEW_OVERVIEW_COLUMN_PROJECT));
        projectColumn.setWidth(100);
        createColumnAdapters(treeViewer.getTableTree().getTable());
        getViewerSorter(3);
    }

    /**
	 * Creates Adapter for sorting and resizing the Columns
	 * 
	 * @param table
	 */
    private void createColumnAdapters(Table table) {
        TableColumn[] columns = table.getColumns();
        columnWidths = new Integer[columns.length];
        columnWidths[0] = new Integer(columns[0].getWidth());
        columnWidths[1] = new Integer(columns[1].getWidth());
        for (int k = 2; k < columns.length; k++) {
            columnWidths[k] = new Integer(columns[k].getWidth());
            final int i = k;
            columns[k].addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    currentSortedColumn = i;
                    columnSortOrder[currentSortedColumn] *= -1;
                    treeViewer.setSorter(getViewerSorter(currentSortedColumn));
                }
            });
            columns[k].addControlListener(new ControlAdapter() {

                public void controlResized(ControlEvent e) {
                    columnWidths[i] = new Integer(treeViewer.getTableTree().getTable().getColumn(i).getWidth());
                }
            });
        }
    }

    /**
	 * Creates the ActionBars
	 */
    private void createActionBars() {
        IToolBarManager manager = getViewSite().getActionBars().getToolBarManager();
        Action switchPackagesAction = new PackageSwitchAction(this);
        switchPackagesAction.setChecked(isPackageFiltered);
        manager.add(switchPackagesAction);
        manager.add(new Separator());
        for (int i = 0; i < priorityActions.length; i++) {
            manager.add(priorityActions[i]);
        }
        manager.add(new Separator());
        Action collapseAllAction = new CollapseAllAction(this);
        manager.add(collapseAllAction);
    }

    private void createDropDownMenu() {
        IMenuManager manager = getViewSite().getActionBars().getMenuManager();
        manager.removeAll();
        createProjectFilterMenu((MenuManager) manager);
    }

    /**
	 * Creates the Context Menu
	 */
    private void createContextMenu() {
        MenuManager manager = new MenuManager();
        manager.setRemoveAllWhenShown(true);
        manager.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                MenuManager submenuManager;
                submenuManager = new MenuManager(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_VIEW_MENU_RESOURCE_FILTER));
                createProjectFilterMenu(submenuManager);
                manager.add(submenuManager);
                submenuManager = new MenuManager(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_VIEW_MENU_PRIORITY_FILTER));
                for (int i = 0; i < priorityActions.length; i++) {
                    submenuManager.add(priorityActions[i]);
                }
                manager.add(submenuManager);
                manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
                manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-end"));
            }
        });
        Table table = treeViewer.getTableTree().getTable();
        table.setMenu(manager.createContextMenu(table));
        getSite().registerContextMenu(manager, treeViewer);
    }

    /**
	 * Return the ViewerSorter for a Column
	 * 
	 * @param column, the Number of the Column in the Table
	 * @return the ViewerSorter for the column
	 */
    private ViewerSorter getViewerSorter(int columnNr) {
        TableColumn column = treeViewer.getTableTree().getTable().getColumn(columnNr);
        final int sortOrder = columnSortOrder[columnNr];
        switch(columnNr) {
            case 2:
                return new TableColumnSorter(column, sortOrder) {

                    public int compare(Viewer viewer, Object e1, Object e2) {
                        String name1 = "";
                        String name2 = "";
                        if ((e1 instanceof PackageRecord) && (e2 instanceof PackageRecord)) {
                            name1 = ((PackageRecord) e1).getName();
                            name2 = ((PackageRecord) e2).getName();
                        } else if ((e1 instanceof FileRecord) && (e2 instanceof FileRecord)) {
                            name1 = ((FileRecord) e1).getName();
                            name2 = ((FileRecord) e2).getName();
                        }
                        return name1.compareToIgnoreCase(name2) * sortOrder;
                    }
                };
            case 3:
                return new TableColumnSorter(column, sortOrder) {

                    public int compare(Viewer viewer, Object e1, Object e2) {
                        int vio1 = getFilteredViolations(e1);
                        int vio2 = getFilteredViolations(e2);
                        return new Integer(vio1).compareTo(new Integer(vio2)) * sortOrder;
                    }
                };
            case 4:
                return new TableColumnSorter(column, sortOrder) {

                    public int compare(Viewer viewer, Object e1, Object e2) {
                        int vio1 = getFilteredViolations(e1);
                        int vio2 = getFilteredViolations(e2);
                        int loc1 = 0;
                        int loc2 = 0;
                        if ((e1 instanceof PackageRecord) && (e2 instanceof PackageRecord)) {
                            PackageRecord pack1 = ((PackageRecord) e1);
                            PackageRecord pack2 = ((PackageRecord) e2);
                            Object[] files1 = pack1.getChildren();
                            for (int i = 0; i < files1.length; i++) {
                                loc1 += ((FileRecord) files1[i]).getLinesOfCode();
                            }
                            Object[] files2 = pack2.getChildren();
                            for (int j = 0; j < files2.length; j++) {
                                loc2 += ((FileRecord) files2[j]).getLinesOfCode();
                            }
                        } else if ((e1 instanceof FileRecord) && (e2 instanceof FileRecord)) {
                            loc1 = ((FileRecord) e1).getLinesOfCode();
                            loc2 = ((FileRecord) e2).getLinesOfCode();
                        }
                        Float vioPerLoc1 = new Float((float) vio1 / loc1);
                        Float vioPerLoc2 = new Float((float) vio2 / loc2);
                        return vioPerLoc1.compareTo(vioPerLoc2) * sortOrder;
                    }
                };
            case 5:
                return new TableColumnSorter(column, sortOrder) {

                    public int compare(Viewer viewer, Object e1, Object e2) {
                        int vio1 = getFilteredViolations(e1);
                        int vio2 = getFilteredViolations(e2);
                        int numMethods1 = 0;
                        int numMethods2 = 0;
                        if ((e1 instanceof PackageRecord) && (e2 instanceof PackageRecord)) {
                            PackageRecord pack1 = ((PackageRecord) e1);
                            PackageRecord pack2 = ((PackageRecord) e2);
                            Object[] files1 = pack1.getChildren();
                            for (int i = 0; i < files1.length; i++) {
                                numMethods1 += ((FileRecord) files1[i]).getNumberOfMethods();
                            }
                            Object[] files2 = pack2.getChildren();
                            for (int j = 0; j < files2.length; j++) {
                                numMethods2 += ((FileRecord) files2[j]).getNumberOfMethods();
                            }
                        } else if ((e1 instanceof FileRecord) && (e2 instanceof FileRecord)) {
                            numMethods1 = ((FileRecord) e1).getNumberOfMethods();
                            numMethods2 = ((FileRecord) e2).getNumberOfMethods();
                        }
                        Float vioPerMethod1;
                        if (numMethods1 == 0) vioPerMethod1 = new Float(0.0f); else vioPerMethod1 = new Float((float) vio1 / numMethods1);
                        Float vioPerMethod2;
                        if (numMethods2 == 0) vioPerMethod2 = new Float(0.0f); else vioPerMethod2 = new Float((float) vio2 / numMethods2);
                        return vioPerMethod1.compareTo(vioPerMethod2) * sortOrder;
                    }
                };
            case 6:
                return new TableColumnSorter(column, sortOrder) {

                    public int compare(Viewer viewer, Object e1, Object e2) {
                        PMDRecord project1 = null;
                        PMDRecord project2 = null;
                        if ((e1 instanceof PackageRecord) && (e2 instanceof PackageRecord)) {
                            project1 = ((PackageRecord) e1).getParent();
                            project2 = ((PackageRecord) e2).getParent();
                        } else if ((e1 instanceof FileRecord) && (e2 instanceof FileRecord)) {
                            project1 = ((FileRecord) e1).getParent().getParent();
                            project2 = ((FileRecord) e2).getParent().getParent();
                        }
                        return (project1.getName()).compareToIgnoreCase(project2.getName()) * sortOrder;
                    }
                };
        }
        return null;
    }

    /**
	 * Setup the Actions for the ActionBars
	 */
    protected void setupActions() {
        Integer[] priorities = PMDPlugin.getDefault().getPriorityValues();
        priorityActions = new PriorityFilterAction[priorities.length];
        for (int i = 0; i < priorities.length; i++) {
            priorityActions[i] = new PriorityFilterAction(priorities[i], this);
            if (priorityFilter.getPriorityFilterList().contains(priorities[i])) priorityActions[i].setChecked(true);
        }
    }

    /**
	 * Create the Menu for filtering Projects
	 * 
	 * @param manager, the MenuManager
	 */
    protected void createProjectFilterMenu(MenuManager manager) {
        ArrayList projectFilterList = projectFilter.getProjectFilterList();
        final ArrayList projectList = new ArrayList();
        if (root != null) {
            PMDRecord[] projects = root.getChildren();
            for (int i = 0; i < projects.length; i++) {
                ProjectRecord project = (ProjectRecord) projects[i];
                if (project.hasMarkers()) {
                    Action projectFilterAction = new ProjectFilterAction(project, this);
                    if (!projectFilterList.contains(projects[i])) projectFilterAction.setChecked(true);
                    manager.add(projectFilterAction);
                    projectList.add(project);
                }
            }
            manager.add(new Separator());
            Action currentProjectAction = new Action() {

                public void run() {
                    projectFilter.setProjectFilterList(projectList);
                    projectFilter.removeProjectFromList(currentProject);
                    refresh();
                }
            };
            currentProjectAction.setText(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_VIEW_ACTION_CURRENT_PROJECT));
            manager.add(currentProjectAction);
        }
    }

    /**
	 * Gets the Violations that are filtered, meaning, if e.g. 
	 * the Priorities 4 and 5 are filtered, this Function returns 
	 * the Number of all Priority 1,2 and 3-Markers
	 * 
	 * @param element
	 * @return the Number of visible Violations for the given Element
	 */
    public int getFilteredViolations(Object element) {
        IMarker[] markers;
        int violations = 0;
        List filterList = priorityFilter.getPriorityFilterList();
        if (element instanceof PackageRecord) {
            PackageRecord packageRec = (PackageRecord) element;
            for (int i = 0; i < filterList.size(); i++) {
                Integer priority = (Integer) filterList.get(i);
                markers = packageRec.findMarkersByAttribute(PMDPlugin.KEY_MARKERATT_PRIORITY, priority);
                if (markers != null) violations += markers.length;
            }
        } else if (element instanceof FileRecord) {
            FileRecord fileRec = (FileRecord) element;
            for (int i = 0; i < filterList.size(); i++) {
                Integer priority = (Integer) filterList.get(i);
                markers = fileRec.findMarkersByAttribute(PMDPlugin.KEY_MARKERATT_PRIORITY, priority);
                if (markers != null) violations += markers.length;
            }
        }
        return violations;
    }

    /**
	 * Sets the View to show Packages and files (false) 
	 * or Files only (true) 
	 * @param packageFiltered
	 */
    public void setIsPackageFiltered(boolean packageFiltered) {
        isPackageFiltered = packageFiltered;
    }

    /**
	 * Returns the State, if Packages are to filter
	 * 
	 * @return true, if only Files should be seen, 
	 * false if Packages and Files should be displayed 
	 */
    public boolean getIsPackageFiltered() {
        return isPackageFiltered;
    }

    /**
	 * Sets the Widths of the Columns
	 */
    public void setColumnWidths() {
        if (treeViewer.getTableTree().getTable().isDisposed()) return;
        TableColumn[] columns = treeViewer.getTableTree().getTable().getColumns();
        for (int k = 0; k < columnWidths.length; k++) {
            columns[k].setWidth(columnWidths[k].intValue());
        }
    }

    /**
	 * Sets the Properties for Sorting
	 * 
	 * @param properties, an Array with Properties, 
	 * the First Value is the Number of the sorted Column, 
	 * the Second one is the Direction (-1 or 1)
	 */
    public void setSorterProperties(Integer[] properties) {
        currentSortedColumn = properties[0].intValue();
        columnSortOrder[currentSortedColumn] = properties[1].intValue();
        treeViewer.setSorter(getViewerSorter(currentSortedColumn));
    }

    public void setFocus() {
        treeViewer.getTableTree().setFocus();
    }

    /**
	 * @return the viewer
	 */
    public TableTreeViewer getViewer() {
        return treeViewer;
    }

    /**
	 * Refresh the View (and its Elements)
	 */
    public void refresh() {
        if (!treeViewer.getControl().isDisposed()) {
            treeViewer.getControl().setRedraw(false);
            treeViewer.refresh();
            createDropDownMenu();
            treeViewer.getControl().setRedraw(true);
        }
    }

    public void doubleClick(DoubleClickEvent event) {
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        Object object = selection.getFirstElement();
        if (object instanceof PackageRecord) {
            PackageRecord packageRec = (PackageRecord) object;
            int expandLevel;
            if (treeViewer.getExpandedState(packageRec)) treeViewer.collapseToLevel(packageRec, TableTreeViewer.ALL_LEVELS); else treeViewer.expandToLevel(packageRec, 1);
        } else if (object instanceof FileRecord) {
            try {
                IFile file = (IFile) ((FileRecord) object).getResource();
                IDE.openEditor(getSite().getPage(), file);
            } catch (PartInitException pie) {
                PMDPlugin.getDefault().logError(PMDConstants.MSGKEY_ERROR_VIEW_EXCEPTION + this.toString(), pie);
            }
        }
    }

    public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        Object object = selection.getFirstElement();
        PMDRecord project = null;
        if (object instanceof PackageRecord) {
            project = ((PackageRecord) object).getParent();
        } else if (object instanceof FileRecord) {
            project = ((FileRecord) object).getParent().getParent();
        }
        if (project == null) return;
        currentProject = project;
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        treeViewer.addSelectionChangedListener(listener);
    }

    public ISelection getSelection() {
        return treeViewer.getSelection();
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        treeViewer.removeSelectionChangedListener(listener);
    }

    public void setSelection(ISelection selection) {
        treeViewer.setSelection(selection);
    }
}
