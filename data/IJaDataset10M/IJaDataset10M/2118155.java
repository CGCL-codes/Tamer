package simulation.editor.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import simulation.editor.ISimulatorContentController;
import simulation.visual.TrackLayer;
import simulation.visual.SimulationScreen.PreDefinedLayers;

public class SimulationPanelHandler {

    private SimulationPanel simulationPanel;

    private ISimulatorContentController simulatorContent;

    public String filename;

    public SimulationPanelHandler() {
        simulationPanel = new SimulationPanel();
        simulationPanel.getJButtonOk().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                approved();
            }
        });
        simulationPanel.getJButtonCancel().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                reverted();
            }
        });
    }

    public SimulationPanel getSimulationPanel() {
        return simulationPanel;
    }

    public void showObject(ISimulatorContentController simulatorContent, String filename) {
        this.filename = filename;
        simulationPanel.getJLabelStatus().setText(" ");
        if (simulatorContent == null) return;
        this.simulatorContent = simulatorContent;
        simulationPanel.getJTextFieldFilename().setText(filename);
        simulationPanel.getJTextFieldSimulationTime().setText((float) simulatorContent.getSimulator().time + "");
        simulationPanel.getJTextFieldStepSize().setText((float) simulatorContent.getSimulator().simulation_step_size + "");
        TrackLayer trackLayer = (TrackLayer) simulatorContent.getSimulationScreen().getLayer(PreDefinedLayers.TRACK_LAYER);
        simulationPanel.getJCheckBoxDrawTrack().setSelected(trackLayer.isOn());
    }

    public void approved() {
        if (simulatorContent == null) return;
        simulationPanel.getJLabelStatus().setText(" ");
        try {
            float time = Float.parseFloat(simulationPanel.getJTextFieldSimulationTime().getText());
            float stepSize = Float.parseFloat(simulationPanel.getJTextFieldStepSize().getText());
            simulatorContent.getSimulator().time = time;
            simulatorContent.getSimulator().simulation_step_size = stepSize;
            TrackLayer trackLayer = (TrackLayer) simulatorContent.getSimulationScreen().getLayer(PreDefinedLayers.TRACK_LAYER);
            trackLayer.setOn(simulationPanel.getJCheckBoxDrawTrack().isSelected());
            simulatorContent.getSimulationScreen().repaint();
        } catch (Exception e) {
            simulationPanel.getJLabelStatus().setText("Check your inputs!");
        }
    }

    public void reverted() {
        if (simulatorContent == null) return;
        showObject(simulatorContent, filename);
    }
}
