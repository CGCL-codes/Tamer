package simulab;

import simulab.threads.SimulationManager;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        SimulationManager.addSimulator("200");
        SimulationManager.addSimulator("200");
        SimulationManager.addSimulator("200");
        SimulationManager.terminate();
        SimulationManager.addSimulator("200");
        Thread.sleep(2000);
    }
}
