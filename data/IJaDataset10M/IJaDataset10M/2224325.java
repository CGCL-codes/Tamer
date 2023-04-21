package samples.staticandinstance;

/**
 * The purpose of this class is to be used to verify that the
 * http://code.google.com/p/powermock/issues/detail?id=4 is fixed.
 * 
 */
public class StaticAndInstanceWithConstructorCodeDemo {

    private final StaticAndInstanceDemo staticAndInstanceDemo;

    public StaticAndInstanceWithConstructorCodeDemo(StaticAndInstanceDemo staticAndInstanceDemo) {
        this.staticAndInstanceDemo = staticAndInstanceDemo;
    }

    public String getMessage() {
        return StaticAndInstanceDemo.getStaticMessage() + staticAndInstanceDemo.getMessage();
    }
}
