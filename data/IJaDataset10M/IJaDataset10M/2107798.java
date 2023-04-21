package com.mymoviejournal.dao;

import com.mymoviejournal.bean.Review;
import com.mymoviejournal.bean.search.ReviewIndex;
import com.mymoviejournal.bean.Comment;
import com.mymoviejournal.bean.User;
import com.mymoviejournal.search.Page;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.Transaction;
import org.hibernate.search.Search;
import org.hibernate.criterion.Restrictions;
import org.hibernate.FetchMode;
import org.hibernate.search.FullTextQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

public class ReviewDao extends HibernateSearchDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Review get(Long id) {
        return (Review) this.sessionFactory.getCurrentSession().get(Review.class, id);
    }

    public void insert(Review review) {
        this.sessionFactory.getCurrentSession().save(review);
    }

    public void update(Review review) {
        this.sessionFactory.getCurrentSession().update(review);
    }

    public void delete(Review review) {
        this.sessionFactory.getCurrentSession().delete(review);
        this.sessionFactory.getCurrentSession().flush();
    }

    public List<Review> list(List<ReviewIndex> indexes) {
        Session session = this.sessionFactory.getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        Transaction tx = fullTextSession.beginTransaction();
        Query termQuery;
        BooleanQuery booleanQuery = new BooleanQuery();
        Term term;
        for (ReviewIndex idx : indexes) {
            term = new Term("id", Long.toString(idx.getReviewId()));
            termQuery = new TermQuery(term);
            booleanQuery.add(termQuery, BooleanClause.Occur.SHOULD);
        }
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(booleanQuery, Review.class);
        fullTextQuery.setSort(new Sort(new SortField("id", SortField.LONG)));
        NavigableSet sortedReviews = new TreeSet(reviewComparator);
        sortedReviews.addAll(fullTextQuery.list());
        tx.commit();
        List<Review> reviews = new ArrayList<Review>();
        for (ReviewIndex idx : indexes) for (Object review : sortedReviews.subSet(idx, true, idx, true)) reviews.add((Review) review);
        return reviews;
    }

    private ReviewComparator reviewComparator = new ReviewComparator();

    class ReviewComparator implements Comparator {

        public int compare(Object review1, Object review2) {
            Long id1;
            if (review1 instanceof Review) id1 = ((Review) review1).getId(); else if (review1 instanceof ReviewIndex) id1 = ((ReviewIndex) review1).getReviewId(); else throw new ClassCastException();
            Long id2;
            if (review2 instanceof Review) id2 = ((Review) review2).getId(); else if (review2 instanceof ReviewIndex) id2 = ((ReviewIndex) review2).getReviewId(); else throw new ClassCastException();
            return id1.compareTo(id2);
        }
    }

    public Page list(int page, int resultCount) {
        Session session = this.sessionFactory.getCurrentSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        Transaction tx = fullTextSession.beginTransaction();
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(new MatchAllDocsQuery(), Review.class);
        fullTextQuery.setSort(new Sort(new SortField("posted", SortField.STRING, true)));
        Page pg = super.getPage(fullTextQuery, page, resultCount);
        tx.commit();
        return pg;
    }

    public Page list(User user, int page, int resultCount) {
        FullTextSession fullTextSession = Search.createFullTextSession(this.sessionFactory.getCurrentSession());
        Transaction tx = fullTextSession.beginTransaction();
        Term t = new Term("author.id", Long.toString(user.getId()));
        TermQuery query = new TermQuery(t);
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(query, Review.class);
        fullTextQuery.setSort(new Sort(new SortField("posted", SortField.STRING, true)));
        Page pg = super.getPage(fullTextQuery, page, resultCount);
        tx.commit();
        return pg;
    }

    public Page search(String term, int page, int resultCount) {
        FullTextSession fullTextSession = Search.createFullTextSession(this.sessionFactory.getCurrentSession());
        Transaction tx = fullTextSession.beginTransaction();
        BooleanQuery booleanQuery = new BooleanQuery();
        Term t = new Term("movie", term);
        TermQuery termQuery = new TermQuery(t);
        booleanQuery.add(termQuery, BooleanClause.Occur.SHOULD);
        t = new Term("review", term);
        termQuery = new TermQuery(t);
        booleanQuery.add(termQuery, BooleanClause.Occur.SHOULD);
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(booleanQuery, Review.class);
        fullTextQuery.setSort(new Sort(new SortField("posted", SortField.STRING, true)));
        Page pg = super.getPage(fullTextQuery, page, resultCount);
        tx.commit();
        return pg;
    }

    public void addComment(Long reviewId, Comment comment) {
        Review review = (Review) this.sessionFactory.getCurrentSession().createCriteria(Review.class).add(Restrictions.idEq(reviewId)).uniqueResult();
        comment.setReview(review);
        review.getComments().add(comment);
        this.sessionFactory.getCurrentSession().save(comment);
    }
}
