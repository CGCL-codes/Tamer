package com.gcapmedia.dab.epg.ui;

import javax.swing.tree.TreeNode;
import com.gcapmedia.dab.epg.Time;

public class TimeTreeNode extends BaseTreeNode {

    private Time time;

    public TimeTreeNode(Time time, TreeNode parent) {
        super(time, parent);
        this.time = time;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return "Time";
    }
}
