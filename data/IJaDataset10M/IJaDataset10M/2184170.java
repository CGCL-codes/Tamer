package com.softserve.mproject.client.swing.ui.panels;

import com.softserve.mproject.utils.exceptions.ClientException;
import com.softserve.mproject.client.swing.Controller;
import com.softserve.mproject.client.swing.ui.ImageRenderer;
import com.softserve.mproject.client.swing.ui.Util;
import com.softserve.mproject.client.swing.ui.PersonDialog;
import com.softserve.mproject.client.swing.ui.SetRenderer;
import com.softserve.mproject.client.swing.ui.tablemodels.PersonTableModel;
import com.softserve.mproject.domain.entity.Address;
import com.softserve.mproject.domain.entity.Person;
import com.softserve.mproject.domain.entity.Phone;
import com.softserve.mproject.utils.common.ImageUtil;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

public class PersonManagerPanel extends JPanel {

    private JButton add, del, edit;

    private JTable table;

    private Controller ctrl;

    private JFrame frame;

    public PersonManagerPanel(JFrame frame) {
        this.frame = frame;
        initUI();
        initListeners();
    }

    public void setController(Controller ctrl) {
        this.ctrl = ctrl;
        this.ctrl.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (PersonManagerPanel.this.ctrl != null) {
                    table.setModel(new PersonTableModel(PersonManagerPanel.this.ctrl.getDataSet()));
                    int i = table.getSelectedRow();
                    if (i < table.getRowCount()) {
                        table.getSelectionModel().setSelectionInterval(i, i);
                    } else {
                        table.getSelectionModel().setSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
                    }
                }
            }
        });
    }

    private void initUI() {
        add = Util.getAddButton();
        del = Util.getDeleteButton();
        edit = Util.getEditButton();
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(1, 3));
        toolbar.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        toolbar.add(add);
        toolbar.add(del);
        toolbar.add(edit);
        table = new JTable(new PersonTableModel());
        table.setDefaultRenderer(Image.class, new ImageRenderer());
        table.setDefaultRenderer(Set.class, new SetRenderer());
        JScrollPane p = new JScrollPane(table);
        this.setLayout(new BorderLayout());
        this.add(toolbar, BorderLayout.NORTH);
        this.add(p, BorderLayout.CENTER);
    }

    private void initListeners() {
        add.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                PersonDialog dialog = new PersonDialog(frame);
                dialog.setVisible(true);
                if (dialog.isOk()) {
                    try {
                        Person p = new Person();
                        p.setFirstName(dialog.getFirstName());
                        p.setLastName(dialog.getLastName());
                        p.setSex((String) dialog.getSex());
                        p.setAge(dialog.getAge());
                        p.setEmail(dialog.getEmail());
                        p.setDescription(dialog.getDescription());
                        if (dialog.getImage() != null) {
                            p.setImage(ImageUtil.convertToByteArray(dialog.getImage()));
                        }
                        for (Phone ph : dialog.getPhones()) {
                            p.addPhone(ph);
                        }
                        for (Address ad : dialog.getAddreses()) {
                            p.addAddress(ad);
                        }
                        if (ctrl != null) {
                            ctrl.add(p);
                        }
                    } catch (ClientException ex) {
                        Util.showError(frame, "Connection error", ex);
                    }
                }
                table.getSelectionModel().setSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            }
        });
        del.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (ctrl != null) {
                        ctrl.delete(getSecletedPerson());
                    }
                } catch (ClientException ex) {
                    Util.showError(frame, "Connection error", ex);
                }
                int i = table.getSelectedRow();
                if (i >= 0 && i < table.getRowCount()) {
                    table.getSelectionModel().setSelectionInterval(i, i);
                } else {
                    table.getSelectionModel().setSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
                }
            }
        });
        edit.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Person oldP = getSecletedPerson();
                PersonDialog dialog = new PersonDialog(frame);
                dialog.setFirstName(oldP.getFirstName());
                dialog.setLastName(oldP.getLastName());
                dialog.setAge(oldP.getAge());
                dialog.setSex(oldP.getSex());
                dialog.setEmail(oldP.getEmail());
                dialog.setDescription(oldP.getDescription());
                dialog.setImage(ImageUtil.convertToImage(oldP.getImage()));
                dialog.setPhones(oldP.getPhones());
                dialog.setAddresses(oldP.getAddresses());
                dialog.setVisible(true);
                if (dialog.isOk()) {
                    Person p = new Person();
                    p.setId(oldP.getId());
                    p.setFirstName(dialog.getFirstName());
                    p.setLastName(dialog.getLastName());
                    p.setSex((String) dialog.getSex());
                    p.setAge(dialog.getAge());
                    p.setEmail(dialog.getEmail());
                    p.setDescription(dialog.getDescription());
                    if (dialog.getImage() != null) {
                        p.setImage(ImageUtil.convertToByteArray(dialog.getImage()));
                    }
                    for (Phone ph : dialog.getPhones()) {
                        p.addPhone(ph);
                    }
                    for (Address ad : dialog.getAddreses()) {
                        p.addAddress(ad);
                    }
                    try {
                        if (ctrl != null) {
                            ctrl.change(p);
                        }
                    } catch (ClientException ex) {
                        Util.showError(frame, "Connection error", ex);
                    }
                }
            }
        });
    }

    public Person getSecletedPerson() {
        Person p = new Person();
        int i = table.getSelectedRow();
        if (i < 0 || i >= table.getRowCount()) {
            return p;
        }
        p.setId((Integer) table.getValueAt(i, 0));
        p.setFirstName((String) table.getValueAt(i, 1));
        p.setLastName((String) table.getValueAt(i, 2));
        p.setAge((Integer) table.getValueAt(i, 3));
        p.setSex((String) table.getValueAt(i, 4));
        p.setEmail((String) table.getValueAt(i, 5));
        p.setPhones((Set<Phone>) table.getValueAt(i, 6));
        p.setAddresses((Set<Address>) table.getValueAt(i, 7));
        p.setDescription((String) table.getValueAt(i, 8));
        Object o = table.getValueAt(i, 9);
        if (o != null) {
            p.setImage(ImageUtil.convertToByteArray((Image) o));
        }
        return p;
    }
}
