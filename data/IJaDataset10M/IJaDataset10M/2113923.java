package net.kano.joustsim.oscar.oscar.service.ssi;

import net.kano.joustsim.Screenname;
import java.util.Set;

public interface PermissionListListener {

    void handlePrivacyModeChange(PermissionList list, PrivacyMode oldMode, PrivacyMode newMode);

    void handleBuddyBlocked(PermissionList list, Set<Screenname> oldBlocked, Set<Screenname> newBlocked, Screenname sn);

    void handleBuddyUnblocked(PermissionList list, Set<Screenname> oldBlocked, Set<Screenname> newBlocked, Screenname sn);

    void handleBuddyAllowed(PermissionList list, Set<Screenname> oldAllowed, Set<Screenname> newAllowed, Screenname sn);

    void handleBuddyRemovedFromAllowList(PermissionList list, Set<Screenname> oldAllowed, Set<Screenname> newAllowed, Screenname sn);
}
