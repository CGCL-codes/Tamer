package edu.java.texbooks.scjp.collections.test01;

public class TestEqualsMethod {

    public static void main(String[] args) {
        testHashCode2();
    }

    public static void testHashCode2() {
        MyObject mo = new MyObject("pesho");
        System.out.println("mo.hashCode()= " + mo.hashCode());
        System.out.println("mo.getName().hashCode()= " + mo.getName().hashCode());
    }

    public static void test01() {
        Object object1 = new Object();
        Object object2 = new Object();
        System.out.println("Call toString() method of object1: " + object1.toString());
        MyObject cecoObject = new MyObject("Ceco");
        MyObject nikoObject = new MyObject("Nikolay");
        System.out.println("Call toString() method of myObject: " + cecoObject.toString());
        System.out.println("\n-----------------------------\n");
        System.out.println("object1 == object2       -->" + (object1 == object2));
        System.out.println("object1.equals(object2)  -->" + (object1.equals(object2)));
        System.out.println();
        System.out.println("object1 == object1       -->" + (object1 == object1));
        System.out.println("object1.equals(object1)  -->" + (object1.equals(object1)));
        System.out.println("\n-----------------------------\n");
        System.out.println("cecoObject.equals(nikoObject)    -->" + cecoObject.equals(nikoObject));
        System.out.println("\nSet cecoObject's name to nikoObject.");
        nikoObject.setName(cecoObject.getName());
        System.out.println("\ncecoObject.equals(nikoObject)    -->" + cecoObject.equals(nikoObject));
    }

    public static void testHashCode() {
        Object object = new Object();
        System.out.println("HashCode of the object1: " + object.hashCode());
        System.out.print("Print again the hashCode. It would be consistent: ");
        System.out.println(object.hashCode());
        System.out.println("\n------------------\n");
        Object object2 = object;
        System.out.println("HashCode of the object2: " + object2.hashCode());
        System.out.println("\n------------------\n");
        Object object3 = new Object();
        System.out.println("HashCode of the object3: " + object3.hashCode());
        System.out.println("\n------------------\n");
        String string1 = "ceco";
        String string2 = new String("ceco");
        System.out.println("string1.equals(string2)   -->  " + (string1.equals(string2)));
        System.out.println("HashCode of the string1: " + string1.hashCode());
        System.out.println("HashCode of the string2: " + string2.hashCode());
        String string3 = "ceoc";
        System.out.println("HashCode of the string3: " + string3.hashCode());
    }

    public static void testStringComparison() {
        String string1 = "test";
        String string2 = new String("test");
        String string3 = "test";
        System.out.println("string1 == string1         -->" + (string1 == string1));
        System.out.println("string1.equals(string1)    -->" + (string1.equals(string1)));
        System.out.println("\n----------------------------- \n");
        System.out.println("string1 == string2         -->" + (string1 == string2));
        System.out.println("string1.equals(string2)    -->" + (string1.equals(string2)));
        System.out.println("\n----------------------------- \n");
        System.out.println("string1 == string3         -->" + (string1 == string3));
        System.out.println("string1.equals(string3)    -->" + (string1.equals(string3)));
    }
}
