package com.exm.chp06;

/**
 *
 * @author Supervisor
 */
public class CallBy {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Test obj = new Test(0, 0);
        int x = 6;
        int y = 12;
        System.out.println("Call-by-value");
        System.out.println("x: " + x + " y: " + y);
        obj.nochange(x, y);
        System.out.println("x: " + x + " y: " + y);
        System.out.println("Call-by-reference");
        Test obj1 = new Test(9, 10);
        System.out.println("obj1.a: " + obj1.a + " obj1.b: " + obj1.b);
        obj.change(obj1);
        System.out.println("obj1.a: " + obj1.a + " obj1.b: " + obj1.b);
        System.out.println("Call-by-reference [Integer]");
        Integer g = new Integer(100);
        System.out.println("g: " + g);
        obj.callbyRef(g);
        System.out.println("g: " + g);
        System.out.println("Call-by-reference [String]");
        String str = new String("jake");
        System.out.println("str: " + str);
        obj.callbyStr(str);
        System.out.println("str: " + str);
        System.out.println("Call-by-reference [StringBuffer]");
        StringBuffer str2 = new StringBuffer("jake");
        System.out.println("str2: " + str2);
        obj.callbyStrB(str2);
        System.out.println("str2: " + str2);
    }
}
