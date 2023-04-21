package net.sf.cotta;

public class AssertionFactory {

    public TPathAssertion tPath(TPath path) {
        return new TPathAssertion(path);
    }

    public ExpectionAssertion exception(Exception exception) {
        return new ExpectionAssertion(exception);
    }
}
