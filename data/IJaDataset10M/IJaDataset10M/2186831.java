package com.tresys.slide.plugin.editors;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.commands.operations.TriggeredOperations;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.ITextViewerExtension6;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.IUndoManagerExtension;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import com.tresys.slide.plugin.SLIDEPlugin;
import com.tresys.slide.plugin.preferences.PreferenceConstants;
import com.tresys.slide.plugin.text.Pair;
import com.tresys.slide.plugin.text.PairMatcher;

public abstract class BaseTextEditor extends AbstractDecoratedTextEditor implements ISLIDEEditor {

    protected static final Pair paranBrackets = new Pair('(', ')');

    protected static final Pair curlyBrackets = new Pair('{', '}');

    protected static final Pair angleBrackets = new Pair('<', '>');

    /** The editor's bracket matcher */
    protected PairMatcher fBracketMatcher;

    protected IProject proj;

    public BaseTextEditor() {
        super();
        setPreferenceStore(SLIDEPlugin.getDefault().getPreferenceStore());
        fBracketMatcher = new PairMatcher(new Pair[] { paranBrackets, curlyBrackets, angleBrackets });
    }

    protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
        super.configureSourceViewerDecorationSupport(support);
        support.setCharacterPairMatcher(fBracketMatcher);
        support.setMatchingCharacterPainterPreferenceKeys(PreferenceConstants.EDITOR_MATCHING_BRACKETS, PreferenceConstants.EDITOR_MATCHING_BRACKETS_COLOR);
    }

    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
    }

    protected void initializeEditor() {
        super.initializeEditor();
        setEditorContextMenuId("#BaseEditor");
    }

    protected void createActions() {
        super.createActions();
        Action action = new ContentAssistAction(ResourceBundleController.getResourceBundle(), "ContentAssistProposal", this);
        action.setText("Context Proposal");
        action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
        setAction("ContentAssistProposal", action);
        markAsStateDependentAction("ContentAssistProposal", true);
    }

    public void editorContextMenuAboutToShow(IMenuManager menu) {
        super.editorContextMenuAboutToShow(menu);
        addGroup(menu, ITextEditorActionConstants.GROUP_ADD, "SLIDE");
        addAction(menu, ITextEditorActionConstants.GROUP_FIND, "ContentAssistProposal");
        addAction(menu, "SLIDE", "WRAP_IN_IFDEF");
        addAction(menu, "SLIDE", "ADDDOMAIN");
    }

    public void AddMouseListener(MouseListener listener) {
        if (getSourceViewer() != null && getSourceViewer().getTextWidget() != null) getSourceViewer().getTextWidget().addMouseListener(listener);
    }

    public void RemoveMouseListener(MouseListener listener) {
        if (getSourceViewer() != null && getSourceViewer().getTextWidget() != null) getSourceViewer().getTextWidget().removeMouseListener(listener);
    }

    public void AddKeyListener(KeyListener listener) {
        if (getSourceViewer() != null && getSourceViewer().getTextWidget() != null) getSourceViewer().getTextWidget().addKeyListener(listener);
    }

    public void RemoveKeyListener(KeyListener listener) {
        if (getSourceViewer() != null && getSourceViewer().getTextWidget() != null) getSourceViewer().getTextWidget().removeKeyListener(listener);
    }

    public IProject getProject() {
        return proj;
    }

    public void doQuietSave(IProgressMonitor progressMonitor) {
        updateState(getEditorInput());
        validateState(getEditorInput());
        performQuietSave(false, progressMonitor);
    }

    protected void performQuietSave(boolean overwrite, IProgressMonitor progressMonitor) {
        IDocumentProvider provider = getDocumentProvider();
        if (provider == null) return;
        try {
            provider.aboutToChange(getEditorInput());
            IEditorInput input = getEditorInput();
            provider.saveDocument(progressMonitor, input, getDocumentProvider().getDocument(input), overwrite);
        } catch (CoreException x) {
            IStatus status = x.getStatus();
            if (status == null || status.getSeverity() != IStatus.CANCEL) handleExceptionOnSave(x, progressMonitor);
        }
    }

    /**
	 * Add an operation to the undo list
	 * @param i_operation - operation to be performed
	 */
    public void addUndoableOperation(IUndoableOperation i_operation) {
        ISourceViewer srcViewer = this.getSourceViewer();
        IUndoManager undoManager = null;
        if (srcViewer instanceof ITextViewerExtension6) undoManager = ((ITextViewerExtension6) srcViewer).getUndoManager();
        IOperationHistory operationHistory = OperationHistoryFactory.getOperationHistory();
        if (undoManager != null) i_operation.addContext(((IUndoManagerExtension) undoManager).getUndoContext());
        TriggeredOperations operGroup = new TriggeredOperations(i_operation, operationHistory);
        try {
            operationHistory.execute(operGroup, null, null);
        } catch (ExecutionException ee) {
        }
    }

    public String getCursorPosition() {
        return super.getCursorPosition();
    }
}
