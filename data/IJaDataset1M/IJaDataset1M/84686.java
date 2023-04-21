package org.blueoxygen.komodo.topic_front;

import java.sql.SQLException;
import java.util.List;
import org.blueoxygen.cimande.persistence.hibernate.HibernateSessionFactory;
import org.blueoxygen.cimande.persistence.hibernate.HibernateSessionFactoryAware;
import org.blueoxygen.komodo.Article;
import org.blueoxygen.komodo.Creator;
import org.blueoxygen.komodo.category.ArticleCategory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

/**
 * @author Aji
 *
 */
public class SaringArtikel extends SearchArticleForm implements HibernateSessionFactoryAware {

    private Session sess;

    public List articles;

    private HibernateSessionFactory hsf;

    private Creator creator = new Creator();

    private String keyword = "";

    private int maxPage, currPage, nextPage, prevPage = 0, page = 0;

    private int maxRowPerPage = 10;

    private String orderBy = "logInformation.createDate";

    private int resultRows;

    public String execute() {
        try {
            sess = hsf.createSession();
            Criteria crit = sess.createCriteria(Article.class);
            if (!getKeyword().equalsIgnoreCase("")) {
                String keywordQuery = "%" + getKeyword() + "%";
                System.out.println(keyword);
                crit.add(Expression.disjunction().add(Expression.like("title", keywordQuery).ignoreCase()).add(Expression.like("subject", keywordQuery).ignoreCase()).add(Expression.like("description", keywordQuery).ignoreCase()).add(Expression.like("identifier", keywordQuery).ignoreCase()).add(Expression.like("source", keywordQuery).ignoreCase()).add(Expression.like("relation", keywordQuery).ignoreCase()).add(Expression.like("coverage", keywordQuery).ignoreCase()).add(Expression.like("rights", keywordQuery).ignoreCase()));
            }
            articles = crit.list();
            resultRows = crit.list().size();
            maxPage = resultRows / maxRowPerPage;
            if (resultRows % maxRowPerPage == 0) maxPage = maxPage - 1;
            articles = crit.addOrder(Order.desc(orderBy)).setFirstResult(currPage * maxRowPerPage).setMaxResults(maxRowPerPage).list();
            prevPage = currPage - 1;
            nextPage = currPage + 1;
            page = currPage + 1;
            hsf.endSession(sess);
            return SUCCESS;
        } catch (HibernateException e) {
            return ERROR;
        } catch (SQLException e) {
            return ERROR;
        }
    }

    public List getArticles() {
        return articles;
    }

    public void setArticles(List articles) {
        this.articles = articles;
    }

    public HibernateSessionFactory getHsf() {
        return hsf;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Session getSess() {
        return sess;
    }

    public void setSess(Session sess) {
        this.sess = sess;
    }

    public void setHibernateSessionFactory(HibernateSessionFactory hsf) {
        this.hsf = hsf;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getMaxRowPerPage() {
        return maxRowPerPage;
    }

    public void setMaxRowPerPage(int maxRowPerPage) {
        this.maxRowPerPage = maxRowPerPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getResultRows() {
        return resultRows;
    }

    public void setResultRows(int resultRows) {
        this.resultRows = resultRows;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }
}
