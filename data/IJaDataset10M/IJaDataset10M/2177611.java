package net.sourceforge.tile3d.view.panel.customer;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.sourceforge.tile3d.model.Customer;
import net.sourceforge.tile3d.socket.CustomerInfomation;
import net.sourceforge.tile3d.view.main.MainFrame;
import net.sourceforge.tile3d.view.util.FunctionUtil;
import net.sourceforge.tile3d.view.util.MessageError;
import net.sourceforge.tile3d.view.util.MessageUtil;

public class CustomerInfoFrame extends JFrame {

    JLabel jLabel1;

    JTextField fullname;

    JLabel jLabel2;

    JTextField address;

    JLabel jLabel3;

    JTextField phone;

    JLabel jLabel4;

    JTextField email;

    JLabel jLabel5;

    JTextField fax;

    JPanel jPanel1 = new JPanel();

    JButton okButton;

    JButton cancelButton;

    private MainFrame m_mainFrame;

    public CustomerInfoFrame(MainFrame mainFrame) {
        super("Thông tin khách hàng");
        m_mainFrame = mainFrame;
        init();
    }

    private void init() {
        getContentPane().setLayout(null);
        setSize(500, 300);
        jLabel1 = new JLabel();
        fullname = new JTextField();
        jLabel2 = new JLabel();
        address = new JTextField();
        jLabel3 = new JLabel();
        phone = new JTextField();
        jLabel4 = new JLabel();
        email = new JTextField();
        jLabel5 = new JLabel();
        fax = new JTextField();
        jPanel1 = new JPanel();
        okButton = new JButton("Chấp nhận");
        cancelButton = new JButton();
        jLabel1.setText("Họ tên");
        jLabel1.setBounds(new Rectangle(25, 22, 47, 14));
        jLabel3.setText("Số điện thoại");
        jLabel3.setBounds(new Rectangle(25, 82, 82, 14));
        phone.setBounds(new Rectangle(134, 82, 309, 19));
        jLabel4.setText("Email");
        jLabel4.setBounds(new Rectangle(25, 110, 34, 14));
        email.setBounds(new Rectangle(135, 113, 229, 19));
        jLabel5.setText("Fax");
        jLabel5.setBounds(new Rectangle(25, 145, 34, 14));
        fax.setBounds(new Rectangle(136, 145, 152, 19));
        jPanel1.setBounds(new Rectangle(25, 211, 420, 40));
        jPanel1.setLayout(null);
        jLabel2.setText("Địa chỉ");
        jLabel2.setBounds(new Rectangle(25, 51, 56, 14));
        fullname.setBounds(new Rectangle(135, 22, 153, 19));
        address.setBounds(new Rectangle(134, 54, 307, 19));
        okButton.setBounds(new Rectangle(124, 194, 92, 23));
        cancelButton.setBounds(new Rectangle(276, 194, 73, 23));
        cancelButton.setText("Hủy bỏ");
        this.getContentPane().add(jLabel3);
        this.getContentPane().add(fullname);
        this.getContentPane().add(jLabel2);
        this.getContentPane().add(phone);
        this.getContentPane().add(jLabel4);
        this.getContentPane().add(email);
        this.getContentPane().add(jLabel5);
        this.getContentPane().add(fax);
        this.getContentPane().add(jPanel1);
        this.getContentPane().add(address);
        this.getContentPane().add(jLabel1);
        this.getContentPane().add(okButton);
        this.getContentPane().add(cancelButton);
        setVisible(true);
    }

    public void addActionButton() {
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent p_arg0) {
                Customer customer = getCustomer();
                MessageError error = new MessageError();
                FunctionUtil.checkCustomerInfo(customer, error);
                if (error.size() == 0) {
                    getMainFrame().sendInfo(customer);
                } else {
                    MessageUtil.announce(error);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent p_arg0) {
                int response = MessageUtil.askQuestion("Bạn có muốn thoát?");
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                dispose();
            }
        });
    }

    public CustomerInfoFrame(CustomerInfomation p_infomation, boolean p_enabled) {
        init();
        fullname.setText(p_infomation.getCustomer().getFullName());
        phone.setText(p_infomation.getCustomer().getPhone());
        email.setText(p_infomation.getCustomer().getEmail());
        fax.setText(p_infomation.getCustomer().getFax());
        address.setText(p_infomation.getCustomer().getAddress());
        if (!p_enabled) {
            fullname.setEditable(false);
            email.setEditable(false);
            fax.setEditable(false);
            phone.setEditable(false);
            address.setEditable(false);
        }
        okButton.setText("In");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent p_e) {
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent p_arg0) {
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new CustomerInfoFrame(null);
    }

    public Customer getCustomer() {
        Customer customer = new Customer();
        customer.setAddress(address.getText());
        customer.setEmail(email.getText());
        customer.setFax((fax.getText()));
        customer.setFullName(fullname.getText());
        customer.setPhone(phone.getText());
        System.out.println(customer);
        return customer;
    }

    public MainFrame getMainFrame() {
        return m_mainFrame;
    }

    public void setMainFrame(MainFrame p_mainFrame) {
        m_mainFrame = p_mainFrame;
    }
}
