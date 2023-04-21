package org.gjt.gamp.gamp;

import javax.swing.tree.*;
import javax.vecmath.*;
import javax.media.j3d.*;
import java.util.*;
import java.io.*;
import java.awt.datatransfer.*;
import org.gjt.gamp.persistance.*;

/**
This class binds a SceneObject3D to a MutableTreeNode so that the Swing
jTree classes can be used to manipulate the object hierarchy. It wraps some
of the MutableTreeNode methods so that the Java3D scene graph is updated
when user manipulations are applied to the MutableTreeNode
*/
public class SceneObjectMutableTreeNode implements MutableTreeNode, Transferable, ClipboardOwner, Fileable {

    public static DataFlavor SceneObjectMutableTreeNodeFlavor = new DataFlavor(SceneObjectMutableTreeNode.class, "SceneObjectMutableTreeNode Object");

    SceneObject3D obj;

    private MutableTreeNode parent;

    public SceneObjectMutableTreeNode() {
        obj = null;
        parent = null;
    }

    public SceneObjectMutableTreeNode(SceneObject3D scObj) {
        obj = scObj;
        obj.setTreeNode(this);
    }

    public void insert(MutableTreeNode child, int index) {
        System.out.println("In SceneObjectMutableTreeNode.insert");
        Transform3D childToNewParent = new Transform3D();
        Transform3D tr1 = new Transform3D();
        Transform3D tr2 = new Transform3D();
        SceneObject3D myObj = obj;
        SceneObject3D newChildObj = ((SceneObjectMutableTreeNode) child).obj;
        newChildObj.getLocalToVworld(childToNewParent);
        obj.getLocalToVworld(tr1);
        tr1.invert();
        childToNewParent.mul(tr1);
        tr1.set(childToNewParent);
        tr1.invert();
        newChildObj.objScale.getTransform(tr2);
        childToNewParent.mul(tr2);
        childToNewParent.mul(tr1);
        newChildObj.getRoot().detach();
        myObj.objScale.addChild(newChildObj.getRoot());
        ((SceneObjectMutableTreeNode) child).parent.remove(child);
        ((SceneObjectMutableTreeNode) child).parent = this;
        newChildObj.objScale.setTransform(childToNewParent);
    }

    /**
    Removes the child at index from the receiver.
  	*/
    public void remove(int index) {
        System.out.println("In SceneObjectMutableTreeNode.remove1");
        remove((MutableTreeNode) getChildAt(index));
    }

    /**
    Removes node from the receiver.
  	*/
    public void remove(MutableTreeNode node) {
        System.out.println("In SceneObjectMutableTreeNode.remove2");
        if (node != null) {
            node.removeFromParent();
        }
    }

    /**
    Removes the receiver from its parent.
  	*/
    public void removeFromParent() {
        System.out.println("In SceneObjectMutableTreeNode.removeFromParent");
        obj.objRoot.detach();
        parent = null;
    }

    /**
    Sets the parent of the receiver to newParent.
  	*/
    public void setParent(MutableTreeNode newParent) {
        System.out.println("In SceneObjectMutableTreeNode.setParent");
        parent = newParent;
    }

    public void setUserObject(Object object) {
        throw new RuntimeException("should not be called");
    }

    /**
    Returns the children of the reciever as an Enumeration.
  	*/
    public Enumeration children() {
        throw new RuntimeException("not done yet");
    }

    /**
    Returns true if the receiver allows children.
    */
    public boolean getAllowsChildren() {
        return true;
    }

    /**
    Returns the child TreeNode at index childIndex.
    */
    public TreeNode getChildAt(int childIndex) {
        Vector v = new Vector(12);
        getChildren(v);
        return (TreeNode) ((SceneObject3D) v.elementAt(childIndex)).getTreeNode();
    }

    /**
    Returns the number of children TreeNodes the receiver contains.
    */
    public int getChildCount() {
        Vector v = new Vector(12);
        getChildren(v);
        return v.size();
    }

    /**
    Returns the index of node in the receivers children.
    */
    public int getIndex(TreeNode node) {
        for (int i = 0; i < obj.objScale.numChildren(); i++) {
            if (obj.objScale.getChild(i).getUserData() instanceof SceneObject3D) {
                if (((SceneObject3D) obj.objScale.getChild(i).getUserData()).getTreeNode() == node) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
    Returns the parent TreeNode of the receiver.
    */
    public TreeNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        Vector v = new Vector(24);
        getChildren(v);
        if (v.size() == 0) {
            return true;
        }
        return false;
    }

    /**
    
    */
    private synchronized void getChildren(Vector v) {
        v.removeAllElements();
        for (int i = 0; i < obj.objScale.numChildren(); i++) {
            if (obj.objScale.getChild(i).getUserData() instanceof SceneObject3D) {
                if (obj.objScale.getChild(i).getUserData() != obj) {
                    v.addElement(obj.objScale.getChild(i).getUserData());
                }
            }
        }
    }

    /**
    debugging method that checks this treenode for consistancy
    all children of this treenode should return it as their parent
    this treenode's parent should return it as its child
    */
    public void check() {
        Vector v = new Vector(12);
        getChildren(v);
        for (int i = 0; i < v.size(); i++) {
            if (((SceneObject3D) v.elementAt(i)).getTreeNode().getParent() != this) throw new RuntimeException("MutableTreeNode has child whos parent is not him.");
        }
        if (parent != null) {
            if (parent.getIndex(this) == -1) throw new RuntimeException("node's parent does not have me as it's child");
        }
        if (obj.getTreeNode() != this) throw new RuntimeException("node has obj that doesn't have it");
    }

    public Object getTransferData(DataFlavor flavor) {
        return this;
    }

    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] dfa = { SceneObjectMutableTreeNodeFlavor };
        return dfa;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavor.equals(SceneObjectMutableTreeNodeFlavor)) return true; else return false;
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    public void putProperties(Shuttle s) throws IOException {
        s.putProperty("obj", s.FILEABLE, obj);
        if (parent.getClass() == DefaultMutableTreeNode.class) {
            s.putProperty("parent", s.FILEABLE, null);
        } else {
            s.putProperty("parent", s.FILEABLE, (SceneObjectMutableTreeNode) parent);
        }
    }

    public void acceptProperty(String propertyName, Object value) {
        if (propertyName.equals("obj")) {
            obj = (SceneObject3D) value;
        } else if (propertyName.equals("parent")) {
            if (value != null) parent = (MutableTreeNode) value;
        }
    }

    public int fileableVersionNum() {
        return 1;
    }
}
