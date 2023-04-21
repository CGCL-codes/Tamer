package edu.bsu.monopoly.comm;

public class GameStatus {

    public PlayerList playerList;

    public EstateList estateList;

    public EstateGroupList estateGroupList;

    public TradeList tradeList;

    private Player Me;

    private int myId;

    public GameStatus() {
        playerList = new PlayerList();
        estateList = new EstateList();
        estateGroupList = new EstateGroupList();
        tradeList = new TradeList();
        myId = -1;
    }

    public void setMyId(int id) {
        this.myId = id;
    }

    public Player getMe() {
        if (Me == null) Me = playerList.getPlayerById(myId);
        if (Me == null) System.out.println("Fuck");
        return Me;
    }
}
