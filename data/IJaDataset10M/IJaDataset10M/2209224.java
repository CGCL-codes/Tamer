package MedicalSoftware;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import java.awt.CardLayout;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollBar;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class AdminUI extends JFrame {

    private JPanel contentPane;

    private JList ApptList;

    private JLabel lblApptDate;

    private JLabel lblApptTime;

    private JLabel lblApptReason;

    private JLabel lblApptDD;

    private JTextField textFieldPrescription;

    private JTextField textFieldLabWork;

    private JTextField textFieldFollowUp;

    private JTextField textFieldOtherIns;

    private JTextField textFieldNameSearch;

    private JTextField textFieldNewName;

    private SystemAdmin admin;

    private Patient patient;

    private JTextField textFieldUsername;

    private JTextField textFieldPassword;

    private JButton btnCreate;

    private JTextField textFieldNewEmail;

    private JTextField textFieldNewAddress;

    private JTextField textFieldNewState;

    private JTextField textFieldNewCountry;

    private JTextField textFieldNewZip;

    private JTextField textFieldNewSSN;

    private JTextField textFieldNewBirthday;

    private int newuserchoice = 3;

    private String[] apptArray;

    private JTextField txtTime;

    private JTextField txtDate;

    private JTextField txtName;

    private JTextField txtNameInvoice;

    private JTextField txtDoctorInvoice;

    private JTextField txtDueDate;

    private JTextField txtTotal;

    private JTextField txtPaid;

    private JTextField textFieldApptDate;

    private JTextField textFieldApptTime;

    private JTextField textFieldApptReason;

    private JTextField textFieldApptDD;

    /**
	 * Create the frame.
	 */
    public AdminUI() {
        initialize();
    }

    public AdminUI(String username, AVL<String, Info> information, AVL<String, Info> informationName) {
        admin = new SystemAdmin(username, information, informationName);
        initialize();
        this.setVisible(true);
    }

    public AdminUI(SystemAdmin admin) {
        this.admin = admin;
        initialize();
        this.setVisible(true);
    }

    public AdminUI(Login log) {
        this();
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 640, 480);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JLabel lblUsername = new JLabel(" username");
        menuBar.add(lblUsername);
        JButton btnLogout = new JButton("logout");
        menuBar.add(btnLogout);
        btnLogout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Server ser = new Server();
                ser.saveAppt(admin.getTree());
                ser.save(admin.getTree());
                ser.saveTreatment(admin.getTree());
                ser.saveOrders(admin.getTree());
                ser.saveInvoice(admin.getTree());
                System.exit(0);
            }
        });
        JButton btnRefresh = new JButton("Refresh");
        menuBar.add(btnRefresh);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane);
        JPanel patientsTab = new JPanel();
        tabbedPane.addTab("Patients", null, patientsTab, null);
        patientsTab.setLayout(new CardLayout(0, 0));
        JTabbedPane patientsTabPane = new JTabbedPane(JTabbedPane.LEFT);
        patientsTabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        patientsTab.add("Patients", patientsTabPane);
        JPanel PersonalInfoPanel = new JPanel();
        patientsTabPane.addTab("Personal Info", null, PersonalInfoPanel, null);
        GridBagLayout gbl_PersonalInfoPanel = new GridBagLayout();
        gbl_PersonalInfoPanel.columnWidths = new int[] { 0, 0, 0, 0 };
        gbl_PersonalInfoPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_PersonalInfoPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
        gbl_PersonalInfoPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        PersonalInfoPanel.setLayout(gbl_PersonalInfoPanel);
        JLabel lblName = new JLabel("Name:");
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.anchor = GridBagConstraints.EAST;
        gbc_lblName.insets = new Insets(0, 0, 5, 5);
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 0;
        PersonalInfoPanel.add(lblName, gbc_lblName);
        JLabel lblNameInfo = new JLabel(admin.getInfo().getName());
        GridBagConstraints gbc_lblNameInfo = new GridBagConstraints();
        gbc_lblNameInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblNameInfo.gridx = 1;
        gbc_lblNameInfo.gridy = 0;
        PersonalInfoPanel.add(lblNameInfo, gbc_lblNameInfo);
        JButton btnNameEdit = new JButton("edit");
        btnNameEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter name: ", "", 1);
                if (str != null) {
                    admin.getInfo().setName(str);
                }
            }
        });
        GridBagConstraints gbc_btnNameEdit = new GridBagConstraints();
        gbc_btnNameEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnNameEdit.gridx = 2;
        gbc_btnNameEdit.gridy = 0;
        PersonalInfoPanel.add(btnNameEdit, gbc_btnNameEdit);
        JLabel lblUsername_2 = new JLabel("Username:");
        lblUsername_2.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_lblUsername_2 = new GridBagConstraints();
        gbc_lblUsername_2.anchor = GridBagConstraints.EAST;
        gbc_lblUsername_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblUsername_2.gridx = 0;
        gbc_lblUsername_2.gridy = 1;
        PersonalInfoPanel.add(lblUsername_2, gbc_lblUsername_2);
        JLabel lblUsernameInfo = new JLabel(admin.getInfo().getUserName());
        GridBagConstraints gbc_lblUsernameInfo = new GridBagConstraints();
        gbc_lblUsernameInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblUsernameInfo.gridx = 1;
        gbc_lblUsernameInfo.gridy = 1;
        PersonalInfoPanel.add(lblUsernameInfo, gbc_lblUsernameInfo);
        JButton btnUsernameEdit = new JButton("edit");
        btnUsernameEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter Username: ", "", 1);
                if (str != null) {
                    admin.getInfo().setUserName(str);
                }
            }
        });
        GridBagConstraints gbc_btnUsernameEdit = new GridBagConstraints();
        gbc_btnUsernameEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnUsernameEdit.gridx = 2;
        gbc_btnUsernameEdit.gridy = 1;
        PersonalInfoPanel.add(btnUsernameEdit, gbc_btnUsernameEdit);
        JLabel lblAddress = new JLabel("Address:");
        GridBagConstraints gbc_lblAddress = new GridBagConstraints();
        gbc_lblAddress.anchor = GridBagConstraints.EAST;
        gbc_lblAddress.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddress.gridx = 0;
        gbc_lblAddress.gridy = 2;
        PersonalInfoPanel.add(lblAddress, gbc_lblAddress);
        JLabel lblAddressInfo = new JLabel(admin.getInfo().getAddress());
        GridBagConstraints gbc_lblAddressInfo = new GridBagConstraints();
        gbc_lblAddressInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddressInfo.gridx = 1;
        gbc_lblAddressInfo.gridy = 2;
        PersonalInfoPanel.add(lblAddressInfo, gbc_lblAddressInfo);
        JButton btnAddressEdit = new JButton("edit");
        btnAddressEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter Address: ", "", 1);
                if (str != null) {
                    admin.getInfo().setAddress(str);
                }
            }
        });
        GridBagConstraints gbc_btnAddressEdit = new GridBagConstraints();
        gbc_btnAddressEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnAddressEdit.gridx = 2;
        gbc_btnAddressEdit.gridy = 2;
        PersonalInfoPanel.add(btnAddressEdit, gbc_btnAddressEdit);
        JLabel lblState = new JLabel("State:");
        GridBagConstraints gbc_lblState = new GridBagConstraints();
        gbc_lblState.anchor = GridBagConstraints.EAST;
        gbc_lblState.insets = new Insets(0, 0, 5, 5);
        gbc_lblState.gridx = 0;
        gbc_lblState.gridy = 3;
        PersonalInfoPanel.add(lblState, gbc_lblState);
        JLabel lblStateInfo = new JLabel(admin.getInfo().getState());
        GridBagConstraints gbc_lblStateInfo = new GridBagConstraints();
        gbc_lblStateInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblStateInfo.gridx = 1;
        gbc_lblStateInfo.gridy = 3;
        PersonalInfoPanel.add(lblStateInfo, gbc_lblStateInfo);
        JButton btnStateEdit = new JButton("edit");
        btnStateEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter State: ", "", 1);
                if (str != null) {
                    admin.getInfo().setState(str);
                }
            }
        });
        GridBagConstraints gbc_btnStateEdit = new GridBagConstraints();
        gbc_btnStateEdit.anchor = GridBagConstraints.SOUTH;
        gbc_btnStateEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnStateEdit.gridx = 2;
        gbc_btnStateEdit.gridy = 3;
        PersonalInfoPanel.add(btnStateEdit, gbc_btnStateEdit);
        JLabel lblZip_1 = new JLabel("Zip:");
        GridBagConstraints gbc_lblZip_1 = new GridBagConstraints();
        gbc_lblZip_1.anchor = GridBagConstraints.EAST;
        gbc_lblZip_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblZip_1.gridx = 0;
        gbc_lblZip_1.gridy = 4;
        PersonalInfoPanel.add(lblZip_1, gbc_lblZip_1);
        JLabel lblZipInfo = new JLabel(String.valueOf(admin.getInfo().getZip()));
        GridBagConstraints gbc_lblZipInfo = new GridBagConstraints();
        gbc_lblZipInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblZipInfo.gridx = 1;
        gbc_lblZipInfo.gridy = 4;
        PersonalInfoPanel.add(lblZipInfo, gbc_lblZipInfo);
        JButton btnZipEdit = new JButton("edit");
        btnZipEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter zip: ", "", 1);
                if (str != null) {
                    admin.getInfo().setZip(Integer.parseInt(str));
                }
            }
        });
        GridBagConstraints gbc_btnZipEdit = new GridBagConstraints();
        gbc_btnZipEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnZipEdit.gridx = 2;
        gbc_btnZipEdit.gridy = 4;
        PersonalInfoPanel.add(btnZipEdit, gbc_btnZipEdit);
        JLabel lblCountry_1 = new JLabel("Country:");
        GridBagConstraints gbc_lblCountry_1 = new GridBagConstraints();
        gbc_lblCountry_1.anchor = GridBagConstraints.EAST;
        gbc_lblCountry_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblCountry_1.gridx = 0;
        gbc_lblCountry_1.gridy = 5;
        PersonalInfoPanel.add(lblCountry_1, gbc_lblCountry_1);
        JLabel lblCountryInfo = new JLabel(admin.getInfo().getCountry());
        GridBagConstraints gbc_lblCountryInfo = new GridBagConstraints();
        gbc_lblCountryInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblCountryInfo.gridx = 1;
        gbc_lblCountryInfo.gridy = 5;
        PersonalInfoPanel.add(lblCountryInfo, gbc_lblCountryInfo);
        JButton btnCountryEdit = new JButton("edit");
        btnCountryEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter Country: ", "", 1);
                if (str != null) {
                    admin.getInfo().setCountry(str);
                }
            }
        });
        GridBagConstraints gbc_btnCountryEdit = new GridBagConstraints();
        gbc_btnCountryEdit.anchor = GridBagConstraints.SOUTH;
        gbc_btnCountryEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnCountryEdit.gridx = 2;
        gbc_btnCountryEdit.gridy = 5;
        PersonalInfoPanel.add(btnCountryEdit, gbc_btnCountryEdit);
        JLabel lblPhone = new JLabel("Phone:");
        GridBagConstraints gbc_lblPhone = new GridBagConstraints();
        gbc_lblPhone.anchor = GridBagConstraints.EAST;
        gbc_lblPhone.insets = new Insets(0, 0, 5, 5);
        gbc_lblPhone.gridx = 0;
        gbc_lblPhone.gridy = 6;
        PersonalInfoPanel.add(lblPhone, gbc_lblPhone);
        JLabel lblPhoneInfo = new JLabel("no info");
        GridBagConstraints gbc_lblPhoneInfo = new GridBagConstraints();
        gbc_lblPhoneInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblPhoneInfo.gridx = 1;
        gbc_lblPhoneInfo.gridy = 6;
        PersonalInfoPanel.add(lblPhoneInfo, gbc_lblPhoneInfo);
        JButton btnPhoneEdit = new JButton("edit");
        btnPhoneEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter Phone Number: ", "", 1);
                if (str != null) {
                }
            }
        });
        GridBagConstraints gbc_btnPhoneEdit = new GridBagConstraints();
        gbc_btnPhoneEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnPhoneEdit.gridx = 2;
        gbc_btnPhoneEdit.gridy = 6;
        PersonalInfoPanel.add(btnPhoneEdit, gbc_btnPhoneEdit);
        JLabel lblEmail = new JLabel("Email:");
        GridBagConstraints gbc_lblEmail = new GridBagConstraints();
        gbc_lblEmail.anchor = GridBagConstraints.EAST;
        gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
        gbc_lblEmail.gridx = 0;
        gbc_lblEmail.gridy = 7;
        PersonalInfoPanel.add(lblEmail, gbc_lblEmail);
        JLabel lblEmailInfo = new JLabel(admin.getInfo().getEmail());
        GridBagConstraints gbc_lblEmailInfo = new GridBagConstraints();
        gbc_lblEmailInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblEmailInfo.gridx = 1;
        gbc_lblEmailInfo.gridy = 7;
        PersonalInfoPanel.add(lblEmailInfo, gbc_lblEmailInfo);
        JButton btnEmailEdit = new JButton("edit");
        btnEmailEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter email: ", "", 1);
                if (str != null) {
                    admin.getInfo().setEmail(str);
                }
            }
        });
        GridBagConstraints gbc_btnEmailEdit = new GridBagConstraints();
        gbc_btnEmailEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnEmailEdit.gridx = 2;
        gbc_btnEmailEdit.gridy = 7;
        PersonalInfoPanel.add(btnEmailEdit, gbc_btnEmailEdit);
        JLabel lblGender = new JLabel("Gender:");
        GridBagConstraints gbc_lblGender = new GridBagConstraints();
        gbc_lblGender.anchor = GridBagConstraints.EAST;
        gbc_lblGender.insets = new Insets(0, 0, 5, 5);
        gbc_lblGender.gridx = 0;
        gbc_lblGender.gridy = 8;
        PersonalInfoPanel.add(lblGender, gbc_lblGender);
        JLabel lblGenderInfo = new JLabel("questionable");
        GridBagConstraints gbc_lblGenderInfo = new GridBagConstraints();
        gbc_lblGenderInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblGenderInfo.gridx = 1;
        gbc_lblGenderInfo.gridy = 8;
        PersonalInfoPanel.add(lblGenderInfo, gbc_lblGenderInfo);
        JButton btnGenderEdit = new JButton("edit");
        btnGenderEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter Gender: ", "", 1);
                if (str != null) {
                }
            }
        });
        GridBagConstraints gbc_btnGenderEdit = new GridBagConstraints();
        gbc_btnGenderEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnGenderEdit.gridx = 2;
        gbc_btnGenderEdit.gridy = 8;
        PersonalInfoPanel.add(btnGenderEdit, gbc_btnGenderEdit);
        JLabel lblAge = new JLabel("Birthday:");
        GridBagConstraints gbc_lblAge = new GridBagConstraints();
        gbc_lblAge.anchor = GridBagConstraints.EAST;
        gbc_lblAge.insets = new Insets(0, 0, 5, 5);
        gbc_lblAge.gridx = 0;
        gbc_lblAge.gridy = 9;
        PersonalInfoPanel.add(lblAge, gbc_lblAge);
        JLabel lblAgeInfo = new JLabel(String.valueOf(admin.getInfo().getBirthday()));
        GridBagConstraints gbc_lblAgeInfo = new GridBagConstraints();
        gbc_lblAgeInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblAgeInfo.gridx = 1;
        gbc_lblAgeInfo.gridy = 9;
        PersonalInfoPanel.add(lblAgeInfo, gbc_lblAgeInfo);
        JButton btnAgeEdit = new JButton("edit");
        btnAgeEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter Birthday: ", "", 1);
                if (str != null) {
                    admin.getInfo().setBirthday(Integer.parseInt(str));
                }
            }
        });
        GridBagConstraints gbc_btnAgeEdit = new GridBagConstraints();
        gbc_btnAgeEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnAgeEdit.gridx = 2;
        gbc_btnAgeEdit.gridy = 9;
        PersonalInfoPanel.add(btnAgeEdit, gbc_btnAgeEdit);
        JLabel lblSocialSecurityNumber = new JLabel("Social Security Number");
        GridBagConstraints gbc_lblSocialSecurityNumber = new GridBagConstraints();
        gbc_lblSocialSecurityNumber.anchor = GridBagConstraints.EAST;
        gbc_lblSocialSecurityNumber.insets = new Insets(0, 0, 0, 5);
        gbc_lblSocialSecurityNumber.gridx = 0;
        gbc_lblSocialSecurityNumber.gridy = 10;
        PersonalInfoPanel.add(lblSocialSecurityNumber, gbc_lblSocialSecurityNumber);
        JLabel lblSSNInfo = new JLabel("XXX-XX-XXXX");
        GridBagConstraints gbc_lblSSNInfo = new GridBagConstraints();
        gbc_lblSSNInfo.insets = new Insets(0, 0, 0, 5);
        gbc_lblSSNInfo.gridx = 1;
        gbc_lblSSNInfo.gridy = 10;
        PersonalInfoPanel.add(lblSSNInfo, gbc_lblSSNInfo);
        JButton btnSSNEdit = new JButton("edit");
        btnSSNEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String str = JOptionPane.showInputDialog(null, "Enter name: ", "", 1);
                if (str != null) {
                    admin.getInfo().setSSN(Integer.parseInt(str));
                }
            }
        });
        GridBagConstraints gbc_btnSSNEdit = new GridBagConstraints();
        gbc_btnSSNEdit.gridx = 2;
        gbc_btnSSNEdit.gridy = 10;
        PersonalInfoPanel.add(btnSSNEdit, gbc_btnSSNEdit);
        JPanel MedicalInfoPanel = new JPanel();
        patientsTabPane.addTab("Medical Info", null, MedicalInfoPanel, null);
        GridBagLayout gbl_MedicalInfoPanel = new GridBagLayout();
        gbl_MedicalInfoPanel.columnWidths = new int[] { 0, 0, 0, 0 };
        gbl_MedicalInfoPanel.rowHeights = new int[] { 0, 0, 0, 0 };
        gbl_MedicalInfoPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
        gbl_MedicalInfoPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
        MedicalInfoPanel.setLayout(gbl_MedicalInfoPanel);
        JLabel lblPharmacy = new JLabel("Pharmacy:");
        GridBagConstraints gbc_lblPharmacy = new GridBagConstraints();
        gbc_lblPharmacy.anchor = GridBagConstraints.EAST;
        gbc_lblPharmacy.insets = new Insets(0, 0, 5, 5);
        gbc_lblPharmacy.gridx = 0;
        gbc_lblPharmacy.gridy = 0;
        MedicalInfoPanel.add(lblPharmacy, gbc_lblPharmacy);
        JLabel lblPharmacyInfo = new JLabel("no info");
        GridBagConstraints gbc_lblPharmacyInfo = new GridBagConstraints();
        gbc_lblPharmacyInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblPharmacyInfo.gridx = 1;
        gbc_lblPharmacyInfo.gridy = 0;
        MedicalInfoPanel.add(lblPharmacyInfo, gbc_lblPharmacyInfo);
        JButton btnPharmacyEdit = new JButton("edit");
        GridBagConstraints gbc_btnPharmacyEdit = new GridBagConstraints();
        gbc_btnPharmacyEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnPharmacyEdit.gridx = 2;
        gbc_btnPharmacyEdit.gridy = 0;
        MedicalInfoPanel.add(btnPharmacyEdit, gbc_btnPharmacyEdit);
        JLabel lblInsuranceCarrier = new JLabel("Insurance Carrier:");
        GridBagConstraints gbc_lblInsuranceCarrier = new GridBagConstraints();
        gbc_lblInsuranceCarrier.anchor = GridBagConstraints.EAST;
        gbc_lblInsuranceCarrier.insets = new Insets(0, 0, 5, 5);
        gbc_lblInsuranceCarrier.gridx = 0;
        gbc_lblInsuranceCarrier.gridy = 1;
        MedicalInfoPanel.add(lblInsuranceCarrier, gbc_lblInsuranceCarrier);
        JLabel lblInsuranceInfo = new JLabel("no info");
        GridBagConstraints gbc_lblInsuranceInfo = new GridBagConstraints();
        gbc_lblInsuranceInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblInsuranceInfo.gridx = 1;
        gbc_lblInsuranceInfo.gridy = 1;
        MedicalInfoPanel.add(lblInsuranceInfo, gbc_lblInsuranceInfo);
        JButton btnInsuranceEdit = new JButton("edit");
        GridBagConstraints gbc_btnInsuranceEdit = new GridBagConstraints();
        gbc_btnInsuranceEdit.insets = new Insets(0, 0, 5, 0);
        gbc_btnInsuranceEdit.gridx = 2;
        gbc_btnInsuranceEdit.gridy = 1;
        MedicalInfoPanel.add(btnInsuranceEdit, gbc_btnInsuranceEdit);
        JLabel lblAllergies = new JLabel("Allergies:");
        GridBagConstraints gbc_lblAllergies = new GridBagConstraints();
        gbc_lblAllergies.anchor = GridBagConstraints.EAST;
        gbc_lblAllergies.insets = new Insets(0, 0, 0, 5);
        gbc_lblAllergies.gridx = 0;
        gbc_lblAllergies.gridy = 2;
        MedicalInfoPanel.add(lblAllergies, gbc_lblAllergies);
        JLabel lblAllergiesInfo = new JLabel("no info");
        GridBagConstraints gbc_lblAllergiesInfo = new GridBagConstraints();
        gbc_lblAllergiesInfo.insets = new Insets(0, 0, 0, 5);
        gbc_lblAllergiesInfo.gridx = 1;
        gbc_lblAllergiesInfo.gridy = 2;
        MedicalInfoPanel.add(lblAllergiesInfo, gbc_lblAllergiesInfo);
        JButton btnAllergiesEdit = new JButton("edit");
        GridBagConstraints gbc_btnAllergiesEdit = new GridBagConstraints();
        gbc_btnAllergiesEdit.gridx = 2;
        gbc_btnAllergiesEdit.gridy = 2;
        MedicalInfoPanel.add(btnAllergiesEdit, gbc_btnAllergiesEdit);
        JPanel TreatmentRecordsPanel = new JPanel();
        patientsTabPane.addTab("Treatment Records", null, TreatmentRecordsPanel, null);
        GridBagLayout gbl_TreatmentRecordsPanel = new GridBagLayout();
        gbl_TreatmentRecordsPanel.columnWidths = new int[] { 0, 0, 0, 0 };
        gbl_TreatmentRecordsPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_TreatmentRecordsPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
        gbl_TreatmentRecordsPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        TreatmentRecordsPanel.setLayout(gbl_TreatmentRecordsPanel);
        JButton btnNewTreatmentRecord = new JButton("New");
        GridBagConstraints gbc_btnNewTreatmentRecord = new GridBagConstraints();
        gbc_btnNewTreatmentRecord.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewTreatmentRecord.gridx = 0;
        gbc_btnNewTreatmentRecord.gridy = 0;
        TreatmentRecordsPanel.add(btnNewTreatmentRecord, gbc_btnNewTreatmentRecord);
        btnNewTreatmentRecord.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
            }
        });
        JButton btnGetTreatmentRecord = new JButton("Get Treatment Record");
        btnGetTreatmentRecord.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                String name = JOptionPane.showInputDialog("Enter the name of the patient you are searching for:");
                if (admin.getTree().find(name) != null) {
                    if (admin.getTree().find(name).getRecord() != null) {
                        ArrayList<TRecords> record = admin.getTree().find(name).getRecord().getRecords();
                    }
                }
            }
        });
        GridBagConstraints gbc_btnGetTreatmentRecord = new GridBagConstraints();
        gbc_btnGetTreatmentRecord.insets = new Insets(0, 0, 5, 5);
        gbc_btnGetTreatmentRecord.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnGetTreatmentRecord.gridx = 1;
        gbc_btnGetTreatmentRecord.gridy = 0;
        TreatmentRecordsPanel.add(btnGetTreatmentRecord, gbc_btnGetTreatmentRecord);
        JScrollBar scrollBarTreatmentRecords = new JScrollBar();
        GridBagConstraints gbc_scrollBarTreatmentRecords = new GridBagConstraints();
        gbc_scrollBarTreatmentRecords.insets = new Insets(0, 0, 5, 0);
        gbc_scrollBarTreatmentRecords.gridheight = 7;
        gbc_scrollBarTreatmentRecords.gridx = 2;
        gbc_scrollBarTreatmentRecords.gridy = 0;
        TreatmentRecordsPanel.add(scrollBarTreatmentRecords, gbc_scrollBarTreatmentRecords);
        JLabel lblTreatingDoctor = new JLabel("Treating Doctor:");
        GridBagConstraints gbc_lblTreatingDoctor = new GridBagConstraints();
        gbc_lblTreatingDoctor.anchor = GridBagConstraints.EAST;
        gbc_lblTreatingDoctor.insets = new Insets(0, 0, 5, 5);
        gbc_lblTreatingDoctor.gridx = 0;
        gbc_lblTreatingDoctor.gridy = 1;
        TreatmentRecordsPanel.add(lblTreatingDoctor, gbc_lblTreatingDoctor);
        JLabel lblTreatingDoctorInfo = new JLabel("select treament record");
        GridBagConstraints gbc_lblTreatingDoctorInfo = new GridBagConstraints();
        gbc_lblTreatingDoctorInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblTreatingDoctorInfo.gridx = 1;
        gbc_lblTreatingDoctorInfo.gridy = 1;
        TreatmentRecordsPanel.add(lblTreatingDoctorInfo, gbc_lblTreatingDoctorInfo);
        JLabel lblDatetime = new JLabel("Date/Time:");
        GridBagConstraints gbc_lblDatetime = new GridBagConstraints();
        gbc_lblDatetime.anchor = GridBagConstraints.EAST;
        gbc_lblDatetime.insets = new Insets(0, 0, 5, 5);
        gbc_lblDatetime.gridx = 0;
        gbc_lblDatetime.gridy = 2;
        TreatmentRecordsPanel.add(lblDatetime, gbc_lblDatetime);
        JLabel lblDateTimeInfo = new JLabel("select treatment record");
        GridBagConstraints gbc_lblDateTimeInfo = new GridBagConstraints();
        gbc_lblDateTimeInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblDateTimeInfo.gridx = 1;
        gbc_lblDateTimeInfo.gridy = 2;
        TreatmentRecordsPanel.add(lblDateTimeInfo, gbc_lblDateTimeInfo);
        JLabel lblAttendingNurse = new JLabel("Attending Nurse:");
        GridBagConstraints gbc_lblAttendingNurse = new GridBagConstraints();
        gbc_lblAttendingNurse.anchor = GridBagConstraints.EAST;
        gbc_lblAttendingNurse.insets = new Insets(0, 0, 5, 5);
        gbc_lblAttendingNurse.gridx = 0;
        gbc_lblAttendingNurse.gridy = 3;
        TreatmentRecordsPanel.add(lblAttendingNurse, gbc_lblAttendingNurse);
        JLabel lblAttendingNurseInfo = new JLabel("select treatment record");
        GridBagConstraints gbc_lblAttendingNurseInfo = new GridBagConstraints();
        gbc_lblAttendingNurseInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblAttendingNurseInfo.gridx = 1;
        gbc_lblAttendingNurseInfo.gridy = 3;
        TreatmentRecordsPanel.add(lblAttendingNurseInfo, gbc_lblAttendingNurseInfo);
        JLabel lblDoctorsOrders = new JLabel("Doctor's Orders:");
        GridBagConstraints gbc_lblDoctorsOrders = new GridBagConstraints();
        gbc_lblDoctorsOrders.anchor = GridBagConstraints.EAST;
        gbc_lblDoctorsOrders.insets = new Insets(0, 0, 5, 5);
        gbc_lblDoctorsOrders.gridx = 0;
        gbc_lblDoctorsOrders.gridy = 4;
        TreatmentRecordsPanel.add(lblDoctorsOrders, gbc_lblDoctorsOrders);
        JButton btnViewDoctorsOrders = new JButton("view");
        GridBagConstraints gbc_btnViewDoctorsOrders = new GridBagConstraints();
        gbc_btnViewDoctorsOrders.insets = new Insets(0, 0, 5, 5);
        gbc_btnViewDoctorsOrders.gridx = 1;
        gbc_btnViewDoctorsOrders.gridy = 4;
        TreatmentRecordsPanel.add(btnViewDoctorsOrders, gbc_btnViewDoctorsOrders);
        JLabel lblChiefComplaint = new JLabel("Chief Complaint:");
        GridBagConstraints gbc_lblChiefComplaint = new GridBagConstraints();
        gbc_lblChiefComplaint.anchor = GridBagConstraints.EAST;
        gbc_lblChiefComplaint.insets = new Insets(0, 0, 5, 5);
        gbc_lblChiefComplaint.gridx = 0;
        gbc_lblChiefComplaint.gridy = 5;
        TreatmentRecordsPanel.add(lblChiefComplaint, gbc_lblChiefComplaint);
        JLabel lblChiefComplaintInfo = new JLabel("select treatment record");
        GridBagConstraints gbc_lblChiefComplaintInfo = new GridBagConstraints();
        gbc_lblChiefComplaintInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblChiefComplaintInfo.gridx = 1;
        gbc_lblChiefComplaintInfo.gridy = 5;
        TreatmentRecordsPanel.add(lblChiefComplaintInfo, gbc_lblChiefComplaintInfo);
        JLabel lblVitalSigns = new JLabel("Vital Signs:");
        GridBagConstraints gbc_lblVitalSigns = new GridBagConstraints();
        gbc_lblVitalSigns.anchor = GridBagConstraints.EAST;
        gbc_lblVitalSigns.insets = new Insets(0, 0, 5, 5);
        gbc_lblVitalSigns.gridx = 0;
        gbc_lblVitalSigns.gridy = 6;
        TreatmentRecordsPanel.add(lblVitalSigns, gbc_lblVitalSigns);
        JButton btnViewVitalSigns = new JButton("view");
        GridBagConstraints gbc_btnViewVitalSigns = new GridBagConstraints();
        gbc_btnViewVitalSigns.insets = new Insets(0, 0, 5, 5);
        gbc_btnViewVitalSigns.gridx = 1;
        gbc_btnViewVitalSigns.gridy = 6;
        TreatmentRecordsPanel.add(btnViewVitalSigns, gbc_btnViewVitalSigns);
        JLabel lblDiagnosis = new JLabel("Diagnosis:");
        GridBagConstraints gbc_lblDiagnosis = new GridBagConstraints();
        gbc_lblDiagnosis.anchor = GridBagConstraints.EAST;
        gbc_lblDiagnosis.insets = new Insets(0, 0, 0, 5);
        gbc_lblDiagnosis.gridx = 0;
        gbc_lblDiagnosis.gridy = 7;
        TreatmentRecordsPanel.add(lblDiagnosis, gbc_lblDiagnosis);
        JLabel lblDiagnosisInfo = new JLabel("select treatment record");
        GridBagConstraints gbc_lblDiagnosisInfo = new GridBagConstraints();
        gbc_lblDiagnosisInfo.insets = new Insets(0, 0, 0, 5);
        gbc_lblDiagnosisInfo.gridx = 1;
        gbc_lblDiagnosisInfo.gridy = 7;
        TreatmentRecordsPanel.add(lblDiagnosisInfo, gbc_lblDiagnosisInfo);
        JPanel InvoicesPanel = new JPanel();
        patientsTabPane.addTab("Invoices", null, InvoicesPanel, null);
        GridBagLayout gbl_InvoicesPanel = new GridBagLayout();
        gbl_InvoicesPanel.columnWidths = new int[] { 0, 0, 0, 0 };
        gbl_InvoicesPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        gbl_InvoicesPanel.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
        gbl_InvoicesPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        InvoicesPanel.setLayout(gbl_InvoicesPanel);
        JLabel lblName_1 = new JLabel("Name:");
        GridBagConstraints gbc_lblName_1 = new GridBagConstraints();
        gbc_lblName_1.anchor = GridBagConstraints.EAST;
        gbc_lblName_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblName_1.gridx = 1;
        gbc_lblName_1.gridy = 0;
        InvoicesPanel.add(lblName_1, gbc_lblName_1);
        txtNameInvoice = new JTextField();
        GridBagConstraints gbc_txtNameInvoice = new GridBagConstraints();
        gbc_txtNameInvoice.insets = new Insets(0, 0, 5, 0);
        gbc_txtNameInvoice.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtNameInvoice.gridx = 2;
        gbc_txtNameInvoice.gridy = 0;
        InvoicesPanel.add(txtNameInvoice, gbc_txtNameInvoice);
        txtNameInvoice.setColumns(10);
        JLabel lblDoctor = new JLabel("Doctor:");
        GridBagConstraints gbc_lblDoctor = new GridBagConstraints();
        gbc_lblDoctor.anchor = GridBagConstraints.EAST;
        gbc_lblDoctor.insets = new Insets(0, 0, 5, 5);
        gbc_lblDoctor.gridx = 1;
        gbc_lblDoctor.gridy = 1;
        InvoicesPanel.add(lblDoctor, gbc_lblDoctor);
        txtDoctorInvoice = new JTextField();
        GridBagConstraints gbc_txtDoctorInvoice = new GridBagConstraints();
        gbc_txtDoctorInvoice.insets = new Insets(0, 0, 5, 0);
        gbc_txtDoctorInvoice.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtDoctorInvoice.gridx = 2;
        gbc_txtDoctorInvoice.gridy = 1;
        InvoicesPanel.add(txtDoctorInvoice, gbc_txtDoctorInvoice);
        txtDoctorInvoice.setColumns(10);
        JLabel lblDueDate = new JLabel("Due Date:");
        GridBagConstraints gbc_lblDueDate = new GridBagConstraints();
        gbc_lblDueDate.anchor = GridBagConstraints.EAST;
        gbc_lblDueDate.insets = new Insets(0, 0, 5, 5);
        gbc_lblDueDate.gridx = 1;
        gbc_lblDueDate.gridy = 2;
        InvoicesPanel.add(lblDueDate, gbc_lblDueDate);
        txtDueDate = new JTextField();
        GridBagConstraints gbc_txtDueDate = new GridBagConstraints();
        gbc_txtDueDate.insets = new Insets(0, 0, 5, 0);
        gbc_txtDueDate.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtDueDate.gridx = 2;
        gbc_txtDueDate.gridy = 2;
        InvoicesPanel.add(txtDueDate, gbc_txtDueDate);
        txtDueDate.setColumns(10);
        JLabel lblTotal = new JLabel("Total:");
        GridBagConstraints gbc_lblTotal = new GridBagConstraints();
        gbc_lblTotal.anchor = GridBagConstraints.EAST;
        gbc_lblTotal.insets = new Insets(0, 0, 5, 5);
        gbc_lblTotal.gridx = 1;
        gbc_lblTotal.gridy = 3;
        InvoicesPanel.add(lblTotal, gbc_lblTotal);
        txtTotal = new JTextField();
        GridBagConstraints gbc_txtTotal = new GridBagConstraints();
        gbc_txtTotal.insets = new Insets(0, 0, 5, 0);
        gbc_txtTotal.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtTotal.gridx = 2;
        gbc_txtTotal.gridy = 3;
        InvoicesPanel.add(txtTotal, gbc_txtTotal);
        txtTotal.setColumns(10);
        JLabel lblPaid = new JLabel("Paid:");
        GridBagConstraints gbc_lblPaid = new GridBagConstraints();
        gbc_lblPaid.anchor = GridBagConstraints.EAST;
        gbc_lblPaid.insets = new Insets(0, 0, 5, 5);
        gbc_lblPaid.gridx = 1;
        gbc_lblPaid.gridy = 4;
        InvoicesPanel.add(lblPaid, gbc_lblPaid);
        txtPaid = new JTextField();
        GridBagConstraints gbc_txtPaid = new GridBagConstraints();
        gbc_txtPaid.insets = new Insets(0, 0, 5, 0);
        gbc_txtPaid.anchor = GridBagConstraints.NORTH;
        gbc_txtPaid.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtPaid.gridx = 2;
        gbc_txtPaid.gridy = 4;
        InvoicesPanel.add(txtPaid, gbc_txtPaid);
        txtPaid.setColumns(10);
        JButton btnCreateInvoice = new JButton("Create Invoice");
        btnCreateInvoice.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                String name = txtNameInvoice.getText();
                String doc = txtDoctorInvoice.getText();
                int due = Integer.parseInt(txtDueDate.getText());
                int total = Integer.parseInt(txtTotal.getText());
                String paid = txtPaid.getText();
            }
        });
        GridBagConstraints gbc_btnCreateInvoice = new GridBagConstraints();
        gbc_btnCreateInvoice.gridx = 2;
        gbc_btnCreateInvoice.gridy = 5;
        InvoicesPanel.add(btnCreateInvoice, gbc_btnCreateInvoice);
        JPanel ReportsPanel = new JPanel();
        patientsTabPane.addTab("Reports", null, ReportsPanel, null);
        GridBagLayout gbl_ReportsPanel = new GridBagLayout();
        gbl_ReportsPanel.columnWidths = new int[] { 0, 0 };
        gbl_ReportsPanel.rowHeights = new int[] { 0, 0, 0 };
        gbl_ReportsPanel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
        gbl_ReportsPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        ReportsPanel.setLayout(gbl_ReportsPanel);
        JButton btnGetHealthHistory = new JButton("Get Health History");
        GridBagConstraints gbc_btnGetHealthHistory = new GridBagConstraints();
        gbc_btnGetHealthHistory.insets = new Insets(0, 0, 5, 0);
        gbc_btnGetHealthHistory.gridx = 0;
        gbc_btnGetHealthHistory.gridy = 0;
        ReportsPanel.add(btnGetHealthHistory, gbc_btnGetHealthHistory);
        JButton btnPrintIncomeStatement = new JButton("Print Income Statement");
        GridBagConstraints gbc_btnPrintIncomeStatement = new GridBagConstraints();
        gbc_btnPrintIncomeStatement.gridx = 0;
        gbc_btnPrintIncomeStatement.gridy = 1;
        ReportsPanel.add(btnPrintIncomeStatement, gbc_btnPrintIncomeStatement);
        if (admin.getInfo().getRecord() != null) {
            ArrayList<TRecords> records = admin.getInfo().getRecord().getRecords();
            XYSeries series = new XYSeries("Average Weight");
            for (int i = 0; i < records.size(); i++) {
                series.add((double) records.get(i).getDate(), (double) records.get(i).getWeight());
                i++;
            }
            XYDataset xyDataset = new XYSeriesCollection(series);
            JFreeChart chart = ChartFactory.createXYAreaChart("Weight", "Date", "Weight", xyDataset, PlotOrientation.VERTICAL, true, true, false);
            ChartPanel GraphPanel = new ChartPanel(chart);
            ChartFrame frame = new ChartFrame("Weight Chart", chart);
            patientsTabPane.addTab("Graph", null, GraphPanel, null);
        }
        JLabel lblNotFunctionalYet_1 = new JLabel("");
        JPanel SearchPanel = new JPanel();
        patientsTabPane.addTab("Search/Delete", null, SearchPanel, null);
        GridBagLayout gbl_SearchPanel = new GridBagLayout();
        gbl_SearchPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        gbl_SearchPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_SearchPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        gbl_SearchPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        SearchPanel.setLayout(gbl_SearchPanel);
        JLabel label = new JLabel("Name:");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.anchor = GridBagConstraints.EAST;
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridx = 0;
        gbc_label.gridy = 2;
        SearchPanel.add(label, gbc_label);
        textFieldNameSearch = new JTextField();
        textFieldNameSearch.setColumns(10);
        GridBagConstraints gbc_textFieldNameSearch = new GridBagConstraints();
        gbc_textFieldNameSearch.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldNameSearch.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldNameSearch.gridx = 1;
        gbc_textFieldNameSearch.gridy = 2;
        SearchPanel.add(textFieldNameSearch, gbc_textFieldNameSearch);
        JButton button = new JButton("Search");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                patient = new Patient(admin.Search(textFieldNameSearch.getText()));
            }
        });
        GridBagConstraints gbc_button = new GridBagConstraints();
        gbc_button.insets = new Insets(0, 0, 5, 5);
        gbc_button.gridx = 1;
        gbc_button.gridy = 3;
        SearchPanel.add(button, gbc_button);
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                admin.deleteUser(textFieldNameSearch.getText());
            }
        });
        GridBagConstraints gbc_btnDelete = new GridBagConstraints();
        gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
        gbc_btnDelete.gridx = 1;
        gbc_btnDelete.gridy = 4;
        SearchPanel.add(btnDelete, gbc_btnDelete);
        JPanel NewUserPanel = new JPanel();
        patientsTabPane.addTab("New User", null, NewUserPanel, null);
        GridBagLayout gbl_NewUserPanel = new GridBagLayout();
        gbl_NewUserPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
        gbl_NewUserPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_NewUserPanel.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
        gbl_NewUserPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        NewUserPanel.setLayout(gbl_NewUserPanel);
        JLabel lblNewName = new JLabel("Name: ");
        GridBagConstraints gbc_lblNewName = new GridBagConstraints();
        gbc_lblNewName.gridwidth = 2;
        gbc_lblNewName.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewName.anchor = GridBagConstraints.EAST;
        gbc_lblNewName.gridx = 0;
        gbc_lblNewName.gridy = 1;
        NewUserPanel.add(lblNewName, gbc_lblNewName);
        textFieldNewName = new JTextField();
        GridBagConstraints gbc_textFieldNewName = new GridBagConstraints();
        gbc_textFieldNewName.gridwidth = 2;
        gbc_textFieldNewName.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldNewName.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldNewName.gridx = 2;
        gbc_textFieldNewName.gridy = 1;
        NewUserPanel.add(textFieldNewName, gbc_textFieldNewName);
        textFieldNewName.setColumns(10);
        JLabel lblUsername_1 = new JLabel("username: ");
        GridBagConstraints gbc_lblUsername_1 = new GridBagConstraints();
        gbc_lblUsername_1.anchor = GridBagConstraints.EAST;
        gbc_lblUsername_1.gridwidth = 2;
        gbc_lblUsername_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblUsername_1.gridx = 0;
        gbc_lblUsername_1.gridy = 2;
        NewUserPanel.add(lblUsername_1, gbc_lblUsername_1);
        textFieldUsername = new JTextField();
        GridBagConstraints gbc_textFieldUsername = new GridBagConstraints();
        gbc_textFieldUsername.gridwidth = 2;
        gbc_textFieldUsername.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldUsername.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldUsername.gridx = 2;
        gbc_textFieldUsername.gridy = 2;
        NewUserPanel.add(textFieldUsername, gbc_textFieldUsername);
        textFieldUsername.setColumns(10);
        JLabel lblPassword = new JLabel("password: ");
        GridBagConstraints gbc_lblPassword = new GridBagConstraints();
        gbc_lblPassword.anchor = GridBagConstraints.EAST;
        gbc_lblPassword.gridwidth = 2;
        gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
        gbc_lblPassword.gridx = 0;
        gbc_lblPassword.gridy = 3;
        NewUserPanel.add(lblPassword, gbc_lblPassword);
        textFieldPassword = new JTextField();
        GridBagConstraints gbc_textFieldPassword = new GridBagConstraints();
        gbc_textFieldPassword.gridwidth = 2;
        gbc_textFieldPassword.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldPassword.gridx = 2;
        gbc_textFieldPassword.gridy = 3;
        NewUserPanel.add(textFieldPassword, gbc_textFieldPassword);
        textFieldPassword.setColumns(10);
        JLabel lblNewEmail = new JLabel("Email: ");
        GridBagConstraints gbc_lblNewEmail = new GridBagConstraints();
        gbc_lblNewEmail.anchor = GridBagConstraints.EAST;
        gbc_lblNewEmail.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewEmail.gridx = 1;
        gbc_lblNewEmail.gridy = 4;
        NewUserPanel.add(lblNewEmail, gbc_lblNewEmail);
        textFieldNewEmail = new JTextField();
        GridBagConstraints gbc_textFieldNewEmail = new GridBagConstraints();
        gbc_textFieldNewEmail.gridwidth = 2;
        gbc_textFieldNewEmail.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldNewEmail.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldNewEmail.gridx = 2;
        gbc_textFieldNewEmail.gridy = 4;
        NewUserPanel.add(textFieldNewEmail, gbc_textFieldNewEmail);
        textFieldNewEmail.setColumns(10);
        JLabel lblNewAddress = new JLabel("Address: ");
        GridBagConstraints gbc_lblNewAddress = new GridBagConstraints();
        gbc_lblNewAddress.anchor = GridBagConstraints.EAST;
        gbc_lblNewAddress.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewAddress.gridx = 1;
        gbc_lblNewAddress.gridy = 5;
        NewUserPanel.add(lblNewAddress, gbc_lblNewAddress);
        textFieldNewAddress = new JTextField();
        GridBagConstraints gbc_textFieldNewAddress = new GridBagConstraints();
        gbc_textFieldNewAddress.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldNewAddress.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldNewAddress.gridx = 2;
        gbc_textFieldNewAddress.gridy = 5;
        NewUserPanel.add(textFieldNewAddress, gbc_textFieldNewAddress);
        textFieldNewAddress.setColumns(10);
        JLabel lblState_1 = new JLabel("State: ");
        GridBagConstraints gbc_lblState_1 = new GridBagConstraints();
        gbc_lblState_1.anchor = GridBagConstraints.EAST;
        gbc_lblState_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblState_1.gridx = 3;
        gbc_lblState_1.gridy = 5;
        NewUserPanel.add(lblState_1, gbc_lblState_1);
        textFieldNewState = new JTextField();
        GridBagConstraints gbc_textFieldNewState = new GridBagConstraints();
        gbc_textFieldNewState.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldNewState.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldNewState.gridx = 4;
        gbc_textFieldNewState.gridy = 5;
        NewUserPanel.add(textFieldNewState, gbc_textFieldNewState);
        textFieldNewState.setColumns(10);
        JLabel lblCountry = new JLabel("Country: ");
        GridBagConstraints gbc_lblCountry = new GridBagConstraints();
        gbc_lblCountry.anchor = GridBagConstraints.EAST;
        gbc_lblCountry.insets = new Insets(0, 0, 5, 5);
        gbc_lblCountry.gridx = 1;
        gbc_lblCountry.gridy = 6;
        NewUserPanel.add(lblCountry, gbc_lblCountry);
        textFieldNewCountry = new JTextField();
        GridBagConstraints gbc_textFieldNewCountry = new GridBagConstraints();
        gbc_textFieldNewCountry.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldNewCountry.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldNewCountry.gridx = 2;
        gbc_textFieldNewCountry.gridy = 6;
        NewUserPanel.add(textFieldNewCountry, gbc_textFieldNewCountry);
        textFieldNewCountry.setColumns(10);
        JLabel lblZip = new JLabel("Zip: ");
        GridBagConstraints gbc_lblZip = new GridBagConstraints();
        gbc_lblZip.anchor = GridBagConstraints.EAST;
        gbc_lblZip.insets = new Insets(0, 0, 5, 5);
        gbc_lblZip.gridx = 3;
        gbc_lblZip.gridy = 6;
        NewUserPanel.add(lblZip, gbc_lblZip);
        textFieldNewZip = new JTextField();
        GridBagConstraints gbc_textFieldNewZip = new GridBagConstraints();
        gbc_textFieldNewZip.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldNewZip.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldNewZip.gridx = 4;
        gbc_textFieldNewZip.gridy = 6;
        NewUserPanel.add(textFieldNewZip, gbc_textFieldNewZip);
        textFieldNewZip.setColumns(10);
        JLabel lblSsn = new JLabel("SSN: ");
        GridBagConstraints gbc_lblSsn = new GridBagConstraints();
        gbc_lblSsn.anchor = GridBagConstraints.EAST;
        gbc_lblSsn.insets = new Insets(0, 0, 5, 5);
        gbc_lblSsn.gridx = 1;
        gbc_lblSsn.gridy = 7;
        NewUserPanel.add(lblSsn, gbc_lblSsn);
        textFieldNewSSN = new JTextField();
        GridBagConstraints gbc_textFieldNewSSN = new GridBagConstraints();
        gbc_textFieldNewSSN.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldNewSSN.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldNewSSN.gridx = 2;
        gbc_textFieldNewSSN.gridy = 7;
        NewUserPanel.add(textFieldNewSSN, gbc_textFieldNewSSN);
        textFieldNewSSN.setColumns(10);
        JLabel lblBirthday = new JLabel("Birthday: ");
        GridBagConstraints gbc_lblBirthday = new GridBagConstraints();
        gbc_lblBirthday.anchor = GridBagConstraints.EAST;
        gbc_lblBirthday.insets = new Insets(0, 0, 5, 5);
        gbc_lblBirthday.gridx = 1;
        gbc_lblBirthday.gridy = 8;
        NewUserPanel.add(lblBirthday, gbc_lblBirthday);
        textFieldNewBirthday = new JTextField();
        GridBagConstraints gbc_textFieldNewBirthday = new GridBagConstraints();
        gbc_textFieldNewBirthday.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldNewBirthday.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldNewBirthday.gridx = 2;
        gbc_textFieldNewBirthday.gridy = 8;
        NewUserPanel.add(textFieldNewBirthday, gbc_textFieldNewBirthday);
        textFieldNewBirthday.setColumns(10);
        JRadioButton rdbtnPatient = new JRadioButton("Patient");
        rdbtnPatient.setSelected(true);
        rdbtnPatient.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                newuserchoice = 3;
            }
        });
        rdbtnPatient.setActionCommand("3");
        GridBagConstraints gbc_rdbtnPatient = new GridBagConstraints();
        gbc_rdbtnPatient.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnPatient.gridx = 1;
        gbc_rdbtnPatient.gridy = 9;
        NewUserPanel.add(rdbtnPatient, gbc_rdbtnPatient);
        JRadioButton rdbtnDoctor = new JRadioButton("Doctor");
        rdbtnDoctor.setActionCommand("1");
        rdbtnDoctor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                newuserchoice = 1;
            }
        });
        GridBagConstraints gbc_rdbtnDoctor = new GridBagConstraints();
        gbc_rdbtnDoctor.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnDoctor.gridx = 2;
        gbc_rdbtnDoctor.gridy = 9;
        NewUserPanel.add(rdbtnDoctor, gbc_rdbtnDoctor);
        JRadioButton rdbtnNurse = new JRadioButton("Nurse");
        rdbtnNurse.setActionCommand("2");
        rdbtnNurse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                newuserchoice = 2;
            }
        });
        GridBagConstraints gbc_rdbtnNurse = new GridBagConstraints();
        gbc_rdbtnNurse.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnNurse.gridx = 3;
        gbc_rdbtnNurse.gridy = 9;
        NewUserPanel.add(rdbtnNurse, gbc_rdbtnNurse);
        JRadioButton rdbtnAdmin = new JRadioButton("Admin");
        rdbtnAdmin.setActionCommand("0");
        rdbtnAdmin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                newuserchoice = 0;
            }
        });
        GridBagConstraints gbc_rdbtnAdmin = new GridBagConstraints();
        gbc_rdbtnAdmin.insets = new Insets(0, 0, 5, 0);
        gbc_rdbtnAdmin.gridx = 4;
        gbc_rdbtnAdmin.gridy = 9;
        NewUserPanel.add(rdbtnAdmin, gbc_rdbtnAdmin);
        ButtonGroup group = new ButtonGroup();
        group.add(rdbtnPatient);
        group.add(rdbtnDoctor);
        group.add(rdbtnNurse);
        group.add(rdbtnAdmin);
        btnCreate = new JButton("Create");
        btnCreate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                admin.createUser(textFieldNewName.getText(), textFieldPassword.getText(), textFieldUsername.getText(), textFieldNewEmail.getText(), textFieldNewAddress.getText(), textFieldNewState.getText(), textFieldNewCountry.getText(), Integer.parseInt(textFieldNewSSN.getText()), Integer.parseInt(textFieldNewZip.getText()), Integer.parseInt(textFieldNewBirthday.getText()), 0);
                JOptionPane.showMessageDialog(null, "Created new user successfully :)");
            }
        });
        GridBagConstraints gbc_btnCreate = new GridBagConstraints();
        gbc_btnCreate.insets = new Insets(0, 0, 0, 5);
        gbc_btnCreate.gridx = 2;
        gbc_btnCreate.gridy = 10;
        NewUserPanel.add(btnCreate, gbc_btnCreate);
        JPanel panel = new JPanel();
        tabbedPane.addTab("Appointments", null, panel, null);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0 };
        gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);
        JPanel pnlApptsTab = new JPanel();
        GridBagConstraints gbc_pnlApptsTab = new GridBagConstraints();
        gbc_pnlApptsTab.fill = GridBagConstraints.BOTH;
        gbc_pnlApptsTab.gridx = 0;
        gbc_pnlApptsTab.gridy = 0;
        panel.add(pnlApptsTab, gbc_pnlApptsTab);
        GridBagLayout gbl_pnlApptsTab = new GridBagLayout();
        gbl_pnlApptsTab.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        gbl_pnlApptsTab.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_pnlApptsTab.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
        gbl_pnlApptsTab.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        pnlApptsTab.setLayout(gbl_pnlApptsTab);
        JLabel label_1 = new JLabel("Date:");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.anchor = GridBagConstraints.EAST;
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 0;
        gbc_label_1.gridy = 0;
        pnlApptsTab.add(label_1, gbc_label_1);
        textFieldApptDate = new JTextField();
        textFieldApptDate.setColumns(10);
        GridBagConstraints gbc_textFieldApptDate = new GridBagConstraints();
        gbc_textFieldApptDate.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldApptDate.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldApptDate.gridx = 1;
        gbc_textFieldApptDate.gridy = 0;
        pnlApptsTab.add(textFieldApptDate, gbc_textFieldApptDate);
        JLabel label_2 = new JLabel("Time:");
        GridBagConstraints gbc_label_2 = new GridBagConstraints();
        gbc_label_2.anchor = GridBagConstraints.EAST;
        gbc_label_2.insets = new Insets(0, 0, 5, 5);
        gbc_label_2.gridx = 0;
        gbc_label_2.gridy = 1;
        pnlApptsTab.add(label_2, gbc_label_2);
        textFieldApptTime = new JTextField();
        textFieldApptTime.setColumns(10);
        GridBagConstraints gbc_textFieldApptTime = new GridBagConstraints();
        gbc_textFieldApptTime.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldApptTime.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldApptTime.gridx = 1;
        gbc_textFieldApptTime.gridy = 1;
        pnlApptsTab.add(textFieldApptTime, gbc_textFieldApptTime);
        JLabel label_3 = new JLabel("Reason:");
        GridBagConstraints gbc_label_3 = new GridBagConstraints();
        gbc_label_3.anchor = GridBagConstraints.EAST;
        gbc_label_3.insets = new Insets(0, 0, 5, 5);
        gbc_label_3.gridx = 0;
        gbc_label_3.gridy = 2;
        pnlApptsTab.add(label_3, gbc_label_3);
        textFieldApptReason = new JTextField();
        textFieldApptReason.setColumns(10);
        GridBagConstraints gbc_textFieldApptReason = new GridBagConstraints();
        gbc_textFieldApptReason.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldApptReason.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldApptReason.gridx = 1;
        gbc_textFieldApptReason.gridy = 2;
        pnlApptsTab.add(textFieldApptReason, gbc_textFieldApptReason);
        JLabel label_4 = new JLabel("Desired Doctor:");
        GridBagConstraints gbc_label_4 = new GridBagConstraints();
        gbc_label_4.anchor = GridBagConstraints.EAST;
        gbc_label_4.insets = new Insets(0, 0, 5, 5);
        gbc_label_4.gridx = 0;
        gbc_label_4.gridy = 3;
        pnlApptsTab.add(label_4, gbc_label_4);
        textFieldApptDD = new JTextField();
        textFieldApptDD.setColumns(10);
        GridBagConstraints gbc_textFieldApptDD = new GridBagConstraints();
        gbc_textFieldApptDD.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldApptDD.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldApptDD.gridx = 1;
        gbc_textFieldApptDD.gridy = 3;
        pnlApptsTab.add(textFieldApptDD, gbc_textFieldApptDD);
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                admin.createAppt(patient.getInfo().getName(), Integer.parseInt(textFieldApptDate.getText()), Integer.parseInt(textFieldApptTime.getText()), textFieldApptDD.getText(), textFieldApptReason.getText());
                JOptionPane.showMessageDialog(null, "Created :)");
            }
        });
        GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
        gbc_btnSubmit.insets = new Insets(0, 0, 5, 5);
        gbc_btnSubmit.gridx = 1;
        gbc_btnSubmit.gridy = 4;
        pnlApptsTab.add(btnSubmit, gbc_btnSubmit);
        JLabel lblDateDisplay = new JLabel("Date: ");
        GridBagConstraints gbc_lblDateDisplay = new GridBagConstraints();
        gbc_lblDateDisplay.anchor = GridBagConstraints.EAST;
        gbc_lblDateDisplay.insets = new Insets(0, 0, 5, 5);
        gbc_lblDateDisplay.gridx = 2;
        gbc_lblDateDisplay.gridy = 5;
        pnlApptsTab.add(lblDateDisplay, gbc_lblDateDisplay);
        lblApptDate = new JLabel("<void>");
        GridBagConstraints gbc_lblApptDate = new GridBagConstraints();
        gbc_lblApptDate.insets = new Insets(0, 0, 5, 0);
        gbc_lblApptDate.gridx = 3;
        gbc_lblApptDate.gridy = 5;
        pnlApptsTab.add(lblApptDate, gbc_lblApptDate);
        JLabel lblTimeDisplay = new JLabel("Time: ");
        GridBagConstraints gbc_lblTimeDisplay = new GridBagConstraints();
        gbc_lblTimeDisplay.anchor = GridBagConstraints.EAST;
        gbc_lblTimeDisplay.insets = new Insets(0, 0, 5, 5);
        gbc_lblTimeDisplay.gridx = 2;
        gbc_lblTimeDisplay.gridy = 6;
        pnlApptsTab.add(lblTimeDisplay, gbc_lblTimeDisplay);
        lblApptTime = new JLabel("<void>");
        GridBagConstraints gbc_lblApptTime = new GridBagConstraints();
        gbc_lblApptTime.insets = new Insets(0, 0, 5, 0);
        gbc_lblApptTime.gridx = 3;
        gbc_lblApptTime.gridy = 6;
        pnlApptsTab.add(lblApptTime, gbc_lblApptTime);
        JLabel lblReasonDisplay = new JLabel("Reason: ");
        GridBagConstraints gbc_lblReasonDisplay = new GridBagConstraints();
        gbc_lblReasonDisplay.anchor = GridBagConstraints.EAST;
        gbc_lblReasonDisplay.insets = new Insets(0, 0, 5, 5);
        gbc_lblReasonDisplay.gridx = 2;
        gbc_lblReasonDisplay.gridy = 7;
        pnlApptsTab.add(lblReasonDisplay, gbc_lblReasonDisplay);
        lblApptReason = new JLabel("no reason stated");
        GridBagConstraints gbc_lblApptReason = new GridBagConstraints();
        gbc_lblApptReason.anchor = GridBagConstraints.NORTH;
        gbc_lblApptReason.gridheight = 2;
        gbc_lblApptReason.insets = new Insets(0, 0, 5, 0);
        gbc_lblApptReason.gridx = 3;
        gbc_lblApptReason.gridy = 7;
        pnlApptsTab.add(lblApptReason, gbc_lblApptReason);
        JLabel lblDDDisplay = new JLabel("Desired Doctor: ");
        GridBagConstraints gbc_lblDDDisplay = new GridBagConstraints();
        gbc_lblDDDisplay.anchor = GridBagConstraints.EAST;
        gbc_lblDDDisplay.insets = new Insets(0, 0, 5, 5);
        gbc_lblDDDisplay.gridx = 2;
        gbc_lblDDDisplay.gridy = 9;
        pnlApptsTab.add(lblDDDisplay, gbc_lblDDDisplay);
        lblApptDD = new JLabel("None Stated");
        GridBagConstraints gbc_lblApptDD = new GridBagConstraints();
        gbc_lblApptDD.insets = new Insets(0, 0, 5, 0);
        gbc_lblApptDD.gridx = 3;
        gbc_lblApptDD.gridy = 9;
        pnlApptsTab.add(lblApptDD, gbc_lblApptDD);
        ApptList = new JList();
        ApptList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        ApptList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane listScroller = new JScrollPane(ApptList);
        ApptList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent arg0) {
                int index = ApptList.getSelectedIndex();
                lblApptDate.setText(String.valueOf(admin.getInfo().getAppt().sortedAppt().get(index).getDate()));
                lblApptTime.setText(String.valueOf(admin.getInfo().getAppt().sortedAppt().get(index).getTime()));
                lblApptReason.setText(admin.getInfo().getAppt().sortedAppt().get(index).getReason());
                lblApptDD.setText(admin.getInfo().getAppt().sortedAppt().get(index).getDoctor());
            }
        });
        GridBagConstraints gbc_listScroller = new GridBagConstraints();
        gbc_listScroller.gridheight = 6;
        gbc_listScroller.insets = new Insets(0, 0, 5, 5);
        gbc_listScroller.fill = GridBagConstraints.BOTH;
        gbc_listScroller.gridx = 1;
        gbc_listScroller.gridy = 5;
        pnlApptsTab.add(listScroller, gbc_listScroller);
        JButton btnDeleteAppts = new JButton("Delete");
        btnDeleteAppts.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                ApptList.remove(ApptList.getSelectedIndex());
            }
        });
        GridBagConstraints gbc_btnDeleteAppts = new GridBagConstraints();
        gbc_btnDeleteAppts.insets = new Insets(0, 0, 5, 5);
        gbc_btnDeleteAppts.gridx = 1;
        gbc_btnDeleteAppts.gridy = 11;
        pnlApptsTab.add(btnDeleteAppts, gbc_btnDeleteAppts);
    }
}
