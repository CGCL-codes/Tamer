package net.sf.gamebay.game.board.impl.my;

import net.sf.gamebay.game.board.Column;
import net.sf.gamebay.game.board.Row;
import net.sf.gamebay.game.board.impl.FieldImpl;

/**
 * TODO
 */
public class FieldMyImpl extends FieldImpl {

    /**
     * @see net.sf.gamebay.game.board.impl.FieldImpl#setRow(net.sf.gamebay.game.board.Row)
     */
    @Override
    public void setRow(Row newRow) {
        super.setRow(newRow);
        if (getColumn() != null) {
            initId();
        }
    }

    /**
     * @see net.sf.gamebay.game.board.impl.FieldImpl#setColumn(net.sf.gamebay.game.board.Column)
     */
    @Override
    public void setColumn(Column newColumn) {
        super.setColumn(newColumn);
        if (getRow() != null) {
            initId();
        }
    }

    private void initId() {
        if (!isSetId()) {
            char ch = (char) (getColumn().getIdx() + 97);
            String newId = String.valueOf(ch);
            newId += String.valueOf(getRow().getIdx() + 1);
            setId(newId);
        }
    }
}
