package org.lokee.punchcard.config.criteria.handler.preparedstatement;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.lokee.punchcard.Card;
import org.lokee.punchcard.Deck;
import org.lokee.punchcard.PunchCardUtil;
import org.lokee.punchcard.config.CardConfig;
import org.lokee.punchcard.config.DeckConfig;
import org.lokee.punchcard.config.criteria.CriteriaException;
import org.lokee.punchcard.config.criteria.ICriteriaMessages;
import org.lokee.punchcard.config.criteria.ICriterion;
import org.lokee.punchcard.config.criteria.handler.ICriteriaHandler;
import org.lokee.punchcard.persistence.jdbc.SQLNull;
import org.lokee.punchcard.util.ILogSeverity;

public class PreparedStatementCriteriaHandler implements ICriteriaHandler {

    private List valueList = null;

    private StringBuffer sql = null;

    private Card card;

    private Deck deck;

    private CardConfig cardConfig;

    private DeckConfig deckConfig;

    public PreparedStatementCriteriaHandler(Card card, Deck deck, CardConfig cardConfig, DeckConfig deckConfig) {
        this();
        this.card = card;
        this.deck = deck;
        this.cardConfig = cardConfig;
        this.deckConfig = deckConfig;
    }

    public PreparedStatementCriteriaHandler() {
        sql = new StringBuffer();
        valueList = new ArrayList();
    }

    public void addSQL(String sql) {
        this.sql.append(sql);
    }

    public String getSQL() {
        return this.sql.toString();
    }

    public void handleCriterion(ICriterion criterion) throws CriteriaException {
        IPSCriterionHandler handler = PSCriteriaHandlerFactory.fetchCriterionHandler(criterion);
        handler.handleCriterion(this, criterion);
    }

    public void addValueObjects(Object ob) {
        valueList.add(ob);
    }

    public Object[] getAllValueObjects() {
        return valueList.toArray(new Object[valueList.size()]);
    }

    public Object resolveValue(ICriterion criterion, Object value, Class clazz) throws CriteriaException {
        if (value instanceof String) {
            try {
                if (clazz == null) {
                    clazz = getValueClass(criterion);
                }
            } catch (ClassNotFoundException e) {
                throw new CriteriaException(ICriteriaMessages.E_ERROR_CLASS_NOT_FOUND + " value(" + value + ") criterion(" + criterion + ")", ILogSeverity.FATAL, e);
            }
            if (clazz == null) {
                clazz = value.getClass();
            }
            value = PunchCardUtil.convertValueToObject(PunchCardUtil.translateValue(value.toString(), card, cardConfig, deck, deckConfig), clazz, criterion.getProperty("format"));
        }
        if (value == null && Date.class.isAssignableFrom(clazz)) {
            return new SQLNull(Types.TIMESTAMP, clazz);
        } else if (Date.class.isAssignableFrom(clazz)) {
            return new java.sql.Date(((Date) value).getTime());
        }
        return value;
    }

    private Class getValueClass(ICriterion criterion) throws ClassNotFoundException {
        String className = criterion.getProperty("value-class-name");
        if (className == null) {
            return null;
        }
        return Class.forName(className);
    }

    /**
	 * @return the card
	 */
    public Card getCard() {
        return card;
    }

    /**
	 * @param card the card to set
	 */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
	 * @return the cardConfig
	 */
    public CardConfig getCardConfig() {
        return cardConfig;
    }

    /**
	 * @param cardConfig the cardConfig to set
	 */
    public void setCardConfig(CardConfig cardConfig) {
        this.cardConfig = cardConfig;
    }

    /**
	 * @return the deck
	 */
    public Deck getDeck() {
        return deck;
    }

    /**
	 * @param deck the deck to set
	 */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
	 * @return the deckConfig
	 */
    public DeckConfig getDeckConfig() {
        return deckConfig;
    }

    /**
	 * @param deckConfig the deckConfig to set
	 */
    public void setDeckConfig(DeckConfig deckConfig) {
        this.deckConfig = deckConfig;
    }
}
