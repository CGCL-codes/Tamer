package com.googlecode.ehcache.annotations.key;

import java.util.Arrays;

public class MethodInvocationHelper {

    public void testMethod0() {
    }

    public Object testMethod1(Object arg1) {
        return arg1;
    }

    public Object testMethod2(int[] arg1, String arg2, boolean[] arg3, Object arg4) {
        return Arrays.asList(arg1, arg2, arg3, arg4);
    }

    @SuppressWarnings("unchecked")
    public Object testMethod3(int arg1, long arg2, boolean arg3, Integer arg4) {
        return Arrays.asList(arg1, arg2, arg3, arg4);
    }
}
