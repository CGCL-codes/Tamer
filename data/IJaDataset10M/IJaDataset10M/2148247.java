package test.org.hrodberaht.inject.testservices.largepackage.sub5;

import javax.inject.Inject;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 18:00:28
 * @version 1.0
 * @since 1.0
 */
public class Volvo implements Car {

    @Inject
    @Spare
    Tire spareTire;

    @Inject
    @Spare
    VindShield spareVindShield;

    @Inject
    Tire frontLeft;

    @Inject
    Tire frontRight;

    @Inject
    Tire backRight;

    @Inject
    Tire backLeft;

    @Inject
    VindShield vindShield;

    @Inject
    TestDriver driver;

    @Inject
    TestDriverManager driverManager;

    @Inject
    public Volvo(@Spare Tire spareTire) {
        this.spareTire = spareTire;
    }

    public Volvo() {
    }

    public String brand() {
        return "volvo";
    }

    public TestDriver getDriver() {
        return driver;
    }

    public TestDriverManager getDriverManager() {
        return driverManager;
    }

    public Tire getSpareTire() {
        return spareTire;
    }

    public VindShield getSpareVindShield() {
        return spareVindShield;
    }

    public Tire getFrontLeft() {
        return frontLeft;
    }

    public Tire getFrontRight() {
        return frontRight;
    }

    public Tire getBackRight() {
        return backRight;
    }

    public Tire getBackLeft() {
        return backLeft;
    }

    public VindShield getVindShield() {
        return vindShield;
    }
}
