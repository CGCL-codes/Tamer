package principal;

import Agents.AgentMessageReceiver;
import StreetController.StreetControllerAgent;
import analyzer.AnalyzerAgent;
import carGeneration.CarGeneratorAgent;
import lightControl.LightControlAgent;
import org.objectweb.proactive.core.util.CircularArrayList;
import org.objectweb.proactive.examples.StandardFrame;

public class simulatorApplet extends StandardFrame implements AgentMessageReceiver {

    private SimulatorControler simulatorControler;

    private Action startAction;

    private Action suspendAction;

    private Action resumeAction;

    private Action callAction;

    private Action chainedCallAction;

    protected StreetControllerAgentListModel streetAgentListModel;

    protected CarGeneratorAgent carGeneratorAgent;

    protected javax.swing.JList agentList;

    protected javax.swing.JTextArea itineraryField;

    private LightControlAgent lightControlAgent;

    private AnalyzerAgent analyzerAgent;

    public simulatorApplet(SimulatorControler c, CircularArrayList streetList) {
        super("Simulation Controller");
        this.simulatorControler = c;
        this.streetAgentListModel = new StreetControllerAgentListModel(streetList);
        this.startAction = new StartAction();
        this.suspendAction = new SuspendAction();
        this.resumeAction = new ResumeAction();
        this.callAction = new CallAction();
        this.chainedCallAction = new ChainedCallAction();
        carGeneratorAgent = null;
        init(600, 300);
    }

    public void start() {
        receiveMessage("Started...");
    }

    protected javax.swing.JPanel createRootPanel() {
        javax.swing.JPanel rootPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        javax.swing.JPanel p1 = new javax.swing.JPanel(new java.awt.GridLayout(0, 1));
        javax.swing.JButton bStart = new javax.swing.JButton("Start Simulation");
        bStart.setToolTipText("Start Simulation");
        bStart.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                simulatorControler.receiveMessage("0");
            }
        });
        p1.add(bStart);
        javax.swing.JButton bSuspend = new javax.swing.JButton("Suspend");
        bSuspend.setToolTipText("Suspend itinerary");
        bSuspend.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                executeAction(agentList.getSelectedIndices(), suspendAction);
            }
        });
        p1.add(bSuspend);
        javax.swing.JButton bResume = new javax.swing.JButton("Resume");
        bResume.setToolTipText("Resume itinerary");
        bResume.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                executeAction(agentList.getSelectedIndices(), resumeAction);
            }
        });
        p1.add(bResume);
        javax.swing.JButton bAnalyzerAgent = new javax.swing.JButton("Start Analyzer Agent");
        bAnalyzerAgent.setToolTipText("Start Analyzer Agent");
        bAnalyzerAgent.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                startAnalyzerAgent();
                analyzerAgent.start();
            }
        });
        p1.add(bAnalyzerAgent);
        rootPanel.add(p1, java.awt.BorderLayout.WEST);
        javax.swing.JPanel p2 = new javax.swing.JPanel(new java.awt.GridLayout(0, 1));
        javax.swing.JButton bAddAgent = new javax.swing.JButton("Add agent");
        bAddAgent.setToolTipText("Add one new agent");
        bAddAgent.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                receiveMessage("Add new agent pressed");
                addAgent();
            }
        });
        p2.add(bAddAgent);
        javax.swing.JButton bCall = new javax.swing.JButton("Call");
        bCall.setToolTipText("Call");
        bCall.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                executeAction(agentList.getSelectedIndices(), callAction);
            }
        });
        p2.add(bCall);
        javax.swing.JButton bAddCarGeneratorAgent = new javax.swing.JButton("Add carGeneratorAgent");
        bAddCarGeneratorAgent.setToolTipText("Add CarGeneratorAgent");
        bAddCarGeneratorAgent.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                addCarGeneratorAgent();
            }
        });
        p2.add(bAddCarGeneratorAgent);
        javax.swing.JButton bAddLightControlAgent = new javax.swing.JButton("Add LightControl Agent");
        bAddLightControlAgent.setToolTipText("Add LightControl Agent");
        bAddLightControlAgent.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                addLightControlAgent();
            }
        });
        p2.add(bAddLightControlAgent);
        rootPanel.add(p2, java.awt.BorderLayout.EAST);
        javax.swing.JPanel p3 = new javax.swing.JPanel(new java.awt.GridLayout(1, 0));
        agentList = new javax.swing.JList(streetAgentListModel);
        agentList.setToolTipText("Agent list");
        p3.add(new javax.swing.JScrollPane(agentList));
        itineraryField = new javax.swing.JTextArea();
        itineraryField.setToolTipText("Itinerary of selected agent");
        p3.add(new javax.swing.JScrollPane(itineraryField));
        rootPanel.add(p3, java.awt.BorderLayout.CENTER);
        return rootPanel;
    }

    private void addAgent() {
        int n = streetAgentListModel.getSize();
        try {
            StreetControllerAgent newStreetAgent = simulatorControler.createStreetAgent(n);
            StreetControllerAgentWrapper pw = new StreetControllerAgentWrapper(newStreetAgent, "Agent " + n);
            streetAgentListModel.addStreetControllerAgent(pw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (n > 0) {
            StreetControllerAgentWrapper p1 = (StreetControllerAgentWrapper) streetAgentListModel.getElementAt(n - 1);
            StreetControllerAgentWrapper p2 = (StreetControllerAgentWrapper) streetAgentListModel.getElementAt(n);
            (p1.streetControllerAgent).setOther(p2.streetControllerAgent);
        } else {
            agentList.setSelectedIndex(0);
        }
    }

    private void addCarGeneratorAgent() {
        try {
            CarGeneratorAgent newCarGeneratorAgent = simulatorControler.createCarGeneratorAgent(1);
            this.carGeneratorAgent = newCarGeneratorAgent;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLightControlAgent() {
        try {
            LightControlAgent newLightControlAgent = simulatorControler.createLightControlAgent(1);
            this.lightControlAgent = newLightControlAgent;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAnalyzerAgent() {
        try {
            AnalyzerAgent newAnalyzerAgent = simulatorControler.createAnalyzerAgent(1);
            this.analyzerAgent = newAnalyzerAgent;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeAction(int[] selection, Action action) {
        if (selection.length == 0) {
            receiveMessage("You must select an agent in the list first or add one if none is already present");
        } else {
            for (int i = 0; i < selection.length; i++) {
                int value = selection[i];
                StreetControllerAgentWrapper p = (StreetControllerAgentWrapper) streetAgentListModel.getElementAt(value);
                action.execute(p.streetControllerAgent);
            }
        }
    }

    private String getItinerary(int[] selection) {
        if (selection.length == 0) {
            receiveMessage("You must select an agent in the list first or add one if none is already present");
        } else if (selection.length > 1) {
            receiveMessage("You must select one agent");
        } else {
            int value = selection[0];
            StreetControllerAgentWrapper p = (StreetControllerAgentWrapper) streetAgentListModel.getElementAt(value);
            String[] tabItinerary = p.getItinerary();
            String itinerary = new String();
            if (tabItinerary.length != 0) {
                itinerary = tabItinerary[0];
                for (int i = 1; i < tabItinerary.length; i++) {
                    itinerary = itinerary + "\n" + tabItinerary[i];
                }
            }
            return itinerary;
        }
        return null;
    }

    private void setItinerary(int[] selection) {
        if (selection.length == 0) {
            receiveMessage("You must select an agent in the list first or add one if none is already present");
        } else {
            for (int i = 0; i < selection.length; i++) {
                int value = selection[i];
                StreetControllerAgentWrapper p = (StreetControllerAgentWrapper) streetAgentListModel.getElementAt(value);
                p.setItinerary(itineraryField.getText());
            }
        }
    }

    private interface Action {

        public void execute(StreetControllerAgent p);
    }

    private class StartAction implements Action {

        public void execute(StreetControllerAgent p) {
            p.start();
        }
    }

    private class SuspendAction implements Action {

        public void execute(StreetControllerAgent p) {
            p.suspend();
        }
    }

    private class ResumeAction implements Action {

        public void execute(StreetControllerAgent p) {
            p.resume();
        }
    }

    private class CallAction implements Action {

        public void execute(StreetControllerAgent p) {
            receiveMessage(p.call());
        }
    }

    private class ChainedCallAction implements Action {

        public void execute(StreetControllerAgent p) {
            p.chainedCall();
        }
    }

    private static class StreetControllerAgentListModel extends javax.swing.AbstractListModel {

        private CircularArrayList streetControllerAgentList;

        public StreetControllerAgentListModel(CircularArrayList streetControllerAgentList) {
            this.streetControllerAgentList = streetControllerAgentList;
        }

        public boolean isEmpty() {
            return streetControllerAgentList.isEmpty();
        }

        public int getSize() {
            return streetControllerAgentList.size();
        }

        public Object getElementAt(int index) {
            return streetControllerAgentList.get(index);
        }

        public void addStreetControllerAgent(StreetControllerAgentWrapper pw) {
            int n = streetControllerAgentList.size();
            streetControllerAgentList.add(pw);
            fireIntervalAdded(this, n, n);
        }

        public void clear() {
            int n = streetControllerAgentList.size();
            if (n > 0) {
                streetControllerAgentList.clear();
                fireIntervalRemoved(this, 0, n - 1);
            }
        }
    }

    private static class StreetControllerAgentWrapper implements java.io.Serializable {

        StreetControllerAgent streetControllerAgent;

        String name;

        StreetControllerAgentWrapper(StreetControllerAgent streetControllerAgent, String name) {
            this.streetControllerAgent = streetControllerAgent;
            this.name = name;
        }

        public String[] getItinerary() {
            return streetControllerAgent.getItinerary();
        }

        public void setItinerary(String itinerary) {
            java.util.ArrayList itineraryList = new java.util.ArrayList();
            java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(itinerary, "\n");
            while (tokenizer.hasMoreTokens()) itineraryList.add(tokenizer.nextToken());
            String[] intineraryArray = (String[]) itineraryList.toArray(new String[0]);
            streetControllerAgent.setItinerary(intineraryArray);
        }

        public String toString() {
            return name;
        }
    }
}
