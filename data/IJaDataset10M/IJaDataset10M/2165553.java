package net.sf.josas.ui.swing.view;

import java.text.ParseException;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.MaskFormatter;
import net.sf.josas.model.MembershipManager;
import net.sf.josas.model.ZipModel;
import net.sf.josas.om.Addressable;
import net.sf.josas.om.Person;
import net.sf.josas.ui.Messages;
import net.sf.josas.ui.swing.control.MembershipController;
import org.jdesktop.swingx.JXDatePicker;

/**
 * Dialog pane for entering/editing members.
 *
 * @author frederic
 *
 */
public class MembershipDialogPane extends JPanel {

    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    /** Constant. */
    private static final String PREFIX = "member.dialog.";

    /** Identity pane title. */
    private static final String IDENTITY_TITLE = PREFIX + "identity.title";

    /** Contact pane title. */
    private static final String CONTACT_TITLE = PREFIX + "contact.title";

    /** Contact pane title. */
    private static final String MEMBERSHIP_TITLE = PREFIX + "membership.title";

    /** Name label name. */
    private static final String NAME_LABEL = PREFIX + "name.label";

    /** First name label name. */
    private static final String FIRSTNAME_LABEL = PREFIX + "firstname.label";

    /** Birth date label name. */
    private static final String BIRTH_DATE_LABEL = PREFIX + "birthdate.label";

    /** Birth place label name. */
    private static final String BIRTH_PLACE_LABEL = PREFIX + "birthplace.label";

    /** Email label name. */
    private static final String EMAIL_LABEL = PREFIX + "email.label";

    /** Mobile label name. */
    private static final String MOBILE_LABEL = PREFIX + "mobile.label";

    /** Name field name. */
    public static final String NAME_TEXT = PREFIX + "name.text";

    /** First name field name. */
    public static final String FIRSTNAME_TEXT = PREFIX + "firstname.text";

    /** Birth date field name. */
    public static final String BIRTHDATE_TEXT = PREFIX + "birthdate.text";

    /** Birth place zip code field name. */
    public static final String BIRTH_ZIP_TEXT = PREFIX + "birth.zip.text";

    /** Birth place city field name. */
    public static final String BIRTH_CITY_TEXT = PREFIX + "birth.city.text";

    /** Mobile phone field name. */
    public static final String MOBILE_TEXT = PREFIX + "mobile.text";

    /** Email field name. */
    public static final String EMAIL_TEXT = PREFIX + "email.text";

    /** Address field name. */
    public static final String ADDRESS_FIELD = AddressPane.ADDRESS_FIELD;

    /** Other address field name. */
    public static final String OTHER_ADDRESS_FIELD = AddressPane.OTHERADDRESS_FIELD;

    /** Address zip field name. */
    public static final String ZIP_FIELD = AddressPane.ZIP_FIELD;

    /** Address city field name. */
    public static final String CITY_FIELD = AddressPane.CITY_FIELD;

    /** Address phone field name. */
    public static final String PHONE_FIELD = AddressPane.PHONE_FIELD;

    /** Address fax field name. */
    public static final String FAX_FIELD = AddressPane.FAX_FIELD;

    /** Field for selecting the membership start date. */
    public static final String START_DATE_TEXT = PREFIX + "startdate.text";

    /** start date label. */
    private static final String START_DATE_LABEL = PREFIX + "startdate.label";

    /** Name field. */
    private final JTextField nameField;

    /** Firstname field. */
    private final JTextField firstnameField;

    /** Birth date field. */
    private final JXDatePicker birthdateField;

    /** Birth place zip code field. */
    private JTextField birthZipField;

    /** Birth place city field. */
    private final JTextField birthCityField;

    /** Mobile field. */
    private final JTextField mobileField;

    /** Email field. */
    private final JTextField emailField;

    /** Address pane. */
    private final AddressPane addressPane;

    /** Membership start date field. */
    private final JXDatePicker startDateField;

    /**
    * Constructor.
    *
    * @param controller
    *            controller
     * @param model membership manager
    */
    public MembershipDialogPane(final MembershipController controller, final MembershipManager model) {
        nameField = new JTextField();
        nameField.setName(NAME_TEXT);
        firstnameField = new JTextField();
        firstnameField.setName(FIRSTNAME_TEXT);
        birthdateField = new JXDatePicker();
        birthdateField.setName(BIRTHDATE_TEXT);
        try {
            birthZipField = new JFormattedTextField(new MaskFormatter(ZipModel.MASK));
            birthZipField.setName(BIRTH_ZIP_TEXT);
            birthZipField.setColumns(ZipModel.MASK.length());
            birthZipField.setMaximumSize(birthZipField.getPreferredSize());
            birthZipField.addFocusListener(controller);
        } catch (ParseException e) {
            e.printStackTrace();
            assert false;
        }
        birthCityField = new JTextField();
        birthCityField.setName(BIRTH_CITY_TEXT);
        addressPane = controller.getAddressController().getView();
        mobileField = new JTextField();
        mobileField.setName(MOBILE_TEXT);
        emailField = new JTextField();
        emailField.setName(EMAIL_TEXT);
        startDateField = new JXDatePicker();
        startDateField.setName(START_DATE_TEXT);
        startDateField.setDate(new GregorianCalendar().getTime());
        Addressable member = controller.getMember();
        if (member != null) {
            nameField.setText(member.getName());
            startDateField.setDate(model.getMembership(member).getStartDate());
            if (member instanceof Person) {
                Person person = (Person) member;
                firstnameField.setText(person.getFirstName());
                birthdateField.setDate(person.getBirthDate());
                birthZipField.setText(person.getBirthZip());
                birthCityField.setText(person.getBirthCity());
                mobileField.setText(person.getMobile());
                emailField.setText(person.getEmail());
            }
        }
        layoutComponents();
    }

    /**
    * Layout the various components.
    */
    private void layoutComponents() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        JPanel identityPane = buildIdentityPane();
        JPanel personalPane = buildContactPane();
        JPanel memberShipPane = buildMembershipPane();
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(identityPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(addressPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(personalPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(memberShipPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(identityPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(addressPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(personalPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(memberShipPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }

    /**
     * @return membership pane
     */
    private JPanel buildMembershipPane() {
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(Messages.getString(MEMBERSHIP_TITLE)));
        JLabel startDateLabel = new JLabel(Messages.getString(START_DATE_LABEL));
        startDateLabel.setName(START_DATE_LABEL);
        GroupLayout layout = new GroupLayout(pane);
        pane.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(startDateLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(startDateField).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(startDateLabel).addComponent(startDateField))));
        return pane;
    }

    /**
    * @return contact pane
    */
    private JPanel buildContactPane() {
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(Messages.getString(CONTACT_TITLE)));
        JLabel emailLabel = new JLabel(Messages.getString(EMAIL_LABEL));
        emailLabel.setName(EMAIL_LABEL);
        JLabel mobileLabel = new JLabel(Messages.getString(MOBILE_LABEL));
        mobileLabel.setName(MOBILE_LABEL);
        GroupLayout layout = new GroupLayout(pane);
        pane.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(mobileLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(mobileField).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(emailLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(emailField).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(mobileLabel).addComponent(mobileField).addComponent(emailLabel).addComponent(emailField))));
        layout.linkSize(mobileField, addressPane.getPhoneField());
        return pane;
    }

    /**
    * @return identity pane
    */
    private JPanel buildIdentityPane() {
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(Messages.getString(IDENTITY_TITLE)));
        JLabel nameLabel = new JLabel(Messages.getString(NAME_LABEL));
        nameLabel.setName(NAME_LABEL);
        JLabel firstNameLabel = new JLabel(Messages.getString(FIRSTNAME_LABEL));
        firstNameLabel.setName(FIRSTNAME_LABEL);
        JLabel birthDateLabel = new JLabel(Messages.getString(BIRTH_DATE_LABEL));
        birthDateLabel.setName(BIRTH_DATE_LABEL);
        JLabel birthPlaceLabel = new JLabel(Messages.getString(BIRTH_PLACE_LABEL));
        birthPlaceLabel.setName(BIRTH_PLACE_LABEL);
        GroupLayout layout = new GroupLayout(pane);
        pane.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(nameLabel).addComponent(birthDateLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(nameField).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(firstNameLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(firstnameField)).addGroup(layout.createSequentialGroup().addComponent(birthdateField).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(birthPlaceLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(birthZipField).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(birthCityField))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField).addComponent(firstNameLabel).addComponent(firstnameField)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(birthDateLabel).addComponent(birthdateField).addComponent(birthPlaceLabel).addComponent(birthZipField).addComponent(birthCityField)).addContainerGap()));
        layout.linkSize(mobileField, addressPane.getPhoneField());
        return pane;
    }

    /**
    * @return the nameField
    */
    public final JTextField getNameField() {
        return nameField;
    }

    /**
    * @return the firstnameField
    */
    public final JTextField getFirstnameField() {
        return firstnameField;
    }

    /**
    * @return the birthdateField
    */
    public final JXDatePicker getBirthdateField() {
        return birthdateField;
    }

    /**
    * @return the birthZipField
    */
    public final JTextField getBirthZipField() {
        return birthZipField;
    }

    /**
    * @return the birthCityField
    */
    public final JTextField getBirthCityField() {
        return birthCityField;
    }

    /**
     * @return the mobileField
     */
    public final JTextField getMobileField() {
        return mobileField;
    }

    /**
     * @return the emailField
     */
    public final JTextField getEmailField() {
        return emailField;
    }
}
