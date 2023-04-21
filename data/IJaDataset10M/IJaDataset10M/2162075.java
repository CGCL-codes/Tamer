package br.com.arsmachina.esculentus.dao.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.com.arsmachina.dao.SortCriterion;
import br.com.arsmachina.dao.hibernate.GenericDAOImpl;
import br.com.arsmachina.esculentus.dao.BookmarkedURLDAO;
import br.com.arsmachina.esculentus.entity.BookmarkedURL;

/**
 * {@link BookmarkedURLDAO} implementation using Hibernate.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class BookmarkedURLDAOImpl extends GenericDAOImpl<BookmarkedURL, Integer> implements BookmarkedURLDAO {

    private static final SortCriterion DEFAULT_SORT_CRITERION = new SortCriterion("lastBookmarkedDate", false);

    private static final SortCriterion[] DEFAULT_SORT_CRITERIA = new SortCriterion[] { DEFAULT_SORT_CRITERION };

    /**
	 * Single constructor of this class.
	 * 
	 * @param sessionFactory a {@link SessionFactory}. It cannot be null.
	 */
    public BookmarkedURLDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @SuppressWarnings("unchecked")
    public List<BookmarkedURL> findRecent(int firstIndex, int maximumResults) {
        Criteria criteria = createCriteria();
        criteria.setFirstResult(firstIndex);
        criteria.setMaxResults(maximumResults);
        criteria.addOrder(Order.desc("lastBookmarkedDate"));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<BookmarkedURL> findMostBookmarked(int firstIndex, int maximumResults) {
        Criteria criteria = createCriteria();
        criteria.setFirstResult(firstIndex);
        criteria.setMaxResults(maximumResults);
        criteria.addOrder(Order.desc("count"));
        criteria.addOrder(Order.desc("lastBookmarkedDate"));
        return criteria.list();
    }

    public BookmarkedURL findByUrl(String url) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("url", url.trim()));
        return (BookmarkedURL) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<BookmarkedURL> find(List<String> tags, int firstIndex, int maximumResults, SortCriterion... sortingCriteria) {
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("Parameter tags cannot be null nor empty");
        }
        Criteria criteria = createCriteria(firstIndex, maximumResults, sortingCriteria);
        for (String tag : tags) {
            criteria.add(Restrictions.sqlRestriction("{alias}.id = any (select bookmarkedurl_id from bookmarkedurl_tags where tag = ?)", tag, Hibernate.STRING));
        }
        return criteria.list();
    }

    @Override
    public SortCriterion[] getDefaultSortCriteria() {
        return DEFAULT_SORT_CRITERIA;
    }
}
