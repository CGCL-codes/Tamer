package fi.passiba.services.biblestudy.dao;

import fi.passiba.hibernate.BaseDaoHibernate;
import fi.passiba.services.biblestudy.persistance.Booksection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class BooksectionDAO extends BaseDaoHibernate<Booksection> implements IBooksectionDAO {

    public BooksectionDAO() {
        setQueryClass(Booksection.class);
    }

    public List<Booksection> findBooksectionByBooksectionIdAndBibleTranslationId(long bibletranslationid, long booksectionid) {
        Criteria crit = super.getSessionFactory().getCurrentSession().createCriteria(getQueryClass());
        crit.add(Restrictions.eq("id", booksectionid));
        crit.createCriteria("bibletranslation").add(Restrictions.eq("id", bibletranslationid));
        return crit.list();
    }

    public List<Booksection> findBookSectionByBibleTranslationId(long id) {
        Criteria crit = super.getSessionFactory().getCurrentSession().createCriteria(getQueryClass());
        crit.createCriteria("bibletranslation").add(Restrictions.eq("id", id));
        return crit.list();
    }
}
