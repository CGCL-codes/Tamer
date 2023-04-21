package joc.systemtest.post;

import joc.Condition;
import joc.JOCCondition;
import joc.JOCEnabled;
import joc.PostCondition;
import joc.contract.Pure;
import org.junit.Before;
import org.junit.Test;

public class CustomPostConditionIsPureWithGenericsSystemTest {

    private DummyClass dummy;

    @Before
    public void before() {
        dummy = new DummyClass(0);
        dummy.balance = 100;
    }

    @Test(expected = AssertionError.class)
    public void testIllegalAccessInPostCondition() throws Exception {
        dummy.withdrawCorrect(50);
    }

    @Test
    public void testCorrectFromPureMethod() throws Exception {
        dummy.pureMethod();
    }

    @JOCEnabled
    public static class DummyClass extends AbstractWithdrawable<Integer, String> {

        protected int balance;

        public DummyClass(int amount) {
            balance = amount;
        }

        @Override
        public Integer getBalance(String stuff) {
            balance = 0;
            return balance;
        }

        @Pure
        public void pureMethod() {
        }

        @Override
        public void withdrawCorrect(int amount) {
            balance -= amount;
        }
    }

    @JOCEnabled
    public abstract static class AbstractWithdrawable<T, U> {

        @PostCondition(PostMethod.class)
        public abstract void withdrawCorrect(int amount);

        @Pure
        public abstract T getBalance(U stuff);
    }

    @JOCCondition
    protected static class PostMethod {

        @Condition
        protected boolean after(AbstractWithdrawable<Integer, String> target, int amount) {
            target.getBalance("");
            return true;
        }
    }
}
