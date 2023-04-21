package com.dyuproject.protostuff.me;

import junit.framework.Assert;
import com.dyuproject.protostuff.me.Foo.EnumSample;

/**
 * The objects to be tested.
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public final class SerializableObjects {

    public static final Baz negativeBaz = newBaz(-567, "negativeBaz", -202020202);

    public static final Bar negativeBar = newBar(-12, "negativeBar", negativeBaz, Bar.Status.STARTED, ByteString.copyFromUtf8("a1"), Boolean.TRUE, -130.031f, -1000.0001d, -101010101);

    public static final Baz baz = newBaz(567, "baz", 202020202);

    public static final Bar bar = newBar(890, "bar", baz, Bar.Status.STARTED, ByteString.copyFromUtf8("b2"), Boolean.TRUE, 150.051f, 2000.0002d, 303030303);

    public static final Foo foo = newFoo(new Integer[] { 90210, -90210, 0 }, new String[] { "ab", "cd" }, new Bar[] { bar, negativeBar }, new int[] { EnumSample.TYPE0, EnumSample.TYPE2 }, new ByteString[] { ByteString.copyFromUtf8("ef"), ByteString.copyFromUtf8("gh") }, new Boolean[] { Boolean.TRUE, Boolean.FALSE }, new Float[] { 1234.4321f, -1234.4321f, 0f }, new Double[] { 12345678.87654321d, -12345678.87654321d, 0d }, new Long[] { 7060504030201l, -7060504030201l, 0l });

    public static Baz newBaz(int id, String name, long timestamp) {
        Baz baz = new Baz();
        baz.setId(id);
        baz.setName(name);
        baz.setTimestamp(timestamp);
        return baz;
    }

    public static Bar newBar(int someInt, String someString, Baz someBaz, int someEnum, ByteString someBytes, Boolean someBoolean, float someFloat, double someDouble, long someLong) {
        Bar bar = new Bar();
        bar.setSomeInt(someInt);
        bar.setSomeString(someString);
        bar.setSomeBaz(someBaz);
        bar.setSomeEnum(someEnum);
        bar.setSomeBytes(someBytes);
        bar.setSomeBoolean(someBoolean);
        bar.setSomeFloat(someFloat);
        bar.setSomeDouble(someDouble);
        bar.setSomeLong(someLong);
        return bar;
    }

    public static Foo newFoo(Integer[] someInt, String[] someString, Bar[] someBar, int[] someEnum, ByteString[] someBytes, Boolean[] someBoolean, Float[] someFloat, Double[] someDouble, Long[] someLong) {
        Foo foo = new Foo();
        for (int i = 0; i < someInt.length; i++) foo.addSomeInt(someInt[i]);
        for (int i = 0; i < someString.length; i++) foo.addSomeString(someString[i]);
        for (int i = 0; i < someBar.length; i++) foo.addSomeBar(someBar[i]);
        for (int i = 0; i < someEnum.length; i++) foo.addSomeEnum(new Integer(someEnum[i]));
        for (int i = 0; i < someBytes.length; i++) foo.addSomeBytes(someBytes[i]);
        for (int i = 0; i < someBoolean.length; i++) foo.addSomeBoolean(someBoolean[i]);
        for (int i = 0; i < someFloat.length; i++) foo.addSomeFloat(someFloat[i]);
        for (int i = 0; i < someDouble.length; i++) foo.addSomeDouble(someDouble[i]);
        for (int i = 0; i < someLong.length; i++) foo.addSomeLong(someLong[i]);
        return foo;
    }

    public static <T> void assertEquals(T m1, T m2) {
        Assert.assertEquals(m1, m2);
    }
}
