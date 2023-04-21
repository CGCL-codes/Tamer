package org.mandarax.examples.crm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import org.apache.log4j.BasicConfigurator;
import org.mandarax.util.ProofAnalyzer;

/**
 * A swing based interface for the discount calculator.
 * The interface is actually a panel (in order to use it in an applet),
 * but the main method will open it wrapped in a frame.
 * @see org.mandarax.examples.crm.DiscountCalculator
 * @author <A HREF="mailto:mandarax@jbdietrich.com">Jens Dietrich</A>
 * @version 3.3.1 <9 August 2004>
 * @since 1.2
 */
public class DiscountCalculatorView extends JPanel {

    private JComboBox cbxCustomers = null;

    private JLabel labResult = new JLabel("press \"calculate\"");

    private JList listExplanation = new JList(new DefaultListModel());

    private JList listKB = new JList(new DefaultListModel());

    private JButton butCalculate = new JButton("calculate");

    private JCheckBox checkAlias = new JCheckBox("use alias");

    private JTable tabTransactions = null;

    private KBServer kbserv = new KBServer();

    private DiscountCalculator calc = new DiscountCalculator();

    private Customer selectedCustomer = null;

    private boolean useAlias = true;

    class TransactionTableModel extends javax.swing.table.AbstractTableModel {

        private java.util.List transactions;

        TransactionTableModel(java.util.List t) {
            super();
            transactions = t;
        }

        public int getRowCount() {
            return transactions.size();
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int rowIndex, int colIndex) {
            Transaction t = (Transaction) transactions.get(rowIndex);
            if (colIndex == 0) {
                return NumberFormat.getCurrencyInstance(Locale.US).format(t.getAmount());
            }
            if (colIndex == 1) {
                return t.getKindOfPayment().toString();
            }
            if (colIndex == 2) {
                DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
                return df.format(t.getTimeStamp().getTime());
            }
            return null;
        }

        public String getColumnName(int index) {
            if (index == 0) {
                return "amount ($)";
            }
            if (index == 1) {
                return "payment";
            }
            if (index == 2) {
                return "date";
            }
            return "?";
        }
    }

    /**
     * Constructor.
     */
    public DiscountCalculatorView() {
        super();
        initialize();
    }

    /**
     * Calculate the discount for the selected customer and display it.
     */
    private void calculate() {
        calc.calculateDiscount(selectedCustomer, kbserv.getKB());
        displayResult();
        butCalculate.setEnabled(false);
    }

    /**
     * Display the selected customer.
     * In particular, display his transactions.
     */
    private void displayCustomer() {
        java.util.List transactions = (selectedCustomer == null) ? new Vector() : DB.getTransactions(selectedCustomer.getId());
        tabTransactions.setModel(new TransactionTableModel(transactions));
        TableColumn col = tabTransactions.getColumn("amount ($)");
        col.setWidth(100);
        col = tabTransactions.getColumn("payment");
        col.setWidth(150);
        col = tabTransactions.getColumn("date");
        col.setWidth(150);
    }

    /**
     * Display the knowledge base.
     */
    private void displayKB() {
        DefaultListModel listModel = (DefaultListModel) listKB.getModel();
        listModel.removeAllElements();
        for (Iterator it = kbserv.getKB().getClauseSets().iterator(); it.hasNext(); ) {
            listModel.addElement(useAlias ? kbserv.getAlias(it.next()) : it.next());
        }
    }

    /**
     * Display the result calculated.
     */
    private void displayResult() {
        labResult.setText(calc.getDiscount().toString());
        if (calc.getDerivation() == null) {
            return;
        }
        DefaultListModel listModel = (DefaultListModel) listExplanation.getModel();
        Collection usedKnowledge = ProofAnalyzer.getAppliedClauses(calc.getDerivation());
        listModel.removeAllElements();
        for (Iterator it = usedKnowledge.iterator(); it.hasNext(); ) {
            listModel.addElement(useAlias ? kbserv.getAlias(it.next()) : it.next());
        }
    }

    /**
     * Initialize the events.
     */
    private void initEvents() {
        cbxCustomers.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                selectedCustomer = (Customer) e.getItem();
                if (selectedCustomer != null) {
                    butCalculate.setEnabled(true);
                }
                displayCustomer();
                reset();
            }
        });
        butCalculate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });
        checkAlias.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                useAlias = checkAlias.isSelected();
                displayResult();
                displayKB();
            }
        });
    }

    /**
     * Initialize the object.
     */
    private void initialize() {
        JPanel north = new JPanel(new GridLayout(2, 1, 3, 3));
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel outputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Vector custList = new Vector();
        custList.addAll(DB.getCustomers().values());
        cbxCustomers = new JComboBox(custList);
        cbxCustomers.setSelectedIndex(0);
        selectedCustomer = (Customer) custList.get(0);
        inputPanel.add(new JLabel("Calculate discount for: "));
        inputPanel.add(cbxCustomers);
        inputPanel.add(butCalculate);
        JLabel labCalculated = new JLabel("   Calculated discount: ");
        labCalculated.setFont(new Font("dialog.bold", 1, 14));
        labCalculated.setForeground(Color.black);
        outputPanel.add(labCalculated);
        labResult.setForeground(Color.red);
        labResult.setFont(new Font("dialog.bold", 1, 14));
        outputPanel.add(labResult);
        outputPanel.setBorder(BorderFactory.createEtchedBorder());
        outputPanel.setBackground(Color.white);
        north.add(inputPanel);
        north.add(outputPanel);
        JTabbedPane notebook = new JTabbedPane(JTabbedPane.TOP);
        JPanel explanationPanel = new JPanel(new BorderLayout(5, 5));
        explanationPanel.add(new JScrollPane(listExplanation), BorderLayout.CENTER);
        explanationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        notebook.add("used rules", explanationPanel);
        JPanel tabPanel = new JPanel(new GridLayout(1, 1));
        tabTransactions = new JTable(new TransactionTableModel(DB.getTransactions(selectedCustomer.getId())));
        tabTransactions.sizeColumnsToFit(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tabPanel.add(new JScrollPane(tabTransactions));
        notebook.add("user transactions", tabPanel);
        JPanel kbPanel = new JPanel(new GridLayout(1, 1));
        kbPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        kbPanel.add(new JScrollPane(listKB));
        notebook.add("all rules", kbPanel);
        displayKB();
        JPanel centralPanel = new JPanel(new BorderLayout(5, 5));
        centralPanel.add(new JLabel("The calculation is based on the following rules and data:", JLabel.CENTER), BorderLayout.NORTH);
        centralPanel.add(notebook, BorderLayout.CENTER);
        JPanel aliasPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        checkAlias.setSelected(useAlias);
        aliasPanel.add(checkAlias);
        centralPanel.add(aliasPanel, BorderLayout.SOUTH);
        setLayout(new BorderLayout(5, 5));
        add(north, BorderLayout.NORTH);
        add(centralPanel, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        initEvents();
    }

    /**
     * Application entry point.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        JFrame wrapper = new JFrame("Mandarax discount calculator");
        wrapper.getContentPane().setLayout(new GridLayout(1, 1));
        DiscountCalculatorView view = new DiscountCalculatorView();
        wrapper.getContentPane().add(view);
        wrapper.setSize(600, 400);
        wrapper.setLocation(200, 100);
        wrapper.setVisible(true);
        WindowAdapter wa = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        wrapper.addWindowListener(wa);
    }

    /**
     * Reset the interface (if a new customer has been selected).
     */
    private void reset() {
        labResult.setText("press \"calculate\"");
        listExplanation.setModel(new DefaultListModel());
    }
}
