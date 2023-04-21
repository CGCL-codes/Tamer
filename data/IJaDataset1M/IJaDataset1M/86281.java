package org.xito.appmanager;

import java.io.*;
import java.util.*;
import javax.swing.tree.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xito.appmanager.store.AppStoreNode;
import org.xito.appmanager.store.ApplicationNode;
import org.xito.appmanager.store.GroupNode;
import org.xito.xmldocs.*;
import org.xito.launcher.*;

/**
 *
 * @author DRICHAN
 */
public class GroupTreeNodeWrapper extends AppStoreTreeNodeWrapper {

    /** Creates a new instance of AppTreeNode */
    public GroupTreeNodeWrapper(GroupNode node) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null");
        }
        super.allowsChildren = true;
        setUserObject(node);
        initialize();
    }

    /**
    * Setup the Children
    */
    protected void initialize() {
        AppStoreNode childNodes[] = ((GroupNode) getUserObject()).getNodes();
        for (int i = 0; i < childNodes.length; i++) {
            if (childNodes[i] instanceof ApplicationNode) {
                add(new ApplicationTreeNodeWrapper(null, (ApplicationNode) childNodes[i]));
            } else if (childNodes[i] instanceof GroupNode) {
                add(new GroupTreeNodeWrapper((GroupNode) childNodes[i]));
            }
        }
    }

    public GroupNode getGroupNode() {
        return (GroupNode) getUserObject();
    }
}
