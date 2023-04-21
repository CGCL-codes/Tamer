package poker.beliefs;

public class Card extends CardData {

    public static final int SPADES = 0, HEARTS = 1, DIAMONDS = 2, CLUBS = 3;

    public static final int ACE = 14, JACK = 11, QUEEN = 12, KING = 13;

    /** 
   *  Default Constructor. <br>
   *  Create a new <code>Card</code>.
   */
    public Card() {
    }

    /** 
   *  Init Constructor. <br>
   *  Create a new <code>Card</code>.
   * @param suit    
   * @param value    
   */
    public Card(int cvalue, int suit) {
        setSuit(suit);
        setCValue(cvalue);
    }

    /** 
   *  Clone Constructor. <br>
   *  Create a new <code>Card</code>.<br>
   *  Copy all attributes from <code>proto</code> to this instance.
   *
   *  @param proto The prototype instance.
   */
    public Card(Card proto) {
        setSuit(proto.getSuit());
        setCValue(proto.getCValue());
    }

    public String getSuitAsString() {
        switch(this.getSuit()) {
            case SPADES:
                return "Spades";
            case HEARTS:
                return "Hearts";
            case DIAMONDS:
                return "Diamonds";
            case CLUBS:
                return "Clubs";
            default:
                return "??";
        }
    }

    public String getValueAsString() {
        switch(this.getCValue()) {
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "10";
            case 11:
                return "Jack";
            case 12:
                return "Queen";
            case 13:
                return "King";
            case 14:
                return "Ace";
            default:
                return "??";
        }
    }

    public String toString() {
        return getValueAsString() + " of " + getSuitAsString();
    }

    /** 
   *  Get a clone of this <code>Card</code>.
   *  @return a shalow copy of this instance.
   */
    public Object clone() {
        return new Card(this);
    }

    /** 
   *  Test the equality of this <code>Card</code> 
   *  and an object <code>obj</code>.
   *
   *  @param obj the object this test will be performed with
   *  @return false if <code>obj</code> is not of <code>Card</code> class,
   *          true if all attributes are equal.   
   */
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card cmp = (Card) obj;
            if (getSuit() != cmp.getSuit()) return false;
            if (getCValue() != cmp.getCValue()) return false;
            return true;
        }
        return false;
    }
}
