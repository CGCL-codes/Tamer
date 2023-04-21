package locals_out;

public class A_test531 {

    class Inner {

        public int x;
    }

    public void foo() {
        Inner inner = new Inner();
        extracted(inner);
    }

    protected void extracted(final Inner inner) {
        inner.x = 10;
    }
}
