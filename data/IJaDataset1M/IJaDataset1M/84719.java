package edu.gatech.oad.antlab.person;

public class TestMain {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Person5 p5 = new Person5("Bob");
        System.out.println(p5.toString("oneTwo"));
        Person1 wai = new Person1("Joe");
        System.out.println(wai.toString("ks suc"));
    }
}
