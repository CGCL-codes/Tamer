package hu.schmidtsoft.map.model;

import hu.schmidtsoft.transform.P2;

/**
 * Four 2d coordinates
 * @author rizsi
 *
 */
public class Rectangle {

    public P2 a, b, c, d;

    public Rectangle(P2 a, P2 b, P2 c, P2 d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public String toString() {
        return "[" + a + ", " + b + ", " + c + ", " + d + "]";
    }
}
