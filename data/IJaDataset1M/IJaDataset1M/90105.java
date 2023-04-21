package cn.ekuma.epos.analysis.productCategory;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.tree.TreeNode;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import cn.ekuma.data.dao.bean.I_Category;
import cn.ekuma.epos.businesslogic.OrderUtil;
import cn.ekuma.epos.datalogic.I_DataLogicERP;
import com.openbravo.bean.erp.viewbean.ProductCategoryAnalysisInfo;
import com.openbravo.bean.erp.viewbean.ProductCategoryTransaction;
import com.openbravo.data.basic.BasicException;
import com.openbravo.format.Formats;
import com.openbravo.pos.base.AppLocal;

public class AnalysisModelManager {

    private ProductCategoryTreeTableNode rootNode;

    private ProductCategoryTreeTableModel treeTableModel;

    ArrayList<String> transactionColumnName;

    List<ProductCategoryAnalysisInfo> productCategoryAnalysisInfoList;

    public static String[] COLUMNNAME_ProductCategoryAnalysisInfo = { AppLocal.getIntString("label.prodcategory"), AppLocal.getIntString("label.storeNum"), AppLocal.getIntString("label.prodpricebuy"), AppLocal.getIntString("label.prodpricesell"), AppLocal.getIntString("label.profits") };

    I_DataLogicERP dlSales;

    boolean rootBuilder = false;

    public AnalysisModelManager(I_DataLogicERP dlSales) {
        this.dlSales = dlSales;
        transactionColumnName = new ArrayList();
        rootNode = new ProductCategoryTreeTableNode(null);
        treeTableModel = new ProductCategoryTreeTableModel(rootNode);
    }

    public TreeTableModel getTreeTableModel() {
        return new ProductCategoryTreeTableModel(rootNode);
    }

    public void query(Object filter) throws BasicException {
        transactionColumnName.clear();
        Object[] filter1 = (Object[]) filter;
        Object[] analysisFilter = new Object[] { filter1[6], filter1[7], filter1[8], filter1[9] };
        if (!rootBuilder) {
            productCategoryAnalysisInfoList = dlSales.list(ProductCategoryAnalysisInfo.class, analysisFilter);
            buildTreeForList(productCategoryAnalysisInfoList, rootNode);
            rootBuilder = true;
        }
        List<ProductCategoryTransaction> productCategoryTransaction = dlSales.productCategoryTransactionListQBF(filter1);
        productCategoryTransaction.addAll(dlSales.productCategoryPOSTransactionListQBF(filter1));
        reflush(rootNode, productCategoryTransaction);
    }

    private void reflush(ProductCategoryTreeTableNode node, List<ProductCategoryTransaction> productCategoryTransaction) {
        if (node.getNode() != null) {
            node.setIncludeTransaction(getProductCategoryTransactionByCagegoryId(node.getNode().getID(), productCategoryTransaction));
        }
        for (int i = 0; i < node.getChildCount(); i++) reflush((ProductCategoryTreeTableNode) node.getChildAt(i), productCategoryTransaction);
    }

    private void buildTreeForList(List<ProductCategoryAnalysisInfo> m_aData, ProductCategoryTreeTableNode node) {
        if (m_aData.isEmpty()) return;
        List<ProductCategoryAnalysisInfo> recapt = new ArrayList();
        ProductCategoryTreeTableNode temp;
        for (ProductCategoryAnalysisInfo c : m_aData) {
            if (node.isChinad(c)) {
                temp = new ProductCategoryTreeTableNode(c);
                temp.setParent(node);
                node.insert(temp, node.getChildCount());
            } else recapt.add(c);
        }
        for (int i = 0; i < node.getChildCount(); i++) buildTreeForList(recapt, (ProductCategoryTreeTableNode) node.getChildAt(i));
    }

    private Map<String, ProductCategoryTransaction> getProductCategoryTransactionByCagegoryId(String id, List<ProductCategoryTransaction> aList) {
        Map<String, ProductCategoryTransaction> rete = new HashMap<String, ProductCategoryTransaction>();
        String realOrderName;
        for (ProductCategoryTransaction t : aList) {
            if (id.equalsIgnoreCase(t.getProductCategoryId())) {
                realOrderName = t.getOrderTypeName() + "(" + (t.getOrderType() == 0 ? OrderUtil.ORDERTYPE_Normal : OrderUtil.ORDERTYPE_Revoerse) + ")";
                if (!transactionColumnName.contains(realOrderName)) transactionColumnName.add(realOrderName);
                rete.put(realOrderName, t);
            }
        }
        aList.removeAll(rete.values());
        return rete;
    }

    class ProductCategoryTreeTableModel extends AbstractTreeTableModel {

        public ProductCategoryTreeTableModel(TreeTableNode root) {
            super(root);
        }

        @Override
        public String getColumnName(int column) {
            if (column < COLUMNNAME_ProductCategoryAnalysisInfo.length) return COLUMNNAME_ProductCategoryAnalysisInfo[column];
            return transactionColumnName.get(column - COLUMNNAME_ProductCategoryAnalysisInfo.length);
        }

        @Override
        public int getColumnCount() {
            return COLUMNNAME_ProductCategoryAnalysisInfo.length + transactionColumnName.size();
        }

        @Override
        public Object getValueAt(Object node, int column) {
            if (column < 0 || column >= getColumnCount()) {
                throw new IllegalArgumentException("column must be a valid index");
            }
            TreeTableNode ttn = (TreeTableNode) node;
            if (column >= ttn.getColumnCount()) {
                return null;
            }
            return ttn.getValueAt(column);
        }

        @Override
        public Object getChild(Object parent, int index) {
            return ((TreeTableNode) parent).getChildAt(index);
        }

        @Override
        public int getChildCount(Object parent) {
            return ((TreeTableNode) parent).getChildCount();
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            return ((TreeTableNode) parent).getIndex((TreeTableNode) child);
        }

        public void reflush() {
            super.root = rootNode;
            modelSupport.fireNewRoot();
        }

        public TreeTableNode[] getPathToRoot(TreeTableNode aNode) {
            List<TreeTableNode> path = new ArrayList<TreeTableNode>();
            TreeTableNode node = aNode;
            while (node != root) {
                path.add(0, node);
                node = (TreeTableNode) node.getParent();
            }
            if (node == root) {
                path.add(0, node);
            }
            return path.toArray(new TreeTableNode[0]);
        }
    }

    class ProductCategoryTreeTableNode implements TreeTableNode {

        ProductCategoryTreeTableNode parent;

        ProductCategoryAnalysisInfo node;

        Map<String, ProductCategoryTransaction> transaction = new HashMap<String, ProductCategoryTransaction>();

        List<ProductCategoryTreeTableNode> childs = new ArrayList();

        public ProductCategoryTreeTableNode(ProductCategoryTreeTableNode parent, ProductCategoryAnalysisInfo node) {
            this.parent = parent;
            this.node = node;
        }

        public void insert(ProductCategoryTreeTableNode temp, int childCount) {
            childs.add(childCount, temp);
        }

        public ProductCategoryTreeTableNode(ProductCategoryAnalysisInfo node) {
            this.node = node;
        }

        public boolean isChinad(I_Category category1) {
            boolean isChinad = false;
            if (node == null) {
                if (category1.getM_sParentID() == null) isChinad = true;
            } else if (node.getID().equalsIgnoreCase(category1.getM_sParentID())) isChinad = true;
            return isChinad;
        }

        @Override
        public int getChildCount() {
            return childs.size();
        }

        @Override
        public int getIndex(TreeNode node) {
            return 0;
        }

        @Override
        public boolean getAllowsChildren() {
            return !childs.isEmpty();
        }

        @Override
        public boolean isLeaf() {
            return childs.isEmpty();
        }

        @Override
        public Enumeration<? extends TreeTableNode> children() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TreeTableNode getChildAt(int i) {
            return childs.get(i);
        }

        @Override
        public int getColumnCount() {
            return COLUMNNAME_ProductCategoryAnalysisInfo.length + transactionColumnName.size();
        }

        @Override
        public TreeTableNode getParent() {
            return this.parent;
        }

        @Override
        public Object getUserObject() {
            return node;
        }

        @Override
        public Object getValueAt(int column) {
            if (node == null) return null;
            if (column < COLUMNNAME_ProductCategoryAnalysisInfo.length) {
                switch(column) {
                    case 0:
                        return node.getName();
                    case 1:
                        return node.getMultiply();
                    case 2:
                        return Formats.CURRENCY.formatValue(node.getBuyPrice());
                    case 3:
                        return Formats.CURRENCY.formatValue(node.getSellPrice());
                    case 4:
                        return Formats.PERCENT.formatValue(node.getProfits());
                }
            } else {
                int realColumnIndex = column - COLUMNNAME_ProductCategoryAnalysisInfo.length;
                ProductCategoryTransaction pTran = getTransaxtion(transactionColumnName.get(realColumnIndex));
                if (pTran != null) return pTran.getMultiply();
            }
            return null;
        }

        private ProductCategoryTransaction getTransaxtion(String string) {
            return transaction.get(string);
        }

        @Override
        public boolean isEditable(int arg0) {
            return false;
        }

        @Override
        public void setUserObject(Object arg0) {
        }

        @Override
        public void setValueAt(Object arg0, int arg1) {
        }

        public ProductCategoryAnalysisInfo getNode() {
            return node;
        }

        public void setNode(ProductCategoryAnalysisInfo node) {
            this.node = node;
        }

        public Map<String, ProductCategoryTransaction> getIncludeTransaction() {
            return transaction;
        }

        public void setIncludeTransaction(Map<String, ProductCategoryTransaction> includeTransaction) {
            transaction = includeTransaction;
        }

        public List<ProductCategoryTreeTableNode> getChilds() {
            return childs;
        }

        public void setChilds(List<ProductCategoryTreeTableNode> childs) {
            this.childs = childs;
        }

        public void setParent(ProductCategoryTreeTableNode parent) {
            this.parent = parent;
        }
    }
}
