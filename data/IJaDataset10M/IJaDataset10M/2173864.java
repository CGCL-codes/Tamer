package edu.upmc.opi.caBIG.caTIES.installer.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.upmc.opi.caBIG.caTIES.client.dispatcher.CaTIES_Dispatcher;
import edu.upmc.opi.caBIG.caTIES.client.dispatcher.CaTIES_DispatcherFactory;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Error;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_FormatUtils;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Utils;
import edu.upmc.opi.caBIG.caTIES.connector.CaTIES_DataSourceManager;
import edu.upmc.opi.caBIG.caTIES.connector.CaTIES_SpiritServer;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.DistributionProtocolImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.DistributionProtocolOrganizationImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.OrganizationImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.UserImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.UserOrganizationImpl;
import edu.upmc.opi.caBIG.caTIES.installer.CaTIES_ConfigurationProperties;
import edu.upmc.opi.caBIG.caTIES.installer.InstallationUI;
import edu.upmc.opi.caBIG.common.CaBIG_LobUtilities;

public class CaTIES_JoinProtocolTabPanel extends JPanel implements ListSelectionListener, ActionListener {

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_JoinProtocolTabPanel.class);

    private static final long serialVersionUID = 6442302247074188216L;

    private JList list;

    private DefaultListModel listModel;

    private static final String joinProtocolString = "Join Protocol";

    private static final String clearProtocolString = "Clear";

    private static final String refreshProtocolString = "Refresh";

    private JButton refreshButton;

    private JButton clearButton;

    private JButton joinButton;

    private JTextField protocolTitleTextField;

    private JRadioButton tissueProviderButton = null;

    private JRadioButton dataProviderButton = null;

    private JRadioButton tissueAndDataProviderButton = null;

    private InstallationUI installationUI;

    private CaTIES_ConfigurationProperties cfg;

    private DistributionProtocolImpl currentProtocol = null;

    private OrganizationImpl currentUserOrg = null;

    private JTextArea resultsArea;

    private JTextField irbIdentifierTextField;

    private JTextField irbApprovalDateTextField;

    private JTextField irbExpirationDateTextField;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("CaTIES_JoinProtocol Tab Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = new CaTIES_JoinProtocolTabPanel(null, CaTIES_ConfigurationProperties.getInstance());
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.setSize(new Dimension(600, 800));
        frame.pack();
        CaTIES_Utils.centerComponent(frame);
        frame.setVisible(true);
    }

    public CaTIES_JoinProtocolTabPanel() {
    }

    public CaTIES_JoinProtocolTabPanel(InstallationUI installationUI, CaTIES_ConfigurationProperties cfg) {
        this.setInstallationUI(installationUI);
        this.cfg = cfg;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        add(buildExternalProtocolsListPanel(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        add(buildJoinProtocolActionPanel(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        add(buildActionPanel(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        add(buildResultsTextAreaPanel(), gbc);
    }

    private JPanel buildExternalProtocolsListPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(5, 5));
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "External Protocols", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);
        p.add(listScrollPane, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildJoinProtocolActionPanel() {
        JPanel p = new JPanel();
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "Join Protocol", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        this.protocolTitleTextField = new JTextField(20);
        this.protocolTitleTextField.setEnabled(false);
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 0;
        gbc.ipady = 0;
        p.add(protocolTitleTextField, gbc);
        JLabel irbIdentifierLabel = new JLabel("IRB Identifier");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(irbIdentifierLabel, gbc);
        this.irbIdentifierTextField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(this.irbIdentifierTextField, gbc);
        JLabel irbApprovalDateLabel = new JLabel("IRB Approval Date");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(irbApprovalDateLabel, gbc);
        this.irbApprovalDateTextField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(this.irbApprovalDateTextField, gbc);
        JLabel irbExpiredDateLabel = new JLabel("IRB Expiration Date");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        p.add(irbExpiredDateLabel, gbc);
        this.irbExpirationDateTextField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(this.irbExpirationDateTextField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 0;
        gbc.ipady = 0;
        p.add(buildRoleSelectionRadioButtonPanel(), gbc);
        return p;
    }

    private String getSelectedRole() {
        String role = DistributionProtocolOrganizationImpl.ROLE_TISSUE_PROVIDER;
        if (dataProviderButton.isSelected()) {
            role = DistributionProtocolOrganizationImpl.ROLE_DATA_PROVIDER;
        }
        return role;
    }

    private JPanel buildRoleSelectionRadioButtonPanel() {
        String tissueProviderCommand = "Tissue Provider";
        tissueProviderButton = new JRadioButton(tissueProviderCommand);
        tissueProviderButton.setMnemonic(KeyEvent.VK_B);
        tissueProviderButton.setActionCommand(tissueProviderCommand);
        tissueProviderButton.setSelected(true);
        String dataProviderCommand = "Data Provider";
        dataProviderButton = new JRadioButton(dataProviderCommand);
        dataProviderButton.setMnemonic(KeyEvent.VK_B);
        dataProviderButton.setActionCommand(dataProviderCommand);
        dataProviderButton.setSelected(true);
        String tissueAndDataProviderCommand = "Tissue and Data Provider";
        this.tissueAndDataProviderButton = new JRadioButton(tissueAndDataProviderCommand);
        this.tissueAndDataProviderButton.setActionCommand(tissueAndDataProviderCommand);
        this.tissueAndDataProviderButton.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(tissueProviderButton);
        group.add(dataProviderButton);
        group.add(tissueAndDataProviderButton);
        JPanel radioPanel = new JPanel(new GridLayout(1, 0));
        radioPanel.add(tissueProviderButton);
        radioPanel.add(dataProviderButton);
        radioPanel.add(tissueAndDataProviderButton);
        return radioPanel;
    }

    private JPanel buildActionPanel() {
        JPanel p = new JPanel();
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "Actions", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        GridLayout gridLayout = new GridLayout(1, 3, 2, 2);
        p.setLayout(gridLayout);
        this.refreshButton = new JButton(refreshProtocolString);
        refreshButton.setActionCommand(refreshProtocolString);
        refreshButton.addActionListener(this);
        refreshButton.setEnabled(true);
        JPanel installButtonPanel = new JPanel();
        installButtonPanel.add(this.refreshButton);
        p.add(installButtonPanel);
        this.clearButton = new JButton(clearProtocolString);
        clearButton.setActionCommand(clearProtocolString);
        clearButton.addActionListener(this);
        clearButton.setEnabled(true);
        installButtonPanel = new JPanel();
        installButtonPanel.add(this.clearButton);
        p.add(installButtonPanel);
        this.joinButton = new JButton(joinProtocolString);
        joinButton.setActionCommand(joinProtocolString);
        joinButton.addActionListener(this);
        joinButton.setEnabled(true);
        installButtonPanel = new JPanel();
        installButtonPanel.add(this.joinButton);
        p.add(installButtonPanel);
        return p;
    }

    private JPanel buildResultsTextAreaPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "Results", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        this.resultsArea = new JTextArea();
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
        gbc.ipadx = 2;
        gbc.ipady = 2;
        p.add(this.resultsArea, gbc);
        return p;
    }

    class RefreshListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            processRefresh();
        }
    }

    class ProtocolWrapper {

        public DistributionProtocolImpl prtcl = null;

        public ProtocolWrapper(DistributionProtocolImpl prtcl) {
            this.prtcl = prtcl;
        }

        public String toString() {
            return prtcl.getTitle();
        }
    }

    private void processRefresh() {
        if (this.cfg.getCurrentUser() != null) {
            if (this.cfg.getCtrmDataSourceManager() == null) {
                this.cfg.setCtrmDataSourceManager(new CaTIES_DataSourceManager());
                this.cfg.getCtrmDataSourceManager().getMySQLSecureAdministrationRemoteSession(this.cfg.getSpiritIpAddress(), this.cfg.getSpiritPort());
            }
            StringBuffer sb = new StringBuffer();
            sb.append("select o ");
            sb.append("from ");
            sb.append("UserImpl u, ");
            sb.append("UserOrganizationImpl uo, ");
            sb.append("OrganizationImpl o ");
            sb.append("WHERE ");
            sb.append("uo.organization = o ");
            sb.append("and ");
            sb.append("uo.user = u ");
            sb.append("and ");
            sb.append("uo.role = 'Administrator'");
            sb.append("and ");
            sb.append("u.username = :username");
            String queryAsString = sb.toString();
            Query q = this.cfg.getCtrmDataSourceManager().getSession().createQuery(queryAsString);
            q.setProperties(this.cfg.getCurrentUser());
            this.currentUserOrg = (OrganizationImpl) q.uniqueResult();
            sb = new StringBuffer();
            sb.append("select dp1 from " + "\n");
            sb.append("          DistributionProtocolImpl dp1," + "\n");
            sb.append("          DistributionProtocolOrganizationImpl dpo1," + "\n");
            sb.append("          OrganizationImpl o1 " + "\n");
            sb.append("       where " + "\n");
            sb.append("          dp1 = dpo1.distributionProtocol and" + "\n");
            sb.append("          o1 = dpo1.organization and" + "\n");
            sb.append("          o1.abbreviationName not like :abbreviationName and " + "\n");
            sb.append("          dp1 not in (" + "\n");
            sb.append("                        select dp from " + "\n");
            sb.append("                                               DistributionProtocolImpl dp," + "\n");
            sb.append("                                               DistributionProtocolOrganizationImpl dpo," + "\n");
            sb.append("                                               OrganizationImpl o" + "\n");
            sb.append("                               where" + "\n");
            sb.append("                                               dp = dpo.distributionProtocol and" + "\n");
            sb.append("                                               o = dpo.organization and " + "\n");
            sb.append("                                               o.abbreviationName like :abbreviationName )" + "\n");
            queryAsString = sb.toString();
            q = this.cfg.getCtrmDataSourceManager().getSession().createQuery(queryAsString);
            q.setProperties(this.currentUserOrg);
            @SuppressWarnings("unchecked") List<DistributionProtocolImpl> protocols = q.list();
            for (DistributionProtocolImpl protocol : protocols) {
                ProtocolWrapper protocolWrapper = new ProtocolWrapper(protocol);
                DefaultListModel model = (DefaultListModel) this.list.getModel();
                model.add(0, protocolWrapper);
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (list.getSelectedIndex() == -1) {
                joinButton.setEnabled(false);
                this.currentProtocol = null;
                this.protocolTitleTextField.setText("");
            } else {
                ProtocolWrapper wrapper = (ProtocolWrapper) list.getSelectedValue();
                this.currentProtocol = wrapper.prtcl;
                this.protocolTitleTextField.setText(wrapper.toString());
                DistributionProtocolOrganizationImpl dpo = new DistributionProtocolOrganizationImpl();
                resetIrbInformation(dpo);
                fillIrbInformation(dpo);
                joinButton.setEnabled(true);
            }
        }
    }

    public void processJoin() {
        buildOrgGraphAndPushGridMaps();
        assignInDb();
        reportSuccessfullJoin();
    }

    private void reportSuccessfullJoin() {
        if (this.currentProtocol != null) {
            this.resultsArea.append("Successfully joined " + this.currentProtocol.getShortTitle() + "\n");
        }
    }

    private void assignInDb() {
        Transaction tx = this.cfg.getCtrmDataSourceManager().getSession().beginTransaction();
        DistributionProtocolOrganizationImpl dpo = new DistributionProtocolOrganizationImpl();
        scrapeIrbInformation(dpo);
        this.currentProtocol.addDistributionProtocolOrganization(dpo);
        this.currentUserOrg.addDistributionProtocolOrganization(dpo);
        Session session = this.cfg.getCtrmDataSourceManager().getSession();
        session.saveOrUpdate(this.currentProtocol);
        session.saveOrUpdate(this.currentUserOrg);
        session.saveOrUpdate(dpo);
        session.flush();
        tx.commit();
    }

    private void buildOrgGraphAndPushGridMaps() {
        StringBuffer sb = new StringBuffer();
        sb.append("select u ");
        sb.append("from ");
        sb.append("UserImpl u, ");
        sb.append("DistributionProtocolImpl p, ");
        sb.append("DistributionProtocolAssignmentImpl dpa, ");
        sb.append("DistributionProtocolOrganizationImpl dpo ");
        sb.append("WHERE ");
        sb.append("dpa.user = u ");
        sb.append("and ");
        sb.append("dpa.distributionProtocolOrganization = dpo ");
        sb.append("and ");
        sb.append("dpo.distributionProtocol = p ");
        sb.append("and ");
        sb.append("dpa.role = 'Researcher' ");
        sb.append("and ");
        sb.append("p = :prtcl");
        String queryAsString = sb.toString();
        Query q = this.cfg.getCtrmDataSourceManager().getSession().createQuery(queryAsString);
        q.setEntity("prtcl", this.currentProtocol);
        @SuppressWarnings("unchecked") List<UserImpl> users = q.list();
        if (users.size() > 0) {
            OrganizationImpl parentPlaceHolderOrg = new OrganizationImpl();
            OrganizationImpl currentOrgProxy = new OrganizationImpl();
            parentPlaceHolderOrg.addOrganization(currentOrgProxy);
            currentOrgProxy.setAbbreviationName(this.currentUserOrg.getAbbreviationName());
            currentOrgProxy.setName(this.currentUserOrg.getName());
            currentOrgProxy.setPublicIP(this.currentUserOrg.getPublicIP());
            currentOrgProxy.setPublicHTTPPort(this.currentUserOrg.getPublicHTTPPort());
            for (UserImpl user : users) {
                logger.debug("Will add " + user.getDistinguishedName());
                UserOrganizationImpl userOrg = new UserOrganizationImpl();
                userOrg.setRole("Researcher");
                UserImpl remoteResearcher = new UserImpl();
                remoteResearcher.setUsername(user.getUsername());
                remoteResearcher.setDistinguishedName(user.getDistinguishedName());
                remoteResearcher.addUserOrganization(userOrg);
                currentOrgProxy.addUserOrganization(userOrg);
            }
            pushGridMaps(parentPlaceHolderOrg);
        }
    }

    private void scrapeIrbInformation(DistributionProtocolOrganizationImpl dpo) {
        dpo.setIrbIdentifier(this.irbIdentifierTextField.getText());
        dpo.setIrbApprovalDate(CaTIES_FormatUtils.parseDateFromMySqlString(this.irbApprovalDateTextField.getText()));
        dpo.setIrbApprovalDate(CaTIES_FormatUtils.parseDateFromMySqlString(this.irbExpirationDateTextField.getText()));
        dpo.setRole(getSelectedRole());
    }

    private void fillIrbInformation(DistributionProtocolOrganizationImpl dpo) {
        this.irbIdentifierTextField.setText(dpo.getIrbIdentifier());
        this.irbApprovalDateTextField.setText(CaTIES_FormatUtils.formatDateForMySQL(dpo.getIrbApprovalDate()));
        this.irbExpirationDateTextField.setText(CaTIES_FormatUtils.formatDateForMySQL(dpo.getIrbExpirationDate()));
    }

    private void resetIrbInformation(DistributionProtocolOrganizationImpl dpo) {
        Random generator = new Random();
        generator.nextInt();
        String randomIrbNumber = CaTIES_FormatUtils.formatIntegerAsDigitString(generator.nextInt(7), "0000000");
        dpo.setIrbIdentifier("IRB-" + randomIrbNumber);
        dpo.setRole(DistributionProtocolOrganizationImpl.ROLE_TISSUE_PROVIDER);
        dpo.setIrbApprovalDate(new Date());
        Long startDateAsLong = dpo.getIrbApprovalDate().getTime();
        Long endDateAsLong = startDateAsLong + 10L * 365L * 24L * 60L * 60L * 1000L;
        dpo.setIrbExpirationDate(new Date(endDateAsLong));
    }

    private void pushGridMaps(OrganizationImpl parentPlaceHolderOrg) {
        try {
            OrganizationImpl spiritOrg = new OrganizationImpl();
            spiritOrg.setPublicIP(CaTIES_SpiritServer.getInstance().getSpiritIpAddress());
            spiritOrg.setPublicHTTPPort(CaTIES_SpiritServer.getInstance().getSpiritPort());
            String className = "edu.upmc.opi.caBIG.caTIES.server.dispatcher.provisioning.CaTIES_HubGridMapPusher";
            CaTIES_Dispatcher dispatcher = CaTIES_DispatcherFactory.createAdminDispatcher(spiritOrg, className);
            if (!dispatcher.connect()) {
                logger.error("Failed to initialize hub remote command processor");
            }
            String responsePayLoad = dispatcher.processCommand(CaBIG_LobUtilities.encodeLob(parentPlaceHolderOrg));
            dispatcher.disconnect();
            Object responseObject = (Object) CaBIG_LobUtilities.decodeLob(responsePayLoad.getBytes());
            if (responseObject instanceof CaTIES_Error) {
                logger.warn(((CaTIES_Error) responseObject).getExceptionAsString());
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        logger.debug("Got action " + ae.getActionCommand());
        if (ae.getActionCommand().equalsIgnoreCase("Refresh")) {
            processRefresh();
        } else if (ae.getActionCommand().equalsIgnoreCase("Clear")) {
            processClear();
        } else if (ae.getActionCommand().equalsIgnoreCase("Join Protocol")) {
            processJoin();
        }
    }

    private void processClear() {
        DistributionProtocolOrganizationImpl dpo = new DistributionProtocolOrganizationImpl();
        resetIrbInformation(dpo);
        fillIrbInformation(dpo);
        ((DefaultListModel) this.list.getModel()).clear();
        this.resultsArea.setText("");
    }

    public void setInstallationUI(InstallationUI installationUI) {
        this.installationUI = installationUI;
    }

    public InstallationUI getInstallationUI() {
        return installationUI;
    }
}
