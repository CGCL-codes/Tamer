package sunlabs.brazil.sunlabs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import sunlabs.brazil.session.PropertiesCacheManager;
import sunlabs.brazil.util.Format;
import sunlabs.brazil.util.Glob;
import sunlabs.brazil.util.LexML;
import sunlabs.brazil.util.StringMap;

/**
 * Create a tree representation of an xml file whose parts may be
 * referenced as a dictionary.  This is currently "read only".
 */
public class XmlTree extends Dictionary implements PropertiesCacheManager.Saveable {

    String ident = null;

    Node root = null;

    int nodes = 0;

    Hashtable tags = null;

    NodeName nodeName = new DefaultNodeName();

    String prefix = null;

    String delim = ".";

    String dfltSuffix = "cdata";

    static Hashtable suffixes;

    static {
        suffixes = new Hashtable();
        suffixes.put("cdata", "normal");
        suffixes.put("tag", "normal");
        suffixes.put("index", "normal");
        suffixes.put("attributes", "normal");
        suffixes.put("children", "normal");
        suffixes.put("childCount", "normal");
        suffixes.put("xml", "normal");
        suffixes.put("value", "strip");
        suffixes.put("glob", "special");
        suffixes.put("all", "normal");
    }

    /**
     * Make an empty tree.
     */
    public XmlTree() {
    }

    /**
     * Given an XML string, build the tree.
     */
    public XmlTree(String src) throws IllegalArgumentException {
        replace(src);
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    /**
     * Add an element to the tag process list.
     * Once a Process tag is defined, only tags defined are
     * processed.  All other tags are treated as singletons
     */
    public void setTag(String tag) {
        if (tags == null) {
            tags = new Hashtable();
        }
        tags.put(tag.toLowerCase(), "");
    }

    /**
     * Set the list of tags to process
     */
    public void setTags(Hashtable tags) {
        this.tags = tags;
    }

    public Hashtable getTags() {
        return tags;
    }

    /**
     * set the name of this tree
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * set the node delimiter.
     */
    public void setDelim(String delim) {
        this.delim = (delim == null ? "." : delim);
    }

    public boolean setDflt(String dflt) {
        if (dflt != null && suffixes.get(dflt) != null) {
            dfltSuffix = dflt;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set the class that determines a node's name.
     * This may be used to change the way nodes are named in an arbitrary
     * fashion.
     */
    public void setComparator(NodeName nodeName) {
        this.nodeName = nodeName;
    }

    /** Print a tree
     * @param node: The starting node
     * @param sb:   where to append the results to
     * @param level: the nesting level
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        toString(root, sb, 0);
        return sb.toString();
    }

    public void toString(Node node, StringBuffer sb, int level) {
        sb.append("\n");
        indent(sb, level);
        sb.append("<" + node.getTag());
        StringMap attrs = node.getAttributes();
        if (attrs != null) {
            for (int i = 0; i < attrs.size(); i++) {
                String key = attrs.getKey(i);
                String value = fix(attrs.get(i));
                sb.append(" ").append(key).append("=").append(value);
            }
        }
        if (node.isSingle()) {
            sb.append(" />");
        } else {
            sb.append(">");
        }
        String cdata = node.getCdata();
        if (cdata != null) {
            sb.append("\n");
            indent(sb, level + 1);
            sb.append(cdata);
        }
        if (!node.isSingle()) {
            int n = node.childCount();
            for (int i = 0; i < n; i++) {
                toString(node.getChild(i), sb, level + 1);
            }
            sb.append("\n");
            indent(sb, level);
            sb.append("</" + node.getTag() + ">");
        }
    }

    /** 
     * Quote an attribute properly.
     * This doesn't distinguish FOO="" from FOO
     */
    static String fix(String s) {
        if (s.startsWith("\"") || s.startsWith("\'")) {
            return s;
        } else if (s.indexOf("\"") < 0) {
            return "\"" + s + "\"";
        } else {
            return "\'" + s + "\'";
        }
    }

    static void indent(StringBuffer sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
    }

    /**
     * Replace the XmlTree with new markup.
     * @param src:	the xml data
     */
    public void replace(String src) throws IllegalXmlException {
        nodes = 0;
        root = new Node("ROOT", false, null, null, Node.ROOT, 0);
        Stack stack = new Stack();
        stack.push(new StackInfo(root, 0));
        Node parent = root;
        LexML lex = new LexML(src);
        Node current = root;
        IllegalXmlException ex = null;
        while (lex.nextToken()) {
            switch(lex.getType()) {
                case LexML.COMMENT:
                    break;
                case LexML.STRING:
                    current.appendCdata(lex.getBody());
                    break;
                case LexML.TAG:
                    String name = lex.getTag().toLowerCase();
                    if (name.startsWith("/")) {
                        name = name.substring(1);
                        if (tags != null && !tags.containsKey(name)) {
                            continue;
                        }
                        if (!name.equals(parent.getTag())) {
                            int sl = line(lex.getString(), ((StackInfo) stack.peek()).position);
                            ex = IllegalXmlException.getEx(ex, ident, sl, parent.getTag(), line(lex), "</" + name + ">");
                            for (int i = stack.size() - 2; i > 0; i--) {
                                Node node = ((StackInfo) stack.elementAt(i)).parent;
                                String tag = node.getTag();
                                if (tag.equals(name)) {
                                    while (++i <= stack.size()) {
                                        stack.pop();
                                    }
                                    stack.pop();
                                    current = parent = ((StackInfo) stack.peek()).parent;
                                    break;
                                }
                            }
                            continue;
                        } else {
                            stack.pop();
                            current = parent = ((StackInfo) stack.peek()).parent;
                        }
                    } else {
                        boolean single = lex.isSingleton();
                        if (!single && tags != null && !tags.containsKey(name)) {
                            single = true;
                        }
                        int count = ((StackInfo) stack.peek()).getCount(name);
                        Node n = new Node(name, single, lex.getAttributes(), parent, Node.TAG, count);
                        current = n;
                        nodes++;
                        parent.addChild(n);
                        if (!single) {
                            stack.push(new StackInfo(n, lex.getLocation()));
                            parent = n;
                        }
                    }
                    break;
                default:
                    System.out.println("Oops, invalid type!");
                    break;
            }
        }
        stack.pop();
        while (stack.size() > 2) {
            stack.pop();
            parent = ((StackInfo) stack.peek()).parent;
            int sl = line(lex.getString(), ((StackInfo) stack.peek()).position);
            ex = IllegalXmlException.getEx(ex, ident, sl, parent.getTag(), 1 + line(lex.getString(), lex.getLocation()), "eof");
        }
        if (ex != null) {
            throw ex;
        }
    }

    public boolean setAttribute(String name, String key, String value) {
        Node n = search(name);
        if (n != null) {
            n.putAttribute(key, value);
            return true;
        } else {
            return false;
        }
    }

    public boolean setCdata(String name, String data) {
        Node n = search(name);
        if (n != null) {
            n.setCdata(data);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Do some more reasonable error handling.  We'll stuff more info
     * into the exception.
     *
     */
    public static class IllegalXmlException extends IllegalArgumentException {

        public Vector errors = null;

        public IllegalXmlException(String s) {
            super(s);
        }

        public int addError(int sl, String st, int cl, String ct) {
            if (errors == null) {
                errors = new Vector();
            }
            errors.add(new XmlErrorInfo(sl, st, cl, ct));
            return errors.size();
        }

        public static IllegalXmlException getEx(IllegalXmlException e, String s, int sl, String st, int cl, String ct) {
            if (e == null) {
                e = new IllegalXmlException(s);
            }
            e.addError(sl, st, cl, ct);
            return e;
        }

        public String toString() {
            String result = "" + errors.size() + " error(s) processing: " + getMessage();
            for (int i = 0; i < errors.size(); i++) {
                XmlErrorInfo xi = (XmlErrorInfo) errors.elementAt(i);
                result += "\n" + (i + 1) + ")\t" + "found \"" + xi.currentTag + "\" at line " + xi.currentLine + ", need match for \"" + xi.startTag + "\" at line " + xi.startLine;
            }
            return result;
        }
    }

    public static class XmlErrorInfo {

        public int startLine;

        public String startTag;

        public int currentLine;

        public String currentTag;

        public XmlErrorInfo(int startLine, String startTag, int currentLine, String currentTag) {
            this.startLine = startLine;
            this.startTag = startTag;
            this.currentLine = currentLine;
            this.currentTag = currentTag;
        }
    }

    public interface NodeName {

        public String getName(Node n);
    }

    /**
     * The node is named by the specified attribute.  If no attribute
     * is specified, use the tag name instead, followed by the index in ().
     */
    public static class DefaultNodeName implements NodeName {

        String attribute;

        String dflt;

        public DefaultNodeName() {
            this(null, null);
        }

        public DefaultNodeName(String attribute, String dflt) {
            this.attribute = attribute;
            this.dflt = (dflt == null ? "" : dflt);
        }

        public String getName(Node n) {
            if (n == null) {
                return null;
            } else if (attribute == null) {
                return n.getTag() + "(" + n.getIndex() + ")";
            } else {
                String name = n.getAttribute(attribute);
                return name == null ? dflt : Format.deQuote(name);
            }
        }

        public String toString() {
            return attribute + "(" + dflt + ")";
        }
    }

    /**
     * Find a node in the tree by name, starting at the root.
     * @param s		The node pathname
     * @return		The node, if found, or null
     */
    public Node search(String s) {
        StringTokenizer st = new StringTokenizer(s, delim);
        if (prefix != null && !prefix.equals(st.nextToken())) {
            return null;
        }
        return search(root, st);
    }

    /**
     * Find a node in the tree by name, starting under any node.
     */
    public Node search(Node node, StringTokenizer st) {
        if (st.hasMoreTokens()) {
            String name = st.nextToken();
            for (int i = 0; i < node.childCount(); i++) {
                Node child = node.getChild(i);
                if (name.equals(nodeName.getName(child))) {
                    return search(child, st);
                }
            }
            return null;
        } else {
            return node;
        }
    }

    /**
     * Find all nodes that match a glob pattern, starting at the root.
     */
    public Vector match(String pattern) {
        StringTokenizer st = new StringTokenizer(pattern, delim);
        if (prefix != null && !prefix.equals(st.nextToken())) {
            return null;
        }
        Vector results = new Vector();
        match(root, st, results);
        return results;
    }

    /**
     * Find all nodes that match a glob pattern, starting at any node.
     */
    public void match(Node node, StringTokenizer st, Vector results) {
        if (st.hasMoreTokens()) {
            String pattern = st.nextToken();
            for (int i = 0; i < node.childCount(); i++) {
                Node child = node.getChild(i);
                String name = nodeName.getName(child);
                if (Glob.match(pattern, name)) {
                    match(child, st, results);
                } else {
                }
            }
        } else {
            results.addElement(node);
        }
    }

    public Enumeration elements() {
        Vector v = new Vector();
        elements(root, v);
        return v.elements();
    }

    public static void elements(Node n, Vector v) {
        if (n.type != Node.ROOT) {
            v.addElement(n);
        }
        for (int i = 0; i < n.childCount(); i++) {
            elements(n.getChild(i), v);
        }
    }

    public Enumeration keys() {
        Vector v = new Vector();
        keys(root, prefix, delim, v);
        return v.elements();
    }

    public void keys(Node n, String prefix, String delim, Vector v) {
        if (n.type != Node.ROOT) {
            String name = nodeName.getName(n);
            if (prefix == null) {
                prefix = name;
            } else {
                prefix += delim + name;
            }
            v.addElement(prefix);
        }
        for (int i = 0; i < n.childCount(); i++) {
            keys(n.getChild(i), prefix, delim, v);
        }
    }

    /**
     * Given a node description, return the value, if any.
     * Descriptions are of the form:
     *   [prefix].name.[suffix]
     * where :
     *  [prefix] is the name of the tree
     *  "." is the current delimiter,
     *  name is the path name of a node in the tree
     *  [suffix] specifies which part of the node to return as a string.
     *  See getpart() for the list of valid suffixes.
     *
     */
    public Object get(Object k) {
        if (!(k instanceof String) || !((String) k).startsWith(prefix)) {
            return null;
        }
        return getPart((String) k);
    }

    /**
     * Given a node descriptor, return the result. XXX not done
     *
     * modifiers:
     *  cdata:		return cdata
     *  tag:		the name
     *  index:		which tag within whis parent
     *  attributes:	the list of attribute names
     *  children:	the list of children
     *  childCount:	the number of children
     *  <attribute>.value	the value for attribute <attribute>
     *  glob		nodes matching the glob pattern
     *  all		all nodes under this one
     */
    public String getPart(String s) {
        int i = s.lastIndexOf(delim);
        if (i < 0) {
            return null;
        }
        String result = null;
        String suffix = s.substring(i + 1);
        String root = s.substring(0, i);
        Node node = null;
        String type = (String) suffixes.get(suffix);
        if (type == null) {
            type = (String) suffixes.get(dfltSuffix);
            suffix = dfltSuffix;
            root = s;
        }
        if (type.equals("normal")) {
            node = search(root);
            if (node == null) {
                return null;
            }
        }
        if (suffix.equals("cdata")) {
            result = node.getCdata();
            if (result == null) {
                result = "";
            }
        } else if (suffix.equals("tag")) {
            result = node.getTag();
        } else if (suffix.equals("index")) {
            result = "" + node.getIndex();
        } else if (suffix.equals("attributes")) {
            StringMap attributes = node.getAttributes();
            StringBuffer sb = new StringBuffer();
            for (i = 0; i < attributes.size(); i++) {
                sb.append(attributes.getKey(i)).append(" ");
            }
            result = sb.toString();
        } else if (suffix.equals("children")) {
            int count = node.childCount();
            if (count == 0) {
                result = "";
            } else {
                StringBuffer sb = new StringBuffer();
                for (i = 0; i < node.childCount(); i++) {
                    Node child = node.getChild(i);
                    sb.append(nodeName.getName(child)).append(" ");
                }
                result = sb.toString();
            }
        } else if (suffix.equals("xml")) {
            StringBuffer sb = new StringBuffer();
            toString(node, sb, 0);
            result = sb.toString();
        } else if (suffix.equals("childCount")) {
            result = "" + node.childCount();
        } else if (suffix.equals("all")) {
            Vector v = new Vector();
            keys(node, prefix, delim, v);
            StringBuffer sb = new StringBuffer();
            for (i = 0; i < v.size(); i++) {
                sb.append(v.elementAt(i)).append(" ");
            }
            result = sb.toString();
        } else if (suffix.equals("value")) {
            i = root.lastIndexOf(delim);
            if (i < 0) {
                result = null;
            } else {
                String attribute = root.substring(i + 1);
                root = root.substring(0, i);
                node = search(root);
                if (node != null) {
                    result = node.getAttribute(attribute);
                } else {
                    result = null;
                }
            }
        } else if (suffix.equals("glob")) {
            Vector v = match(root);
            StringBuffer sb = new StringBuffer();
            for (i = 0; i < v.size(); i++) {
                sb.append(nodeName.getName((Node) v.elementAt(i))).append(" ");
            }
        } else {
            System.out.println("Suffix unimplemented: " + suffix);
        }
        return result;
    }

    public Object put(Object k, Object v) {
        System.out.println("Tree ignoring put of: " + k + "=" + v);
        return null;
    }

    public Object remove(Object o) {
        return null;
    }

    public int size() {
        return nodes;
    }

    public boolean isEmpty() {
        return nodes == 0;
    }

    public void load(InputStream in) throws IOException {
        replace(getFile(in));
    }

    public void save(OutputStream out, String header) throws IOException {
        out.write(("<!-- " + header + "-->\n" + this).getBytes());
    }

    /**
     * Find the line at the current processing position.
     */
    static int line(LexML lex) {
        return line(lex.getString(), lex.getLocation());
    }

    static int line(String str, int stop) {
        int line = 1;
        int index = 0;
        while ((index = str.indexOf('\n', index)) > 0 && index < stop) {
            line++;
            index++;
        }
        return line;
    }

    public static void main(String[] args) {
        XmlTree x = new XmlTree();
        x.setIdent(args[0]);
        for (int i = 1; i < args.length; i++) {
            x.setTag(args[i]);
        }
        try {
            x.replace(getFile(args[0]));
            System.out.println(args[0] + " OK");
        } catch (IllegalXmlException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("" + e);
        }
    }

    public static String getFile(String s) throws IOException {
        FileInputStream in = new FileInputStream(s);
        return getFile(in);
    }

    public static String getFile(InputStream in) throws IOException {
        byte[] b = new byte[in.available()];
        in.read(b);
        String data = new String(b);
        in.close();
        return data;
    }

    /**
     * Info to keep on stack.  We keep the char position with the
     * node so we can generate more useful error messages for invalid
     * documents.
     */
    static class StackInfo {

        public Node parent;

        public int position;

        public Hashtable names;

        public StackInfo(Node parent, int position) {
            this.parent = parent;
            this.position = position;
            this.names = null;
        }

        public int getCount(String name) {
            int result = 0;
            if (names == null) {
                names = new Hashtable();
            }
            Incr incr = (Incr) names.get(name);
            if (incr == null) {
                names.put(name, new Incr());
            } else {
                result = incr.incr();
            }
            return result;
        }

        public String toString() {
            return "Stack: " + parent + " children(" + names + ")";
        }
    }

    static class Incr {

        int count;

        public Incr() {
            count = 0;
        }

        public int incr() {
            count++;
            return count;
        }

        public String toString() {
            return "" + count;
        }
    }

    /**
     * This describes a node of the XML tree
     */
    public static class Node {

        static final int ROOT = 1;

        static final int TAG = 2;

        static final int COMMENT = 3;

        String tag;

        int type;

        int index;

        boolean singleton;

        StringMap attributes;

        String cdata;

        Node parent;

        Vector children;

        public Node(String tag, boolean singleton, StringMap attributes, Node parent, int type, int index) {
            this.tag = tag;
            this.singleton = singleton;
            this.type = type;
            this.index = index;
            this.attributes = attributes;
            this.cdata = null;
            this.parent = parent;
            this.children = null;
        }

        public void setCdata(String s) {
            cdata = s;
        }

        public void putAttribute(String key, String value) {
            if (value != null) {
                attributes.put(key, value);
            } else {
                attributes.remove(key);
            }
        }

        public void appendCdata(String s) {
            s = s.trim();
            if (s.length() > 0) {
                cdata = cdata == null ? s : cdata + " " + s;
            }
        }

        public void addChild(Node child) {
            if (children == null) {
                children = new Vector();
            }
            children.addElement(child);
        }

        public Node getChild(int i) {
            return (Node) children.elementAt(i);
        }

        public String getTag() {
            return tag;
        }

        public int getIndex() {
            return index;
        }

        public boolean isSingle() {
            return singleton;
        }

        public void setSingle(boolean s) {
            singleton = s;
        }

        public StringMap getAttributes() {
            return attributes;
        }

        public String getAttribute(String name) {
            return attributes == null ? null : attributes.get(name);
        }

        public String getCdata() {
            return cdata;
        }

        public Node getParent() {
            return parent;
        }

        public int childCount() {
            return children == null ? 0 : children.size();
        }

        public String toString() {
            return "Node: " + tag + "[" + index + "] (attr=" + attributes + ") " + cdata;
        }
    }
}
