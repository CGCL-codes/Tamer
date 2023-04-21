package game;

public class HandValue {

    private HandType ht = HandType.HIGH_CARD;

    private byte[] kicker = { 0, 0, 0, 0 };

    private int value = 0;

    public Hand(HandType ht, int value) {
        this.ht = ht;
        this.value = value;
    }

    public Hand(HandType ht, byte[] kicker, int value) {
        this.ht = ht;
        this.value = value;
        setKicker(kicker);
    }

    private void setKicker(byte[] kicker) {
        switch(this.ht) {
            case ONE_PAIR:
                this.kicker = kicker;
                this.kicker[0] = kicker[0];
                this.kicker[1] = kicker[1];
                this.kicker[2] = kicker[2];
                this.kicker[3] = (byte) 0;
                break;
            case TWO_PAIRS:
            case FOUR_OF_A_KIND:
                this.kicker[0] = kicker[0];
                break;
            case HIGH_CARD:
                this.value = kicker[0];
                this.kicker[0] = kicker[1];
                this.kicker[1] = kicker[2];
                this.kicker[2] = kicker[3];
                this.kicker[3] = kicker[4];
                break;
            case THREE_OF_A_KIND:
                this.kicker[0] = kicker[0];
                this.kicker[1] = kicker[1];
                break;
        }
    }

    public HandType getHandType() {
        return ht;
    }

    public int getHandValue() {
        return value;
    }

    public byte[] getKicker() {
        return kicker;
    }

    public int compareTo(HandType o) {
        return ht.compareTo(o);
    }

    public String kToString() {
        return (kicker[0] + ", " + kicker[1] + ", " + kicker[2] + ", " + kicker[3]);
    }

    public String toString() {
        return (this.ht.toString() + " of value " + this.value + " Kickers: " + kToString());
    }
}
