package client.filter;

import client.ClientAction;

public class PlayerClientFilter extends ClientFilter {

    private Integer playerId = null;

    public PlayerClientFilter(boolean keep, Integer playerId) {
        super(keep);
        this.playerId = playerId;
    }

    @Override
    protected boolean match(ClientAction action) {
        return playerId.equals(action.getPlayerID());
    }
}
