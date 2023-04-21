package com.dbxml.db.admin.dialogs;

import java.awt.*;
import javax.swing.*;
import com.dbxml.db.admin.Admin;
import com.dbxml.db.admin.swing.PropertyInfo;
import com.dbxml.db.admin.swing.PropertyPane;
import com.dbxml.db.client.CollectionClient;
import com.dbxml.xml.dom.DOMHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.Border;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * CollectionDialog
 */
public final class CollectionDialog extends JDialog {

    private static final String[] ColumnNames = new String[] { "Name", "Value" };

    private static final FilerInfo[] Filers = new FilerInfo[] { new FilerInfo("Native XML Collection", com.dbxml.db.common.btree.BTreeFiler.class, "compressed"), new FilerInfo("Memory XML Collection", com.dbxml.db.common.filers.MemFiler.class, "compressed"), new FilerInfo("File System XML Collection", com.dbxml.db.common.filers.FSFiler.class, "tesxt"), new FilerInfo("RDBMS Mapped Collection", com.dbxml.db.enterprise.dbfiler.DBFiler.class, "compressed"), new FilerInfo("Native Binary Collection", com.dbxml.db.common.btree.BTreeFiler.class, "binary"), new FilerInfo("Memory Binary Collection", com.dbxml.db.common.filers.MemFiler.class, "binary"), new FilerInfo("File System Binary collection", com.dbxml.db.common.filers.FSFiler.class, "binary") };

    private List[] properties = new List[Filers.length];

    private CollectionClient col;

    private boolean okClicked = false;

    ImageIcon imgDatabase;

    JLabel lblCreate = new JLabel();

    Border brdSearch;

    GridBagLayout thisLayout = new GridBagLayout();

    JPanel pnlButtons = new JPanel();

    JButton btnCreate = new JButton();

    JButton btnCancel = new JButton();

    JLabel lblType = new JLabel();

    JComboBox cmbType = new JComboBox(Filers);

    JLabel lblProps = new JLabel();

    BorderLayout pnlButtonsLayout = new BorderLayout();

    JPanel pnlDialog = new JPanel();

    JLabel lblName = new JLabel();

    JTextField txtName = new JTextField();

    private PropertyPane propertyPane = new PropertyPane() {

        public boolean checkValidity() {
            if (super.checkValidity()) return testRequired(); else {
                btnCreate.setEnabled(false);
                return false;
            }
        }
    };

    public CollectionDialog(Frame owner, CollectionClient col) {
        super(owner, true);
        this.col = col;
        PropertyInfo pageSize = new PropertyInfo("pagesize", "Page Size", "4096", true, PropertyInfo.TYPE_INT);
        PropertyInfo directory = new PropertyInfo("location", "Directory", "", true);
        PropertyInfo extensions = new PropertyInfo("ext", "Extensions", "xml xsl xsd xhtml svg");
        PropertyInfo readOnly = new PropertyInfo("readonly", "Read Only", "false", false, PropertyInfo.TYPE_BOOLEAN);
        properties[0] = new ArrayList();
        properties[0].add(pageSize);
        properties[1] = new ArrayList();
        properties[2] = new ArrayList();
        properties[2].add(directory);
        properties[2].add(extensions);
        properties[2].add(readOnly);
        properties[3] = new ArrayList();
        properties[3].add(new PropertyInfo("driver", "Driver Class", "", true));
        properties[3].add(new PropertyInfo("url", "JDBC URL", "", true));
        properties[3].add(new PropertyInfo("username", "User Name"));
        properties[3].add(new PropertyInfo("password", "Password", "", false, PropertyInfo.TYPE_PASSWORD));
        properties[3].add(new PropertyInfo("mapfile", "Map File"));
        properties[3].add(new PropertyInfo("namespace", "Output Namespace"));
        properties[3].add(new PropertyInfo("sort", "Record Sorting", "true", false, PropertyInfo.TYPE_BOOLEAN));
        properties[3].add(new PropertyInfo("distinct", "Distinct Records", "true", false, PropertyInfo.TYPE_BOOLEAN));
        properties[3].add(new PropertyInfo("passthru", "SQL Passthru", "false", false, PropertyInfo.TYPE_BOOLEAN));
        properties[4] = new ArrayList();
        properties[4].add(pageSize);
        properties[5] = new ArrayList();
        properties[6] = new ArrayList();
        properties[6].add(directory);
        properties[6].add(extensions);
        properties[6].add(readOnly);
        propertyPane.addProperties(properties[0]);
        propertyPane.checkValidity();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
            cmbType.setMaximumRowCount(Filers.length);
            pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = getSize();
            if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
            setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        } catch (Exception e) {
            Admin.getInstance().addMessage(e.toString());
        }
    }

    private void jbInit() throws Exception {
        imgDatabase = new ImageIcon(CollectionDialog.class.getResource("database.gif"));
        brdSearch = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        lblCreate.setIcon(imgDatabase);
        this.setModal(true);
        this.setTitle("Create Collection");
        this.setResizable(false);
        lblName.setText("Name");
        propertyPane.setMinimumSize(new Dimension(250, 250));
        propertyPane.setPreferredSize(new Dimension(250, 250));
        this.getContentPane().add(pnlDialog);
        pnlDialog.setLayout(thisLayout);
        pnlButtons.setLayout(pnlButtonsLayout);
        btnCreate.setFont(new Font("Dialog", 0, 12));
        btnCreate.setActionCommand("connect");
        btnCreate.setSelected(true);
        btnCreate.setText("create");
        btnCreate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnCreate_actionPerformed(e);
            }
        });
        btnCancel.setFont(new Font("Dialog", 0, 12));
        btnCancel.setActionCommand("cancel");
        btnCancel.setText("cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnCancel_actionPerformed(e);
            }
        });
        lblType.setText("Type");
        lblProps.setText("Properties");
        cmbType.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cmbType_actionPerformed(e);
            }
        });
        txtName.setText("");
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                txtName_keyReleased(e);
            }
        });
        pnlButtonsLayout.setHgap(5);
        pnlDialog.add(pnlButtons, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 10, 10), 0, 0));
        pnlButtons.add(btnCreate, BorderLayout.CENTER);
        pnlButtons.add(btnCancel, BorderLayout.EAST);
        pnlDialog.add(lblType, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 10, 0), 0, 0));
        pnlDialog.add(cmbType, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0));
        pnlDialog.add(lblProps, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 10, 10, 0), 0, 0));
        pnlDialog.add(lblCreate, new GridBagConstraints(0, 0, 1, 5, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        pnlDialog.add(lblName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 0));
        pnlDialog.add(txtName, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        pnlDialog.add(propertyPane, new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 0, 0));
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            hide();
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    void btnCreate_actionPerformed(ActionEvent e) {
        try {
            okClicked = true;
            String name = txtName.getText();
            Document doc = DOMHelper.newDocument();
            Element root = doc.createElement("collection");
            root.setAttribute("name", name);
            int idx = cmbType.getSelectedIndex();
            root.setAttribute("type", Filers[idx].type);
            doc.appendChild(root);
            Element filer = doc.createElement("filer");
            filer.setAttribute("class", Filers[idx].filerClass.getName());
            PropertyInfo[] props = propertyPane.getProperties();
            for (int i = 0; i < props.length; i++) {
                PropertyInfo pi = props[i];
                String value = pi.getValue();
                if (value.trim().length() > 0) filer.setAttribute(pi.getName(), value);
            }
            root.appendChild(filer);
            root.appendChild(doc.createElement("indexes"));
            root.appendChild(doc.createElement("triggers"));
            root.appendChild(doc.createElement("extensions"));
            col.createCollection(name, doc);
            hide();
        } catch (Exception ex) {
            Admin.getInstance().addMessage(ex.getMessage());
        }
    }

    void btnCancel_actionPerformed(ActionEvent e) {
        hide();
    }

    void cmbType_actionPerformed(ActionEvent e) {
        List l = properties[cmbType.getSelectedIndex()];
        propertyPane.clearProperties();
        propertyPane.addProperties(l);
        propertyPane.checkValidity();
    }

    void txtName_keyReleased(KeyEvent e) {
        propertyPane.checkValidity();
    }

    boolean testRequired() {
        if (txtName.getText().trim().length() == 0) {
            btnCreate.setEnabled(false);
            return false;
        }
        btnCreate.setEnabled(true);
        return true;
    }

    /**
    * FilerInfo
    */
    private static class FilerInfo {

        public String name;

        public Class filerClass;

        public String type;

        public FilerInfo(String name, Class filerClass, String type) {
            this.name = name;
            this.filerClass = filerClass;
            this.type = type;
        }

        public String toString() {
            return name;
        }
    }
}
