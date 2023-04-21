package poker.beliefs;

import poker.beliefs.*;
import jadex.adapter.fipa.AgentIdentifier;

/**
 *  Java class for concept Table of poker ontology.
 */
public abstract class TableData implements java.beans.BeanInfo {

    /** Attribute for slot bets. */
    protected java.util.List bets;

    /** Attribute for slot numHand. */
    protected int numhand;

    /** Attribute for slot turn. */
    protected AgentIdentifier turn;

    /** Attribute for slot bigBlind. */
    protected int bigblind;

    /** Attribute for slot numRound. */
    protected int numround;

    /** Attribute for slot players. */
    protected java.util.List players;

    /** Attribute for slot smallBlind. */
    protected int smallblind;

    /** Attribute for slot numShows. */
    protected int numshows;

    /** Attribute for slot commCards. */
    protected java.util.List commcards;

    /** Attribute for slot showdown. */
    protected java.util.List showdown;

    /**
	 *  Default Constructor. <br>
	 *  Create a new <code>Table</code>.
	 */
    public TableData() {
        this.bets = new java.util.ArrayList();
        this.players = new java.util.ArrayList();
        this.commcards = new java.util.ArrayList();
        this.showdown = new java.util.ArrayList();
    }

    /**
	 *  Get the bets of this Table.
	 * @return bets
	 */
    public Bet[] getBets() {
        return (Bet[]) bets.toArray(new Bet[bets.size()]);
    }

    /**
	 *  Set the bets of this Table.
	 * @param bets the value to be set
	 */
    public void setBets(Bet[] bets) {
        this.bets.clear();
        for (int i = 0; i < bets.length; i++) this.bets.add(bets[i]);
    }

    /**
	 *  Get an bets of this Table.
	 *  @param idx The index.
	 *  @return bets
	 */
    public Bet getBet(int idx) {
        return (Bet) this.bets.get(idx);
    }

    /**
	 *  Set a bet to this Table.
	 *  @param idx The index.
	 *  @param bet a value to be added
	 */
    public void setBet(int idx, Bet bet) {
        this.bets.set(idx, bet);
    }

    /**
	 *  Add a bet to this Table.
	 *  @param bet a value to be removed
	 */
    public void addBet(Bet bet) {
        this.bets.add(bet);
    }

    /**
	 *  Remove a bet from this Table.
	 *  @param bet a value to be removed
	 *  @return  True when the bets have changed.
	 */
    public boolean removeBet(Bet bet) {
        return this.bets.remove(bet);
    }

    /**
	 *  Get the numHand of this Table.
	 * @return numHand
	 */
    public int getNumHand() {
        return this.numhand;
    }

    /**
	 *  Set the numHand of this Table.
	 * @param numhand the value to be set
	 */
    public void setNumHand(int numhand) {
        this.numhand = numhand;
    }

    /**
	 *  Get the turn of this Table.
	 * @return turn
	 */
    public AgentIdentifier getTurn() {
        return this.turn;
    }

    /**
	 *  Set the turn of this Table.
	 * @param turn the value to be set
	 */
    public void setTurn(AgentIdentifier turn) {
        this.turn = turn;
    }

    /**
	 *  Get the bigBlind of this Table.
	 * @return bigBlind
	 */
    public int getBigBlind() {
        return this.bigblind;
    }

    /**
	 *  Set the bigBlind of this Table.
	 * @param bigblind the value to be set
	 */
    public void setBigBlind(int bigblind) {
        this.bigblind = bigblind;
    }

    /**
	 *  Get the numRound of this Table.
	 * @return numRound
	 */
    public int getNumRound() {
        return this.numround;
    }

    /**
	 *  Set the numRound of this Table.
	 * @param numround the value to be set
	 */
    public void setNumRound(int numround) {
        this.numround = numround;
    }

    /**
	 *  Get the players of this Table.
	 * @return players
	 */
    public AgentIdentifier[] getPlayers() {
        return (AgentIdentifier[]) players.toArray(new AgentIdentifier[players.size()]);
    }

    /**
	 *  Set the players of this Table.
	 * @param players the value to be set
	 */
    public void setPlayers(AgentIdentifier[] players) {
        this.players.clear();
        for (int i = 0; i < players.length; i++) this.players.add(players[i]);
    }

    /**
	 *  Get an players of this Table.
	 *  @param idx The index.
	 *  @return players
	 */
    public AgentIdentifier getPlayer(int idx) {
        return (AgentIdentifier) this.players.get(idx);
    }

    /**
	 *  Set a player to this Table.
	 *  @param idx The index.
	 *  @param player a value to be added
	 */
    public void setPlayer(int idx, AgentIdentifier player) {
        this.players.set(idx, player);
    }

    /**
	 *  Add a player to this Table.
	 *  @param player a value to be removed
	 */
    public void addPlayer(AgentIdentifier player) {
        this.players.add(player);
    }

    /**
	 *  Remove a player from this Table.
	 *  @param player a value to be removed
	 *  @return  True when the players have changed.
	 */
    public boolean removePlayer(AgentIdentifier player) {
        return this.players.remove(player);
    }

    /**
	 *  Get the smallBlind of this Table.
	 * @return smallBlind
	 */
    public int getSmallBlind() {
        return this.smallblind;
    }

    /**
	 *  Set the smallBlind of this Table.
	 * @param smallblind the value to be set
	 */
    public void setSmallBlind(int smallblind) {
        this.smallblind = smallblind;
    }

    /**
	 *  Get the numShows of this Table.
	 * @return numShows
	 */
    public int getNumShows() {
        return this.numshows;
    }

    /**
	 *  Set the numShows of this Table.
	 * @param numshows the value to be set
	 */
    public void setNumShows(int numshows) {
        this.numshows = numshows;
    }

    /**
	 *  Get the commCards of this Table.
	 * @return commCards
	 */
    public Card[] getCommCards() {
        return (Card[]) commcards.toArray(new Card[commcards.size()]);
    }

    /**
	 *  Set the commCards of this Table.
	 * @param commcards the value to be set
	 */
    public void setCommCards(Card[] commcards) {
        this.commcards.clear();
        for (int i = 0; i < commcards.length; i++) this.commcards.add(commcards[i]);
    }

    /**
	 *  Get an commCards of this Table.
	 *  @param idx The index.
	 *  @return commCards
	 */
    public Card getCommCard(int idx) {
        return (Card) this.commcards.get(idx);
    }

    /**
	 *  Set a commCard to this Table.
	 *  @param idx The index.
	 *  @param commcard a value to be added
	 */
    public void setCommCard(int idx, Card commcard) {
        this.commcards.set(idx, commcard);
    }

    /**
	 *  Add a commCard to this Table.
	 *  @param commcard a value to be removed
	 */
    public void addCommCard(Card commcard) {
        this.commcards.add(commcard);
    }

    /**
	 *  Remove a commCard from this Table.
	 *  @param commcard a value to be removed
	 *  @return  True when the commCards have changed.
	 */
    public boolean removeCommCard(Card commcard) {
        return this.commcards.remove(commcard);
    }

    /**
	 *  Get the showdown of this Table.
	 * @return showdown
	 */
    public Hand[] getShowdown() {
        return (Hand[]) showdown.toArray(new Hand[showdown.size()]);
    }

    /**
	 *  Set the showdown of this Table.
	 * @param showdown the value to be set
	 */
    public void setShowdown(Hand[] showdown) {
        this.showdown.clear();
        for (int i = 0; i < showdown.length; i++) this.showdown.add(showdown[i]);
    }

    /**
	 *  Get an showdown of this Table.
	 *  @param idx The index.
	 *  @return showdown
	 */
    public Hand getShowdown(int idx) {
        return (Hand) this.showdown.get(idx);
    }

    /**
	 *  Set a showdown to this Table.
	 *  @param idx The index.
	 *  @param showdown a value to be added
	 */
    public void setShowdown(int idx, Hand showdown) {
        this.showdown.set(idx, showdown);
    }

    /**
	 *  Add a showdown to this Table.
	 *  @param showdown a value to be removed
	 */
    public void addShowdown(Hand showdown) {
        this.showdown.add(showdown);
    }

    /**
	 *  Remove a showdown from this Table.
	 *  @param showdown a value to be removed
	 *  @return  True when the showdown have changed.
	 */
    public boolean removeShowdown(Hand showdown) {
        return this.showdown.remove(showdown);
    }

    /** The property descriptors, constructed on first access. */
    private java.beans.PropertyDescriptor[] pds = null;

    /**
	 *  Get the bean descriptor.
	 *  @return The bean descriptor.
	 */
    public java.beans.BeanDescriptor getBeanDescriptor() {
        return null;
    }

    /**
	 *  Get the property descriptors.
	 *  @return The property descriptors.
	 */
    public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
        if (pds == null) {
            try {
                pds = new java.beans.PropertyDescriptor[] { new java.beans.IndexedPropertyDescriptor("bets", this.getClass(), "getBets", "setBets", "getBet", "setBet"), new java.beans.PropertyDescriptor("numHand", this.getClass(), "getNumHand", "setNumHand"), new java.beans.PropertyDescriptor("turn", this.getClass(), "getTurn", "setTurn"), new java.beans.PropertyDescriptor("bigBlind", this.getClass(), "getBigBlind", "setBigBlind"), new java.beans.PropertyDescriptor("numRound", this.getClass(), "getNumRound", "setNumRound"), new java.beans.IndexedPropertyDescriptor("players", this.getClass(), "getPlayers", "setPlayers", "getPlayer", "setPlayer"), new java.beans.PropertyDescriptor("smallBlind", this.getClass(), "getSmallBlind", "setSmallBlind"), new java.beans.PropertyDescriptor("numShows", this.getClass(), "getNumShows", "setNumShows"), new java.beans.IndexedPropertyDescriptor("commCards", this.getClass(), "getCommCards", "setCommCards", "getCommCard", "setCommCard"), new java.beans.IndexedPropertyDescriptor("showdown", this.getClass(), "getShowdown", "setShowdown", "getShowdown", "setShowdown") };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pds;
    }

    /**
	 *  Get the default property index.
	 *  @return The property index.
	 */
    public int getDefaultPropertyIndex() {
        return -1;
    }

    /**
	 *  Get the event set descriptors.
	 *  @return The event set descriptors.
	 */
    public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
        return null;
    }

    /**
	 *  Get the default event index.
	 *  @return The default event index.
	 */
    public int getDefaultEventIndex() {
        return -1;
    }

    /**
	 *  Get the method descriptors.
	 *  @return The method descriptors.
	 */
    public java.beans.MethodDescriptor[] getMethodDescriptors() {
        return null;
    }

    /**
	 *  Get additional bean info.
	 *  @return Get additional bean info.
	 */
    public java.beans.BeanInfo[] getAdditionalBeanInfo() {
        return null;
    }

    /**
	 *  Get the icon.
	 *  @return The icon.
	 */
    public java.awt.Image getIcon(int iconKind) {
        return null;
    }

    /**
	 *  Load the image.
	 *  @return The image.
	 */
    public java.awt.Image loadImage(final String resourceName) {
        try {
            final Class c = getClass();
            java.awt.image.ImageProducer ip = (java.awt.image.ImageProducer) java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

                public Object run() {
                    java.net.URL url;
                    if ((url = c.getResource(resourceName)) == null) {
                        return null;
                    } else {
                        try {
                            return url.getContent();
                        } catch (java.io.IOException ioe) {
                            return null;
                        }
                    }
                }
            });
            if (ip == null) return null;
            java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
            return tk.createImage(ip);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
	 *  Get a string representation of this Table.
	 *  @return The string representation.
	 */
    public String toString() {
        return "Table(" + ")";
    }
}
