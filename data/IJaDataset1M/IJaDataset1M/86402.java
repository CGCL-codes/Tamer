package org.lokee.punchcard.plugins.suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.lokee.punchcard.Card;
import org.lokee.punchcard.Constants;
import org.lokee.punchcard.Deck;
import org.lokee.punchcard.Field;
import org.lokee.punchcard.IAccessContext;
import org.lokee.punchcard.PunchCardException;
import org.lokee.punchcard.PunchCardUtil;
import org.lokee.punchcard.config.CardConfig;
import org.lokee.punchcard.config.DeckConfig;
import org.lokee.punchcard.config.FieldConfig;
import org.lokee.punchcard.config.IPunchCardConfig;
import org.lokee.punchcard.config.LookupContextConfig;
import org.lokee.punchcard.config.NativeQueryConfig;
import org.lokee.punchcard.config.criteria.AndCriterion;
import org.lokee.punchcard.config.criteria.CriteriaConfig;
import org.lokee.punchcard.config.criteria.CriteriaException;
import org.lokee.punchcard.config.criteria.ICriterion;
import org.lokee.punchcard.config.criteria.KeyValueCriterion;
import org.lokee.punchcard.config.criteria.handler.preparedstatement.PreparedStatementCriteriaHandler;
import org.lokee.punchcard.persistence.ICardHandler;
import org.lokee.punchcard.persistence.IPersistentMessages;
import org.lokee.punchcard.persistence.ISessionContext;
import org.lokee.punchcard.persistence.PersistentException;
import org.lokee.punchcard.persistence.cardhandlers.CardHandlerContext;
import org.lokee.punchcard.rules.IRuleManager;
import org.lokee.punchcard.util.ILogSeverity;
import org.lokee.punchcard.util.Utilities;

/**
 * @author CLaguerre The class handles cards that does batch inserts. like
 *         assign a user with a bunch of roles. It cooresponds to the
 *         BatchCardConfig. The BatchCardConfig class has the proper bean
 *         properties to allow this class to do it's job.
 */
public class SuitCardHandler implements ICardHandler {

    public void storeCard(Card card, Deck deck, CardConfig cardConfig, DeckConfig deckConfig, CardHandlerContext context, ISessionContext sessionContext) throws PersistentException {
        SuitCardConfig suitCardConfig = (SuitCardConfig) cardConfig;
        String[] cardNames = suitCardConfig.getCardDescriptorNames();
        for (int i = 0; i < cardNames.length; i++) {
            if (suitCardConfig.isPersistable(cardNames[i])) {
                CardConfig includedCardConfig = deckConfig.getCardConfig(cardNames[i]);
                Card includedCard = deck.getCard(cardNames[i]);
                updateIncludedCard(includedCard, card, includedCardConfig, suitCardConfig);
                context.getPersistentManager().storeCard(includedCard, deck, includedCardConfig, deckConfig, PunchCardUtil.getPersistentContext(context), sessionContext);
                updateSuitCard(includedCard, card, includedCardConfig, suitCardConfig);
            }
        }
        sessionContext.commitSession();
    }

    public void initializeCard(Card card, Deck deck, CardConfig cardConfig, DeckConfig deckConfig, CardHandlerContext context, ISessionContext sessionContext) throws PersistentException {
        SuitCardConfig suitCardConfig = (SuitCardConfig) cardConfig;
        String[] cardNames = suitCardConfig.getCardDescriptorNames();
        for (int i = 0; i < cardNames.length; i++) {
            CardConfig includedCardConfig = deckConfig.getCardConfig(cardNames[i]);
            Card includedCard = deck.getCard(cardNames[i]);
            updateIncludedCard(includedCard, card, includedCardConfig, suitCardConfig);
            try {
                if (PunchCardUtil.isValidPrimaryKeys(Constants.EXECUTION_TYPE_SELECT, includedCard, includedCardConfig, deck, deckConfig, context.getRuleManager(), context.getAccessContext())) {
                    context.getPersistentManager().initializeCard(includedCard, deck, includedCardConfig, deckConfig, PunchCardUtil.getPersistentContext(context), sessionContext);
                }
            } catch (PunchCardException e) {
                throw new PersistentException(IPersistentMessages.E_ERROR_INITIALIZING_CARD + ": card(" + includedCard + ") cardConfig(" + includedCardConfig + ")", ILogSeverity.FATAL, e);
            }
            updateSuitCard(includedCard, card, includedCardConfig, suitCardConfig);
        }
    }

    public int deleteCard(Card card, Deck deck, CardConfig cardConfig, DeckConfig deckConfig, CardHandlerContext context, ISessionContext sessionContext) throws PersistentException {
        SuitCardConfig suitCardConfig = (SuitCardConfig) cardConfig;
        String[] cardNames = suitCardConfig.getCardDescriptorNames();
        int deletedCTR = 0;
        for (int i = cardNames.length - 1; i >= 0; i--) {
            if (suitCardConfig.isPersistable(cardNames[i])) {
                CardConfig includedCardConfig = deckConfig.getCardConfig(cardNames[i]);
                Card includedCard = deck.getCard(cardNames[i]);
                updateIncludedCard(includedCard, card, includedCardConfig, suitCardConfig);
                try {
                    if (PunchCardUtil.isValidPrimaryKeys(Constants.EXECUTION_TYPE_DELETE, includedCard, includedCardConfig, deck, deckConfig, context.getRuleManager(), context.getAccessContext())) {
                        deletedCTR = context.getPersistentManager().deleteCard(includedCard, deck, includedCardConfig, deckConfig, PunchCardUtil.getPersistentContext(context), sessionContext);
                        updateSuitCard(includedCard, card, includedCardConfig, suitCardConfig);
                    }
                } catch (PunchCardException e) {
                    throw new PersistentException(IPersistentMessages.E_ERROR_DELETE_CARD + ": card(" + includedCard + ") cardConfig(" + includedCardConfig + ")", ILogSeverity.FATAL, e);
                }
            }
        }
        return (deletedCTR > 0) ? 1 : 0;
    }

    public Card[] searchForCards(Card card, Deck deck, CardConfig cardConfig, DeckConfig deckConfig, CardHandlerContext context, ISessionContext sessionContext) throws PersistentException {
        SuitCardConfig suitCardConfig = (SuitCardConfig) cardConfig;
        List<Object> criteriaObjects = new ArrayList<Object>();
        LookupContextConfig lookupContext = null;
        if (cardConfig.getLookupContextConfig() != null) {
            NativeQueryConfig nativeQueryConfig = (NativeQueryConfig) PunchCardUtil.getValidDependent(cardConfig.getLookupContextConfig().getAllNativeQueryConfig(), card, deck);
            if (nativeQueryConfig != null) {
                lookupContext = new LookupContextConfig();
                lookupContext.addNativeQueryConfig(nativeQueryConfig);
            }
        }
        if (lookupContext == null) {
            try {
                lookupContext = buildLookupContext(card, deck, suitCardConfig, deckConfig, context, criteriaObjects, sessionContext);
            } catch (CriteriaException e) {
                throw new PersistentException(IPersistentMessages.E_ERROR_PERFORMING_SEARCH + ": trying to build lookup context - card(" + card + ") suitCardConfig(" + suitCardConfig + ")", ILogSeverity.FATAL, e);
            }
        }
        try {
            return context.getPersistentManager().detachedSearch(cardConfig.getName(), cardConfig.getDbKey(), cardConfig.getCatalog(), cardConfig.getSchema(), buildFullyQualifiedCardDescNames(suitCardConfig.getCardDescriptorNames(), deckConfig), lookupContext, criteriaObjects, sessionContext);
        } catch (PunchCardException e) {
            throw new PersistentException(IPersistentMessages.E_ERROR_PERFORMING_SEARCH + ": trying to execute detached search - lookupContext(" + lookupContext + ") card(" + card + ") suitCardConfig(" + suitCardConfig + ")", ILogSeverity.FATAL, e);
        }
    }

    private String[] buildFullyQualifiedCardDescNames(String[] names, DeckConfig deckConfig) {
        String[] qNames = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            CardConfig cardConfig = deckConfig.getCardConfig(names[i]);
            qNames[i] = PunchCardUtil.buildFullyQualifiedSQLTableName(cardConfig.getCatalog(), cardConfig.getSchema(), PunchCardUtil.getSinkName(cardConfig));
        }
        return qNames;
    }

    private LookupContextConfig buildLookupContext(Card card, Deck deck, SuitCardConfig cardConfig, DeckConfig deckConfig, CardHandlerContext context, List<Object> criteriaObjects, ISessionContext sessionContext) throws CriteriaException {
        StringBuffer buf = new StringBuffer(StringUtils.defaultString(cardConfig.getProperty("query.setup.script")));
        if (buf.length() > 0) {
            buf.append(" ");
        }
        buf.append("Select ");
        FieldConfig fieldConfigs[] = cardConfig.getAllFieldConfigs();
        for (int i = 0; i < fieldConfigs.length; i++) {
            String cardName = fieldConfigs[i].getCardName();
            if (StringUtils.isBlank(cardName)) {
                continue;
            }
            if (!PunchCardUtil.isExecutableField(new String[] { Constants.EXECUTION_TYPE_SELECT }, card.getField(fieldConfigs[i].getName()), card, deck, fieldConfigs[i], cardConfig, deckConfig, context.getRuleManager(), context.getAccessContext())) {
                continue;
            }
            SuitFieldConfig suitFieldConfig = (SuitFieldConfig) fieldConfigs[i];
            if (i > 0) {
                buf.append(", ");
            }
            if (StringUtils.isNotBlank(suitFieldConfig.getQueryScript())) {
                buf.append(suitFieldConfig.getQueryScript());
            } else {
                CardConfig selectedCardConfig = deckConfig.getCardConfig(cardName.split(",")[0]);
                buf.append(selectedCardConfig.getAlias()).append(".");
                buf.append(suitFieldConfig.getFieldName());
            }
            buf.append(" as \"").append(suitFieldConfig.getName()).append("\"");
        }
        buf.append(" From");
        CriteriaConfig config = new CriteriaConfig();
        String[] cardNames = cardConfig.getCardDescriptorNames();
        for (int i = 0; i < cardNames.length; i++) {
            CardConfig includedCardConfig = deckConfig.getCardConfig(cardNames[i]);
            Card includedCard = deck.getCard(cardNames[i]);
            updateIncludedCard(includedCard, card, includedCardConfig, cardConfig);
            if (i > 0) {
                buf.append(",");
            }
            buf.append(" ").append(PunchCardUtil.getCardFullNameForSQLStatement(includedCardConfig)).append(" ").append(includedCardConfig.getAlias());
            buildIncludedCardCriteriaConfig(config, includedCard, includedCardConfig, deck, deckConfig, context.getRuleManager(), context.getAccessContext());
        }
        buf.append(" Where ");
        buildCriteriaConfig(config, card, cardConfig, deck, deckConfig, context.getRuleManager(), context.getAccessContext());
        PreparedStatementCriteriaHandler handler = new PreparedStatementCriteriaHandler(card, deck, cardConfig, deckConfig);
        config.toSQL(handler);
        buf.append(handler.getSQL());
        criteriaObjects.addAll(Arrays.asList(handler.getAllValueObjects()));
        LookupContextConfig lookupContext = new LookupContextConfig();
        NativeQueryConfig queryConfig = new NativeQueryConfig();
        queryConfig.setQueryString(buf.toString());
        Utilities.getLogger(this.getClass()).debug("******sql(" + buf.toString() + ")");
        lookupContext.addNativeQueryConfig(queryConfig);
        return lookupContext;
    }

    public void populateCardWithMetaData(CardConfig cardConfig, DeckConfig deckConfig, CardHandlerContext context, ISessionContext sessionContext) throws PersistentException {
    }

    public void resetCard(Card card, Deck deck, CardConfig cardConfig, DeckConfig deckConfig, CardHandlerContext context, ISessionContext sessionContext) throws PersistentException {
        SuitCardConfig suitCardConfig = (SuitCardConfig) cardConfig;
        String[] cardNames = suitCardConfig.getCardDescriptorNames();
        for (int i = 0; i < cardNames.length; i++) {
            CardConfig includedCardConfig = deckConfig.getCardConfig(cardNames[i]);
            Card includedCard = deck.getCard(cardNames[i]);
            try {
                context.getPersistentManager().resetCard(includedCard, deck, includedCardConfig, deckConfig, PunchCardUtil.getPersistentContext(context), sessionContext);
            } catch (Exception e) {
                throw new PersistentException(IPersistentMessages.E_ERROR_INITIALIZING_CARD + ": card(" + includedCard + ") cardConfig(" + includedCardConfig + ")", ILogSeverity.FATAL, e);
            }
            updateSuitCard(includedCard, card, includedCardConfig, suitCardConfig);
        }
    }

    public boolean isCardExist(Card card, Deck deck, CardConfig cardConfig, DeckConfig deckConfig, CardHandlerContext context, ISessionContext sessionContext) throws PersistentException {
        if (card.isCardExistInRepository() && !card.isRecheckCardStatusInRepository()) {
            return card.isCardExistInRepository();
        }
        try {
            try {
                if (!PunchCardUtil.isValidPrimaryKeys(Constants.EXECUTION_TYPE_SELECT, card, cardConfig, deck, deckConfig, context.getRuleManager(), context.getAccessContext())) {
                    card.setCardExistInRepository(false);
                    return false;
                }
            } catch (PunchCardException e) {
                throw new PersistentException(IPersistentMessages.E_ERROR_INITIALIZING_CARD + ": card(" + card + ") cardConfig(" + cardConfig + ")", ILogSeverity.FATAL, e);
            }
            Card[] cards = searchForCards(card, deck, cardConfig, deckConfig, context, sessionContext);
            if (cards.length > 0) {
                card.setCardExistInRepository(true);
            } else {
                card.setCardExistInRepository(false);
            }
        } finally {
            card.setRecheckCardStatusInRepository(false);
        }
        return card.isCardExistInRepository();
    }

    private void updateIncludedCard(Card includedCard, Card suitCard, CardConfig includedCardConfig, SuitCardConfig suitCardConfig) {
        Field[] fields = includedCard.getAllFields();
        for (int i = 0; i < fields.length; i++) {
            SuitFieldConfig includedFieldConfig = (SuitFieldConfig) includedCardConfig.getFieldConfig(fields[i].getName());
            SuitFieldConfig suitFieldConfig = includedFieldConfig.getRelatedSuitFieldConfig();
            if (suitCard.getField(suitFieldConfig.getName()) == null) {
                continue;
            }
            if (PunchCardUtil.hasTokens(includedFieldConfig.getValue())) {
                continue;
            }
            Utilities.getLogger(this.getClass()).debug("field (" + fields[i] + ")");
            Utilities.getLogger(this.getClass()).debug("suitCard field(" + suitCard.getField(suitFieldConfig.getName()) + ")");
            Utilities.getLogger(this.getClass()).debug("suitFieldConfig(" + suitFieldConfig.getName() + ")");
            fields[i].setValues(suitCard.getField(suitFieldConfig.getName()).getValues());
        }
    }

    private void updateSuitCard(Card includedCard, Card suitCard, CardConfig includedCardConfig, SuitCardConfig suitCardConfig) {
        Field[] fields = includedCard.getAllFields();
        for (int i = 0; i < fields.length; i++) {
            SuitFieldConfig includedFieldConfig = (SuitFieldConfig) includedCardConfig.getFieldConfig(fields[i].getName());
            SuitFieldConfig suitFieldConfig = includedFieldConfig.getRelatedSuitFieldConfig();
            if (suitCard.getField(suitFieldConfig.getName()) == null) {
                continue;
            }
            suitCard.getField(suitFieldConfig.getName()).setValues(fields[i].getValues());
        }
    }

    private void buildCriteriaConfig(CriteriaConfig config, Card card, SuitCardConfig cardConfig, Deck deck, DeckConfig deckConfig, IRuleManager ruleManager, IAccessContext accessContext) {
        if (cardConfig.getJoinCriteriaConfig() != null && cardConfig.getJoinCriteriaConfig().getAllCriterion().length > 0) {
            if (config.getAllCriterion().length > 0) {
                config.addCriterion(new AndCriterion());
            }
            ICriterion[] criterions = cardConfig.getJoinCriteriaConfig().getAllCriterion();
            for (int i = 0; i < criterions.length; i++) {
                config.addCriterion(criterions[i]);
            }
        }
        LookupContextConfig searchConfig = cardConfig.getLookupContextConfig();
        if (searchConfig != null) {
            CriteriaConfig criteriaConfig = (CriteriaConfig) PunchCardUtil.getValidDependent(searchConfig.getAllCriteriaConfig(), card, deck);
            if (criteriaConfig != null && criteriaConfig.getAllCriterion().length > 0) {
                if (config.getAllCriterion().length > 0) {
                    config.addCriterion(new AndCriterion());
                }
                for (ICriterion criterion : criteriaConfig.getAllCriterion()) {
                    config.addCriterion(criterion);
                }
            }
        }
    }

    private void buildIncludedCardCriteriaConfig(CriteriaConfig config, Card card, CardConfig cardConfig, Deck deck, DeckConfig deckConfig, IRuleManager ruleManager, IAccessContext accessContext) {
        int criterionSize = config.getAllCriterion().length;
        FieldConfig[] fieldConfigs = cardConfig.getAllFieldConfigs();
        for (int i = 0; i < fieldConfigs.length; i++) {
            Field field = card.getField(fieldConfigs[i].getName());
            Utilities.getLogger(this.getClass()).debug(field);
            SuitFieldConfig suitFieldConfig = (SuitFieldConfig) fieldConfigs[i];
            if (field.getValue() != null && !"".equals(field.getValue()) && PunchCardUtil.isExecutableField(new String[] { Constants.EXECUTION_TYPE_SEARCH }, field, card, deck, fieldConfigs[i], cardConfig, deckConfig, ruleManager, accessContext) && !Constants.DISPLAY_TYPE_EXCLUDE.equals(PunchCardUtil.isValidDisplayField(field, fieldConfigs[i], card, cardConfig, deck, deckConfig, accessContext))) {
                String value = field.getValue();
                if (value != null && value.trim().matches(PunchCardUtil.TOKEN_PATTERN_STRING)) {
                    continue;
                }
                if (criterionSize > 0) {
                    config.addCriterion(new AndCriterion());
                }
                String wildCard = PunchCardUtil.findProperty("CARD_SEARCH_WILD_CARD", new IPunchCardConfig[] { fieldConfigs[i], cardConfig, deckConfig });
                if (String.class.isAssignableFrom(fieldConfigs[i].getExecuteClass()) && StringUtils.isNotBlank(wildCard)) {
                    addKVToCriteriaConfig(config, field, suitFieldConfig, card, cardConfig, deck, deckConfig, wildCard + value + wildCard, "like");
                } else if (String.class.isAssignableFrom(fieldConfigs[i].getExecuteClass()) && value.indexOf(Constants.QUERY_WILDCARD) > -1) {
                    addKVToCriteriaConfig(config, field, suitFieldConfig, card, cardConfig, deck, deckConfig, value, "like");
                } else {
                    addKVToCriteriaConfig(config, field, suitFieldConfig, card, cardConfig, deck, deckConfig, null, "=");
                }
                criterionSize++;
            }
        }
    }

    private void addKVToCriteriaConfig(CriteriaConfig criteriaConfig, Field field, SuitFieldConfig fieldConfig, Card card, CardConfig cardConfig, Deck deck, DeckConfig deckConfig, String value, String operator) {
        String[] names = fieldConfig.getCardNames();
        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                criteriaConfig.addCriterion(new AndCriterion());
            }
            CardConfig selectedCardConfig = deckConfig.getCardConfig(names[i]);
            String qualifiedCardName = selectedCardConfig.getAlias();
            KeyValueCriterion kv = new KeyValueCriterion();
            kv.setKey(qualifiedCardName + "." + fieldConfig.getFieldName());
            kv.setOperator(operator);
            if (value == null) {
                kv.setValue(PunchCardUtil.convertValuesToObject(field, card, deck, fieldConfig, cardConfig, deckConfig));
            } else {
                kv.setValue(value);
            }
            criteriaConfig.addCriterion(kv);
        }
    }
}
