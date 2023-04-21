package org.fudaa.fudaa.sinavi3;

public class Sinavi3Lib {

    public static String actionArgumentStringOnly(final String _a) {
        final int i = _a.indexOf('(');
        String arg = null;
        if (i >= 0) {
            arg = _a.substring(i + 1, _a.length() - 1);
        }
        return arg;
    }

    public static String actionStringWithoutArgument(final String _a) {
        final int i = _a.indexOf('(');
        return (i >= 0) ? _a.substring(0, i) : _a;
    }
}
