package chapter8;

public class MyOuterClass {

    private int x = 8;

    public static void main(String[] args) {
        MyOuterClass.MyInnerClass in = new MyOuterClass().new MyInnerClass();
        in.seeOuter();
    }

    public void makeInner() {
        MyInnerClass in = new MyInnerClass();
        in.seeOuter();
    }

    public class MyInnerClass {

        public void seeOuter() {
            System.out.println("Outer x is " + x);
            System.out.println("Outer class ref is " + MyOuterClass.this);
            System.out.println("Inner class ref is " + this);
        }
    }
}
