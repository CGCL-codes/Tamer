package jstudio.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import jstudio.control.Accounting;
import jstudio.gui.generic.EntityManagerPanel;
import jstudio.gui.generic.EntityPanel;
import jstudio.gui.generic.NicePanel;
import jstudio.model.Invoice;
import jstudio.model.Person;
import jstudio.model.Product;
import jstudio.report.ReportGenerator;
import jstudio.report.ReportGeneratorGUI;
import jstudio.util.Configuration;
import jstudio.util.GUITool;
import jstudio.util.Language;

@SuppressWarnings("serial")
public class InvoicePanel extends EntityPanel<Invoice> {

    public static final String INVOICE_REPORT = "report.invoice", INVOICE_REPORT_DEF = "/reports/invoice.jasper";

    private JTextField dateField, stampField, nameField, lastnameField, addressField, cityField, provinceField, capField, codeField;

    private JCheckBox noteCheck;

    private ProductTable productTable;

    private JButton okButton, cancelButton, printButton, closeButton, deleteButton, editButton, viewButton;

    private JButton pickPersonButton;

    public InvoicePanel(Invoice invoice, EntityManagerPanel<Invoice> manager, boolean editable) {
        super(invoice, manager);
        Calendar c = Calendar.getInstance();
        c.setTime(invoice.getDate());
        NicePanel panel = new NicePanel(entity.getInvoiceId(), editable ? Language.string("Edit details") : Language.string("View details"));
        panel.getBody().setLayout(new BorderLayout());
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        JPanel head = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(4, 4, 4, 4);
        dateField = GUITool.createDateField(head, gc, Language.string("Date"), this.entity.getDate(), editable, Invoice.dateFormat);
        if (editable) {
            pickPersonButton = GUITool.createButton(head, gc, Language.string("Pick"), this);
        }
        nameField = GUITool.createField(head, gc, Language.string("Name"), this.entity.getName(), editable);
        lastnameField = GUITool.createField(head, gc, Language.string("Lastname"), this.entity.getLastname(), editable);
        addressField = GUITool.createField(head, gc, Language.string("Address"), this.entity.getAddress(), editable);
        cityField = GUITool.createField(head, gc, Language.string("City"), this.entity.getCity(), editable);
        provinceField = GUITool.createProvinceField(head, gc, Language.string("Province"), this.entity.getProvince(), editable);
        capField = GUITool.createCAPField(head, gc, Language.string("CAP"), this.entity.getCap(), editable);
        codeField = GUITool.createField(head, gc, Language.string("Code"), this.entity.getCode(), editable);
        String sval = editable ? Float.toString(entity.getStamp()) : Product.formatCurrency(entity.getStamp());
        stampField = GUITool.createField(head, gc, Language.string("Stamp"), sval, editable);
        noteCheck = GUITool.createCheck(head, gc, Language.string("L675 Compliant"), this.entity.getNote(), editable);
        JPanel body = new JPanel(new BorderLayout());
        productTable = new ProductTable(invoice, manager, editable);
        productTable.refresh();
        productTable.setBorder(BorderFactory.createTitledBorder(Language.string("Products")));
        body.add(productTable, BorderLayout.CENTER);
        panel.getBody().add(head, BorderLayout.NORTH);
        panel.getBody().add(body, BorderLayout.CENTER);
        if (editable) {
            if (entity.getId() > 0l) {
                deleteButton = new JButton(Language.string("Delete"));
                deleteButton.addActionListener(this);
                panel.addButton(deleteButton);
            }
            viewButton = new JButton(Language.string("View"));
            viewButton.addActionListener(this);
            panel.addButton(viewButton);
            panel.addButtonsGlue();
            okButton = new JButton(Language.string("Ok"));
            okButton.addActionListener(this);
            panel.addButton(okButton);
            cancelButton = new JButton(Language.string("Cancel"));
            cancelButton.addActionListener(this);
            panel.addButton(cancelButton);
        } else {
            deleteButton = new JButton(Language.string("Delete"));
            deleteButton.addActionListener(this);
            panel.addButton(deleteButton);
            editButton = new JButton(Language.string("Edit"));
            editButton.addActionListener(this);
            panel.addButton(editButton);
            printButton = new JButton(Language.string("Print"));
            printButton.addActionListener(this);
            panel.addButton(printButton);
            panel.addButtonsGlue();
            closeButton = new JButton(Language.string("Close"));
            closeButton.addActionListener(this);
            panel.addButton(closeButton);
        }
    }

    public void showProduct(Product t) {
        JDialog dialog = new ProductPanel(t, productTable, entity, manager).createDialog(getDialog());
        dialog.setVisible(true);
    }

    private boolean applyChanges() {
        try {
            Date d = Person.birthdateFormat.parse(dateField.getText());
            entity.setDate(d);
        } catch (ParseException e1) {
            String msg = Language.string("Wrong date format for {0}, expected {1}", dateField.getText(), Person.birthdateFormat.toPattern());
            JOptionPane.showMessageDialog(this, msg, Language.string("Date format error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            entity.setStamp(Float.parseFloat(stampField.getText()));
        } catch (NumberFormatException e) {
            String msg = Language.string("Wrong stamp currency format for {0}", stampField.getText());
            JOptionPane.showMessageDialog(this, msg, Language.string("Stamp format error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        entity.setName(nameField.getText());
        entity.setLastname(lastnameField.getText());
        entity.setAddress(addressField.getText());
        entity.setCity(cityField.getText());
        entity.setProvince(provinceField.getText());
        entity.setCap(capField.getText());
        entity.setCode(codeField.getText());
        if (entity.getId() == 0) entity.setId(controller.getNextId());
        if (entity.getNumber() == 0) entity.setNumber(((Accounting) controller).getNextInvoiceNumber(entity.getDate()));
        entity.setNote(noteCheck.isSelected() ? Language.string("L675 compliant") : "");
        controller.store(entity);
        return true;
    }

    private boolean checkModified() {
        if (!entity.getName().equals(nameField.getText())) return true;
        if (!entity.getLastname().equals(lastnameField.getText())) return true;
        if (!entity.getAddress().equals(addressField.getText())) return true;
        if (!entity.getCap().equals(capField.getText())) return true;
        if (!entity.getCity().equals(cityField.getText())) return true;
        if (!entity.getCode().equals(codeField.getText())) return true;
        if (!entity.getProvince().equals(provinceField.getText())) return true;
        if ((entity.getNote().length() > 0 && !noteCheck.isSelected()) || entity.getNote().length() == 0 && noteCheck.isSelected()) return true;
        if (!Float.toString(entity.getStamp()).equals(stampField.getText())) return true;
        return false;
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == pickPersonButton) {
            PersonSelectionPanel psp = new PersonSelectionPanel(controller.getApplication().getAddressBook());
            psp.showDialog(this, nameField.getText() + " " + lastnameField.getText());
            Person p = psp.getSelected();
            if (p != null) {
                nameField.setText(p.getName());
                lastnameField.setText(p.getLastname());
                addressField.setText(p.getAddress());
                capField.setText(p.getCap());
                cityField.setText(p.getCity());
                codeField.setText(p.getCode());
                provinceField.setText(p.getProvince());
            }
        } else if (o == okButton) {
            if (applyChanges()) {
                getDialog().dispose();
                manager.refresh();
            }
        } else if (o == cancelButton || o == closeButton) {
            getDialog().dispose();
        } else if (o == printButton) {
            ReportGenerator rg = new ReportGenerator();
            rg.setReport(Configuration.getGlobal(INVOICE_REPORT, INVOICE_REPORT_DEF));
            rg.setHead(entity);
            rg.setHeadValue("date", Person.birthdateFormat.format(entity.getDate()));
            rg.setHeadValue("note", entity.getNote());
            rg.setData(entity.getProducts());
            rg.setHeadValue("stamp", Product.formatCurrency(entity.getStamp()));
            rg.setHeadValue("totalcost", Product.formatCurrency(productTable.getTotal()));
            ReportGeneratorGUI rgui = new ReportGeneratorGUI(rg, "invoice_" + entity.getFilePrefix());
            rgui.showGUI((Window) SwingUtilities.getRoot(this));
        } else if (o == editButton) {
            getDialog().dispose();
            JDialog dialog = new InvoicePanel(super.entity, super.manager, true).createDialog(super.manager.getTopLevelAncestor());
            dialog.setVisible(true);
        } else if (o == deleteButton) {
            int ch = JOptionPane.showConfirmDialog(super.manager, Language.string("Are you sure you want to remove invoice {0} of {1}?", entity.getInvoiceId(), Invoice.dateFormat.format(entity.getDate())), Language.string("Remove invoice?"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ch == JOptionPane.YES_OPTION) {
                controller.delete(entity);
                getDialog().dispose();
                manager.refresh();
            }
        } else if (o == viewButton) {
            if (checkModified()) {
                int ch = JOptionPane.showConfirmDialog(this, Language.string("Apply changes to {0}?", entity.getInvoiceId()), Language.string("Changes made"), JOptionPane.YES_NO_CANCEL_OPTION);
                if (ch == JOptionPane.CANCEL_OPTION) return;
                if (ch == JOptionPane.YES_OPTION) applyChanges();
            }
            getDialog().dispose();
            JDialog dialog = new InvoicePanel(super.entity, super.manager, false).createDialog(super.manager.getTopLevelAncestor());
            dialog.setVisible(true);
        }
    }
}
