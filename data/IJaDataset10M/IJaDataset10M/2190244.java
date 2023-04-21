package CardServer;

/**
 * User: shmalikov.sergey
 * Date: 11.08.2007
 */
public class CardFilter {

    private String cardType;

    private String cardCommand;

    private String cardExactPower;

    private String cardLessPower;

    private String cardGreaterPower;

    private String rotated;

    private String cardAttrib;

    private String filterInverse;

    private String cardDamaged;

    private String cardHaveNotAttrib;

    public CardFilter generateCardFilter(Card card, Integer ability) {
        CardFilter cardFilter = new CardFilter();
        cardFilter.setCardType(card.getNumAbilityKey(ability, Card.ActionCard));
        cardFilter.setCardCommand(card.getNumAbilityKey(ability, Card.ActionCardCommand));
        cardFilter.setCardAttrib(card.getNumAbilityKey(ability, Card.ActionCardAttrib));
        cardFilter.setCardExactPower(card.getNumAbilityKey(ability, Card.ActionCardExactPower));
        cardFilter.setRotated(card.getNumAbilityKey(ability, Card.ActionCardRotated));
        cardFilter.setCardLessPower(card.getNumAbilityKey(ability, Card.ActionCardLessPower));
        cardFilter.setCardGreaterPower(card.getNumAbilityKey(ability, Card.ActionCardGreaterPower));
        cardFilter.setFilterInverse(card.getNumAbilityKey(ability, Card.ActionCardFilterInverse));
        cardFilter.setCardDamaged(card.getNumAbilityKey(ability, Card.ActionCardDamaged));
        cardFilter.setCardHaveNotAttrib(card.getNumAbilityKey(ability, Card.ActionCardHaveNotAttrib));
        return cardFilter;
    }

    public String getCardHaveNotAttrib() {
        return cardHaveNotAttrib;
    }

    public void setCardHaveNotAttrib(String cardHaveNotAttrib) {
        this.cardHaveNotAttrib = cardHaveNotAttrib;
    }

    public String getCardAttrib() {
        return cardAttrib;
    }

    public void setCardAttrib(String cardAttrib) {
        this.cardAttrib = cardAttrib;
    }

    public String getRotated() {
        return rotated;
    }

    public void setRotated(String rotated) {
        this.rotated = rotated;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardCommand() {
        return cardCommand;
    }

    public void setCardCommand(String cardCommand) {
        this.cardCommand = cardCommand;
    }

    public String getCardExactPower() {
        return cardExactPower;
    }

    public void setCardExactPower(String cardExactPower) {
        this.cardExactPower = cardExactPower;
    }

    public String getCardLessPower() {
        return cardLessPower;
    }

    public void setCardLessPower(String cardLessPower) {
        this.cardLessPower = cardLessPower;
    }

    public String getCardGreaterPower() {
        return cardGreaterPower;
    }

    public void setCardGreaterPower(String cardGreaterPower) {
        this.cardGreaterPower = cardGreaterPower;
    }

    public String getFilterInverse() {
        return filterInverse;
    }

    public void setFilterInverse(String filterInverse) {
        this.filterInverse = filterInverse;
    }

    public String getCardDamaged() {
        return cardDamaged;
    }

    public void setCardDamaged(String cardDamaged) {
        this.cardDamaged = cardDamaged;
    }

    public String toString() {
        return ("\n[Card attrib:" + cardAttrib + "\nCard type:" + cardType + "\nRotated:" + rotated + "\nCard command:" + cardCommand + "]\n");
    }
}
