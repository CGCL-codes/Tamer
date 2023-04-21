package com.spicesoft.clientobjects.samples.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class CustomerForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private Customer customer;

    private int rows = 0;

    private CellConstraints cc = new CellConstraints();

    private Random rnd = new Random(System.currentTimeMillis());

    public CustomerForm(Customer customer) {
        this.customer = customer;
        initUI();
    }

    private void initUI() {
        this.setTitle("Customer Information");
        JPanel cp = new JPanel();
        FormLayout layout = new FormLayout("5dlu, p, 5dlu, p:g, 5dlu", "3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu");
        cp.setLayout(layout);
        this.setContentPane(cp);
        addTextField(Customer.FIRST_NAME, "First Name");
        addTextField(Customer.LAST_NAME, "Last Name");
        addDateField(Customer.BIRTH_DATE, "Date of Birth");
        addCheckBox(Customer.CORPORATE_CUSTOMER, "Corporate Account");
        AddressPanel permanentAddressPanel = new AddressPanel(customer.getPermanentAddress(), "Permanent Address");
        this.getContentPane().add(permanentAddressPanel, cc.xyw(2, 2 * (++rows), 3));
        AddressPanel mailingAddressPanel = new AddressPanel(customer.getMailingAddress(), "Mailing Address");
        this.getContentPane().add(mailingAddressPanel, cc.xyw(2, 2 * (++rows), 3));
        JButton randomizeBtn = new JButton("Test Bindings");
        randomizeBtn.addActionListener(this.randomizeAction);
        randomizeBtn.setToolTipText("<html>To show that bean values are really bound to the UI<br>" + " elements, this button will randomize customer's first<br>" + " and last name, permanent address and invert corporate<br>" + " account field.</html>");
        this.getContentPane().add(randomizeBtn, cc.xy(2, 2 * (++rows)));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(this.closeAction);
        this.getContentPane().add(closeBtn, cc.xy(4, 2 * rows));
        JButton printBeanBtn = new JButton("Print Bean");
        printBeanBtn.addActionListener(this.printBeanAction);
        printBeanBtn.setToolTipText("<html>Displays the bean's current contents." + " As you change the contents of the form the bean's contents" + " should change automatically.</html>");
        this.getContentPane().add(printBeanBtn, cc.xy(2, 2 * (++rows)));
    }

    private void addTextField(String fieldName, String labelText) {
        PropertyAdapter adapter = new PropertyAdapter(customer, fieldName, true);
        JTextField field = BasicComponentFactory.createTextField(adapter, true);
        rows++;
        this.getContentPane().add(new JLabel(labelText), cc.xy(2, 2 * rows));
        this.getContentPane().add(field, cc.xy(4, 2 * rows));
    }

    private void addCheckBox(String fieldName, String labelText) {
        PropertyAdapter adapter = new PropertyAdapter(customer, fieldName, true);
        JCheckBox field = BasicComponentFactory.createCheckBox(adapter, labelText);
        rows++;
        this.getContentPane().add(new JLabel(labelText), cc.xy(2, 2 * rows));
        this.getContentPane().add(field, cc.xy(4, 2 * rows));
    }

    private void addDateField(String fieldName, String labelText) {
        PropertyAdapter adapter = new PropertyAdapter(customer, fieldName, true);
        JTextField field = BasicComponentFactory.createDateField(adapter);
        rows++;
        this.getContentPane().add(new JLabel(labelText), cc.xy(2, 2 * rows));
        this.getContentPane().add(field, cc.xy(4, 2 * rows));
    }

    private ActionListener randomizeAction = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            customer.setFirstName(randomString());
            customer.setLastName(randomString());
            customer.getPermanentAddress().setAddressLine1(randomString());
            customer.setCorporateCustomer(!customer.isCorporateCustomer());
        }
    };

    private ActionListener printBeanAction = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new JDialog(CustomerForm.this);
            JPanel cp = new JPanel();
            FormLayout layout = new FormLayout("5dlu, p, 5dlu", "3dlu, p, 3dlu");
            cp.setLayout(layout);
            dialog.setContentPane(cp);
            JTextArea printArea = new JTextArea(customer.toString() + "\n\n" + customer.fullName() + "\n\n" + customer.getBirthDate());
            printArea.setColumns(50);
            printArea.setRows(20);
            dialog.getContentPane().add(new JScrollPane(printArea), cc.xy(2, 2));
            dialog.pack();
            dialog.setVisible(true);
        }
    };

    private ActionListener closeAction = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            CustomerForm.this.dispose();
        }
    };

    private String randomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) ('a' + (rnd.nextInt() % 26)));
        }
        return sb.toString();
    }
}
