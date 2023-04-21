package org.nexopenframework.samples.tutorial.one.facades;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nexopenframework.persistence.PersistenceManager;
import org.nexopenframework.persistence.hibernate3.AnnotationLocatorSessionFactoryBean;
import org.nexopenframework.persistence.hibernate3.junit4.TransactionalCallbackListener;
import org.nexopenframework.persistence.hibernate3.junit4.TransactionalTestSupport;
import org.nexopenframework.persistence.hibernate3.junit4.TransactionalTestSupport.DatabaseConfigurer;
import org.nexopenframework.persistence.hibernate3.junit4.TransactionalTestSupport.SessionFactoryBuilder;
import org.nexopenframework.persistence.test.ManagedTransaction;
import org.nexopenframework.samples.tutorial.one.facades.PersonFacadeImpl;
import org.nexopenframework.samples.tutorial.one.model.Person;
import org.nexopenframework.samples.tutorial.one.model.Phone;
import org.nexopenframework.samples.tutorial.one.model.PhoneType;
import org.nexopenframework.test.junit4.NexOpenJUnit4ClassRunner;
import org.nexopenframework.test.junit4.annotations.TestCallbackListeners;
import org.nexopenframework.test.junit4.support.PerformanceCallbackListener;
import org.nexopenframework.util.DefaultScanner;
import org.nexopenframework.util.ResourceLocator;
import org.nexopenframework.util.Scanner;
import org.nexopenframework.util.sql.SQLDataLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>tutorial-one using NexOpen</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:you@yourcompany.dot">francesc</a>
 * @version 1.0
 * @since 1.0
 */
@RunWith(NexOpenJUnit4ClassRunner.class)
@TestCallbackListeners({ TransactionalCallbackListener.class, PerformanceCallbackListener.class })
public class PersonFacadeImplTest {

    /**logging facility*/
    private static final Log LOGGER = LogFactory.getLog(PersonFacadeImplTest.class);

    /** NexOpen ServiceComponent class to test*/
    private static PersonFacadeImpl sc;

    /**
   * <p>Constructor in order to initialize necessary logic for testing</p>
   */
    public PersonFacadeImplTest() {
        super();
    }

    /**
   *  <p>Generic test method to check the main functionalities of {@link PersonFacadeImpl}</p>
   *
   *  TODO Implement properly this method
   */
    @Test
    @Transactional
    public void funcPersonFacadeImpl() {
        try {
            final Person p = new Person();
            p.setAddress("Sesam street");
            p.setAge(28);
            p.setName("Paul");
            p.setSurname("Dirac");
            p.setDescription("a given description");
            final Phone phone1 = new Phone();
            phone1.setType(PhoneType.Home);
            phone1.setNumber("934567843");
            phone1.setDescription("Home phone");
            p.addPhone(phone1);
            sc.createPerson(p);
            p.setName("Paul J.");
            p.setAge(38);
            sc.updatePerson(p);
            assertEquals(p.getName(), sc.findByPersonById(p.getId()).getName());
            sc.deletePerson(p.getId());
        } catch (Throwable thr) {
            LOGGER.error("Exception arised in funcPersonFacadeImpl", thr);
            fail(thr.toString());
        }
    }

    /**
   *  <p>Generic test method to check the main functionalities of {@link PersonFacadeImpl}
   *     where you manage your transaction</p>
   *
   *  <p>IMPORTANT NOTE : it is not started automatically a transaction</p>
   * 
   *  TODO Implement properly this method
   */
    @Test
    @ManagedTransaction
    public void funcPersonFacadeImplManaged() {
        try {
            TransactionalTestSupport.beginTransaction();
            Person p = new Person();
            p.setAddress("Sesam street");
            p.setAge(28);
            p.setName("Paul");
            p.setSurname("Dirac");
            p.setDescription("a given description");
            final Phone phone1 = new Phone();
            phone1.setType(PhoneType.Home);
            phone1.setNumber("934567843");
            phone1.setDescription("Home phone");
            p.addPhone(phone1);
            sc.createPerson(p);
            TransactionalTestSupport.commitTransaction();
            TransactionalTestSupport.beginTransaction();
            p = sc.findByPersonById(p.getId());
            p.setName("Paul J.");
            p.setAge(38);
            final Phone phone2 = new Phone();
            phone2.setType(PhoneType.Work);
            phone2.setNumber("934567943");
            phone2.setDescription("Work phone");
            p.addPhone(phone2);
            sc.updatePerson(p);
            TransactionalTestSupport.commitTransaction();
            TransactionalTestSupport.beginTransaction();
            assertEquals(p.getName(), sc.findByPersonById(p.getId()).getName());
            TransactionalTestSupport.commitTransaction();
            TransactionalTestSupport.beginTransaction();
            sc.deletePerson(p.getId());
            TransactionalTestSupport.commitTransaction();
        } catch (Throwable thr) {
            LOGGER.error("Exception arised in funcPersonFacadeImplManaged", thr);
            fail(thr.toString());
        }
    }

    /**
    * <p>Method to custom initialization before each test method
    *    is invoked.</p>
    *
    */
    @Before
    public void setUp() {
    }

    /**
    * <p>Method to shutdown resources after invocation of each test method</p>
    *
    */
    @After
    public void tearDown() {
    }

    /**
    * <p>Starts HSQLDB database and configure your {@link PersistenceManager}</p>
    *
	* @throws java.lang.Exception
	*/
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        final DatabaseConfigurer dbc = new DatabaseConfigurer() {

            public int getPort() {
                return Integer.parseInt(TransactionalTestSupport.DEFAULT_PORT);
            }

            public String getDatabaseName() {
                return TransactionalTestSupport.DEFAULT_DATABASE_NAME;
            }
        };
        TransactionalTestSupport.startDatabase(dbc);
        final SessionFactoryBuilder builder = new SessionFactoryBuilder() {

            public void handleHibernateProperties(final Properties hibernateProperties) {
            }

            public void handleLocalSessionFactoryBean(final LocalSessionFactoryBean lsf) {
            }

            public LocalSessionFactoryBean newLocalSessionFactoryBean() {
                final AnnotationLocatorSessionFactoryBean alsfb = new AnnotationLocatorSessionFactoryBean();
                final Scanner scanner = new DefaultScanner();
                scanner.addResourceName("nexopen.properties");
                alsfb.setScanner(scanner);
                return alsfb;
            }
        };
        final PersistenceManager pm = TransactionalTestSupport.createPersistenceManager(builder);
        sc = new PersonFacadeImpl();
        sc.pm = pm;
    }

    /**
    * <p>Executed after finish the test for {@link PersonFacadeImpl}</p>
    *
    * @throws java.lang.Exception
    */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        TransactionalTestSupport.stopDatabase();
        TransactionalTestSupport.release();
    }

    /**
	 * <p>performs the data loader of the given resource</p>
	 * 
	 * @throws IOException 
	 */
    static void sqlDataLoader(String sqlFileName) throws IOException {
        InputStream is = ResourceLocator.getResourceAsStream(sqlFileName);
        try {
            final Resource sqlFile = new InputStreamResource(is);
            final SQLDataLoader loader = new SQLDataLoader();
            loader.setFile(sqlFile);
            loader.setDataSource(TransactionalTestSupport.getDataSource());
            loader.afterPropertiesSet();
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }
}
