package net.sf.jpim.contact.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sf.mailsomething.mail.MailAddress;

public class CommunicationsImpl implements Communications {

    protected List m_PhoneNumbers;

    protected List m_EmailAddresses;

    protected String m_Mailer;

    protected int m_PreferredEmail;

    protected int m_PreferredNumber;

    protected MailAddress preferredAddress;

    public CommunicationsImpl() {
        m_PhoneNumbers = Collections.synchronizedList(new ArrayList(5));
        m_EmailAddresses = Collections.synchronizedList(new ArrayList(5));
    }

    public Iterator getPhoneNumbers() {
        return m_PhoneNumbers.iterator();
    }

    public PhoneNumber[] listPhoneNumbers() {
        PhoneNumber[] numbers = new PhoneNumber[m_PhoneNumbers.size()];
        return (PhoneNumber[]) m_PhoneNumbers.toArray(numbers);
    }

    public PhoneNumber getPhoneNumber(int num) throws IndexOutOfBoundsException {
        return (PhoneNumber) m_PhoneNumbers.get(num);
    }

    public void addPhoneNumber(PhoneNumber number) {
        for (int i = 0; i < getPhoneNumberCount(); i++) {
            if (getPhoneNumber(i).getNumber().equals(number.getNumber())) {
                return;
            }
        }
        m_PhoneNumbers.add(number);
    }

    public void removePhoneNumber(PhoneNumber number) {
        m_PhoneNumbers.remove(number);
    }

    public PhoneNumber[] listPreferredPhoneNumbers() {
        ArrayList list = new ArrayList(m_PhoneNumbers.size());
        for (Iterator iterator = m_PhoneNumbers.iterator(); iterator.hasNext(); ) {
            PhoneNumber num = (PhoneNumber) iterator.next();
            if (num.isPreferred()) {
                list.add(num);
            }
        }
        PhoneNumber[] numbers = new PhoneNumber[list.size()];
        return (PhoneNumber[]) list.toArray(numbers);
    }

    public PhoneNumber[] listPhoneNumbersByType(int TYPE) {
        ArrayList list = new ArrayList(m_PhoneNumbers.size());
        for (Iterator iterator = m_PhoneNumbers.iterator(); iterator.hasNext(); ) {
            PhoneNumber num = (PhoneNumber) iterator.next();
            if (num.isType(TYPE)) {
                list.add(num);
            }
        }
        PhoneNumber[] numbers = new PhoneNumber[list.size()];
        return (PhoneNumber[]) list.toArray(numbers);
    }

    public int getPhoneNumberCount() {
        return m_PhoneNumbers.size();
    }

    public Iterator getEmailAddresses() {
        return m_EmailAddresses.iterator();
    }

    public MailAddress[] listMailAddresses() {
        MailAddress[] emails = new MailAddress[m_EmailAddresses.size()];
        return (MailAddress[]) m_EmailAddresses.toArray(emails);
    }

    public MailAddress getMailAddress(int num) throws IndexOutOfBoundsException {
        return (MailAddress) m_EmailAddresses.get(num);
    }

    public void addMailAddress(MailAddress email) {
        m_EmailAddresses.add(email);
    }

    public void removeMailAddress(MailAddress email) {
        m_EmailAddresses.remove(email);
    }

    public MailAddress[] listPreferredEmailAddresses() {
        ArrayList list = new ArrayList(m_EmailAddresses.size());
        for (Iterator iterator = m_EmailAddresses.iterator(); iterator.hasNext(); ) {
            MailAddress addr = (MailAddress) iterator.next();
        }
        MailAddress[] addrs = new MailAddress[list.size()];
        return (MailAddress[]) list.toArray(addrs);
    }

    public int getMailAddressCount() {
        return m_EmailAddresses.size();
    }

    public String getMailer() {
        return m_Mailer;
    }

    public void setMailer(String mailer) {
        m_Mailer = mailer;
    }

    public MailAddress getPreferredAddress() {
        if (preferredAddress == null && !m_EmailAddresses.isEmpty()) return (MailAddress) m_EmailAddresses.get(0);
        return preferredAddress;
    }

    public void setPreferredAddress(MailAddress address) {
        preferredAddress = address;
    }
}
