package edu.biik.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import edu.biik.controller.ApplicationController;
import edu.biik.entities.AlertSummary;
import edu.biik.entities.Reference;
import edu.biik.entities.Signature;
import edu.biik.framework.Event;
import edu.biik.framework.BiikFilteringPanel;
import edu.biik.framework.FiltersState;
import edu.biik.utils.BareBonesBrowserLaunch;
import edu.biik.gui.SummaryPanel;

public class BiikMainWindow extends JFrame {

    private JComboBox comboBox;

    private ButtonGroup buttonGroup_2 = new ButtonGroup();

    private ButtonGroup buttonGroup_1 = new ButtonGroup();

    private ButtonGroup buttonGroup = new ButtonGroup();

    private JList referencesList;

    private JTextField refDetailsTextfield;

    private JTextArea payloadTextArea;

    private JTable table;

    private EventTableModel tableModel;

    private JScrollPane scrollPane;

    private JScrollPane scrollPane_1;

    private SummaryPanel summaryPanel;

    private JLabel detailsLabel;

    private JLabel priorityLabel2;

    private JLabel sigClassLabel;

    private JLabel signatureLabel;

    private Map<Integer, Reference> referenceMap;

    private JButton checkButton;

    private DefaultListModel dlistModelForReferenceList;

    private Event currentEvent = null;

    private boolean payloadInAscii = true;

    private Reference selectedReference = null;

    /**
	 * Launch the application
	 * @param args
	 */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            BiikMainWindow frame = new BiikMainWindow();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Create the frame
	 */
    public BiikMainWindow() {
        super();
        setTitle("Biik Visualization System");
        getContentPane().setLayout(new BorderLayout());
        setBounds(100, 100, 1018, 624);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        final JMenu applicationMenu = new JMenu();
        applicationMenu.setText("Application");
        menuBar.add(applicationMenu);
        final JMenuItem exitMenuItem = new JMenuItem();
        exitMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                ApplicationController.getInstance().closeApplication();
            }
        });
        exitMenuItem.setText("Exit");
        applicationMenu.add(exitMenuItem);
        final JMenu aboutMenu = new JMenu();
        aboutMenu.setText("Help");
        menuBar.add(aboutMenu);
        final JMenuItem biikDocumentationMenuItem = new JMenuItem();
        final BiikMainWindow mainWindow = this;
        biikDocumentationMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                HelpDialog helpDialog;
                try {
                    helpDialog = new HelpDialog();
                    helpDialog.setVisible(true);
                } catch (Exception e) {
                    System.out.println("Problem loading help file.");
                }
            }
        });
        biikDocumentationMenuItem.setText("Biik Documentation");
        aboutMenu.add(biikDocumentationMenuItem);
        aboutMenu.addSeparator();
        final JMenuItem aboutBiikMenuItem = new JMenuItem();
        aboutBiikMenuItem.setText("About Biik");
        aboutMenu.add(aboutBiikMenuItem);
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(panel, BorderLayout.WEST);
        final JPanel panel_1 = new JPanel();
        panel.add(panel_1);
        panel_1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC }));
        panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Filters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JCheckBox authenticationAttacksCheckBox = new JCheckBox();
        authenticationAttacksCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (authenticationAttacksCheckBox.isSelected()) {
                    FiltersState.setAuthenticationAttackActive(true);
                } else {
                    FiltersState.setAuthenticationAttackActive(false);
                }
            }
        });
        authenticationAttacksCheckBox.setSelected(true);
        authenticationAttacksCheckBox.setText("Authentication Attacks");
        panel_1.add(authenticationAttacksCheckBox, new CellConstraints());
        final JCheckBox webAttacksCheckbox = new JCheckBox();
        webAttacksCheckbox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (webAttacksCheckbox.isSelected()) {
                    FiltersState.setWebAttackActive(true);
                } else {
                    FiltersState.setWebAttackActive(false);
                }
            }
        });
        webAttacksCheckbox.setSelected(true);
        webAttacksCheckbox.setText("Web Attacks");
        panel_1.add(webAttacksCheckbox, new CellConstraints(1, 2));
        final JCheckBox dosAttacksCheckbox = new JCheckBox();
        dosAttacksCheckbox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (dosAttacksCheckbox.isSelected()) {
                    FiltersState.setDosAttackActive(true);
                } else {
                    FiltersState.setDosAttackActive(false);
                }
            }
        });
        dosAttacksCheckbox.setSelected(true);
        dosAttacksCheckbox.setText("DOS Attacks");
        panel_1.add(dosAttacksCheckbox, new CellConstraints(1, 3));
        final JCheckBox reconAttacksCheckbox = new JCheckBox();
        reconAttacksCheckbox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (reconAttacksCheckbox.isSelected()) {
                    FiltersState.setReconAttackActive(true);
                } else {
                    FiltersState.setReconAttackActive(false);
                }
            }
        });
        reconAttacksCheckbox.setSelected(true);
        reconAttacksCheckbox.setText("Reconnaissance");
        panel_1.add(reconAttacksCheckbox, new CellConstraints(1, 4));
        final JCheckBox suspiciousItemsCheckbox = new JCheckBox();
        suspiciousItemsCheckbox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (suspiciousItemsCheckbox.isSelected()) {
                    FiltersState.setSuspiciousItemActive(true);
                } else {
                    FiltersState.setSuspiciousItemActive(false);
                }
            }
        });
        suspiciousItemsCheckbox.setSelected(true);
        suspiciousItemsCheckbox.setText("Suspicious Items");
        panel_1.add(suspiciousItemsCheckbox, new CellConstraints(1, 5));
        final JCheckBox protocolPortActivityCheckbox = new JCheckBox();
        protocolPortActivityCheckbox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (protocolPortActivityCheckbox.isSelected()) {
                    FiltersState.setProtocolPortActivityActive(true);
                } else {
                    FiltersState.setProtocolPortActivityActive(false);
                }
            }
        });
        protocolPortActivityCheckbox.setSelected(true);
        protocolPortActivityCheckbox.setText("Protocol/Port Activity");
        panel_1.add(protocolPortActivityCheckbox, new CellConstraints(1, 6));
        final JCheckBox shellcodeAttacksCheckbox = new JCheckBox();
        shellcodeAttacksCheckbox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (shellcodeAttacksCheckbox.isSelected()) {
                    FiltersState.setShellcodeAttackActive(true);
                } else {
                    FiltersState.setShellcodeAttackActive(false);
                }
            }
        });
        shellcodeAttacksCheckbox.setSelected(true);
        shellcodeAttacksCheckbox.setText("Shellcode Attacks");
        panel_1.add(shellcodeAttacksCheckbox, new CellConstraints(1, 7));
        final JCheckBox miscCheckbox = new JCheckBox();
        miscCheckbox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (miscCheckbox.isSelected()) {
                    FiltersState.setMiscAttackActive(true);
                } else {
                    FiltersState.setMiscAttackActive(false);
                }
            }
        });
        miscCheckbox.setSelected(true);
        miscCheckbox.setText("Miscellaneous / Unclassified");
        panel_1.add(miscCheckbox, new CellConstraints(1, 8));
        final JCheckBox tcpPacketsCheckBox = new JCheckBox();
        tcpPacketsCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (tcpPacketsCheckBox.isSelected()) {
                    FiltersState.setTcpPacketActive(true);
                } else {
                    FiltersState.setTcpPacketActive(false);
                }
            }
        });
        tcpPacketsCheckBox.setSelected(true);
        tcpPacketsCheckBox.setText("TCP Packets");
        panel_1.add(tcpPacketsCheckBox, new CellConstraints(1, 10));
        final JCheckBox udpPacketsCheckBox = new JCheckBox();
        udpPacketsCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (udpPacketsCheckBox.isSelected()) {
                    FiltersState.setUdpPacketActive(true);
                } else {
                    FiltersState.setUdpPacketActive(false);
                }
            }
        });
        udpPacketsCheckBox.setSelected(true);
        udpPacketsCheckBox.setText("UDP Packets");
        panel_1.add(udpPacketsCheckBox, new CellConstraints(1, 11));
        final JCheckBox icmpPacketsCheckBox = new JCheckBox();
        icmpPacketsCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (icmpPacketsCheckBox.isSelected()) {
                    FiltersState.setIcmpPacketActive(true);
                } else {
                    FiltersState.setIcmpPacketActive(false);
                }
            }
        });
        icmpPacketsCheckBox.setSelected(true);
        icmpPacketsCheckBox.setText("ICMP Packets");
        panel_1.add(icmpPacketsCheckBox, new CellConstraints(1, 12));
        final JButton reflectFiltersButton = new JButton();
        reflectFiltersButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ApplicationController.getInstance().updateVisualizationsBasedOnFilters();
            }
        });
        reflectFiltersButton.setText("Update Visualizations");
        panel_1.add(reflectFiltersButton, new CellConstraints(1, 14));
        final JTabbedPane tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        final JScrollPane scrollPane_3 = new JScrollPane();
        tabbedPane.addTab("Summary", null, scrollPane_3, null);
        final JScrollPane scrollPane_4 = new JScrollPane();
        scrollPane_3.setViewportView(scrollPane_4);
        summaryPanel = new SummaryPanel();
        scrollPane_4.setViewportView(summaryPanel);
        final JPanel panel_3 = new JPanel();
        panel_3.setLayout(new BorderLayout());
        tabbedPane.addTab("Selected Events Info", null, panel_3, null);
        scrollPane = new JScrollPane();
        panel_3.add(scrollPane, BorderLayout.CENTER);
        table = null;
        final JPanel panel_9 = new JPanel();
        panel_9.setLayout(new BoxLayout(panel_9, BoxLayout.Y_AXIS));
        panel_3.add(panel_9, BorderLayout.EAST);
        final JPanel panel_11 = new JPanel();
        panel_11.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, new ColumnSpec("default:grow(1.0)"), FormFactory.RELATED_GAP_COLSPEC, new ColumnSpec("85dlu:grow(1.0)") }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, new RowSpec("top:60dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, new RowSpec("fill:default") }));
        panel_11.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Signature Details", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panel_9.add(panel_11);
        final JLabel systemLabel = new JLabel();
        systemLabel.setText("Name :");
        panel_11.add(systemLabel, new CellConstraints(2, 1));
        final JLabel tagLabel = new JLabel();
        tagLabel.setText("Class :");
        panel_11.add(tagLabel, new CellConstraints(2, 3));
        signatureLabel = new JLabel();
        panel_11.add(signatureLabel, new CellConstraints(4, 1));
        sigClassLabel = new JLabel();
        panel_11.add(sigClassLabel, new CellConstraints(4, 3));
        final JLabel priorityLabel = new JLabel();
        priorityLabel.setText("Priority :");
        panel_11.add(priorityLabel, new CellConstraints(2, 5));
        priorityLabel2 = new JLabel();
        panel_11.add(priorityLabel2, new CellConstraints(4, 5));
        final JLabel referenceLabel_1 = new JLabel();
        referenceLabel_1.setText("Reference(s) :");
        panel_11.add(referenceLabel_1, new CellConstraints(2, 7));
        detailsLabel = new JLabel();
        detailsLabel.setText("Ref. Details :");
        panel_11.add(detailsLabel, new CellConstraints(2, 9));
        refDetailsTextfield = new JTextField();
        refDetailsTextfield.setEditable(false);
        panel_11.add(refDetailsTextfield, new CellConstraints(4, 9));
        checkButton = new JButton();
        checkButton.setText("Check");
        checkButton.setEnabled(false);
        panel_11.add(checkButton, new CellConstraints(2, 11, 3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        checkButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Reference selectedReference = BiikMainWindow.this.selectedReference;
                String referenceSystem = selectedReference.getReferenceSystem();
                String url = "";
                if (referenceSystem.equals("cve")) {
                    url = "http://cve.mitre.org/cgi-bin/cvename.cgi?name=" + selectedReference.getReferenceTag();
                } else if (referenceSystem.equals("bugtraq")) {
                    url = "http://www.securityfocus.com/bid/" + selectedReference.getReferenceTag();
                } else if (referenceSystem.equals("nessus")) {
                    url = "http://www.nessus.org/plugins/index.php?view=single&id=" + selectedReference.getReferenceTag();
                } else if (referenceSystem.equals("arachNIDS")) {
                    url = "http://www.whitehats.com/info/IDS/" + selectedReference.getReferenceTag();
                } else {
                    url = selectedReference.getReferenceTag();
                }
                BareBonesBrowserLaunch.openURL(url);
            }
        });
        referencesList = new JList();
        referencesList.setVisibleRowCount(5);
        panel_11.add(referencesList, new CellConstraints(4, 7, CellConstraints.FILL, CellConstraints.FILL));
        dlistModelForReferenceList = new DefaultListModel();
        referencesList.setModel(dlistModelForReferenceList);
        final BiikMainWindow biikMainWindow = this;
        referencesList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                checkButton.setEnabled(true);
                Reference selectedReference = referenceMap.get(referencesList.getSelectedIndex());
                if (selectedReference == null) {
                    return;
                }
                biikMainWindow.selectedReference = selectedReference;
                String referenceTag = selectedReference.getReferenceTag();
                if (referenceTag != null) {
                    refDetailsTextfield.setText(referenceTag);
                } else {
                    refDetailsTextfield.setText("");
                }
            }
        });
        final JPanel panel_12 = new JPanel();
        panel_12.setLayout(new BorderLayout());
        panel_12.setPreferredSize(new Dimension(0, 130));
        panel_12.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Payload Data", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panel_3.add(panel_12, BorderLayout.SOUTH);
        final JScrollPane scrollPane_2 = new JScrollPane();
        panel_12.add(scrollPane_2);
        payloadTextArea = new JTextArea();
        payloadTextArea.setEditable(false);
        payloadTextArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        payloadTextArea.setAutoscrolls(false);
        scrollPane_2.setViewportView(payloadTextArea);
        final JPanel panel_2 = new JPanel();
        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
        panel_12.add(panel_2, BorderLayout.EAST);
        final JRadioButton viewInHexRadioButton = new JRadioButton();
        buttonGroup_1.add(viewInHexRadioButton);
        viewInHexRadioButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                payloadInAscii = false;
                updatePayloadField();
            }
        });
        viewInHexRadioButton.setText("View in HEX");
        panel_2.add(viewInHexRadioButton);
        final JRadioButton viewInAsciiRadioButton = new JRadioButton();
        viewInAsciiRadioButton.setSelected(true);
        buttonGroup_1.add(viewInAsciiRadioButton);
        viewInAsciiRadioButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                payloadInAscii = true;
                updatePayloadField();
            }
        });
        viewInAsciiRadioButton.setText("View in ASCII");
        panel_2.add(viewInAsciiRadioButton);
        final JPanel panel_6 = new JPanel();
        panel_6.setLayout(new BorderLayout());
        getContentPane().add(panel_6, BorderLayout.NORTH);
        final JPanel panel_7 = new JPanel();
        final FlowLayout flowLayout_1 = new FlowLayout();
        flowLayout_1.setAlignment(FlowLayout.LEFT);
        panel_7.setLayout(flowLayout_1);
        panel_6.add(panel_7, BorderLayout.NORTH);
        final JButton launchMainVisualizationButton = new JButton();
        launchMainVisualizationButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ApplicationController.getInstance().launchMainVisualization();
            }
        });
        launchMainVisualizationButton.setText("Launch Main Visualization");
        panel_7.add(launchMainVisualizationButton);
        final JLabel histogramGranularityLabel = new JLabel();
        histogramGranularityLabel.setText("Histogram Granularity :");
        panel_7.add(histogramGranularityLabel);
        comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[] { "Normal", "Fine", "Very Fine", "Large" }));
        panel_7.add(comboBox);
        final JButton redrawHistogramButton = new JButton();
        redrawHistogramButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) getGranularityComboBox().getSelectedItem();
                if (selectedItem.equals("Normal")) {
                    ApplicationController.getInstance().setGranularityMultiplier(1.00);
                } else if (selectedItem.equals("Fine")) {
                    ApplicationController.getInstance().setGranularityMultiplier(0.75);
                } else if (selectedItem.equals("Very Fine")) {
                    ApplicationController.getInstance().setGranularityMultiplier(0.5);
                } else {
                    ApplicationController.getInstance().setGranularityMultiplier(1.25);
                }
                ApplicationController.getInstance().updateHistogram();
            }
        });
        redrawHistogramButton.setText("Redraw Histogram");
        panel_7.add(redrawHistogramButton);
        final JPanel panel_8 = new JPanel();
        panel_8.setPreferredSize(new Dimension(500, 80));
        panel_8.setLayout(new BorderLayout());
        panel_6.add(panel_8);
        scrollPane_1 = new JScrollPane();
        panel_8.add(scrollPane_1);
        final JLabel statusMessageLabel = new JLabel();
        statusMessageLabel.setText("Welcome to Biik!");
        getContentPane().add(statusMessageLabel, BorderLayout.SOUTH);
        setResizable(false);
        referenceMap = new HashMap<Integer, Reference>();
    }

    public void displayHistogram(BiikFilteringPanel timeSliceHistogramPanel) {
        scrollPane_1.setViewportView(timeSliceHistogramPanel);
        scrollPane_1.validate();
    }

    public void populateTable(ArrayList<Event> eventList) {
        tableModel = new EventTableModel();
        tableModel.setEventList(eventList);
        table = new JTable(tableModel);
        table.validate();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                Integer eventID = (Integer) tableModel.getValueAt(table.getSelectedRow(), 0);
                showPacketInformation(eventID);
                showSignatureDetails(eventID);
            }
        });
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        scrollPane.setViewportView(table);
        scrollPane.invalidate();
    }

    private void showPacketInformation(int eventID) {
        Event event = ApplicationController.getInstance().getEvent(eventID);
        currentEvent = event;
        String dataPayload = "";
        if (payloadInAscii) {
            dataPayload = event.getData().getDataPayload();
        } else {
            dataPayload = event.getData().getHexDataPayload();
        }
        if (dataPayload != null) {
            payloadTextArea.setText(dataPayload);
        } else {
            payloadTextArea.setText("");
        }
        refDetailsTextfield.setText("");
        checkButton.setEnabled(false);
        dlistModelForReferenceList.removeAllElements();
    }

    private void updatePayloadField() {
        if (currentEvent == null) {
            return;
        }
        Event event = currentEvent;
        String dataPayload = "";
        if (payloadInAscii) {
            dataPayload = event.getData().getDataPayload();
        } else {
            dataPayload = event.getData().getHexDataPayload();
        }
        if (dataPayload != null) {
            payloadTextArea.setText(dataPayload);
        } else {
            payloadTextArea.setText("");
        }
    }

    public void initializeAlertSummaryPanel(AlertSummary alertSummary) {
        summaryPanel.initialize(alertSummary);
    }

    private void showSignatureDetails(int eventID) {
        Event event = ApplicationController.getInstance().getEvent(eventID);
        Signature sigObject = event.getSignatureObject();
        sigClassLabel.setText(sigObject.getSignatureClass());
        signatureLabel.setText(sigObject.getSignatureName());
        priorityLabel2.setText(sigObject.getPriority() + "");
        List<Reference> references = sigObject.getReferences();
        referenceMap.clear();
        referencesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (references.size() == 0) {
            return;
        }
        for (int i = 0; i < references.size(); i++) {
            dlistModelForReferenceList.add(i, references.get(i).getReferenceSystem());
            referenceMap.put(i, references.get(i));
        }
        referencesList.invalidate();
    }

    public JComboBox getGranularityComboBox() {
        return comboBox;
    }
}
