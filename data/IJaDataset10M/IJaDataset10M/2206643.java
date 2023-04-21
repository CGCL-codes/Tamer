package it.chesslab.scoresheet;

import it.chesslab.chessboard.Color;
import it.chesslab.commons.ArrayLib;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**  */
public final class ScoresheetPosition {

    /**  */
    private final Scoresheet scoresheet;

    /**  */
    private final String fen;

    private final int activeColor;

    /**  */
    private final ScoresheetPosition parentPosition;

    /**  */
    private final ScoresheetMove parentMove;

    /**  */
    private final List moves = new ArrayList();

    /**  */
    private final Map childPositions = new HashMap();

    /**  */
    private String comment = null;

    /**  */
    private final Map tags = new HashMap();

    /**  */
    ScoresheetPosition(Scoresheet scoresheet, String fen, ScoresheetPosition parentPosition, ScoresheetMove parentMove) {
        this.scoresheet = scoresheet;
        this.fen = fen;
        int indexOfColorChar = fen.indexOf(' ') + 1;
        char colorChar = fen.charAt(indexOfColorChar);
        switch(colorChar) {
            case 'w':
                this.activeColor = Color.WHITE;
                break;
            case 'b':
                this.activeColor = Color.BLACK;
                break;
            default:
                throw new IllegalStateException();
        }
        this.parentPosition = parentPosition;
        this.parentMove = parentMove;
    }

    /**  */
    public final String getFen() {
        return this.fen;
    }

    /**  */
    public final int getActiveColor() {
        return this.activeColor;
    }

    /**  */
    public final ScoresheetPosition getParentPosition() {
        return this.parentPosition;
    }

    /**  */
    public final ScoresheetMove getParentMove() {
        return this.parentMove;
    }

    /**  */
    public ScoresheetPosition addMoveAsLast(String notation, String childFen) {
        return this.addMove(notation, childFen, this.moves.size());
    }

    /**  */
    private ScoresheetPosition addMove(String notation, String childFen, int index) {
        ScoresheetMove move = new ScoresheetMove(this.scoresheet, notation, this.activeColor);
        ScoresheetPosition childPosition = new ScoresheetPosition(this.scoresheet, childFen, this, move);
        this.moves.add(index, move);
        this.childPositions.put(notation, childPosition);
        this.scoresheet.setModified(true);
        this.scoresheet.addIndexedPosition(childPosition);
        return childPosition;
    }

    /**  */
    public final ScoresheetPosition getMainChildPosition() {
        return this.getChildPosition(0);
    }

    /**  */
    public final ScoresheetPosition getChildPosition(int index) {
        return this.getChildPosition(this.getMove(index).getNotation());
    }

    /**  */
    public final ScoresheetPosition getChildPosition(String notation) {
        return (ScoresheetPosition) this.childPositions.get(notation);
    }

    /**  */
    public final ScoresheetPosition[] getChildPositions() {
        ScoresheetMove[] moves = this.getMoves();
        ScoresheetPosition[] childPositions = new ScoresheetPosition[moves.length];
        for (int i = 0; i < moves.length; i++) {
            childPositions[i] = (ScoresheetPosition) this.childPositions.get(moves[i].getNotation());
        }
        return childPositions;
    }

    /**  */
    public final ScoresheetMove getMainMove() {
        return this.getMove(0);
    }

    /**  */
    public final ScoresheetMove getMove(int index) {
        return (ScoresheetMove) this.moves.get(index);
    }

    /**  */
    public final ScoresheetMove[] getMoves() {
        return (ScoresheetMove[]) ArrayLib.getArray(this.moves, ScoresheetMove.class);
    }

    /**  */
    public final int getNumberOfChildren() {
        return this.moves.size();
    }

    /**  */
    public final boolean isRootPosition() {
        return this.parentPosition == null;
    }

    /**  */
    public final boolean isLeafPosition() {
        return this.childPositions.size() == 0;
    }

    /**  */
    public final void setComment(String comment) {
        this.comment = comment;
        this.scoresheet.setModified(true);
    }

    /**  */
    public final String getComment() {
        return this.comment;
    }

    /**  */
    public final void setTag(String tag, Object value) {
        if (tag == null || tag.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (value == null) {
            this.tags.remove(tag);
        } else {
            this.tags.put(tag, value);
        }
        this.scoresheet.setModified(true);
    }

    /**  */
    public final Object getTag(String tag) {
        return this.tags.get(tag);
    }

    /**  */
    public final boolean existsTag(String tag) {
        return this.tags.containsKey(tag);
    }

    /**  */
    public final String[] getTagNames() {
        return (String[]) ArrayLib.getArrayOfKeys(this.tags, String.class);
    }

    /**  */
    public final int getNumberOfTags() {
        return this.tags.size();
    }

    /**  */
    public final Object removeTag(String tag) {
        if (tag == null || tag.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.scoresheet.setModified(true);
        return this.tags.remove(tag);
    }

    /**  */
    public final void removeTags() {
        this.tags.clear();
        this.scoresheet.setModified(true);
    }

    /**  */
    public void modify(ScoresheetPositionModifier modifier, boolean recursive) {
        modifier.modify(this);
        if (recursive) {
            ScoresheetPosition[] childPositions = (ScoresheetPosition[]) ArrayLib.getArrayOfValues(this.childPositions, ScoresheetPosition.class);
            for (int i = 0; i < childPositions.length; i++) {
                childPositions[i].modify(modifier, recursive);
            }
        }
    }
}
