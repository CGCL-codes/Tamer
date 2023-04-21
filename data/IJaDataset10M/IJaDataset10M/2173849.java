package org.wfp.rita.test.hibernate;

import java.sql.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.sql.Template;
import org.wfp.rita.test.base.HibernateTestBase;

/**
 * @see <a href="http://opensource.atlassian.com/projects/hibernate/browse/HHH-5185">HHH-5185</a>
 * 
 * Note that <a href="http://opensource.atlassian.com/projects/hibernate/browse/HHH-5185">HHH-5185</a>
 * was closed as a duplicate of
 * <a href="http://opensource.atlassian.com/projects/hibernate/browse/HHH-5135">HHH-5135</a>
 * and Gail Badner comments that the issue is that
 * {@link Template#renderWhereStringTemplate(String, String, org.hibernate.dialect.Dialect, org.hibernate.dialect.function.SQLFunctionRegistry)}
 * thinks that MONTH is a SQL function, but in this case it's a keyword.
 * <p>
 * I'm not sure whether I agree with Gail on this. Please retest after
 * HHH-5135 is closed, and reopen HHH-5185 if necessary.  
 * 
 * @author Chris Wilson <chris+rita@aptivate.org>
 */
public class HibernateJoinFormulaAliasTest extends HibernateTestBase {

    @Entity
    @Table(name = "houses")
    private static class House {

        @Id
        public Integer id;

        @OneToMany(mappedBy = "firstHome")
        @Fetch(FetchMode.JOIN)
        public Set<Cat> cats1;

        @OneToMany(mappedBy = "secondHome")
        @Fetch(FetchMode.JOIN)
        public Set<Cat> cats2;
    }

    @Entity
    @Table(name = "cats")
    private static class Cat {

        @Id
        public Integer id;

        @ManyToOne
        @Fetch(FetchMode.SELECT)
        public House firstHome;

        @ManyToOne
        @Fetch(FetchMode.SELECT)
        public House secondHome;

        @Column(name = "kittens")
        public Integer kittens;

        @Formula("(kittens * 4) + 3")
        public Integer legs;

        @Column
        public Date birthday;

        @Formula("EXTRACT(MONTH FROM birthday)")
        public Integer birthMonth;
    }

    protected Class[] getMappings() {
        return new Class[] { House.class, Cat.class };
    }

    public void testFailing() {
        Session session = openSession();
        session.get(House.class, 1, LockMode.READ);
        session.close();
    }
}
