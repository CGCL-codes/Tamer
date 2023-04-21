package org.fpdev.apps.rtemaster.actions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.fpdev.core.data.LegacyDB;
import org.fpdev.core.basenet.BLink;
import org.fpdev.core.data.SpatialDataConnection.LinkAttr;
import org.fpdev.apps.rtemaster.RouteMaster;

/**
 *
 * @author demory
 */
public class RenameLinksAction implements ACAction {

    private Map<BLink, String> links_;

    private String newName_;

    /** Creates a new instance of CUpdateAddressRangeAction */
    public RenameLinksAction(Set<BLink> links, String newName) {
        links_ = new HashMap<BLink, String>();
        for (Iterator<BLink> linkIter = links.iterator(); linkIter.hasNext(); ) {
            BLink link = linkIter.next();
            links_.put(link, link.getDisplayName());
        }
        newName_ = newName;
    }

    public String getName() {
        return "Rename Links";
    }

    public boolean doAction(RouteMaster av) {
        for (Iterator<BLink> linkIter = links_.keySet().iterator(); linkIter.hasNext(); ) {
            linkIter.next().setName(newName_);
        }
        av.getEngine().getDataPackage().getSpatialDataConn().modifyLinks(links_.keySet(), new LinkAttr[] { LinkAttr.NAME });
        return true;
    }

    public boolean undoAction(RouteMaster av) {
        for (Iterator<BLink> linkIter = links_.keySet().iterator(); linkIter.hasNext(); ) {
            BLink link = linkIter.next();
            link.setName(links_.get(link));
        }
        av.getEngine().getDataPackage().getSpatialDataConn().modifyLinks(links_.keySet(), new LinkAttr[] { LinkAttr.NAME });
        return true;
    }
}
