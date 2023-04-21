package gui;

import exception.InvalidOSNumberException;
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
import javax.swing.JTable;
import javax.swing.table.TableModel;
import exception.MyHibernateException;
import exception.NoUnitPriceDefinedException;
import bean.person.client.Client;
import bean.ossolicitation.OrderStatus;
import facade.Facade;
import bean.ossolicitation.OSSolicitation;
import bean.payment.Payment;
import bean.payment.PaymentPiece;
import report.OSReport;
import bean.ossolicitation.Service;
import bean.ossolicitation.ServiceItem;
import bean.ossolicitation.ServiceOrderDebrisWater;
import bean.person.employee.EmployeeRole;
import bean.payment.PaymentType;
import bean.ossolicitation.ServiceOrderType;
import bean.ossolicitation.ServiceType;
import printer.OSEmissionPrinter;
import util.MyTableModel;
import util.Refreshable;
import util.TableAction;
import util.Util;
import net.sf.jasperreports.engine.JRException;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author w4m
 */
public class OSDebrisWaterPanelSync extends javax.swing.JPanel implements Refreshable {

    private static final int TYPE_CLIENT = 0;

    private static final int TYPE_DRIVER = 1;

    private static final int TYPE_ASSISTANT = 4;

    private static final int TYPE_PLATE = 2;

    private static final int TYPE_PAYMENTTYPE = 3;

    private static final String MSG_OSNUMBER_INVALID = "Número de OS inválido ou já existente.";

    private static final String MSG_REQUIRED_PAYMENTTYPE = "Informe uma forma de pagamento.";

    private static final String MSG_REQUIRED_OSNUMBER = "Informe o número da OS.";

    private static final String TITLE_WARNING = "AVISO";

    private static final String TITLE_1 = "MENSAGEM";

    private static final String TITLE_2 = "CONFIRMAÇÃO";

    private static final String MSG_1 = "Você deve selecionar pelo menos um serviço.";

    private static final String MSG_2 = "Deseja sincronizar essa ordem de serviço?";

    private static final String MSG_3 = "Selecione um cliente.";

    private static final String PAYMENTTYPE_CB_INITIAL_MSG = "<INFORME A FORMA DE PAGAMENTO>";

    private static final String CLIENT_CB_INTIAL_MSG = "<INFORME OU SELECIONE UM CLIENTE>";

    private static final String EMPLOYEE_CB_INITIAL_MSG = "<INFORME OU SELECIONE UM FUNCIONÁRIO>";

    private static final String PLATE_CB_INITIAL_MSG = "<SELECIONE UM VEÍCULO>";

    private static OSDebrisWaterPanelSync instance = new OSDebrisWaterPanelSync();

    private static Facade facade = Facade.getInstance();

    private static MainFrame mainFrame = MainFrame.getInstance();

    private TableModel waterServiceModel;

    private TableModel debrisServiceModel;

    private static final NumberFormat nf = new DecimalFormat(",###.##", new DecimalFormatSymbols(Util.LOCALE));

    private Client client;

    private OSDebrisWaterPanelSync() {
        initComponents();
        init();
        Util.applyTableRowColor(serviceTable);
    }

    private void init() {
        client = null;
        waterServiceModel = new MyTableModel(MyTableModel.WATER_MODEL, MyTableModel.SERVICES_COLS_MODEL);
        debrisServiceModel = new MyTableModel(MyTableModel.DEBRIS_MODEL, MyTableModel.SERVICES_COLS_MODEL);
        AutoCompleteDecorator.decorate(searchNameCB);
        obsOSSTA.setDocument(Util.uppeCaseDocument());
        obsClientTA.setDocument(Util.uppeCaseDocument());
        TableAction.addActionKeyManager(serviceTable);
    }

    public static OSDebrisWaterPanelSync getInstance() {
        return instance;
    }

    private void fillFields() throws MyHibernateException {
        String clientItem = (String) searchNameCB.getSelectedItem();
        int paymentTypeIndex = paymentCB.getSelectedIndex();
        fillComboBox(searchNameCB, TYPE_CLIENT);
        fillComboBox(paymentCB, TYPE_PAYMENTTYPE);
        fillInfoClientFields();
        if (clientItem != null) {
            searchNameCB.setSelectedItem(clientItem);
        }
        if (paymentTypeIndex > Util.FIRST_INDEX) {
            paymentCB.setSelectedIndex(paymentTypeIndex);
        }
    }

    private void fillInfoClientFields() {
        if (client != null) {
            searchNameCB.setSelectedItem(client.getName());
            codeTF.setText(Util.getFormattedCode(client.getId()));
            phoneTF.setText(client.getPhoneNumber());
            obsClientTA.setText(client.getObs());
        }
    }

    private void fillComboBox(JComboBox comboBox, int type) throws MyHibernateException {
        comboBox.removeAllItems();
        List<String> itens = null;
        switch(type) {
            case TYPE_CLIENT:
                itens = facade.loadClientsNames();
                comboBox.addItem(CLIENT_CB_INTIAL_MSG);
                break;
            case TYPE_DRIVER:
                itens = facade.loadEmployeesNames(EmployeeRole.DRIVER.toString());
                comboBox.addItem(EMPLOYEE_CB_INITIAL_MSG);
                break;
            case TYPE_ASSISTANT:
                itens = facade.loadEmployeesNames(EmployeeRole.ASSISTANT.toString());
                comboBox.addItem(EMPLOYEE_CB_INITIAL_MSG);
                break;
            case TYPE_PLATE:
                itens = facade.loadVehiclesPlates();
                comboBox.addItem(PLATE_CB_INITIAL_MSG);
                break;
            case TYPE_PAYMENTTYPE:
                itens = new ArrayList<String>();
                for (PaymentType pt : PaymentType.values()) {
                    itens.add(pt.toString());
                }
                comboBox.addItem(PAYMENTTYPE_CB_INITIAL_MSG);
                break;
        }
        for (String item : itens) {
            comboBox.addItem(item);
        }
    }

    private void registerServiceOrder() {
        OSSolicitation solicitation = new OSSolicitation();
        solicitation.associateServiceOrder(new ServiceOrderDebrisWater());
        solicitation.setUser(MainFrame.getInstance().getUser());
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Set<ServiceItem> servicesItens = getServicesItens();
            if (servicesItens.size() == 0) {
                JOptionPane.showMessageDialog(instance, MSG_1, TITLE_1, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!discountTF.equals("")) {
                double discount = 0.0;
                try {
                    discount = nf.parse(discountTF.getText()).doubleValue();
                } catch (ParseException e) {
                    discount = 0.0;
                }
                solicitation.getServiceOrder().setDiscount(discount);
            }
            Calendar executionDate = Calendar.getInstance();
            executionDate.setTime(previsionDateJXDP.getDate());
            Calendar emissionDate = Calendar.getInstance();
            emissionDate.setTime(emissionDateJXDP.getDate());
            solicitation.getServiceOrder().setStatus(OrderStatus.OPENED);
            solicitation.getServiceOrder().associateServiceItens(servicesItens);
            solicitation.getServiceOrder().setEmissionDate(emissionDate);
            ((ServiceOrderDebrisWater) solicitation.getServiceOrder()).setPrevisionStartDate(executionDate);
            solicitation.getServiceOrder().setObs(obsOSSTA.getText());
            solicitation.setClient(client);
            solicitation.getServiceOrder().associatePayment(doPayment());
            solicitation.getServiceOrder().setOrderNumber(nf.parse(numberOSTF.getText()).intValue());
            if (Util.confirmationDialog(mainFrame, TITLE_2, MSG_2)) {
                if (debrisAndUnblockRB.isSelected()) {
                    solicitation.getServiceOrder().setServiceOrderType(ServiceOrderType.DEBRIS);
                } else {
                    solicitation.getServiceOrder().setServiceOrderType(ServiceOrderType.WATER);
                    if (capacityTF.getSelectedItem() != null || !capacityTF.getSelectedItem().toString().equals("")) {
                        ((ServiceOrderDebrisWater) solicitation.getServiceOrder()).setCapacity(Util.getDoubleValue(capacityTF.getSelectedItem().toString()));
                    }
                }
                facade.saveOSSolicitation(solicitation);
                OSEmissionPrinter.printReport(new OSReport(solicitation, printServiceValueCBX.isSelected()), solicitation.getServiceOrder().getServiceOrderType());
                clearAll();
                refresh();
                mainFrame.homePanel();
            } else {
                return;
            }
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            Logger.getLogger(OSDebrisWaterPanelSync.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            Logger.getLogger(OSDebrisWaterPanelSync.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MyHibernateException ex) {
            Logger logger = Logger.getLogger(OSDebrisWaterPanelSync.class.getName());
            if (ex.getCause() instanceof com.mysql.jdbc.MysqlDataTruncation) {
                JOptionPane.showMessageDialog(this, "A data informada é inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                Util.errorSQLPane(mainFrame, ex);
                logger.log(Level.SEVERE, null, ex);
            }
        } catch (NoUnitPriceDefinedException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(OSDebrisWaterPanelSync.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Por favor informe um valor válido para o volume total.", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (InvalidOSNumberException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private Payment doPayment() {
        Payment payment = null;
        PaymentPiece paymentPiece = null;
        if (paymentCB.getSelectedIndex() != Util.FIRST_INDEX) {
            payment = new Payment();
            paymentPiece = new PaymentPiece();
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
        }
        return payment;
    }

    private Set<ServiceItem> getServicesItens() throws NoUnitPriceDefinedException, MyHibernateException {
        Set<ServiceItem> serviceItens = new HashSet<ServiceItem>();
        for (int i = 0; i < serviceTable.getRowCount(); i++) {
            try {
                ServiceItem si = new ServiceItem();
                String qnt = serviceTable.getValueAt(i, serviceTable.getColumn(Util.COLUMN_QNT_NAME).getModelIndex()).toString();
                if (qnt.equals("")) {
                    si.setQuantity(1);
                } else {
                    si.setQuantity(Integer.parseInt(qnt));
                }
                String unitPrice = serviceTable.getValueAt(i, serviceTable.getColumn(Util.COLUMN_UNIT_PRICE_NAME).getModelIndex()).toString();
                if (unitPrice.equals("") && qnt.equals("")) {
                    continue;
                } else if (unitPrice.equals("")) {
                    throw new NoUnitPriceDefinedException(serviceTable.getValueAt(i, serviceTable.getColumn(Util.COLUMN_SERVICE_NAME).getModelIndex()).toString());
                }
                si.setUnitPrice(nf.parse(unitPrice).doubleValue());
                Service service = facade.loadService(ServiceType.get(serviceTable.getValueAt(i, serviceTable.getColumn(Util.COLUMN_SERVICE_NAME).getModelIndex()).toString()));
                si.setService(service);
                serviceItens.add(si);
            } catch (ParseException ex) {
                Logger.getLogger(OSDebrisWaterPanelSync.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return serviceItens;
    }

    private void initComponents() {
        servicesBG = new javax.swing.ButtonGroup();
        clientPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        codeLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        searchNameCB = new javax.swing.JComboBox();
        codeTF = new javax.swing.JTextField();
        phoneTF = new TextField(TextField.TYPE_PHONE);
        searchClientBtn = new javax.swing.JButton();
        regClientBtn = new javax.swing.JButton();
        servicePanel = new javax.swing.JPanel();
        serviceTablePanel = new javax.swing.JPanel();
        serviceJScrollPane = new javax.swing.JScrollPane();
        serviceTable = new javax.swing.JTable();
        debrisAndUnblockRB = new javax.swing.JRadioButton();
        waterRB = new javax.swing.JRadioButton();
        totalPriceLabel = new javax.swing.JLabel();
        totalPriceTF = new TextField(TextField.TYPE_CURRENCY);
        paymentTypeLabel = new javax.swing.JLabel();
        paymentCB = new javax.swing.JComboBox();
        discountLabel = new javax.swing.JLabel();
        discountTF = new TextField(TextField.TYPE_CURRENCY);
        receiveLB = new javax.swing.JLabel();
        dateJXDP = new org.jdesktop.swingx.JXDatePicker();
        capacityLabel = new javax.swing.JLabel();
        capacityTF = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        printServiceValueCBX = new javax.swing.JCheckBox();
        numberOSLabel = new javax.swing.JLabel();
        numberOSTF = new TextField(TextField.TYPE_DEFAULT);
        emissionDateLabel = new javax.swing.JLabel();
        previsionDateLabel = new javax.swing.JLabel();
        emissionDateJXDP = new org.jdesktop.swingx.JXDatePicker();
        emissionDateJXDP.setDateInMillis(Facade.getInstance().getCurrentDate().getTimeInMillis());
        previsionDateJXDP = new org.jdesktop.swingx.JXDatePicker();
        previsionDateJXDP.setDateInMillis(Facade.getInstance().getCurrentDate().getTimeInMillis());
        obsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        obsOSSTA = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        obsClientTA = new javax.swing.JTextArea();
        registerBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Expedir Ordem de Serviço - DETRITOS & DESENTUPIMENTO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        clientPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Dados do Cliente "));
        nameLabel.setText("Nome:");
        codeLabel.setText("Código:");
        phoneLabel.setText("Telefone:");
        searchNameCB.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchNameCBItemStateChanged(evt);
            }
        });
        codeTF.setEditable(false);
        codeTF.setEnabled(false);
        searchClientBtn.setText("Procurar");
        searchClientBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchClientBtnActionPerformed(evt);
            }
        });
        regClientBtn.setText("Cadastrar");
        regClientBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regClientBtnActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout clientPanelLayout = new javax.swing.GroupLayout(clientPanel);
        clientPanel.setLayout(clientPanelLayout);
        clientPanelLayout.setHorizontalGroup(clientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(clientPanelLayout.createSequentialGroup().addContainerGap().addComponent(nameLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(searchNameCB, 0, 0, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(phoneLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(phoneTF, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(codeLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(codeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(searchClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(regClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        clientPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { regClientBtn, searchClientBtn });
        clientPanelLayout.setVerticalGroup(clientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(clientPanelLayout.createSequentialGroup().addGroup(clientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(clientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(nameLabel).addComponent(searchNameCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(phoneLabel).addComponent(phoneTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(codeLabel).addComponent(codeTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(searchClientBtn)).addComponent(regClientBtn, javax.swing.GroupLayout.Alignment.CENTER)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        servicePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Dados da Ordem de Serviço"));
        serviceTablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Serviços "));
        serviceTable.setModel(new MyTableModel());
        serviceTable.setColumnSelectionAllowed(true);
        serviceTable.getTableHeader().setReorderingAllowed(false);
        serviceJScrollPane.setViewportView(serviceTable);
        serviceTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        serviceTable.getColumn(Util.COLUMN_QNT_NAME).setCellEditor(Util.getNumericCellEditor(serviceTable, true, totalPriceTF));
        serviceTable.getColumn(Util.COLUMN_UNIT_PRICE_NAME).setCellEditor(Util.getCurrencyCellEditor(serviceTable, true, totalPriceTF));
        serviceTable.getColumn(Util.COLUMN_SUBTOTAL_NAME).setCellEditor(new DefaultCellEditor(new TextField(TextField.TYPE_CURRENCY)));
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
        servicesBG.add(debrisAndUnblockRB);
        debrisAndUnblockRB.setSelected(true);
        debrisAndUnblockRB.setText("DETRITOS & DESENTUPIMENTO");
        debrisAndUnblockRB.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debrisAndUnblockRBActionPerformed(evt);
            }
        });
        servicesBG.add(waterRB);
        waterRB.setText("CARRADA DE ÁGUA");
        waterRB.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                waterRBStateChanged(evt);
            }
        });
        waterRB.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                waterRBActionPerformed(evt);
            }
        });
        totalPriceLabel.setText("Total:");
        paymentTypeLabel.setText("Forma de Pagamento:");
        paymentCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<SELECIONE A FORMA DE PAGAMENTO>" }));
        paymentCB.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                paymentCBItemStateChanged(evt);
            }
        });
        discountLabel.setText("Desconto:");
        receiveLB.setText("Recebimento:");
        receiveLB.setEnabled(false);
        dateJXDP.setEnabled(false);
        capacityLabel.setText("‣ VOLUME TOTAL(Litros):");
        capacityLabel.setVisible(false);
        capacityTF.setEditable(true);
        capacityTF.setModel(new javax.swing.DefaultComboBoxModel(DefaultCapacity.values()));
        capacityTF.setVisible(false);
        ((javax.swing.JTextField) capacityTF.getEditor().getEditorComponent()).setDocument(gui.TextField.realTF());
        printServiceValueCBX.setSelected(true);
        printServiceValueCBX.setText("Imprimir valor");
        printServiceValueCBX.setToolTipText("");
        javax.swing.GroupLayout serviceTablePanelLayout = new javax.swing.GroupLayout(serviceTablePanel);
        serviceTablePanel.setLayout(serviceTablePanelLayout);
        serviceTablePanelLayout.setHorizontalGroup(serviceTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(serviceTablePanelLayout.createSequentialGroup().addContainerGap().addGroup(serviceTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(serviceJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1061, Short.MAX_VALUE).addGroup(serviceTablePanelLayout.createSequentialGroup().addComponent(debrisAndUnblockRB).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(waterRB).addGap(18, 18, 18).addComponent(capacityLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(capacityTF, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 474, Short.MAX_VALUE)).addGroup(serviceTablePanelLayout.createSequentialGroup().addComponent(paymentTypeLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(paymentCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(receiveLB).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(dateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE).addGap(23, 23, 23).addComponent(printServiceValueCBX).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(discountLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(discountTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(totalPriceLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(totalPriceTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(0, 0, 0)));
        serviceTablePanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { discountTF, totalPriceTF });
        serviceTablePanelLayout.setVerticalGroup(serviceTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(serviceTablePanelLayout.createSequentialGroup().addGroup(serviceTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(serviceTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(waterRB).addComponent(capacityLabel).addComponent(capacityTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(serviceTablePanelLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(debrisAndUnblockRB))).addGap(9, 9, 9).addComponent(serviceJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE).addGroup(serviceTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(paymentTypeLabel).addComponent(discountLabel).addComponent(totalPriceTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(totalPriceLabel).addComponent(discountTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(paymentCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(receiveLB).addComponent(dateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(printServiceValueCBX)).addContainerGap()));
        numberOSLabel.setText("Número:");
        emissionDateLabel.setText("Data de emissão:");
        previsionDateLabel.setText("Data de Previsão/Execução:");
        javax.swing.GroupLayout servicePanelLayout = new javax.swing.GroupLayout(servicePanel);
        servicePanel.setLayout(servicePanelLayout);
        servicePanelLayout.setHorizontalGroup(servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(servicePanelLayout.createSequentialGroup().addContainerGap().addGroup(servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(serviceTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(servicePanelLayout.createSequentialGroup().addComponent(numberOSLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(numberOSTF, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(emissionDateLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(emissionDateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(previsionDateLabel).addGap(6, 6, 6).addComponent(previsionDateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        servicePanelLayout.setVerticalGroup(servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(servicePanelLayout.createSequentialGroup().addGroup(servicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(numberOSLabel).addComponent(numberOSTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(emissionDateLabel).addComponent(previsionDateLabel).addComponent(emissionDateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(previsionDateJXDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(serviceTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        obsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Adicionais"));
        obsOSSTA.setColumns(20);
        obsOSSTA.setRows(5);
        jScrollPane1.setViewportView(obsOSSTA);
        jLabel1.setText("Observações da OS: ");
        jLabel2.setText("Observações do cliente:");
        obsClientTA.setColumns(20);
        obsClientTA.setEditable(false);
        obsClientTA.setRows(5);
        jScrollPane2.setViewportView(obsClientTA);
        javax.swing.GroupLayout obsPanelLayout = new javax.swing.GroupLayout(obsPanel);
        obsPanel.setLayout(obsPanelLayout);
        obsPanelLayout.setHorizontalGroup(obsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(obsPanelLayout.createSequentialGroup().addGroup(obsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(obsPanelLayout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, obsPanelLayout.createSequentialGroup().addGap(24, 24, 24).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE))).addContainerGap()));
        obsPanelLayout.setVerticalGroup(obsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(obsPanelLayout.createSequentialGroup().addContainerGap().addGroup(obsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(obsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        registerBtn.setText("Sincronizar");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });
        clearBtn.setText("Limpar Tela");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });
        backBtn.setText("«« Voltar");
        backBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(clientPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(servicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(obsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { backBtn, clearBtn, registerBtn });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(clientPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(servicePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(obsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(11, 11, 11).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(clearBtn).addComponent(registerBtn).addComponent(backBtn)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (client == null) {
            JOptionPane.showMessageDialog(instance, MSG_3);
            searchNameCB.requestFocusInWindow();
            return;
        }
        if (paymentCB.getSelectedIndex() == Util.FIRST_INDEX) {
            JOptionPane.showMessageDialog(mainFrame, MSG_REQUIRED_PAYMENTTYPE, TITLE_WARNING, JOptionPane.INFORMATION_MESSAGE);
            paymentCB.requestFocusInWindow();
            return;
        }
        if (numberOSTF.getText() == null || numberOSTF.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(mainFrame, MSG_REQUIRED_OSNUMBER, TITLE_WARNING, JOptionPane.INFORMATION_MESSAGE);
            numberOSTF.requestFocusInWindow();
            return;
        }
        registerServiceOrder();
    }

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {
        clearAll();
    }

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.backPanel();
    }

    private void regClientBtnActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.showPanel(RegClientPanel.getInstance(instance));
    }

    private void searchClientBtnActionPerformed(java.awt.event.ActionEvent evt) {
        mainFrame.showPanel(SearchClientPanel.getInstance(instance));
    }

    private void searchNameCBItemStateChanged(java.awt.event.ItemEvent evt) {
        Util.clearAllFields(clientPanel);
        String clientName = evt.getItem().toString();
        if (!clientName.equals(CLIENT_CB_INTIAL_MSG)) {
            try {
                client = facade.loadClient(clientName);
                fillInfoClientFields();
            } catch (MyHibernateException ex) {
                Util.errorSQLPane(clientPanel, ex);
                Logger.getLogger(OSDebrisWaterPanelSync.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            client = null;
        }
    }

    private void discountTFKeyReleased(java.awt.event.KeyEvent evt) {
        try {
            if (Character.isDigit(evt.getKeyChar())) {
                applyDiscount();
            }
            if (discountTF.getText().equals("")) {
                discountTF.setText(Util.getFormattedCurrency(0.0));
                totalPriceTF.setText(Util.getFormattedCurrency(Util.calculateTotal(serviceTable, Util.COLUMN_SUBTOTAL_NAME)));
            }
        } catch (NumberFormatException e) {
        }
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

    private void waterRBActionPerformed(java.awt.event.ActionEvent evt) {
        setModel(serviceTable);
    }

    private void waterRBStateChanged(javax.swing.event.ChangeEvent evt) {
        if (waterRB.isSelected()) {
            capacityTF.setVisible(true);
            capacityLabel.setVisible(true);
            capacityTF.setSelectedIndex(1);
        } else {
            capacityTF.setVisible(false);
            capacityLabel.setVisible(false);
        }
    }

    private void debrisAndUnblockRBActionPerformed(java.awt.event.ActionEvent evt) {
        setModel(serviceTable);
    }

    private void applyDiscount() {
        double discount = 0.0;
        try {
            discount = nf.parse(discountTF.getText()).doubleValue();
        } catch (ParseException e) {
            discount = 0.0;
        }
        double total = Util.calculateTotal(serviceTable, Util.COLUMN_SUBTOTAL_NAME);
        if (discount <= total) {
            total -= discount;
            totalPriceTF.setText(NumberFormat.getCurrencyInstance().format(total));
        } else {
            JOptionPane.showMessageDialog(mainFrame, "O valor do desconto deve ser menor que o valor total.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setModel(JTable table) {
        if (debrisAndUnblockRB.isSelected()) {
            table.setModel(debrisServiceModel);
        } else {
            table.setModel(waterServiceModel);
        }
        table.getColumn(Util.COLUMN_QNT_NAME).setCellEditor(Util.getNumericCellEditor(serviceTable, true, totalPriceTF));
        table.getColumn(Util.COLUMN_UNIT_PRICE_NAME).setCellEditor(Util.getCurrencyCellEditor(serviceTable, true, totalPriceTF));
        table.getColumn(Util.COLUMN_SUBTOTAL_NAME).setCellEditor(new DefaultCellEditor(new TextField(TextField.TYPE_CURRENCY)));
        clearTable();
        totalPriceTF.setText("");
    }

    private javax.swing.JButton backBtn;

    private javax.swing.JLabel capacityLabel;

    private javax.swing.JComboBox capacityTF;

    private javax.swing.JButton clearBtn;

    private javax.swing.JPanel clientPanel;

    private javax.swing.JLabel codeLabel;

    private javax.swing.JTextField codeTF;

    private org.jdesktop.swingx.JXDatePicker dateJXDP;

    private javax.swing.JRadioButton debrisAndUnblockRB;

    private javax.swing.JLabel discountLabel;

    private gui.TextField discountTF;

    private org.jdesktop.swingx.JXDatePicker emissionDateJXDP;

    private javax.swing.JLabel emissionDateLabel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JLabel nameLabel;

    private javax.swing.JLabel numberOSLabel;

    private javax.swing.JTextField numberOSTF;

    private javax.swing.JTextArea obsClientTA;

    private javax.swing.JTextArea obsOSSTA;

    private javax.swing.JPanel obsPanel;

    private javax.swing.JComboBox paymentCB;

    private javax.swing.JLabel paymentTypeLabel;

    private javax.swing.JLabel phoneLabel;

    private gui.TextField phoneTF;

    private org.jdesktop.swingx.JXDatePicker previsionDateJXDP;

    private javax.swing.JLabel previsionDateLabel;

    private javax.swing.JCheckBox printServiceValueCBX;

    private javax.swing.JLabel receiveLB;

    private javax.swing.JButton regClientBtn;

    private javax.swing.JButton registerBtn;

    private javax.swing.JButton searchClientBtn;

    private javax.swing.JComboBox searchNameCB;

    private javax.swing.JScrollPane serviceJScrollPane;

    private javax.swing.JPanel servicePanel;

    private javax.swing.JTable serviceTable;

    private javax.swing.JPanel serviceTablePanel;

    private javax.swing.ButtonGroup servicesBG;

    private javax.swing.JLabel totalPriceLabel;

    private gui.TextField totalPriceTF;

    private javax.swing.JRadioButton waterRB;

    public void refresh() {
        try {
            fillFields();
            printServiceValueCBX.setSelected(true);
            searchNameCB.requestFocusInWindow();
            dateJXDP.setDateInMillis(facade.getCurrentDate().getTimeInMillis());
            emissionDateJXDP.setDateInMillis(facade.getCurrentDate().getTimeInMillis());
            previsionDateJXDP.setDateInMillis(facade.getCurrentDate().getTimeInMillis());
        } catch (MyHibernateException ex) {
            Util.errorSQLPane(mainFrame, ex);
            Logger.getLogger(OSDebrisWaterPanelSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearAll() {
        Util.clearAllFields(instance);
        searchNameCB.setSelectedIndex(Util.FIRST_INDEX);
        paymentCB.setSelectedIndex(Util.FIRST_INDEX);
        dateJXDP.setDateInMillis(facade.getCurrentDate().getTimeInMillis());
        emissionDateJXDP.setDateInMillis(facade.getCurrentDate().getTimeInMillis());
        previsionDateJXDP.setDateInMillis(facade.getCurrentDate().getTimeInMillis());
        clearTable();
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
}
