package com.centraview.mail;

import java.io.Serializable;

/**
 * Represents a single Email Rule Criteria as a Value Object.
 * @since v1.0.12
 */
public class RuleCriteriaVO implements Serializable {

    /** The ruleID of the email rule this RuleCriteriaVO is associated with. */
    private int ruleID = -1;

    /** The order in which this Criteria must be applied when matchin this rule to a message. */
    private int orderID = -1;

    /** The Expression Type of this Search Criteria. And/Or. */
    private String expressionType = "";

    /** Tells whether this Rule is enabled or disabled. */
    private int fieldID = -1;

    /** Tells whether the message is to be moved when the Rule matches. */
    private int conditionID = -1;

    /** The folderID of the email folder to which matching messages are to be moved. */
    private String value = "";

    public int getRuleID() {
        return (this.ruleID);
    }

    public void setRuleID(int newRuleID) {
        this.ruleID = newRuleID;
    }

    public int getOrderID() {
        return (this.orderID);
    }

    public void setOrderID(int newOrderID) {
        this.orderID = newOrderID;
    }

    public String getExpressionType() {
        return (this.expressionType);
    }

    public void setExpressionType(String newExpressionType) {
        if (newExpressionType != null && (newExpressionType.equals("AND") || newExpressionType.equals("OR"))) {
            this.expressionType = newExpressionType;
        } else {
            this.expressionType = "OR";
        }
    }

    public int getFieldID() {
        return (this.fieldID);
    }

    public void setFieldID(int newFieldID) {
        this.fieldID = newFieldID;
    }

    public int getConditionID() {
        return (this.conditionID);
    }

    public void setConditionID(int newConditionID) {
        this.conditionID = newConditionID;
    }

    public String getValue() {
        return (this.value);
    }

    public void setValue(String newValue) {
        this.value = newValue;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("RuleCriteriaVO = [\n");
        sb.append("  ruleID = [" + ruleID + "]\n");
        sb.append("  orderID = [" + orderID + "]\n");
        sb.append("  expressionType = [" + expressionType + "]\n");
        sb.append("  fieldID = [" + fieldID + "]\n");
        sb.append("  conditionID = [" + conditionID + "]\n");
        sb.append("  value = [" + value + "]\n");
        sb.append("]\n");
        return (sb.toString());
    }
}
