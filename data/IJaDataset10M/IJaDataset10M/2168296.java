package webserg.pazzlers.ch6;

public class StaticTest {

    static {
        p();
    }

    public static int k = get();

    static {
        k = 1;
        System.out.println("static " + k);
    }

    static int get() {
        System.out.println("init k");
        return 2;
    }

    static void p() {
        k = 5;
        System.out.println("static block");
    }
}
