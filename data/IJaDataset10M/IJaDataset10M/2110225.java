package com.gcapmedia.dab.epg.ui;

import javax.swing.tree.TreeNode;
import com.gcapmedia.dab.epg.Link;

public class LinkTreeNode extends BaseTreeNode {

    private Link link;

    public LinkTreeNode(Link link, TreeNode parent) {
        super(link, parent);
        this.link = link;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return "Link";
    }
}
