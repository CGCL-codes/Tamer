package org.eyrene.jgames.ui;

import org.eyrene.jgames.core.*;

/**
 * <p>Title: GameUI.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 */
public interface GameUI {

    public void start();

    public GameView getGameView();

    public void setGameView(GameView gameView);

    public GameController getGameController();

    public void setGameController(GameController gameController);

    public GameFieldView getGameFieldView();

    public void setGameFieldView(GameFieldView gameFieldView);

    public GameFieldController getGameFieldController();

    public void setGameFieldController(GameFieldController gameFieldController);

    public PlayerView getPlayerView(int player);

    public void setPlayerView(int player, PlayerView playerView);

    public PlayerController getPlayerController(int player);

    public void setPlayerController(int player, PlayerController playerController);

    public GameMasterView getGameMasterView();

    public void setGameMasterView(GameMasterView gameMasterView);

    public GameMasterController getGameMasterController();

    public void setGameMasterController(GameMasterController gameMasterController);
}
