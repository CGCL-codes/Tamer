package fr.uha.ensisa.ir.walther.bluepoker.core;

import java.util.Vector;
import fr.uha.ensisa.ir.walther.bluepoker.Config;

public class Party {

    private Vector players;

    private Cards cards;

    private int smallBlindAmount;

    private int bigBlindAmount;

    private int pot;

    private Board board;

    private Turn currentTurn;

    public Party(Game game) {
        this.players = game.getOrderedPlayersList();
        this.cards = new Cards();
        this.smallBlindAmount = game.getSmallBlindAmount();
        this.bigBlindAmount = game.getBigBlindAmount();
        this.pot = 0;
        this.board = new Board();
        this.currentTurn = new Turn(this);
    }

    public void initializeParty() {
        this.cards.generateStack();
        for (int i = 0; i < 2; ++i) for (int j = 0; j < this.players.size(); ++j) try {
            getPlayer(j).addCard(this.cards.popCard());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnDone() {
        if (this.board.getCurrentState() == Config.STATE_RIVER) {
        } else nextTurn();
    }

    public void nextTurn() {
        switch(this.board.getCurrentState()) {
            case Config.STATE_PREFLOP:
                addCardsToBoard(Config.CARDS_REVEALED_AT_FLOP);
                this.currentTurn = new Turn(this);
                this.currentTurn.Start();
                break;
            case Config.STATE_FLOP:
                addCardsToBoard(Config.CARDS_REVEALED_AT_TURN);
                this.currentTurn = new Turn(this);
                this.currentTurn.Start();
                break;
            case Config.STATE_TURN:
                addCardsToBoard(Config.CARDS_REVEALED_AT_RIVER);
                this.currentTurn = new Turn(this);
                this.currentTurn.Start();
                break;
            default:
                break;
        }
    }

    public void firstTurn() {
        this.currentTurn = new Turn(this);
        this.currentTurn.Start();
    }

    public void addCardsToBoard(int count) {
        try {
            this.cards.popCard();
            for (int i = 0; i < count; ++i) this.board.addCard(this.cards.popCard());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(AbstractPlayer player) {
        if (this.players.contains(player)) this.players.removeElement(player);
    }

    public void removePlayer(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < this.players.size()) this.players.removeElementAt(playerIndex);
    }

    public AbstractPlayer getPlayer(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < this.players.size()) return (AbstractPlayer) this.players.elementAt(playerIndex);
        return null;
    }

    public int getPlayerIndex(AbstractPlayer player) {
        if (this.players.contains(player)) return this.players.indexOf(player);
        return -1;
    }

    public Vector getPlayers() {
        return this.players;
    }

    public Board getBoard() {
        return this.board;
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public int getSmallBlindAmount() {
        return smallBlindAmount;
    }

    public int getBigBlindAmount() {
        return bigBlindAmount;
    }
}
