package edu.ba.library.management.hibernate;

import org.hibernate.Session;
import org.junit.Test;
import edu.ba.library.management.article.Article;
import edu.ba.library.management.article.Copy;
import edu.ba.library.management.util.HibernateHelper;
import junit.framework.TestCase;

public class HibernateTest extends TestCase {

    @Test
    public void testHibernate() {
        Session s = HibernateHelper.getSession();
        s.beginTransaction();
    }

    @Test
    public void testSave() {
        Article a = new Article();
        a.setArticleNumber("xyz");
        a.setTitle("Harry Schlotter");
        Copy c = new Copy();
        c.setCopyNumber(1);
        c.setArticle(a);
        a.getCopies().add(c);
        Session s = HibernateHelper.getSession();
        s.beginTransaction();
        s.save(a);
        s.save(c);
        s.getTransaction().commit();
    }
}
