package de.mogwai.eareport;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.mogwai.common.client.looks.components.DefaultStatusBar;
import de.mogwai.common.client.looks.tools.WindowHelper;
import de.mogwai.reports.ReportingComponent;
import de.mogwai.reports.ReportingFactory;
import de.mogwai.reports.jasper.JasperExportType;
import de.mogwai.reports.jasper.JasperReportingComponent;

public class MainFrame extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(MainFrame.class);

    private JTextField eapFile = new JTextField();

    private JButton eapSelect = new JButton("...");

    private JTextField tempDir = new JTextField();

    private JButton tempSelect = new JButton("...");

    private JTextField wordTemplate = new JTextField();

    private JButton wordTemplateSelect = new JButton("...");

    private JTextField wordOutput = new JTextField();

    private JButton wordOutputSelect = new JButton("...");

    private JTextField jasperTemplate = new JTextField();

    private JComboBox jasperFormat = new JComboBox();

    private JButton jasperTemplateSelect = new JButton("...");

    private JTextField jasperOutput = new JTextField();

    private JButton jasperOutputSelect = new JButton("...");

    private JButton execute = new JButton("Generate!");

    private JList includeElements = new JList();

    private JList preventRecursion = new JList();

    private JTabbedPane reportTypePane = new JTabbedPane();

    private DefaultStatusBar statusBar = new DefaultStatusBar();

    public MainFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Mogwai Enterprise Architect Reports");
        setResizable(false);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                saveSettings();
            }
        });
        CellConstraints cons = new CellConstraints();
        Container theContainer = new JPanel();
        theContainer.setLayout(new FormLayout("2dlu,50dlu,2dlu,fill:150dlu,2dlu,20dlu,2dlu", "2dlu,p,2dlu,fill:100dlu,2dlu,p,2dlu,p,2dlu,p,2dlu"));
        theContainer.add(new JLabel("EAP File:"), cons.xy(2, 2));
        theContainer.add(eapFile, cons.xy(4, 2));
        theContainer.add(eapSelect, cons.xy(6, 2));
        eapSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commandSelectEAP();
            }
        });
        JPanel thePanel = new JPanel();
        thePanel.setLayout(new FormLayout("fill:50dlu:grow,2dlu,fill:50dlu:grow", "p,2dlu,fill:p:grow"));
        thePanel.add(DefaultComponentFactory.getInstance().createSeparator("Include elements"), cons.xy(1, 1));
        thePanel.add(DefaultComponentFactory.getInstance().createSeparator("Prevent recursion for"), cons.xy(3, 1));
        thePanel.add(new JScrollPane(includeElements), cons.xy(1, 3));
        thePanel.add(new JScrollPane(preventRecursion), cons.xy(3, 3));
        theContainer.add(thePanel, cons.xywh(2, 4, 5, 1));
        JPanel theCommonPanel = new JPanel();
        theCommonPanel.setLayout(new FormLayout("60dlu,2dlu,fill:100dlu:grow,2dlu,20dlu", "2dlu,p,2dlu"));
        theCommonPanel.add(new JLabel("Temp directory:"), cons.xy(1, 2));
        theCommonPanel.add(tempDir, cons.xy(3, 2));
        theCommonPanel.add(tempSelect, cons.xy(5, 2));
        tempSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commandSelectTemp();
            }
        });
        theContainer.add(theCommonPanel, cons.xywh(2, 6, 5, 1));
        theContainer.add(reportTypePane, cons.xywh(2, 8, 5, 1));
        JPanel theWordPanel = new JPanel();
        reportTypePane.add("MSWord Generator", theWordPanel);
        theWordPanel.setLayout(new FormLayout("2dlu,60dlu,2dlu,fill:100dlu:grow,2dlu,20dlu,2dlu", "2dlu,p,2dlu,p,2dlu,p,2dlu"));
        theWordPanel.add(new JLabel("Template:"), cons.xy(2, 2));
        theWordPanel.add(wordTemplate, cons.xy(4, 2));
        theWordPanel.add(wordTemplateSelect, cons.xy(6, 2));
        wordTemplateSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commandSelectWordTemplate();
            }
        });
        theWordPanel.add(new JLabel("Output:"), cons.xy(2, 4));
        theWordPanel.add(wordOutput, cons.xy(4, 4));
        theWordPanel.add(wordOutputSelect, cons.xy(6, 4));
        wordOutputSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commandSelectWordOutput();
            }
        });
        JPanel theJasperPanel = new JPanel();
        reportTypePane.add("Jasper Generator", theJasperPanel);
        theJasperPanel.setLayout(new FormLayout("2dlu,60dlu,2dlu,fill:100dlu:grow,2dlu,20dlu,2dlu", "2dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu"));
        theJasperPanel.add(new JLabel("Template:"), cons.xy(2, 2));
        theJasperPanel.add(jasperTemplate, cons.xy(4, 2));
        theJasperPanel.add(jasperTemplateSelect, cons.xy(6, 2));
        jasperTemplateSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commandSelectJasperTemplate();
            }
        });
        theJasperPanel.add(new JLabel("Generator:"), cons.xy(2, 4));
        theJasperPanel.add(jasperFormat, cons.xywh(4, 4, 2, 1));
        DefaultComboBoxModel theGeneratorModel = new DefaultComboBoxModel();
        for (JasperExportType<JRExporter> theType : JasperReportingComponent.KNOWN_TYPES) {
            theGeneratorModel.addElement(theType);
        }
        jasperFormat.setModel(theGeneratorModel);
        jasperFormat.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commandUpdateJasperOutput();
            }
        });
        theJasperPanel.add(new JLabel("Output:"), cons.xy(2, 6));
        theJasperPanel.add(jasperOutput, cons.xy(4, 6));
        theJasperPanel.add(jasperOutputSelect, cons.xy(6, 6));
        jasperOutputSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commandSelectJasperOutput();
            }
        });
        theContainer.add(execute, cons.xy(2, 10));
        execute.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commandExecute();
            }
        });
        DefaultListModel theModel = new DefaultListModel();
        for (String theElementTypes : EAExporter.ELEMENTTYPES) {
            theModel.addElement(theElementTypes);
        }
        includeElements.setModel(theModel);
        preventRecursion.setModel(theModel);
        includeElements.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        includeElements.setSelectionInterval(0, theModel.size() - 1);
        preventRecursion.setSelectedValue("Activity", true);
        Container theContentPane = getContentPane();
        theContentPane.setLayout(new BorderLayout());
        theContentPane.add(theContainer, BorderLayout.CENTER);
        theContentPane.add(statusBar, BorderLayout.SOUTH);
        loadSettings();
    }

    private void commandSelectEAP() {
        JFileChooser theChooser = new JFileChooser();
        String theFile = eapFile.getText();
        if (!StringUtils.isEmpty(theFile)) {
            theChooser.setSelectedFile(new File(theFile));
        }
        theChooser.setFileFilter(new GenericFileFilter(".eap", "Enterprise Architect Project"));
        if (theChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            eapFile.setText(theChooser.getSelectedFile().toString());
        }
    }

    private void commandSelectTemp() {
        JFileChooser theChooser = new JFileChooser();
        String theFile = tempDir.getText();
        if (!StringUtils.isEmpty(theFile)) {
            theChooser.setSelectedFile(new File(theFile));
        }
        theChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (theChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            tempDir.setText(theChooser.getSelectedFile().toString());
        }
    }

    private void commandSelectWordTemplate() {
        JFileChooser theChooser = new JFileChooser();
        String theFile = wordTemplate.getText();
        if (!StringUtils.isEmpty(theFile)) {
            theChooser.setSelectedFile(new File(theFile));
        }
        theChooser.setFileFilter(new GenericFileFilter(".docx", "Microsoft Word Document"));
        if (theChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            wordTemplate.setText(theChooser.getSelectedFile().toString());
        }
    }

    private void commandSelectJasperTemplate() {
        JFileChooser theChooser = new JFileChooser();
        String theFile = jasperTemplate.getText();
        if (!StringUtils.isEmpty(theFile)) {
            theChooser.setSelectedFile(new File(theFile));
        }
        theChooser.setFileFilter(new GenericFileFilter(".jrxml", "Jasper Reports Template"));
        if (theChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            jasperTemplate.setText(theChooser.getSelectedFile().toString());
        }
    }

    private void commandSelectWordOutput() {
        JFileChooser theChooser = new JFileChooser();
        String theFile = wordOutput.getText();
        if (!StringUtils.isEmpty(theFile)) {
            theChooser.setSelectedFile(new File(theFile));
        }
        theChooser.setFileFilter(new GenericFileFilter(".docx", "Microsoft Word Document"));
        if (theChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            wordOutput.setText(theChooser.getSelectedFile().toString());
        }
    }

    private void commandSelectJasperOutput() {
        JFileChooser theChooser = new JFileChooser();
        String theFile = jasperOutput.getText();
        if (!StringUtils.isEmpty(theFile)) {
            theChooser.setSelectedFile(new File(theFile));
        }
        if (theChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String theFileName = theChooser.getSelectedFile().toString();
            theFileName = computeFileName(theFileName);
            jasperOutput.setText(theFileName);
        }
    }

    private void commandUpdateJasperOutput() {
        String theFileName = jasperOutput.getText();
        if (!StringUtils.isEmpty(theFileName)) {
            theFileName = computeFileName(theFileName);
            jasperOutput.setText(theFileName);
        }
    }

    private String computeFileName(String theFileName) {
        JasperExportType<JRExporter> theType = (JasperExportType<JRExporter>) jasperFormat.getSelectedItem();
        int p = theFileName.lastIndexOf(".");
        if (p >= 0) {
            theFileName = theFileName.substring(0, p) + theType.getExtension();
        } else {
            theFileName += theType.getExtension();
        }
        return theFileName;
    }

    private void commandExecute() {
        final EAExporter theExporter = new EAExporter();
        Object[] theIncludes = includeElements.getSelectedValues();
        theExporter.getElementsOfInterest().clear();
        for (Object theObject : theIncludes) {
            theExporter.getElementsOfInterest().add((String) theObject);
        }
        Object[] thePrevent = preventRecursion.getSelectedValues();
        theExporter.getPreventRecursionFor().clear();
        for (Object theObject : thePrevent) {
            theExporter.getPreventRecursionFor().add((String) theObject);
        }
        try {
            final File theOutputDir = new File(tempDir.getText());
            final File theXMLFile = new File(theOutputDir, theExporter.getModelXMLFileName());
            boolean generate = true;
            if (theXMLFile.exists()) {
                if (JOptionPane.showConfirmDialog(this, "Do you want to overwrite the existing export?", "Question", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    generate = false;
                }
            }
            final boolean theFinalGenerate = generate;
            Thread theRunner = new Thread() {

                @Override
                public void run() {
                    SwingWorker<String, String> theWorker = new SwingWorker<String, String>() {

                        @Override
                        protected String doInBackground() throws Exception {
                            try {
                                if (theFinalGenerate) {
                                    publish("Generating EA Export...");
                                    ValidationResult theResult = theExporter.exportModel(eapFile.getText(), theOutputDir);
                                    if (theResult.getMessages().size() > 0) {
                                        final JDialog theDialog = new JDialog(MainFrame.this, "Validation problems", true);
                                        JTextArea theArea = new JTextArea();
                                        theDialog.getContentPane().add(new JScrollPane(theArea), BorderLayout.CENTER);
                                        JButton theButton = new JButton("Ok");
                                        theButton.addActionListener(new ActionListener() {

                                            public void actionPerformed(ActionEvent e) {
                                                theDialog.setVisible(false);
                                            }
                                        });
                                        theDialog.getContentPane().add(theButton, BorderLayout.SOUTH);
                                        theDialog.setSize(400, 300);
                                        StringBuffer theBuffer = new StringBuffer();
                                        for (ValidationMessage theMessage : theResult.getMessages()) {
                                            theBuffer.append(theMessage.getSevertiy() + ": " + theMessage.getMessage() + "\n");
                                        }
                                        theArea.setText(theBuffer.toString());
                                        Point p = MainFrame.this.getLocation();
                                        Dimension theSize = MainFrame.this.getSize();
                                        p.x += (int) (theSize.width / 2);
                                        p.y += (int) (theSize.height / 2);
                                        theSize = theDialog.getSize();
                                        p.x -= (int) (theSize.width / 2);
                                        p.y -= (int) (theSize.height / 2);
                                        theDialog.setLocation(p);
                                        theDialog.setVisible(true);
                                    }
                                }
                                publish("Generating Documentation...");
                                ReportingComponent theComponent = null;
                                File theTemplateFile;
                                File theTempFile = new File(tempDir.getText());
                                File theOutputFile;
                                if (reportTypePane.getSelectedIndex() == 0) {
                                    theComponent = ReportingFactory.createDOCXReportingComponent();
                                    theTemplateFile = new File(wordTemplate.getText());
                                    theOutputFile = new File(wordOutput.getText());
                                } else {
                                    theComponent = ReportingFactory.createJasperReportingComponent((JasperExportType<JRExporter>) jasperFormat.getSelectedItem());
                                    theTemplateFile = new File(jasperTemplate.getText());
                                    theOutputFile = new File(jasperOutput.getText());
                                }
                                theComponent.createReport(theTemplateFile, theXMLFile, theTempFile, theOutputFile, true);
                                publish("Finished");
                                JOptionPane.showMessageDialog(MainFrame.this, "Generation ok", "Information", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                LOGGER.error("Error on generation", e);
                                publish("Generation failed!");
                                JOptionPane.showMessageDialog(MainFrame.this, "Generation failed", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            return null;
                        }

                        @Override
                        protected void process(List<String> chunks) {
                            for (String theChunk : chunks) {
                                statusBar.setText(theChunk);
                            }
                        }
                    };
                    theWorker.execute();
                    try {
                        theWorker.get();
                    } catch (Exception e) {
                        LOGGER.error("Error on generation", e);
                        JOptionPane.showMessageDialog(MainFrame.this, "Generation failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            theRunner.start();
        } catch (Exception e) {
            LOGGER.error("Error on generation", e);
            JOptionPane.showMessageDialog(this, "Generation failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSettings() {
        Preferences thePrefs = Preferences.userNodeForPackage(MainFrame.class);
        eapFile.setText(thePrefs.get("EAP", null));
        tempDir.setText(thePrefs.get("TEMPDIR", null));
        wordTemplate.setText(thePrefs.get("TEMPLATE", null));
        wordOutput.setText(thePrefs.get("OUTPUT", null));
        jasperTemplate.setText(thePrefs.get("JTEMPLATE", null));
        jasperOutput.setText(thePrefs.get("JOUTPUT", null));
        String theFormat = thePrefs.get("JFORMAT", null);
        if (!StringUtils.isEmpty(theFormat)) {
            for (JasperExportType<JRRtfExporter> theType : JasperReportingComponent.KNOWN_TYPES) {
                if (theFormat.equals(theType.toString())) {
                    jasperFormat.setSelectedItem(theType);
                }
            }
        }
        reportTypePane.setSelectedIndex(Integer.parseInt(thePrefs.get("TAB", "0")));
    }

    private void saveSettings() {
        Preferences thePrefs = Preferences.userNodeForPackage(MainFrame.class);
        thePrefs.put("EAP", eapFile.getText());
        thePrefs.put("TEMPDIR", tempDir.getText());
        thePrefs.put("TEMPLATE", wordTemplate.getText());
        thePrefs.put("OUTPUT", wordOutput.getText());
        thePrefs.put("JTEMPLATE", jasperTemplate.getText());
        thePrefs.put("JOUTPUT", jasperOutput.getText());
        thePrefs.put("JFORMAT", jasperFormat.getSelectedItem().toString());
        thePrefs.put("TAB", "" + reportTypePane.getSelectedIndex());
    }

    public static void main(String[] args) {
        MainFrame theFrame = new MainFrame();
        theFrame.pack();
        new WindowHelper(theFrame).center();
        theFrame.setVisible(true);
    }
}
