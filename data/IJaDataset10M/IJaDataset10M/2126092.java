package com.ars_subtilior.ducktail;

import org.testng.annotations.Configuration;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;
import com.ars_subtilior.ducktail.helper.Dog;
import com.ars_subtilior.ducktail.helper.Duck;
import com.ars_subtilior.ducktail.helper.DuckImpl;
import com.ars_subtilior.ducktail.helper.Frog;

/**
 * @author Dominik Wei-Fieg
 *
 */
@Test(groups = { "unittests" })
public class TestDuckTyper {

    private Frog frog;

    private Dog dog;

    private DuckImpl duckImpl;

    @Configuration(beforeTestMethod = true)
    public void setup() {
        frog = new Frog();
        dog = new Dog();
        duckImpl = new DuckImpl();
    }

    @Configuration(afterTestMethod = true)
    public void tearDown() {
        frog = null;
        dog = null;
        duckImpl = null;
    }

    @Test(groups = { "assert" })
    public void testRespondsTo() {
        assert DuckTyper.respondsTo(frog, Duck.class) : "Frog should have responded to the Duck interface";
        assert DuckTyper.respondsTo(duckImpl, Duck.class) : "DuckImpl has to respond to Duck interface since it implements it";
        assert !DuckTyper.respondsTo(dog, Duck.class) : "Dog should not have responded to the Duck interface";
    }

    @Test(groups = { "fails" })
    @ExpectedExceptions({ IllegalArgumentException.class })
    public void testRespondsToWrongArgument() {
        DuckTyper.respondsTo(dog, Dog.class);
    }

    @Test(groups = { "assert" })
    public void testDuckType() {
        Duck duck = DuckTyper.duckType(frog, Duck.class);
        assert duck instanceof Duck : "Expected to get a duck back";
        duck = DuckTyper.duckType(duckImpl, Duck.class);
        assert duck == duckImpl : "Expected duck to simply be duckImpl";
    }

    @Test(groups = { "fails" })
    @ExpectedExceptions({ IllegalArgumentException.class })
    public void testWrongArguments() {
        DuckTyper.duckType(dog, Duck.class);
    }

    @Test(groups = { "fails" })
    @ExpectedExceptions({ IllegalArgumentException.class })
    public void testWrongArgumentsTwo() {
        DuckTyper.duckType(dog, Frog.class);
    }
}
