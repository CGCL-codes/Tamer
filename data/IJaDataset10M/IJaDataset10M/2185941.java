package integrationTests;

import org.junit.*;

public final class BooleanExpressionsTest extends CoverageTest {

    BooleanExpressions tested;

    @Test
    public void evalBuggyCombination() {
        assertTrue(tested.eval1(true, false, 1));
        findMethodData(7, "eval1");
        assertPaths(4, 1, 1);
    }

    @Test
    public void evalOnlySomeCombinations() {
        assertTrue(tested.eval1(true, true, 0));
        assertFalse(tested.eval1(true, false, 0));
        findMethodData(7, "eval1");
        assertPaths(4, 3, 3);
    }

    @Test
    public void evalAllCombinations() {
        assertTrue(tested.eval2(true, true, 0));
        assertTrue(tested.eval2(true, false, 1));
        assertFalse(tested.eval2(true, false, 0));
        assertFalse(tested.eval2(false, true, 0));
        findMethodData(12, "eval2");
        assertPaths(4, 4, 4);
    }

    @Test
    public void evalAllPaths() {
        assertFalse(tested.eval3(false, true, false));
        assertTrue(tested.eval3(true, true, false));
        assertTrue(tested.eval3(true, false, true));
        assertFalse(tested.eval3(true, false, false));
        findMethodData(17, "eval3");
        assertPaths(4, 4, 4);
    }

    @Test
    public void evalOnlyFirstAndSecondBranches() {
        assertFalse(tested.eval4(false, true, false));
        assertFalse(tested.eval4(false, false, false));
        assertFalse(tested.eval4(false, true, true));
        assertFalse(tested.eval4(false, false, true));
        assertTrue(tested.eval4(true, false, false));
        assertTrue(tested.eval4(true, false, true));
        findMethodData(22, "eval4");
        assertPaths(4, 2, 6);
    }

    @Test
    public void eval5() {
        assertFalse(tested.eval5(false, true, true));
        assertTrue(tested.eval5(false, false, false));
        findMethodData(27, "eval5");
        assertPaths(4, 2, 2);
        assertRegularPath(4, 0);
        assertRegularPath(8, 1);
        assertRegularPath(10, 0);
        assertShadowedPath(14, 0);
        assertRegularPath(13, 1);
    }

    @Test
    public void methodWithComplexExpressionWhichCallsAnotherInSameClass() {
        BooleanExpressions.isSameTypeIgnoringAutoBoxing(int.class, Integer.class);
        findMethodData(35, "isSameTypeIgnoringAutoBoxing");
        assertPaths(8, 1, 1);
        findMethodData(43, "isWrapperOfPrimitiveType");
        assertPaths(63, 1, 1);
    }

    @Test
    public void trivialMethodWhichReturnsBooleanInput() {
        assertTrue(tested.simplyReturnsInput(true));
        assertFalse(tested.simplyReturnsInput(false));
        findMethodData(53, "simplyReturnsInput");
        assertPaths(1, 1, 2);
        assertPath(2, 2);
    }

    @Test
    public void methodWhichReturnsNegatedBoolean() {
        assertTrue(tested.returnsNegatedInput(false));
        findMethodData(58, "returnsNegatedInput");
        assertPaths(1, 1, 1);
        assertShadowedPath(6, 0);
        assertRegularPath(5, 1);
    }

    @Test
    public void methodWithIfElseAndTrivialTernaryOperator() {
        assertTrue(tested.returnsTrivialResultFromInputAfterIfElse(false, 1));
        assertFalse(tested.returnsTrivialResultFromInputAfterIfElse(true, 0));
        findMethodData(65, "returnsTrivialResultFromInputAfterIfElse");
        assertPaths(2, 2, 2);
        assertShadowedPath(11, 0);
        assertRegularPath(10, 1);
        assertShadowedPath(10, 1);
        assertRegularPath(9, 1);
    }

    @Test
    public void methodWithTrivialTernaryOperatorAndTrivialIfElse() {
        assertTrue(tested.returnsResultPreviouslyComputedFromInput(false, 1));
        assertFalse(tested.returnsResultPreviouslyComputedFromInput(false, 0));
        assertTrue(tested.returnsResultPreviouslyComputedFromInput(true, 1));
        assertTrue(tested.returnsResultPreviouslyComputedFromInput(true, -1));
        findMethodData(77, "returnsResultPreviouslyComputedFromInput");
        assertPaths(4, 3, 4);
        assertPath(12, 1);
        assertPath(11, 1);
        assertPath(11, 0);
        assertPath(10, 2);
    }
}
