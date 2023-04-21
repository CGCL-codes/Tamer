package com.dm;

public class AbstractImpl extends Containner {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        AbstractImpl.getName();
        String s = new String("hello");
        AbstractImpl.addSomething(s);
        System.out.println(s);
    }

    public static String getName() {
        try {
            System.out.println(2 << 3);
            System.out.println("before return");
            return "return";
        } catch (Exception e) {
        } finally {
            System.out.println("finally");
        }
        return null;
    }

    public static void addSomething(String s) {
        s = s + "something";
        System.out.println(s);
    }
}
