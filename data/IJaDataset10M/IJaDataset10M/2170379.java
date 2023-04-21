package lemmings;

import java.io.Serializable;

/**
 * Lemming feladat: Rep�l�.
 */
public class TaskFly extends LemmingTask implements Serializable {

    /**
     * Egyedi feladat t�pus azonos�t�.
     */
    public static final transient int TASK_ID = 4;

    public TaskFly() {
    }

    /**
     * Feladat el�felt�tel�nek vizsg�lata. Ha teljes�l aktiv�lja a feladatot.
     */
    public void checkCondition() {
        if (lemming.getMoveState().getDirection() == MoveState.DIR_DOWN && lemming.getMoveState().getMovedCellNum() > 2) {
            armed = true;
        }
    }

    /**
     * A feladat elv�geztet�se.
     */
    public void doTask() {
        if (lemming.getGameCell().getNeighbours()[MoveState.DIR_DOWN] instanceof GameCellStone || lemming.getGameCell().getNeighbours()[MoveState.DIR_DOWN] == null) {
            armed = false;
            finished = true;
            lemming.getMoveState().restoreLastHorizDir();
        } else {
            lemming.move();
        }
    }

    /**
     * A feladat egyedi azonos�t�j�t adja vissza.
     */
    public int getId() {
        return TASK_ID;
    }

    /**
     * toString() met�dus
     */
    public String toString() {
        return "fly";
    }
}
