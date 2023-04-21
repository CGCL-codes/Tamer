package com.nhncorp.cubridqa.utils;

import java.util.Set;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * The utility class provide velocity template generation and format string through velocity template .
 * @ClassName: TreeUtil
 * @date 2009-9-1
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class TreeUtil {

    /**
	 * dispose the selected tree item .
	 * 
	 * @param treeItem
	 */
    public static void removeItem(TreeItem treeItem) {
        treeItem.dispose();
    }

    /**
	 * get the items which checked Recursively.
	 * 
	 * @param treeItem
	 * @param set
	 */
    public static void getCheckedItem(TreeItem treeItem, Set<TreeItem> set) {
        if (treeItem.getChecked()) {
            set.add(treeItem);
        }
        if (treeItem.getItems() != null && treeItem.getItems().length > 0) {
            TreeItem[] items = treeItem.getItems();
            for (TreeItem item : items) {
                getCheckedItem(item, set);
            }
        }
    }

    /**
	 * get all the tree item Recursively.
	 * 
	 * @param treeItem
	 * @param set
	 */
    public static void getAllItem(TreeItem treeItem, Set<TreeItem> set) {
        set.add(treeItem);
        if (treeItem.getItems() != null && treeItem.getItems().length > 0) {
            TreeItem[] items = treeItem.getItems();
            for (TreeItem item : items) {
                getAllItem(item, set);
            }
        }
    }
}
