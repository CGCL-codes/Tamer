package de.boardgamesonline.bgo2.shogun.client;

import de.boardgamesonline.bgo2.shogun.controller.GUIController;
import de.boardgamesonline.bgo2.shogun.game.Banner;
import de.boardgamesonline.bgo2.shogun.game.Gamefield;
import de.boardgamesonline.bgo2.shogun.game.GameState;
import de.boardgamesonline.bgo2.shogun.game.Province;
import de.boardgamesonline.bgo2.shogun.game.Unit.UnitType;
import de.boardgamesonline.bgo2.shogun.gui.GUIFightBaPr;
import de.boardgamesonline.bgo2.shogun.gui.GUIKokuSpending;

/**
 * Der Shogun Client
 */
public class ShogunClient extends Thread {

    /**
	 * Das Spielfeld des aktuellen Spiels
	 */
    private Gamefield gamefield = new Gamefield();

    /**
	 * Der sessionId des Spielers
	 */
    private String sessionId;

    /**
	 * Der gameId des Spiels
	 */
    private String gameId;

    /**
	 * Die Anzahl der �bertragenen Gamefield-�nderungen
	 */
    private int changesFlag = -1;

    /**
	 * Die PlayerID
	 */
    private int playerID;

    /**
	 * @param sessionId
	 *            Der sessionId des Spielers
	 * @param gameId
	 *            Der gameId des Spiels
	 */
    public ShogunClient(String sessionId, String gameId) {
        this.sessionId = sessionId;
        this.gameId = gameId;
    }

    /**
	 * @see java.lang.Thread#run()
	 */
    @Override
    public void run() {
        while (true) {
            while (this.gamefield.getGamestate().getState().equals(GameState.State.Wait)) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gamefield.getGamestate().distributeProvinces();
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (Province pr : gamefield.getProvinces()) {
                    if (pr.getPlayer() != null) {
                        GUIController.colorizeProvince(pr.getPlayer(), pr);
                        GUIController.placeUnit(pr, UnitType.Spearman);
                        pr.addUnit(UnitType.Spearman);
                        System.out.println(pr.getProvinceName() + " an " + pr.getPlayer().getName());
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                gamefield.getGamestate().setState(GameState.State.Koku);
            }
            while (this.gamefield.getGamestate().getState().equals(GameState.State.Koku)) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new GUIKokuSpending(gamefield.getPlayers().get(1));
                gamefield.getGamestate().setState(GameState.State.KokuWait);
            }
            while (this.gamefield.getGamestate().getState().equals(GameState.State.KokuWait)) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Shogun.getFrame().deIconifyArmy();
            while (this.gamefield.getGamestate().getState().equals(GameState.State.DeliverUnits)) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Banner b = gamefield.getBanners().get(0)[0];
            Province p = gamefield.getProvince(4);
            new GUIFightBaPr(b, p);
            while (this.gamefield.getGamestate().getState().equals(GameState.State.Attack)) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Gibt das Spielfeld des aktuellen Spiel zur�ck
	 * 
	 * @return Das Spielfeld des Clienten
	 */
    public Gamefield getGamefield() {
        return gamefield;
    }

    /**
	 * holt sich die GameID
	 * 
	 * @return gameID
	 */
    public String getGameID() {
        return gameId;
    }

    /**
	 * holt die PlayerId
	 * 
	 * @return PlayerID
	 */
    public int getPlayerID() {
        return playerID;
    }

    /**
	 * setzt die PlayerID
	 * 
	 * @param playerID
	 *            setzt die PlayerID
	 */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
