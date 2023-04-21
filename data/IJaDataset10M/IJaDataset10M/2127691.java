package mta.connect.four.game;

import mta.connect.four.network.NetworkGame;

public class RemotePlayer {

    NetworkGame networkGame;

    public RemotePlayer(NetworkGame networkGame) {
        this.networkGame = networkGame;
    }

    public void sendCurrMove() {
    }
}
