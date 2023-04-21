package pl.radical.open.gg;

import pl.radical.open.gg.event.ContactListListener;
import java.io.InputStream;
import java.util.Collection;

/**
 * Through this service client can either clear, export or import contact list to the Gadu-Gadu server.
 * <p>
 * Created on 2004-11-27
 * 
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 */
public interface IContactListService {

    /**
	 * Clears user's contact list from the Gadu-Gadu server.
	 * 
	 * @throws GGException
	 *             if there is an error while exporting contact.
	 * @throws GGSessionException
	 *             if we are currently not logged in.
	 */
    void clearContactList() throws GGException;

    /**
	 * Exports user's contact list to the Gadu-Gadu server.
	 * 
	 * @param users
	 *            collection of <code>LocalUser</code> objects.
	 * @throws GGException
	 *             if where is an error while exporting contact list.
	 * @throws GGSessionException
	 *             if we are currently not logged in.
	 * @throws NullPointerException
	 *             if users collection is null.
	 */
    void exportContactList(Collection<LocalUser> users) throws GGException;

    /**
	 * Imports user's contact list from the Gadu-Gadu server.
	 * 
	 * @throws GGException
	 *             if there is an error while importing contact list.
	 * @throws GGSessionException
	 *             if we are currently not logged in.
	 */
    void importContactList() throws GGException;

    void exportContactList(InputStream input) throws GGException;

    /**
	 * Adds <code>ContactListListener</code> object to the list of listeners to be notified of events such as
	 * successfuly importing or exporting contact list.
	 * 
	 * @param contactListListener
	 *            the listener to be notified of contact list events.
	 */
    void addContactListListener(ContactListListener listListener);

    /**
	 * Removes <code>ContactListListener</code> object from the list of listeners that are notified of events such as
	 * successfuly importing or exporting contact list.
	 * 
	 * @param contactListListener
	 *            the listener object to be removed from contact list events notification.
	 */
    void removeContactListlistener(ContactListListener listListener);
}
