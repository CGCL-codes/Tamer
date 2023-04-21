package com.google.code.hibernate.rest.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;

/**
 * 
 * @author wangzijian
 * 
 */
public abstract class ExampleSessionFactoryBuilder {

    private ExampleSessionFactoryBuilder() {
    }

    public static SessionFactory build() {
        return new AnnotationConfiguration().addAnnotatedClass(Book.class).addAnnotatedClass(Student.class).addAnnotatedClass(Teacher.class).addAnnotatedClass(StudentTeacher.class).setProperty(Environment.DRIVER, org.h2.Driver.class.getName()).setProperty(Environment.DIALECT, H2Dialect.class.getName()).setProperty(Environment.HBM2DDL_AUTO, "create-drop").setProperty(Environment.SHOW_SQL, "false").setProperty(Environment.FORMAT_SQL, "true").setProperty(Environment.URL, "jdbc:h2:mem:").setProperty(Environment.USER, "sa").setProperty(Environment.PASS, "").buildSessionFactory();
    }
}
