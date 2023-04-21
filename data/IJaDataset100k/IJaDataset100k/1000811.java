package sql;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.util.Log;

/**
 *  Description of the Class
 *
 * @author     svu
 * @created    26 ������ 2001 �.
 */
public class SqlServerDialog extends JDialog {

    private JTextField nameField;

    private JComboBox serverTypeList;

    private Hashtable controls = new Hashtable();

    private CardLayout serverTypeCards;

    private JPanel serverTypePanel;

    private SqlServerRecord rec;

    private int mode;

    private JButton okBtn;

    private JButton cancelBtn;

    /**
   *  Description of the Field
   *
   * @since
   */
    public static final int ADD_MODE = 0;

    /**
   *  Description of the Field
   *
   * @since
   */
    public static final int DEL_MODE = 1;

    /**
   *  Description of the Field
   *
   * @since
   */
    public static final int EDIT_MODE = 2;

    private static final String EMPTY_CARD = "__empty__";

    /**
   *  Constructor for the SqlServerDialog object
   *
   * @param  f     Description of Parameter
   * @param  rec   Description of Parameter
   * @param  mode  Description of Parameter
   * @since
   */
    public SqlServerDialog(JFrame f, SqlServerRecord rec, int mode) {
        super(f, jEdit.getProperty("sql.serverconfig.title"), true);
        this.rec = rec;
        this.mode = mode;
        init();
    }

    /**
   *  Gets the Result attribute of the SqlServerDialog object
   *
   * @return    The Result value
   * @since
   */
    public SqlServerRecord getResult() {
        return rec;
    }

    /**
   *  Description of the Method
   *
   * @since
   */
    public void init() {
        final Pane pane = new Pane();
        nameField = new JTextField(15);
        pane.addC(jEdit.getProperty("sql.name.label"), nameField);
        pane.addS("sql.config.label");
        final Hashtable types = SqlServerType.getAllTypes();
        final Vector typeNames = new Vector();
        for (Enumeration e = types.elements(); e.hasMoreElements(); ) {
            final String n = ((SqlServerType) e.nextElement()).getName();
            typeNames.addElement(n);
        }
        serverTypeList = new JComboBox(typeNames);
        pane.addC(jEdit.getProperty("sql.serverType.label"), serverTypeList);
        pane.addS("");
        serverTypePanel = new JPanel(serverTypeCards = new CardLayout());
        serverTypePanel.add(EMPTY_CARD, new JPanel());
        for (Enumeration e = SqlServerType.getAllTypes().elements(); e.hasMoreElements(); ) {
            final SqlServerType type = (SqlServerType) e.nextElement();
            final String name = type.getName();
            final Pane serverPane = new Pane();
            final Hashtable serverTypeControls = new Hashtable();
            for (Enumeration ep = type.getConnectionParameters().elements(); ep.hasMoreElements(); ) {
                final SqlServerType.ConnectionParameter param = (SqlServerType.ConnectionParameter) ep.nextElement();
                JTextField tf;
                if (rec.PASSWORD.equals(param.getName())) tf = new JPasswordField(param.getDefaultValue(), 15); else tf = new JTextField(param.getDefaultValue(), 15);
                serverTypeControls.put(param.getName(), tf);
                serverPane.addC(param.getDescription(), tf);
            }
            controls.put(type.getName(), serverTypeControls);
            serverTypePanel.add(name, serverPane);
        }
        serverTypeList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                final String name = (String) serverTypeList.getSelectedItem();
                serverTypeCards.show(serverTypePanel, name == null ? EMPTY_CARD : name);
            }
        });
        getContentPane().setLayout(new BorderLayout());
        Box hp = Box.createHorizontalBox();
        hp.add(hp.createHorizontalStrut(10));
        Box vp = Box.createVerticalBox();
        vp.add(pane);
        vp.add(serverTypePanel);
        hp.add(vp);
        hp.add(hp.createHorizontalStrut(10));
        getContentPane().add(BorderLayout.CENTER, hp);
        hp = Box.createHorizontalBox();
        hp.add(hp.createHorizontalStrut(10));
        hp.add(BorderLayout.WEST, okBtn = new JButton("OK"));
        hp.add(hp.createHorizontalStrut(10));
        hp.add(hp.createHorizontalGlue());
        hp.add(BorderLayout.EAST, cancelBtn = new JButton("Cancel"));
        hp.add(hp.createHorizontalStrut(10));
        vp = Box.createVerticalBox();
        vp.add(vp.createVerticalStrut(10));
        vp.add(hp);
        vp.add(vp.createVerticalStrut(10));
        getContentPane().add(BorderLayout.SOUTH, vp);
        okBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (!validateParams()) return;
                save();
                SqlServerDialog.this.setVisible(false);
            }
        });
        cancelBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                rec = null;
                SqlServerDialog.this.setVisible(false);
            }
        });
        serverTypeCards.show(serverTypePanel, EMPTY_CARD);
        serverTypeList.setSelectedItem(null);
        this.load();
        nameField.setEnabled(ADD_MODE == mode);
        serverTypeList.setEnabled(ADD_MODE == mode);
        for (Enumeration e = controls.elements(); e.hasMoreElements(); ) {
            Hashtable serverControls = (Hashtable) e.nextElement();
            for (Enumeration e1 = serverControls.elements(); e1.hasMoreElements(); ) {
                JTextField tf = (JTextField) e1.nextElement();
                tf.setEnabled(DEL_MODE != mode);
            }
        }
        pack();
    }

    /**
   *  Description of the Method
   *
   * @return    Description of the Returned Value
   * @since
   */
    public boolean validateParams() {
        if (mode != ADD_MODE) return true;
        final String typeName = (String) serverTypeList.getSelectedItem();
        if (typeName == null) {
            GUIUtilities.message(this, "sql.configurationError", new Object[] { jEdit.getProperty("sql.emptyType") });
            return false;
        }
        final String name = nameField.getText();
        if (name == null || "".equals(name)) {
            GUIUtilities.message(this, "sql.configurationError", new Object[] { jEdit.getProperty("sql.emptyName") });
            return false;
        }
        if (name.indexOf(SqlVFS.separatorChar) != -1 || name.indexOf(' ') != -1 || name.indexOf('\t') != -1) {
            GUIUtilities.message(this, "sql.configurationError", new Object[] { jEdit.getProperty("sql.illegalName") });
            return false;
        }
        return true;
    }

    /**
   *  Description of the Method
   *
   * @since
   */
    public void load() {
        if (rec == null) return;
        nameField.setText(rec.getName());
        final String typeName = rec.getServerType().getName();
        serverTypeCards.show(serverTypePanel, typeName);
        serverTypeList.setSelectedItem(typeName);
        final Hashtable serverControls = (Hashtable) controls.get(typeName);
        for (Enumeration e = serverControls.keys(); e.hasMoreElements(); ) {
            final String propName = (String) e.nextElement();
            final JTextField tf = (JTextField) serverControls.get(propName);
            tf.setText(rec.getProperty(propName));
        }
    }

    /**
   *  Description of the Method
   *
   * @since
   */
    public void save() {
        final String typeName = (String) serverTypeList.getSelectedItem();
        if (ADD_MODE == mode) {
            final SqlServerType type = SqlServerType.getByName(typeName);
            rec = new SqlServerRecord(type);
            final String name = nameField.getText();
            rec.setName(name);
        }
        final Hashtable serverControls = (Hashtable) controls.get(typeName);
        for (Enumeration e = serverControls.keys(); e.hasMoreElements(); ) {
            final String propName = (String) e.nextElement();
            final JTextField tf = (JTextField) serverControls.get(propName);
            rec.setProperty(propName, tf.getText());
        }
    }

    protected static class Pane extends AbstractOptionPane {

        /**
     *  Constructor for the Pane object
     *
     * @since
     */
        public Pane() {
            super("?");
        }

        public void addC(String s, Component c) {
            addComponent(s, c);
        }

        public void addS(String s) {
            addSeparator(s);
        }
    }
}
