package net.sf.gamebay.game.board.impl.my;

import net.sf.gamebay.game.board.Board;
import net.sf.gamebay.game.board.impl.RowImpl;

/**
 * TODO
 */
public class RowMyImpl extends RowImpl {

    /**
     * @see game.board.impl.ALineImpl#getIdx()
     */
    @Override
    public int getIdx() {
        return ((Board) eContainer()).getRows().indexOf(this);
    }
}
