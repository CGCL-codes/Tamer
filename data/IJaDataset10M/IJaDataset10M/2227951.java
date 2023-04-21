package com.g2d.display.ui.tree;

import java.io.Serializable;
import java.util.Collection;

/**
 * 定义可以用作 TreeView 中树节点的对象所需的要求。 
 * @author WAZA
 */
public interface TreeNode extends Serializable {

    /**
	 * 以 Enumeration 的形式返回接收者的子节点。
	 */
    public Collection<TreeNode> children();

    /** 
	 * 返回索引 childIndex 位置的子 TreeNode。
	 */
    public TreeNode getChildAt(int childIndex);

    /** 
	 * 返回接收者包含的子 TreeNode 数。 
	 */
    public int getChildCount();

    /**
	 * 返回接收者子节点中的 node 的索引。 
	 */
    public int getIndex(TreeNode node);

    /** 
	 * 返回接收者的父 TreeNode。 
	 */
    public TreeNode getParent();

    /**
	 * 为该节点添加一个子节点
	 * @param node 子节点
	 */
    public void addChild(TreeNode node);

    /**
	 * 插入在现有子节点之前
	 * @param node 要插入的子节点
	 * @param before 要插入到这个节点之前
	 */
    public void insertChild(TreeNode node, TreeNode before);

    /**
	 * 从该节点删除一个子节点
	 * @param node 子节点
	 */
    public boolean removeChild(TreeNode node);

    /**
	 * 删除所有子节点
	 */
    public void removeAllChilds();

    /**
	 * 获得该树节点在树中的深度
	 * @return <0 意味着该节点不在树中，根节点的深度为0，依次递增 
	 * @return
	 */
    public int getDepth();

    /**
	 * 设置该树节点在树中的深度，不清楚内部原理的情况下请不要手动调用
	 * @param depth 深度值
	 */
    public void setDepth(int depth);
}

;
