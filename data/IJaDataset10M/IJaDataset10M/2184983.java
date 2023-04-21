package skycastle.sound.editor.componentlibrary;

import skycastle.sound.Signal;
import skycastle.sound.producers.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * @author Hans H�ggstr�m
 */
public class ComponentLibrary {

    private final DefaultMutableTreeNode myRootNode = new DefaultMutableTreeNode("Components", true);

    private DefaultTreeModel myLibraryTreeModel = new DefaultTreeModel(myRootNode, true);

    public ComponentLibrary() {
        registerBasicModules();
    }

    public TreeModel getTreeModel() {
        return myLibraryTreeModel;
    }

    private void registerBasicModules() {
        addComponent(ConstantSignal.class);
        addComponent(AddSignal.class);
        addComponent(MultiplySignal.class);
        addComponent(SineWaveSignal.class);
        addComponent(BandLimitedNoiseSignal.class);
    }

    private void addComponent(final Class<? extends Signal> componentClass) {
        final PrimitiveComponentMetadata componentMetadata = new PrimitiveComponentMetadata(componentClass);
        final DefaultMutableTreeNode mutableTreeNode = new DefaultMutableTreeNode(componentMetadata, false);
        myLibraryTreeModel.insertNodeInto(mutableTreeNode, myRootNode, myRootNode.getChildCount());
    }
}
