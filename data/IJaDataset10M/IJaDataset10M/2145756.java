package org.xaware.tracer.editors;

import java.util.HashSet;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.jdom.Document;
import org.jdom.Element;
import org.xaware.ide.xadev.gui.editor.BizdocInputHelper;
import org.xaware.rvpublisher.model.TracerManager;
import org.xaware.tracer.actions.ElementStepAction;
import org.xaware.tracer.actions.LoadInputDataHelper;
import org.xaware.tracer.actions.RunToBreakpointAction;
import org.xaware.tracer.actions.StepIntoAction;
import org.xaware.tracer.actions.StepOverAction;
import org.xaware.tracer.engine.BizdocTraceEngine;
import org.xaware.tracer.menumanagers.TracerMenuManager;
import org.xaware.tracer.model.TracerModel;
import org.xaware.tracer.panels.MainTreePanel;
import org.xaware.tracer.util.TraceUtil;

/**
 * MainTracer is the EditorPart that forms the main TreeView and controls the tracing
 * 
 * @author jtarnowski
 */
public class MainTracerEditorPart extends EditorPart implements IDoubleClickListener, ISelectionListener, ITracer {

    public static final String ID = MainTracer.class.getName();

    protected TracerModel model;

    protected MainTreePanel treePanel;

    boolean inputLoaded = false;

    protected HashSet<Element> breakpoints = new HashSet<Element>();

    protected BizdocTraceEngine traceEngine = new BizdocTraceEngine();

    protected Menu menu;

    protected TracerMenuManager popup;

    /** Container/helper for input parameters so state can be kept */
    private BizdocInputHelper inputHelper;

    /**
     * Initializes this editor with the given editor site and input. This method is automatically called shortly after
     * the part is instantiated, marking the start of the part's lifecycle. In our case, we assert that we are editing a
     * file.
     */
    @Override
    public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
        if (!(input instanceof IFileEditorInput)) {
            throw new PartInitException("Invalid Input: Must be IFileEditorInput");
        }
        super.setSite(site);
        super.setInput(input);
        inputHelper = new BizdocInputHelper();
    }

    /**
     * This is called by eclipse to create the GUI
     */
    @Override
    public void createPartControl(final Composite parent) {
        treePanel = new MainTreePanel(parent);
        treePanel.addDoubleClickListener(this);
        treePanel.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(final SelectionChangedEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (model == null) {
                    model = new TracerModel();
                }
                model.setSelectedElem((Element) selection.getFirstElement());
                final TracerManager manager = TracerManager.getManager();
                manager.setModel(model);
            }
        });
        getSite().getPage().addPostSelectionListener(this);
    }

    public void registerSelectionProvider(final IWorkbenchPartSite aSite) {
        aSite.setSelectionProvider(treePanel);
    }

    /**
     * Setup the GUI
     * 
     */
    public void populateMainPage() {
        populateMainPage(null);
    }

    public void populateMainPage(final String filename) {
        try {
            inputLoaded = false;
            if (filename != null) {
                loadBizDoc(filename);
            } else {
                final IEditorInput input = getEditorInput();
                final FileEditorInput fInput = (FileEditorInput) input;
                final IFile iFile = fInput.getFile();
                loadBizDoc(iFile);
            }
            if (menu == null) {
                menu = new Menu(treePanel.getTree().getParent());
                popup = new TracerMenuManager(menu, this);
            }
            treePanel.getTree().setMenu(menu);
            setInitialTitle();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the input file and set up to be able to trace
     * 
     * @param iFile
     *            IFile
     * @throws CoreException
     */
    void loadBizDoc(final IFile iFile) throws CoreException {
        final Document doc = TraceUtil.getDocument(iFile);
        model = new TracerModel();
        final TracerManager manager = TracerManager.getManager();
        manager.setModel(model);
        final Element elem = doc.getRootElement();
        model.setRootElem(elem);
        model.setDoc(doc);
        model.setEngine(traceEngine);
        model.setInputRequired(true);
        model.setRunAllowed(true);
        traceEngine.setupBizDoc(elem, iFile.getName());
        refreshTree();
    }

    /**
     * Read the input file and set up to be able to trace
     * 
     * @param url 
     *            String
     */
    void loadBizDoc(final String url) {
        final Document doc = TraceUtil.getDocument(url);
        model = new TracerModel();
        final TracerManager manager = TracerManager.getManager();
        manager.setModel(model);
        model.setRootElem(doc.getRootElement());
        model.setDoc(doc);
        model.setEngine(traceEngine);
        traceEngine.setupBizDoc(doc.getRootElement(), url);
        refreshTree();
    }

    /**
     * Refresh the tree panel. This will also refresh the valuesPanel with the info about the current node.
     */
    public void refreshTree() {
        treePanel.setBreakpoints(model.getBreakpoints());
        treePanel.setCurrentElement(model.getCurrentElem());
        treePanel.refreshTree(model.getRootElem(), model.getBreakpoints(), model.getCurrentElem());
        TracerManager.getManager().setModel(model);
    }

    /**
     * Update the editor's title based upon the content being edited.
     */
    public void setInitialTitle() {
        final IEditorInput input = getEditorInput();
        setPartName(input.getName());
        setTitleToolTip(input.getToolTipText());
    }

    /**
     * Set title when tracing has started
     */
    public void setExecutingTitle() {
        final IEditorInput input = getEditorInput();
        setPartName("Executing : " + input.getName());
        setTitleToolTip(input.getToolTipText());
    }

    /**
     * Asks this part to take focus within the workbench. In our case, focus is redirected to the appropriate control
     * based upon what page is currently selected???
     */
    @Override
    public void setFocus() {
        final TracerManager manager = TracerManager.getManager();
        manager.setModel(model);
        treePanel.getTree().setFocus();
    }

    /**
     * Returns whether the "Save As" operation is supported by this part.
     */
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /**
     * Abstract method that must be implemented. Do nothing
     */
    @Override
    public void doSave(final IProgressMonitor monitor) {
    }

    /**
     * Abstract method that must be implemented. Do nothing
     */
    @Override
    public void doSaveAs() {
    }

    /**
     * Abstract method that must be implemented. Do nothing
     */
    @Override
    public boolean isDirty() {
        return false;
    }

    public Shell getShell() {
        return treePanel.getTree().getShell();
    }

    public BizdocTraceEngine getEngine() {
        return traceEngine;
    }

    public HashSet getBreakpoints() {
        return breakpoints;
    }

    public void refreshAll(final Element rootNode, final Element currentNode) {
        refreshTree();
    }

    /**
     * When a node is selected, display it's values. If another type of editor is given focus, clear views
     */
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        if (part == this || (part instanceof MainTracer)) {
            return;
        }
        final IWorkbench wb = PlatformUI.getWorkbench();
        final IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
        final IWorkbenchPage page = win.getActivePage();
        if (page != null) {
            final IEditorPart editor = page.getActiveEditor();
            if (!(editor instanceof MainTracer)) {
                final TracerManager manager = TracerManager.getManager();
                manager.setModel(new TracerModel());
            }
        }
    }

    /**
     * Double-clicking with the mouse toggles breakpoint.
     * 
     * @param m -
     *            DoubleClickEvent
     */
    public void doubleClick(final DoubleClickEvent m) {
        toggleBreakpoint();
    }

    /**
     * Toggle breakpoint on currently selected element
     */
    public void toggleBreakpoint() {
        Element elem = null;
        final Element selected = model.getSelectedElem();
        if (selected != null) {
            if (!breakpoints.contains(selected)) {
                breakpoints.add(selected);
                model.addBreakpoints(selected);
            } else {
                breakpoints.remove(selected);
                model.removeBreakpoints(selected);
            }
            refreshTree();
        }
    }

    /**
     * Just what it says
     */
    public void removeAllBreakpoints() {
        breakpoints.clear();
        model.clearBreakpoints();
        refreshTree();
    }

    public void loadInput() {
        LoadInputDataHelper.loadInputData(this);
    }

    /**
     * One step of execution - into or over
     */
    public void stepInto() {
        final StepIntoAction stepper = new StepIntoAction();
        stepper.setActiveEditor(getMainTracer());
        stepper.run();
    }

    /**
     * One step of execution
     */
    public void step() {
        final StepOverAction stepper = new StepOverAction();
        stepper.setActiveEditor(getMainTracer());
        stepper.run();
    }

    /**
     * Element step of execution
     */
    public void stepNext() {
        final ElementStepAction stepper = new ElementStepAction();
        stepper.setActiveEditor(getMainTracer());
        stepper.run();
    }

    /**
     * Several steps of execution
     */
    public void runToBreakpoint() {
        final RunToBreakpointAction runner = new RunToBreakpointAction();
        runner.setActiveEditor(getMainTracer());
        runner.run();
    }

    /**
     * clean up goes here
     */
    @Override
    public void dispose() {
        treePanel.getTree().dispose();
        popup.dispose();
    }

    public MainTracer getMainTracer() {
        final MultiPageEditorSite site = (MultiPageEditorSite) getEditorSite();
        return (MainTracer) site.getMultiPageEditor();
    }

    /**
     * Get the model
     */
    public TracerModel getModel() {
        return model;
    }

    /**
     * @param model
     *            The model to set.
     */
    public void setModel(final TracerModel model) {
        this.model = model;
    }

    public Element getRootElem() {
        return model.getRootElem();
    }

    /**
     * @return Returns the inputHelper.
     */
    public BizdocInputHelper getInputHelper() {
        return inputHelper;
    }
}
