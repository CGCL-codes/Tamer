package br.com.radiceti.falke.samples.contacts.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import br.com.radiceti.falke.samples.contacts.Contact;
import br.com.radiceti.falke.samples.contacts.ContactChange;

public class ContactList extends JFrame implements ContactListener {

    private static final long serialVersionUID = 1L;

    private ContactController controller;

    private JList list;

    private DefaultListModel listModel;

    public ContactList(ContactController controller) {
        super("My Contacts");
        this.controller = controller;
        controller.add(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setLayout(new BorderLayout(8, 8));
        createComponents();
        createList();
        pack();
    }

    private void createComponents() {
        createButtons();
        createList();
    }

    private void createList() {
        final int PADDING = 4;
        list = new JList();
        list.setPreferredSize(new Dimension(300, 200));
        list.setModel(createListModel());
        list.setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 1), new EmptyBorder(PADDING, PADDING, PADDING, PADDING)));
        add(list, BorderLayout.CENTER);
    }

    private ListModel createListModel() {
        listModel = new DefaultListModel();
        for (Contact contact : controller.list()) add(contact);
        return listModel;
    }

    private void createButtons() {
        JPanel buttons = new JPanel();
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                add();
            }
        });
        editButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                edit();
            }
        });
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                delete();
            }
        });
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);
        add(buttons, BorderLayout.NORTH);
    }

    private Contact getSelectedContact() {
        return (Contact) list.getSelectedValue();
    }

    private void add() {
        ContactForm form = new ContactForm(this);
        form.setVisible(true);
        if (form.confirmed()) {
            controller.createContact(form.getContactName(), form.getPhone());
        }
    }

    private void edit() {
        if (getSelectedContact() != null) {
            ContactForm form = new ContactForm(this);
            form.fillWith(getSelectedContact());
            form.setVisible(true);
            if (form.confirmed()) {
                ContactChange changes = new ContactChange();
                changes.setName(form.getContactName());
                changes.setPhone(form.getPhone());
                controller.saveContact(getSelectedContact(), changes);
            }
        }
    }

    private void delete() {
        if (getSelectedContact() != null) {
            if (confirm("Are you sure that you want to delete this contact?")) controller.delete(getSelectedContact());
        }
    }

    private boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    @Override
    public void add(Contact contact) {
        listModel.addElement(contact);
    }

    @Override
    public void update(Contact contact) {
        listModel.set(list.getSelectedIndex(), getSelectedContact());
    }

    @Override
    public void delete(Contact contact) {
        listModel.removeElement(contact);
    }
}
