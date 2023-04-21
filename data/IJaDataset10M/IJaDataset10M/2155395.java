package gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import database.objects.Operate;

/**
 * @author Landim - Arthur Landim
 *
 */
public class PanelOperation extends JPanel {

    private int id;

    private JLabel operation;

    private JLabel transaction;

    private JLabel object;

    public PanelOperation() {
    }

    public PanelOperation(int id, String oper, String transaction, String object) {
        this.operation = new JLabel(oper);
        this.transaction = new JLabel(transaction);
        this.object = new JLabel(object);
        this.id = id;
        Font font = new Font("Arial", Font.BOLD, 18);
        this.operation.setFont(font);
        this.transaction.setFont(font);
        this.object.setFont(font);
        setBackground(Color.WHITE);
        if (operation.equals(Operate.ABORT)) setBackground(Color.RED);
        if (operation.equals(Operate.COMMIT)) setBackground(Color.GREEN);
        setLayout(new BorderLayout());
        add(this.operation, BorderLayout.WEST);
        add(this.transaction, BorderLayout.CENTER);
        add(this.object, BorderLayout.EAST);
        Border borderTable = BorderFactory.createLineBorder(Color.GRAY, 1);
        setBorder(borderTable);
        setVisible(true);
        setSize(30, 25);
        setMinimumSize(new Dimension(40, 25));
        setPreferredSize(new Dimension(40, 25));
        setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
    }

    @Override
    public boolean equals(Object obj) {
        try {
            boolean result = id == ((PanelOperation) obj).getId();
            return result;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @return Returns the object.
     */
    public JLabel getObject() {
        return object;
    }

    /**
     * @param object The object to set.
     */
    public void setObject(JLabel object) {
        this.object = object;
    }

    /**
     * @return Returns the operation.
     */
    public JLabel getOperation() {
        return operation;
    }

    /**
     * @param operation The operation to set.
     */
    public void setOperation(JLabel operation) {
        this.operation = operation;
    }

    /**
     * @return Returns the transaction.
     */
    public JLabel getTransaction() {
        return transaction;
    }

    /**
     * @param transaction The transaction to set.
     */
    public void setTransaction(JLabel transaction) {
        this.transaction = transaction;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
}
