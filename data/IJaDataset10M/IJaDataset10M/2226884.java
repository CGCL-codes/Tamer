package edu.upmc.opi.caBIG.caTIES.installer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import com.sun.org.apache.xml.internal.security.encryption.XMLCipher;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_MiddleTierUtils;
import edu.upmc.opi.caBIG.caTIES.common.GeneralUtilities;
import edu.upmc.opi.caBIG.caTIES.connector.CaTIES_DataSourceManager;
import edu.upmc.opi.caBIG.caTIES.connector.CaTIES_SpiritServer;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.UserImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.UserOrganizationImpl;
import edu.upmc.opi.caBIG.caTIES.installer.panel.CaTIES_AssignUserTabPanel;
import edu.upmc.opi.caBIG.caTIES.installer.panel.CaTIES_AuthenticationTabPanel;
import edu.upmc.opi.caBIG.caTIES.installer.panel.CaTIES_CoderTabPanel;
import edu.upmc.opi.caBIG.caTIES.installer.panel.CaTIES_JoinProtocolTabPanel;
import edu.upmc.opi.caBIG.caTIES.installer.panel.CaTIES_LogoutTabPanel;
import edu.upmc.opi.caBIG.caTIES.installer.panel.CaTIES_MapTabPanel;
import edu.upmc.opi.caBIG.caTIES.installer.panel.CaTIES_NewProtocolTabPanel;
import edu.upmc.opi.caBIG.caTIES.installer.panel.CaTIES_NewUserTabPanel;
import edu.upmc.opi.caBIG.caTIES.installer.panel.CaTIES_ResearcherTabPanel;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_DistributionProtocolAssignmentImpl;

public class InstallationUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = -5040682180137288122L;

    public static final String TOMCAT_HOME = "";

    public static final String PUBLIC_HIB_CONF = "config/hibernate/mysql/hibernate.public.cfg.xml";

    InstallationUI self = this;

    private Logger logger = Logger.getLogger(InstallationUI.class);

    private JTabbedPane tabbedPane;

    private JPanel mapTabbedPanel = null;

    private JPanel authenticatePanel = null;

    private JPanel logoutPanel;

    private JPanel installPanel = null;

    private CaTIES_NewUserTabPanel newHonestBrokerTabPanel = null;

    private CaTIES_NewUserTabPanel newResearcherTabPanel = null;

    private JPanel newProtocolTabPane;

    private JPanel joinProtocolTabPanel;

    private JPanel assignUserTabPanel;

    private JPanel researchTabPanel;

    private JComboBox abbrevList;

    private final CaTIES_ConfigurationProperties cfg = CaTIES_ConfigurationProperties.getInstance();

    private JPanel coderPanel;

    public InstallationUI(String frameTitle) {
        super(frameTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initGUI();
    }

    @SuppressWarnings("unused")
    private void initializeLoggingParameters() {
        java.util.logging.Logger.getLogger(XMLCipher.class.getName()).setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("").setLevel(Level.OFF);
        org.apache.commons.logging.Log cipherLog = org.apache.commons.logging.LogFactory.getLog(InstallationUI.class.getName());
        if (cipherLog.isDebugEnabled()) {
            cipherLog.debug("Hello Cipher Log is starting.");
        }
        if (cipherLog.isWarnEnabled()) {
            cipherLog.warn("WARN a Hello Cipher Log is starting.");
        }
    }

    public static void main(String[] args) {
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Thread.currentThread().setName("caTIES Event Dispatch Thread");
                ToolTipManager.sharedInstance().setInitialDelay(200);
                installSpiritServerCertificateLocally();
                InstallationUI nr = new InstallationUI("caTIES 5 Node Registration Testing UI");
                nr.addWindowListener(new WindowAdapter() {

                    public void windowClosing(WindowEvent arg0) {
                        if (NativeInterface.isOpen()) {
                            NativeInterface.close();
                        }
                        System.exit(0);
                    }
                });
                nr.pack();
                nr.setSize(new Dimension(800, 800));
                GeneralUtilities.centerFrame(nr);
                nr.setVisible(true);
            }
        });
        NativeInterface.runEventPump();
    }

    protected static void installSpiritServerCertificateLocally() {
        CaTIES_CertificateInstaller certificateInstaller = new CaTIES_CertificateInstaller();
        String certificateFileName = "spirit.0";
        certificateInstaller.setTgtCertificateFileName(certificateFileName);
        certificateInstaller.setSrcUrlPath(CaTIES_SpiritServer.getInstance().getHtmlPrefix() + "installer/" + certificateFileName);
        certificateInstaller.execute();
    }

    protected void initGUI() {
        logger.debug("Initializing GUI");
        this.tabbedPane = new JTabbedPane();
        this.mapTabbedPanel = new CaTIES_MapTabPanel();
        addSpiritTab(this.tabbedPane);
        this.installPanel = buildInstallationTabPane();
        addInstallationTab(this.tabbedPane);
        this.authenticatePanel = buildAuthenticationTabPane();
        addAuthenticationTab(this.tabbedPane);
        this.logoutPanel = buildLogoutTabPanel();
        this.coderPanel = buildCoderTabPanel();
        this.newHonestBrokerTabPanel = buildNewUserTabPane(CaTIES_DistributionProtocolAssignmentImpl.ROLE_HONEST_BROKER, this.cfg.getHonestBroker());
        this.newResearcherTabPanel = buildNewUserTabPane(CaTIES_DistributionProtocolAssignmentImpl.ROLE_RESEARCHER, this.cfg.getResearcher());
        this.newProtocolTabPane = buildNewProtocolTabPane();
        this.joinProtocolTabPanel = buildJoinProtocolTabPane();
        this.assignUserTabPanel = buildAssignUserTabPane();
        this.researchTabPanel = buildResearcherTabPane();
        getContentPane().add(this.tabbedPane, BorderLayout.CENTER);
    }

    private void addLogoutTab(JTabbedPane tabbedPane) {
        tabbedPane.addTab("Log out", null, this.logoutPanel, "Demonstrates the logout of a CaTIES node");
    }

    private void addCoderTab(JTabbedPane tabbedPane) {
        tabbedPane.addTab("Coder", null, this.coderPanel, "Translate terms to codes");
    }

    private void addAuthenticationTab(JTabbedPane tabbedPane) {
        tabbedPane.addTab("Log in", null, this.authenticatePanel, "Demonstrates the autentication of a CaTIES node");
    }

    private void addSpiritTab(JTabbedPane tabbedPane) {
        tabbedPane.addTab("Spirit Nodes", null, this.mapTabbedPanel, "Spirit Nodes currently registered.");
    }

    private void addInstallationTab(JTabbedPane tabbedPane) {
        tabbedPane.addTab("Installation", null, this.installPanel, "Demonstrates the installation of a CaTIES node");
    }

    public void determineUserCapabilities() {
        if (this.cfg.getCurrentUser() != null) {
            if (this.cfg.getCtrmDataSourceManager() == null) {
                this.cfg.setCtrmDataSourceManager(new CaTIES_DataSourceManager());
                this.cfg.getCtrmDataSourceManager().getMySQLRemoteSessionWithActivityId(this.cfg.getSpiritIpAddress(), this.cfg.getSpiritPort(), this.cfg.getUserCtrmActivityId());
            }
            this.cfg.setCurrentUser(CaTIES_MiddleTierUtils.fetchUser(this.cfg.getCtrmDataSourceManager().getSession(), this.cfg.getCurrentUser().getUsername()));
            StringBuffer sb = new StringBuffer();
            sb.append("select uo ");
            sb.append("from ");
            sb.append("UserOrganizationImpl uo, ");
            sb.append("UserImpl u ");
            sb.append("WHERE ");
            sb.append("uo.user = u ");
            sb.append(" and ");
            sb.append(" u.username = :username");
            String queryAsString = sb.toString();
            Query q = this.cfg.getCtrmDataSourceManager().getSession().createQuery(queryAsString);
            q.setProperties(this.cfg.getCurrentUser());
            HashSet<String> userRoles = new HashSet<String>();
            @SuppressWarnings("unchecked") List<UserOrganizationImpl> userOrgs = q.list();
            for (UserOrganizationImpl userOrg : userOrgs) {
                String userRole = userOrg.getRole();
                userRoles.add(userRole);
            }
            removeAllExceptMap();
            addLogoutTab(this.tabbedPane);
            addCoderTab(this.tabbedPane);
            if (userRoles.contains(UserOrganizationImpl.ROLE_ADMINISTRATOR)) {
                this.newHonestBrokerTabPanel = buildNewUserTabPane(CaTIES_DistributionProtocolAssignmentImpl.ROLE_HONEST_BROKER, this.cfg.getHonestBroker());
                this.newResearcherTabPanel = buildNewUserTabPane(CaTIES_DistributionProtocolAssignmentImpl.ROLE_RESEARCHER, this.cfg.getResearcher());
                this.newProtocolTabPane = buildNewProtocolTabPane();
                this.tabbedPane.addTab("New Honest Broker", null, this.newHonestBrokerTabPanel, "Demonstrates the provisioning of a CaTIES spoke " + this.newHonestBrokerTabPanel.getRole().toLowerCase());
                this.tabbedPane.addTab("New Researcher", null, this.newResearcherTabPanel, "Demonstrates the provisioning of a CaTIES spoke " + this.newResearcherTabPanel.getRole().toLowerCase());
                this.tabbedPane.addTab("New Protocol", null, this.newProtocolTabPane, "Demonstrates the provisioning of a protocol");
                this.tabbedPane.addTab("Join Protocol", null, this.joinProtocolTabPanel, "Demonstrates the joining of an external protocol");
                this.tabbedPane.addTab("Assign User", null, assignUserTabPanel, "Demonstrates the assignment of a user to a local protocol.");
            } else if (userRoles.contains(UserOrganizationImpl.ROLE_RESEARCHER)) {
                this.tabbedPane.addTab("Researching", null, researchTabPanel, "Demonstrates research calls of a CaTIES spoke user");
            }
            this.tabbedPane.repaint();
        }
    }

    private void removeAllExceptMap() {
        for (int idx = this.tabbedPane.getComponentCount() - 1; idx > 0; idx--) {
            this.tabbedPane.remove(idx);
        }
    }

    public void processLogout() {
        this.cfg.setCtrmDataSourceManager(null);
        removeAllExceptMap();
        addAuthenticationTab(this.tabbedPane);
        this.tabbedPane.repaint();
    }

    private JPanel buildAssignUserTabPane() {
        return new CaTIES_AssignUserTabPanel(this, this.cfg);
    }

    private JPanel buildJoinProtocolTabPane() {
        return new CaTIES_JoinProtocolTabPanel(this, this.cfg);
    }

    private JPanel buildNewProtocolTabPane() {
        return new CaTIES_NewProtocolTabPanel(this, this.cfg);
    }

    private CaTIES_NewUserTabPanel buildNewUserTabPane(String role, UserImpl user) {
        return new CaTIES_NewUserTabPanel(this, role, user);
    }

    private JPanel buildResearcherTabPane() {
        return new CaTIES_ResearcherTabPanel(this, this.cfg);
    }

    private JPanel buildAuthenticationTabPane() {
        return new CaTIES_AuthenticationTabPanel(this, this.cfg);
    }

    private JPanel buildLogoutTabPanel() {
        return new CaTIES_LogoutTabPanel(this, this.cfg);
    }

    private JPanel buildCoderTabPanel() {
        return new CaTIES_CoderTabPanel(this, this.cfg);
    }

    private JPanel buildInstallationTabPane() {
        JPanel p = new JPanel(new GridBagLayout());
        JPanel installationPanel = buildInstallationPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 0;
        gbc.ipady = 0;
        p.add(installationPanel, gbc);
        JPanel organizationPanel = buildOrganizationPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        p.add(organizationPanel, gbc);
        JPanel administratorPanel = buildAdministratorPanel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        p.add(administratorPanel, gbc);
        JPanel actionsPanel = new InstallationUIActionPanel(this);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(actionsPanel, gbc);
        return p;
    }

    private JPanel buildInstallationPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "Installation Targets", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        int verticalGridPosition = 0;
        JLabel lbl = new JLabel("Installation Root");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        p.add(lbl, gbc);
        JTextField f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getInstallationRootDirectory());
        gbc.gridx = 1;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        verticalGridPosition++;
        lbl = new JLabel("RDBMS IP Address");
        gbc.gridx = 0;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getRdbmsIpAddress());
        gbc.gridx = 1;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        verticalGridPosition++;
        lbl = new JLabel("RDBMS Port");
        gbc.gridx = 0;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getRdbmsPort());
        gbc.gridx = 1;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        verticalGridPosition++;
        lbl = new JLabel("IP Address");
        gbc.gridx = 0;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getPublicIP());
        gbc.gridx = 1;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        verticalGridPosition++;
        lbl = new JLabel("Public Https Port");
        gbc.gridx = 0;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getPublicHTTPPort());
        gbc.gridx = 1;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        verticalGridPosition++;
        lbl = new JLabel("Private Https Port");
        gbc.gridx = 0;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getPrivateHTTPPort());
        gbc.gridx = 1;
        gbc.gridy = verticalGridPosition;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        return p;
    }

    private JPanel buildOrganizationPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "Organization", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        JLabel lbl = new JLabel("Name");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        p.add(lbl, gbc);
        String[] abbrevs = this.cfg.vectorizeOrganizationAbbreviations();
        this.abbrevList = new JComboBox(abbrevs);
        abbrevList.setEditable(true);
        abbrevList.addActionListener(this);
        lbl.setLabelFor(this.abbrevList);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(this.abbrevList, gbc);
        lbl = new JLabel("Abbreviation");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        JTextField f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getAbbreviationName());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Street");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getAddress().getStreet());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("City");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getAddress().getCity());
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("State");
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(2);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getAddress().getState());
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Zip");
        gbc.gridx = 5;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(2);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getAddress().getZipCode());
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Country");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(2);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getAddress().getCountry());
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Phone");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(9);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getAddress().getPhoneNumber());
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Fax");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(9);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getAddress().getFaxNumber());
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Email");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(9);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getSpokeOrg().getAddress().getEmailAddress());
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        return p;
    }

    private JPanel buildAdministratorPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "Administrator", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        JLabel lbl = new JLabel("Username");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        p.add(lbl, gbc);
        JTextField f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getUsername());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("First");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getFirstName());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Last");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getLastName());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Street");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getAddress().getStreet());
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("City");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(20);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getAddress().getCity());
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("State");
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(2);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getAddress().getState());
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Zip");
        gbc.gridx = 5;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(2);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getAddress().getZipCode());
        gbc.gridx = 6;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Country");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(2);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getAddress().getCountry());
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Phone");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(9);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getAddress().getPhoneNumber());
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Fax");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(9);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getAddress().getFaxNumber());
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        lbl = new JLabel("Email");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(lbl, gbc);
        f = new JTextField(9);
        lbl.setLabelFor(f);
        f.setText(this.cfg.getAdministrator().getAddress().getEmailAddress());
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(f, gbc);
        return p;
    }

    public CaTIES_ConfigurationProperties scrapeScreen() {
        scrapeScreen(this.rootPane);
        this.cfg.combineAbbreviationWithRootPath();
        return this.cfg;
    }

    private void scrapeScreen(Component component) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            Border border = panel.getBorder();
            if (border instanceof TitledBorder) {
                TitledBorder tBorder = (TitledBorder) border;
                String borderTitle = tBorder.getTitle();
                if (borderTitle.equals("Organization")) {
                    scrapeOrganizationScreen(component);
                } else if (borderTitle.equals("Administrator")) {
                    scrapeAdministratorScreen(component);
                } else if (borderTitle.equals("Installation Targets")) {
                    scrapeInstallationTargetsScreen(component);
                }
            }
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                scrapeScreen(container.getComponent(idx));
            }
        }
    }

    private void scrapeInstallationTargetsScreen(Component component) {
        if (component instanceof JLabel) {
            JLabel lbl = (JLabel) component;
            String lblValue = lbl.getText();
            Component lblComponent = lbl.getLabelFor();
            if (lblComponent instanceof JTextField) {
                JTextField f = (JTextField) lblComponent;
                String fValue = f.getText();
                if (lblValue.equals("Installation Root")) {
                    this.cfg.setInstallationRootDirectory(fValue);
                } else if (lblValue.equals("IP Address")) {
                    this.cfg.getSpokeOrg().setPublicIP(fValue);
                    this.cfg.getSpokeOrg().setPrivateIP(fValue);
                } else if (lblValue.equals("RDBMS IP Address")) {
                    this.cfg.setRdbmsIpAddress(fValue);
                } else if (lblValue.equals("RDBMS Port")) {
                    this.cfg.setRdbmsPort(fValue);
                } else if (lblValue.equals("Public Https Port")) {
                    this.cfg.getSpokeOrg().setPublicHTTPPort(fValue);
                } else if (lblValue.equals("Private Https Port")) {
                    this.cfg.getSpokeOrg().setPrivateHTTPPort(fValue);
                }
            }
        } else if (component instanceof Container) {
            Container container = (Container) component;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                scrapeInstallationTargetsScreen(container.getComponent(idx));
            }
        }
    }

    private void scrapeAdministratorScreen(Component component) {
        if (component instanceof JLabel) {
            JLabel lbl = (JLabel) component;
            String lblValue = lbl.getText();
            Component lblComponent = lbl.getLabelFor();
            if (lblComponent instanceof JTextField) {
                JTextField f = (JTextField) lblComponent;
                String fValue = f.getText();
                if (lblValue.equals("Username")) {
                    this.cfg.getAdministrator().setUsername(fValue);
                } else if (lblValue.equals("First")) {
                    this.cfg.getAdministrator().setFirstName(fValue);
                } else if (lblValue.equals("Last")) {
                    this.cfg.getAdministrator().setLastName(fValue);
                } else if (lblValue.equals("Street")) {
                    this.cfg.getAdministrator().getAddress().setStreet(fValue);
                } else if (lblValue.equals("City")) {
                    this.cfg.getAdministrator().getAddress().setCity(fValue);
                } else if (lblValue.equals("State")) {
                    this.cfg.getAdministrator().getAddress().setState(fValue);
                } else if (lblValue.equals("Zip")) {
                    this.cfg.getAdministrator().getAddress().setZipCode(fValue);
                } else if (lblValue.equals("Country")) {
                    this.cfg.getAdministrator().getAddress().setCountry(fValue);
                } else if (lblValue.equals("Phone")) {
                    this.cfg.getAdministrator().getAddress().setPhoneNumber(fValue);
                } else if (lblValue.equals("Fax")) {
                    this.cfg.getAdministrator().getAddress().setFaxNumber(fValue);
                } else if (lblValue.equals("Email")) {
                    this.cfg.getAdministrator().getAddress().setEmailAddress(fValue);
                }
            }
        } else if (component instanceof Container) {
            Container container = (Container) component;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                scrapeAdministratorScreen(container.getComponent(idx));
            }
        }
    }

    private void scrapeOrganizationScreen(Component component) {
        if (component instanceof JLabel) {
            JLabel lbl = (JLabel) component;
            String lblValue = lbl.getText();
            Component lblComponent = lbl.getLabelFor();
            if (lblComponent instanceof JTextField) {
                JTextField f = (JTextField) lblComponent;
                String fValue = f.getText();
                if (lblValue.equals("Name")) {
                    this.cfg.getSpokeOrg().setName(fValue);
                } else if (lblValue.equals("Abbreviation")) {
                    this.cfg.getSpokeOrg().setAbbreviationName(fValue);
                } else if (lblValue.equals("Street")) {
                    this.cfg.getSpokeOrg().getAddress().setStreet(fValue);
                } else if (lblValue.equals("City")) {
                    this.cfg.getSpokeOrg().getAddress().setCity(fValue);
                } else if (lblValue.equals("State")) {
                    this.cfg.getSpokeOrg().getAddress().setState(fValue);
                } else if (lblValue.equals("Zip")) {
                    this.cfg.getSpokeOrg().getAddress().setZipCode(fValue);
                } else if (lblValue.equals("Country")) {
                    this.cfg.getSpokeOrg().getAddress().setCountry(fValue);
                } else if (lblValue.equals("Phone")) {
                    this.cfg.getSpokeOrg().getAddress().setPhoneNumber(fValue);
                } else if (lblValue.equals("Fax")) {
                    this.cfg.getSpokeOrg().getAddress().setFaxNumber(fValue);
                } else if (lblValue.equals("Email")) {
                    this.cfg.getSpokeOrg().getAddress().setEmailAddress(fValue);
                }
            }
        } else if (component instanceof Container) {
            Container container = (Container) component;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                scrapeOrganizationScreen(container.getComponent(idx));
            }
        }
    }

    public void populateScreen() {
        if (this.authenticatePanel != null) {
            ((CaTIES_AuthenticationTabPanel) this.authenticatePanel).setCfg(this.cfg);
        }
        populateScreen(this.rootPane);
    }

    private void populateScreen(Component component) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            Border border = panel.getBorder();
            if (border instanceof TitledBorder) {
                TitledBorder tBorder = (TitledBorder) border;
                String borderTitle = tBorder.getTitle();
                if (borderTitle.equals("Organization")) {
                    populateOrganizationScreen(component);
                } else if (borderTitle.equals("Administrator")) {
                    populateAdministratorScreen(component);
                } else if (borderTitle.equals("Installation Targets")) {
                    populateInstallationTargetsScreen(component);
                }
            }
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                populateScreen(container.getComponent(idx));
            }
        }
    }

    private void populateInstallationTargetsScreen(Component component) {
        if (component instanceof JLabel) {
            JLabel lbl = (JLabel) component;
            String lblValue = lbl.getText();
            Component lblComponent = lbl.getLabelFor();
            if (lblComponent instanceof JTextField) {
                JTextField f = (JTextField) lblComponent;
                if (lblValue.equals("Installation Root")) {
                    f.setText(this.cfg.getInstallationRootDirectory());
                } else if (lblValue.equals("RDBMS IP Address")) {
                    f.setText(this.cfg.getRdbmsIpAddress());
                } else if (lblValue.equals("RDBMS Port")) {
                    f.setText(this.cfg.getRdbmsPort());
                } else if (lblValue.equals("Public Https Port")) {
                    f.setText(this.cfg.getSpokeOrg().getPublicHTTPPort());
                } else if (lblValue.equals("Private Https Port")) {
                    f.setText(this.cfg.getSpokeOrg().getPrivateHTTPPort());
                }
            }
        } else if (component instanceof Container) {
            Container container = (Container) component;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                populateInstallationTargetsScreen(container.getComponent(idx));
            }
        }
    }

    private void populateAdministratorScreen(Component component) {
        if (component instanceof JLabel) {
            JLabel lbl = (JLabel) component;
            String lblValue = lbl.getText();
            Component lblComponent = lbl.getLabelFor();
            if (lblComponent instanceof JTextField) {
                JTextField f = (JTextField) lblComponent;
                if (lblValue.equals("Username")) {
                    f.setText(this.cfg.getAdministrator().getUsername());
                } else if (lblValue.equals("First")) {
                    f.setText(this.cfg.getAdministrator().getFirstName());
                } else if (lblValue.equals("Last")) {
                    f.setText(this.cfg.getAdministrator().getLastName());
                } else if (lblValue.equals("Street")) {
                    f.setText(this.cfg.getAdministrator().getAddress().getStreet());
                } else if (lblValue.equals("City")) {
                    f.setText(this.cfg.getAdministrator().getAddress().getCity());
                } else if (lblValue.equals("State")) {
                    f.setText(this.cfg.getAdministrator().getAddress().getState());
                } else if (lblValue.equals("Zip")) {
                    f.setText(this.cfg.getAdministrator().getAddress().getZipCode());
                } else if (lblValue.equals("Country")) {
                    f.setText(this.cfg.getAdministrator().getAddress().getCountry());
                } else if (lblValue.equals("Phone")) {
                    f.setText(this.cfg.getAdministrator().getAddress().getPhoneNumber());
                } else if (lblValue.equals("Fax")) {
                    f.setText(this.cfg.getAdministrator().getAddress().getFaxNumber());
                } else if (lblValue.equals("Email")) {
                    f.setText(this.cfg.getAdministrator().getAddress().getEmailAddress());
                }
            }
        } else if (component instanceof Container) {
            Container container = (Container) component;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                populateAdministratorScreen(container.getComponent(idx));
            }
        }
    }

    private void populateOrganizationScreen(Component component) {
        if (component instanceof JLabel) {
            JLabel lbl = (JLabel) component;
            String lblValue = lbl.getText();
            Component lblComponent = lbl.getLabelFor();
            if (lblComponent instanceof JTextField) {
                JTextField f = (JTextField) lblComponent;
                if (lblValue.equals("Name")) {
                    f.setText(this.cfg.getSpokeOrg().getName());
                } else if (lblValue.equals("Abbreviation")) {
                    f.setText(this.cfg.getSpokeOrg().getAbbreviationName());
                } else if (lblValue.equals("Street")) {
                    f.setText(this.cfg.getSpokeOrg().getAddress().getStreet());
                } else if (lblValue.equals("City")) {
                    f.setText(this.cfg.getSpokeOrg().getAddress().getCity());
                } else if (lblValue.equals("State")) {
                    f.setText(this.cfg.getSpokeOrg().getAddress().getState());
                } else if (lblValue.equals("Zip")) {
                    f.setText(this.cfg.getSpokeOrg().getAddress().getZipCode());
                } else if (lblValue.equals("Country")) {
                    f.setText(this.cfg.getSpokeOrg().getAddress().getCountry());
                } else if (lblValue.equals("Phone")) {
                    f.setText(this.cfg.getSpokeOrg().getAddress().getPhoneNumber());
                } else if (lblValue.equals("Fax")) {
                    f.setText(this.cfg.getSpokeOrg().getAddress().getFaxNumber());
                } else if (lblValue.equals("Email")) {
                    f.setText(this.cfg.getSpokeOrg().getAddress().getEmailAddress());
                }
            }
        } else if (component instanceof Container) {
            Container container = (Container) component;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                populateOrganizationScreen(container.getComponent(idx));
            }
        }
    }

    public void disableAdministrativeActions() {
        ((CaTIES_NewUserTabPanel) this.tabbedPane.getComponentAt(2)).disableActions();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("comboBoxChanged")) {
            String selectedAbbrev = (String) this.abbrevList.getSelectedItem();
            this.cfg.setDataSet(selectedAbbrev);
            populateScreen();
        }
    }

    public CaTIES_ConfigurationProperties getCfg() {
        return this.cfg;
    }
}
