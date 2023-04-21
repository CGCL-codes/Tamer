package org.mitre.rt.client.ui.search.searchers;

import java.util.*;
import org.apache.xmlbeans.*;
import org.mitre.rt.rtclient.*;
import org.mitre.rt.rtclient.RTDocument.*;
import org.mitre.rt.rtclient.RTDocument.RT.*;
import org.mitre.rt.rtclient.ApplicationType.*;

/**
 *
 * @author BWORRELL
 */
public class ProfileSearcher extends AbsSearcher {

    public ProfileSearcher(ApplicationType app) {
        super(app);
    }

    @Override
    protected IdedVersionedItemType findOwner(IdedVersionedItemType item) {
        if (item instanceof ProfileType) return item; else return null;
    }

    @Override
    protected Map<IdedVersionedItemType, Integer> doSearch(String query) {
        Map<IdedVersionedItemType, Integer> results = new HashMap<IdedVersionedItemType, Integer>();
        Profiles profiles = this.app.getProfiles();
        if (profiles != null) {
            for (ProfileType profile : profiles.getProfileList()) {
                XmlObject[] objects = null;
                try {
                    objects = profile.selectPath(query);
                } catch (Exception ex) {
                    objects = new XmlObject[1];
                }
                if (objects != null && objects.length > 0) results.put(profile, objects.length);
            }
        }
        return results;
    }
}
