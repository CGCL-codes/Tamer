package org.ocallahan.chronomancer.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.ocallahan.chronicle.FindSourceInfoQuery;
import org.ocallahan.chronicle.Function;
import org.ocallahan.chronicle.MMapInfo;
import org.ocallahan.chronicle.MemRange;
import org.ocallahan.chronicle.QueryUtils;
import org.ocallahan.chronicle.ScanCountQuery;
import org.ocallahan.chronicle.ScanExecQuery;
import org.ocallahan.chronicle.ScanQuery;
import org.ocallahan.chronicle.Session;
import org.ocallahan.chronicle.SourceCoordinate;
import org.ocallahan.chronomancer.ISourceAnnotator;
import org.ocallahan.chronomancer.State;
import org.ocallahan.chronomancer.views.LoopAnalyzer.Exec;

public class SourceAnnotator implements ISourceAnnotator {

    private SourceViewer sourceViewer;

    private boolean isCancelled;

    private HashMap<String, FileAnnotations> annotations = new HashMap<String, FileAnnotations>();

    private HashMap<ITextEditor, FileAnnotations> editorAnnotations = new HashMap<ITextEditor, FileAnnotations>();

    private ITextEditor currentPositionEditor;

    private Annotation currentPositionAnnotation;

    private Position currentPosition;

    public State getState() {
        return sourceViewer.getState();
    }

    public SourceAnnotator(SourceViewer sourceViewer, long currentTStamp) {
        this.sourceViewer = sourceViewer;
        ScopeCalculator sc = new ScopeCalculator(currentTStamp);
        Session s = getState().getSession();
        QueryUtils.findStartOfCall(s, currentTStamp, sc);
        QueryUtils.findRunningFunction(s, currentTStamp, sc);
    }

    public ITextEditor getCurrentEditor() {
        return currentPositionEditor;
    }

    public int getCurrentLine() {
        if (currentPositionAnnotation == null) return -1;
        IEditorInput input = currentPositionEditor.getEditorInput();
        IDocumentProvider provider = currentPositionEditor.getDocumentProvider();
        IDocument currentPositionDocument = provider.getDocument(input);
        try {
            return currentPositionDocument.getLineOfOffset(currentPosition.getOffset());
        } catch (BadLocationException e) {
            return -1;
        }
    }

    public List<TStampAddrPair> getExecAnnotations(ITextEditor editor, int line) {
        FileAnnotations fa = editorAnnotations.get(editor);
        if (fa == null) return Collections.emptyList();
        ArrayList<TStampAddrPair> result = new ArrayList<TStampAddrPair>();
        for (Coordinate c : fa.markups.keySet()) {
            if (c.getLine() == line) {
                result.addAll(fa.markups.get(c).tStampAddrs);
            }
        }
        Collections.sort(result);
        return result;
    }

    public List<LoopInfo> getLoopAnnotations(ITextEditor editor, int line) {
        FileAnnotations fa = editorAnnotations.get(editor);
        if (fa == null) return Collections.emptyList();
        ArrayList<LoopInfo> result = new ArrayList<LoopInfo>();
        for (Coordinate c : fa.markups.keySet()) {
            if (c.getLine() == line) {
                result.addAll(fa.markups.get(c).loops);
            }
        }
        Collections.sort(result);
        return result;
    }

    public void terminate() {
        synchronized (this) {
            if (isCancelled) return;
            isCancelled = true;
        }
        getState().setSourceAnnotator(null);
        for (ITextEditor editor : editorAnnotations.keySet()) {
            IEditorInput input = editor.getEditorInput();
            IDocumentProvider provider = editor.getDocumentProvider();
            IDocument doc = provider.getDocument(input);
            IAnnotationModel model = provider.getAnnotationModel(input);
            FileAnnotations fa = editorAnnotations.get(editor);
            for (Markup anno : fa.markups.values()) {
                if (anno.position != null) {
                    doc.removePosition(anno.position);
                }
                model.removeAnnotation(anno);
            }
        }
        if (currentPositionAnnotation != null) {
            IEditorInput input = currentPositionEditor.getEditorInput();
            IDocumentProvider provider = currentPositionEditor.getDocumentProvider();
            IDocument currentPositionDocument = provider.getDocument(input);
            IAnnotationModel currentPositionModel = provider.getAnnotationModel(input);
            currentPositionDocument.removePosition(currentPosition);
            currentPositionModel.removeAnnotation(currentPositionAnnotation);
        }
    }

    private synchronized boolean checkCancelled() {
        return isCancelled;
    }

    public static int getOffset(IDocument doc, Coordinate c) {
        try {
            int line = c.getLine();
            int lineLength = doc.getLineLength(line);
            return doc.getLineOffset(line) + Math.min(lineLength - 1, c.getColumn());
        } catch (BadLocationException e) {
            return doc.getLength();
        }
    }

    private void commitFileAnnotations(String fileName, FileAnnotations fa) {
        ITextEditor textEditor = openInEditor(fileName);
        if (textEditor == null) return;
        editorAnnotations.put(textEditor, fa);
        IEditorInput input = textEditor.getEditorInput();
        IDocumentProvider provider = textEditor.getDocumentProvider();
        IDocument doc = provider.getDocument(input);
        IAnnotationModel model = provider.getAnnotationModel(input);
        for (Markup m : fa.markups.values()) {
            if (!m.loops.isEmpty()) {
                Collections.sort(m.loops);
                LoopInfo loop = m.loops.get(0);
                m.setType("org.ocallahan.chronomancer.sourceLoopHead");
                m.setText("Iteration " + loop.getCurrentIteration() + "/" + loop.getTotalIterations());
            }
            Collections.sort(m.tStampAddrs);
            try {
                int startOffset = getOffset(doc, m.start);
                Coordinate end;
                if (fa.markups.containsKey(m.end)) {
                    end = m.end;
                } else {
                    end = new Coordinate(m.start.getLine() + 1, 0);
                }
                int endOffset = getOffset(doc, end);
                m.position = new Position(startOffset, endOffset - startOffset);
                doc.addPosition(m.position);
                model.addAnnotation(m, m.position);
            } catch (BadLocationException ex) {
            }
        }
        getState().setSourceAnnotator(this);
    }

    private void commit() {
        if (checkCancelled()) return;
        for (String fileName : annotations.keySet()) {
            commitFileAnnotations(fileName, annotations.get(fileName));
        }
    }

    public static class FileAnnotations {

        HashMap<Coordinate, Markup> markups = new HashMap<Coordinate, Markup>();

        IAnnotationModel model;

        IDocument document;
    }

    public static class Coordinate implements Comparable<Coordinate> {

        private int line;

        private int column;

        public Coordinate(int line, int column) {
            this.line = line;
            this.column = column;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }

        @Override
        public int hashCode() {
            return line * 31 + column;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Coordinate)) return false;
            Coordinate other = (Coordinate) obj;
            return line == other.line && column == other.column;
        }

        public Coordinate max(Coordinate other) {
            return compareTo(other) < 0 ? other : this;
        }

        public int compareTo(Coordinate other) {
            if (line < other.line) return -1;
            if (line > other.line) return 1;
            if (column < other.column) return -1;
            if (column > other.column) return 1;
            return 0;
        }
    }

    /**
	 * XXX rework this to handle column info
	 */
    public static class Markup extends Annotation {

        Markup(Coordinate start, Coordinate end) {
            super("org.ocallahan.chronomancer.sourceAnnotation", false, "");
            this.start = start;
            this.end = end;
        }

        Coordinate start;

        Coordinate end;

        ArrayList<TStampAddrPair> tStampAddrs = new ArrayList<TStampAddrPair>();

        ArrayList<LoopInfo> loops = new ArrayList<LoopInfo>();

        Position position;
    }

    private static IFile findFile(String fileName) {
        IWorkspace w = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = w.getRoot();
        IProject[] projects = root.getProjects();
        for (IPath p = new Path(fileName); !p.isEmpty(); p = p.removeFirstSegments(1)) {
            for (IProject project : projects) {
                if (project.isOpen()) {
                    IResource r = project.findMember(p);
                    if (r != null && r.getType() == IResource.FILE) return (IFile) r;
                }
            }
        }
        return null;
    }

    public ITextEditor openInEditor(String fileName) {
        if (fileName == null) return null;
        IFile f = findFile(fileName);
        if (f == null) return null;
        IEditorDescriptor editorDescriptor = IDE.getDefaultEditor(f);
        if (editorDescriptor == null) return null;
        String editorID = editorDescriptor.getId();
        IWorkbenchPage page = getState().getWindow().getActivePage();
        try {
            IEditorPart part = page.openEditor(new FileEditorInput(f), editorID);
            if (!(part instanceof ITextEditor)) return null;
            return (ITextEditor) part;
        } catch (PartInitException ex) {
            return null;
        }
    }

    public void showCurrentLine(SourceCoordinate src) {
        if (checkCancelled()) return;
        currentPositionEditor = openInEditor(src.getFileName());
        if (currentPositionEditor == null) return;
        IEditorInput input = currentPositionEditor.getEditorInput();
        IDocumentProvider provider = currentPositionEditor.getDocumentProvider();
        IDocument currentPositionDocument = provider.getDocument(input);
        try {
            int offset = currentPositionDocument.getLineOffset(src.getStartLine() - 1);
            currentPositionEditor.selectAndReveal(offset, 0);
            int end = currentPositionDocument.getLineOffset(src.getStartLine());
            currentPositionAnnotation = new Annotation("org.ocallahan.chronomancer.sourceCurrentLine", false, "Current line");
            IAnnotationModel currentPositionModel = provider.getAnnotationModel(input);
            currentPosition = new Position(offset, end - offset);
            currentPositionDocument.addPosition(currentPosition);
            currentPositionModel.addAnnotation(currentPositionAnnotation, currentPosition);
        } catch (BadLocationException e) {
        }
    }

    private Markup getMarkupFor(SourceCoordinate source, long tStamp, long address) {
        String fileName = source.getFileName();
        FileAnnotations fa = annotations.get(fileName);
        if (fa == null) {
            fa = new FileAnnotations();
            annotations.put(fileName, fa);
        }
        Coordinate start = new Coordinate(source.getStartLine() - 1, source.getStartColumn() - 1);
        Markup markup = fa.markups.get(start);
        if (markup == null) {
            Coordinate end = new Coordinate(source.getEndLine() - 1, source.getEndColumn() - 1);
            markup = new Markup(start, end);
            fa.markups.put(start, markup);
        }
        return markup;
    }

    private void prepareLoopAnnotation(long tStamp, long address, SourceCoordinate source, long iterationIndex, long totalIterations) {
        if (checkCancelled()) return;
        Markup markup = getMarkupFor(source, tStamp, address);
        markup.loops.add(new LoopInfo(tStamp, address, iterationIndex, totalIterations));
    }

    private void prepareExecutionAnnotation(long tStamp, long address, SourceCoordinate source) {
        if (checkCancelled()) return;
        Markup m = getMarkupFor(source, tStamp, address);
        m.tStampAddrs.add(new TStampAddrPair(tStamp, address));
    }

    private class ScopeCalculator implements QueryUtils.StartReceiver, QueryUtils.FunctionReceiver {

        private long currentTStamp;

        private long startTStamp;

        private long endTStamp;

        private Function function;

        private int outstandingRequests = 2;

        public ScopeCalculator(long currentTStamp) {
            this.currentTStamp = currentTStamp;
        }

        public void receiveStart(long startTStamp, long endTStamp, long beforeCallSP, long stackEnd, int thread) {
            this.startTStamp = startTStamp;
            this.endTStamp = endTStamp;
            checkDone();
        }

        public void receiveNothing() {
            checkDone();
        }

        public void receiveFunction(Function function) {
            this.function = function;
            checkDone();
        }

        private void checkDone() {
            --outstandingRequests;
            if (outstandingRequests > 0) return;
            if (function == null || function.getCompilationUnit() == null || endTStamp == 0 || checkCancelled()) {
                getState().getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        commit();
                    }
                });
                return;
            }
            LoopAnalyzer.analyze(getState().getSession(), function, startTStamp, endTStamp, new LoopListener(currentTStamp, function));
        }
    }

    /**
	 * Everything in this class happens on the session thread.
	 */
    private class LoopListener implements LoopAnalyzer.Listener {

        private long currentTStamp;

        private Function function;

        private int outstandingRequests = 1;

        LoopListener(long currentTStamp, Function function) {
            this.currentTStamp = currentTStamp;
            this.function = function;
        }

        public void foundOutsideLoop(Exec[] instructionsExecuted) {
            addExecAnnotations(instructionsExecuted);
        }

        public void foundLoop(Exec head, long endTStamp, long secondIterationTStamp, long lastIterationTStamp) {
            if (checkCancelled()) return;
            long address = head.getAddress();
            IterationScanner iterationScanner = new IterationScanner(head, endTStamp);
            Session s = getState().getSession();
            MemRange[] ranges = new MemRange[] { new MemRange(address, address + 1) };
            ScanCountQuery countBefore = new ScanCountQuery(s, head.getTStamp() + 1, currentTStamp + 1, "INSTR_EXEC", address, iterationScanner);
            ScanCountQuery countAfter = new ScanCountQuery(s, currentTStamp + 1, endTStamp, "INSTR_EXEC", address, iterationScanner);
            ScanExecQuery scanBefore = new ScanExecQuery(s, head.getTStamp() + 1, currentTStamp + 1, ranges, ScanQuery.Termination.LAST, iterationScanner);
            ScanExecQuery scanAfter = new ScanExecQuery(s, currentTStamp + 1, endTStamp, ranges, ScanQuery.Termination.FIRST, iterationScanner);
            iterationScanner.setQueries(countBefore, countAfter, scanBefore, scanAfter);
            countBefore.send();
            countAfter.send();
            scanBefore.send();
            scanAfter.send();
        }

        public void done() {
            completedRequest();
        }

        private void completedRequest() {
            --outstandingRequests;
            if (outstandingRequests == 0) {
                getState().getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        commit();
                    }
                });
            }
        }

        private void addLoopAnnotation(final long headAddress, final long iterationStartTStamp, long iterationEndTStamp, final long iterationIndex, final long totalIterations) {
            if (checkCancelled()) return;
            ++outstandingRequests;
            FindSourceInfoQuery fq = new FindSourceInfoQuery(getState().getSession(), iterationStartTStamp, new long[] { headAddress }, new FindSourceInfoQuery.Listener() {

                public void notifyDone(FindSourceInfoQuery q, boolean complete, final Map<Long, SourceCoordinate> sources) {
                    getState().getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            SourceCoordinate src = sources.get(headAddress);
                            if (src == null) return;
                            prepareLoopAnnotation(iterationStartTStamp, headAddress, src, iterationIndex, totalIterations);
                        }
                    });
                    completedRequest();
                }
            });
            fq.send();
        }

        private void addExecAnnotations(LoopAnalyzer.Exec[] execs) {
            if (checkCancelled()) return;
            long[] addrs = new long[execs.length];
            final HashMap<Long, Long> execTimes = new HashMap<Long, Long>();
            for (int i = 0; i < addrs.length; ++i) {
                addrs[i] = execs[i].getAddress();
                execTimes.put(addrs[i], execs[i].getTStamp());
            }
            ++outstandingRequests;
            FindSourceInfoQuery fq = new FindSourceInfoQuery(getState().getSession(), execs[0].getTStamp(), addrs, new FindSourceInfoQuery.Listener() {

                public void notifyDone(FindSourceInfoQuery q, boolean complete, final Map<Long, SourceCoordinate> sources) {
                    getState().getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            for (Long addr : sources.keySet()) {
                                SourceCoordinate s = sources.get(addr);
                                long tStamp = execTimes.get(addr);
                                prepareExecutionAnnotation(tStamp, addr, s);
                            }
                        }
                    });
                    completedRequest();
                }
            });
            fq.send();
        }

        class IterationScanner implements ScanCountQuery.Listener, ScanExecQuery.Listener {

            IterationScanner(LoopAnalyzer.Exec loopHead, long endTStamp) {
                this.loopHead = loopHead;
                currentIterationStart = loopHead.getTStamp();
                currentIterationEnd = endTStamp;
            }

            public void setQueries(ScanCountQuery countBefore, ScanCountQuery countAfter, ScanExecQuery scanBefore, ScanExecQuery scanAfter) {
                this.countBefore = countBefore;
                this.countAfter = countAfter;
                this.scanBefore = scanBefore;
                this.scanAfter = scanAfter;
            }

            public void notifyMMapResult(ScanQuery q, long stamp, long start, long end, MMapInfo info) {
            }

            public void notifyDone(ScanQuery q, boolean complete) {
                if (checkCancelled()) return;
                if (q == scanBefore) {
                    scanBefore = null;
                } else if (q == scanAfter) {
                    scanAfter = null;
                }
                tryDone();
            }

            public void notifyDone(ScanCountQuery q, boolean complete, long count) {
                if (checkCancelled()) return;
                if (complete) {
                    if (q == countBefore) {
                        beforeIterationCount = count + 1;
                    } else {
                        afterIterationCount = count;
                    }
                }
                if (q == countBefore) {
                    countBefore = null;
                } else if (q == countAfter) {
                    countAfter = null;
                }
                tryDone();
            }

            public void notifyExecResult(ScanExecQuery q, long tStamp, long start, long end) {
                if (q == scanBefore) {
                    currentIterationStart = tStamp;
                } else {
                    currentIterationEnd = tStamp;
                }
            }

            private void tryDone() {
                if (countBefore != null || countAfter != null || scanBefore != null || scanAfter != null) return;
                addLoopAnnotation(loopHead.getAddress(), currentIterationStart, currentIterationEnd, beforeIterationCount, beforeIterationCount + afterIterationCount);
                ++outstandingRequests;
                LoopAnalyzer.analyze(getState().getSession(), function, currentIterationStart, currentIterationEnd, LoopListener.this);
            }

            private LoopAnalyzer.Exec loopHead;

            private ScanCountQuery countBefore;

            private ScanCountQuery countAfter;

            private ScanExecQuery scanBefore;

            private ScanExecQuery scanAfter;

            private long currentIterationStart;

            private long currentIterationEnd;

            private long beforeIterationCount;

            private long afterIterationCount;
        }

        ;
    }
}
