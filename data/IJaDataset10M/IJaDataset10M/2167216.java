package com.bluedashred.survey.model;

import java.io.Serializable;
import com.bluedashred.main.BaseBean;

/**
 * OK: Each question is tied to a section 
 * @author james.yong
 * Not need cascade update
 */
public class SurveyQuestionBean extends BaseBean implements Serializable {

    private int sort_order;

    private String question_text;

    private SurveyChoiceBean answer;

    SurveyChoiceListBean choice_list;

    public SurveyQuestionBean(int sort_order, String question_text, SurveyChoiceBean answer, SurveyChoiceListBean choice_list) {
        super();
        this.sort_order = sort_order;
        this.question_text = question_text;
        this.answer = answer;
        this.choice_list = choice_list;
    }

    public SurveyChoiceBean getAnswer() {
        return answer;
    }

    public void setAnswer(SurveyChoiceBean answer) {
        this.answer = answer;
    }

    public SurveyChoiceListBean getChoice_list() {
        return choice_list;
    }

    public void setChoice_list(SurveyChoiceListBean choice_list) {
        this.choice_list = choice_list;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }
}
