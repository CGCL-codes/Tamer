package edu.berkeley.guir.quill;

// See end of file for software license.

import edu.berkeley.guir.quill.gesturelib.*;
import edu.berkeley.guir.quill.util.CollectionListener;
import edu.berkeley.guir.quill.util.CollectionEvent;
import edu.berkeley.guir.quill.util.CollectionAdapter;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.beans.*;

/** 
 * <P>
 * This software is distributed under the 
 * <A HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt">
 * Berkeley Software License</A>.
 
*/
public class GestureTreeModel extends DefaultTreeModel {
  protected EventListenerList modelListenerList = new EventListenerList();

  protected GesturePackage	gesturePackage;
  protected GestureTreeNode	testRootNode;
  protected GestureTreeNode	trainingRootNode;

  public GestureTreeModel(GesturePackage gPackage)
  {
    // there is no empty constructor, which is stupid, so pass a dummy
    super(new DefaultMutableTreeNode(), true);
    gesturePackage = gPackage;
    setRoot(buildTree(gPackage));
  }

  public TreeNode getTestRootNode()
  {
    return testRootNode;
  }

  public TreeNode getTrainingRootNode()
  {
    return trainingRootNode;
  }

  public GesturePackage getGesturePackage()
  {
    return gesturePackage;
  }

  /** create a tree from a GesturePackage and return its root */
  protected GestureTreeNode buildTree(GesturePackage gesturePackage)
  {
    GestureTreeNode rootNode = buildTree((GestureObject) gesturePackage);
    trainingRootNode = (GestureTreeNode) rootNode.getChildAt(0);
    testRootNode = (GestureTreeNode) rootNode.getChildAt(1);

    return rootNode;
  }

  /*
  protected void buildTestNodes()
  {
    // note: may need to change the next line if we ever need to
    // lookup TreeNodes from user objects
    testRootNode.removeAllChildren();
    for (Iterator setIter = gesturePackage.getTestSets().iterator();
	 setIter.hasNext();) {
      GestureSet testSet = (GestureSet) setIter.next();
      System.err.println("Adding test set: " + testSet.getName());
      testRootNode.add(buildTree(testSet));
    }
  }
  */

  protected GestureTreeNode
    buildTree(GestureObject gestureObject)
  {
    GestureTreeNode node = makeTreeNode(gestureObject);
    if (node.getAllowsChildren()) {
      addChildren((GestureContainer) gestureObject, node);
    }
    return node;
  }

  /** Sometimes the root needs to be special, so break this out of
      buildTree.  Doesn't add Gestures to the tree. */
  protected GestureTreeNode
    addChildren(GestureContainer gestureContainer,
		GestureTreeNode root)
  {
    for (Iterator iter = gestureContainer.iterator(); iter.hasNext();) {
      GestureObject gestureObj = (GestureObject) iter.next();
      if (gestureObj instanceof GestureContainer) {
	GestureTreeNode node = buildTree(gestureObj);
	root.add(node);
      }
    }
    return root;
  }

  /* Use this instead of "new GestureTreeNode" so we can implement
     reverse lookup someday if necessary. */
  protected GestureTreeNode makeTreeNode(GestureObject userObj)
  {
    return makeTreeNode(userObj, !(userObj instanceof GestureObject) ||
			((userObj instanceof GestureContainer) &&
			!(userObj instanceof GestureCategory)));
  }

  /* Use this instead of "new GestureTreeNode" so we can implement
     reverse lookup someday if necessary. */
  protected GestureTreeNode makeTreeNode(GestureObject userObj,
					 boolean allowsChildren)
  {
    GestureTreeNode result;
    if (userObj instanceof Gesture) {
      result = new GestureTreeNode(userObj, allowsChildren) {
	public String toString() {
	  return "example";
	}
      };
    }
    else {
      result = new GestureTreeNode(userObj, allowsChildren);
    }
    return result;
  }

  /** Same as {@link #insertObjects(TreeNode,int,List)} except takes
      an array instead of a List */
  public void insertObjects(TreeNode treeNode, int startIndex,
			    GestureObject[] newObjs)
  {
    insertObjects(treeNode, startIndex, Arrays.asList(newObjs));
  }

  /** Make tree nodes for gestureObjects and insert them as children
      of treeNode, starting at startIndex (or -1 to append to the
      end).  If any members of gestureObjects are GestureContainers,
      their children get added recursively.  */
  public void insertObjects(TreeNode treeNode, int startIndex,
			    List gestureObjects)
  {
    GestureTreeNode parentNode = (GestureTreeNode) treeNode;
    /*
    System.out.println("insertObjects: childCount=" + parent.getChildCount()
		       + "\tstartIndex=" + startIndex + "\tinsert count=" +
		       newObjs.length);
    */
    if (startIndex == -1) {
      startIndex = parentNode.getChildCount();
    }
    int i = 0;
    for (Iterator iter = gestureObjects.iterator(); iter.hasNext(); i++) {
      /*
      System.out.println("inserting " + newObjs[i] + "\tat " + (startIndex+i));
      */
      GestureObject newObj = (GestureObject) iter.next();
      GestureTreeNode childNode = makeTreeNode(newObj);
      insertNodeInto(childNode, parentNode, startIndex+i);
      if ((newObj instanceof GestureContainer) &&
	  !(newObj instanceof GestureCategory)) {
	GestureContainer container = (GestureContainer) newObj;
	insertObjects(childNode, 0, AbstractGestureContainer.
		      getChildList(container));
      }
    }
    /*
    printChildren(parent);
    */
  }

  public void printChildren(GestureTreeNode node)
  {
    System.out.println("children for " + node + ":");
    for (Enumeration enum = node.children(); enum.hasMoreElements();) {
      System.out.println("\t" + enum.nextElement());
    }
  }

  public void removeObjects(TreeNode treeNode, int startIndex, int count)
  {
    GestureTreeNode parent = (GestureTreeNode) treeNode;
    for (int i = count-1; i >= 0; i--) {
      DefaultMutableTreeNode child = (DefaultMutableTreeNode)
	parent.getChildAt(startIndex+i);
      removeNodeFromParent(child);
    }
  }

  public boolean isNodeRenamable(TreeNode node)
  {
    return (node != getRoot()) &&
      (node != getTrainingRootNode()) && (node != getTestRootNode());
  }

  protected class GestureTreeNode extends DefaultMutableTreeNode {
    protected PropertyChangeListener propChangeListener =
      new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
	  /*
	  System.out.println("firing nodeChanged: " + GestureTreeNode.this +
			     "\tprop: " + evt.getPropertyName() + "\t= " +
			     evt.getOldValue() + " -> " +
			     evt.getNewValue());
	  */
	  // JLabel sometimes throws an exception (at line 383) and I
	  // think the only way it can is if its icon is modified by 2
	  // threads concurrently.  I would expect it to be safe to
	  // modify the TreeModel from any thread and that JTree would
	  // take care that widget ops only happen on the event
	  // thread, but I think it doesn't.
	  SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
	      GestureTreeModel.this.nodeChanged(GestureTreeNode.this);
	    }
	  });
	}
      };
    protected CollectionListener collectionListener =
      new CollectionAdapter() {
	public void elementAdded(CollectionEvent e) {
	  /*
	  System.out.println("elementAdded: " + GestureTreeNode.this +
			     "\tstart:" + e.getStartIndex() + "\t#:" +
			     e.getElementCount());
	  */
	  GestureObject[] newObjs = (GestureObject[]) e.getElements();
	  insertObjects(GestureTreeNode.this, e.getStartIndex(), newObjs);
	}
	public void elementRemoved(CollectionEvent e) {
	  removeObjects(GestureTreeNode.this, e.getStartIndex(),
			e.getElementCount());
	}
      };

    public GestureTreeNode()
    {
      super(null);
    }

    public GestureTreeNode(GestureObject obj)
    {
      this(obj, true);
    }

    public GestureTreeNode(GestureObject obj, boolean allowsChildren)
    {
      // the superclass doesn't use setUserObject in the constructor :-(
      super(null, allowsChildren);
      setUserObject(obj);
    }

    public void setUserObject(Object userObject) {
      Object oldUserObject = getUserObject();
      if ((oldUserObject != null) &&
	  (oldUserObject instanceof GestureObject)) {
	unsetListeners((GestureObject) oldUserObject);
      }
      super.setUserObject(userObject);
      if ((userObject != null) &&
	  (userObject instanceof GestureObject)) {
	setListeners((GestureObject) userObject);
      }
    }

    protected void unsetListeners(GestureObject obj)
    {
      obj.removePropertyChangeListener(propChangeListener);
      if ((obj instanceof GestureContainer) &&
	  !(obj instanceof GestureCategory)) {
	GestureContainer container = (GestureContainer) obj;
	container.removeCollectionListener(collectionListener);
      }
    }

    protected void setListeners(GestureObject obj)
    {
      /*
      System.out.println("Adding listeners to " + obj);
      */
      obj.addPropertyChangeListener(propChangeListener);
      if ((obj instanceof GestureContainer) &&
	  !(obj instanceof GestureCategory)) {
	// We don't display GestureCategory children in the tree, so
	// don't listen to it.
	GestureContainer container = (GestureContainer) obj;
	container.addCollectionListener(collectionListener);
      }
    }

    public String toString()
    {
      Object userObj = getUserObject();
      if (userObj instanceof GestureContainer) {
	return ((GestureContainer) userObj).getName();
      }
      else {
	return userObj.toString();
      }
    }
  }
}

/*
Copyright (c) 2001 Regents of the University of California.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.

3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:

      This product includes software developed by the Group for User 
      Interface Research at the University of California at Berkeley.

4. The name of the University may not be used to endorse or promote products 
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
*/
