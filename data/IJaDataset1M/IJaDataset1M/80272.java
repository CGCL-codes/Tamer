package tgreiner.amy.reversi.engine;

public class GameStage {

    public int getGameStage(final int discCount) {
        return Math.max(0, (discCount - 13) / 4);
    }

    public static int getNumStages() {
        return 13;
    }
}
