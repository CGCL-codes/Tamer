package it.novabyte.idb1;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingIterator;
import org.codehaus.jackson.map.ObjectMapper;

public class DlgConnection extends JDialog {

    private static final long serialVersionUID = 8964684949428683959L;

    private final JComboBox ctlDriver;

    private final JTextField ctlUrl;

    private final JTextField ctlUser;

    private final JPasswordField ctlPassword;

    @SuppressWarnings("rawtypes")
    private final DefaultListModel ctlConnectionsModel;

    @SuppressWarnings("rawtypes")
    private final JList ctlConnections;

    private WrpConnection connection;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DlgConnection(CtlStatement ctlConnection) {
        super(SwingUtilities.getWindowAncestor(ctlConnection), "Open Connection", ModalityType.DOCUMENT_MODAL);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        ctlDriver = new JComboBox();
        ctlUrl = new JTextField(30);
        ctlUser = new JTextField(30);
        ctlPassword = new JPasswordField(30);
        ctlConnectionsModel = new DefaultListModel();
        ctlConnections = new JList(ctlConnectionsModel);
        JButton ctlOk = new JButton(new AbstractAction("Ok") {

            private static final long serialVersionUID = 2107001312057028177L;

            @Override
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
        JButton ctlCancel = new JButton(new AbstractAction("Cancel") {

            private static final long serialVersionUID = -3793541292638895333L;

            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        JPanel ctlActions = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        ctlActions.add(ctlOk);
        ctlActions.add(ctlCancel);
        GridBagGenerator gbg = new GridBagGenerator();
        gbg.prepare(getContentPane());
        add(new JLabel("Driver:"), gbg.get(1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE));
        add(ctlDriver, gbg.get(1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
        gbg.newRow();
        add(new JLabel("Url:"), gbg.get(1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE));
        add(ctlUrl, gbg.get(1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
        gbg.newRow();
        add(new JLabel("User:"), gbg.get(1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE));
        add(ctlUser, gbg.get(1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
        gbg.newRow();
        add(new JLabel("Password:"), gbg.get(1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE));
        add(ctlPassword, gbg.get(1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL));
        gbg.newRow();
        add(new JLabel("Connections:"), gbg.get(2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE));
        gbg.newRow();
        add(new JScrollPane(ctlConnections), gbg.get(2, GridBagConstraints.RELATIVE, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
        gbg.newRow();
        add(ctlActions, gbg.get(2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(ctlConnection);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                onOpened();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    protected void onOpened() {
        SwingWorker<Object, JsonConnection> w = new SwingWorker<Object, JsonConnection>() {

            @Override
            protected Object doInBackground() throws Exception {
                FileInputStream in;
                try {
                    in = new FileInputStream("connections.json");
                } catch (FileNotFoundException xp) {
                    return null;
                }
                ObjectMapper om = new ObjectMapper();
                JsonParser jp = om.getJsonFactory().createJsonParser(in);
                MappingIterator<JsonConnection> it = om.readValues(jp, JsonConnection.class);
                while (it.hasNext()) {
                    JsonConnection jsonConnection = it.next();
                    publish(jsonConnection);
                }
                return null;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void process(List<JsonConnection> chunks) {
                for (JsonConnection jsonConnection : chunks) ctlConnectionsModel.addElement(jsonConnection);
            }
        };
        w.execute();
    }

    protected void onOk() {
        String driver = ctlDriver.getText();
        if ((driver == null) || (driver.length() == 0)) {
            JOptionPane.showMessageDialog(this, "Specify the driver class name.", "Can't connect", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String url = ctlUrl.getText();
        if ((url == null) || (url.length() == 0)) {
            JOptionPane.showMessageDialog(this, "Specify the url of connection.", "Can't connect", JOptionPane.ERROR_MESSAGE);
            return;
        }
        WrpConnection connection = new WrpConnection();
        connection.setDriver(driver);
        connection.setUrl(url);
        connection.setUser(ctlUser.getText());
        connection.setPassword(ctlPassword.getText());
        try {
            connection.connect();
        } catch (Throwable xp) {
            JOptionPane.showMessageDialog(this, xp.getMessage(), "Can't connect", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.connection = connection;
        dispose();
    }

    protected void onCancel() {
        if (connection != null) {
            connection.close();
            connection = null;
        }
        dispose();
    }

    public WrpConnection getConnection() {
        return connection;
    }
}
