package joc.systemtest.prehook;

import joc.BeforeHook;
import joc.Condition;
import joc.JOCCondition;
import joc.JOCEnabled;
import joc.PreCondition;
import joc.PreConditionHook;
import org.junit.Before;
import org.junit.Test;

public class CustomPreHookInConstructorSystemTest {

    private DummyClass dummy;

    @Before
    public void before() {
        dummy = new DummyClass(100);
    }

    @Test
    public void testPreHookCorrect() {
        dummy.withdraw(100);
    }

    @JOCEnabled
    public static class DummyClass {

        @PreConditionHook(CallSequence.class)
        public DummyClass(int maxWithdraw) {
        }

        @PreCondition(CallSequence.class)
        public void withdraw(int amount) {
        }
    }

    @JOCCondition
    public static class CallSequence {

        private int maxWithdraw;

        @BeforeHook
        public void beforeConstructor(DummyClass target, int maxWithdraw) {
            this.maxWithdraw = maxWithdraw;
        }

        @Condition
        public boolean beforeWithdraw(DummyClass target, int amount) {
            return amount <= maxWithdraw;
        }
    }
}
