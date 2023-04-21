package joc.systemtest.posthook;

import joc.AfterHook;
import joc.Condition;
import joc.JOCCondition;
import joc.JOCEnabled;
import joc.PostCondition;
import joc.PostConditionHook;
import org.junit.Test;

public class CustomPostHookInConstructorSystemTest {

    @Test
    public void testPreHookInConstructorCorrect() {
        DummyClass dummy = new DummyClass(true);
        dummy.withdraw();
    }

    @Test(expected = AssertionError.class)
    public void testPreHookInConstructorIncorrect() {
        DummyClass dummy = new DummyClass(false);
        dummy.withdraw();
    }

    @Test
    public void testPreHookForConstructorCorrect() {
        DummyClassForConstructor dummy = new DummyClassForConstructor(true);
        dummy.withdraw();
    }

    @Test(expected = AssertionError.class)
    public void testPreHookForConstructorIncorrect() {
        DummyClassForConstructor dummy = new DummyClassForConstructor(false);
        dummy.withdraw();
    }

    @JOCEnabled
    public static class DummyClass {

        @PostConditionHook(CallSequence.class)
        public DummyClass(boolean isValid) {
        }

        @PostCondition(CallSequence.class)
        public void withdraw() {
        }
    }

    @JOCCondition
    public static class CallSequence {

        private boolean isValid = false;

        @AfterHook
        public void afterConstructor(DummyClass target, boolean isValid) {
            this.isValid = isValid;
        }

        @Condition
        public boolean afterWithdraw() {
            return isValid;
        }
    }

    @JOCEnabled
    public static class DummyClassForConstructor {

        @PostCondition(CallSequenceForConstructor.class)
        public DummyClassForConstructor(boolean callWithdraw) {
            if (callWithdraw) {
                withdraw();
            }
        }

        @PostConditionHook(CallSequenceForConstructor.class)
        public void withdraw() {
        }
    }

    @JOCCondition
    public static class CallSequenceForConstructor {

        private boolean isWithdrawCalled = false;

        @AfterHook
        public void afterWithdraw() {
            isWithdrawCalled = true;
        }

        @Condition
        public boolean afterConstructor() {
            return isWithdrawCalled;
        }
    }
}
