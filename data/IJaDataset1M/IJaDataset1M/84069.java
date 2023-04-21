package de.beas.explicanto.distribution.dao.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Hibernate;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import de.beas.explicanto.distribution.dao.QuestionDAO;
import de.beas.explicanto.distribution.model.*;
import de.beas.explicanto.distribution.util.PagedList;
import de.beas.explicanto.distribution.util.Utils;

/**
 * Spring DAO impl of the DAO interface for question.
 * 
 * @author dorel
 *  
 */
public final class QuestionDAOImpl extends HibernateDaoSupport implements QuestionDAO {

    public java.lang.String newQuestion(Question question) {
        return (java.lang.String) getHibernateTemplate().save(question);
    }

    public void updateQuestion(Question question) {
        getHibernateTemplate().update(question);
    }

    public Question getQuestion(java.lang.String id) {
        return (Question) getHibernateTemplate().load(Question.class, id);
    }

    public void removeQuestion(final java.lang.String id) {
        getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Question question = (Question) session.load(Question.class, id);
                Set childrens = null;
                if (Question.MULTIPLE_ANSWER_MATRIX.equals(question.getType())) {
                    childrens = ((MultipleAnswerMatrixQuestion) question).getMatrixQuestions();
                } else if (Question.SINGLE_ANSWER_MATRIX.equals(question.getType())) {
                    childrens = ((SingleAnswerMatrixQuestion) question).getMatrixQuestions();
                } else if (Question.SINGLE_ANSWER.equals(question.getType())) {
                    SingleAnswerMatrixQuestion parent = ((SingleAnswerQuestion) question).getParent();
                    if (parent != null) {
                        parent.removeQuestion((SingleAnswerQuestion) question);
                        removeQuestionResponses(id, session);
                        ((SingleAnswerQuestion) question).setParent(null);
                        session.update(parent);
                        session.flush();
                        return null;
                    }
                } else if (Question.MULTIPLE_ANSWER.equals(question.getType())) {
                    MultipleAnswerMatrixQuestion parent = ((MultipleAnswerQuestion) question).getParent();
                    if (parent != null) {
                        parent.removeQuestion((MultipleAnswerQuestion) question);
                        removeQuestionResponses(id, session);
                        ((MultipleAnswerQuestion) question).setParent(null);
                        session.update(parent);
                        session.flush();
                        return null;
                    }
                }
                if (childrens != null) {
                    childrens.clear();
                }
                removeQuestionResponses(id, session);
                session.delete(question);
                session.flush();
                return null;
            }

            /**
             * @param id
             * @param session
             * @throws HibernateException
             */
            private void removeQuestionResponses(final java.lang.String id, Session session) throws HibernateException {
                List questionResponses = getHibernateTemplate().find("FROM QuestionResponse questionResponse where questionResponse.question.id = ?", id, Hibernate.STRING);
                Iterator iterator = questionResponses.iterator();
                while (iterator.hasNext()) {
                    session.delete(iterator.next());
                }
            }
        });
    }

    public java.util.List getAllQuestions() {
        return getHibernateTemplate().find("FROM Question question");
    }

    public java.util.List getQuestionsBetween(final int start, final int end) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                int count = ((Integer) session.iterate("select count(*) from Question").next()).intValue();
                Query q = session.createQuery("FROM Question question");
                q.setFirstResult(start);
                q.setMaxResults(end);
                return new PagedList(q.list(), count);
            }
        });
    }

    public List getQuestionsForSurveyBetween(final int start, final int stop, final String surveyId) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Survey survey = (Survey) session.get(Survey.class, surveyId);
                List questionsNoParent = Utils.sortDescByCreatedAt(survey.getQuestionsWithNoParent());
                int count = questionsNoParent.size();
                List res = new ArrayList();
                for (int i = 0; i < count; i++) {
                    if (i >= start && i < start + stop) {
                        res.add(questionsNoParent.get(i));
                    }
                }
                return new PagedList(res, count);
            }
        });
    }

    public List getQuestionsForMatrixQuestionBetween(final int start, final int stop, final String parentId) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                Question matrixQuestion = (Question) session.get(Question.class, parentId);
                List questions = new ArrayList();
                if (Question.SINGLE_ANSWER_MATRIX.equalsIgnoreCase(matrixQuestion.getType())) questions = new ArrayList(((SingleAnswerMatrixQuestion) matrixQuestion).getMatrixQuestions()); else if (Question.MULTIPLE_ANSWER_MATRIX.equalsIgnoreCase(matrixQuestion.getType())) questions = new ArrayList(((MultipleAnswerMatrixQuestion) matrixQuestion).getMatrixQuestions());
                Utils.sortDescByCreatedAt(questions);
                int count = questions.size();
                List res = new ArrayList();
                for (int i = 0; i < count; i++) {
                    if (i >= start && i < start + stop) {
                        res.add(questions.get(i));
                    }
                }
                return new PagedList(res, count);
            }
        });
    }

    public void removeQuestions(final List ids) {
        for (Iterator it = ids.iterator(); it.hasNext(); ) {
            String id = (String) it.next();
            removeQuestion(id);
        }
    }
}
