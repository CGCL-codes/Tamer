package net.sf.amemailchecker.ext.addressbook.ui;

import net.sf.amemailchecker.gui.ActionControlFactory;
import net.sf.amemailchecker.ext.addressbook.AddressBookProvider;
import net.sf.amemailchecker.ext.addressbook.settings.AddressBookResourceContext;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;

public class ContactEditPanel extends JPanel {

    private JTextField displayNameField;

    private ActionListComposite postAddressesList;

    public void init(ContactEditPanelModel model) {
        AddressBookResourceContext bundle = AddressBookProvider.getProvider().getResourceContext();
        JTextField name = new JTextField();
        name.setDocument(model.getNameModel());
        JPanel namePanel = ActionControlFactory.Factory.createTitleWrapper(name, bundle.getI18NBundleValue("label.person.name"));
        JTextField surname = new JTextField();
        surname.setDocument(model.getSurnameModel());
        JPanel surnamePanel = ActionControlFactory.Factory.createTitleWrapper(surname, bundle.getI18NBundleValue("label.person.surname"));
        displayNameField = new JTextField();
        displayNameField.setDocument(model.getDisplayNameModel());
        JPanel displayNamePanel = ActionControlFactory.Factory.createTitleWrapper(displayNameField, bundle.getI18NBundleValue("label.person.display.name"));
        JPanel personIdentityPanel = new JPanel();
        personIdentityPanel.setLayout(new BoxLayout(personIdentityPanel, BoxLayout.Y_AXIS));
        personIdentityPanel.add(namePanel);
        personIdentityPanel.add(Box.createVerticalStrut(5));
        personIdentityPanel.add(surnamePanel);
        personIdentityPanel.add(Box.createVerticalStrut(5));
        personIdentityPanel.add(displayNamePanel);
        postAddressesList = new ActionListComposite();
        postAddressesList.setBorder(BorderFactory.createTitledBorder(bundle.getI18NBundleValue("label.email.addresses")));
        postAddressesList.setEditPanelExpandable(false);
        postAddressesList.setUseRearrangeAction(true);
        postAddressesList.init(model.getPostAddressesModel(), bundle);
        JTextArea note = new JTextArea(model.getNoteModel());
        JPanel notePanel = ActionControlFactory.Factory.createTitleWrapper(note, bundle.getI18NBundleValue("label.note"));
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.ipadx = 10;
        layout.setConstraints(personIdentityPanel, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        layout.setConstraints(postAddressesList, constraints);
        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.ipady = 100;
        constraints.fill = GridBagConstraints.BOTH;
        layout.setConstraints(notePanel, constraints);
        add(personIdentityPanel);
        add(postAddressesList);
        add(notePanel);
        if (displayNameField.getDocument().getLength() <= 0) {
            DocumentListener reflectDocumentListener = new ReflectDocumentListener(new Document[] { displayNameField.getDocument() });
            name.getDocument().addDocumentListener(reflectDocumentListener);
            surname.getDocument().addDocumentListener(reflectDocumentListener);
        }
    }
}
