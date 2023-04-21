package org.springframework.webflow.engine.support;

import org.springframework.webflow.action.EventFactorySupport;
import org.springframework.webflow.engine.TransitionCriteria;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.AnnotatedAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.MockRequestContext;
import junit.framework.TestCase;

/**
 * Unit tests for {@link TransitionCriteriaChain}.
 * 
 * @author Erwin Vervaet
 */
public class TransitionCriteriaChainTests extends TestCase {

    private TransitionCriteriaChain chain;

    private MockRequestContext context;

    protected void setUp() throws Exception {
        chain = new TransitionCriteriaChain();
        context = new MockRequestContext();
    }

    public void testEmptyChain() {
        assertTrue(chain.test(context));
    }

    public void testAllTrue() {
        TestTransitionCriteria criteria1 = new TestTransitionCriteria(true);
        TestTransitionCriteria criteria2 = new TestTransitionCriteria(true);
        TestTransitionCriteria criteria3 = new TestTransitionCriteria(true);
        chain.add(criteria1);
        chain.add(criteria2);
        chain.add(criteria3);
        assertTrue(chain.test(context));
        assertTrue(criteria1.tested);
        assertTrue(criteria2.tested);
        assertTrue(criteria3.tested);
    }

    public void testWithFalse() {
        TestTransitionCriteria criteria1 = new TestTransitionCriteria(true);
        TestTransitionCriteria criteria2 = new TestTransitionCriteria(false);
        TestTransitionCriteria criteria3 = new TestTransitionCriteria(true);
        chain.add(criteria1);
        chain.add(criteria2);
        chain.add(criteria3);
        assertFalse(chain.test(context));
        assertTrue(criteria1.tested);
        assertTrue(criteria2.tested);
        assertFalse(criteria3.tested);
    }

    public void testCriteriaChainForNoActions() {
        TransitionCriteria actionChain = TransitionCriteriaChain.criteriaChainFor(null);
        assertTrue(actionChain.test(context));
    }

    public void testCriteriaChainForActions() {
        AnnotatedAction[] actions = new AnnotatedAction[] { new AnnotatedAction(new TestAction(true)), new AnnotatedAction(new TestAction(false)) };
        TransitionCriteria actionChain = TransitionCriteriaChain.criteriaChainFor(actions);
        assertFalse(actionChain.test(context));
    }

    private static class TestTransitionCriteria implements TransitionCriteria {

        public boolean tested = false;

        private boolean result;

        public TestTransitionCriteria(boolean result) {
            this.result = result;
        }

        public boolean test(RequestContext context) {
            tested = true;
            return result;
        }
    }

    private static class TestAction implements Action {

        private boolean result;

        public TestAction(boolean result) {
            this.result = result;
        }

        public Event execute(RequestContext context) throws Exception {
            if (result) {
                return new EventFactorySupport().success(this);
            } else {
                return new EventFactorySupport().error(this);
            }
        }
    }
}
