package org.lokee.punchcard.webui;

import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.lokee.punchcard.Card;
import org.lokee.punchcard.CardDetail;
import org.lokee.punchcard.Constants;
import org.lokee.punchcard.Deck;
import org.lokee.punchcard.DeckDetail;
import org.lokee.punchcard.ExceededMaxSearchResultException;
import org.lokee.punchcard.FieldDetail;
import org.lokee.punchcard.IAccessContext;
import org.lokee.punchcard.IPunchCardMessages;
import org.lokee.punchcard.ISourceContext;
import org.lokee.punchcard.PunchCardException;
import org.lokee.punchcard.PunchCardManagerFactory;
import org.lokee.punchcard.PunchCardRunTimeException;
import org.lokee.punchcard.PunchCardUtil;
import org.lokee.punchcard.config.CardConfig;
import org.lokee.punchcard.config.DeckConfig;
import org.lokee.punchcard.persistence.ISessionContext;
import org.lokee.punchcard.util.ILogSeverity;
import org.lokee.punchcard.util.Utilities;

public class PunchCardController implements IUIController {

    public void initialize(String propertyFileName) throws UIControllerException {
        PunchCardManagerFactory.initializePunchCard(propertyFileName);
    }

    public boolean isDeleteable(CardDetail cardDetail) {
        return PunchCardUtil.hasExecutionAccess(Constants.EXECUTION_TYPE_DELETE, cardDetail.getCardConfig(), cardDetail.getAccessContext());
    }

    public boolean isUpdateable(CardDetail cardDetail) {
        return PunchCardUtil.hasExecutionAccess(Constants.EXECUTION_TYPE_UPDATE, cardDetail.getCardConfig(), cardDetail.getAccessContext());
    }

    public boolean isInsertable(CardDetail cardDetail) {
        return PunchCardUtil.hasExecutionAccess(Constants.EXECUTION_TYPE_INSERT, cardDetail.getCardConfig(), cardDetail.getAccessContext());
    }

    public boolean isSearchable(CardDetail cardDetail) {
        return PunchCardUtil.hasExecutionAccess(Constants.EXECUTION_TYPE_SEARCH, cardDetail.getCardConfig(), cardDetail.getAccessContext());
    }

    public boolean isDownloadable(CardDetail cardDetail) {
        return cardDetail.getCardConfig().isDownloadable();
    }

    public CardDetail getCardDetail(String deckName, String cardName, IAccessContext accessContext) throws UIControllerException {
        CardDetail cardDetail;
        if (deckName == null) {
            throw new UIControllerException(IPunchCardMessages.E_ERROR_ILLEGAL_ACTION + ": deckName can not be null", ILogSeverity.FATAL);
        }
        if (cardName == null) {
            try {
                cardDetail = PunchCardManagerFactory.getInstance().getFirstCardDetail(deckName, accessContext);
            } catch (PunchCardException e) {
                throw new UIControllerException(IUIControllerMessages.E_ERROR_GETTING_CARD + ": unable to get first card for deckName(" + deckName + ")", ILogSeverity.FATAL, e);
            }
        } else {
            try {
                cardDetail = PunchCardManagerFactory.getInstance().getCardDetail(deckName, cardName, accessContext);
            } catch (PunchCardException e) {
                throw new UIControllerException(IUIControllerMessages.E_ERROR_GETTING_CARD + ": unable to get card for cardName(" + cardName + ") deckName(" + deckName + ")", ILogSeverity.FATAL, e);
            }
        }
        return cardDetail;
    }

    public CardDetail[] search(CardDetail searchCriteriaCard, ISourceContext fieldMessages, IAccessContext accessContext) throws UIControllerException {
        resolveImports(searchCriteriaCard, accessContext);
        boolean isValidCardForSearch = true;
        try {
            isValidCardForSearch = PunchCardManagerFactory.getInstance().isValidCard(Constants.VALIDATION_TYPE_SEARCH, searchCriteriaCard.getCard(), searchCriteriaCard.getDeck(), searchCriteriaCard.getCardConfig(), searchCriteriaCard.getDeckConfig(), fieldMessages, accessContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_SEARCH_FOR_CARD + ":unable to validate card for search for card(" + searchCriteriaCard.getCard() + ")", ILogSeverity.FATAL, e);
        }
        if (!isValidCardForSearch) {
            return new CardDetail[] {};
        }
        try {
            return PunchCardManagerFactory.getInstance().searchForCards(searchCriteriaCard.getCard(), searchCriteriaCard.getDeck(), searchCriteriaCard.getCardConfig(), searchCriteriaCard.getDeckConfig(), accessContext);
        } catch (ExceededMaxSearchResultException e) {
            String message = PunchCardUtil.findResource("MAXIMUM_SEARCH_RESULT_SIZE.MESSAGE", searchCriteriaCard.getCardConfig(), searchCriteriaCard.getDeckConfig(), accessContext.getUserLocale(), "Exceeded Maximum ({0}) Result Size, number of records returned ({1}). Please refined your search criteria.");
            fieldMessages.addFieldData(Constants.APPLICATION_MESSAGE, Utilities.formatMessage(message, new String[] { String.valueOf(e.getMaximum()), String.valueOf(e.getActual()) }));
            return new CardDetail[] {};
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_SEARCH_FOR_CARD + ":unable to search for cards using(" + searchCriteriaCard.getCard() + ")", ILogSeverity.FATAL, e);
        }
    }

    public void refreshCardDetail(CardDetail cardDetail, ISourceContext data, IAccessContext accessContext) {
        PunchCardUtil.applySourceValues(cardDetail.getCard(), cardDetail.getCardConfig(), cardDetail.getDeck(), cardDetail.getDeckConfig(), data, accessContext);
    }

    public void resetCardDetail(CardDetail cardDetail, IAccessContext accessContext) throws UIControllerException {
        PunchCardUtil.resetCard(cardDetail.getCard(), cardDetail.getCardConfig(), cardDetail.getDeck(), cardDetail.getDeckConfig(), accessContext);
    }

    public void applyBinding(CardDetail cardDetail, IAccessContext accessContext) throws UIControllerException {
        PunchCardUtil.applyBinding(cardDetail.getCard(), cardDetail.getCardConfig(), cardDetail.getDeck(), cardDetail.getDeckConfig(), accessContext);
    }

    public void applyBindingToPrimaryKeys(CardDetail cardDetail, IAccessContext accessContext) throws UIControllerException {
        PunchCardUtil.applyBindingToPrimaryKeys(cardDetail.getCard(), cardDetail.getCardConfig(), cardDetail.getDeck(), cardDetail.getDeckConfig(), accessContext);
    }

    public boolean save(CardDetail cardDetail, ISourceContext fieldMessages, IAccessContext accessContext) throws UIControllerException {
        List<ISessionContext> sessionContexts;
        try {
            sessionContexts = PunchCardManagerFactory.getInstance().fetchSessionContexts(cardDetail.getCardConfig(), cardDetail.getDeckConfig());
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_SAVE_CARD + ": card(" + cardDetail.getCard() + ")", ILogSeverity.FATAL, e);
        }
        boolean rt = true;
        for (ISessionContext sessionContext : sessionContexts) {
            sessionContext.setAutoCommitSession(true);
            try {
                boolean temp = save(cardDetail, fieldMessages, accessContext, sessionContext);
                if (!temp && sessionContext.isCritical()) {
                    rt = temp;
                }
            } catch (Exception e) {
                sessionContext.rollbackSession();
                if (sessionContext.isCritical()) {
                    throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_SAVE_CARD + ": card(" + cardDetail.getCard() + ")", ILogSeverity.FATAL, e);
                }
                Utilities.getLogger(getClass()).error("Error, (Will not propagate since sessionContext is not critical) Unable to Save Cards(" + ToStringBuilder.reflectionToString(cardDetail.getCard(), ToStringStyle.SIMPLE_STYLE) + ")", e);
            } finally {
                sessionContext.closeSession();
            }
        }
        return rt;
    }

    public boolean save(CardDetail cardDetail, ISourceContext fieldMessages, IAccessContext accessContext, ISessionContext sessionContext) throws UIControllerException {
        try {
            if (!PunchCardManagerFactory.getInstance().isValidCard(Constants.VALIDATION_TYPE_STORE, cardDetail.getCard(), cardDetail.getDeck(), cardDetail.getCardConfig(), cardDetail.getDeckConfig(), fieldMessages, accessContext)) {
                return false;
            }
            PunchCardManagerFactory.getInstance().storeCard(cardDetail.getCard(), cardDetail.getDeck(), cardDetail.getCardConfig(), cardDetail.getDeckConfig(), accessContext, sessionContext);
            UIControllerFactory.getInstance().applyBinding(cardDetail, accessContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_SAVE_CARD + ": card(" + cardDetail.getCard() + ")", ILogSeverity.FATAL, e);
        }
        return true;
    }

    public CardDetail getNextCardDetail(CardDetail cardDetail, IAccessContext accessContext) throws UIControllerException {
        CardDetail nextCardDetail = null;
        try {
            nextCardDetail = PunchCardManagerFactory.getInstance().getNextCardDetail(cardDetail.getDeck(), cardDetail.getCard().getName(), accessContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_ERROR_GETTING_CARD + ": unable to get next card for card(" + cardDetail.getCard() + "), from deck(" + cardDetail.getDeck() + ")", ILogSeverity.FATAL, e);
        }
        if (nextCardDetail != null) {
            return nextCardDetail;
        }
        return null;
    }

    public int delete(CardDetail cardDetail, ISourceContext fieldMessages, IAccessContext accessContext) throws UIControllerException {
        ISessionContext sessionContext;
        try {
            sessionContext = PunchCardManagerFactory.getInstance().fetchSessionContext(cardDetail.getCardConfig(), cardDetail.getDeckConfig());
            sessionContext.setAutoCommitSession(true);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_DELETE_CARD + ": card(" + cardDetail.getCard() + ")", ILogSeverity.FATAL, e);
        }
        try {
            return delete(cardDetail, fieldMessages, accessContext, sessionContext);
        } finally {
            sessionContext.closeSession();
        }
    }

    public int delete(CardDetail cardDetail, ISourceContext fieldMessages, IAccessContext accessContext, ISessionContext sessionContext) throws UIControllerException {
        try {
            if (!PunchCardManagerFactory.getInstance().isValidCard(Constants.VALIDATION_TYPE_DELETE, cardDetail.getCard(), cardDetail.getDeck(), cardDetail.getCardConfig(), cardDetail.getDeckConfig(), fieldMessages, accessContext)) {
                return 0;
            }
            if (!PunchCardManagerFactory.getInstance().isValidCardPrimaryKeys(Constants.VALIDATION_TYPE_DELETE, cardDetail.getCard(), cardDetail.getDeck(), cardDetail.getCardConfig(), cardDetail.getDeckConfig(), fieldMessages, accessContext)) {
                return 0;
            }
            return PunchCardManagerFactory.getInstance().deleteCard(cardDetail.getCard(), cardDetail.getDeck(), cardDetail.getCardConfig(), cardDetail.getDeckConfig(), accessContext, sessionContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_DELETE_CARD + ": card(" + cardDetail.getCard() + ")", ILogSeverity.FATAL, e);
        }
    }

    public CardDetail getCardDetail(Deck deck, String cardName, IAccessContext accessContext) throws UIControllerException {
        try {
            return PunchCardManagerFactory.getInstance().getCardDetail(deck, cardName, accessContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_ERROR_GETTING_CARD + ": cardName(" + cardName + ") from deck(" + deck + ")", ILogSeverity.FATAL, e);
        }
    }

    public CardDetail getEvaluatedCardDetail(Deck deck, String cardName, ISourceContext messages, IAccessContext accessContext) throws UIControllerException {
        try {
            return PunchCardManagerFactory.getInstance().getEvaluatedCardDetail(deck, cardName, messages, accessContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_ERROR_GETTING_CARD + ": Error getting evaluated card for cardName(" + cardName + ") from deck(" + deck + ")", ILogSeverity.FATAL, e);
        }
    }

    public DeckDetail getDeckDetail(String deckName, IAccessContext accessContext) throws UIControllerException {
        try {
            return PunchCardManagerFactory.getInstance().getDeckDetail(deckName, accessContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_ERROR_GETTING_DECK + ": deckName(" + deckName + ")", ILogSeverity.FATAL, e);
        }
    }

    public void resolveImports(CardDetail cardDetail, IAccessContext accessContext) throws UIControllerException {
        try {
            PunchCardManagerFactory.getInstance().resolveImports(cardDetail.getCard(), cardDetail.getDeck(), cardDetail.getCardConfig(), cardDetail.getDeckConfig(), accessContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_RESOLVE_IMPORTS + ": card(" + cardDetail.getCard() + "), deck(" + cardDetail.getDeck() + ")", ILogSeverity.FATAL, e);
        }
    }

    public boolean isDeckAccessible(String deckName, IAccessContext accessContext) throws UIControllerException {
        try {
            return PunchCardManagerFactory.getInstance().isDeckAccessible(deckName, accessContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_CHECK_ACCESSIBILITY + ": for deckName(" + deckName + "), accessContext(" + accessContext + ")", ILogSeverity.FATAL, e);
        }
    }

    public CardConfig[] getCardConfigSequence(String deckName, IAccessContext accessContext) throws UIControllerException {
        try {
            return PunchCardManagerFactory.getInstance().getCardConfigSequence(deckName, accessContext);
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_UNABLE_TO_CONSTRUCT_CARD_SEQUENCE + ": for deckName(" + deckName + ")", ILogSeverity.FATAL, e);
        }
    }

    public String[] getAllRegisteredDeckNames() throws UIControllerException {
        try {
            return PunchCardManagerFactory.getInstance().fetchAllDeckNames();
        } catch (PunchCardException e) {
            throw new UIControllerException(IUIControllerMessages.E_ERROR_GETTING_DECK + ": trying to get all decknames", ILogSeverity.FATAL, e);
        }
    }

    public CardDetail constructNewCardDetail(Card card, Deck deck, CardConfig cardConfig, DeckConfig deckConfig, IAccessContext accessContext) throws UIControllerException {
        FieldDetail[] fieldDetails;
        try {
            fieldDetails = PunchCardUtil.getFieldDetailsForCard(cardConfig.getAllFieldConfigs(), card, cardConfig, deck, deckConfig, false, false, accessContext);
        } catch (PunchCardRunTimeException e) {
            throw new UIControllerException(IPunchCardMessages.E_ERROR_GETTING_CARD_FIELD_DETAIL + " : searchForCards : PunchCardException: deckName=(" + deck.getName() + ") cardName=(" + card.getName() + ")", ILogSeverity.FATAL, e);
        }
        return new CardDetail(card, deck, cardConfig, deckConfig, fieldDetails, accessContext);
    }

    public ISessionContext fetchSessionContext(CardConfig cardConfig, DeckConfig deckConfig) {
        try {
            return PunchCardManagerFactory.getInstance().fetchSessionContext(cardConfig, deckConfig);
        } catch (PunchCardException e) {
            throw new PunchCardRunTimeException(IPunchCardMessages.E_ERROR_GETTING_SESSION_CONTEXT + " : fetchSessionContext : PunchCardException: deckName=(" + deckConfig.getName() + ") cardName=(" + cardConfig.getName() + ")", ILogSeverity.FATAL, e);
        }
    }
}
