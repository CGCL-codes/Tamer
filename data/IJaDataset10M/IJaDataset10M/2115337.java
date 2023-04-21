package org.neodatis.odb.gui.xml;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.gui.LoggerPanel;
import org.neodatis.odb.gui.Messages;
import org.neodatis.odb.gui.component.GUITool;
import org.neodatis.odb.xml.XMLExporter;
import org.neodatis.tool.ILogger;
import org.neodatis.tool.wrappers.OdbString;
import org.neodatis.tool.wrappers.OdbTime;

public class XmlExportPanel extends JPanel implements ActionListener, Runnable {

    private JButton btExport;

    private JButton btCancel;

    private JTextField tfXmlFile;

    private JButton btBrowseXml;

    private JTextField tfOdbFile;

    private JButton btBrowseOdb;

    private JTextField tfUser;

    private JTextField tfPassword;

    private LoggerPanel loggerPanel;

    private ILogger logger;

    public XmlExportPanel(ILogger logger) {
        this.logger = logger;
        init();
    }

    private void init() {
        JLabel label1 = new JLabel(Messages.getString("Xml File name to export to"));
        JLabel label2 = new JLabel(Messages.getString("ODB file to export from"));
        tfXmlFile = new JTextField(20);
        tfOdbFile = new JTextField(20);
        btBrowseXml = new JButton(Messages.getString("..."));
        btBrowseOdb = new JButton(Messages.getString("..."));
        btExport = new JButton(Messages.getString("Export File"));
        btCancel = new JButton(Messages.getString("Cancel"));
        btBrowseOdb.setActionCommand("browse-odb");
        btBrowseXml.setActionCommand("browse-xml");
        btExport.setActionCommand("export");
        btCancel.setActionCommand("cancel");
        loggerPanel = new LoggerPanel();
        btBrowseOdb.addActionListener(this);
        btBrowseXml.addActionListener(this);
        btExport.addActionListener(this);
        btCancel.addActionListener(this);
        JPanel left = new JPanel(new GridLayout(3, 1, 2, 2));
        JPanel center = new JPanel(new GridLayout(3, 1, 2, 2));
        left.add(label1);
        JPanel panel1 = new JPanel();
        panel1.add(tfXmlFile);
        panel1.add(btBrowseXml);
        left.add(label2);
        left.add(new JLabel());
        JPanel panel2 = new JPanel();
        panel2.add(tfOdbFile);
        panel2.add(btBrowseOdb);
        JPanel panel3 = new JPanel();
        JLabel lbUser = new JLabel("User");
        JLabel lbPassword = new JLabel("Password");
        tfUser = new JTextField(5);
        tfPassword = new JPasswordField(5);
        panel3.add(lbUser);
        panel3.add(tfUser);
        panel3.add(lbPassword);
        panel3.add(tfPassword);
        center.add(panel1);
        center.add(panel2);
        center.add(panel3);
        JPanel wpanel = new JPanel(new BorderLayout(2, 2));
        wpanel.add(left, BorderLayout.WEST);
        wpanel.add(center, BorderLayout.CENTER);
        wpanel.add(new JScrollPane(loggerPanel), BorderLayout.SOUTH);
        wpanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel bpanel = new JPanel();
        bpanel.add(btCancel);
        bpanel.add(btExport);
        setLayout(new BorderLayout(4, 4));
        add(wpanel, BorderLayout.CENTER);
        add(bpanel, BorderLayout.SOUTH);
        add(GUITool.buildHeaderPanel("XML Export Wizard"), BorderLayout.NORTH);
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if ("browse-xml".equals(action)) {
            browse("xml");
        }
        if ("browse-odb".equals(action)) {
            browse("odb");
        }
        if ("cancel".equals(action)) {
            cancel();
        }
        if ("export".equals(action)) {
            try {
                Thread t = new Thread(this);
                t.start();
            } catch (Exception e1) {
                logger.error("Error while export to XML : ", e1);
            }
        }
    }

    private void exportToXml() throws Exception {
        boolean ok = false;
        File xmlFile = new File(tfXmlFile.getText());
        File odbFile = new File(tfOdbFile.getText());
        if (!odbFile.exists()) {
            JOptionPane.showMessageDialog(this, Messages.getString("The database file " + odbFile.getPath() + " does not exist!"));
            return;
        }
        long start = OdbTime.getCurrentTimeInMs();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String user = tfUser.getText();
        String password = tfPassword.getText();
        if (user.length() == 0 && password.length() == 0) {
            user = null;
            password = null;
        }
        ODB odb = ODBFactory.open(odbFile.getPath(), user, password);
        XMLExporter exporter = new XMLExporter(odb);
        exporter.setExternalLogger(loggerPanel);
        try {
            exporter.export(xmlFile.getParent(), xmlFile.getName());
            ok = true;
        } catch (Throwable e) {
            logger.error(OdbString.exceptionToString(e, true));
        }
        odb.close();
        disableFields();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        long end = OdbTime.getCurrentTimeInMs();
        if (ok) {
            loggerPanel.info("export successfull (" + (end - start) + "ms)");
        } else {
            loggerPanel.info("Error while exporting (" + (end - start) + "ms)");
        }
    }

    private void cancel() {
        disableFields();
    }

    private void disableFields() {
        tfOdbFile.setEnabled(false);
        tfXmlFile.setEnabled(false);
        btExport.setEnabled(false);
        btCancel.setEnabled(false);
        btBrowseOdb.setEnabled(false);
        btBrowseXml.setEnabled(false);
    }

    private void browse(String type) {
        final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        String title = null;
        if (type.equals("xml")) {
            title = Messages.getString("Choose the name of the xml file to export to");
        } else {
            title = Messages.getString("Choose the name of the ODB file to export from");
        }
        fc.setDialogTitle(title);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (type.equals("xml")) {
                tfXmlFile.setText(fc.getSelectedFile().getPath());
            } else {
                tfOdbFile.setText(fc.getSelectedFile().getPath());
            }
        }
    }

    public void run() {
        try {
            exportToXml();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            loggerPanel.error(e);
        }
    }
}
