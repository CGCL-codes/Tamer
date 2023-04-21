package org.radrails.db.internal.ui.editors.sql;

import java.util.ResourceBundle;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.ITextEditorHelpContextIds;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.AddTaskAction;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.ResourceAction;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.radrails.db.internal.ui.DbUIPlugin;
import org.radrails.rails.internal.ui.editors.IManagedSourceViewerConfiguration;

/**
 * This class is responsible for configuring the SQL editor.
 *  
 */
public class SQLEditor extends AbstractDecoratedTextEditor {

    private class DefineFoldingRegionAction extends TextEditorAction {

        public DefineFoldingRegionAction(ResourceBundle bundle, String prefix, ITextEditor editor) {
            super(bundle, prefix, editor);
        }

        private IAnnotationModel getAnnotationModel(ITextEditor editor) {
            return (IAnnotationModel) editor.getAdapter(ProjectionAnnotationModel.class);
        }

        public void run() {
            ITextEditor editor = getTextEditor();
            ISelection selection = editor.getSelectionProvider().getSelection();
            if (selection instanceof ITextSelection) {
                ITextSelection textSelection = (ITextSelection) selection;
                if (!textSelection.isEmpty()) {
                    IAnnotationModel model = getAnnotationModel(editor);
                    if (model != null) {
                        int start = textSelection.getStartLine();
                        int end = textSelection.getEndLine();
                        try {
                            IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
                            int offset = document.getLineOffset(start);
                            int endOffset = document.getLineOffset(end + 1);
                            Position position = new Position(offset, endOffset - offset);
                            model.addAnnotation(new ProjectionAnnotation(), position);
                        } catch (BadLocationException x) {
                        }
                    }
                }
            }
        }
    }

    private ProjectionSupport fProjectionSupport;

    /**
   * Constructor for SQLEditor. Intialization takes place in the constructor of
   * SQLEditor using setDocumentProvider and setRangeIndicator.
   */
    public SQLEditor() {
        super();
        setRangeIndicator(new DefaultRangeIndicator());
        setPreferenceStore(DbUIPlugin.getInstance().getPreferenceStore());
    }

    protected void initializeEditor() {
        super.initializeEditor();
        setSourceViewerConfiguration(new SQLEditorSourceViewerConfiguration());
    }

    /**
   * Clean up the font colors used in the syntax hilighting
   *  
   */
    public void disposeColorProvider() {
        DbUIPlugin.getInstance().disposeColorProvider();
        super.dispose();
    }

    /**
   * Method to install the editor actions.
   * 
   * <p>Changes required in V 2.1. Shortcut keys on global actions must be
   * explicitly set. Content Assist and Context Information Shortcut keys must
   * be set to the key defintion ID's.
   * 
   * @see org.eclipse.ui.texteditor.AbstractTextEditor#createActions()    
   */
    protected void createActions() {
        super.createActions();
        ResourceBundle bundle = DbUIPlugin.getInstance().getResourceBundle();
        IAction a = new TextOperationAction(bundle, "ContentAssistProposal.", this, ISourceViewer.CONTENTASSIST_PROPOSALS);
        a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
        setAction("ContentAssistProposal", a);
        a = new TextOperationAction(bundle, "ContentAssistTip.", this, ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION);
        a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION);
        setAction("ContentAssistTip", a);
        a = new TextOperationAction(bundle, "ContentFormatProposal.", this, ISourceViewer.FORMAT);
        setAction("ContentFormatProposal", a);
        ResourceAction ra = new AddTaskAction(bundle, "AddTask.", this);
        ra.setHelpContextId(ITextEditorHelpContextIds.ADD_TASK_ACTION);
        ra.setActionDefinitionId(ITextEditorActionDefinitionIds.ADD_TASK);
        setAction(IDEActionFactory.ADD_TASK.getId(), ra);
        a = new DefineFoldingRegionAction(bundle, "DefineFoldingRegion.", this);
        setAction("DefineFoldingRegion", a);
        a = new SQLFormatAction(bundle, "Format.", this);
        a.setActionDefinitionId("org.radrails.db.ui.editors.format");
        setAction("Format", a);
    }

    /**
   * @see AbstractTextEditor#editorContextMenuAboutToShow(org.eclipse.jface.action.IMenuManager)
   */
    public void editorContextMenuAboutToShow(IMenuManager menu) {
        super.editorContextMenuAboutToShow(menu);
        addAction(menu, "ContentAssistProposal");
        addAction(menu, "ContentAssistTip");
        addAction(menu, "ContentFormatProposal");
        addAction(menu, "DefineFoldingRegion");
        addAction(menu, "Format");
    }

    protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
        fAnnotationAccess = createAnnotationAccess();
        fOverviewRuler = createOverviewRuler(getSharedColors());
        ISourceViewer viewer = new SQLEditorSourceViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
        getSourceViewerDecorationSupport(viewer);
        return viewer;
    }

    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
        fProjectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
        fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error");
        fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.warning");
        fProjectionSupport.install();
        viewer.doOperation(ProjectionViewer.TOGGLE);
    }

    protected void adjustHighlightRange(int offset, int length) {
        ISourceViewer viewer = getSourceViewer();
        if (viewer instanceof ITextViewerExtension5) {
            ITextViewerExtension5 extension = (ITextViewerExtension5) viewer;
            extension.exposeModelRange(new Region(offset, length));
        }
    }

    public Object getAdapter(Class required) {
        if (fProjectionSupport != null) {
            Object adapter = fProjectionSupport.getAdapter(getSourceViewer(), required);
            if (adapter != null) return adapter;
        }
        return super.getAdapter(required);
    }

    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setDocumentProvider(new TextFileDocumentProvider());
        super.init(site, input);
    }

    protected boolean affectsTextPresentation(PropertyChangeEvent event) {
        return ((IManagedSourceViewerConfiguration) getSourceViewerConfiguration()).affectsTextPresentation(event) || super.affectsTextPresentation(event);
    }

    protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
        ((IManagedSourceViewerConfiguration) getSourceViewerConfiguration()).handlePropertyChangeEvent(event);
        super.handlePreferenceStoreChanged(event);
    }

    public void setCaretPosition(CaretPosition pos) {
        try {
            int lineOffset = this.getSourceViewer().getDocument().getLineOffset(pos.line);
            this.selectAndReveal(lineOffset + pos.column, 0);
        } catch (BadLocationException e) {
        }
    }

    public class CaretPosition {

        protected CaretPosition(int line, int column) {
            this.line = line;
            this.column = column;
        }

        protected int getColumn() {
            return column;
        }

        protected int getLine() {
            return line;
        }

        private int line;

        private int column;
    }

    public CaretPosition getCaretPosition() {
        StyledText styledText = this.getSourceViewer().getTextWidget();
        int caret = widgetOffset2ModelOffset(getSourceViewer(), styledText.getCaretOffset());
        IDocument document = getSourceViewer().getDocument();
        try {
            int line = document.getLineOfOffset(caret);
            int lineOffset = document.getLineOffset(line);
            return new CaretPosition(line, caret - lineOffset);
        } catch (BadLocationException e) {
            return new CaretPosition(0, 0);
        }
    }
}
