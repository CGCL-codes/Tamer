package com.magic.visitor;

public class Client {

    private static ObjectStructure aObjects;

    private static Visitor visitor;

    public static void main(String[] args) {
        aObjects = new ObjectStructure();
        aObjects.add(new NodeA());
        aObjects.add(new NodeB());
        visitor = new Visitor();
        aObjects.action(visitor);
    }
}
