package org.cilogon.service.util;

import org.cilogon.service.storage.ArchivedUser;
import org.cilogon.service.storage.User;
import org.junit.Test;
import java.net.URI;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Nov 24, 2010 at  12:38:42 PM
 */
public class ArchivedUserStoreTest extends ServiceTestBase {

    @Test
    public void testArchiveUserStore() throws Exception {
        URI uri = URI.create("http://cilogon.org/serverA/users/119");
        User user = newUser();
        String oldName = user.getFirstName();
        user.setFirstName("Roderick");
        URI archivedUserUID = getArchivedUserStore().archiveUser(user.getUid());
        getArchivedUserStore().containsKey(archivedUserUID);
        ArchivedUser archivedUser = getArchivedUserStore().get(archivedUserUID);
        User oldUser = archivedUser.getUser();
        assert oldUser.getUid().equals(user.getUid());
        assert oldUser.getLastName().equals(user.getLastName());
        assert oldUser.getEmail().equals(user.getEmail());
        assert oldUser.getIDPName().equals(user.getIDPName());
        assert oldUser.getFirstName().equals(oldName);
        assert oldUser.getSerialIdentifier().equals(user.getUid());
        assert oldUser.getCreationTime().equals(user.getCreationTime());
    }

    @Test
    public void testLastArchivedUser() throws Exception {
        User user = newUser();
        assert getArchivedUserStore().getLastArchivedUser(user.getUid()) == null;
        for (int i = 0; i < count; i++) {
            user.setFirstName("Bob-" + i);
            getArchivedUserStore().archiveUser(user.getUid());
            getUserStore().update(user);
        }
        assert getArchivedUserStore().getLastArchivedUser(user.getUid()).getUser().getFirstName().equals("Bob-" + (count - 2));
    }
}
