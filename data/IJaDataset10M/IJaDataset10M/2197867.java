package foo;

public class SimpleNames {

    int f;

    static int s;

    public void hello() {
        f = 3;
        s = 4;
        SimpleNames.s = foo.SimpleNames.s;
        if (false) {
            foo.SimpleNames.this.hello();
            hello();
            this.hello();
        }
        final int i = 5;
        new Object() {

            public int hashCode() {
                f = 5;
                s = 6;
                SimpleNames.this.f = 5;
                SimpleNames.s = 6;
                if (false) {
                    foo.SimpleNames.this.hello();
                    SimpleNames.this.hello();
                    hello();
                    this.hashCode();
                    hashCode();
                }
                return i;
            }
        };
    }

    public static void main(String args[]) {
        new SimpleNames().hello();
    }
}
