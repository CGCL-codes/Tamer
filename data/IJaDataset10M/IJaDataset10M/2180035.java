package samples;

class Base<T> {

    public T a;

    public T b;
}

public class ClassImplementsRational extends Base<Integer> implements Rational {

    public ClassImplementsRational(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public String toString() {
        return a + "/" + b;
    }

    public boolean same(Rational rhs) {
        int diff = a * rhs.getB() - b * rhs.getA();
        return diff == 0;
    }

    public static void main(String[] args) {
        Rational a = new ClassImplementsRational(3, 4);
        Rational b = new ClassImplementsRational(2, 9);
        System.out.println(a.same(b));
    }
}
