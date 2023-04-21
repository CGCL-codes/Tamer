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
 * IndexDialog
 */
public final class IndexDialog extends JDialog {

    private static final String[] ColumnNames = new String[] { "Name", "Value" };

    private static final IndexerInfo[] Indexers = new IndexerInfo[] { new IndexerInfo("String Value Index", com.dbxml.db.common.indexers.ValueIndexer.class, "string"), new IndexerInfo("Trimmed Value Index", com.dbxml.db.common.indexers.ValueIndexer.class, "trimmed"), new IndexerInfo("Integer Value Index", com.dbxml.db.common.indexers.ValueIndexer.class, "long"), new IndexerInfo("Decimal Value Index", com.dbxml.db.common.indexers.ValueIndexer.class, "double"), new IndexerInfo("Byte Value Index", com.dbxml.db.common.indexers.ValueIndexer.class, "byte"), new IndexerInfo("Character Value Index", com.dbxml.db.common.indexers.ValueIndexer.class, "char"), new IndexerInfo("Boolean Value Index", com.dbxml.db.common.indexers.ValueIndexer.class, "boolean"), new IndexerInfo("Name Index", com.dbxml.db.common.indexers.NameIndexer.class, null), new IndexerInfo("Full Text Index", com.dbxml.db.common.fulltext.FullTextIndexer.class, null) };

    private List[] properties = new List[Indexers.length];

    private CollectionClient col;

    private boolean okClicked = false;

    ImageIcon imgIndex;

    JLabel lblCreate = new JLabel();

    Border brdSearch;

    GridBagLayout thisLayout = new GridBagLayout();

    JPanel pnlButtons = new JPanel();

    JButton btnCreate = new JButton();

    JButton btnCancel = new JButton();

    JLabel lblType = new JLabel();

    JComboBox cmbType = new JComboBox(Indexers);

    JLabel lblProps = new JLabel();

    BorderLayout pnlButtonsLayout = new BorderLayout();

    JPanel pnlDialog = new JPanel();

    JLabel lblName = new JLabel();

    JTextField txtName = new JTextField();

    PropertyPane propertyPane = new PropertyPane() {

        public boolean checkValidity() {
            if (super.checkValidity()) return testRequired(); else {
                btnCreate.setEnabled(false);
                return false;
            }
        }
    };

    public IndexDialog(Frame owner, CollectionClient col) {
        super(owner, true);
        this.col = col;
        PropertyInfo pattern = new PropertyInfo("pattern", "Pattern", "", true);
        PropertyInfo pageSize = new PropertyInfo("pagesize", "Page Size", "4096", false, PropertyInfo.TYPE_INT);
        properties[0] = new ArrayList();
        properties[0].add(pattern);
        properties[0].add(pageSize);
        properties[1] = new ArrayList();
        properties[1].add(pattern);
        properties[1].add(pageSize);
        properties[2] = new ArrayList();
        properties[2].add(pattern);
        properties[2].add(pageSize);
        properties[3] = new ArrayList();
        properties[3].add(pattern);
        properties[3].add(pageSize);
        properties[4] = new ArrayList();
        properties[4].add(pattern);
        properties[4].add(pageSize);
        properties[5] = new ArrayList();
        properties[5].add(pattern);
        properties[5].add(pageSize);
        properties[6] = new ArrayList();
        properties[6].add(pattern);
        properties[6].add(pageSize);
        properties[7] = new ArrayList();
        properties[7].add(pattern);
        properties[7].add(pageSize);
        properties[8] = new ArrayList();
        properties[8].add(pattern);
        properties[8].add(pageSize);
        properties[8].add(new PropertyInfo("stemmer", "Stemmer Class"));
        properties[8].add(new PropertyInfo("stopwords", "Stop Word List"));
        properties[8].add(new PropertyInfo("rollcase", "Case Rolling", "true", false, PropertyInfo.TYPE_BOOLEAN));
        propertyPane.addProperties(properties[0]);
        propertyPane.checkValidity();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
            cmbType.setMaximumRowCount(Indexers.length);
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
        imgIndex = new ImageIcon(IndexDialog.class.getResource("index.gif"));
        brdSearch = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        lblCreate.setIcon(imgIndex);
        this.setModal(true);
        this.setTitle("Create Index");
        this.setResizable(false);
        lblName.setText("Name");
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
        propertyPane.setPreferredSize(new Dimension(250, 250));
        propertyPane.setMinimumSize(new Dimension(250, 250));
        pnlDialog.add(pnlButtons, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 10, 10), 0, 0));
        pnlButtons.add(btnCreate, BorderLayout.CENTER);
        pnlButtons.add(btnCancel, BorderLayout.EAST);
        pnlDialog.add(lblType, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 10, 0), 0, 0));
        pnlDialog.add(cmbType, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0));
        pnlDialog.add(lblProps, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 10, 10, 0), 0, 0));
        pnlDialog.add(propertyPane, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 0, 0));
        pnlDialog.add(lblCreate, new GridBagConstraints(0, 0, 1, 6, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        pnlDialog.add(lblName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 0), 0, 0));
        pnlDialog.add(txtName, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
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
            int idx = cmbType.getSelectedIndex();
            Document doc = DOMHelper.newDocument();
            Element root = doc.createElement("index");
            root.setAttribute("name", name);
            root.setAttribute("class", Indexers[idx].indexerClass.getName());
            if (Indexers[idx].type != null) root.setAttribute("type", Indexers[idx].type);
            doc.appendChild(root);
            PropertyInfo[] props = propertyPane.getProperties();
            for (int i = 0; i < props.length; i++) {
                PropertyInfo pi = props[i];
                String value = pi.getValue();
                if (value.trim().length() > 0) root.setAttribute(pi.getName(), value);
            }
            col.createIndexer(doc);
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
    * IndexerInfo
    */
    private static class IndexerInfo {

        public String name;

        public Class indexerClass;

        public String type;

        public IndexerInfo(String name, Class indexerClass, String type) {
            this.name = name;
            this.indexerClass = indexerClass;
            this.type = type;
        }

        public String toString() {
            return name;
        }
    }
}
