package ui;

import javax.swing.text.*;
import reducedmodel.*;
import java.util.*;
import javax.swing.text.AbstractDocument.*;
import javax.swing.event.*;

/**
 * This implementation of the StyledDocument adds functionality related
 * to managing the document as a set of Segments. A segment is an abstract
 * grouping of elements in the document. For the purposes of code splitting,
 * a segment will be a set of one or more adjacent lines of text.
 * <p>
 * The second added function of this document is the ability to get a list 
 * of styles represented as HighlighStatus objects between two given offsets
 * in the document. We enforce that the two offsets do not cross segment
 * boundaries. This functionality will enable the views that render each 
 * element to color its own region of text correctly, giving the behavior
 * as the old ColoringView used in DrJava.
 * <p>
 * The old ColoringView was based off of the PlainView in swing. This view
 * worked off the notion that it was responsible for rendering ALL elements
 * in the document rather than fitting into the heirarchical structure used
 * in the StyledDocument. The new implementation of the ColoringView should
 * allow its functionality to be divided among the Elements. In the 
 * AbstrctDJDocuments in DrJava, this will require that the document implements
 * this functionality in a way that is compatible with the reduced model.
 * 
 * NOTE: The structure of the elements in the document are as follows:
 * <pre>
 * Section Element (root) <- only one
 *  |
 *  +- Paragraph Element (line) <- one for each line
 *     |
 *     +- Content Element (leaf) <- one or more
 * </pre>
 * 
 * Each paragraph element corresponds to a single line in the document.
 * Within that paragraph, there can be multiple elements that span that line.
 * Because of this, we can assume that index given to root.getElement
 * as well as the index returned from root.getElementIndex are both
 * zero-based line numbers.
 */
public class SegmentedDocument extends DefaultStyledDocument {

    BraceReduction _reduced = new ReducedModelControl();

    protected int _currentLocation = 0;

    protected EditorKit createDefaultEditorKit() {
        return new SegmentedEditorKit();
    }

    /**
   * This takes in an offset into the document and determines whether this 
   * offset lies within the first element in the segment. 
   * (At this point, a segment is being interpreted as one line, but it
   * shouldn't be difficult to make a segment be )
   * @param pos An offset into the document
   * @return Whether the given offset lies within the first element of 
   * the segment
   */
    public boolean isFirstInSegment(int pos) {
        int line = getLineFromPosition(pos);
        int idx = getDefaultRootElement().getElement(line - 1).getElementIndex(pos);
        return idx == 0;
    }

    public boolean isLineVisible(int pos) {
        synchronized (_reduced) {
            return _reduced.isVisible(pos);
        }
    }

    /** Add a character to the underlying reduced model. ONLY called from _reduced synchronized code!
   *  @param curChar the character to be added. */
    private void _addCharToReducedModel(char curChar) {
        _reduced.insertChar(curChar);
    }

    /** Get the current location of the cursor in the document.  Unlike the usual swing document model, 
   *  which is stateless, because of our implementation of the underlying reduced model, we need to 
   *  keep track of the current location.
   * @return where the cursor is as the number of characters into the document 
   */
    public int getCurrentLocation() {
        return _currentLocation;
    }

    /** Change the current location of the document
   *  @param loc the new absolute location 
   */
    public void setCurrentLocation(int loc) {
        readLock();
        try {
            synchronized (_reduced) {
                move(loc - _currentLocation);
            }
        } finally {
            readUnlock();
        }
    }

    /** The actual cursor movement logic.  Helper for setCurrentLocation(int).
   *  @param dist the distance from the current location to the new location.
   */
    public void move(int dist) {
        readLock();
        try {
            synchronized (_reduced) {
                int newLoc = _currentLocation + dist;
                if (newLoc < 0) newLoc = 0; else if (newLoc > getLength()) newLoc = getLength();
                _currentLocation = newLoc;
                _reduced.move(dist);
            }
        } finally {
            readUnlock();
        }
    }

    public HighlightStatus[] getStyleForRegion(int startPos, int endPos) {
        Vector<reducedmodel.HighlightStatus> v;
        readLock();
        try {
            synchronized (_reduced) {
                move(startPos - _currentLocation);
                v = _reduced.getHighlightStatus(startPos, endPos - startPos);
            }
        } finally {
            readUnlock();
        }
        return (HighlightStatus[]) v.toArray();
    }

    public int balanceBackward() {
        readLock();
        try {
            synchronized (_reduced) {
                return _reduced.balanceBackward();
            }
        } finally {
            readUnlock();
        }
    }

    public int balanceForward() {
        readLock();
        try {
            synchronized (_reduced) {
                return _reduced.balanceForward();
            }
        } finally {
            readUnlock();
        }
    }

    public void goToPreviousCloseCurlyBrace() {
    }

    public void goToPreviousOpenCurlyBrace() {
    }

    public int getSegmentIndexForPos(int pos) {
        return 0;
    }

    public ReducedModelState stateAtRelLocation(int dist) {
        readLock();
        try {
            synchronized (_reduced) {
                return _reduced.moveWalkerGetState(dist);
            }
        } finally {
            readUnlock();
        }
    }

    public ReducedModelState getStateAtCurrent() {
        readLock();
        try {
            synchronized (_reduced) {
                return _reduced.getStateAtCurrent();
            }
        } finally {
            readUnlock();
        }
    }

    public void resetReducedModelLocation() {
        readLock();
        try {
            synchronized (_reduced) {
                _reduced.resetLocation();
            }
        } finally {
            readUnlock();
        }
    }

    /**
   * Resolves an offset position in the document to the line number to 
   * which it corresponds. If the position is out of bounds, this method
   * will return the nearest line to the given position.
   * @param pos The offset in the document to resolve
   * @return The number of the line that contains the given offset 
   * (one based: 1,2,3...)
   */
    public int getLineFromPosition(int pos) {
        return getDefaultRootElement().getElementIndex(pos) + 1;
    }

    /**
   * Resolves a line number to the offset in the document to which it 
   * corresponds. If the line number is less than one, this snaps to 0,
   * and if it is greater than the number of lines in the document, it
   * returns the length of the document.
   * @param line The line number to resolve (one-based: 1,2,3...)
   * @return the offset in the document that that line starts on
   */
    public int getPositionFromLine(int line) {
        if (line < 1) return 0;
        if (line > getDefaultRootElement().getElementCount()) return getLength();
        return getDefaultRootElement().getElement(line - 1).getStartOffset();
    }

    public int foldAtCurrent() {
        System.out.println("In document getting ready to fold at location " + _currentLocation);
        int dist = _reduced.foldAtCurrent(this);
        System.out.println("Finished folding");
        return dist;
    }

    public void unfoldAtCurrent() {
    }

    public void foldView(View v) {
        synchronized (_reduced) {
            _reduced.hideView(v);
        }
    }

    protected void insertUpdate(AbstractDocument.DefaultDocumentEvent chng, AttributeSet attr) {
        super.insertUpdate(chng, attr);
        try {
            final int offset = chng.getOffset();
            final int length = chng.getLength();
            final String str = getText(offset, length);
            InsertCommand doCommand = new InsertCommand(offset, str);
            doCommand.run();
        } catch (BadLocationException ble) {
            throw new RuntimeException(ble);
        }
    }

    protected void removeUpdate(AbstractDocument.DefaultDocumentEvent chng) {
        final int offset = chng.getOffset();
        final int length = chng.getLength();
        super.removeUpdate(chng);
        Runnable doCommand = new RemoveCommand(offset, length);
        doCommand.run();
    }

    /** Inserts a string of text into the document.  Custom processing of the insert is not done here;
   *  that is done in {@link #insertUpdate}.
   */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        writeLock();
        try {
            synchronized (_reduced) {
                super.insertString(offset, str, a);
            }
        } finally {
            writeUnlock();
        }
    }

    /** Removes a block of text from the specified location.  We don't update the reduced model here; that happens
   *  in {@link #removeUpdate}.
   */
    public void remove(int offset, int len) throws BadLocationException {
        writeLock();
        try {
            synchronized (_reduced) {
                super.remove(offset, len);
            }
        } finally {
            writeUnlock();
        }
    }

    protected class InsertCommand implements Runnable {

        private final int _offset;

        private final String _text;

        public InsertCommand(final int offset, final String text) {
            _offset = offset;
            _text = text;
        }

        public void run() {
            readLock();
            try {
                synchronized (_reduced) {
                    _reduced.move(_offset - _currentLocation);
                    int len = _text.length();
                    for (int i = 0; i < len; i++) {
                        char curChar = _text.charAt(i);
                        _addCharToReducedModel(curChar);
                    }
                    _currentLocation = _offset + len;
                    _styleChanged();
                }
            } finally {
                readUnlock();
            }
        }
    }

    protected class RemoveCommand implements Runnable {

        private final int _offset;

        private final int _length;

        public RemoveCommand(final int offset, final int length) {
            _offset = offset;
            _length = length;
        }

        public void run() {
            readLock();
            try {
                synchronized (_reduced) {
                    setCurrentLocation(_offset);
                    _reduced.delete(_length);
                    _styleChanged();
                }
            } finally {
                readUnlock();
            }
        }
    }

    protected void _styleChanged() {
        writeLock();
        try {
            int length = getLength() - _currentLocation;
            DocumentEvent evt = new DefaultDocumentEvent(_currentLocation, length, DocumentEvent.EventType.CHANGE);
            fireChangedUpdate(evt);
        } finally {
            writeUnlock();
        }
    }

    public String getText() {
        readLock();
        try {
            return getText(0, getLength());
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        } finally {
            readUnlock();
        }
    }

    public void clear() {
        writeLock();
        try {
            remove(0, getLength());
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        } finally {
            writeUnlock();
        }
    }
}
