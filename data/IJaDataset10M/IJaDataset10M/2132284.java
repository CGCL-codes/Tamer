package com.rapidminer.gui.report;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.gui.properties.BasicPropertyTable;
import com.rapidminer.gui.renderer.Renderer;
import com.rapidminer.gui.tools.ExtendedJScrollPane;
import com.rapidminer.gui.tools.SwingTools;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.ResultObject;
import com.rapidminer.operator.report.ReportGenerator;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.report.Renderable;
import com.rapidminer.report.ReportException;
import com.rapidminer.report.ReportStream;
import com.rapidminer.report.Reportable;
import com.rapidminer.report.Tableable;
import com.rapidminer.tools.OperatorService;

/** 
 * This class provides a configuration dialog which is used to assemble the 
 * desired report structure from given report elements and to determine its
 * format.
 * 
 * @author Helge Homburg
 */
public class ReportSelectionDialog extends JDialog implements ActionListener, TreeSelectionListener {

    private static final long serialVersionUID = 5963494650911179008L;

    private ReportGenerator currentReportGenerator;

    private boolean showReportItemParameters;

    private JButton btOptions;

    private JButton btReport;

    private JButton btClose;

    private JComboBox formatChooser;

    private JProgressBar progressBar;

    private JLabel progressLabel;

    private String[] reportFormats = { "Excel", "HTML", "PDF", "RTF" };

    private String currentFormat = "PDF";

    private ReportSelectionTreeModel model;

    private PreviewPanel previewArea;

    private Image defaultPreviewImage;

    private Image tablePreviewImage;

    private Image textPreviewImage;

    /** Properties for the renderers. */
    private BasicPropertyTable propertyTable = new BasicPropertyTable();

    /** The currently selected result. */
    private ReportResultAndRenderer currentResultAndRenderer;

    /** The name of the currently selected result. */
    private String currentResultName;

    /** The data set on which we currently work (necessary for the creation of some renderers). */
    private ExampleSet exampleSet;

    public ReportSelectionDialog(Frame mainFrame, ExampleSet exampleSet, boolean showReportItemParameters) {
        super(mainFrame, "Reporting", false);
        this.exampleSet = exampleSet;
        this.showReportItemParameters = showReportItemParameters;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        TreeMap<ReportSelectionTreeNode, TreeSet<ReportSelectionTreeNode>> treeMap = new TreeMap<ReportSelectionTreeNode, TreeSet<ReportSelectionTreeNode>>();
        for (String currentGroup : ReportService.getItemGroups()) {
            TreeSet<ReportSelectionTreeNode> nodes = new TreeSet<ReportSelectionTreeNode>();
            for (String currentItem : ReportService.getItems(currentGroup)) {
                nodes.add(new ReportSelectionTreeNode(currentItem, currentGroup, false));
            }
            treeMap.put(new ReportSelectionTreeNode(currentGroup, false), nodes);
        }
        initPanelComponents(mainFrame, treeMap);
    }

    public ReportSelectionDialog(Frame mainFrame, TreeMap<ReportSelectionTreeNode, TreeSet<ReportSelectionTreeNode>> treeMap) {
        super(mainFrame, "Reporting", false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initPanelComponents(mainFrame, treeMap);
    }

    private void initPanelComponents(Frame mainFrame, TreeMap<ReportSelectionTreeNode, TreeSet<ReportSelectionTreeNode>> treeMap) {
        setLayout(new BorderLayout());
        int width = mainFrame.getWidth() * 5 / 6;
        int height = mainFrame.getHeight() * 5 / 6;
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(width / 4);
        JPanel treePanel = new JPanel();
        BorderLayout treePanelLayout = new BorderLayout();
        treePanel.setLayout(treePanelLayout);
        DefaultMutableTreeNode root;
        root = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode superNode = new DefaultMutableTreeNode(new ReportSelectionTreeNode("All", "root", false));
        root.insert(superNode, 0);
        Set<ReportSelectionTreeNode> keys = treeMap.keySet();
        int groupCounter = 0;
        for (ReportSelectionTreeNode currentGroupNode : keys) {
            TreeSet<ReportSelectionTreeNode> currentItems = treeMap.get(currentGroupNode);
            DefaultMutableTreeNode currentGroupTreeNode = new DefaultMutableTreeNode(currentGroupNode);
            superNode.insert(currentGroupTreeNode, groupCounter);
            groupCounter++;
            int itemCounter = 0;
            for (ReportSelectionTreeNode currentItem : currentItems) {
                DefaultMutableTreeNode currentItemTreeNode = new DefaultMutableTreeNode(currentItem);
                currentGroupTreeNode.insert(currentItemTreeNode, itemCounter);
                itemCounter++;
            }
        }
        model = new ReportSelectionTreeModel(root, false);
        JTree exportSelectionTree = new JTree(model);
        exportSelectionTree.addTreeSelectionListener(this);
        exportSelectionTree.setRootVisible(false);
        exportSelectionTree.expandPath(new TreePath(superNode.getPath()));
        exportSelectionTree.setShowsRootHandles(true);
        ReportSelectionTreeNodeRenderer renderer = new ReportSelectionTreeNodeRenderer();
        exportSelectionTree.setCellRenderer(renderer);
        ReportSelectionTreeNodeEditor editor = new ReportSelectionTreeNodeEditor(exportSelectionTree, this);
        exportSelectionTree.setCellEditor(editor);
        exportSelectionTree.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(exportSelectionTree);
        mainSplitPane.add(scrollPane);
        JPanel treeSupportPanel = new JPanel();
        GridBagLayout treeSupportPanelLayout = new GridBagLayout();
        treeSupportPanel.setLayout(treeSupportPanelLayout);
        GridBagConstraints treeSupportConstraints = new GridBagConstraints();
        treeSupportConstraints.insets = new Insets(4, 4, 4, 4);
        treeSupportConstraints.gridwidth = GridBagConstraints.REMAINDER;
        treeSupportConstraints.weightx = 1.0;
        treeSupportConstraints.weighty = 1.0;
        treeSupportConstraints.fill = GridBagConstraints.BOTH;
        previewArea = new PreviewPanel();
        Border border = BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Preview"), BorderFactory.createEtchedBorder());
        previewArea.setBorder(border);
        try {
            this.defaultPreviewImage = SwingTools.createImage("preview.png").getImage();
            this.tablePreviewImage = SwingTools.createImage("table_preview.png").getImage();
            this.textPreviewImage = SwingTools.createImage("text_preview.png").getImage();
            previewArea.setPreviewImage(defaultPreviewImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (showReportItemParameters) {
            JSplitPane previewParameterSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            previewParameterSplit.add(previewArea);
            JPanel parameterPanel = new JPanel();
            border = BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Properties"), BorderFactory.createEtchedBorder());
            parameterPanel.setBorder(border);
            GridBagLayout parameterLayout = new GridBagLayout();
            parameterPanel.setLayout(parameterLayout);
            GridBagConstraints parameterC = new GridBagConstraints();
            parameterC.fill = GridBagConstraints.BOTH;
            parameterC.insets = new Insets(4, 4, 4, 4);
            parameterC.weightx = 1;
            parameterC.weighty = 1;
            parameterC.gridwidth = GridBagConstraints.REMAINDER;
            JScrollPane propertyPane = new ExtendedJScrollPane(propertyTable);
            parameterLayout.setConstraints(propertyPane, parameterC);
            parameterPanel.add(propertyPane);
            JPanel updateParametersButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton updateParametersButton = new JButton("Update");
            updateParametersButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    updateParameterSettings();
                }
            });
            updateParametersButtonPanel.add(updateParametersButton);
            parameterC.weighty = 0;
            parameterLayout.setConstraints(updateParametersButtonPanel, parameterC);
            parameterPanel.add(updateParametersButtonPanel);
            previewParameterSplit.add(parameterPanel);
            treeSupportPanelLayout.setConstraints(previewParameterSplit, treeSupportConstraints);
            treeSupportPanel.add(previewParameterSplit);
            previewParameterSplit.setDividerLocation((int) (height / 2.0d));
        } else {
            treeSupportPanelLayout.setConstraints(previewArea, treeSupportConstraints);
            treeSupportPanel.add(previewArea);
        }
        JPanel optionsPanel = new JPanel();
        GridBagLayout optionsPanelLayout = new GridBagLayout();
        optionsPanel.setLayout(optionsPanelLayout);
        GridBagConstraints optionsConstraints = new GridBagConstraints();
        optionsConstraints.insets = new Insets(4, 4, 4, 4);
        optionsConstraints.fill = GridBagConstraints.BOTH;
        JLabel formatDescription = new JLabel("Format: ");
        optionsPanelLayout.setConstraints(formatDescription, optionsConstraints);
        optionsPanel.add(formatDescription);
        formatChooser = new JComboBox(reportFormats);
        formatChooser.setSelectedIndex(2);
        formatChooser.addActionListener(this);
        optionsConstraints.gridwidth = GridBagConstraints.RELATIVE;
        optionsPanelLayout.setConstraints(formatChooser, optionsConstraints);
        optionsPanel.add(formatChooser);
        btOptions = new JButton("Options");
        btOptions.addActionListener(this);
        optionsConstraints.gridwidth = GridBagConstraints.REMAINDER;
        optionsPanelLayout.setConstraints(btOptions, optionsConstraints);
        optionsPanel.add(btOptions);
        treeSupportConstraints.weighty = 0.0;
        treeSupportPanelLayout.setConstraints(optionsPanel, treeSupportConstraints);
        treeSupportPanel.add(optionsPanel);
        mainSplitPane.add(treeSupportPanel);
        add(mainSplitPane, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        progressLabel = new JLabel("Progress:");
        progressPanel.add(progressLabel);
        progressLabel.setVisible(false);
        progressBar = new JProgressBar();
        progressPanel.add(progressBar);
        progressBar.setVisible(false);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btReport = new JButton("Create Report...");
        btReport.setEnabled(false);
        btReport.addActionListener(this);
        buttonPanel.add(btReport);
        btClose = new JButton("Close");
        btClose.addActionListener(this);
        buttonPanel.add(btClose);
        controlPanel.add(progressPanel, BorderLayout.WEST);
        controlPanel.add(buttonPanel, BorderLayout.EAST);
        updateOptionsButton();
        add(controlPanel, BorderLayout.SOUTH);
        setSize(width, height);
        setLocationRelativeTo(mainFrame);
    }

    private void createReport(LinkedList<ReportSelectionTreeNode> selectedNodes) {
        final LinkedList<ReportItem> selectedItems = new LinkedList<ReportItem>();
        selectedItems.addAll(selectedNodes);
        File file = getOutputFile();
        if (file != null) {
            Thread reportResultsThread = new Thread() {

                @Override
                public void run() {
                    try {
                        if (selectedItems.size() != 0) {
                            disableDialogControls();
                            enableProgressBar();
                            setProgressBar(0);
                            ReportStream reportStream = currentReportGenerator.createReportStream();
                            initProgressBar(0, selectedItems.size() + 1);
                            try {
                                String currentGroup = "";
                                String oldGroup = currentGroup;
                                for (ReportItem selectedItem : selectedItems) {
                                    setProgressBar(1);
                                    ReportResultAndRenderer reportResultAndRenderer = ReportService.getResult(selectedItem.getGroup(), selectedItem.getName());
                                    ResultObject resultObject = reportResultAndRenderer.getResultObject();
                                    Renderer renderer = reportResultAndRenderer.getRenderer();
                                    Set<String> parameterKeys = reportResultAndRenderer.getParameterKeys();
                                    for (String key : parameterKeys) {
                                        Object value = reportResultAndRenderer.getParameter(key);
                                        if (value != null) {
                                            renderer.setParameter(key, value.toString());
                                        }
                                    }
                                    Reportable reportable = null;
                                    if (exampleSet != null) reportable = renderer.createReportable(resultObject, new IOContainer(exampleSet), 800, 600); else reportable = renderer.createReportable(resultObject, null, 800, 600);
                                    if (reportable != null) {
                                        String name = ReportService.getReportItemName(selectedItem.getGroup(), selectedItem.getName());
                                        currentGroup = selectedItem.getGroup();
                                        if (!currentGroup.equals(oldGroup)) {
                                            String newSection = ReportService.getReportItemName("group", currentGroup);
                                            if (newSection != null) {
                                                reportStream.startSection(newSection, 0);
                                                oldGroup = currentGroup;
                                            }
                                        }
                                        reportStream.append(name, reportable, 800, 600);
                                    }
                                }
                                reportStream.close();
                                setProgressBar(1);
                                disableProgressBar();
                                enableDialogControls();
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new ReportException(e, "Report failed.");
                            } finally {
                                disableProgressBar();
                                enableDialogControls();
                            }
                        }
                    } catch (Exception e) {
                        disableProgressBar();
                        enableDialogControls();
                        SwingTools.showSimpleErrorMessage("Report failed.", e);
                    }
                }
            };
            reportResultsThread.start();
        }
    }

    private File getOutputFile() {
        String formatType = null;
        try {
            formatType = currentReportGenerator.getParameterAsString(ReportGenerator.PARAMETER_FORMAT);
        } catch (UndefinedParameterError e) {
            return null;
        }
        if (formatType.equals(ReportGenerator.FORMATS[ReportGenerator.FORMAT_PDF])) {
            File file = SwingTools.chooseFile(this, null, false, "pdf", "Rapid-I PDF Report");
            if (file != null) {
                currentReportGenerator.getParameters().setParameter(ReportGenerator.PARAMETER_PDF_OUTPUT_FILE, file.getAbsolutePath());
            }
            return file;
        }
        if (formatType.equals(ReportGenerator.FORMATS[ReportGenerator.FORMAT_RTF])) {
            File file = SwingTools.chooseFile(this, null, false, "rtf", "Rapid-I RTF Report");
            if (file != null) {
                currentReportGenerator.getParameters().setParameter(ReportGenerator.PARAMETER_RTF_OUTPUT_FILE, file.getAbsolutePath());
            }
            return file;
        }
        if (formatType.equals(ReportGenerator.FORMATS[ReportGenerator.FORMAT_EXCEL])) {
            File file = SwingTools.chooseFile(this, null, false, "xls", "Rapid-I Excel Report");
            if (file != null) {
                currentReportGenerator.getParameters().setParameter(ReportGenerator.PARAMETER_EXCEL_OUTPUT_FILE, file.getAbsolutePath());
            }
            return file;
        }
        if (formatType.equals(ReportGenerator.FORMATS[ReportGenerator.FORMAT_HTML])) {
            File file = SwingTools.chooseFile(this, null, false, true, null, "Rapid-I HTML Report");
            if (file != null) {
                currentReportGenerator.getParameters().setParameter(ReportGenerator.PARAMETER_HTML_OUTPUT_DIRECTORY, file.getAbsolutePath());
            }
            return file;
        }
        return null;
    }

    private ReportGenerator getCurentReportGenerator() {
        ReportGenerator reportGenerationOperator = null;
        try {
            reportGenerationOperator = OperatorService.createOperator(ReportGenerator.class);
        } catch (OperatorCreationException e) {
        }
        if (reportGenerationOperator != null) {
            if (currentFormat.equals("Excel")) {
                reportGenerationOperator.getParameters().setParameter(ReportGenerator.PARAMETER_FORMAT, ReportGenerator.FORMATS[ReportGenerator.FORMAT_EXCEL]);
            }
            if (currentFormat.equals("HTML")) {
                reportGenerationOperator.getParameters().setParameter(ReportGenerator.PARAMETER_FORMAT, ReportGenerator.FORMATS[ReportGenerator.FORMAT_HTML]);
            }
            if (currentFormat.equals("PDF")) {
                reportGenerationOperator.getParameters().setParameter(ReportGenerator.PARAMETER_FORMAT, ReportGenerator.FORMATS[ReportGenerator.FORMAT_PDF]);
            }
            if (currentFormat.equals("RTF")) {
                reportGenerationOperator.getParameters().setParameter(ReportGenerator.PARAMETER_FORMAT, ReportGenerator.FORMATS[ReportGenerator.FORMAT_RTF]);
            }
            reportGenerationOperator.getParameters().setParameter(ReportGenerator.PARAMETER_NAME, "Process Result Report");
        } else {
            SwingTools.showVerySimpleErrorMessage("Reporting engine not available, please contact Rapid-I for further assistance.");
        }
        return reportGenerationOperator;
    }

    private void updateOptionsButton() {
        currentReportGenerator = getCurentReportGenerator();
        String parameter = null;
        try {
            parameter = currentReportGenerator.getParameterAsString(ReportGenerator.PARAMETER_FORMAT);
        } catch (UndefinedParameterError e) {
        }
        if (parameter != null) {
            if (parameter.equals(ReportGenerator.FORMATS[ReportGenerator.FORMAT_PDF]) || parameter.equals(ReportGenerator.FORMATS[ReportGenerator.FORMAT_RTF])) {
                btOptions.setEnabled(true);
            } else {
                btOptions.setEnabled(false);
            }
        } else {
            btOptions.setEnabled(true);
        }
    }

    private void setProgressBar(int progress) {
        progressBar.setValue(progressBar.getValue() + progress);
    }

    private void disableProgressBar() {
        progressBar.setValue(progressBar.getMinimum());
        progressBar.setEnabled(false);
        progressLabel.setVisible(false);
        progressBar.setVisible(false);
    }

    private void enableProgressBar() {
        progressLabel.setVisible(true);
        progressBar.setEnabled(true);
        progressBar.setVisible(true);
    }

    private void initProgressBar(int minimum, int maximum) {
        progressBar.setMinimum(minimum);
        progressBar.setMaximum(maximum);
    }

    private void enableDialogControls() {
        btReport.setEnabled(true);
        btClose.setEnabled(true);
        ReportSelectionDialog.this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void disableDialogControls() {
        btReport.setEnabled(false);
        btClose.setEnabled(false);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == formatChooser) {
            currentFormat = (String) formatChooser.getSelectedItem();
            updateOptionsButton();
        }
        if (e.getSource() == btOptions) {
            boolean useTemplate = false;
            if (currentFormat.equals("PDF")) {
                useTemplate = true;
            }
            new ReportOptionDialog("Reporter Settings", currentReportGenerator, useTemplate).setVisible(true);
        }
        if (e.getSource() == btClose) {
            dispose();
        }
        if (e.getSource() == btReport) {
            createReport(model.getSelectedNodes());
        }
    }

    public void valueChanged(TreeSelectionEvent e) {
        try {
            String name, group;
            TreePath path = e.getNewLeadSelectionPath();
            if (path != null) {
                this.currentResultAndRenderer = null;
                this.currentResultName = null;
                DefaultMutableTreeNode aDMTNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                ReportSelectionTreeNode aRSTNode = (ReportSelectionTreeNode) aDMTNode.getUserObject();
                name = aRSTNode.getName();
                DefaultMutableTreeNode parentDMTNode = (DefaultMutableTreeNode) aDMTNode.getParent();
                if (parentDMTNode != null) {
                    if (parentDMTNode.equals(model.getRoot())) {
                        group = "root";
                    } else {
                        ReportSelectionTreeNode parentRSTNode = (ReportSelectionTreeNode) parentDMTNode.getUserObject();
                        group = parentRSTNode.getName();
                    }
                } else {
                    group = "root";
                }
                if (!group.equals("root")) {
                    ReportResultAndRenderer selectedResult = ReportService.getResult(group, name);
                    updatePreview(selectedResult, name);
                    updateParameters(selectedResult);
                } else {
                    try {
                        previewArea.setPreviewImage(defaultPreviewImage);
                        previewArea.repaint();
                        updateParameters(null);
                    } catch (Exception eX) {
                    }
                }
            } else {
                try {
                    previewArea.setPreviewImage(defaultPreviewImage);
                    previewArea.repaint();
                    updateParameters(null);
                } catch (Exception eX) {
                }
            }
        } catch (Exception eX) {
            previewArea.setPreviewImage(defaultPreviewImage);
            previewArea.repaint();
            updateParameters(null);
        }
    }

    private void updatePreview(ReportResultAndRenderer selectedResult, String name) {
        this.currentResultAndRenderer = selectedResult;
        this.currentResultName = name;
        Renderer renderer = selectedResult.getRenderer();
        ResultObject resultObject = selectedResult.getResultObject();
        Set<String> parameterKeys = selectedResult.getParameterKeys();
        for (String key : parameterKeys) {
            Object value = selectedResult.getParameter(key);
            if (value != null) {
                renderer.setParameter(key, value.toString());
            }
        }
        Reportable reportable = null;
        if (exampleSet != null) reportable = renderer.createReportable(resultObject, new IOContainer(exampleSet), 800, 600); else reportable = renderer.createReportable(resultObject, null, 800, 600);
        if (reportable != null) {
            if (reportable instanceof Renderable) {
                Renderable renderable = (Renderable) reportable;
                renderable.prepareRendering();
                int width = renderable.getRenderWidth(previewArea.getWidth());
                int height = renderable.getRenderHeight(previewArea.getHeight());
                if (width == 0) width = 800;
                if (height == 0) height = 600;
                BufferedImage previewImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                renderable.render(previewImage.getGraphics(), width, height);
                previewArea.setPreviewImage(previewImage);
                previewArea.repaint();
                renderable.finishRendering();
            } else if (reportable instanceof Tableable) {
                try {
                    Tableable table = (Tableable) reportable;
                    table.prepareReporting();
                    previewArea.setPreviewImage(tablePreviewImage);
                    String[] text = { name, table.getColumnNumber() + " columns  &  " + table.getRowNumber() + " rows" };
                    previewArea.setText(text);
                    previewArea.repaint();
                    table.finishReporting();
                } catch (Exception eX) {
                    eX.printStackTrace();
                }
            } else if (reportable instanceof com.rapidminer.report.Readable) {
                try {
                    com.rapidminer.report.Readable readable = (com.rapidminer.report.Readable) reportable;
                    previewArea.setPreviewImage(textPreviewImage);
                    String[] text = { name, "Consists of " + readable.toString().length() + " tokens." };
                    previewArea.setText(text);
                    previewArea.repaint();
                } catch (Exception eX) {
                    eX.printStackTrace();
                }
            } else {
                try {
                    previewArea.setPreviewImage(defaultPreviewImage);
                    previewArea.repaint();
                } catch (Exception eX) {
                    eX.printStackTrace();
                }
            }
        }
    }

    private void updateParameters(ReportResultAndRenderer selectedResult) {
        if (selectedResult != null) {
            Renderer renderer = selectedResult.getRenderer();
            propertyTable.setParameters(renderer.getParameters());
        } else {
            propertyTable.clearParameterTypes();
        }
    }

    private void updateParameterSettings() {
        if (currentResultAndRenderer == null) {
            return;
        }
        for (int p = 0; p < propertyTable.getRowCount(); p++) {
            String key = (String) propertyTable.getValueAt(p, 0);
            Object value = propertyTable.getValueAt(p, 1);
            if (value != null) currentResultAndRenderer.setParameter(key, value.toString());
        }
        updatePreview(currentResultAndRenderer, currentResultName);
    }

    public void updateReportButtonState() {
        int numberOfSelectedNodes = model.getSelectedNodes().size();
        if (numberOfSelectedNodes > 0) {
            btReport.setEnabled(true);
        } else {
            btReport.setEnabled(false);
        }
    }
}
