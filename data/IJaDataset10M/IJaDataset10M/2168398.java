package org.pokerengine;

/**
 * Interface for the player handling.
 * @author Julien Vivenot <jvivenot@users.sf.net>
 */
public interface Player {

    /**
     * Returns the amount of chips that the player have.
     * @return Amount of chips the player have.
     */
    int getChips();

    /**
     * Adds an amount of chips to the player stack.
     * @return Amount of chips the player have after the add.
     * @param newchips Amount of chips to add.
     */
    int addChips(int newchips);

    /**
     * Substract a certain amount of chips from the player stack.
     * @param oldchips Amount of chips to remove from the player chipstack.
     * @return Amount of chips the player still have.
     */
    int substractChips(int oldchips);

    /**
     * Returns the bet decided by the player. 
     * This function is called at each time the player needs to make a decision.
     * If the move is invalid, the player will be check/folded.
     * @param pot Object describing the pot. Contains every bet made by the other players.
     * @return A Pot.Bet instance. Contains the player id, and the amount of chips to put in the pot. 
     */
    Pot.Bet playHand(Pot pot);

    /**
     * Returns a short name describing the player.
     * @return Short name describing the player.
     */
    String getScreenName();

    /**
     * Returns a long name describing the player.
     * @return Long name describing the player.
     */
    String getLongName();

    /**
     * Add a Card to the Player hand, as a personal card
     * @param c Card to be added to the pocket hand.
     */
    void addCard(Card c);

    /**
     * Adds each card in the Cards instance to the player personal hand.
     * @param c Cards to be added.
     */
    void addCards(Cards c);

    /**
     * Adds a Card to the player community cards.
     * @param c Card to be added.
     */
    void addCommunityCard(Card c);

    /**
     * Adds every card in Cards instance to the player community cards.
     * @param c Cards to be added.
     */
    void addCommunityCards(Cards c);

    /**
     * Returns all the cards of the player, including community cards.
     * @return All the cards the player can use. Pocket + community.
     * For example, on the river, in texas holdem game, this would be a Cards instance which size would be 7.
     */
    Cards getAllCards();

    /**
     * Returns only a Cards instance containing every community card.
     * @return Community cards, in a Cards instance.
     */
    Cards getCommunityCards();

    /**
     * Returns a standard evalutation made through org.pokersource.eval.StandardEval.evalHigh
     * @return long describing the hand of the player. The higher the better.
     */
    long getStandardEvalHigh();

    /**
     * Returns a Cards instance containing every personal card.
     * @return All personal cards.
     */
    Cards getCards();

    /**
    * Returns player id.
    * @return Int representing the player.
    */
    int getPlayerID();

    /**
    * Returns a clone of this player.
    * @return An instance of Player exactly equaled to this, but cloned.
    */
    Player clone();

    /**
    * Returns the need to sit out of the player.
    * @return Has to be true if the player wants to be sit out and leave.
    */
    boolean getNeedToSitOut();
}
