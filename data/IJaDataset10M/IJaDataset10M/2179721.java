package net.sf.compositor.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XML parsing without the tricky bits.
 */
public class SimplisticXmlParser {

    private static final boolean NOT_SHORTCUT = false;

    private static final boolean SHORTCUT = true;

    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("(?x) # comments and whitespace \n" + "\\s*      # space              \n" + "(?:       # begin attributes   \n" + "\\b.+?\\b # name               \n" + "\\s*=\\s* # equals             \n" + "([\"'])   # open quotes        \n" + ".*?       # value              \n" + "\\1       # close quotes       \n" + "\\s*      # space              \n" + ")*        # end attributes     \n" + "/?        # shortcut?          \n" + ">         # done               \n");

    private final String m_xml;

    protected final Element m_preRoot;

    /**
	 * We assume that you have a string of XML. Hah - as if!
	 *
	 * @param xml  some well-formed XML without mixed content
	 * @throws XmlParserException  if <code>xml</code> cannot be parsed
	 */
    public SimplisticXmlParser(final String xml) throws XmlParserException {
        if (null == xml) {
            throw new XmlParserException("Input xml is null.");
        }
        final StringBuilder buf = new StringBuilder(xml.trim());
        final Mutint offset = new Mutint(0);
        m_preRoot = new Element();
        for (int startPos; -1 != (startPos = buf.indexOf("<?")); ) {
            final int endPos = buf.indexOf("?>");
            if (-1 == endPos) {
                throw new XmlParserException("Bad processing instruction.");
            }
            buf.delete(startPos, endPos + 2);
        }
        for (int startPos; -1 != (startPos = buf.indexOf("<!--")); ) {
            final int endPos = buf.indexOf("-->", startPos);
            if (-1 == endPos) {
                throw new XmlParserException("Bad comment.");
            }
            try {
                buf.delete(startPos, endPos + 3);
            } catch (final StringIndexOutOfBoundsException x) {
                throw new XmlParserException("Could not find end of comment (" + x + ") startPos " + startPos + " endPos " + endPos, x);
            }
        }
        while (0 < buf.length() && Character.isWhitespace(buf.charAt(0))) {
            buf.delete(0, 1);
        }
        if (9 <= buf.length() && "<!".equals(buf.substring(0, 2)) && "DOCTYPE".equalsIgnoreCase(buf.substring(2, 9)) && Character.isWhitespace(buf.charAt(9))) {
            buf.delete(0, buf.indexOf(">") + 1);
        }
        m_xml = buf.toString().trim();
        if (0 != m_xml.length()) {
            m_preRoot.addChild(new Element(m_xml, offset));
        }
        if (m_xml.length() != offset.value) {
            throw new XmlParserException("Extraneous content after root element.");
        }
    }

    /**
	 * Copy constructor
	 */
    public SimplisticXmlParser(final SimplisticXmlParser source) {
        m_xml = source.m_xml;
        m_preRoot = new Element(source.m_preRoot);
    }

    private static String showContext(final String xml, final Mutint offset) {
        return showContext(xml, offset.value);
    }

    private static String showContext(final String xml, final int offset) {
        final int start = Math.max(offset - 19, 0);
        final int end = Math.min(offset + 19, xml.length());
        int mark = Math.max(offset - start, 0);
        final StringBuilder result = new StringBuilder("\n");
        final StringBuilder chunk = new StringBuilder(xml.substring(start, end));
        for (int i = chunk.length() - 1; i >= 0; i--) {
            switch(chunk.charAt(i)) {
                case '\n':
                    chunk.replace(i, i + 1, "\\n");
                    if (i <= mark) {
                        mark++;
                    }
                    break;
                case '\r':
                    chunk.replace(i, i + 1, "\\r");
                    if (i <= mark) {
                        mark++;
                    }
                    break;
                case '\t':
                    chunk.replace(i, i + 1, "\\t");
                    if (i <= mark) {
                        mark++;
                    }
                    break;
            }
        }
        result.append(chunk);
        result.append('\n');
        for (int i = 0; i < mark; i++) {
            result.append('-');
        }
        result.append('^');
        return result.toString();
    }

    /**
	 * Represents an element in a simplistic XML tree. Has <em>either</em>
	 * content <em>or</em> child elements. Alert readers will have spotted that
	 * not all XML conforms to this pattern.
	 */
    protected class Element {

        private final String m_name;

        private String m_content;

        private final List<Element> m_children = new ArrayList<Element>();

        private final Map<String, String> m_attributes = new LinkedHashMap<String, String>();

        Element() {
            m_name = "";
            m_content = null;
        }

        Element(final Element source) {
            m_name = source.m_name;
            m_content = source.m_content;
            for (final Element e : source.m_children) {
                final Element child = new Element(e);
                m_children.add(child);
            }
            m_attributes.putAll(source.m_attributes);
        }

        Element(final String xml, final Mutint offset) throws XmlParserException {
            char thisChar;
            final StringBuilder buf = new StringBuilder();
            final boolean shortcut;
            final String content;
            if (0 == xml.length()) {
                throw new XmlParserException("XML has no content.");
            }
            skipWhite(xml, offset);
            if ('<' != xml.charAt(offset.value)) {
                throw new XmlParserException("Tag not found at offset " + offset + '.' + showContext(xml, offset));
            }
            if (!Character.isLetter(thisChar = xml.charAt(++offset.value))) {
                throw new XmlParserException("Tag name not found at offset " + offset + '.' + (thisChar == '/' && xml.charAt(++offset.value) != ' ' ? " Bad tag nesting?" : "") + showContext(xml, offset));
            }
            do {
                buf.append(thisChar);
            } while (Character.isLetterOrDigit(thisChar = xml.charAt(++offset.value)) || thisChar == ':' || thisChar == '_' || thisChar == '-');
            m_name = buf.toString();
            if (thisChar == '/') {
                shortcut = SHORTCUT;
                if ('>' != xml.charAt(++offset.value)) {
                    throw new XmlParserException("Tag shortcut closure incorrect at offset " + offset + '.' + showContext(xml, offset));
                }
                offset.value++;
            } else {
                shortcut = snagAttribs(xml, offset);
            }
            if (shortcut) {
                content = "";
            } else {
                final String closeTag = "</" + m_name + '>';
                final int closeTagLen = closeTag.length();
                skipWhite(xml, offset);
                if (offset.value + closeTagLen > xml.length()) {
                    throw new XmlParserException("Mismatched tags: " + m_name + '.' + showContext(xml, offset));
                } else if (closeTag.equals(xml.substring(offset.value, offset.value + closeTagLen))) {
                    content = "";
                    offset.value += closeTagLen;
                } else {
                    if ('<' == xml.charAt(offset.value)) {
                        if (xml.startsWith("<![CDATA[", offset.value)) {
                            final int cdataStart = offset.value + "<![CDATA[".length();
                            final int cdataEnd = xml.indexOf("]]>", cdataStart);
                            if (-1 == cdataEnd) {
                                throw new XmlParserException("Unclosed CDATA section: " + showContext(xml, offset));
                            } else {
                                content = xml.substring(cdataStart, cdataEnd);
                                offset.value = cdataEnd + "]]>".length();
                            }
                        } else {
                            content = null;
                            try {
                                while (!closeTag.equals(xml.substring(offset.value, offset.value + closeTagLen))) {
                                    this.addChild(new Element(m_xml, offset));
                                    skipWhite(xml, offset);
                                }
                            } catch (final StringIndexOutOfBoundsException e) {
                                throw new XmlParserException("Could not construct children of " + m_name + ": " + e + showContext(xml, offset), e);
                            }
                        }
                    } else {
                        final int tagPos = xml.indexOf('<', offset.value);
                        content = xml.substring(offset.value, tagPos).trim();
                        offset.value = tagPos;
                    }
                    try {
                        if (!closeTag.equals(xml.substring(offset.value, offset.value + closeTagLen))) {
                            throw new XmlParserException("Mismatched close tag: " + m_name + " at " + offset + '.' + showContext(xml, offset));
                        }
                    } catch (final StringIndexOutOfBoundsException e) {
                        throw new XmlParserException("Could not find end of " + m_name + ": " + e + showContext(xml, offset), e);
                    }
                    offset.value += closeTagLen;
                }
            }
            m_content = EntityDecoder.replace(content);
        }

        private boolean snagAttribs(final String xml, final Mutint offset) throws XmlParserException {
            boolean result = NOT_SHORTCUT;
            final int endPos = findEndOfTag(xml, offset.value);
            if (xml.charAt(endPos - 1) == '/') {
                result = SHORTCUT;
            }
            try {
                for (final AttribTokenizer t = new AttribTokenizer(xml.substring(offset.value, endPos)); t.hasMoreTokens(); ) {
                    final String s = t.nextToken();
                    if ("/".equals(s) && !t.hasMoreTokens()) {
                        break;
                    } else {
                        final StringTokenizer t2 = new StringTokenizer(s, "=\"'");
                        final String name;
                        final String value;
                        try {
                            name = t2.nextToken();
                        } catch (final NoSuchElementException e) {
                            throw new XmlParserException("Bad tag attributes: \"" + s + "\" near " + offset + '.' + showContext(xml, offset), e);
                        }
                        if (t2.hasMoreTokens()) {
                            value = EntityDecoder.replace(t2.nextToken());
                        } else {
                            value = "";
                        }
                        m_attributes.put(name, value);
                    }
                }
            } catch (final XmlParserException e) {
                throw new XmlParserException(e.getMessage() + showContext(xml, offset), e);
            }
            offset.value = endPos + 1;
            return result;
        }

        private int findEndOfTag(final String xml, final int offset) throws XmlParserException {
            if ('>' == xml.charAt(offset)) return offset;
            if ('/' == xml.charAt(offset) && '>' == xml.charAt(offset + 1)) return offset + 1;
            final Matcher m = ATTRIBUTE_PATTERN.matcher(xml);
            if (!m.find(offset) || offset != m.start()) {
                throw new XmlParserException("Bad attribute syntax, offset " + offset + '.' + showContext(xml, offset));
            }
            return m.end() - 1;
        }

        private class AttribTokenizer {

            final Iterator m_iterator;

            private AttribTokenizer(final String source) throws XmlParserException {
                final List<String> tokenlets = new LinkedList<String>();
                final List<String> tokens = new LinkedList<String>();
                if (!"/".equals(source.trim())) {
                    for (final StringTokenizer m_tok = new StringTokenizer(source, " \t\r\n='\"", true); m_tok.hasMoreTokens(); ) {
                        tokenlets.add(m_tok.nextToken());
                    }
                    try {
                        for (final ListIterator i = tokenlets.listIterator(); i.hasNext(); ) {
                            final StringBuilder value = new StringBuilder();
                            String tmp;
                            stripWhiteSpace(i);
                            final String name = (String) i.next();
                            stripWhiteSpace(i);
                            if (!"=".equals(i.next())) {
                                throw new XmlParserException("Attribute name \"" + name + "\" not followed by equals.");
                            }
                            stripWhiteSpace(i);
                            final String quote = (String) i.next();
                            if (!"\"".equals(quote) && !"'".equals(quote)) {
                                throw new XmlParserException("Attribute value not quoted.");
                            }
                            for (tmp = (String) i.next(); !quote.equals(tmp); tmp = (String) i.next()) {
                                value.append(tmp);
                            }
                            stripWhiteSpace(i);
                            tokens.add(name + '=' + quote + value + quote);
                            if (!i.hasNext() || "/".equals(i.next())) {
                                break;
                            } else {
                                i.previous();
                            }
                        }
                    } catch (final NoSuchElementException e) {
                        throw new XmlParserException("Out of elements tokenizing attributes - source: [" + source + ']', e);
                    }
                }
                m_iterator = tokens.iterator();
            }

            private boolean hasMoreTokens() {
                return m_iterator.hasNext();
            }

            private void stripWhiteSpace(final ListIterator li) {
                stripper: while (li.hasNext()) {
                    final CharacterIterator ci = new StringCharacterIterator((String) li.next());
                    for (char c = ci.first(); c != CharacterIterator.DONE; c = ci.next()) {
                        switch(c) {
                            case ' ':
                            case '\t':
                            case '\n':
                            case '\r':
                                continue;
                            default:
                                li.previous();
                                break stripper;
                        }
                    }
                }
            }

            private String nextToken() {
                return (String) m_iterator.next();
            }
        }

        String getName() {
            return m_name;
        }

        /**
		 * Returns the path tail of the added child, including any index
		 * suffix, e.g. <code>child[99]</code>
		 */
        public final String addChild(final String child) {
            int count = 0;
            for (Element thisChild : m_children) if (thisChild.getName().equals(child)) count++;
            try {
                m_children.add(new Element('<' + child + "/>", new Mutint(0)));
            } catch (final XmlParserException x) {
                throw new RuntimeException("Could not add " + child + ": " + x, x);
            }
            return 0 == count ? child : child + '[' + count + ']';
        }

        final void addChild(final Element child) {
            m_children.add(child);
        }

        public List<Element> getChildren() {
            return m_children;
        }

        String getContent() {
            return m_content;
        }

        void setContent(final String content) {
            m_content = content;
        }

        String getAttribute(final String attribute) {
            return m_attributes.get(attribute);
        }

        int childCount() {
            return m_children.size();
        }

        Element getChild(final String name) throws XmlPathException {
            final int openBracket = name.indexOf('[');
            int index;
            final String shortName;
            Element result = null;
            final String attribName;
            final String attribValue;
            if (-1 == openBracket) {
                index = 0;
                shortName = name;
                attribName = null;
                attribValue = null;
            } else if (0 == openBracket) {
                throw new XmlPathException("Invalid indexed path.");
            } else {
                final int closeBracket = name.indexOf(']');
                if (openBracket > closeBracket) {
                    throw new XmlPathException("Invalid indexed path bracket sequence.");
                }
                if (name.charAt(openBracket + 1) == '@') {
                    final String selStr = name.substring(openBracket + 2, closeBracket);
                    final int eqPos = selStr.indexOf('=');
                    if (-1 == eqPos) {
                        throw new XmlPathException("Bad attribute specification - no equals.");
                    }
                    attribName = selStr.substring(0, eqPos);
                    attribValue = normaliseAttribSelectorValue(selStr.substring(eqPos + 1));
                    index = -1;
                } else {
                    try {
                        index = Integer.parseInt(name.substring(openBracket + 1, closeBracket));
                    } catch (final NumberFormatException e) {
                        throw new XmlPathException("Invalid path index: " + e, e);
                    }
                    if (0 > index) {
                        throw new XmlPathException("Negative path index.");
                    }
                    attribName = null;
                    attribValue = null;
                }
                shortName = name.substring(0, openBracket);
            }
            for (final Element thisChild : m_children) {
                if (thisChild.getName().equals(shortName)) {
                    if (null != attribName) {
                        if (attribValue.equals(thisChild.getAttribute(attribName))) {
                            result = thisChild;
                            break;
                        }
                    } else if (0 == index) {
                        result = thisChild;
                        break;
                    }
                    index--;
                }
            }
            return result;
        }

        private String escapeApos(final String source) {
            final StringBuilder buf = new StringBuilder(source);
            for (int pos; -1 < (pos = buf.indexOf("'")); ) {
                buf.replace(pos, pos + 1, "&#x27;");
            }
            return buf.toString();
        }

        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder("<");
            result.append(m_name);
            if (0 < getAttributeCount()) {
                for (String name : getAttributeNames()) {
                    result.append(' ');
                    result.append(name);
                    result.append("='");
                    result.append(escapeApos(getAttribute(name)));
                    result.append('\'');
                }
            }
            if (null != m_content && 0 < m_content.length()) {
                result.append('>');
                result.append(m_content);
                result.append("</");
                result.append(m_name);
                result.append('>');
            } else if (0 < m_children.size()) {
                result.append('>');
                result.append(Env.NL);
                for (final Element child : m_children) {
                    result.append(StringUtil.indent(child.toString()));
                }
                result.append("</");
                result.append(m_name);
                result.append('>');
            } else {
                result.append(" />");
            }
            result.append(Env.NL);
            return result.toString();
        }

        private int getAttributeCount() {
            return m_attributes.size();
        }

        private Set<String> getAttributeNames() {
            return m_attributes.keySet();
        }

        public void setAttribute(final String name, final String value) {
            m_attributes.put(name, value);
        }
    }

    private static String normaliseAttribSelectorValue(final String v) throws XmlPathException {
        final int len = v.length();
        if (0 == len) {
            throw new XmlPathException("Attribute selector has no value");
        }
        final char firstChar = v.charAt(0);
        final char lastChar = v.charAt(len - 1);
        switch(firstChar) {
            case '\'':
            case '"':
                if (lastChar != firstChar) {
                    throw new XmlPathException("Mismatched quotes in attribute selector");
                }
                return v.substring(1, len - 1);
            default:
                return v;
        }
    }

    /**
	 * We assume that you know an XPath-ish way of accessing a leaf element.
	 * Mixed content be damned!
	 *
	 * @param path  an XPath-ish expression
	 * @return  the content of the specified leaf element
	 * @throws XmlParserException  if <code>path</code> does not resolve to a
	 *                             leaf element
	 */
    public String getElementContent(final String path) throws XmlPathException {
        final Element element = findElement(path);
        if (null == element.getContent()) {
            throw new XmlPathException("Path " + path + " does not lead to a leaf element.");
        }
        return element.getContent();
    }

    /**
	 * We assume that you know an XPath-ish way of accessing an element.
	 * Mixed content be damned!
	 *
	 * @param path       an XPath-ish expression
	 * @param attribute  the name of an attribute
	 * @return  the content of the specified element attribute, or null if the
	 *          element has no such attribute
	 * @throws XmlParserException  if <code>path</code> does not resolve to an
	 *                             element
	 */
    public String getElementAttribute(final String path, final String attribute) throws XmlPathException {
        return findElement(path).getAttribute(attribute);
    }

    /**
	 * How many children does this element have?
	 *
	 * @see #getChildCount(String,String)
	 */
    public int getChildCount(final String path) throws XmlPathException {
        return findElement(path).childCount();
    }

    /**
	 * How many children of a certain type does this element have?
	 *
	 * @see #getChildCount(String)
	 */
    public int getChildCount(final String path, final String element) throws XmlPathException {
        int result = 0;
        try {
            for (final Element child : findElement(path).getChildren()) {
                if (child.getName().equals(element)) {
                    result++;
                }
            }
        } catch (final XmlPathException x) {
        }
        return result;
    }

    /**
	 * Gets an element from a path.
	 *
	 * @throws XmlPathException  if path invalid (but perhaps should return null instead)
	 */
    protected Element findElement(final String path) throws XmlPathException {
        Element result = m_preRoot;
        if ("/".equals(path)) {
            return result;
        }
        for (final StringTokenizer t = new StringTokenizer(path, "/"); t.hasMoreTokens(); ) {
            final String token = t.nextToken();
            result = result.getChild(token);
            if (null == result) {
                throw new XmlPathException("Path " + path + ", element " + token + " not found.");
            }
        }
        return result;
    }

    static void skipWhite(final String s, final Mutint offset) {
        for (final int count = s.length(); offset.value < count; offset.value++) {
            if (!Character.isWhitespace(s.charAt(offset.value))) {
                break;
            }
        }
    }

    /**
	 * Gets a list of <code>String</code> element paths that are children of
	 * <code>path</code>, in the correct order. Note that these are fully
	 * qualified, e.g. parsing
	 * <code>&lt;a&gt;&lt;b/&gt;&lt;b/&gt;&lt;/a&gt;</code> and calling
	 * <code>getElementPaths("a")</code> would return the list
	 * <code>"a/b", "a/b[1]"</code>.
	 *
	 * @param path  where to look for elements
	 * @see #getElementPathTails(String)
	 * @see #getElementNames(String)
	 * @see #getElementInfos(String)
	 */
    public List<String> getElementPaths(final String path) throws XmlPathException {
        final List<String> result = new ArrayList<String>();
        final List<Element> children = findElement(path).getChildren();
        final Map<String, Mutint> counts = new HashMap<String, Mutint>();
        for (final Element child : children) {
            final StringBuilder newPath = new StringBuilder("/".equals(path) ? "" : path);
            final String name = child.getName();
            Mutint count = counts.get(name);
            if (null == count) {
                count = new Mutint(0);
                counts.put(name, count);
            } else {
                count.value++;
            }
            if (0 != newPath.length()) {
                newPath.append('/');
            }
            newPath.append(name);
            if (0 < count.value) {
                newPath.append('[');
                newPath.append(count.value);
                newPath.append(']');
            }
            result.add('/' == newPath.charAt(0) ? newPath.substring(1) : newPath.toString());
        }
        return result;
    }

    /**
	 * Gets a list of <code>String</code> element names that are children of
	 * <code>path</code>, in the correct order. Note that these are not
	 * qualified in any way, e.g. parsing
	 * <code>&lt;a&gt;&lt;b/&gt;&lt;b/&gt;&lt;/a&gt;</code> and calling
	 * <code>getElementNames("a")</code> would return the list
	 * <code>"b", "b"</code> rather than <code>"b", "b[1]"</code>. For
	 * qualified names, use {@link #getElementPathTails(String)}
	 *
	 * @param path  where to look for elements
	 * @see #getElementPathTails(String)
	 * @see #getElementPaths(String)
	 * @see #getElementInfos(String)
	 */
    public List<String> getElementNames(final String path) throws XmlPathException {
        final List<String> result = new ArrayList<String>();
        final List<Element> children = findElement(path).getChildren();
        for (final Element child : children) {
            result.add(child.getName());
        }
        return result;
    }

    /**
	 * Gets a list of <code>String</code> element paths that are children of
	 * <code>path</code>, in the correct order. Note that these are qualified
	 * with appropriate indexes, e.g. parsing
	 * <code>&lt;a&gt;&lt;b/&gt;&lt;b/&gt;&lt;/a&gt;</code> and calling
	 * <code>getElementPathTails("a")</code> would return the list
	 * <code>"b", "b[1]"</code>. For un-qualified names, use {@link
	 * #getElementNames(String)}
	 *
	 * @param path  where to look for elements
	 * @see #getElementNames(String)
	 * @see #getElementPaths(String)
	 * @see #getElementInfos(String)
	 */
    public List<String> getElementPathTails(final String path) throws XmlPathException {
        final List<String> childPaths = getElementPaths(path);
        final List<String> result = new ArrayList<String>(childPaths.size());
        for (final String childPath : childPaths) {
            final int slashPos = childPath.lastIndexOf('/');
            result.add(childPath.substring(slashPos + 1));
        }
        return result;
    }

    /**
	 * Gets a list of <code>String</code> element paths that are children of
	 * <code>path</code>, in the correct order. Note that these are qualified
	 * with appropriate indexes, e.g. parsing
	 * <code>&lt;a&gt;&lt;b/&gt;&lt;b/&gt;&lt;/a&gt;</code> and calling
	 * <code>getElementPathTails("a")</code> would return the list
	 * <code>"b", "b[1]"</code>. For un-qualified names, use {@link
	 * #getElementNames(String)}
	 *
	 * @param path  where to look for elements
	 * @see #getElementPathTails(String)
	 * @see #getElementPaths(String)
	 * @see #getElementNames(String)
	 */
    public List<ElementInfo> getElementInfos(final String path) throws XmlPathException {
        final List<String> childPaths = getElementPaths(path);
        final List<ElementInfo> result = new ArrayList<ElementInfo>(childPaths.size());
        for (final String childPath : childPaths) result.add(new ElementInfo(childPath));
        return result;
    }

    /**
	 * Gets a map of <code>String</code> element names to
	 * <code>Integer</code> element counts.
	 *
	 * @param path  where to look for elements
	 */
    public Map<String, Integer> getElementCounts(final String path) throws XmlPathException {
        final List<Element> children = findElement(path).getChildren();
        final Map<String, Integer> result = new HashMap<String, Integer>();
        for (final Element child : children) {
            final String name = child.getName();
            final Integer count = result.get(name);
            if (null == count) {
                result.put(name, 1);
            } else {
                result.put(name, count.intValue() + 1);
            }
        }
        return result;
    }

    /**
	 * Returns the number of attributes an element has.
	 */
    public int getElementAttributeCount(final String path) throws XmlPathException {
        return findElement(path).getAttributeCount();
    }

    /**
	 * Returns the available attribute names of an element.
	 */
    public Set<String> getElementAttributeNames(final String path) throws XmlPathException {
        return findElement(path).getAttributeNames();
    }

    @Override
    public String toString() {
        final List<Element> children = m_preRoot.getChildren();
        final int childCount = children.size();
        switch(childCount) {
            case 0:
                return "";
            case 1:
                return children.get(0).toString();
            default:
                throw new IllegalStateException("XML should contain one root element, not " + childCount + ": " + children.get(0).getName() + ", " + children.get(1).getName() + (2 < childCount ? "..." : "."));
        }
    }

    public class ElementInfo {

        private final String m_path;

        private String m_name;

        private String m_tail;

        private ElementInfo(final String path) {
            m_path = path;
        }

        public String getPath() {
            return m_path;
        }

        public String getName() {
            if (null == m_name) {
                final String tail = getTail();
                final int bp = tail.indexOf('[');
                m_name = -1 == bp ? tail : tail.substring(0, bp);
            }
            return m_name;
        }

        public String getTail() {
            if (null == m_tail) {
                final int sp = m_path.lastIndexOf('/');
                m_tail = -1 == sp ? m_path : m_path.substring(sp + 1);
            }
            return m_tail;
        }

        public String toString() {
            return m_path + " - " + getTail() + " - " + getName();
        }
    }
}
