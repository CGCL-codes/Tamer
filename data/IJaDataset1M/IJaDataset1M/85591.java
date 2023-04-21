package com.indigen.victor.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.indigen.victor.actions.VictorAction;
import com.indigen.victor.util.XmlUtils;

/**
 * Helper class to discover new sitemap nodes based on a {@link com.indigen.victor.core.Rule}
 */
public class SitemapZoneBuilder extends SitemapBuilder {

    /**
	 * The rule to discover links
	 */
    Rule rule;

    /**
	 * Store for the already processed pages
	 */
    HashSet processedPages = new HashSet();

    /**
	 * Object constructor
	 * @param action
	 * @param lang
	 * @param ruleNode
	 */
    public SitemapZoneBuilder(VictorAction action, String lang, Node ruleNode) {
        super(action, lang);
        rule = Rule.getRule(action, ruleNode, null, null, false);
    }

    /**
	 * Start the discovery
	 */
    void process() {
        while (queuedNodes.size() > 0) {
            String nodeId = (String) queuedNodes.remove(0);
            action.getProgress().incrementCurrent();
            Node nodeNode = action.getNodeFromXPath("/project/sitemaps/sitemap[id='" + sitemapId + "']//node[@id='" + nodeId + "']");
            String pageId = getAttachedPageId(nodeNode, lang);
            if (pageId != null && pageId.length() > 0) {
                pageId = resolvePageId(pageId);
                if (!processedPages.contains(pageId)) {
                    processedPages.add(pageId);
                    Node dom = action.getNodeFromFile(pageId, "/html");
                    List segments = rule.apply(dom, pageId);
                    Iterator i = segments.iterator();
                    while (i.hasNext()) {
                        Segment segment = (Segment) i.next();
                        Node node = segment.getContainerElement(dom);
                        processNodeForLinks(node, nodeId);
                    }
                }
            }
            List childNodes = action.getNodesFromXPath(nodeNode, "nodes//node");
            Iterator i = childNodes.iterator();
            while (i.hasNext()) {
                Node childNode = (Node) i.next();
                String childNodeId = ((Element) childNode).getAttribute("id");
                action.getProgress().incrementMax();
                queuedNodes.add(childNodeId);
            }
        }
    }

    /**
	 * Process an HTML DOM node to discover links
	 * @param node
	 * @param nodeId
	 */
    void processNodeForLinks(Node node, String nodeId) {
        Node node0 = node;
        while (node0 != null) {
            processLinkElement(node0, nodeId);
            node0 = node0.getParentNode();
        }
        List aNodes = XmlUtils.getNodesFromXPath(node, ".//a[@href]");
        Iterator i = aNodes.iterator();
        while (i.hasNext()) {
            node0 = (Node) i.next();
            processLinkElement(node0, nodeId);
        }
    }

    /**
	 * Process an HTML <code>A</code> tag
	 * @param node
	 * @param nodeId
	 */
    void processLinkElement(Node node, String nodeId) {
        if (node.getNodeName().equals("a")) {
            Element elem = (Element) node;
            String href = elem.getAttribute("href");
            if (href != null && href.length() > 0) {
                processLink(href, nodeId);
            }
        }
    }

    /**
	 * Process a discovered link
	 * @param href
	 * @param nodeId
	 */
    void processLink(String href, String nodeId) {
        String pageId = resolvePageId(href);
        if (pageId != null && isUnassignedPage(sitemapId, pageId)) {
            Node pageNode = action.getNodeFromXPath("/project/pages/page[id='" + pageId + "']");
            if (pageNode != null) {
                String newNodeId = createNode(nodeId);
                assignPage(newNodeId, pageId);
                action.getProgress().incrementMax();
                queuedNodes.add(newNodeId);
            }
        }
    }
}
