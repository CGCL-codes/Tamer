package org.authorsite.mailarchive.model;

/**
 * Representation of an email address in the Mailarchive system.
 * 
 * <p>The interface essentially wraps an email address with some additional
 * useful information.</p>
 * 
 * <p>Such useful information includes whether or not the address corresponds
 * to a mailing list. Mailing lists here are understood to be such lists as might be run
 * by <a href="http://www.greatcircle.com/majordomo/">Majordomo</a>,
 * <a href="http://www.list.org/">GNU Mailman</a> rather than the notion
 * of Group Addresses from rfc 822. These remain a grey area for the
 * Mailarchive application.</p>
 * 
 * <p>An email address may also be said to be associated with a process. This is
 * used to indicate that an email address is typically associated with content that is 
 * computer generated - such as an automated alert, receipt from an e-commerce 
 * site or the like.</p>
 * 
 * <p>Email addresses are often constructed with a <em>Personal Name</em>. This can
 * of course vary and a new one be assigned by a MUA for each use of an email address. This
 * interface takes the somewhat naive approach that for archival purposes one and only Personal
 * Name should be used for each address. The recommended approach is to use the latest one,
 * or to defer to the name encapsulated in the associated <code>Person</code>.</p>
 * 
 * <p>Within the context of the Mailarchive program, a <code>Person</code> may be associated
 * with an email address. Again, this is a little naive - many people may associated, for instance,
 * with a webmaster@example.com email address, and a Person's current name may not be the
 * personal name they used to send email under before a name change. However, for the majority
 * of cases it should be sufficient. Future versions may add the additional complexity if required.</p>
 * 
 * @author jejking
 * @see org.authorsite.mailarchive.model.Person
 * @version $Revision: 1.4 $
 */
public interface EmailAddress extends Identifiable {

    public String getAddress();

    public void setAddress(String newAddress);

    public String getPersonalName();

    public void setPersonalName(String newPersonalName);

    public boolean isMailingList();

    public void setMailingList(boolean newMailingList);

    public boolean isProcess();

    public void setProcess(boolean newProcess);

    public Person getPerson();

    public void setPerson(Person newPerson);
}
