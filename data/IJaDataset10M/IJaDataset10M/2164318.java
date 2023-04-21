package gui;

import java.awt.Cursor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import exception.MyHibernateException;
import bean.person.employee.Employee;
import bean.person.Phone;
import bean.person.PhoneType;
import facade.Facade;
import bean.person.Address;
import bean.person.employee.EmployeeRole;
import util.Refreshable;
import util.Util;

/**
 *
 * @author  W4M
 */
public class RegEmployeePanel extends javax.swing.JPanel implements Refreshable {

    private static final String TITLE_WARNING = "AVISO";

    private static final String MSG_REQUIRED_FIELDS = "Campo requerido:\n* Nome";

    private static RegEmployeePanel instance;

    private static Facade facade = Facade.getInstance();

    private static MainFrame mainFrame = MainFrame.getInstance();

    /** Creates new form RegEmployeePanel */
    private RegEmployeePanel() {
        initComponents();
        rgTF.setMaxLenght(Util.RG_MAX_LENGTH);
        Util.setCityModel(stateCB, cityCB);
    }

    public static RegEmployeePanel getInstance() {
        if (instance == null) {
            instance = new RegEmployeePanel();
        }
        instance.nameTf.requestFocusInWindow();
        return instance;
    }

    private void initComponents() {
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        registerBtn = new javax.swing.JButton();
        clearAllBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        infoPanel = new javax.swing.JPanel();
        obsLabel = new javax.swing.JLabel();
        obsJScrollPane = new javax.swing.JScrollPane();
        obsTA = new javax.swing.JTextArea();
        obsTA.setDocument(Util.uppeCaseDocument());
        employeePanel = new javax.swing.JPanel();
        infoEmployeePanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        numberLabel = new javax.swing.JLabel();
        complementLabel = new javax.swing.JLabel();
        districtLabel = new javax.swing.JLabel();
        zipcodeLabel = new javax.swing.JLabel();
        cityLabel = new javax.swing.JLabel();
        stateLabel = new javax.swing.JLabel();
        cpfLabel = new javax.swing.JLabel();
        rgLabel = new javax.swing.JLabel();
        zipcodeTF = new TextField(TextField.TYPE_ZIPCODE);
        stateCB = new javax.swing.JComboBox();
        numberTF = new gui.TextField();
        numberTF.setMaxLenght(6);
        nameTf = new TextField(TextField.TYPE_ALFA);
        addressTF = new TextField(TextField.TYPE_ALFA);
        complementTF = new TextField(TextField.TYPE_ALFA);
        districtTF = new TextField(TextField.TYPE_ALFA);
        rgTF = new gui.TextField();
        label3 = new javax.swing.JLabel();
        cityCB = new javax.swing.JComboBox();
        cpfTF = new TextField(TextField.TYPE_CPF);
        complementLabel1 = new javax.swing.JLabel();
        workNumberTF = new gui.TextField();
        complementLabel2 = new javax.swing.JLabel();
        driverLicenseTF = new gui.TextField(gui.TextField.TYPE_DEFAULT);
        driverLicenseTF.setMaxLenght(11);
        complementLabel3 = new javax.swing.JLabel();
        roleCB = new javax.swing.JComboBox();
        phonesPanel = new javax.swing.JPanel();
        resLabel = new javax.swing.JLabel();
        mobLabel = new javax.swing.JLabel();
        resTF = new TextField(TextField.TYPE_PHONE);
        mobTF = new TextField(TextField.TYPE_PHONE);
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Cadastro de Funcionário ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        label1.setForeground(new java.awt.Color(255, 0, 0));
        label1.setText("*");
        label2.setText("Campo requerido");
        registerBtn.setText("Cadastrar");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });
        clearAllBtn.setText("Limpar Tudo");
        clearAllBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAllBtnActionPerformed(evt);
            }
        });
        backBtn.setText("«« Voltar");
        backBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });
        infoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Informações Adicionais"));
        obsLabel.setText("Observações:");
        obsTA.setColumns(20);
        obsTA.setRows(5);
        obsJScrollPane.setViewportView(obsTA);
        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(infoPanelLayout.createSequentialGroup().addContainerGap().addComponent(obsLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(obsJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE).addContainerGap()));
        infoPanelLayout.setVerticalGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(infoPanelLayout.createSequentialGroup().addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(obsLabel).addComponent(obsJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        employeePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Funcionário"));
        nameLabel.setText("Nome:");
        addressLabel.setText("Logradouro:");
        numberLabel.setText("Número:");
        complementLabel.setText("Complemento:");
        districtLabel.setText("Bairro:");
        zipcodeLabel.setText("CEP:");
        cityLabel.setText("Cidade:");
        stateLabel.setText("Estado:");
        cpfLabel.setText("CPF:");
        rgLabel.setText("RG:");
        stateCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        stateCB.setSelectedIndex(14);
        stateCB.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                stateCBItemStateChanged(evt);
            }
        });
        rgTF.setText("textField10");
        label3.setForeground(new java.awt.Color(255, 0, 0));
        label3.setText("*");
        cityCB.setEditable(true);
        cityCB.setModel(Util.loadCity("PB"));
        complementLabel1.setText("C. trabalho:");
        workNumberTF.setToolTipText("Carteira de Trabalho (Profissional)");
        complementLabel2.setText("CNH:");
        complementLabel2.setToolTipText("Carteira Nacional de Habilitação");
        driverLicenseTF.setToolTipText("Carteira Nacional de Habilitação");
        complementLabel3.setText("Função:");
        roleCB.setEditable(true);
        roleCB.setModel(new javax.swing.DefaultComboBoxModel(EmployeeRole.values()));
        roleCB.setPreferredSize(new java.awt.Dimension(25, 27));
        ((javax.swing.JTextField) roleCB.getEditor().getEditorComponent()).setDocument(util.Util.uppeCaseDocument());
        roleCB.setSelectedItem(EmployeeRole.values()[0].toString());
        javax.swing.GroupLayout infoEmployeePanelLayout = new javax.swing.GroupLayout(infoEmployeePanel);
        infoEmployeePanel.setLayout(infoEmployeePanelLayout);
        infoEmployeePanelLayout.setHorizontalGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(infoEmployeePanelLayout.createSequentialGroup().addGap(12, 12, 12).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoEmployeePanelLayout.createSequentialGroup().addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(nameLabel)).addComponent(addressLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(complementLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(districtLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(cityLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(cpfLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(complementLabel1, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(complementLabel3, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(infoEmployeePanelLayout.createSequentialGroup().addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cityCB, 0, 0, Short.MAX_VALUE).addComponent(workNumberTF, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE).addComponent(cpfTF, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE).addComponent(districtTF, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE).addComponent(addressTF, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(numberLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(zipcodeLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(stateLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(rgLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(complementLabel2, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(stateCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(rgTF, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(driverLicenseTF, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(zipcodeTF, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addComponent(numberTF, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(nameTf, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addComponent(complementTF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addComponent(roleCB, 0, 404, Short.MAX_VALUE)).addContainerGap()));
        infoEmployeePanelLayout.setVerticalGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(infoEmployeePanelLayout.createSequentialGroup().addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(nameLabel).addComponent(label3).addComponent(nameTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(addressLabel).addComponent(addressTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(numberLabel).addComponent(numberTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(complementLabel).addComponent(complementTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(districtLabel).addComponent(districtTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(zipcodeLabel).addComponent(zipcodeTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(cityLabel).addComponent(stateLabel).addComponent(stateCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cityCB, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(cpfLabel).addComponent(cpfTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(rgLabel).addComponent(rgTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(8, 8, 8).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(workNumberTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(complementLabel1).addComponent(complementLabel2).addComponent(driverLicenseTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(infoEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(complementLabel3).addComponent(roleCB, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        infoEmployeePanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { addressTF, cityCB, complementTF, cpfTF, districtTF, driverLicenseTF, nameTf, numberTF, rgTF, roleCB, workNumberTF, zipcodeTF });
        phonesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Telefones "));
        resLabel.setText("Residencial:");
        mobLabel.setText("Celular:");
        resTF.setText("textField4");
        mobTF.setText("textField5");
        javax.swing.GroupLayout phonesPanelLayout = new javax.swing.GroupLayout(phonesPanel);
        phonesPanel.setLayout(phonesPanelLayout);
        phonesPanelLayout.setHorizontalGroup(phonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(phonesPanelLayout.createSequentialGroup().addContainerGap().addGroup(phonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mobLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(resLabel, javax.swing.GroupLayout.Alignment.TRAILING)).addGap(10, 10, 10).addGroup(phonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mobTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(resTF, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        phonesPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { mobTF, resTF });
        phonesPanelLayout.setVerticalGroup(phonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(phonesPanelLayout.createSequentialGroup().addGroup(phonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(resLabel).addComponent(resTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(12, 12, 12).addGroup(phonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(mobLabel).addComponent(mobTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        javax.swing.GroupLayout employeePanelLayout = new javax.swing.GroupLayout(employeePanel);
        employeePanel.setLayout(employeePanelLayout);
        employeePanelLayout.setHorizontalGroup(employeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(employeePanelLayout.createSequentialGroup().addComponent(infoEmployeePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(phonesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        employeePanelLayout.setVerticalGroup(employeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(phonesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(employeePanelLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(infoEmployeePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(infoPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(label1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(label2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE).addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(clearAllBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(employeePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { backBtn, clearAllBtn, registerBtn });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(employeePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(infoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(label2).addComponent(label1)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(registerBtn).addComponent(clearAllBtn).addComponent(backBtn))).addGap(78, 78, 78)));
    }

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Employee employee = buildEmployee();
        try {
            if (employee != null) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                facade.saveEmployee(employee);
                JOptionPane.showMessageDialog(instance, "Funcionário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                Util.clearAllFields(this);
            }
        } catch (MyHibernateException ex) {
            Logger.getLogger(RegEmployeePanel.class.getName()).log(Level.SEVERE, null, ex);
            Util.errorSQLPane(instance, ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void clearAllBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Util.clearAllFields(this);
        cityCB.setSelectedItem("JOÃO PESSOA");
        stateCB.setSelectedIndex(Util.DEFAULT_STATE_INDEX);
    }

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.backPanel();
    }

    private void stateCBItemStateChanged(java.awt.event.ItemEvent evt) {
        Util.setCityModel(stateCB, cityCB);
    }

    private Employee buildEmployee() {
        Employee employee = new Employee();
        Address address = new Address();
        if (nameTf.getText().equals("")) {
            JOptionPane.showMessageDialog(instance, MSG_REQUIRED_FIELDS, TITLE_WARNING, JOptionPane.INFORMATION_MESSAGE);
            nameTf.requestFocusInWindow();
            return null;
        }
        employee.setName(nameTf.getText());
        employee.setObs(obsTA.getText());
        employee.setWorkNumber("" + workNumberTF.getText());
        employee.setDriverLicense("" + driverLicenseTF.getText());
        employee.setEmployeeRole("" + roleCB.getSelectedItem().toString());
        if (numberTF.getText().equals("")) {
            address.setNumber(Util.normalize("S/N"));
        } else {
            address.setNumber(Util.normalize(numberTF.getText()));
        }
        address.setAddress(addressTF.getText());
        address.setCity(cityCB.getSelectedItem().toString());
        address.setComplement(complementTF.getText());
        address.setDistrict(districtTF.getText());
        address.setState(stateCB.getSelectedItem().toString());
        address.setZipCode(zipcodeTF.getText());
        employee.associateAddress(address);
        if (!cpfTF.getText().equals("")) {
            employee.setCpf(cpfTF.getText());
        }
        if (!rgTF.getText().equals("")) {
            employee.setRg(rgTF.getText());
        }
        if (!resTF.getText().equals("")) {
            employee.addPhone(new Phone(resTF.getText(), PhoneType.CELL));
        }
        if (!mobTF.getText().equals("")) {
            employee.addPhone(new Phone(mobTF.getText(), PhoneType.RESIDENCIAL));
        }
        employee.setEffective(1);
        return employee;
    }

    private javax.swing.JLabel addressLabel;

    private gui.TextField addressTF;

    private javax.swing.JButton backBtn;

    private javax.swing.JComboBox cityCB;

    private javax.swing.JLabel cityLabel;

    private javax.swing.JButton clearAllBtn;

    private javax.swing.JLabel complementLabel;

    private javax.swing.JLabel complementLabel1;

    private javax.swing.JLabel complementLabel2;

    private javax.swing.JLabel complementLabel3;

    private gui.TextField complementTF;

    private javax.swing.JLabel cpfLabel;

    private gui.TextField cpfTF;

    private javax.swing.JLabel districtLabel;

    private gui.TextField districtTF;

    private gui.TextField driverLicenseTF;

    private javax.swing.JPanel employeePanel;

    private javax.swing.JPanel infoEmployeePanel;

    private javax.swing.JPanel infoPanel;

    private javax.swing.JLabel label1;

    private javax.swing.JLabel label2;

    private javax.swing.JLabel label3;

    private javax.swing.JLabel mobLabel;

    private gui.TextField mobTF;

    private javax.swing.JLabel nameLabel;

    private gui.TextField nameTf;

    private javax.swing.JLabel numberLabel;

    private gui.TextField numberTF;

    private javax.swing.JScrollPane obsJScrollPane;

    private javax.swing.JLabel obsLabel;

    private javax.swing.JTextArea obsTA;

    private javax.swing.JPanel phonesPanel;

    private javax.swing.JButton registerBtn;

    private javax.swing.JLabel resLabel;

    private gui.TextField resTF;

    private javax.swing.JLabel rgLabel;

    private gui.TextField rgTF;

    private javax.swing.JComboBox roleCB;

    private javax.swing.JComboBox stateCB;

    private javax.swing.JLabel stateLabel;

    private gui.TextField workNumberTF;

    private javax.swing.JLabel zipcodeLabel;

    private gui.TextField zipcodeTF;

    public void refresh() {
        nameTf.requestFocusInWindow();
        roleCB.setSelectedItem(EmployeeRole.values()[0].toString());
    }
}
