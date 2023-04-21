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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.xaware.ide.xadev.gui.editor.XAInternalFrame;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * The Validation Results pane appears along the bottom of the default XA-Designer window. All It displays validation
 * results of Designer.
 */
public class ValidationResultsView extends ViewPart {

    /** Holds Validation Results view ID */
    public static final String ID = "org.xaware.ide.plugin.view.ValidationResultsView";

    /** XAware logger for ValidationResultsView */
    private static XAwareLogger logger = XAwareLogger.getXAwareLogger(ValidationResultsView.class.getName());

    /** Dynamic Help Context ID */
    private static ValidationResultsViewContextProvider contextProvider = null;

    /** Default String */
    private static final String DEFAULT_RSLT_STR = "";

    /** Source Viewer instance */
    protected SourceViewer viewer = null;

    /** Selection Listener instance */
    private ISelectionListener pageSelectionListener;

    /** Active Editor instance */
    protected IEditorPart activeEditor = null;

    /**
     * The content provider class is responsible for providing objects to the view. It can wrap existing objects in
     * adapters or simply return objects as-is. These objects may be sensitive to the current input of the view, or
     * ignore it and always show the same content (like Task List, for example).
     */
    public ValidationResultsView() {
        super();
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
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
            pageSelectionChanged(null, null);
        } catch (final Exception e) {
            logger.severe("Unable to construct GUI for Validation Results View:" + e.getMessage(), e);
        }
    }

    /**
     * Cretaes a text viewer for Validation Results view.
     * 
     * @param parent
     *            composite.
     */
    private void createTextViewer(final Composite parent) {
        viewer = new SourceViewer(parent, new CompositeRuler(), SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        final TextPresentation style = new TextPresentation();
        style.addStyleRange(new StyleRange(0, DEFAULT_RSLT_STR.length(), null, null, SWT.BOLD));
        viewer.changeTextPresentation(style, true);
        viewer.setEditable(false);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        final IWorkbenchPart part = getSite().getPage().getActiveEditor();
        initViewer(part);
    }

    /**
     * Hooks the selection listener with the page so that whenever the new page is selected the Validation Results view
     * is re-initialized.
     * 
     */
    private void hookPageSelection() {
        pageSelectionListener = new ISelectionListener() {

            public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
                pageSelectionChanged(part, selection);
            }
        };
        getSite().getPage().addPostSelectionListener(pageSelectionListener);
    }

    /**
     * Initializes the Validation Results view whenever the new page is selected.
     * 
     * @param part
     *            the workbench part containing the selection.
     * @param selection
     *            the current selection.
     */
    protected void pageSelectionChanged(final IWorkbenchPart part, final ISelection selection) {
        final IEditorPart editor = getSite().getPage().getActiveEditor();
        if (editor != null && editor != activeEditor) {
            activeEditor = editor;
            initViewer(editor);
        }
    }

    /**
     * Initializes the Validation Results view.
     * 
     * @param part
     *            the workbench part containing the selection.
     */
    private void initViewer(final IWorkbenchPart part) {
        if (part instanceof IEditorPart) {
            activeEditor = (IEditorPart) part;
        }
        if (part instanceof XAInternalFrame) {
            final XAInternalFrame currentFrame = (XAInternalFrame) part;
            if ((currentFrame.getType() == XAInternalFrame.BIZ_DOC) || (currentFrame.getType() == XAInternalFrame.BIZ_COMP)) {
                showValidationResultsFromFrame();
            } else {
                viewer.setDocument(new Document(DEFAULT_RSLT_STR));
            }
        } else {
            viewer.setDocument(new Document(DEFAULT_RSLT_STR));
        }
    }

    /**
     * Sets the validation results to the view.
     */
    private void showValidationResultsFromFrame() {
        String validationResults = ((XAInternalFrame) activeEditor).getValidationResults();
        viewer.setDocument(new Document(validationResults));
    }

    /**
     * Disposes of this view. Clients should not call this method; it is called by the Eclipse infrastructure.
     * <p>
     * This is the last method called on the view. At this point the part controls (if they were ever created) have been
     * disposed as part of an SWT composite. There is no guarantee that createPartControl() has been called, so the part
     * controls may never have been created.
     * <p>
     * Within this method a part may release any resources, fonts, images, etc.&nbsp; held by this part. It is also very
     * important to deregister all listeners from the workbench.
     */
    @Override
    public void dispose() {
        super.dispose();
        logger = null;
        viewer = null;
        getSite().getPage().removePostSelectionListener(pageSelectionListener);
        activeEditor = null;
    }

    /**
     * access to the document's text
     * 
     * @return
     */
    public String getValidationResults() {
        return viewer.getDocument().get();
    }

    /**
     * Get Adapter method providing access to the dynamic help's context provider for the XAware Results viewer.
     */
    @Override
    public Object getAdapter(final Class adapter) {
        if (adapter.equals(IContextProvider.class)) {
            if (contextProvider == null) {
                contextProvider = new ValidationResultsViewContextProvider();
            }
            return contextProvider;
        }
        return super.getAdapter(adapter);
    }

    /**
     * Context provider for the XAware validation Results view
     */
    protected class ValidationResultsViewContextProvider implements IContextProvider {

        /** Context ID for Validation Results View. */
        private static final String contextIdText = "org.xaware.help.xaware_validation_results_context";

        /** Content instance for Validation Results View. */
        private final IContext context = HelpSystem.getContext(contextIdText);

        /**
         * (non-Javadoc)
         * 
         * @see org.eclipse.help.IContextProvider#getContext(java.lang.Object)
         */
        public IContext getContext(final Object target) {
            return context;
        }

        /**
         * (non-Javadoc)
         * 
         * @see org.eclipse.help.IContextProvider#getContextChangeMask()
         */
        public int getContextChangeMask() {
            return IContextProvider.NONE;
        }

        /**
         * (non-Javadoc)
         * 
         * @see org.eclipse.help.IContextProvider#getSearchExpression(java.lang.Object)
         */
        public String getSearchExpression(final Object target) {
            return null;
        }
    }
}
