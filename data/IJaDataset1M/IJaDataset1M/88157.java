package net.mjrz.fm.entity;

import net.mjrz.fm.entity.beans.*;
import net.mjrz.fm.actions.ActionRequest;
import net.mjrz.fm.actions.CreateInitialAccounts;
import net.mjrz.fm.constants.*;
import net.mjrz.fm.utils.*;
import java.awt.Cursor;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * @author Mjrz contact@mjrz.net
 *
 */
public final class AccountCategoryMap implements TreeModel {

    private LinkedHashMap<Long, AccountCategory> categoryMap;

    private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();

    AccountCategory root = new AccountCategory(1, Long.valueOf(-1), Long.valueOf(-1));

    User user = null;

    FManEntityManager em = new FManEntityManager();

    public void setUser(User u) {
        user = u;
    }

    public Object getRoot() {
        return root;
    }

    public int getChildCount(Object parent) {
        AccountCategory p = (AccountCategory) parent;
        return this.getChildren(p).size();
    }

    public boolean isLeaf(Object node) {
        AccountCategory p = (AccountCategory) node;
        return this.getChildren(p).size() == 0;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public Object getChild(Object parent, int index) {
        ArrayList<AccountCategory> children = this.getChildren((AccountCategory) parent);
        if (index < 0 || index >= children.size()) return null;
        return children.get(index);
    }

    public int getIndexOfChild(Object parent, Object child) {
        AccountCategory p = (AccountCategory) parent;
        ArrayList<AccountCategory> children = this.getChildren(p);
        return children.indexOf(child);
    }

    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.addElement(l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }

    private void addInitialAccounts(FManEntityManager em) throws Exception {
        AccountCategory i = new AccountCategory(user.getUid(), Long.valueOf(AccountTypes.ACCT_TYPE_INCOME), Long.valueOf(AccountTypes.ACCT_TYPE_ROOT));
        i.setCategoryName(AccountTypes.getAccountType(AccountTypes.ACCT_TYPE_INCOME));
        AccountCategory c = new AccountCategory(user.getUid(), Long.valueOf(AccountTypes.ACCT_TYPE_CASH), Long.valueOf(AccountTypes.ACCT_TYPE_ROOT));
        c.setCategoryName(AccountTypes.getAccountType(AccountTypes.ACCT_TYPE_CASH));
        AccountCategory e = new AccountCategory(user.getUid(), Long.valueOf(AccountTypes.ACCT_TYPE_EXPENSE), Long.valueOf(AccountTypes.ACCT_TYPE_ROOT));
        e.setCategoryName(AccountTypes.getAccountType(AccountTypes.ACCT_TYPE_EXPENSE));
        AccountCategory l = new AccountCategory(user.getUid(), Long.valueOf(AccountTypes.ACCT_TYPE_LIABILITY), Long.valueOf(AccountTypes.ACCT_TYPE_ROOT));
        l.setCategoryName(AccountTypes.getAccountType(AccountTypes.ACCT_TYPE_LIABILITY));
        em.addAccountCategory(i);
        em.addAccountCategory(c);
        em.addAccountCategory(e);
        em.addAccountCategory(l);
    }

    @SuppressWarnings("unchecked")
    public AccountCategoryMap(long uid, String rootName) {
        categoryMap = new LinkedHashMap<Long, AccountCategory>();
        user = new User();
        user.setUid(uid);
        root.setCategoryName(rootName);
        try {
            em = new FManEntityManager();
            List list = em.getAccountCategories(user.getUid());
            if (list == null || list.size() == 0) {
                addInitialAccounts(em);
                list = em.getAccountCategories(user.getUid());
            }
            int sz = list.size();
            for (int i = 0; i < sz; i++) {
                AccountCategory c = (AccountCategory) list.get(i);
                categoryMap.put(c.getCategoryId(), c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<Long> getCategoryIds() {
        return categoryMap.keySet();
    }

    public LinkedHashMap<Long, AccountCategory> getMap() {
        LinkedHashMap<Long, AccountCategory> ret = new LinkedHashMap<Long, AccountCategory>();
        try {
            for (Long ac : categoryMap.keySet()) {
                AccountCategory clone = (AccountCategory) categoryMap.get(ac).clone();
                ret.put(ac, clone);
            }
            return ret;
        } catch (Exception e) {
            return ret;
        }
    }

    public void addCategory(AccountCategory a, boolean persistant) {
        boolean isValid = isValidCategory(a);
        if (!isValid) {
            return;
        }
        try {
            categoryMap.put(a.getCategoryId(), a);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void addCategory(AccountCategory parent, AccountCategory a, boolean persistant) {
        boolean isValid = isValidCategory(a);
        if (!isValid) {
            return;
        }
        try {
            if (persistant) em.addAccountCategory(a);
            categoryMap.put(a.getCategoryId(), a);
            TreeModelListener l = treeModelListeners.get(0);
            ArrayList<AccountCategory> ancestors = this.getAncestors(a);
            TreeModelEvent e = new TreeModelEvent(a, ancestors.toArray());
            l.treeNodesInserted(e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public String removeNodeFromParent(AccountCategory c) {
        if (c.getCategoryId() == AccountTypes.ACCT_TYPE_ROOT || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_INCOME || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_CASH || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_EXPENSE || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_LIABILITY) {
            return "Cannot remove category: " + c.getCategoryName();
        }
        ArrayList<AccountCategory> rlist = new ArrayList<AccountCategory>();
        getRemoveNodeList(rlist, c);
        boolean success = false;
        try {
            success = em.deleteAccountCategory(rlist);
            if (success) removeNode(c);
            TreeModelListener l = treeModelListeners.get(0);
            ArrayList<AccountCategory> ancestors = this.getAncestors(c);
            TreeModelEvent e = new TreeModelEvent(c, ancestors.toArray());
            l.treeNodesRemoved(e);
            return null;
        } catch (Exception e) {
            return "Invalid selection";
        }
    }

    public String renameNode(AccountCategory c, String name) {
        if (c == null) {
            return "Invalid selection";
        }
        if (name == null || name.length() == 0) return "Invalid name";
        if (c.getCategoryId() == AccountTypes.ACCT_TYPE_ROOT || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_INCOME || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_CASH || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_EXPENSE || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_LIABILITY) {
            return "Cannot rename category: " + c.getCategoryName();
        }
        AccountCategory parent = getCategory(c.getParentCategoryId());
        ArrayList<AccountCategory> children = this.getChildren(parent);
        for (AccountCategory child : children) {
            if (child.getCategoryName().equals(name)) return "A category with same name already exists";
        }
        if (c.getCategoryId() == AccountTypes.ACCT_TYPE_ROOT || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_INCOME || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_CASH || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_EXPENSE || c.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_LIABILITY) {
            return "Cannot rename category: " + c.getCategoryName();
        }
        try {
            c.setCategoryName(name);
            em.updateAccountCategory(c);
            TreeModelListener l = treeModelListeners.get(0);
            ArrayList<AccountCategory> ancestors = this.getAncestors(c);
            TreeModelEvent e = new TreeModelEvent(c, ancestors.toArray());
            l.treeNodesChanged(e);
            return null;
        } catch (Exception e) {
            return "Invalid Selection";
        }
    }

    private void getRemoveNodeList(ArrayList<AccountCategory> list, AccountCategory c) {
        ArrayList<AccountCategory> children = getChildren(c);
        for (AccountCategory t : children) {
            getRemoveNodeList(list, t);
        }
        list.add(c);
    }

    private void removeNode(AccountCategory c) {
        ArrayList<AccountCategory> children = getChildren(c);
        for (AccountCategory t : children) {
            removeNode(t);
        }
        categoryMap.remove(c.getCategoryId());
    }

    public AccountCategory getRootCategory(AccountCategory a) {
        AccountCategory root = a;
        int max_depth = categoryMap.size();
        int iter = 0;
        if ((a.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_INCOME) || (a.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_CASH) || (a.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_EXPENSE) || (a.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_LIABILITY)) {
            return a;
        }
        while (true) {
            AccountCategory tmp = categoryMap.get(root.getParentCategoryId());
            if (tmp == null) return null;
            if (tmp.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_ROOT) {
                break;
            }
            if (tmp.getParentCategoryId().intValue() == AccountTypes.ACCT_TYPE_ROOT) {
                root = tmp;
                break;
            }
            if (iter >= max_depth) {
                root = null;
                break;
            }
            root = tmp;
            iter++;
        }
        return root;
    }

    public ArrayList<AccountCategory> getAncestors(int id) {
        ArrayList<AccountCategory> ret = new ArrayList<AccountCategory>();
        AccountCategory a = this.getCategory(id);
        if (a == null) return ret;
        ret.add(a);
        while (true) {
            AccountCategory p = this.getParent(a);
            if (p == null) break;
            if (p.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_ROOT) {
                ret.add(p);
                break;
            }
            ret.add(p);
            a = p;
        }
        return ret;
    }

    public ArrayList<AccountCategory> getAncestors(AccountCategory a) {
        ArrayList<AccountCategory> ret = new ArrayList<AccountCategory>();
        ret.add(a);
        while (true) {
            AccountCategory p = this.getParent(a);
            if (p == null) break;
            ret.add(0, p);
            a = p;
        }
        ret.add(0, root);
        return ret;
    }

    public ArrayList<AccountCategory> getChildren(Long categoryId) {
        ArrayList<AccountCategory> ret = new ArrayList<AccountCategory>();
        Set<Long> keys = categoryMap.keySet();
        for (Long i : keys) {
            AccountCategory tmp = categoryMap.get(i);
            if (tmp.getParentCategoryId().longValue() == categoryId.longValue()) ret.add(tmp);
        }
        return ret;
    }

    public ArrayList<AccountCategory> getChildren(AccountCategory a) {
        ArrayList<AccountCategory> ret = new ArrayList<AccountCategory>();
        Long parentId = a.getCategoryId();
        Set<Long> keys = categoryMap.keySet();
        for (Long i : keys) {
            AccountCategory tmp = categoryMap.get(i);
            if (tmp.getParentCategoryId().longValue() == parentId.longValue()) ret.add(tmp);
        }
        return ret;
    }

    public AccountCategory getParent(AccountCategory c) {
        AccountCategory parent = categoryMap.get(c.getParentCategoryId());
        return parent;
    }

    public AccountCategory getCategory(long id) {
        return categoryMap.get(id);
    }

    private boolean isValidCategory(AccountCategory a) {
        if (a.getCategoryId() != null) {
            if (a.getCategoryId() == AccountTypes.ACCT_TYPE_INCOME || a.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_CASH || a.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_EXPENSE || a.getCategoryId().intValue() == AccountTypes.ACCT_TYPE_LIABILITY) {
                return false;
            }
        }
        if (a.getCategoryName() == null || a.getCategoryName().length() == 0) return false;
        if (a.getCategoryName().length() > 30) return false;
        if (categoryMap.containsKey(a.getCategoryId())) {
            return false;
        }
        if (!categoryMap.containsKey(a.getParentCategoryId())) {
            return false;
        }
        if (a.getCategoryId().intValue() == a.getParentCategoryId()) {
            return false;
        }
        AccountCategory parent = getCategory(a.getParentCategoryId());
        ArrayList<AccountCategory> children = this.getChildren(parent);
        for (AccountCategory child : children) {
            if (child.getCategoryName().equals(a.getCategoryName())) return false;
        }
        return true;
    }

    public int getSize() {
        return categoryMap.size();
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        Set<Long> keys = categoryMap.keySet();
        for (Long i : keys) {
            AccountCategory a = categoryMap.get(i);
            ret.append(a);
            ret.append("\n");
        }
        return ret.toString();
    }
}
