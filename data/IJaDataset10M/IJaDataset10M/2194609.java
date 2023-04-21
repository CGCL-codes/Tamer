package edu.java.lectures.lec04.inheritance;

/**
 * Rawnostranen triygylnik
 * 
 * @author admin
 */
public class EquilateralTriangle {

    double a;

    public EquilateralTriangle() {
    }

    public EquilateralTriangle(double a) {
        this.a = a;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double calculatePerimeter() {
        return 3 * a;
    }

    public double calculateSurface() {
        return ((Math.sqrt(3) / 2) * Math.pow(a, 2)) / 2;
    }

    @Override
    public String toString() {
        return "(" + this.a + ")";
    }

    public String getMedicenterName() {
        return "E";
    }
}
