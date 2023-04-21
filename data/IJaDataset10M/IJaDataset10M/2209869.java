package biz.xl.thinkinjava.ch1;

public class DerivedClass extends BaseClass {

    public int[] a;

    public static int x;

    public DerivedClass() {
        System.out.println("The Derived Class initialize");
    }

    static {
        System.out.println("The derived class's static block!");
    }

    public void testC() {
        System.out.println("The derived class's testC method!");
    }

    {
        x = 90;
        System.out.println("The derived class non static block");
    }
}
