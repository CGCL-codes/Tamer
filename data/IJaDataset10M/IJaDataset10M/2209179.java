package saf;

import saf.mvc.FighterModel;

public interface IArenaEngine {

    public FighterModel getCurrentFighter();

    public void nextTurn();

    public FighterModel getFightWinner();

    public Boolean isTheCase(String condition);

    public void doMoveAction(String action);

    public void doFightAction(String action);
}
