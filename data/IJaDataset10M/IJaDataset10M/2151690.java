package fi.passiba.services.biblestudy.dao;

import fi.passiba.hibernate.BaseDaoHibernate;
import fi.passiba.services.biblestudy.persistance.Book;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

public class BookDAO extends BaseDaoHibernate<Book> implements IBookDAO {

    public BookDAO() {
        setQueryClass(Book.class);
    }

    public List<Book> findBooksByBookIDSectionIDandTranslationId(long translationid, long sectionid, long bookid) {
        Query query = super.getSessionFactory().getCurrentSession().createQuery("select distinct b from Book b join b.booksection s join s.bibletranslation t where t.id=:translationid and s.id=:sectionid and b.id=:bookid");
        query.setMaxResults(1);
        query.setLong("translationid", translationid);
        query.setLong("sectionid", sectionid);
        query.setLong("bookid", bookid);
        return query.list();
    }

    public List<Book> findBooksByBooksectionId(long id) {
        Criteria crit = super.getSessionFactory().getCurrentSession().createCriteria(getQueryClass());
        crit.createCriteria("booksection").add(Restrictions.eq("id", id));
        return crit.list();
    }

    public void updateBooksinBatch(List<Book> books) {
        int index = 0;
        for (Book book : books) {
            this.update(book);
            if (((index++) % 15) == 0) {
                super.getSessionFactory().getCurrentSession().flush();
                super.getSessionFactory().getCurrentSession().clear();
            }
        }
    }
}
