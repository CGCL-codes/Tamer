package reportingModule;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.screencap.PNGDump;
import gui.form.Main;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.view.JRViewer;
import org.apache.commons.collections15.Transformer;
import ui.FileOperator;

/**
 *
 * @author user
 */
public class ReportingModule extends javax.swing.JFrame {

    /**
     * Reporting Module is responsible for handling all the plagiarism check results
     * and display the results in a Graphical User Interface.
     */
    static final Color HILIT_COLOR = new Color(255, 160, 122);

    private File projectFolder;

    private boolean deletefolder;

    private JRViewer jasperViewer;

    private HashMap hashMapJasper = new HashMap();

    private HashMap<String, String[]> resultMap;

    private String selectedDocumentPath;

    private Highlighter highlighterSelectedFile = new DefaultHighlighter();

    private Highlighter highlighterSecondFile = new DefaultHighlighter();

    private Highlighter highlighterOnScreenView = new DefaultHighlighter();

    private Highlighter.HighlightPainter painter;

    private ArrayList urlListTemp = new ArrayList();

    private HashMap<String, String> matchingToPreprocessed = new HashMap<String, String>();

    private HashMap<String, ArrayList<Integer>> indexHighligherMap = new HashMap<String, ArrayList<Integer>>();

    private HashMap<String, String> fileToUrlMap = new HashMap<String, String>();

    private ArrayList<String> fileNameList = new ArrayList<String>();

    private Graph<Integer, CustomEdge> connectedGraph;

    private Layout<Integer, CustomEdge> layout;

    private VisualizationViewer<Integer, CustomEdge> visualizationViewer;

    private DefaultListModel listModelGraph = new DefaultListModel();

    private ArrayList<String> urlArrayList = new ArrayList<String>();

    private ArrayList<InternetSourcesInfo> onlineSourceList = new ArrayList<InternetSourcesInfo>();

    private ArrayList<FileMarkerGraph> fileSourceList = new ArrayList<FileMarkerGraph>();

    public ReportingModule() {
        this.setSize(500, 500);
        initComponents();
        PreviousButton.setEnabled(false);
        jTabbedPane1.setEnabledAt(1, false);
        jTabbedPane1.setEnabledAt(2, false);
        jTabbedPane1.setEnabledAt(3, false);
        jTabbedPane1.setEnabledAt(4, false);
        browserPanel.setVisible(false);
        jTextField1.setVisible(false);
        this.setIconImage(Main.getPlagiabustImage());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        browserPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        browser = new javax.swing.JEditorPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        showFileContentTextPane = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        selectedFileEditorPane = new javax.swing.JEditorPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        suspectedFileEditorPane = new javax.swing.JEditorPane();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fileListComboBox = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        selectedFileTextField = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        graphScrollPane = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        graphNodeList = new javax.swing.JList();
        jPanel15 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jasperReportScrollPane = new javax.swing.JScrollPane(jasperViewer);
        jPanel1 = new javax.swing.JPanel();
        nextButton = new javax.swing.JButton();
        PreviousButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Plagiarism Check Results Viewer");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        jScrollPane3.setBorder(null);
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("DejaVu Sans", 1, 11));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("\n\n\nThis Wizard will guide you to view the Plagiarism check results of the selected single Document. \nFeatues include\n\n1. On Screen View\nIn the Report OnScreen View of the plagiarism Check results, you can view the document along with the possible plagirized phrases highlighted in red colour. You can view the source of the plagiarized by clicking on the phrase. The content of the file will be displayed in the browser section.\n\n2. Dynamic Cross Check\nYou can select the file you want to compare from the list and the contents along with the similarity phrases will be highlighted in a relevant colours.\n\n3. Final Report\nAll the Details of the plagiarism Check will be displayed in a single report, Including the possible plagiarized sources and the possible internet Sources. Document Statictics will be displayed in a graphical chart.");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setBorder(javax.swing.BorderFactory.createTitledBorder("Welcome"));
        jScrollPane3.setViewportView(jTextArea1);
        jLabel1.setFont(new java.awt.Font("Times New Roman", 3, 18));
        jLabel1.setText("Welcome to the Plagiarism Results Viewer");
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/welcome-icon.png")));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/welcome-icon.png")));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reportingImage2.png")));
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(30, 30, 30).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(89, 89, 89).addComponent(jLabel4).addGap(141, 141, 141).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(76, 76, 76).addComponent(jLabel8)).addGroup(jPanel2Layout.createSequentialGroup().addGap(3, 3, 3).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1))).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(37, 37, 37))).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(18, 18, 18).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel2Layout.createSequentialGroup().addGap(33, 33, 33).addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)))));
        jTabbedPane1.addTab("Introduction", jPanel2);
        browserPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Browser", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        browser.setContentType("text/html");
        browser.setEditable(false);
        jScrollPane7.setViewportView(browser);
        javax.swing.GroupLayout browserPanelLayout = new javax.swing.GroupLayout(browserPanel);
        browserPanel.setLayout(browserPanelLayout);
        browserPanelLayout.setHorizontalGroup(browserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, browserPanelLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        browserPanelLayout.setVerticalGroup(browserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(browserPanelLayout.createSequentialGroup().addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE).addContainerGap()));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("selected File"));
        jScrollPane6.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane6MouseClicked(evt);
            }
        });
        showFileContentTextPane.setEditable(false);
        showFileContentTextPane.setForeground(java.awt.Color.gray);
        showFileContentTextPane.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showFileContentTextPaneMouseClicked(evt);
            }
        });
        showFileContentTextPane.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                showFileContentTextPaneMouseMoved(evt);
            }
        });
        jScrollPane5.setViewportView(showFileContentTextPane);
        jScrollPane6.setViewportView(jScrollPane5);
        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE).addContainerGap()));
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE).addContainerGap()));
        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 2, 11));
        jLabel3.setText("Click on the phrases highlighted in Red to see the suspeced Source");
        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(browserPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel3)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup().addGap(21, 21, 21).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(browserPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        jTabbedPane1.addTab("On Screen View", jPanel6);
        selectedFileEditorPane.setEditable(false);
        jScrollPane1.setViewportView(selectedFileEditorPane);
        suspectedFileEditorPane.setEditable(false);
        jScrollPane2.setViewportView(suspectedFileEditorPane);
        jButton4.setText("Next Step");
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jLabel5.setText("Selected File: ");
        jLabel6.setText("Suspected File");
        jLabel7.setText("Select the file to see the comparison");
        fileListComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        fileListComboBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fileListComboBoxItemStateChanged(evt);
            }
        });
        fileListComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileListComboBoxActionPerformed(evt);
            }
        });
        selectedFileTextField.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        selectedFileTextField.setEditable(false);
        selectedFileTextField.setFont(new java.awt.Font("Tahoma", 1, 11));
        selectedFileTextField.setToolTipText("");
        selectedFileTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedFileTextFieldActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(29, 29, 29).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(502, 502, 502).addComponent(jLabel2).addGap(738, 738, 738).addComponent(jButton4)).addComponent(jLabel6))).addGroup(jPanel3Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(243, 243, 243)).addGroup(jPanel3Layout.createSequentialGroup().addGap(33, 33, 33).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7).addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(selectedFileTextField).addComponent(fileListComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 712, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(1506, 1506, 1506))).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(36, 36, 36).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(27, 27, 27).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(fileListComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel7)).addGap(12, 12, 12).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(selectedFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5)).addGap(18, 18, 18).addComponent(jLabel6).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel3Layout.createSequentialGroup().addGap(93, 93, 93).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE).addComponent(jButton4).addGap(234, 234, 234)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))))));
        jTabbedPane1.addTab("Dynamic Cross Check", jPanel3);
        jScrollPane4.setViewportView(graphNodeList);
        jButton7.setBackground(java.awt.Color.red);
        jButton7.setForeground(java.awt.Color.red);
        jLabel41.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel41.setText("Plagiarism Percentage");
        jLabel42.setFont(new java.awt.Font("DejaVu Sans", 0, 7));
        jLabel42.setText("0% -10 %");
        jLabel43.setFont(new java.awt.Font("DejaVu Sans", 0, 7));
        jLabel43.setText("10%-20%");
        jLabel44.setFont(new java.awt.Font("DejaVu Sans", 0, 7));
        jLabel44.setText(">20%");
        jLabel45.setBackground(java.awt.Color.green);
        jLabel45.setOpaque(true);
        jLabel46.setBackground(java.awt.Color.yellow);
        jLabel46.setOpaque(true);
        jLabel47.setBackground(java.awt.Color.red);
        jLabel47.setOpaque(true);
        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel15Layout.createSequentialGroup().addContainerGap().addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel15Layout.createSequentialGroup().addComponent(jLabel44).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)).addGroup(jPanel15Layout.createSequentialGroup().addComponent(jLabel42).addGap(18, 18, 18).addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)).addGroup(jPanel15Layout.createSequentialGroup().addComponent(jLabel43).addGap(18, 18, 18).addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))).addContainerGap(47, Short.MAX_VALUE)).addGroup(jPanel15Layout.createSequentialGroup().addComponent(jLabel41).addContainerGap(99, Short.MAX_VALUE)));
        jPanel15Layout.setVerticalGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel15Layout.createSequentialGroup().addComponent(jLabel41).addGap(12, 12, 12).addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel42).addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(7, 7, 7).addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel15Layout.createSequentialGroup().addComponent(jLabel43).addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel15Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel44)).addGroup(jPanel15Layout.createSequentialGroup().addGap(1, 1, 1).addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel15Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addGap(28, 28, 28).addComponent(graphScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 682, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addGap(40, 40, 40).addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel7Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(22, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addGap(21, 21, 21).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup().addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(graphScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(31, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Connectivity Graph", jPanel7);
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 1176, Short.MAX_VALUE));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 517, Short.MAX_VALUE));
        jPanel5.add(jasperReportScrollPane);
        jasperReportScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jPanel4.setLayout(null);
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jasperReportScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1125, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(48, 48, 48).addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addContainerGap(30, Short.MAX_VALUE).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jasperReportScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jTabbedPane1.addTab("Final Report", jPanel4);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 479, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/next-icon.png")));
        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        PreviousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/previous-icon.png")));
        PreviousButton.setText("Previous");
        PreviousButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreviousButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(684, 684, 684).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(680, 680, 680).addComponent(PreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(36, 36, 36).addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1151, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(PreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(30, 30, 30)));
        pack();
    }

    /**
     * Action performed for the next button of the GUI
     * @param evt
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int index = jTabbedPane1.getSelectedIndex();
        int nextIndex = index + 1;
        if (nextIndex != 5) {
            jTabbedPane1.setSelectedIndex(nextIndex);
            PreviousButton.setEnabled(true);
            jTabbedPane1.setEnabledAt(index, true);
            jTabbedPane1.setEnabledAt(index + 1, true);
        }
        if (nextIndex == 4) {
            nextButton.setEnabled(false);
        }
    }

    /**
     *
     * @param evt
     */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        jTabbedPane1.setSelectedIndex(2);
    }

    /**
     * Tabbed panel previous button action performed
     * @param evt
     */
    private void PreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {
        nextButton.setEnabled(true);
        int index = jTabbedPane1.getSelectedIndex();
        int prevIndex = index - 1;
        if (index != 0) {
            jTabbedPane1.setSelectedIndex(prevIndex);
        }
        if (prevIndex == 0) {
            PreviousButton.setEnabled(false);
        }
    }

    private void fileListComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {
    }

    /**
     * Action listener for the file list Combobox
     * @param evt
     */
    private void fileListComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        JComboBox cb = (JComboBox) evt.getSource();
        String fileName = (String) cb.getSelectedItem();
        Iterator it = fileToUrlMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String fileNameTemp = (String) pair.getKey();
            String url = (String) pair.getValue();
            if (url.equalsIgnoreCase(fileName)) {
                fileName = fileNameTemp;
            }
        }
        try {
            updateFileDisplayers(fileName);
        } catch (Exception ex) {
            Logger.getLogger(ReportingModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void selectedFileTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jScrollPane6MouseClicked(java.awt.event.MouseEvent evt) {
    }

    /**
     * Mouse listener for the OnScreen View of the selected file
     * @param evt
     */
    private void showFileContentTextPaneMouseClicked(java.awt.event.MouseEvent evt) {
        ArrayList<Integer> phraseIndexes = new ArrayList<Integer>();
        Iterator indexListIterator = indexHighligherMap.entrySet().iterator();
        String matchedFile = "";
        String content = "";
        String internetMatch = "";
        while (indexListIterator.hasNext()) {
            Map.Entry pair = (Map.Entry) indexListIterator.next();
            String match = (String) pair.getKey();
            phraseIndexes = indexHighligherMap.get(match);
            int startIndex = phraseIndexes.get(0);
            int endIndex = phraseIndexes.get(1);
            int offset = showFileContentTextPane.viewToModel(evt.getPoint());
            try {
                int start = Utilities.getWordStart(showFileContentTextPane, offset);
                if ((start > startIndex) && start < endIndex) {
                    String word = showFileContentTextPane.getDocument().getText(startIndex, endIndex - startIndex);
                    String preprocessedText = matchingToPreprocessed.get(word.trim());
                    Set<String> docList = resultMap.keySet();
                    Iterator iter = docList.iterator();
                    while (iter.hasNext()) {
                        String suspectedfilename = (String) iter.next();
                        String[] matchVal = resultMap.get(suspectedfilename);
                        String matchString = matchVal[0];
                        if (matchString != null) {
                            String[] matchings = matchString.split("~");
                            for (int k = 0; k < matchings.length; k++) {
                                if (preprocessedText.equalsIgnoreCase(matchings[k])) {
                                    internetMatch = suspectedfilename;
                                    matchedFile = matchedFile + "\n" + suspectedfilename;
                                }
                            }
                        }
                    }
                    content = "<p><b>The suspected File </b>  <font color='red'>" + matchedFile + "</font></p> ";
                    Iterator mapIterator = fileToUrlMap.entrySet().iterator();
                    while (mapIterator.hasNext()) {
                        Map.Entry fileUrlPair = (Map.Entry) mapIterator.next();
                        String downloadedFileName = (String) fileUrlPair.getKey();
                        if (internetMatch.equalsIgnoreCase(downloadedFileName)) {
                            internetMatch = (String) fileUrlPair.getValue();
                            content = "<p><b>The suspected Online Source </b><b><a href='" + internetMatch + "'>" + internetMatch + "</a></b></p> ";
                        }
                    }
                    String heading = "View Source";
                    SourceViewer tooltip = new SourceViewer(heading, content, showFileContentTextPane, browser, browserPanel);
                    tooltip.showDetails();
                }
            } catch (Exception ex) {
                Logger.getLogger(ReportingModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void showFileContentTextPaneMouseMoved(java.awt.event.MouseEvent evt) {
    }

    /**
     * Action event of the form closing
     * @param evt
     */
    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        if (this.deletefolder) {
            this.deleteDir(projectFolder);
        }
    }

    /**
     * Delete the project folder after closing the window.
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Set the data to the class and perform the report generating
     * @param projectFolderTemp
     * @param deleteFolderTemp
     */
    public void setData(File projectFolderTemp, boolean deleteFolderTemp) {
        this.deletefolder = deleteFolderTemp;
        this.projectFolder = projectFolderTemp;
        Set<String> docList = resultMap.keySet();
        Iterator docListIterator = docList.iterator();
        FileOperator setTextToTextAreas = new FileOperator();
        while (docListIterator.hasNext()) {
            String name = (String) docListIterator.next();
            fileNameList.add(name);
            this.setIndexDetails(name);
        }
        this.setFilesToFileSelector();
        selectedFileTextField.setText(selectedDocumentPath);
        selectedFileTextField.setToolTipText(selectedDocumentPath);
        String texts = setTextToTextAreas.textSetter(selectedDocumentPath);
        showFileContentTextPane.setText(texts);
        showFileContentTextPane.setHighlighter(highlighterOnScreenView);
        texthighlighterOnScreenView();
        this.generateGraph();
        this.generateFinalReport();
    }

    /**
     * Set the word indexing details of the query in the original file Name *
     * @param fileName
     */
    public void setIndexDetails(String fileName) {
        String fileName2 = fileName;
        Set<String> docList = resultMap.keySet();
        Iterator iter = docList.iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            if (name.equalsIgnoreCase(fileName2)) {
                String[] matchVal = resultMap.get(name);
                jTextField1.setText(matchVal[0]);
                this.highlighterDetails(matchVal[0], fileName);
            }
        }
    }

    /**
     * Get the highlighter details with the index details of the selected file
     * @param queryTemp
     * @param filename
     */
    public void highlighterDetails(String queryTemp, String filename) {
        FileOperator setTextToTextAreas = new FileOperator();
        String[] textsOfFiles = setTextToTextAreas.textSetter(selectedDocumentPath, filename);
        String content = textsOfFiles[0].toLowerCase();
        String queryString = queryTemp;
        String[] query = null;
        if (queryString.length() <= 0) {
            return;
        }
        if (queryString.contains("~")) {
            query = queryString.split("~");
        } else {
            query = new String[1];
            query[0] = queryString;
        }
        for (int i = 0; i < query.length; i++) {
            String searchQuery = query[i];
            TextHighlighter highlighterFirstFile = new TextHighlighter();
            String[] highlightindexedInfoFirstFile = highlighterFirstFile.highlightTexts(content, searchQuery);
            int startIndexFirst = Integer.valueOf(highlightindexedInfoFirstFile[0]);
            int endIndexFirst = Integer.valueOf(highlightindexedInfoFirstFile[1]);
            String match = highlightindexedInfoFirstFile[2];
            ArrayList<Integer> arr = new ArrayList<Integer>();
            arr.add(startIndexFirst);
            arr.add(endIndexFirst);
            indexHighligherMap.put(match, arr);
            matchingToPreprocessed.put(highlightindexedInfoFirstFile[2], highlightindexedInfoFirstFile[3]);
        }
    }

    /**
     * Set the matching files to the file selector combobox
     */
    public void setFilesToFileSelector() {
        Set<String> filenameListofurls = fileToUrlMap.keySet();
        for (int i = fileNameList.size(); i > 0; i--) {
            String fileName = fileNameList.get(i - 1);
            if (filenameListofurls != null) {
                if ((filenameListofurls.contains(fileName)) == false) {
                    fileListComboBox.addItem(fileNameList.get(i - 1));
                } else {
                    fileListComboBox.addItem((String) fileToUrlMap.get(fileName));
                    urlArrayList.add((String) fileToUrlMap.get(fileName));
                }
            } else {
                fileListComboBox.addItem(fileNameList.get(i - 1));
            }
        }
    }

    /**
     * Text Highlighter of the On-Screen View of the report
     */
    public void texthighlighterOnScreenView() {
        Iterator indexMapIterator = indexHighligherMap.entrySet().iterator();
        ArrayList<Integer> phraseIndexes = new ArrayList<Integer>();
        StyledDocument doc = showFileContentTextPane.getStyledDocument();
        while (indexMapIterator.hasNext()) {
            Map.Entry pair = (Map.Entry) indexMapIterator.next();
            String match = (String) pair.getKey();
            phraseIndexes = indexHighligherMap.get(match);
            for (int i = 0; i < phraseIndexes.size(); i++) {
                int startIndex = phraseIndexes.get(0);
                int endIndex = phraseIndexes.get(1);
                try {
                    doc.remove(startIndex, endIndex - startIndex);
                    Style style = showFileContentTextPane.addStyle(match, null);
                    StyleConstants.setForeground(style, Color.red);
                    StyleConstants.setBold(style, true);
                    StyleConstants.setItalic(style, true);
                    doc.insertString(startIndex, match, style);
                } catch (BadLocationException e) {
                }
            }
        }
    }

    /**
     * Set Text Highlighter details for  files
     * @param queryTemp
     * @param paraphrasedFirstPhraseTemp
     * @param paraphrasedSecondPhraseTemp
     */
    public void highlighter(String queryTemp, String paraphrasedFirstPhraseTemp, String paraphrasedSecondPhraseTemp) {
        String paraphrasedFirstPhrase = paraphrasedFirstPhraseTemp;
        String paraphrasedSecondPhrase = paraphrasedSecondPhraseTemp;
        String queryString = queryTemp;
        selectedFileEditorPane.setHighlighter(highlighterSelectedFile);
        suspectedFileEditorPane.setHighlighter(highlighterSecondFile);
        highlighterSelectedFile.removeAllHighlights();
        highlighterSecondFile.removeAllHighlights();
        String content = selectedFileEditorPane.getText();
        String content2 = suspectedFileEditorPane.getText();
        String[] query = null;
        String[] queryforFirstFile = null;
        String[] queryforSecondFile = null;
        if ((queryString.length() <= 0) && (paraphrasedFirstPhrase.length() <= 0)) {
            return;
        }
        query = queryString.split("~");
        queryforFirstFile = paraphrasedFirstPhrase.split("~");
        queryforSecondFile = paraphrasedSecondPhrase.split("~");
        if (queryString.length() != 0) {
            ColourMap colourMap = new ColourMap();
            ArrayList<Color> colourArray = colourMap.getColourArray(query);
            setHighlighterToBothTextFiles(query, content, content2, colourArray);
        }
        if (queryforFirstFile.length != 1) {
            ColourMap colourMap = new ColourMap();
            ArrayList<Color> colourArray = colourMap.getColourArray(queryforFirstFile);
            setHighlighterToFirstTextFile(queryforFirstFile, content, content, colourArray);
        }
        if (queryforSecondFile.length != 1) {
            ColourMap colourMap = new ColourMap();
            ArrayList<Color> colourArray = colourMap.getColourArray(queryforSecondFile);
            setHighlighterToSecondTextFile(queryforSecondFile, content2, content2, colourArray);
        }
    }

    /**
     * Set the text Highlighter for both files
     * @param queryArray
     * @param contentTemp
     * @param content2Temp
     * @param colourArrayTemp
     */
    public void setHighlighterToBothTextFiles(String[] queryArray, String contentTemp, String content2Temp, ArrayList<Color> colourArrayTemp) {
        String content = contentTemp;
        String content2 = content2Temp;
        ArrayList<Color> colourArray = colourArrayTemp;
        for (int i = 0; i < queryArray.length; i++) {
            String searchQuery = queryArray[i];
            TextHighlighter highlighterFirstFile = new TextHighlighter();
            TextHighlighter highlighterSecondFile = new TextHighlighter();
            String[] highlightindexedInfoFirstFile = highlighterFirstFile.highlightTexts(content, searchQuery);
            String[] highlightindexedInfoSecondFile = highlighterSecondFile.highlightTexts(content2, searchQuery);
            int startIndexFirst = Integer.valueOf(highlightindexedInfoFirstFile[0]);
            int endIndexFirst = Integer.valueOf(highlightindexedInfoFirstFile[1]);
            int startIndexSecond = Integer.valueOf(highlightindexedInfoSecondFile[0]);
            int endIndexSecond = Integer.valueOf(highlightindexedInfoSecondFile[1]);
            try {
                Color HILIT_COLOR = colourArray.get(i);
                if (startIndexFirst != -1) {
                    painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
                    highlighterSelectedFile.addHighlight(startIndexFirst, endIndexFirst, painter);
                    selectedFileEditorPane.setCaretPosition(endIndexFirst);
                }
                if (startIndexSecond != -1) {
                    painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
                    this.highlighterSecondFile.addHighlight(startIndexSecond, endIndexSecond, painter);
                    suspectedFileEditorPane.setCaretPosition(endIndexSecond);
                }
            } catch (BadLocationException ex) {
                Logger.getLogger(ReportingModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Set the text Highlighter for selected File
     * @param queryArray
     * @param contentTemp
     * @param content2Temp
     * @param colourArrayTemp
     */
    public void setHighlighterToFirstTextFile(String[] queryArray, String contentTemp, String content2Temp, ArrayList<Color> colourArrayTemp) {
        String content = contentTemp;
        ArrayList<Color> colourArray = colourArrayTemp;
        for (int i = 0; i < queryArray.length; i++) {
            String searchQuery = queryArray[i];
            TextHighlighterParaphrase highlighterFirstFile = new TextHighlighterParaphrase();
            String[] highlightindexedInfoFirstFile = highlighterFirstFile.highlightTexts(content, searchQuery);
            int startIndexFirst = Integer.valueOf(highlightindexedInfoFirstFile[0]);
            int endIndexFirst = Integer.valueOf(highlightindexedInfoFirstFile[1]);
            try {
                Color HILIT_COLOR = colourArray.get(i);
                if (startIndexFirst != -1) {
                    painter = new UnderLineHIghlighter.UnderlineHighlightPainter(HILIT_COLOR);
                    highlighterSelectedFile.addHighlight(startIndexFirst, endIndexFirst, painter);
                    selectedFileEditorPane.setCaretPosition(endIndexFirst);
                }
            } catch (BadLocationException ex) {
                Logger.getLogger(ReportingModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Set the text Highlighter for second File
     * @param queryArray
     * @param contentTemp
     * @param content2Temp
     * @param colourArrayTemp
     */
    public void setHighlighterToSecondTextFile(String[] queryArray, String contentTemp, String content2Temp, ArrayList<Color> colourArrayTemp) {
        String content2 = content2Temp;
        ArrayList<Color> colourArray = colourArrayTemp;
        for (int i = 0; i < queryArray.length; i++) {
            String searchQuery = queryArray[i];
            TextHighlighterParaphrase highlighterSecondFile = new TextHighlighterParaphrase();
            String[] highlightindexedInfoSecondFile = highlighterSecondFile.highlightTexts(content2, searchQuery);
            int startIndexSecond = Integer.valueOf(highlightindexedInfoSecondFile[0]);
            int endIndexSecond = Integer.valueOf(highlightindexedInfoSecondFile[1]);
            try {
                Color HILIT_COLOR = colourArray.get(i);
                if (startIndexSecond != -1) {
                    painter = new UnderLineHIghlighter.UnderlineHighlightPainter(HILIT_COLOR);
                    this.highlighterSecondFile.addHighlight(startIndexSecond, endIndexSecond, painter);
                    suspectedFileEditorPane.setCaretPosition(endIndexSecond);
                }
            } catch (BadLocationException ex) {
                Logger.getLogger(ReportingModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Final report Generator
     */
    public void generateFinalReport() {
        File file = new File(selectedDocumentPath);
        ArrayList<String> onlineSourceArray = new ArrayList<String>();
        String fileName = file.getAbsolutePath();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String time = sdf.format(cal.getTime());
        String nodeList = "";
        for (int i = 0; i < listModelGraph.size(); i++) {
            String node = (String) listModelGraph.get(i);
            fileSourceList.add(new FileMarkerGraph(node));
        }
        for (int i = 0; i < urlArrayList.size(); i++) {
            onlineSourceArray.add(urlArrayList.get(i));
            onlineSourceList.add(new InternetSourcesInfo(urlArrayList.get(i)));
        }
        if (onlineSourceArray.size() < 15) {
            while (onlineSourceArray.size() != 15) {
                onlineSourceArray.add("");
            }
        }
        hashMapJasper.put("SUBREPORT_DIR", "jasper/");
        hashMapJasper.put("onlineSource", onlineSourceArray);
        hashMapJasper.put("onlineSourceList", onlineSourceList);
        hashMapJasper.put("fileSourceList", fileSourceList);
        hashMapJasper.put("field", nodeList);
        hashMapJasper.put("time", time);
        hashMapJasper.put("docName", fileName);
        JRDataSource dataSource = createReportDataSource();
        JasperPrint jasperPrint;
        try {
            jasperPrint = JasperFillManager.fillReport("jasper/report3.jasper", hashMapJasper, dataSource);
            jasperViewer = new JRViewer(jasperPrint);
            jasperViewer.addHyperlinkListener(new ReportHyperlinkListner());
            jasperReportScrollPane.getViewport().add(jasperViewer);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dataSource for the Jasper Report
     * @return JRDataSource
     */
    public JRDataSource createReportDataSource() {
        JRBeanArrayDataSource dataSource;
        DataFetcherSingleSearch[] reportRows = initializeBeanArray();
        dataSource = new JRBeanArrayDataSource(reportRows);
        return dataSource;
    }

    /**
     * Set the results details
     * @return resultsDetails
     */
    public DataFetcherSingleSearch[] initializeBeanArray() {
        Set<String> docList = resultMap.keySet();
        DataFetcherSingleSearch[] reportRows = new DataFetcherSingleSearch[docList.size()];
        Iterator iter = docList.iterator();
        int count = 0;
        while (iter.hasNext()) {
            ArrayList<InternetSourceStore> onlineSourceArray = new ArrayList<InternetSourceStore>();
            Set<String> filenameListofurls = fileToUrlMap.keySet();
            Iterator it = filenameListofurls.iterator();
            while (it.hasNext()) {
                String fName = (String) it.next();
                onlineSourceArray.add(new InternetSourceStore(fileToUrlMap.get(fName)));
            }
            String suspectedfilename = (String) iter.next();
            String[] matchVal = resultMap.get(suspectedfilename);
            String matchValue = matchVal[1] + "%";
            if (filenameListofurls.contains(suspectedfilename)) {
                suspectedfilename = (String) fileToUrlMap.get(suspectedfilename);
            }
            reportRows[count] = new DataFetcherSingleSearch(suspectedfilename, matchValue, onlineSourceArray);
            count++;
        }
        return reportRows;
    }

    /**
     * Generate the connectivity Graph
     */
    public void generateGraph() {
        connectedGraph = new SparseMultigraph<Integer, CustomEdge>();
        HashMap<String, Integer> docToIntegerMap = new HashMap<String, Integer>();
        int verCount = 0;
        Set<String> filenameListofurls = fileToUrlMap.keySet();
        graphNodeList.setModel(listModelGraph);
        Set<String> docList = resultMap.keySet();
        Iterator iter = docList.iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            int countNo = ++verCount;
            if (filenameListofurls != null) {
                if ((filenameListofurls.contains(name)) == false) {
                    listModelGraph.add(verCount - 1, String.valueOf(countNo) + " -- " + name);
                } else {
                    listModelGraph.add(verCount - 1, String.valueOf(countNo) + " -- " + fileToUrlMap.get(name));
                }
            } else {
                listModelGraph.add(verCount - 1, String.valueOf(countNo) + " -- " + name);
            }
            docToIntegerMap.put(name, countNo);
            connectedGraph.addVertex((Integer) countNo);
        }
        connectedGraph.addVertex((Integer) 0);
        Iterator it = resultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String fileName = (String) pair.getKey();
            String[] matchArray = (String[]) pair.getValue();
            String value = matchArray[1] + "%";
            if ((connectedGraph.findEdge(0, docToIntegerMap.get(fileName))) == null) {
                connectedGraph.addEdge(new CustomEdge(value), 0, docToIntegerMap.get(fileName));
            }
        }
        layout = new CircleLayout(connectedGraph);
        layout.setSize(new Dimension(600, 600));
        visualizationViewer = new VisualizationViewer<Integer, CustomEdge>(layout);
        visualizationViewer.setSize(600, 600);
        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        visualizationViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {

            private final Color[] palette = { Color.WHITE, Color.WHITE, Color.black };

            public Paint transform(Integer i) {
                if (i == 0) {
                    return palette[0];
                } else {
                    return palette[2];
                }
            }
        };
        Transformer<CustomEdge, Paint> edgesPaint = new Transformer<CustomEdge, Paint>() {

            private final Color[] palette = { Color.GREEN, Color.YELLOW, Color.RED };

            public Paint transform(CustomEdge edgeValue) {
                String stringvalue = edgeValue.toString();
                stringvalue = stringvalue.replaceAll("%", "");
                Double value = Double.valueOf(stringvalue);
                int intval = value.intValue();
                if (intval <= 10) {
                    return palette[0];
                }
                if (intval > 10 && intval <= 20) {
                    return palette[1];
                } else {
                    return palette[2];
                }
            }
        };
        visualizationViewer.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        visualizationViewer.getRenderContext().setEdgeFillPaintTransformer(edgesPaint);
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        visualizationViewer.setGraphMouse(gm);
        graphScrollPane.setViewportView(visualizationViewer);
        saveGraph(visualizationViewer);
    }

    /**
     * Update the fileviewer according to the selected File
     * @param filaName
     */
    private void updateFileDisplayers(String fileName) {
        String fileName1 = selectedDocumentPath;
        String fileName2 = fileName;
        FileOperator setTextToTextAreas = new FileOperator();
        String[] texts = setTextToTextAreas.textSetter(fileName1, fileName2);
        String field1 = texts[0];
        String field2 = texts[1];
        selectedFileEditorPane.setText(field1.toLowerCase());
        suspectedFileEditorPane.setText(field2.toLowerCase());
        Set<String> docList = resultMap.keySet();
        Iterator iter = docList.iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            if (name.equalsIgnoreCase(fileName2)) {
                String[] matchVal = resultMap.get(name);
                this.highlighter(matchVal[0], matchVal[2], matchVal[3]);
            }
        }
    }

    /**
     * Set the fileToUrl Map
     * @param fileUrlMap
     */
    public void setMap(HashMap<String, String> fileUrlMap) {
        this.fileToUrlMap = fileUrlMap;
        if (fileUrlMap == null) {
            System.err.println("Fileurl map is null");
        }
    }

    /**
     *
     * @param results
     */
    public void setTemp(HashMap<String, String[]> results) {
        resultMap = results;
    }

    /**
     * Set the Selected Document
     * @param doc
     */
    public void setDocument(String doc) {
        selectedDocumentPath = doc;
    }

    /**
     * Set the URL List
     * @param urlList
     */
    public void setUrl(ArrayList<String> urlList) {
        for (int i = 0; i < urlList.size(); i++) {
            urlListTemp.add(urlList.get(i));
        }
    }

    /**
     * Save the Graph as a .png file
     * @param visualizationViewer
     */
    public void saveGraph(VisualizationViewer<Integer, CustomEdge> visualizationViewer) {
        PNGDump dumper = new PNGDump();
        try {
            dumper.dumpComponent(new File("jasper/reportImages/test.png"), visualizationViewer);
        } catch (IOException e) {
            System.err.println("dump failed");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ReportingModule().setVisible(true);
            }
        });
    }

    private javax.swing.JButton PreviousButton;

    private javax.swing.JEditorPane browser;

    private javax.swing.JPanel browserPanel;

    private javax.swing.JComboBox fileListComboBox;

    private javax.swing.JList graphNodeList;

    private javax.swing.JScrollPane graphScrollPane;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton7;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel41;

    private javax.swing.JLabel jLabel42;

    private javax.swing.JLabel jLabel43;

    private javax.swing.JLabel jLabel44;

    private javax.swing.JLabel jLabel45;

    private javax.swing.JLabel jLabel46;

    private javax.swing.JLabel jLabel47;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel15;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JScrollPane jScrollPane5;

    private javax.swing.JScrollPane jScrollPane6;

    private javax.swing.JScrollPane jScrollPane7;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JScrollPane jasperReportScrollPane;

    private javax.swing.JButton nextButton;

    private javax.swing.JEditorPane selectedFileEditorPane;

    private javax.swing.JTextField selectedFileTextField;

    private javax.swing.JTextPane showFileContentTextPane;

    private javax.swing.JEditorPane suspectedFileEditorPane;
}
