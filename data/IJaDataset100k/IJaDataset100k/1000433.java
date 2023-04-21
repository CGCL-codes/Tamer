package com.legstar.cixs.jbossesb.test.lsfileac;

import com.legstar.cixs.jbossesb.testers.adapter.jms.AbstractAdapterObjectJmsClientTester;
import com.legstar.test.coxb.LsfileacCases;
import com.legstar.test.coxb.lsfileac.QueryData;
import com.legstar.test.coxb.lsfileac.QueryLimit;

/**
 * Test the generated LSFILEAC adapter using Objects.
 *
 */
public class LsfileacObjectJmsActionTest extends AbstractAdapterObjectJmsClientTester {

    /**
     * Construct the test case.
     */
    public LsfileacObjectJmsActionTest() {
        super("lsfileac", getRequestObject(), null);
    }

    /** {@inheritDoc} */
    public void checkObjects(final Object expectedObject, final Object actualObject) {
        LsfileacResponseHolder reply = (LsfileacResponseHolder) actualObject;
        LsfileacCases.checkJavaObjectReplyData(reply.getReplyData());
        LsfileacCases.checkJavaObjectReplyStatus(reply.getReplyStatus());
    }

    /**
     * @return a composite holder with 2 inner value objects
     */
    public static LsfileacRequestHolder getRequestObject() {
        QueryData qdt = LsfileacCases.getJavaObjectQueryData();
        QueryLimit qlt = LsfileacCases.getJavaObjectQueryLimit();
        LsfileacRequestHolder result = new LsfileacRequestHolder();
        result.setQueryData(qdt);
        result.setQueryLimit(qlt);
        return result;
    }

    /** Containers not supported by CICSTS23. */
    public void testRoundTripCICSTS23DirectHttp() {
        return;
    }

    /** Containers not supported by CICSTS23. */
    public void testRoundTripCICSTS23DirectMQ() {
        return;
    }

    /** Containers not supported by CICSTS23. */
    public void testRoundTripCICSTS23DirectSocket() {
        return;
    }

    /** Containers not supported by CICSTS23. */
    public void testRoundTripCICSTS23PooledHttp() {
        return;
    }

    /** Containers not supported by CICSTS23. */
    public void testRoundTripCICSTS23PooledMQ() {
        return;
    }

    /** Containers not supported by CICSTS23. */
    public void testRoundTripCICSTS23PooledSocket() {
        return;
    }

    /** Containers not supported by CICS MQ Bridge. */
    public void testRoundTripCICSTS31DirectMQ() {
        return;
    }

    /** Containers not supported by CICS MQ Bridge. */
    public void testRoundTripCICSTS31PooledMQ() {
        return;
    }
}
