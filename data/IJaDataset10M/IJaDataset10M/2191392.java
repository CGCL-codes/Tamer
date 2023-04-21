package clubmaster;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Hashtable;
import javax.swing.event.ListSelectionEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * The application's main frame.
 */
public class ClubMasterView extends FrameView {

    private static final String S_LIST_DEFAULT = "Select";

    private int iChildId = -1;

    private int iParentId = -1;

    private String sCFname;

    private String sCLname;

    private String sCGrade;

    private String sCRating;

    private String sCDues;

    private String sCStatus;

    private String sCScore1;

    private String sCScore2;

    private String sGender;

    private String sTeacher;

    private String sPurchShirt;

    private String sFName;

    private String sLName;

    private String sPhone1;

    private String sPhone2;

    private String sPhone3;

    private String sPhoneType1;

    private String sPhoneType2;

    private String sPhoneType3;

    private String sEmail1;

    private String sEmail2;

    private String sVolunteer;

    /**
     *
     * @param app
     */
    public ClubMasterView(SingleFrameApplication app) {
        super(app);
        System.out.println("DEBUG->ClubMasterView initComponents");
        initComponents();
        System.out.println("DEBUG->ClubMasterView clubProps");
        clubProps = new ClubProps();
        System.out.println("DEBUG->ClubMasterView DbActions");
        dbActions = new DbActions(clubProps);
        initLoadData();
        initForm();
        jtabMembers.setColumnSelectionAllowed(false);
        jtabMembers.setCellSelectionEnabled(false);
        jtabMembers.setRowSelectionAllowed(true);
        jtabMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = jtabMembers.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsmData = (ListSelectionModel) e.getSource();
                if (!lsmData.isSelectionEmpty()) {
                    int iRow = lsmData.getMinSelectionIndex();
                    try {
                        iChildId = Integer.parseInt(jtabMembers.getValueAt(iRow, 0).toString());
                        sCFname = jtabMembers.getValueAt(iRow, 1).toString();
                        sCLname = jtabMembers.getValueAt(iRow, 2).toString();
                        sGender = jtabMembers.getValueAt(iRow, 3).toString();
                        sCGrade = jtabMembers.getValueAt(iRow, 4).toString();
                        sTeacher = jtabMembers.getValueAt(iRow, 5).toString();
                        sCRating = jtabMembers.getValueAt(iRow, 6).toString();
                        sCStatus = jtabMembers.getValueAt(iRow, 7).toString();
                        sCDues = jtabMembers.getValueAt(iRow, 8).toString();
                        sPurchShirt = jtabMembers.getValueAt(iRow, 9).toString();
                        sCScore1 = jtabMembers.getValueAt(iRow, 10).toString();
                        firstNameField.setText(sCFname);
                        lastNameField.setText(sCLname);
                        jcbGrade.setSelectedItem(sCGrade);
                        ratingField.setText(sCRating);
                        jcbStatus.setSelectedItem(sCStatus);
                        jcbDuesPaid.setSelectedItem(sCDues);
                        jcbPurchShirt.setSelectedItem(sPurchShirt);
                        jtfTeacher.setText(sTeacher);
                        testScore1Field.setText(sCScore1);
                        testScore2Field.setText(sCScore2);
                        enableParentFields();
                        enableSelectedButtons();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "ClubMasterView valueChanged EXC [" + e1.getMessage() + "]");
                        System.out.println("DEBUG->ClubMasterView valueChanged EXC [" + e1.getMessage() + "]");
                    }
                }
            }
        });
        ListSelectionModel rowPM = jtabParents.getSelectionModel();
        rowPM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsmData = (ListSelectionModel) e.getSource();
                if (!lsmData.isSelectionEmpty()) {
                    int iRow = lsmData.getMinSelectionIndex();
                    try {
                        iParentId = Integer.parseInt(jtabParents.getValueAt(iRow, 0).toString());
                        String sFullName = jtabParents.getValueAt(iRow, 1).toString();
                        sLName = sFullName.substring(0, sFullName.indexOf(","));
                        sFName = sFullName.substring(sFullName.indexOf(",") + 1);
                        String sPhone1Full = jtabParents.getValueAt(iRow, 2).toString();
                        sPhone1 = sPhone1Full.substring(0, sPhone1Full.indexOf("/"));
                        sPhoneType1 = sPhone1Full.substring(sPhone1Full.indexOf("/") + 1);
                        String sPhone2Full = jtabParents.getValueAt(iRow, 3).toString();
                        sPhone2 = sPhone2Full.substring(0, sPhone2Full.indexOf("/"));
                        sPhoneType2 = sPhone2Full.substring(sPhone2Full.indexOf("/") + 1);
                        String sPhone3Full = jtabParents.getValueAt(iRow, 4).toString();
                        sPhone3 = sPhone3Full.substring(0, sPhone3Full.indexOf("/"));
                        sPhoneType3 = sPhone3Full.substring(sPhone3Full.indexOf("/"));
                        sEmail1 = jtabParents.getValueAt(iRow, 5).toString();
                        sEmail2 = jtabParents.getValueAt(iRow, 6).toString();
                        sVolunteer = jtabParents.getValueAt(iRow, 7).toString();
                        if (sPhone2.equals("NULL")) {
                            sPhone2 = "";
                        }
                        if (sPhone3.equals("NULL")) {
                            sPhone3 = "";
                        }
                        if (sEmail2.equals("NULL")) {
                            sEmail2 = "";
                        }
                        jtfParFname.setText(sFName);
                        jtfParLname.setText(sLName);
                        jtfParPhone1.setText(sPhone1);
                        jcbPhoneType1.setSelectedItem(sPhoneType1);
                        jtfParPhone2.setText(sPhone2);
                        jcbPhoneType2.setSelectedItem(sPhoneType2);
                        jtfParPhone3.setText(sPhone3);
                        jcbPhoneType3.setSelectedItem(sPhoneType3);
                        jtfParEmail1.setText(sEmail1);
                        jtfParEmail2.setText(sEmail2);
                        jcbVolunteer.setSelectedItem(sVolunteer);
                        enableParentFields();
                        enableSelectedButtons();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "ClubMasterView valueChanged(2) EXC [" + e1.getMessage() + "]");
                        System.out.println("DEBUG->ClubMasterView valueChanged(2) EXC [" + e1.getMessage() + "]");
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     *
     */
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ClubMasterApp.getApplication().getMainFrame();
            aboutBox = new ClubMasterAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ClubMasterApp.getApplication().show(aboutBox);
    }

    private void initLoadData() {
        Object[][] objRsData = dbActions.initLoadMembers("");
        Object[][] objRsPar = dbActions.initLoadParents();
        if (objRsData != null) {
            try {
                System.out.println("DEBUG->ClubMasterView initLoadData Length(1) [" + objRsData.length + "]");
                jtabMembers.setModel(new javax.swing.table.DefaultTableModel(objRsData, new String[] { "ID", "First Name", "Last Name", "Gender", "Grade", "Teacher", "Rating", "Status", "Dues Paid", "T-Shirt", "Test Score 1", "Test Score 2" }));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ClubMasterView initLoadData Exc [" + e.getMessage() + "]");
                System.out.println("DEBUG->ClubMasterView initLoadData Exc [" + e.getMessage() + "]");
            }
        }
        if (objRsPar != null) {
            try {
                System.out.println("DEBUG->ClubMasterView initLoadData Length(1) [" + objRsPar.length + "]");
                jtabParents.setModel(new javax.swing.table.DefaultTableModel(objRsPar, new String[] { "ID", "Name", "Main Phone/Type", "PhoneType", "Phone/Type", "Email", "Other", "Volunteer" }));
            } catch (Exception e) {
                System.out.println("DEBUG->ClubMasterView initLoadData (Parents) EXC [" + e.getMessage() + "]");
            }
        }
    }

    /**
     *
     */
    @Action
    public void initForm() {
        clearForm();
        clearParentFields();
        enableFields();
        disableParentFields();
    }

    private void disableParentFields() {
        jtfParFname.setEnabled(false);
        jtfParLname.setEnabled(false);
        jtfParPhone1.setEnabled(false);
        jtfParPhone2.setEnabled(false);
        jtfParPhone3.setEnabled(false);
        jtfParEmail1.setEnabled(false);
        jtfParEmail2.setEnabled(false);
        jcbPhoneType1.setEnabled(false);
        jcbPhoneType2.setEnabled(false);
        jcbPhoneType3.setEnabled(false);
        jcbVolunteer.setEnabled(false);
        this.jbSelExisting.setEnabled(false);
        this.jbParAdd.setEnabled(false);
        this.jbParClear.setEnabled(false);
        this.jbParRep.setEnabled(false);
        this.jbLink.setEnabled(false);
    }

    private void enableParentFields() {
        jtfParFname.setEnabled(true);
        jtfParLname.setEnabled(true);
        jtfParPhone1.setEnabled(true);
        jtfParPhone2.setEnabled(true);
        jtfParPhone3.setEnabled(true);
        jtfParEmail1.setEnabled(true);
        jtfParEmail2.setEnabled(true);
        jcbPhoneType1.setEnabled(true);
        jcbPhoneType2.setEnabled(true);
        jcbPhoneType3.setEnabled(true);
        jcbVolunteer.setEnabled(true);
        this.jbSelExisting.setEnabled(true);
        this.jbParAdd.setEnabled(true);
        this.jbParClear.setEnabled(true);
        this.jbParRep.setEnabled(true);
    }

    private void clearParentFields() {
        jtfParFname.setText("");
        jtfParLname.setText("");
        jtfParPhone1.setText("");
        jtfParPhone2.setText("");
        jtfParPhone3.setText("");
        jtfParEmail1.setText("");
        jtfParEmail2.setText("");
        jcbVolunteer.setSelectedItem(S_LIST_DEFAULT);
        jcbPhoneType1.setSelectedItem(S_LIST_DEFAULT);
        jcbPhoneType2.setSelectedItem(S_LIST_DEFAULT);
        jcbPhoneType3.setSelectedItem(S_LIST_DEFAULT);
        this.jbParRep.setEnabled(false);
        this.jbLink.setEnabled(false);
    }

    private void clearForm() {
        ratingField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        jcbStatus.setSelectedItem(S_LIST_DEFAULT);
        jcbDuesPaid.setSelectedItem(S_LIST_DEFAULT);
        ratingField.setText("");
        testScore1Field.setText("");
        testScore2Field.setText("");
        jtfTeacher.setText("");
        jcbPurchShirt.setSelectedItem("No Shirt");
        jcbGrade.setSelectedItem(S_LIST_DEFAULT);
        jtabMembers.clearSelection();
    }

    private void enableFields() {
        firstNameField.setEnabled(true);
        firstNameField.setEditable(true);
        lastNameField.setEnabled(true);
        jcbStatus.setEnabled(true);
        jcbDuesPaid.setEnabled(true);
        testScore1Field.setEnabled(true);
        testScore2Field.setEnabled(true);
        jbMod.setEnabled(false);
        jbDel.setEnabled(false);
    }

    private void enableSelectedButtons() {
        jbMod.setEnabled(true);
        jbDel.setEnabled(true);
        jbParRep.setEnabled(true);
        jbParClear.setEnabled(true);
    }

    private boolean isChildDataValid() {
        boolean bReturn = true;
        sCFname = firstNameField.getText().toString();
        sCLname = lastNameField.getText().toString();
        sCGrade = jcbGrade.getSelectedItem().toString();
        sCDues = jcbDuesPaid.getSelectedItem().toString();
        sCStatus = jcbStatus.getSelectedItem().toString();
        sCScore1 = testScore1Field.getText().toString();
        sCScore2 = testScore2Field.getText().toString();
        sGender = jcbGender.getSelectedItem().toString();
        sTeacher = jtfTeacher.getText().toString();
        sCRating = ratingField.getText().toString();
        sPurchShirt = jcbPurchShirt.getSelectedItem().toString();
        if (firstNameField.getText().toString().length() == 0 || lastNameField.getText().toString().length() == 0) {
            JOptionPane.showMessageDialog(null, "First Name and Last Name are required");
            System.out.println("DEBUG->isChildDataValid, First Name and Last Name are required");
            return false;
        }
        if (jcbStatus.getSelectedItem().toString().length() == 0 || jcbDuesPaid.getSelectedItem().toString().length() == 0) {
            JOptionPane.showMessageDialog(null, "Status and Dues Paid are required");
            System.out.println("DEBUG->isChildDataValid, Status and Dues Paid are required");
            return false;
        }
        if (jcbGrade.getSelectedItem().toString().equals(S_LIST_DEFAULT)) {
            JOptionPane.showMessageDialog(null, "Grade must be selected");
            System.out.println("DEBUG->isChildDataValid, Grade must be selected");
            return false;
        }
        if (sTeacher.length() == 0) {
            JOptionPane.showMessageDialog(null, "Teacher must be entered");
            System.out.println("DEBUG->isChildDataValid, Teacher is invalid");
            return false;
        }
        try {
            if (testScore1Field.getText().toString().length() > 0) {
                double dTmpScore1 = Double.parseDouble(testScore1Field.getText().toString());
            }
            if (testScore2Field.getText().toString().length() > 0) {
                double dTmpScore2 = Double.parseDouble(testScore2Field.getText().toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Test Score 1 or Test Score 2 invalid [" + e.getMessage() + "]");
            System.out.println("DEBUG->isChildDataValid, Test Score 1 or Test Score 2 invalid [" + e.getMessage() + "]");
            return false;
        }
        return bReturn;
    }

    private boolean isParentDataValid() {
        boolean bReturn = true;
        sFName = jtfParFname.getText().toString();
        sLName = jtfParLname.getText().toString();
        sPhone1 = jtfParPhone1.getText().toString();
        sPhone2 = jtfParPhone2.getText().toString();
        sPhone3 = jtfParPhone3.getText().toString();
        sPhoneType1 = jcbPhoneType1.getSelectedItem().toString();
        sPhoneType2 = jcbPhoneType2.getSelectedItem().toString();
        sPhoneType3 = jcbPhoneType3.getSelectedItem().toString();
        sEmail1 = jtfParEmail1.getText().toString();
        sEmail2 = jtfParEmail2.getText().toString();
        sVolunteer = jcbVolunteer.getSelectedItem().toString();
        if (sFName.length() == 0 || sLName.length() == 0) {
            JOptionPane.showMessageDialog(null, "Parent First and Last name are required");
            System.out.println("DEBUG->Parents isParentDataValid First and Last name are required");
            return false;
        }
        if (sPhone1.length() == 0 || sPhoneType1.equals(S_LIST_DEFAULT)) {
            JOptionPane.showMessageDialog(null, "Parents Phone 1 and Phone Type 1 are required");
            System.out.println("DEBUG->Parents isParentDataValid Phone 1 and Phone Type 1 are required");
            return false;
        }
        if ((sPhone2.length() > 0 && sPhoneType2.equals(S_LIST_DEFAULT)) || (sPhone2.length() == 0 && (!sPhoneType2.equals(S_LIST_DEFAULT)))) {
            JOptionPane.showMessageDialog(null, "Parents Invalid value for Phone 2 or Phone Type 2");
            System.out.println("DEBUG->Parents isParentDataValid Invalid value for Phone 2 or Phone Type 2");
            return false;
        }
        if ((sPhone3.length() > 0 && sPhoneType3.equals(S_LIST_DEFAULT)) || (sPhone3.length() == 0 && (!sPhoneType3.equals(S_LIST_DEFAULT)))) {
            JOptionPane.showMessageDialog(null, "Parents Invalid value for Phone 3 or Phone Type 3");
            System.out.println("DEBUG->Parents isParentDataValid Invalid value for Phone 3 or Phone Type 3");
            return false;
        }
        if (sEmail1.length() == 0) {
            JOptionPane.showMessageDialog(null, "Parents Email 1 is required");
            System.out.println("DEBUG->Parents isParentDataValid Email 1 is required");
            return false;
        }
        if (sVolunteer.equals(S_LIST_DEFAULT)) {
            JOptionPane.showMessageDialog(null, "Parents Volunteer is required");
            System.out.println("DEBUG->Parents isParentDataValid Volunteer is required");
            return false;
        }
        return bReturn;
    }

    /** addToTable
     *   Add a new row to jtabMembers
     *   This is called after a successful database insert
     */
    public void addToTable() {
        DefaultTableModel jtabModel = (DefaultTableModel) jtabMembers.getModel();
        Object[] objTemp = { iChildId, sCFname, sCLname, sGender, sCGrade, sTeacher, sCRating, sCStatus, sCDues, sPurchShirt, sCScore1, sCScore2 };
        jtabModel.insertRow(jtabMembers.getRowCount(), objTemp);
    }

    /** addToParentsTable
     *   Add a new row to jtabParents
     *   This is called after a successful database insert
     */
    private void addToParentsTable() {
        DefaultTableModel jtabModel = (DefaultTableModel) jtabParents.getModel();
        Object[] objTemp = { iParentId, sLName + "," + sFName, sPhone1 + "/" + sPhoneType1, sPhone2 + "/" + sPhoneType2, sPhone3 + "/" + sPhoneType3, sEmail1, sEmail2, sVolunteer };
        jtabModel.insertRow(jtabParents.getRowCount(), objTemp);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        firstNameLabel = new javax.swing.JLabel();
        lastNameLabel = new javax.swing.JLabel();
        gradeLabel = new javax.swing.JLabel();
        ratingLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        duesPaidLabel = new javax.swing.JLabel();
        testScore1Label = new javax.swing.JLabel();
        testScore2Label = new javax.swing.JLabel();
        firstNameField = new javax.swing.JTextField();
        lastNameField = new javax.swing.JTextField();
        ratingField = new javax.swing.JTextField();
        testScore1Field = new javax.swing.JTextField();
        testScore2Field = new javax.swing.JTextField();
        jcbGrade = new javax.swing.JComboBox();
        jbClear = new javax.swing.JButton();
        jbAdd = new javax.swing.JButton();
        jbMod = new javax.swing.JButton();
        jbDel = new javax.swing.JButton();
        jbExit = new javax.swing.JButton();
        jcbDuesPaid = new javax.swing.JComboBox();
        jcbStatus = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtfParFname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtfParPhone2 = new javax.swing.JTextField();
        jtfParPhone3 = new javax.swing.JTextField();
        jtfParPhone1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtfParLname = new javax.swing.JTextField();
        jbSelExisting = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jcbPhoneType3 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jcbPhoneType2 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jcbPhoneType1 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jcbVolunteer = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jtfParEmail2 = new javax.swing.JTextField();
        jtfParEmail1 = new javax.swing.JTextField();
        jbParAdd = new javax.swing.JButton();
        jbParRep = new javax.swing.JButton();
        jbParClear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtabMembers = new javax.swing.JTable();
        jbLink = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jcbGender = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jtfTeacher = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtabParents = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jcbPurchShirt = new javax.swing.JComboBox();
        jbSearch = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jmiMeetings = new javax.swing.JMenuItem();
        jmiCalcRatings = new javax.swing.JMenuItem();
        jmiInv = new javax.swing.JMenuItem();
        jmiGameResults = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(clubmaster.ClubMasterApp.class).getContext().getResourceMap(ClubMasterView.class);
        mainPanel.setName("mainPanel");
        mainPanel.setPreferredSize(new java.awt.Dimension(500, 700));
        firstNameLabel.setText(resourceMap.getString("firstNameLabel.text"));
        firstNameLabel.setName("firstNameLabel");
        lastNameLabel.setText(resourceMap.getString("lastNameLabel.text"));
        lastNameLabel.setName("lastNameLabel");
        gradeLabel.setText(resourceMap.getString("gradeLabel.text"));
        gradeLabel.setName("gradeLabel");
        ratingLabel.setText(resourceMap.getString("ratingLabel.text"));
        ratingLabel.setName("ratingLabel");
        statusLabel.setText(resourceMap.getString("statusLabel.text"));
        statusLabel.setName("statusLabel");
        duesPaidLabel.setText(resourceMap.getString("duesPaidLabel.text"));
        duesPaidLabel.setName("duesPaidLabel");
        testScore1Label.setText(resourceMap.getString("testScore1Label.text"));
        testScore1Label.setName("testScore1Label");
        testScore2Label.setText(resourceMap.getString("testScore2Label.text"));
        testScore2Label.setName("testScore2Label");
        firstNameField.setName("firstNameField");
        lastNameField.setName("lastNameField");
        lastNameField.setVerifyInputWhenFocusTarget(false);
        ratingField.setEditable(false);
        ratingField.setName("ratingField");
        testScore1Field.setName("testScore1Field");
        testScore2Field.setName("testScore2Field");
        jcbGrade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "K", "1", "2", "3", "4", "5", "6" }));
        jcbGrade.setName("jcbGrade");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(clubmaster.ClubMasterApp.class).getContext().getActionMap(ClubMasterView.class, this);
        jbClear.setAction(actionMap.get("initForm"));
        jbClear.setText(resourceMap.getString("jbClear.text"));
        jbClear.setName("jbClear");
        jbAdd.setAction(actionMap.get("saveRecord"));
        jbAdd.setText(resourceMap.getString("jbAdd.text"));
        jbAdd.setName("jbAdd");
        jbMod.setAction(actionMap.get("saveRecord"));
        jbMod.setText(resourceMap.getString("jbMod.text"));
        jbMod.setName("jbMod");
        jbDel.setAction(actionMap.get("deleteRecord"));
        jbDel.setText(resourceMap.getString("jbDel.text"));
        jbDel.setName("jbDel");
        jbExit.setAction(actionMap.get("exitApp"));
        jbExit.setText(resourceMap.getString("jbExit.text"));
        jbExit.setName("jbExit");
        jcbDuesPaid.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Y", "N" }));
        jcbDuesPaid.setName("jcbDuesPaid");
        jcbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "A", "I", "N" }));
        jcbStatus.setName("jcbStatus");
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel2.setFont(resourceMap.getFont("jLabel2.font"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        jtfParFname.setName("jtfParFname");
        jLabel5.setText(resourceMap.getString("jLabel5.text"));
        jLabel5.setName("jLabel5");
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        jtfParPhone2.setName("jtfParPhone2");
        jtfParPhone3.setName("jtfParPhone3");
        jtfParPhone1.setName("jtfParPhone1");
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setName("jLabel6");
        jtfParLname.setName("jtfParLname");
        jbSelExisting.setAction(actionMap.get("findExistingParent"));
        jbSelExisting.setText(resourceMap.getString("jbSelExisting.text"));
        jbSelExisting.setName("jbSelExisting");
        jLabel8.setText(resourceMap.getString("jLabel8.text"));
        jLabel8.setName("jLabel8");
        jcbPhoneType3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Cell", "Home", "Work" }));
        jcbPhoneType3.setName("jcbPhoneType3");
        jLabel9.setText(resourceMap.getString("jLabel9.text"));
        jLabel9.setName("jLabel9");
        jcbPhoneType2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Cell", "Home", "Work" }));
        jcbPhoneType2.setName("jcbPhoneType2");
        jLabel10.setText(resourceMap.getString("jLabel10.text"));
        jLabel10.setName("jLabel10");
        jcbPhoneType1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Cell", "Home", "Work" }));
        jcbPhoneType1.setName("jcbPhoneType1");
        jLabel11.setText(resourceMap.getString("jLabel11.text"));
        jLabel11.setName("jLabel11");
        jcbVolunteer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Y", "N" }));
        jcbVolunteer.setName("jcbVolunteer");
        jLabel12.setText(resourceMap.getString("jLabel12.text"));
        jLabel12.setName("jLabel12");
        jLabel13.setText(resourceMap.getString("jLabel13.text"));
        jLabel13.setName("jLabel13");
        jtfParEmail2.setName("jtfParEmail2");
        jtfParEmail1.setName("jtfParEmail1");
        jbParAdd.setAction(actionMap.get("saveParRecord"));
        jbParAdd.setText(resourceMap.getString("jbParAdd.text"));
        jbParAdd.setName("jbParAdd");
        jbParRep.setText(resourceMap.getString("jbParRep.text"));
        jbParRep.setName("jbParRep");
        jbParClear.setAction(actionMap.get("clearParentData"));
        jbParClear.setText(resourceMap.getString("jbParClear.text"));
        jbParClear.setName("jbParClear");
        jScrollPane1.setName("jScrollPane1");
        jtabMembers.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null, null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null, null, null, null, null } }, new String[] { "Member ID", "First Name", "Last Name", "Gender", "Grade", "Teacher", "Rating", "Status", "Dues Paid", "T-Shirt", "Test Score 1", "Test Score 2" }) {

            Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Short.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };

            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jtabMembers.setColumnSelectionAllowed(true);
        jtabMembers.setName("jtabMembers");
        jScrollPane1.setViewportView(jtabMembers);
        jtabMembers.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jtabMembers.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title10"));
        jtabMembers.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title0"));
        jtabMembers.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title1"));
        jtabMembers.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title21"));
        jtabMembers.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title2"));
        jtabMembers.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title22"));
        jtabMembers.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title3"));
        jtabMembers.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title4"));
        jtabMembers.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title5"));
        jtabMembers.getColumnModel().getColumn(9).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title11"));
        jtabMembers.getColumnModel().getColumn(10).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title12"));
        jtabMembers.getColumnModel().getColumn(11).setHeaderValue(resourceMap.getString("jtabMembers.columnModel.title13"));
        jbLink.setAction(actionMap.get("linkExistingParentToMember"));
        jbLink.setText(resourceMap.getString("jbLink.text"));
        jbLink.setName("jbLink");
        jLabel14.setText(resourceMap.getString("jLabel14.text"));
        jLabel14.setName("jLabel14");
        jcbGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "M", "F" }));
        jcbGender.setName("jcbGender");
        jLabel15.setText(resourceMap.getString("jLabel15.text"));
        jLabel15.setName("jLabel15");
        jtfTeacher.setText(resourceMap.getString("jtfTeacher.text"));
        jtfTeacher.setName("jtfTeacher");
        jLabel16.setIcon(resourceMap.getIcon("jLabel16.icon"));
        jLabel16.setText(resourceMap.getString("jLabel16.text"));
        jLabel16.setName("jLabel16");
        jLabel17.setFont(resourceMap.getFont("jLabel17.font"));
        jLabel17.setText(resourceMap.getString("jLabel17.text"));
        jLabel17.setName("jLabel17");
        jScrollPane2.setName("jScrollPane2");
        jtabParents.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null } }, new String[] { "ID", "Name", "Main Phone", "Phone/Type", "Phone/Type", "Main Email", "Other Email", "Volunteer" }) {

            Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jtabParents.setName("jtabParents");
        jScrollPane2.setViewportView(jtabParents);
        jtabParents.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jtabParents.columnModel.title0"));
        jtabParents.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jtabParents.columnModel.title1"));
        jtabParents.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jtabParents.columnModel.title2"));
        jtabParents.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jtabParents.columnModel.title3"));
        jtabParents.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jtabParents.columnModel.title4"));
        jtabParents.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("jtabParents.columnModel.title5"));
        jtabParents.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("jtabParents.columnModel.title6"));
        jtabParents.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("jtabParents.columnModel.title7"));
        jLabel18.setText(resourceMap.getString("jLabel18.text"));
        jLabel18.setName("jLabel18");
        jcbPurchShirt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Shirt", "Youth-S", "Youth-M", "Youth-L", "Youth-XL", "Youth-XXL", "Adult-S", "Adult-M", "Adult-L", "Adult-XL", "Adult-XXL" }));
        jcbPurchShirt.setName("jcbPurchShirt");
        jbSearch.setAction(actionMap.get("searchForMembers"));
        jbSearch.setText(resourceMap.getString("jbSearch.text"));
        jbSearch.setName("jbSearch");
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGap(119, 119, 119).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(firstNameLabel).addComponent(ratingLabel).addComponent(jLabel14)).addGap(24, 24, 24).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ratingField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jcbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jcbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(gradeLabel).addComponent(lastNameLabel).addComponent(jLabel15)).addComponent(duesPaidLabel)).addGap(18, 18, 18).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jcbDuesPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jcbGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jtfTeacher, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)))).addGroup(mainPanelLayout.createSequentialGroup().addComponent(testScore2Label).addGap(12, 12, 12).addComponent(testScore2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel18).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jcbPurchShirt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))).addGap(138, 138, 138)).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addComponent(jbClear).addGap(18, 18, 18).addComponent(jbAdd)).addGroup(mainPanelLayout.createSequentialGroup().addComponent(testScore1Label).addGap(18, 18, 18).addComponent(testScore1Field, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)).addGroup(mainPanelLayout.createSequentialGroup().addComponent(statusLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbMod).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jbDel).addGap(8, 8, 8).addComponent(jbSearch).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jbExit).addGap(112, 112, 112)))).addGroup(mainPanelLayout.createSequentialGroup().addGap(19, 19, 19).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addComponent(jLabel4).addComponent(jLabel5).addComponent(jLabel7).addComponent(jLabel13).addComponent(jLabel12)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jtfParFname, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jtfParPhone3, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jtfParPhone2, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jtfParPhone1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))).addGap(32, 32, 32).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel10).addComponent(jLabel9).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jcbPhoneType3, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jcbPhoneType2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jcbPhoneType1, javax.swing.GroupLayout.Alignment.LEADING, 0, 99, Short.MAX_VALUE)).addComponent(jtfParLname, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jtfParEmail2, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jtfParEmail1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jcbVolunteer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(mainPanelLayout.createSequentialGroup().addGap(137, 137, 137).addComponent(jLabel2)).addGroup(mainPanelLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addComponent(jbParClear).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbParAdd).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbParRep).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbSelExisting).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jbLink)).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGap(146, 146, 146)).addGroup(mainPanelLayout.createSequentialGroup().addGap(343, 343, 343).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel16)))).addContainerGap()));
        mainPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { testScore1Field, testScore2Field });
        mainPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { jLabel1, jLabel2 });
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGap(124, 124, 124).addComponent(jLabel2).addGap(33, 33, 33).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jtfParFname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6).addComponent(jtfParLname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jtfParPhone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel10)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(jtfParPhone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel9)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(jtfParPhone3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel8))).addGroup(mainPanelLayout.createSequentialGroup().addComponent(jcbPhoneType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbPhoneType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbPhoneType3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel13).addComponent(jtfParEmail1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel12).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jtfParEmail2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel11).addComponent(jcbVolunteer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15, 15, 15).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(firstNameLabel).addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lastNameLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(ratingField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ratingLabel).addComponent(gradeLabel).addComponent(jcbGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jcbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel15).addComponent(jtfTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel14)).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGap(7, 7, 7).addComponent(statusLabel)).addGroup(mainPanelLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(mainPanelLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(duesPaidLabel).addComponent(jcbDuesPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(testScore1Label).addComponent(testScore1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(testScore2Label).addComponent(jLabel18).addComponent(jcbPurchShirt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(testScore2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(11, 11, 11).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jbClear).addComponent(jbAdd).addComponent(jbMod).addComponent(jbDel).addComponent(jbExit).addComponent(jbSearch)))).addGap(10, 10, 10).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jbParClear).addComponent(jbParAdd).addComponent(jbParRep).addComponent(jbSelExisting).addComponent(jbLink)).addGap(18, 18, 18).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)).addContainerGap(57, Short.MAX_VALUE)));
        mainPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { jLabel1, jLabel2 });
        menuBar.setName("menuBar");
        fileMenu.setText(resourceMap.getString("fileMenu.text"));
        fileMenu.setName("fileMenu");
        jSeparator1.setName("jSeparator1");
        fileMenu.add(jSeparator1);
        jSeparator2.setName("jSeparator2");
        fileMenu.add(jSeparator2);
        exitMenuItem.setAction(actionMap.get("quit"));
        exitMenuItem.setName("exitMenuItem");
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        jMenu1.setText(resourceMap.getString("jMenu1.text"));
        jMenu1.setName("jMenu1");
        jmiMeetings.setAction(actionMap.get("showMeetings"));
        jmiMeetings.setText(resourceMap.getString("jmiMeetings.text"));
        jmiMeetings.setName("jmiMeetings");
        jMenu1.add(jmiMeetings);
        jmiCalcRatings.setAction(actionMap.get("calculateRatings"));
        jmiCalcRatings.setText(resourceMap.getString("jmiCalcRatings.text"));
        jmiCalcRatings.setName("jmiCalcRatings");
        jMenu1.add(jmiCalcRatings);
        jmiInv.setAction(actionMap.get("showInv"));
        jmiInv.setText(resourceMap.getString("jmiInv.text"));
        jmiInv.setName("jmiInv");
        jMenu1.add(jmiInv);
        jmiGameResults.setAction(actionMap.get("showGameResults"));
        jmiGameResults.setText(resourceMap.getString("jmiGameResults.text"));
        jmiGameResults.setName("jmiGameResults");
        jMenu1.add(jmiGameResults);
        jMenuItem3.setAction(actionMap.get("showExpenses"));
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text"));
        jMenuItem3.setName("jMenuItem3");
        jMenu1.add(jMenuItem3);
        jMenuItem7.setAction(actionMap.get("resetData"));
        jMenuItem7.setText(resourceMap.getString("jMenuItem7.text"));
        jMenuItem7.setName("jMenuItem7");
        jMenu1.add(jMenuItem7);
        jMenuItem8.setAction(actionMap.get("archiveData"));
        jMenuItem8.setText(resourceMap.getString("jMenuItem8.text"));
        jMenuItem8.setName("jMenuItem8");
        jMenu1.add(jMenuItem8);
        menuBar.add(jMenu1);
        jMenu3.setText(resourceMap.getString("jMenu3.text"));
        jMenu3.setName("jMenu3");
        jMenuItem2.setAction(actionMap.get("printIncompleteMembers"));
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text"));
        jMenuItem2.setName("jMenuItem2");
        jMenu3.add(jMenuItem2);
        jMenuItem4.setAction(actionMap.get("printUnpaidExpenses"));
        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text"));
        jMenuItem4.setName("jMenuItem4");
        jMenu3.add(jMenuItem4);
        jMenuItem5.setAction(actionMap.get("printInventory"));
        jMenuItem5.setText(resourceMap.getString("jMenuItem5.text"));
        jMenuItem5.setName("jMenuItem5");
        jMenu3.add(jMenuItem5);
        jMenuItem6.setAction(actionMap.get("printDemographics"));
        jMenuItem6.setText(resourceMap.getString("jMenuItem6.text"));
        jMenuItem6.setName("jMenuItem6");
        jMenu3.add(jMenuItem6);
        menuBar.add(jMenu3);
        jMenu2.setName("jMenu2");
        jMenuItem1.setAction(actionMap.get("printIncompleteMembers"));
        jMenuItem1.setName("jMenuItem1");
        jMenu2.add(jMenuItem1);
        menuBar.add(jMenu2);
        helpMenu.setText(resourceMap.getString("helpMenu.text"));
        helpMenu.setName("helpMenu");
        aboutMenuItem.setAction(actionMap.get("showAboutBox"));
        aboutMenuItem.setName("aboutMenuItem");
        helpMenu.add(aboutMenuItem);
        jSeparator5.setName("jSeparator5");
        helpMenu.add(jSeparator5);
        menuBar.add(helpMenu);
        setComponent(mainPanel);
        setMenuBar(menuBar);
    }

    /**
     *
     */
    @Action
    public void exitApp() {
        System.out.println("DEBUG->ClubMasterView - Exiting...");
        dbActions.closeDb();
        System.exit(0);
    }

    /**
     *
     */
    @Action
    public void saveRecord() {
        if (isChildDataValid()) {
            String sFirstName = firstNameField.getText().toString();
            String sLastName = lastNameField.getText().toString();
            String sStatus = jcbStatus.getSelectedItem().toString();
            String sDuesPaid = jcbDuesPaid.getSelectedItem().toString();
            String sTest1 = testScore1Field.getText().toString();
            String sTest2 = testScore2Field.getText().toString();
            String sGrade = jcbGrade.getSelectedItem().toString();
            sGender = jcbGender.getSelectedItem().toString();
            sTeacher = jtfTeacher.getText().toString();
            sPurchShirt = jcbPurchShirt.getSelectedItem().toString();
            double iTest1 = 0;
            double iTest2 = 0;
            try {
                if (sTest1.equals("")) {
                    sTest1 = "0";
                }
                iTest1 = Double.parseDouble(sTest1);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Exception converting number while saving [" + e.getMessage() + "]");
                System.out.println("DEBUG->ClubMasterView saveRecord Test1 EXC [" + e.getMessage() + "]");
                iTest1 = 0;
            }
            try {
                if (sTest2.equals("")) {
                    sTest2 = "0";
                }
                iTest2 = Double.parseDouble(sTest2);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Exception converting number while saving [" + e.getMessage() + "]");
                System.out.println("DEBUG->ClubMasterView saveRecord Test2 EXC [" + e.getMessage() + "]");
                iTest2 = 0;
            }
            if (jbAdd.isFocusOwner()) {
                if ((iChildId = dbActions.insertMember(sFirstName, sLastName, sGrade, sStatus, sDuesPaid, iTest1, iTest2, sGender, sTeacher, sPurchShirt)) < 0) {
                    JOptionPane.showMessageDialog(null, "Save record failed...Please try again");
                    System.out.println("DEBUG->saveRecord, Save Failed");
                } else {
                    System.out.println("DEBUG->ClubMasterView saveRecord iChildId [" + iChildId + "]");
                    this.enableParentFields();
                    addToTable();
                }
            } else {
                System.out.println("DEBUG->saveRecord FName [" + sFName + "], LName [" + sLName + "]");
                dbActions.updateMember(iChildId, sCFname, sCLname, sGrade, sStatus, sDuesPaid, iTest1, iTest2, sGender, sTeacher, sPurchShirt);
                int iRow = jtabMembers.getSelectedRow();
                jtabMembers.setValueAt(iChildId, iRow, 0);
                jtabMembers.setValueAt(sCFname, iRow, 1);
                jtabMembers.setValueAt(sCLname, iRow, 2);
                jtabMembers.setValueAt(sGender, iRow, 3);
                jtabMembers.setValueAt(sGrade, iRow, 4);
                jtabMembers.setValueAt(sTeacher, iRow, 5);
                jtabMembers.setValueAt(sStatus, iRow, 7);
                jtabMembers.setValueAt(sDuesPaid, iRow, 8);
                jtabMembers.setValueAt(sPurchShirt, iRow, 9);
                jtabMembers.setValueAt(iTest1, iRow, 10);
                jtabMembers.setValueAt(iTest2, iRow, 11);
            }
        }
    }

    /**
     *
     */
    @Action
    public void clearParentData() {
        this.clearParentFields();
    }

    /**
     *
     */
    @Action
    public void saveParRecord() {
        if (isParentDataValid()) {
            if (this.jbParAdd.isFocusOwner()) {
                iParentId = dbActions.insertParent(iChildId, sFName, sLName, sPhone1, sPhone2, sPhone3, sPhoneType1, sPhoneType2, sPhoneType3, sEmail1, sEmail2, sVolunteer);
                System.out.println("DEBUG->Parents saveParent - Calling dbAction save method");
                addToParentsTable();
            } else {
                int iRow = jtabMembers.getSelectedRow();
                jtabMembers.setValueAt(sLName + "," + sFName, iRow, 1);
                jtabMembers.setValueAt(sPhone1 + "/" + sPhoneType1, iRow, 2);
                jtabMembers.setValueAt(sPhone2 + "/" + sPhoneType2, iRow, 3);
                jtabMembers.setValueAt(sPhone3 + "/" + sPhoneType3, iRow, 4);
                jtabMembers.setValueAt(sEmail1, iRow, 5);
                jtabMembers.setValueAt(sEmail2, iRow, 6);
                jtabMembers.setValueAt(sVolunteer, iRow, 7);
                dbActions.updateParent(iParentId, iChildId, sFName, sLName, sPhone1, sPhone2, sPhone3, sPhone1, sPhone2, sPhone3, sEmail1, sEmail2, sVolunteer);
            }
        }
    }

    /**
     * 
     */
    @Action
    public void showMeetings() {
        Meetings meetings = new Meetings(dbActions);
        meetings.setVisible(true);
    }

    /**
     *
     */
    @Action
    public void showInv() {
        Inventory invObj = new Inventory(dbActions);
        invObj.setVisible(true);
    }

    /**
     *
     */
    @Action
    public void calculateRatings() {
        dbActions.computeRating();
    }

    /**
     *
     */
    @Action
    public void findExistingParent() {
        String sPName1 = this.jtfParFname.getText().toString();
        String sPName2 = this.jtfParLname.getText().toString();
        String sPhone = this.jtfParPhone1.getText().toString();
        Hashtable htReturnData = new Hashtable();
        if ((sPName2.length() == 0) && (sPhone.length() == 0)) {
            JOptionPane.showMessageDialog(null, "Find Existing Parent: Last Name or Phone 1 is required");
            System.out.println("DEBUG->ClubMasterView findExistingParent - Last Name or Phone 1 is required");
            return;
        }
        htReturnData = dbActions.getParentData(sPName1, sPName2, sPhone);
        String sReturnValue = htReturnData.get("ReturnValue").toString();
        String sMessage = htReturnData.get("ReturnMessage").toString();
        if (sReturnValue.equals("0")) {
            System.out.println("DEBUG->ClubMasterView findExistingParent - Invalid number of records found");
            iParentId = Integer.parseInt(htReturnData.get("ID").toString());
            this.jtfParFname.setText(htReturnData.get("FirstName").toString());
            this.jtfParLname.setText(htReturnData.get("LastName").toString());
            this.jtfParPhone1.setText(htReturnData.get("Phone1").toString());
            this.jtfParPhone2.setText(htReturnData.get("Phone2").toString());
            this.jtfParPhone3.setText(htReturnData.get("Phone3").toString());
            this.jtfParEmail1.setText(htReturnData.get("Email1").toString());
            this.jtfParEmail2.setText(htReturnData.get("Email2").toString());
            this.jcbPhoneType1.setSelectedItem(htReturnData.get("PhoneType1").toString());
            this.jcbPhoneType2.setSelectedItem(htReturnData.get("PhoneType2").toString());
            this.jcbPhoneType3.setSelectedItem(htReturnData.get("PhoneType3").toString());
            this.jcbVolunteer.setSelectedItem(htReturnData.get("Volunteer").toString());
            jbLink.setEnabled(true);
            this.jbParAdd.setEnabled(false);
            this.jbParRep.setEnabled(false);
            this.jbParClear.setEnabled(true);
        } else {
            System.out.println("DEBUG->ClubMasterView findExistingParent Failure [" + sMessage + "]");
        }
    }

    /**
     *
     */
    @Action
    public void linkExistingParentToMember() {
        if (iParentId > 0 && iChildId > 0) {
            dbActions.updateParentMember(iParentId, iChildId);
        } else {
            JOptionPane.showMessageDialog(null, "linkExistingParentToMember - Invalid ID Parent [" + iParentId + "], Member [" + iChildId + "]");
            System.out.println("DEBUG->ClubMasterView linkExistingParentToMember - Invalid ID Parent [" + iParentId + "], Member [" + iChildId + "]");
        }
    }

    /**
     *
     */
    @Action
    public void showGameResults() {
        GameResults gameResults = new GameResults(dbActions);
        gameResults.setVisible(true);
    }

    /**
     *
     */
    @Action
    public void deleteRecord() {
        if (iChildId > 0) {
            if (dbActions.deleteMemberRecord(iChildId)) {
                DefaultTableModel jtabModel = (DefaultTableModel) jtabMembers.getModel();
                jtabModel.removeRow(jtabMembers.getSelectedRow());
                clearForm();
            }
        }
    }

    /** printIncompleteMembers
     *   This will print all members that do not have an active status
     */
    @Action
    public void printIncompleteMembers() {
        Object[][] objData = dbActions.getIncompleteMemberData();
        String sFilename = dbActions.getRptDir() + "/Members_MissingData" + ".html";
        FileWriter fwOut;
        BufferedWriter bwOut;
        String sHeader = "<CENTER><B>Kilmer Chess Club</B><BR>Incomplete Members Report</CENTER><BR>";
        if (objData != null && objData.length > 0) {
            try {
                fwOut = new FileWriter(sFilename);
                bwOut = new BufferedWriter(fwOut);
                bwOut.write(sHeader);
                bwOut.write("<table border='1'><tr bgcolor='#FF0000'><td>Name</td><td>Teacher</td><td>Dues Paid</td><td>Parent</td></tr>");
                for (int i = 0; i < objData.length; i++) {
                    bwOut.write("<tr bgcolor='#FFFFFF'><td>" + objData[i][1] + "</td><td>" + objData[i][2] + "</td><td>" + objData[i][3] + "</td><td>" + objData[i][4] + "</td></tr>");
                }
                bwOut.write("</table>");
                bwOut.flush();
                bwOut.close();
                fwOut.close();
                String theUrl = "file://" + sFilename;
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + theUrl);
            } catch (Exception e) {
                System.out.println("DEBUG->printIncompleteMembers EXC [" + e.getMessage() + "]");
                e.printStackTrace();
            }
        }
    }

    /** showExpenses
     *   This will open the expenses form
     */
    @Action
    public void showExpenses() {
        Expenses expenses = new Expenses(dbActions);
        expenses.setVisible(true);
    }

    /** printUnpaidExpenses
     *   This will print all expenses that do not have a status of "Paid"
     */
    @Action
    public void printUnpaidExpenses() {
        Object[][] objData = dbActions.getExpenses("Unpaid");
        String sFilename = dbActions.getRptDir() + "/Expenses_Unpaid" + ".html";
        FileWriter fwOut;
        BufferedWriter bwOut;
        String sHeader = "<CENTER><B>Kilmer Chess Club</B><BR>Unpaid Expenses Report</CENTER><BR>";
        if (objData != null && objData.length > 0) {
            try {
                fwOut = new FileWriter(sFilename);
                bwOut = new BufferedWriter(fwOut);
                bwOut.write(sHeader);
                bwOut.write("<table border='1'><tr bgcolor='#FF0000'><td>Expense Type</td><td>Title</td><td>Amount</td><td>Status</td></tr>");
                for (int i = 0; i < objData.length; i++) {
                    bwOut.write("<tr bgcolor='#FFFFFF'><td>" + objData[i][0] + "</td><td>" + objData[i][1] + "</td><td>" + objData[i][2] + "</td><td>" + objData[i][3] + "</td></tr>");
                }
                bwOut.write("</table>");
                bwOut.flush();
                bwOut.close();
                fwOut.close();
                String theUrl = "file://" + sFilename;
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + theUrl);
            } catch (Exception e) {
                System.out.println("DEBUG->printUnpaidExpenses EXC [" + e.getMessage() + "]");
                e.printStackTrace();
            }
        }
    }

    /** printInventory
     *   This will print all of the current inventory
     */
    @Action
    public void printInventory() {
        Object[][] objData = dbActions.loadInvData();
        String sFilename = dbActions.getRptDir() + "/Inventory" + ".html";
        FileWriter fwOut;
        BufferedWriter bwOut;
        String sHeader = "<CENTER><B>Kilmer Chess Club</B><BR>Inventory Report</CENTER><BR>";
        if (objData != null && objData.length > 0) {
            try {
                fwOut = new FileWriter(sFilename);
                bwOut = new BufferedWriter(fwOut);
                bwOut.write(sHeader);
                bwOut.write("<table border='1'><tr bgcolor='#FF0000'><td>Name</td><td>Description</td><td>Purchased From</td><td>Quantity</td></tr>");
                for (int i = 0; i < objData.length; i++) {
                    bwOut.write("<tr bgcolor='#FFFFFF'><td>" + objData[i][1] + "</td><td>" + objData[i][2] + "</td><td>" + objData[i][3] + "</td><td>" + objData[i][4] + "</td></tr>");
                }
                bwOut.write("</table>");
                bwOut.flush();
                bwOut.close();
                fwOut.close();
                String theUrl = "file://" + sFilename;
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + theUrl);
            } catch (Exception e) {
                System.out.println("DEBUG->printInventory EXC [" + e.getMessage() + "]");
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    @Action
    public void searchForMembers() {
        String sQueryPred = "";
        String sQueryVerb = "WHERE ";
        if (lastNameField.getText().length() > 0) {
            sQueryPred = sQueryVerb + " last_name = '" + lastNameField.getText() + "'";
            sQueryVerb = " AND ";
        }
        if (!jcbGrade.getSelectedItem().toString().equals(S_LIST_DEFAULT)) {
            sQueryPred = sQueryPred + sQueryVerb + " grade = '" + jcbGrade.getSelectedItem().toString() + "'";
        }
        if (sQueryPred != null) {
            Object[][] objRsData = dbActions.initLoadMembers(sQueryPred);
            if (objRsData != null) {
                try {
                    System.out.println("DEBUG->ClubMasterView initLoadData Length(1) [" + objRsData.length + "]");
                    jtabMembers.setModel(new javax.swing.table.DefaultTableModel(objRsData, new String[] { "ID", "First Name", "Last Name", "Gender", "Grade", "Teacher", "Rating", "Status", "Dues Paid", "Test Score 1", "Test Score 2" }));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ClubMasterView initLoadData Exc [" + e.getMessage() + "]");
                    System.out.println("DEBUG->ClubMasterView initLoadData Exc [" + e.getMessage() + "]");
                }
            }
        }
    }

    /**
     *
     */
    @Action
    public void resetData() {
        dbActions.resetData();
        System.out.println("DEBUG->resetData, Clearing Member and Parent tables");
        DefaultTableModel dm = (DefaultTableModel) jtabMembers.getModel();
        dm.getDataVector().removeAllElements();
        DefaultTableModel dm1 = (DefaultTableModel) jtabParents.getModel();
        dm1.getDataVector().removeAllElements();
    }

    /**
     *
     */
    @Action
    public void archiveData() {
        System.out.println("DEBUG->archiveData, Archiving data to history tables");
        dbActions.archiveData();
    }

    private javax.swing.JLabel duesPaidLabel;

    private javax.swing.JTextField firstNameField;

    private javax.swing.JLabel firstNameLabel;

    private javax.swing.JLabel gradeLabel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenu jMenu3;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JMenuItem jMenuItem4;

    private javax.swing.JMenuItem jMenuItem5;

    private javax.swing.JMenuItem jMenuItem6;

    private javax.swing.JMenuItem jMenuItem7;

    private javax.swing.JMenuItem jMenuItem8;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSeparator jSeparator5;

    private javax.swing.JButton jbAdd;

    private javax.swing.JButton jbClear;

    private javax.swing.JButton jbDel;

    private javax.swing.JButton jbExit;

    private javax.swing.JButton jbLink;

    private javax.swing.JButton jbMod;

    private javax.swing.JButton jbParAdd;

    private javax.swing.JButton jbParClear;

    private javax.swing.JButton jbParRep;

    private javax.swing.JButton jbSearch;

    private javax.swing.JButton jbSelExisting;

    private javax.swing.JComboBox jcbDuesPaid;

    private javax.swing.JComboBox jcbGender;

    private javax.swing.JComboBox jcbGrade;

    private javax.swing.JComboBox jcbPhoneType1;

    private javax.swing.JComboBox jcbPhoneType2;

    private javax.swing.JComboBox jcbPhoneType3;

    private javax.swing.JComboBox jcbPurchShirt;

    private javax.swing.JComboBox jcbStatus;

    private javax.swing.JComboBox jcbVolunteer;

    private javax.swing.JMenuItem jmiCalcRatings;

    private javax.swing.JMenuItem jmiGameResults;

    private javax.swing.JMenuItem jmiInv;

    private javax.swing.JMenuItem jmiMeetings;

    private javax.swing.JTable jtabMembers;

    private javax.swing.JTable jtabParents;

    private javax.swing.JTextField jtfParEmail1;

    private javax.swing.JTextField jtfParEmail2;

    private javax.swing.JTextField jtfParFname;

    private javax.swing.JTextField jtfParLname;

    private javax.swing.JTextField jtfParPhone1;

    private javax.swing.JTextField jtfParPhone2;

    private javax.swing.JTextField jtfParPhone3;

    private javax.swing.JTextField jtfTeacher;

    private javax.swing.JTextField lastNameField;

    private javax.swing.JLabel lastNameLabel;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JMenuBar menuBar;

    private javax.persistence.Query query;

    private javax.swing.JTextField ratingField;

    private javax.swing.JLabel ratingLabel;

    private javax.swing.JLabel statusLabel;

    private javax.swing.JTextField testScore1Field;

    private javax.swing.JLabel testScore1Label;

    private javax.swing.JTextField testScore2Field;

    private javax.swing.JLabel testScore2Label;

    private JDialog aboutBox;

    private ClubProps clubProps;

    private DbActions dbActions;
}
