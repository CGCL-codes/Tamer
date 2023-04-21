package bdddoc4j.examples.stack.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import bdddoc4j.examples.Ref;
import bdddoc4j.examples.RefClass;
import bdddoc4j.examples.ScenarioSupport;
import bdddoc4j.examples.Story;

/**
 * 
 * @Author Micael Vesterlund, micael.vesterlund@gmail.com
 */
@Ref(Story.STACK_DESCRIBED_WITH_METHODS)
@RefClass(Stack.class)
public class TestStackBehavior2 extends ScenarioSupport<TestStackBehavior2> {

    private Stack<String> stack;

    private static final String foo = "foo";

    private static final String bar = "bar";

    private String poped;

    private String peeked;

    private int sizeBefore;

    private int sizeAfter;

    @Test
    public void aNewlyCreatedStackShouldBeEmpty() {
        given.aNewlyCreatedStack();
        then.stackIsEmpty();
    }

    @Test
    public void shouldNotBeEmptyAfterPush() {
        given.aNewlyCreatedStack();
        when.pushedIsCalledWith(foo);
        then.stackIsNotEmpty();
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionUponNullPush() {
        given.aNewlyCreatedStack();
        when.pushedIsCalledWith(null);
        then.anExceptionAreThrown();
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionUponPopWithoutPush() {
        given.aNewlyCreatedStack();
        when.popIsCalled();
        then.anExceptionAreThrown();
    }

    @Test
    public void shouldPopPushedValue() {
        given.aNewlyCreatedStack();
        and.pushedIsCalledWith(foo);
        when.popIsCalled();
        then.popedValueIs(foo);
        and.stackIsEmpty();
    }

    @Test
    public void shouldPopLastPushedValueFirst() {
        given.aNewlyCreatedStack();
        and.pushedIsCalledWith(foo);
        and.pushedIsCalledWith(bar);
        when.popIsCalled();
        then.popedValueIs(bar);
        and.sizeOfStackHasDecreasedWith(1);
        and.stackIsNotEmpty();
    }

    @Test
    public void shouldLeaveValueOnStackAfterPeek() {
        given.aNewlyCreatedStack();
        and.pushedIsCalledWith(foo);
        and.pushedIsCalledWith(bar);
        when.peekIsCalled();
        then.peekedValueIs(bar);
        and.sizeOfStackHasNotChanged();
    }

    @Before
    public void setUp() {
        poped = null;
        peeked = null;
        sizeBefore = 0;
        sizeAfter = 0;
    }

    protected void aNewlyCreatedStack() {
        stack = new Stack<String>();
    }

    protected void givenOnePushedItem(String item) {
        sizeBefore = stack.size();
        stack.push(item);
        sizeAfter = stack.size();
    }

    protected void pushedIsCalledWith(String item) {
        givenOnePushedItem(item);
    }

    protected void popIsCalled() {
        sizeBefore = stack.size();
        poped = stack.pop();
        sizeAfter = stack.size();
    }

    protected void peekIsCalled() {
        sizeBefore = stack.size();
        peeked = stack.peek();
        sizeAfter = stack.size();
    }

    protected void anExceptionAreThrown() {
        fail("method hasn't caused an exception when it should");
    }

    protected void popedValueIs(String lastPushed) {
        assertThat(lastPushed, equalTo(poped));
    }

    protected void peekedValueIs(String lastPushed) {
        assertThat(lastPushed, equalTo(peeked));
    }

    protected void sizeOfStackHasNotChanged() {
        assertThat(sizeAfter, equalTo(sizeBefore));
    }

    protected void sizeOfStackHasDecreasedWith(int size) {
        assertThat(sizeAfter, equalTo(sizeBefore - size));
    }

    protected void stackIsEmpty() {
        if (!stack.isEmpty()) {
            throw new AssertionError();
        }
    }

    protected void stackIsNotEmpty() {
        if (stack.isEmpty()) {
            throw new AssertionError();
        }
    }
}
