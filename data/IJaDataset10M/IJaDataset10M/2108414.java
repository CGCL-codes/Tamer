package com.ken.dgking.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import com.ken.dgking.common.CommonFactory;
import com.ken.dgking.handler.CustomerServicesHandler;
import com.ken.dgking.handler.GoodsServicesHandler;
import com.ken.dgking.handler.SalesInServicesHandler;
import com.ken.dgking.model.Goods;
import com.ken.dgking.model.SalesIn;

public class SalesFrame extends JInternalFrame {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    JTextField ID_salestextField;

    JTextField ID_customerField;

    JTextField numberField;

    JComboBox paytypeComboBox;

    JTextField salestimeField;

    JTextField operaterField;

    JTextField goodsField;

    JTextField explainField;

    private double goodsPrices = 0;

    public SalesFrame() {
        super("���۵�", true, true, true, true);
        this.setBounds(0, 0, screenSize.width * 2 / 3, screenSize.height * 2 / 3);
        this.getContentPane().add(importgoods());
    }

    public JPanel importgoods() {
        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        JLabel ID_saleslable = new JLabel("����Ʊ��:");
        ID_salestextField = new JTextField(10);
        JLabel ID_privoderLabel = new JLabel("�ͻ����:");
        ID_customerField = new JTextField(10);
        JLabel numberLabel = new JLabel("����:");
        numberField = new JTextField(10);
        panel1.add(ID_saleslable);
        panel1.add(ID_salestextField);
        panel1.add(ID_privoderLabel);
        panel1.add(ID_customerField);
        panel1.add(numberLabel);
        panel1.add(numberField);
        JPanel panel2 = new JPanel();
        JLabel paytypeLabel = new JLabel("֧������:");
        paytypeComboBox = new JComboBox();
        paytypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "��ѡ��֧������", "�ֽ�", "���п�", "֧Ʊ" }));
        JLabel salestimeLabel = new JLabel("����ʱ��:");
        salestimeField = new JTextField(10);
        JLabel opreaterLabel = new JLabel("����Ա:");
        operaterField = new JTextField(10);
        panel2.add(paytypeLabel);
        panel2.add(paytypeComboBox);
        panel2.add(salestimeLabel);
        panel2.add(salestimeField);
        panel2.add(opreaterLabel);
        panel2.add(operaterField);
        JPanel panel3 = new JPanel();
        JScrollPane goodScrollPane = new JScrollPane();
        final JTable goodsTable = new JTable(new MyTableModel());
        goodsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel model = (ListSelectionModel) e.getSource();
                int index = model.getMaxSelectionIndex();
                goodsField.setText(goodsTable.getValueAt(index, 0).toString());
                goodsPrices = Double.valueOf(goodsTable.getValueAt(index, 8).toString());
            }
        });
        goodsTable.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 3 - 60, screenSize.height / 3));
        goodScrollPane.setViewportView(goodsTable);
        panel3.add(goodScrollPane);
        JPanel panel4 = new JPanel();
        JLabel goodsLabel = new JLabel("��Ʒ���:");
        goodsField = new JTextField(10);
        JLabel explainLabel = new JLabel("��Ʒע��:");
        explainField = new JTextField(20);
        JButton addButton = new JButton("���");
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
                ID_salestextField.setText("SI" + formate.format(date));
                formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                salestimeField.setText(formate.format(date));
                operaterField.setText(MainFrame.username);
                numberField.setText("");
                setEnableTrue();
            }
        });
        JButton salesButton = new JButton("����");
        salesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String salesInID = ID_salestextField.getText();
                String customerId = ID_customerField.getText();
                String numberStr = numberField.getText();
                String payType = paytypeComboBox.getSelectedItem().toString();
                String salesInTime = salestimeField.getText();
                String operator = operaterField.getText();
                String goodsID = goodsField.getText();
                String comment = explainField.getText();
                double price = 0;
                if (numberStr == null || numberStr.trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "��������Ʒ����", "����", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int number = 0;
                try {
                    number = Integer.valueOf(numberStr);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "��Ʒ���������Ϸ�", "����", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (paytypeComboBox.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "��ѡ��֧������", "����", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (customerId == null || customerId.trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "������ͻ����", "����", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (goodsID == null || goodsID.trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "��ѡ����Ʒ", "����", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                CustomerServicesHandler handler1 = CommonFactory.getCustomerServices();
                if (!handler1.isExited(customerId)) {
                    JOptionPane.showMessageDialog(null, "�ÿͻ���Ų����ڣ���˶���Ϣ", "����", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                price = goodsPrices * number;
                if (number <= 0) {
                    JOptionPane.showMessageDialog(null, "��Ʒ���������Ϸ�", "����", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                SalesIn salesIn = new SalesIn(salesInID, customerId, goodsID, payType, number, price, salesInTime, operator, comment);
                SalesInServicesHandler handler = CommonFactory.getSalesInServices();
                GoodsServicesHandler goodsServicesHandle = CommonFactory.getGoodsServices();
                int storageNum = goodsServicesHandle.getStorageNum(salesIn.getGoodsId());
                if (storageNum < number) {
                    JOptionPane.showMessageDialog(null, "�����������Ϊ" + storageNum + "������������", "����", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (handler.addSalesIn(salesIn)) {
                    JOptionPane.showMessageDialog(null, "���۵���ӳɹ���Ӧ����" + price + "Ԫ", "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
                    numberField.setText("");
                    setEnableFalse();
                } else {
                    JOptionPane.showMessageDialog(null, "���۵����ʧ�ܣ��밴Ҫ���������", "����", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        setEnableFalse();
        panel4.add(goodsLabel);
        panel4.add(goodsField);
        panel4.add(explainLabel);
        panel4.add(explainField);
        panel4.add(addButton);
        panel4.add(salesButton);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);
        return panel;
    }

    private void setEnableFalse() {
        ID_salestextField.setEnabled(false);
        ID_customerField.setEnabled(false);
        numberField.setEnabled(false);
        paytypeComboBox.setEnabled(false);
        salestimeField.setEnabled(false);
        operaterField.setEnabled(false);
        explainField.setEnabled(false);
        goodsField.setEnabled(false);
    }

    private void setEnableTrue() {
        ID_customerField.setEnabled(true);
        numberField.setEnabled(true);
        paytypeComboBox.setEnabled(true);
        explainField.setEnabled(true);
    }

    class MyTableModel extends AbstractTableModel {

        GoodsServicesHandler handler = CommonFactory.getGoodsServices();

        Vector<Goods> goodsVector = handler.getAllGoods();

        private String[] columnNames = { "��Ʒ���", "��Ʒ���", "���", "���", "��װ", "������", "��׼�ĺ�", "����", "�۸�", "��Ӧ�̱��" };

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return goodsVector.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            Goods goods = goodsVector.get(row);
            return goods.getGoodsValue(col);
        }

        @SuppressWarnings("unchecked")
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }
}
