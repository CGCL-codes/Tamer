package org.nmc.pachyderm.authoring;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import ca.ucalgary.apollo.core.*;
import ca.ucalgary.apollo.app.*;
import ca.ucalgary.apollo.authentication.simple.*;
import java.net.*;
import org.nmc.pachyderm.foundation.*;
import java.util.GregorianCalendar;

/**
 *  A "simple" class to let users edit their own accounts (pulling the user from session) OR for admins to edit anyone's account (using the setPerson() method to get the appropriate user set).
 *
 * @author     dnorman
 * @created    June 5, 2005
 */
public class EditAccount extends MCPage {

    /**
	 *  The CXDirectory person being edited.
	 */
    private CXDirectoryPerson person;

    /**
	 *  The username of the CXDirectoryPerson being edited - not a direct property of the CXDirectoryPerson, so some lookup is done against the authentication database.
	 */
    public String username;

    /**
	 *  First attempt at an edited password. Must match password2 to be successful.
	 */
    public String password1;

    /**
	 *  Second attempt at an edited password. Must match password2 to be successful.
	 */
    public String password2;

    /**
	 *  A generic error message to be displayed.
	 */
    public String message;

    public String uri;

    /**
	 *  Constructor for the EditAccount object
	 *
	 * @param  context  The WOContext. Generic WO stuff.
	 */
    public EditAccount(WOContext context) {
        super(context);
        try {
            NSArray simpleAuthSourcesArray = (NSArray) PXUtility.defaultObjectForKey("SimpleAuthSources");
            System.out.println("\nsimpleAuthSourcesArray: " + simpleAuthSourcesArray + "\n");
            NSDictionary simpleAuthSourcesPrimary = (NSDictionary) simpleAuthSourcesArray.objectAtIndex(0);
            uri = (String) simpleAuthSourcesPrimary.valueForKey("uri");
        } catch (Exception e) {
            System.out.println("Cannot get value of 'uri' from 'SimpleAuthSources' key in defaults db\n");
        }
    }

    /**
	 *  Setter, called to externally set the CXDirectoryPerson being edited - like from a ListAccounts page, for instance...
	 *
	 * @param  _person  The new person value
	 */
    public void setPerson(CXDirectoryPerson _person) {
        if (_person == null) {
            Session session = (Session) session();
            _person = session.person();
        }
        NSArray authmaps = (NSArray) _person.valueForProperty("authmaps");
        EOEnterpriseObject auth = (EOEnterpriseObject) authmaps.lastObject();
        String externalID = (String) auth.valueForKey("externalId");
        username = externalID.substring(0, (externalID.indexOf("@")));
        person = _person;
    }

    /**
	 *  Getter, to retrieve the private person value
	 *
	 * @return    The person being edited
	 */
    public CXDirectoryPerson person() {
        if (person == null) {
            setPerson(null);
        }
        return person;
    }

    /**
	 *  Getter to handle pulling a value from a CXMultiValue for display in a simple text field.
	 *
	 * @return    The String value of the primary email address for this user's EmailProperty CXMultiValue
	 */
    public String email() {
        CXMultiValue email = (CXMultiValue) person.valueForProperty(CXDirectoryPerson.EmailProperty);
        return (email != null) ? (String) email.primaryValue() : null;
    }

    /**
	 *  Setter to receive a String, wrap it in a CXMultiValue, and stick it into the CXDirectoryPerson's EmailProperty
	 *
	 * @param  _email  The new email value
	 */
    public void setEmail(String _email) {
        CXMutableMultiValue emailValue = new CXMutableMultiValue();
        emailValue.addValueWithLabel(_email, "work");
        person.setValueForProperty(emailValue.immutableClone(), CXDirectoryPerson.EmailProperty);
    }

    /**
	 *  Save the edited user. Saves user info, and password if provided properly.
	 *
	 * @return    WOComponent to return to (null/this)
	 */
    public WOComponent saveAccount() {
        message = "";
        if (person != null) {
            if ((password1 != null) && (password1.equals(password2))) {
                NSMutableDictionary personDict = new NSMutableDictionary();
                personDict.takeValueForKey(username, "username");
                personDict.takeValueForKey(password1, "password");
                try {
                    SimpleAuthentication sa = new SimpleAuthentication(new URI(uri));
                    boolean result = false;
                    if (sa._checkUserExists(username)) {
                        result = sa._updateUser(personDict);
                    } else {
                        sa._addUser(username, password1);
                        result = sa._checkUserExists(username);
                    }
                    if (result == false) {
                        message = "There was an error saving your modified account information";
                    }
                } catch (Exception e) {
                    System.out.println("EditAccount.saveAccount exception saving password");
                    e.printStackTrace();
                }
            }
            try {
                CXDirectoryServices.sharedServices().save();
            } catch (Exception e) {
                System.out.println("saveAccount() exception!");
                e.printStackTrace();
            }
        }
        return context().page();
    }

    public MCContext d2wContext() {
        MCContext ctx = localContext();
        if (ctx == null) {
            ctx = new MCContext();
            ctx.takeValueForKey("editAccountInvitation", "task");
        }
        return ctx;
    }
}
