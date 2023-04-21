package br.com.settech.querycreator.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;

public class ConfigureTemporalDataBase {

    public static void main(String[] args) {
    }

    public static SessionFactory createSessionFactoryAndPopulateTheDatabase() {
        AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration();
        annotationConfiguration.addAnnotatedClass(User.class).addAnnotatedClass(Address.class);
        annotationConfiguration.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect").setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:filter_example_database").setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver").setProperty("hibernate.hbm2ddl.auto", "create-drop").setProperty("hibernate.connection.username", "sa").setProperty("hibernate.connection.password", "").setProperty("hibernate.connection.autocommit", "true").setProperty("hibernate.show_sql", "true");
        SessionFactory sessionFactory = annotationConfiguration.buildSessionFactory();
        Session session = sessionFactory.openSession();
        User user = new User("Alberto Souza", "alberto@email.com.br");
        User user2 = new User("Vinicius", "vinicius@email.com.br");
        Address userAddress1 = new Address(user, "rio vermelho");
        Address userAddress2 = new Address(user, "brotas");
        Address userAddress3 = new Address(user, "pituba");
        Address user2Address1 = new Address(user2, "rio vermelho");
        Address user2Address2 = new Address(user2, "iguatemi");
        Address user2Address3 = new Address(user2, "barra");
        user.addAddress(userAddress1).addAddress(userAddress2).addAddress(userAddress3);
        user2.addAddress(user2Address3).addAddress(user2Address2).addAddress(user2Address1);
        session.save(user);
        session.save(user2);
        session.close();
        return sessionFactory;
    }
}
