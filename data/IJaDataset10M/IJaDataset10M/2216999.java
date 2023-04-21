package staticContext;

public class StaticContext5e1 {

    int method() {
        return 17;
    }

    static int i = method();

    StaticContext5e1() {
        int k;
    }

    public static void main() {
        StaticContext5e1 p = new StaticContext5e1();
    }
}
