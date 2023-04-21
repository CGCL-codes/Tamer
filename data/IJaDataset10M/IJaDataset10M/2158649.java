package com.jgeppert.struts2.jquery.tree.views.freemarker.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @author <a href="http://www.jgeppert.com">Johannes Geppert</a>
 * 
 */
public class JqueryTreeModels {

    protected TreeModel tree;

    protected TreeItemModel treeItem;

    private ValueStack stack;

    private HttpServletRequest req;

    private HttpServletResponse res;

    public JqueryTreeModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        this.stack = stack;
        this.req = req;
        this.res = res;
    }

    public TreeModel getTree() {
        if (tree == null) {
            tree = new TreeModel(stack, req, res);
        }
        return tree;
    }

    public TreeItemModel getTreeItem() {
        if (treeItem == null) {
            treeItem = new TreeItemModel(stack, req, res);
        }
        return treeItem;
    }
}
