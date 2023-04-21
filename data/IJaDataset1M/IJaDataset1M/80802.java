package de.pallo.joti.sys;

import com.drew.metadata.Metadata;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import javax.swing.tree.*;
import javax.swing.event.TreeModelListener;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * -- Copyright (C) 2004 M. Pallo <markus@pallo.de>
 * <p/>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
 * <p/>
 * <p/>
 * $Revision: 1.1 $
 * Author: M. Pallo
 * last changed by $Author: baskote $
 */
public class MetadataTreeModel implements TreeModel {

    private DefaultMutableTreeNode root = null;

    public MetadataTreeModel(Metadata metadata) {
        root = new DefaultMutableTreeNode(Translation.getString(Translation.joti_metadataframe_root));
        Iterator dirIterator = metadata.getDirectoryIterator();
        while (dirIterator.hasNext()) {
            Object dirObject = dirIterator.next();
            SimpleLog.logDebug(dirObject.getClass().getName());
            Directory dir = (Directory) dirObject;
            DefaultMutableTreeNode dirNode = new DefaultMutableTreeNode(dir.getName());
            root.add(dirNode);
            Iterator tagIterator = dir.getTagIterator();
            while (tagIterator.hasNext()) {
                Tag tag = (Tag) tagIterator.next();
                try {
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(tag.getTagName());
                    dirNode.add(childNode);
                    DefaultMutableTreeNode childValueNode = new DefaultMutableTreeNode(tag.getDescription());
                    childNode.add(childValueNode);
                } catch (Exception ex) {
                }
            }
        }
    }

    public Object getRoot() {
        return root;
    }

    public int getChildCount(Object parent) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
        return node.getChildCount();
    }

    public boolean isLeaf(Object node) {
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) node;
        return n.isLeaf();
    }

    public void addTreeModelListener(TreeModelListener l) {
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }

    public Object getChild(Object parent, int index) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
        return node.getChildAt(index);
    }

    public int getIndexOfChild(Object parent, Object child) {
        DefaultMutableTreeNode p = (DefaultMutableTreeNode) parent;
        DefaultMutableTreeNode c = (DefaultMutableTreeNode) child;
        return p.getIndex(c);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }
}
