package org.fpdev.apps.admin.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.fpdev.core.data.LegacyDB;
import org.fpdev.core.basenet.BLink;
import org.fpdev.core.basenet.BNode;
import org.fpdev.core.transit.Station;
import org.fpdev.apps.admin.AdminClient;

/**
 *
 * @author demory
 */
public class DeleteNodeAction implements ACAction {

    private BNode node_;

    private Station station_;

    private String staNodeName_;

    private List<DeleteLinkAction> delLinkActions_;

    private boolean updateDB_;

    /** Creates a new instance of DeleteNodeAction */
    public DeleteNodeAction(BNode node, boolean updateDB) {
        node_ = node;
        updateDB_ = updateDB;
    }

    public String getName() {
        return "Delete Node";
    }

    public boolean doAction(AdminClient av) {
        Set<BLink> linkSet = av.getEngine().getBaseNet().incidentLinks(node_);
        for (Iterator<BLink> links = linkSet.iterator(); links.hasNext(); ) {
            BLink link = links.next();
            if (link.registeredPathCount() > 0) {
                av.msg("Delete failed: adjacent link belongs to active path");
                return false;
            }
        }
        delLinkActions_ = new LinkedList<DeleteLinkAction>();
        for (Iterator<BLink> links = linkSet.iterator(); links.hasNext(); ) {
            DeleteLinkAction dla = new DeleteLinkAction(links.next(), updateDB_);
            dla.doAction(av);
            delLinkActions_.add(dla);
        }
        av.getEngine().getBaseNet().deleteNode(node_);
        if (node_.isStation()) {
            station_ = node_.getStation();
            staNodeName_ = node_.getStaNodeName();
            node_.getStation().removeNode(node_);
        }
        if (updateDB_) {
            av.getEngine().getDataPackage().getSpatialDataConn().deleteNode(node_);
        }
        return true;
    }

    public void updateDB(LegacyDB db) {
    }

    public boolean undoAction(AdminClient av) {
        CreateNodeAction cna = new CreateNodeAction(node_, updateDB_);
        cna.doAction(av);
        if (station_ != null) {
            station_.addNode(node_, staNodeName_);
        }
        for (Iterator<DeleteLinkAction> dlas = delLinkActions_.iterator(); dlas.hasNext(); ) {
            dlas.next().undoAction(av);
        }
        return true;
    }
}
