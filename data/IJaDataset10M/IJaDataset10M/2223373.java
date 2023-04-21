package registerUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import cashRegister.*;

@SuppressWarnings("serial")
public class UserModifyCard extends GUIMode implements ListSelectionListener {

    private JTextField name;

    private JTextField ID;

    private JTextField balance;

    private JTextField sugarLevel;

    private JTextField message;

    private JButton applyButton;

    private JButton backButton;

    private JList userList;

    private DefaultListModel userListModel = new DefaultListModel();

    private ArrayList<Customer> userListArr = new ArrayList<Customer>(400);

    private JScrollPane userListScroller;

    private CashRegister register;

    private RootGUI parent;

    private GridBagConstraints gbc = new GridBagConstraints();

    private Customer currUser = null;

    public UserModifyCard(CashRegister register, RootGUI parent) {
        super();
        this.register = register;
        this.parent = parent;
        userList = new JList(userListModel);
        userList.setDoubleBuffered(true);
        userList.addKeyListener(parent);
        userList.setFont(new Font("Courier", userList.getFont().getStyle(), userList.getFont().getSize() + 3));
        userListScroller = new JScrollPane(userList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        userList.addListSelectionListener(this);
        name = new JTextField(20);
        name.setBorder(BorderFactory.createEmptyBorder());
        name.setHorizontalAlignment(JTextField.RIGHT);
        name.setEditable(true);
        name.addKeyListener(parent);
        ID = new JTextField(20);
        ID.setBorder(BorderFactory.createEmptyBorder());
        ID.setHorizontalAlignment(JTextField.RIGHT);
        ID.setEditable(false);
        ID.addKeyListener(parent);
        balance = new JTextField(10);
        balance.setBorder(BorderFactory.createEmptyBorder());
        balance.setHorizontalAlignment(JTextField.RIGHT);
        balance.setEditable(true);
        balance.addKeyListener(parent);
        sugarLevel = new JTextField(10);
        sugarLevel.setBorder(BorderFactory.createEmptyBorder());
        sugarLevel.setHorizontalAlignment(JTextField.RIGHT);
        sugarLevel.setEditable(true);
        sugarLevel.addKeyListener(parent);
        message = new JTextField(10);
        message.setBorder(BorderFactory.createEmptyBorder());
        message.setHorizontalAlignment(JTextField.LEFT);
        message.setEditable(false);
        message.addKeyListener(parent);
        applyButton = new JButton("Apply");
        applyButton.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 7));
        applyButton.setEnabled(true);
        applyButton.addKeyListener(parent);
        applyButton.addActionListener(this);
        backButton = new JButton("Back (Esc)");
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 7));
        backButton.setEnabled(true);
        backButton.addKeyListener(parent);
        backButton.addActionListener(this);
        addKeyListener(parent);
        userListScroller.addKeyListener(parent);
        drawGUI();
    }

    private void drawGUI() {
        setLayout(new GridBagLayout());
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.ipadx = 5;
        gbc.ipady = 5;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridheight = 5;
        gbc.gridwidth = 2;
        add(userListScroller, gbc);
        gbc.gridheight = 1;
        gbc.gridwidth = 40;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        add(new JLabel("Name: "), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(name, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel("ID: "), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(ID, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel("Balance: "), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(balance, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel("Sugar Level: "), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(sugarLevel, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        add(message, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.EAST;
        add(applyButton, gbc);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(backButton, gbc);
        validate();
        name.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == applyButton) {
            if (!Prompt.isParsableToDouble(balance.getText()) || !Prompt.isParsableToInt(sugarLevel.getText()) || name.getText().equals("")) {
                message.setForeground(Color.RED);
                message.setText("Illegal values!");
                return;
            }
            int newBal = (int) (Double.parseDouble(balance.getText()) * 100);
            int newSug = Integer.parseInt(sugarLevel.getText());
            if (newBal != currUser.getBalance()) currUser.setBalance(newBal);
            if (newSug != currUser.getSugar()) currUser.setSugar(newSug);
            if (!name.getText().equals(currUser.getName())) currUser.setName(name.getText());
            reload();
            message.setForeground(Color.BLACK);
            message.setText("User modified successfully!");
            validate();
        }
        if (e.getSource() == backButton) {
            try {
                Robot r = new Robot();
                r.keyPress(KeyEvent.VK_ESCAPE);
                r.keyRelease(KeyEvent.VK_ESCAPE);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
	 * Displays a user.
	 * @param c user to be displayed
	 */
    public void displayUser(Customer c) {
        if (c == null) {
            name.setText("");
            ID.setText("");
            balance.setText("");
            sugarLevel.setText("");
            return;
        }
        name.setText(c.getName());
        ID.setText(Long.toString(c.getID()));
        balance.setText(Final.formatMoneyString(c.getBalance(), false));
        sugarLevel.setText(Integer.toString(c.getSugar()));
    }

    /**
	 * Called right before switching to this card.
	 */
    @Override
    public void reload() {
        name.setText("");
        ID.setText("");
        balance.setText("");
        sugarLevel.setText("");
        message.setText("");
        message.setForeground(Color.BLACK);
        userListModel.clear();
        userListArr.clear();
        List<Customer> li = register.getCustomerList();
        Collections.sort(li);
        for (Customer e : li) {
            userListModel.addElement(e.getID() + "   " + e.getName());
            userListArr.add(e);
        }
        currUser = null;
        name.requestFocus();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == userList) {
            if (userList.getSelectedIndex() == -1) {
                displayUser(null);
                return;
            }
            currUser = userListArr.get(userList.getSelectedIndex());
            message.setText("");
            displayUser(currUser);
        }
    }
}
