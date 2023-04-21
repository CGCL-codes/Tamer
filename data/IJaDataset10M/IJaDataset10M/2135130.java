package org.mpn.contacts.importer;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.data.extensions.OrgName;
import com.google.gdata.data.extensions.OrgTitle;
import com.google.gdata.data.extensions.Organization;
import com.google.gdata.data.extensions.PhoneNumber;
import org.apache.log4j.Logger;
import org.mpn.contacts.framework.db.Row;
import org.mpn.contacts.ui.Data;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

/**
 * todo [!] Create javadocs for org.mpn.contacts.importer.ExportGmail here
 *
 * @author <a href="mailto:pmoukhataev@jnetx.ru">Pavel Moukhataev</a>
 * @version $Revision: 21 $
 */
public class ExportGmailWww {

    static final Logger log = Logger.getLogger("slee.ExportGmail");

    private static final Map<String, String> IM_TYPES = new HashMap<String, String>();

    private static final Map<Long, String> organizationNames = new HashMap<Long, String>();

    static {
        IM_TYPES.put(Data.IM_TYPE_ICQ, "ICQ");
        IM_TYPES.put(Data.IM_TYPE_JABBER, "JABBER");
        IM_TYPES.put(Data.IM_TYPE_SKYPE, "SKYPE");
        IM_TYPES.put(Data.IM_TYPE_MSN, "MSN");
        for (Row row : Data.organizationTable) {
            organizationNames.put(row.getData(Data.organizationTable.id), row.getData(Data.organizationName));
        }
    }

    public void doExportGmail(String login, String password) throws Exception {
        ContactsService myService = new ContactsService("jContacts");
        myService.setUserCredentials(login + "@gmail.com", password);
        URL feedUrl = new URL("http://www.google.com/m8/feeds/contacts/" + login + "@gmail.com/full");
        ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
        Set<String> legacyEmails = new HashSet<String>();
        entriesCycle: for (ContactEntry entry : resultFeed.getEntries()) {
            for (Email email : entry.getEmailAddresses()) {
                String[] emailStrings = email.getAddress().split(";");
                for (String emailString : emailStrings) {
                    if (emailString.trim().length() == 0) return;
                    if (emailString.endsWith("icq.matrix.xfyre.com") || emailString.endsWith("icq.jabber.snc.ru")) {
                        legacyEmails.add(emailString);
                    }
                    Row personRow = searchPersonByEmail(emailString.toLowerCase());
                    if (personRow != null) {
                        if (exportPerson(entry, personRow)) {
                            entry.update();
                        }
                        continue entriesCycle;
                    }
                }
            }
            if (legacyEmails.isEmpty()) continue;
            entry.getEmailAddresses().removeAll(legacyEmails);
            if (entry.getEmailAddresses().isEmpty()) entry.delete(); else entry.update();
        }
    }

    private Row searchPersonByEmail(String emailString) {
        for (Row personMessagingRow : Data.personMessagingTable) {
            String messagingId = personMessagingRow.getData(Data.personMessagingId);
            if (messagingId.equals(emailString)) {
                Long personId_Messaging = personMessagingRow.getData(Data.personTable.id);
                for (Row personRow : Data.personTable) {
                    Long personId = personRow.getData(Data.personTable.id);
                    if (personId_Messaging.equals(personId)) return personRow;
                }
            }
        }
        return null;
    }

    private boolean exportPerson(ContactEntry entry, Row personRow) {
        List<Im> imAddresses = new ArrayList<Im>();
        List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
        List<Email> emails = new ArrayList<Email>();
        String email = null;
        String companyEmail = null;
        String company = null;
        String companyPosition = null;
        String icq = null;
        Long personId = personRow.getData(Data.personTable.id);
        for (Row personMessagingRow : Data.personMessagingTable) {
            if (!personMessagingRow.getData(Data.personTable.id).equals(personId)) continue;
            String type = personMessagingRow.getData(Data.personMessagingType);
            String id = personMessagingRow.getData(Data.personMessagingId);
            if (type.equals(Data.IM_TYPE_EMAIL)) {
                if (id.startsWith("pmoukhataev") || id.startsWith("pavel.moukhataev")) return false;
                if (id.contains("@vc.") || id.contains("datastations") || id.contains("valuecommerce") || id.contains("looksmart")) {
                } else if (id.contains("artezio")) {
                    company = "Artezio";
                    companyEmail = id;
                } else if (id.contains("jnetx")) {
                    companyEmail = id;
                } else {
                    if (email == null) {
                        email = id;
                    } else {
                        emails.add(createEmail(id, false, Email.Rel.HOME));
                    }
                }
            } else if (type.equals(Data.IM_TYPE_MOBILE)) {
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setPhoneNumber(id);
                phoneNumber.setRel(PhoneNumber.Rel.HOME);
                phoneNumbers.add(phoneNumber);
            } else {
                if (type.equals(Data.IM_TYPE_ICQ)) {
                    emails.add(createEmail(id + "@icq.highsecure.ru", false, Email.Rel.HOME));
                    icq = id;
                }
                String imType = IM_TYPES.get(type);
                if (imType == null) {
                    log.error("IM TYPE not found : " + type);
                }
                imAddresses.add(new Im(id, null, Boolean.FALSE, imType, Im.Rel.HOME));
            }
        }
        for (Row personOrganizationRow : Data.personOrganizationTable) {
            if (personOrganizationRow.getData(Data.personTable.id).equals(personId)) {
                company = organizationNames.get(personOrganizationRow.getData(Data.organizationTable.id));
                String companyDepartment = personOrganizationRow.getData(Data.personOrganizationDepartment);
                if (companyDepartment != null) {
                    company = company + " (" + companyDepartment + ")";
                }
                companyPosition = personOrganizationRow.getData(Data.personOrganizationPosition);
                String companyPhoneString = personOrganizationRow.getData(Data.phone);
                if (companyPhoneString != null) {
                    PhoneNumber companyPhone = new PhoneNumber();
                    companyPhone.setPhoneNumber(companyPhoneString);
                    companyPhone.setRel(PhoneNumber.Rel.WORK);
                    phoneNumbers.add(companyPhone);
                }
                break;
            }
        }
        StringBuilder name = new StringBuilder();
        appendString(name, " ", personRow.getData(Data.personFirstName));
        appendString(name, " ", personRow.getData(Data.personMiddleName));
        appendString(name, " ", personRow.getData(Data.personLastName));
        if (email == null && emails.isEmpty()) {
            return false;
        }
        if (name.length() == 0) {
            if (icq != null) name.append(icq); else if (email != null) name.append(email);
        }
        log.info(name);
        entry.getEmailAddresses().clear();
        entry.getEmailAddresses().addAll(emails);
        entry.setTitle(new PlainTextConstruct(name.toString()));
        if (email != null) {
            Email primaryEmail = createEmail(email, true, Email.Rel.GENERAL);
            primaryEmail.setLabel("Primary");
            entry.getEmailAddresses().add(primaryEmail);
        }
        if (companyEmail != null) {
            entry.getEmailAddresses().add(createEmail(email, false, Email.Rel.WORK));
        }
        if (company != null) {
            Organization organization = new Organization(null, Boolean.TRUE, Organization.Rel.WORK);
            organization.setOrgName(new OrgName(company));
            organization.setOrgTitle(new OrgTitle(company));
            entry.setExtension(organization);
        }
        if (!imAddresses.isEmpty()) {
            entry.getImAddresses().clear();
            entry.getImAddresses().addAll(imAddresses);
        }
        if (!phoneNumbers.isEmpty()) {
            entry.getPhoneNumbers().clear();
            entry.getPhoneNumbers().addAll(phoneNumbers);
        }
        return true;
    }

    static Email createEmail(String emailString, boolean primary, String rel) {
        Email email = new Email();
        email.setAddress(emailString);
        email.setPrimary(primary);
        email.setRel(rel);
        return email;
    }

    static void appendString(StringBuilder value, String delimiter, String... values) {
        appendString(value, delimiter, Arrays.asList(values));
    }

    static void appendString(StringBuilder value, String delimiter, Collection<String> values) {
        for (String s : values) {
            if (s != null) {
                s = s.trim();
                if (s.length() > 0) {
                    if (value.length() > 0) {
                        value.append(delimiter);
                    }
                    value.append(s);
                }
            }
        }
    }
}
