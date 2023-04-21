package org.amse.grigory.dchess.kernel;

import java.io.IOException;

/**
 *
 * @author grigory
 */
public interface IModel {

    public String getRulesFile();

    public String getLastMove();

    public Move getMoveCoordinates(int index);

    public int getMovesCount();

    public int getHeight();

    public int getWidth();

    public String getName();

    public int getIllegalSquareNumber();

    public int getIllegalX(int index);

    public int getIllegalY(int index);

    public String getFigureName(int x, int y);

    public int getFigureColor(int x, int y);

    public int getColor();

    public Desk getDeskClone();

    public void setDesk(Desk desk);

    public void setRulesFile(String rulesFile);

    public void setTime(int time);

    public void setWhiteTime(int time);

    public void setBlackTime(int time);

    public void newGame() throws Exception;

    public void start();

    public int getWhiteTime();

    public int getBlackTime();

    public boolean isPaused();

    public void pause();

    public void continueGame();

    public void addListener(IModelListener listener);

    public void updateAll();

    public boolean canMove(int x, int y, int tx, int ty);

    public boolean makeMove(int x, int y, int tx, int ty);

    public int isWin();

    public Figure getWhiteFigure(int index);

    public Figure getBlackFigure(int index);

    public int getWhiteCount();

    public int getBlackCount();

    public void undo();

    public boolean canUndo();

    public void redo();

    public boolean canRedo();

    public int getCurrentMoveNumber();
}
