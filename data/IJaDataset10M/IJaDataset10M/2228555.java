package my.test.pkg;

/**
 * Part of the contestJ test framework.
 * 
 * Distributed under the terms of the LGPL.
 * 
 * Copyright (C) 2005 Thomas Roka-Aardal
 * 
 * @author Thomas Roka-Aardal
 * 
 */
public class TestFactory {

    public TestInterface getTestInterfaceImplementation() {
        return new Test2();
    }
}
