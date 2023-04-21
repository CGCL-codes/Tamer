package org.spockframework.mock;

public abstract class MockInteractionDecorator implements IMockInteraction {

    protected final IMockInteraction decorated;

    public MockInteractionDecorator(IMockInteraction decorated) {
        this.decorated = decorated;
    }

    public int getLine() {
        return decorated.getLine();
    }

    public int getColumn() {
        return decorated.getColumn();
    }

    public String getText() {
        return decorated.getText();
    }

    public boolean matches(IMockInvocation invocation) {
        return decorated.matches(invocation);
    }

    public Object accept(IMockInvocation invocation) {
        return decorated.accept(invocation);
    }

    public boolean isSatisfied() {
        return decorated.isSatisfied();
    }

    public boolean isExhausted() {
        return decorated.isExhausted();
    }

    @Override
    public String toString() {
        return decorated.toString();
    }
}
