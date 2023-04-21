package edu.java.lectures.lec05.polimorphism;

public class ProbaTriangle {

    public static void main(String[] args) {
        System.out.println("\n\n----------EquilateralTriangle----------");
        EquilateralTriangle rawnostranenTr = new EquilateralTriangle(3);
        System.out.println(rawnostranenTr.getMedicenterName());
        System.out.println("\n\n----------IsoscelesTriangle----------");
        IsoscelesTriangle rawnobedrenTr = new IsoscelesTriangle(2, 3);
        System.out.println(rawnobedrenTr.getMedicenterName());
        System.out.println(rawnobedrenTr.a);
        System.out.println(((EquilateralTriangle) rawnobedrenTr).a);
        System.out.println(((EquilateralTriangle) rawnobedrenTr).getMedicenterName());
        System.out.println("\n\n----------ScaleneTriangle----------");
        ScaleneTriangle raznostranenTr = new ScaleneTriangle(2, 3, 4);
        System.out.println(raznostranenTr.getMedicenterName());
    }
}
