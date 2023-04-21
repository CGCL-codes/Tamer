package researchgrants.parts.GrantRequest.agency;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import researchgrants.parts.LoggedData.LoggedData;
import researchgrants.parts.LoggedData.panels.LoggedDataPanelsText;
import researchgrants.parts.LoggedData.panels.LoggedDataPanelsTextArea;
import researchgrants.parts.db.Db;
import researchgrants.parts.panelsManager.LoggedDataPanelsManager;
import researchgrants.utils.MyDialog;

/**
 *
 * @author  DOStudent1
 */
public class AgencyContactView extends MyDialog {

    private static final int hgap = 0;

    AgencyContact agencyContact;

    boolean isNewAgencyContact;

    int agencyId;

    private LoggedDataPanelsManager nameView;

    private LoggedDataPanelsManager phoneView;

    private LoggedDataPanelsManager emailView;

    private LoggedDataPanelsManager addressView;

    private LoggedDataPanelsManager notesView;

    JButton btnSaveAll;

    AgencyContactView(Window parent, int agencyId) {
        super(parent);
        isNewAgencyContact = true;
        this.agencyId = agencyId;
        initComponents();
        initDisplay();
    }

    AgencyContactView(Window parent, AgencyContact agencyContact) {
        super(parent);
        this.agencyContact = agencyContact;
        isNewAgencyContact = false;
        initComponents();
        initDisplay();
    }

    private void createNewAgencyContactFromFields() {
        try {
            Connection conn = Db.openDbConnection();
            PreparedStatement createAgencyContactStatement = Db.createPreparedStatement(conn, "INSERT INTO tblAgencyContacts (AgencyID, NameRef, PhoneRef, EmailRef, AddressRef, NotesRef) VALUES (?, ?, ?, ?, ?, ?)");
            createAgencyContactStatement.setInt(1, agencyId);
            createAgencyContactStatement.setInt(2, saveLoggedDataPanelsManagerData(nameView));
            createAgencyContactStatement.setInt(3, saveLoggedDataPanelsManagerData(phoneView));
            createAgencyContactStatement.setInt(4, saveLoggedDataPanelsManagerData(emailView));
            createAgencyContactStatement.setInt(5, saveLoggedDataPanelsManagerData(addressView));
            createAgencyContactStatement.setInt(6, saveLoggedDataPanelsManagerData(notesView));
            createAgencyContactStatement.executeUpdate();
            int newAgencyContactId = Db.getLastIdentity();
            createAgencyContactStatement.close();
            Db.closeDbConnection(conn);
            this.agencyContact = AgencyContact.getById(newAgencyContactId);
        } catch (SQLException ex) {
            Logger.getLogger(AgencyContactView.class.getName()).log(Level.SEVERE, null, ex);
        }
        isNewAgencyContact = false;
        initDisplay();
    }

    private void initDisplay() {
        if (isNewAgencyContact) {
            btnSaveAll = new JButton("Save New Agency Contact");
            btnSaveAll.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    createNewAgencyContactFromFields();
                }
            });
        }
        if (isNewAgencyContact) {
            lblId.setText("<Auto>");
        } else {
            lblId.setText(Integer.toString(agencyContact.getId()));
        }
        LoggedData name = null;
        LoggedData phone = null;
        LoggedData email = null;
        LoggedData address = null;
        LoggedData notes = null;
        if (!isNewAgencyContact) {
            name = agencyContact.getLoggedDataName();
            phone = agencyContact.getLoggedDataPhone();
            email = agencyContact.getLoggedDataEmail();
            address = agencyContact.getLoggedDataAddress();
            notes = agencyContact.getLoggedDataNotes();
        }
        nameView = createLoggedDataPanelManager(false, isNewAgencyContact, new LoggedDataPanelsText(), this, name, pnlFieldName);
        phoneView = createLoggedDataPanelManager(false, isNewAgencyContact, new LoggedDataPanelsText(), this, phone, pnlFieldPhone);
        emailView = createLoggedDataPanelManager(false, isNewAgencyContact, new LoggedDataPanelsText(), this, email, pnlFieldEmail);
        addressView = createLoggedDataPanelManager(false, isNewAgencyContact, new LoggedDataPanelsTextArea(), this, address, pnlFieldAddress);
        notesView = createLoggedDataPanelManager(false, isNewAgencyContact, new LoggedDataPanelsText(), this, notes, pnlFieldNotes);
        JPanel pnlOptionalButtonsInner = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlOptionalButtonsInner.setLayout(new GridLayout());
        boolean displayButtonsPanel = false;
        if (isNewAgencyContact) {
            pnlOptionalButtonsInner.add(btnSaveAll);
            displayButtonsPanel = true;
        }
        pnlOptionalButtons.removeAll();
        if (displayButtonsPanel) {
            pnlOptionalButtons.add(wrapSection(pnlOptionalButtonsInner, "Options"));
        }
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    public AgencyContact getAgencyContact() {
        return (agencyContact);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        pnlID = new javax.swing.JPanel();
        lblCaptionId = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        pnlName = new javax.swing.JPanel();
        lblCaptionName = new javax.swing.JLabel();
        pnlFieldName = new javax.swing.JPanel();
        pnlPhone = new javax.swing.JPanel();
        lblCaptionPhone = new javax.swing.JLabel();
        pnlFieldPhone = new javax.swing.JPanel();
        pnlAddress = new javax.swing.JPanel();
        lblCaptionAddress = new javax.swing.JLabel();
        pnlFieldAddress = new javax.swing.JPanel();
        pnlEmail = new javax.swing.JPanel();
        lblCaptionEmail = new javax.swing.JLabel();
        pnlFieldEmail = new javax.swing.JPanel();
        pnlNotes = new javax.swing.JPanel();
        pnlNotes2 = new javax.swing.JPanel();
        lblCaptionNotes = new javax.swing.JLabel();
        pnlFieldNotes = new javax.swing.JPanel();
        pnlOptionalButtons = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(researchgrants.ResearchGrantsApp.class).getContext().getResourceMap(AgencyContactView.class);
        setTitle(resourceMap.getString("Form.title"));
        setName("Form");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));
        pnlID.setName("pnlID");
        pnlID.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, hgap, 0));
        lblCaptionId.setText(resourceMap.getString("lblCaptionId.text"));
        lblCaptionId.setName("lblCaptionId");
        pnlID.add(lblCaptionId);
        lblId.setText(resourceMap.getString("lblId.text"));
        lblId.setName("lblId");
        pnlID.add(lblId);
        getContentPane().add(pnlID);
        pnlName.setName("pnlName");
        pnlName.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, hgap, 0));
        lblCaptionName.setText(resourceMap.getString("lblCaptionName.text"));
        lblCaptionName.setName("lblCaptionName");
        pnlName.add(lblCaptionName);
        pnlFieldName.setName("pnlFieldName");
        pnlFieldName.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        pnlName.add(pnlFieldName);
        getContentPane().add(pnlName);
        pnlPhone.setName("pnlPhone");
        pnlPhone.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, hgap, 0));
        lblCaptionPhone.setText(resourceMap.getString("lblCaptionPhone.text"));
        lblCaptionPhone.setName("lblCaptionPhone");
        pnlPhone.add(lblCaptionPhone);
        pnlFieldPhone.setName("pnlFieldPhone");
        pnlFieldPhone.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        pnlPhone.add(pnlFieldPhone);
        getContentPane().add(pnlPhone);
        pnlAddress.setName("pnlAddress");
        pnlAddress.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, hgap, 0));
        lblCaptionAddress.setText(resourceMap.getString("lblCaptionAddress.text"));
        lblCaptionAddress.setName("lblCaptionAddress");
        pnlAddress.add(lblCaptionAddress);
        pnlFieldAddress.setName("pnlFieldAddress");
        pnlFieldAddress.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        pnlAddress.add(pnlFieldAddress);
        getContentPane().add(pnlAddress);
        pnlEmail.setName("pnlEmail");
        pnlEmail.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, hgap, 0));
        lblCaptionEmail.setText(resourceMap.getString("lblCaptionEmail.text"));
        lblCaptionEmail.setName("lblCaptionEmail");
        pnlEmail.add(lblCaptionEmail);
        pnlFieldEmail.setName("pnlFieldEmail");
        pnlFieldEmail.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        pnlEmail.add(pnlFieldEmail);
        getContentPane().add(pnlEmail);
        pnlNotes.setName("pnlNotes");
        pnlNotes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, hgap, 0));
        pnlNotes2.setName("pnlNotes2");
        pnlNotes2.setLayout(new java.awt.GridBagLayout());
        lblCaptionNotes.setText(resourceMap.getString("lblCaptionNotes.text"));
        lblCaptionNotes.setName("lblCaptionNotes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlNotes2.add(lblCaptionNotes, gridBagConstraints);
        pnlFieldNotes.setName("pnlFieldNotes");
        pnlFieldNotes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlNotes2.add(pnlFieldNotes, gridBagConstraints);
        pnlNotes.add(pnlNotes2);
        getContentPane().add(pnlNotes);
        pnlOptionalButtons.setName("pnlOptionalButtons");
        pnlOptionalButtons.setLayout(new javax.swing.BoxLayout(pnlOptionalButtons, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(pnlOptionalButtons);
        pack();
    }

    private javax.swing.JLabel lblCaptionAddress;

    private javax.swing.JLabel lblCaptionEmail;

    private javax.swing.JLabel lblCaptionId;

    private javax.swing.JLabel lblCaptionName;

    private javax.swing.JLabel lblCaptionNotes;

    private javax.swing.JLabel lblCaptionPhone;

    private javax.swing.JLabel lblId;

    private javax.swing.JPanel pnlAddress;

    private javax.swing.JPanel pnlEmail;

    private javax.swing.JPanel pnlFieldAddress;

    private javax.swing.JPanel pnlFieldEmail;

    private javax.swing.JPanel pnlFieldName;

    private javax.swing.JPanel pnlFieldNotes;

    private javax.swing.JPanel pnlFieldPhone;

    private javax.swing.JPanel pnlID;

    private javax.swing.JPanel pnlName;

    private javax.swing.JPanel pnlNotes;

    private javax.swing.JPanel pnlNotes2;

    private javax.swing.JPanel pnlOptionalButtons;

    private javax.swing.JPanel pnlPhone;
}
