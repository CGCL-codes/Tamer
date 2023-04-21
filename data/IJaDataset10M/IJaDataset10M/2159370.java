package simple_in;

public class TestPrimitiveParam {

    public void foo() {
        int a = 1;
        int b = bar(a);
        System.out.println(a);
    }

    public int bar(int a) {
        return a++;
    }
}
