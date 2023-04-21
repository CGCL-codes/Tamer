package org.codecover.instrumentation.java15.test.test1;

import java.util.Iterator;
import org.codecover.instrumentation.java15.test.test2.AVLTree;
import org.codecover.instrumentation.java15.test.test5.ComplexInterface;
import org.codecover.instrumentation.java15.test.test6.ComplexEnum;
import org.codecover.instrumentation.java15.test.test7.ComplexAnnotation;

public class TestClass {

    public static void main(String[] args) {
        AVLTree<String> avl = new AVLTree<String>(String.class);
        int base = Integer.MAX_VALUE / 4;
        for (int i = 0; i <= 200; i++) {
            base += 1928374;
            String thisString = Integer.toString(base);
            thisString = thisString.substring(thisString.length() - 4, thisString.length());
            avl.add(thisString);
        }
        Iterator<String> iterator;
        System.out.println("Up>>>>>>>>>");
        iterator = avl.iteratorUpAll();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("Up<<<<<<<<<");
        System.out.println("Down>>>>>>>>>");
        iterator = avl.iteratorDownAll();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("Down<<<<<<<<<");
        System.out.println("Between>>>>>>>>>");
        iterator = avl.iteratorUpBetween("1111", "8888");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("Between<<<<<<<<<");
        ComplexInterface compInt = new ComplexInterface.ComplexUser();
        System.out.println(compInt.toBool());
        System.out.println(compInt.toLong());
        ComplexEnum.ClassInComplexEnum compEnum = new ComplexEnum.ClassInComplexEnum();
        System.out.println(compEnum.getFavourite());
        ComplexAnnotation.ClassInComplexAnnotation compAnn = new ComplexAnnotation.ClassInComplexAnnotation();
        compAnn.annotated();
        new TestClass();
    }

    public static void MAIN(String[] args) {
    }

    public static void MaiN(String[] args) {
    }

    public TestClass() {
        System.out.println("Juhuu");
        System.out.flush();
        doSomething();
    }

    private void doSomething() {
        int a = 45;
        int b = a * a;
        double c = Math.pow(a, b);
        --a;
        a = a >> 1;
        a = a >> 1;
        a = a >>> 1;
        a = a >>> 1;
        a = a >>> 1;
        while (a == 45) {
            throw new RuntimeException();
        }
        b = b * a;
        String s = new String("Ökologische Äcker sind übermütig und weiß.").trim();
    }

    protected void blubb() throws Exception {
        do {
            throw new Exception();
        } while (true);
    }
}
