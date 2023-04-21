package edu.upmc.opi.caBIG.caTIES.installer.panel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
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
import org.jdom.Element;
import edu.upmc.opi.caBIG.caTIES.client.dispatcher.CaTIES_Dispatcher;
import edu.upmc.opi.caBIG.caTIES.client.dispatcher.CaTIES_DispatcherFactory;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Constants;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Error;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Utils;
import edu.upmc.opi.caBIG.caTIES.connector.CaTIES_DataSourceManager;
import edu.upmc.opi.caBIG.caTIES.connector.CaTIES_SpiritServer;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.DistributionProtocolImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.OrganizationImpl;
import edu.upmc.opi.caBIG.caTIES.installer.CaTIES_ConfigurationProperties;
import edu.upmc.opi.caBIG.caTIES.installer.InstallationUI;
import edu.upmc.opi.caBIG.caTIES.server.dispatcher.coding.CaTIES_Coder;
import edu.upmc.opi.caBIG.common.CaBIG_LobUtilities;

public class CaTIES_CoderTabPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = -3992337870183224526L;

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_CoderTabPanel.class);

    private CaTIES_ConfigurationProperties cfg;

    private final JList localProtocolList = new JList(new DefaultListModel());

    private final JTextField searchTextField = new JTextField();

    private final DefaultListModel localProtocolListModel = (DefaultListModel) localProtocolList.getModel();

    private static final String btnNameRefresh = "Refresh";

    private static final String btnNameSearch = "Search";

    private static final String btnNameClear = "Clear";

    private JButton btnRefresh = new JButton(btnNameRefresh);

    private JButton btnSearch = new JButton(btnNameSearch);

    private JButton btnClear = new JButton(btnNameClear);

    private JTextArea resultsArea;

    private JTable lineItemTable = null;

    private InstallationUI installationUI;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Testing CaTIES_TabPanelCoder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = new CaTIES_CoderTabPanel(null, CaTIES_ConfigurationProperties.getInstance());
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.setSize(new Dimension(600, 1000));
        frame.pack();
        CaTIES_Utils.centerComponent(frame);
        frame.setVisible(true);
    }

    public CaTIES_CoderTabPanel(InstallationUI installationUI, CaTIES_ConfigurationProperties cfg) {
        logger.debug("Constructing a CaTIES_ResearcherTabPanel");
        this.installationUI = installationUI;
        this.cfg = cfg;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.25;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        add(buildSearchTermEntryPanel(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        add(buildActionPanel(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.75;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        add(buildResultsPanel(), gbc);
        enableActions();
    }

    private JPanel buildSearchTermEntryPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "Search Term", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        JLabel searchLabel = new JLabel("Search Term");
        searchLabel.setLabelFor(this.searchTextField);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        p.add(searchLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(searchTextField, gbc);
        return p;
    }

    private JPanel buildResultsPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "Results", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        CaTIES_TableModelCoderResults tableModel = new CaTIES_TableModelCoderResults();
        tableModel.fillDummyRow();
        this.lineItemTable = new JTable();
        this.lineItemTable.setModel(tableModel);
        this.lineItemTable.setPreferredSize(new Dimension(600, 250));
        ListSelectionModel listMod = this.lineItemTable.getSelectionModel();
        listMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listMod.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ;
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.ipadx = 2;
        gbc.ipady = 2;
        p.add(this.lineItemTable, gbc);
        return p;
    }

    private String processSearch() {
        String response = "Failed to read document.";
        try {
            System.setProperty(CaTIES_Constants.PROPERTY_KEY_HUB_LOCATION, CaTIES_SpiritServer.getInstance().getInstallerPath());
            String searchString = this.searchTextField.getText();
            Element phraseElement = new Element("Phrase");
            phraseElement.setText(searchString);
            Element phraseRequestElement = new Element("PhraseRequest");
            phraseRequestElement.setAttribute("Limit", "10");
            phraseRequestElement.addContent(phraseElement);
            OrganizationImpl spiritOrg = new OrganizationImpl();
            spiritOrg.setPublicIP(CaTIES_SpiritServer.getInstance().getSpiritIpAddress());
            spiritOrg.setPublicHTTPPort(CaTIES_SpiritServer.getInstance().getSpiritPort());
            CaTIES_Dispatcher dispatcher = CaTIES_DispatcherFactory.createAnonymousUnEncryptedDispatcher(spiritOrg, CaTIES_Coder.class.getName());
            if (!dispatcher.connect()) {
                logger.error("Failed to connect to spirit organization.");
            }
            response = dispatcher.processCommand(CaBIG_LobUtilities.encodeLob(phraseRequestElement));
            dispatcher.disconnect();
            Object responseObject = CaBIG_LobUtilities.decodeLob(response.getBytes());
            if (responseObject instanceof CaTIES_Error) {
                System.err.println(((CaTIES_Error) responseObject).getExceptionAsString());
            } else {
                response = (String) responseObject;
                logger.debug(response);
                CaTIES_TableModelCoderResults tableModel = new CaTIES_TableModelCoderResults();
                tableModel.addPayLoad(response);
                this.lineItemTable.setModel(tableModel);
                this.lineItemTable.repaint();
            }
        } catch (Exception x) {
            x.printStackTrace();
            response = "Failed to read document.";
        }
        return response;
    }

    private JPanel buildActionPanel() {
        JPanel p = new JPanel();
        Border border = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(2, 2, 2, 2));
        border = new TitledBorder(border, "Actions", TitledBorder.LEFT, TitledBorder.TOP);
        p.setBorder(border);
        GridLayout gridLayout = new GridLayout(1, 3, 2, 2);
        p.setLayout(gridLayout);
        this.btnRefresh.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnRefresh);
        p.add(buttonPanel);
        this.btnClear.addActionListener(this);
        buttonPanel = new JPanel();
        buttonPanel.add(btnClear);
        p.add(buttonPanel);
        this.btnSearch.addActionListener(this);
        buttonPanel = new JPanel();
        buttonPanel.add(btnSearch);
        p.add(buttonPanel);
        return p;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        logger.debug("Got action " + ae.getActionCommand());
        if (ae.getActionCommand().equalsIgnoreCase("Refresh")) {
            processRefresh();
        } else if (ae.getActionCommand().equalsIgnoreCase("Clear")) {
            processClear();
        } else if (ae.getActionCommand().equalsIgnoreCase("Search")) {
            processSearch();
        }
    }

    private synchronized void processRefresh() {
        if (this.cfg.getCurrentUser() != null) {
            if (this.cfg.getCtrmDataSourceManager() == null) {
                this.cfg.setCtrmDataSourceManager(new CaTIES_DataSourceManager());
                this.cfg.getCtrmDataSourceManager().getMySQLRemoteSessionWithActivityId(this.cfg.getSpiritIpAddress(), this.cfg.getSpiritPort(), "Secure");
            }
            fillLocalProtocolsList();
        }
    }

    private void fillLocalProtocolsList() {
        StringBuffer sb = null;
        sb = new StringBuffer();
        sb.append("select p ");
        sb.append("from ");
        sb.append("UserImpl u, ");
        sb.append("DistributionProtocolAssignmentImpl dpa, ");
        sb.append("DistributionProtocolImpl p, ");
        sb.append("DistributionProtocolOrganizationImpl dpo ");
        sb.append("WHERE ");
        sb.append("dpa.user = u ");
        sb.append("and ");
        sb.append("dpa.distributionProtocolOrganization = dpo ");
        sb.append("and ");
        sb.append("dpo.distributionProtocol = p ");
        sb.append("and ");
        sb.append("u.username = :username ");
        String queryAsString = sb.toString();
        Query q = this.cfg.getCtrmDataSourceManager().getSession().createQuery(queryAsString);
        q.setProperties(this.cfg.getCurrentUser());
        @SuppressWarnings("unchecked") List<DistributionProtocolImpl> protocols = q.list();
        for (DistributionProtocolImpl protocol : protocols) {
            ProtocolWrapper protocolWrapper = new ProtocolWrapper(protocol);
            this.localProtocolListModel.add(0, protocolWrapper);
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

    private void processClear() {
        this.searchTextField.setText("");
        ((CaTIES_TableModelCoderResults) this.lineItemTable.getModel()).clear();
        this.lineItemTable.repaint();
    }

    public void enableActions() {
        recursivelyFindAndSetEnableStatus(this, true);
    }

    public void disableActions() {
        recursivelyFindAndSetEnableStatus(this, false);
    }

    private void recursivelyFindAndSetEnableStatus(Component component, boolean isEnabled) {
        if (component instanceof JButton) {
            JButton btn = (JButton) component;
            btn.setEnabled(isEnabled);
        } else if (component instanceof Container) {
            Container container = (Container) component;
            for (int idx = 0; idx < container.getComponentCount(); idx++) {
                recursivelyFindAndSetEnableStatus(container.getComponent(idx), isEnabled);
            }
        }
    }

    public InstallationUI getInstallationUI() {
        return installationUI;
    }

    public void setInstallationUI(InstallationUI installationUI) {
        this.installationUI = installationUI;
    }
}
