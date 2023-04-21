package org.tolven.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import org.tolven.core.entity.Question;
import org.tolven.core.entity.QuestionAnswer;
import org.tolven.core.entity.QuestionType;
import org.tolven.core.entity.TolvenUser;

/**
 * Faces Action bean concerned with questions-answer process.
 * @author Sashikanth Vema
 */
public class QuestionsAction extends TolvenAction {

    private List<UserAnswers> listUIUnansweredQuestions;

    /** Creates a new instance of QuestionsAction 
     * @throws NamingException */
    public QuestionsAction() throws NamingException {
    }

    public List<UserAnswers> getUnansweredQuestions() throws NamingException {
        if (listUIUnansweredQuestions == null) {
            listUIUnansweredQuestions = new ArrayList<UserAnswers>();
            TolvenUser user = getActivationBean().findTolvenUser(getSessionTolvenUserId());
            for (Question q : getQuestionAnswerBean().getUnansweredQuestions(user)) {
                List<SelectItem> listAnswers = new ArrayList<SelectItem>();
                if (q.getType().equalsIgnoreCase(QuestionType.ONELIST.value())) listAnswers.add(new SelectItem("NA", "Select One"));
                for (QuestionAnswer a : q.getAnswers()) listAnswers.add(new SelectItem("" + a.getId(), a.getAnswer()));
                List<String> listUserAnswers = new ArrayList<String>();
                listUserAnswers.add("");
                listUIUnansweredQuestions.add(new UserAnswers(q, listAnswers, listUserAnswers));
            }
        }
        return listUIUnansweredQuestions;
    }

    /**
     * <p>Updates the selected answers for the current user.</p> 
     * <p>First a check is made to make sure that user has answered all the unanswered questions, 
     * if not returning with an error message so that the same page is displayed for the user.</p>
     * <p>If user has answered all the unanswered questions, they are stored in TolvenUserAnswer entity.</p>
     */
    public String updateAnswers() throws NamingException {
        Map<String, String[]> requestParamsMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
        Set<String> keySet = requestParamsMap.keySet();
        int i = 0;
        for (String strKey : keySet) {
            if (strKey.indexOf("questionsForm:questions") != -1 && !requestParamsMap.get(strKey)[0].equals("NA")) {
                i++;
            }
        }
        TolvenUser user = getActivationBean().findTolvenUser(getSessionTolvenUserId());
        if (i != getQuestionAnswerBean().findUnansweredQuestions(user)) {
            FacesMessage facesMessage = new FacesMessage();
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesMessage.setSummary("All the questions need to be answered");
            facesMessage.setDetail("Before proceeding further, all the questions need to be answered.  " + "If any of the answers need to be changed later, that can be done by updating the preferences");
            FacesContext.getCurrentInstance().addMessage("questionsForm:questions", facesMessage);
            return "error";
        } else {
            for (String strKey : keySet) {
                if (strKey.indexOf("questionsForm:questions") != -1) {
                    String[] answers = requestParamsMap.get(strKey);
                    for (String strAnswer : answers) {
                        if (strAnswer.equalsIgnoreCase("NA")) continue;
                        long answerId = Long.valueOf(strAnswer);
                        QuestionAnswer answer = getQuestionAnswerBean().findAnswer(answerId);
                        getQuestionAnswerBean().createTolvenUserAnswer(user, answer);
                    }
                }
            }
        }
        return "success";
    }

    public class UserAnswers {

        List<String> userAnswers;

        Question question;

        List<SelectItem> answers;

        public UserAnswers(Question question, List<SelectItem> answers, List<String> userAnswers) {
            this.question = question;
            this.answers = answers;
            setUserAnswers(userAnswers);
        }

        public void setUserAnswers(List<String> userAnswers) {
            this.userAnswers = userAnswers;
        }

        public List<String> getUserAnswers() {
            return userAnswers;
        }

        public String getType() {
            return this.question.getType();
        }

        public String getQuestion() {
            return this.question.getQuestion();
        }

        public void setAnswers(List<SelectItem> answers) {
            this.answers = answers;
        }

        public List<SelectItem> getAnswers() {
            return this.answers;
        }
    }
}
