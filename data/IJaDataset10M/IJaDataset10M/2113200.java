package org.xaware.ide.xadev.gui.view;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.xaware.ide.xadev.gui.actions.FileExportExecuteResultsAction;
import org.xaware.ide.xadev.gui.actions.RawViewAction;
import org.xaware.ide.xadev.gui.editor.XAInternalFrame;
import org.xaware.ide.xadev.gui.model.IResultsViewerManagerListener;
import org.xaware.ide.xadev.gui.model.ResultsEvent;
import org.xaware.ide.xadev.gui.model.ResultsManager;
import org.xaware.ide.xadev.gui.xmleditor.xaware.XMLResultsConfiguration;
import org.xaware.rvpublisher.editor.IResultsViewEditor;
import org.xaware.rvpublisher.model.BaseTracerModel;
import org.xaware.rvpublisher.model.ITracerListener;
import org.xaware.rvpublisher.model.TracerEvent;
import org.xaware.rvpublisher.model.TracerManager;
import org.xaware.shared.util.UserPrefs;
import org.xaware.shared.util.Utils;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * The Results pane appears along the bottom of the default XA-Designer window.
 * All It displays execution results from both Designer and Tracer
 */
public class ResultsView extends ViewPart implements ITracerListener, IResultsView {

    public static final String ID = "org.xaware.ide.plugin.view.ResultsView";

    private static XAwareLogger lf = XAwareLogger.getXAwareLogger("org.xaware.ide.xadev.gui.view.ResultsView");

    /** Dynamic Help Context ID */
    private static ResultsViewContextProvider contextProvider = null;

    private static final String DEFAULT_RSLT_STR = "";

    protected static FileExportExecuteResultsAction exportAction = null;

    protected static RawViewAction formatAction = null;

    protected SourceViewer viewer = null;

    private ResultsManager manager = null;

    private ISelectionListener pageSelectionListener;

    private ResultsListener resultsListener;

    private boolean m_FormatOutput = true;

    protected IEditorPart activeEditor = null;

    private XMLOutputter m_xmloutputter = UserPrefs.getInstance().getResultsViewerOutputter();

    /**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
    public ResultsView() {
        super();
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 * 
	 * @param aParentComp
	 *            Composite
	 */
    @Override
    public void createPartControl(final Composite aParentComp) {
        try {
            aParentComp.setLayout(new FillLayout());
            createTextViewer(aParentComp);
            hookPageSelection();
            hookResultsListener();
            createToolbarButtons();
            tracerCreatePartControl(aParentComp);
            pageSelectionChanged(null, null);
        } catch (final Exception e) {
            lf.printStackTrace(e);
        }
    }

    private void createToolbarButtons() {
        try {
            if (exportAction == null) {
                exportAction = new FileExportExecuteResultsAction();
            }
            getViewSite().getActionBars().getToolBarManager().add(exportAction);
            exportAction.setImageDescriptor(org.xaware.ide.shared.UserPrefs.getImageDescriptorIconFor(org.xaware.ide.shared.UserPrefs.SAVE_RESULTS_VIEW));
            exportAction.setDisabledImageDescriptor(org.xaware.ide.shared.UserPrefs.getImageDescriptorIconFor(org.xaware.ide.shared.UserPrefs.SAVE_OFF_RESULTS_VIEW));
            if (formatAction == null) {
                formatAction = new RawViewAction();
            }
            formatAction.setResultsView(this);
            getViewSite().getActionBars().getToolBarManager().add(formatAction);
            formatAction.setImageDescriptor(org.xaware.ide.shared.UserPrefs.getImageDescriptorIconFor(org.xaware.ide.shared.UserPrefs.FORMAT_RESULTS_VIEW));
            formatAction.setDisabledImageDescriptor(org.xaware.ide.shared.UserPrefs.getImageDescriptorIconFor(org.xaware.ide.shared.UserPrefs.FORMAT_OFF_RESULTS_VIEW));
            viewer.setDocument(new Document(DEFAULT_RSLT_STR));
            exportAction.setEnabled(false);
            formatAction.setEnabled(false);
        } catch (final RuntimeException e) {
            lf.printStackTrace(e);
        }
    }

    public void tracerCreatePartControl(final Composite parent) {
        final TracerManager manager = TracerManager.getManager();
        manager.addTracerManagerListener(this);
        final BaseTracerModel model = manager.getModel();
        if (model != null) {
            final Element el = model.getRootElem();
            if (el != null) {
                setInput(el);
            } else {
                final Element elem = new Element("Results");
                elem.setText("<Results>Not Yet Executed</Results>");
                setInput(elem);
            }
        }
    }

    private void hookResultsListener() {
        manager = ResultsManager.getManager();
        resultsListener = new ResultsListener();
        manager.addResultsManagerListener(resultsListener);
    }

    private void createTextViewer(final Composite parent) {
        viewer = new SourceViewer(parent, new CompositeRuler(), SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        setInput(null);
        final TextPresentation style = new TextPresentation();
        style.addStyleRange(new StyleRange(0, DEFAULT_RSLT_STR.length(), null, null, SWT.BOLD));
        viewer.changeTextPresentation(style, true);
        viewer.setEditable(false);
        final XMLResultsConfiguration xmlConfiguration = new XMLResultsConfiguration();
        viewer.configure(xmlConfiguration);
        final StyledText resultsAreaTxt = viewer.getTextWidget();
        addCopyPopupMenu(resultsAreaTxt);
    }

    /**
	 * Create a popup menu on the SytledText area pass as a parameter with only
	 * copy enabled.
	 * 
	 * @param styledTxtArea
	 */
    protected void addCopyPopupMenu(final StyledText styledTxtArea) {
        final Menu txtAreaMenu = new Menu(styledTxtArea);
        final MenuItem cutitem = new MenuItem(txtAreaMenu, SWT.PUSH);
        cutitem.setText("Cut");
        cutitem.setEnabled(false);
        final MenuItem copyItem = new MenuItem(txtAreaMenu, SWT.PUSH);
        copyItem.setText("Copy");
        copyItem.setAccelerator(SWT.MOD1 + 'C');
        copyItem.setEnabled(true);
        copyItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent event) {
                styledTxtArea.copy();
            }
        });
        final MenuItem pasteItem = new MenuItem(txtAreaMenu, SWT.PUSH);
        pasteItem.setText("Paste");
        pasteItem.setEnabled(false);
        styledTxtArea.setMenu(txtAreaMenu);
    }

    /**
	 * Set the input Element for display
	 * 
	 * @param root
	 */
    public void setInput(final Element root) {
        String xml = null;
        if (root != null) {
            if (m_FormatOutput) {
                final Element elem = (Element) root.clone();
                Utils.removeFormatting(elem);
                xml = m_xmloutputter.outputString(elem);
            } else {
                final XMLOutputter xmlOut = new XMLOutputter();
                xml = xmlOut.outputString(root);
            }
        } else {
            xml = null;
        }
        if (xml != null) {
            final Document doc = new Document(xml);
            viewer.setDocument(doc);
        } else {
            viewer.setDocument(new Document(DEFAULT_RSLT_STR));
        }
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    @Override
    public void setFocus() {
        final IWorkbenchPart part = getSite().getPage().getActiveEditor();
        initViewer(part);
    }

    private void hookPageSelection() {
        pageSelectionListener = new ISelectionListener() {

            public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
                pageSelectionChanged(part, selection);
            }
        };
        getSite().getPage().addPostSelectionListener(pageSelectionListener);
    }

    protected void pageSelectionChanged(final IWorkbenchPart part, final ISelection selection) {
        final IEditorPart editor = getSite().getPage().getActiveEditor();
        if (editor != null && editor != activeEditor) {
            activeEditor = editor;
            initViewer(editor);
        }
    }

    private void initViewer(final IWorkbenchPart part) {
        if (part instanceof IEditorPart) {
            activeEditor = (IEditorPart) part;
            formatAction.setCurrentEditor(activeEditor);
        }
        if (part instanceof XAInternalFrame) {
            final XAInternalFrame currentFrame = (XAInternalFrame) part;
            if ((currentFrame.getType() == XAInternalFrame.BIZ_DOC) || (currentFrame.getType() == XAInternalFrame.BIZ_COMP)) {
                exportAction.setEnabled(true);
                formatAction.setEnabled(true);
                showResultsFromFrame();
            } else {
                viewer.setDocument(new Document(DEFAULT_RSLT_STR));
                exportAction.setEnabled(false);
                formatAction.setEnabled(false);
            }
        } else if (part instanceof IResultsViewEditor) {
            exportAction.setEnabled(true);
            formatAction.setEnabled(true);
            final Element elem = ((IResultsViewEditor) part).getExecuteResults();
            setInput(elem);
        } else {
            viewer.setDocument(new Document(DEFAULT_RSLT_STR));
            exportAction.setEnabled(false);
            formatAction.setEnabled(false);
        }
    }

    private void showResultsFromFrame() {
        final Element elem = ((XAInternalFrame) activeEditor).getExecuteResults();
        if (elem != null) {
            setInput(elem);
        }
    }

    public class ResultsListener implements IResultsViewerManagerListener {

        public void dataChanged(final ResultsEvent event) {
            if (activeEditor == null) {
                activeEditor = event.getEditor();
            }
            if (activeEditor == event.getEditor()) {
                try {
                    if ((((XAInternalFrame) activeEditor).getType() == XAInternalFrame.BIZ_DOC) || (((XAInternalFrame) activeEditor).getType() == XAInternalFrame.BIZ_COMP)) {
                        setInput(event.getResultElement());
                    } else {
                        viewer.setDocument(new Document(DEFAULT_RSLT_STR));
                        exportAction.setEnabled(false);
                        formatAction.setEnabled(false);
                    }
                } finally {
                    viewer.refresh();
                }
            }
        }

        public void formatOutput(final boolean formatOutput) {
            setFormatOutput(formatOutput);
        }
    }

    public void setFormatOutput(final boolean formatOutput) {
        m_FormatOutput = formatOutput;
        formatAction.setFormat(formatOutput);
    }

    /**
	 * Disposes of this view. Clients should not call this method; it is called
	 * by the Eclipse infrastructure.
	 * <p>
	 * This is the last method called on the view. At this point the part
	 * controls (if they were ever created) have been disposed as part of an SWT
	 * composite. There is no guarantee that createPartControl() has been
	 * called, so the part controls may never have been created.
	 * <p>
	 * Within this method a part may release any resources, fonts, images,
	 * etc.&nbsp; held by this part. It is also very important to deregister all
	 * listeners from the workbench.
	 */
    @Override
    public void dispose() {
        super.dispose();
        lf = null;
        exportAction = null;
        formatAction = null;
        viewer = null;
        manager.removeResultsManagerListener(resultsListener);
        resultsListener = null;
        getSite().getPage().removePostSelectionListener(pageSelectionListener);
        resultsListener = null;
        activeEditor = null;
        m_xmloutputter = null;
        final TracerManager manager = TracerManager.getManager();
        manager.removeTracerManagerListener(this);
    }

    /**
	 * Update when the model has changed
	 */
    public void dataChanged(final TracerEvent evt) {
        final IEditorPart part = getSite().getPage().getActiveEditor();
        activeEditor = part;
        formatAction.setCurrentEditor(activeEditor);
        if (activeEditor instanceof XAInternalFrame) {
            setInput(((XAInternalFrame) activeEditor).getExecuteResults());
        } else if (activeEditor instanceof IResultsViewEditor) {
            final BaseTracerModel model = evt.getModel();
            if (model != null) {
                final Element el = model.getRootElem();
                setInput(el);
            }
        } else {
            setInput(null);
        }
    }

    /**
	 * access to the document's text
	 * 
	 * @return
	 */
    public String getResults() {
        return viewer.getDocument().get();
    }

    /**
	 * Get Adapter method providing access to the dynamic help's context
	 * provider for the XAware Results viewer.
	 */
    @Override
    public Object getAdapter(final Class adapter) {
        if (adapter.equals(IContextProvider.class)) {
            if (contextProvider == null) {
                contextProvider = new ResultsViewContextProvider();
            }
            return contextProvider;
        }
        return super.getAdapter(adapter);
    }

    /**
	 * Context provider for the XAware Results view
	 */
    protected class ResultsViewContextProvider implements IContextProvider {

        private static final String contextIdText = "org.xaware.help.xaware_execute_results_context";

        private final IContext context = HelpSystem.getContext(contextIdText);

        public IContext getContext(final Object target) {
            return context;
        }

        public int getContextChangeMask() {
            return IContextProvider.NONE;
        }

        public String getSearchExpression(final Object target) {
            return null;
        }
    }
}
