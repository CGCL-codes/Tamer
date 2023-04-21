package com.farukcankaya.simplemodel;

public class LocationPrint {

    private LocationPrint() {
    }

    public static void print() {
        try {
            throw new Exception();
        } catch (Exception ex) {
            print0(ex.getStackTrace()[1], "");
        }
    }

    public static void print(String message) {
        try {
            throw new Exception();
        } catch (Exception ex) {
            print0(ex.getStackTrace()[1], message);
        }
    }

    private static void print0(StackTraceElement e, String message) {
        System.out.println(new StringBuilder().append(e.getClassName()).append(".").append(e.getMethodName()).append("(").append(e.getFileName()).append(":").append(e.getLineNumber()).append(")").append(" ").append(message).toString());
    }
}
