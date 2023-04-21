package net.taylor.identity.entity;

import javax.persistence.EntityManager;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.taylor.embedded.Bootstrap;
import net.taylor.testing.JpaCrudTest;

/**
 * Unit tests for the GroupType.
 *
 * @author jgilbert
 * @generated
 */
public class GroupTypeTest extends JpaCrudTest<GroupType> {

    /** @generated */
    public GroupTypeTest(String name) {
        super(name);
    }

    /** @generated */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new GroupTypeTest("testCrud"));
        return new Bootstrap(suite);
    }

    /** @generated */
    protected void initData(EntityManager em) throws Exception {
    }

    /** @generated */
    protected void prePersist(EntityManager em) throws Exception {
        GroupTypeBuilder builder = GroupTypeBuilder.instance();
        builder.name("name");
        builder.description("description");
        builder.leaf(true);
        entity = builder.build();
    }

    /** @generated */
    protected void preMerge(EntityManager em) throws Exception {
    }

    /** @generated */
    protected void postMerge(EntityManager em) throws Exception {
    }

    /** @generated */
    protected void preRemove(EntityManager em) throws Exception {
    }
}
