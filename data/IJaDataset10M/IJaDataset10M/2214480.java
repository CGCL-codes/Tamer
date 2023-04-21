package com.dcivision.form.bean;

import com.dcivision.framework.bean.AbstractBaseObject;

/**
  FormRoutingRule.java

  This class is the serializable bean reflecting business logic uses.

    @author           Vera Wang
    @company          DCIVision Limited
    @creation date    16/05/2005
    @version          $Revision: 1.3 $
*/
public class FormRoutingRule extends AbstractBaseObject {

    public static final String REVISION = "$Revision: 1.3 $";

    public static final String ROUTING_TYPE_VERIFY = "V";

    public static final String ROUTING_TYPE_CALCULATE = "C";

    public static final String CHECK_RULE_MANDATORY = "M";

    private Integer formRecordID = null;

    private String ruleName = null;

    private String ruleType = null;

    private String checkRule = null;

    private String operationType = null;

    private Object formEquation = null;

    private Object formOperationEquation = null;

    private String targetElementID = null;

    public FormRoutingRule() {
        super();
    }

    public Integer getFormRecordID() {
        return this.formRecordID;
    }

    public void setFormRecordID(Integer formRecordID) {
        this.formRecordID = formRecordID;
    }

    public String getRuleName() {
        return (this.ruleName);
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleType() {
        return (this.ruleType);
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getCheckRule() {
        return checkRule;
    }

    public void setCheckRule(String checkRule) {
        this.checkRule = checkRule;
    }

    public String getOperationType() {
        return (this.operationType);
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Object getFormEquation() {
        return (this.formEquation);
    }

    public void setFormEquation(Object formEquation) {
        this.formEquation = formEquation;
    }

    public Object getFormOperationEquation() {
        return (this.formOperationEquation);
    }

    public void setFormOperationEquation(Object formOperationEquation) {
        this.formOperationEquation = formOperationEquation;
    }

    public String getTargetElementID() {
        return (this.targetElementID);
    }

    public void setTargetElementID(String targetElementID) {
        this.targetElementID = targetElementID;
    }
}
