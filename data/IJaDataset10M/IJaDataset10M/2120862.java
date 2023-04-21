package com.android.sdklib.internal.repository;

import com.android.sdklib.annotations.Nullable;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;
import com.android.sdklib.repository.RepoConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * An sdk-repository source, i.e. a download site.
 * A repository describes one or more {@link Package}s available for download.
 */
public class SdkRepoSource extends SdkSource {

    /**
     * Constructs a new source for the given repository URL.
     * @param url The source URL. Cannot be null. If the URL ends with a /, the default
     *            repository.xml filename will be appended automatically.
     * @param uiName The UI-visible name of the source. Can be null.
     */
    public SdkRepoSource(String url, String uiName) {
        super(url, uiName);
    }

    /**
     * Returns true if this is an addon source.
     * We only load addons and extras from these sources.
     */
    @Override
    public boolean isAddonSource() {
        return false;
    }

    @Override
    protected String getUrlDefaultXmlFile() {
        return SdkRepoConstants.URL_DEFAULT_FILENAME;
    }

    @Override
    protected int getNsLatestVersion() {
        return SdkRepoConstants.NS_LATEST_VERSION;
    }

    @Override
    protected String getNsUri() {
        return SdkRepoConstants.NS_URI;
    }

    @Override
    protected String getNsPattern() {
        return SdkRepoConstants.NS_PATTERN;
    }

    @Override
    protected String getSchemaUri(int version) {
        return SdkRepoConstants.getSchemaUri(version);
    }

    @Override
    protected String getRootElementName() {
        return SdkRepoConstants.NODE_SDK_REPOSITORY;
    }

    @Override
    protected InputStream getXsdStream(int version) {
        return SdkRepoConstants.getXsdStream(version);
    }

    /**
     * The purpose of this method is to support forward evolution of our schema.
     * <p/>
     * At this point, we know that xml does not point to any schema that this version of
     * the tool knows how to process, so it's not one of the possible 1..N versions of our
     * XSD schema.
     * <p/>
     * We thus try to interpret the byte stream as a possible XML stream. It may not be
     * one at all in the first place. If it looks anything line an XML schema, we try to
     * find its &lt;tool&gt; and the &lt;platform-tools&gt; elements. If we find any,
     * we recreate a suitable document that conforms to what we expect from our XSD schema
     * with only those elements.
     * <p/>
     * To be valid, the &lt;tool&gt; and the &lt;platform-tools&gt; elements must have at
     * least one &lt;archive&gt; compatible with this platform.
     * <p/>
     * Starting the sdk-repository schema v3, &lt;tools&gt; has a &lt;min-platform-tools-rev&gt;
     * node, so technically the corresponding XML schema will be usable only if there's a
     * &lt;platform-tools&gt; with the request revision number. We don't enforce that here, as
     * this is done at install time.
     * <p/>
     * If we don't find anything suitable, we drop the whole thing.
     *
     * @param xml The input XML stream. Can be null.
     * @return Either a new XML document conforming to our schema with at least one &lt;tool&gt;
     *         and &lt;platform-tools&gt; element or null.
     * @throws IOException if InputStream.reset() fails
     * @null Can return null on failure.
     */
    @Override
    protected Document findAlternateToolsXml(@Nullable InputStream xml) throws IOException {
        if (xml == null) {
            return null;
        }
        xml.reset();
        Document oldDoc = null;
        Document newDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            oldDoc = builder.parse(xml);
            factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
            newDoc = builder.newDocument();
        } catch (Exception e) {
        }
        if (oldDoc == null || newDoc == null) {
            return null;
        }
        Pattern nsPattern = Pattern.compile(getNsPattern());
        Node oldRoot = null;
        String prefix = null;
        for (Node child = oldDoc.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                prefix = null;
                String name = child.getNodeName();
                int pos = name.indexOf(':');
                if (pos > 0 && pos < name.length() - 1) {
                    prefix = name.substring(0, pos);
                    name = name.substring(pos + 1);
                }
                if (SdkRepoConstants.NODE_SDK_REPOSITORY.equals(name)) {
                    NamedNodeMap attrs = child.getAttributes();
                    String xmlns = "xmlns";
                    if (prefix != null) {
                        xmlns += ":" + prefix;
                    }
                    Node attr = attrs.getNamedItem(xmlns);
                    if (attr != null) {
                        String uri = attr.getNodeValue();
                        if (uri != null && nsPattern.matcher(uri).matches()) {
                            oldRoot = child;
                            break;
                        }
                    }
                }
            }
        }
        if (oldRoot == null || prefix == null || prefix.length() == 0) {
            return null;
        }
        final String ns = getNsUri();
        Element newRoot = newDoc.createElementNS(ns, getRootElementName());
        newRoot.setPrefix(prefix);
        newDoc.appendChild(newRoot);
        int numTool = 0;
        String[] elementNames = { SdkRepoConstants.NODE_TOOL, SdkRepoConstants.NODE_PLATFORM_TOOL, SdkRepoConstants.NODE_LICENSE };
        Element element = null;
        while ((element = findChild(oldRoot, element, prefix, elementNames)) != null) {
            boolean isElementValid = false;
            String name = element.getLocalName();
            if (name == null) {
                name = element.getNodeName();
                int pos = name.indexOf(':');
                if (pos > 0 && pos < name.length() - 1) {
                    name = name.substring(pos + 1);
                }
            }
            if (SdkRepoConstants.NODE_LICENSE.equals(name)) {
                isElementValid = true;
            } else {
                try {
                    Node revision = findChild(element, null, prefix, RepoConstants.NODE_REVISION);
                    Node archives = findChild(element, null, prefix, RepoConstants.NODE_ARCHIVES);
                    if (revision == null || archives == null) {
                        continue;
                    }
                    try {
                        String content = revision.getTextContent();
                        content = content.trim();
                        int rev = Integer.parseInt(content);
                        if (rev < 1) {
                            continue;
                        }
                    } catch (NumberFormatException ignore) {
                        continue;
                    }
                    if (SdkRepoConstants.NODE_TOOL.equals(name)) {
                        Node minPTRev = findChild(element, null, prefix, RepoConstants.NODE_MIN_PLATFORM_TOOLS_REV);
                        if (minPTRev == null) {
                            continue;
                        }
                        try {
                            String content = minPTRev.getTextContent();
                            content = content.trim();
                            int rev = Integer.parseInt(content);
                            if (rev < 1) {
                                continue;
                            }
                        } catch (NumberFormatException ignore) {
                            continue;
                        }
                    }
                    Node archive = null;
                    while ((archive = findChild(archives, archive, prefix, RepoConstants.NODE_ARCHIVE)) != null) {
                        try {
                            Os os = (Os) XmlParserUtils.getEnumAttribute(archive, RepoConstants.ATTR_OS, Os.values(), null);
                            Arch arch = (Arch) XmlParserUtils.getEnumAttribute(archive, RepoConstants.ATTR_ARCH, Arch.values(), Arch.ANY);
                            if (os == null || !os.isCompatible() || arch == null || !arch.isCompatible()) {
                                continue;
                            }
                            Node node = findChild(archive, null, prefix, RepoConstants.NODE_URL);
                            String url = node == null ? null : node.getTextContent().trim();
                            if (url == null || url.length() == 0) {
                                continue;
                            }
                            node = findChild(archive, null, prefix, RepoConstants.NODE_SIZE);
                            long size = 0;
                            try {
                                size = Long.parseLong(node.getTextContent());
                            } catch (Exception e) {
                            }
                            if (size < 1) {
                                continue;
                            }
                            node = findChild(archive, null, prefix, RepoConstants.NODE_CHECKSUM);
                            if (node == null) {
                                continue;
                            }
                            NamedNodeMap attrs = node.getAttributes();
                            Node typeNode = attrs.getNamedItem(RepoConstants.ATTR_TYPE);
                            if (typeNode == null || !RepoConstants.ATTR_TYPE.equals(typeNode.getNodeName()) || !RepoConstants.SHA1_TYPE.equals(typeNode.getNodeValue())) {
                                continue;
                            }
                            String sha1 = node == null ? null : node.getTextContent().trim();
                            if (sha1 == null || sha1.length() != RepoConstants.SHA1_CHECKSUM_LEN) {
                                continue;
                            }
                            isElementValid = true;
                        } catch (Exception ignore1) {
                        }
                    }
                } catch (Exception ignore2) {
                    if (System.getenv("TESTING") != null) {
                        throw new RuntimeException(ignore2);
                    }
                }
            }
            if (isElementValid) {
                duplicateNode(newRoot, element, SdkRepoConstants.NS_URI, prefix);
                numTool++;
            }
        }
        return numTool > 0 ? newDoc : null;
    }

    /**
     * Helper method used by {@link #findAlternateToolsXml(InputStream)} to find a given
     * element child in a root XML node.
     */
    private Element findChild(Node rootNode, Node after, String prefix, String[] nodeNames) {
        for (int i = 0; i < nodeNames.length; i++) {
            if (nodeNames[i].indexOf(':') < 0) {
                nodeNames[i] = prefix + ":" + nodeNames[i];
            }
        }
        Node child = after == null ? rootNode.getFirstChild() : after.getNextSibling();
        for (; child != null; child = child.getNextSibling()) {
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            for (String nodeName : nodeNames) {
                if (nodeName.equals(child.getNodeName())) {
                    return (Element) child;
                }
            }
        }
        return null;
    }

    /**
     * Helper method used by {@link #findAlternateToolsXml(InputStream)} to find a given
     * element child in a root XML node.
     */
    private Node findChild(Node rootNode, Node after, String prefix, String nodeName) {
        return findChild(rootNode, after, prefix, new String[] { nodeName });
    }

    /**
     * Helper method used by {@link #findAlternateToolsXml(InputStream)} to duplicate a node
     * and attach it to the given root in the new document.
     */
    private Element duplicateNode(Element newRootNode, Element oldNode, String namespaceUri, String prefix) {
        Document newDoc = newRootNode.getOwnerDocument();
        Element newNode = null;
        String nodeName = oldNode.getNodeName();
        int pos = nodeName.indexOf(':');
        if (pos > 0 && pos < nodeName.length() - 1) {
            nodeName = nodeName.substring(pos + 1);
            newNode = newDoc.createElementNS(namespaceUri, nodeName);
            newNode.setPrefix(prefix);
        } else {
            newNode = newDoc.createElement(nodeName);
        }
        newRootNode.appendChild(newNode);
        NamedNodeMap attrs = oldNode.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);
            Attr newAttr = null;
            String attrName = oldNode.getNodeName();
            pos = attrName.indexOf(':');
            if (pos > 0 && pos < attrName.length() - 1) {
                attrName = attrName.substring(pos + 1);
                newAttr = newDoc.createAttributeNS(namespaceUri, attrName);
                newAttr.setPrefix(prefix);
            } else {
                newAttr = newDoc.createAttribute(attrName);
            }
            newAttr.setNodeValue(attr.getNodeValue());
            if (pos > 0) {
                newNode.getAttributes().setNamedItemNS(newAttr);
            } else {
                newNode.getAttributes().setNamedItem(newAttr);
            }
        }
        for (Node child = oldNode.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                duplicateNode(newNode, (Element) child, namespaceUri, prefix);
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                Text newText = newDoc.createTextNode(child.getNodeValue());
                newNode.appendChild(newText);
            }
        }
        return newNode;
    }
}
