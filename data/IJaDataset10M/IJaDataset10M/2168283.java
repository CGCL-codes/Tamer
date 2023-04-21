package ui;

import java.awt.GridLayout;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import simulationEngine.SimulationEngine;
import ui.model.TableListModel;

@SuppressWarnings("serial")
public class SimulationExecutionStatisticCard extends JPanel {

    private GridLayout gridL = new GridLayout(3, 1);

    private JList simulationStack = null;

    private JTable networkActionQueue = null;

    private JTable metricLifeControl = null;

    private Vector<String> stackContent = null;

    private Vector<String> metricHeader = null;

    private Vector<String> networkActionHeader = null;

    private Vector<String> getMetricHeader() {
        if (metricHeader == null) {
            metricHeader = new Vector<String>();
            metricHeader.add("Aktion");
            metricHeader.add("Timestep");
            metricHeader.add("Host1");
            metricHeader.add("Host2");
        }
        return metricHeader;
    }

    private Vector<String> getNetworkActionHeader() {
        if (metricHeader == null) {
            metricHeader = new Vector<String>();
            metricHeader.add("Spalte1");
            metricHeader.add("Spalte2");
            metricHeader.add("Spalte3");
        }
        return metricHeader;
    }

    public SimulationExecutionStatisticCard(SimulationEngine engine) {
        super();
        this.stackContent = engine.getScenarioParameter().getSimpleActionNames();
        this.setLayout(gridL);
        this.add(new JScrollPane(this.getSimulationStack()));
        this.add(new JScrollPane(this.getNetworkActionQueue()));
        this.add(new JScrollPane(this.getMetricLifeControl()));
    }

    private JList getSimulationStack() {
        if (simulationStack == null) {
            simulationStack = new JList(stackContent);
        }
        return simulationStack;
    }

    private JTable getNetworkActionQueue() {
        if (networkActionQueue == null) {
            networkActionQueue = new JTable();
        }
        return networkActionQueue;
    }

    private JTable getMetricLifeControl() {
        if (metricLifeControl == null) {
            metricLifeControl = new JTable(new TableListModel(null, this.getMetricHeader()));
        }
        return metricLifeControl;
    }

    public void addNewMetricRow(Vector<String> newRow) {
        if (newRow.size() == ((TableListModel) this.getMetricLifeControl().getModel()).getColumnCount()) {
            ((TableListModel) this.getMetricLifeControl().getModel()).addRow(newRow);
        } else {
            System.out.println("Wrong ColumnCount");
        }
        this.getMetricLifeControl().updateUI();
    }
}
