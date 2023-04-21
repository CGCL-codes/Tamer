package de.fmannan.addbook.editor;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import de.fmannan.addbook.common.fieldtypes.IField;
import de.fmannan.addbook.common.fieldtypes.IReadOnlyField;
import de.fmannan.addbook.common.fieldtypes.ISectionField;
import de.fmannan.addbook.common.fieldtypes.IStringField;
import de.fmannan.addbook.common.fieldtypes.IStringSelectField;
import de.fmannan.addbook.domainmodel.Contact;
import de.fmannan.addbook.domainmodel.Group;
import de.fmannan.addbook.domainmodel.GroupRegistrar;

public class ContactEditorInputAdapter implements IEditorInput, IGenericEditorInput {

    private static Logger log = Logger.getLogger(GroupEditorInputAdapter.class);

    private Contact contact;

    private List<IField> fieldsList;

    /**
	 * Create the adapter. The fieldList determines which fields have to be
	 * displayed by the Editor.
	 * 
	 * @param adaptableObject
	 *            Object to be adapted.
	 * @param adapterType
	 *            Type of the adapter to create.
	 */
    public ContactEditorInputAdapter(Object adaptableObject, Class adapterType) {
        super();
        this.contact = (Contact) adaptableObject;
        fieldsList = new ArrayList<IField>();
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "First Name";
            }

            public String getValue() {
                return contact.getFirstName();
            }

            public void setValue(String value) {
                contact.setFirstName(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Last Name";
            }

            public String getValue() {
                return contact.getLastName();
            }

            public void setValue(String value) {
                contact.setLastName(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getValue() {
                return contact.getPrivateData().getBirthdate();
            }

            public void setValue(String value) {
                contact.getPrivateData().setBirthdate(value);
            }

            public String getLabel() {
                return "Birthdate";
            }
        });
        fieldsList.add(new IStringSelectField() {

            public String getLabel() {
                return "Group";
            }

            public String getValue() {
                if (contact.getParent() != null) {
                    return ((Group) contact.getParent()).getGroupName();
                }
                return "";
            }

            public void setValue(String value) {
                if (!value.equals("")) {
                    if (contact.getParent() != null) ((Group) contact.getParent()).removeContact(contact);
                    GroupRegistrar.getInstance().getGroup(value).addContact(contact);
                }
            }

            public String[] getSelectables() {
                return GroupRegistrar.getInstance().getGroupNames();
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Remarks";
            }

            public String getValue() {
                return contact.getPrivateData().getRemark();
            }

            public void setValue(String value) {
                contact.getPrivateData().setRemark(value);
            }
        });
        fieldsList.add(new IReadOnlyField() {

            public String getLabel() {
                return "";
            }

            public String getValue() {
                return "";
            }
        });
        fieldsList.add(new ISectionField() {

            public String getLabel() {
                return "";
            }

            public String getDescription() {
                return "Private Contact Data";
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Street";
            }

            public String getValue() {
                return contact.getPrivateData().getStreet();
            }

            public void setValue(String value) {
                contact.getPrivateData().setStreet(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "City";
            }

            public String getValue() {
                return contact.getPrivateData().getCity();
            }

            public void setValue(String value) {
                contact.getPrivateData().setCity(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Postal Code";
            }

            public String getValue() {
                return contact.getPrivateData().getPostalCode();
            }

            public void setValue(String value) {
                contact.getPrivateData().setPostalCode(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Country";
            }

            public String getValue() {
                return contact.getPrivateData().getCountry();
            }

            public void setValue(String value) {
                contact.getPrivateData().setCountry(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Phone number";
            }

            public String getValue() {
                return String.valueOf(contact.getPrivateData().getPhoneNumber());
            }

            public void setValue(String value) {
                contact.getPrivateData().setPhoneNumber(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Mobile number";
            }

            public String getValue() {
                return String.valueOf(contact.getPrivateData().getMobilePhoneNumber());
            }

            public void setValue(String value) {
                contact.getPrivateData().setMobilePhoneNumber(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Email";
            }

            public String getValue() {
                return contact.getPrivateData().getEmail();
            }

            public void setValue(String value) {
                contact.getPrivateData().setEmail(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Website";
            }

            public String getValue() {
                return contact.getPrivateData().getWebsite();
            }

            public void setValue(String value) {
                contact.getPrivateData().setWebsite(value);
            }
        });
        fieldsList.add(new IReadOnlyField() {

            public String getLabel() {
                return "";
            }

            public String getValue() {
                return "";
            }
        });
        fieldsList.add(new ISectionField() {

            public String getLabel() {
                return "";
            }

            public String getDescription() {
                return "Office Contact Data";
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Company";
            }

            public String getValue() {
                return contact.getOfficeData().getCompanyName();
            }

            public void setValue(String value) {
                contact.getOfficeData().setCompanyName(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Department";
            }

            public String getValue() {
                return contact.getOfficeData().getDepartment();
            }

            public void setValue(String value) {
                contact.getOfficeData().setDepartment(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Occupation";
            }

            public String getValue() {
                return contact.getOfficeData().getOccupation();
            }

            public void setValue(String value) {
                contact.getOfficeData().setOccupation(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Street";
            }

            public String getValue() {
                return contact.getOfficeData().getStreet();
            }

            public void setValue(String value) {
                contact.getOfficeData().setStreet(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "City";
            }

            public String getValue() {
                return contact.getOfficeData().getCity();
            }

            public void setValue(String value) {
                contact.getOfficeData().setCity(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Postal Code";
            }

            public String getValue() {
                return contact.getOfficeData().getPostalCode();
            }

            public void setValue(String value) {
                contact.getOfficeData().setPostalCode(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Country";
            }

            public String getValue() {
                return contact.getOfficeData().getCountry();
            }

            public void setValue(String value) {
                contact.getOfficeData().setCountry(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Phone number";
            }

            public String getValue() {
                return String.valueOf(contact.getOfficeData().getPhoneNumber());
            }

            public void setValue(String value) {
                contact.getOfficeData().setPhoneNumber(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Mobile number";
            }

            public String getValue() {
                return String.valueOf(contact.getOfficeData().getMobilePhoneNumber());
            }

            public void setValue(String value) {
                contact.getOfficeData().setMobilePhoneNumber(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Email";
            }

            public String getValue() {
                return contact.getOfficeData().getEmail();
            }

            public void setValue(String value) {
                contact.getOfficeData().setEmail(value);
            }
        });
        fieldsList.add(new IStringField() {

            public String getLabel() {
                return "Website";
            }

            public String getValue() {
                return contact.getOfficeData().getWebsite();
            }

            public void setValue(String value) {
                contact.getOfficeData().setWebsite(value);
            }
        });
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    @Override
    public String getName() {
        String name = contact.getName();
        if (name == null) {
            name = "Neu";
        }
        return name;
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    @Override
    public String getToolTipText() {
        return "Modify Contact";
    }

    @Override
    public Object getAdapter(Class adapter) {
        return Platform.getAdapterManager().getAdapter(this, adapter);
    }

    public boolean equals(Object obj) {
        boolean result;
        if (obj == null) {
            result = false;
        } else if (!(obj instanceof ContactEditorInputAdapter)) {
            result = false;
        } else {
            ContactEditorInputAdapter castedObj = (ContactEditorInputAdapter) obj;
            result = this.contact.equals(castedObj.contact);
        }
        return result;
    }

    public int hashCode() {
        return contact.hashCode();
    }

    @Override
    public void commit() {
        if (this.contact.isNewlyCreated()) {
            log.debug("Contact is newly created - trying to commit");
        }
    }

    public Object getAdaptedObject() {
        return contact;
    }

    @Override
    public List<IField> getFieldsList() {
        return fieldsList;
    }

    @Override
    public String getTitle() {
        return "Contact Details";
    }
}
