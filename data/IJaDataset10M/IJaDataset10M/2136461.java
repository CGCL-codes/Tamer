package ecbm.game.Hosting;

import ecbm.entity.bomberman.BombermanType;
import ecbm.game.BombermanInfo;
import ecbm.game.engine.GenericGame;
import ecbm.game.engine.LocalGame;
import java.util.ArrayList;

/**
 *
 * @author ito
 */
public class LocalHostGame extends GenericHostGame {

    private ArrayList<BombermanInfo> bmInfo = new ArrayList<BombermanInfo>();

    public void addBomberman(String name, BombermanType type) {
        bmInfo.add(new BombermanInfo(name, type, false));
    }

    public LocalGame getLocalGame() {
        return new LocalGame(_setupWorld(), _setupField(), bmInfo, _turnTimeInSeconds, _numberOfWins);
    }

    @Override
    public GenericGame getGenericGame() {
        return getLocalGame();
    }
}
