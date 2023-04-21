package unbbayes.datamining.gui.evaluation.batchEvaluation;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import unbbayes.controller.IconController;
import unbbayes.datamining.evaluation.batchEvaluation.BatchEvaluation;
import unbbayes.datamining.gui.evaluation.batchEvaluation.controllers.BatchEvaluationMainController;
import unbbayes.datamining.gui.evaluation.batchEvaluation.controllers.ClassifiersTabController;
import unbbayes.datamining.gui.evaluation.batchEvaluation.controllers.DatasetsTabController;
import unbbayes.datamining.gui.evaluation.batchEvaluation.controllers.EvaluationsTabController;
import unbbayes.datamining.gui.evaluation.batchEvaluation.controllers.LogsTabController;
import unbbayes.datamining.gui.evaluation.batchEvaluation.controllers.PreprocessorsTabController;

/**
 *
 * @author Emerson Lopes Machado - emersoft@conectanet.com.br
 * @date 02/08/2007
 */
public class BatchEvaluationMain extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private ResourceBundle resource;

    private JTabbedPane mainPanel;

    private ImageIcon openIcon;

    private ImageIcon saveIcon;

    private ImageIcon runIcon;

    private ImageIcon helpIcon;

    private JPanel jPanel = new JPanel();

    private JPanel contentPane;

    private JMenu jMenuFile = new JMenu();

    private JMenu jMenuHelp = new JMenu();

    private JMenuItem jMenuHelpAbout = new JMenuItem();

    private JMenuItem jMenuFileExit = new JMenuItem();

    private JMenuItem jMenuOpen = new JMenuItem();

    private JMenuItem jMenuSave = new JMenuItem();

    private JMenuItem jMenuRun = new JMenuItem();

    private JMenuBar jMenuBar = new JMenuBar();

    private Border border;

    private BorderLayout borderLayout = new BorderLayout();

    private BorderLayout borderLayout2 = new BorderLayout();

    private TitledBorder titledBorder;

    private JToolBar jToolBar = new JToolBar();

    private JButton helpButton = new JButton();

    private JButton openButton = new JButton();

    private JButton saveButton = new JButton();

    private JButton runButton = new JButton();

    private JLabel statusBar = new JLabel();

    private DatasetsTab datasetsTab;

    private PreprocessorsTab preprocessorTab;

    private ClassifiersTab classifierTab;

    private EvaluationsTab evaluationTab;

    private LogsTab logTab;

    private BatchEvaluationMainController controller;

    public BatchEvaluationMain() {
        super("", true, true, true, true);
        resource = ResourceBundle.getBundle("unbbayes.datamining.gui." + "evaluation.batchEvaluation.resources.BatchEvaluationResource");
        setTitle(resource.getString("mainTitle"));
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        controller = new BatchEvaluationMainController(this);
        try {
            buildView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Component initialization
	 * @throws Exception if any error
	 */
    private void buildView() throws Exception {
        mainPanel = buildPanel();
        IconController iconController = IconController.getInstance();
        openIcon = iconController.getOpenIcon();
        saveIcon = iconController.getSaveIcon();
        runIcon = iconController.getCompileIcon();
        helpIcon = iconController.getHelpIcon();
        contentPane = (JPanel) this.getContentPane();
        border = BorderFactory.createLineBorder(new Color(153, 153, 153), 1);
        titledBorder = new TitledBorder(border, resource.getString("selectProgram"));
        contentPane.setLayout(borderLayout);
        this.setSize(new Dimension(640, 480));
        jMenuFile.setMnemonic(((Character) resource.getObject("fileMnemonic")).charValue());
        jMenuFile.setText(resource.getString("file"));
        jMenuHelp.setMnemonic(((Character) resource.getObject("helpMnemonic")).charValue());
        jMenuHelp.setText(resource.getString("help"));
        jMenuHelpAbout.setIcon(helpIcon);
        jMenuHelpAbout.setMnemonic(((Character) resource.getObject("helpTopicsMnemonic")).charValue());
        jMenuHelpAbout.setText(resource.getString("helpTopics"));
        jMenuHelpAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                help(e);
            }
        });
        jPanel.setLayout(borderLayout2);
        jPanel.setBorder(titledBorder);
        titledBorder.setTitle(resource.getString("status"));
        statusBar.setText(resource.getString("welcome"));
        jMenuFileExit.setMnemonic(((Character) resource.getObject("fileExitMnemonic")).charValue());
        jMenuFileExit.setText(resource.getString("exit"));
        jMenuFileExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fileExit(e);
            }
        });
        jToolBar.setFloatable(false);
        jMenuOpen.setIcon(openIcon);
        jMenuOpen.setMnemonic(((Character) resource.getObject("openScriptMnemonic")).charValue());
        jMenuOpen.setText(resource.getString("openScript"));
        jMenuOpen.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openScript();
            }
        });
        jMenuSave.setIcon(saveIcon);
        jMenuSave.setMnemonic(((Character) resource.getObject("saveScriptMnemonic")).charValue());
        jMenuSave.setText(resource.getString("saveScript"));
        jMenuSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveScript();
            }
        });
        jMenuRun.setIcon(runIcon);
        jMenuRun.setMnemonic(((Character) resource.getObject("runScriptMnemonic")).charValue());
        jMenuRun.setText(resource.getString("runScript"));
        jMenuRun.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                runScript();
            }
        });
        helpButton.setIcon(helpIcon);
        helpButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                help(e);
            }
        });
        openButton.setToolTipText(resource.getString("openScript"));
        openButton.setIcon(openIcon);
        openButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openScript();
            }
        });
        saveButton.setToolTipText(resource.getString("saveScript"));
        saveButton.setIcon(saveIcon);
        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveScript();
            }
        });
        runButton.setToolTipText(resource.getString("runScript"));
        runButton.setIcon(runIcon);
        runButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                runScript();
            }
        });
        jMenuFile.add(jMenuOpen);
        jMenuFile.add(jMenuSave);
        jMenuFile.add(jMenuRun);
        jMenuFile.add(jMenuFileExit);
        jMenuHelp.add(jMenuHelpAbout);
        jMenuBar.add(jMenuFile);
        jMenuBar.add(jMenuHelp);
        this.setJMenuBar(jMenuBar);
        contentPane.add(jPanel, BorderLayout.SOUTH);
        jPanel.add(statusBar, BorderLayout.CENTER);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(jToolBar, BorderLayout.NORTH);
        jToolBar.add(openButton, null);
        jToolBar.add(saveButton, null);
        jToolBar.add(runButton, null);
        jToolBar.add(helpButton, null);
    }

    private JTabbedPane buildPanel() throws Exception {
        JTabbedPane tabbedPane = new JTabbedPane();
        BatchEvaluation model = controller.getModel();
        datasetsTab = new DatasetsTab(this, model);
        preprocessorTab = new PreprocessorsTab(this, model);
        classifierTab = new ClassifiersTab(this, model);
        evaluationTab = new EvaluationsTab(this, model);
        logTab = new LogsTab(this, model);
        String datasetsTabTitle = datasetsTab.getTabTitle();
        String preprocessorTabTitle = preprocessorTab.getTabTitle();
        String classifierTabTitle = classifierTab.getTabTitle();
        String evaluationTabTitle = evaluationTab.getTabTitle();
        String logTabTitle = logTab.getTabTitle();
        JPanel datasetsTabPanel = datasetsTab.getTabPanel();
        JPanel preprocessorTabPanel = preprocessorTab.getTabPanel();
        JPanel classifierTabPanel = classifierTab.getTabPanel();
        JPanel evaluationTabPanel = evaluationTab.getTabPanel();
        JPanel logTabPanel = logTab.getTabPanel();
        tabbedPane.addTab(datasetsTabTitle, datasetsTabPanel);
        tabbedPane.addTab(preprocessorTabTitle, preprocessorTabPanel);
        tabbedPane.addTab(classifierTabTitle, classifierTabPanel);
        tabbedPane.addTab(evaluationTabTitle, evaluationTabPanel);
        tabbedPane.addTab(logTabTitle, logTabPanel);
        return tabbedPane;
    }

    private void fileExit(ActionEvent e) {
        controller.fileExit();
    }

    private void openScript() {
        controller.openScript();
    }

    private void saveScript() {
        controller.saveScript();
    }

    protected void runScript() {
        controller.runScript();
    }

    private void help(ActionEvent e) {
        controller.help();
    }

    public ResourceBundle getResourceBundle() {
        return resource;
    }

    public void setStatusBar(String text) {
        statusBar.setText(text);
    }

    public DatasetsTabController getDatasetsTabController() {
        return datasetsTab.getController();
    }

    public PreprocessorsTabController getPreprocessorsTabController() {
        return preprocessorTab.getController();
    }

    public ClassifiersTabController getClassifiersTabController() {
        return classifierTab.getController();
    }

    public EvaluationsTabController getEvaluationsTabController() {
        return evaluationTab.getController();
    }

    public LogsTabController getLogsTabController() {
        return logTab.getController();
    }

    public void disableFunctions() {
        openButton.setEnabled(false);
        runButton.setEnabled(false);
        jMenuOpen.setEnabled(false);
        jMenuRun.setEnabled(false);
    }

    public void enableFunctions() {
        openButton.setEnabled(true);
        runButton.setEnabled(true);
        jMenuOpen.setEnabled(true);
        jMenuRun.setEnabled(true);
    }
}
