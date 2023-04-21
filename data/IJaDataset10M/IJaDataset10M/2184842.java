package cardgames.control.maumau.messages;

/**
 * Indicates some player did not put down a card but intermitted.
 */
public class PenaltyCardsSent extends MauMauTableMessage {

    /**
     * ID used to make instances of this class transferable over the net.
     * <p>
     * @see <a href="http://dev.root1.de/wiki/simon/Sample_helloworld" target="_BLANK">SIMON-sample</a>
     */
    @SuppressWarnings("deprecation")
    static final long serialVersionUID = (new java.util.Date(2011, 5, 11)).getTime();

    String reason;

    int cards;

    public PenaltyCardsSent(String penalizedPlayersName, int cardsCount, String reason) {
        super(penalizedPlayersName);
        this.reason = new String(reason);
        this.cards = cardsCount;
    }

    public String toString() {
        return name + " got " + cards + " penalty-cards, reason: " + reason;
    }

    public String getReason() {
        return reason;
    }

    public String getPenalizedName() {
        return name;
    }

    public int getPenaltyCradsCount() {
        return cards;
    }
}
