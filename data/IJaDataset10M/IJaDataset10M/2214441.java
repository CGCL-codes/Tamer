package net.sourceforge.openconferencer.client.contact;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.openconferencer.client.Activator;
import net.sourceforge.openconferencer.client.Messages;
import net.sourceforge.openconferencer.client.util.LogHelper;
import org.eclipse.core.runtime.Platform;
import com.skype.ContactList;
import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User.Status;

/**
 * @author aleksandar
 */
public class SkypeProvider implements IContactProvider {

    private SkypeContactDisplay contactDisplay = new SkypeContactDisplay();

    /**
     * 
     */
    public SkypeProvider() {
        loadLibraries();
    }

    /**
     * 
     */
    @SuppressWarnings("deprecation")
    protected void loadLibraries() {
        try {
            URL url = Platform.getBundle(Activator.PLUGIN_ID).getEntry("/");
            url = Platform.asLocalURL(url);
            File libDirectory = new File(url.getPath(), "lib");
            String libraryPath = System.getProperty("java.library.path");
            libraryPath += File.pathSeparator + libDirectory.getAbsolutePath();
            System.setProperty("java.library.path", libraryPath);
        } catch (Exception ex) {
            LogHelper.error("Error occured starting Skype Provider.", ex);
        }
    }

    @Override
    public ContactInformation[] getAllContacts() throws ProviderUnavailableException {
        try {
            return getSkypeContacts(false);
        } catch (Throwable e) {
            throw new ProviderUnavailableException(Messages.exception_skype_client, e);
        }
    }

    @Override
    public ContactInformation[] getAvailableContacts() throws ProviderUnavailableException {
        try {
            return getSkypeContacts(true);
        } catch (Throwable e) {
            throw new ProviderUnavailableException(Messages.exception_skype_client, e);
        }
    }

    /**
	 * @param online
	 * @return
	 */
    protected ContactInformation[] getSkypeContacts(final boolean online) throws SkypeException {
        ContactList contactList = Skype.getContactList();
        Friend friends[] = contactList.getAllFriends();
        List<ContactInformation> contacts = new ArrayList<ContactInformation>();
        for (Friend f : friends) {
            if (online && f.getStatus() == Status.OFFLINE) continue;
            SkypeContactInformation ci = new SkypeContactInformation();
            ci.setId(f.getId());
            ci.setName(f.getDisplayName());
            ci.setFullName(f.getFullName());
            ci.setCity(f.getCity());
            ci.setCountry(f.getCountry());
            ci.setStatus(f.getStatus());
            contacts.add(ci);
        }
        ContactInformation cif[] = new ContactInformation[contacts.size()];
        contacts.toArray(cif);
        Arrays.sort(cif);
        return cif;
    }

    @Override
    public IContactDisplay getContactDisplay() {
        return contactDisplay;
    }

    @Override
    public void closeProvider() {
    }
}
