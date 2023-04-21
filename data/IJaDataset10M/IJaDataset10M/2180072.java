package net.taylor.agile.entity;

import java.util.Date;
import javax.persistence.EntityManager;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.taylor.embedded.Bootstrap;
import net.taylor.testing.JpaCrudTest;

/**
 * Unit tests for the Burn.
 *
 * @author jgilbert
 * @generated
 */
public class BurnTest extends JpaCrudTest<Burn> {

    /** @generated */
    public BurnTest(String name) {
        super(name);
    }

    /** @generated */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new BurnTest("testCrud"));
        return new Bootstrap(suite);
    }

    /** @generated */
    protected void initData(EntityManager em) throws Exception {
    }

    /** @generated */
    protected void prePersist(EntityManager em) throws Exception {
        BurnBuilder builder = BurnBuilder.instance();
        builder.date(truncateDate(new Date()));
        builder.total(0l);
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
