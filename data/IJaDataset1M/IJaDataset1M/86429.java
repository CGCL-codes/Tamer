package ui;

import model.TraceUser;
import model.factors.FactorCalculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;
import edu.sharif.ce.dml.common.util.PublicConfig;
import edu.sharif.ce.dml.common.util.InvalidConfigFileException;
import edu.sharif.ce.dml.common.parameters.logic.parameterable.Parameterable;
import edu.sharif.ce.dml.common.parameters.ui.dialogs.ParameterableList;
import edu.sharif.ce.dml.common.parameters.data.ParameterableConfigLoader;

/**
 * Created by IntelliJ IDEA.
 * User: Masoud
 * Date: Apr 24, 2007
 * Time: 11:51:14 AM
 */
public class MainFrame extends JFrame {

    public static final String FACTORS_FILE = "factorsconfig.xml";

    private File[] selectedTraces;

    public MainFrame() throws HeadlessException {
        super("Mobility Recognizer");
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem importTracesMenu = new JMenuItem("Import Traces");
        fileMenu.add(importTracesMenu);
        importTracesMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser openFileChooser = new JFileChooser();
                    openFileChooser.setMultiSelectionEnabled(true);
                    openFileChooser.setCurrentDirectory(new File(PublicConfig.getInstance().getLastFolderProperty()));
                    if (openFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        selectedTraces = openFileChooser.getSelectedFiles();
                        PublicConfig.getInstance().setLastFolderProperty(selectedTraces[0]);
                    }
                } catch (InvalidConfigFileException e1) {
                    e1.printStackTrace();
                }
            }
        });
        try {
            java.util.List<Parameterable> loadedParameterables = ParameterableConfigLoader.load(FACTORS_FILE);
            final java.util.List<FactorCalculator> factors = new ArrayList<FactorCalculator>(loadedParameterables.size());
            for (Parameterable parameterable : loadedParameterables) {
                factors.add((FactorCalculator) parameterable);
            }
            final ParameterableList parameterableList = new ParameterableList(loadedParameterables);
            JMenu runMenu = new JMenu("Run");
            menuBar.add(runMenu);
            JMenuItem learnTracesMenu = new JMenuItem("Learn Traces");
            runMenu.add(learnTracesMenu);
            learnTracesMenu.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (selectedTraces != null) {
                        TraceUser traceUser = new TraceUser(selectedTraces, true, factors);
                        traceUser.start();
                    } else {
                        JOptionPane.showMessageDialog(null, "Please import new trace files", "No Trace File", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            JMenuItem useMenu = new JMenuItem("Use Traces");
            runMenu.add(useMenu);
            useMenu.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (selectedTraces != null) {
                        TraceUser traceUser = new TraceUser(selectedTraces, false, factors);
                        traceUser.start();
                    } else {
                        JOptionPane.showMessageDialog(null, "Please import new trace files", "No Trace File", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            JMenu optionMenu = new JMenu("Option");
            menuBar.add(optionMenu);
            JMenuItem factorsConfigMenu = new JMenuItem("Factors Config");
            optionMenu.add(factorsConfigMenu);
            factorsConfigMenu.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    parameterableList.setModal(true);
                    parameterableList.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}
