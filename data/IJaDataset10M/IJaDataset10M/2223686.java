package org.vizzini.ai.logic;

/**
 * Provides unit tests for the <code>AtomicSentenceEQ</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public class AtomicSentenceEQTest extends AbstractAtomicSentenceTest {

    /** First value. */
    private static final Object VALUE00 = "true";

    /** First left. */
    private static final IArgument LEFT0 = new LiteralArgument(Boolean.TRUE);

    /** First right. */
    private static final IArgument RIGHT0 = new LiteralArgument(Boolean.TRUE);

    /** First property name. */
    private static final String PROPERTY_NAME0 = "someProperty";

    /** Second left. */
    private static final IArgument LEFT1 = new PropertyArgument(PROPERTY_NAME0);

    /** Second right. */
    private static final IArgument RIGHT1 = new PropertyArgument(PROPERTY_NAME0);

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.2
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AtomicSentenceEQTest.class);
    }

    /**
     * Test the <code>evaluate()</code> method.
     *
     * @since  v0.2
     */
    public void testEvaluate() {
        Exception exception = null;
        try {
            assertTrue(_sentence0.evaluate(null));
            PropertyArgumentTest.TestItem testItem = new PropertyArgumentTest.TestItem();
            testItem.setSomeProperty(VALUE00);
            assertTrue(_sentence1.evaluate(testItem));
            assertTrue(_sentence2.evaluate(testItem));
        } catch (LogicException e) {
            exception = e;
        }
        assertNull(exception);
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractAtomicSentenceTest#testGetLeft()
     */
    @Override
    public void testGetLeft() {
        IAtomicSentence sentence0 = (IAtomicSentence) _sentence0;
        IAtomicSentence sentence1 = (IAtomicSentence) _sentence1;
        assertEquals(LEFT0, sentence0.getLeft());
        assertEquals(LEFT1, sentence1.getLeft());
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractAtomicSentenceTest#testGetOperator()
     */
    @Override
    public void testGetOperator() {
        IAtomicSentence sentence0 = (IAtomicSentence) _sentence0;
        IAtomicSentence sentence1 = (IAtomicSentence) _sentence1;
        assertEquals(OperatorEnum.EQ, sentence0.getOperator());
        assertEquals(OperatorEnum.EQ, sentence1.getOperator());
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractAtomicSentenceTest#testGetRight()
     */
    @Override
    public void testGetRight() {
        IAtomicSentence sentence0 = (IAtomicSentence) _sentence0;
        IAtomicSentence sentence1 = (IAtomicSentence) _sentence1;
        assertEquals(RIGHT0, sentence0.getRight());
        assertEquals(RIGHT1, sentence1.getRight());
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractAtomicSentenceTest#testToDebugString()
     */
    @Override
    public void testToDebugString() {
        String expected0 = "org.vizzini.ai.logic.AtomicSentenceEQ [description=null,left=org.vizzini.ai.logic.LiteralArgument [argument=Literal,value=true],operator=is,right=org.vizzini.ai.logic.LiteralArgument [argument=Literal,value=true],sentenceType=Atomic,useForwardLogic=true]";
        System.out.println("expected = " + _sentence0.toDebugString());
        assertEquals(expected0, _sentence0.toDebugString());
        String expected1 = "org.vizzini.ai.logic.AtomicSentenceEQ [description=null,left=org.vizzini.ai.logic.PropertyArgument [argument=Property,propertyName=someProperty],operator=is,right=org.vizzini.ai.logic.PropertyArgument [argument=Property,propertyName=someProperty],sentenceType=Atomic,useForwardLogic=true]";
        System.out.println("expected = " + _sentence1.toDebugString());
        assertEquals(expected1, _sentence1.toDebugString());
        assertEquals(expected0, _sentence2.toDebugString());
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractAtomicSentenceTest#create()
     */
    @Override
    protected ISentence create() {
        return new AtomicSentenceEQ(LEFT0, RIGHT0, true);
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractAtomicSentenceTest#create1()
     */
    @Override
    protected ISentence create1() {
        return new AtomicSentenceEQ(LEFT1, RIGHT1, true);
    }
}
