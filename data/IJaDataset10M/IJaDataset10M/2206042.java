package icescrum2.dao.model;

public class PlanningPokerCard {

    private int cardValue;

    public PlanningPokerCard(int cardValue) {
        this.cardValue = cardValue;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    public String getDisplay() {
        if (cardValue == PlanningPokerGame.SPECIALCARD_HALF_NUMBER) return "1/2"; else if (cardValue <= PlanningPokerGame.SPECIALCARD_UNKNOW_NUMBER) return "?"; else return String.valueOf(this.cardValue);
    }
}
