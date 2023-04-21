package initializer_out;

public class A_test1002 {

    {
        String s = "Eclipse";
        extracted(s);
    }

    protected void extracted(final String s) {
        System.out.println(s);
    }
}
