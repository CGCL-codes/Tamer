package joptsimple;

/**
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 */
public class OptionalArgumentOptionSpecValuesImmutabilityTest extends AbstractOptionSpecValuesImmutabilityTestCase<Integer> {

    @Override
    protected AbstractOptionSpec<Integer> newOptionSpec() {
        return new OptionalArgumentOptionSpec<Integer>("life").ofType(Integer.class);
    }

    @Override
    protected String firstArg() {
        return "1";
    }

    @Override
    protected String secondArg() {
        return "2";
    }

    @Override
    protected Integer newItem() {
        return 3;
    }

    @Override
    protected Integer containedItem() {
        return 2;
    }
}
