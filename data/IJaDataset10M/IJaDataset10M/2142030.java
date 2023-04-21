package test.conffailure;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ClassWithFailedBeforeTestClass {

    @BeforeClass
    public void setUpShouldFail() {
        throw new RuntimeException("Failing in setUp");
    }

    @Test
    public void dummy() {
    }
}
