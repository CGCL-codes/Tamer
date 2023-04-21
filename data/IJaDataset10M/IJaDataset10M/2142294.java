package artifacts;

import alice.cartago.Artifact;
import alice.cartago.OPERATION;

public class WashingMachine extends Artifact {

    private String name;

    @OPERATION
    void init(String washMachName) {
        this.name = washMachName;
    }

    @OPERATION
    void turnOn() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        signal("turnedOn(" + name + ")");
        log("Washing Machine is Turned on");
    }

    @OPERATION
    void setUp() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        signal("setUp(" + name + ")");
        log("Washing Machine is Set Up");
    }

    @OPERATION
    void putDetergent() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        signal("detergantPut(" + name + ")");
        log("Washing Machine detergant is put in");
    }

    @OPERATION
    void start() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        signal("started(" + name + ")");
        log("Washing Machine is started");
    }
}
