package com.bayareasoftware.chartengine.ds.util;

import static com.bayareasoftware.chartengine.ds.util.HtmlTag.getTag;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import com.bayareasoftware.chartengine.model.DataSourceInfo;
import com.bayareasoftware.chartengine.model.StringUtil;
import com.bayareasoftware.chartengine.model.TableSynopsis;
import com.bayareasoftware.chartengine.util.URLUtil;

public final class HtmlUtil {

    /**
     * Find the first node of a given type
     * 
     * @param element
     * @param tag
     * @return The first node of the specified type, or null if none found
     */
    public static Node getChild(Node element, HtmlTag tag) {
        for (Node node : getChildren(element)) {
            if (getTag(node) == tag) return node;
        }
        return null;
    }

    public static List<Node> getNodesByTag(Node n, HtmlTag tag, List<Node> ret) {
        if (ret == null) {
            ret = new ArrayList<Node>();
        }
        if (getTag(n) == tag) {
            ret.add(n);
        }
        NodeList nl = n.getChildNodes();
        for (int i = 0; nl != null && i < nl.getLength(); i++) {
            Node c = nl.item(i);
            getNodesByTag(c, tag, ret);
        }
        return ret;
    }

    /**
     * Non-breaking space is char 160, replace with ' '
     * http://htmlhelp.com/reference/charset/iso160-191.html
     */
    private static final char NBSP_CHAR = 160;

    public static final int MAX_STRING_LENGTH = 50;

    public static String getText(Node elementNode) {
        return getText(elementNode, MAX_STRING_LENGTH);
    }

    public static String getText(Node elementNode, int maxLen) {
        assert elementNode.getNodeType() == Node.ELEMENT_NODE;
        StringBuilder sb = new StringBuilder();
        myloop: for (Node node : getChildren(elementNode)) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                sb.append(node.getNodeValue());
                continue;
            }
            switch(getTag(node)) {
                case Q:
                    appendText(sb.append('"'), node).append('"');
                    break;
                case DEL:
                case STRIKE:
                    continue;
                case TT:
                case I:
                case B:
                case BIG:
                case SMALL:
                case CENTER:
                case EM:
                case STRONG:
                case DFN:
                case CODE:
                case SAMP:
                case KBD:
                case VAR:
                case CITE:
                case INS:
                case P:
                case U:
                case UL:
                case S:
                case SUB:
                case SUP:
                case FONT:
                case A:
                case SPAN:
                case ABBR:
                    appendText(sb, node);
                    break;
                case BR:
                    sb.append('\n');
                    break;
                default:
                    break;
            }
            if (sb.length() >= maxLen) {
                break myloop;
            }
        }
        String ret = sb.toString();
        ret = ret.replace(NBSP_CHAR, ' ');
        ret = StringUtil.trim(ret);
        if (ret != null && ret.length() > maxLen) {
            ret = ellipsis(ret, maxLen);
        }
        return ret;
    }

    private static String ellipsis(String s, int maxLen) {
        int len = s.length();
        if (len > maxLen) {
            s = s.substring(0, maxLen) + "...";
        }
        return s;
    }

    private static StringBuilder appendText(StringBuilder sb, Node elementNode) {
        for (Node node : getChildren(elementNode)) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                sb.append(node.getNodeValue());
            } else {
                switch(getTag(node)) {
                    case Q:
                        appendText(sb.append('"'), node).append('"');
                        break;
                    case DEL:
                    case STRIKE:
                        continue;
                    case TT:
                    case I:
                    case B:
                    case BIG:
                    case SMALL:
                    case CENTER:
                    case EM:
                    case STRONG:
                    case DFN:
                    case CODE:
                    case SAMP:
                    case KBD:
                    case VAR:
                    case CITE:
                    case INS:
                    case P:
                    case U:
                    case UL:
                    case S:
                    case SUB:
                    case SUP:
                    case FONT:
                    case A:
                    case SPAN:
                        appendText(sb, node);
                        break;
                    case BR:
                        sb.append('\n');
                        break;
                    default:
                        break;
                }
                if (sb.length() >= MAX_STRING_LENGTH) {
                    break;
                }
            }
        }
        return sb;
    }

    public static Document getHtmlDocument(InputStream is) throws IOException {
        Tidy tidy = new Tidy();
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        try {
            return tidy.parseDOM(is, null);
        } finally {
            is.close();
        }
    }

    public static final int SYNOPSIS_ROWS = 10;

    public static List<TableSynopsis> tables2synopses(List<HtmlTable> tables) {
        List<TableSynopsis> ret = new ArrayList<TableSynopsis>();
        int count = 0;
        for (HtmlTable t : tables) {
            if (t.getTotalRows() < 2) {
                count++;
                continue;
            }
            List<String[]> data = t.getData();
            int maxCols = 0;
            boolean allNulls = true;
            for (String[] row : data) {
                if (row.length > maxCols) maxCols = row.length;
                for (int i = 0; i < row.length; i++) {
                    if (row[i] != null && !(row[i].trim().equals(""))) {
                        allNulls = false;
                        break;
                    }
                }
            }
            if (allNulls || maxCols == 1) {
                count++;
                continue;
            }
            TableSynopsis s = new TableSynopsis();
            String tid = t.getTagId();
            s.setIndex(count++);
            if (tid != null) {
                s.setTableId(t.getTagId());
            } else {
                s.setTableId(String.valueOf(s.getIndex()));
            }
            s.setTotalRows(t.getTotalRows());
            s.setDataList(data, maxCols);
            ret.add(s);
        }
        return ret;
    }

    public static List<HtmlTable> getTableSynopses(DataSourceInfo hi) throws IOException {
        Document doc = getHtmlDocument(URLUtil.openURL(hi.getUrl(), hi.getUsername(), hi.getPassword()));
        return getTableSynopses(doc);
    }

    public static List<HtmlTable> getTableSynopses(Document doc) {
        Node html = doc.getDocumentElement();
        if (HtmlTag.getTag(html) != HtmlTag.HTML) {
            throw new IllegalArgumentException("Not an HTML document");
        }
        Node body = HtmlUtil.getChild(html, HtmlTag.BODY);
        if (body == null) {
            throw new IllegalArgumentException("No HTML <BODY> tag found");
        }
        List<Node> tableNodes = HtmlUtil.getNodesByTag(body, HtmlTag.TABLE, null);
        List<HtmlTable> ret = new ArrayList<HtmlTable>();
        for (Node n : tableNodes) {
            HtmlTable ht = new HtmlTable(n, SYNOPSIS_ROWS);
            ret.add(ht);
        }
        return ret;
    }

    /**
     * Counts the number of (immediate) children of the target
     * node that have the specified tag type
     * @return the number of child tags
     */
    public static int countChildNodes(Node n, HtmlTag childtype) {
        NodeList nl = n.getChildNodes();
        int ret = 0;
        int len = nl.getLength();
        for (int i = 0; i < len; i++) {
            Node child = nl.item(i);
            if (getTag(child) == childtype) {
                ret++;
            }
        }
        return ret;
    }

    public static String getAttribute(Node node, String name) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) return null;
        Node attr = attributes.getNamedItem(name);
        return attr != null ? attr.getNodeValue() : null;
    }

    public static List<Node> getChildren(Node node) {
        final NodeList nodes = node.getChildNodes();
        return new AbstractList<Node>() {

            public Node get(int i) {
                if (i < 0 || i >= size()) throw new IndexOutOfBoundsException();
                return nodes.item(i);
            }

            public int size() {
                return nodes.getLength();
            }
        };
    }

    /**
     * Get the contents of a <title> tag
     * @param parent
     * @return
     */
    public static String getDocumentTitle(Node parent) {
        String ret = null;
        if (parent instanceof Document) {
            parent = ((Document) parent).getDocumentElement();
        }
        if (HtmlTag.getTag(parent) == HtmlTag.HTML) {
            Node head = getChild(parent, HtmlTag.HEAD);
            if (head != null) {
                Node title = getChild(head, HtmlTag.TITLE);
                if (title != null) {
                    ret = getText(title, 300);
                }
            }
        }
        return ret;
    }

    /**
     * get the HtmlTable from a document 
     * 
     * @param doc
     * @param tid   - if this is a string, get the table with this id, otherwise, treat it as N meaning get the Nth table on the 
     *              - page via depth first search
     * @param maxRows
     * @return  null  if table not found
     */
    public static HtmlTable getTable(Document doc, String tid, int maxRows) {
        Node html = doc.getDocumentElement();
        if (HtmlTag.getTag(html) != HtmlTag.HTML) {
            throw new IllegalArgumentException("Not an HTML document");
        }
        Node body = HtmlUtil.getChild(html, HtmlTag.BODY);
        if (body == null) {
            throw new IllegalArgumentException("No HTML <BODY> tag found");
        }
        List<Node> tableNodes = HtmlUtil.getNodesByTag(body, HtmlTag.TABLE, null);
        Node table = getTable(tableNodes, tid);
        if (table == null) {
            int tableNum = -1;
            try {
                tableNum = Integer.parseInt(tid);
            } catch (NullPointerException ignore) {
            } catch (NumberFormatException ignore) {
            }
            if (tableNum > -1 && tableNum < tableNodes.size()) {
                table = tableNodes.get(tableNum);
            }
        }
        HtmlTable htmlTable = null;
        if (table != null) {
            htmlTable = new HtmlTable(table, maxRows);
        }
        return htmlTable;
    }

    private static Node getTable(List<Node> nodes, String id) {
        for (Node node : nodes) {
            if (isTable(node, id)) return node;
        }
        return null;
    }

    private static boolean isTable(Node node, String id) {
        return HtmlTag.getTag(node) == HtmlTag.TABLE && (id == null || id.equals(HtmlUtil.getAttribute(node, "id")));
    }
}
