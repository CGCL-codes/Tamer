package bbalc.core.actions.player;

import bbalc.com.codec.IPlayerCommand;
import bbalc.core.actions.Action;
import bbalc.tools.MessageFactory;

/**
 * represents a Handoff Action.
 */
public class Handoff extends Action {

    int receiverNum;

    boolean receiverIsTeamHome;

    public Handoff(int num, boolean isTeamHome, int receiverNum, boolean receiverIsTeamHome) {
        super(IPlayerCommand.PLAYER_HANDOFF, num, isTeamHome);
        this.receiverNum = receiverNum;
        this.receiverIsTeamHome = receiverIsTeamHome;
    }

    public String toCMDString() {
        return IPlayerCommand.PLAYER_HANDOFF + ":" + MessageFactory.getPlayerNumber(this.getNumber(), this.isTeamHome()) + ":" + MessageFactory.getPlayerNumber(receiverNum, receiverIsTeamHome);
    }

    public String toString() {
        return "(" + this.getTypeString() + ") num=" + this.getNumber() + " isTeamHome=" + this.isTeamHome() + " vs receiverNum=" + receiverNum + " receiverIsTeamHome=" + receiverIsTeamHome;
    }
}
