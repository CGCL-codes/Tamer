package org.primordion.user.app.Swing.SwingTestA;

import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.io.SwingTreeModel;
import org.primordion.xholon.util.MiscRandom;

/**
 * The TreeModel for the SwingTestA sample application.
 * @author Ken Webb
 *
 */
public class SwingTreeModelTestA extends SwingTreeModel {

    public void postConfigure() {
        treeModelRoot = getFirstChild();
        super.postConfigure();
    }

    public void act() {
        int rNum = MiscRandom.getRandomInt(0, 200);
        switch(rNum) {
            case 5:
                {
                    IXholon source = treeModelRoot.getLastChild();
                    IXholon node = source.appendChild("Seven", "a new node");
                    IXholon children[] = new IXholon[1];
                    children[0] = node;
                    IXholon path[] = new IXholon[3];
                    path[0] = treeModelRoot;
                    path[1] = source;
                    path[2] = node;
                    int childIndices[] = new int[1];
                    childIndices[0] = getIndexOfChild(source, node);
                    fireTreeNodesInserted(source, path, childIndices, children);
                    break;
                }
            case 6:
                {
                    IXholon source = treeModelRoot.getLastChild();
                    IXholon node = source.getFirstChild();
                    if (node != null) {
                        IXholon children[] = new IXholon[1];
                        children[0] = node;
                        IXholon path[] = new IXholon[3];
                        path[0] = treeModelRoot;
                        path[1] = source;
                        path[2] = node;
                        int childIndices[] = new int[1];
                        childIndices[0] = getIndexOfChild(source, node);
                        node.removeChild();
                        node.remove();
                        fireTreeNodesRemoved(source, path, childIndices, children);
                    }
                    break;
                }
            case 7:
                {
                    IXholon source = treeModelRoot.getLastChild();
                    IXholon node = source.getLastChild();
                    if (node != null) {
                        node.setRoleName("A CHANGED NODE");
                        IXholon children[] = new IXholon[1];
                        children[0] = node;
                        IXholon path[] = new IXholon[3];
                        path[0] = treeModelRoot;
                        path[1] = source;
                        path[2] = node;
                        int childIndices[] = new int[1];
                        childIndices[0] = getIndexOfChild(source, node);
                        fireTreeNodesChanged(source, path, childIndices, children);
                    }
                    break;
                }
        }
        super.act();
    }
}
