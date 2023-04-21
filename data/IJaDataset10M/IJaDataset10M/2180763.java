package ca.gc.drdc_rddc.atlantic.application;

import ca.gc.drdc_rddc.atlantic.vmsa.VMSATime;

public class ExampleFederate1 extends ModelFederate {

    public void registerInterests() {
        super.registerInterests();
    }

    public void simulateStep() {
        VMSATime fedTime = (VMSATime) model.getTimeManager().getSimulationTime();
        System.out.println("time=" + fedTime.toDouble());
    }

    public static void main(String[] args) {
        ModelFederate federate = new ExampleFederate1();
        federate.launch(args);
    }
}
