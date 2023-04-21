package Game.Stores;

import Game.Logic.GameLoop;

/**
 *
 * @author medv4380
 */
public class LogicStore extends Store {

    private static LogicStore single = new LogicStore();

    public static LogicStore get() {
        return single;
    }

    public GameLoop getGameLoop(int Index) {
        return (GameLoop) this.getObject(Index);
    }

    @Override
    public boolean remove(int Index) {
        try {
            getGameLoop(Index).stop();
        } catch (Exception e) {
            return false;
        }
        return super.remove(Index);
    }

    public boolean remove(GameLoop loop) {
        try {
            loop.stop();
        } catch (Exception e) {
            return false;
        }
        return super.remove(loop);
    }

    public boolean stopGameLoops() {
        try {
            for (int ctr = 0; ctr < getNumberOfObjects(); ctr++) {
                getGameLoop(ctr).stop();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean setFPS(int fps) {
        try {
            for (int ctr = 0; ctr < getNumberOfObjects(); ctr++) {
                getGameLoop(ctr).setTargetFPS(fps);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean startGameLoops() {
        try {
            for (int ctr = 0; ctr < getNumberOfObjects(); ctr++) {
                getGameLoop(ctr).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
