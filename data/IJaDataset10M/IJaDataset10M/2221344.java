package net.sourceforge.purrpackage.test.instrument.a;

import net.sourceforge.purrpackage.test.instrument.IFoo;

public class FooATestNG implements IFoo {

    static {
        "A <clinit> method".length();
    }

    public void fromSameBeforeClass() {
    }

    public void fromOtherBeforeClass() {
    }

    public void fromSameBeforeMethod() {
    }

    public void fromOtherBeforeMethod() {
    }

    public void fromSamePackage() {
    }

    public void fromOtherPackage() {
    }

    public void fromSameOldSchool() {
    }

    public void fromNowhere() {
    }

    public void fromSameAfterMethod() {
    }

    public void fromOtherAfterMethod() {
    }

    public void fromSameAfterClass() {
    }

    public void fromOtherAfterClass() {
    }

    String x;

    public void branch(boolean same) {
        x = "x";
        if (same) {
            x = "y";
        } else {
            x = "z";
        }
    }

    public void specialForA() {
    }
}
