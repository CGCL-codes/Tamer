package commons;

import org.makagiga.commons.StringList;
import org.makagiga.test.Test;
import org.makagiga.test.Tester;

@Test(flags = Test.NO_DEFAULT_EQUALS_WARNING)
public final class TestStringList {

    @Test
    public void test_example() {
        StringList list = new StringList("foo", "bar");
        list.sort();
        String s = list.toString(" ");
        assert s.equals("bar foo");
    }

    @Test
    public void test_constructor() {
        StringList arrayList;
        arrayList = new StringList();
        Tester.testSerializable(arrayList);
        assert arrayList.isEmpty();
        Tester.testException(IllegalArgumentException.class, new Tester.Code() {

            public void run() throws Throwable {
                new StringList(-1);
            }
        });
        arrayList = new StringList(0);
        Tester.testSerializable(arrayList);
        assert arrayList.isEmpty();
        arrayList = new StringList(1);
        Tester.testSerializable(arrayList);
        assert arrayList.isEmpty();
        Tester.testException(NullPointerException.class, new Tester.Code() {

            public void run() throws Throwable {
                new StringList((String[]) null);
            }
        });
        arrayList = new StringList((String) null);
        Tester.testSerializable(arrayList);
        assert arrayList.size() == 1;
        assert arrayList.get(0) == null;
        arrayList = new StringList("foo", null, "bar");
        Tester.testSerializable(arrayList);
        assert arrayList.size() == 3;
        assert arrayList.get(0).equals("foo");
        assert arrayList.get(1) == null;
        assert arrayList.get(2).equals("bar");
    }

    @Test
    public void test_toArray() {
        String[] array;
        StringList arrayList;
        arrayList = new StringList();
        assert arrayList.toArray().length == 0;
        arrayList = new StringList((String) null);
        array = arrayList.toArray();
        assert array.length == 1;
        assert array[0] == null;
        arrayList = new StringList("foo", "bar");
        array = arrayList.toArray();
        assert array.length == 2;
        assert array[0].equals("foo");
        assert array[1].equals("bar");
    }
}
