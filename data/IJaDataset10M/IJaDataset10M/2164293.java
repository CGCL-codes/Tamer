package exceptions;

class Exception7 {

    Exception7() {
    }

    int i;
}

public class ThrowCatch7 {

    public static void main() {
        int i = 0;
        try {
            f();
        } catch (Exception7 e) {
            i = 1;
        }
        i = 2;
    }

    static void f() throws Exception7 {
        boolean x = true;
        if (x) throw new Exception7();
    }
}
