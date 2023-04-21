package org.susan.java.classes.main;

public class MainNotSpecial {

    public static void main() {
        System.out.println("In no-argument main");
    }

    public static void main(String args[]) {
        System.out.println("In String[] main");
    }

    public static void main(int argsc, String argv[]) {
        System.out.println("In int, String[] main");
    }

    public static void main(String args) {
        System.out.println("In string main");
    }
}
