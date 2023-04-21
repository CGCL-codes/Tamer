package simulation.editor;

import javax.swing.JFrame;
import simulation.model.Simulator;
import simulation.visual.SimulationScreen;

public interface ISimulatorContentController {

    Simulator getSimulator();

    SimulationScreen getSimulationScreen();

    JFrame getSimulationFrame();

    void setSimulator(Simulator simulator);

    void setSimulationScreen(SimulationScreen screen);
}
