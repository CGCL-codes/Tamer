package ua.od.lonewolf.Crow.Model.Tree;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import ua.od.lonewolf.Crow.Model.Element.Architecture;
import ua.od.lonewolf.Crow.Model.Element.CRS;
import ua.od.lonewolf.Crow.Model.Element.Code;
import ua.od.lonewolf.Crow.Model.Element.DetailedDesign;
import ua.od.lonewolf.Crow.Model.Element.ElementType;
import ua.od.lonewolf.Crow.Model.Element.IElement;
import ua.od.lonewolf.Crow.Model.Element.RegisterFactory;
import ua.od.lonewolf.Crow.Model.Element.TRS;
import ua.od.lonewolf.Crow.Model.Element.Test;
import ua.od.lonewolf.Crow.Util.TypeUtilities;
import ua.od.lonewolf.Crow.View.Structure.TreeModelTwoStageListener;

public class StructureModel implements TreeModel {

    /**
    * Array of listeners to model changes
    */
    private ArrayList<TreeModelListener> treeModelListeners = null;

    private ITreeElement root;

    public StructureModel(ITreeElement root) {
        this.root = root;
        treeModelListeners = new ArrayList<TreeModelListener>();
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
        ArrayList<TreeModelListener> arr = new ArrayList<TreeModelListener>();
        ArrayList<TreeModelTwoStageListener> arrTwo = new ArrayList<TreeModelTwoStageListener>();
        for (int i = 0; i < treeModelListeners.size(); i++) {
            TreeModelListener listener = treeModelListeners.get(i);
            if (listener instanceof TreeModelTwoStageListener) {
                arrTwo.add((TreeModelTwoStageListener) listener);
            } else {
                arr.add(listener);
            }
        }
        if (arrTwo.size() > 0) {
            for (int i = 0; i < arrTwo.size(); i++) {
                arr.add(arrTwo.get(i));
            }
        }
        treeModelListeners = arr;
    }

    public void addTreeModelTwoStageListener(TreeModelTwoStageListener l) {
        treeModelListeners.add(l);
    }

    @Override
    public Object getChild(Object parent, int index) {
        ITreeElement el = (ITreeElement) parent;
        return el.getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        ITreeElement el = (ITreeElement) parent;
        return el.getChildCount();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) {
            return -1;
        }
        ITreeElement el = (ITreeElement) parent;
        for (int i = 0; i < el.getChildCount(); i++) {
            if (el.getChildAt(i).equals(child)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object getRoot() {
        return this.root;
    }

    @Override
    public boolean isLeaf(Object node) {
        ITreeElement el = (ITreeElement) node;
        return (el.getChildCount() == 0);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public void clear() {
        ProductElement el = (ProductElement) getRoot();
        el.removeAllChildren();
    }

    @SuppressWarnings("unchecked")
    public void reload(Session session) {
        fireTreeStructureWillChange((ITreeElement) getRoot());
        clear();
        List<IElement> arch = new ArrayList<IElement>();
        List<IElement> dd = new ArrayList<IElement>();
        List<IElement> code = new ArrayList<IElement>();
        List<IElement> test = new ArrayList<IElement>();
        long productId = ((ProductElement) this.getRoot()).getSourceElement().getId();
        try {
            session.beginTransaction();
            addCollectionWithCRQ(session, productId, CRS.class);
            addCollectionWithCRQ(session, productId, TRS.class);
            Criteria critArch = session.createCriteria(Architecture.class);
            critArch.add(Restrictions.eq("productId", productId));
            critArch.addOrder(Order.asc("name"));
            arch = critArch.list();
            Criteria critDd = session.createCriteria(DetailedDesign.class);
            critDd.add(Restrictions.eq("productId", productId));
            critDd.addOrder(Order.asc("name"));
            dd = critDd.list();
            Criteria critCode = session.createCriteria(Code.class);
            critCode.add(Restrictions.eq("productId", productId));
            critCode.addOrder(Order.asc("name"));
            code = critCode.list();
            Criteria critTest = session.createCriteria(Test.class);
            critTest.add(Restrictions.eq("productId", productId));
            critTest.addOrder(Order.asc("name"));
            test = critTest.list();
            session.getTransaction().commit();
            addCollection(arch, ElementType.ARCH);
            addCollection(dd, ElementType.DD);
            addCollection(code, ElementType.CODE);
            addCollection(test, ElementType.TEST);
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            fireTreeStructureChanged((ITreeElement) getRoot());
        }
    }

    private void addCollectionWithCRQ(Session session, long productId, Class<? extends IElement> type) throws SQLException {
        Map<Integer, StructureElement> addedIds = new Hashtable<Integer, StructureElement>();
        ITreeElement root = (ITreeElement) getRoot();
        CollectionElement collection = new CollectionElement(TypeUtilities.classToEnclosureType(type), root);
        String tableName = TypeUtilities.enclosureTypeToString(TypeUtilities.classToEnclosureType(type));
        String sql = "select " + tableName + ".id, crq.child_id from " + tableName + " " + "left join crq on (" + tableName + ".id = crq.parent_id and crq.Element_type = '" + tableName + "') where product_id = " + productId + " order by " + tableName + ".id, child_id";
        SQLQuery query = session.createSQLQuery(sql);
        ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
        long curParentId = -1;
        IElement parentEl = null;
        StructureElement strucParentEl = null;
        while (results.next()) {
            Object[] ids = results.get();
            int parentId = (Integer) ids[0];
            int childId = -1;
            if (ids[1] != null) {
                childId = (Integer) ids[1];
            }
            if (parentId != curParentId && !addedIds.keySet().contains(parentId)) {
                String sqlCheckParent = "select parent_id from crq where crq.child_id = " + parentId + " and crq.Element_type = '" + tableName + "'";
                if (session.createSQLQuery(sqlCheckParent).list().size() > 0) {
                    continue;
                } else {
                    parentEl = RegisterFactory.getElement(session, parentId, type);
                    strucParentEl = new StructureElement(parentEl, collection);
                    addedIds.put(parentId, strucParentEl);
                    curParentId = parentId;
                }
            }
            strucParentEl = addedIds.get(parentId);
            if (childId >= 0) {
                IElement childEl = RegisterFactory.getElement(session, childId, type);
                if (childEl != null) {
                    StructureElement strucChildEl = new StructureElement(childEl, strucParentEl);
                    addedIds.put(childId, strucChildEl);
                } else {
                    System.out.println("No child found for [" + childId + ", parent=[" + parentId + "], " + "type = " + type);
                }
            }
        }
    }

    protected void addCollection(List<IElement> list, ElementType enclosureType) {
        ITreeElement root = (ITreeElement) getRoot();
        CollectionElement el = new CollectionElement(enclosureType, root);
        for (int i = 0; i < list.size(); i++) {
            new StructureElement(list.get(i), el);
        }
    }

    public void fireTreeStructureChanged(ITreeElement oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, new Object[] { oldRoot });
        for (int i = 0; i < len; i++) {
            TreeModelListener tml = treeModelListeners.get(i);
            tml.treeStructureChanged(e);
        }
    }

    public void fireStructureElementAdded(StructureElement el) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, getPathForStructureElement(el));
        for (int i = 0; i < len; i++) {
            TreeModelListener tml = treeModelListeners.get(i);
            tml.treeNodesInserted(e);
        }
    }

    public void fireStructureElementRemoved(TreeModelEvent evt) {
        int len = treeModelListeners.size();
        for (int i = 0; i < len; i++) {
            TreeModelListener tml = treeModelListeners.get(i);
            tml.treeNodesRemoved(evt);
        }
    }

    public void fireTreeStructureWillChange(ITreeElement oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, new Object[] { oldRoot });
        for (int i = 0; i < len; i++) {
            if (treeModelListeners.get(i) instanceof TreeModelTwoStageListener) {
                TreeModelTwoStageListener tml = (TreeModelTwoStageListener) treeModelListeners.get(i);
                tml.treeStructureWillBeChanged(e);
            }
        }
    }

    public TreePath locateAndGetPathForStructureElement(StructureElement strucEl) {
        for (int i = 0; i < getChildCount(getRoot()); i++) {
            if (getChild(getRoot(), i) instanceof CollectionElement) {
                CollectionElement collection = (CollectionElement) getChild(getRoot(), i);
                if (collection.getEnclosureType() == TypeUtilities.classToEnclosureType(strucEl.getSourceElement().getClass())) {
                    ITreeElement el = collection.findStructureElement(strucEl.getSourceElement().getId());
                    if (el != null && el instanceof StructureElement) {
                        return getPathForStructureElement((StructureElement) el);
                    }
                }
            }
        }
        return null;
    }

    public TreePath getPathForStructureElement(StructureElement strucEl) {
        List<TreeNode> arrPath = new ArrayList<TreeNode>();
        arrPath.add((ITreeElement) getRoot());
        List<TreeNode> lstRevert = new ArrayList<TreeNode>();
        lstRevert.add(strucEl);
        while (!(strucEl.getParent() instanceof CollectionElement)) {
            lstRevert.add(strucEl.getParent());
            strucEl = (StructureElement) strucEl.getParent();
        }
        lstRevert.add(strucEl.getParent());
        for (int i = lstRevert.size() - 1; i >= 0; i--) {
            arrPath.add(lstRevert.get(i));
        }
        return new TreePath(arrPath.toArray());
    }
}
