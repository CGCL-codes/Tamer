package br.net.woodstock.rockframework.util;

import java.io.File;
import java.util.Collection;
import java.util.Map;

public abstract class Assert {

    private static final ValueChecker VALUE_CHECKER;

    static {
        VALUE_CHECKER = new ValueChecker(new ValueExceptionBuilder() {

            @Override
            public RuntimeException newException(final String message) {
                return new AssertionFailedException(message);
            }
        });
    }

    private Assert() {
        super();
    }

    public static void between(final double value, final double minimum, final double maximum, final String name) {
        Assert.VALUE_CHECKER.between(value, minimum, maximum, name);
    }

    public static void between(final long value, final long minimum, final long maximum, final String name) {
        Assert.VALUE_CHECKER.between(value, minimum, maximum, name);
    }

    public static void equals(final Object value, final Object other, final String name) {
        Assert.VALUE_CHECKER.equals(value, other, name);
    }

    public static void greaterOrEqual(final double value, final double minimum, final String name) {
        Assert.VALUE_CHECKER.greaterOrEqual(value, minimum, name);
    }

    public static void greaterOrEqual(final long value, final long minimum, final String name) {
        Assert.VALUE_CHECKER.greaterOrEqual(value, minimum, name);
    }

    public static void greaterThan(final double value, final double minimum, final String name) {
        Assert.VALUE_CHECKER.greaterThan(value, minimum, name);
    }

    public static void greaterThan(final long value, final long minimum, final String name) {
        Assert.VALUE_CHECKER.greaterThan(value, minimum, name);
    }

    @SuppressWarnings({ "rawtypes" })
    public static void instanceOf(final Object value, final Class type, final String name) {
        Assert.VALUE_CHECKER.instanceOf(value, type, name);
    }

    @SuppressWarnings({ "rawtypes" })
    public static void instanceOf(final Object value, final Class[] types, final String name) {
        Assert.VALUE_CHECKER.instanceOf(value, types, name);
    }

    public static void isArray(final Object obj, final String name) {
        Assert.VALUE_CHECKER.isArray(obj, name);
    }

    public static void isDirectory(final File file, final String name) {
        Assert.VALUE_CHECKER.isDirectory(file, name);
    }

    public static void isFile(final File file, final String name) {
        Assert.VALUE_CHECKER.isFile(file, name);
    }

    public static void lessOrEqual(final double value, final double maximum, final String name) {
        Assert.VALUE_CHECKER.lessOrEqual(value, maximum, name);
    }

    public static void lessOrEqual(final long value, final long maximum, final String name) {
        Assert.VALUE_CHECKER.lessOrEqual(value, maximum, name);
    }

    public static void lessThan(final double value, final double maximum, final String name) {
        Assert.VALUE_CHECKER.lessThan(value, maximum, name);
    }

    public static void lessThan(final long value, final long maximum, final String name) {
        Assert.VALUE_CHECKER.lessThan(value, maximum, name);
    }

    public static void notNull(final Object obj, final String name) {
        Assert.VALUE_CHECKER.notNull(obj, name);
    }

    @SuppressWarnings("rawtypes")
    public static void notEmpty(final Collection collection, final String name) {
        Assert.VALUE_CHECKER.notEmpty(collection, name);
    }

    @SuppressWarnings("rawtypes")
    public static void notEmpty(final Map map, final String name) {
        Assert.VALUE_CHECKER.notEmpty(map, name);
    }

    public static void notEmpty(final Object[] array, final String name) {
        Assert.VALUE_CHECKER.notEmpty(array, name);
    }

    public static void notEmpty(final Object array, final String name) {
        Assert.VALUE_CHECKER.notEmpty(array, name);
    }

    public static void notEmpty(final String str, final String name) {
        Assert.VALUE_CHECKER.notEmpty(str, name);
    }

    public static void validRegex(final String value, final String pattern, final String name) {
        Assert.VALUE_CHECKER.validRegex(value, pattern, name);
    }
}
