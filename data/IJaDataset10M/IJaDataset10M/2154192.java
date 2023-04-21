package swarm.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import swarm.SystemProperties;
import swarm.engine.Individual;
import swarm.engine.PopulationSimulator;
import swarm.engine.PopulationSimulatorWithStatistics;
import swarm.engine.MetricsCalculator.Grouping;
import swarm.engine.statistics.Clusterer.Cluster;
import swarm.gui.OSCOutputConfiguration.Routing;

public class OSCOutputWindow extends JFrame implements PropertyChangeListener, ActionListener, FocusListener {

    private static final int getNumberOfOutputConfigRows() {
        return SystemProperties.getInt(OSCOutputWindow.class, "numberOfOutputConfigRows", 20);
    }

    private OSCOutput output;

    private List<RoutingPanel> routings;

    private Box vBox;

    private JMenuItem menuSave, menuLoad;

    private JFileChooser fileSelector = new JFileChooser();

    {
        fileSelector.setSelectedFile(new File(System.getProperty("user.dir", ".")));
    }

    private final PopulationSimulatorWithStatistics simulator;

    private JTextField ipAddress;

    private JSpinner port;

    public class RoutingPanel extends JPanel implements FocusListener {

        private static final long serialVersionUID = 1L;

        private JTextField oscAddress;

        private GroupingComboBox grouping;

        private Routing lastState = null;

        private JCheckBox includeHistograms;

        public RoutingPanel() {
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            oscAddress = new JTextField();
            grouping = new GroupingComboBox(simulator.getMetricsCalculator());
            includeHistograms = new JCheckBox("Include Histograms?");
            add(oscAddress);
            add(grouping);
            add(includeHistograms);
            oscAddress.addFocusListener(this);
            grouping.addFocusListener(this);
            includeHistograms.addFocusListener(this);
            setMaximumSize(new Dimension(1500, 30));
            setMinimumSize(new Dimension(500, 20));
        }

        public OSCOutputConfiguration.Routing getState() {
            if (oscAddress.getText().length() == 0) return null;
            return new OSCOutputConfiguration.Routing(oscAddress.getText(), (Grouping) grouping.getSelectedItem(), includeHistograms.isSelected());
        }

        public void setState(OSCOutputConfiguration.Routing v) {
            lastState = v;
            oscAddress.setText(v.getOscAddress());
            grouping.setSelectedItem(v.getGrouping());
            grouping.repaint();
            includeHistograms.setSelected(v.getIncludeHistograms());
        }

        public void focusGained(FocusEvent e) {
            focusLost(e);
        }

        public void focusLost(FocusEvent e) {
            final Routing state = getState();
            firePropertyChange("state", lastState, state);
            lastState = state;
        }
    }

    public OSCOutputWindow(PopulationSimulatorWithStatistics simulator) {
        this.simulator = simulator;
        output = new OSCOutput(simulator);
        vBox = Box.createVerticalBox();
        setContentPane(vBox);
        menuSave = new JMenuItem("Save");
        menuSave.addActionListener(this);
        menuLoad = new JMenuItem("Load");
        menuLoad.addActionListener(this);
        JMenuBar mb = new JMenuBar();
        mb.add(menuSave);
        mb.add(menuLoad);
        mb.setMaximumSize(new Dimension(1024, 30));
        vBox.add(mb);
        ipAddress = new JTextField("localhost");
        ipAddress.addFocusListener(this);
        port = new JSpinner(new SpinnerNumberModel(7001, 1, 32768, 1));
        port.addFocusListener(this);
        vBox.add(makeLabeledRow("IP Address", ipAddress));
        vBox.add(makeLabeledRow("Port", port));
        routings = new ArrayList<RoutingPanel>(getNumberOfOutputConfigRows());
        for (int i = 0; i < getNumberOfOutputConfigRows(); i++) {
            addRoutingPanel();
        }
        setMinimumSize(new Dimension(800, 100));
        setSize(new Dimension(1000, 500));
        update();
    }

    private Component makeLabeledRow(String string, JComponent comp) {
        Box box = Box.createHorizontalBox();
        box.add(new JLabel(string + ":"));
        box.add(comp);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    public void simulationUpdated() {
        output.simulationUpdated();
    }

    public OSCOutputConfiguration getConfiguration() {
        ArrayList<Routing> tempRoutings = new ArrayList<Routing>(routings.size());
        for (RoutingPanel rp : routings) {
            if (rp != null) {
                final Routing state = rp.getState();
                if (state != null) {
                    tempRoutings.add(state);
                }
            }
        }
        return new OSCOutputConfiguration(ipAddress.getText(), ((Number) port.getValue()).shortValue(), tempRoutings);
    }

    public void setConfiguration(OSCOutputConfiguration config) {
        Iterator<Routing> iter1 = config.getRoutings().iterator();
        {
            Iterator<RoutingPanel> iter2 = routings.iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                Routing r = iter1.next();
                RoutingPanel rp = iter2.next();
                rp.setState(r);
            }
        }
        while (iter1.hasNext()) {
            Routing r = iter1.next();
            final RoutingPanel routingPanel = addRoutingPanel();
            routingPanel.setState(r);
        }
        final int target = config.getRoutings().size() + 2;
        while (target > routings.size()) {
            addRoutingPanel();
        }
        ipAddress.setText(config.getIpAddress());
        port.setValue(config.getPort());
    }

    private RoutingPanel addRoutingPanel() {
        final RoutingPanel routingPanel = new RoutingPanel();
        routings.add(routingPanel);
        vBox.add(routingPanel);
        routingPanel.addPropertyChangeListener("state", this);
        return routingPanel;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof RoutingPanel) update();
    }

    private void update() {
        output.setConfig(getConfiguration());
    }

    private static String readFileAsString(File filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    private static void writeFileFromString(File file, String content) throws IOException {
        FileWriter writter = new FileWriter(file);
        try {
            writter.write(content);
        } finally {
            writter.close();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuSave) {
            int ret = fileSelector.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileSelector.getSelectedFile();
                try {
                    writeFileFromString(file, getConfiguration().toString());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(this, e1.getMessage(), "Error Saving File", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        } else if (e.getSource() == menuLoad) {
            int ret = fileSelector.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileSelector.getSelectedFile();
                try {
                    setConfiguration(OSCOutputConfiguration.fromString(readFileAsString(file)));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(this, e1.getMessage(), "Error Loading File: " + file, JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        }
    }

    public void focusGained(FocusEvent e) {
        update();
    }

    public void focusLost(FocusEvent e) {
        update();
    }
}
