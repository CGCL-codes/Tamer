package com.organic.maynard.outliner.menus.file;

import com.organic.maynard.outliner.model.DocumentInfo;
import com.organic.maynard.outliner.model.propertycontainer.*;
import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.io.*;
import com.organic.maynard.outliner.guitree.*;
import com.organic.maynard.outliner.util.preferences.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.xml.sax.*;
import com.organic.maynard.io.FileTools;
import com.organic.maynard.util.string.StringTools;
import com.organic.maynard.util.string.Replace;
import com.organic.maynard.util.string.StanStringTools;
import com.organic.maynard.util.vector.StanVectorTools;
import com.organic.maynard.xml.XMLTools;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.5 $, $Date: 2004/02/03 21:56:36 $
 */
public class RecentFilesList extends AbstractOutlinerMenu implements ActionListener, GUITreeComponent, JoeReturnCodes, JoeXMLConstants {

    private static final String TRUNC_STRING = GUITreeLoader.reg.getText("trunc_string");

    private static final int CHRONO_ORDER = 0;

    private static final int ALFA_ORDER = 1;

    private static final int ASCII_ORDER = 2;

    private static final int FULL_PATHNAME = 0;

    private static final int TRUNC_PATHNAME = 1;

    private static final int JUST_FILENAME = 2;

    private static final int TOP_TO_BOTTOM = 0;

    private static final int BOTTOM_TO_TOP = 1;

    private static final int NOT_THERE = -1;

    private static RecentFilesList recentFilesList = null;

    private static Vector frameInfoList = null;

    private static TreeSet alfaAsciiTree = null;

    private static int currentDisplayOrdering = -1;

    private static int currentDisplayNameForm = -1;

    private static int currentDisplayDirection = -1;

    private static int currentRecentFilesListSize = 0;

    public RecentFilesList() {
        recentFilesList = this;
    }

    public void startSetup(Attributes atts) {
        super.startSetup(atts);
        setEnabled(false);
        ((JMenu) GUITreeLoader.elementStack.get(GUITreeLoader.elementStack.size() - 2)).add(this);
        currentRecentFilesListSize = Preferences.getPreferenceInt(Preferences.RECENT_FILES_LIST_SIZE).cur;
        java.util.List obj = PropertyContainerUtil.parseXML(Outliner.RECENT_FILES_FILE);
        if ((obj != null) && (obj instanceof Vector)) {
            frameInfoList = (Vector) obj;
            StanVectorTools.removeDupesHeadside(frameInfoList);
            int filSize = Preferences.getPreferenceInt(Preferences.FRAME_INFO_LIST_SIZE).cur;
            if (frameInfoList.size() > filSize) {
                StanVectorTools.trimSizeSaveTail(frameInfoList, filSize);
            }
        } else {
            frameInfoList = new Vector();
        }
    }

    public void actionPerformed(ActionEvent e) {
        DocumentInfo docInfo = ((RecentFilesListItem) e.getSource()).getDocumentInfo();
        String protocolName = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PROTOCOL_NAME);
        FileProtocol protocol = null;
        if (protocolName == null || protocolName.equals("")) {
            protocol = Outliner.fileProtocolManager.getDefault();
        } else {
            protocol = Outliner.fileProtocolManager.getProtocol(protocolName);
        }
        if (!PropertyContainerUtil.getPropertyAsBoolean(docInfo, DocumentInfo.KEY_IMPORTED)) {
            FileMenu.openFile(docInfo, protocol);
        } else {
            FileMenu.importFile(docInfo, protocol);
        }
    }

    public static Vector getFrameInfoList() {
        return frameInfoList;
    }

    public static int getSizeOfFrameInfoList() {
        return frameInfoList.size();
    }

    public static void setFrameInfoList(Vector list) {
        frameInfoList = list;
    }

    public static void addDocumentInfo(DocumentInfo docInfo) {
        frameInfoList.add(docInfo);
    }

    public void setDisplayOptions(int ordering, int nameForm, int direction) {
        if ((currentDisplayOrdering != ordering) || (currentDisplayNameForm != nameForm) || (currentDisplayDirection != direction)) {
            currentDisplayOrdering = ordering;
            currentDisplayNameForm = nameForm;
            currentDisplayDirection = direction;
            syncTreeSet();
            syncMenuItems();
        }
    }

    private void syncTreeSet() {
        if (alfaAsciiTree == null) {
            alfaAsciiTree = new TreeSet();
        }
        alfaAsciiTree.clear();
        if ((currentDisplayOrdering != ALFA_ORDER) && (currentDisplayOrdering != ASCII_ORDER)) {
            return;
        }
        int frameInfoListSize = frameInfoList.size();
        if (frameInfoListSize == 0) {
            return;
        }
        int recentFilesListSize = Preferences.getPreferenceInt(Preferences.RECENT_FILES_LIST_SIZE).cur;
        if (recentFilesListSize == 0) {
            return;
        }
        DocumentInfo docInfo = null;
        StrungDocumentInfo strungDocInfo = null;
        for (int i = frameInfoListSize - 1, j = 0; (i >= 0) && (j < recentFilesListSize); i--) {
            docInfo = (DocumentInfo) frameInfoList.get(i);
            if (PropertyContainerUtil.getPropertyAsBoolean(docInfo, DocumentInfo.KEY_HELP_FILE)) {
                continue;
            }
            String path = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH);
            switch(currentDisplayNameForm) {
                case FULL_PATHNAME:
                default:
                    strungDocInfo = new StrungDocumentInfo(path, docInfo);
                    break;
                case TRUNC_PATHNAME:
                    strungDocInfo = new StrungDocumentInfo(StanStringTools.getTruncatedPathName(path, TRUNC_STRING), docInfo);
                    break;
                case JUST_FILENAME:
                    strungDocInfo = new StrungDocumentInfo(StanStringTools.getFileNameFromPathName(path), docInfo);
                    break;
            }
            strungDocInfo.setIgnoreCase(currentDisplayOrdering == ALFA_ORDER);
            alfaAsciiTree.add(strungDocInfo);
            j++;
        }
    }

    private void syncMenuItems() {
        removeAll();
        int frameInfoListSize = frameInfoList.size();
        if (frameInfoListSize == 0) {
            return;
        }
        int recentFilesListSize = Preferences.getPreferenceInt(Preferences.RECENT_FILES_LIST_SIZE).cur;
        if (recentFilesListSize == 0) {
            return;
        }
        switch(currentDisplayOrdering) {
            case CHRONO_ORDER:
            default:
                if (currentDisplayOrdering == TOP_TO_BOTTOM) {
                    currentDisplayOrdering = BOTTOM_TO_TOP;
                } else {
                    currentDisplayOrdering = TOP_TO_BOTTOM;
                }
                DocumentInfo docInfo = null;
                for (int j = 0, i = frameInfoListSize - 1; (i >= 0) && (j < recentFilesListSize); i--) {
                    docInfo = (DocumentInfo) frameInfoList.get(i);
                    if (docInfo == null) {
                        continue;
                    }
                    if (PropertyContainerUtil.getPropertyAsBoolean(docInfo, DocumentInfo.KEY_HELP_FILE)) {
                        continue;
                    }
                    addMenuItemForFileToMenu(docInfo);
                    j++;
                }
                if (currentDisplayOrdering == TOP_TO_BOTTOM) {
                    currentDisplayOrdering = BOTTOM_TO_TOP;
                } else {
                    currentDisplayOrdering = TOP_TO_BOTTOM;
                }
                break;
            case ALFA_ORDER:
            case ASCII_ORDER:
                StrungDocumentInfo sdi = null;
                for (Iterator iter = alfaAsciiTree.iterator(); iter.hasNext(); ) {
                    sdi = (StrungDocumentInfo) iter.next();
                    addMenuItemForFileToMenu(sdi.getDocumentInfo());
                }
                break;
        }
    }

    /**
	 * Adds a menu item for a file to the  recent files menu.
	 */
    private void addMenuItemForFileToMenu(DocumentInfo docInfo) {
        RecentFilesListItem menuItem = null;
        String path = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH);
        switch(currentDisplayNameForm) {
            case FULL_PATHNAME:
            default:
                menuItem = new RecentFilesListItem(path, docInfo);
                break;
            case TRUNC_PATHNAME:
                menuItem = new RecentFilesListItem(StanStringTools.getTruncatedPathName(path, TRUNC_STRING), docInfo);
                break;
            case JUST_FILENAME:
                menuItem = new RecentFilesListItem(StanStringTools.getFileNameFromPathName(path), docInfo);
                break;
        }
        menuItem.addActionListener(this);
        switch(currentDisplayDirection) {
            case TOP_TO_BOTTOM:
            default:
                add(menuItem);
                break;
            case BOTTOM_TO_TOP:
                insert(menuItem, 0);
                break;
        }
        setEnabled(true);
    }

    protected static void addFileNameToList(DocumentInfo docInfo) {
        int position = NOT_THERE;
        String filename = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH);
        for (int i = 0, limit = frameInfoList.size(); i < limit; i++) {
            if (PropertyContainerUtil.propertyEqualsAsString(((DocumentInfo) frameInfoList.get(i)), DocumentInfo.KEY_PATH, filename)) {
                position = i;
            }
        }
        if (position == NOT_THERE) {
            if (frameInfoList.size() >= Preferences.getPreferenceInt(Preferences.FRAME_INFO_LIST_SIZE).cur) {
                frameInfoList.remove(0);
            }
            frameInfoList.add(docInfo);
        } else {
            frameInfoList.set(position, docInfo);
            StanVectorTools.moveElementToTail(frameInfoList, position);
        }
        recentFilesList.syncTreeSet();
        recentFilesList.syncMenuItems();
    }

    public static void removeFileNameFromList(DocumentInfo docInfo) {
        int position = NOT_THERE;
        String filename = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH);
        for (int i = 0, limit = frameInfoList.size(); i < limit; i++) {
            if (PropertyContainerUtil.propertyEqualsAsString(((DocumentInfo) frameInfoList.get(i)), DocumentInfo.KEY_PATH, filename)) {
                position = i;
            }
        }
        if (position == NOT_THERE) {
            return;
        }
        StanVectorTools.moveElementToTail(frameInfoList, position);
        frameInfoList.setSize(frameInfoList.size() - 1);
        recentFilesList.syncTreeSet();
        recentFilesList.syncMenuItems();
    }

    public static void syncSize() {
        int sizeSet = Preferences.getPreferenceInt(Preferences.RECENT_FILES_LIST_SIZE).cur;
        if (sizeSet != recentFilesList.currentRecentFilesListSize) {
            recentFilesList.currentRecentFilesListSize = sizeSet;
            recentFilesList.syncTreeSet();
            recentFilesList.syncMenuItems();
        }
    }

    /**
	 * Gets a DocumentInfo object for the provided filepath if a recent file
	 * exists for that filepath.
	 */
    public static DocumentInfo getDocumentInfo(String pathname) {
        if (pathname == null) {
            return null;
        }
        for (int i = 0, limit = frameInfoList.size(); i < limit; i++) {
            DocumentInfo docInfo = (DocumentInfo) frameInfoList.get(i);
            if (docInfo == null) {
                continue;
            }
            if (PropertyContainerUtil.propertyEqualsAsString(docInfo, DocumentInfo.KEY_PATH, pathname)) {
                return docInfo;
            }
        }
        return null;
    }

    /**
	 * Saves the recent files list to disk as a serialized object.
	 */
    public static void saveConfigFile(String filename) {
        StringBuffer buf = new StringBuffer();
        buf.append(XMLTools.getXMLDeclaration());
        String line_ending = "\n";
        buf.append(line_ending);
        PropertyContainerUtil.writeXML(buf, frameInfoList, 0, line_ending);
        try {
            FileTools.dumpStringToFile(new File(filename), buf.toString(), "UTF-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
