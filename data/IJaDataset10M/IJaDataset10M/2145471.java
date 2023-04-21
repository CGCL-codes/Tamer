package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.JAXBException;
import client.OCSF.*;
import client.shoppingCartFrame.*;
import client.shoppingCartFrame.Transaction.convertTransactionXML;

@SuppressWarnings("serial")
public class BillSysSeller extends JFrame implements BillSysIF, ActionListener {

    /**
	   * The default port to connect on.
	   */
    public static final int DEFAULT_PORT = 5555;

    private BillSysClient client;

    private ShoppingCartPanel cart;

    private ItemPickerPanel item;

    static StringReader reader;

    private convertTransactionXML convert;

    JPanel main = new JPanel();

    JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));

    /**
	   * Button to get information from the shopping cart
	   */
    private JButton placeOrder = new JButton("Place Order");

    /**
	   * Constructs an instance of the sensor.
	   *
	   * @param host The host to connect to.
	   * @param port The port to connect on.
	   */
    public void billsysSellerStarter(String host, int port) {
        try {
            client = new BillSysClient(host, port, this);
            if (client.isConnected()) {
                display("CONNECTED TO " + host);
            }
        } catch (Exception exception) {
            display("ERROR - Message could not be sent" + " to the server. DISCONNECTED.");
            System.exit(1);
        }
    }

    public void accept() {
        try {
            String message = convert.getWriter().toString();
            System.out.println(message);
            client.handleMessageFromClientUI(message);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public void display(String message) {
        reader = new StringReader(message);
        System.out.println("> " + message);
    }

    private void startGUI() {
        placeOrder.addActionListener(this);
        placeOrder.setPreferredSize(new Dimension(360, 40));
        cart = new ShoppingCartPanel(client);
        item = new ItemPickerPanel(reader);
        convert = new convertTransactionXML();
        main.setLayout(new GridLayout(1, 2, 2, 2));
        controls.add(cart);
        controls.add(placeOrder);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(1, 1));
        setTitle("BillSys 2.0 - client");
        setResizable(false);
        main.add(item);
        main.add(controls);
        getContentPane().add(main);
        setSize(800, 480);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == placeOrder) {
            if (!(cart.isEmpty())) {
                String orderNr = cart.getTransactionNr();
                int seller = cart.getSeller();
                int total = cart.getTotal();
                convert.setTransaction(orderNr, seller, total);
                convert.setTransactionItems(cart.doPlaceOrder());
                try {
                    convert.convertTransactionDetails();
                } catch (JAXBException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                    e.printStackTrace();
                }
                accept();
                cart.reset();
            } else {
                JOptionPane.showMessageDialog(this, "Shopping Cart empty!");
                return;
            }
        }
    }

    public static void main(String[] args) {
        BillSysSeller chat = new BillSysSeller();
        String host = "localhost";
        int port = 5555;
        chat.billsysSellerStarter(host, port);
        chat.startGUI();
    }
}
