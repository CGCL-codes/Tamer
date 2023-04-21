package net.sf.lastdrive.core;

import net.sf.lastdrive.api.DriveNode;
import net.sf.lastdrive.api.NamedNode;
import net.sf.lastdrive.api.NodeActionException;
import net.sf.lastdrive.api.NodeContainer;
import net.sf.lastdrive.api.NodeName;
import net.sf.lastdrive.api.NodeNameException;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public abstract class AbstractNodeContainer extends BasicNamedNode implements NodeContainer {

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @throws NodeNameException DOCUMENT ME!
     */
    public void foldering(NodeName name) {
        NodeName parent = name.getParentName();
        if (parent == null) {
            throw new NodeNameException(NodeNameException.ErrorCode.PARENT_EXPECTED, name.getPath());
        }
        addFolderIfNotExists(parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean addFolderIfNotExists(NodeName node) {
        NodeName parent = node.getParentName();
        if (parent != null) {
            addFolderIfNotExists(parent);
        }
        if (getManager().nodeExists(node)) {
            return false;
        }
        NamedNode folder = createFolder(node);
        getManager().insert(this, folder, false);
        return true;
    }

    /**
     * Create an absolute name in context of container 
     */
    public String getPath(DriveNode node) throws NodeActionException {
        if (node instanceof NamedNode) {
            String path = ((NamedNode) node).getNodeName().getPath();
            if (getDrive() == null) return path;
            String cPath = getNodeName().getPath();
            String drivePath = getDrive().getRootName().getPath();
            if (path.startsWith(cPath)) return path;
            if (path.startsWith(drivePath)) {
                throw new NodeActionException(NodeActionException.ErrorCode.REFERENCE_INTEGRETY, path);
            }
            return cPath + path;
        }
        throw new NodeActionException(NodeActionException.ErrorCode.UNACCEPTABLE_TYPE, node.getClass().getName());
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract NamedNode createFolder(NodeName name);
}
