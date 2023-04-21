package preprocessing.automatic.gui;

import preprocessing.automatic.*;
import preprocessing.automatic.links.ClassificatorLink;
import preprocessing.automatic.mutation.AdvancedMutator;
import preprocessing.automatic.mutation.Mutator;
import preprocessing.automatic.selection.Selector;
import preprocessing.automatic.selection.SequentialSelector;
import preprocessing.automatic.selection.TournamentSelector;
import preprocessing.methods.BasePreprocessor;
import preprocessing.PreprocessingMethodsList;
import preprocessing.storage.SimplePreprocessingStorage;
import game.weka.core.FastVector;
import game.utils.CrossRoad;
import game.utils.DebugInfo;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

/**
 * Class representing dialog with configuration for AutoPreprocessDialog.
 *
 * @author Miloslav Pavlicek
 */
class ConfigureDialog extends JDialog implements ActionListener, WindowListener {

    private JSlider elitismSlider;

    private JPanel generationPanel;

    private JSlider generationSlider;

    private JCheckBox fixedGenerationsCheckBox;

    private JSlider computeNetworkRoundsSlider;

    private JCheckBox forceFitnessRecomputationCheckBox;

    private JSlider popsizeSlider;

    private JPanel chromosomeChooserPanel;

    private JSlider chromosomeChooserSlider;

    private JSlider initPreprocessorProbSlider;

    private JRadioButton chromosomeRandomRadioButton;

    private JRadioButton chromosomeCustomRadioButton;

    private JButton chromosomeFillButton;

    private JButton chromosomeSaveButton;

    private JScrollPane chromosomeTreeScrollPane;

    private JSlider tournamentSlider;

    private JSlider addPreprocessorSlider;

    private JSlider removePreprocessorSlider;

    private JSlider swapPreprocessorsSlider;

    private JSlider sequenceDisableFlagSlider;

    private JSlider configMutationSlider;

    private JSlider maxSequenceLengthSlider;

    private JRadioButton tournamentSelectionRadioButton;

    private JRadioButton sequentialSelectionRadioButton;

    private JCheckBox discardGenerationsCheckBox;

    private JCheckBox showBestChromosomeCheckBox;

    private JList globalList;

    private JList localList;

    private JComboBox methodChooser;

    private JTextField dumpDirectoryText;

    private JButton dumpDirectoryButton;

    private JCheckBox dumpChromosomeStatistic;

    private JCheckBox dumpModelOutputBox;

    private JCheckBox dumpBestModelOutputBox;

    private JCheckBox dumpAlsoModelsBox;

    private JCheckBox dumpModelsInXMLBox;

    private JCheckBox dumpBestChromosomesBox;

    private JCheckBox dumpAllChromosomesBox;

    private JCheckBox dumpChomosomesInLastGenerationBox;

    private final ConfigureDialog configureDialog;

    private final AutoPreprocessDialog autoPreprocessDialog;

    private final FastVector preprocessors;

    private final PreprocessorAbbreviationMap preprocessorAbbreviationMap;

    private final ChromosomeLoader chromosomeLoader;

    private DataSplit<SimplePreprocessingStorage> dataStoreSplit;

    private final boolean[] isChromosomeCustom;

    private final ChromosomeTree[] chromosomeTrees;

    private GeneticConfig config;

    /**
     * Creates new configuration dialog for setting parameters for genetic computation.
     *
     * @param autoPreprocessDialog        auto preprocessing dialog owning this dialog.
     * @param preprocessors               list of available preprocessors for selection.
     * @param preprocessorAbbreviationMap map of preprocessor name to its abbreviation, used when displaying hints.
     * @param chromosomeLoader            unit loading and saving chromosomes to files.
     * @param treeDataSplit               DataSplit containing input training/testing data.
     */
    public ConfigureDialog(final AutoPreprocessDialog autoPreprocessDialog, FastVector preprocessors, PreprocessorAbbreviationMap preprocessorAbbreviationMap, ChromosomeLoader chromosomeLoader, DataSplit<SimplePreprocessingStorage> treeDataSplit) {
        configureDialog = this;
        this.autoPreprocessDialog = autoPreprocessDialog;
        this.preprocessors = preprocessors;
        this.preprocessorAbbreviationMap = preprocessorAbbreviationMap;
        this.chromosomeLoader = chromosomeLoader;
        this.dataStoreSplit = treeDataSplit;
        config = GeneticConfig.getInstance();
        isChromosomeCustom = new boolean[1000];
        chromosomeTrees = new ChromosomeTree[1000];
        initComponents();
        loadConfig();
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Configuration");
        setSize(500, getHeight());
        setResizable(false);
        addWindowListener(this);
    }

    /**
     * Initializes GUI components.
     */
    private void initComponents() {
        final int MARGIN_SIZE = 20;
        JPanel contentPane = new JPanel();
        JButton closeButton = new JButton();
        JButton saveButton = new JButton();
        elitismSlider = new JSlider();
        generationPanel = new JPanel();
        generationSlider = new JSlider();
        fixedGenerationsCheckBox = new JCheckBox();
        computeNetworkRoundsSlider = new JSlider();
        forceFitnessRecomputationCheckBox = new JCheckBox();
        popsizeSlider = new JSlider();
        chromosomeChooserPanel = new JPanel();
        chromosomeChooserSlider = new JSlider();
        initPreprocessorProbSlider = new JSlider();
        chromosomeRandomRadioButton = new JRadioButton();
        chromosomeCustomRadioButton = new JRadioButton();
        chromosomeFillButton = new JButton();
        chromosomeSaveButton = new JButton();
        JButton chromosomeLoadButton = new JButton();
        chromosomeTreeScrollPane = new JScrollPane();
        tournamentSlider = new JSlider();
        addPreprocessorSlider = new JSlider();
        removePreprocessorSlider = new JSlider();
        swapPreprocessorsSlider = new JSlider();
        sequenceDisableFlagSlider = new JSlider();
        configMutationSlider = new JSlider();
        maxSequenceLengthSlider = new JSlider();
        tournamentSelectionRadioButton = new JRadioButton();
        sequentialSelectionRadioButton = new JRadioButton();
        globalList = new JList();
        localList = new JList();
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                configureDialog.setVisible(false);
            }
        });
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                saveConfig();
            }
        });
        generationSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                generationPanel.setBorder(BorderFactory.createTitledBorder("Generation count: " + generationSlider.getValue()));
            }
        });
        elitismSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                elitismSlider.setBorder(BorderFactory.createTitledBorder("Elitism size: " + elitismSlider.getValue()));
            }
        });
        computeNetworkRoundsSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                computeNetworkRoundsSlider.setBorder(BorderFactory.createTitledBorder("Rounds to pick best fitness from: " + computeNetworkRoundsSlider.getValue()));
            }
        });
        popsizeSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                int popsizeValue = popsizeSlider.getValue();
                popsizeSlider.setBorder(BorderFactory.createTitledBorder("Population size: " + popsizeValue));
                elitismSlider.setMaximum(popsizeValue);
                chromosomeChooserSlider.setMaximum(popsizeValue);
                tournamentSlider.setMaximum(popsizeValue);
                elitismSlider.setLabelTable(elitismSlider.createStandardLabels(popsizeValue));
                if (popsizeValue > 1) {
                    chromosomeChooserSlider.setLabelTable(chromosomeChooserSlider.createStandardLabels(popsizeValue - 1));
                    tournamentSlider.setLabelTable(tournamentSlider.createStandardLabels(popsizeValue - 1));
                }
            }
        });
        initPreprocessorProbSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                initPreprocessorProbSlider.setBorder(BorderFactory.createTitledBorder("Probability of creating preprocessor in new random chromosome sequence: " + initPreprocessorProbSlider.getValue() + "%"));
            }
        });
        chromosomeChooserSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                chromosomeChooserPanel.setBorder(BorderFactory.createTitledBorder("Chromosome " + chromosomeChooserSlider.getValue() + " of initial population"));
                int chromosomeIdx = chromosomeChooserSlider.getValue() - 1;
                chromosomeTreeScrollPane.setVisible(false);
                if (isChromosomeCustom[chromosomeIdx]) {
                    chromosomeFillButton.setEnabled(true);
                    chromosomeSaveButton.setEnabled(true);
                    chromosomeCustomRadioButton.setSelected(true);
                    chromosomeTreeScrollPane.setViewportView(chromosomeTrees[chromosomeIdx].getTree());
                    chromosomeTreeScrollPane.setVisible(true);
                } else {
                    chromosomeRandomRadioButton.setSelected(true);
                    chromosomeFillButton.setEnabled(false);
                    chromosomeSaveButton.setEnabled(false);
                }
            }
        });
        chromosomeRandomRadioButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                chromosomeFillButton.setEnabled(false);
                chromosomeSaveButton.setEnabled(false);
                int chromosomeIdx = chromosomeChooserSlider.getValue() - 1;
                isChromosomeCustom[chromosomeIdx] = false;
                if (chromosomeTrees[chromosomeIdx] != null) {
                    chromosomeTreeScrollPane.setVisible(false);
                }
            }
        });
        chromosomeCustomRadioButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                chromosomeFillButton.setEnabled(true);
                chromosomeSaveButton.setEnabled(true);
                int chromosomeIdx = chromosomeChooserSlider.getValue() - 1;
                isChromosomeCustom[chromosomeIdx] = true;
                if (chromosomeTrees[chromosomeIdx] == null) {
                    Chromosome chromosome = new Chromosome(0.0, false, 1, chromosomeIdx, getSelectedPreprocessors(), dataStoreSplit.trainingData(), preprocessorAbbreviationMap);
                    chromosomeTrees[chromosomeIdx] = new ChromosomeTree("", chromosome, true);
                }
                chromosomeTreeScrollPane.setViewportView(chromosomeTrees[chromosomeIdx].getTree());
                chromosomeTreeScrollPane.setVisible(true);
            }
        });
        chromosomeFillButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int chromosomeIdx = chromosomeChooserSlider.getValue() - 1;
                for (int i = 0; i < chromosomeChooserSlider.getMaximum(); i++) {
                    if (!isChromosomeCustom[i]) {
                        try {
                            Chromosome chromosomeClone = (Chromosome) chromosomeTrees[chromosomeIdx].getChromosome().clone();
                            if (chromosomeTrees[i] == null) {
                                chromosomeTrees[i] = new ChromosomeTree("", chromosomeClone, true);
                            } else {
                                chromosomeTrees[i].setChromosome(chromosomeClone);
                            }
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        isChromosomeCustom[i] = true;
                    }
                }
            }
        });
        chromosomeSaveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int chromosomeIdx = chromosomeChooserSlider.getValue() - 1;
                chromosomeLoader.saveChromosome(configureDialog, chromosomeTrees[chromosomeIdx].getChromosome());
            }
        });
        chromosomeLoadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int chromosomeIdx = chromosomeChooserSlider.getValue() - 1;
                Chromosome chromosome = chromosomeLoader.loadChromosomeAndCheckCompatibility(configureDialog, dataStoreSplit.trainingData(), getSelectedPreprocessors());
                if (chromosome == null) {
                    return;
                }
                if (chromosomeTrees[chromosomeIdx] == null) {
                    chromosomeTrees[chromosomeIdx] = new ChromosomeTree("", chromosome, true);
                } else {
                    chromosomeTrees[chromosomeIdx].setChromosome(chromosome);
                }
                isChromosomeCustom[chromosomeIdx] = true;
                chromosomeTreeScrollPane.setViewportView(chromosomeTrees[chromosomeIdx].getTree());
                chromosomeTreeScrollPane.setVisible(true);
                chromosomeCustomRadioButton.setSelected(true);
                chromosomeFillButton.setEnabled(true);
                chromosomeSaveButton.setEnabled(true);
            }
        });
        tournamentSelectionRadioButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                tournamentSlider.setEnabled(true);
            }
        });
        sequentialSelectionRadioButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                tournamentSlider.setEnabled(false);
            }
        });
        tournamentSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                tournamentSlider.setBorder(BorderFactory.createTitledBorder("Tournament size: " + tournamentSlider.getValue()));
            }
        });
        addPreprocessorSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                addPreprocessorSlider.setBorder(BorderFactory.createTitledBorder("Probability of adding new preprocessor to sequence: " + addPreprocessorSlider.getValue() + "%"));
                recomputeMutationSliderMax();
            }
        });
        removePreprocessorSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                removePreprocessorSlider.setBorder(BorderFactory.createTitledBorder("Probability of removing preprocessor from sequence: " + removePreprocessorSlider.getValue() + "%"));
                recomputeMutationSliderMax();
            }
        });
        swapPreprocessorsSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                swapPreprocessorsSlider.setBorder(BorderFactory.createTitledBorder("Probability of swapping two preprocessors in sequence: " + swapPreprocessorsSlider.getValue() + "%"));
                recomputeMutationSliderMax();
            }
        });
        sequenceDisableFlagSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                sequenceDisableFlagSlider.setBorder(BorderFactory.createTitledBorder("Probability of preprocessor sequence disable flag mutation: " + sequenceDisableFlagSlider.getValue() + "%"));
            }
        });
        configMutationSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                configMutationSlider.setBorder(BorderFactory.createTitledBorder("Probability of configuration parameter mutation: " + configMutationSlider.getValue() + "%"));
            }
        });
        maxSequenceLengthSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event) {
                maxSequenceLengthSlider.setBorder(BorderFactory.createTitledBorder("Maximum number of preprocessors in sequence: " + maxSequenceLengthSlider.getValue()));
            }
        });
        int global = 0;
        int local = 0;
        for (int i = 0; i < preprocessors.size(); i++) {
            BasePreprocessor p = (BasePreprocessor) preprocessors.elementAt(i);
            if (p.getType() == BasePreprocessor.Type.GLOBAL) {
                global++;
            }
            if (p.getType() == BasePreprocessor.Type.LOCAL) {
                local++;
            }
        }
        BasePreprocessor[] localNames = new BasePreprocessor[local];
        BasePreprocessor[] globalNames = new BasePreprocessor[global];
        global = 0;
        local = 0;
        for (int i = 0; i < preprocessors.size(); i++) {
            BasePreprocessor p = (BasePreprocessor) preprocessors.elementAt(i);
            if (p.getType() == BasePreprocessor.Type.GLOBAL) {
                globalNames[global] = p;
                global++;
            }
            if (p.getType() == BasePreprocessor.Type.LOCAL) {
                localNames[local] = p;
                local++;
            }
        }
        MethodListRenderer mlr = new MethodListRenderer();
        globalList.setListData(globalNames);
        globalList.setSelectionInterval(0, globalNames.length - 1);
        globalList.setCellRenderer(mlr);
        localList.setListData(localNames);
        localList.setSelectionInterval(0, localNames.length - 1);
        localList.setCellRenderer(mlr);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        JTabbedPane rootTabbedPane = new JTabbedPane();
        contentPane.add(rootTabbedPane);
        contentPane.add(Box.createRigidArea(new Dimension(0, 3)));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createRigidArea(new Dimension(3, 0)));
        saveButton.setText("Save");
        saveButton.setToolTipText("Save configuration, except initial population and selected preprocessors.");
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalGlue());
        closeButton.setText("Close");
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createHorizontalGlue());
        contentPane.add(buttonPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 3)));
        getRootPane().setDefaultButton(closeButton);
        JPanel geneticSetupPanel = new JPanel();
        geneticSetupPanel.setLayout(new GridLayout(1, 1));
        rootTabbedPane.addTab("Genetic algorithm", null, geneticSetupPanel, "Configure genetic algorithm parameters");
        JTabbedPane geneticPanel = new JTabbedPane();
        geneticSetupPanel.add(geneticPanel);
        JPanel generalGeneticInnerPanel = new JPanel();
        generalGeneticInnerPanel.setAlignmentY(TOP_ALIGNMENT);
        JPanel generalGeneticOuterPanel = new JPanel();
        generalGeneticOuterPanel.setLayout(new BoxLayout(generalGeneticOuterPanel, BoxLayout.X_AXIS));
        generalGeneticOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        generalGeneticOuterPanel.add(generalGeneticInnerPanel);
        generalGeneticOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        generalGeneticInnerPanel.setLayout(new BoxLayout(generalGeneticInnerPanel, BoxLayout.Y_AXIS));
        geneticPanel.addTab("General", generalGeneticOuterPanel);
        generationPanel.setLayout(new BoxLayout(generationPanel, BoxLayout.X_AXIS));
        generationSlider.setMajorTickSpacing(499);
        generationSlider.setMaximum(500);
        generationSlider.setMinimum(1);
        generationSlider.setPaintLabels(true);
        generationSlider.setValue(100);
        generationPanel.add(generationSlider);
        fixedGenerationsCheckBox.setSelected(true);
        fixedGenerationsCheckBox.setText("Fixed");
        fixedGenerationsCheckBox.setToolTipText("Fixed or counted from last time best individual was found");
        generationPanel.add(fixedGenerationsCheckBox);
        generationPanel.setAlignmentX(LEFT_ALIGNMENT);
        generalGeneticInnerPanel.add(generationPanel);
        generalGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        elitismSlider.setPaintLabels(true);
        elitismSlider.setValue(2);
        elitismSlider.setAlignmentX(LEFT_ALIGNMENT);
        generalGeneticInnerPanel.add(elitismSlider);
        generalGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        computeNetworkRoundsSlider.setToolTipText("Compute fitness several times to suppresses oscillation at the expense of time complexity");
        computeNetworkRoundsSlider.setMaximum(10);
        computeNetworkRoundsSlider.setMajorTickSpacing(9);
        computeNetworkRoundsSlider.setMinimum(1);
        computeNetworkRoundsSlider.setMinorTickSpacing(1);
        computeNetworkRoundsSlider.setPaintTicks(true);
        computeNetworkRoundsSlider.setSnapToTicks(true);
        computeNetworkRoundsSlider.setPaintLabels(true);
        computeNetworkRoundsSlider.setValue(3);
        computeNetworkRoundsSlider.setAlignmentX(LEFT_ALIGNMENT);
        generalGeneticInnerPanel.add(computeNetworkRoundsSlider);
        generalGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        forceFitnessRecomputationCheckBox.setText("Force recomputation of fitness for each chromosome");
        forceFitnessRecomputationCheckBox.setToolTipText("Fitness will be recomputed even if contents of chromosome remains unchanged");
        forceFitnessRecomputationCheckBox.setAlignmentX(LEFT_ALIGNMENT);
        generalGeneticInnerPanel.add(forceFitnessRecomputationCheckBox);
        JPanel populationGeneticInnerPanel = new JPanel();
        populationGeneticInnerPanel.setAlignmentY(TOP_ALIGNMENT);
        JPanel populationGeneticOuterPanel = new JPanel();
        populationGeneticOuterPanel.setLayout(new BoxLayout(populationGeneticOuterPanel, BoxLayout.X_AXIS));
        populationGeneticOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        populationGeneticOuterPanel.add(populationGeneticInnerPanel);
        populationGeneticOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        populationGeneticInnerPanel.setLayout(new BoxLayout(populationGeneticInnerPanel, BoxLayout.Y_AXIS));
        geneticPanel.addTab("Population", populationGeneticOuterPanel);
        popsizeSlider.setMajorTickSpacing(chromosomeTrees.length - 1);
        popsizeSlider.setMaximum(chromosomeTrees.length);
        popsizeSlider.setMinimum(1);
        popsizeSlider.setValue(100);
        popsizeSlider.setPaintLabels(true);
        popsizeSlider.setAlignmentX(LEFT_ALIGNMENT);
        populationGeneticInnerPanel.add(popsizeSlider);
        populationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        initPreprocessorProbSlider.setValue(20);
        initPreprocessorProbSlider.setAlignmentX(LEFT_ALIGNMENT);
        populationGeneticInnerPanel.add(initPreprocessorProbSlider);
        populationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        chromosomeChooserPanel.setLayout(new BoxLayout(chromosomeChooserPanel, BoxLayout.X_AXIS));
        JPanel chromosomeChooserInnerPanel = new JPanel();
        chromosomeChooserInnerPanel.setAlignmentY(TOP_ALIGNMENT);
        chromosomeChooserInnerPanel.setLayout(new BoxLayout(chromosomeChooserInnerPanel, BoxLayout.Y_AXIS));
        chromosomeChooserPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        chromosomeChooserPanel.add(chromosomeChooserInnerPanel);
        chromosomeChooserPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        chromosomeChooserSlider.setPaintLabels(true);
        chromosomeChooserSlider.setMinimum(1);
        chromosomeChooserSlider.setValue(1);
        chromosomeChooserSlider.setAlignmentX(LEFT_ALIGNMENT);
        chromosomeChooserInnerPanel.add(chromosomeChooserSlider);
        chromosomeChooserInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        chromosomeRandomRadioButton.setText("Random");
        chromosomeCustomRadioButton.setText("Custom");
        ButtonGroup chromosomeContentsButtonGroup = new ButtonGroup();
        chromosomeContentsButtonGroup.add(chromosomeRandomRadioButton);
        chromosomeContentsButtonGroup.add(chromosomeCustomRadioButton);
        chromosomeRandomRadioButton.setSelected(true);
        chromosomeRandomRadioButton.setMnemonic('R');
        chromosomeRandomRadioButton.setDisplayedMnemonicIndex(0);
        chromosomeRandomRadioButton.setAlignmentX(LEFT_ALIGNMENT);
        chromosomeChooserInnerPanel.add(chromosomeRandomRadioButton);
        JPanel chromosomeCustomPanel = new JPanel();
        chromosomeCustomPanel.setLayout(new BoxLayout(chromosomeCustomPanel, BoxLayout.X_AXIS));
        chromosomeCustomRadioButton.setMnemonic('u');
        chromosomeCustomRadioButton.setDisplayedMnemonicIndex(1);
        chromosomeCustomRadioButton.setAlignmentX(LEFT_ALIGNMENT);
        chromosomeCustomPanel.add(chromosomeCustomRadioButton);
        chromosomeCustomPanel.add(Box.createHorizontalGlue());
        chromosomeFillButton.setText("Fill");
        chromosomeFillButton.setToolTipText("Fills all random chromosomes in population with this one");
        chromosomeFillButton.setEnabled(false);
        chromosomeCustomPanel.add(chromosomeFillButton);
        chromosomeCustomPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        chromosomeSaveButton.setText("Save");
        chromosomeSaveButton.setToolTipText("Saves this chromosome to file");
        chromosomeSaveButton.setEnabled(false);
        chromosomeCustomPanel.add(chromosomeSaveButton);
        chromosomeCustomPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        chromosomeLoadButton.setText("Load");
        chromosomeLoadButton.setToolTipText("Loads chromosome from file to this position");
        chromosomeCustomPanel.add(chromosomeLoadButton);
        chromosomeCustomPanel.setAlignmentX(LEFT_ALIGNMENT);
        chromosomeChooserInnerPanel.add(chromosomeCustomPanel);
        JPanel chromosomeTreePanel = new JPanel();
        chromosomeTreePanel.setLayout(new GridLayout(1, 1));
        chromosomeTreePanel.setMinimumSize(new Dimension(0, 200));
        chromosomeTreePanel.setPreferredSize(chromosomeTreePanel.getMinimumSize());
        chromosomeTreePanel.setAlignmentX(LEFT_ALIGNMENT);
        chromosomeTreePanel.add(chromosomeTreeScrollPane);
        chromosomeChooserInnerPanel.add(chromosomeTreePanel);
        chromosomeChooserPanel.setAlignmentX(LEFT_ALIGNMENT);
        populationGeneticInnerPanel.add(chromosomeChooserPanel);
        populationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        populationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        JPanel selectionGeneticInnerPanel = new JPanel();
        selectionGeneticInnerPanel.setAlignmentY(TOP_ALIGNMENT);
        JPanel selectionGeneticOuterPanel = new JPanel();
        selectionGeneticOuterPanel.setLayout(new BoxLayout(selectionGeneticOuterPanel, BoxLayout.X_AXIS));
        selectionGeneticOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        selectionGeneticOuterPanel.add(selectionGeneticInnerPanel);
        selectionGeneticOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        selectionGeneticInnerPanel.setLayout(new BoxLayout(selectionGeneticInnerPanel, BoxLayout.Y_AXIS));
        geneticPanel.addTab("Selection", selectionGeneticOuterPanel);
        ButtonGroup selectionButtonGroup = new ButtonGroup();
        selectionButtonGroup.add(tournamentSelectionRadioButton);
        selectionButtonGroup.add(sequentialSelectionRadioButton);
        tournamentSelectionRadioButton.setSelected(true);
        tournamentSelectionRadioButton.setText("Tournament selection");
        tournamentSelectionRadioButton.setMnemonic('T');
        tournamentSelectionRadioButton.setDisplayedMnemonicIndex(0);
        tournamentSelectionRadioButton.setAlignmentX(LEFT_ALIGNMENT);
        selectionGeneticInnerPanel.add(tournamentSelectionRadioButton);
        tournamentSlider.setPaintLabels(true);
        tournamentSlider.setMinimum(1);
        tournamentSlider.setValue(4);
        tournamentSlider.setAlignmentX(LEFT_ALIGNMENT);
        selectionGeneticInnerPanel.add(tournamentSlider);
        sequentialSelectionRadioButton.setText("Sequential selection");
        sequentialSelectionRadioButton.setMnemonic('S');
        sequentialSelectionRadioButton.setDisplayedMnemonicIndex(0);
        sequentialSelectionRadioButton.setToolTipText("All individuals are put in new generation from old one in sequence ");
        sequentialSelectionRadioButton.setAlignmentX(LEFT_ALIGNMENT);
        selectionGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        selectionGeneticInnerPanel.add(sequentialSelectionRadioButton);
        selectionGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        JPanel mutationGeneticInnerPanel = new JPanel();
        mutationGeneticInnerPanel.setAlignmentY(TOP_ALIGNMENT);
        JPanel mutationGeneticOuterPanel = new JPanel();
        mutationGeneticOuterPanel.setLayout(new BoxLayout(mutationGeneticOuterPanel, BoxLayout.X_AXIS));
        mutationGeneticOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        mutationGeneticOuterPanel.add(mutationGeneticInnerPanel);
        mutationGeneticOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        mutationGeneticInnerPanel.setLayout(new BoxLayout(mutationGeneticInnerPanel, BoxLayout.Y_AXIS));
        geneticPanel.addTab("Mutation", mutationGeneticOuterPanel);
        removePreprocessorSlider.setValue(0);
        swapPreprocessorsSlider.setValue(0);
        addPreprocessorSlider.setValue(23);
        mutationGeneticInnerPanel.add(addPreprocessorSlider);
        mutationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        removePreprocessorSlider.setValue(17);
        mutationGeneticInnerPanel.add(removePreprocessorSlider);
        mutationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        swapPreprocessorsSlider.setValue(15);
        mutationGeneticInnerPanel.add(swapPreprocessorsSlider);
        mutationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        sequenceDisableFlagSlider.setValue(10);
        mutationGeneticInnerPanel.add(sequenceDisableFlagSlider);
        mutationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        configMutationSlider.setValue(10);
        mutationGeneticInnerPanel.add(configMutationSlider);
        mutationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        maxSequenceLengthSlider.setMajorTickSpacing(9);
        maxSequenceLengthSlider.setMaximum(10);
        maxSequenceLengthSlider.setMinimum(1);
        maxSequenceLengthSlider.setValue(5);
        mutationGeneticInnerPanel.add(maxSequenceLengthSlider);
        mutationGeneticInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        JPanel preprocessorsInnerPanel = new JPanel();
        preprocessorsInnerPanel.setAlignmentY(TOP_ALIGNMENT);
        JPanel preprocessorsOuterPanel = new JPanel();
        preprocessorsOuterPanel.setLayout(new BoxLayout(preprocessorsOuterPanel, BoxLayout.X_AXIS));
        preprocessorsOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        preprocessorsOuterPanel.add(preprocessorsInnerPanel);
        preprocessorsOuterPanel.add(Box.createRigidArea(new Dimension(MARGIN_SIZE, 0)));
        preprocessorsInnerPanel.setLayout(new BoxLayout(preprocessorsInnerPanel, BoxLayout.Y_AXIS));
        rootTabbedPane.addTab("Applicable preprocessors", null, preprocessorsOuterPanel, "Select applicable preprocessors for computation");
        preprocessorsInnerPanel.add(localList);
        localList.setBorder(BorderFactory.createTitledBorder("Local Methods"));
        preprocessorsInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        preprocessorsInnerPanel.add(globalList);
        globalList.setBorder(BorderFactory.createTitledBorder("Global Methods"));
        preprocessorsInnerPanel.add(Box.createRigidArea(new Dimension(0, MARGIN_SIZE)));
        CellConstraints cc = new CellConstraints();
        JPanel miscellaneousPanel = new JPanel();
        miscellaneousPanel.setLayout(new FormLayout("10px,default,10px", "10px,pref:grow,pref:grow,pref:grow,pref,10px"));
        JPanel miscUpperPanel = new JPanel();
        miscUpperPanel.setLayout(new FormLayout("5dlu,left:default:grow,5dlu", "15dlu,4dlu,15dlu"));
        discardGenerationsCheckBox = new JCheckBox();
        discardGenerationsCheckBox.setText("Don't keep computed generations in memory");
        discardGenerationsCheckBox.setToolTipText("Saves memory by not storing generations, however stats won't be available");
        discardGenerationsCheckBox.setAlignmentX(LEFT_ALIGNMENT);
        miscUpperPanel.add(discardGenerationsCheckBox, cc.xy(2, 1));
        showBestChromosomeCheckBox = new JCheckBox();
        showBestChromosomeCheckBox.setText("Show dialog with best chromosome when computation finished");
        showBestChromosomeCheckBox.setToolTipText("Choose whether to display dialog with best chromosome at the end of computation");
        showBestChromosomeCheckBox.setSelected(true);
        showBestChromosomeCheckBox.setAlignmentX(LEFT_ALIGNMENT);
        miscUpperPanel.add(showBestChromosomeCheckBox, cc.xy(2, 3));
        miscellaneousPanel.add(miscUpperPanel, cc.xy(2, 2));
        methodChooser = new JComboBox();
        FastVector lnk = MethodLinksList.getInstance().getLinksList();
        for (int i = 0; i < lnk.size(); i++) {
            ClassificatorLink l = (ClassificatorLink) lnk.elementAt(i);
            methodChooser.addItem(l.getMethodID());
        }
        miscellaneousPanel.add(methodChooser, cc.xy(2, 3));
        JPanel miscLowerPanel = new JPanel();
        miscLowerPanel.setLayout(new FormLayout("left:default:grow", "15dlu,15dlu,15dlu,15dlu,15dlu,15dlu,15dlu,15dlu,15dlu,15dlu"));
        miscellaneousPanel.add(miscLowerPanel, cc.xy(2, 4));
        dumpBestChromosomesBox = new JCheckBox();
        dumpBestChromosomesBox.setSelected(config.isDumpBestChromosomes());
        dumpBestChromosomesBox.setToolTipText("Save best preprocessing method sequences generated by GA");
        dumpBestChromosomesBox.setText("Save best preprocessing method sequences generated by GA");
        dumpBestChromosomesBox.addActionListener(this);
        miscLowerPanel.add(dumpBestChromosomesBox, cc.xy(1, 1));
        dumpChomosomesInLastGenerationBox = new JCheckBox();
        dumpChomosomesInLastGenerationBox.setSelected(config.isDumpChromosomesInLastGeneration());
        dumpChomosomesInLastGenerationBox.setToolTipText("Save all preprocessing method sequences in last generation");
        dumpChomosomesInLastGenerationBox.setText("Save all preprocessing method sequences in last generation");
        dumpChomosomesInLastGenerationBox.addActionListener(this);
        miscLowerPanel.add(dumpChomosomesInLastGenerationBox, cc.xy(1, 2));
        dumpAllChromosomesBox = new JCheckBox();
        dumpAllChromosomesBox.setSelected(config.isDumpChromosomesInLastGeneration());
        dumpAllChromosomesBox.setToolTipText("Save preprocessing method sequences in all generations generated by GA");
        dumpAllChromosomesBox.setText("Save preprocessing method sequences all generations generated by GA");
        dumpAllChromosomesBox.addActionListener(this);
        miscLowerPanel.add(dumpAllChromosomesBox, cc.xy(1, 3));
        dumpChromosomeStatistic = new JCheckBox();
        dumpChromosomeStatistic.setEnabled(true);
        dumpChromosomeStatistic.setSelected(config.isDumpChromosomeStatistics());
        dumpChromosomeStatistic.setText("Save utilization of preprocessing methods in chromosomes in all generations.");
        dumpChromosomeStatistic.setToolTipText("Save utilization of preprocessing methods in chromosomes in all generations.");
        dumpChromosomeStatistic.addActionListener(this);
        miscLowerPanel.add(dumpChromosomeStatistic, cc.xy(1, 5));
        dumpBestModelOutputBox = new JCheckBox();
        dumpBestModelOutputBox.setEnabled(true);
        dumpBestModelOutputBox.setSelected(config.isDumpBestModelOutput());
        dumpBestModelOutputBox.setText("Save text output of best model generated in each generation.");
        dumpBestModelOutputBox.setToolTipText("Save text output of best model generated in each generation.");
        dumpBestModelOutputBox.addActionListener(this);
        miscLowerPanel.add(dumpBestModelOutputBox, cc.xy(1, 6));
        dumpModelOutputBox = new JCheckBox();
        dumpModelOutputBox.setSelected(config.isDumpModelOutput());
        dumpModelOutputBox.setToolTipText("Save text output of all models generated in each generation.");
        dumpModelOutputBox.setText("Save text output of all models generated in each generation.");
        dumpModelOutputBox.addActionListener(this);
        miscLowerPanel.add(dumpModelOutputBox, cc.xy(1, 7));
        dumpAlsoModelsBox = new JCheckBox();
        boolean b = config.isDumpAllChromosomes() || config.isDumpBestChromosomes();
        dumpAlsoModelsBox.setEnabled(b);
        dumpAlsoModelsBox.setSelected(config.isDumpAlsoModels() && b);
        dumpAlsoModelsBox.setToolTipText("Save also serialisation of corresponding models (all or best).");
        dumpAlsoModelsBox.setText("Save also serialisation of corresponding models (all or best).");
        dumpAlsoModelsBox.addActionListener(this);
        miscLowerPanel.add(dumpAlsoModelsBox, cc.xy(1, 9));
        dumpModelsInXMLBox = new JCheckBox();
        b = config.isDumpAllChromosomes() || config.isDumpBestChromosomes();
        dumpModelsInXMLBox.setEnabled(b);
        dumpModelsInXMLBox.setSelected(config.isDumpAlsoModels() && b);
        dumpModelsInXMLBox.setToolTipText("Save also XML serialisation (via XStream) of corresponding models (all or best).");
        dumpModelsInXMLBox.setText("Save also XML serialisation (via XStream) of corresponding models (all or best).");
        dumpModelsInXMLBox.addActionListener(this);
        miscLowerPanel.add(dumpModelsInXMLBox, cc.xy(1, 10));
        JPanel pan = new JPanel(new FormLayout("right:default,5dlu,fill:default:grow,5dlu,left:default", "center:20dlu"));
        pan.add(new JLabel("Directory to save to"), cc.xy(1, 1));
        dumpDirectoryText = new JTextField();
        dumpDirectoryText.setText(config.getDumpDirectory());
        pan.add(dumpDirectoryText, cc.xy(3, 1));
        dumpDirectoryButton = new JButton();
        dumpDirectoryButton.setText("...");
        dumpDirectoryButton.addActionListener(this);
        pan.add(dumpDirectoryButton, cc.xy(5, 1));
        miscellaneousPanel.add(pan, cc.xy(2, 5));
        rootTabbedPane.addTab("Miscellaneous", null, miscellaneousPanel, "Miscellaneous options");
        setContentPane(contentPane);
        pack();
        buttonPanel.add(Box.createRigidArea(new Dimension(saveButton.getWidth() + 3, 0)));
    }

    /**
     * Returns selected preprocessors in array collection.
     *
     * @return selected preprocessors, locals at index 0, globals at 1.
     */
    private ArrayList<BasePreprocessor>[] getSelectedPreprocessors() {
        ArrayList<BasePreprocessor>[] ret = new ArrayList[2];
        ret[0] = new ArrayList<BasePreprocessor>(localList.getSelectedValues().length);
        ret[1] = new ArrayList<BasePreprocessor>(globalList.getSelectedValues().length);
        PreprocessingMethodsList list = CrossRoad.getInstance().getListOfMethods();
        Object[] arr = localList.getSelectedValues();
        for (int i = 0; i < arr.length; i++) {
            BasePreprocessor b = (BasePreprocessor) arr[i];
            ret[0].add(b);
        }
        arr = globalList.getSelectedValues();
        for (int i = 0; i < arr.length; i++) {
            BasePreprocessor b = (BasePreprocessor) arr[i];
            ret[1].add(b);
        }
        return ret;
    }

    /**
     * Recomputes add, remove and swap sliders maximum when changing value of one of them. Sum of all three
     * probabilities is always less or equal to 100.
     */
    private void recomputeMutationSliderMax() {
        int addVal = addPreprocessorSlider.getValue();
        int removeVal = removePreprocessorSlider.getValue();
        int swapVal = swapPreprocessorsSlider.getValue();
        addPreprocessorSlider.setMaximum(100 - removeVal - swapVal);
        removePreprocessorSlider.setMaximum(100 - addVal - swapVal);
        swapPreprocessorsSlider.setMaximum(100 - addVal - removeVal);
    }

    /**
     * Attempts to load some configuration parameters from file. Configurations that are not loaded from file are
     * applicable preprocessors (depends on units available in TreeData) and initial population (depends on selected
     * preprocessors).
     */
    private void loadConfig() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("autopp.cfg"));
            try {
                elitismSlider.setValue(objectInputStream.readInt());
                generationSlider.setValue(objectInputStream.readInt());
                fixedGenerationsCheckBox.setSelected(objectInputStream.readBoolean());
                computeNetworkRoundsSlider.setValue(objectInputStream.readInt());
                forceFitnessRecomputationCheckBox.setSelected(objectInputStream.readBoolean());
                popsizeSlider.setValue(objectInputStream.readInt());
                initPreprocessorProbSlider.setValue(objectInputStream.readInt());
                tournamentSlider.setValue(objectInputStream.readInt());
                addPreprocessorSlider.setValue(objectInputStream.readInt());
                removePreprocessorSlider.setValue(objectInputStream.readInt());
                swapPreprocessorsSlider.setValue(objectInputStream.readInt());
                sequenceDisableFlagSlider.setValue(objectInputStream.readInt());
                configMutationSlider.setValue(objectInputStream.readInt());
                maxSequenceLengthSlider.setValue(objectInputStream.readInt());
                boolean tournamentSelected = objectInputStream.readBoolean();
                tournamentSelectionRadioButton.setSelected(tournamentSelected);
                tournamentSlider.setEnabled(tournamentSelected);
                sequentialSelectionRadioButton.setSelected(!tournamentSelected);
                discardGenerationsCheckBox.setSelected(objectInputStream.readBoolean());
                showBestChromosomeCheckBox.setSelected(objectInputStream.readBoolean());
            } catch (Exception e) {
                System.err.println("Configuration file corrupted!");
                return;
            }
            objectInputStream.close();
        } catch (IOException e) {
            return;
        }
        System.out.println("autopp.cfg configuration loaded");
    }

    /**
     * Saves configuration parameters in file. Configurations that are not saved to file are
     * applicable preprocessors (depends on units available in TreeData) and initial population (depends on selected
     * preprocessors).
     */
    private void saveConfig() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("autopp.cfg"));
            objectOutputStream.writeInt(elitismSlider.getValue());
            objectOutputStream.writeInt(generationSlider.getValue());
            objectOutputStream.writeBoolean(fixedGenerationsCheckBox.isSelected());
            objectOutputStream.writeInt(computeNetworkRoundsSlider.getValue());
            objectOutputStream.writeBoolean(forceFitnessRecomputationCheckBox.isSelected());
            objectOutputStream.writeInt(popsizeSlider.getValue());
            objectOutputStream.writeInt(initPreprocessorProbSlider.getValue());
            objectOutputStream.writeInt(tournamentSlider.getValue());
            objectOutputStream.writeInt(addPreprocessorSlider.getValue());
            objectOutputStream.writeInt(removePreprocessorSlider.getValue());
            objectOutputStream.writeInt(swapPreprocessorsSlider.getValue());
            objectOutputStream.writeInt(sequenceDisableFlagSlider.getValue());
            objectOutputStream.writeInt(configMutationSlider.getValue());
            objectOutputStream.writeInt(maxSequenceLengthSlider.getValue());
            objectOutputStream.writeBoolean(tournamentSelectionRadioButton.isSelected());
            objectOutputStream.writeBoolean(discardGenerationsCheckBox.isSelected());
            objectOutputStream.writeBoolean(showBestChromosomeCheckBox.isSelected());
            objectOutputStream.close();
            JOptionPane.showMessageDialog(this, "Configuration saved to autopp.cfg", "Configuration saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            setLocationRelativeTo(autoPreprocessDialog);
        }
        super.setVisible(visible);
    }

    /**
     * Returns information if generations should be saved for later statistics purposes.
     *
     * @return true if generations should be saved, false if only last two should be kept.
     */
    public boolean keepGenerationHistory() {
        return !discardGenerationsCheckBox.isSelected();
    }

    /**
     * Returns information if dialog with best chromosome should be displayed when computation finished.
     *
     * @return true if dialog should be displayed.
     */
    public boolean showBestChromosomeDialog() {
        return showBestChromosomeCheckBox.isSelected();
    }

    /**
     * Returns genetic config class according to adjusted values in dialog.
     *
     * @return genetic config containing parameters for genetic computation unit.
     */
    public GeneticConfig getGeneticConfig() {
        Selector selector;
        if (tournamentSelectionRadioButton.isSelected()) {
            selector = new TournamentSelector(tournamentSlider.getValue(), popsizeSlider.getValue());
        } else {
            selector = new SequentialSelector(elitismSlider.getValue(), popsizeSlider.getValue());
        }
        Mutator mutator = new AdvancedMutator(addPreprocessorSlider.getValue() / 100.0, removePreprocessorSlider.getValue() / 100.0, swapPreprocessorsSlider.getValue() / 100.0, sequenceDisableFlagSlider.getValue() / 100.0, configMutationSlider.getValue() / 100.0, maxSequenceLengthSlider.getValue());
        ArrayList<Chromosome> initialPopulation = new ArrayList<Chromosome>(popsizeSlider.getValue());
        for (int i = 0; i < popsizeSlider.getValue(); i++) {
            Chromosome chromosome;
            if (isChromosomeCustom[i]) {
                chromosome = chromosomeTrees[i].getChromosome();
                chromosome.setForceFitnessRecomputation(forceFitnessRecomputationCheckBox.isSelected());
                chromosome.setComputeNetworkRounds(computeNetworkRoundsSlider.getValue());
            } else {
                chromosome = new Chromosome(initPreprocessorProbSlider.getValue() / 100.0, forceFitnessRecomputationCheckBox.isSelected(), computeNetworkRoundsSlider.getValue(), i, getSelectedPreprocessors(), dataStoreSplit.trainingData(), preprocessorAbbreviationMap);
            }
            chromosome.setRank(i);
            chromosome.fitnessChanged();
            try {
                initialPopulation.add(i, (Chromosome) chromosome.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        initialPopulation.trimToSize();
        GeneticConfig config = GeneticConfig.getInstance();
        config.setGenerationCount(generationSlider.getValue());
        config.setGenerationCountFixed(fixedGenerationsCheckBox.isSelected());
        config.setElitismSize(elitismSlider.getValue());
        config.setInitialPopulation(initialPopulation);
        config.setMutator(mutator);
        config.setSelector(selector);
        config.setPreprocessors(getSelectedPreprocessors());
        try {
            config.setModelName((String) methodChooser.getSelectedItem());
        } catch (NoSuchMethodException e) {
            DebugInfo.putErrorMessage("Classification method " + ((String) methodChooser.getSelectedItem()) + " was not found :(. ");
            return null;
        }
        return config;
    }

    /**
     * Refreshes all chromosomes in population after selecting or deselecting preprocessors. Sequences of chromosomes
     * are checked for genes that have disabled associated preprocessor, those genes are deleted from sequences.
     * Also popup menu for new preprocessor addition is refreshed.
     */
    private void refreshChromosomes() {
        ArrayList<BasePreprocessor>[] applicablePreprocessors = getSelectedPreprocessors();
        for (int i = 0; i < chromosomeTrees.length; i++) {
            ChromosomeTree chromosomeTree = chromosomeTrees[i];
            if (chromosomeTree != null) {
                Chromosome oldChromosome = chromosomeTree.getChromosome();
                Chromosome newChromosome = new Chromosome(0.0, false, 1, i, getSelectedPreprocessors(), dataStoreSplit.trainingData(), preprocessorAbbreviationMap);
                int oldSequenceIndex = 0;
                int newSequenceIndex = 0;
                while (oldSequenceIndex != oldChromosome.getGeneSequenceCount() && newSequenceIndex != newChromosome.getGeneSequenceCount()) {
                    GeneSequence oldSequence = oldChromosome.getGeneSequenceAt(oldSequenceIndex);
                    GeneSequence newSequence = newChromosome.getGeneSequenceAt(newSequenceIndex);
                    if (oldSequence.isLocal()) {
                        if (newSequence.isLocal()) {
                            newSequence.setDisabled(oldSequence.isDisabled());
                            for (Gene gene : oldSequence) {
                                for (BasePreprocessor basePreprocessor : newSequence.getApplicablePreprocessors()) {
                                    if (gene.getAssociatedPreprocessor().getPreprocessingMethodName().equals(basePreprocessor.getPreprocessingMethodName())) {
                                        newSequence.add(gene);
                                    }
                                }
                            }
                            newSequenceIndex++;
                        }
                        oldSequenceIndex++;
                    } else {
                        if (newSequence.isLocal()) {
                            newSequenceIndex++;
                        } else {
                            newSequence.setDisabled(oldSequence.isDisabled());
                            for (Gene gene : oldSequence) {
                                if (newSequence.getApplicablePreprocessors().contains(gene.getAssociatedPreprocessor())) {
                                    newSequence.add(gene);
                                }
                            }
                            newSequenceIndex++;
                            oldSequenceIndex++;
                        }
                    }
                }
                chromosomeTree.setChromosome(newChromosome, applicablePreprocessors);
            }
        }
    }

    /**
     * Sets new training/testing data loaded by AutoPreprocessDialog. Invalidates initial population if new data
     * are not compatible with current, i.e. input attribute count or names don't match.
     *
     * @param treeDataSplit newly loaded DataSplit containing training/testing data
     */
    public void setTreeDataSplit(DataSplit<SimplePreprocessingStorage> treeDataSplit) {
        for (int i = 0; i < chromosomeTrees.length; i++) {
            isChromosomeCustom[i] = false;
            chromosomeTrees[i] = null;
        }
        chromosomeChooserSlider.setValue(1);
        chromosomeTreeScrollPane.setVisible(false);
        chromosomeRandomRadioButton.setSelected(true);
        chromosomeFillButton.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dumpBestChromosomesBox) {
            config.setDumpBestChromosomes(dumpBestChromosomesBox.isSelected());
            return;
        }
        if (e.getSource() == dumpChomosomesInLastGenerationBox) {
            config.setDumpChromosomesInLastGeneration(dumpChomosomesInLastGenerationBox.isSelected());
            return;
        }
        if (e.getSource() == dumpAllChromosomesBox) {
            config.setDumpAllChromosomes(dumpAllChromosomesBox.isSelected());
            return;
        }
        if (e.getSource() == dumpChromosomeStatistic) {
            config.setDumpChromosomeStatistics(dumpChromosomeStatistic.isSelected());
            return;
        }
        if (e.getSource() == dumpModelOutputBox) {
            config.setDumpModelOutput(dumpModelOutputBox.isSelected());
            if (config.isDumpModelOutput() || config.isDumpBestModelOutput()) {
                dumpAlsoModelsBox.setEnabled(true);
                dumpAlsoModelsBox.setSelected(config.isDumpAlsoModels());
                dumpAlsoModelsBox.setToolTipText("Save also serialisation of corresponding models (all or best).");
                dumpModelsInXMLBox.setEnabled(true);
                dumpModelsInXMLBox.setSelected(config.isDumpModelsInXML());
                dumpModelsInXMLBox.setToolTipText("Save also XML serialisation (via XStream) of corresponding models (all or best).");
            } else {
                config.setDumpAlsoModels(false);
                config.setDumpModelsInXML(false);
                dumpAlsoModelsBox.setEnabled(false);
                dumpAlsoModelsBox.setSelected(config.isDumpAlsoModels());
                dumpAlsoModelsBox.setToolTipText("To enable this option, select \"Save also serialisation of corresponding models (all or best)\" or \"Save also XML serialisation (via XStream) of corresponding models (all or best)\"");
                dumpModelsInXMLBox.setEnabled(false);
                dumpModelsInXMLBox.setSelected(config.isDumpModelsInXML());
                dumpModelsInXMLBox.setToolTipText("To enable this option, select \"Save also serialisation of corresponding models (all or best)\" or \"Save also XML serialisation (via XStream) of corresponding models (all or best)\"");
            }
            return;
        }
        if (e.getSource() == dumpBestModelOutputBox) {
            config.setDumpBestModelOutput(dumpBestModelOutputBox.isSelected());
            if (config.isDumpModelOutput() || config.isDumpBestModelOutput()) {
                dumpAlsoModelsBox.setEnabled(true);
                dumpAlsoModelsBox.setSelected(config.isDumpAlsoModels());
                dumpAlsoModelsBox.setToolTipText("Save also serialisation of corresponding models (all or best).");
                dumpModelsInXMLBox.setEnabled(true);
                dumpModelsInXMLBox.setSelected(config.isDumpModelsInXML());
                dumpModelsInXMLBox.setToolTipText("Save also XML serialisation (via XStream) of corresponding models (all or best).");
            } else {
                config.setDumpAlsoModels(false);
                config.setDumpModelsInXML(false);
                dumpAlsoModelsBox.setEnabled(false);
                dumpAlsoModelsBox.setSelected(config.isDumpAlsoModels());
                dumpAlsoModelsBox.setToolTipText("To enable this option, select \"Save also serialisation of corresponding models (all or best)\" or \"Save also XML serialisation (via XStream) of corresponding models (all or best)\"");
                dumpModelsInXMLBox.setEnabled(false);
                dumpModelsInXMLBox.setSelected(config.isDumpModelsInXML());
                dumpModelsInXMLBox.setToolTipText("To enable this option, select \"Save also serialisation of corresponding models (all or best)\" or \"Save also XML serialisation (via XStream) of corresponding models (all or best)\"");
            }
            return;
        }
        if (e.getSource() == dumpAlsoModelsBox) {
            config.setDumpAlsoModels(dumpAlsoModelsBox.isSelected());
            return;
        }
        if (e.getSource() == dumpModelsInXMLBox) {
            config.setDumpModelsInXML(dumpModelsInXMLBox.isSelected());
            return;
        }
        if (e.getSource() == dumpDirectoryButton) {
            JFileChooser fc = new JFileChooser(new File(dumpDirectoryText.getText()));
            fc.setMultiSelectionEnabled(false);
            fc.setDialogType(JFileChooser.SAVE_DIALOG);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showSaveDialog(dumpDirectoryButton) == JFileChooser.APPROVE_OPTION) {
                dumpDirectoryText.setText(fc.getSelectedFile().getAbsolutePath());
                config.setDumpDirectory(fc.getSelectedFile().getAbsolutePath());
            }
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        config.setDumpDirectory(dumpDirectoryText.getText());
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
