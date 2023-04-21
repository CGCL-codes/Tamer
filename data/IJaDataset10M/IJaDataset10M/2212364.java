package org.dance.editor;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.text.AttributeSet;
import org.dance.editor.lexer.Lexer;
import org.dance.editor.lexer.Token;

/**
 * Run the Syntax Highlighting as a separate thread. Things that need to be
 * colored are messaged to the thread and put in a list.
 */
class Colorer extends Thread {

    /**
	 * A simple wrapper representing something that needs to be colored. Placed
	 * into an object so that it can be stored in a Vector.
	 */
    private static class RecolorEvent {

        public int position;

        public int adjustment;

        public RecolorEvent(int position, int adjustment) {
            this.position = position;
            this.adjustment = adjustment;
        }
    }

    /**
	 * Stores the document we are coloring. We use a WeakReference
	 * so that the document is eligible for garbage collection when
	 * it is no longer being used. At that point, this thread will
	 * shut down itself.
	 */
    private WeakReference document;

    /**
	 * Keep a list of places in the file that it is safe to restart the
	 * highlighting. This happens whenever the lexer reports that it has
	 * returned to its initial state. Since this list needs to be sorted and
	 * we need to be able to retrieve ranges from it, it is stored in a
	 * balanced tree.
	 */
    private TreeSet iniPositions = new TreeSet(DocPositionComparator.instance);

    /**
	 * As we go through and remove invalid positions we will also be finding
	 * new valid positions. Since the position list cannot be deleted from
	 * and written to at the same time, we will keep a list of the new
	 * positions and simply add it to the list of positions once all the old
	 * positions have been removed.
	 */
    private HashSet newPositions = new HashSet();

    /**
	 * Vector that stores the communication between the two threads.
	 */
    private volatile LinkedList events = new LinkedList();

    /**
	 * When accessing the linked list, we need to create a critical section.
	 * we will synchronize on this object to ensure that we don't get unsafe
	 * thread behavior.
	 */
    private Object eventsLock = new Object();

    /**
	 * The amount of change that has occurred before the place in the
	 * document that we are currently highlighting (lastPosition).
	 */
    private volatile int change = 0;

    /**
	 * The last position colored
	 */
    private volatile int lastPosition = -1;

    /**
	 * Creates the coloring thread for the given document.
	 * 
	 * @param document The document to be colored.
	 */
    public Colorer(HighlightedDocument document) {
        this.document = new WeakReference(document);
    }

    /**
	 * Tell the Syntax Highlighting thread to take another look at this
	 * section of the document. It will process this as a FIFO. This method
	 * should be done inside a docLock.
	 */
    public void color(int position, int adjustment) {
        if (position < lastPosition) {
            if (lastPosition < position - adjustment) {
                change -= lastPosition - position;
            } else {
                change += adjustment;
            }
        }
        synchronized (eventsLock) {
            if (!events.isEmpty()) {
                RecolorEvent curLast = (RecolorEvent) events.getLast();
                if (adjustment < 0 && curLast.adjustment < 0) {
                    if (position == curLast.position) {
                        curLast.adjustment += adjustment;
                        return;
                    }
                } else if (adjustment >= 0 && curLast.adjustment >= 0) {
                    if (position == curLast.position + curLast.adjustment) {
                        curLast.adjustment += adjustment;
                        return;
                    } else if (curLast.position == position + adjustment) {
                        curLast.position = position;
                        curLast.adjustment += adjustment;
                        return;
                    }
                }
            }
            events.add(new RecolorEvent(position, adjustment));
            eventsLock.notifyAll();
        }
    }

    /**
	 * The colorer runs forever and may sleep for long periods of time. It
	 * should be interrupted every time there is something for it to do.
	 */
    public void run() {
        while (document.get() != null) {
            try {
                RecolorEvent re;
                synchronized (eventsLock) {
                    while (events.isEmpty() && document.get() != null) {
                        eventsLock.wait(1000);
                    }
                    re = (RecolorEvent) events.removeFirst();
                }
                processEvent(re.position, re.adjustment);
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    private void processEvent(int position, int adjustment) {
        HighlightedDocument doc = (HighlightedDocument) document.get();
        if (doc == null) return;
        Lexer syntaxLexer = doc.getSyntaxLexer();
        DocumentReader documentReader = doc.getDocumentReader();
        Object docLock = doc.getDocumentLock();
        SortedSet workingSet;
        Iterator workingIt;
        DocPosition startRequest = new DocPosition(position);
        DocPosition endRequest = new DocPosition(position + Math.abs(adjustment));
        DocPosition dp;
        DocPosition dpStart = null;
        DocPosition dpEnd = null;
        try {
            workingSet = iniPositions.headSet(startRequest);
            dpStart = (DocPosition) workingSet.last();
        } catch (NoSuchElementException x) {
            dpStart = new DocPosition(0);
        }
        if (adjustment < 0) {
            workingSet = iniPositions.subSet(startRequest, endRequest);
            workingIt = workingSet.iterator();
            while (workingIt.hasNext()) {
                workingIt.next();
                workingIt.remove();
            }
        }
        workingSet = iniPositions.tailSet(startRequest);
        workingIt = workingSet.iterator();
        while (workingIt.hasNext()) {
            ((DocPosition) workingIt.next()).adjustPosition(adjustment);
        }
        workingSet = iniPositions.tailSet(dpStart);
        workingIt = workingSet.iterator();
        dp = null;
        if (workingIt.hasNext()) {
            dp = (DocPosition) workingIt.next();
        }
        try {
            Token t;
            boolean done = false;
            dpEnd = dpStart;
            synchronized (docLock) {
                syntaxLexer.reset(documentReader, 0, dpStart.getPosition(), 0);
                documentReader.seek(dpStart.getPosition());
                t = syntaxLexer.getNextToken();
            }
            newPositions.add(dpStart);
            while (!done && t != null) {
                if (t.getCharEnd() <= doc.getLength()) {
                    doc.setCharacterAttributes(t.getCharBegin() + change, t.getCharEnd() - t.getCharBegin(), TokenStyles.getStyle(t.getDescription()), true);
                    dpEnd = new DocPosition(t.getCharEnd());
                }
                lastPosition = (t.getCharEnd() + change);
                if (t.getState() == Token.INITIAL_STATE) {
                    while (dp != null && dp.getPosition() <= t.getCharEnd()) {
                        if (dp.getPosition() == t.getCharEnd() && dp.getPosition() >= endRequest.getPosition()) {
                            done = true;
                            dp = null;
                        } else if (workingIt.hasNext()) {
                            dp = (DocPosition) workingIt.next();
                        } else {
                            dp = null;
                        }
                    }
                    newPositions.add(dpEnd);
                }
                synchronized (docLock) {
                    t = syntaxLexer.getNextToken();
                }
            }
            workingIt = iniPositions.subSet(dpStart, dpEnd).iterator();
            while (workingIt.hasNext()) {
                workingIt.next();
                workingIt.remove();
            }
            workingIt = iniPositions.tailSet(new DocPosition(doc.getLength())).iterator();
            while (workingIt.hasNext()) {
                workingIt.next();
                workingIt.remove();
            }
            iniPositions.addAll(newPositions);
            newPositions.clear();
        } catch (IOException x) {
        }
        synchronized (docLock) {
            lastPosition = -1;
            change = 0;
        }
    }
}
