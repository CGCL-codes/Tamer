package edu.webteach.dao.hibernate;

import edu.webteach.dao.*;

/**
 * @author Igor Shubovych
 */
public class HibernateDAOFactory extends DAOFactory {

    @Override
    public NewsDAO getNewsDAO() {
        return new NewsDAOImpl();
    }

    @Override
    public AlarmDAO getAlarmDAO() {
        return new AlarmDAOImpl();
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOImpl();
    }

    @Override
    public GroupDAO getGroupDAO() {
        return new GroupDAOImpl();
    }

    @Override
    public AnswerDAO getAnswerDAO() {
        return new AnswerDAOImpl();
    }

    @Override
    public QuestionDAO getQuestionDAO() {
        return new QuestionDAOImpl();
    }

    @Override
    public HistoryDAO getHistoryDAO() {
        return new HistoryDAOImpl();
    }

    @Override
    public TopicDAO getTopicDAO() {
        return new TopicDAOImpl();
    }

    @Override
    public QuestionAnswerDAO getQuestionAnswerDAO() {
        return new QuestionAnswerDAOImpl();
    }

    /**
     * @see edu.webteach.dao.DAOFactory#getTheoryDAO()
     */
    @Override
    public TheoryDAO getTheoryDAO() {
        return new TheoryDAOImpl();
    }
}
