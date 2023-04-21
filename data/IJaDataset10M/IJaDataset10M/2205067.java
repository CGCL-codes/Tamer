package commons;

import java.text.ParseException;
import org.makagiga.commons.IntegerProperty;
import org.makagiga.test.Test;
import org.makagiga.test.Tester;

@Test(className = IntegerProperty.class, flags = Test.NO_DEFAULT_EQUALS_WARNING)
public final class TestIntegerProperty {

    public static final IntegerProperty myProperty = new IntegerProperty(10);

    @Test
    public void test_example() {
        if (myProperty.get() == 10) {
        }
    }

    @Test
    public void test_constructor_parse() throws ParseException {
        IntegerProperty i;
        i = new IntegerProperty();
        Tester.testSerializable(i);
        assert i.get() == 0;
        assert i.getDefaultValue() == 0;
        i = new IntegerProperty(666);
        Tester.testSerializable(i);
        assert i.get() == 666;
        assert i.getDefaultValue() == 666;
        Tester.testException(ParseException.class, new Tester.Code() {

            public void run() throws Throwable {
                new IntegerProperty(null);
            }
        });
        Tester.testException(ParseException.class, new Tester.Code() {

            public void run() throws Throwable {
                new IntegerProperty("");
            }
        });
        Tester.testException(ParseException.class, new Tester.Code() {

            public void run() throws Throwable {
                new IntegerProperty("foo");
            }
        });
        Tester.testException(ParseException.class, new Tester.Code() {

            public void run() throws Throwable {
                new IntegerProperty("ff");
            }
        });
        i = new IntegerProperty("-666");
        Tester.testSerializable(i);
        assert i.get() == -666;
        assert i.getDefaultValue() == -666;
        i.parse("666");
        assert i.get() == 666;
        assert i.getDefaultValue() == -666;
        i = new IntegerProperty("0");
        Tester.testSerializable(i);
        assert i.get() == 0;
        assert i.getDefaultValue() == 0;
        i = new IntegerProperty("666");
        Tester.testSerializable(i);
        assert i.get() == 666;
        assert i.getDefaultValue() == 666;
        i.parse("-666");
        assert i.get() == -666;
        assert i.getDefaultValue() == 666;
    }
}
