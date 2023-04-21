package org.eledge.components.questions;

import static org.eledge.Eledge.*;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.eledge.LocalizedStringSelectionModel;
import org.eledge.domain.questiontypes.NumericAssignmentQuestion;

/**
 * @author robertz
 * 
 */
public abstract class NumericEdit extends BaseComponent {

    private IPropertySelectionModel policyModel;

    public abstract NumericAssignmentQuestion getQuestion();

    public IPropertySelectionModel getPolicyModel() {
        if (policyModel == null) {
            policyModel = new LocalizedStringSelectionModel(getQuestion().getAcceptablePolicies(), getMessages());
        }
        return policyModel;
    }

    public void update(IRequestCycle cycle) {
        dbcommit(cycle);
    }

    public void delete(IRequestCycle cycle) {
        EditQuestionHelper.delete(getQuestion(), cycle);
    }

    public Integer getWeight() {
        return getQuestion().getWeight();
    }

    public void setWeight(Integer i) {
        getQuestion().setWeight(i);
    }

    public Integer getPointValue() {
        return getQuestion().getPointValue();
    }

    public void setPointValue(Integer i) {
        getQuestion().setPointValue(i);
    }

    public Integer getNumber() {
        return getQuestion().getNumber();
    }

    public void setNumber(Integer i) {
        getQuestion().setNumber(i);
    }

    public String getQuestionText() {
        return getQuestion().getQuestionText();
    }

    public void setQuestionText(String s) {
        getQuestion().setQuestionText(s);
    }

    public String getCorrectAnswer() {
        return getQuestion().getCorrectAnswer();
    }

    public void setCorrectAnswer(String s) {
        getQuestion().setCorrectAnswer(s);
    }

    public Float getPrecision() {
        return getQuestion().getPrecision();
    }

    public void setPrecision(Float f) {
        getQuestion().setPrecision(f);
    }

    public String getUnits() {
        return getQuestion().getUnits();
    }

    public void setUnits(String val) {
        getQuestion().setUnits(val);
    }

    public String getDecimalPolicy() {
        return getQuestion().getDecimalPolicy();
    }

    public void setDecimalPolicy(String pol) {
        getQuestion().setDecimalPolicy(pol);
    }

    public String getFractionPolicy() {
        return getQuestion().getFractionPolicy();
    }

    public void setFractionPolicy(String pol) {
        getQuestion().setFractionPolicy(pol);
    }

    public String getExponentialPolicy() {
        return getQuestion().getExponentialPolicy();
    }

    public void setExponentialPolicy(String pol) {
        getQuestion().setExponentialPolicy(pol);
    }
}
