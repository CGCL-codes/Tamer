package edu.java.homework.hw08.ipj09.exer01done;

import java.util.Scanner;

public class RaznostranenTriangle extends RavnobedrenTriangle {

    double side3 = 0.0d;

    public RaznostranenTriangle() {
        this(0, 0, 0);
    }

    public RaznostranenTriangle(double side1, double side2, double side3) {
        super(side1, side2);
        this.side3 = side3;
    }

    public RaznostranenTriangle(RaznostranenTriangle raznostranenTr) {
        this(raznostranenTr.getSide1(), raznostranenTr.getSide2(), raznostranenTr.getSide3());
    }

    public double getSide3() {
        return side3;
    }

    public void setSide3(double side3) {
        this.side3 = side3;
    }

    protected static boolean isTriangle(double a, double b, double c) {
        return (a + b > c) && (b + c > a) && (a + c > b);
    }

    @Override
    protected void correct() {
        Scanner input = new Scanner(System.in);
        while (isTriangle(getSide1(), getSide2(), getSide3()) == false) {
            System.out.println("Please input correct sides of the triangle:");
            System.out.print("side1= ");
            setSide1(input.nextDouble());
            System.out.print("side2= ");
            setSide2(input.nextDouble());
            System.out.print("side3= ");
            setSide3(input.nextDouble());
        }
    }

    @Override
    public double getPerimeter() {
        return getSide1() + getSide2() + getSide3();
    }

    @Override
    public double getSurface() {
        double semiP = getPerimeter() / 2;
        double tmp = semiP * (semiP - getSide1()) * (semiP - getSide2()) * (semiP - getSide3());
        return Math.sqrt(tmp);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString()).append("\n");
        sb.append("side3: ").append(getSide3());
        return sb.toString();
    }
}
