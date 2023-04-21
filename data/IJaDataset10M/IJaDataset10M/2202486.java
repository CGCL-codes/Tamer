package test;

public class TestSchemeWithNestedIf3 {

    private int a = 0;

    public void test() {
        a = a + 1;
        if (a > 1) {
            a = a - 1;
        } else {
            if (a > 1) {
                a = a - 1;
            } else {
                a = 1 - a;
            }
        }
        a = a + 2;
        return;
    }
}
