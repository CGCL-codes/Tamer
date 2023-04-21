package ie.ul.brendancleary.forager.views;

import ie.ul.brendancleary.forager.automatic.ExperimentHarness;
import ie.ul.brendancleary.forager.index.Index;
import ie.ul.brendancleary.forager.index.IndexElement;
import ie.ul.brendancleary.forager.lm.BayesTools;
import ie.ul.brendancleary.forager.lm.KLDTools;
import ie.ul.brendancleary.forager.util.Stemmer;
import ie.ul.brendancleary.forager.vsm.VSMTools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.MarkerTransfer;
import org.eclipse.ui.part.ViewPart;

/**
 * Implements a search results like view that presents the user 
 * with assignment probabilities for every method, compile unit and package in the 
 * selected projects.
 */
@SuppressWarnings("unchecked")
public class AssignmentResultsView extends ViewPart implements TreeListener {

    public static final int RANK_COLUMN = 0;

    public static final int ELEMENT_COLUMN = 1;

    public static final int LM_COLUMN = 2;

    public static final int DLM_COLUMN = 3;

    public static final int VSM_COLUMN = 4;

    public static final int LSI_COLUMN = 5;

    public static final int KLD_COLUMN = 6;

    public static final int QEKLD_COLUMN = 7;

    public static final int ASSIGNMENT_RESULT_COLUMN = 8;

    private TreeViewer viewer;

    private Action doubleClickAction;

    private Action methodFilterAction;

    private Action typeFilterAction;

    private Action fileFilterAction;

    private Action packageFilterAction;

    private Action projectFilterAction;

    private Action previousResultAction;

    private Action nextResultAction;

    private Action removeMatchAction;

    private Action expandAllAction;

    private Action collapseAllAction;

    private Action histroyAction;

    private Action exportAssignmentResultsAction;

    private Action experimentAction;

    private Vector<AssignmentResult> assignmentResultVector = new Vector<AssignmentResult>();

    private Vector<String[]> assignmentHistory = new Vector<String[]>(1);

    private String[] userQuery;

    private HashMap<String, Double> topIFScores;

    private Index projectIndex;

    private Action openCognitiveMapAction;

    private HashMap<String, HashMap<String, Double>> cognitiveMap = new HashMap<String, HashMap<String, Double>>();

    private Action openFrequencyTableAction;

    private HashMap<String, Integer> frequencyTable = new HashMap<String, Integer>();

    /**
	 * The constructor.
	 */
    public AssignmentResultsView() {
    }

    /**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
    public void createPartControl(Composite parent) {
        setViewer(new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION));
        getViewer().setContentProvider(new AssignmentResultsViewContentProvider());
        getViewer().setLabelProvider(new AssignmentResultsViewLabelProvider());
        final Tree viewTree = getViewer().getTree();
        viewTree.setLinesVisible(true);
        viewTree.setHeaderVisible(true);
        TreeColumn column = new TreeColumn(viewTree, SWT.LEFT, RANK_COLUMN);
        column.setText("Rank");
        column.setWidth(10);
        column.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getViewer().setSorter(new ViewerSorter());
            }
        });
        column = new TreeColumn(viewTree, SWT.LEFT, ELEMENT_COLUMN);
        column.setText("Element");
        column.setWidth(250);
        column.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getViewer().setComparator(new ViewerComparator());
            }
        });
        column = new TreeColumn(viewTree, SWT.LEFT, LM_COLUMN);
        column.setText("LM");
        column.setWidth(150);
        column.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getViewer().setSorter(new DoublesSorter(LM_COLUMN));
            }
        });
        column = new TreeColumn(viewTree, SWT.LEFT, DLM_COLUMN);
        column.setText("DLM");
        column.setWidth(150);
        column.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getViewer().setSorter(new DoublesSorter(DLM_COLUMN));
            }
        });
        column = new TreeColumn(viewTree, SWT.LEFT, VSM_COLUMN);
        column.setText("VSM");
        column.setWidth(100);
        column.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getViewer().setSorter(new DoublesSorter(VSM_COLUMN));
            }
        });
        column = new TreeColumn(viewTree, SWT.LEFT, LSI_COLUMN);
        column.setText("LSI");
        column.setWidth(100);
        column.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getViewer().setSorter(new DoublesSorter(LSI_COLUMN));
            }
        });
        column = new TreeColumn(viewTree, SWT.LEFT, KLD_COLUMN);
        column.setText("KLD");
        column.setWidth(100);
        column.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getViewer().setSorter(new DoublesSorter(KLD_COLUMN));
            }
        });
        column = new TreeColumn(viewTree, SWT.LEFT, QEKLD_COLUMN);
        column.setText("QE-KLD");
        column.setWidth(100);
        column.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getViewer().setSorter(new DoublesSorter(QEKLD_COLUMN));
            }
        });
        getViewer().setSorter(new DoublesSorter(LM_COLUMN));
        viewTree.addTreeListener(this);
        getViewer().setInput(new ArrayList());
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
        try {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot workspaceRoot = workspace.getRoot();
            IJavaModel model = JavaCore.create(workspaceRoot);
            IJavaProject projects[] = model.getJavaProjects();
            setProjectIndex(new Index(projects));
        } catch (JavaModelException e1) {
            e1.printStackTrace();
        }
        int ops = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { MarkerTransfer.getInstance() };
        viewer.addDragSupport(ops, transfers, new AssignmentResultDragListener(this));
        getSite().setSelectionProvider(viewer);
        final Listener labelListener = new ListenerABC1(viewTree);
        viewTree.addListener(SWT.MouseHover, new Listener() {

            Shell tip = null;

            Label label = null;

            public void handleEvent(Event event) {
                switch(event.type) {
                    case SWT.Dispose:
                    case SWT.KeyDown:
                    case SWT.MouseMove:
                        {
                            if (tip == null) break;
                            tip.dispose();
                            tip = null;
                            label = null;
                            break;
                        }
                    case SWT.MouseHover:
                        {
                            TreeItem item = viewTree.getItem(new Point(event.x, event.y));
                            if (item != null) {
                                if (item.getData() instanceof Vector) {
                                    Vector resultVector = ((Vector) item.getData());
                                    AssignmentResult result = ((AssignmentResult) resultVector.get(ASSIGNMENT_RESULT_COLUMN));
                                    if (tip != null && !tip.isDisposed()) tip.dispose();
                                    tip = new Shell(getSite().getShell(), SWT.ON_TOP | SWT.TOOL);
                                    tip.setLayout(new FillLayout());
                                    label = new Label(tip, SWT.NONE);
                                    label.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                                    label.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                                    label.setData("_TABLEITEM", item);
                                    label.setText(item.getText() + " - (" + result.getJavaElement().getPath() + ")");
                                    label.addListener(SWT.MouseExit, labelListener);
                                    label.addListener(SWT.MouseDown, labelListener);
                                    Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                                    Rectangle rect = item.getBounds(0);
                                    Point pt = viewTree.toDisplay(rect.x, rect.y);
                                    tip.setBounds(pt.x, pt.y, size.x, size.y);
                                    tip.setVisible(true);
                                }
                            }
                        }
                }
            }
        });
    }

    /**
	 * Wrapper to create a set markers class and run the operation
	 *
	 */
    public void setMarkers() {
        SetMarkersOperation operation = new SetMarkersOperation(this);
        try {
            operation.run(null);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                AssignmentResultsView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(getViewer().getControl());
        getViewer().getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, getViewer());
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(previousResultAction);
        manager.add(nextResultAction);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        manager.add(removeMatchAction);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        manager.add(expandAllAction);
        manager.add(collapseAllAction);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        manager.add(methodFilterAction);
        manager.add(typeFilterAction);
        manager.add(fileFilterAction);
        manager.add(packageFilterAction);
        manager.add(projectFilterAction);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        manager.add(histroyAction);
        manager.add(exportAssignmentResultsAction);
        manager.add(experimentAction);
        manager.add(openCognitiveMapAction);
        manager.add(openFrequencyTableAction);
    }

    private void makeActions() {
        methodFilterAction = new Action("", IAction.AS_CHECK_BOX) {

            public void run() {
                generateAssignmentResultSet();
            }
        };
        methodFilterAction.setImageDescriptor(org.eclipse.jdt.ui.JavaUI.getSharedImages().getImageDescriptor(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PUBLIC));
        methodFilterAction.setToolTipText("Filter by method");
        methodFilterAction.setChecked(true);
        typeFilterAction = new Action("", IAction.AS_CHECK_BOX) {

            public void run() {
                generateAssignmentResultSet();
            }
        };
        typeFilterAction.setImageDescriptor(org.eclipse.jdt.ui.JavaUI.getSharedImages().getImageDescriptor(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CLASS_DEFAULT));
        typeFilterAction.setToolTipText("Filter by type");
        fileFilterAction = new Action("", IAction.AS_CHECK_BOX) {

            public void run() {
                generateAssignmentResultSet();
            }
        };
        fileFilterAction.setImageDescriptor(org.eclipse.jdt.ui.JavaUI.getSharedImages().getImageDescriptor(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CUNIT));
        fileFilterAction.setToolTipText("Filter by file");
        packageFilterAction = new Action("", IAction.AS_CHECK_BOX) {

            public void run() {
                generateAssignmentResultSet();
            }
        };
        packageFilterAction.setImageDescriptor(org.eclipse.jdt.ui.JavaUI.getSharedImages().getImageDescriptor(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PACKAGE));
        packageFilterAction.setToolTipText("Filter by package");
        projectFilterAction = new Action("", IAction.AS_CHECK_BOX) {

            public void run() {
                generateAssignmentResultSet();
            }
        };
        projectFilterAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(IDE.SharedImages.IMG_OBJ_PROJECT));
        projectFilterAction.setToolTipText("Filter by project");
        removeMatchAction = new Action() {

            public void run() {
                TreeItem[] selectedItems = getViewer().getTree().getSelection();
                for (TreeItem item : selectedItems) {
                    Vector resultVector = ((Vector) item.getData());
                    AssignmentResult result = ((AssignmentResult) resultVector.get(ASSIGNMENT_RESULT_COLUMN));
                    result.setDisplay(false);
                }
                viewer.refresh();
            }
        };
        removeMatchAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/search_rem.gif"));
        removeMatchAction.setToolTipText("Remove selected results");
        expandAllAction = new Action() {

            public void run() {
                getViewer().expandAll();
            }
        };
        expandAllAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/expandall.gif"));
        expandAllAction.setToolTipText("Expand all");
        collapseAllAction = new Action() {

            public void run() {
                getViewer().collapseAll();
            }
        };
        collapseAllAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/collapseall.gif"));
        collapseAllAction.setToolTipText("Collapse all");
        histroyAction = new Action("", Action.AS_DROP_DOWN_MENU) {

            public void run() {
            }
        };
        histroyAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/search_history.gif"));
        histroyAction.setToolTipText("Show previous searches");
        histroyAction.setMenuCreator(new AssignmentResultHistory(getAssignmentHistory(), this));
        previousResultAction = new Action() {

            public void run() {
                try {
                    int index = 0;
                    TreeItem[] selectedItems = getViewer().getTree().getSelection();
                    if (selectedItems.length > 0) {
                        index = getViewer().getTree().indexOf(selectedItems[0]);
                    }
                    selectedItems = new TreeItem[1];
                    if (index > 0) {
                        selectedItems[0] = getViewer().getTree().getItem(index - 1);
                    } else {
                        selectedItems[0] = getViewer().getTree().getItem(index);
                    }
                    getViewer().getTree().setSelection(selectedItems);
                    doubleClickAction.run();
                } catch (Exception e) {
                }
            }
        };
        previousResultAction.setToolTipText("Open previous assignment result");
        previousResultAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/search_prev.gif"));
        nextResultAction = new Action() {

            public void run() {
                try {
                    int index = 0;
                    TreeItem[] selectedItems = getViewer().getTree().getSelection();
                    if (selectedItems.length > 0) {
                        index = getViewer().getTree().indexOf(selectedItems[0]);
                    } else {
                        index = -1;
                    }
                    selectedItems = new TreeItem[1];
                    if (index < getViewer().getTree().getItemCount() - 1) {
                        selectedItems[0] = getViewer().getTree().getItem(index + 1);
                    } else {
                        selectedItems[0] = getViewer().getTree().getItem(index);
                    }
                    getViewer().getTree().setSelection(selectedItems);
                    doubleClickAction.run();
                } catch (Exception e) {
                }
            }
        };
        nextResultAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/search_next.gif"));
        nextResultAction.setToolTipText("Open next assignment result");
        doubleClickAction = new Action() {

            public void run() {
                ISelection selection = getViewer().getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                try {
                    AssignmentResult assignmentResult = (AssignmentResult) ((Vector) obj).get(ASSIGNMENT_RESULT_COLUMN);
                    if (assignmentResult.getType() <= IndexElement.COMPILATION_UNIT) {
                        IMarker marker = assignmentResult.getMarker();
                        org.eclipse.ui.ide.IDE.openEditor(getViewSite().getPage(), marker);
                        setConceptOccurrenceMarkers(assignmentResult);
                    }
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        };
        exportAssignmentResultsAction = new Action() {

            public void run() {
                FileDialog fileChooser = new FileDialog(getViewSite().getShell());
                String theChoosenOne = fileChooser.open();
                if (theChoosenOne != null) {
                    File choosenFile = new File(theChoosenOne);
                    saveAssignmentResults(choosenFile, getAssignmentResultVector());
                }
            }
        };
        exportAssignmentResultsAction.setToolTipText("Save Results");
        experimentAction = new Action() {

            public void run() {
                ExperimentHarness harness = new ExperimentHarness(getViewSite());
                harness.runExperiment();
            }
        };
        experimentAction.setToolTipText("Run Experiment");
        experimentAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/search_next.gif"));
        openCognitiveMapAction = new Action() {

            public void run() {
                loadCognitiveMapFile();
            }
        };
        openCognitiveMapAction.setToolTipText("Open Cognitive Map File");
        openCognitiveMapAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/search_next.gif"));
        openFrequencyTableAction = new Action() {

            public void run() {
                loadFrequencyTableFile();
            }
        };
        openFrequencyTableAction.setToolTipText("Open Frequency Table File");
        openCognitiveMapAction.setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "icons/search_next.gif"));
    }

    private void hookDoubleClickAction() {
        getViewer().addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                doubleClickAction.run();
            }
        });
    }

    public void saveAssignmentResults(File choosenFile, Vector<AssignmentResult> results) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(choosenFile)));
            for (AssignmentResult r : results) {
                try {
                    String sig = Signature.toString(((IMethod) r.getJavaElement()).getSignature(), null, null, false, false);
                    sig = sig.replaceAll(",", " ");
                    String line = r.getJavaElement().getElementName() + "-" + sig + "-" + r.getJavaElement().getPath().toString() + "," + r.getLMScore() + "," + r.getDLMScore() + "," + r.getVsmScore() + "," + r.getLsiScore() + "," + r.getKldScore() + "," + r.getQekldScore();
                    out.println(line);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (JavaModelException e) {
                    e.printStackTrace();
                }
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        getViewer().getControl().setFocus();
    }

    /**
	 * Generates a set of assignmnet results
	 */
    public void generateAssignmentResultSet() {
        assignmentResultVector.clear();
        assert (assignmentResultVector.size() == 0);
        final Vector<Double> queryVector = calculateVSMQueryVector();
        calculateIFFlows();
        viewer.getControl().getDisplay().syncExec(new Runnable() {

            public void run() {
                Iterator<IndexElement> methodIndexIterator = getProjectIndex().getMethodIndexVector().iterator();
                while (methodIndexIterator.hasNext()) {
                    IndexElement element = methodIndexIterator.next();
                    if (filtered(element.getType())) {
                        double lmScore = BayesTools.calculateLMScore(element, getUserQuery());
                        double dlmScore = BayesTools.calculateDLMScore(element, getUserQuery(), lmScore);
                        double vsmScore = VSMTools.calculateVSMScore(element, queryVector);
                        double lsiScore = 0.0;
                        double kldScore = KLDTools.KLDivergence(element, getUserQuery());
                        double qekldcore = KLDTools.KLDivergence(element, getUserQuery(), getTopIFScores());
                        AssignmentResult newAssignment = new AssignmentResult();
                        newAssignment.setJavaElement(element.getJavaElement());
                        newAssignment.setType(element.getType());
                        newAssignment.setStart(element.getStart());
                        newAssignment.setEnd(element.getEnd());
                        newAssignment.setLMScore(lmScore);
                        newAssignment.setDLMScore(dlmScore);
                        newAssignment.setVsmScore(vsmScore);
                        newAssignment.setLsiScore(lsiScore);
                        newAssignment.setKldScore(kldScore);
                        newAssignment.setQekldScore(qekldcore);
                        newAssignment.setFrequency(element.getTextLength());
                        newAssignment.setDisplay(AssignmentResultsView.this.methodFilterAction.isChecked());
                        assignmentResultVector.add(newAssignment);
                    }
                }
                setMarkers();
                listToTree(getAssignmentResultVector());
                viewer.setInput(getAssignmentResultVector());
            }
        });
    }

    /**
	 * Generates a set of assignmnet results
	 */
    public void generateAssignmentResultSetQuite() {
        assignmentResultVector.clear();
        assert (assignmentResultVector.size() == 0);
        final Vector<Double> queryVector = calculateVSMQueryVector();
        calculateIFFlows();
        viewer.getControl().getDisplay().syncExec(new Runnable() {

            public void run() {
                Iterator<IndexElement> methodIndexIterator = getProjectIndex().getMethodIndexVector().iterator();
                while (methodIndexIterator.hasNext()) {
                    IndexElement element = methodIndexIterator.next();
                    double lmScore = BayesTools.calculateLMScore(element, getUserQuery());
                    double dlmScore = BayesTools.calculateDLMScore(element, getUserQuery(), lmScore);
                    double vsmScore = VSMTools.calculateVSMScore(element, queryVector);
                    double lsiScore = 0.0;
                    double kldScore = KLDTools.KLDivergence(element, getUserQuery());
                    double qekldcore = KLDTools.KLDivergence(element, getUserQuery(), getTopIFScores());
                    AssignmentResult newAssignment = new AssignmentResult();
                    newAssignment.setJavaElement(element.getJavaElement());
                    newAssignment.setType(element.getType());
                    newAssignment.setStart(element.getStart());
                    newAssignment.setEnd(element.getEnd());
                    newAssignment.setLMScore(lmScore);
                    newAssignment.setDLMScore(dlmScore);
                    newAssignment.setVsmScore(vsmScore);
                    newAssignment.setLsiScore(lsiScore);
                    newAssignment.setKldScore(kldScore);
                    newAssignment.setQekldScore(qekldcore);
                    newAssignment.setFrequency(element.getTextLength());
                    newAssignment.setDisplay(AssignmentResultsView.this.methodFilterAction.isChecked());
                    assignmentResultVector.add(newAssignment);
                }
            }
        });
    }

    /**
	 * Imposes a hierarchical structure over the given list of assignment results, 
	 * where the hierarchy is based on parent child IJavaElement relations. 
	 * The hierarchy can be accessed though the getChildern / getParent methods on 
	 * AssignmentResult.
	 * 
	 * NOTE the hierarchy is required for tree content providers.
	 * 
	 * @param resultsList
	 */
    public void listToTree(Vector<AssignmentResult> resultsList) {
        for (AssignmentResult ar1 : resultsList) {
            for (AssignmentResult ar2 : resultsList) {
                if (ar1.getJavaElement().getParent().equals(ar2.getJavaElement())) {
                    ar2.getChildern().add(ar1);
                    ar1.setParent(ar2);
                }
                if (ar1.getJavaElement() instanceof PackageFragment) {
                    if (ar1.getJavaElement().getParent().getParent().equals(ar2.getJavaElement())) {
                        ar2.getChildern().add(ar1);
                        ar1.setParent(ar2);
                    }
                }
            }
        }
    }

    /**
	 * We could calculate these independantly however while were iterating over the 
	 * assignment result set once we might as well get everything we need now.
	 * 
	 * @param rootElement
	 * @param container
	 */
    public void getAverageProbabilities(IJavaElement rootElement, AssignmentResult container) {
        double childCount = 0;
        for (AssignmentResult ar : assignmentResultVector) {
            try {
                if (rootElement instanceof org.eclipse.jdt.internal.core.JavaProject) {
                    if (ar.getJavaElement().getParent().getParent().equals(rootElement)) {
                        container.setLMScore(container.getLMScore() + ar.getLMScore());
                        container.setDLMScore(container.getDLMScore() + ar.getDLMScore());
                        container.setVsmScore(container.getVsmScore() + ar.getVsmScore());
                        container.setLsiScore(container.getLsiScore() + ar.getLsiScore());
                        container.setKldScore(container.getKldScore() + ar.getKldScore());
                        container.setQekldScore(container.getQekldScore() + ar.getQekldScore());
                        container.setFrequency(container.getFrequency() + ar.getFrequency());
                        childCount++;
                    }
                }
            } catch (NullPointerException e) {
            }
            if (ar.getJavaElement().getParent().equals(rootElement)) {
                container.setLMScore(container.getLMScore() + ar.getLMScore());
                container.setDLMScore(container.getDLMScore() + ar.getDLMScore());
                container.setVsmScore(container.getVsmScore() + ar.getVsmScore());
                container.setLsiScore(container.getLsiScore() + ar.getLsiScore());
                container.setKldScore(container.getKldScore() + ar.getKldScore());
                container.setQekldScore(container.getQekldScore() + ar.getQekldScore());
                container.setFrequency(container.getFrequency() + ar.getFrequency());
                childCount++;
            }
        }
        if ((container.getLMScore() > 0) && (container.getDLMScore() > 0) && (container.getVsmScore() > 0) && (container.getLsiScore() > 0) & (container.getKldScore() > 0) && (container.getQekldScore() > 0)) {
            container.setLMScore(container.getLMScore() / childCount);
            container.setDLMScore(container.getDLMScore() / childCount);
            container.setVsmScore(container.getVsmScore() / childCount);
            container.setLsiScore(container.getLsiScore() / childCount);
            container.setKldScore(container.getKldScore() / childCount);
            container.setQekldScore(container.getQekldScore() / childCount);
        } else {
            container.setLMScore(0);
            container.setDLMScore(0);
            container.setVsmScore(0);
            container.setLsiScore(0);
            container.setKldScore(0);
            container.setQekldScore(0);
        }
    }

    /**
	 * Checks to see if we need to generate a set of assignment results for 
	 * this element type given the set of element types the user wishes to calculate 
	 * probabilities for. 
	 * This is required as there exists a hierarchical dependency between different index 
	 * elemets when it comes to calculating probability of assignment.
	 * 
	 * @param type
	 * @return
	 */
    public boolean filtered(int type) {
        int highest = -1;
        if (this.methodFilterAction.isChecked()) highest = IndexElement.METHOD;
        if (this.typeFilterAction.isChecked()) highest = IndexElement.TYPE;
        if (this.fileFilterAction.isChecked()) highest = IndexElement.COMPILATION_UNIT;
        if (this.packageFilterAction.isChecked()) highest = IndexElement.PACKAGE;
        if (this.projectFilterAction.isChecked()) highest = IndexElement.PROJECT;
        if (type <= highest) {
            return true;
        } else {
            return false;
        }
    }

    public void setViewer(TreeViewer viewer) {
        this.viewer = viewer;
    }

    public TreeViewer getViewer() {
        return viewer;
    }

    public void setAssignmentResultVector(Vector<AssignmentResult> assignmentResultVector) {
        this.assignmentResultVector = assignmentResultVector;
    }

    public Vector<AssignmentResult> getAssignmentResultVector() {
        return assignmentResultVector;
    }

    public void setProjectIndex(Index projectIndex) {
        this.projectIndex = projectIndex;
    }

    public Index getProjectIndex() {
        return projectIndex;
    }

    /**
	 * Sets termporary markers on occurrences of concepts for a given assignment result
	 * across the associated compliation unit.
	 * The markers are deleted opon close of the compilation unit.
	 * @param result
	 */
    public void setConceptOccurrenceMarkers(AssignmentResult result) {
        SetConceptOccurrenceOperation operation = new SetConceptOccurrenceOperation(this, result);
        try {
            operation.run(null);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Listenens for collapse events on the treeview so that we can redraw the labels
	 */
    public void treeCollapsed(TreeEvent e) {
    }

    public void treeExpanded(TreeEvent e) {
    }

    public void setAssignmentHistory(Vector<String[]> assignmentHistory) {
        this.assignmentHistory = assignmentHistory;
    }

    public Vector<String[]> getAssignmentHistory() {
        return assignmentHistory;
    }

    public String[] getUserQuery() {
        return userQuery;
    }

    public void setUserQuery(String[] query) {
        Stemmer stemmer = new Stemmer();
        Vector<String> tempVector = new Vector<String>();
        for (String s : query) {
            char[] temp = s.toCharArray();
            stemmer.add(temp, temp.length);
            stemmer.stem();
            tempVector.add(stemmer.toString());
        }
        String[] retVal = new String[tempVector.size()];
        tempVector.toArray(retVal);
        userQuery = retVal;
    }

    public void setIFScores(HashMap<String, Double> topIFScores) {
        this.topIFScores = topIFScores;
    }

    public HashMap<String, Double> getTopIFScores() {
        return topIFScores;
    }

    /**
	 * Identifies the subset of the candidate concepts set which exist in the cognitive map concept list. 
	 * 
	 * 1.1	straight lookup of string as specified by the user
	 * 1.2	if straight lookup fails then check for the stemmed version	
	 * 
	 * @param candidateConcepts
	 * @return
	 */
    public Vector<String> lookupCandiateConceptsInCognitiveMap(String[] candidateConcepts, Set<String> cognitiveMapConceptSet) {
        Vector<String> out = new Vector<String>();
        Stemmer stemmer = new Stemmer();
        for (String candidateConcept : candidateConcepts) {
            if (cognitiveMapConceptSet.contains(candidateConcept)) {
                out.add(candidateConcept);
            } else {
                stemmer = new Stemmer();
                stemmer.add(candidateConcept.toCharArray(), candidateConcept.toCharArray().length);
                stemmer.stem();
                String stemmedCandidateConcept = stemmer.toString();
                if (cognitiveMapConceptSet.contains(stemmedCandidateConcept)) {
                    out.add(stemmedCandidateConcept);
                }
            }
        }
        return out;
    }

    /** 
	 * encapsulates the stuff that needs to be done to generate a vsm score for a document
	 * @return
	 */
    private Vector<Double> calculateVSMQueryVector() {
        Vector<String> query = new Vector<String>();
        for (int i = 0; i < getUserQuery().length; i++) {
            query.add(getUserQuery()[i]);
        }
        final Vector<Double> queryVector = VSMTools.computeQueryVector(getProjectIndex().getCorpusVocabulary(), query);
        return queryVector;
    }

    /**
	 * Calculates the information flows present in the cognitive map given a users query, which is then 
	 * used in the query expansion kl-divergence score.
	 *
	 */
    private void calculateIFFlows() {
        Vector<String> validQueryConcepts = lookupCandiateConceptsInCognitiveMap(getUserQuery(), getCognitiveMap().keySet());
        HashMap<String, Double> queryConceptVector = KLDTools.composeQueryConceptVector(validQueryConcepts, getCognitiveMap());
        HashMap<String, Double> ifScores = KLDTools.calculateIFScores(getCognitiveMap(), queryConceptVector);
        HashMap<String, Double> top50IFScores = KLDTools.getTopXIFScores(ifScores, 10);
        setIFScores(top50IFScores);
    }

    /**
	 * Does all the stuff that we need to do to setup a given map file as the 
	 * active cognitive map. Also fires off CognitiveMapChangedEvent's to listeners.
	 * 
	 * @param cognitiveMapFile
	 */
    public void loadCognitiveMapFile() {
        FileDialog fileChooser = new FileDialog(getViewSite().getShell());
        fileChooser.setText("Choose Cognitive Map File");
        String cognitiveMapFile = fileChooser.open();
        HashMap<String, HashMap<String, Integer>> rawCognitiveMap = parseCognitiveMap(new File(cognitiveMapFile));
        this.setCognitiveMap(KLDTools.createProbablisticCognitiveMap(rawCognitiveMap));
    }

    public void loadFrequencyTableFile() {
        FileDialog fileChooser = new FileDialog(getViewSite().getShell());
        fileChooser.setText("Choose Frequency Table File");
        String frequencyTableFile = fileChooser.open();
        parseFrequencyTable(new File(frequencyTableFile));
    }

    /**
	 * Parses Cognitive map from file into 2d hashmap.
	 *
	 */
    public HashMap<String, HashMap<String, Integer>> parseCognitiveMap(File cognitiveMapFile) {
        HashMap<String, HashMap<String, Integer>> temp = new HashMap<String, HashMap<String, Integer>>();
        try {
            FileReader fileReader = new FileReader(cognitiveMapFile);
            BufferedReader bufReader = new BufferedReader(fileReader);
            try {
                if (bufReader.ready()) {
                    while (true) {
                        String lineString = bufReader.readLine();
                        if (lineString == null) {
                            break;
                        }
                        if ((lineString != null) && (!lineString.equalsIgnoreCase(""))) {
                            String[] lineArray = lineString.split(",");
                            int frequency = Integer.parseInt(lineArray[0].trim());
                            String c1 = lineArray[1].trim();
                            String c2 = lineArray[2].trim();
                            c1 = c1.replaceAll("_", " ");
                            c2 = c2.replaceAll("_", " ");
                            if (temp.containsKey(c1.trim())) {
                                HashMap tempHash = (HashMap) temp.get(c1.trim());
                                if (tempHash.containsKey(c2.trim())) {
                                    int tempFreq = ((Integer) tempHash.get(c2.trim())).intValue();
                                    tempHash.put(c2.trim(), tempFreq + frequency);
                                } else {
                                    tempHash.put(c2.trim(), frequency);
                                }
                            } else {
                                HashMap tempHash = new HashMap();
                                tempHash.put(c2.trim(), frequency);
                                temp.put(c1.trim(), tempHash);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
	 * Parses the frequency table in getFrequencyTableFile() into a HashMap<String, Ineteger> 
	 * accessed through getFrequencyTable().
	 * 
	 * The parser works over files of the format: concept,frequency
	 * where concept is a String and frequency is an int.
	 */
    public void parseFrequencyTable(File frequencyTableFile) {
        setFrequencyTable(new HashMap<String, Integer>());
        try {
            FileReader fileReader = new FileReader(frequencyTableFile);
            BufferedReader bufReader = new BufferedReader(fileReader);
            try {
                if (bufReader.ready()) {
                    while (true) {
                        String lineString = bufReader.readLine();
                        if (lineString == null) {
                            break;
                        }
                        if ((lineString != null) && (!lineString.equalsIgnoreCase(""))) {
                            String[] lineArray = lineString.split(",");
                            String concept = lineArray[0].trim();
                            int frequency = Integer.parseInt(lineArray[1].trim());
                            assert (getFrequencyTable().get(concept) == null);
                            getFrequencyTable().put(concept, new Integer(frequency));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setCognitiveMap(HashMap<String, HashMap<String, Double>> cognitiveMap) {
        this.cognitiveMap = cognitiveMap;
    }

    public HashMap<String, HashMap<String, Double>> getCognitiveMap() {
        return cognitiveMap;
    }

    public void setFrequencyTable(HashMap<String, Integer> frequencyTable) {
        this.frequencyTable = frequencyTable;
    }

    public HashMap<String, Integer> getFrequencyTable() {
        return frequencyTable;
    }
}
