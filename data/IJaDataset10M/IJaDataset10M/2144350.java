package org.vizzini.ai.logic;

/**
 * Provides unit tests for the <code>ComplexSentenceOR</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public class ComplexSentenceORTest extends AbstractComplexSentenceTest {

    /** First value. */
    private static final Object VALUE00 = "value0";

    /** First left. */
    private static final IArgument LEFT0 = new LiteralArgument(VALUE00);

    /** First right. */
    private static final IArgument RIGHT0 = new LiteralArgument(VALUE00);

    /** First property name. */
    private static final String PROPERTY_NAME0 = "someProperty";

    /** Second right. */
    private static final IArgument RIGHT1 = new PropertyArgument(PROPERTY_NAME0);

    /** First left sentence. */
    private static final ISentence LEFT_S0 = SentenceFactory.TRUE;

    /** First right sentence. */
    private static final ISentence RIGHT_S0 = SentenceFactory.TRUE;

    /** Second left sentence. */
    private static final ISentence LEFT_S1 = new AtomicSentenceEQ(LEFT0, RIGHT0, true);

    /** Second right sentence. */
    private static final ISentence RIGHT_S1 = new AtomicSentenceEQ(LEFT0, RIGHT1, true);

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.2
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ComplexSentenceORTest.class);
    }

    /**
     * Test the <code>search()</code> method.
     *
     * @since  v0.2
     */
    public void testEvaluate() {
        Exception exception = null;
        try {
            Object[][] truthTable = { { SentenceFactory.FALSE, SentenceFactory.FALSE, Boolean.FALSE }, { SentenceFactory.FALSE, SentenceFactory.TRUE, Boolean.TRUE }, { SentenceFactory.TRUE, SentenceFactory.FALSE, Boolean.TRUE }, { SentenceFactory.TRUE, SentenceFactory.TRUE, Boolean.TRUE } };
            for (int i = 0; i < truthTable.length; i++) {
                ISentence left = (ISentence) truthTable[i][0];
                ISentence right = (ISentence) truthTable[i][1];
                boolean expected = ((Boolean) truthTable[i][2]).booleanValue();
                IComplexSentence sentence = new ComplexSentenceOR(left, right);
                assertEquals(expected, sentence.evaluate(null));
            }
        } catch (LogicException e) {
            exception = e;
        }
        assertNull(exception);
    }

    /**
     * Test the <code>search()</code> method.
     *
     * @since  v0.2
     */
    public void testEvaluate1() {
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
     * @see  org.vizzini.ai.logic.AbstractComplexSentenceTest#testGetLeft()
     */
    @Override
    public void testGetLeft() {
        IComplexSentence sentence0 = (IComplexSentence) _sentence0;
        assertEquals(LEFT_S0, sentence0.getLeft());
        IComplexSentence sentence1 = (IComplexSentence) _sentence1;
        assertEquals(LEFT_S1, sentence1.getLeft());
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractComplexSentenceTest#testGetRight()
     */
    @Override
    public void testGetRight() {
        IComplexSentence sentence0 = (IComplexSentence) _sentence0;
        assertEquals(RIGHT_S0, sentence0.getRight());
        IComplexSentence sentence1 = (IComplexSentence) _sentence1;
        assertEquals(RIGHT_S1, sentence1.getRight());
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractComplexSentenceTest#testToDebugString()
     */
    @Override
    public void testToDebugString() {
        String expected0 = "org.vizzini.ai.logic.ComplexSentenceOR [connective=OR,description=null,left=(org.vizzini.ai.logic.LiteralArgument [argument=Literal,value=true] null org.vizzini.ai.logic.LiteralArgument [argument=Literal,value=true]),right=(org.vizzini.ai.logic.LiteralArgument [argument=Literal,value=true] null org.vizzini.ai.logic.LiteralArgument [argument=Literal,value=true]),sentenceType=Complex]";
        System.out.println("expected = " + _sentence0.toDebugString());
        assertEquals(expected0, _sentence0.toDebugString());
        String expected1 = "org.vizzini.ai.logic.ComplexSentenceOR [connective=OR,description=null,left=(org.vizzini.ai.logic.LiteralArgument [argument=Literal,value=value0] is org.vizzini.ai.logic.LiteralArgument [argument=Literal,value=value0]),right=(org.vizzini.ai.logic.LiteralArgument [argument=Literal,value=value0] is org.vizzini.ai.logic.PropertyArgument [argument=Property,propertyName=someProperty]),sentenceType=Complex]";
        System.out.println("expected = " + _sentence1.toDebugString());
        assertEquals(expected1, _sentence1.toDebugString());
        assertEquals(expected0, _sentence2.toDebugString());
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractComplexSentenceTest#create()
     */
    @Override
    protected ISentence create() {
        return new ComplexSentenceOR(LEFT_S0, RIGHT_S0);
    }

    /**
     * @see  org.vizzini.ai.logic.AbstractComplexSentenceTest#create1()
     */
    @Override
    protected ISentence create1() {
        return new ComplexSentenceOR(LEFT_S1, RIGHT_S1);
    }
}
