package at.ssw.coco.ide.features.semanticHighlighting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jface.text.AbstractDocument;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;
import at.ssw.coco.ide.editor.ATGEditor;
import at.ssw.coco.ide.features.semanticHighlighting.SemanticHighlightingManager.HighlightedPosition;
import at.ssw.coco.ide.features.semanticHighlighting.SemanticHighlightingManager.HighlightingStyle;
import at.ssw.coco.ide.features.semanticHighlighting.SemanticHighlightings.DeprecatedMemberHighlighting;
import at.ssw.coco.lib.model.atgmodel.ATGModelListener;

/**
 * managing class for text reconciling
 * 
 * @author Markus Koppensteiner <mkoppensteiner@users.sf.net>
 */
public class SemanticHighlightingReconciler {

    private class ATGListener implements ATGModelListener {

        public void modelChanged() {
            fDocumentSynchronizer.documentChanged();
            refresh();
        }
    }

    /**
	 * Collects positions from the AST.
	 */
    private class PositionCollector extends ASTVisitor {

        private SemanticToken fToken = new SemanticToken();

        private void addPosition(int offset, int length, HighlightingStyle style) {
            boolean isExisting = false;
            for (int i = 0; i < fRemovedPositions.size(); i++) {
                HighlightedPosition position = fRemovedPositions.get(i);
                if (position == null) {
                    continue;
                }
                if (position.isEqual(offset, length, style)) {
                    isExisting = true;
                    fRemovedPositions.set(i, null);
                    fNOfRemovedPositions--;
                    break;
                }
            }
            if (!isExisting) {
                HighlightedPosition position = fJobPresenter.createHighlightedPosition(offset, length, style);
                fAddedPositions.add(position);
            }
        }

        @Override
        public boolean visit(BooleanLiteral node) {
            return visitLiteral(node);
        }

        @Override
        public boolean visit(CharacterLiteral node) {
            return visitLiteral(node);
        }

        @Override
        public boolean visit(ConstructorInvocation node) {
            if (fJobDeprecatedMemberHighlighting != null) {
                IMethodBinding constructorBinding = node.resolveConstructorBinding();
                if (constructorBinding != null && constructorBinding.isDeprecated()) {
                    int offset = node.getStartPosition();
                    int length = 4;
                    if (offset > -1 && length > 0) {
                        offset = fDocumentSynchronizer.mapToATG(offset, node);
                    }
                    if (offset >= 0) {
                        addPosition(offset, length, fJobDeprecatedMemberHighlighting);
                    }
                }
            }
            return true;
        }

        @Override
        public boolean visit(NumberLiteral node) {
            return visitLiteral(node);
        }

        @Override
        public boolean visit(SimpleName node) {
            fToken.update(node);
            for (int i = 0; i < fJobSemanticHighlightings.length; i++) {
                SemanticHighlighting semanticHighlighting = fJobSemanticHighlightings[i];
                if (fJobHighlightings[i].isEnabled() && semanticHighlighting.consumes(fToken)) {
                    int offset = node.getStartPosition();
                    int length = node.getLength();
                    if (offset > -1 && length > 0) {
                        offset = fDocumentSynchronizer.mapToATG(offset, node);
                        if (offset < 0) {
                            break;
                        }
                        addPosition(offset, length, fJobHighlightings[i]);
                        break;
                    }
                }
            }
            fToken.clear();
            return false;
        }

        @Override
        public boolean visit(SuperConstructorInvocation node) {
            if (fJobDeprecatedMemberHighlighting != null) {
                IMethodBinding constructorBinding = node.resolveConstructorBinding();
                if (constructorBinding != null && constructorBinding.isDeprecated()) {
                    int offset = node.getStartPosition();
                    int length = 5;
                    if (offset > -1 && length > 0) {
                        offset = fDocumentSynchronizer.mapToATG(offset, node);
                    }
                    if (offset >= 0) {
                        addPosition(offset, length, fJobDeprecatedMemberHighlighting);
                    }
                }
            }
            return true;
        }

        private boolean visitLiteral(Expression node) {
            fToken.update(node);
            for (int i = 0; i < fJobSemanticHighlightings.length; i++) {
                SemanticHighlighting semanticHighlighting = fJobSemanticHighlightings[i];
                if (fJobHighlightings[i].isEnabled() && semanticHighlighting.consumesLiteral(fToken)) {
                    int offset = node.getStartPosition();
                    int length = node.getLength();
                    if (offset > -1 && length > 0) {
                        offset = fDocumentSynchronizer.mapToATG(offset, node);
                        if (offset < 0) {
                            break;
                        }
                        addPosition(offset, length, fJobHighlightings[i]);
                        break;
                    }
                }
            }
            fToken.clear();
            return false;
        }
    }

    private class TIListener implements ITextInputListener {

        public void inputDocumentAboutToBeChanged(IDocument oldInput, IDocument newInput) {
            synchronized (fJobLock) {
                if (fJob != null) {
                    fJob.cancel();
                    fJob = null;
                }
            }
            if (newInput != null) {
                checkLineDelim(newInput);
            }
        }

        public void inputDocumentChanged(IDocument oldInput, IDocument newInput) {
            fDocumentSynchronizer.inputDocumentChanged();
            refresh();
        }
    }

    private TIListener tiListener = new TIListener();

    private ATGListener atgListener = new ATGListener();

    /** the PositionCollector */
    private PositionCollector fCollector = new PositionCollector();

    /** the ATGEditor */
    private ATGEditor fEditor;

    /** the sourceViewer */
    private ISourceViewer fSourceViewer;

    /** the Presenter */
    private SemanticHighlightingPresenter fPresenter;

    /** all semantic highlightings (types) */
    private SemanticHighlighting[] fSemanticHighlightings;

    /** all semantic highlighting styles */
    private HighlightingStyle[] fHighlightings;

    /** atg/java sync utility */
    private DocumentSynchronizer fDocumentSynchronizer;

    /** positions to add */
    private TreeSet<HighlightedPosition> fAddedPositions;

    /** positions to remove */
    private List<HighlightedPosition> fRemovedPositions = new ArrayList<HighlightedPosition>();

    /**
	 * number of removed positions (fRemovedPositions.size() minus
	 * null_elements)
	 */
    private int fNOfRemovedPositions;

    /** scheduling job */
    private Job fJob;

    /** lock object for fJob */
    private final Object fJobLock = new Object();

    /** lock object for reconciling */
    private final Object fReconcileLock = new Object();

    /** flag that indicates running reconciling */
    private boolean fIsReconciling = false;

    /** background presenter */
    private SemanticHighlightingPresenter fJobPresenter;

    /** background semantic highlightings */
    private SemanticHighlighting[] fJobSemanticHighlightings;

    /** background semantic highlighting styles */
    private HighlightingStyle[] fJobHighlightings;

    /** background DeprecatedMemberHighlighting */
    private HighlightingStyle fJobDeprecatedMemberHighlighting;

    /**
	 * ensures unix line delimiters
	 * 
	 * @param document
	 *            the document to be ensured
	 */
    private void checkLineDelim(IDocument document) {
        if (document.get().contains("\r\n")) {
            String newDoc = document.get().replaceAll("(\r\n)", "\n");
            document.set(newDoc);
        }
        if (document instanceof AbstractDocument) {
            ((AbstractDocument) document).setInitialLineDelimiter("\n");
        }
    }

    /**
	 * 
	 * @param node
	 *            starting node
	 * @return an array containing that node
	 */
    private ASTNode[] getNodeArray(ASTNode node) {
        return new ASTNode[] { node };
    }

    public DocumentSynchronizer getSyncer() {
        return fDocumentSynchronizer;
    }

    /**
	 * installs the Reconciler
	 * 
	 * @param editor
	 *            the used ATGEditor
	 * @param sourceViewer
	 *            the used sourceViewer
	 * @param presenter
	 *            the used presenter
	 * @param semanticHighlightings
	 *            all used semantic highlighting types
	 * @param highlightings
	 *            styles for the highlighting types
	 */
    public void install(ATGEditor editor, ISourceViewer sourceViewer, SemanticHighlightingPresenter presenter, SemanticHighlighting[] semanticHighlightings, HighlightingStyle[] highlightings) {
        fPresenter = presenter;
        fSemanticHighlightings = semanticHighlightings;
        fHighlightings = highlightings;
        fEditor = editor;
        fSourceViewer = sourceViewer;
        checkLineDelim(fSourceViewer.getDocument());
        fSourceViewer.addTextInputListener(tiListener);
        fEditor.getATGModelProvider().addModelListener(atgListener);
        fDocumentSynchronizer = new DocumentSynchronizer(fEditor, fSourceViewer);
        Comparator<HighlightedPosition> c = new Comparator<HighlightedPosition>() {

            public int compare(HighlightedPosition h1, HighlightedPosition h2) {
                return h1.offset - h2.offset;
            }
        };
        fAddedPositions = new TreeSet<HighlightedPosition>(c);
        scheduleJob();
    }

    /**
	 * invokes reconciling
	 * 
	 * @param ast
	 *            the abstract syntax tree parsed from the java file
	 * @param progressMonitor
	 *            the progress monitor
	 */
    public void reconcile(CompilationUnit ast, IProgressMonitor progressMonitor) {
        synchronized (fReconcileLock) {
            if (fIsReconciling) {
                return;
            } else {
                fIsReconciling = true;
            }
        }
        fJobPresenter = fPresenter;
        fJobSemanticHighlightings = fSemanticHighlightings;
        fJobHighlightings = fHighlightings;
        try {
            if (fJobPresenter == null || fJobSemanticHighlightings == null || fJobHighlightings == null) {
                return;
            }
            fJobPresenter.setCanceled(progressMonitor.isCanceled());
            if (ast == null || fJobPresenter.isCanceled()) {
                return;
            }
            ASTNode[] subtrees = getNodeArray(ast);
            if (subtrees.length == 0) {
                return;
            }
            startReconcilingPositions();
            if (!fJobPresenter.isCanceled()) {
                fJobDeprecatedMemberHighlighting = null;
                for (int i = 0, n = fJobSemanticHighlightings.length; i < n; i++) {
                    SemanticHighlighting semanticHighlighting = fJobSemanticHighlightings[i];
                    if (fJobHighlightings[i].isEnabled() && semanticHighlighting instanceof DeprecatedMemberHighlighting) {
                        fJobDeprecatedMemberHighlighting = fJobHighlightings[i];
                        break;
                    }
                }
                reconcilePositions(subtrees);
            }
            TextPresentation textPresentation = null;
            if (!fJobPresenter.isCanceled()) {
                textPresentation = fJobPresenter.createPresentation(fAddedPositions, fRemovedPositions);
            }
            if (!fJobPresenter.isCanceled()) {
                updatePresentation(textPresentation, fAddedPositions, fRemovedPositions);
            }
            stopReconcilingPositions();
        } finally {
            fJobPresenter = null;
            fJobSemanticHighlightings = null;
            fJobHighlightings = null;
            synchronized (fReconcileLock) {
                fIsReconciling = false;
            }
        }
    }

    /**
	 * reconciles all positions under subtrees
	 * 
	 * @param subtrees
	 *            an array containing the root node
	 */
    private void reconcilePositions(ASTNode[] subtrees) {
        for (int i = 0, n = subtrees.length; i < n; i++) {
            subtrees[i].accept(fCollector);
        }
        List<HighlightedPosition> oldPositions = fRemovedPositions;
        List<HighlightedPosition> newPositions = new ArrayList<HighlightedPosition>(fNOfRemovedPositions);
        for (int i = 0; i < oldPositions.size(); i++) {
            HighlightedPosition current = oldPositions.get(i);
            if (current != null) {
                newPositions.add(current);
            }
        }
        fRemovedPositions = newPositions;
    }

    /**
	 * updates the text presentation
	 */
    public void refresh() {
        scheduleJob();
    }

    /**
	 * updates the text presentation
	 */
    private void scheduleJob() {
        final DocumentSynchronizer syncer = fDocumentSynchronizer;
        synchronized (fJobLock) {
            final Job oldJob = fJob;
            if (fJob != null) {
                fJob.cancel();
                fJob = null;
            }
            fJob = new Job("SemanticHighlighting_job") {

                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    if (oldJob != null) {
                        try {
                            oldJob.join();
                        } catch (InterruptedException e) {
                            return Status.CANCEL_STATUS;
                        }
                    }
                    if (monitor.isCanceled()) {
                        return Status.CANCEL_STATUS;
                    }
                    CompilationUnit ast = syncer.getAst();
                    reconcile(ast, monitor);
                    synchronized (fJobLock) {
                        if (fJob == this) {
                            fJob = null;
                        }
                    }
                    return Status.OK_STATUS;
                }
            };
            fJob.setSystem(true);
            fJob.setPriority(Job.DECORATE);
            fJob.schedule();
        }
    }

    /**
	 * prepares reconciling
	 */
    private void startReconcilingPositions() {
        fJobPresenter.addAllPositions(fRemovedPositions);
        fNOfRemovedPositions = fRemovedPositions.size();
    }

    /**
	 * performs clean up after reconciling
	 */
    private void stopReconcilingPositions() {
        fRemovedPositions.clear();
        fNOfRemovedPositions = 0;
        fAddedPositions.clear();
    }

    /**
	 * uninstalls the Reconciler
	 */
    public void uninstall() {
        if (fPresenter != null) {
            fPresenter.setCanceled(true);
        }
        if (fEditor != null) {
            fSourceViewer.removeTextInputListener(tiListener);
            fEditor.getATGModelProvider().removeModelListener(atgListener);
            fEditor = null;
        }
        fSourceViewer = null;
        fSemanticHighlightings = null;
        fHighlightings = null;
        fPresenter = null;
        fDocumentSynchronizer = null;
    }

    /**
	 * executes text presentation update asynchronously
	 * 
	 * @param textPresentation
	 *            repair description
	 * @param addedPositions
	 *            positions to add
	 * @param removedPositions
	 *            positions to remove
	 */
    private void updatePresentation(TextPresentation textPresentation, TreeSet<HighlightedPosition> addedPositions, List<HighlightedPosition> removedPositions) {
        Runnable runnable = fJobPresenter.createUpdateRunnable(textPresentation, addedPositions, removedPositions);
        if (runnable == null || fEditor == null) {
            return;
        }
        IWorkbenchPartSite site = fEditor.getSite();
        if (site == null) {
            return;
        }
        Shell shell = site.getShell();
        if (shell == null || shell.isDisposed()) {
            return;
        }
        Display display = shell.getDisplay();
        if (display == null || display.isDisposed()) {
            return;
        }
        display.asyncExec(runnable);
    }
}
