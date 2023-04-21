package frontend.test;

import backend.client.IClient;
import backend.constants.Constants;
import backend.constants.Orientation;
import backend.state.Board;
import backend.state.Player;
import backend.state.ships.Ships;
import backend.util.BackendException;

public class TestClient implements IClient {

    private Player mPlayer;

    private Board mBoard;

    public TestClient(String aid, Board aBoard) {
        mBoard = aBoard;
        mPlayer = new Player(aid, mBoard);
    }

    public void connect(String server, String port) {
        mPlayer.setMyTurn(true);
    }

    public void disconnect() {
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public void move(int x, int y) {
        boolean rValue = false;
        try {
            if (x <= 4) {
                mPlayer.getOppBoard().setCoordinate(Constants.BOARD_HIT, x, y);
                rValue = true;
            } else {
                mPlayer.getOppBoard().setCoordinate(Constants.BOARD_MISS, x, y);
                rValue = false;
            }
        } catch (BackendException e) {
            e.printStackTrace();
        }
        mPlayer.setMyTurn(rValue);
        if (!rValue) {
            Thread t = new Thread(new TestOpponent(mPlayer));
            t.start();
        }
    }

    public void waitForTurn() {
    }

    public void signalReadiness() {
    }
}
