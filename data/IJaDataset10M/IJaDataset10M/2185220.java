package org.wfp.rita.test.hibernate;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.wfp.rita.base.RitaException;
import org.wfp.rita.datafacade.DataFacade;
import org.wfp.rita.pojo.base.VersionedRecord;
import org.wfp.rita.pojo.face.RecordOwner;
import org.wfp.rita.rpc.DataCarrier;

/**
 * Hibernate fails to join one table to another's non-primary key fields.
 * 
 * http://opensource.atlassian.com/projects/hibernate/browse/HHH-4595
 * @author chris
 */
public class HibernateCloneObjectCausesCollectionRemoveException extends TestCase {

    @Entity
    @Table(name = "project")
    private static class Project {

        @Id
        @GenericGenerator(name = "generator", strategy = "increment")
        @GeneratedValue(generator = "generator")
        public Integer id;

        @OneToMany
        @JoinColumn(name = "project_id")
        public Set<Site> sites = new HashSet<Site>(0);
    }

    @Entity
    @Table(name = "site")
    private static class Site {

        @Id
        @GenericGenerator(name = "generator", strategy = "increment")
        @GeneratedValue(generator = "generator")
        public Integer id;

        @ManyToOne
        @JoinColumn(name = "project_id")
        public Project project;
    }

    /**
     * Make protected method visible for testing. 
     * @author chris
     */
    private class TestDataCarrier extends DataCarrier {

        public TestDataCarrier(Object source, Session session, SessionFactory factory, Configuration config) throws RitaException {
            super(source, session, factory, config);
        }

        @Override
        protected Object decode(RecordOwner owner, Session session, SessionFactory factory, Configuration config) throws RitaException {
            return super.decode(owner, session, factory, config);
        }
    }

    public void test() throws Exception {
        AnnotationConfiguration conf = new AnnotationConfiguration();
        conf.addAnnotatedClass(Project.class);
        conf.addAnnotatedClass(Site.class);
        conf.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        conf.setProperty("hibernate.connection.url", "jdbc:mysql://localhost/test");
        conf.setProperty("hibernate.connection.username", "root");
        conf.setProperty("hibernate.connection.password", "");
        conf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        SessionFactory fact = conf.buildSessionFactory();
        SchemaExport exporter = new SchemaExport(conf, ((SessionFactoryImpl) fact).getSettings());
        exporter.setHaltOnError(false);
        exporter.execute(true, true, true, false);
        exporter.setHaltOnError(true);
        exporter.execute(true, true, false, true);
        for (Object e : exporter.getExceptions()) {
            throw (Exception) e;
        }
        Session session = fact.openSession();
        Project proj = new Project();
        session.save(proj);
        session.flush();
        Site site1 = new Site();
        site1.project = proj;
        session.save(site1);
        session.flush();
        assertEquals(0, proj.sites.size());
        session.refresh(proj);
        assertEquals(1, proj.sites.size());
        TestDataCarrier dc = new TestDataCarrier(proj, session, fact, conf);
        Object clone = dc.decode(new RecordOwner() {

            @Override
            public boolean isOurObject(VersionedRecord record) throws RitaException {
                return true;
            }
        }, session, fact, conf);
        session.delete(proj);
        session.flush();
        session.close();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(HibernateCloneObjectCausesCollectionRemoveException.class);
    }
}
