package com.jazz.reflect;

public class ReflectTest {

    public ReflectTest() {
        System.out.println("hello world second ");
    }

    public static void main(String[] args) throws Exception {
        Object rf = new ReflectTest();
        Class clz1 = "str".getClass();
        Class clz2 = String.class;
        Class clz3 = Class.forName("java.lang.String");
        System.out.println(void.class.isPrimitive());
        System.out.println(boolean.class);
        System.out.println(void.class);
        byte[] brr = new byte[0];
        System.out.println(brr.getClass().getName());
        long[] lrr = new long[0];
        System.out.println(lrr.getClass().getName());
        String[] srr = new String[0];
        System.out.println(srr.getClass().getName());
        System.out.println(int.class == Integer.class);
        System.out.println(int.class == Integer.TYPE);
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("�� �� Ҫ �� ��");
    }
}
