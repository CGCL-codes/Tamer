package staticContext;

public class StaticContext4p {

    static int i;

    static {
        i = 17;
    }

    StaticContext4p() {
        int k;
    }

    public static void main() {
        StaticContext4p p = new StaticContext4p();
        System.out.println(p.i);
    }
}
