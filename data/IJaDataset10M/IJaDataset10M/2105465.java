package com.crearic.tools.javabeans;

import com.crearic.tools.Constants;
import com.crearic.tools.db.DbCon;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * This component generates a value using a the database connection (DbCon), and
 * a SQL query that returns a value.
 * Example:
 *     ValueGenerator gen = new ValueGenerator();
 *     gen.setSQLGenerator("SELECT MAX(CAST(CliCodi AS INTEGER))+1 AS maxcodi FROM Clients");
 * 
 * @author David Majà Martínez <dmaja@crearic.com>
 */
public class ValueGenerator extends JPanel implements ActionListener {

    private static final String BTN_GENERATOR = "BTN_GENERATOR";

    /** The resource bundle to take the strings. */
    protected ResourceBundle strings;

    /** The sql query generator. */
    protected String sqlGenerator;

    /** The text field that will show the generated value. */
    protected JTextField txtValue;

    /** The button used to generate the value. */
    protected JButton btnGenerator;

    /**
	 * Creates a default instance of ValueGenerator.
	 */
    public ValueGenerator() {
        super();
        init();
    }

    /**
	 * Initial configuration for components.
	 */
    private void init() {
        strings = ResourceBundle.getBundle("com/company/tools/javabeans/messages");
        this.setLayout(new BorderLayout());
        txtValue = new JTextField();
        this.add(txtValue, BorderLayout.CENTER);
        btnGenerator = new JButton(new ImageIcon(getClass().getResource("/com/company/tools/resources/images/16/view-refresh.png")));
        btnGenerator.setActionCommand(BTN_GENERATOR);
        btnGenerator.addActionListener(this);
        btnGenerator.setMargin(new Insets(0, 0, 0, 0));
        btnGenerator.setToolTipText(strings.getString("generateValue"));
        this.add(btnGenerator, BorderLayout.EAST);
    }

    /**
	 * Generates the value using the DbCon class to create a connection and
	 * the sql query indicated by the user.
	 */
    private void generateValue() {
        Session session = DbCon.getInstance().getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery(sqlGenerator);
            List list = query.list();
            if (list.size() > 0) {
                Object obj = list.get(0);
                if (obj != null) txtValue.setText(obj.toString());
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) tx.rollback();
            ErrorDialog.saveError(Constants.FILE_ERRORS, ex);
            txtValue.setText("");
            throw ex;
        } finally {
            session.close();
        }
    }

    /**
	 * Gets the sql query sentence used to generate a valuel.
	 * @return the sql query.
	 */
    public String getSqlGenerator() {
        return sqlGenerator;
    }

    /**
	 * Sets the sql query that will generate the value. This query must return
	 * only one value.
	 * @param sqlGenerator The sql query generator.
	 */
    public void setSqlGenerator(String sqlGenerator) {
        this.sqlGenerator = sqlGenerator;
    }

    /**
	 * Gets if the text field is editable.
	 * @return <code>true</code> if text field is editable, <code>false</code> otherwise.
	 */
    public boolean isEditable() {
        return txtValue.isEditable();
    }

    /**
	 * Sets the text field editable or non editable.
	 * @param editable <code>true</code> if text field will be editable,
	 * <code>false</code> otherwise.
	 */
    public void setEditable(boolean editable) {
        txtValue.setEditable(editable);
    }

    /**
	 * Determines whether this component is enabled.
	 * @return <code>true</code> if the component is enabled, <code>false</code> otherwise
	 */
    @Override
    public boolean isEnabled() {
        return btnGenerator != null && btnGenerator.isEnabled();
    }

    /**
	 * Enables or disables this component, depending on the value of the parameter enabled.
	 * @param enabled If <code>true</code>, this component is enabled; 
	 * otherwise this component is disabled
	 */
    @Override
    public void setEnabled(boolean enabled) {
        btnGenerator.setEnabled(enabled);
        txtValue.setEnabled(enabled);
    }

    /**
	 * Gets the string value of the component.
	 * @return the text showed in the text field.
	 */
    public String getText() {
        return txtValue.getText();
    }

    /**
	 * Sets the value of the component.
	 * @param text The value for the component.
	 */
    public void setText(String text) {
        txtValue.setText(text);
    }

    /**
	 * Adds a document listener to the text field, for detect changes in the
	 * value of the component.
	 * @param listener The document listener that will be added.
	 */
    public void addDocumentListener(DocumentListener listener) {
        txtValue.getDocument().addDocumentListener(listener);
    }

    /**
	 * Removes a document listener from the text field.
	 * @param listener The listener that will be removed.
	 */
    public void removeDocumentListener(DocumentListener listener) {
        txtValue.getDocument().removeDocumentListener(listener);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(BTN_GENERATOR)) {
            generateValue();
        }
    }
}
