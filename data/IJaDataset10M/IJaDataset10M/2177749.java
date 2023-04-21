package expressions;

public class PointerComparison01 {

    public static void main() {
        PointerComparison01 p = null;
        PointerComparison01LeftChild q = null;
        PointerComparison01RightChild r = null;
        boolean ok = true;
        if (p != q) ok = false;
        if (q != p) ok = false;
        if (p != r) ok = false;
        if (r != p) ok = false;
        p = new PointerComparison01RightChild();
        if (p == q) ok = false;
        if (q == p) ok = false;
        if (p == r) ok = false;
        if (r == p) ok = false;
        r = new PointerComparison01RightChild();
        q = new PointerComparison01LeftChild();
        if (p == q) ok = false;
        if (q == p) ok = false;
        if (p == r) ok = false;
        if (r == p) ok = false;
        p = q;
        if (p != q) ok = false;
        if (q != p) ok = false;
        if (p == r) ok = false;
        if (r == p) ok = false;
        p = r;
        if (p == q) ok = false;
        if (q == p) ok = false;
        if (p != r) ok = false;
        if (r != p) ok = false;
        if (ok) System.out.println(42);
    }
}

class PointerComparison01LeftChild extends PointerComparison01 {
}

class PointerComparison01RightChild extends PointerComparison01 {
}
