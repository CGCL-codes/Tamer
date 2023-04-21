package edu.umich.soar.debugger.general;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/************************************************************************
 * 
 * IMPORTANT NOTES ON THIS CLASS: This class was written long before SML
 * existed. During the writing of SML, this class was re-implemented in C++ as
 * ElementXML. In general, the sml.ElementXML class should be the class of
 * choice -- it's much more rigorously tested and will be consistently
 * maintained. So why is this class still here? There's a lot of code in the
 * debugger that uses this class and we just haven't had time to go through and
 * remove all of the uses of this code and use sml.ElementXML instead. To make
 * sure the two aren't muddled up this one has been renamed to JavaElementXML.
 * 
 * The ElementXML class is a general utility class for reading and writing XML
 * files. It represents an XML element, such as (using square brackets instead
 * of angled so that javadoc doesn't have a fit):
 * 
 * <pre>
 * [author genre="fiction"]
 * "tolkien"
 * [book]"the hobbit"[/book]
 * [/author]
 * </pre>
 * 
 * The components here are:
 * 
 * <pre>
 * Tag name   - "author"
 * Attribute  - "genre"
 * Value      - "fiction"
 * Contents   - "tolkien"
 * Children   - another element: tag "book", contents "the hobbit".
 * </pre>
 * 
 * Note that because XML elements can contain other child XML elements that one
 * element (and its children) is generally sufficient to represent an entire XML
 * file.
 * 
 * To write an XML file, construct the root XML element (and add all of its
 * children). Then call the WriteToFile function.
 * 
 * To read an XML file call the ReadFromFile function, which builds the XML root
 * element (and its children) and then returns it.
 * 
 * Of the two, the read function is much more complex. It uses a parser, which
 * in turn uses a lexical analyser.
 * 
 * The job of the lexical analyser is to turn the input stream into a series of
 * tokens: E.g. Given "[tag att="value"] the tokens would be
 * 
 * <pre>
 * symbol       - "[",
 * identifier   - "tag",
 * identifier   - "att",
 * symbol       - "="
 * quotedString - "value"
 * symbol	   - "]"
 * </pre>
 * 
 * Once the stream is broken into tokens, the parser then consumes them to build
 * the XML element.
 * 
 *************************************************************************/
public class JavaElementXML {

    private static class LexXML {

        public static final int kSymbol = 1;

        public static final int kIdentifier = 2;

        public static final int kQuotedString = 3;

        public static final int kComment = 4;

        public static final int kEOF = 5;

        /************************************************************************
         * 
         * The token class represents one element in the input stream. E.g.
         * Given "[tag att="value"] the tokens would be symbol - "[", identifier
         * - "tag", identifier - "att", symbol - "=" quotedString - "value"
         * symbol - "]"
         * 
         *************************************************************************/
        private static class Token {

            private String m_Value;

            private int m_Type;

            public Token(String value, int type) {
                m_Value = value;
                m_Type = type;
            }

            public int getType() {
                return this.m_Type;
            }

            public String getValue() {
                return this.m_Value;
            }
        }

        protected BufferedReader m_Input = null;

        protected String m_CurrentLine = null;

        protected int m_Pos = 0;

        protected Token m_CurrentToken = null;

        protected String m_LastComment = null;

        /************************************************************************
         * 
         * Constructor for the XML lexical analyser.
         * 
         * @param input
         *            The input stream of raw text from the XML file
         * 
         *************************************************************************/
        public LexXML(BufferedReader input) throws Exception {
            m_Input = input;
            m_CurrentLine = m_Input.readLine();
            m_CurrentLine.replaceAll(kSystemLineSeparator, kLineSeparator);
            m_CurrentLine += kLineSeparator;
            m_Pos = 0;
            m_LastComment = null;
            GetNextToken();
        }

        protected void setLastComment(String comment) {
            m_LastComment = comment;
        }

        public String getLastComment() {
            return m_LastComment;
        }

        /************************************************************************
         * 
         * Read the next character from the input file.
         * 
         * This function reads lines from the input file and inserts the local
         * line break character. For example, if you're running on Windows
         * reading a Linux file, the Windows line break chars will appear in the
         * character stream even though the original file used Linux line break
         * char.
         * 
         *************************************************************************/
        protected void GetNextChar() throws java.io.IOException {
            if (m_CurrentLine == null) return;
            m_Pos++;
            if (m_Pos >= m_CurrentLine.length()) {
                m_CurrentLine = m_Input.readLine();
                m_CurrentLine += kLineSeparator;
                m_Pos = 0;
            }
        }

        /************************************************************************
         * 
         * Returns the current character from the input stream.
         * 
         *************************************************************************/
        protected char getCurrentChar() {
            return m_CurrentLine.charAt(m_Pos);
        }

        /************************************************************************
         * 
         * Returns true if we're at the end of the file.
         * 
         *************************************************************************/
        protected boolean IsEOF() {
            return m_CurrentLine == null;
        }

        /************************************************************************
         * 
         * Returns true if the character is white space.
         * 
         *************************************************************************/
        protected boolean IsWhiteSpace(char ch) {
            return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n');
        }

        /************************************************************************
		* 
		* Returns true if this is a symbol (i.e. a special char in XML, like "<")
		* 
		*************************************************************************/
        protected boolean IsSymbol(char ch) {
            return (ch == kOpenTagChar || ch == kCloseTagChar || ch == kEndMarkerChar || ch == kHeaderChar || ch == kEqualsChar);
        }

        protected boolean IsCommentStart(char ch) {
            return (ch == kCommentStartChar);
        }

        /************************************************************************
         * 
         * Returns true if this is the quote character (")
         * 
         *************************************************************************/
        protected boolean IsQuote(char ch) {
            return (ch == '\"');
        }

        /************************************************************************
         * 
         * Set the value of the current token.
         * 
         *************************************************************************/
        protected void SetCurrentToken(String value, int type) {
            m_CurrentToken = new Token(value, type);
        }

        /************************************************************************
         * 
         * This is the main lexical analyser functions--gets the next token from
         * the input stream (e.g. an identifier, or a symbol etc.)
         * 
         *************************************************************************/
        public void GetNextToken() throws Exception {
            if (IsEOF() && m_CurrentToken.getType() == kEOF) {
                throw new Exception("Unexpected end of file when parsing file");
            }
            while (!IsEOF() && IsWhiteSpace(getCurrentChar())) GetNextChar();
            if (IsEOF()) {
                SetCurrentToken("", kEOF);
                return;
            }
            if (IsSymbol(getCurrentChar())) {
                SetCurrentToken(String.valueOf(getCurrentChar()), kSymbol);
                GetNextChar();
                if (IsCommentStart(getCurrentChar())) {
                    StringBuffer buffer = new StringBuffer();
                    GetNextChar();
                    GetNextChar();
                    GetNextChar();
                    boolean done = false;
                    while (!IsEOF() && !done) {
                        char last = getCurrentChar();
                        buffer.append(last);
                        GetNextChar();
                        done = (last == kCloseTagChar && buffer.length() > 3 && buffer.substring(buffer.length() - kCommentEndString.length()).equals(kCommentEndString));
                    }
                    SetCurrentToken(buffer.substring(0, buffer.length() - 3), kComment);
                }
                return;
            }
            if (IsQuote(getCurrentChar())) {
                StringBuffer buffer = new StringBuffer();
                GetNextChar();
                while (!IsEOF() && !IsQuote(getCurrentChar())) {
                    buffer.append(getCurrentChar());
                    GetNextChar();
                }
                GetNextChar();
                SetCurrentToken(buffer.toString(), kQuotedString);
                return;
            }
            StringBuffer identifier = new StringBuffer();
            while (!IsEOF() && !IsWhiteSpace(getCurrentChar()) && !IsSymbol(getCurrentChar()) && !IsQuote(getCurrentChar())) {
                identifier.append(getCurrentChar());
                GetNextChar();
            }
            SetCurrentToken(identifier.toString(), kIdentifier);
        }

        public String getCurrentTokenValue() {
            return this.m_CurrentToken.getValue();
        }

        /************************************************************************
         * 
         * Returns true if the current token matches the given type. E.g. if
         * (Have(kSymbol)) { // Process symbol }
         * 
         * @param type
         *            The type to test against.
         * 
         *************************************************************************/
        public boolean Have(int type) throws Exception {
            return (m_CurrentToken.getType() == type);
        }

        /************************************************************************
		* 
		* Returns true AND consumes the current token, if the values match.
		* We can consume the token, because the parser already knows its value.
		* E.g. if (Have("<")) { // Parse what comes after "<" }
		* 
		* @param value			The string value to test
		* 
		* @return True if value matches current token.
		* 
		*************************************************************************/
        public boolean Have(String value) throws Exception {
            if (m_CurrentToken.getValue().equals(value)) {
                GetNextToken();
                return true;
            }
            return false;
        }

        /************************************************************************
         * 
         * Checks that the current token matches the given value. If not, throws
         * an exception. Used for places in the parse when you know what must
         * come next. E.g. At the end of an XML token : MustBe("/") ;
         * MustBe(">") ;
         * 
         * @param value
         *            The value to test
         * 
         *************************************************************************/
        public void MustBe(String value) throws Exception {
            if (!m_CurrentToken.getValue().equals(value)) {
                throw new Exception("Looking for " + value + " instead found " + m_CurrentToken.getValue());
            }
            GetNextToken();
        }

        /************************************************************************
         * 
         * Checks that the current token matches the given type. If it does,
         * returns the value (this is often useful when testing for
         * identifiers). If it does not match, throws an exception.
         * 
         * @param type
         *            The type to test (e.g. kSymbol)
         * 
         * @return The value of the current token (if matches type).
         * 
         *************************************************************************/
        public String MustBe(int type) throws Exception {
            if (m_CurrentToken.getType() != type) {
                throw new Exception("Found incorrect type when parsing token " + m_CurrentToken.getValue());
            }
            String result = m_CurrentToken.getValue();
            GetNextToken();
            return result;
        }
    }

    public static final String kClassAttribute = "Class";

    public static final String kVersionAttribute = "Version";

    protected static final char kOpenTagChar = '<';

    protected static final String kOpenTagString = "<";

    protected static final char kCloseTagChar = '>';

    protected static final String kCloseTagString = ">";

    protected static final char kEndMarkerChar = '/';

    protected static final String kEndMarkerString = "/";

    protected static final char kHeaderChar = '?';

    protected static final String kHeaderString = "?";

    protected static final char kEqualsChar = '=';

    protected static final String kEqualsString = "=";

    protected static final char kCommentStartChar = '!';

    protected static final String kCommentStartString = "<!--";

    protected static final String kCommentEndString = "-->";

    protected static final String kLineSeparator = "\n";

    protected static final String kSystemLineSeparator = System.getProperty("line.separator");

    /** List of attribute names and string values */
    protected HashMap<String, String> m_AttributeList = new HashMap<String, String>();

    /** List of elements contained in this element */
    protected ArrayList<JavaElementXML> m_ChildElementList = new ArrayList<JavaElementXML>();

    /** The tag for this item */
    protected String m_TagName = null;

    /**
     * The comment for this item which will be added to the XML in front of this
     * tag
     */
    protected String m_Comment = null;

    /**
     * The content, lies between the tag start and end [tag]"content"[/tag].
     * Always quoted in file.
     */
    protected String m_Contents = null;

    /**
     * Sometimes its helpful to keep a reference to the object used to create
     * this element. This is essentially "user data" for the client of this
     * class
     */
    protected Object m_Source = null;

    /**
     * The parent node for this element (i.e. the inverse of the child
     * relationship)
     */
    protected JavaElementXML m_Parent = null;

    /************************************************************************
     * 
     * Constructor -- builds the new XML element and assigns it a tag name.
     * 
     * @param tagName
     *            The name of the tag for this element
     * 
     *************************************************************************/
    public JavaElementXML(String tagName) {
        if (tagName.indexOf(" ") != -1) throw new Error("Not allowed tag names with spaces inside them -- at least not for my form of XML");
        this.m_TagName = tagName;
    }

    /**
     * The parent node for this element (i.e. the inverse of the child
     * relationship)
     */
    public JavaElementXML getParent() {
        return this.m_Parent;
    }

    /**
     * The parent node for this element (i.e. the inverse of the child
     * relationship)
     */
    public void setParent(JavaElementXML element) {
        this.m_Parent = element;
    }

    /************************************************************************
     * 
     * These convert invalid XML chars to XML escape sequences and back again
     * (e.g. "<" => "&lt;" and so on.
     * 
     *************************************************************************/
    protected static String convertToEscapes(String src) {
        StringBuffer xmlSafe = new StringBuffer();
        int len = src.length();
        for (int i = 0; i < len; i++) {
            char c = src.charAt(i);
            switch(c) {
                case '<':
                    xmlSafe.append("&lt;");
                    break;
                case '>':
                    xmlSafe.append("&gt;");
                    break;
                case '&':
                    xmlSafe.append("&amp;");
                    break;
                case '"':
                    xmlSafe.append("&quot;");
                    break;
                case '\'':
                    xmlSafe.append("&apos;");
                    break;
                default:
                    xmlSafe.append(c);
            }
        }
        return xmlSafe.toString();
    }

    protected static String convertFromEscapes(String src) {
        StringBuffer orig = new StringBuffer();
        int len = src.length();
        for (int i = 0; i < len; i++) {
            char c = src.charAt(i);
            if (c == '&' && i < len - 2) {
                char next1 = src.charAt(i + 1);
                char next2 = src.charAt(i + 2);
                switch(next1) {
                    case 'l':
                        i += 3;
                        orig.append("<");
                        break;
                    case 'g':
                        i += 3;
                        orig.append(">");
                        break;
                    case 'q':
                        i += 5;
                        orig.append('"');
                        break;
                    case 'a':
                        if (next2 == 'm') {
                            i += 4;
                            orig.append("&");
                        } else {
                            i += 5;
                            orig.append("\'");
                        }
                        ;
                        break;
                }
            } else {
                orig.append(c);
            }
        }
        return orig.toString();
    }

    /************************************************************************
     * 
     * Constructor -- builds the new XML element and assigns it a tag name and
     * initial contents. Useful for simple tags.
     * 
     * @param tagName
     *            The name of the tag for this element
     * @param contents
     *            The body of this tag ([tag]"contents"[/tag])
     * 
     *************************************************************************/
    public JavaElementXML(String tagName, String contents) {
        this(tagName);
        this.addContents(contents);
    }

    /************************************************************************
     * 
     * Add a string to the "contents" of the XML element. The contents is the
     * part falling between the opening and closing tag. E.g.
     * <tag>contents</tag> The contents can be multi-line, but cannot contain
     * any embedded quotes.
     * 
     * @param contents
     *            The string to add to the current contents.
     * 
     *************************************************************************/
    public void addContents(String contents) {
        int quote = contents.indexOf('\"');
        if (quote != -1) {
            Debug.println("Tried to add a contents string with an embedded string character");
            return;
        }
        if (this.m_Contents == null) this.m_Contents = contents; else this.m_Contents += contents;
    }

    /************************************************************************
     * 
     * Gets the contents of this XML element.
     * 
     *************************************************************************/
    public String getContents() {
        return this.m_Contents;
    }

    /************************************************************************
     * 
     * Get and set the "source" object -- whose use is up to the client (not
     * required at all)
     * 
     *************************************************************************/
    public Object getSource() {
        return this.m_Source;
    }

    public void setSource(Object obj) {
        this.m_Source = obj;
    }

    /************************************************************************
     * 
     * Returns a Hashtable, mapping from attribute name to attribute value.
     * (String to String).
     * 
     *************************************************************************/
    public HashMap<String, String> getAttributeMap() {
        return this.m_AttributeList;
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute. The value is always a string.
     * 
     *************************************************************************/
    public String getAttribute(String name) {
        return this.m_AttributeList.get(name);
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute. The value is always a string.
     * This version throws an exception if the attribute is missing.
     * 
     *************************************************************************/
    public String getAttributeThrows(String name) throws Exception {
        String value = this.m_AttributeList.get(name);
        if (value == null) throw new Exception("Could not find attribute " + name + " while parsing XML document");
        return value;
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute, interpreting it as an double. If
     * the attribute is missing or cannot be parsed as an int, this function
     * throws an exception.
     * 
     *************************************************************************/
    public double getAttributeDoubleThrows(String name) throws Exception {
        String val = getAttributeThrows(name);
        double doubleVal = Double.parseDouble(val);
        return doubleVal;
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute, interpreting it as an double. If
     * the attribute is missing returns the defaultValue. If the attribute is
     * present but cannot be parsed as an int this throws.
     * 
     *************************************************************************/
    public double getAttributeDoubleDefault(String name, double defaultValue) throws Exception {
        String val = this.getAttribute(name);
        if (val == null) return defaultValue;
        double doubleVal = Double.parseDouble(val);
        return doubleVal;
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute, interpreting it as an int. If the
     * attribute is missing or cannot be parsed as an int, this function throws
     * an exception.
     * 
     *************************************************************************/
    public int getAttributeIntThrows(String name) throws Exception {
        String val = getAttributeThrows(name);
        int intVal = Integer.parseInt(val);
        return intVal;
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute, interpreting it as an int. If the
     * attribute is missing returns the defaultValue. If the attribute is
     * present but cannot be parsed as an int this throws.
     * 
     *************************************************************************/
    public int getAttributeIntDefault(String name, int defaultValue) throws Exception {
        String val = this.getAttribute(name);
        if (val == null) return defaultValue;
        int intVal = Integer.parseInt(val);
        return intVal;
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute, interpreting it as a long. If the
     * attribute is missing or cannot be parsed as a long, this function throws
     * an exception.
     * 
     *************************************************************************/
    public long getAttributeLongThrows(String name) throws Exception {
        String val = getAttributeThrows(name);
        long longVal = Long.parseLong(val);
        return longVal;
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute, interpreting it as a long. If the
     * attribute is missing returns the defaultValue. If the attribute is
     * present but cannot be parsed as an long this throws
     * 
     *************************************************************************/
    public long getAttributeLongDefault(String name, long defaultValue) throws Exception {
        String val = this.getAttribute(name);
        if (val == null) return defaultValue;
        long longVal = Long.parseLong(val);
        return longVal;
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute, interpreting it as an boolean. If
     * the attribute is missing or cannot be parsed as a bool, this function
     * returns the default value without throwing.
     * 
     *************************************************************************/
    public boolean getAttributeBooleanDefault(String name, boolean defaultValue) {
        String val = getAttribute(name);
        if (val == null) return defaultValue;
        if (val.equalsIgnoreCase("true")) return true;
        if (val.equalsIgnoreCase("false")) return false;
        return defaultValue;
    }

    /************************************************************************
     * 
     * Returns the value of a named attribute, interpreting it as an boolean. If
     * the attribute is missing or cannot be parsed as a bool, this function
     * throws an exception.
     * 
     *************************************************************************/
    public boolean getAttributeBooleanThrows(String name) throws Exception {
        String val = getAttributeThrows(name);
        if (val.equalsIgnoreCase("true")) return true;
        if (val.equalsIgnoreCase("false")) return false;
        throw new Exception("Could not parse the attribute " + name + ":" + val + " as a boolean");
    }

    /************************************************************************
     * 
     * Returns the number of children of this element.
     * 
     *************************************************************************/
    public int getNumberChildren() {
        return this.m_ChildElementList.size();
    }

    /************************************************************************
     * 
     * Returns a specific child of this element.
     * 
     * @param index
     *            The position in the list (>=0 and < number of children)
     * 
     * @return The element at the given position.
     * 
     *************************************************************************/
    public JavaElementXML getChild(int index) {
        return this.m_ChildElementList.get(index);
    }

    /************************************************************************
     * 
     * Returns a list of all of the children of this element. The list is of
     * ElementXML objects.
     * 
     *************************************************************************/
    protected ArrayList<JavaElementXML> getChildElementList() {
        return this.m_ChildElementList;
    }

    /************************************************************************
     * 
     * Replaces one child with another (in the same position in the list of
     * children).
     * 
     * @param existingChild
     *            The child being replaced
     * @param newChild
     *            The child being added (can be null -> just deletes)
     * 
     * @return True if replacement succeeds (i.e. existingChild is found)
     *************************************************************************/
    public boolean replaceChild(JavaElementXML existingChild, JavaElementXML newChild) {
        for (int i = 0; i < this.m_ChildElementList.size(); i++) {
            if (getChild(i) == existingChild) {
                m_ChildElementList.remove(i);
                existingChild.setParent(null);
                if (newChild != null) {
                    m_ChildElementList.add(i, newChild);
                    newChild.setParent(this);
                }
                return true;
            }
        }
        return false;
    }

    /************************************************************************
     * 
     * Removes a child from the XML tree
     * 
     * @param existingChild
     *            The child being removed
     * 
     * @return True if removal succeeds (i.e. existingChild is found)
     *************************************************************************/
    public boolean removeChild(JavaElementXML child) {
        return replaceChild(child, null);
    }

    /************************************************************************
     * 
     * Returns the tag name for this element. E.g. <author>Thomas</author> the
     * tag name is "author".
     * 
     *************************************************************************/
    public String getTagName() {
        return this.m_TagName;
    }

    /************************************************************************
     * 
     * A comment can be placed in front of this tag in the XML output. It takes
     * the form: <!--Comment String--> <tag>...</tag>
     * 
     * This doesn't allow completely general commenting but should be sufficient
     * for most uses and allows us to hide comments completely from a parser.
     * 
     *************************************************************************/
    public void setComment(String comment) {
        m_Comment = comment;
    }

    public String getComment() {
        return m_Comment;
    }

    /************************************************************************
     * 
     * Add an attribute and value pair. The value is always stored as a string.
     * 
     * @param name
     *            The attribute name
     * @param value
     *            The attribute value
     * 
     *************************************************************************/
    public void addAttribute(String name, String value) {
        if (name.indexOf(" ") != -1) throw new Error("Can't have attribute names that contain a space -- won't parse correctly");
        this.m_AttributeList.put(name, value);
    }

    /************************************************************************
     * 
     * Add a child element to this element's list of children.
     * 
     * @param element
     *            The element to add
     * 
     *************************************************************************/
    public void addChildElement(JavaElementXML element) {
        this.m_ChildElementList.add(element);
        element.setParent(this);
    }

    /************************************************************************
     * 
     * Find a child element based on an attribut name
     * 
     * @param attName
     *            The name to match against
     * 
     *************************************************************************/
    public JavaElementXML findChildByAtt(String attName, String value) {
        for (java.util.Iterator<JavaElementXML> iter = m_ChildElementList.iterator(); iter.hasNext(); ) {
            JavaElementXML element = iter.next();
            String att = element.getAttribute(attName);
            if (att != null && att.equalsIgnoreCase(value)) return element;
        }
        return null;
    }

    /************************************************************************
     * 
     * Find a child element based on a tag name
     * 
     * @param tagName
     *            The name to match against
     * 
     *************************************************************************/
    public JavaElementXML findChildByName(String tagName) {
        for (java.util.Iterator<JavaElementXML> iter = m_ChildElementList.iterator(); iter.hasNext(); ) {
            JavaElementXML element = iter.next();
            if (element.getTagName().equalsIgnoreCase(tagName)) return element;
        }
        return null;
    }

    /************************************************************************
     * 
     * Find a child element based on an attribute and value If the child does
     * not exist, this version throws an exception.
     * 
     * @param tagName
     *            The name to match against
     * 
     *************************************************************************/
    public JavaElementXML findChildByAttThrows(String attName, String value) throws Exception {
        JavaElementXML child = findChildByAtt(attName, value);
        if (child == null) throw new Exception("Could not find child node with name: " + attName + " while parsing XML document");
        return child;
    }

    /************************************************************************
     * 
     * Find a child element based on its tag name (case insensitve). If the
     * child does not exist, this version throws an exception.
     * 
     * @param tagName
     *            The name to match against
     * 
     *************************************************************************/
    public JavaElementXML findChildByNameThrows(String tagName) throws Exception {
        JavaElementXML child = findChildByName(tagName);
        if (child == null) throw new Exception("Could not find child node with name: " + tagName + " while parsing XML document");
        return child;
    }

    /************************************************************************
     * 
     * Find a child's contents based on its tag name (case insensitve). This
     * turns out to be a handy utility function.
     * 
     * @param tagName
     *            The name to match against
     * 
     *************************************************************************/
    public String findContentsByName(String tagName) {
        JavaElementXML element = findChildByName(tagName);
        if (element != null) return element.getContents();
        return null;
    }

    /************************************************************************
     * 
     * Write out the element and all of its children to a given file.
     * 
     * @param filename
     *            The path name of the output file.
     * 
     *************************************************************************/
    public void WriteToFile(String filename) throws java.io.IOException {
        FileWriter fw = new FileWriter(filename);
        BufferedWriter output = new BufferedWriter(fw);
        JavaElementXML.WriteHeader(output);
        WriteToStream(output, 0);
        output.close();
        fw.close();
    }

    public String WriteToString() {
        StringWriter sw = new StringWriter();
        BufferedWriter output = new BufferedWriter(sw);
        try {
            JavaElementXML.WriteHeader(output);
            WriteToStream(output, 0);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sw.toString();
    }

    public String toString() {
        return WriteToString();
    }

    /************************************************************************
     * 
     * Reads an XML file and builds the ElementXML object that represents it. As
     * an XML element can contain arbitrary children, the root object returned
     * can contain references to an arbitrarily deep tree of elements.
     * 
     * @param filename
     *            The xml file to read.
     * 
     * @return The root element.
     * 
     *************************************************************************/
    public static JavaElementXML ReadFromFile(String filename) throws Exception {
        FileReader fr = new FileReader(filename);
        BufferedReader input = new BufferedReader(fr);
        LexXML lexXML = new LexXML(input);
        JavaElementXML element = JavaElementXML.ReadFromLex(lexXML);
        input.close();
        fr.close();
        return element;
    }

    /************************************************************************
     * 
     * Parse an XML element and its children. This is the main parsing function.
     * The lexical analyser contains a references to the input stream being
     * parsed. The function calls itself recursively to parse its children.
     * 
     * @param lex
     *            The lexical analyser for this parse.
     * 
     * @return The element that has just been read from the input stream.
     * 
     *************************************************************************/
    public static JavaElementXML ParseElement(LexXML lex) throws Exception {
        if (lex.Have(kHeaderString)) {
            while (!lex.Have(kCloseTagString)) lex.GetNextToken();
            return null;
        }
        String tagName = lex.MustBe(LexXML.kIdentifier);
        JavaElementXML element = new JavaElementXML(tagName);
        if (lex.getLastComment() != null) {
            element.setComment(convertFromEscapes(lex.getLastComment()));
            lex.setLastComment(null);
        }
        while (lex.Have(LexXML.kIdentifier)) {
            String name = lex.getCurrentTokenValue();
            lex.GetNextToken();
            lex.MustBe(kEqualsString);
            String value = lex.MustBe(LexXML.kQuotedString);
            value = convertFromEscapes(value);
            element.addAttribute(name, value);
        }
        lex.MustBe(kCloseTagString);
        boolean endTag = false;
        while (!endTag) {
            if (lex.Have(LexXML.kQuotedString)) {
                String contents = lex.getCurrentTokenValue();
                contents = convertFromEscapes(contents);
                element.addContents(contents);
                lex.GetNextToken();
                continue;
            }
            if (lex.Have(LexXML.kComment)) {
                lex.setLastComment(lex.getCurrentTokenValue());
                lex.GetNextToken();
                continue;
            }
            if (lex.Have(kOpenTagString)) {
                if (lex.Have(kEndMarkerString)) {
                    endTag = true;
                } else {
                    JavaElementXML child = ParseElement(lex);
                    element.addChildElement(child);
                }
                continue;
            }
            String value = lex.getCurrentTokenValue();
            value = convertFromEscapes(value);
            element.addContents(value);
            lex.GetNextToken();
        }
        tagName = lex.MustBe(LexXML.kIdentifier);
        if (!tagName.equals(element.getTagName())) throw new Exception("The closing tag for " + element.getTagName() + " doesn't match the opening tag");
        lex.MustBe(kCloseTagString);
        return element;
    }

    /************************************************************************
     * 
     * Parse the input stream (encoded by the lexical analyser) until we find
     * the first XML element. Return that element. Note: This assumes the file
     * contains only a single element, with all other elements stored as its
     * children. BADBAD: If this is not true, this function should be
     * generalized to create a root node and store all of the elements it finds
     * in this root node.
     * 
     * @param lex
     *            The lexical analyser, tied to a specific input stream
     * 
     * @return The root XML element.
     * 
     *************************************************************************/
    public static JavaElementXML ReadFromLex(LexXML lex) throws Exception {
        JavaElementXML element = null;
        while (element == null) {
            if (lex.Have(LexXML.kComment)) {
                lex.setLastComment(lex.getCurrentTokenValue());
                lex.GetNextToken();
                continue;
            }
            if (lex.Have(kOpenTagString)) element = ParseElement(lex); else lex.GetNextToken();
        }
        return element;
    }

    /************************************************************************
     * 
     * Write out this XML element to the given output stream. The output is
     * indented by 'indent' spaces so that the output looks more pleasant when
     * viewed in an editor.
     * 
     * @param output
     *            The output stream
     * @param indent
     *            The number of spaces to indent this XML element.
     * 
     *************************************************************************/
    public void WriteToStream(BufferedWriter output, int indent) throws java.io.IOException {
        StringBuffer buffer = new StringBuffer();
        boolean oneLine = (this.getNumberChildren() == 0);
        for (int i = 0; i < indent; i++) {
            buffer.append("  ");
        }
        if (m_Comment != null) {
            output.write(buffer.toString());
            output.write(kCommentStartString);
            output.write(convertToEscapes(m_Comment));
            output.write(kCommentEndString);
            output.newLine();
        }
        buffer.append(kOpenTagString);
        buffer.append(m_TagName);
        java.util.Set<Entry<String, String>> entrySet = this.m_AttributeList.entrySet();
        for (java.util.Iterator<Entry<String, String>> iter = entrySet.iterator(); iter.hasNext(); ) {
            Entry<String, String> entry = iter.next();
            String name = entry.getKey();
            String value = entry.getValue();
            buffer.append(" ");
            buffer.append(name);
            buffer.append("=");
            buffer.append("\"");
            buffer.append(convertToEscapes(value));
            buffer.append("\"");
        }
        buffer.append(kCloseTagString);
        output.write(buffer.toString());
        if (!oneLine) output.newLine();
        if (this.m_Contents != null) {
            if (!oneLine) {
                for (int i = 0; i < indent; i++) {
                    output.write("  ");
                }
            }
            output.write("\"" + convertToEscapes(this.m_Contents) + "\"");
            if (!oneLine) output.newLine();
        }
        for (java.util.Iterator<JavaElementXML> iter = m_ChildElementList.iterator(); iter.hasNext(); ) {
            JavaElementXML element = iter.next();
            element.WriteToStream(output, indent + 1);
        }
        if (!oneLine) {
            for (int i = 0; i < indent; i++) {
                output.write("  ");
            }
        }
        output.write(kOpenTagString + kEndMarkerString + m_TagName + kCloseTagString);
        output.newLine();
    }

    /************************************************************************
     * 
     * Write out the XML header. BADBAD: Not sure what needs to go in here to
     * make it fully valid XML. I think just the version is sufficient.
     * 
     * @param output
     *            The output stream.
     * 
     *************************************************************************/
    public static void WriteHeader(BufferedWriter output) throws java.io.IOException {
        output.write("<?xml version=\"1.0\"?>");
        output.newLine();
    }

    /************************************************************************
     * 
     * Given an XML element that represents an object from a class, this
     * function creates the object. It does this by looking for a "class"
     * attribute and then instantiating an instance of that class. Note: This
     * will only work if the class (a) exists and (b) has a public default
     * constructor.
     * 
     * @return The object represented by this XML element (or null if there's an
     *         error).
     * 
     *************************************************************************/
    public Object CreateObjectFromXMLDefaultConstructorNoThrow() {
        String className = this.getAttribute(kClassAttribute);
        if (className == null) return null;
        try {
            Class<?> childClass = Class.forName(className);
            Object obj = childClass.newInstance();
            return obj;
        } catch (Exception e) {
            String msg = e.getMessage();
            Debug.println("Problem creating object: " + msg + ".  Does it have a default constructor?");
            return null;
        }
    }

    /************************************************************************
     * 
     * Given an XML element that represents an object from a class, this
     * function creates the object. It does this by looking for a "class"
     * attribute and then instantiating an instance of that class. Note: This
     * will only work if the class (a) exists and (b) has a "createInstance()"
     * public method that takes no arguments.
     * 
     * I think this is a better solution than the methods which require a
     * default constructor.
     * 
     * @return The object represented by this XML element.
     * 
     *************************************************************************/
    public Object CreateObjectFromXML() throws Exception {
        String className = this.getAttribute(kClassAttribute);
        if (className == null) throw new Exception("This XML object does not have a class attribute, so no object can be built from it");
        try {
            Class<?> childClass = Class.forName(className);
            java.lang.reflect.Method method = childClass.getMethod("createInstance", (Class[]) null);
            Object obj = method.invoke(null, (Object[]) null);
            return obj;
        } catch (Exception e) {
            String msg = e.getMessage();
            Debug.println("Problem creating object: " + msg + ".  Does it have a createInstance method?");
            throw e;
        }
    }

    /************************************************************************
     * 
     * Given an XML element that represents an object from a class, this
     * function creates the object. It does this by looking for a "class"
     * attribute and then instantiating an instance of that class. Note: This
     * will only work if the class (a) exists and (b) has a public default
     * constructor.
     * 
     * I have generally decided that I'm not very happy with this because it
     * requires a public default constructor and that removes a nice paradigm
     * where some classes would have a constructor taking arguments, so we could
     * be sure they are initialized correctly.
     * 
     * Instead, I've therefore written a replacement for this which requires a
     * createInstance() static method--not something likely to be used in error
     * by a developer.
     * 
     * @return The object represented by this XML element.
     * 
     *************************************************************************/
    public Object CreateObjectFromXMLDefaultConstructor() throws Exception {
        String className = this.getAttribute(kClassAttribute);
        if (className == null) throw new Exception("This XML object does not have a class attribute, so no object can be built from it");
        try {
            Class<?> childClass = Class.forName(className);
            Object obj = childClass.newInstance();
            return obj;
        } catch (Exception e) {
            String msg = e.getMessage();
            Debug.println("Problem creating object: " + msg + ".  Does it have a default constructor?");
            throw e;
        }
    }

    /************************************************************************
     * 
     * A test function that writes out a sample file and reads it back in.
     * 
     * @return True if the test succeeds, else false.
     * 
     *************************************************************************/
    public static boolean Test() {
        try {
            File directory = edu.umich.soar.debugger.general.Debug.FindDataDirectory();
            File file = new File(directory, "test.xml");
            JavaElementXML root = new JavaElementXML("rootNode");
            root.addAttribute("att1", "this is first attribute value");
            root.addAttribute("att2", "true");
            root.addContents("Root contents");
            JavaElementXML child1 = new JavaElementXML("child1");
            JavaElementXML child2 = new JavaElementXML("child2");
            JavaElementXML subChild = new JavaElementXML("subchild");
            JavaElementXML subSubChild = new JavaElementXML("subSubChild");
            root.addChildElement(child1);
            root.addChildElement(child2);
            child1.addChildElement(subChild);
            child1.addContents("Child 1 has contents");
            child1.addContents(kLineSeparator);
            child1.addContents("Child 1 has more contents");
            child2.addAttribute("color", "red");
            subChild.addChildElement(subSubChild);
            root.WriteToFile(file.getAbsolutePath());
            JavaElementXML readRoot = JavaElementXML.ReadFromFile(file.getAbsolutePath());
            Debug.Check(readRoot.getTagName().equals("rootNode"));
            Debug.Check(readRoot.getChildElementList().size() == 2);
            Debug.Check(readRoot.getContents().equals("Root contents"));
            String val1 = (String) readRoot.getAttributeMap().get("att1");
            String val4 = (String) readRoot.getAttributeMap().get("att2");
            Debug.Check(val1 != null && val1.equals("this is first attribute value"));
            Debug.Check(val4 != null && val4.equals("true"));
            JavaElementXML readChild1 = (JavaElementXML) readRoot.getChildElementList().get(0);
            JavaElementXML readChild2 = (JavaElementXML) readRoot.getChildElementList().get(1);
            Debug.Check(readChild1.getTagName().equals("child1"));
            Debug.Check(readChild2.getTagName().equals("child2"));
            String contents = "Child 1 has contents" + kLineSeparator + "Child 1 has more contents";
            Debug.Check(readChild1.getContents().equals(contents));
            String val2 = (String) readChild2.getAttributeMap().get("color");
            Debug.Check(val2 != null && val2.equals("red"));
            String val3 = readChild2.getAttribute("color");
            Debug.Check(val3 != null && val3.equals("red"));
            JavaElementXML readSubChild = (JavaElementXML) readChild1.getChildElementList().get(0);
            JavaElementXML sameSubChild = readChild1.getChild(0);
            Debug.Check(readSubChild == sameSubChild);
            Debug.Check(readSubChild.getTagName().equals("subchild"));
            JavaElementXML readSubSubChild = (JavaElementXML) readSubChild.getChildElementList().get(0);
            Debug.Check(readSubSubChild.getTagName().equals("subSubChild"));
        } catch (Throwable t) {
            return false;
        }
        return true;
    }
}
