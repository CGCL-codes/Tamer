package org.lokee.punchcard.config.criteria.handler.preparedstatement;

import org.lokee.punchcard.PunchCardUtil;
import org.lokee.punchcard.config.criteria.CriteriaException;
import org.lokee.punchcard.config.criteria.ICriterion;
import org.lokee.punchcard.config.criteria.KeyToKeyCriterion;

public class KeyToKeyCriterionHandler implements IPSCriterionHandler {

    public void handleCriterion(PreparedStatementCriteriaHandler criteriaHandler, ICriterion criterion) throws CriteriaException {
        KeyToKeyCriterion ktokcriterion = (KeyToKeyCriterion) criterion;
        String key1 = PunchCardUtil.translateValue(ktokcriterion.getKey1(), criteriaHandler.getCard(), criteriaHandler.getCardConfig(), criteriaHandler.getDeck(), criteriaHandler.getDeckConfig());
        String key2 = PunchCardUtil.translateValue(ktokcriterion.getKey2(), criteriaHandler.getCard(), criteriaHandler.getCardConfig(), criteriaHandler.getDeck(), criteriaHandler.getDeckConfig());
        criteriaHandler.addSQL(" " + key1 + " " + ktokcriterion.getOperator() + " " + key2);
    }
}
