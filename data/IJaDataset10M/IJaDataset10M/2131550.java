package test.org.hrodberaht.inject.testservices.annotated;

import org.hrodberaht.inject.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Date;

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

    private String information = null;

    private String initText = null;

    private String initTextSpecial = null;

    @Injected
    Tire specialInjectField;

    Tire specialInjectMethod;

    @SpecialResource
    String someInformation;

    @Inject
    public Volvo(@Spare Tire spareTire) {
        this.spareTire = spareTire;
    }

    @Injected
    public void setSpecialInjectMethod(Tire specialInjectMethod) {
        this.specialInjectMethod = specialInjectMethod;
    }

    public Volvo() {
    }

    @PostConstruct
    public void init() {
        if (someInformation != null) {
            initText = "Initialized " + someInformation;
        } else {
            initText = "Initialized";
        }
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getInitText() {
        return initText;
    }

    public String getInitTextSpecial() {
        return initTextSpecial;
    }

    public Tire getSpecialInjectField() {
        return specialInjectField;
    }

    public Tire getSpecialInjectMethod() {
        return specialInjectMethod;
    }
}
