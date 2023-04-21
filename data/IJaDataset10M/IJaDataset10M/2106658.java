package com.espada.bugtracker.test;

import com.espada.bugtracker.app.*;

/**
 * Title: All bugtracker tests
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class AllTests {

    Application app;

    public AllTests(String jdoConfigPath) throws Exception {
        app = newApplication(jdoConfigPath);
        System.out.println("Application created successful");
    }

    ;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: ");
            System.out.println("  java com.espada.bugtracker.test.AllTests <Castor JDO config path>");
        } else {
            AllTests allTests = new AllTests(args[0]);
            allTests.run();
        }
    }

    private Application newApplication(String JDOConfigPath) throws Exception {
        org.exolab.castor.jdo.JDO jdo = new org.exolab.castor.jdo.JDO();
        jdo.loadConfiguration(JDOConfigPath);
        jdo.setDatabaseName("bugtracker");
        return (new Application(jdo));
    }

    ;

    private void run() throws Exception {
        println("Signing up as tester (password tester)");
        signup();
        println("Signing up as tester successful");
        println("if tester forget password he receives the following:");
        println("---------------------------------------------------------");
        println(app.forgotPassword("tester@tester.com"));
        println("---------------------------------------------------------");
        Session session = app.login("tester", "tester");
        if (session == null) {
            throw new Exception("Tester failed to logged in (username 'tester' password 'tester')");
        }
        ;
        printUsers(session);
        printProjects(session);
        deleteTester(app);
    }

    ;

    private void printUsers(Session session) {
        Users users = session.getBugtracker().getUsers();
        print(users, "All Users");
        FilteredOQLCollection admins = new FilteredOQLCollection(users, "adminAsInt!=0");
        print(admins, "Administrators");
    }

    ;

    private void printProjects(Session session) {
        print(session.getBugtracker().getProjects(), "AllProjects");
    }

    ;

    private void print(java.util.Collection collection, String name) {
        println(name + " count");
        println(collection.size());
        println("printing " + name);
        println("----------------------------------");
        java.util.Iterator i = collection.iterator();
        while (i.hasNext()) {
            println(i.next());
        }
        ;
        println("-------------end-------------------");
    }

    ;

    private void println(int i) {
        System.out.println(i);
    }

    ;

    private void println(String s) {
        System.out.println(s);
    }

    ;

    private void println(Object o) {
        System.out.println(o);
    }

    ;

    private void deleteTester(Application app) throws Exception {
        println("trying to delete tester");
        println("logging in as Admin");
        Session session = app.login("Admin", "Admin");
        if (session == null) {
            throw new Exception("Admin login failed");
        }
        ;
        Users users = session.getBugtracker().getUsers();
        println("deleting tester");
        org.exolab.castor.jdo.Database db = users.getJDO().getDatabase();
        db.begin();
        try {
            users.remove(users.find("tester", "tester", db), db);
        } catch (Exception e) {
            db.rollback();
        }
        ;
        db.commit();
        println("successful");
    }

    ;

    private void signup() throws Exception {
        User user = new User();
        user.setName("tester");
        user.setPassword("tester");
        user.setEmail("tester@tester.com");
        app.signUp(user);
    }
}
