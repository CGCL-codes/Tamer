package org.greenscape.opencontact;

import org.greenscape.opencontact.api.ContactService;
import org.greenscape.opencontact.api.ContactGroup;
import org.greenscape.opencontact.api.Contact;
import org.greenscape.opencontact.api.GroupAccount;
import java.util.List;
import java.util.Map;
import org.greenscape.openaccount.Account;
import org.greenscape.openaccount.AccountService;
import org.greenscape.openaccount.Service;
import org.greenscape.persistence.PersistenceService;

/**
 * Synchronizes the local data cache with online data
 * @author smsajid
 */
public class ContactSynchronizer implements Runnable {

    Map<String, Service> services;

    public ContactSynchronizer(Map<String, Service> services) {
        this.services = services;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        List<ContactGroup> localGroups = PersistenceService.getDefault().executeNamedQuery("findAllGroups");
        List<Contact> localContacts = PersistenceService.getDefault().executeNamedQuery("findAllContacts");
        for (Account account : AccountService.getActiveAccounts()) {
            ContactService service = (ContactService) services.get(account.getProvider());
            Map<String, ContactGroup> groups = service.getGroups(account);
            for (ContactGroup group : groups.values()) {
                update(group, localGroups);
            }
        }
    }

    private void update(ContactGroup group, List<ContactGroup> localGroups) {
        ContactGroup cg = null;
        boolean found = false;
        for (ContactGroup g : localGroups) {
            if (g.getName().equalsIgnoreCase(group.getName())) {
                found = true;
                cg = g;
                String loginId = group.getGroupAccounts().get(0).getAccount().getLoginId();
                boolean exists = false;
                for (GroupAccount ga : cg.getGroupAccounts()) {
                    if (ga.getAccount().getLoginId().equalsIgnoreCase(loginId)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    cg.getGroupAccounts().add(group.getGroupAccounts().get(0));
                }
                break;
            }
        }
        if (!found) {
            cg = group;
        }
        if (cg != null) {
            PersistenceService.getDefault().persist(cg);
        }
    }
}
