package aidc.aigui.box;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import aidc.aigui.Gui;
import aidc.aigui.box.abstr.AbstractBox;
import aidc.aigui.dialogs.DisplayWindow;
import aidc.aigui.mathlink.MathAnalog;
import aidc.aigui.resources.AIFnComboBox;
import aidc.aigui.resources.AIFnFileParam;
import aidc.aigui.resources.AIFnFileSelect;
import aidc.aigui.resources.AIFnOption;
import aidc.aigui.resources.AIFnOptionGroup;
import aidc.aigui.resources.GuiHelper;
import aidc.aigui.resources.MathematicaFormat;
import aidc.aigui.resources.SwingWorker;
import com.wolfram.jlink.MathLinkException;
import aidc.aigui.plot.*;

/**
 * @author vboos
 *
 */
public class SimulationData extends AbstractBox {

    protected void setInstNumber(int instNum) {
        simulationDataCounter = instNum;
    }

    private JTabbedPane tabbedPane;

    int simulationDataCounter;

    private DefaultListModel data;

    private JList list;

    private double[] upperBorder;

    private double[] lowerBorder;

    private static int indexOfSelection = -1;

    private JButton jbDisplayBodePlot, jbDisplayNicholPlot, jbGetVariables, jbSelectReference;

    private JTextField jtfUpperBorder, jtfLowerBorder;

    private JLabel jlbSelectedSignal;

    private String oldName1, oldAdvancedOptions, oldReferenceSignal, oldSimulator;

    private AIFnFileSelect acfileComp;

    private AIFnComboBox simltrCbComp;

    private MathematicaFormat mf;

    private BodePlotFrame bpf;

    private final String sNoSelected = "(no signal selected)";

    /**
     * Listener for changes in text fields
     */
    private DocumentListener documentListener = new DocumentListener() {

        public void insertUpdate(DocumentEvent event) {
            setModified(true);
        }

        public void removeUpdate(DocumentEvent event) {
            setModified(true);
        }

        public void changedUpdate(DocumentEvent event) {
        }
    };

    boolean useMathematicaPlots = false;

    /**
     * Default class constructor
     */
    public SimulationData() {
    }

    public void init(int boxCount, int positionX, int positionY, AbstractBox ancestor, HashMap<String, String> hm) {
        super.init(boxCount, positionX, positionY, ancestor, hm);
        mf = new MathematicaFormat();
    }

    protected JPanel createPanel() {
        AIFnOptionGroup mainOpts = aifunc.getOptionGroup("main");
        AIFnOption simltrOption = (AIFnOption) mainOpts.getOption("Simulator");
        AIFnFileParam acfileOption = (AIFnFileParam) mainOpts.getOption("jtfOpenCir");
        simltrCbComp = (AIFnComboBox) simltrOption.createComponent(this);
        acfileComp = (AIFnFileSelect) acfileOption.createComponent(this);
        acfileComp.setAcSelect(simltrCbComp);
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelMain = new JPanel();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel panelRange = new JPanel();
        panelRange.setLayout(new BoxLayout(panelRange, BoxLayout.LINE_AXIS));
        panelMain.setLayout(new GridBagLayout());
        panelMain.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        GridBagConstraints c = new GridBagConstraints();
        jtfUpperBorder = new JTextField(10);
        jtfLowerBorder = new JTextField(10);
        jtfUpperBorder.setMinimumSize(new Dimension(100, 20));
        jtfLowerBorder.setMinimumSize(new Dimension(100, 20));
        jtfLowerBorder.getDocument().addDocumentListener(documentListener);
        jtfUpperBorder.getDocument().addDocumentListener(documentListener);
        JComboBox jcbSimulator;
        jcbSimulator = (JComboBox) simltrCbComp.getComponent();
        data = new DefaultListModel();
        list = new JList(data);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                jbDisplayBodePlot.setEnabled(true);
                jbDisplayNicholPlot.setEnabled(true);
                jbSelectReference.setEnabled(true);
            }
        });
        JScrollPane scrolList = new JScrollPane(list);
        scrolList.setPreferredSize(new Dimension(240, 100));
        JLabel l1 = new JLabel(acfileOption.getLabel());
        JLabel l3 = new JLabel("Simulator ->");
        l1.setLabelFor(acfileComp.getComponent());
        l3.setLabelFor(jcbSimulator);
        jbDisplayBodePlot = new JButton("BodePlot");
        jbDisplayBodePlot.addActionListener(this);
        jbDisplayNicholPlot = new JButton("NicholPlot");
        jbDisplayNicholPlot.addActionListener(this);
        jbSelectReference = new JButton("Set reference signal");
        jbSelectReference.addActionListener(this);
        jlbSelectedSignal = new JLabel(sNoSelected, JLabel.CENTER);
        jlbSelectedSignal.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        createApplyButton();
        jbClose = new JButton("Close");
        jbClose.setActionCommand("HideForm");
        jbClose.addActionListener(this);
        jbGetVariables = new JButton("GetVariables");
        jbGetVariables.addActionListener(this);
        jcbSimulator.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object selObj = ((JComboBox) e.getSource()).getSelectedItem();
                boolean bEnable = (selObj != null) && (selObj.toString().length() > 0);
                acfileComp.setEnabled(bEnable);
                list.setEnabled(bEnable);
                jbGetVariables.setEnabled(bEnable);
            }
        });
        jcbSimulator.setSelectedIndex(-1);
        JPanel vars = new JPanel(new GridLayout(3, 1));
        vars.add(jbGetVariables);
        vars.add(jbSelectReference);
        vars.add(jlbSelectedSignal);
        panelRange.add(jtfLowerBorder);
        panelRange.add(new JLabel("   -   "));
        panelRange.add(jtfUpperBorder);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        panelMain.add(l3, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.BOTH;
        panelMain.add(simltrCbComp.getComponent(), c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.NONE;
        panelMain.add(new JLabel("Select blank simulator if no simulation data available."), c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        panelMain.add(l1, c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        panelMain.add(acfileComp.getComponent(), c);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        panelMain.add(new JLabel("Variables:"), c);
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        panelMain.add(scrolList, c);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        panelMain.add(new JLabel("Range:"), c);
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        panelMain.add(panelRange, c);
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        panelMain.add(vars, c);
        buttonPanel.add(Box.createRigidArea(jbClose.getPreferredSize()));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(jbDisplayBodePlot);
        buttonPanel.add(jbDisplayNicholPlot);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(jbApply);
        buttonPanel.add(jbClose);
        jbDisplayBodePlot.setEnabled(false);
        jbDisplayNicholPlot.setEnabled(false);
        jbSelectReference.setEnabled(false);
        tabbedPane = new JTabbedPane();
        addOptionPanes(tabbedPane);
        tabbedPane.setEnabled(false);
        JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelMain, tabbedPane);
        splitPanel.setDividerSize(1);
        splitPanel.setResizeWeight(0.8);
        panel.add(createHintsPanel(), BorderLayout.NORTH);
        panel.add(splitPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(530, 380));
        setHints(getInfoIcon(), "Select the simulator or left blank");
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == jbSelectReference) {
            int index = list.getSelectedIndex();
            if (indexOfSelection != -1) data.setElementAt(getProperty("referenceSignal", "").toString(), indexOfSelection);
            if (index > -1) {
                Object obj = data.getElementAt(index);
                hmProps.put("referenceSignal", obj.toString());
                hmProps.put("referenceSignalMinValue", mf.formatMath(lowerBorder[index]));
                hmProps.put("referenceSignalMaxValue", mf.formatMath(upperBorder[index]));
                obj = " -> " + obj;
                data.setElementAt(obj, index);
                indexOfSelection = index;
                if (indexOfSelection >= 0) {
                    tabbedPane.setEnabled(true);
                }
                jlbSelectedSignal.setText(hmProps.get("referenceSignal"));
                setModified(true);
            } else System.out.println("You have to get variables first!!");
        }
        if (e.getSource() == jbDisplayBodePlot) {
            final SwingWorker worker = new SwingWorker() {

                public Object construct() {
                    if (evaluateNotebookCommands((AbstractBox) ab) >= 0) {
                        try {
                            if (useMathematicaPlots) {
                                String command = "";
                                String listOfVariables = createListOfVariables();
                                if (listOfVariables != null) {
                                    command = "BodePlot[" + listOfVariables + ",{f," + jtfLowerBorder.getText() + "," + jtfUpperBorder.getText() + "}]";
                                    DisplayWindow dw = new DisplayWindow(boxClassInfo.strBoxName + " (" + boxNumber + ") - BodePlot");
                                    dw.setImageCommand(command, 500, 500, 72);
                                } else {
                                    ((AbstractBox) ab).showMessage("Please choose a signal from the list");
                                }
                            } else {
                                if (bpf == null) {
                                    bpf = new BodePlotFrame();
                                    bpf.setTitle(boxClassInfo.strBoxName + " (" + boxNumber + ") - BodePlot");
                                    bpf.setSize(bpf.getWidth(), 400);
                                } else {
                                    bpf.clear();
                                }
                                int[] aIdxSel = list.getSelectedIndices();
                                StringBuilder sbCommand = new StringBuilder(80);
                                sbCommand.append("nv = InterpolatingFunctionToList[\"");
                                int initialLength = sbCommand.length();
                                for (int i = 0; i < aIdxSel.length; i++) {
                                    String nodename = (String) list.getModel().getElementAt(aIdxSel[i]);
                                    if (aIdxSel[i] == indexOfSelection) nodename = getProperty("referenceSignal", "");
                                    sbCommand.setLength(initialLength);
                                    sbCommand.append(nodename);
                                    sbCommand.append("\"/.First[");
                                    if (Gui.gui.aiVersion == Gui.AI_VERSION_3) sbCommand.append("GetData[");
                                    sbCommand.append("simulationData");
                                    sbCommand.append(simulationDataCounter);
                                    if (Gui.gui.aiVersion == Gui.AI_VERSION_3) sbCommand.append("]");
                                    sbCommand.append("]]");
                                    String strListN = MathAnalog.evaluateToInputForm(sbCommand.toString(), 0, false);
                                    bpf.addInterpolatingFunction(strListN, i, nodename);
                                }
                                bpf.setVisible(true);
                                bpf.toFront();
                            }
                        } catch (MathLinkException e) {
                            MathAnalog.notifyUser();
                        }
                    }
                    return new Object();
                }
            };
            worker.ab = this;
            worker.start();
        } else if (e.getSource() == jbDisplayNicholPlot) {
            final SwingWorker worker = new SwingWorker() {

                public Object construct() {
                    byte[] tabx = null;
                    if (evaluateNotebookCommands((AbstractBox) ab) >= 0) {
                        try {
                            String command = "";
                            String listOfVariables = createListOfVariables();
                            if (listOfVariables != null) {
                                command = "NicholPlot[" + listOfVariables + ",{f," + jtfLowerBorder.getText() + "," + jtfUpperBorder.getText() + "},AspectRatio->0.8]";
                                tabx = MathAnalog.evaluateToImage(command, 500, 500, 72, true, true);
                            } else ((AbstractBox) ab).showMessage("Please choose a signal from the list");
                            if (tabx != null) {
                                DisplayWindow dw = new DisplayWindow(boxClassInfo.strBoxName + " (" + boxNumber + ") - NicholPlot");
                                dw.setImageCommand(command, 500, 500, 72);
                            }
                        } catch (MathLinkException e) {
                            MathAnalog.notifyUser();
                        }
                    }
                    return new Object();
                }
            };
            worker.ab = this;
            worker.start();
        } else if (e.getSource() == jbGetVariables) {
            final SwingWorker worker = new SwingWorker() {

                public Object construct() {
                    try {
                        if (evaluateNotebookCommands((AbstractBox) ab) >= 0) {
                            fillOutList();
                        }
                        jbDisplayBodePlot.setEnabled(false);
                        jbDisplayNicholPlot.setEnabled(false);
                        jbSelectReference.setEnabled(false);
                    } catch (MathLinkException e) {
                        data.clear();
                        MathAnalog.notifyUser();
                    }
                    return new Object();
                }

                public void finished() {
                    list.setModel(data);
                    if (data.getSize() > 0) {
                        list.setSelectedIndex(indexOfSelection);
                        list.ensureIndexIsVisible(indexOfSelection);
                    }
                }
            };
            worker.ab = this;
            worker.start();
        }
    }

    public synchronized int evaluateNotebookCommands(AbstractBox ab) {
        try {
            String command, result;
            showForm(false);
            saveState();
            int iReturn;
            if ((iReturn = ancestor.evaluateNotebookCommands(this)) < 0) return iReturn;
            String newName1 = acfileComp.getComponentText();
            if (newName1.length() == 0) {
                System.out.println("No simulation data");
                return 0;
            }
            StringBuilder sb = new StringBuilder();
            appendOptionSettings(sb);
            String newAdvancedOptions = sb.toString();
            String newReferenceSignal = new String("");
            String newSimulator = simltrCbComp.getComponentText();
            if (hmProps.containsKey("referenceSignal")) newReferenceSignal = hmProps.get("referenceSignal").toString();
            if (!actionSuccessfull || !newName1.equals(oldName1) || !newSimulator.equals(oldSimulator) || newName1.equals("") || !oldAdvancedOptions.equals(newAdvancedOptions) || !newReferenceSignal.equals(oldReferenceSignal) || iReturn > 0) {
                oldName1 = newName1;
                oldAdvancedOptions = newAdvancedOptions;
                oldReferenceSignal = newReferenceSignal;
                oldSimulator = newSimulator;
                command = "simulationData" + simulationDataCounter + " = ReadSimulationData[\"" + GuiHelper.escape(acfileComp.getComponentText()) + "\",Simulator -> " + newSimulator + newAdvancedOptions + "]";
                result = MathAnalog.evaluateToOutputForm(command, 300, true);
                increaseNumberOfActions(ab);
                if (checkResult(new String[] { command }, new String[] { result }) < 0) return -1;
                Object o1 = hmProps.get("referenceSignal");
                if (o1 != null) {
                    if (Gui.gui.aiVersion == Gui.AI_VERSION_3) command = "vout" + simulationDataCounter + " = \"" + o1.toString() + "\"/.First[GetData[simulationData" + simulationDataCounter + "]]"; else command = "vout" + simulationDataCounter + " = \"" + o1.toString() + "\"/.First[simulationData" + simulationDataCounter + "]";
                    result = MathAnalog.evaluateToOutputForm(command, 300, true);
                    if (checkResult(new String[] { command }, new String[] { result }) < 0) return -1;
                }
                return 1;
            }
            if (!ab.equals(this) && numberOfActions != ab.getLastParentAction()) {
                ab.setLastParentAction(numberOfActions);
                return 2;
            }
            return 0;
        } catch (MathLinkException e) {
            MathAnalog.notifyUser();
            return -1;
        }
    }

    /**
     * Method enables or disables widgets on the JPanel, according to whether
     * they are filled out correctly and to their selection.
     *  
     */
    public void setWidgets() {
    }

    public void saveState() {
        String temp;
        if (frameForm != null) {
            simltrCbComp.saveState(hmProps);
            acfileComp.saveState(hmProps);
            hmProps.put("referenceSignalInterFunction", "vout" + simulationDataCounter);
            hmProps.put("variablesData", String.valueOf(data.size()));
            hmProps.put("referenceSignalMinValue", jtfLowerBorder.getText());
            hmProps.put("referenceSignalMaxValue", jtfUpperBorder.getText());
            for (int i = 0; i < data.size(); i++) {
                temp = "variablesData" + i;
                if (i != indexOfSelection) hmProps.put(temp, data.getElementAt(i).toString()); else hmProps.put(temp, getProperty("referenceSignal", "").toString());
            }
            if (upperBorder != null) {
                hmProps.put("upperBorder", new Integer(this.upperBorder.length).toString());
                for (int i = 0; i < this.upperBorder.length; i++) {
                    temp = "upperBorder" + i;
                    hmProps.put(temp, Double.toString(this.upperBorder[i]));
                    temp = "lowerBorder" + i;
                    hmProps.put(temp, Double.toString(this.lowerBorder[i]));
                }
            }
            saveOptionPanes(hmProps);
        }
        saveNotebook();
        setModified(false);
        Gui.gui.stateDoc.setModified(true);
    }

    public void loadState() {
        String temp, temp2;
        simltrCbComp.loadState(hmProps);
        acfileComp.loadState(hmProps);
        if (hmProps.get("jtfOpenCir") != null) {
            acfileComp.setEnabled(true);
        }
        String signal = hmProps.get("referenceSignal");
        boolean bSignalSelected = (signal != null && signal.length() > 0);
        if (!bSignalSelected) signal = sNoSelected;
        jlbSelectedSignal.setText(signal);
        if (bSignalSelected) {
            tabbedPane.setEnabled(true);
        }
        indexOfSelection = -1;
        if (hmProps.get("variablesData") != null) {
            data.clear();
            for (int i = 0; i < Integer.parseInt(hmProps.get("variablesData").toString()); i++) {
                temp = "variablesData" + i;
                temp2 = hmProps.get(temp).toString();
                if (temp2.equals(signal)) {
                    temp2 = " -> " + temp2;
                    indexOfSelection = i;
                }
                data.addElement(temp2);
            }
        }
        list.setSelectedIndex(indexOfSelection);
        if (indexOfSelection >= 0) {
            list.ensureIndexIsVisible(indexOfSelection);
        }
        if (hmProps.get("referenceSignalMinValue") != null) {
            jtfLowerBorder.setText(hmProps.get("referenceSignalMinValue").toString());
        }
        if (hmProps.get("referenceSignalMaxValue") != null) {
            jtfUpperBorder.setText(hmProps.get("referenceSignalMaxValue").toString());
        }
        if (hmProps.get("upperBorder") != null) {
            this.upperBorder = new double[new Integer(hmProps.get("upperBorder").toString()).intValue()];
            this.lowerBorder = new double[new Integer(hmProps.get("upperBorder").toString()).intValue()];
            for (int i = 0; i < upperBorder.length; i++) {
                temp = "upperBorder" + i;
                this.upperBorder[i] = Double.parseDouble(hmProps.get(temp).toString());
                temp = "lowerBorder" + i;
                this.lowerBorder[i] = Double.parseDouble(hmProps.get(temp).toString());
            }
        }
        loadOptionPanes(hmProps);
        setModified(false);
    }

    public void fillOutList() throws MathLinkException {
        double s = 0, s1 = 0;
        if (!acfileComp.getComponentText().trim().equals("")) {
            if (evaluateNotebookCommands(this) >= 0) {
                data.clear();
                String command = "", command1 = "", command2 = "";
                if (Gui.gui.aiVersion == Gui.AI_VERSION_3) {
                    command = "temp = Map[First,First[GetData[simulationData" + simulationDataCounter + "]]]";
                    command1 = "temp1 = Map[Last, Map[First, Map[First, Map[Last, First[GetData[simulationData" + simulationDataCounter + "]]]]]]";
                    command2 = "temp2 = Map[First, Map[First, Map[First, Map[Last, First[GetData[simulationData" + simulationDataCounter + "]]]]]]";
                } else {
                    command = "temp = Map[First,First[simulationData" + simulationDataCounter + "]]";
                    command1 = "temp1 = Map[Last, Map[First, Map[First, Map[Last, First[simulationData" + simulationDataCounter + "]]]]]";
                    command2 = "temp2 = Map[First, Map[First, Map[First, Map[Last, First[simulationData" + simulationDataCounter + "]]]]]";
                }
                String result = MathAnalog.evaluateToOutputForm(command, 0, false);
                result = MathAnalog.evaluateToOutputForm(command1, 0, false);
                result = MathAnalog.evaluateToOutputForm(command2, 0, false);
                command = "Length[temp]";
                result = MathAnalog.evaluateToOutputForm(command, 0, false);
                int b = Integer.parseInt(result);
                upperBorder = new double[b];
                lowerBorder = new double[b];
                String lVar = "";
                String j = null;
                String jj = null;
                String temp = "";
                for (int i = 1; i <= b; i++) {
                    Integer nowy = new Integer(i);
                    j = nowy.toString();
                    jj = "temp[[" + j + "]]";
                    lVar = MathAnalog.evaluateToOutputForm(jj, 0, false);
                    if (lVar.equals(hmProps.get("referenceSignal"))) {
                        lVar = " -> " + lVar;
                        temp = lVar;
                        tabbedPane.setEnabled(true);
                    }
                    data.addElement(lVar);
                    jj = "temp1[[" + j + "]]";
                    s = MathAnalog.evaluateToDouble(jj, false);
                    upperBorder[i - 1] = s;
                    jj = "temp2[[" + j + "]]";
                    s1 = MathAnalog.evaluateToDouble(jj, false);
                    lowerBorder[i - 1] = s1;
                }
                if (temp.equals("")) indexOfSelection = -1; else indexOfSelection = data.indexOf(temp);
                jtfUpperBorder.setText(mf.formatMath(s));
                jtfLowerBorder.setText(mf.formatMath(s1));
                hmProps.put("referenceSignalMinValue", mf.formatMath(s1));
                hmProps.put("referenceSignalMaxValue", mf.formatMath(s));
            }
        } else System.out.println("Wrong filename!!");
    }

    public String createListOfVariables() {
        try {
            Object[] val;
            int o = 0;
            String command;
            String value = "{";
            int[] no = list.getSelectedIndices();
            val = list.getSelectedValues();
            for (int c = 0; c < no.length; c++) {
                if (no[c] == indexOfSelection) val[c] = getProperty("referenceSignal", "");
            }
            if (val == null) {
                return null;
            } else if (val.length >= 1) {
                while (o < val.length) {
                    if (o != 0) value = value + ",";
                    if (Gui.gui.aiVersion == Gui.AI_VERSION_3) command = MathAnalog.evaluateToOutputForm("vout" + o + "sd" + simulationDataCounter + " = \"" + val[o].toString() + "\"/.First[GetData[simulationData" + simulationDataCounter + "]]", 300, true); else command = MathAnalog.evaluateToOutputForm("vout" + o + "sd" + simulationDataCounter + " = \"" + val[o].toString() + "\"/.First[simulationData" + simulationDataCounter + "]", 300, true);
                    command.getClass();
                    value = value + "vout" + o + "sd" + simulationDataCounter + "[f]";
                    o++;
                }
                value = value + "}";
            }
            return value;
        } catch (MathLinkException e) {
            MathAnalog.notifyUser();
            return null;
        }
    }

    private void saveNotebook() {
        String sSimulator = hmProps.get("Simulator");
        String sOpenCir = hmProps.get("jtfOpenCir");
        if (sSimulator != null && sSimulator.length() > 0 && sOpenCir != null && sOpenCir.length() > 0) {
            StringBuilder sbLine = new StringBuilder();
            sbLine.append("simulationData");
            sbLine.append(simulationDataCounter);
            sbLine.append(" = ReadSimulationData[\"");
            sbLine.append(GuiHelper.escape(sOpenCir));
            sbLine.append("\",Simulator -> ");
            sbLine.append(sSimulator);
            AIFnOptionGroup advOptions = aifunc.getOptionGroup("adv");
            if (advOptions != null) {
                advOptions.appendOptionSettings(hmProps, sbLine);
            }
            sbLine.append("]");
            hmProps.put("notebookLine0", sbLine.toString());
            sbLine.setLength(0);
            sbLine.append(hmProps.get("referenceSignalInterFunction"));
            sbLine.append(" = \"");
            sbLine.append(hmProps.get("referenceSignal"));
            if (Gui.gui.aiVersion == Gui.AI_VERSION_3) {
                sbLine.append("\"/.First[GetData[simulationData");
                sbLine.append(simulationDataCounter);
                sbLine.append("]]");
            } else {
                sbLine.append("\"/.First[simulationData");
                sbLine.append(simulationDataCounter);
                sbLine.append("]");
            }
            hmProps.put("notebookLine1", sbLine.toString());
            sbLine.setLength(0);
            sbLine.append("BodePlot[");
            sbLine.append(hmProps.get("referenceSignalInterFunction"));
            sbLine.append("[f],{f");
            appendParameter("referenceSignalMinValue", "0", sbLine);
            appendParameter("referenceSignalMaxValue", "0", sbLine);
            sbLine.append("},AspectRatio->0.8]");
            hmProps.put("notebookLine2", sbLine.toString());
            sbLine.setLength(0);
            sbLine.append("NicholPlot[");
            sbLine.append(hmProps.get("referenceSignalInterFunction"));
            sbLine.append("[f],{f");
            appendParameter("referenceSignalMinValue", "0", sbLine);
            appendParameter("referenceSignalMaxValue", "0", sbLine);
            sbLine.append("},AspectRatio->0.8]");
            hmProps.put("notebookLine3", sbLine.toString());
            hmProps.put("notebookLineCounter", "3");
        }
    }
}
