package de.beas.explicanto.distribution.tests;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import de.beas.explicanto.distribution.services.Ctx;
import junit.framework.TestCase;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

/**
 * The BaseTestCase class.
 *
 * @author mirceac
 */
public abstract class BaseTestCase extends TestCase {

    private SessionFactory sessionFactory = null;

    public BaseTestCase(String arg) {
        super(arg);
    }

    protected void setUp() throws Exception {
        super.setUp();
        sessionFactory = (SessionFactory) getBean("sessionFactory");
        Session s = SessionFactoryUtils.getNewSession(sessionFactory);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
    }

    protected Object getBean(String beanName) {
        return Ctx.getSessionFactory();
    }

    protected void tearDown() throws Exception {
        SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        Session s = holder.getSession();
        try {
            clear(s);
        } finally {
            s.flush();
            TransactionSynchronizationManager.unbindResource(sessionFactory);
            SessionFactoryUtils.closeSessionIfNecessary(s, sessionFactory);
            super.tearDown();
        }
    }

    private void clear(Session s) {
        Connection con = null;
        try {
            con = s.connection();
            Statement statement = con.createStatement();
            statement.execute("delete from blog_items");
            statement.execute("delete from blogs");
            statement.execute("delete from user_to_groups");
            statement.execute("delete from course_to_groups");
            statement.execute("delete from course_to_profilees");
            statement.execute("delete from question_responses_to_answers");
            statement.execute("delete from question_responsees");
            statement.execute("delete from responsees");
            statement.execute("delete from answers");
            statement.execute("delete from questions");
            statement.execute("delete from surveys");
            statement.execute("delete from learner_scos");
            statement.execute("delete from course_items");
            statement.execute("delete from course_to_users");
            statement.execute("delete from coursees");
            statement.execute("delete from users");
            statement.execute("delete from profilees");
            statement.execute("delete from groups");
            statement.execute("delete from rss_items");
            statement.execute("delete from rss_channels");
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        }
    }
}
