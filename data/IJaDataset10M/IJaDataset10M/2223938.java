package plumber;

/**
 * test Steps used for non-annotated tests
 * @author mgarber
 *
 */
public class Steps {

    public boolean isEven(TestContext context) {
        return (context.getValue() % 2 == 0);
    }

    public void timesTwo(TestContext context) {
        if (context.getRetryCount() == 1) throw new RuntimeException("testing retry");
        context.setValue(context.getValue() * 2);
    }

    public void timesThree(TestContext context) {
        context.setValue(context.getValue() * 3);
    }
}
