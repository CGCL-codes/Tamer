package net.taylor.identity.entity;

import javax.persistence.EntityManager;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.taylor.embedded.Bootstrap;
import net.taylor.testing.JpaCrudTest;

/**
 * Unit tests for the User.
 *
 * @author jgilbert01
 * @version $Id: JpaJUnit.javajet,v 1.7 2007/11/13 16:35:44 jgilbert01 Exp $
 * @generated
 */
public class UserTest extends JpaCrudTest<User> {

    /** @generated */
    public UserTest(String name) {
        super(name);
    }

    /** @generated */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new UserTest("testCrud"));
        return new Bootstrap(suite);
    }

    /** @generated */
    protected void initData(EntityManager em) throws Exception {
    }

    /** @NOT generated */
    protected void prePersist(EntityManager em) throws Exception {
        entity = new User();
        entity.setId("tester1");
        entity.setPassword("tester1");
        entity.setConfirmation("tester1");
        entity.setName("Tester One");
        entity.setEmail("user@taylor.net");
    }

    /** @NOT generated */
    protected void preMerge(EntityManager em) throws Exception {
        entity.setName("Updated Tester One");
    }

    /** @generated */
    protected void postMerge(EntityManager em) throws Exception {
    }

    /** @generated */
    protected void preRemove(EntityManager em) throws Exception {
    }
}
