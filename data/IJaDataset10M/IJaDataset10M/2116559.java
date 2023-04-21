package net.mreunion.web.pages.survey;

import java.util.List;
import net.mreunion.web.entities.Survey;
import net.mreunion.web.entities.SurveyQuestion;
import net.mreunion.web.model.IdSelectModel;
import net.mreunion.web.services.SurveyManager;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;

public class CreateSurveyQuestion {

    @SuppressWarnings("unused")
    @Property
    private IdSelectModel<Survey> surveyIdModel;

    @Property
    private SurveyQuestion surveyQuestion;

    @Property
    private Long surveyId;

    @Property
    private Long surveyQuestionId;

    @Inject
    private SurveyManager surveyManager;

    @Inject
    private PropertyAccess _propertyAccess;

    @CommitAfter
    Object onSuccess() {
        Survey survey = surveyManager.getSurvey(surveyId);
        surveyQuestion.setSurvey(survey);
        surveyManager.add(surveyQuestion);
        return SurveyQuestionList.class;
    }

    Long onPassivate() {
        return surveyQuestionId;
    }

    void onActivate(Long surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
        this.surveyQuestion = surveyManager.getSurveyQuestion(surveyQuestionId);
    }

    void onPrepare() {
        List<Survey> surveys = surveyManager.getAllSurvey();
        this.surveyIdModel = new IdSelectModel<Survey>(surveys, Survey.class, "name", "id", _propertyAccess);
    }
}
