package org.avm.sure.survey.mvc.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import org.avm.sure.domain.Question;
import org.avm.sure.domain.Response;
import org.avm.sure.domain.Result;
import org.avm.sure.domain.Survey;
import org.avm.sure.service.api.QuestionService;
import org.avm.sure.service.facade.SurveyFacade;
import org.avm.sure.survey.view.builder.ResponseComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.synyx.hades.domain.Page;

public class SurveyBean {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private Application application;

    @Autowired
    private SurveyFacade surveyFacade;

    private String surveyId;

    private Survey survey;

    private Result result;

    private List<UIComponent> surveyViews;

    private HtmlPanelGrid panelGrid;

    private int currentView;

    private int nextView;

    private int previousView;

    private int maximumViewNumber;

    private long dateOpen;

    private long dateClose;

    /**
     * <p>
     * Initialize all views and response skeleton based on the survey ID provided via the <code>surveyid</code> request
     * parameter.
     * </p>
     */
    public SurveyBean() {
        init();
    }

    /**
     * @return the current value of {@link #id}
     */
    public String getSurveyId() {
        return surveyId;
    }

    /**
     * @return the title of the survey
     */
    public String getTitle() {
        return survey.getTitle();
    }

    /**
     * @return the current page number
     */
    public String getCurrentPageNumber() {
        return Integer.toString(currentView + 1);
    }

    /**
     * @return the number of pages in this survey
     */
    public String getNumberOfPages() {
        return Integer.toString(maximumViewNumber + 1);
    }

    /**
     * @return return <code>true</code> if the current page is not the first page of the survey, otherwise return
     *         <code>false</code>
     * 
     */
    public boolean isNotFirstPage() {
        return (currentView != 0 ? true : false);
    }

    /**
     * @return return <code>true</code> if the current page is not the last page of the survey, otherwise return
     *         <code>false</code>
     */
    public boolean isNotLastPage() {
        return ((currentView != maximumViewNumber) ? true : false);
    }

    /**
     * Component binding method used to reference a container component of survey questions/answers (represented as components
     * themselves).
     * 
     * @return <code>HtmlPanelGrid</code> container component.
     */
    public HtmlPanelGrid getSurveyContent() {
        panelGrid = (HtmlPanelGrid) surveyViews.get(currentView);
        return panelGrid;
    }

    /**
     * Standard "setter" method used to bind the container component to this bean instance.
     * 
     * @param panelGrid - the container component instance bound to this bean.
     */
    public void setSurveyContent(HtmlPanelGrid panelGrid) {
        this.panelGrid = panelGrid;
    }

    /**
     * <p>
     * When called, the {@link Result} will be marshalled to disk and this <code>SurveyBean</code> instance will be removed from
     * <code>session</code> scope.
     * </p>
     * 
     * @return the outcome string from the action. <code>success</code> if no errors occur during the marshal process, otherwise
     *         <code>error</code> will be returned
     */
    public String submit() {
        if (!isSurveyOpen()) {
            createErrorMessage("Survey CLOSED", "This survey is now closed.");
            return "error";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove("survey");
        try {
            surveyFacade.storeResult(result);
        } catch (Exception e) {
            Throwable t = e.getCause();
            if (t == null) {
                t = e;
            }
            t.printStackTrace();
            createErrorMessage("ERROR", "An unexpected error has occurred: " + t.toString());
            return "error";
        }
        return "success";
    }

    /**
     * <p>
     * Action method used to get the next page of survey questions.
     * </p>
     * 
     * @return The outcome string from the action.
     */
    public String next() {
        if (!isSurveyOpen()) {
            createErrorMessage("Survey CLOSED", "This survey is now closed.");
            return "error";
        }
        nextView = currentView;
        String outcome = null;
        if (nextView == maximumViewNumber) {
            outcome = "success";
        }
        if (nextView < maximumViewNumber) {
            nextView++;
        }
        if (nextView <= maximumViewNumber) {
            UIComponent formComponent = FacesContext.getCurrentInstance().getViewRoot().findComponent("SurveyForm");
            UIComponent contentComponent = formComponent.findComponent("SurveyContent");
            surveyViews.set(currentView, contentComponent);
            setSurveyResponses(contentComponent);
            formComponent.getChildren().remove(contentComponent);
            contentComponent = surveyViews.get(nextView);
            formComponent.getChildren().add(contentComponent);
            currentView = nextView;
        }
        return outcome;
    }

    /**
     * <p>
     * Action method used to get the previous page of survey questions.
     * </p>
     * 
     * @return The outcome string from the action.
     */
    public String previous() {
        if (!isSurveyOpen()) {
            createErrorMessage("Survey CLOSED", "This survey is now closed.");
            return "error";
        }
        previousView = currentView;
        if (currentView == 0) {
            return null;
        }
        if (previousView > 0) {
            previousView--;
        }
        UIComponent formComponent = FacesContext.getCurrentInstance().getViewRoot().findComponent("SurveyForm");
        UIComponent contentComponent = formComponent.findComponent("SurveyContent");
        surveyViews.set(currentView, contentComponent);
        formComponent.getChildren().remove(contentComponent);
        contentComponent = surveyViews.get(previousView);
        formComponent.getChildren().add(contentComponent);
        currentView = previousView;
        return null;
    }

    /**
     * <p>
     * Return <code>true</code> if the survey is still open, otherwise <code>false</code>.
     * </p>
     */
    private boolean isSurveyOpen() {
        long now = new Date().getTime();
        if (now >= dateOpen && now <= dateClose) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Initialize this <code>SurveyBean</code> instance.
     * </p>
     */
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        application = context.getApplication();
        surveyId = context.getExternalContext().getRequestParameterMap().get("surveyid");
        Integer[] surveyIds = surveyFacade.getAvailableSurveyIds();
        if (surveyIds.length == 0) {
            throw new IllegalStateException("No surveys found.");
        }
        try {
            survey = surveyFacade.getSurvey(Integer.valueOf(surveyId));
            dateOpen = survey.getDateOpen().getMillis();
            dateClose = survey.getDateClose().getMillis();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        buildSurveyViews();
        buildSurveyResponses();
    }

    /**
     * <p>
     * This method builds a structure that associates pages of survey questions with a survey. Each survey may have one or more
     * views (pages).
     * </p>
     */
    private void buildSurveyViews() {
        surveyViews = new ArrayList<UIComponent>();
        surveyViews.add(buildQuestionPageView(survey.getQuestions()));
        addSubmitterPageView();
        maximumViewNumber = surveyViews.size() - 1;
    }

    /**
     * <p>
     * Obtains and stores a skeletal {@link Result} object in which survey answers will be stored.
     * </p>
     */
    private void buildSurveyResponses() {
        try {
            result = surveyFacade.createBaseSurveyResult(Integer.valueOf(surveyId));
        } catch (Exception sue) {
            throw new IllegalStateException(sue.getMessage());
        }
    }

    /**
     * <p>
     * This method creates and returns a component instance that is determined from the question identifier.
     * </p>
     * 
     * @param question - the survey question.
     * @param sequence - the sequence number of the survey question/amswer
     * 
     * @return <code>UIComponent</code> the possible responses represented as a component instance.
     */
    private UIComponent getResponseComponent(Question question, int sequence) {
        UIComponent component = ResponseComponentFactory.build(question);
        component.setId('A' + Integer.toString(sequence) + '-' + question.getId());
        return component;
    }

    /**
     * <p>
     * This method populates a <code>ResultEntity</code> from response component values.
     * </p>
     * 
     * @param component the repsonse component
     */
    private void setSurveyResponses(UIComponent component) {
        List<UIComponent> children = component.getChildren();
        for (UIComponent child : children) {
            setSurveyResponses(child);
        }
        String componentId = component.getId();
        if (componentId != null && componentId.charAt(0) == 'A') {
            List<Response> responses = result.getResponses();
            int startPosition = componentId.indexOf('-') + 1;
            String sSurveyId = componentId.substring(startPosition);
            Integer questionId = Integer.valueOf(sSurveyId);
            Response response = null;
            for (Response responseEntity : responses) {
                Question question = responseEntity.getQuestion();
                if (question == null || question.getId().equals(questionId)) {
                    response = responseEntity;
                    break;
                }
            }
            Question question = questionService.find(questionId);
            response.setQuestion(question);
            if (component instanceof UISelectMany) {
                String value = "";
                Object[] values = ((UISelectMany) component).getSelectedValues();
                for (int i = 0; i < values.length; i++) {
                    value += values[i];
                    if (i == (values.length - 1)) {
                        break;
                    }
                    value += ',';
                }
                response.setText(value);
            } else if (component instanceof UIInput) {
                Object value = ((UIInput) component).getValue();
                if (value == null) {
                    value = "";
                }
                response.setText((String) value);
            }
        }
    }

    /**
     * <p>
     * Instantiate a <code>HtmlPanelGrid</code> which will contain all survey question and response components. This is
     * essentially the top level view that will be switched in and out.
     * </p>
     * 
     * @return an <code>HtmlPanelGrid</code>
     */
    private HtmlPanelGrid createDynamicContentGrid() {
        HtmlPanelGrid grid = new HtmlPanelGrid();
        grid.setId("SurveyContent");
        grid.setColumns(1);
        return grid;
    }

    /**
     * <p>
     * Creates and returns a <code>UIComponent</code> view containing questions and possible responses based on the information
     * contained in the provided {@link Page}.
     * </p>
     * 
     * @param page the page from which the view will be constructed
     * 
     * @return a view of UIComponents representative of the specified {@link Page}
     */
    private UIComponent buildQuestionPageView(List<Question> questions) {
        HtmlPanelGrid parent = createDynamicContentGrid();
        int index = 0;
        for (Question question : questions) {
            addSpacerComponent(parent, "10", "1");
            addQuestionComponent(parent, question);
            addResponseComponent(parent, question, index);
            index++;
        }
        return parent;
    }

    /**
     * <p>
     * Adds an <code>HtmlGraphicImage</code> spacer (for formatting) as a child of <code>parent</code>.
     * </p>
     * 
     * @param parent the parent component to which this component will be added as a child
     * @param height the height of the spacer
     * @param width the width of the spacer
     */
    private void addSpacerComponent(UIComponent parent, String height, String width) {
        HtmlGraphicImage image = new HtmlGraphicImage();
        image.setValue("images/a.gif");
        image.setHeight(height);
        image.setWidth(width);
        parent.getChildren().add(image);
    }

    /**
     * <p>
     * Adds an <code>HtmlPanelGroup</code> containing one or two <code>HtmlOutputText</code> components depending on if the
     * question is required or not. If the question is required, then two components will be added to the group with the first
     * being a red asterisk.
     * </p>
     * 
     * @param parent the parent component to which this component will be added as a child the {@link Question} to construct the
     *        question from
     * @param question
     */
    private void addQuestionComponent(UIComponent parent, Question question) {
        HtmlPanelGroup group = new HtmlPanelGroup();
        if (question.isRequired()) {
            HtmlOutputText requiredMarker = new HtmlOutputText();
            requiredMarker.setEscape(false);
            requiredMarker.setStyle("font-weight: bold; color: red");
            requiredMarker.setValue("*&nbsp;");
            group.getChildren().add(requiredMarker);
        }
        addTextComponent(group, question.getText());
        parent.getChildren().add(group);
    }

    /**
     * <p>
     * Adds an <code>HtmlOutputText</code> as a child of <code>parent</code>.
     * </p>
     * 
     * @param parent the parent component to which this component will be added as a child of <code>parent</code>.</p>
     * @param value the value of the text component
     */
    private void addTextComponent(UIComponent parent, String value) {
        HtmlOutputText text = new HtmlOutputText();
        text.setValue(value);
        text.setStyle("font-weight: bold;");
        parent.getChildren().add(text);
    }

    /**
     * <p>
     * Adds a <code>UIComponent</code> representing the possible response(s) for the <code>question</code> as a child of
     * <code>parent</code>.
     * </p>
     * 
     * @param parent he parent component to which this component will be added as a child of <code>parent</code>.</p>
     * @param question the {@link Question} to construct the response from
     * @param sequence the index of the question
     */
    private void addResponseComponent(UIComponent parent, Question question, int sequence) {
        HtmlPanelGrid responseGrid = new HtmlPanelGrid();
        responseGrid.setColumns(3);
        List<UIComponent> children = responseGrid.getChildren();
        addSpacerComponent(responseGrid, "1", "30");
        UIComponent response = getResponseComponent(question, sequence);
        HtmlMessage message = new HtmlMessage();
        message.setId('M' + response.getId());
        message.setFor(response.getId());
        message.setStyleClass("error");
        children.add(response);
        children.add(message);
        parent.getChildren().add(responseGrid);
    }

    /**
     * <p>
     * Adds the view that will ultimately submit the survey. This must be the last view in {@link #surveyViews}.
     * </p
     * ?
     */
    private void addSubmitterPageView() {
        HtmlPanelGrid parent = createDynamicContentGrid();
        HtmlPanelGrid child = new HtmlPanelGrid();
        child.setColumns(2);
        List<UIComponent> children = child.getChildren();
        addTextComponent(child, "Enter your name or leave as ANONYMOUS: ");
        HtmlInputText input = new HtmlInputText();
        input.setValue("ANONYMOUS");
        HtmlCommandButton submit = new HtmlCommandButton();
        submit.setId("SurveySubmit");
        submit.setType("submit");
        submit.setStyleClass("button");
        MethodExpression actionExpression = application.getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{survey.submit}", null, null);
        submit.setActionExpression(actionExpression);
        submit.setValue("Submit Survey");
        children.add(input);
        children.add(submit);
        parent.getChildren().add(child);
        surveyViews.add(parent);
    }

    /**
     * <p>
     * Create a new error message.
     * </p>
     * 
     * @param title the title
     * @param message the error message
     */
    private void createErrorMessage(String title, String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        ValueExpression value = context.getApplication().getExpressionFactory().createValueExpression("#{msg}", String.class);
        MessageBean bean = (MessageBean) value.getValue(context.getELContext());
        bean.setTitle(title);
        bean.setMessage(message);
    }
}
