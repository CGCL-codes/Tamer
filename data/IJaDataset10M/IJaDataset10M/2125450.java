package poker.beliefs;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import poker.beliefs.*;
import jadex.adapter.fipa.AgentIdentifier;

/**
 *  Editable Java class for concept <code>Table</code> of poker ontology.
 */
public class Table extends TableData {

    private Hashtable<AgentIdentifier, Bet> betSet;

    private Hashtable<AgentIdentifier, Hand> showSet;

    public static final int REGISTER = 0, PREFLOP = 1, FLOP = 2, TURN = 3, RIVER = 4, WINNER_CHECK = 5;

    public static final int SMALLBLIND = 5, BIGBLIND = 10;

    /** 
	 *  Default Constructor. <br>
	 *  Create a new <code>Table</code>.
	 */
    public Table() {
    }

    public Table(int smallBlind, int bigBlind) {
        super();
        this.bigblind = bigBlind;
        this.smallblind = smallBlind;
        this.betSet = new Hashtable<AgentIdentifier, Bet>();
        this.showSet = new Hashtable<AgentIdentifier, Hand>();
    }

    /** 
	 *  Clone Constructor. <br>
	 *  Create a new <code>Table</code>.<br>
	 *  Copy all attributes from <code>proto</code> to this instance.
	 *
	 *  @param proto The prototype instance.
	 */
    public Table(Table proto) {
        setBets(proto.getBets());
        setBigBlind(proto.getBigBlind());
        setCommCards(proto.getCommCards());
        setNumHand(proto.getNumHand());
        setNumRound(proto.getNumRound());
        setNumShows(proto.getNumShows());
        setPlayers(proto.getPlayers());
        setShowdown(proto.getShowdown());
        setSmallBlind(proto.getSmallBlind());
        setTurn(proto.getTurn());
    }

    public void setBet(AgentIdentifier player, Bet bet) {
        betSet.put(player, bet);
    }

    public Bet getBet(AgentIdentifier player) {
        return betSet.get(player);
    }

    public Hashtable<AgentIdentifier, Bet> getBetSet() {
        return betSet;
    }

    public Hashtable<AgentIdentifier, Hand> getShowSet() {
        return showSet;
    }

    public void setBestHand(AgentIdentifier player, Hand hand) {
        showSet.put(player, hand);
        int aux = getNumShows();
        aux++;
        setNumShows(aux);
    }

    public Hand getBestHand(AgentIdentifier player) {
        return showSet.get(player);
    }

    public void nextHand() {
        int aux = getNumHand();
        aux++;
        setNumHand(aux);
    }

    public void nextRound() {
        int aux = getNumRound();
        aux++;
        setNumRound(aux);
    }

    public List getListPlayers() {
        return this.players;
    }

    /**
	 *  Get a string representation of this <code>Table</code>.
	 *  @return The string representation.
	 */
    public String toString() {
        return "Table(" + ")";
    }

    /** 
	 *  Get a clone of this <code>Table</code>.
	 *  @return a shalow copy of this instance.
	 */
    public Object clone() {
        return new Table(this);
    }

    /** 
	 *  Test the equality of this <code>Table</code> 
	 *  and an object <code>obj</code>.
	 *
	 *  @param obj the object this test will be performed with
	 *  @return false if <code>obj</code> is not of <code>Table</code> class,
	 *          true if all attributes are equal.   
	 */
    public boolean equals(Object obj) {
        if (obj instanceof Table) {
            Table cmp = (Table) obj;
            if (getBets() != cmp.getBets() && (getBets() == null || !getBets().equals(cmp.getBets()))) return false;
            if (getBigBlind() != cmp.getBigBlind()) return false;
            if (getCommCards() != cmp.getCommCards() && (getCommCards() == null || !getCommCards().equals(cmp.getCommCards()))) return false;
            if (getNumHand() != cmp.getNumHand()) return false;
            if (getNumRound() != cmp.getNumRound()) return false;
            if (getNumShows() != cmp.getNumShows()) return false;
            if (getPlayers() != cmp.getPlayers() && (getPlayers() == null || !getPlayers().equals(cmp.getPlayers()))) return false;
            if (getShowdown() != cmp.getShowdown() && (getShowdown() == null || !getShowdown().equals(cmp.getShowdown()))) return false;
            if (getSmallBlind() != cmp.getSmallBlind()) return false;
            if (getTurn() != cmp.getTurn() && (getTurn() == null || !getTurn().equals(cmp.getTurn()))) return false;
            return true;
        }
        return false;
    }
}
