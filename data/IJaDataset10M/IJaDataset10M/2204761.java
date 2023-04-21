package net.sf.nebulacards.util;

import net.sf.nebulacards.main.*;

/**
 * Provide default implementations of the methods of NebulaUi.  When overriding
 * any of these methods, you must call the overridden method within
 * the overriding method (i.e. using <i>super</i>), otherwise the functionality
 * of this base class cannot be guaranteed.
 * @see net.sf.nebulacards.main.NebulaUi
 * @version 0.8
 * @author James Ranson
 */
public class UIAdapter implements NebulaUi {

    private UIListener uia_callbacks = null;

    private int uia_whereAmI;

    private PileOfCards uia_hand;

    private Player[] uia_players;

    /**
	 * Store the callback methods for access via getCallbacks().
	 * If overriding, call super.setCallbacks() somewhere in your method.
	 * @param callbacks Some object whose UIListener methods this UI should
	 * call in order to communicate with the server.
	 * @see #getCallbacks()
	 */
    public void setCallbacks(UIListener callbacks) {
        uia_callbacks = callbacks;
    }

    /**
	 * Retrieve the callbacks stored by setCallbacks()
	 * @return Some object whose UIListener methods this UI should
	 * call in order to communicate with the server.
	 * @see #setCallbacks(net.sf.nebulacards.main.UIListener)
	 */
    protected final UIListener getCallbacks() {
        return uia_callbacks;
    }

    /**
	 * Store the table position for access via getPosition().
	 * If overriding, call super.setPosition() somewhere in your method.
	 * @param where The player's table index.
	 * @see #getPosition()
	 */
    public void setPosition(int where) {
        uia_whereAmI = where;
    }

    /**
	 * Get the player's position at the table.
	 * @see #setPosition(int)
	 * @return The player's position at the table.
	 */
    public final int getPosition() {
        return uia_whereAmI;
    }

    /**
	 * Store the hand for access via getHand().  
	 * If overriding, call super.dealHand() somewhere in your method.
	 * @param h The player's new hand.
	 * @see #getHand()
	 */
    public void dealHand(PileOfCards h) {
        uia_hand = h;
    }

    /**
	 * Get the player's hand.
	 * @see #dealHand(PileOfCards)
	 * @return The hand the player received via the last call to dealHand(),
	 * or <i>null</i> if dealHand() has not yet been called.
	 */
    public final PileOfCards getHand() {
        return uia_hand;
    }

    /**
	 * Store the player data for access via getPlayers().
	 * If overriding, call super.dealHand() somewhere in your method.
	 * @see #getPlayers()
	 */
    public void setPlayers(Player[] p) {
        uia_players = p;
    }

    /**
	 * Get the player data stored by setPlayers().
	 * @see #setPlayers(Player[])
	 */
    public Player[] getPlayers() {
        return uia_players;
    }

    /**
	 * Submit a meta operation.  Shortcut for getCallbacks().submit().
	 */
    protected final void submit(MetaOperation mo) {
        getCallbacks().submit(mo);
    }

    /**
	 * Submit a bid.  Shortcut for getCallbacks().submitBid().
	 */
    protected final boolean submitBid(int bid) {
        return getCallbacks().submitBid(bid);
    }

    /**
	 * Submit a chat message.  Shortcut for getCallbacks().submitChat().
	 */
    protected final void submitChat(String s) {
        getCallbacks().submitChat(s);
    }

    /**
	 * Submit a pass.  Shortcut for getCallbacks().submitPass().
	 */
    protected final boolean submitPass(PileOfCards pass) {
        return getCallbacks().submitPass(pass);
    }

    /**
	 * Submit a play.  Shortcut for getCallbacks().submitPlay().
	 */
    protected final boolean submitPlay(PlayingCard play) {
        return getCallbacks().submitPlay(play);
    }

    /**
	 * Submit a response.  Shortcut for getCallbacks().submitResponse().
	 */
    protected final void submitResponse(String s) {
        getCallbacks().submitResponse(s);
    }

    /**
	 * Inform the server that you want to quit.  
	 * Shortcut for getCallbacks().wantToQuit().
	 */
    protected void wantToQuit() {
        getCallbacks().wantToQuit();
    }

    public void setTrump(int t, String n) {
    }

    public void respond(String query) {
    }

    public void setGameName(String n) {
    }

    public void setBid(int who, int bid) {
    }

    public void cardToTableau(int pos, PlayingCard c) {
    }

    public void clearTableau(int whoWonTrick) {
    }

    public void yourTurn() {
    }

    public void yourTurnToBid() {
    }

    public void yourTurnToPass(int howmany, int who) {
    }

    public void accepted() {
    }

    public void rejected() {
    }

    public void chat(String s) {
    }

    public void endGame(GameResult gr) {
    }

    public void endHand() {
    }

    public void booted(String why) {
    }

    public void playedSoFar(PileOfCards bp) {
    }

    public void miscInfo(Object o) {
    }
}
