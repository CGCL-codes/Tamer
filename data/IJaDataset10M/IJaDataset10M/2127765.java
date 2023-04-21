package net.sf.blunder;

import java.net.SocketException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractLookupTest {

    @Autowired
    private Blunder blunder;

    @Before
    public void onSetUp() {
        System.setProperty("net.sf.blunder.autolearning", "false");
    }

    public Blunder getBlunder() {
        return blunder;
    }

    public void setBlunder(Blunder blunder) {
        this.blunder = blunder;
    }

    @Test
    public void testOnlyLeafException() {
        Exception e = new DummyTestCaseException03("test case");
        Blunder ep = getBlunder().process(e);
        Assert.assertFalse("Error should be defined in database.", ep.isUndefinedError());
        Assert.assertTrue(ep.isBusinessException(e));
        Assert.assertTrue(ep.isBusinessError());
        Assert.assertFalse(ep.isBusinessException(getTestCaseJavaLangException()));
        Assert.assertEquals(e, ep.getLeafException());
        Assert.assertEquals(e, ep.getRootException());
        Assert.assertNull(ep.getBusinessException());
        e = getTestCaseJavaLangException();
        ep = getBlunder().process(e);
        Assert.assertFalse("Error should be defined in database.", ep.isUndefinedError());
        Assert.assertEquals(e, ep.getLeafException());
        Assert.assertEquals(e, ep.getRootException());
        Assert.assertNull(ep.getBusinessException());
    }

    @Test
    public void testRootLeafExceptionsChained() {
        Exception dummyExcep01 = new DummyTestCaseException01();
        Exception dummyExcep03 = new DummyTestCaseException03("test case", dummyExcep01);
        Blunder ep = getBlunder().process(dummyExcep03);
        Assert.assertFalse("Error should be defined in database.", ep.isUndefinedError());
        Assert.assertTrue(ep.isServerError());
        Assert.assertTrue(ep.isBusinessException(dummyExcep03));
        Assert.assertEquals(dummyExcep03, ep.getLeafException());
        Assert.assertEquals(dummyExcep01, ep.getRootException());
        Assert.assertNull(ep.getBusinessException());
    }

    @Test
    public void testRootLeafBusinessExceptionsChained() {
        Exception dummyExcep02 = new DummyTestCaseException02();
        Exception dummyExcep01 = new DummyTestCaseException01("mesage", dummyExcep02);
        Exception dummyExcep03 = new DummyTestCaseException03("test case", dummyExcep01);
        Blunder ep = getBlunder().process(dummyExcep03);
        Assert.assertFalse("Error should be defined in database.", ep.isUndefinedError());
        Assert.assertTrue(ep.isBusinessException(dummyExcep03));
        Assert.assertTrue(ep.isBusinessError());
        Assert.assertEquals(dummyExcep03, ep.getLeafException());
        Assert.assertEquals(dummyExcep02, ep.getRootException());
        Assert.assertNull(ep.getBusinessException());
    }

    @Test
    public void testRootLeafNotBusinessExceptionsChained() {
        Exception socketExcep = new SocketException();
        Exception dummyExcep02 = new DummyTestCaseException02("http", socketExcep);
        Exception dummyExcep01 = new DummyTestCaseException01("message", dummyExcep02);
        Exception dummyExcep03 = new DummyTestCaseException03("test case", dummyExcep01);
        Blunder ep = getBlunder().process(dummyExcep03);
        Assert.assertFalse("Error should be defined in database.", ep.isUndefinedError());
        Assert.assertTrue(ep.isBusinessException(dummyExcep03));
        Assert.assertTrue(ep.isServerError());
        Assert.assertFalse(ep.isBusinessException(socketExcep));
        Assert.assertEquals(dummyExcep03, ep.getLeafException());
        Assert.assertEquals(socketExcep, ep.getRootException());
        Assert.assertNotNull(ep.getBusinessException());
        Assert.assertEquals(dummyExcep02, ep.getBusinessException());
        Exception ise = new IllegalStateException(socketExcep);
        ep = getBlunder().process(ise);
        Assert.assertFalse("Error should be defined in database.", ep.isUndefinedError());
        Assert.assertFalse(ep.isBusinessException(socketExcep));
        Assert.assertTrue(ep.isUserError());
        Assert.assertFalse(ep.isBusinessException(ise));
        Assert.assertEquals(socketExcep, ep.getRootException());
        Assert.assertEquals(ise, ep.getLeafException());
        Assert.assertNull(ep.getBusinessException());
        dummyExcep03 = new DummyTestCaseException03("test", socketExcep);
        ise = new IllegalStateException(dummyExcep03);
        ep = getBlunder().process(ise);
        Assert.assertFalse("Error should be defined in database.", ep.isUndefinedError());
        Assert.assertFalse(ep.isBusinessException(ise));
        Assert.assertEquals(socketExcep, ep.getRootException());
        Assert.assertEquals(ise, ep.getLeafException());
        Assert.assertNotNull(ep.getBusinessException());
        Assert.assertEquals(dummyExcep03, ep.getBusinessException());
        dummyExcep03 = new DummyTestCaseException03("test");
        ise = new IllegalStateException(dummyExcep03);
        ep = getBlunder().process(ise);
        Assert.assertFalse("Error should be defined in database.", ep.isUndefinedError());
        Assert.assertFalse(ep.isBusinessException(ise));
        Assert.assertEquals(dummyExcep03, ep.getRootException());
        Assert.assertEquals(ise, ep.getLeafException());
        Assert.assertNull(ep.getBusinessException());
    }

    @Test
    public void testExceptionRelationPosibleSolutions() {
        String causa1 = "Cause 1";
        String causa2 = "Error cause 2";
        Blunder ep = getBlunder().process(getTestCaseJavaLangException());
        Assert.assertFalse("Error should be defined in database.", ep.isUndefinedError());
        ep.getExceptionRelation().setPossibleSolutionsList("{\"" + causa1 + "\",\"" + causa2 + "\"}");
        String[] solutions = ep.getPossibleSolutions();
        Assert.assertEquals(2, solutions.length);
        Assert.assertEquals(causa1, solutions[0]);
        Assert.assertEquals(causa2, solutions[1]);
        String notOgnlCause = "not a ognl message cause";
        ep.getExceptionRelation().setPossibleSolutionsList(notOgnlCause);
        solutions = ep.getPossibleSolutions();
        Assert.assertEquals(1, solutions.length);
        Assert.assertEquals(notOgnlCause, solutions[0]);
    }

    @Test
    public void testLookUp() {
        Exception socketExcep = new SocketException();
        Exception dummyExcep02 = new DummyTestCaseException02("http", socketExcep);
        Exception dummyExcep01 = new DummyTestCaseException01("message", dummyExcep02);
        Exception dummyExcep03 = new DummyTestCaseException03("test case", dummyExcep01);
        Assert.assertFalse("Should be defined", getBlunder().process(dummyExcep03).isUndefinedError());
        Assert.assertTrue(getBlunder().process(dummyExcep03).isServerError());
        Assert.assertTrue(getBlunder().process(new IllegalStateException(socketExcep)).isUserError());
        Assert.assertTrue(getBlunder().process(new IllegalStateException(dummyExcep03)).isUserError());
        Assert.assertEquals(getBlunder().process(new IllegalStateException(socketExcep)).getExceptionRelation(), getBlunder().process(new IllegalStateException(dummyExcep03)).getExceptionRelation());
    }

    private Exception getTestCaseJavaLangException() {
        return new Exception("test case only");
    }

    private class DummyTestCaseException01 extends Exception {

        public DummyTestCaseException01() {
            super();
        }

        public DummyTestCaseException01(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private class DummyTestCaseException02 extends Exception {

        public DummyTestCaseException02() {
            super();
        }

        public DummyTestCaseException02(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private class DummyTestCaseException03 extends Exception {

        public DummyTestCaseException03(String message, Throwable cause) {
            super(message, cause);
        }

        public DummyTestCaseException03(String message) {
            super(message);
        }
    }
}
