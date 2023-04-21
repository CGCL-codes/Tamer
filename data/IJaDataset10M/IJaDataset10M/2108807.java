package gui;

import java.awt.Cursor;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import exception.DiscountHigherThantTotalException;
import exception.MyHibernateException;
import exception.NoUnitPriceDefinedException;
import exception.MyObjectNotFoundException;
import exception.OldVersionException;
import facade.Facade;
import bean.person.Address;
import bean.person.client.Client;
import bean.ossolicitation.OSSolicitation;
import bean.payment.Payment;
import bean.payment.PaymentPiece;
import bean.person.Person;
import bean.ossolicitation.Service;
import bean.ossolicitation.ServiceItem;
import bean.ossolicitation.ServiceOrder;
import bean.ossolicitation.ServiceOrderToilet;
import bean.user.User;
import bean.user.AccessLevel;
import bean.ossolicitation.OrderStatus;
import bean.payment.PaymentType;
import bean.ossolicitation.ServiceOrderType;
import bean.ossolicitation.ServiceType;
import printer.OSEmissionPrinter;
import report.ToiletReport;
import util.MyTableModel;
import util.Refreshable;
import util.TableAction;
import util.Util;
import net.sf.jasperreports.engine.JRException;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 * @author  w4m
 */
public class ShowOSToiletAwingPanel extends javax.swing.JPanel implements Refreshable {

    private static final int FIRST_ROW = 0;

    private static final int SECOND_ROW = 1;

    private static final int THIRD_ROW = 2;

    private final String TITLE = "MENSAGEM";

    private final String TITLE_WARNING = "AVISO";

    private final String TITLE_SUCCESSFUL = "SUCESSO";

    private final String MSG_REQUIRED_SERVICE_ITEM = "Você deve selecionar pelo menos um serviço.";

    private final String MSG_UPDATE_CONFIRMATION = "Deseja atualizar essa ordem de serviço?";

    private final String MSG_UPDATE_SUCCESSFUL = "Ordem de serviço atualizada com sucesso!";

    private final String MSG_CONCLUSION_CONFIRMATION = "Deseja concluir essa ordem de serviço?";

    private final String MSG_DISCOUNT = "O valor do desconto deve ser menor que o valor total.";

    private final String MSG_START_DATE_AFTER_END_DATE = "A data inicial deve ser menor que a final.";

    private final String MSG_CANCEL_CONFIRMATION = "Deseja realmente cancelar esta ordem de serviços?";

    private final String MSG_CANCEL_SUCCESSEFUL = "Ordem de serviços cancelada com sucesso!";

    private final String MSG_ALREADY_CONCLUDED = "Ordem de serviços já concluida.";

    private final String MSG_INVLAID_PAYMENTTYPE = "É necessário informar uma forma de pagamento.";

    private static ShowOSToiletAwingPanel instance;

    private static Facade facade;

    private static MainFrame mainFrame;

    private OSSolicitation osSolicitation;

    private final NumberFormat nf = new DecimalFormat(",###.##", new DecimalFormatSymbols(Util.LOCALE));

    private ShowOSToiletAwingPanel() {
        initComponents();
        init();
    }

    private void init() {
        try {
            facade = Facade.getInstance();
            mainFrame = MainFrame.getInstance();
            fillComboBox(paymentCB, Util.TYPE_PAYMENT);
            AutoCompleteDecorator.decorate(paymentCB);
            refTA.setDocument(Util.uppeCaseDocument());
            obsOSSTA.setDocument(Util.uppeCaseDocument());
            obsClientTA.setDocument(Util.uppeCaseDocument());
            Util.setCityModel(stateCB, cityCB);
            TableAction.addActionKeyManager(serviceTable);
        } catch (MyHibernateException ex) {
            Util.errorSQLPane(this, ex);
            Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ShowOSToiletAwingPanel getInstance(OSSolicitation osSolicitation) {
        if (instance == null) {
            instance = new ShowOSToiletAwingPanel();
        }
        instance.osSolicitation = osSolicitation;
        return instance;
    }

    public static ShowOSToiletAwingPanel getInstance(Client client) {
        if (instance == null) {
            instance = new ShowOSToiletAwingPanel();
        }
        instance.osSolicitation = new OSSolicitation();
        instance.osSolicitation.setClient(client);
        instance.fillClientInfo(client);
        return instance;
    }

    private void fillFields() {
        Util.clearAllFields(instance);
        if (osSolicitation.getServiceOrder() != null && osSolicitation.getServiceOrder() instanceof ServiceOrderToilet) {
            fillClientInfo(osSolicitation.getClient());
            fillUserInfo(osSolicitation.getUser());
            fillServiceOrderInfo(osSolicitation.getServiceOrder());
            fillAddressInfo(((ServiceOrderToilet) osSolicitation.getServiceOrder()).getEventPlace());
            fillPaymentInfo(osSolicitation);
            fillServiceTable();
            removeButtons(osSolicitation.getStatus());
        }
    }

    private void fillPaymentInfo(OSSolicitation osSolicitation) {
        if (osSolicitation.getPaymentType() != null) {
            paymentCB.setSelectedItem(osSolicitation.getPaymentType().toString());
        } else {
            paymentCB.setSelectedIndex(0);
        }
        Payment payment = osSolicitation.getPayment();
        if (payment != null && payment.getPaymentType().equals(PaymentType.TO_RECEIVE)) {
            dateJXDP.setDate(payment.getDate().getTime());
        }
        discountTF.setText(Util.getFormattedCurrency(osSolicitation.getDiscount()));
        totalTF.setText(Util.getFormattedCurrency(osSolicitation.getTotalPrice()));
    }

    private void fillClientInfo(Person client) {
        if (client != null) {
            nameTF.setText(client.getName());
            phoneTF.setText(client.getPhoneNumber());
            codeTF.setText(Util.getFormattedCode(client.getId()));
            obsClientTA.setText(client.getObs());
        }
    }

    private void fillUserInfo(User user) {
        if (user != null) {
            userTF.setText(user.getLogin());
        }
    }

    private void fillServiceOrderInfo(ServiceOrder order) {
        if (order instanceof ServiceOrderToilet) {
            ServiceOrderToilet orderToilet = (ServiceOrderToilet) order;
            eventNameTF.setText(orderToilet.getEvent());
            startDateJXDP.setDate(orderToilet.getStartDate().getTime());
            endDateJXDP.setDate(orderToilet.getFinishDate().getTime());
            osNumberTF.setText(Util.getFormattedCode(orderToilet.getOrderNumber()));
            emissionDateTF.setText(Util.getFormattedDate(orderToilet.getEmissionDate()));
            obsOSSTA.setText(order.getObs());
        }
    }

    private void fillAddressInfo(Address address) {
        if (address != null) {
            addressTF.setText(address.getAddress());
            numberTF.setText(address.getNumber());
            complementTF.setText(address.getComplement());
            districtTF.setText(address.getDistrict());
            zipcodeTF.setText(address.getZipCode());
            if (address.getState() != null) {
                stateCB.setSelectedItem(address.getState());
            }
            if (address.getCity() != null) {
                cityCB.setSelectedItem(address.getCity());
            }
            refTA.setText(address.getReference());
        }
    }

    private void fillServiceTable() {
        clearTable();
        for (ServiceItem serviceItem : osSolicitation.getServiceItens()) {
            if (serviceItem.getServiceType().equals(ServiceType.TOILET)) {
                fillTableRow(serviceItem, FIRST_ROW);
            } else if (serviceItem.getServiceType().equals(ServiceType.AWNING)) {
                fillTableRow(serviceItem, SECOND_ROW);
            } else {
                fillTableRow(serviceItem, THIRD_ROW);
            }
        }
    }

    private void fillTableRow(ServiceItem serviceItem, int row) {
        serviceTable.setValueAt(serviceItem.getQuantity(), row, Util.COLUMN_QNT);
        serviceTable.setValueAt(Util.getFormattedCurrency(serviceItem.getUnitPrice()), row, Util.COLUMN_UNIT_PRICE);
        serviceTable.setValueAt(Util.getFormattedCurrency(serviceItem.getQuantity() * serviceItem.getUnitPrice()), row, Util.COLUMN_SUBTOTAL);
    }

    private void fillComboBox(JComboBox comboBox, int type) throws MyHibernateException {
        comboBox.removeAllItems();
        List<? extends String> itens = new ArrayList<String>();
        if (type == Util.TYPE_CLIENT) {
            comboBox.addItem(Util.CLIENT_INITIAL_MSG_CB);
            itens = facade.loadClientsNames();
        } else if (type == Util.TYPE_PAYMENT) {
            comboBox.addItem(Util.PAYMENT_INITIAL_MSG_CB);
            itens = facade.loadPaymentTypes();
        }
        for (String item : itens) {
            comboBox.addItem(item);
        }
    }

    private void removeButtons(OrderStatus status) {
        if (status.equals(OrderStatus.CLOSED)) {
            updateBtn.setVisible(true);
            cancelBtn.setVisible(false);
            completeOsBtn.setVisible(false);
            printBtn.setVisible(true);
        } else if (status.equals(OrderStatus.CANCELLED)) {
            updateBtn.setVisible(false);
            cancelBtn.setVisible(false);
            completeOsBtn.setVisible(false);
            printBtn.setVisible(false);
        } else {
            updateBtn.setVisible(true);
            cancelBtn.setVisible(true);
            completeOsBtn.setVisible(true);
            printBtn.setVisible(true);
        }
        this.validateTree();
        this.repaint();
    }

    private void clearTable() {
        for (int i = 0; i < serviceTable.getRowCount(); i++) {
            for (int j = 0; j < serviceTable.getModel().getColumnCount(); j++) {
                if (j != Util.COLUMN_SERVICE) {
                    serviceTable.getModel().setValueAt("", i, j);
                }
            }
        }
    }

    private void initComponents() {
        clientPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        codeLabel = new javax.swing.JLabel();
        nameTF = new TextField(TextField.TYPE_ALFA);
        phoneTF = new TextField(TextField.TYPE_PHONE);
        codeTF = new gui.TextField();
        userPanel = new javax.swing.JPanel();
        emissorLabel = new javax.swing.JLabel();
        userTF = new javax.swing.JTextField();
        eventPanel = new javax.swing.JPanel();
        eventNameLabel = new javax.swing.JLabel();
        startDateLabel = new javax.swing.JLabel();
        endDateLabel = new javax.swing.JLabel();
        eventNameTF = new TextField(TextField.TYPE_ALFA);
        startDateJXDP = new org.jdesktop.swingx.JXDatePicker();
        endDateJXDP = new org.jdesktop.swingx.JXDatePicker();
        eventLocalPanel = new javax.swing.JPanel();
        addressLabel = new javax.swing.JLabel();
        complementLabel = new javax.swing.JLabel();
        districtLabel = new javax.swing.JLabel();
        cityLabel = new javax.swing.JLabel();
        refLabel = new javax.swing.JLabel();
        numberLabel = new javax.swing.JLabel();
        zipcodeLabel = new javax.swing.JLabel();
        stateLabel = new javax.swing.JLabel();
        addressTF = new TextField(TextField.TYPE_ALFA);
        numberTF = new gui.TextField();
        complementTF = new TextField(TextField.TYPE_ALFA);
        districtTF = new TextField(TextField.TYPE_ALFA);
        zipcodeTF = new TextField(TextField.TYPE_ZIPCODE);
        cityCB = new javax.swing.JComboBox();
        stateCB = new javax.swing.JComboBox();
        refScrollPane = new javax.swing.JScrollPane();
        refTA = new javax.swing.JTextArea();
        osPanel = new javax.swing.JPanel();
        servicePanel = new javax.swing.JPanel();
        serviceScrollPane = new javax.swing.JScrollPane();
        serviceTable = new javax.swing.JTable();
        paymentTypeLabel = new javax.swing.JLabel();
        discountLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        paymentCB = new javax.swing.JComboBox();
        discountTF = new TextField(TextField.TYPE_CURRENCY);
        totalTF = new TextField(TextField.TYPE_CURRENCY);
        dateJXDP = new org.jdesktop.swingx.JXDatePicker();
        receiveLB = new javax.swing.JLabel();
        printServiceValueCBX = new javax.swing.JCheckBox();
        osNumberLabel = new javax.swing.JLabel();
        emissionDateLabel = new javax.swing.JLabel();
        osNumberTF = new gui.TextField();
        emissionDateTF = new TextField(TextField.TYPE_ALFA);
        aditionalInfoPanel = new javax.swing.JPanel();
        obsLabel = new javax.swing.JLabel();
        obsScrollPane = new javax.swing.JScrollPane();
        obsOSSTA = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        obsClientTA = new javax.swing.JTextArea();
        updateBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        completeOsBtn = new javax.swing.JButton();
        printBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        visitsBtn = new javax.swing.JButton();
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Visualizar OS ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12)));
        clientPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Cliente "));
        nameLabel.setText("Nome:");
        phoneLabel.setText("Telefone:");
        codeLabel.setText("Código:");
        nameTF.setEditable(false);
        nameTF.setEnabled(false);
        phoneTF.setEnabled(false);
        codeTF.setEnabled(false);
        javax.swing.GroupLayout clientPanelLayout = new javax.swing.GroupLayout(clientPanel);
        clientPanel.setLayout(clientPanelLayout);
        clientPanelLayout.setHorizontalGroup(clientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(clientPanelLayout.createSequentialGroup().addContainerGap().addComponent(nameLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(nameTF, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(phoneLabel).addGap(5, 5, 5).addComponent(phoneTF, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(codeLabel).addGap(2, 2, 2).addComponent(codeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        clientPanelLayout.setVerticalGroup(clientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(clientPanelLayout.createSequentialGroup().addGroup(clientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nameLabel).addComponent(codeLabel).addComponent(codeTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(phoneLabel).addComponent(phoneTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(nameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        userPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Usuário "));
        emissorLabel.setText("Emissor:");
        userTF.setEnabled(false);
        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(userPanelLayout.createSequentialGroup().addContainerGap().addComponent(emissorLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(userTF, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        userPanelLayout.setVerticalGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(userPanelLayout.createSequentialGroup().addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(emissorLabel).addComponent(userTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        eventPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Evento "));
        eventNameLabel.setText("Nome:");
        startDateLabel.setText("Data inicio:");
        endDateLabel.setText("Data Final:");
        eventLocalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Local "));
        addressLabel.setText("Logradouro:");
        complementLabel.setText("Complemento:");
        districtLabel.setText("Bairro:");
        cityLabel.setText("Cidade:");
        refLabel.setText("Referência:");
        numberLabel.setText("Número:");
        zipcodeLabel.setText("CEP:");
        stateLabel.setText("Estado:");
        cityCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "JOÃO PESSOA" }));
        stateCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PB" }));
        stateCB.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                stateCBItemStateChanged(evt);
            }
        });
        refTA.setColumns(20);
        refTA.setRows(5);
        refScrollPane.setViewportView(refTA);
        javax.swing.GroupLayout eventLocalPanelLayout = new javax.swing.GroupLayout(eventLocalPanel);
        eventLocalPanel.setLayout(eventLocalPanelLayout);
        eventLocalPanelLayout.setHorizontalGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(eventLocalPanelLayout.createSequentialGroup().addContainerGap().addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(refLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(addressLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(complementLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(cityLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(districtLabel, javax.swing.GroupLayout.Alignment.TRAILING)).addGap(12, 12, 12).addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(refScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE).addComponent(complementTF, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE).addGroup(eventLocalPanelLayout.createSequentialGroup().addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(cityCB, 0, 807, Short.MAX_VALUE).addComponent(addressTF, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE).addComponent(districtTF, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)).addGap(13, 13, 13).addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(stateLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(zipcodeLabel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(numberLabel, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(stateCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(zipcodeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(numberTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap()));
        eventLocalPanelLayout.setVerticalGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(eventLocalPanelLayout.createSequentialGroup().addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(addressLabel).addComponent(numberLabel).addComponent(numberTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(addressTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(complementLabel).addComponent(complementTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(districtLabel).addComponent(zipcodeTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(zipcodeLabel).addComponent(districtTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(cityLabel).addComponent(stateLabel).addComponent(stateCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cityCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(eventLocalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(refLabel).addComponent(refScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout eventPanelLayout = new javax.swing.GroupLayout(eventPanel);
        eventPanel.setLayout(eventPanelLayout);
        eventPanelLayout.setHorizontalGroup(eventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, eventPanelLayout.createSequentialGroup().addContainerGap().addGroup(eventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(eventLocalPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(eventPanelLayout.createSequentialGroup().addComponent(eventNameLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(eventNameTF, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(startDateLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(startDateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(endDateLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(endDateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        eventPanelLayout.setVerticalGroup(eventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(eventPanelLayout.createSequentialGroup().addGroup(eventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(eventNameLabel).addComponent(startDateLabel).addComponent(endDateLabel).addComponent(eventNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(startDateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(endDateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(eventLocalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(48, Short.MAX_VALUE)));
        osPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Ordem de serviço "));
        servicePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Serviços "));
        serviceTable.setModel(new MyTableModel(MyTableModel.TOILET_MODEL, MyTableModel.SERVICES_COLS_MODEL));
        serviceTable.setCellSelectionEnabled(true);
        serviceTable.getTableHeader().setReorderingAllowed(false);
        serviceScrollPane.setViewportView(serviceTable);
        serviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serviceTable.getColumn(Util.COLUMN_QNT_NAME).setCellEditor(Util.getNumericCellEditor(serviceTable, true, totalTF));
        serviceTable.getColumn(Util.COLUMN_UNIT_PRICE_NAME).setCellEditor(Util.getCurrencyCellEditor(serviceTable, true, totalTF));
        serviceTable.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                DefaultCellEditor dce = ((DefaultCellEditor) serviceTable.getCellEditor());
                if (dce != null) {
                    dce.stopCellEditing();
                }
            }
        });
        paymentTypeLabel.setText("Forma de pagamento:");
        discountLabel.setText("Desconto:");
        totalLabel.setText("Total:");
        paymentCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<SELECIONE A FORMA DE PAGAMENTO>" }));
        paymentCB.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                paymentCBItemStateChanged(evt);
            }
        });
        discountTF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountTFActionPerformed(evt);
            }
        });
        discountTF.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                discountTFKeyReleased(evt);
            }
        });
        totalTF.setEnabled(false);
        receiveLB.setText("Recebimento:");
        printServiceValueCBX.setSelected(true);
        printServiceValueCBX.setText("Imprimir valor");
        javax.swing.GroupLayout servicePanelLayout = new javax.swing.GroupLayout(servicePanel);
        servicePanel.setLayout(servicePanelLayout);
        servicePanelLayout.setHorizontalGroup(servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(servicePanelLayout.createSequentialGroup().addContainerGap().addGroup(servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(serviceScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1046, Short.MAX_VALUE).addGroup(servicePanelLayout.createSequentialGroup().addComponent(paymentTypeLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(paymentCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(receiveLB).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(dateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE).addComponent(printServiceValueCBX).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(discountLabel).addGap(6, 6, 6).addComponent(discountTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(totalLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(totalTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        servicePanelLayout.setVerticalGroup(servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(servicePanelLayout.createSequentialGroup().addComponent(serviceScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(paymentTypeLabel).addComponent(paymentCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(discountLabel).addComponent(discountTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(totalLabel).addComponent(totalTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(dateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(receiveLB).addComponent(printServiceValueCBX)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        osNumberLabel.setText("Número:");
        emissionDateLabel.setText("Data de emissão:");
        osNumberTF.setEnabled(false);
        emissionDateTF.setEnabled(false);
        javax.swing.GroupLayout osPanelLayout = new javax.swing.GroupLayout(osPanel);
        osPanel.setLayout(osPanelLayout);
        osPanelLayout.setHorizontalGroup(osPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(osPanelLayout.createSequentialGroup().addContainerGap().addGroup(osPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(servicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(osPanelLayout.createSequentialGroup().addComponent(osNumberLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(osNumberTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(emissionDateLabel).addGap(4, 4, 4).addComponent(emissionDateTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        osPanelLayout.setVerticalGroup(osPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(osPanelLayout.createSequentialGroup().addGroup(osPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(osNumberLabel).addComponent(osNumberTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(emissionDateLabel).addComponent(emissionDateTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(servicePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(28, Short.MAX_VALUE)));
        aditionalInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Informações adicionais "));
        obsLabel.setText("Observações do cliente:");
        obsOSSTA.setColumns(20);
        obsOSSTA.setRows(5);
        obsScrollPane.setViewportView(obsOSSTA);
        jLabel1.setText("Observações da OS:");
        obsClientTA.setColumns(20);
        obsClientTA.setEditable(false);
        obsClientTA.setRows(5);
        jScrollPane1.setViewportView(obsClientTA);
        javax.swing.GroupLayout aditionalInfoPanelLayout = new javax.swing.GroupLayout(aditionalInfoPanel);
        aditionalInfoPanel.setLayout(aditionalInfoPanelLayout);
        aditionalInfoPanelLayout.setHorizontalGroup(aditionalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(aditionalInfoPanelLayout.createSequentialGroup().addGroup(aditionalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(aditionalInfoPanelLayout.createSequentialGroup().addContainerGap().addComponent(obsLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aditionalInfoPanelLayout.createSequentialGroup().addGap(27, 27, 27).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(obsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE))).addContainerGap()));
        aditionalInfoPanelLayout.setVerticalGroup(aditionalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(aditionalInfoPanelLayout.createSequentialGroup().addGap(4, 4, 4).addGroup(aditionalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(obsLabel).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(aditionalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(obsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        updateBtn.setText("Atualizar OS");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });
        backBtn.setText("«« Voltar");
        backBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });
        completeOsBtn.setText("Concluir OS");
        completeOsBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completeOsBtnActionPerformed(evt);
            }
        });
        printBtn.setText("Imprimir OS");
        printBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });
        cancelBtn.setText("Cancelar OS");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        visitsBtn.setText("Visitas");
        visitsBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitsBtnActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(aditionalInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(osPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(eventPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(clientPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(userPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(visitsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(completeOsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { backBtn, updateBtn });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(userPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(clientPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(eventPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(osPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(aditionalInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelBtn).addComponent(updateBtn).addComponent(completeOsBtn).addComponent(printBtn).addComponent(backBtn).addComponent(visitsBtn)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (validateOS()) {
            if (Util.confirmationDialog(TITLE, MSG_UPDATE_CONFIRMATION)) {
                registerServiceOrder(osSolicitation, true);
            }
        }
    }

    private boolean validateOS() {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startDateJXDP.getDate());
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endDateJXDP.getDate());
        if (startDate.after(endDate)) {
            JOptionPane.showMessageDialog(instance, MSG_START_DATE_AFTER_END_DATE);
            startDateJXDP.requestFocusInWindow();
            return false;
        }
        if (paymentCB.getSelectedIndex() <= Util.FIRST_INDEX) {
            JOptionPane.showMessageDialog(instance, MSG_INVLAID_PAYMENTTYPE);
            return false;
        }
        try {
            applyDiscount();
        } catch (DiscountHigherThantTotalException ex) {
            JOptionPane.showMessageDialog(instance, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            discountTF.requestFocusInWindow();
            return false;
        }
        return true;
    }

    private void registerServiceOrder(OSSolicitation solicitation, boolean update) {
        Set<ServiceType> servicesToRemove = new HashSet<ServiceType>();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            solicitation.setUser(MainFrame.getInstance().getUser());
            Set<ServiceItem> servicesToAdd = getServicesItens(servicesToRemove);
            if (servicesToAdd.size() == 0) {
                JOptionPane.showMessageDialog(instance, MSG_REQUIRED_SERVICE_ITEM, TITLE, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Address eventPlace = getAddress();
            ServiceOrder order = doServiceOrder(servicesToAdd, servicesToRemove, eventPlace);
            solicitation.getServiceOrder().associatePayment(doPayment());
            solicitation.associateServiceOrder(order);
            solicitation.getServiceOrder().setObs(obsOSSTA.getText());
            if (update) {
                facade.updateOSSolicitation(solicitation);
                this.refresh();
                JOptionPane.showMessageDialog(instance, MSG_UPDATE_SUCCESSFUL, TITLE_SUCCESSFUL, JOptionPane.INFORMATION_MESSAGE);
            } else {
                rentInEquipmentByName(solicitation);
                facade.closeOSSolicitation(solicitation);
                mainFrame.homePanel();
            }
        } catch (MyObjectNotFoundException ex) {
            Util.objectAlreadyRemovedPane(instance, ex);
            Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OldVersionException ex) {
            if (Util.transactionErroDialog()) {
                try {
                    osSolicitation = facade.loadOSSolicitation(osSolicitation.getId());
                    refresh();
                    Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MyHibernateException ex1) {
                    Util.errorSQLPane(instance, ex1);
                    Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } catch (MyHibernateException ex) {
            Util.errorSQLPane(instance, ex);
            Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoUnitPriceDefinedException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void rentInEquipmentByName(OSSolicitation solicitation) throws MyObjectNotFoundException, MyHibernateException, OldVersionException {
        if (solicitation.getStatus().equals(OrderStatus.EXECUTED) || solicitation.getStatus().equals(OrderStatus.EXECUTING)) {
            for (ServiceItem item : solicitation.getServiceItens()) {
                facade.rentInEquipmentByName(item.getService().toString(), item.getQuantity(), facade.getCurrentDate());
            }
        }
    }

    private Payment doPayment() throws MyHibernateException, MyObjectNotFoundException, OldVersionException {
        Payment payment = null;
        PaymentPiece paymentPiece = null;
        if (paymentCB.getSelectedIndex() != Util.FIRST_INDEX) {
            if (osSolicitation.getPayment() == null) {
                payment = new Payment();
                paymentPiece = new PaymentPiece();
                paymentPiece.setDate(facade.getCurrentDate());
            } else {
                payment = osSolicitation.getPayment();
                paymentPiece = osSolicitation.getPayment().getPaymentPieces().get(0);
                paymentPiece.setDone(PaymentPiece.DONE);
            }
            paymentPiece.setPrice(Util.calculateTotal(serviceTable, Util.COLUMN_SUBTOTAL_NAME));
            String selected = (String) paymentCB.getSelectedItem();
            if (selected.equals(PaymentType.TO_RECEIVE.toString())) {
                Calendar c = Calendar.getInstance();
                c.setTime(dateJXDP.getDate());
                paymentPiece.setDate(c);
                paymentPiece.setDone(PaymentPiece.NOT_DONE);
            } else {
                paymentPiece.setDate(facade.getCurrentDate());
            }
            payment.addPaymentPiece(paymentPiece);
            payment.setPaymentType(PaymentType.get(paymentCB.getSelectedItem().toString()));
        } else {
            if (osSolicitation.getPayment() != null) {
                facade.removePayment(osSolicitation.getPayment());
            }
        }
        return payment;
    }

    private ServiceOrder doServiceOrder(Set<ServiceItem> servicesToAdd, Set<ServiceType> servicesToRemove, Address eventPlace) {
        ServiceOrderToilet order = (ServiceOrderToilet) osSolicitation.getServiceOrder();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startDateJXDP.getDate());
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endDateJXDP.getDate());
        order.setServiceOrderType(ServiceOrderType.TOILET);
        for (ServiceItem item : servicesToAdd) {
            order.addServiceItem(item);
        }
        for (ServiceType serviceType : servicesToRemove) {
            order.removeServiceItem(serviceType);
        }
        order.setEmissionDate(facade.getCurrentDate());
        order.setStartDate(startDate);
        order.setFinishDate(endDate);
        order.setEvent(eventNameTF.getText());
        order.setEventPlace(eventPlace);
        double discount;
        try {
            discount = nf.parse(discountTF.getText()).doubleValue();
        } catch (ParseException e) {
            discount = 0.0;
        }
        order.setDiscount(discount);
        return order;
    }

    private Address getAddress() {
        Address address = ((ServiceOrderToilet) osSolicitation.getServiceOrder()).getEventPlace();
        address.setAddress(Util.normalize(addressTF.getText()));
        if (!numberTF.getText().equals("")) {
            address.setNumber(Util.normalize(numberTF.getText()));
        } else {
            address.setNumber(Util.normalize("S/N"));
        }
        address.setComplement(Util.normalize(complementTF.getText()));
        address.setDistrict(Util.normalize(districtTF.getText()));
        address.setZipCode(Util.normalize(zipcodeTF.getText()));
        address.setCity(Util.normalize(cityCB.getSelectedItem().toString()));
        address.setState(Util.normalize(stateCB.getSelectedItem().toString()));
        address.setReference(Util.normalize(refTA.getText()));
        return address;
    }

    private Set<ServiceItem> getServicesItens(Set<ServiceType> servicesToRemove) throws NoUnitPriceDefinedException {
        Set<ServiceItem> serviceItens = new HashSet<ServiceItem>();
        for (int i = 0; i < serviceTable.getRowCount(); i++) {
            try {
                ServiceItem si = new ServiceItem();
                ServiceType serviceType = ServiceType.get(serviceTable.getValueAt(i, serviceTable.getColumn(Util.COLUMN_SERVICE_NAME).getModelIndex()).toString());
                String qnt = serviceTable.getValueAt(i, serviceTable.getColumn(Util.COLUMN_QNT_NAME).getModelIndex()).toString();
                if (qnt.equals("")) {
                    si.setQuantity(1);
                } else {
                    si.setQuantity(Integer.parseInt(qnt));
                }
                String unitPrice = serviceTable.getValueAt(i, serviceTable.getColumn(Util.COLUMN_UNIT_PRICE_NAME).getModelIndex()).toString();
                if (unitPrice.equals("") && qnt.equals("")) {
                    servicesToRemove.add(serviceType);
                    continue;
                } else if (unitPrice.equals("")) {
                    throw new NoUnitPriceDefinedException(serviceTable.getValueAt(i, serviceTable.getColumn(Util.COLUMN_SERVICE_NAME).getModelIndex()).toString());
                }
                si.setUnitPrice(nf.parse(unitPrice).doubleValue());
                Service service = facade.loadService(serviceType);
                si.setService(service);
                serviceItens.add(si);
            } catch (MyHibernateException ex) {
                Util.errorSQLPane(instance, ex);
                Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(OSDebrisWaterPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return serviceItens;
    }

    private void applyDiscount() throws DiscountHigherThantTotalException {
        double discount = 0.0;
        try {
            discount = nf.parse(discountTF.getText()).doubleValue();
        } catch (ParseException e) {
            discount = 0.0;
        }
        double total = Util.calculateTotal(serviceTable, Util.COLUMN_SUBTOTAL_NAME);
        if (discount <= total) {
            total -= discount;
            totalTF.setText(NumberFormat.getCurrencyInstance().format(total));
        } else {
            throw new DiscountHigherThantTotalException(MSG_DISCOUNT);
        }
    }

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.backPanel();
    }

    private void stateCBItemStateChanged(java.awt.event.ItemEvent evt) {
        Util.setCityModel(stateCB, cityCB);
    }

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            OSEmissionPrinter.printReport(new ToiletReport(osSolicitation, printServiceValueCBX.isSelected()));
        } catch (JRException ex) {
            Logger.getLogger(OSMapPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OSMapPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (Util.confirmationDialog(TITLE_WARNING, MSG_CANCEL_CONFIRMATION)) {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                rentInEquipmentByName(osSolicitation);
                facade.cancelOSSolicitation(this.osSolicitation);
                refresh();
                JOptionPane.showMessageDialog(this, MSG_CANCEL_SUCCESSEFUL, TITLE_SUCCESSFUL, JOptionPane.INFORMATION_MESSAGE);
                mainFrame.homePanel();
            } catch (MyObjectNotFoundException ex) {
                Util.objectAlreadyRemovedPane(instance, ex);
                Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OldVersionException ex) {
                if (Util.transactionErroDialog()) {
                    try {
                        osSolicitation = facade.loadOSSolicitation(osSolicitation.getId());
                        refresh();
                        Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MyHibernateException ex1) {
                        Util.errorSQLPane(instance, ex1);
                        Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            } catch (MyHibernateException ex) {
                Util.errorSQLPane(instance, ex);
                Logger.getLogger(ShowOSToiletAwingPanel.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    private void completeOsBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (validateOS()) {
            if (this.osSolicitation.getStatus() == OrderStatus.EXECUTING || osSolicitation.getStatus() == OrderStatus.OPENED || this.osSolicitation.getStatus() == OrderStatus.TRANSFERRED) {
                if (Util.confirmationDialog(TITLE, MSG_CONCLUSION_CONFIRMATION)) {
                    registerServiceOrder(osSolicitation, false);
                }
            } else {
                JOptionPane.showMessageDialog(this, MSG_ALREADY_CONCLUDED, TITLE_WARNING, JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void visitsBtnActionPerformed(java.awt.event.ActionEvent evt) {
        ToiletAwingVisitsDialog.getInstance(osSolicitation).setVisible(true);
    }

    private void paymentCBItemStateChanged(java.awt.event.ItemEvent evt) {
        if (paymentCB.getSelectedIndex() > Util.ERROR_INDEX) {
            String selected = (String) paymentCB.getSelectedItem();
            if (selected.equals(PaymentType.TO_RECEIVE.toString())) {
                receiveLB.setEnabled(true);
                dateJXDP.setEnabled(true);
            } else {
                receiveLB.setEnabled(false);
                dateJXDP.setEnabled(false);
            }
        }
    }

    private void discountTFKeyReleased(java.awt.event.KeyEvent evt) {
        try {
            if (Character.isDigit(evt.getKeyChar())) {
                applyDiscount();
            }
            if (discountTF.getText().equals("")) {
                discountTF.setText(Util.getFormattedCurrency(0.0));
                totalTF.setText(Util.getFormattedCurrency(Util.calculateTotal(serviceTable, Util.COLUMN_SUBTOTAL_NAME)));
            }
        } catch (DiscountHigherThantTotalException ex) {
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), TITLE_WARNING, JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
        }
    }

    private void discountTFActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JLabel addressLabel;

    private gui.TextField addressTF;

    private javax.swing.JPanel aditionalInfoPanel;

    private javax.swing.JButton backBtn;

    private javax.swing.JButton cancelBtn;

    private javax.swing.JComboBox cityCB;

    private javax.swing.JLabel cityLabel;

    private javax.swing.JPanel clientPanel;

    private javax.swing.JLabel codeLabel;

    private gui.TextField codeTF;

    private javax.swing.JLabel complementLabel;

    private gui.TextField complementTF;

    private javax.swing.JButton completeOsBtn;

    private org.jdesktop.swingx.JXDatePicker dateJXDP;

    private javax.swing.JLabel discountLabel;

    private gui.TextField discountTF;

    private javax.swing.JLabel districtLabel;

    private gui.TextField districtTF;

    private javax.swing.JLabel emissionDateLabel;

    private gui.TextField emissionDateTF;

    private javax.swing.JLabel emissorLabel;

    private org.jdesktop.swingx.JXDatePicker endDateJXDP;

    private javax.swing.JLabel endDateLabel;

    private javax.swing.JPanel eventLocalPanel;

    private javax.swing.JLabel eventNameLabel;

    private gui.TextField eventNameTF;

    private javax.swing.JPanel eventPanel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel nameLabel;

    private gui.TextField nameTF;

    private javax.swing.JLabel numberLabel;

    private gui.TextField numberTF;

    private javax.swing.JTextArea obsClientTA;

    private javax.swing.JLabel obsLabel;

    private javax.swing.JTextArea obsOSSTA;

    private javax.swing.JScrollPane obsScrollPane;

    private javax.swing.JLabel osNumberLabel;

    private gui.TextField osNumberTF;

    private javax.swing.JPanel osPanel;

    private javax.swing.JComboBox paymentCB;

    private javax.swing.JLabel paymentTypeLabel;

    private javax.swing.JLabel phoneLabel;

    private gui.TextField phoneTF;

    private javax.swing.JButton printBtn;

    private javax.swing.JCheckBox printServiceValueCBX;

    private javax.swing.JLabel receiveLB;

    private javax.swing.JLabel refLabel;

    private javax.swing.JScrollPane refScrollPane;

    private javax.swing.JTextArea refTA;

    private javax.swing.JPanel servicePanel;

    private javax.swing.JScrollPane serviceScrollPane;

    private javax.swing.JTable serviceTable;

    private org.jdesktop.swingx.JXDatePicker startDateJXDP;

    private javax.swing.JLabel startDateLabel;

    private javax.swing.JComboBox stateCB;

    private javax.swing.JLabel stateLabel;

    private javax.swing.JLabel totalLabel;

    private gui.TextField totalTF;

    private javax.swing.JButton updateBtn;

    private javax.swing.JPanel userPanel;

    private javax.swing.JTextField userTF;

    private javax.swing.JButton visitsBtn;

    private javax.swing.JLabel zipcodeLabel;

    private gui.TextField zipcodeTF;

    public void refresh() {
        fillFields();
        if (mainFrame.getUserLoggedAccessLevel().equals(AccessLevel.USER) && paymentCB.getSelectedItem().equals(PaymentType.TO_RECEIVE.toString())) {
            paymentCB.setEnabled(false);
        } else {
            paymentCB.setEnabled(true);
        }
        printServiceValueCBX.setSelected(true);
        backBtn.requestFocusInWindow();
    }
}
