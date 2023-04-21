package net.sf.molae.pipe.test;

import net.sf.molae.pipe.binop.BinaryFunction;

/**
 * Test methods to check the contract of <code>BinaryFunction</code>
 * implementing classes where both operands have the same type.
 * @version 2.0
 * @author Ralph Wagner
 */
public class S2BinaryFunctionTest<B extends BinaryFunction<S, S, T>, S, T> extends BinaryFunctionTest<B, S, S, T> {

    /**
     * Creates a new test with the specified candidate.
     * @param testObject the candidate of the test
     * @throws NullPointerException if the specified test objects is
     * <code>null</code>.
     */
    public S2BinaryFunctionTest(B testObject) {
        super(testObject);
    }

    /**
     * Creates a new instance of this class with the specified testObject
     * as test object.
     * @param testObject the candidate of the test
     * @return an instance of this class with the specified testObject
     * as test object.
     * @throws NullPointerException if the specified test objects is
     * <code>null</code>.
     */
    public static final <S, T> S2BinaryFunctionTest<BinaryFunction<S, S, T>, S, T> getInstance(BinaryFunction<S, S, T> testObject) {
        return new S2BinaryFunctionTest<BinaryFunction<S, S, T>, S, T>(testObject);
    }

    private void testUsing(S arg1, S arg2) {
        testWith(arg1);
        testWith(arg1, arg1);
        testWith(arg1, arg2);
        testWith(arg1, arg1, arg1);
        testWith(arg1, arg1, arg2);
        testWith(arg1, arg2, arg1);
        testWith(arg1, arg2, arg2);
    }

    /**
     * Performs various tests using the specified objects as arguments
     * to the compute function of the tested binary function.
     * @param arg1 a valid left and right argument to the underlying binary
     * function
     * @param arg2 a valid left and right argument to the underlying binary
     * function
     * @throws AssertionFailedError if the <code>BinaryFunction</code>
     * contract is broken by the test object.
     */
    public void testProperties(S arg1, S arg2) {
        testUsing(arg1, arg2);
        testUsing(arg2, arg1);
    }

    /**
     * Performs various tests using the specified objects as arguments
     * to the compute function of the tested binary function.
     * @param arg1 a valid left and right argument to the underlying binary
     * function
     * @param arg2 a valid left and right argument to the underlying binary
     * function
     * @param arg3 a valid left and right argument to the underlying binary
     * function
     * @throws AssertionFailedError if the <code>BinaryFunction</code>
     * contract is broken by the test object.
     */
    public void testProperties(S arg1, S arg2, S arg3) {
        testProperties(arg1, arg2);
        testProperties(arg1, arg3);
        testProperties(arg2, arg3);
        testWith(arg1, arg2, arg3);
        testWith(arg1, arg3, arg2);
        testWith(arg2, arg1, arg3);
        testWith(arg2, arg3, arg1);
        testWith(arg3, arg1, arg2);
        testWith(arg3, arg2, arg1);
    }
}
