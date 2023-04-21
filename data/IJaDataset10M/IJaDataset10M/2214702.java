package uk.co.fortunecookie.timesheet.data.entities.tests;

import java.util.List;
import org.junit.Test;
import org.junit.Before;
import junit.framework.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Query;
import java.io.File;
import javax.sql.DataSource;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import uk.co.fortunecookie.timesheet.data.entities.ActivityType;

public class ActivityTypeTest {

    private TransactionTemplate transactionTemplate = null;

    private ApplicationContext ctx = null;

    SessionFactory factory = null;

    @Before
    public void setUp() throws Exception {
        String[] paths = { "application-context-hibernate.xml" };
        ctx = new ClassPathXmlApplicationContext(paths);
        PlatformTransactionManager transactionManager = (PlatformTransactionManager) ctx.getBean("transactionManager");
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        factory = (SessionFactory) ctx.getBean("sessionFactory");
        DataSource datasource = (DataSource) ctx.getBean("dataSource");
        IDatabaseConnection connection = new DatabaseDataSourceConnection(datasource);
        connection.getConfig().setFeature(org.dbunit.database.DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
        FlatXmlDataSet insertDataSet = new FlatXmlDataSet(new File("src/test/resources/dbunit-test-data.xml"));
        DatabaseOperation operation = org.dbunit.operation.DatabaseOperation.CLEAN_INSERT;
        operation.execute(connection, insertDataSet);
    }

    @Test
    public void testFindAllActivityTypes() throws Exception {
        this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @SuppressWarnings("unchecked")
            public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Session session = factory.getCurrentSession();
                Query query = session.createQuery("from ActivityType");
                List<ActivityType> results = query.list();
                Assert.assertNotNull(results);
                Assert.assertEquals(new Integer(1), new Integer(results.size()));
            }
        });
    }

    @Test
    public void testGetActivityTypeByIdentity() throws Exception {
        this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Session session = factory.getCurrentSession();
                ActivityType result = (ActivityType) session.get(ActivityType.class, new java.lang.Integer(1));
                Assert.assertNotNull(result);
                Assert.assertEquals(new java.lang.String("s"), result.getName());
                Assert.assertEquals(new java.lang.Boolean(true), result.getProductive());
            }
        });
    }

    @Test
    public void testEqualsAndHashcode() throws Exception {
        this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Session session = factory.getCurrentSession();
                ActivityType one = (ActivityType) session.get(ActivityType.class, new java.lang.Integer(1));
                ActivityType two = (ActivityType) session.get(ActivityType.class, new java.lang.Integer(1));
                Assert.assertTrue(one.equals(two));
                Assert.assertTrue(two.equals(one));
                Assert.assertEquals(one.hashCode(), two.hashCode());
                Assert.assertEquals(one.compareTo(two), 0);
                Assert.assertEquals(two.compareTo(one), 0);
            }
        });
    }

    @Test
    public void testCreateActivityType() throws Exception {
        this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                ActivityType create = new ActivityType();
                create.setName(new java.lang.String("s"));
                create.setProductive(new java.lang.Boolean(true));
                Session session = factory.getCurrentSession();
                session.persist(create);
                ActivityType reload = (ActivityType) session.get(ActivityType.class, create.getActivityTypeId());
                Assert.assertNotNull(reload);
                Assert.assertEquals(reload.getName(), new java.lang.String("s"));
                Assert.assertEquals(reload.getProductive(), new java.lang.Boolean(true));
            }
        });
    }

    @Test
    public void testSaveActivityType() throws Exception {
        this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Session session = factory.getCurrentSession();
                ActivityType result = (ActivityType) session.get(ActivityType.class, new java.lang.Integer(1));
                result.setName(new java.lang.String("s"));
                result.setProductive(new java.lang.Boolean(true));
                session.save(result);
                ActivityType reload = (ActivityType) session.get(ActivityType.class, new java.lang.Integer(1));
                Assert.assertEquals(reload.getName(), new java.lang.String("s"));
                Assert.assertEquals(reload.getProductive(), new java.lang.Boolean(true));
            }
        });
    }

    @Test
    public void testRemoveActivityType() throws Exception {
        this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @SuppressWarnings("unchecked")
            public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Session session = factory.getCurrentSession();
                ActivityType result = (ActivityType) session.get(ActivityType.class, new java.lang.Integer(1));
                session.delete(result);
                Query query = session.createQuery("from ActivityType");
                List<ActivityType> results = query.list();
                Assert.assertNotNull(results);
                Assert.assertEquals(new Integer(0), new Integer(results.size()));
            }
        });
    }
}
