package de.grogra.blocks.functionParser;

public class D extends Expr {

    double x;

    public double eval() {
        return x;
    }

    public void setX(double x) {
    }

    public void setI(double x) {
    }

    public void setPreI(double x) {
    }

    public void setP(double x) {
    }

    public void setD(double x) {
        this.x = x;
    }

    public void setN1(double x) {
    }

    public void setN2(double x) {
    }

    public void setN3(double x) {
    }

    public void setH(double x) {
    }

    public void setHl(double x) {
    }

    public String toString() {
        return "d";
    }
}
