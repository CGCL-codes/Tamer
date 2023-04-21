package model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import dataStruct.Customer;
import defination.Def;

public class CustomerModel implements Def {

    /**
	 * ���
	 */
    public int ID;

    /**
	 * ����
	 */
    public String name;

    public String nickName;

    /**
	 * ��ע
	 */
    public String note;

    /**
	 * �Ƿ��Ա
	 */
    public boolean isMember;

    private int sexID;

    /**
	 * 
	 */
    private Vector<ContactInfo> contactVec;

    private Customer customer;

    private CustomerModel thisInstance;

    private JTextField nameTextField;

    private JTextField nickNameTextField;

    private JRadioButton maleButton, femaleButton;

    private JCheckBox memberCheckBox;

    public CustomerModel(Customer customer) {
        thisInstance = this;
        this.customer = customer;
        loadData();
        contactVec = new Vector<ContactInfo>();
        contactVec.add(new ContactInfo(ContactInfo.METHOD_MOBILE, "aaaaa"));
        initView();
        saveData();
    }

    public CustomerModel() {
        thisInstance = this;
        customer = new Customer();
        initData();
        try {
            customer.CUSTOMER_NAME = name;
            customer.NICKNAME = nickName;
            customer.ISVIP = isMember ? "1" : "0";
            customer.SEX_ID = "" + (sexID + 1);
            customer.Save();
            ID = customer.CUSTOMER_ID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        contactVec = new Vector<ContactInfo>();
        contactVec.add(new ContactInfo(ContactInfo.METHOD_MOBILE, "aaaaa"));
        initView();
        saveData();
    }

    public String getStringByColume(int colume) {
        switch(colume) {
            case CUSTOMER_COLUME_ID:
                return "" + ID;
            case CUSTOMER_COLUME_NAME:
                return name;
            case CUSTOMER_COLUME_MEMBER:
                return isMember ? "��" : "��";
            default:
                return "";
        }
    }

    private JPanel editView;

    private static final String BORDER_STR[] = { "������", "��ϵ��ʽ" };

    private static final int BASIC_INFO = 0;

    private static final int CONTACT_INFO = BASIC_INFO + 1;

    private static final int OTHERS_INFO = CONTACT_INFO + 1;

    private static final int HISTORY_INFO = OTHERS_INFO + 1;

    private static final int PANE_NUM = BORDER_STR.length;

    private JComponent[] subPane = new JComponent[PANE_NUM];

    private static final int PREFERRED_WIDTH = 400;

    private void initView() {
        if (editView == null) {
            editView = new JPanel();
            scrollPane = new JScrollPane(editView);
            editView.setLayout(new GridBagLayout());
            for (int i = 0; i < PANE_NUM; i++) {
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = i;
                constraints.anchor = GridBagConstraints.NORTHWEST;
                switch(i) {
                    case BASIC_INFO:
                        subPane[i] = new BasicInfoPane();
                        break;
                    case CONTACT_INFO:
                        subPane[i] = new JPanel();
                        subPane[i].setLayout(new BorderLayout());
                        subPane[i].add(new JScrollPane(new ContractList()), BorderLayout.CENTER);
                        subPane[i].add(new ContactControlPane(), BorderLayout.SOUTH);
                        subPane[i].setPreferredSize(new Dimension(PREFERRED_WIDTH, 200));
                        break;
                    default:
                        subPane[i] = new JPanel();
                        subPane[i].setPreferredSize(new Dimension(100, 300));
                        break;
                }
                Border etched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
                Border titled = BorderFactory.createTitledBorder(etched, BORDER_STR[i]);
                subPane[i].setBorder(titled);
                editView.add(subPane[i], constraints);
            }
        }
    }

    private JScrollPane scrollPane;

    public JComponent getEditView() {
        return scrollPane;
    }

    public String getTabString() {
        return "�༭\"" + name + "\"��";
    }

    public void initData() {
        name = "δ����";
        nickName = "�ǳ�";
        isMember = false;
        sexID = 0;
    }

    public void saveData() {
        getDataFromView();
        customer.CUSTOMER_NAME = name;
        customer.NICKNAME = nickName;
        customer.ISVIP = isMember ? "1" : "0";
        customer.SEX_ID = "" + (sexID + 1);
        try {
            customer.Update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        name = customer.CUSTOMER_NAME;
        nickName = customer.NICKNAME;
        ID = customer.CUSTOMER_ID;
        sexID = customer.SEX_ID.equals("1") ? 0 : 1;
        isMember = customer.ISVIP.equals("1");
        try {
            customer.Update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            customer.Delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataFromView() {
        name = nameTextField.getText();
        nickName = nickNameTextField.getText();
        sexID = maleButton.isSelected() ? 0 : 1;
        isMember = memberCheckBox.isSelected();
    }

    public boolean checkForSaving() {
        return false;
    }

    class BasicInfoPane extends JPanel {

        private static final int PANE_HEIGHT = 300;

        private final String INFO_STR[] = { "����", "�ǳ�", "�Ա�", "��������", "�Ƿ��Ա", "��Ա��" };

        private BasicInfoPane() {
            super();
            this.setLayout(new GridBagLayout());
            for (int i = 0; i < 6; i++) {
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = i;
                constraints.anchor = GridBagConstraints.WEST;
                this.add(new JLabel(INFO_STR[i]), constraints);
            }
            for (int i = 0; i < 6; i++) {
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 1;
                constraints.gridy = i;
                constraints.anchor = GridBagConstraints.WEST;
                JComponent com = null;
                switch(i) {
                    case 0:
                        {
                            nameTextField = new JTextField(10);
                            nameTextField.setText("" + name);
                            nameTextField.addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    name = nameTextField.getText();
                                    CustomerManager.getInstance().renewTab(thisInstance);
                                }
                            });
                            com = nameTextField;
                        }
                        break;
                    case 1:
                        {
                            nickNameTextField = new JTextField(10);
                            nickNameTextField.setText("" + nickName);
                            nickNameTextField.addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    nickName = nickNameTextField.getText();
                                    CustomerManager.getInstance().renewTab(thisInstance);
                                }
                            });
                            com = nickNameTextField;
                        }
                        break;
                    case 2:
                        {
                            JPanel pane = new JPanel();
                            ButtonGroup group = new ButtonGroup();
                            maleButton = new JRadioButton("��", true);
                            group.add(maleButton);
                            femaleButton = new JRadioButton("Ů", false);
                            group.add(femaleButton);
                            pane.add(maleButton);
                            pane.add(femaleButton);
                            if (sexID == 0) {
                                maleButton.setSelected(true);
                            } else {
                                femaleButton.setSelected(true);
                            }
                            com = pane;
                        }
                        break;
                    case 3:
                        {
                            JPanel pane = new JPanel();
                            JComboBox yearBox = new JComboBox();
                            JComboBox monthBox = new JComboBox();
                            JComboBox dayBox = new JComboBox();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date());
                            int thisyear = calendar.get(Calendar.YEAR);
                            System.out.println("this year is" + thisyear);
                            for (int j = thisyear - 80; j < thisyear + 1; j++) {
                                yearBox.addItem("" + j);
                            }
                            for (int j = 0; j < 12; j++) {
                                monthBox.addItem("" + (j + 1));
                            }
                            for (int j = 0; j < 31; j++) {
                                dayBox.addItem("" + (j + 1));
                            }
                            pane.add(yearBox);
                            pane.add(new JLabel("��"));
                            pane.add(monthBox);
                            pane.add(new JLabel("�� "));
                            pane.add(dayBox);
                            pane.add(new JLabel("��"));
                            pane.add(new JLabel("  "));
                            com = pane;
                        }
                        break;
                    case 4:
                        memberCheckBox = new JCheckBox();
                        memberCheckBox.setSelected(isMember);
                        com = memberCheckBox;
                        break;
                    case 5:
                        com = new JLabel("xxxx xxxx xxxx xxxx");
                        break;
                }
                this.add(com, constraints);
            }
        }
    }

    private static final String[] COLUME_STR = { "��ϵ��ʽ", "��ϸ" };

    private static final String MOTHOD_STR[] = { "�ֻ����", "�̶��绰" };

    class ContractList extends JTable {

        private static final int PANE_HEIGHT = 300;

        private static final int PANE_WIDTH = 100;

        public ContractList() {
            super(new ContractModel());
            initColumes(COLUME_STR);
            this.setRowHeight(30);
        }

        private void initColumes(String[] columes) {
            DefaultTableColumnModel columeModel = new DefaultTableColumnModel();
            for (int i = 0; i < columes.length; i++) {
                TableColumn tc = new TableColumn(i);
                tc.setMinWidth(40);
                if (i == 0) {
                    tc.setMaxWidth(150);
                }
                if (i == 1) {
                    tc.setMaxWidth(350);
                }
                tc.setHeaderValue(columes[i]);
                System.out.println("columes[i]:" + columes[i]);
                columeModel.addColumn(tc);
            }
            JComboBox methodIDBox;
            methodIDBox = new JComboBox();
            for (int i = 0; i < MOTHOD_STR.length; i++) {
                methodIDBox.addItem(MOTHOD_STR[i]);
            }
            TableCellEditor contactMethodEditor = new DefaultCellEditor(methodIDBox);
            columeModel.getColumn(0).setCellEditor(contactMethodEditor);
            JTextField text;
            text = new JTextField(20);
            contactMethodEditor = new DefaultCellEditor(text);
            columeModel.getColumn(1).setCellEditor(contactMethodEditor);
            this.setColumnModel(columeModel);
            this.setVisible(true);
        }

        public boolean isCellEditable(int r, int c) {
            return true;
        }
    }

    class ContractModel extends AbstractTableModel {

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return contactVec.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return contactVec.elementAt(rowIndex).getValueColume(columnIndex);
        }
    }

    class ContactControlPane extends JPanel {

        private JButton addButton;

        private JButton eraseButton;

        public ContactControlPane() {
            super();
            addButton = new JButton("���");
            eraseButton = new JButton("ɾ��");
            this.add(addButton);
            this.add(eraseButton);
        }
    }
}
