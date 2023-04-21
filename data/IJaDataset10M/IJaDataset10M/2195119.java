package org.axed.user.client;

import com.google.gwt.core.client.GWT;

/**
 * This pointer will change its position with document changes.
 * Be aware this one takes more CPU time with each change than using an Anchor,
 * but less when moved. Therefore e.g. used for the cursor.
 */
public class AxedAutoPointer extends AxedPointer implements AxedListener {

    /**
	 * Constructor.
	 */
    public AxedAutoPointer(AxedArea axed) {
        super(axed);
    }

    /**
	 * A portion from the document is deleted. Check if the pointer is in between or after.
	 */
    private void removeText(AxedInterval iv) {
        if (iv.isOneLined()) {
            if (line == iv.p1.line && col > iv.p1.col && col <= iv.p2.col) {
                col = iv.p1.col;
            } else if (line == iv.p1.line && col > iv.p2.col) {
                col -= iv.p2.col - iv.p1.col;
            } else {
                return;
            }
        } else if (line == iv.p1.line && col > iv.p1.col) {
            col = iv.p1.col;
        } else if (line > iv.p1.line && line < iv.p2.line) {
            set(iv.p1);
        } else if (line == iv.p2.line) {
            col += iv.p1.col - iv.p2.col;
            line = iv.p1.line;
        } else if (line > iv.p2.line) {
            line -= iv.p2.line - iv.p1.line;
        } else {
            return;
        }
        axed.moveCursor(this, AxedEvent.MOVETYPE_FOLLOWUP);
    }

    /**
	 * A portion from the document is deleted. Check if the pointer is in between or after.
	 */
    private void insertText(AxedInterval iv) {
        if (line < iv.p1.line) {
            return;
        }
        if (line == iv.p1.line) {
            if (col <= iv.p1.col) {
                return;
            }
            line += iv.p2.line - iv.p1.line;
            col += iv.p2.col - iv.p1.col;
            axed.moveCursor(this, AxedEvent.MOVETYPE_FOLLOWUP);
            return;
        }
        assert line > iv.p1.line;
        line += iv.p2.line - iv.p1.line;
        axed.moveCursor(this, AxedEvent.MOVETYPE_FOLLOWUP);
    }

    /**
	 * Handles events from the AxedArea to reposition the pointer
	 * due to text changes.
	 */
    public void onAxedEvent(AxedEvent event) {
        switch(event.type) {
            case AxedEvent.SPLIT_LINE:
                if (this.compare(event.getSplitLine().pnt) > 0) {
                    line++;
                    axed.moveCursor(this, AxedEvent.MOVETYPE_FOLLOWUP);
                }
                break;
            case AxedEvent.JOIN_LINE:
                if (line > event.getJoinLine().pnt.line) {
                    line--;
                    axed.moveCursor(this, AxedEvent.MOVETYPE_FOLLOWUP);
                }
                break;
            case AxedEvent.INSERT_TEXT:
                insertText(event.getInsertText().iv);
                break;
            case AxedEvent.REMOVE_TEXT:
                removeText(event.getRemoveText().iv);
                break;
            case AxedEvent.PASTE:
                break;
        }
    }
}
