package com.frameworkset.common.tag.tree.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import com.frameworkset.common.tag.contextmenu.ContextMenuImpl;
import com.frameworkset.common.tag.tree.itf.ICollapseListener;
import com.frameworkset.common.tag.tree.itf.IExpandListener;
import com.frameworkset.common.tag.tree.itf.ISelectListener;
import com.frameworkset.common.tag.tree.itf.ITree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.common.tag.tree.itf.IUnselectListener;

/**
 * �������ṹ�ĳ�����
 * @author biaoping.yin
 * created on 2005-3-25
 * version 1.0
 */
public abstract class Tree extends ContextMenuImpl implements ITree, java.io.Serializable {

    /**
	 * level:����Ĭ��չ���㼶
	 */
    protected int level = -1;

    protected boolean singleSelectionMode = false;

    protected Set expanded = new TreeSet();

    protected Set selected = new TreeSet();

    protected ITreeNode root = null;

    protected List expandListeners = new ArrayList();

    protected List collapseListeners = new ArrayList();

    protected List selectListeners = new ArrayList();

    protected List unselectListeners = new ArrayList();

    protected List nodeListeners = new ArrayList();

    /**��ʶ��ǰչ���ýڵ�*/
    protected ITreeNode curExpanded;

    protected boolean dynamic = true;

    protected boolean needObservable = false;

    /**���½�����Ϣ*/
    protected boolean refreshNode = true;

    protected boolean uprecursive = false;

    protected boolean recursive = false;

    public static final String mode_static = "static";

    public static final String mode_static_dynamic = "static-dynamic";

    public static final String mode_dynamic = "dynamic";

    /**
	 * ָ���Ƿ�����ڵ��������,ȱʡΪtrue
	 */
    private boolean sortable = false;

    public void setNeedObservable(boolean needObservable) {
        this.needObservable = needObservable;
    }

    /**
	 * �ж��Ƿ���Ҫע�ᵽ�۲�����
	 * @return boolean
	 */
    protected boolean isNeedObservable() {
        return this.needObservable;
    }

    public ITreeNode getRoot() {
        return this.root;
    }

    public void setRoot(ITreeNode node) {
        this.root = node;
        this.root.setTree(this);
    }

    public ITreeNode findNode(String treeNodeId) {
        return findNode(getRoot(), treeNodeId);
    }

    protected ITreeNode findNode(ITreeNode treeNode, String treeNodeId) {
        if (treeNode.getId().equals(treeNodeId)) {
            return treeNode;
        }
        Iterator children = treeNode.getChildren().iterator();
        while (children.hasNext()) {
            ITreeNode child = (ITreeNode) children.next();
            ITreeNode match = findNode(child, treeNodeId);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    public Set findNodes(Set treeNodeIds) {
        Set treeNodes = new HashSet();
        findNodes(getRoot(), treeNodeIds, treeNodes);
        return treeNodes;
    }

    protected void findNodes(ITreeNode treeNode, Set treeNodeIds, Set treeNodes) {
        if (treeNodeIds.contains(treeNode.getId())) {
            treeNodes.add(treeNode);
        }
        Iterator children = treeNode.getChildren().iterator();
        while (children.hasNext()) {
            findNodes((ITreeNode) children.next(), treeNodeIds, treeNodes);
        }
    }

    public boolean isExpanded(String treeNodeId) {
        return this.expanded.contains(treeNodeId);
    }

    /**
     * 
     *  Description:
     * @param expandedNode
     * @param curLevel
     * @see com.frameworkset.common.tag.tree.itf.ITree#expand(com.frameworkset.common.tag.tree.itf.ITreeNode, int)
     */
    public void expand(ITreeNode expandedNode, int curLevel) {
        this.expanded.add(expandedNode.getId());
        if (needObservable && curLevel > level) {
            notifyObservers(expandedNode);
        }
        if (this.expandListeners.size() > 0) {
            Iterator iterator = this.expandListeners.iterator();
            while (iterator.hasNext()) {
                ((IExpandListener) iterator.next()).nodeExpanded(expandedNode, this, curLevel, needObservable);
            }
        }
    }

    /**
     *  ��̬ģʽʱʹ��
     *  Description:
     * @param expandedNode
     * @param curLevel
     * @see com.frameworkset.common.tag.tree.itf.ITree#expand(com.frameworkset.common.tag.tree.itf.ITreeNode, int)
     */
    public void impactExpand(ITreeNode expandedNode, int curLevel) {
        if (needObservable && curLevel > level) {
            notifyObservers(expandedNode);
        }
        if (this.expandListeners.size() > 0) {
            Iterator iterator = this.expandListeners.iterator();
            while (iterator.hasNext()) {
                ((IExpandListener) iterator.next()).impactExpandNode(expandedNode, this, curLevel, needObservable);
            }
        }
    }

    /**
     * ҳ���ϵ��չ��ͼ��ʱ�����ñ�����չ������Ľڵ�
     *  Description:
     * @param treeNodeId
     * @see com.frameworkset.common.tag.tree.itf.ITree#expand(java.lang.String)
     */
    public void expand(String treeNodeId, String mode, String scope, HttpServletRequest request) {
        if (mode.equals(Tree.mode_static_dynamic)) {
            String treeName = request.getParameter("__nodename");
            String type = request.getParameter("__nodetype");
            String path = request.getParameter("__nodepath");
            String isfirstChild = request.getParameter("__nodefirst");
            String islastChild = request.getParameter("__nodelast");
            ITreeNode expandedNode = new TreeNode(treeNodeId, treeName, type, true, null, null, null, null, path, null);
            if (isfirstChild != null && !isfirstChild.equals("true")) {
                ITreeNode leftNode = new TreeNode();
                expandedNode.setLeftNode(leftNode);
            }
            if (islastChild != null && !islastChild.equals("true")) {
                ITreeNode lastNode = new TreeNode();
                expandedNode.setRightNode(lastNode);
            }
            expandedNode.setHasChildren(true);
            expandedNode.setTree(this);
            this.curExpanded = expandedNode;
            expand(expandedNode, this.getUnknownLevel());
        } else {
            ITreeNode expandedNode = findNode(treeNodeId);
            this.curExpanded = expandedNode;
            expand(expandedNode, this.getUnknownLevel());
        }
    }

    /**
	 * 
	 *  Description:����ڵ���۵�ͼ��ʱ���ñ�����
	 * @param treeNodeId
	 * @see com.frameworkset.common.tag.tree.itf.ITree#collapse(java.lang.String)
	 */
    public void collapse(String treeNodeId) {
        this.expanded.remove(treeNodeId);
        ITreeNode collapsedNode = findNode(treeNodeId);
        if (needObservable) {
            notifyObservers(collapsedNode);
        }
        if (this.collapseListeners.size() > 0) {
            Iterator iterator = this.collapseListeners.iterator();
            while (iterator.hasNext()) {
                ((ICollapseListener) iterator.next()).nodeCollapsed(collapsedNode, this, needObservable);
            }
        }
    }

    public Set getExpandedNodes() {
        return findNodes(this.expanded);
    }

    public void addExpandListener(IExpandListener expandListener) {
        this.expandListeners.add(expandListener);
    }

    public void removeExpandListener(IExpandListener expandListener) {
        this.expandListeners.remove(expandListener);
    }

    public void addCollapseListener(ICollapseListener collapseListener) {
        this.collapseListeners.add(collapseListener);
    }

    public void removeCollapseListener(ICollapseListener collapseListener) {
        this.collapseListeners.remove(collapseListener);
    }

    public boolean isSelected(String treeNodeId) {
        return this.selected.contains(treeNodeId);
    }

    public void select(String treeNodeId) {
        if (isSingleSelectionMode()) {
            unSelectAll();
        }
        ITreeNode selectedNode = findNode(treeNodeId);
        this.selected.add(treeNodeId);
        if (needObservable) {
            notifyObservers(selectedNode);
        }
        if (this.selectListeners.size() > 0) {
            Iterator iterator = this.selectListeners.iterator();
            while (iterator.hasNext()) {
                ((ISelectListener) iterator.next()).nodeSelected(selectedNode, this, needObservable);
            }
        }
    }

    public void unSelect(String treeNodeId) {
        this.selected.remove(treeNodeId);
        ITreeNode unselectedNode = findNode(treeNodeId);
        if (needObservable) {
            notifyObservers(unselectedNode);
        }
        if (this.unselectListeners.size() > 0) {
            Iterator iterator = this.unselectListeners.iterator();
            while (iterator.hasNext()) {
                ((IUnselectListener) iterator.next()).nodeUnselected(unselectedNode, this, needObservable);
            }
        }
    }

    public void unSelect(ITreeNode unselectedNode) {
        this.selected.remove(unselectedNode.getId());
        if (needObservable) {
            notifyObservers(unselectedNode);
        }
        if (this.unselectListeners.size() > 0) {
            Iterator iterator = this.unselectListeners.iterator();
            while (iterator.hasNext()) {
                ((IUnselectListener) iterator.next()).nodeUnselected(unselectedNode, this, needObservable);
            }
        }
    }

    public void unSelectAll() {
        Iterator iterator = this.selected.iterator();
        while (iterator.hasNext()) {
            unSelect((String) iterator.next());
        }
    }

    public Set getSelectedNodes() {
        return findNodes(this.selected);
    }

    public void setSingleSelectionMode(boolean mode) {
        this.singleSelectionMode = mode;
    }

    public boolean isSingleSelectionMode() {
        return this.singleSelectionMode;
    }

    public void addSelectListener(ISelectListener selectListener) {
        this.selectListeners.add(selectListener);
    }

    public void removeSelectListener(ISelectListener selectListener) {
        this.selectListeners.remove(selectListener);
    }

    public void addUnselectListener(IUnselectListener unselectListener) {
        this.unselectListeners.add(unselectListener);
    }

    public void removeUnselectListener(IUnselectListener unselectListener) {
        this.unselectListeners.remove(unselectListener);
    }

    public Iterator iterator(boolean includeRootNode) {
        return new TreeIterator(this, includeRootNode);
    }

    public Iterator iterator(String parentIndent) {
        return new TreeIterator(this, parentIndent);
    }

    /**
     * added by yinbiaoping on 2004/03/23
     * ���ط���java.util.Observable#notifyObservers(Object o)
     */
    public void notifyObservers(Object o) {
        setChanged();
        super.notifyObservers(o);
    }

    /**
	 * Description:
	 * @return
	 * boolean
	 */
    public boolean isRefreshNode() {
        return refreshNode;
    }

    /**
	 * Description:
	 * @param b
	 * void
	 */
    public void setRefreshNode(boolean b) {
        refreshNode = b;
    }

    /**
	 * ��֪����ǰ�㼶ʱ�����ñ�������ȡ��ǰ�㼶
	 * û��ָ���ڵ���ĵ�ǰ�㼶��Ϊ�˱������������в㼶�����ͻ��ָ����ǰ�㼶��Ĭ�ϲ㼶��1��
	 * ����setSon��������addNode������������Ϊ��ǰ��Ĳ㼶
	 * ��Ĭ��չ���㼶С��չ�����¼����������
	 * Description:
	 * @return
	 * int
	 */
    protected int getUnknownLevel() {
        return level + 1;
    }

    public void setSortable(boolean b) {
        this.sortable = b;
    }

    public boolean isSortable() {
        return sortable;
    }

    /**
	 * ��ȡ��ǰչ���Ľڵ���Ϣ��
	 * ��Ҫ�ڶ���̬��ʽ�����ʱ��̫�ط��ص�ǰ�����ڵ��html��Ϣ
	 */
    public ITreeNode getCurExpanded() {
        return curExpanded;
    }

    public boolean isDynamic() {
        return this.mode.equals("dynamic");
    }

    public boolean isStatic() {
        return this.mode.equals("static");
    }

    public boolean isStaticDynamic() {
        return this.mode.equals("static-dynamic");
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    private String mode;

    private boolean partuprecursive = false;

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    public boolean isUprecursive() {
        return uprecursive;
    }

    public void setUprecursive(boolean uprecursive) {
        this.uprecursive = uprecursive;
    }

    public void setPartuprecursive(boolean partuprecursive) {
        this.partuprecursive = partuprecursive;
    }

    public boolean isPartuprecursive() {
        return partuprecursive;
    }
}
