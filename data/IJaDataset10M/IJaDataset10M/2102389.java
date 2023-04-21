package objectInitialization;

class Parent5 {

    Parent5() {
        System.out.println(100);
    }

    Parent5(int i) {
        System.out.println(99);
    }

    Parent5(char i) {
        System.out.println(98);
    }
}

public class Initialization5 extends Parent5 {

    Initialization5() {
        super('a');
    }

    public static void main() {
        Initialization5 p = new Initialization5();
    }
}
