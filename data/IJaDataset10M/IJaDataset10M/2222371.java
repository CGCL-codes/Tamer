package samples.privatemocking;

/**
 * A class used to test the functionality to mock private methods.
 * 
 * @author Johan Haleby
 */
public class PrivateMethodDemo {

    public String say(String name) {
        return sayIt(name);
    }

    private String sayIt(String name) {
        return "Hello " + name;
    }

    @SuppressWarnings("unused")
    private String sayIt() {
        return "Hello world";
    }

    public int methodCallingPrimitiveTestMethod() {
        return aTestMethod(10);
    }

    public int methodCallingWrappedTestMethod() {
        return aTestMethod(new Integer(15));
    }

    private int aTestMethod(int aValue) {
        return aValue;
    }

    private Integer aTestMethod(Integer aValue) {
        return aValue;
    }
}
