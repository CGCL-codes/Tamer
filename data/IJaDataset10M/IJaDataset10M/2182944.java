package delphorm.dao.tests;

import java.util.Properties;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import delphorm.dao.personne.ImplHibernateUtilisateur;
import delphorm.entite.personne.Utilisateur;

public class TestSupprimerUtilisateur extends TestCase {

    LocalSessionFactoryBean sessionFactory;

    DriverManagerDataSource datasource;

    ImplHibernateUtilisateur daoutilisateur;

    Utilisateur utilisateur;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestSupprimerUtilisateur.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        datasource = new DriverManagerDataSource();
        datasource.setDriverClassName("org.postgresql.Driver");
        datasource.setUsername("admin");
        datasource.setPassword("admin");
        datasource.setUrl("jdbc:postgresql://localhost:5432/autoformdb");
        sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(datasource);
        String[] fichiersmapping = new String[] { "mapping/utilisateur.hbm.xml", "mapping/groupe.hbm.xml", "mapping/groupecollectif.hbm.xml", "mapping/droitGroupe.hbm.xml" };
        sessionFactory.setMappingResources(fichiersmapping);
        Properties prop = new Properties();
        prop.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        prop.put("current_session_context_class", "thread");
        prop.put("show_sql", "true");
        sessionFactory.setHibernateProperties(prop);
        sessionFactory.afterPropertiesSet();
        SessionFactory sf = (SessionFactory) sessionFactory.getObject();
        daoutilisateur = new ImplHibernateUtilisateur();
        daoutilisateur.setSessionFactory(sf);
    }

    public void testSupprimerUtilisateurParId() {
        String login = new String("logintest");
        utilisateur = daoutilisateur.getUtilisateurParLogin(login);
        Assert.assertNotNull(utilisateur);
        Assert.assertNotNull(utilisateur.getId());
        daoutilisateur.supprimerUtilisateurParId(utilisateur.getId());
        utilisateur = daoutilisateur.getUtilisateurParLogin(login);
        Assert.assertNull(utilisateur);
    }
}
