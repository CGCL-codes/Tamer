package edu.java.lectures.lec09.exceptions.abstraction;

import edu.java.lectures.lec09.exceptions.creation.MyException;
import edu.java.lectures.lec09.exceptions.creation.MySubException;

public class MyClass extends MyAbstractClass {

    @Override
    public void doSomething2() throws MySubException {
    }

    @Override
    public void doSomething4() {
    }

    @Override
    public void doSomething3() throws MyException {
    }
}
