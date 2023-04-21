package net.sf.genedator.plugin;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import net.sf.genedator.plugin.exceptions.UniqueDataGeneratingException;
import net.sf.genedator.plugin.loaders.IJarResources;
import net.sf.genedator.plugin.utils.PluginUtils;

/**
 *
 * @author sylwester.balcerek
 */
public class DictionaryValuesPlugin implements PluginDataGenerator {

    private final String NAME = "Dictionary Values Plugin";

    private final String VERSION = "1.0";

    private IJarResources iJarResources;

    private List<String> namesHolder;

    private boolean unique;

    private int nullRatio;

    private JPanel configPanel;

    private JCheckBox uniqueCheckBox;

    private JSlider nullSlider;

    private JTextField fileLocationField;

    private JTextArea valuesTextArea;

    private JRadioButton fileSelectionRadio;

    private JRadioButton areaSelectionRadio;

    private int prevStepNumber = Integer.MAX_VALUE;

    private int prevStepNumberOfRecords;

    public DictionaryValuesPlugin() {
        namesHolder = new ArrayList<String>();
        init();
    }

    private void init() {
        configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.PAGE_AXIS));
        configPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel mainPanel = new JPanel();
        uniqueCheckBox = new JCheckBox();
        nullSlider = new JSlider(0, 100);
        nullSlider.setMajorTickSpacing(20);
        nullSlider.setMinorTickSpacing(10);
        nullSlider.setPaintTicks(true);
        nullSlider.setPaintLabels(true);
        nullSlider.setValue(0);
        GridLayout gridLayout = new GridLayout(0, 2);
        mainPanel.setLayout(gridLayout);
        mainPanel.add(new JLabel("Unique:"));
        mainPanel.add(uniqueCheckBox);
        mainPanel.add(new JLabel("Null ratio %:"));
        mainPanel.add(nullSlider);
        fileLocationField = new JTextField("Path to file with each value put in new line", 5);
        valuesTextArea = new JTextArea("Type here comma separated values", 4, 20);
        fileSelectionRadio = new JRadioButton("Select file for getting values");
        areaSelectionRadio = new JRadioButton("Input values directly");
        JPanel valuesPanel1 = new JPanel();
        SpringLayout springLayout = new SpringLayout();
        valuesPanel1.setLayout(springLayout);
        final JLabel fileLabel = new JLabel("File:");
        final JButton browseButton = new JButton("Browse");
        valuesPanel1.add(fileLabel);
        valuesPanel1.add(browseButton);
        valuesPanel1.add(fileLocationField);
        valuesPanel1.setMinimumSize(new Dimension(400, 35));
        valuesPanel1.setMaximumSize(new Dimension(600, 35));
        valuesPanel1.setPreferredSize(new Dimension(500, 35));
        springLayout.putConstraint(SpringLayout.NORTH, fileLocationField, 5, SpringLayout.NORTH, valuesPanel1);
        springLayout.putConstraint(SpringLayout.SOUTH, fileLocationField, -2, SpringLayout.SOUTH, valuesPanel1);
        springLayout.putConstraint(SpringLayout.WEST, fileLocationField, 50, SpringLayout.WEST, valuesPanel1);
        springLayout.putConstraint(SpringLayout.EAST, fileLocationField, -100, SpringLayout.EAST, valuesPanel1);
        springLayout.putConstraint(SpringLayout.NORTH, fileLabel, 5, SpringLayout.NORTH, valuesPanel1);
        springLayout.putConstraint(SpringLayout.WEST, fileLabel, 1, SpringLayout.WEST, valuesPanel1);
        springLayout.putConstraint(SpringLayout.EAST, fileLabel, -10, SpringLayout.WEST, fileLocationField);
        springLayout.putConstraint(SpringLayout.NORTH, browseButton, 5, SpringLayout.NORTH, valuesPanel1);
        springLayout.putConstraint(SpringLayout.WEST, browseButton, 210, SpringLayout.WEST, valuesPanel1);
        springLayout.putConstraint(SpringLayout.WEST, browseButton, 5, SpringLayout.EAST, fileLocationField);
        springLayout.putConstraint(SpringLayout.EAST, browseButton, -1, SpringLayout.EAST, valuesPanel1);
        JPanel valuesPanel2 = new JPanel();
        SpringLayout springLayout2 = new SpringLayout();
        valuesPanel2.setLayout(springLayout2);
        final JLabel valuesLabel = new JLabel("Values:");
        valuesPanel2.add(valuesLabel);
        JScrollPane areaScrollPane = new JScrollPane(valuesTextArea);
        valuesPanel2.add(areaScrollPane);
        valuesTextArea.setWrapStyleWord(true);
        valuesTextArea.setLineWrap(true);
        valuesPanel2.setMinimumSize(new Dimension(400, 105));
        valuesPanel2.setMaximumSize(new Dimension(600, 105));
        valuesPanel2.setPreferredSize(new Dimension(500, 105));
        springLayout2.putConstraint(SpringLayout.NORTH, areaScrollPane, 5, SpringLayout.NORTH, valuesPanel2);
        springLayout2.putConstraint(SpringLayout.SOUTH, areaScrollPane, 1, SpringLayout.SOUTH, valuesPanel2);
        springLayout2.putConstraint(SpringLayout.WEST, areaScrollPane, 1, SpringLayout.WEST, valuesPanel2);
        springLayout2.putConstraint(SpringLayout.EAST, areaScrollPane, 1, SpringLayout.EAST, valuesPanel2);
        springLayout2.putConstraint(SpringLayout.NORTH, valuesLabel, 5, SpringLayout.NORTH, valuesPanel2);
        springLayout2.putConstraint(SpringLayout.WEST, valuesLabel, 1, SpringLayout.WEST, valuesPanel2);
        springLayout2.putConstraint(SpringLayout.WEST, areaScrollPane, 50, SpringLayout.WEST, valuesLabel);
        valuesPanel2.setOpaque(true);
        fileSelectionRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valuesPanel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaSelectionRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        valuesPanel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        ButtonGroup bg = new ButtonGroup();
        bg.add(fileSelectionRadio);
        bg.add(areaSelectionRadio);
        fileSelectionRadio.setSelected(true);
        valuesTextArea.setEnabled(false);
        configPanel.add(mainPanel);
        configPanel.add(fileSelectionRadio);
        configPanel.add(valuesPanel1);
        configPanel.add(areaSelectionRadio);
        configPanel.add(valuesPanel2);
        final JPanel parentPanel = configPanel;
        browseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int retVal = fc.showOpenDialog(parentPanel);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String fileName = file.getAbsolutePath();
                    fileLocationField.setText(fileName);
                }
            }
        });
        fileSelectionRadio.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                boolean selected = fileSelectionRadio.isSelected();
                if (selected) {
                    fileLocationField.setEnabled(true);
                    valuesTextArea.setEnabled(false);
                    valuesLabel.setEnabled(false);
                    fileLabel.setEnabled(true);
                    browseButton.setEnabled(true);
                }
            }
        });
        areaSelectionRadio.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                boolean selected = areaSelectionRadio.isSelected();
                if (selected) {
                    fileLocationField.setEnabled(false);
                    valuesTextArea.setEnabled(true);
                    valuesLabel.setEnabled(true);
                    fileLabel.setEnabled(false);
                    browseButton.setEnabled(false);
                }
            }
        });
    }

    public String[] generate(int numberOfRecords) {
        String[] generatedNames = new String[numberOfRecords];
        int[] nullIndexes = PluginUtils.getNullIndexes(numberOfRecords, nullRatio);
        if (unique) {
            if (numberOfRecords > namesHolder.size()) {
                throw new UniqueDataGeneratingException("Cannot generate " + numberOfRecords + " of unique records\n for Dictionary Values Plugin");
            }
            HashMap<String, Boolean> map = new LinkedHashMap<String, Boolean>();
            Random rand = new Random();
            for (int i = 0; i < numberOfRecords; i++) {
                String name;
                do {
                    name = namesHolder.get(rand.nextInt(namesHolder.size()));
                } while (map.containsKey(name));
                map.put(name, true);
            }
            Set<String> set = map.keySet();
            generatedNames = set.toArray(generatedNames);
        } else {
            Random rand = new Random();
            for (int i = 0; i < numberOfRecords; i++) {
                generatedNames[i] = namesHolder.get(rand.nextInt(namesHolder.size()));
            }
        }
        for (int i = 0; i < nullIndexes.length; i++) {
            generatedNames[nullIndexes[i]] = "NULL";
        }
        return generatedNames;
    }

    public String[] generate(int numberOfRecords, int step) {
        if (numberOfRecords != prevStepNumberOfRecords && step < prevStepNumber) {
            prevStepNumberOfRecords = numberOfRecords;
        }
        String[] generatedNames = new String[numberOfRecords];
        if (unique) {
            if (numberOfRecords > namesHolder.size()) {
                throw new UniqueDataGeneratingException("Cannot generate " + numberOfRecords + " of unique records\n for Dictionary Values Plugin");
            }
            if (numberOfRecords != prevStepNumberOfRecords && step > prevStepNumber) {
                if ((prevStepNumberOfRecords * (step - 1) + numberOfRecords) > namesHolder.size()) {
                    throw new UniqueDataGeneratingException("Cannot generate " + (prevStepNumberOfRecords * (step - 1) + numberOfRecords) + " of unique records\n for Dictionary Values Plugin");
                }
            }
            int sumPart = prevStepNumberOfRecords * (step - 1);
            for (int i = 0; i < numberOfRecords; i++) {
                generatedNames[i] = namesHolder.get(i + sumPart);
            }
        } else {
            generatedNames = generate(numberOfRecords);
        }
        prevStepNumber = step;
        prevStepNumberOfRecords = numberOfRecords;
        return generatedNames;
    }

    public String getAuthor() {
        return "SB";
    }

    public String getName() {
        return NAME;
    }

    public String getVersion() {
        return VERSION;
    }

    public String getAboutInfo() {
        return "Allows to geenrate data from dictionary values\n provided in external" + "file or typped into text area";
    }

    public void readConfigParameters() {
        unique = uniqueCheckBox.isSelected();
        nullRatio = nullSlider.getValue();
        boolean fileSelected = fileSelectionRadio.isSelected();
        if (fileSelected) {
            String fileName = fileLocationField.getText();
            namesHolder.clear();
            readFromFile(fileName);
        } else {
            String valuesString = valuesTextArea.getText();
            String[] values = valuesString.split(",");
            namesHolder.clear();
            namesHolder = Arrays.asList(values);
        }
    }

    private void readFromFile(String file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DictionaryValuesPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                namesHolder.add(line);
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(DictionaryValuesPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JPanel getConfigPanel() {
        return configPanel;
    }

    public void setJarResources(IJarResources jarResources) {
        this.iJarResources = jarResources;
    }

    public IJarResources getJarResources() {
        return iJarResources;
    }
}
