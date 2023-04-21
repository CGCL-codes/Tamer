package jasperdesign.ui;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import jasperdesign.jdbc.*;
import jasperdesign.*;

public class DriversPanel extends JPanel {

    Config config = null;

    JList driverLst = null;

    JButton editBtn = null;

    JButton newBtn = null;

    JButton deleteBtn = null;

    JasperDesign design = null;

    public DriversPanel(Config cfg) {
        super();
        config = cfg;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        driverLst = new JList();
        add(new JScrollPane(driverLst), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 10, 0);
        editBtn = new JButton("Edit");
        add(editBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 10, 0);
        newBtn = new JButton("New");
        add(newBtn, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        deleteBtn = new JButton("Delete");
        add(deleteBtn, gbc);
        loadList();
        newBtn.addActionListener(new NewDriverAction());
        editBtn.addActionListener(new EditDriverAction());
        deleteBtn.addActionListener(new DeleteDriverAction());
        enableButtons(false);
        driverLst.addListSelectionListener(new SelectListener());
    }

    public void updateDesign(JasperDesign design) throws JRException {
    }

    void loadList() {
        Map map = config.getJdbcDrivers();
        Vector vVars = null;
        if (map.keySet() != null) {
            vVars = new Vector(map.keySet());
        } else {
            vVars = new Vector(10, 10);
        }
        driverLst.setListData(vVars);
    }

    void newDriver() {
        Driver driver = new Driver();
        if (editDriver(driver, "New driver")) {
            config.getJdbcDrivers().put(driver.getName(), driver);
            loadList();
        }
    }

    public boolean editDriver(Driver driver, String aTitle) {
        DriverEditPanel driverPnl = new DriverEditPanel(driver);
        OkCancelDialog dlg = OkCancelDialog.createDialog(this, driverPnl, aTitle, true, 400, 400);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
        if (!dlg.isCancelled()) {
            driverPnl.updateDriver(driver);
        }
        return !dlg.isCancelled();
    }

    void editDriver() {
        String driverName = driverLst.getSelectedValue().toString();
        Driver driver = (Driver) config.getJdbcDrivers().get(driverName);
        if (editDriver(driver, "Edit driver")) {
            config.getJdbcDrivers().remove(driverName);
            config.getJdbcDrivers().put(driver.getName(), driver);
            loadList();
        }
    }

    void deleteDriver() {
        String driverName = driverLst.getSelectedValue().toString();
        int retval = JOptionPane.showConfirmDialog(this, "Delete " + driverName + " driver?", "Delete driver", JOptionPane.OK_CANCEL_OPTION);
        if (retval == JOptionPane.OK_OPTION) {
            config.getJdbcDrivers().remove(driverName);
            loadList();
        }
    }

    void enableButtons(boolean b) {
        editBtn.setEnabled(b);
        deleteBtn.setEnabled(b);
    }

    class NewDriverAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            newDriver();
        }
    }

    class EditDriverAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            editDriver();
        }
    }

    class DeleteDriverAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            deleteDriver();
        }
    }

    class SelectListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent event) {
            enableButtons(driverLst.getSelectedIndex() > -1);
        }
    }
}
