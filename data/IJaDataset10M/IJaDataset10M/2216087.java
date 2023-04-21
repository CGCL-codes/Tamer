package net.sourceforge.geeboss.view.tree;

import net.sourceforge.geeboss.model.midi.roland.RolandFile;
import net.sourceforge.geeboss.model.midi.roland.RolandVirtualDevice;
import net.sourceforge.geeboss.util.ImageUtil;
import org.eclipse.swt.graphics.Image;

/**
 * A file node
 * @author <a href="mailto:fborry@free.fr">Frederic BORRY</a>
 */
public class FileNode extends RootNode {

    /** The associated device */
    private RolandFile mFile;

    /**
     * Create a new PatchTreeNode given a parent node and the holding patch tree
     * @param patchTree the holding patch tree
     */
    public FileNode(PatchTree patchTree) {
        super(patchTree);
        mFile = (RolandFile) patchTree.getDevice();
        setupNode();
    }

    /**
     * Returns the device associated to this tree
     * @return the device associated to this tree 
     */
    public RolandVirtualDevice getDevice() {
        return mFile;
    }

    /**
     * Returns the type of this node
     * @return the type of this node
     */
    public Type getType() {
        return Type.FILE;
    }

    /**
     * Returns the name of this node
     * @return the name of this node
     */
    public String getName() {
        return addDirtyDecoration(mFile.getName());
    }

    /**
     * Returns the implementation specific's icon
     * @param  empty if true returns the empty icon if applicable
     * @return the implementation specific's icon
     */
    protected Image getIcon(boolean empty) {
        return empty ? ImageUtil.mIconFileEmpty : ImageUtil.mIconFile;
    }
}
