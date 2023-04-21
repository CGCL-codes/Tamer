package org.plazmaforge.framework.client.swt.controls;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeCombo extends AbstractExtCombo {

    /**
     * The Table Control
     */
    private Tree tree;

    private List<TreeItem> items = new ArrayList<TreeItem>();

    public TreeCombo(Composite parent, int style) {
        super(parent, style);
    }

    public TreeCombo(Composite parent, int style, int toolStyle) {
        super(parent, style, toolStyle);
    }

    @Override
    protected void createPopupContent() {
        int style = getStyle();
        int listStyle = SWT.SINGLE | SWT.V_SCROLL;
        if ((style & SWT.FLAT) != 0) listStyle |= SWT.FLAT;
        if ((style & SWT.RIGHT_TO_LEFT) != 0) listStyle |= SWT.RIGHT_TO_LEFT;
        if ((style & SWT.LEFT_TO_RIGHT) != 0) listStyle |= SWT.LEFT_TO_RIGHT;
        tree = new Tree(popup, listStyle);
        tree.setHeaderVisible(false);
        tree.setLinesVisible(true);
        popupContent = tree;
        initPopupContentListener();
    }

    @Override
    protected Control getTextParent() {
        return text;
    }

    protected void doControlAdd(String string) {
        doControlAdd(string, null);
    }

    protected void doControlAdd(String string, int index) {
        TreeItem item = getTreeItem(index);
        item.setText(string);
    }

    protected void doControlRemove(int index) {
        TreeItem item = getTreeItem(index);
        if (item != null) {
            if (!item.isDisposed()) {
                item.dispose();
            }
            items.remove(item);
        }
    }

    protected void doControlRemove(int start, int end) {
        for (int i = start; i <= end; i++) {
            doControlRemove(i);
        }
    }

    protected void doControlRemoveAll() {
        tree.removeAll();
    }

    protected void doControlDeselect(int index) {
        TreeItem item = getTreeItem(index);
        tree.deselect(item);
    }

    protected void doControlDeselectAll() {
        tree.deselectAll();
    }

    protected String doControlGetItem(int index) {
        return getTreeItem(index).getText();
    }

    protected int doControlGetItemCount() {
        return getTreeItems().size();
    }

    protected int doControlGetItemHeight() {
        return tree.getItemHeight();
    }

    protected String[] doControlGetItems() {
        List<TreeItem> items = getTreeItems();
        String[] array = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            array[i] = items.get(i).getText();
        }
        return array;
    }

    protected int doControlGetSelectionIndex() {
        TreeItem[] items = tree.getSelection();
        if (items == null || items.length == 0) {
            return -1;
        }
        TreeItem item = items[0];
        return indexOfTreeItem(item);
    }

    protected void doControlSetSelectionIndex(int index) {
        TreeItem item = getTreeItem(index);
        tree.setSelection(item);
    }

    protected void doControlShowSelection() {
        tree.showSelection();
    }

    protected int doControlIndexOf(String string) {
        if (string == null) {
            return -1;
        }
        List<TreeItem> items = getTreeItems();
        for (int i = 0; i < items.size(); i++) {
            TreeItem item = items.get(i);
            if (item.getText().equalsIgnoreCase(string)) return i;
        }
        return -1;
    }

    protected void doControlAdd(String string, Image image) {
        createTreeItem(string, image);
    }

    public void add(String string, Image image) {
        checkWidget();
        doControlAdd(string, image);
    }

    protected TreeItem createTreeItem(String string, Image image) {
        TreeItem item = new TreeItem(tree, 0);
        item.setText(string);
        item.setImage(image);
        return item;
    }

    protected TreeItem createTreeItem(TreeItem parentItem, String string, Image image) {
        TreeItem item = new TreeItem(parentItem, 0);
        item.setText(string);
        item.setImage(image);
        return item;
    }

    public void addValue(Object value) {
        addValue(value, null);
    }

    public void addValue(Object value, Image image) {
        checkWidget();
        if (value == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        Object id = getModel().getObjectKeyValue(value);
        Object parentId = getModel().getObjectParentKeyValue(value);
        String string = getDisplayText(value);
        TreeItem parentItem = getItemById(parentId);
        TreeItem item = parentItem == null ? createTreeItem(string, image) : createTreeItem(parentItem, string, image);
        items.add(item);
        item.setData(value);
        item.setData("id", id);
        doModelAdd(value);
    }

    public void setDataList(List dataList, boolean isResetValue) {
        List list = dataList;
        if (list != null) {
            list = new ArrayList(dataList);
            getModel().sortByParent(list);
        }
        super.setDataList(list, isResetValue);
    }

    private TreeItem getItemById(Object id) {
        if (id == null || tree == null) {
            return null;
        }
        List<TreeItem> items = getTreeItems();
        TreeItem item = null;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            Object itemId = item.getData("id");
            if (id.equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    protected void doSetSelectionText() {
        TreeItem[] items = tree.getSelection();
        if (items == null || items.length == 0) {
            return;
        }
        TreeItem item = items[0];
        String data = item.getText();
        doSetText(data);
    }

    private TreeItem getTreeItem(int index) {
        return items.get(index);
    }

    private List<TreeItem> getTreeItems() {
        return items;
    }

    private int indexOfTreeItem(TreeItem findItem) {
        if (findItem == null || tree == null) {
            return -1;
        }
        List<TreeItem> items = getTreeItems();
        TreeItem item = null;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            if (findItem == item) {
                return i;
            }
        }
        return -1;
    }
}
