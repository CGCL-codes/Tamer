package com.modelmetrics.common.sforce.dao;

public class SproxySampleFactory {

    private static int count = 0;

    public static final String TYPE = "Account";

    public static Sproxy build() {
        count++;
        SproxyStandardImpl ret = new SproxyStandardImpl();
        ret.setType(TYPE);
        ret.setId("0000000" + count);
        ret.setValue("one", "one" + count);
        ret.setValue("two", "two" + count);
        ret.setValue("three", "three" + count);
        ret.setDirty(false);
        return ret;
    }

    public static Sproxy build(int values, int nulls) {
        count++;
        SproxyStandardImpl ret = new SproxyStandardImpl();
        ret.setType(TYPE);
        ret.setId("0000000" + count);
        for (int i = 0; i < values; i++) {
            ret.setValue("key" + i + count, "value" + i + count);
        }
        for (int i = 0; i < nulls; i++) {
            ret.setNull("null" + i + count);
        }
        ret.setDirty(false);
        return ret;
    }

    public static Sproxy buildDirty(int values, int nulls) {
        SproxyStandardImpl ret = (SproxyStandardImpl) build(values, nulls);
        ret.setDirty(true);
        return ret;
    }
}
