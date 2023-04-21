package openfield.appmanager;

import org.openide.filesystems.FileObject;
import org.openide.nodes.AbstractNode;

/**
 *
 * @author shader
 */
class AppFolderNode extends AbstractNode {

    public AppFolderNode(FileObject o) {
        super(new AppNodeContainer(o));
        setDisplayName(o.getName());
        String iconBase = (String) o.getAttribute("iconBase");
        if (iconBase != null) {
            setIconBaseWithExtension(iconBase);
        }
    }
}
