package com.organic.maynard.outliner.menus.file;

import com.organic.maynard.outliner.model.DocumentInfo;
import com.organic.maynard.outliner.model.propertycontainer.*;
import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.data.IntList;
import com.organic.maynard.outliner.io.*;
import com.organic.maynard.outliner.guitree.*;
import com.organic.maynard.outliner.util.preferences.*;
import com.organic.maynard.outliner.util.undo.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.awt.*;
import javax.swing.*;
import org.xml.sax.*;
import com.organic.maynard.util.string.Replace;
import com.organic.maynard.util.string.StanStringTools;

/**
 * This class implements the meat of several File Menu commands: New, Open, Import, Save, Revert, Close.
 *
 * @author  $Author: maynardd $
 * @version $Revision: 1.4 $, $Date: 2004/05/26 20:59:07 $
 */
public class FileMenu extends AbstractOutlinerMenu implements GUITreeComponent, JoeReturnCodes {

    private static final int MODE_SAVE = 0;

    private static final int MODE_EXPORT = 1;

    private static final int MODE_OPEN = 2;

    private static final int MODE_IMPORT = 3;

    private static final int MODE_REVERT = 4;

    private static final String TRUNC_STRING = GUITreeLoader.reg.getText("trunc_string");

    private static final int FULL_PATHNAME = 0;

    private static final int TRUNC_PATHNAME = 1;

    private static final int JUST_FILENAME = 2;

    public FileMenu() {
        super();
        Outliner.menuBar.fileMenu = this;
    }

    /**
	 * Exports a file.
	 *
	 * @param filename the filename to write the exported document to.
	 * @param document the document to export.
	 * @param protocol the Protocol to use when exporting the document.
	 */
    public static void exportFile(String filename, OutlinerDocument document, FileProtocol protocol) {
        saveFile(filename, document, protocol, true, MODE_EXPORT);
    }

    /**
	 * Saves a file using the protocol currently set on the Document's DocumentInfo object.
	 *
	 * @param filename the filename to write the saved document to.
	 * @param document the document to save.
	 * @param saveAs indicates if we are in the process of doing a "Save As" operation rather than a straight "Save".
	 */
    public static void saveFile(String filename, OutlinerDocument document, boolean saveAs) {
        FileProtocol protocol = Outliner.fileProtocolManager.getProtocol(PropertyContainerUtil.getPropertyAsString(document.getDocumentInfo(), DocumentInfo.KEY_PROTOCOL_NAME));
        saveFile(filename, document, protocol, saveAs, MODE_SAVE);
    }

    /**
	 * Saves a file.
	 *
	 * @param filename the filename to write the saved document to.
	 * @param document the document to save.
	 * @param protocol the Protocol to use when saving the document.
	 * @param saveAs indicates if we are in the process of doing a "Save As" operation rather than a straight "Save".
	 */
    public static void saveFile(String filename, OutlinerDocument document, FileProtocol protocol, boolean saveAs) {
        saveFile(filename, document, protocol, saveAs, MODE_SAVE);
    }

    /**
	 * The main save routine which the above export and save methods resolve to.
	 *
	 * @param filename the filename to write the saved document to.
	 * @param document the document to save.
	 * @param protocol the Protocol to use when saving the document.
	 * @param saveAs indicates if we are in the process of doing a "Save As" operation rather than a straight "Save".
	 * @param mode indicates if we are in the process of doing a "Save" or an "Export".
	 */
    private static void saveFile(String filename, OutlinerDocument document, FileProtocol protocol, boolean saveAs, int mode) {
        String msg = null;
        DocumentInfo docInfo = document.getDocumentInfo();
        String fileFormatName = null;
        SaveFileFormat saveOrExportFileFormat = null;
        boolean commentExists = false;
        boolean editableExists = false;
        boolean moveableExists = false;
        boolean attributesExist = false;
        boolean documentAttributesExist = false;
        boolean wereImported = false;
        String savedDocsPrevName = null;
        String title;
        PropertyContainerUtil.setPropertyAsString(docInfo, DocumentInfo.KEY_PROTOCOL_NAME, protocol.getName());
        switch(mode) {
            case MODE_SAVE:
                if (saveAs) {
                    fileFormatName = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_FILE_FORMAT);
                } else {
                    fileFormatName = document.settings.getSaveFormat().cur;
                }
                saveOrExportFileFormat = Outliner.fileFormatManager.getSaveFormat(fileFormatName);
                savedDocsPrevName = document.getFileName();
                document.setFileName(filename);
                docInfo.updateDocumentInfoForDocument(document, saveAs);
                break;
            case MODE_EXPORT:
                fileFormatName = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_FILE_FORMAT);
                saveOrExportFileFormat = Outliner.fileFormatManager.getExportFormat(fileFormatName);
                PropertyContainerUtil.setPropertyAsString(docInfo, DocumentInfo.KEY_PATH, filename);
                break;
            default:
                System.out.println("FileMenu:SaveFile: bad mode parameter");
                return;
        }
        if (saveOrExportFileFormat == null) {
            msg = GUITreeLoader.reg.getText("error_could_not_save_no_file_format");
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH));
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_2, fileFormatName);
            JOptionPane.showMessageDialog(document, msg);
            return;
        }
        if (document.tree.getAttributeCount() > 0) {
            documentAttributesExist = true;
        }
        Node node = document.tree.getRootNode();
        while (true) {
            node = node.nextNode();
            if (node.isRoot()) {
                break;
            }
            if (!commentExists && node.isComment()) {
                commentExists = true;
            }
            if (!editableExists && !node.isEditable()) {
                editableExists = true;
            }
            if (!moveableExists && !node.isMoveable()) {
                moveableExists = true;
            }
            if (!attributesExist && node.getAttributeCount() > 0) {
                attributesExist = true;
            }
        }
        if (commentExists && !saveOrExportFileFormat.supportsComments()) {
            msg = GUITreeLoader.reg.getText("error_file_format_does_not_support_comments");
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, fileFormatName);
            if (USER_ABORTED == promptUser(msg)) {
                return;
            }
        }
        if (editableExists && !saveOrExportFileFormat.supportsEditability()) {
            msg = GUITreeLoader.reg.getText("error_file_format_does_not_support_editability");
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, fileFormatName);
            if (USER_ABORTED == promptUser(msg)) {
                return;
            }
        }
        if (moveableExists && !saveOrExportFileFormat.supportsMoveability()) {
            msg = GUITreeLoader.reg.getText("error_file_format_does_not_support_moveability");
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, fileFormatName);
            if (USER_ABORTED == promptUser(msg)) {
                return;
            }
        }
        if (attributesExist && !saveOrExportFileFormat.supportsAttributes()) {
            msg = GUITreeLoader.reg.getText("error_file_format_does_not_support_attributes");
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, fileFormatName);
            if (USER_ABORTED == promptUser(msg)) {
                return;
            }
        }
        if (documentAttributesExist && !saveOrExportFileFormat.supportsDocumentAttributes()) {
            msg = GUITreeLoader.reg.getText("error_file_format_does_not_support_document_attributes");
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, fileFormatName);
            if (USER_ABORTED == promptUser(msg)) {
                return;
            }
        }
        if (document.hoistStack.isHoisted()) {
            document.hoistStack.temporaryDehoistAll();
        }
        byte[] bytes = saveOrExportFileFormat.save(document.tree, docInfo);
        int saveOrExportResult = USER_ABORTED;
        if (bytes != null) {
            docInfo.setOutputBytes(bytes);
            if (wereImported = PropertyContainerUtil.getPropertyAsBoolean(docInfo, DocumentInfo.KEY_IMPORTED)) {
                PropertyContainerUtil.setPropertyAsBoolean(docInfo, DocumentInfo.KEY_IMPORTED, false);
            }
            if (protocol.saveFile(docInfo)) {
                saveOrExportResult = SUCCESS;
            } else {
                saveOrExportResult = FAILURE;
            }
            docInfo.setOutputBytes(null);
        }
        if (document.hoistStack.isHoisted()) {
            document.hoistStack.temporaryHoistAll();
        }
        switch(saveOrExportResult) {
            case FAILURE:
                msg = GUITreeLoader.reg.getText("error_could_not_save_file");
                msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH));
                JOptionPane.showMessageDialog(document, msg);
            case USER_ABORTED:
                if (wereImported) {
                    PropertyContainerUtil.setPropertyAsBoolean(docInfo, DocumentInfo.KEY_IMPORTED, true);
                }
                break;
            case SUCCESS:
                switch(mode) {
                    case MODE_EXPORT:
                        if (wereImported) {
                            PropertyContainerUtil.setPropertyAsBoolean(docInfo, DocumentInfo.KEY_IMPORTED, true);
                        }
                        break;
                    case MODE_SAVE:
                        document.settings.setUseDocumentSettings(true);
                        UndoableEdit.freezeUndoEdit(document.tree.getEditingNode());
                        RecentFilesList.addFileNameToList(docInfo);
                        document.setModified(false);
                        switch(OutlinerDocument.getTitleNameForm()) {
                            case FULL_PATHNAME:
                            default:
                                title = filename;
                                break;
                            case TRUNC_PATHNAME:
                                title = StanStringTools.getTruncatedPathName(filename, TRUNC_STRING);
                                break;
                            case JUST_FILENAME:
                                title = StanStringTools.getFileNameFromPathName(filename);
                                break;
                        }
                        document.setTitle(title);
                        Outliner.menuBar.windowMenu.updateWindow(document);
                        break;
                }
                break;
        }
    }

    /**
	 * Imports a file. Similar to opening a file. The key difference
	 * is that we assume we can't save it.
	 *
	 * @param docInfo contains all the info about the file we're importing.
	 * @param protocol the Protocol to use when importing the document.
	 */
    protected static void importFile(DocumentInfo docInfo, FileProtocol protocol) {
        openFile(docInfo, protocol, MODE_IMPORT);
    }

    /**
	 * Opens a file. Similar to importing a file. The key difference
	 * is that we assume we can save it.
	 *
	 * @param docInfo contains all the info about the file we're opening.
	 * @param protocol the Protocol to use when opening the document.
	 */
    public static void openFile(DocumentInfo docInfo, FileProtocol protocol) {
        openFile(docInfo, protocol, MODE_OPEN);
    }

    /**
	 * This is the main method used to open or import a file. The above
	 * import and open methods all resolve to this method.
	 *
	 * @param docInfo contains all the info about the file we're opening.
	 * @param protocol the Protocol to use when opening the document.
	 * @param mode indicates if the current process is an "Open" or an "Import".
	 */
    protected static void openFile(DocumentInfo docInfo, FileProtocol protocol, int mode) {
        if (mode != MODE_OPEN && mode != MODE_IMPORT) {
            System.out.println("FileMenu:OpenFile: invalid mode parameter");
            return;
        }
        String filename = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH);
        if (!Outliner.documents.isFileNameUnique(filename)) {
            String msg = GUITreeLoader.reg.getText("message_file_already_open");
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, filename);
            JOptionPane.showMessageDialog(Outliner.outliner, msg);
            return;
        }
        JoeTree tree = Outliner.newTree(null);
        int openOrImportResult = openOrImportFileAndGetTree(tree, docInfo, protocol, mode);
        if ((openOrImportResult != SUCCESS) && (openOrImportResult != SUCCESS_MODIFIED)) {
            return;
        }
        OutlinerDocument newDoc = new OutlinerDocument(PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH), docInfo);
        newDoc.settings.setUseDocumentSettings(true);
        newDoc.setTree(tree);
        if (PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_LINE_ENDING).length() == 0) {
            PropertyContainerUtil.setPropertyAsString(docInfo, DocumentInfo.KEY_LINE_ENDING, Preferences.getPreferenceLineEnding(Preferences.SAVE_LINE_END).cur);
        }
        syncDocumentToDocInfo(newDoc, mode);
        if (mode == MODE_IMPORT) {
            PropertyContainerUtil.setPropertyAsBoolean(docInfo, DocumentInfo.KEY_IMPORTED, true);
        }
        RecentFilesList.addFileNameToList(docInfo);
        setupAndDraw(docInfo, newDoc, openOrImportResult);
    }

    protected static void revertFile(OutlinerDocument document) {
        DocumentInfo docInfo = document.getDocumentInfo();
        FileProtocol protocol = Outliner.fileProtocolManager.getProtocol(PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PROTOCOL_NAME));
        int mode = PropertyContainerUtil.getPropertyAsBoolean(docInfo, DocumentInfo.KEY_IMPORTED) ? MODE_IMPORT : MODE_OPEN;
        JoeTree tree = Outliner.newTree(null);
        int openOrImportResult = openOrImportFileAndGetTree(tree, docInfo, protocol, mode);
        if ((openOrImportResult != SUCCESS) && (openOrImportResult != SUCCESS_MODIFIED)) {
            return;
        } else {
            document.setTree(tree);
        }
        document.undoQueue.clear();
        document.hoistStack.clear();
        Outliner.documentAttributes.configure(tree);
        document.getSettings().setUseDocumentSettings(true);
        syncDocumentToDocInfo(document, MODE_REVERT);
        ((DocumentSettingsView) GUITreeLoader.reg.get(GUITreeComponentRegistry.JDIALOG_DOCUMENT_SETTINGS_VIEW)).configure(document.getSettings());
        setupAndDraw(docInfo, document, openOrImportResult);
    }

    private static void syncDocumentToDocInfo(OutlinerDocument doc, int mode) {
        DocumentInfo docInfo = doc.getDocumentInfo();
        doc.settings.getLineEnd().def = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_LINE_ENDING);
        doc.settings.getLineEnd().restoreCurrentToDefault();
        doc.settings.getLineEnd().restoreTemporaryToDefault();
        doc.settings.getSaveEncoding().def = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_ENCODING_TYPE);
        doc.settings.getSaveEncoding().restoreCurrentToDefault();
        doc.settings.getSaveEncoding().restoreTemporaryToDefault();
        if (mode == MODE_IMPORT) {
            doc.settings.getSaveFormat().def = Preferences.getPreferenceString(Preferences.SAVE_FORMAT).cur;
        } else {
            doc.settings.getSaveFormat().def = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_FILE_FORMAT);
        }
        doc.settings.getSaveFormat().restoreCurrentToDefault();
        doc.settings.getSaveFormat().restoreTemporaryToDefault();
        doc.settings.getApplyFontStyleForComments().def = PropertyContainerUtil.getPropertyAsBoolean(docInfo, DocumentInfo.KEY_APPLY_FONT_STYLE_FOR_COMMENTS);
        doc.settings.getApplyFontStyleForComments().restoreCurrentToDefault();
        doc.settings.getApplyFontStyleForComments().restoreTemporaryToDefault();
        doc.settings.getApplyFontStyleForEditability().def = PropertyContainerUtil.getPropertyAsBoolean(docInfo, DocumentInfo.KEY_APPLY_FONT_STYLE_FOR_EDITABILITY);
        doc.settings.getApplyFontStyleForEditability().restoreCurrentToDefault();
        doc.settings.getApplyFontStyleForEditability().restoreTemporaryToDefault();
        doc.settings.getApplyFontStyleForMoveability().def = PropertyContainerUtil.getPropertyAsBoolean(docInfo, DocumentInfo.KEY_APPLY_FONT_STYLE_FOR_MOVEABILITY);
        doc.settings.getApplyFontStyleForMoveability().restoreCurrentToDefault();
        doc.settings.getApplyFontStyleForMoveability().restoreTemporaryToDefault();
        doc.settings.getUseCreateModDates().def = PropertyContainerUtil.getPropertyAsBoolean(docInfo, DocumentInfo.KEY_USE_CREATE_MOD_DATES);
        doc.settings.getUseCreateModDates().restoreCurrentToDefault();
        doc.settings.getUseCreateModDates().restoreTemporaryToDefault();
        doc.settings.getCreateModDatesFormat().def = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_CREATE_MOD_DATES_FORMAT);
        doc.settings.getCreateModDatesFormat().restoreCurrentToDefault();
        doc.settings.getCreateModDatesFormat().restoreTemporaryToDefault();
        doc.settings.getOwnerName().def = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_OWNER_NAME);
        doc.settings.getOwnerName().restoreCurrentToDefault();
        doc.settings.getOwnerName().restoreTemporaryToDefault();
        doc.settings.getOwnerEmail().def = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_OWNER_EMAIL);
        doc.settings.getOwnerEmail().restoreCurrentToDefault();
        doc.settings.getOwnerEmail().restoreTemporaryToDefault();
        doc.settings.setDateCreated(PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_DATE_CREATED));
        doc.settings.setDateModified(PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_DATE_MODIFIED));
        if (mode != MODE_REVERT) {
            int width = docInfo.getWidth();
            int height = docInfo.getHeight();
            int left = PropertyContainerUtil.getPropertyAsInt(docInfo, DocumentInfo.KEY_WINDOW_LEFT);
            int top = PropertyContainerUtil.getPropertyAsInt(docInfo, DocumentInfo.KEY_WINDOW_TOP);
            doc.setSize(width, height);
            doc.setLocation(left, top);
        }
    }

    private static int openOrImportFileAndGetTree(JoeTree tree, DocumentInfo docInfo, FileProtocol protocol, int mode) {
        String msg = null;
        int openOrImportResult = FAILURE;
        OpenFileFormat openOrImportFileFormat = null;
        if (!protocol.openFile(docInfo)) {
            return FAILURE;
        }
        switch(mode) {
            case MODE_OPEN:
                openOrImportFileFormat = Outliner.fileFormatManager.getOpenFormat(PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_FILE_FORMAT));
                break;
            case MODE_IMPORT:
                openOrImportFileFormat = Outliner.fileFormatManager.getImportFormat(PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_FILE_FORMAT));
                break;
            default:
                System.out.println("FileMenu:OpenFile: bad mode parameter");
                return FAILURE;
        }
        if (openOrImportFileFormat == null) {
            msg = GUITreeLoader.reg.getText("error_could_not_open_no_file_format");
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH));
            msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_2, PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_FILE_FORMAT));
            JOptionPane.showMessageDialog(Outliner.outliner, msg);
        } else {
            openOrImportResult = openOrImportFileFormat.open(tree, docInfo, docInfo.getInputStream());
            if (openOrImportResult == FAILURE) {
                msg = GUITreeLoader.reg.getText("error_could_not_open_file");
                msg = Replace.replace(msg, GUITreeComponentRegistry.PLACEHOLDER_1, PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH));
                JOptionPane.showMessageDialog(Outliner.outliner, msg);
                RecentFilesList.removeFileNameFromList(docInfo);
            } else if (openOrImportResult != FAILURE_USER_ABORTED) {
                if ((tree.getRootNode() == null) || (tree.getRootNode().numOfChildren() <= 0)) {
                    tree.reset();
                }
            }
        }
        docInfo.setInputStream(null);
        return openOrImportResult;
    }

    private static void setupAndDraw(DocumentInfo docInfo, OutlinerDocument doc, int openOrImportResult) {
        JoeTree tree = doc.tree;
        String filename = PropertyContainerUtil.getPropertyAsString(docInfo, DocumentInfo.KEY_PATH);
        tree.clearSelection();
        tree.getVisibleNodes().clear();
        Node rootNode = tree.getRootNode();
        for (int i = 0, limit = tree.getRootNode().numOfChildren(); i < limit; i++) {
            tree.addNode(rootNode.getChild(i));
        }
        doc.setFileName(filename);
        doc.setModified(false);
        String title;
        switch(doc.getTitleNameForm()) {
            case FULL_PATHNAME:
            default:
                title = filename;
                break;
            case TRUNC_PATHNAME:
                title = StanStringTools.getTruncatedPathName(filename, TRUNC_STRING);
                break;
            case JUST_FILENAME:
                title = StanStringTools.getFileNameFromPathName(filename);
                break;
        }
        doc.setTitle(title);
        Outliner.menuBar.windowMenu.updateWindow(doc);
        IntList expandedNodes = docInfo.getExpandedNodes();
        for (int i = 0, limit = expandedNodes.size(); i < limit; i++) {
            int nodeNum = expandedNodes.get(i);
            try {
                Node node = doc.tree.getVisibleNodes().get(nodeNum);
                node.setExpanded(true);
            } catch (Exception e) {
                break;
            }
        }
        Node firstVisibleNode;
        int index = -1;
        try {
            index = PropertyContainerUtil.getPropertyAsInt(docInfo, DocumentInfo.KEY_VERTICAL_SCROLL_STATE) - 1;
            firstVisibleNode = tree.getVisibleNodes().get(index);
        } catch (IndexOutOfBoundsException e) {
            index = 0;
            firstVisibleNode = tree.getVisibleNodes().get(0);
        }
        tree.setEditingNode(firstVisibleNode);
        tree.setCursorPosition(0);
        tree.setComponentFocus(OutlineLayoutManager.TEXT);
        OutlineLayoutManager layout = doc.panel.layout;
        layout.setNodeToDrawFrom(firstVisibleNode, index);
        layout.redraw();
        if (openOrImportResult == SUCCESS_MODIFIED) {
            doc.setModified(true);
        }
        Outliner.menuBar.windowMenu.changeToWindow(doc);
    }

    private static int promptUser(String msg) {
        Object[] options = { GUITreeLoader.reg.getText("yes"), GUITreeLoader.reg.getText("no") };
        int result = JOptionPane.showOptionDialog(Outliner.outliner, msg, GUITreeLoader.reg.getText("confirm_save"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (result == JOptionPane.NO_OPTION) {
            return USER_ABORTED;
        } else {
            return SUCCESS;
        }
    }
}
