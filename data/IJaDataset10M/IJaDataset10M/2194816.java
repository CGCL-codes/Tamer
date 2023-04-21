package castadiva;

import castadiva.CastadivaController.CheckSimulationThread;
import castadiva_gui.MainMenuGUI;
import castadiva_gui.MobilityDesignerGUI;
import castadiva_gui.ProtocolsGUI;
import castadiva_gui.RandomSimulationGUI;
import castadiva_gui.SimulationGUI;
import castadiva_gui.TrafficGUI;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author alvaro
 */
public class SimulationController {

    private SimulationGUI m_simulationWindow;

    private ExecutionPlanner executionPlanner;

    private CastadivaModel m_model;

    private MainMenuGUI m_view;

    private TrafficGUI m_trafficGUI;

    private RandomSimulationGUI m_randomSimulation;

    private CheckSimulationThread checkSimulation;

    private CastadivaController controller;

    private ProtocolsGUI m_protocol;

    private MobilityDesignerGUI mobDes;

    SimulationController(SimulationGUI sim_window, ExecutionPlanner exe, CastadivaModel model, TrafficGUI traffic, RandomSimulationGUI random, CheckSimulationThread checkSim, CastadivaController control, ProtocolsGUI proto, MobilityDesignerGUI mobilityDesigner, MainMenuGUI view) {
        m_simulationWindow = sim_window;
        m_model = model;
        m_trafficGUI = traffic;
        m_randomSimulation = random;
        checkSimulation = checkSim;
        controller = control;
        m_protocol = proto;
        mobDes = mobilityDesigner;
        executionPlanner = exe;
        m_view = view;
    }

    public void SetExecutionPlanner(ExecutionPlanner execution) {
        executionPlanner = execution;
    }

    /**
     *  Insert the listeners of this window
     */
    public void SimulationAllListenersReady() {
        m_simulationWindow.addSimulateButtonListener(new SimulateListener());
        m_simulationWindow.addReplaySimulationListener(new ReplaySimulationListener());
        m_simulationWindow.addResetButtonListener(new ResetSimulationListener());
        m_simulationWindow.addCloseButtonListener(new CloseButtonListener());
        m_simulationWindow.addXTextFieldListener(new XFieldListener());
        m_simulationWindow.addYTextFieldListener(new YFieldListener());
        m_simulationWindow.addZTextFieldListener(new ZFieldListener());
        m_simulationWindow.addATextFieldListener(new AreaFieldListener());
        m_simulationWindow.addXBoundListener(new XBoundListener());
        m_simulationWindow.addYBoundListener(new YBoundListener());
        m_simulationWindow.addSimTimeTextFieldListener(new SimulationTimeListener());
        m_simulationWindow.addMaxSpeedTextFieldListener(new MaxSpeedListener());
        m_simulationWindow.addMinSpeedTextFieldListener(new MinSpeedListener());
        m_simulationWindow.addSimPauseTextFieldListener(new SimulationPauseListener());
        m_simulationWindow.addAPComboBoxListener(new SelectAPListener());
        m_simulationWindow.addTrafficButtonListener(new TrafficViewerListener());
        m_simulationWindow.addSizeTextFieldListener(new SizeFieldListener());
        m_simulationWindow.addRandomSceneryButtonListener(new RandomSceneryListener());
        m_simulationWindow.addChangeReplayTimeListener(new ReplayPositionListener());
        m_simulationWindow.addHelpButtonListener(new HelpSimulationListener());
        m_simulationWindow.addSaveSimulationListener(new SaveScenarioListener());
        m_simulationWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        m_simulationWindow.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                m_view.setVisible(true);
            }
        });
    }

    public void PreviousSimulationSteaps() {
        m_model.SetSimulationTime(m_simulationWindow.ReturnSimulationTime());
        m_model.CalculateRealSimulationTime();
        m_model.SetMobilityMaxSpeed(m_simulationWindow.ReturnMobilityMaxSpeed());
        m_model.SetMobilityMinSpeed(m_simulationWindow.ReturnMobilityMinSpeed());
        m_model.SetBoundX(m_simulationWindow.ReturnXBoundField());
        m_model.SetBoundY(m_simulationWindow.ReturnYBoundField());
        m_model.SetSimulationPause(m_simulationWindow.ReturnMobilityPauseTextField());
        if (m_model.MobiliyActivated()) {
            m_model.ExtendNodePositions();
        }
        m_model.randomSimulating = false;
        m_simulationWindow.EnableReplay(false);
        m_simulationWindow.MarkReplayCheck(false);
        m_model.replay = false;
        m_model.tableModel.UpdateData(m_model.accessPoints.GetTraffic());
        m_trafficGUI.UpdateTable();
        m_trafficGUI.DisableWindow();
        m_randomSimulation.DisableWindow();
        m_model.ChangeRoutingProtocol(m_simulationWindow.ProtocolSelected());
        m_simulationWindow.ChangeSimulationTime(m_model.GetSimulationTime());
        m_randomSimulation.ChangeSimulationTime(m_model.GetSimulationTime());
    }

    class RandomSceneryListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.randomSimulating = false;
            m_model.SetRandomScenery();
            m_simulationWindow.Repaint();
            m_simulationWindow.UpdatePositionPanel();
        }
    }

    class SimulateListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (m_simulationWindow.isExecutionPlanner()) {
                m_simulationWindow.setExecutionPlanner(false);
                if (m_simulationWindow.getLoadPath() != null) {
                    executionPlanner.newRow(m_simulationWindow.getLoadPath());
                    m_simulationWindow.setLoadPath(null);
                } else {
                    JOptionPane.showMessageDialog(m_simulationWindow, "Unable to add Scenario.");
                }
                m_simulationWindow.setVisible(false);
                m_simulationWindow.changeSimulateButtonText("New Simulation");
                executionPlanner.setVisible(true);
            } else {
                PreviousSimulationSteaps();
                m_model.isRandomTraffic = false;
                m_model.AllSimulationSteaps();
                m_simulationWindow.RunningSimulationView();
            }
        }
    }

    class ReplaySimulationListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.PositionateNodesInDeterminedSecond(0);
            m_simulationWindow.ModifyBlackBoard();
            PreviousSimulationSteaps();
            m_model.ReplaySimulationSteaps();
            m_simulationWindow.ReplaySimulationView();
        }
    }

    class ResetSimulationListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.rebootAPs();
            if (m_model.externalTrafficFlow.size() < 1) {
                m_simulationWindow.ActivateButtons(true);
            }
        }
    }

    class CloseButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.StopSimulation();
            m_simulationWindow.setVisible(false);
            if (m_simulationWindow.isExecutionPlanner()) {
                m_simulationWindow.setExecutionPlanner(false);
                executionPlanner.setVisible(true);
            } else {
                m_view.setVisible(true);
            }
        }
    }

    class XFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.selectionedAP1.x = m_simulationWindow.ReturnXTextField();
            m_model.ChangeAP(m_simulationWindow.WhatSelectedAP(), m_model.selectionedAP1);
            m_model.GenerateStaticVisibilityMatrix();
            m_simulationWindow.ModifyBlackBoard();
        }
    }

    class YFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.selectionedAP1.y = m_simulationWindow.ReturnYTextField();
            m_model.ChangeAP(m_simulationWindow.WhatSelectedAP(), m_model.selectionedAP1);
            m_model.GenerateStaticVisibilityMatrix();
            m_simulationWindow.ModifyBlackBoard();
        }
    }

    class ZFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.selectionedAP1.z = m_simulationWindow.ReturnZTextField();
            m_model.ChangeAP(m_simulationWindow.WhatSelectedAP(), m_model.selectionedAP1);
            m_model.GenerateStaticVisibilityMatrix();
            m_simulationWindow.ModifyBlackBoard();
        }
    }

    class XBoundListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.SetBoundX(m_simulationWindow.ReturnXBoundField());
            m_simulationWindow.ModifyBlackBoard();
        }
    }

    class MaxSpeedListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.SetMobilityMaxSpeed(m_simulationWindow.ReturnMobilityMaxSpeed());
        }
    }

    class MinSpeedListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (m_simulationWindow.ReturnMobilityMinSpeed() > m_simulationWindow.ReturnMobilityMaxSpeed()) {
                m_simulationWindow.SetMobilityMaxSpeed(m_simulationWindow.ReturnMobilityMinSpeed());
            }
            m_model.SetMobilityMinSpeed(m_simulationWindow.ReturnMobilityMinSpeed());
        }
    }

    class YBoundListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.SetBoundY(m_simulationWindow.ReturnYBoundField());
            m_simulationWindow.ModifyBlackBoard();
        }
    }

    class AreaFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.selectionedAP1.range = m_simulationWindow.ReturnATextField();
            m_model.ChangeAP(m_simulationWindow.WhatSelectedAP(), m_model.selectionedAP1);
            m_model.GenerateStaticVisibilityMatrix();
            m_simulationWindow.ModifyBlackBoard();
        }
    }

    class SimulationTimeListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int st = m_simulationWindow.ReturnSimulationTime();
            m_model.SetSimulationTime(st);
            m_randomSimulation.ChangeSimulationTime(st);
        }
    }

    class SimulationPauseListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.SetSimulationPause(m_simulationWindow.ReturnMobilityPauseTextField());
        }
    }

    class ReplayPositionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (m_simulationWindow.ReturnReplayTime() > m_model.GetSimulationTime()) {
                m_simulationWindow.ChangeReplayTime(m_model.GetSimulationTime());
            }
            if (m_simulationWindow.ReturnReplayTime() < 0) {
                m_simulationWindow.ChangeReplayTime(0);
            }
            if (m_model.replay) {
                m_model.simulationSeconds = m_simulationWindow.ReturnReplayTime();
                checkSimulation.PaintNodeMovement(m_simulationWindow.ReturnReplayTime());
            }
        }
    }

    class TrafficViewerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.tableModel.UpdateData(m_model.accessPoints.GetTraffic());
            m_trafficGUI.UpdateTable();
            m_trafficGUI.setVisible(true);
        }
    }

    class SelectAPListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (m_model.HowManyAP() > 0) {
                if (m_simulationWindow.WhatSelectedAP() >= 0) {
                    m_model.selectionedAP1 = m_model.GetAP(m_simulationWindow.WhatSelectedAP());
                    m_simulationWindow.UpdatePositionPanel();
                    m_simulationWindow.UpdateShowNodeRange();
                }
            }
        }
    }

    class SizeFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_simulationWindow.GetNewSize();
        }
    }

    class HelpSimulationListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_model.selectedHelp = m_model.SCENARIO_INDEX;
            controller.ShowHelp();
        }
    }

    class SaveScenarioListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            File f = new File(m_model.pathScenario);
            JFileChooser save = new JFileChooser(f);
            save.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int selection = save.showSaveDialog(save);
            if (selection == JFileChooser.APPROVE_OPTION) {
                File file = save.getSelectedFile();
                String fileTemp;
                File finalFile;
                if (controller.hasScenarioDir(file) || controller.isScenarioDir(file)) {
                    int select = JOptionPane.showConfirmDialog(m_simulationWindow, "Are you sure to overwrite?", "Overwrite", JOptionPane.OK_CANCEL_OPTION);
                    if (select == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                    if (controller.isScenarioDir(file)) {
                        fileTemp = file.getAbsolutePath();
                        file = file.getParentFile();
                        finalFile = new File(fileTemp);
                    } else {
                        fileTemp = file.getAbsolutePath() + File.separator + "Scenario";
                        finalFile = new File(fileTemp);
                        finalFile.mkdir();
                    }
                } else {
                    fileTemp = file.getAbsolutePath() + File.separator + "Scenario";
                    finalFile = new File(fileTemp);
                    finalFile.mkdir();
                }
                m_model.pathScenario = finalFile.getAbsolutePath();
                m_model.routingProtocol = m_simulationWindow.ProtocolSelected();
                m_model.SaveCastadiva(finalFile.getAbsolutePath());
                if (m_simulationWindow.isExecutionPlanner()) {
                    m_simulationWindow.setLoadPath(file.getAbsolutePath());
                }
            }
        }
    }

    class ProtocolsListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            m_protocol.setVisible(true);
        }
    }

    class ExecutionPlannerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            executionPlanner.setVisible(true);
            m_view.setVisible(false);
        }
    }

    class MobilityDesignerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            mobDes.setVisible(true);
        }
    }
}
