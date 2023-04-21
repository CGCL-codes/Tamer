package cx.ath.mancel01.dependencyshot.test.circular;

import javax.inject.Inject;

/**
 *
 * @author mathieuancelin
 */
public class ConstructorA implements InterfaceA {

    private InterfaceB b;

    @Inject
    public ConstructorA(InterfaceB b) {
        this.b = b;
    }

    @Override
    public String getValue() {
        return "ConstructorA";
    }

    @Override
    public InterfaceB getB() {
        return b;
    }
}
