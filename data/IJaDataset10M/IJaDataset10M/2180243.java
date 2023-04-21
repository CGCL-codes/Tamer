package net.suberic.pooka.gui.search;

import net.suberic.pooka.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.Vector;
import java.util.HashMap;

/**
 * This is a panel which allows you to choose which folders you will
 * use for searches.
 */
public class SearchFolderPanel extends JPanel {

    Vector selected = new Vector();

    boolean isEditable = true;

    JList folderListSelector;

    java.util.HashMap labelToFolderMap;

    /**
     * Create a SearchFolderPanel with no folders selected.  By default, this
     * means that we have a SearchFolderPanel which can select from all of
     * the available Folders.
     */
    public SearchFolderPanel() {
        this(new FolderInfo[0], Pooka.getStoreManager().getAllOpenFolders());
    }

    /**
     * Create a SearchFolderPanel with all the Folders in the given Store(s)
     * selected.  By default, all available folders will be allowed to be
     * selected.
     */
    public SearchFolderPanel(StoreInfo[] storeList) {
        this(storeList, Pooka.getStoreManager().getAllOpenFolders());
    }

    /**
     * Create a SearchFolderPanel with all the Folders in the given Store(s)
     * selected.  allowedValues shows which Folders are available.
     */
    public SearchFolderPanel(StoreInfo[] storeList, Vector allowedValues) {
        selected = new Vector();
        for (int i = 0; i < storeList.length; i++) selected.addAll(storeList[i].getAllFolders());
        if (!isEditable) {
            createStaticPanel(selected);
        } else {
            createDynamicPanel(selected, allowedValues);
        }
    }

    /**
     * Create a SearchFolderPanel with all the Folders given selected.
     * By default, all available folders will be allowed to be
     * selected.
     */
    public SearchFolderPanel(FolderInfo[] folderList) {
        this(folderList, Pooka.getStoreManager().getAllOpenFolders());
    }

    /**
     * Create a SearchFolderPanel with all the Folders given selected.
     * allowedValues shows which Folders are available.
     */
    public SearchFolderPanel(FolderInfo[] folderList, Vector allowedValues) {
        for (int i = 0; i < folderList.length; i++) {
            selected.add(folderList[i]);
        }
        if (!isEditable) {
            createStaticPanel(selected);
        } else {
            createDynamicPanel(selected, allowedValues);
        }
    }

    /**
     * This creates a static panel.
     */
    public void createStaticPanel(Vector folderList) {
        StringBuffer msg = new StringBuffer();
        msg.append("Searching folders:\n");
        for (int i = 0; i < folderList.size(); i++) msg.append(((FolderInfo) folderList.elementAt(i)).getFolderID() + "\n");
        JTextArea label = new JTextArea(msg.toString());
        this.add(label);
    }

    /**
     * Creates a dynamic panel with the given Folders selected.
     */
    public void createDynamicPanel(Vector folderList, Vector allowedValues) {
        Vector folderNameList = new Vector();
        labelToFolderMap = new java.util.HashMap();
        for (int i = 0; i < allowedValues.size(); i++) {
            FolderInfo folder = (FolderInfo) allowedValues.elementAt(i);
            String name = folder.getFolderID();
            folderNameList.add(name);
            labelToFolderMap.put(name, folder);
        }
        folderListSelector = new JList(folderNameList);
        JScrollPane scroller = new JScrollPane(folderListSelector, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        Dimension scrollerPrefSize = new Dimension(500, 100);
        scroller.setPreferredSize(scrollerPrefSize);
        this.add(scroller);
        folderListSelector.setValueIsAdjusting(true);
        for (int i = 0; i < folderList.size(); i++) {
            String folderId = ((FolderInfo) folderList.elementAt(i)).getFolderID();
            if (folderListSelector.getSelectedIndex() == -1) {
                folderListSelector.setSelectedValue(folderId, true);
            } else {
                int index = folderNameList.indexOf(folderId);
                if (index != -1) folderListSelector.addSelectionInterval(index, index);
            }
        }
        folderListSelector.setValueIsAdjusting(false);
    }

    /**
     * Returns a Vector of selected FolderInfos to search.
     */
    public Vector getSelectedFolders() {
        if (!isEditable) {
            return selected;
        } else {
            Vector returnValue = new Vector();
            Object[] selectedValues = folderListSelector.getSelectedValues();
            for (int i = 0; i < selectedValues.length; i++) {
                String currentSelection = (String) selectedValues[i];
                FolderInfo currentFolder = (FolderInfo) labelToFolderMap.get(currentSelection);
                returnValue.add(currentFolder);
            }
            return returnValue;
        }
    }
}
