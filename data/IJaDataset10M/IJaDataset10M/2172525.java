package br.ita.comp.ces22.quiz.core.dao.jpa;

import br.ita.comp.ces22.quiz.core.dao.DAO;
import br.ita.comp.ces22.quiz.core.dao.DAOFactory;
import br.ita.comp.ces22.quiz.core.dao.QuizDAO;
import br.ita.comp.ces22.quiz.core.dao.UserDAO;
import br.ita.comp.ces22.quiz.domain.Question;

public class JPADAOFactory extends DAOFactory {

    public UserDAO getUserDAO() {
        return new UserJPADAO();
    }

    public DAO<Question> getQuestionsDAO() {
        return new QuestionJPADAO();
    }

    public QuizDAO getQuizDAO() {
        return new QuizJPADAO();
    }
}
