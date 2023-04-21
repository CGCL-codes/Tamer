package org.open18.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.open18.model.TriviaCategory;
import org.open18.model.TriviaQuestion;

@Name("trivia")
@Scope(ScopeType.CONVERSATION)
public class Trivia implements Serializable {

    @In
    private EntityManager entityManager;

    private List<TriviaQuestion> questions;

    private double points;

    private double numQuestions;

    @WebRemote
    public List<String> getCategories() {
        List<TriviaCategory> categories = entityManager.createQuery("select c from TriviaCategory c order by c.name").getResultList();
        List<String> categoryNames = new ArrayList<String>(categories.size());
        for (TriviaCategory cat : categories) {
            categoryNames.add(cat.getName());
        }
        return categoryNames;
    }

    @Begin
    @WebRemote
    public Boolean selectQuiz(String category) {
        questions = entityManager.createQuery("select q from TriviaQuestion q where q.category.name = :category").setParameter("category", category).getResultList();
        numQuestions = questions.size();
        points = 0;
        return numQuestions > 0 ? Boolean.TRUE : null;
    }

    @WebRemote(exclude = "answer")
    public TriviaQuestion drawNextQuestion() {
        if (questions.size() == 0) {
            return null;
        }
        return questions.get(new Random().nextInt(questions.size()));
    }

    @WebRemote
    public boolean answerQuestion(Long id, String response) {
        TriviaQuestion questionInstance = findQuestion(id);
        if (questionInstance == null) {
            return false;
        }
        boolean result = questionInstance.getAnswer().equals(response);
        if (result) {
            points++;
        }
        questions.remove(questionInstance);
        return result;
    }

    public TriviaQuestion findQuestion(Long id) {
        for (TriviaQuestion candidate : questions) {
            if (candidate.getId().equals(id)) {
                return candidate;
            }
        }
        return null;
    }

    @End
    @WebRemote
    public double endQuiz() {
        return Math.round((double) points / numQuestions * 1000) / 10d;
    }
}
