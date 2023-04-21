package ao.sw.engine.player;

import ao.sw.engine.board.Direction;
import ao.sw.engine.board.RelDirection;
import ao.sw.engine.board.BoardArrangement;
import ao.sw.engine.board.BoardLocation;

public interface MoveSpecifier {

    public Direction latestDirection();

    public Direction latestLatestDirection();

    public void clearDirection();

    public void setDirection(Direction whereToGo);

    public Direction setDirection(RelDirection whereToGo);

    public Direction setDirection(RelDirection whereToGo, BoardArrangement board, BoardLocation fromWhere);
}
