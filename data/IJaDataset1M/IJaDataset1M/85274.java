package simulation.editor;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import simulation.model.Simulator;
import simulation.visual.SimulationScreen;
import simulation.visual.WorldScreenMapper;

public class SampleSimulatorContentController implements ISimulatorContentController {

    protected JFrame ownerFrame;

    protected JPanel ownerPanel;

    protected Simulator simulator;

    protected SimulationScreen simulationScreen;

    public SampleSimulatorContentController() {
        simulator = new Simulator();
        simulator.worldContent.xStart = -500;
        simulator.worldContent.xEnd = 500;
        simulator.worldContent.yStart = -500;
        simulator.worldContent.yEnd = 500;
        simulationScreen = new SimulationScreen(simulator, new WorldScreenMapper(1000, 1000, 0, 0, 640, 480));
        ownerPanel = new JPanel(new BorderLayout());
        ownerPanel.add(simulationScreen, BorderLayout.CENTER);
        ownerPanel.invalidate();
        ownerFrame = new JFrame("Simulation Viewer");
        ownerFrame.setContentPane(ownerPanel);
        ownerFrame.pack();
        ownerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ownerFrame.setVisible(true);
    }

    public JFrame getSimulationFrame() {
        return ownerFrame;
    }

    public SimulationScreen getSimulationScreen() {
        return simulationScreen;
    }

    public Simulator getSimulator() {
        return simulator;
    }

    public void setSimulationScreen(SimulationScreen screen) {
        screen.editorListener = simulationScreen.editorListener;
        ownerPanel.removeAll();
        ownerPanel.add(screen, BorderLayout.CENTER);
        simulationScreen = screen;
        ownerPanel.invalidate();
        ownerFrame.pack();
    }

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
}
