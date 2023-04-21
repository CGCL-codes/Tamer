package de.proteinms.xtandemparser.viewer;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.compomics.util.gui.spectrum.SpectrumPanel;
import com.compomics.util.gui.spectrum.DefaultSpectrumAnnotation;
import com.compomics.util.protein.Header;
import de.proteinms.xtandemparser.interfaces.Modification;
import de.proteinms.xtandemparser.xtandem.*;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * This class provides a basic viewer for the spectra.
 *
 * @author Thilo Muth
 */
public class XTandemViewer extends JFrame {

    /**
     * If set to true, all print outs (both standard and error) will be sent to
     * the ErrorLog.txt file in the Properties folder.
     */
    private static boolean useErrorLog = true;

    public static final String APPTITLE = "X!Tandem Viewer";

    private static final String MODIFICATIONSLEGEND = "  |  <M *> are fixed and <M �> are variable modifications.";

    private String lastSelectedFolder = "user.home";

    private SpectrumPanel spectrumPanel;

    private String iXTandemFileString;

    private HashMap<Integer, ArrayList<Peptide>> peptideMap;

    private HashMap<String, String> proteinLabelMap;

    private HashMap<Integer, ArrayList<Double>> allMzValues, allIntensityValues;

    private HashMap<String, ArrayList<Modification>> allFixMods, allVarMods;

    private HashMap<String, FragmentIon[]> ionsMap;

    private HashMap<Integer, String> accMap;

    private Vector spectraTableColToolTips, spectrumTableColToolTips, spectrumJXTableColToolTips, identificationsJXTableColumnToolTips;

    private HashMap<String, Vector<DefaultSpectrumAnnotation>> allAnnotations;

    private JCheckBox aIonsJCheckBox, bIonsJCheckBox, cIonsJCheckBox, chargeOneJCheckBox, chargeTwoJCheckBox, chargeOverTwoJCheckBox, xIonsJCheckBox, yIonsJCheckBox, zIonsJCheckBox;

    private JLabel modificationDetailsJLabel;

    private JScrollPane jScrollPane1;

    private JScrollPane jScrollPane3;

    private JSeparator jSeparator1, jSeparator2;

    private JXTable identificationsTable, spectraTable, spectrumJXTable;

    private JPanel jPanel1, jPanel2, jPanel3, jPanel4, spectrumJPanel;

    private double ionCoverageErrorMargin = 0.0;

    private ProgressDialog progressDialog;

    private JMenuItem openMenuItem, exitMenuItem, aboutMenuItem, helpMenuItem, exportSpectraTableMenuItem, exportAllIdentificationsMenuItem, exportAllSpectraMenuItem, exportSelectedSpectrumMenuItem;

    private XTandemFile iXTandemFile;

    /**
     * Constructor gets the xml output result file.
     *
     * @param aXTandemXmlFile
     * @param lastSelectedFolder
     */
    public XTandemViewer(String aXTandemXmlFile, String lastSelectedFolder) {
        if (useErrorLog && !getJarFilePath().equalsIgnoreCase(".")) {
            try {
                String path = "" + this.getClass().getProtectionDomain().getCodeSource().getLocation();
                path = path.substring(5, path.lastIndexOf("/"));
                path = path + "/Properties/ErrorLog.txt";
                path = path.replace("%20", " ");
                File file = new File(path);
                System.setOut(new java.io.PrintStream(new FileOutputStream(file, true)));
                System.setErr(new java.io.PrintStream(new FileOutputStream(file, true)));
                if (!file.exists()) {
                    file.createNewFile();
                    FileWriter w = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(w);
                    bw.close();
                    w.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error occured when trying to create the ErrorLog." + "See ../Properties/ErrorLog.txt for more details.", "Error Creating ErrorLog", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        constructMenu();
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/xtandemviewer.gif")));
        spectraTable.getColumn(" ").setMaxWidth(35);
        spectraTable.getColumn(" ").setMinWidth(35);
        spectraTable.getColumn("m/z").setMaxWidth(65);
        spectraTable.getColumn("m/z").setMinWidth(65);
        spectraTable.getColumn("Charge").setMaxWidth(65);
        spectraTable.getColumn("Charge").setMinWidth(65);
        spectraTable.getColumn("Identified").setMaxWidth(80);
        spectraTable.getColumn("Identified").setMinWidth(80);
        spectrumJXTable.getColumn(" ").setMaxWidth(35);
        spectrumJXTable.getColumn(" ").setMinWidth(35);
        identificationsTable.getColumn(" ").setMaxWidth(35);
        identificationsTable.getColumn(" ").setMinWidth(35);
        identificationsTable.getColumn("Start").setMaxWidth(45);
        identificationsTable.getColumn("Start").setMinWidth(45);
        identificationsTable.getColumn("End").setMaxWidth(45);
        identificationsTable.getColumn("End").setMinWidth(45);
        identificationsTable.getColumn("Exp. Mass").setMaxWidth(75);
        identificationsTable.getColumn("Exp. Mass").setMinWidth(75);
        identificationsTable.getColumn("Theo. Mass").setMaxWidth(75);
        identificationsTable.getColumn("Theo. Mass").setMinWidth(75);
        identificationsTable.getColumn("E-value").setMinWidth(75);
        identificationsTable.getColumn("E-value").setMaxWidth(75);
        identificationsTable.getColumn("Accession").setPreferredWidth(10);
        spectraTable.getTableHeader().setReorderingAllowed(false);
        spectrumJXTable.getTableHeader().setReorderingAllowed(false);
        identificationsTable.getTableHeader().setReorderingAllowed(false);
        spectraTableColToolTips = new Vector();
        spectraTableColToolTips.add("Spectrum Number");
        spectraTableColToolTips.add("Spectrum File Name");
        spectraTableColToolTips.add("Precursor Mass Over Charge Ratio");
        spectraTableColToolTips.add("Precursor Charge");
        spectraTableColToolTips.add("Spectrum Identified");
        spectrumTableColToolTips = new Vector();
        spectrumTableColToolTips.add(null);
        spectrumTableColToolTips.add("Mass Over Charge Ratio");
        spectrumTableColToolTips.add("Intensity");
        spectrumJXTableColToolTips = new Vector();
        spectrumJXTableColToolTips.add(null);
        spectrumJXTableColToolTips.add("Mass Over Charge Ratio");
        spectrumJXTableColToolTips.add("Intensity");
        identificationsJXTableColumnToolTips = new Vector();
        identificationsJXTableColumnToolTips.add("Spectrum Number");
        identificationsJXTableColumnToolTips.add("Peptide Sequence");
        identificationsJXTableColumnToolTips.add("Modified Peptide Sequence");
        identificationsJXTableColumnToolTips.add("Peptide Start Index");
        identificationsJXTableColumnToolTips.add("Peptide End Index");
        identificationsJXTableColumnToolTips.add("Experimental Mass");
        identificationsJXTableColumnToolTips.add("Theoretical Mass");
        identificationsJXTableColumnToolTips.add("E-value");
        identificationsJXTableColumnToolTips.add("Protein Accession Number");
        identificationsJXTableColumnToolTips.add("Protein Description");
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setVisible(true);
        insertFiles(aXTandemXmlFile, lastSelectedFolder);
    }

    /**
     * Returns the path to the jar file.
     *
     * @return the path to the jar file
     */
    private String getJarFilePath() {
        String path = this.getClass().getResource("XTandemViewer.class").getPath();
        if (path.lastIndexOf("/xtandem-parser-") != -1) {
            path = path.substring(5, path.lastIndexOf("/xtandem-parser-"));
            path = path.replace("%20", " ");
        } else {
            path = ".";
        }
        return path;
    }

    /**
     * Constructing the menu at the top of the frame
     */
    private void constructMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
        menuBar.putClientProperty(PlasticLookAndFeel.IS_3D_KEY, Boolean.FALSE);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        JMenu exportMenu = new JMenu("Export");
        exportMenu.setMnemonic('E');
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        menuBar.add(fileMenu);
        menuBar.add(exportMenu);
        menuBar.add(helpMenu);
        openMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        helpMenuItem = new JMenuItem();
        aboutMenuItem = new JMenuItem();
        exportSpectraTableMenuItem = new JMenuItem();
        exportAllIdentificationsMenuItem = new JMenuItem();
        exportAllSpectraMenuItem = new JMenuItem();
        exportSelectedSpectrumMenuItem = new JMenuItem();
        exportSpectraTableMenuItem.setMnemonic('T');
        exportSpectraTableMenuItem.setText("Spectra Files Table");
        exportSpectraTableMenuItem.setToolTipText("Export the Spectra Files Table as Tab Delimited Text File");
        exportSpectraTableMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportSpectraFilesTable(evt);
            }
        });
        exportMenu.add(exportSpectraTableMenuItem);
        exportAllIdentificationsMenuItem.setMnemonic('I');
        exportAllIdentificationsMenuItem.setText("All Identifications (all hits)");
        exportAllIdentificationsMenuItem.setToolTipText("Export All Identifications (all hits) as Tab Delimited Text File");
        exportAllIdentificationsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportAllIdentifications(evt);
            }
        });
        exportMenu.add(exportAllIdentificationsMenuItem);
        exportSelectedSpectrumMenuItem.setMnemonic('S');
        exportSelectedSpectrumMenuItem.setText("Selected Spectrum");
        exportSelectedSpectrumMenuItem.setToolTipText("Export the Selected Spectrum as Tab Delimited Text File");
        exportSelectedSpectrumMenuItem.setEnabled(false);
        exportSelectedSpectrumMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportSelectedSpectrum(evt);
            }
        });
        exportMenu.add(exportSelectedSpectrumMenuItem);
        exportAllSpectraMenuItem.setMnemonic('S');
        exportAllSpectraMenuItem.setText("All Spectra");
        exportAllSpectraMenuItem.setToolTipText("Export all the Spectra as DTA Files");
        exportAllSpectraMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportAllSpectra(evt);
            }
        });
        exportMenu.add(exportAllSpectraMenuItem);
        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setMnemonic('H');
        helpMenuItem.setText("Help");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpTriggered();
            }
        });
        helpMenu.add(helpMenuItem);
        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aboutTriggered();
            }
        });
        helpMenu.add(aboutMenuItem);
        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setMnemonic('O');
        openMenuItem.setText("Open");
        openMenuItem.setToolTipText("Open a New X!Tandem XML File");
        openMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);
        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.setToolTipText("Exit XTandem Viewer");
        exitMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        setJMenuBar(menuBar);
    }

    /**
     * The method that builds the help frame.
     */
    private void helpTriggered() {
        setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpFrame(this, getClass().getResource("/html/help.html"));
        setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }

    /**
     * The method that builds the about dialog.
     */
    private void aboutTriggered() {
        StringBuffer tMsg = new StringBuffer();
        tMsg.append(APPTITLE + " " + new Properties().getVersion());
        tMsg.append("\n");
        tMsg.append("\n");
        tMsg.append("The XTandem parser is a Java project for extracting information from X!Tandem output xml files.");
        tMsg.append("\n");
        tMsg.append("\n");
        tMsg.append("The latest version is available at http://xtandem-parser.googlecode.com");
        tMsg.append("\n");
        tMsg.append("\n");
        tMsg.append("If any questions arise, contact the corresponding author: ");
        tMsg.append("\n");
        tMsg.append("Thilo.Muth@uni-jena.de");
        tMsg.append("\n");
        tMsg.append("\n");
        tMsg.append("");
        tMsg.append("");
        JOptionPane.showMessageDialog(this, tMsg, "About " + APPTITLE + " " + new Properties().getVersion(), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method initializes all the gui components.
     */
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        spectraTable = new JXTable() {

            @Override
            protected JXTableHeader createDefaultTableHeader() {
                return new JXTableHeader(columnModel) {

                    @Override
                    public String getToolTipText(MouseEvent e) {
                        String tip;
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        tip = (String) spectraTableColToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        jPanel2 = new javax.swing.JPanel();
        modificationDetailsJLabel = new javax.swing.JLabel();
        JLabel jLabel1 = new JLabel();
        JScrollPane jScrollPane4 = new JScrollPane();
        identificationsTable = new JXTable() {

            @Override
            protected JXTableHeader createDefaultTableHeader() {
                return new JXTableHeader(columnModel) {

                    @Override
                    public String getToolTipText(MouseEvent e) {
                        String tip;
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        tip = (String) identificationsJXTableColumnToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        jPanel3 = new JPanel();
        spectrumJPanel = new JPanel();
        jPanel4 = new JPanel();
        aIonsJCheckBox = new JCheckBox();
        bIonsJCheckBox = new JCheckBox();
        cIonsJCheckBox = new JCheckBox();
        jSeparator1 = new JSeparator();
        yIonsJCheckBox = new JCheckBox();
        xIonsJCheckBox = new JCheckBox();
        zIonsJCheckBox = new JCheckBox();
        jSeparator2 = new JSeparator();
        chargeOneJCheckBox = new JCheckBox();
        chargeTwoJCheckBox = new JCheckBox();
        chargeOverTwoJCheckBox = new JCheckBox();
        jScrollPane1 = new JScrollPane();
        spectrumJXTable = new JXTable() {

            @Override
            protected JXTableHeader createDefaultTableHeader() {
                return new JXTableHeader(columnModel) {

                    @Override
                    public String getToolTipText(MouseEvent e) {
                        String tip;
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        tip = (String) spectrumJXTableColToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Spectra Files"));
        spectraTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { " ", "Filename", "m/z", "Charge", "Identified" }) {

            Class[] types = new Class[] { Integer.class, String.class, Double.class, Integer.class, Boolean.class };

            boolean[] canEdit = new boolean[] { false, false, false, false, false };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        spectraTable.setOpaque(false);
        spectraTable.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                spectraJXTableKeyReleased(evt);
            }
        });
        spectraTable.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                spectraJXTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(spectraTable);
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE).addContainerGap()));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Identifications"));
        modificationDetailsJLabel.setFont(modificationDetailsJLabel.getFont().deriveFont((modificationDetailsJLabel.getFont().getStyle() | java.awt.Font.PLAIN)));
        jLabel1.setFont(jLabel1.getFont().deriveFont((jLabel1.getFont().getStyle() | java.awt.Font.PLAIN)));
        jLabel1.setText("Legend:   ");
        identificationsTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { " ", "Sequence", "Modified Sequence", "Start", "End", "Exp. Mass", "Theo. Mass", "E-value", "Accession", "Description" }) {

            Class[] types = new Class[] { Integer.class, String.class, String.class, Integer.class, Integer.class, Double.class, Double.class, Float.class, Float.class, String.class, String.class, String.class };

            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        identificationsTable.setOpaque(false);
        jScrollPane4.setViewportView(identificationsTable);
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(modificationDetailsJLabel)).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup().add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(modificationDetailsJLabel))));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Spectrum"));
        spectrumJPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        spectrumJPanel.setLayout(new javax.swing.BoxLayout(spectrumJPanel, javax.swing.BoxLayout.LINE_AXIS));
        aIonsJCheckBox.setSelected(true);
        aIonsJCheckBox.setText("a");
        aIonsJCheckBox.setToolTipText("Show a-ions");
        aIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        aIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        aIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        aIonsJCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                aIonsJCheckBoxActionPerformed(evt);
            }
        });
        bIonsJCheckBox.setSelected(true);
        bIonsJCheckBox.setText("b");
        bIonsJCheckBox.setToolTipText("Show b-ions");
        bIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        bIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        bIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        bIonsJCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                bIonsJCheckBoxActionPerformed();
            }
        });
        cIonsJCheckBox.setSelected(true);
        cIonsJCheckBox.setText("c");
        cIonsJCheckBox.setToolTipText("Show c-ions");
        cIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        cIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        cIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        cIonsJCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                cIonsJCheckBoxActionPerformed();
            }
        });
        yIonsJCheckBox.setSelected(true);
        yIonsJCheckBox.setText("y");
        yIonsJCheckBox.setToolTipText("Show y-ions");
        yIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        yIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        yIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        yIonsJCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                yIonsJCheckBoxActionPerformed();
            }
        });
        xIonsJCheckBox.setSelected(true);
        xIonsJCheckBox.setText("x");
        xIonsJCheckBox.setToolTipText("Show x-ions");
        xIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        xIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        xIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        xIonsJCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                xIonsJCheckBoxActionPerformed();
            }
        });
        zIonsJCheckBox.setSelected(true);
        zIonsJCheckBox.setText("z");
        zIonsJCheckBox.setToolTipText("Show z-ions");
        zIonsJCheckBox.setMaximumSize(new Dimension(39, 23));
        zIonsJCheckBox.setMinimumSize(new Dimension(39, 23));
        zIonsJCheckBox.setPreferredSize(new Dimension(39, 23));
        zIonsJCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                zIonsJCheckBoxActionPerformed();
            }
        });
        chargeOneJCheckBox.setSelected(true);
        chargeOneJCheckBox.setText("+");
        chargeOneJCheckBox.setToolTipText("Show ions with charge 1");
        chargeOneJCheckBox.setMaximumSize(new Dimension(39, 23));
        chargeOneJCheckBox.setMinimumSize(new Dimension(39, 23));
        chargeOneJCheckBox.setPreferredSize(new Dimension(39, 23));
        chargeOneJCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                chargeOneJCheckBoxActionPerformed();
            }
        });
        chargeTwoJCheckBox.setSelected(true);
        chargeTwoJCheckBox.setText("++");
        chargeTwoJCheckBox.setToolTipText("Show ions with charge 2");
        chargeTwoJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        chargeTwoJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        chargeTwoJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        chargeTwoJCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                chargeTwoJCheckBoxActionPerformed();
            }
        });
        chargeOverTwoJCheckBox.setSelected(true);
        chargeOverTwoJCheckBox.setText(">2");
        chargeOverTwoJCheckBox.setToolTipText("Show ions with charge >2");
        chargeOverTwoJCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                chargeOverTwoJCheckBoxActionPerformed();
            }
        });
        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel4Layout.createSequentialGroup().addContainerGap().add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(yIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup().add(chargeOneJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE).add(2, 2, 2)).add(org.jdesktop.layout.GroupLayout.TRAILING, zIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE).add(xIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(org.jdesktop.layout.GroupLayout.TRAILING, chargeTwoJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, chargeOverTwoJCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(bIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE).add(aIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE).add(cIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE).add(jPanel4Layout.createSequentialGroup().add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE).addContainerGap()).add(jPanel4Layout.createSequentialGroup().add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE).addContainerGap()))));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel4Layout.createSequentialGroup().addContainerGap().add(aIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(bIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(13, 13, 13).add(xIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(yIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(zIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(12, 12, 12).add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chargeOneJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chargeTwoJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chargeOverTwoJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(13, 13, 13)));
        spectrumJXTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { " ", "m/z", "Intensity" }) {

            Class[] types = new Class[] { java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class };

            boolean[] canEdit = new boolean[] { false, false, false };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        spectrumJXTable.setOpaque(false);
        jScrollPane1.setViewportView(spectrumJXTable);
        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE).add(jPanel3Layout.createSequentialGroup().add(spectrumJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(spectrumJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE).addContainerGap()));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(18, 18, 18).add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    /**
     * ToDo: JavaDoc missing...
     *
     * @param aXTandemFile
     * @param lastSelectedFolder
     */
    void insertFiles(String aXTandemFile, String lastSelectedFolder) {
        iXTandemFileString = aXTandemFile;
        progressDialog = new ProgressDialog(this);
        this.lastSelectedFolder = lastSelectedFolder;
        setTitle(APPTITLE + " " + new Properties().getVersion() + "  ---  " + new File(iXTandemFileString).getPath());
        final Thread t = new Thread(new Runnable() {

            public void run() {
                progressDialog.setTitle("Parsing XML File. Please Wait...");
                progressDialog.setIndeterminate(true);
                progressDialog.setVisible(true);
            }
        }, "ProgressDialog");
        t.start();
        new Thread("ParserThread") {

            @Override
            public void run() {
                spectraTable.setSortable(false);
                while (spectraTable.getModel().getRowCount() > 0) {
                    ((DefaultTableModel) spectraTable.getModel()).removeRow(0);
                }
                while (spectrumJXTable.getModel().getRowCount() > 0) {
                    ((DefaultTableModel) spectrumJXTable.getModel()).removeRow(0);
                }
                while (identificationsTable.getModel().getRowCount() > 0) {
                    ((DefaultTableModel) identificationsTable.getModel()).removeRow(0);
                }
                modificationDetailsJLabel.setText("");
                while (spectrumJPanel.getComponents().length > 0) {
                    spectrumJPanel.remove(0);
                }
                spectrumJPanel.validate();
                spectrumJPanel.repaint();
                try {
                    iXTandemFile = new XTandemFile(iXTandemFileString);
                } catch (OutOfMemoryError error) {
                    Runtime.getRuntime().gc();
                    JOptionPane.showMessageDialog(null, "The task used up all the available memory and had to be stopped.\n" + "Memory boundaries are set in ../Properties/JavaOptions.txt.", "Out of Memory Error", JOptionPane.ERROR_MESSAGE);
                    error.printStackTrace();
                    System.exit(0);
                } catch (SAXException saxException) {
                    saxException.getMessage();
                    JOptionPane.showMessageDialog(null, "Error during parsing the xml file!\n" + saxException.getMessage() + "\n" + "Please load xml file in correct format...", "Parser error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                ionCoverageErrorMargin = Parameters.FRAGMENTMASSERROR;
                peptideMap = new HashMap<Integer, ArrayList<Peptide>>();
                proteinLabelMap = new HashMap<String, String>();
                accMap = new HashMap<Integer, String>();
                allMzValues = new HashMap<Integer, ArrayList<Double>>();
                allIntensityValues = new HashMap<Integer, ArrayList<Double>>();
                allFixMods = new HashMap<String, ArrayList<Modification>>();
                allVarMods = new HashMap<String, ArrayList<Modification>>();
                ionsMap = new HashMap<String, FragmentIon[]>();
                Iterator<Spectrum> iter = iXTandemFile.getSpectraIterator();
                PeptideMap pepMap = iXTandemFile.getPeptideMap();
                ProteinMap protMap = iXTandemFile.getProteinMap();
                while (iter.hasNext()) {
                    Spectrum spectrum = iter.next();
                    int spectrumNumber = spectrum.getSpectrumNumber();
                    ArrayList<Peptide> pepList = pepMap.getAllPeptides(spectrumNumber);
                    for (Peptide peptide : pepList) {
                        List<Domain> domainList = peptide.getDomains();
                        for (Domain domain : domainList) {
                            Protein protein = protMap.getProtein(domain.getProteinKey());
                            if (protein != null) {
                                String protAccession = protein.getLabel();
                                proteinLabelMap.put(domain.getDomainKey(), protAccession);
                            }
                            ArrayList<Modification> fixModList = iXTandemFile.getModificationMap().getFixedModifications(domain.getDomainKey());
                            ArrayList<Modification> varModList = iXTandemFile.getModificationMap().getVariableModifications(domain.getDomainKey());
                            allFixMods.put(domain.getDomainKey(), fixModList);
                            allVarMods.put(domain.getDomainKey(), varModList);
                            Vector IonVector = iXTandemFile.getFragmentIonsForPeptide(peptide, domain);
                            for (int i = 0; i < IonVector.size(); i++) {
                                FragmentIon[] ions = (FragmentIon[]) IonVector.get(i);
                                ionsMap.put(domain.getDomainKey() + "_" + i, ions);
                            }
                        }
                    }
                    SupportData supportData = iXTandemFile.getSupportData(spectrumNumber);
                    peptideMap.put(spectrumNumber, pepList);
                    String label = supportData.getFragIonSpectrumDescription();
                    int precursorCharge = spectrum.getPrecursorCharge();
                    double precursorMh = spectrum.getPrecursorMh();
                    String accession = spectrum.getLabel();
                    boolean identified;
                    if (pepList.isEmpty()) {
                        identified = false;
                    } else {
                        identified = true;
                    }
                    accMap.put(spectrumNumber, accession);
                    ((DefaultTableModel) spectraTable.getModel()).addRow(new Object[] { spectrumNumber, label, precursorMh, precursorCharge, identified });
                    ArrayList<Double> mzValues;
                    ArrayList<Double> intensityValues;
                    mzValues = supportData.getXValuesFragIonMass2Charge();
                    intensityValues = supportData.getYValuesFragIonMass2Charge();
                    allMzValues.put(new Integer(spectrumNumber), mzValues);
                    allIntensityValues.put(new Integer(spectrumNumber), intensityValues);
                }
                spectraTable.setSortable(true);
                progressDialog.setVisible(false);
                progressDialog.dispose();
                if (spectraTable.getRowCount() > 0) {
                    spectraTable.setRowSelectionInterval(0, 0);
                    spectraJXTableMouseClicked(null);
                }
            }
        }.start();
    }

    /**
     * This method filters the annotations.
     *
     * @param annotations the annotations to be filtered
     * @return the filtered annotations
     */
    private Vector<DefaultSpectrumAnnotation> filterAnnotations(Vector<DefaultSpectrumAnnotation> annotations) {
        Vector<DefaultSpectrumAnnotation> filteredAnnotations = new Vector();
        for (int i = 0; i < annotations.size(); i++) {
            String currentLabel = annotations.get(i).getLabel();
            boolean useAnnotation = true;
            if (currentLabel.lastIndexOf("a") != -1) {
                if (!aIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("b") != -1) {
                if (!bIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("c") != -1) {
                if (!cIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("x") != -1) {
                if (!xIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("y") != -1) {
                if (!yIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("z") != -1) {
                if (!zIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            }
            if (useAnnotation) {
                if (currentLabel.lastIndexOf("+") == -1) {
                    if (!chargeOneJCheckBox.isSelected()) {
                        useAnnotation = false;
                    }
                } else if (currentLabel.lastIndexOf("+++") != -1) {
                    if (!chargeOverTwoJCheckBox.isSelected()) {
                        useAnnotation = false;
                    }
                } else if (currentLabel.lastIndexOf("++") != -1) {
                    if (!chargeTwoJCheckBox.isSelected()) {
                        useAnnotation = false;
                    }
                }
            }
            if (useAnnotation) {
                filteredAnnotations.add(annotations.get(i));
            }
        }
        return filteredAnnotations;
    }

    /**
     * Opens the file selector dialog for loading another X!Tandem xml file.
     *
     * @param evt
     */
    private void openActionPerformed(ActionEvent evt) {
        new FileSelector(this, APPTITLE);
    }

    /**
     * @see #aIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent)
     */
    private void bIonsJCheckBoxActionPerformed() {
        aIonsJCheckBoxActionPerformed(null);
    }

    /**
     * @see #aIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent)
     */
    private void cIonsJCheckBoxActionPerformed() {
        aIonsJCheckBoxActionPerformed(null);
    }

    /**
     * @see #aIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent)
     */
    private void yIonsJCheckBoxActionPerformed() {
        aIonsJCheckBoxActionPerformed(null);
    }

    /**
     * @see #aIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent)
     */
    private void xIonsJCheckBoxActionPerformed() {
        aIonsJCheckBoxActionPerformed(null);
    }

    /**
     * @see #aIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent)
     */
    private void zIonsJCheckBoxActionPerformed() {
        aIonsJCheckBoxActionPerformed(null);
    }

    /**
     * @see #aIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent)
     */
    private void chargeOneJCheckBoxActionPerformed() {
        aIonsJCheckBoxActionPerformed(null);
    }

    /**
     * @see #aIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent)
     */
    private void chargeTwoJCheckBoxActionPerformed() {
        aIonsJCheckBoxActionPerformed(null);
    }

    /**
     * @see #aIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent)
     */
    private void chargeOverTwoJCheckBoxActionPerformed() {
        aIonsJCheckBoxActionPerformed(null);
    }

    /**
     * Updates the ion coverage annotations
     *
     * @param evt
     */
    private void aIonsJCheckBoxActionPerformed(ActionEvent evt) {
        if (identificationsTable.getRowCount() > 0) {
            int selectedRow = 0;
            if (identificationsTable.getRowCount() > 1 && identificationsTable.getSelectedRow() != -1) {
                selectedRow = identificationsTable.getSelectedRow();
            }
            Vector<DefaultSpectrumAnnotation> currentAnnotations = allAnnotations.get(identificationsTable.getValueAt(selectedRow, 1) + "_" + identificationsTable.getValueAt(selectedRow, 7));
            spectrumPanel.setAnnotations(filterAnnotations(currentAnnotations));
            spectrumPanel.validate();
            spectrumPanel.repaint();
        }
    }

    /**
     * @see #spectraJXTableMouseClicked(java.awt.event.MouseEvent)
     */
    private void spectraJXTableKeyReleased(KeyEvent evt) {
        spectraJXTableMouseClicked(null);
    }

    /**
     * Update the tables based on the spectrum selected.
     *
     * @param evt
     */
    private void spectraJXTableMouseClicked(MouseEvent evt) {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        int row = spectraTable.getSelectedRow();
        if (row != -1) {
            List<Double> mzValues = allMzValues.get((Integer) spectraTable.getValueAt(row, 0));
            List<Double> intensityValues = allIntensityValues.get((Integer) spectraTable.getValueAt(row, 0));
            while (spectrumJXTable.getRowCount() > 0) {
                ((DefaultTableModel) spectrumJXTable.getModel()).removeRow(0);
            }
            spectrumJXTable.scrollRectToVisible(spectrumJXTable.getCellRect(0, 0, false));
            while (spectrumJPanel.getComponents().length > 0) {
                spectrumJPanel.remove(0);
            }
            double[] mzValuesAsDouble = new double[mzValues.size()];
            double[] intensityValuesAsDouble = new double[mzValues.size()];
            for (int i = 0; i < mzValues.size(); i++) {
                ((DefaultTableModel) spectrumJXTable.getModel()).addRow(new Object[] { new Integer(i + 1), mzValues.get(i), intensityValues.get(i) });
                mzValuesAsDouble[i] = mzValues.get(i);
                intensityValuesAsDouble[i] = intensityValues.get(i);
            }
            exportSelectedSpectrumMenuItem.setEnabled(true);
            spectrumPanel = new SpectrumPanel(mzValuesAsDouble, intensityValuesAsDouble, ((Double) spectraTable.getValueAt(row, 2)), "" + spectraTable.getValueAt(row, 3), ((String) spectraTable.getValueAt(row, 1)), 60, false);
            spectrumJPanel.add(spectrumPanel);
            spectrumJPanel.validate();
            spectrumJPanel.repaint();
            while (identificationsTable.getRowCount() > 0) {
                ((DefaultTableModel) identificationsTable.getModel()).removeRow(0);
            }
            allAnnotations = new HashMap();
            modificationDetailsJLabel.setText("");
            if (peptideMap.get((Integer) spectraTable.getValueAt(row, 0)) != null) {
                ArrayList<Peptide> pepList = peptideMap.get((Integer) spectraTable.getValueAt(row, 0));
                Iterator pepIter = pepList.iterator();
                String modificationDetails = "";
                while (pepIter.hasNext()) {
                    Peptide peptide = (Peptide) pepIter.next();
                    List<Domain> domainList = peptide.getDomains();
                    for (Domain domain : domainList) {
                        String sequence = domain.getDomainSequence();
                        String[] modifications = new String[sequence.length()];
                        for (int i = 0; i < modifications.length; i++) {
                            modifications[i] = "";
                        }
                        String modifiedSequence = "";
                        String nTerminal = "";
                        String cTerminal = "";
                        ArrayList<Modification> fixedModList = allFixMods.get(domain.getDomainKey());
                        ArrayList<Modification> varModList = allVarMods.get(domain.getDomainKey());
                        if (fixedModList != null) {
                            for (int i = 0; i < fixedModList.size(); i++) {
                                FixedModification fixMod = (FixedModification) fixedModList.get(i);
                                int[] modRes = new int[domain.getDomainSequence().length()];
                                int modIndex = Integer.parseInt(fixMod.getLocation()) - domain.getDomainStart();
                                modRes[modIndex] = fixMod.getNumber();
                                for (int j = 0; j < modRes.length; j++) {
                                    if (modRes[j] > 0) {
                                        modifications[j] += "<" + "M" + modRes[j] + "*" + ">";
                                    }
                                }
                            }
                        }
                        if (varModList != null) {
                            for (int i = 0; i < varModList.size(); i++) {
                                VariableModification varMod = (VariableModification) varModList.get(i);
                                int[] modRes = new int[domain.getDomainSequence().length()];
                                int modIndex = Integer.parseInt(varMod.getLocation()) - domain.getDomainStart();
                                modRes[modIndex] = varMod.getNumber();
                                for (int j = 0; j < modRes.length; j++) {
                                    if (modRes[j] > 0) {
                                        modifications[j] += "<" + "M" + modRes[j] + "�" + ">";
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < modifications.length; i++) {
                            modifiedSequence += sequence.substring(i, i + 1);
                            if (!modifications[i].equalsIgnoreCase("")) {
                                String[] residues = modifications[i].split(">");
                                for (int j = 0; j < residues.length; j++) {
                                    String currentMod = residues[j] + ">";
                                    int fixModIndex = 0;
                                    int varModIndex = 0;
                                    if (currentMod.length() > 0) {
                                        if (currentMod.contains("*")) {
                                            fixModIndex = (Integer.parseInt(currentMod.substring(2, 3)) - 1);
                                        } else if (currentMod.contains("�")) {
                                            varModIndex = (Integer.parseInt(currentMod.substring(2, 3)) - 1);
                                        }
                                    }
                                    if (modificationDetails.lastIndexOf(currentMod) == -1) {
                                        if (fixedModList.size() > 0 && currentMod.contains("*")) {
                                            modificationDetails += currentMod + " " + fixedModList.get(fixModIndex).getName() + ", ";
                                        } else if (varModList.size() > 0 && currentMod.contains("�")) {
                                            modificationDetails += currentMod + " " + varModList.get(varModIndex).getName() + ", ";
                                        }
                                        modifiedSequence += currentMod;
                                    } else {
                                        modifiedSequence += currentMod;
                                    }
                                }
                            }
                        }
                        if (nTerminal.length() == 0) {
                            nTerminal = "NH2-";
                        } else {
                            nTerminal += "-";
                        }
                        if (cTerminal.length() == 0) {
                            cTerminal = "-COOH";
                        } else {
                            cTerminal = "-" + cTerminal;
                        }
                        int[][] ionCoverage = new int[sequence.length() + 1][12];
                        Vector<DefaultSpectrumAnnotation> currentAnnotations = new Vector();
                        for (int i = 0; i < 12; i++) {
                            FragmentIon[] ions = ionsMap.get(domain.getDomainKey() + "_" + i);
                            for (FragmentIon ion : ions) {
                                int ionNumber = ion.getNumber();
                                int ionType = ion.getType();
                                double mzValue = ion.getMZ();
                                Color color;
                                if (i % 2 == 0) {
                                    color = Color.BLUE;
                                } else {
                                    color = Color.BLACK;
                                }
                                if (ionType == FragmentIon.A_ION) {
                                    ionCoverage[ionNumber][0]++;
                                }
                                if (ionType == FragmentIon.AH2O_ION) {
                                    ionCoverage[ionNumber][1]++;
                                }
                                if (ionType == FragmentIon.ANH3_ION) {
                                    ionCoverage[ionNumber][2]++;
                                }
                                if (ionType == FragmentIon.B_ION) {
                                    ionCoverage[ionNumber][3]++;
                                }
                                if (ionType == FragmentIon.BH2O_ION) {
                                    ionCoverage[ionNumber][4]++;
                                }
                                if (ionType == FragmentIon.BNH3_ION) {
                                    ionCoverage[ionNumber][5]++;
                                }
                                if (ionType == FragmentIon.C_ION) {
                                    ionCoverage[ionNumber][6]++;
                                }
                                if (ionType == FragmentIon.X_ION) {
                                    ionCoverage[ionNumber][7]++;
                                }
                                if (ionType == FragmentIon.Y_ION) {
                                    ionCoverage[ionNumber][8]++;
                                }
                                if (ionType == FragmentIon.YH2O_ION) {
                                    ionCoverage[ionNumber][9]++;
                                }
                                if (ionType == FragmentIon.YNH3_ION) {
                                    ionCoverage[ionNumber][10]++;
                                }
                                if (ionType == FragmentIon.Z_ION) {
                                    ionCoverage[ionNumber][11]++;
                                }
                                String ionDesc = ion.getLetter();
                                if (ionNumber > 0) {
                                    ionDesc += ionNumber;
                                }
                                if (ion.getCharge() > 1) {
                                    for (int j = 0; j < ion.getCharge(); j++) {
                                        ionDesc += "+";
                                    }
                                }
                                currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionDesc));
                            }
                        }
                        allAnnotations.put((sequence + "_" + domain.getDomainExpect()), currentAnnotations);
                        if (allAnnotations.size() == 1) {
                            spectrumPanel.setAnnotations(filterAnnotations(currentAnnotations));
                            spectrumPanel.validate();
                            spectrumPanel.repaint();
                        }
                        int[][] ionCoverageProcessed = new int[sequence.length()][2];
                        if (ionCoverage[1][3] > 0 || ionCoverage[1][4] > 0 || ionCoverage[1][5] > 0) {
                            ionCoverageProcessed[0][0] = 1;
                        }
                        if (ionCoverage[1][8] > 0 || ionCoverage[1][9] > 0 || ionCoverage[1][10] > 0) {
                            ionCoverageProcessed[ionCoverage.length - 2][1] = 1;
                        }
                        if (ionCoverage[ionCoverage.length - 1][3] > 0 || ionCoverage[ionCoverage.length - 1][4] > 0 || ionCoverage[ionCoverage.length - 1][5] > 0) {
                            ionCoverageProcessed[ionCoverage.length - 2][0] = 1;
                        }
                        if (ionCoverage[ionCoverage.length - 1][8] > 0 || ionCoverage[ionCoverage.length - 1][9] > 0 || ionCoverage[ionCoverage.length - 1][10] > 0) {
                            ionCoverageProcessed[0][1] = 1;
                        }
                        for (int i = 2; i < ionCoverage.length - 1; i++) {
                            if (ionCoverage[i][3] > 0 && ionCoverage[i - 1][3] > 0 || ionCoverage[i][4] > 0 && ionCoverage[i - 1][4] > 0 || ionCoverage[i][5] > 0 && ionCoverage[i - 1][5] > 0) {
                                ionCoverageProcessed[i - 1][0] = 1;
                            } else {
                                ionCoverageProcessed[i - 1][0] = 0;
                            }
                            if (ionCoverage[i][8] > 0 && ionCoverage[i - 1][8] > 0 || ionCoverage[i][9] > 0 && ionCoverage[i - 1][9] > 0 || ionCoverage[i][10] > 0 && ionCoverage[i - 1][10] > 0) {
                                ionCoverageProcessed[ionCoverage.length - 1 - i][1] = 1;
                            } else {
                                ionCoverageProcessed[ionCoverage.length - 1 - i][1] = 0;
                            }
                        }
                        String modifiedSequenceColorCoded = "<html>";
                        if (!nTerminal.startsWith("<")) {
                            modifiedSequenceColorCoded += nTerminal;
                        } else {
                            modifiedSequenceColorCoded += "&lt;";
                            modifiedSequenceColorCoded += nTerminal.substring(1, nTerminal.length() - 2);
                            modifiedSequenceColorCoded += "&gt;-";
                        }
                        int aminoAcidCounter = 0;
                        for (int i = 0; i < modifiedSequence.length(); i++) {
                            if (modifiedSequence.charAt(i) == '<') {
                                if (ionCoverageProcessed[aminoAcidCounter - 1][0] > 0) {
                                    modifiedSequenceColorCoded += "<u>";
                                }
                                if (ionCoverageProcessed[aminoAcidCounter - 1][1] > 0) {
                                    modifiedSequenceColorCoded += "<font color=\"red\">";
                                }
                                modifiedSequenceColorCoded += "&lt;";
                                i++;
                                while (modifiedSequence.charAt(i) != '>') {
                                    modifiedSequenceColorCoded += modifiedSequence.charAt(i++);
                                }
                                modifiedSequenceColorCoded += "&gt;";
                                if (ionCoverageProcessed[aminoAcidCounter - 1][0] > 0) {
                                    modifiedSequenceColorCoded += "</u>";
                                }
                                if (ionCoverageProcessed[aminoAcidCounter - 1][1] > 0) {
                                    modifiedSequenceColorCoded += "</font>";
                                }
                            } else {
                                if (ionCoverageProcessed[aminoAcidCounter][0] > 0) {
                                    modifiedSequenceColorCoded += "<u>";
                                }
                                if (ionCoverageProcessed[aminoAcidCounter][1] > 0) {
                                    modifiedSequenceColorCoded += "<font color=\"red\">";
                                }
                                modifiedSequenceColorCoded += modifiedSequence.charAt(i);
                                if (ionCoverageProcessed[aminoAcidCounter][0] > 0) {
                                    modifiedSequenceColorCoded += "</u>";
                                }
                                if (ionCoverageProcessed[aminoAcidCounter][1] > 0) {
                                    modifiedSequenceColorCoded += "</font>";
                                }
                                aminoAcidCounter++;
                            }
                            modifiedSequenceColorCoded += "<font color=\"black\">";
                        }
                        if (!cTerminal.startsWith("-<")) {
                            modifiedSequenceColorCoded += cTerminal;
                        } else {
                            modifiedSequenceColorCoded += "-&lt;";
                            modifiedSequenceColorCoded += cTerminal.substring(2, cTerminal.length() - 1);
                            modifiedSequenceColorCoded += "&gt;";
                        }
                        modifiedSequenceColorCoded += "</html>";
                        double theoMass = (domain.getDomainMh() + domain.getDomainDeltaMh());
                        Header header = Header.parseFromFASTA(proteinLabelMap.get(domain.getDomainKey()));
                        String accession = header.getAccession();
                        String description = header.getDescription();
                        ((DefaultTableModel) identificationsTable.getModel()).addRow(new Object[] { (Integer) spectraTable.getValueAt(row, 0), sequence, modifiedSequenceColorCoded, domain.getDomainStart(), domain.getDomainEnd(), new Double(domain.getDomainMh()), new Double(theoMass), new Float(domain.getDomainExpect()), accession, description });
                    }
                }
                if (modificationDetails.endsWith(", ")) {
                    modificationDetails = modificationDetails.substring(0, modificationDetails.length() - 2);
                }
                if (modificationDetails.length() > 0) {
                    modificationDetailsJLabel.setText(modificationDetails + MODIFICATIONSLEGEND);
                }
                if (identificationsTable.getRowCount() > 1) {
                    identificationsTable.setRowSelectionInterval(0, 0);
                }
            }
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Method for exporting the contents of the spectra files table.
     *
     * @param evt
     */
    private void exportSpectraFilesTable(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(lastSelectedFolder);
        chooser.setFileFilter(new TxtFileFilter());
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Export Spectra File Details");
        File selectedFile;
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }
            while (selectedFile.exists()) {
                int option = JOptionPane.showConfirmDialog(this, "The  file " + chooser.getSelectedFile().getName() + " already exists. Replace file?", "Replace File?", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    chooser = new JFileChooser(lastSelectedFolder);
                    chooser.setFileFilter(new TxtFileFilter());
                    chooser.setMultiSelectionEnabled(false);
                    chooser.setDialogTitle("Export Spectra File Details");
                    returnVal = chooser.showSaveDialog(this);
                    if (returnVal == JFileChooser.CANCEL_OPTION) {
                        return;
                    } else {
                        selectedFile = chooser.getSelectedFile();
                        if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                        }
                    }
                } else {
                    break;
                }
            }
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                selectedFile = chooser.getSelectedFile();
                if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                }
                if (selectedFile.exists()) {
                    selectedFile.delete();
                }
                selectedFile.createNewFile();
                FileWriter f = new FileWriter(selectedFile);
                for (int j = 0; j < spectraTable.getColumnCount() - 1; j++) {
                    f.write(spectraTable.getColumnName(j) + "\t");
                }
                f.write(spectraTable.getColumnName(spectraTable.getColumnCount() - 1) + "\n");
                for (int i = 0; i < spectraTable.getRowCount(); i++) {
                    for (int j = 0; j < spectraTable.getColumnCount() - 1; j++) {
                        f.write(spectraTable.getValueAt(i, j) + "\t");
                    }
                    f.write(spectraTable.getValueAt(i, spectraTable.getColumnCount() - 1) + "\n");
                }
                f.close();
                lastSelectedFolder = selectedFile.getPath();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "An error occured when exporting the spectra file details.", "Error Exporting Spectra Files", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * This method exports all the identifications.
     *
     * @param evt
     */
    private void exportAllIdentifications(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(lastSelectedFolder);
        chooser.setFileFilter(new TxtFileFilter());
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Export All Identifications");
        File selectedFile;
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }
            while (selectedFile.exists()) {
                int option = JOptionPane.showConfirmDialog(this, "The  file " + chooser.getSelectedFile().getName() + " already exists. Replace file?", "Replace File?", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    chooser = new JFileChooser(lastSelectedFolder);
                    chooser.setFileFilter(new TxtFileFilter());
                    chooser.setMultiSelectionEnabled(false);
                    chooser.setDialogTitle("Export All Identifications");
                    returnVal = chooser.showSaveDialog(this);
                    if (returnVal == JFileChooser.CANCEL_OPTION) {
                        return;
                    } else {
                        selectedFile = chooser.getSelectedFile();
                        if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                        }
                    }
                } else {
                    break;
                }
            }
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            try {
                selectedFile = chooser.getSelectedFile();
                if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                }
                if (selectedFile.exists()) {
                    selectedFile.delete();
                }
                selectedFile.createNewFile();
                FileWriter f = new FileWriter(selectedFile);
                for (int j = 0; j < identificationsTable.getColumnCount() - 1; j++) {
                    if (j == 2) {
                        f.write("Modified Sequence" + "\t");
                        f.write("Ion Coverage" + "\t");
                    } else {
                        f.write(identificationsTable.getColumnName(j) + "\t");
                    }
                }
                f.write(identificationsTable.getColumnName(identificationsTable.getColumnCount() - 1) + "\n");
                Iterator<Spectrum> iter = iXTandemFile.getSpectraIterator();
                String modificationDetails = "";
                while (iter.hasNext()) {
                    ArrayList<Peptide> pepList = peptideMap.get(iter.next().getSpectrumNumber());
                    for (Peptide peptide : pepList) {
                        List<Domain> domainList = peptide.getDomains();
                        for (Domain domain : domainList) {
                            String sequence = domain.getDomainSequence();
                            String[] modifications = new String[sequence.length()];
                            for (int i = 0; i < modifications.length; i++) {
                                modifications[i] = "";
                            }
                            String modifiedSequence = "";
                            String nTerminal = "";
                            String cTerminal = "";
                            ArrayList<Modification> fixedModList = allFixMods.get(domain.getDomainKey());
                            ArrayList<Modification> varModList = allVarMods.get(domain.getDomainKey());
                            if (fixedModList != null) {
                                for (int i = 0; i < fixedModList.size(); i++) {
                                    FixedModification fixMod = (FixedModification) fixedModList.get(i);
                                    int[] modRes = new int[domain.getDomainSequence().length()];
                                    int modIndex = Integer.parseInt(fixMod.getLocation()) - domain.getDomainStart();
                                    modRes[modIndex] = fixMod.getNumber();
                                    for (int j = 0; j < modRes.length; j++) {
                                        if (modRes[j] > 0) {
                                            modifications[j] += "<" + "M" + modRes[j] + "*" + ">";
                                        }
                                    }
                                }
                            }
                            if (varModList != null) {
                                for (int i = 0; i < varModList.size(); i++) {
                                    VariableModification varMod = (VariableModification) varModList.get(i);
                                    int[] modRes = new int[domain.getDomainSequence().length()];
                                    int modIndex = Integer.parseInt(varMod.getLocation()) - domain.getDomainStart();
                                    modRes[modIndex] = varMod.getNumber();
                                    for (int j = 0; j < modRes.length; j++) {
                                        if (modRes[j] > 0) {
                                            modifications[j] += "<" + "M" + modRes[j] + "*" + ">";
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < modifications.length; i++) {
                                modifiedSequence += sequence.substring(i, i + 1);
                                if (!modifications[i].equalsIgnoreCase("")) {
                                    String[] residues = modifications[i].split(">");
                                    for (int j = 0; j < residues.length; j++) {
                                        String currentMod = residues[j] + ">";
                                        if (modificationDetails.lastIndexOf(currentMod) == -1) {
                                            if (fixedModList.size() > 0) {
                                                modificationDetails += currentMod + " " + fixedModList.get(j).getName() + ", ";
                                            } else if (varModList.size() > 0) {
                                                modificationDetails += currentMod + " " + varModList.get(j).getName() + ", ";
                                            }
                                            modifiedSequence += currentMod;
                                        } else {
                                            modifiedSequence += currentMod;
                                        }
                                    }
                                }
                            }
                            if (nTerminal.length() == 0) {
                                nTerminal = "NH2-";
                            } else {
                                nTerminal += "-";
                            }
                            if (cTerminal.length() == 0) {
                                cTerminal = "-COOH";
                            } else {
                                cTerminal = "-" + cTerminal;
                            }
                            int[][] ionCoverage = new int[sequence.length() + 1][12];
                            for (int i = 0; i < 12; i++) {
                                FragmentIon[] ions = ionsMap.get(domain.getDomainKey() + "_" + i);
                                for (FragmentIon ion : ions) {
                                    int ionNumber = ion.getNumber();
                                    int ionType = ion.getType();
                                    double mzValue = ion.getMZ();
                                    Color color;
                                    if (i % 2 == 0) {
                                        color = Color.BLUE;
                                    } else {
                                        color = Color.BLACK;
                                    }
                                    if (ionType == FragmentIon.A_ION) {
                                        ionCoverage[ionNumber][0]++;
                                    }
                                    if (ionType == FragmentIon.AH2O_ION) {
                                        ionCoverage[ionNumber][1]++;
                                    }
                                    if (ionType == FragmentIon.ANH3_ION) {
                                        ionCoverage[ionNumber][2]++;
                                    }
                                    if (ionType == FragmentIon.B_ION) {
                                        ionCoverage[ionNumber][3]++;
                                    }
                                    if (ionType == FragmentIon.BH2O_ION) {
                                        ionCoverage[ionNumber][4]++;
                                    }
                                    if (ionType == FragmentIon.BNH3_ION) {
                                        ionCoverage[ionNumber][5]++;
                                    }
                                    if (ionType == FragmentIon.C_ION) {
                                        ionCoverage[ionNumber][6]++;
                                    }
                                    if (ionType == FragmentIon.X_ION) {
                                        ionCoverage[ionNumber][7]++;
                                    }
                                    if (ionType == FragmentIon.Y_ION) {
                                        ionCoverage[ionNumber][8]++;
                                    }
                                    if (ionType == FragmentIon.YH2O_ION) {
                                        ionCoverage[ionNumber][9]++;
                                    }
                                    if (ionType == FragmentIon.YNH3_ION) {
                                        ionCoverage[ionNumber][10]++;
                                    }
                                    if (ionType == FragmentIon.Z_ION) {
                                        ionCoverage[ionNumber][11]++;
                                    }
                                }
                            }
                            int[][] ionCoverageProcessed = new int[sequence.length()][2];
                            if (ionCoverage[1][3] > 0 || ionCoverage[1][4] > 0 || ionCoverage[1][5] > 0) {
                                ionCoverageProcessed[0][0] = 1;
                            }
                            if (ionCoverage[1][8] > 0 || ionCoverage[1][9] > 0 || ionCoverage[1][10] > 0) {
                                ionCoverageProcessed[ionCoverage.length - 2][1] = 1;
                            }
                            if (ionCoverage[ionCoverage.length - 1][3] > 0 || ionCoverage[ionCoverage.length - 1][4] > 0 || ionCoverage[ionCoverage.length - 1][5] > 0) {
                                ionCoverageProcessed[ionCoverage.length - 2][0] = 1;
                            }
                            if (ionCoverage[ionCoverage.length - 1][8] > 0 || ionCoverage[ionCoverage.length - 1][9] > 0 || ionCoverage[ionCoverage.length - 1][10] > 0) {
                                ionCoverageProcessed[0][1] = 1;
                            }
                            for (int i = 2; i < ionCoverage.length - 1; i++) {
                                if (ionCoverage[i][3] > 0 && ionCoverage[i - 1][3] > 0 || ionCoverage[i][4] > 0 && ionCoverage[i - 1][4] > 0 || ionCoverage[i][5] > 0 && ionCoverage[i - 1][5] > 0) {
                                    ionCoverageProcessed[i - 1][0] = 1;
                                } else {
                                    ionCoverageProcessed[i - 1][0] = 0;
                                }
                                if (ionCoverage[i][8] > 0 && ionCoverage[i - 1][8] > 0 || ionCoverage[i][9] > 0 && ionCoverage[i - 1][9] > 0 || ionCoverage[i][10] > 0 && ionCoverage[i - 1][10] > 0) {
                                    ionCoverageProcessed[ionCoverage.length - 1 - i][1] = 1;
                                } else {
                                    ionCoverageProcessed[ionCoverage.length - 1 - i][1] = 0;
                                }
                            }
                            String modifiedSequenceColorCoded = "<html>";
                            if (!nTerminal.startsWith("<")) {
                                modifiedSequenceColorCoded += nTerminal;
                            } else {
                                modifiedSequenceColorCoded += "&lt;";
                                modifiedSequenceColorCoded += nTerminal.substring(1, nTerminal.length() - 2);
                                modifiedSequenceColorCoded += "&gt;-";
                            }
                            int aminoAcidCounter = 0;
                            for (int i = 0; i < modifiedSequence.length(); i++) {
                                if (modifiedSequence.charAt(i) == '<') {
                                    if (ionCoverageProcessed[aminoAcidCounter - 1][0] > 0) {
                                        modifiedSequenceColorCoded += "<u>";
                                    }
                                    if (ionCoverageProcessed[aminoAcidCounter - 1][1] > 0) {
                                        modifiedSequenceColorCoded += "<font color=\"red\">";
                                    }
                                    modifiedSequenceColorCoded += "&lt;";
                                    i++;
                                    while (modifiedSequence.charAt(i) != '>') {
                                        modifiedSequenceColorCoded += modifiedSequence.charAt(i++);
                                    }
                                    modifiedSequenceColorCoded += "&gt;";
                                    if (ionCoverageProcessed[aminoAcidCounter - 1][0] > 0) {
                                        modifiedSequenceColorCoded += "</u>";
                                    }
                                    if (ionCoverageProcessed[aminoAcidCounter - 1][1] > 0) {
                                        modifiedSequenceColorCoded += "</font>";
                                    }
                                } else {
                                    if (ionCoverageProcessed[aminoAcidCounter][0] > 0) {
                                        modifiedSequenceColorCoded += "<u>";
                                    }
                                    if (ionCoverageProcessed[aminoAcidCounter][1] > 0) {
                                        modifiedSequenceColorCoded += "<font color=\"red\">";
                                    }
                                    modifiedSequenceColorCoded += modifiedSequence.charAt(i);
                                    if (ionCoverageProcessed[aminoAcidCounter][0] > 0) {
                                        modifiedSequenceColorCoded += "</u>";
                                    }
                                    if (ionCoverageProcessed[aminoAcidCounter][1] > 0) {
                                        modifiedSequenceColorCoded += "</font>";
                                    }
                                    aminoAcidCounter++;
                                }
                                modifiedSequenceColorCoded += "<font color=\"black\">";
                            }
                            if (!cTerminal.startsWith("-<")) {
                                modifiedSequenceColorCoded += cTerminal;
                            } else {
                                modifiedSequenceColorCoded += "-&lt;";
                                modifiedSequenceColorCoded += cTerminal.substring(2, cTerminal.length() - 1);
                                modifiedSequenceColorCoded += "&gt;";
                            }
                            modifiedSequenceColorCoded += "</html>";
                            modifiedSequence = nTerminal + modifiedSequence + cTerminal;
                            double theoMass = (domain.getDomainMh() + domain.getDomainDeltaMh());
                            String accession = proteinLabelMap.get(domain.getDomainKey());
                            f.write(peptide.getSpectrumNumber() + "\t" + sequence + "\t" + modifiedSequence + "\t" + modifiedSequenceColorCoded + "\t" + domain.getDomainStart() + "\t" + domain.getDomainEnd() + "\t" + new Double(domain.getDomainMh()) + "\t" + theoMass + "\t" + new Float(domain.getDomainExpect()) + "\t" + accession + "\n");
                        }
                    }
                }
                f.close();
                lastSelectedFolder = selectedFile.getPath();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "An error occured when exporting the identifications.", "Error Exporting Identifications", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }

    /**
     * This method exports the selected spectrum information.
     *
     * @param evt
     */
    private void exportSelectedSpectrum(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(lastSelectedFolder);
        chooser.setFileFilter(new TxtFileFilter());
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Export Selected Spectrum");
        File selectedFile;
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }
            while (selectedFile.exists()) {
                int option = JOptionPane.showConfirmDialog(this, "The  file " + chooser.getSelectedFile().getName() + " already exists. Replace file?", "Replace File?", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    chooser = new JFileChooser(lastSelectedFolder);
                    chooser.setFileFilter(new TxtFileFilter());
                    chooser.setMultiSelectionEnabled(false);
                    chooser.setDialogTitle("Export Selected Spectrum");
                    returnVal = chooser.showSaveDialog(this);
                    if (returnVal == JFileChooser.CANCEL_OPTION) {
                        return;
                    } else {
                        selectedFile = chooser.getSelectedFile();
                        if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                        }
                    }
                } else {
                    break;
                }
            }
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            try {
                selectedFile = chooser.getSelectedFile();
                if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                }
                if (selectedFile.exists()) {
                    selectedFile.delete();
                }
                selectedFile.createNewFile();
                FileWriter f = new FileWriter(selectedFile);
                for (int j = 0; j < spectrumJXTable.getColumnCount() - 1; j++) {
                    f.write(spectrumJXTable.getColumnName(j) + "\t");
                }
                f.write(spectrumJXTable.getColumnName(spectrumJXTable.getColumnCount() - 1) + "\n");
                for (int i = 0; i < spectrumJXTable.getRowCount(); i++) {
                    for (int j = 0; j < spectrumJXTable.getColumnCount() - 1; j++) {
                        f.write(spectrumJXTable.getValueAt(i, j) + "\t");
                    }
                    f.write(spectrumJXTable.getValueAt(i, spectrumJXTable.getColumnCount() - 1) + "\n");
                }
                f.close();
                lastSelectedFolder = selectedFile.getPath();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "An error occured when exporting the selected spectrum.", "Error Exporting Selected Spectrum", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Export the all the spectra as DTA files.
     *
     * @param evt
     */
    private void exportAllSpectra(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(lastSelectedFolder);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Export All Spectra As DTA Files");
        File selectedFolder;
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            selectedFolder = chooser.getSelectedFile();
            for (int j = 0; j < spectraTable.getRowCount(); j++) {
                List<Double> mzValues = allMzValues.get((Integer) spectraTable.getValueAt(j, 0));
                List<Double> intensityValues = allIntensityValues.get((Integer) spectraTable.getValueAt(j, 0));
                File currentFile;
                String spectrum = spectraTable.getValueAt(j, 1).toString();
                if (spectrum.contains(".")) {
                    spectrum = spectrum.replace(".", "_");
                }
                if (spectrum.contains("|")) {
                    spectrum = spectrum.replace("|", "_");
                }
                spectrum = spectrum.replaceAll("/+", "_");
                spectrum = spectrum.replaceAll("\\+", "_");
                currentFile = new File(selectedFolder, "" + spectrum + ".dta");
                FileWriter f;
                try {
                    f = new FileWriter(currentFile);
                    double precusorMz = ((Double) spectraTable.getValueAt(j, 2)).doubleValue();
                    int precursorCharge = ((Integer) spectraTable.getValueAt(j, 3)).intValue();
                    double precursorMh = precusorMz * precursorCharge - precursorCharge * Masses.Hydrogen + Masses.Hydrogen;
                    f.write("" + precursorMh);
                    f.write(" " + precursorCharge + "\n");
                    for (int i = 0; i < mzValues.size(); i++) {
                        f.write(mzValues.get(i) + " " + intensityValues.get(i) + "\n");
                    }
                    f.close();
                    lastSelectedFolder = currentFile.getPath();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "An error occured when exporting the spectra.", "Error Exporting Spectra", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
