package org.eclipse.ui.internal.texteditor.quickdiff;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.DocumentRewriteSessionEvent;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentRewriteSessionListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ISynchronizable;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.IAnnotationModelListenerExtension;
import org.eclipse.jface.text.source.ILineDiffInfo;
import org.eclipse.jface.text.source.ILineDiffer;
import org.eclipse.jface.text.source.ILineDifferExtension;
import org.eclipse.jface.text.source.ILineDifferExtension2;
import org.eclipse.jface.text.source.ILineRange;
import org.eclipse.jface.text.source.LineRange;
import org.eclipse.ui.internal.texteditor.NLSUtility;
import org.eclipse.ui.internal.texteditor.TextEditorPlugin;
import org.eclipse.ui.internal.texteditor.quickdiff.compare.equivalence.DJBHashFunction;
import org.eclipse.ui.internal.texteditor.quickdiff.compare.equivalence.DocEquivalenceComparator;
import org.eclipse.ui.internal.texteditor.quickdiff.compare.equivalence.DocumentEquivalenceClass;
import org.eclipse.ui.internal.texteditor.quickdiff.compare.equivalence.IHashFunction;
import org.eclipse.ui.internal.texteditor.quickdiff.compare.rangedifferencer.IRangeComparator;
import org.eclipse.ui.internal.texteditor.quickdiff.compare.rangedifferencer.RangeDifference;
import org.eclipse.ui.internal.texteditor.quickdiff.compare.rangedifferencer.RangeDifferencer;
import org.eclipse.ui.progress.IProgressConstants;
import org.eclipse.ui.texteditor.quickdiff.IQuickDiffReferenceProvider;

/**
 * Standard implementation of <code>ILineDiffer</code> as an incremental diff engine. A
 * <code>DocumentLineDiffer</code> can be initialized to some start state. Once connected to a
 * <code>IDocument</code> and a reference document has been set, changes reported via the
 * <code>IDocumentListener</code> interface will be tracked and the incremental diff updated.
 *
 * <p>The diff state can be queried using the <code>ILineDiffer</code> interface.</p>
 *
 * <p>Since diff information is model information attached to a document, this class implements
 * <code>IAnnotationModel</code> and can be attached to <code>IAnnotationModelExtension</code>s.</p>
 *
 * <p>This class is not supposed to be subclassed.</p>
 *
 * @since 3.0
 */
public class DocumentLineDiffer implements ILineDiffer, IDocumentListener, IAnnotationModel, ILineDifferExtension, ILineDifferExtension2 {

    /**
	 * Artificial line difference information indicating a change with an empty line as original text.
	 */
    private static class LineChangeInfo implements ILineDiffInfo {

        private static final String[] ORIGINAL_TEXT = new String[] { "\n" };

        public int getRemovedLinesBelow() {
            return 0;
        }

        public int getRemovedLinesAbove() {
            return 0;
        }

        public int getChangeType() {
            return CHANGED;
        }

        public boolean hasChanges() {
            return true;
        }

        public String[] getOriginalText() {
            return ORIGINAL_TEXT;
        }
    }

    /** Tells whether this class is in debug mode. */
    private static boolean DEBUG = "true".equalsIgnoreCase(Platform.getDebugOption("org.eclipse.ui.workbench.texteditor/debug/DocumentLineDiffer"));

    /** The delay after which the initialization job is triggered. */
    private static final int INITIALIZE_DELAY = 500;

    /** Suspended state */
    private static final int SUSPENDED = 0;

    /** Initializing state */
    private static final int INITIALIZING = 1;

    /** Synchronized state */
    private static final int SYNCHRONIZED = 2;

    /** This differ's state */
    private int fState = SUSPENDED;

    /** Artificial line difference information indicating a change with an empty line as original text. */
    private final ILineDiffInfo fLineChangeInfo = new LineChangeInfo();

    /** The provider for the reference document. */
    IQuickDiffReferenceProvider fReferenceProvider;

    /** The number of clients connected to this model. */
    private int fOpenConnections;

    /** The current document being tracked. */
    private IDocument fLeftDocument;

    /**
	 * The equivalence class of the left document.
	 * @since 3.2
	 */
    private DocumentEquivalenceClass fLeftEquivalent;

    /** The reference document. */
    private IDocument fRightDocument;

    /**
	 * The equivalence class of the right document.
	 * @since 3.2
	 */
    private DocumentEquivalenceClass fRightEquivalent;

    /**
	 * Flag to indicate whether a change has been made to the line table and any clients should
	 * update their presentation.
	 */
    private boolean fUpdateNeeded;

    /** The listeners on this annotation model. */
    private List fAnnotationModelListeners = new ArrayList();

    /** The job currently initializing the differ, or <code>null</code> if there is none. */
    private Job fInitializationJob;

    /** Stores <code>DocumentEvents</code> while an initialization is going on. */
    private List fStoredEvents = new ArrayList();

    /**
	 * The differences between <code>fLeftDocument</code> and <code>fRightDocument</code>.
	 * This is the model we work on.
	 */
    private List fDifferences = new ArrayList();

    /**
	 * The differences removed in one iteration. Stored to be able to send out differentiated
	 * annotation events.
	 */
    private List fRemoved = new ArrayList();

    /**
	 * The differences added in one iteration. Stored to be able to send out differentiated
	 * annotation events.
	 */
    private List fAdded = new ArrayList();

    /**
	 * The differences changed in one iteration. Stored to be able to send out differentiated
	 * annotation events.
	 */
    private List fChanged = new ArrayList();

    /** The first line affected by a document event. */
    private int fFirstLine;

    /** The number of lines affected by a document event. */
    private int fNLines;

    /** The most recent range difference returned in a getLineInfo call, so it can be recyled. */
    private RangeDifference fLastDifference;

    /**
	 * <code>true</code> if incoming document events should be ignored,
	 * <code>false</code> if not.
	 */
    private boolean fIgnoreDocumentEvents = true;

    /**
	 * The listener for document rewrite sessions.
	 * @since 3.2
	 */
    private final IDocumentRewriteSessionListener fSessionListener = new IDocumentRewriteSessionListener() {

        public void documentRewriteSessionChanged(DocumentRewriteSessionEvent event) {
            if (event.getSession().getSessionType() == DocumentRewriteSessionType.UNRESTRICTED_SMALL) return;
            if (DocumentRewriteSessionEvent.SESSION_START.equals(event.getChangeType())) suspend(); else if (DocumentRewriteSessionEvent.SESSION_STOP.equals(event.getChangeType())) resume();
        }
    };

    private Thread fThread;

    private DocumentEvent fLastUIEvent;

    /**
	 * Creates a new differ.
	 */
    public DocumentLineDiffer() {
    }

    public ILineDiffInfo getLineInfo(int line) {
        if (isSuspended()) return fLineChangeInfo;
        RangeDifference last = fLastDifference;
        if (last != null && last.rightStart() <= line && last.rightEnd() > line) return new DiffRegion(last, line - last.rightStart(), fDifferences, fLeftDocument);
        fLastDifference = getRangeDifferenceForRightLine(line);
        last = fLastDifference;
        if (last != null) return new DiffRegion(last, line - last.rightStart(), fDifferences, fLeftDocument);
        return null;
    }

    public synchronized void revertLine(int line) throws BadLocationException {
        if (!isInitialized()) throw new BadLocationException(QuickDiffMessages.quickdiff_nonsynchronized);
        DiffRegion region = (DiffRegion) getLineInfo(line);
        if (region == null || fRightDocument == null || fLeftDocument == null) return;
        RangeDifference diff = region.getDifference();
        int rOffset = fRightDocument.getLineOffset(line);
        int rLength = fRightDocument.getLineLength(line);
        int leftLine = diff.leftStart() + region.getOffset();
        String replacement;
        if (leftLine >= diff.leftEnd()) replacement = ""; else {
            int lOffset = fLeftDocument.getLineOffset(leftLine);
            int lLength = fLeftDocument.getLineLength(leftLine);
            replacement = fLeftDocument.get(lOffset, lLength);
        }
        fRightDocument.replace(rOffset, rLength, replacement);
    }

    public synchronized void revertBlock(int line) throws BadLocationException {
        if (!isInitialized()) throw new BadLocationException(QuickDiffMessages.quickdiff_nonsynchronized);
        DiffRegion region = (DiffRegion) getLineInfo(line);
        if (region == null || fRightDocument == null || fLeftDocument == null) return;
        RangeDifference diff = region.getDifference();
        int rOffset = fRightDocument.getLineOffset(diff.rightStart());
        int rLength = fRightDocument.getLineOffset(diff.rightEnd() - 1) + fRightDocument.getLineLength(diff.rightEnd() - 1) - rOffset;
        int lOffset = fLeftDocument.getLineOffset(diff.leftStart());
        int lLength = fLeftDocument.getLineOffset(diff.leftEnd() - 1) + fLeftDocument.getLineLength(diff.leftEnd() - 1) - lOffset;
        fRightDocument.replace(rOffset, rLength, fLeftDocument.get(lOffset, lLength));
    }

    public synchronized void revertSelection(int line, int nLines) throws BadLocationException {
        if (!isInitialized()) throw new BadLocationException(QuickDiffMessages.quickdiff_nonsynchronized);
        if (fRightDocument == null || fLeftDocument == null) return;
        int rOffset = -1, rLength = -1, lOffset = -1, lLength = -1;
        RangeDifference diff = null;
        final List differences = fDifferences;
        synchronized (differences) {
            Iterator it = differences.iterator();
            while (it.hasNext()) {
                diff = (RangeDifference) it.next();
                if (line < diff.rightEnd()) {
                    rOffset = fRightDocument.getLineOffset(line);
                    int leftLine = Math.min(diff.leftStart() + line - diff.rightStart(), diff.leftEnd() - 1);
                    lOffset = fLeftDocument.getLineOffset(leftLine);
                    break;
                }
            }
            if (rOffset == -1 || lOffset == -1) return;
            int to = line + nLines - 1;
            while (it.hasNext()) {
                diff = (RangeDifference) it.next();
                if (to < diff.rightEnd()) {
                    int rEndOffset = fRightDocument.getLineOffset(to) + fRightDocument.getLineLength(to);
                    rLength = rEndOffset - rOffset;
                    int leftLine = Math.min(diff.leftStart() + to - diff.rightStart(), diff.leftEnd() - 1);
                    int lEndOffset = fLeftDocument.getLineOffset(leftLine) + fLeftDocument.getLineLength(leftLine);
                    lLength = lEndOffset - lOffset;
                    break;
                }
            }
        }
        if (rLength == -1 || lLength == -1) return;
        fRightDocument.replace(rOffset, rLength, fLeftDocument.get(lOffset, lLength));
    }

    public synchronized int restoreAfterLine(int line) throws BadLocationException {
        if (!isInitialized()) throw new BadLocationException(QuickDiffMessages.quickdiff_nonsynchronized);
        DiffRegion region = (DiffRegion) getLineInfo(line);
        if (region == null || fRightDocument == null || fLeftDocument == null) return 0;
        if (region.getRemovedLinesBelow() < 1) return 0;
        RangeDifference diff = null;
        final List differences = fDifferences;
        synchronized (differences) {
            for (Iterator it = differences.iterator(); it.hasNext(); ) {
                diff = (RangeDifference) it.next();
                if (line >= diff.rightStart() && line < diff.rightEnd()) {
                    if (diff.kind() == RangeDifference.NOCHANGE && it.hasNext()) diff = (RangeDifference) it.next();
                    break;
                }
            }
        }
        if (diff == null) return 0;
        int rOffset = fRightDocument.getLineOffset(diff.rightEnd());
        int rLength = 0;
        int leftLine = diff.leftStart() + diff.rightLength();
        int lOffset = fLeftDocument.getLineOffset(leftLine);
        int lLength = fLeftDocument.getLineOffset(diff.leftEnd() - 1) + fLeftDocument.getLineLength(diff.leftEnd() - 1) - lOffset;
        fRightDocument.replace(rOffset, rLength, fLeftDocument.get(lOffset, lLength));
        return diff.leftLength() - diff.rightLength();
    }

    /**
	 * Returns the receivers initialization state.
	 *
	 * @return <code>true</code> if we are initialized and in sync with the document.
	 */
    private boolean isInitialized() {
        return fState == SYNCHRONIZED;
    }

    /**
	 * Returns the receivers synchronization state.
	 *
	 * @return <code>true</code> if we are initialized and in sync with the document.
	 */
    public synchronized boolean isSynchronized() {
        return fState == SYNCHRONIZED;
    }

    /**
	 * Returns <code>true</code> if the differ is suspended.
	 *
	 * @return <code>true</code> if the differ is suspended
	 */
    public synchronized boolean isSuspended() {
        return fState == SUSPENDED;
    }

    /**
	 * Sets the reference provider for this instance. If one has been installed before, it is
	 * disposed.
	 *
	 * @param provider the new provider
	 */
    public void setReferenceProvider(IQuickDiffReferenceProvider provider) {
        Assert.isNotNull(provider);
        if (provider != fReferenceProvider) {
            if (fReferenceProvider != null) fReferenceProvider.dispose();
            fReferenceProvider = provider;
            initialize();
        }
    }

    /**
	 * Returns the reference provider currently installed, or <code>null</code> if none is installed.
	 *
	 * @return the current reference provider.
	 */
    public IQuickDiffReferenceProvider getReferenceProvider() {
        return fReferenceProvider;
    }

    /**
	 * (Re-)initializes the differ using the current reference and <code>DiffInitializer</code>.
	 * 
	 * @since 3.2 protected for testing reasons, package visible before
	 */
    protected synchronized void initialize() {
        fState = INITIALIZING;
        if (fRightDocument == null) return;
        fIgnoreDocumentEvents = true;
        if (fLeftDocument != null) {
            fLeftDocument.removeDocumentListener(this);
            fLeftDocument = null;
            fLeftEquivalent = null;
        }
        final Job oldJob = fInitializationJob;
        if (oldJob != null) {
            if (oldJob.getState() == Job.WAITING) {
                oldJob.wakeUp(INITIALIZE_DELAY);
                return;
            }
            oldJob.cancel();
        }
        fInitializationJob = new Job(QuickDiffMessages.quickdiff_initialize) {

            public IStatus run(IProgressMonitor monitor) {
                if (oldJob != null) try {
                    oldJob.join();
                } catch (InterruptedException e) {
                    Assert.isTrue(false);
                }
                IQuickDiffReferenceProvider provider = fReferenceProvider;
                final IDocument left;
                try {
                    left = provider == null ? null : provider.getReference(monitor);
                } catch (CoreException e) {
                    synchronized (DocumentLineDiffer.this) {
                        if (isCanceled(monitor)) return Status.CANCEL_STATUS;
                        clearModel();
                        fireModelChanged();
                        return e.getStatus();
                    }
                } catch (OperationCanceledException e) {
                    return Status.CANCEL_STATUS;
                }
                IDocument right = fRightDocument;
                IDocument actual = null;
                IDocument reference = null;
                synchronized (DocumentLineDiffer.this) {
                    if (left == null || right == null) {
                        if (isCanceled(monitor)) return Status.CANCEL_STATUS;
                        clearModel();
                        fireModelChanged();
                        return Status.OK_STATUS;
                    }
                    fLeftDocument = left;
                    fIgnoreDocumentEvents = false;
                }
                left.addDocumentListener(DocumentLineDiffer.this);
                reference = createCopy(left);
                if (reference == null) return Status.CANCEL_STATUS;
                Object lock = null;
                if (right instanceof ISynchronizable) lock = ((ISynchronizable) right).getLockObject();
                if (lock != null) {
                    synchronized (lock) {
                        synchronized (DocumentLineDiffer.this) {
                            if (isCanceled(monitor)) return Status.CANCEL_STATUS;
                            fStoredEvents.clear();
                            actual = createUnprotectedCopy(right);
                        }
                    }
                } else {
                    int i = 0;
                    do {
                        if (i++ == 100) return new Status(IStatus.ERROR, TextEditorPlugin.PLUGIN_ID, IStatus.OK, NLSUtility.format(QuickDiffMessages.quickdiff_error_getting_document_content, new Object[] { left.getClass(), right.getClass() }), null);
                        synchronized (DocumentLineDiffer.this) {
                            if (isCanceled(monitor)) return Status.CANCEL_STATUS;
                            fStoredEvents.clear();
                        }
                        actual = createCopy(right);
                        synchronized (DocumentLineDiffer.this) {
                            if (isCanceled(monitor)) return Status.CANCEL_STATUS;
                            if (fStoredEvents.size() == 0 && actual != null) break;
                        }
                    } while (true);
                }
                IHashFunction hash = new DJBHashFunction();
                DocumentEquivalenceClass leftEquivalent = new DocumentEquivalenceClass(reference, hash);
                fLeftEquivalent = leftEquivalent;
                IRangeComparator ref = new DocEquivalenceComparator(leftEquivalent, null);
                DocumentEquivalenceClass rightEquivalent = new DocumentEquivalenceClass(actual, hash);
                fRightEquivalent = rightEquivalent;
                IRangeComparator act = new DocEquivalenceComparator(rightEquivalent, null);
                List diffs = RangeDifferencer.findRanges(monitor, ref, act);
                synchronized (DocumentLineDiffer.this) {
                    if (isCanceled(monitor)) return Status.CANCEL_STATUS;
                    fDifferences = diffs;
                }
                try {
                    do {
                        DocumentEvent event;
                        synchronized (DocumentLineDiffer.this) {
                            if (isCanceled(monitor)) return Status.CANCEL_STATUS;
                            if (fStoredEvents.isEmpty()) {
                                fInitializationJob = null;
                                fState = SYNCHRONIZED;
                                fLastDifference = null;
                                leftEquivalent.setDocument(left);
                                rightEquivalent.setDocument(right);
                                break;
                            }
                            event = (DocumentEvent) fStoredEvents.remove(0);
                        }
                        IDocument copy = null;
                        if (event.fDocument == right) copy = actual; else if (event.fDocument == left) copy = reference; else Assert.isTrue(false);
                        event = new DocumentEvent(copy, event.fOffset, event.fLength, event.fText);
                        handleAboutToBeChanged(event);
                        actual.replace(event.fOffset, event.fLength, event.fText);
                        handleChanged(event);
                    } while (true);
                } catch (BadLocationException e) {
                    left.removeDocumentListener(DocumentLineDiffer.this);
                    clearModel();
                    initialize();
                    return Status.CANCEL_STATUS;
                }
                fireModelChanged();
                return Status.OK_STATUS;
            }

            private boolean isCanceled(IProgressMonitor monitor) {
                return fInitializationJob != this || monitor != null && monitor.isCanceled();
            }

            private void clearModel() {
                synchronized (DocumentLineDiffer.this) {
                    fLeftDocument = null;
                    fLeftEquivalent = null;
                    fInitializationJob = null;
                    fStoredEvents.clear();
                    fLastDifference = null;
                    fDifferences.clear();
                }
            }

            /**
			 * Creates a copy of <code>document</code> and catches any
			 * exceptions that may occur if the document is modified concurrently.
			 * Only call this method in a synchronized block if the document is
			 * an ISynchronizable and has been locked, as document.get() is called
			 * and may result in a deadlock otherwise.
			 *
			 * @param document the document to create a copy of
			 * @return a copy of the document, or <code>null</code> if an exception was thrown
			 */
            private IDocument createCopy(IDocument document) {
                Assert.isNotNull(document);
                try {
                    return createUnprotectedCopy(document);
                } catch (NullPointerException e) {
                } catch (ArrayStoreException e) {
                } catch (IndexOutOfBoundsException e) {
                } catch (ConcurrentModificationException e) {
                } catch (NegativeArraySizeException e) {
                }
                return null;
            }

            private IDocument createUnprotectedCopy(IDocument document) {
                return new Document(document.get());
            }
        };
        fInitializationJob.setSystem(true);
        fInitializationJob.setPriority(Job.DECORATE);
        fInitializationJob.setProperty(IProgressConstants.NO_IMMEDIATE_ERROR_PROMPT_PROPERTY, Boolean.TRUE);
        fInitializationJob.schedule(INITIALIZE_DELAY);
    }

    public synchronized void documentAboutToBeChanged(DocumentEvent event) {
        if (fIgnoreDocumentEvents) return;
        if (event.getDocument() == fLeftDocument) {
            initialize();
            return;
        }
        if (!isInitialized()) {
            if (fInitializationJob != null) fStoredEvents.add(event);
            return;
        }
        fLastUIEvent = event;
        try {
            handleAboutToBeChanged(event);
        } catch (BadLocationException e) {
            reinitOnError(e);
            return;
        } catch (NullPointerException e) {
            reinitOnError(e);
            return;
        } catch (ArrayStoreException e) {
            reinitOnError(e);
            return;
        } catch (IndexOutOfBoundsException e) {
            reinitOnError(e);
            return;
        } catch (ConcurrentModificationException e) {
            reinitOnError(e);
            return;
        } catch (NegativeArraySizeException e) {
            reinitOnError(e);
            return;
        }
    }

    /**
	 * Unsynchronized version of <code>documentAboutToBeChanged</code>, called by <code>documentAboutToBeChanged</code>
	 * and {@link #initialize()}.
	 *
	 * @param event the document event to be handled
	 * @throws BadLocationException if document access fails
	 */
    void handleAboutToBeChanged(DocumentEvent event) throws BadLocationException {
        Assert.isTrue(fThread == null);
        fThread = Thread.currentThread();
        IDocument doc = event.getDocument();
        DocumentEquivalenceClass rightEquivalent = fRightEquivalent;
        if (doc == null || rightEquivalent == null) return;
        fFirstLine = doc.getLineOfOffset(event.getOffset());
        fNLines = doc.getLineOfOffset(event.getOffset() + event.getLength()) - fFirstLine + 1;
        rightEquivalent.update(event);
    }

    public synchronized void documentChanged(DocumentEvent event) {
        final Thread lastCurrentThread = fThread;
        fThread = null;
        if (fIgnoreDocumentEvents) return;
        if (event.getDocument() == fLeftDocument) {
            initialize();
            return;
        }
        if (event != fLastUIEvent) {
            fLastUIEvent = null;
            return;
        }
        fLastUIEvent = null;
        if (!isInitialized()) return;
        try {
            fThread = lastCurrentThread;
            handleChanged(event);
        } catch (BadLocationException e) {
            reinitOnError(e);
            return;
        } catch (NullPointerException e) {
            reinitOnError(e);
            return;
        } catch (ArrayStoreException e) {
            reinitOnError(e);
            return;
        } catch (IndexOutOfBoundsException e) {
            reinitOnError(e);
            return;
        } catch (ConcurrentModificationException e) {
            reinitOnError(e);
            return;
        } catch (NegativeArraySizeException e) {
            reinitOnError(e);
            return;
        }
        if (fUpdateNeeded) {
            AnnotationModelEvent ame = new AnnotationModelEvent(this, false);
            for (Iterator it = fAdded.iterator(); it.hasNext(); ) {
                RangeDifference rd = (RangeDifference) it.next();
                ame.annotationAdded(rd.getDiffRegion(fDifferences, fLeftDocument));
            }
            for (Iterator it = fRemoved.iterator(); it.hasNext(); ) {
                RangeDifference rd = (RangeDifference) it.next();
                ame.annotationRemoved(rd.getDiffRegion(fDifferences, fLeftDocument));
            }
            for (Iterator it = fChanged.iterator(); it.hasNext(); ) {
                RangeDifference rd = (RangeDifference) it.next();
                ame.annotationChanged(rd.getDiffRegion(fDifferences, fLeftDocument));
            }
            fireModelChanged(ame);
            fUpdateNeeded = false;
        }
    }

    /**
	 * Re-initializes the differ if an exception is thrown upon accessing the documents. This can
	 * happen if the documents get concurrently modified from a background thread.
	 *
	 * @param e the exception thrown, which is logged in debug mode
	 */
    private void reinitOnError(Exception e) {
        if (DEBUG) System.err.println("reinitializing quickdiff:\n" + e.getLocalizedMessage() + "\n" + e.getStackTrace());
        initialize();
    }

    /**
	 * Implementation of documentChanged, non synchronized.
	 *
	 * @param event the document event
	 * @throws BadLocationException if document access fails somewhere
	 */
    void handleChanged(DocumentEvent event) throws BadLocationException {
        Assert.isTrue(fThread == Thread.currentThread());
        fThread = null;
        IDocument left = fLeftDocument;
        DocumentEquivalenceClass leftEquivalent = fLeftEquivalent;
        DocumentEquivalenceClass rightEquivalent = fRightEquivalent;
        if (left == null || leftEquivalent == null || rightEquivalent == null) return;
        IDocument right = event.getDocument();
        IDocument modified = event.getDocument();
        if (modified != left && modified != right) Assert.isTrue(false);
        boolean leftToRight = modified == left;
        String insertion = event.getText();
        int added = insertion == null ? 1 : modified.computeNumberOfLines(insertion) + 1;
        if (added > 50 || fNLines > 50) {
            initialize();
            return;
        }
        int size = Math.max(fNLines, added) + 1;
        int lineDelta = added - fNLines;
        int lastLine = fFirstLine + fNLines - 1;
        int repetitionField;
        if (leftToRight) {
            int originalLine = getRightLine(lastLine + 1);
            repetitionField = searchForRepetitionField(size - 1, right, originalLine);
        } else {
            int originalLine = getLeftLine(lastLine + 1);
            repetitionField = searchForRepetitionField(size - 1, left, originalLine);
        }
        lastLine += repetitionField;
        final RangeDifference consistentBefore, consistentAfter;
        if (leftToRight) {
            consistentBefore = findConsistentRangeBeforeLeft(fFirstLine, size);
            consistentAfter = findConsistentRangeAfterLeft(lastLine, size);
        } else {
            consistentBefore = findConsistentRangeBeforeRight(fFirstLine, size);
            consistentAfter = findConsistentRangeAfterRight(lastLine, size);
        }
        int shiftBefore = 0;
        if (consistentBefore.kind() == RangeDifference.NOCHANGE) {
            int unchanged;
            if (leftToRight) unchanged = Math.min(fFirstLine, consistentBefore.leftEnd()) - consistentBefore.leftStart(); else unchanged = Math.min(fFirstLine, consistentBefore.rightEnd()) - consistentBefore.rightStart();
            shiftBefore = Math.max(0, unchanged - size);
        }
        int shiftAfter = 0;
        if (consistentAfter.kind() == RangeDifference.NOCHANGE) {
            int unchanged;
            if (leftToRight) unchanged = consistentAfter.leftEnd() - Math.max(lastLine + 1, consistentAfter.leftStart()); else unchanged = consistentAfter.rightEnd() - Math.max(lastLine + 1, consistentAfter.rightStart());
            shiftAfter = Math.max(0, unchanged - size);
        }
        int leftStartLine = consistentBefore.leftStart() + shiftBefore;
        int leftLine = consistentAfter.leftEnd();
        if (leftToRight) leftLine += lineDelta;
        int leftEndLine = leftLine - shiftAfter;
        ILineRange leftRange = new LineRange(leftStartLine, leftEndLine - leftStartLine);
        IRangeComparator reference = new DocEquivalenceComparator(leftEquivalent, leftRange);
        int rightStartLine = consistentBefore.rightStart() + shiftBefore;
        int rightLine = consistentAfter.rightEnd();
        if (!leftToRight) rightLine += lineDelta;
        int rightEndLine = rightLine - shiftAfter;
        ILineRange rightRange = new LineRange(rightStartLine, rightEndLine - rightStartLine);
        IRangeComparator change = new DocEquivalenceComparator(rightEquivalent, rightRange);
        if (leftLine - shiftAfter - leftStartLine > 50 || rightLine - shiftAfter - rightStartLine > 50) {
            initialize();
            return;
        }
        List diffs = RangeDifferencer.findRanges(reference, change);
        if (diffs.size() == 0) {
            diffs.add(new RangeDifference(RangeDifference.CHANGE, 0, 0, 0, 0));
        }
        for (Iterator it = diffs.iterator(); it.hasNext(); ) {
            RangeDifference d = (RangeDifference) it.next();
            d.shiftLeft(leftStartLine);
            d.shiftRight(rightStartLine);
        }
        if (shiftBefore > 0) {
            RangeDifference first = (RangeDifference) diffs.get(0);
            if (first.kind() == RangeDifference.NOCHANGE) first.extendStart(-shiftBefore); else diffs.add(0, new RangeDifference(RangeDifference.NOCHANGE, first.rightStart() - shiftBefore, shiftBefore, first.leftStart() - shiftBefore, shiftBefore));
        }
        RangeDifference last = (RangeDifference) diffs.get(diffs.size() - 1);
        if (shiftAfter > 0) {
            if (last.kind() == RangeDifference.NOCHANGE) last.extendEnd(shiftAfter); else diffs.add(new RangeDifference(RangeDifference.NOCHANGE, last.rightEnd(), shiftAfter, last.leftEnd(), shiftAfter));
        }
        synchronized (fDifferences) {
            final ListIterator it = fDifferences.listIterator();
            Iterator newIt = diffs.iterator();
            RangeDifference current;
            boolean changed = false;
            do {
                Assert.isTrue(it.hasNext());
                current = (RangeDifference) it.next();
            } while (current != consistentBefore);
            Assert.isTrue(current == consistentBefore);
            fChanged.clear();
            fRemoved.clear();
            fAdded.clear();
            while (current != consistentAfter) {
                if (newIt.hasNext()) {
                    Object o = newIt.next();
                    if (!current.equals(o)) {
                        fRemoved.add(current);
                        fAdded.add(o);
                        changed = true;
                        it.set(o);
                    }
                } else {
                    fRemoved.add(current);
                    it.remove();
                    changed = true;
                }
                Assert.isTrue(it.hasNext());
                current = (RangeDifference) it.next();
            }
            Assert.isTrue(current == consistentAfter);
            if (newIt.hasNext()) {
                Object o = newIt.next();
                if (!current.equals(o)) {
                    fRemoved.add(current);
                    fAdded.add(o);
                    changed = true;
                    it.set(o);
                }
            } else {
                fRemoved.add(current);
                it.remove();
                changed = true;
            }
            while (newIt.hasNext()) {
                Object next = newIt.next();
                fAdded.add(next);
                it.add(next);
                changed = true;
            }
            boolean init = true;
            int leftShift = 0;
            int rightShift = 0;
            while (it.hasNext()) {
                current = (RangeDifference) it.next();
                if (init) {
                    init = false;
                    leftShift = last.leftEnd() - current.leftStart();
                    rightShift = last.rightEnd() - current.rightStart();
                    if (leftShift != 0 || rightShift != 0) changed = true; else break;
                }
                current.shiftLeft(leftShift);
                current.shiftRight(rightShift);
            }
            fUpdateNeeded = changed;
        }
        fLastDifference = null;
    }

    /**
	 * Finds a consistent range of at least size before <code>line</code> in the left document.
	 *
	 * @param line the line before which the range has to occur
	 * @param size the minimal size of the range
	 * @return the first range found, or the first range in the differ if none can be found
	 */
    private RangeDifference findConsistentRangeBeforeLeft(int line, int size) {
        RangeDifference found = null;
        for (ListIterator it = fDifferences.listIterator(); it.hasNext(); ) {
            RangeDifference difference = (RangeDifference) it.next();
            if (found == null || difference.kind() == RangeDifference.NOCHANGE && (difference.leftEnd() < line && difference.leftLength() >= size || difference.leftEnd() >= line && line - difference.leftStart() >= size)) found = difference;
            if (difference.leftEnd() >= line) break;
        }
        return found;
    }

    /**
	 * Finds a consistent range of at least size after <code>line</code> in the left document.
	 *
	 * @param line the line after which the range has to occur
	 * @param size the minimal size of the range
	 * @return the first range found, or the last range in the differ if none can be found
	 */
    private RangeDifference findConsistentRangeAfterLeft(int line, int size) {
        RangeDifference found = null;
        for (ListIterator it = fDifferences.listIterator(fDifferences.size()); it.hasPrevious(); ) {
            RangeDifference difference = (RangeDifference) it.previous();
            if (found == null || difference.kind() == RangeDifference.NOCHANGE && (difference.leftStart() > line && difference.leftLength() >= size || difference.leftStart() <= line && difference.leftEnd() - line >= size)) found = difference;
            if (difference.leftStart() <= line) break;
        }
        return found;
    }

    /**
	 * Finds a consistent range of at least size before <code>line</code> in the right document.
	 *
	 * @param line the line before which the range has to occur
	 * @param size the minimal size of the range
	 * @return the first range found, or the first range in the differ if none can be found
	 */
    private RangeDifference findConsistentRangeBeforeRight(int line, int size) {
        RangeDifference found = null;
        int unchanged = -1;
        for (ListIterator it = fDifferences.listIterator(); it.hasNext(); ) {
            RangeDifference difference = (RangeDifference) it.next();
            if (found == null) found = difference; else if (difference.kind() == RangeDifference.NOCHANGE) {
                unchanged = Math.min(line, difference.rightEnd()) - difference.rightStart();
                if (unchanged >= size) found = difference;
            }
            if (difference.rightEnd() >= line) break;
        }
        return found;
    }

    /**
	 * Finds a consistent range of at least size after <code>line</code> in the right document.
	 *
	 * @param line the line after which the range has to occur
	 * @param size the minimal size of the range
	 * @return the first range found, or the last range in the differ if none can be found
	 */
    private RangeDifference findConsistentRangeAfterRight(int line, int size) {
        RangeDifference found = null;
        int unchanged = -1;
        for (ListIterator it = fDifferences.listIterator(fDifferences.size()); it.hasPrevious(); ) {
            RangeDifference difference = (RangeDifference) it.previous();
            if (found == null) found = difference; else if (difference.kind() == RangeDifference.NOCHANGE) {
                unchanged = difference.rightEnd() - Math.max(line + 1, difference.rightStart());
                if (unchanged >= size) found = difference;
            }
            if (difference.rightStart() <= line) break;
        }
        return found;
    }

    /**
	 * Returns the size of a repetition field starting a <code>line</code>.
	 *
	 * @param size the maximal length of the repeat window
	 * @param doc the document to search
	 * @param line the line to start searching
	 * @return the size of a found repetition field, or zero
	 * @throws BadLocationException if <code>doc</code> is modified concurrently
	 */
    private int searchForRepetitionField(int size, IDocument doc, int line) throws BadLocationException {
        LinkedList window = new LinkedList();
        int nLines = doc.getNumberOfLines();
        int repetition = line - 1;
        int l = line;
        while (l >= 0 && l < nLines) {
            IRegion r = doc.getLineInformation(l);
            String current = doc.get(r.getOffset(), r.getLength());
            if (!window.isEmpty() && window.get(0).equals(current)) {
                window.removeFirst();
                window.addLast(current);
                repetition = l;
            } else {
                if (window.size() < size) window.addLast(current); else break;
            }
            l++;
        }
        int fieldLength = repetition - line + 1;
        Assert.isTrue(fieldLength >= 0);
        return fieldLength;
    }

    /**
	 * Gets the corresponding line on the left side for a line on the right.
	 *
	 * @param rightLine the line on the right side
	 * @return the corresponding left hand line, or <code>-1</code>
	 */
    private int getLeftLine(int rightLine) {
        RangeDifference d = getRangeDifferenceForRightLine(rightLine);
        if (d == null) return -1;
        return Math.min(d.leftEnd() - 1, d.leftStart() + rightLine - d.rightStart());
    }

    /**
	 * Gets the corresponding line on the right side for a line on the left.
	 *
	 * @param leftLine the line on the left side
	 * @return the corresponding right hand line, or <code>-1</code>
	 */
    private int getRightLine(int leftLine) {
        RangeDifference d = getRangeDifferenceForLeftLine(leftLine);
        if (d == null) return -1;
        return Math.min(d.rightEnd() - 1, d.rightStart() + leftLine - d.leftStart());
    }

    /**
	 * Gets the RangeDifference for a line on the left hand side.
	 *
	 * @param leftLine the line on the left side
	 * @return the corresponding RangeDifference, or <code>null</code>
	 */
    private RangeDifference getRangeDifferenceForLeftLine(int leftLine) {
        for (Iterator it = fDifferences.iterator(); it.hasNext(); ) {
            RangeDifference d = (RangeDifference) it.next();
            if (leftLine >= d.leftStart() && leftLine < d.leftEnd()) {
                return d;
            }
        }
        return null;
    }

    /**
	 * Gets the RangeDifference for a line on the right hand side.
	 *
	 * @param rightLine the line on the right side
	 * @return the corresponding RangeDifference, or <code>null</code>
	 */
    private RangeDifference getRangeDifferenceForRightLine(int rightLine) {
        final List differences = fDifferences;
        synchronized (differences) {
            for (Iterator it = differences.iterator(); it.hasNext(); ) {
                RangeDifference d = (RangeDifference) it.next();
                if (rightLine >= d.rightStart() && rightLine < d.rightEnd()) {
                    return d;
                }
            }
        }
        return null;
    }

    public void addAnnotationModelListener(IAnnotationModelListener listener) {
        fAnnotationModelListeners.add(listener);
    }

    public void removeAnnotationModelListener(IAnnotationModelListener listener) {
        fAnnotationModelListeners.remove(listener);
    }

    public void connect(IDocument document) {
        Assert.isTrue(fRightDocument == null || fRightDocument == document);
        ++fOpenConnections;
        if (fOpenConnections == 1) {
            fRightDocument = document;
            fRightDocument.addDocumentListener(this);
            if (document instanceof IDocumentExtension4) {
                IDocumentExtension4 ext = (IDocumentExtension4) document;
                ext.addDocumentRewriteSessionListener(fSessionListener);
            }
            initialize();
        }
    }

    public void disconnect(IDocument document) {
        Assert.isTrue(fRightDocument == document);
        --fOpenConnections;
        if (fOpenConnections == 0) uninstall();
    }

    /**
	 * Uninstalls all components and dereferences any objects.
	 */
    private void uninstall() {
        Job job = fInitializationJob;
        if (job != null) job.cancel();
        synchronized (this) {
            fState = SUSPENDED;
            fIgnoreDocumentEvents = true;
            fInitializationJob = null;
            if (fLeftDocument != null) fLeftDocument.removeDocumentListener(this);
            fLeftDocument = null;
            fLeftEquivalent = null;
            if (fRightDocument != null) {
                fRightDocument.removeDocumentListener(this);
                if (fRightDocument instanceof IDocumentExtension4) {
                    IDocumentExtension4 ext = (IDocumentExtension4) fRightDocument;
                    ext.removeDocumentRewriteSessionListener(fSessionListener);
                }
            }
            fRightDocument = null;
            fRightEquivalent = null;
            fDifferences.clear();
        }
        if (fReferenceProvider != null) {
            fReferenceProvider.dispose();
            fReferenceProvider = null;
        }
    }

    public void addAnnotation(Annotation annotation, Position position) {
        throw new UnsupportedOperationException();
    }

    public void removeAnnotation(Annotation annotation) {
        throw new UnsupportedOperationException();
    }

    public Iterator getAnnotationIterator() {
        final List copy;
        List differences = fDifferences;
        synchronized (differences) {
            copy = new ArrayList(differences);
        }
        final Iterator iter = copy.iterator();
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext() {
                return iter.hasNext();
            }

            public Object next() {
                RangeDifference diff = (RangeDifference) iter.next();
                return diff.getDiffRegion(copy, fLeftDocument);
            }
        };
    }

    public Position getPosition(Annotation annotation) {
        if (fRightDocument != null && annotation instanceof DiffRegion) {
            RangeDifference difference = ((DiffRegion) annotation).getDifference();
            try {
                int offset = fRightDocument.getLineOffset(difference.rightStart());
                return new Position(offset, fRightDocument.getLineOffset(difference.rightEnd() - 1) + fRightDocument.getLineLength(difference.rightEnd() - 1) - offset);
            } catch (BadLocationException e) {
            }
        }
        return null;
    }

    /**
	 * Informs all annotation model listeners that this model has been changed.
	 */
    protected void fireModelChanged() {
        fireModelChanged(new AnnotationModelEvent(this));
    }

    /**
	 * Informs all annotation model listeners that this model has been changed
	 * as described in the annotation model event. The event is sent out
	 * to all listeners implementing <code>IAnnotationModelListenerExtension</code>.
	 * All other listeners are notified by just calling <code>modelChanged(IAnnotationModel)</code>.
	 *
	 * @param event the event to be sent out to the listeners
	 */
    protected void fireModelChanged(AnnotationModelEvent event) {
        ArrayList v = new ArrayList(fAnnotationModelListeners);
        Iterator e = v.iterator();
        while (e.hasNext()) {
            IAnnotationModelListener l = (IAnnotationModelListener) e.next();
            if (l instanceof IAnnotationModelListenerExtension) ((IAnnotationModelListenerExtension) l).modelChanged(event); else l.modelChanged(this);
        }
    }

    public void suspend() {
        Job job = fInitializationJob;
        if (job != null) job.cancel();
        synchronized (this) {
            fInitializationJob = null;
            if (fRightDocument != null) fRightDocument.removeDocumentListener(this);
            if (fLeftDocument != null) fLeftDocument.removeDocumentListener(this);
            fLeftDocument = null;
            fLeftEquivalent = null;
            fLastDifference = null;
            fStoredEvents.clear();
            fDifferences.clear();
            fState = SUSPENDED;
            fireModelChanged();
        }
    }

    public synchronized void resume() {
        if (fRightDocument != null) fRightDocument.addDocumentListener(this);
        initialize();
    }
}
