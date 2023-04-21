package com.indigen.victor.actions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import com.indigen.victor.core.HTMLProcessor;
import com.indigen.victor.core.ResourceResolver;
import com.indigen.victor.util.IdGenerator;
import com.indigen.victor.util.XmlUtils;

public class PageRedundancyAction extends ReflectAction {

    static final int SIGN_STRUCTURE = 0;

    static final int SIGN_TEXT = 1;

    static final int SIGN_ATTRVALUE = 2;

    static final int SIGN_NB = 3;

    public void calculateSignatures(Node body) {
        boolean force = Boolean.valueOf(XmlUtils.getStringFromXPath(body, "force")).booleanValue();
        List pages = new Vector();
        Iterator i = getNodesFromXPath("/project/pages/page").iterator();
        while (i.hasNext()) {
            Node pageNode = (Node) i.next();
            boolean calcSign = force;
            if (force == false) {
                if (XmlUtils.getNodeFromXPath(pageNode, "signatures") == null) calcSign = true;
            }
            if (calcSign == true) {
                String pageId = XmlUtils.getStringFromXPath(pageNode, "id");
                pages.add(pageId);
            }
        }
        getProgress().setMax(pages.size());
        i = pages.iterator();
        while (i.hasNext()) {
            String pageId = (String) i.next();
            getProgress().incrementCurrent();
            Node dom = getNodeFromFile(pageId, "/html");
            if (dom != null) {
                signPage(pageId, dom);
            }
        }
    }

    void signPage(String pageId, Node dom) {
        MessageDigest[] mdTab = new MessageDigest[SIGN_NB];
        try {
            mdTab[SIGN_STRUCTURE] = MessageDigest.getInstance("MD5");
            mdTab[SIGN_TEXT] = MessageDigest.getInstance("MD5");
            mdTab[SIGN_ATTRVALUE] = MessageDigest.getInstance("MD5");
            makeSignatures(dom, mdTab);
            StringBuffer sb = new StringBuffer();
            sb.append("<signatures>\n");
            sb.append("<signature type='structure'>");
            sb.append(XmlUtils.getStringSignature(mdTab[SIGN_STRUCTURE]));
            sb.append("</signature>\n");
            sb.append("<signature type='text'>");
            sb.append(XmlUtils.getStringSignature(mdTab[SIGN_TEXT]));
            sb.append("</signature>\n");
            sb.append("<signature type='attrvalue'>");
            sb.append(XmlUtils.getStringSignature(mdTab[SIGN_ATTRVALUE]));
            sb.append("</signature>\n");
            sb.append("</signatures>\n");
            updateReplace("/project/pages/page[@id='" + pageId + "']/signatures", "/project/pages/page[@id='" + pageId + "']", sb.toString());
        } catch (NoSuchAlgorithmException e) {
        }
    }

    void makeSignatures(Node node, MessageDigest[] mdTab) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            mdTab[SIGN_STRUCTURE].update(node.getNodeName().getBytes());
            mdTab[SIGN_ATTRVALUE].update(node.getNodeName().getBytes());
            mdTab[SIGN_TEXT].update(node.getNodeName().getBytes());
            Element elem = (Element) node;
            List attrs = new Vector();
            NamedNodeMap nnm = elem.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                attrs.add(nnm.item(i).getNodeName());
            }
            Collections.sort(attrs);
            Iterator j = attrs.iterator();
            while (j.hasNext()) {
                String attrName = (String) j.next();
                mdTab[SIGN_STRUCTURE].update(attrName.getBytes());
                String attrValue = elem.getAttribute(attrName);
                if (attrValue != null) {
                    mdTab[SIGN_ATTRVALUE].update(attrValue.getBytes());
                }
            }
            node = node.getFirstChild();
            while (node != null) {
                makeSignatures(node, mdTab);
                node = node.getNextSibling();
            }
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            mdTab[SIGN_TEXT].update(node.getNodeValue().getBytes());
        }
    }

    public void checkRedundancy(Node body) {
        setTemplate("redundancy-check");
        List categories = new Vector();
        setFlowData("categories", categories);
        Map identical = new Hashtable();
        identical.put("id", VICTOR_NS + "redundancy-identical");
        identical.put("name", "***identical");
        identical.put("type", "category");
        int identicalIndex = 0;
        List identicalGroups = new Vector();
        identical.put("groups", identicalGroups);
        categories.add(identical);
        Map textDiffers = new Hashtable();
        textDiffers.put("id", VICTOR_NS + "redundancy-text-differs");
        textDiffers.put("name", "***text");
        textDiffers.put("type", "category");
        categories.add(textDiffers);
        int textIndex = 0;
        List textGroups = new Vector();
        textDiffers.put("groups", textGroups);
        Map attrDiffers = new Hashtable();
        attrDiffers.put("id", VICTOR_NS + "redundancy-attr-differs");
        attrDiffers.put("name", "***attr");
        attrDiffers.put("type", "category");
        categories.add(attrDiffers);
        int attrIndex = 0;
        List attrGroups = new Vector();
        attrDiffers.put("groups", attrGroups);
        Map signMap = new Hashtable();
        Iterator i = getNodesFromXPath("/project/pages/page[signatures]").iterator();
        while (i.hasNext()) {
            Node pageNode = (Node) i.next();
            String signStructure = XmlUtils.getStringFromXPath(pageNode, "signatures/signature[@type='structure']");
            putListMap(signMap, signStructure, pageNode);
        }
        i = signMap.keySet().iterator();
        while (i.hasNext()) {
            String sign = (String) i.next();
            List pageNodes = (List) signMap.get(sign);
            if (pageNodes.size() > 1) {
                Map textSign = new Hashtable();
                Map attrSign = new Hashtable();
                Map attrtextSign = new Hashtable();
                Iterator j = pageNodes.iterator();
                while (j.hasNext()) {
                    Node pageNode = (Node) j.next();
                    String signText = XmlUtils.getStringFromXPath(pageNode, "signatures/signature[@type='text']");
                    putListMap(textSign, signText, pageNode);
                    String signAttr = XmlUtils.getStringFromXPath(pageNode, "signatures/signature[@type='attrvalue']");
                    putListMap(attrSign, signAttr, pageNode);
                    String signAttrText = signText + "|" + signAttr;
                    putListMap(attrtextSign, signAttrText, pageNode);
                }
                j = attrtextSign.keySet().iterator();
                while (j.hasNext()) {
                    String sign0 = (String) j.next();
                    List identical0 = (List) attrtextSign.get(sign0);
                    if (identical0.size() > 1) {
                        Map identicalGroup = new Hashtable();
                        identicalGroups.add(identicalGroup);
                        identicalIndex++;
                        identicalGroup.put("name", "" + identicalIndex);
                        identicalGroup.put("type", "group");
                        identicalGroup.put("id", VICTOR_NS + "redundancy-identical-" + identicalIndex);
                        List pageList = new Vector();
                        identicalGroup.put("pages", pageList);
                        Iterator k = identical0.iterator();
                        while (k.hasNext()) {
                            Node pageNode = (Node) k.next();
                            String name = XmlUtils.getStringFromXPath(pageNode, "title");
                            String pageId = XmlUtils.getStringFromXPath(pageNode, "@id");
                            Map page = new Hashtable();
                            pageList.add(page);
                            page.put("id", VICTOR_NS + "page-element-" + IdGenerator.getId());
                            page.put("pageid", pageId);
                            page.put("name", name);
                            page.put("type", "page");
                        }
                    }
                }
                j = textSign.keySet().iterator();
                while (j.hasNext()) {
                    String sign0 = (String) j.next();
                    List text0 = (List) textSign.get(sign0);
                    if (text0.size() > 1) {
                        boolean already = false;
                        Iterator k = attrtextSign.keySet().iterator();
                        while (k.hasNext()) {
                            String key = (String) k.next();
                            List identicalList = (List) attrtextSign.get(key);
                            if (identicalList.containsAll(text0)) {
                                already = true;
                                break;
                            }
                        }
                        if (already == false) {
                            Map textGroup = new Hashtable();
                            attrGroups.add(textGroup);
                            textIndex++;
                            textGroup.put("name", "" + textIndex);
                            textGroup.put("type", "group");
                            textGroup.put("id", VICTOR_NS + "redundancy-text-differs-" + textIndex);
                            List pageList = new Vector();
                            textGroup.put("pages", pageList);
                            k = text0.iterator();
                            while (k.hasNext()) {
                                Node pageNode = (Node) k.next();
                                String name = XmlUtils.getStringFromXPath(pageNode, "title");
                                String pageId = XmlUtils.getStringFromXPath(pageNode, "@id");
                                Map page = new Hashtable();
                                pageList.add(page);
                                page.put("id", VICTOR_NS + "page-element-" + IdGenerator.getId());
                                page.put("pageid", pageId);
                                page.put("name", name);
                                page.put("type", "page");
                            }
                        }
                    }
                }
                j = attrSign.keySet().iterator();
                while (j.hasNext()) {
                    String sign0 = (String) j.next();
                    List attr0 = (List) attrSign.get(sign0);
                    if (attr0.size() > 1) {
                        boolean already = false;
                        Iterator k = attrtextSign.keySet().iterator();
                        while (k.hasNext()) {
                            String key = (String) k.next();
                            List identicalList = (List) attrtextSign.get(key);
                            if (identicalList.containsAll(attr0)) {
                                already = true;
                                break;
                            }
                        }
                        if (already == false) {
                            Map attrGroup = new Hashtable();
                            textGroups.add(attrGroup);
                            attrIndex++;
                            attrGroup.put("name", "" + attrIndex);
                            attrGroup.put("type", "group");
                            attrGroup.put("id", VICTOR_NS + "redundancy-attr-differs-" + attrIndex);
                            List pageList = new Vector();
                            attrGroup.put("pages", pageList);
                            k = attr0.iterator();
                            while (k.hasNext()) {
                                Node pageNode = (Node) k.next();
                                String name = XmlUtils.getStringFromXPath(pageNode, "title");
                                String pageId = XmlUtils.getStringFromXPath(pageNode, "@id");
                                Map page = new Hashtable();
                                pageList.add(page);
                                page.put("id", VICTOR_NS + "page-element-" + IdGenerator.getId());
                                page.put("pageid", pageId);
                                page.put("name", name);
                                page.put("type", "page");
                            }
                        }
                    }
                }
            }
        }
    }

    void putListMap(Map map, Object key, Object value) {
        List list = (List) map.get(key);
        if (list == null) {
            list = new Vector();
            map.put(key, list);
        }
        list.add(value);
    }

    void mergePages(List merges, Map mergeMap) {
        ResourceResolver resolver = new RedundancyResourceResolver(mergeMap);
        List allPages = getNodesFromXPath("/project/pages/page");
        getProgress().setMax(allPages.size());
        Iterator i = allPages.iterator();
        while (i.hasNext()) {
            Node pageNode = (Node) i.next();
            String pageId = (String) XmlUtils.getStringFromXPath(pageNode, "@id");
            Node dom = getNodeFromFile(pageId, "/html");
            if (dom != null) {
                HTMLProcessor processor = new HTMLProcessor(resolver);
                processor.process(dom);
                if (processor.getChangeCount() > 0) {
                    savePageFile(pageId, dom);
                    signPage(pageId, dom);
                }
            }
            getProgress().incrementCurrent();
        }
        Iterator k = merges.iterator();
        while (k.hasNext()) {
            HashSet pages = (HashSet) k.next();
            String masterPage = (String) mergeMap.get(pages.iterator().next());
            i = pages.iterator();
            while (i.hasNext()) {
                String pageId = (String) i.next();
                if (!pageId.equals(masterPage)) {
                    updateRemove("/project/pages/page[@id='" + pageId + "']");
                }
            }
            String homeId = getStringFromXPath("/project/home-id");
            if (pages.contains(homeId) && !masterPage.equals(homeId)) {
                updateReplace("/project/home-id", "/project", "<home-id>" + XmlUtils.xmlEscape(masterPage) + "</home-id>\n");
            }
            i = getNodesFromXPath("/project/sitemaps/sitemap").iterator();
            while (i.hasNext()) {
                Node sitemapNode = (Node) i.next();
                String sitemapId = XmlUtils.getStringFromXPath(sitemapNode, "@id");
                boolean alreadyMasterPage = false;
                if (XmlUtils.getNodeFromXPath(sitemapNode, ".//node[pageid='" + masterPage + "']") != null) alreadyMasterPage = true;
                Iterator j = pages.iterator();
                while (j.hasNext()) {
                    String pageId = (String) j.next();
                    if (pageId != masterPage) {
                        Node nodeNode = XmlUtils.getNodeFromXPath(sitemapNode, ".//node[pageid='" + pageId + "']");
                        String nodeId = XmlUtils.getStringFromXPath(nodeNode, "@id");
                        if (nodeNode != null) {
                            if (alreadyMasterPage) {
                                updateRemove("/project/sitemaps/sitemap[@id='" + sitemapId + "']//node[@id='" + nodeId + "']/pageid[text()='" + pageId + "']");
                            } else {
                                String lang = XmlUtils.getStringFromXPath(nodeNode, "@lang");
                                updateReplace("/project/sitemaps/sitemap[@id='" + sitemapId + "']//node[@id='" + nodeId + "']/pageid[text()='" + pageId + "']", "/project/sitemaps/sitemap[@id='" + sitemapId + "']//node[@id='" + nodeId + "']", "<pageid lang='" + lang + "'>" + XmlUtils.xmlEscape(masterPage) + "</pageid>\n");
                                alreadyMasterPage = true;
                            }
                        }
                    }
                }
            }
            i = pages.iterator();
            while (i.hasNext()) {
                String pageId = (String) i.next();
                if (!pageId.equals(masterPage)) {
                    Iterator j = getStringsFromXPath("/project/instances/instance[inst-value/page-id[text()='" + pageId + "']]/@id").iterator();
                    while (j.hasNext()) {
                        String instanceId = (String) j.next();
                        updateRemove("/project/instances/instance[@id='" + instanceId + "']");
                    }
                }
            }
        }
        int newPageCount = getNodesFromXPath("/project/pages/page").size();
        updateReplace("/project/info/resource-page-count", "/project/info", "<resource-page-count>" + newPageCount + "</resource-page-count>\n");
    }

    public void mergePages(Node body) {
        Map mergeMap = new Hashtable();
        List merges = new Vector();
        Iterator i = XmlUtils.getNodesFromXPath(body, "merge").iterator();
        while (i.hasNext()) {
            Node mergeNode = (Node) i.next();
            List pages = new Vector();
            Iterator j = XmlUtils.getNodesFromXPath(mergeNode, "page").iterator();
            while (j.hasNext()) {
                Node pageNode = (Node) j.next();
                String pageId = XmlUtils.getStringFromXPath(pageNode, "@id");
                if (mergeMap.get(pageId) == null) {
                    pages.add(pageId);
                }
            }
            if (pages.size() > 1) {
                Collections.sort(pages);
                String masterPage = (String) pages.get(0);
                j = pages.iterator();
                while (j.hasNext()) {
                    String pageId = (String) j.next();
                    mergeMap.put(pageId, masterPage);
                }
                HashSet pagesSet = new HashSet(pages);
                merges.add(pagesSet);
            }
        }
        mergePages(merges, mergeMap);
    }

    public void extrapolatePages(Node body) {
        Map pageIdMap = new Hashtable();
        HashSet pagesSet = new HashSet();
        Iterator i = XmlUtils.getNodesFromXPath(body, "page").iterator();
        while (i.hasNext()) {
            Node pageNode = (Node) i.next();
            String pageId = XmlUtils.getStringFromXPath(pageNode, "@id");
            String pageItemId = XmlUtils.getStringFromXPath(pageNode, "text()");
            pageIdMap.put(pageId, pageItemId);
            pagesSet.add(pageId);
        }
        HashSet checkPagesSet = new HashSet();
        i = XmlUtils.getNodesFromXPath(body, "check-page").iterator();
        while (i.hasNext()) {
            Node pageNode = (Node) i.next();
            String pageId = XmlUtils.getStringFromXPath(pageNode, "@id");
            String pageItemId = XmlUtils.getStringFromXPath(pageNode, "text()");
            pageIdMap.put(pageId, pageItemId);
            checkPagesSet.add(pageId);
        }
        getProgress().setMax(pagesSet.size() + checkPagesSet.size());
        HashSet mismatchXPathes = new HashSet();
        List samples = new Vector();
        i = pagesSet.iterator();
        while (i.hasNext()) {
            String pageId = (String) i.next();
            Node dom = getNodeFromFile(pageId, "/html");
            getProgress().incrementCurrent();
            samples.add(dom);
        }
        try {
            buildMismatch(samples, mismatchXPathes);
            Node reference = (Node) samples.get(0);
            i = checkPagesSet.iterator();
            while (i.hasNext()) {
                String pageId = (String) i.next();
                getProgress().incrementCurrent();
                Node dom = getNodeFromFile(pageId, "/html");
                try {
                    if (compareNodes(reference, dom, mismatchXPathes) == true) {
                        pagesSet.add(pageId);
                    }
                } catch (CompareException e) {
                }
            }
            setTemplate("redundancy-extrapolate");
            List pages = new Vector();
            setFlowData("pages", pages);
            i = pagesSet.iterator();
            while (i.hasNext()) {
                String pageId = (String) i.next();
                String pageItemId = (String) pageIdMap.get(pageId);
                pages.add(pageItemId);
            }
        } catch (BuildMismatchException e) {
            getLogger().error("buildMismatch: documents are not comparable", e);
        }
    }

    private boolean compareNodes(Node ref, Node node, HashSet mismatchXPathes) throws CompareException {
        if (ref.getNodeType() != node.getNodeType()) throw new CompareException();
        if (ref.getNodeType() == Node.ELEMENT_NODE) {
            Element refElem = (Element) ref;
            Map refAttributes = new Hashtable();
            NamedNodeMap nnm = refElem.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                Node attrNode = nnm.item(i);
                refAttributes.put(attrNode.getNodeName(), attrNode.getNodeValue());
            }
            Element curElem = (Element) node;
            Map curAttributes = new Hashtable();
            nnm = curElem.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                Node attrNode = nnm.item(i);
                curAttributes.put(attrNode.getNodeName(), attrNode.getNodeValue());
            }
            if (refAttributes.size() != curAttributes.size()) throw new CompareException();
            Iterator i = refAttributes.keySet().iterator();
            while (i.hasNext()) {
                String attrName = (String) i.next();
                String refAttrValue = (String) refAttributes.get(attrName);
                String curAttrValue = (String) curAttributes.get(attrName);
                if (curAttrValue == null) throw new CompareException();
                if (!refAttrValue.equals(curAttrValue)) {
                    String xpath = XmlUtils.generateXPath(node, node.getOwnerDocument().getDocumentElement());
                    xpath += "/@" + attrName;
                    if (!mismatchXPathes.contains(xpath)) return false;
                }
            }
            ref = ref.getFirstChild();
            node = node.getFirstChild();
            if (ref != node && (ref == null || node == null)) throw new CompareException();
            while (ref != null) {
                boolean res = compareNodes(ref, node, mismatchXPathes);
                if (res == false) return false;
                ref = ref.getNextSibling();
                node = node.getNextSibling();
                if (ref != node && (ref == null || node == null)) throw new CompareException();
            }
        } else if (ref.getNodeType() == Node.TEXT_NODE) {
            if (!ref.getNodeValue().equals(node.getNodeValue())) {
                String xpath = XmlUtils.generateXPath(node, node.getOwnerDocument().getDocumentElement());
                if (!mismatchXPathes.contains(xpath)) return false;
            }
        }
        return true;
    }

    void buildMismatch(List nodes, HashSet mismatchXPathes) throws BuildMismatchException {
        int nodeType = -1;
        Iterator i = nodes.iterator();
        while (i.hasNext()) {
            Node node = (Node) i.next();
            int nodeType0 = node.getNodeType();
            if (nodeType == -1) {
                nodeType = nodeType0;
            } else {
                if (nodeType != nodeType0) throw new BuildMismatchException();
            }
        }
        if (nodeType == Node.ELEMENT_NODE) {
            String elementName = null;
            i = nodes.iterator();
            while (i.hasNext()) {
                Node node = (Node) i.next();
                String elementName0 = node.getNodeName();
                if (elementName == null) {
                    elementName = elementName0;
                } else {
                    if (!elementName.equals(elementName0)) throw new BuildMismatchException();
                }
            }
            Map attributes = new Hashtable();
            i = nodes.iterator();
            while (i.hasNext()) {
                Element elem = (Element) i.next();
                NamedNodeMap nnm = elem.getAttributes();
                for (int j = 0; j < nnm.getLength(); j++) {
                    Node attrNode = nnm.item(j);
                    String attrName = attrNode.getNodeName();
                    String attrValue = attrNode.getNodeValue();
                    putListMap(attributes, attrName, attrValue);
                }
            }
            i = attributes.keySet().iterator();
            while (i.hasNext()) {
                String attrName = (String) i.next();
                List attrValues = (List) attributes.get(attrName);
                if (attrValues.size() != nodes.size()) throw new BuildMismatchException();
                boolean sameAttr = true;
                String attrValue = null;
                Iterator j = attrValues.iterator();
                while (j.hasNext()) {
                    String attrValue0 = (String) j.next();
                    if (attrValue == null) {
                        attrValue = attrValue0;
                    } else {
                        if (!attrValue.equals(attrValue0)) {
                            sameAttr = false;
                            break;
                        }
                    }
                }
                if (sameAttr == false) {
                    Node node = (Node) nodes.get(0);
                    String xpath = XmlUtils.generateXPath(node, node.getOwnerDocument().getDocumentElement());
                    mismatchXPathes.add(xpath + "/@" + attrName);
                }
            }
            List nodes0 = new Vector();
            i = nodes.iterator();
            while (i.hasNext()) {
                Node node = (Node) i.next();
                Node childNode = node.getFirstChild();
                if (childNode != null) nodes0.add(childNode);
            }
            while (nodes0.size() > 0) {
                if (nodes.size() != nodes0.size()) throw new BuildMismatchException();
                buildMismatch(nodes0, mismatchXPathes);
                List nodes1 = new Vector();
                i = nodes0.iterator();
                while (i.hasNext()) {
                    Node node = (Node) i.next();
                    Node nextNode = node.getNextSibling();
                    if (nextNode != null) nodes1.add(nextNode);
                }
                nodes0 = nodes1;
            }
        } else if (nodeType == Node.TEXT_NODE) {
            String nodeValue = null;
            boolean sameNodeValue = true;
            i = nodes.iterator();
            while (i.hasNext()) {
                Node node = (Node) i.next();
                String nodeValue0 = node.getNodeValue();
                if (nodeValue == null) {
                    nodeValue = nodeValue0;
                } else {
                    if (!nodeValue.equals(nodeValue0)) {
                        sameNodeValue = false;
                        break;
                    }
                }
            }
            if (sameNodeValue == false) {
                Node node = (Node) nodes.get(0);
                String xpath = XmlUtils.generateXPath(node, node.getOwnerDocument().getDocumentElement());
                mismatchXPathes.add(xpath);
            }
        }
    }

    public void comparePages(Node body) {
        setFlowData("oid", getOid());
        List pages = new Vector();
        setFlowData("pages", pages);
        Iterator i = XmlUtils.getNodesFromXPath(body, "page").iterator();
        while (i.hasNext()) {
            Node pageNode = (Node) i.next();
            String pageId = XmlUtils.getStringFromXPath(pageNode, "@id");
            Map page = new Hashtable();
            pages.add(page);
            page.put("id", pageId);
            String title = getStringFromXPath("/project/pages/page[@id='" + pageId + "']/title");
            if (title != null) page.put("title", title);
        }
        setTemplateOutput("html");
        setTemplate("compare-pages");
    }

    class RedundancyResourceResolver implements ResourceResolver {

        Map mergeMap;

        RedundancyResourceResolver(Map mergeMap) {
            this.mergeMap = mergeMap;
        }

        public String resolve(String url, String forcedMimeType) {
            String masterPage = (String) mergeMap.get(url);
            if (masterPage != null) return masterPage; else return url;
        }
    }

    class BuildMismatchException extends Exception {
    }

    class CompareException extends Exception {
    }
}
