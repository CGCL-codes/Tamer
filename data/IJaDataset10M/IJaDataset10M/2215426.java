package org.nakedobjects.viewer.skylark.text;

import org.nakedobjects.viewer.skylark.Location;

public class TextSelection {

    private final CursorPosition cursor;

    private final CursorPosition start;

    public TextSelection(CursorPosition cursor, CursorPosition start) {
        this.cursor = cursor;
        this.start = start;
    }

    /**
     * Determine if the selection is back to front. Returns true if the cursor postion is before
     * the start postion.
     */
    private boolean backwardSelection() {
        return cursor.isBefore(start);
    }

    public void extendTo(CursorPosition pos) {
        cursor.asFor(pos);
    }

    /**
     * extends the selection so the end point is the same as the cursor.
     */
    public void extendTo(Location at) {
        cursor.cursorAt(at);
    }

    public CursorPosition from() {
        return backwardSelection() ? cursor : start;
    }

    /**
     * returns true is a selection exists - if the start and end locations are not the same
     */
    public boolean hasSelection() {
        return !cursor.samePosition(start);
    }

    /**
     * clears the selection so nothing is selected. The start and end points are set to the same
     * values as the cursor.
     */
    public void resetTo(CursorPosition pos) {
        start.asFor(pos);
        cursor.asFor(pos);
    }

    public void selectSentence() {
        resetTo(cursor);
        start.home();
        cursor.end();
    }

    /**
     * set the selection to be for the word marked by the current cursor
     *  
     */
    public void selectWord() {
        resetTo(cursor);
        start.wordLeft();
        cursor.wordRight();
    }

    public CursorPosition to() {
        return backwardSelection() ? start : cursor;
    }

    public String toString() {
        return "Selection [from=" + start.getLine() + ":" + start.getCharacter() + ",to=" + cursor.getLine() + ":" + cursor.getCharacter() + "]";
    }
}
