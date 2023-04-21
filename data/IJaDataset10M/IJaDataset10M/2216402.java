package commons;

import java.text.ParseException;
import org.makagiga.commons.DoubleProperty;
import org.makagiga.test.Test;
import org.makagiga.test.TestConstructor;
import org.makagiga.test.TestMethod;
import org.makagiga.test.Tester;

@Test(className = DoubleProperty.class, flags = Test.NO_DEFAULT_EQUALS_WARNING)
public final class TestDoubleProperty {

    public static final DoubleProperty myProperty = new DoubleProperty(3.14d);

    @Test(constructors = @TestConstructor, methods = @TestMethod(name = "getType"))
    public void test_commons() {
        TestProperty.test(Double.class, new DoubleProperty(), 0.0d, "0.0", 3.14d, "3.14");
    }

    @Test(constructors = { @TestConstructor(parameters = "double"), @TestConstructor(parameters = "String") }, methods = @TestMethod(name = "parse", parameters = "String"))
    public void test_constructor() throws ParseException {
        DoubleProperty d;
        d = new DoubleProperty(3.14d);
        assert d.get() == 3.14d;
        assert d.getDefaultValue() == 3.14d;
        Tester.testSerializable(d);
        Tester.testParseException(new Tester.Code() {

            public void run() throws Throwable {
                new DoubleProperty((String) null);
            }
        });
        Tester.testParseException(new Tester.Code() {

            public void run() throws Throwable {
                new DoubleProperty("");
            }
        });
        Tester.testParseException(new Tester.Code() {

            public void run() throws Throwable {
                new DoubleProperty("foo");
            }
        });
        d = new DoubleProperty("0");
        assert d.get() == 0.0d;
        assert d.getDefaultValue() == 0.0d;
        Tester.testSerializable(d);
        d = new DoubleProperty("3.14");
        assert d.get() == 3.14d;
        assert d.getDefaultValue() == 3.14d;
        Tester.testSerializable(d);
        d = new DoubleProperty("3.14d");
        assert d.get() == 3.14d;
        assert d.getDefaultValue() == 3.14d;
        Tester.testSerializable(d);
        d = new DoubleProperty("-3.14");
        assert d.get() == -3.14d;
        assert d.getDefaultValue() == -3.14d;
        Tester.testSerializable(d);
        d = new DoubleProperty("-3.14d");
        assert d.get() == -3.14d;
        assert d.getDefaultValue() == -3.14d;
        Tester.testSerializable(d);
    }
}
