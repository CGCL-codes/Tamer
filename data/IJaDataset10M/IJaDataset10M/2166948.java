package powermock.examples.suppress.constructorhierarchy;

/**
 * Example that demonstrates PowerMock's ability to suppress constructor hierarchies.
 */
public class EvilChild extends EvilGrandChild {

    public EvilChild() {
        System.loadLibrary("evil child.dll");
    }
}
