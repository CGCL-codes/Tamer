package xbrowser.cache.io;

import java.net.*;
import java.util.*;
import org.w3c.dom.*;
import xbrowser.XProjectConstants;
import xbrowser.XRepository;
import xbrowser.cache.XCacheItem;
import xbrowser.cache.XCacheManager;
import xbrowser.util.XMLManager;

public class XCacheIndexDefaultSerializer implements XCacheIndexSerializer {

    public XCacheIndexDefaultSerializer() {
    }

    public void importCacheIndexes(String file_name, XCacheManager cache_manager) throws Exception {
        URL dtd_url = XRepository.getResourceManager().getResourceURL(DTD_URL);
        Document doc = XMLManager.readFileDocument(file_name, DTD_SYMBOL, dtd_url);
        Node node = XMLManager.findNode(doc, "xcache").getNextSibling().getFirstChild();
        while (node != null) {
            if (node instanceof Element) {
                if (node.getNodeName().equals("item")) loadCacheItem(node, cache_manager);
            }
            node = node.getNextSibling();
        }
    }

    private void loadCacheItem(Node node, XCacheManager cache_manager) throws DOMException {
        XCacheItem cache_item = new XCacheItem();
        node = node.getFirstChild();
        while (node != null) {
            if (node instanceof Element) {
                if (node.getNodeName().equals("url")) cache_item.setURL(XMLManager.getNodeValue(node)); else if (node.getNodeName().equals("file")) cache_item.setFileName(XMLManager.getNodeValue(node)); else if (node.getNodeName().equals("content-type")) cache_item.setContentType(XMLManager.getNodeValue(node));
            }
            node = node.getNextSibling();
        }
        cache_manager.addCacheItem(cache_item);
    }

    public void exportCacheIndexes(String file_name, XCacheManager cache_manager) throws Exception {
        Document doc = XMLManager.newDocument();
        Element root = doc.createElement("xcache");
        doc.appendChild(root);
        Iterator it = cache_manager.getCacheItems();
        while (it.hasNext()) saveCacheItem((XCacheItem) it.next(), doc, root);
        root.normalize();
        XMLManager.writeDocument(file_name, doc, null, DTD_SYMBOL, null);
    }

    private void saveCacheItem(XCacheItem cache_item, Document doc, Element parent_node) {
        Element node = doc.createElement("item");
        XMLManager.addDataNodeTo(doc, node, "url", cache_item.getURL());
        XMLManager.addDataNodeTo(doc, node, "file", cache_item.getFileName());
        XMLManager.addDataNodeTo(doc, node, "content-type", cache_item.getContentType());
        parent_node.appendChild(node);
    }

    private final String DTD_SYMBOL = "xbrowser:default:cache";

    private final String DTD_URL = XProjectConstants.DTD_DIR + "XCache.dtd";
}
