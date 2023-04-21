package frost.fileTransfer.download;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.w3c.dom.*;
import frost.*;
import frost.gui.objects.Board;
import frost.storage.StorageException;

/**
 * @author $Author: bback $
 * @version $Revision: 2004 $
 */
public class DownloadModelXmlDAO implements DownloadModelDAO {

    private static Logger logger = Logger.getLogger(DownloadModelXmlDAO.class.getName());

    private static final String XML_FILENAME = "downloads.xml";

    private static final String TMP_FILENAME = "downloads.xml.tmp";

    private static final String BAK_FILENAME = "downloads.xml.bak";

    private String directory;

    /**
	 * @param settings
	 */
    public DownloadModelXmlDAO(SettingsClass settings) {
        directory = settings.getValue("config.dir");
    }

    public void create() throws StorageException {
        File xmlFile = new File(directory + XML_FILENAME);
        try {
            boolean success = xmlFile.createNewFile();
            if (!success) {
                throw new StorageException("There was a problem while creating the storage.");
            }
        } catch (IOException ioe) {
            throw new StorageException("There was a problem while creating the storage.", ioe);
        }
    }

    /**
	 * @param element
	 * @return
	 */
    private FrostDownloadItem createDownloadItem(Element element) {
        String filename = XMLTools.getChildElementsCDATAValue(element, "filename");
        String filesize = XMLTools.getChildElementsTextValue(element, "filesize");
        String fileage = XMLTools.getChildElementsTextValue(element, "fileage");
        String key = XMLTools.getChildElementsCDATAValue(element, "key");
        String retries = XMLTools.getChildElementsTextValue(element, "retries");
        String state = XMLTools.getChildElementsTextValue(element, "state");
        String owner = XMLTools.getChildElementsTextValue(element, "owner");
        String sourceboardname = XMLTools.getChildElementsTextValue(element, "sourceboard");
        String enableDownload = element.getAttribute("enableDownload");
        String SHA1 = XMLTools.getChildElementsTextValue(element, "SHA1");
        String batch = XMLTools.getChildElementsTextValue(element, "batch");
        if (filename == null || state == null || (key == null && SHA1 == null)) {
            logger.warning("Invalid download item found. Removed.");
            return null;
        }
        int iState = -1;
        try {
            iState = Integer.parseInt(state);
        } catch (NumberFormatException ex) {
            iState = -1;
        }
        if (iState != FrostDownloadItem.STATE_DONE) {
            iState = FrostDownloadItem.STATE_WAITING;
        }
        boolean isDownloadEnabled = false;
        if (enableDownload == null || enableDownload.length() == 0 || enableDownload.toLowerCase().equals("true")) {
            isDownloadEnabled = true;
        }
        Board board = null;
        if (sourceboardname != null) {
            board = MainFrame.getInstance().getTofTreeModel().getBoardByName(sourceboardname);
            if (board == null) {
                logger.warning("Download item found (" + filename + ") whose source board (" + sourceboardname + ") does not exist. Removed.");
                return null;
            }
        }
        FrostDownloadItem dlItem = new FrostDownloadItem(filename, filesize, fileage, key, retries, owner, SHA1, iState, isDownloadEnabled, board);
        dlItem.setBatch(batch);
        return dlItem;
    }

    /**
	 * @param downloadItem
	 * @param doc
	 * @return
	 */
    private Element createElement(FrostDownloadItem downloadItem, Document doc) {
        Element itemElement = doc.createElement("FrostDownloadTableItem");
        String isDownloadEnabled;
        if (downloadItem.getEnableDownload() == null) {
            isDownloadEnabled = "true";
        } else {
            isDownloadEnabled = downloadItem.getEnableDownload().toString();
        }
        itemElement.setAttribute("enableDownload", isDownloadEnabled);
        Element element;
        Text text;
        CDATASection cdata;
        element = doc.createElement("filename");
        cdata = doc.createCDATASection(downloadItem.getFileName());
        element.appendChild(cdata);
        itemElement.appendChild(element);
        if (downloadItem.getFileSize() != null) {
            element = doc.createElement("filesize");
            text = doc.createTextNode(downloadItem.getFileSize().toString());
            element.appendChild(text);
            itemElement.appendChild(element);
        }
        element = doc.createElement("fileage");
        text = doc.createTextNode(downloadItem.getFileAge());
        element.appendChild(text);
        itemElement.appendChild(element);
        element = doc.createElement("key");
        cdata = doc.createCDATASection(downloadItem.getKey());
        element.appendChild(cdata);
        itemElement.appendChild(element);
        element = doc.createElement("retries");
        text = doc.createTextNode(String.valueOf(downloadItem.getRetries()));
        element.appendChild(text);
        itemElement.appendChild(element);
        element = doc.createElement("state");
        text = doc.createTextNode(String.valueOf(downloadItem.getState()));
        element.appendChild(text);
        itemElement.appendChild(element);
        element = doc.createElement("SHA1");
        text = doc.createTextNode(downloadItem.getSHA1());
        element.appendChild(text);
        itemElement.appendChild(element);
        if (downloadItem.getBatch() != null) {
            element = doc.createElement("batch");
            text = doc.createTextNode(downloadItem.getBatch());
            element.appendChild(text);
            itemElement.appendChild(element);
        }
        if (downloadItem.getOwner() != null && downloadItem.getOwner().compareToIgnoreCase("anonymous") != 0) {
            element = doc.createElement("owner");
            text = doc.createTextNode(String.valueOf(downloadItem.getOwner()));
            element.appendChild(text);
            itemElement.appendChild(element);
        }
        if (downloadItem.getSourceBoard() != null) {
            element = doc.createElement("sourceboard");
            text = doc.createTextNode(downloadItem.getSourceBoard().getName());
            element.appendChild(text);
            itemElement.appendChild(element);
        }
        return itemElement;
    }

    public boolean exists() {
        File xmlFile = new File(directory + XML_FILENAME);
        if (xmlFile.length() == 0) {
            xmlFile.delete();
        }
        if (xmlFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void load(DownloadModel downloadModel) throws StorageException {
        Document doc = null;
        try {
            doc = XMLTools.parseXmlFile(directory + XML_FILENAME, false);
        } catch (Exception ex) {
            throw new StorageException("Exception while parsing the downloads model XML file.");
        }
        if (doc == null) {
            throw new StorageException("Could not parse the downloads model XML file.");
        }
        Element rootNode = doc.getDocumentElement();
        if (rootNode.getTagName().equals("FrostDownloadTable") == false) {
            throw new StorageException("The downloads model XML file is invalid: does not contain the root tag FrostDownloadTable.");
        }
        ArrayList nodelist = XMLTools.getChildElementsByTagName(rootNode, "FrostDownloadTableItemList");
        if (nodelist.size() != 1) {
            throw new StorageException("The downloads model XML file is invalid: FrostDownloadTableItemList not found or duplicated.");
        }
        Element itemListRootNode = (Element) nodelist.get(0);
        nodelist = XMLTools.getChildElementsByTagName(itemListRootNode, "FrostDownloadTableItem");
        if (nodelist.size() == 0) {
            logger.info("The downloads model XML file has no items.");
        } else {
            for (int x = 0; x < nodelist.size(); x++) {
                Element element = (Element) nodelist.get(x);
                FrostDownloadItem downloadItem = createDownloadItem(element);
                if (downloadItem != null) {
                    downloadModel.addDownloadItem(downloadItem);
                }
            }
            logger.info("Loaded " + nodelist.size() + " items into the downloads model.");
        }
    }

    public void save(DownloadModel downloadModel) throws StorageException {
        Document doc = XMLTools.createDomDocument();
        if (doc == null) {
            throw new StorageException("Could not create the XML document.");
        }
        Element rootElement = doc.createElement("FrostDownloadTable");
        doc.appendChild(rootElement);
        Element itemsRoot = doc.createElement("FrostDownloadTableItemList");
        rootElement.appendChild(itemsRoot);
        for (int x = 0; x < downloadModel.getItemCount(); x++) {
            FrostDownloadItem downloadItem = (FrostDownloadItem) downloadModel.getItemAt(x);
            Element element = createElement(downloadItem, doc);
            itemsRoot.appendChild(element);
        }
        File downloadsFile = new File(directory + XML_FILENAME);
        if (downloadsFile.exists()) {
            File bakFile = new File(directory + BAK_FILENAME);
            bakFile.delete();
            if (FileAccess.copyFile(directory + XML_FILENAME, directory + BAK_FILENAME) == false) {
                logger.log(Level.SEVERE, "Error while copying " + XML_FILENAME + " to " + BAK_FILENAME);
            }
        }
        File downloadsTmpFile = new File(directory + TMP_FILENAME);
        if (downloadsTmpFile.exists()) {
            downloadsTmpFile.delete();
        }
        if (XMLTools.writeXmlFile(doc, directory + TMP_FILENAME)) {
            if (downloadsTmpFile.exists()) {
                downloadsFile.delete();
                if (!downloadsTmpFile.renameTo(downloadsFile)) {
                    if (!FileAccess.copyFile(directory + BAK_FILENAME, directory + XML_FILENAME)) {
                        throw new StorageException("Error while restoring " + XML_FILENAME);
                    }
                }
            } else {
                throw new StorageException("Could not save " + XML_FILENAME);
            }
        } else {
            throw new StorageException("Could not save " + XML_FILENAME);
        }
    }
}
