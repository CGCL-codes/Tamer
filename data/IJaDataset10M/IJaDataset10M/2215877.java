package org.blueoxygen.mantra.participants;

import java.sql.SQLException;
import org.blueoxygen.cimande.persistence.hibernate.HibernateSessionFactory;
import org.blueoxygen.cimande.persistence.hibernate.HibernateSessionFactoryAware;
import org.blueoxygen.mantra.entity.Event;
import org.blueoxygen.mantra.entity.Participants;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.blueoxygen.mantra.participants.FormParticipants;

public class SearchParticipants extends FormParticipants implements HibernateSessionFactoryAware {

    private HibernateSessionFactory hsf;

    private Session sess;

    private int maxPage, currPage, nextPage, prevPage = 0, page = 0;

    private int maxRowPerPage = 18;

    private String orderBy = "name";

    private int resultRows;

    private String dropValue;

    public HibernateSessionFactory getHsf() {
        return hsf;
    }

    public void setHsf(HibernateSessionFactory hsf) {
        this.hsf = hsf;
    }

    public Session getSess() {
        return sess;
    }

    public void setSess(Session sess) {
        this.sess = sess;
    }

    public String getDropValue() {
        return dropValue;
    }

    public void setDropValue(String dropValue) {
        this.dropValue = dropValue;
    }

    public String execute() {
        sess = hsf.createSession();
        Criteria crit = sess.createCriteria(Participants.class);
        if (getDropValue() != null && !"".equalsIgnoreCase(getDropValue())) {
            crit.add(Expression.like("event.kategori", getDropValue()));
        }
        resultRows = crit.list().size();
        maxPage = resultRows / maxRowPerPage;
        prevPage = currPage - 1;
        nextPage = currPage + 1;
        page = currPage + 1;
        if (resultRows % maxRowPerPage == 0) maxPage = maxPage - 1;
        setParticipantses(crit.addOrder(Order.asc(orderBy)).setFirstResult(currPage * maxRowPerPage).setMaxResults(maxRowPerPage).list());
        try {
            hsf.endSession(sess);
            hsf.closeSession(sess);
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public void setHibernateSessionFactory(HibernateSessionFactory hsf) {
        this.hsf = hsf;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxRowPerPage() {
        return maxRowPerPage;
    }

    public void setMaxRowPerPage(int maxRowPerPage) {
        this.maxRowPerPage = maxRowPerPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getResultRows() {
        return resultRows;
    }

    public void setResultRows(int resultRows) {
        this.resultRows = resultRows;
    }
}
