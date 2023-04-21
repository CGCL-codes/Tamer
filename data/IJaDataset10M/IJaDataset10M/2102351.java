package net.noderunner.exml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.noderunner.exml.ElementRule.ElementRuleState;

/**
 * This is a very simple XML reader, which follows the "XML road map," a
 * parsing guide found in "The XML Companion" by Neil Bradley.
 * Parsing can be done discretely, by stopping at element declarations.
 * The names follow the road map naming scheme, so the Java naming
 * convention is not followed.
 * <p>
 * The parsing is not 100% strict.  Here are the following known (by design)
 * liberties:
 * <ul>
 * <li>Input data is not checked for invalid values, such as formfeed,
 * out-of-range unicode, etc.  This would create unnecessary overhead.</li>
 * <li>XML data is not checked past the end of the last document closing tag.
 * This allows for streaming data to be parsed.  Check for EOF by calling
 * <code>hasMoreData</code> when done parsing to enforce this.</li>
 * <li>Unescaped &gt; is allowed in character data.</li>
 * </ul>
 * </p>
 * <p>
 * The parsing is not 100% conformant.  Here are the following known
 * issues:
 * <ul>
 * <li>Parameter entities are not treated as complete objects.</li>
 * <li>Attribue value whitespace is not normalized.</li>
 * </ul>
 *
 * <p>
 * How to parse an XML document:
 * <pre>
 * java.io.Reader r = new java.io.StringReader("&lt;doc&gt;Text&lt;doc&gt;");
 * XmlReader xr = new XmlReader(r);
 * Document d = xr.document();
 * </pre>
 * </p>
 *
 * @see Document
 * @author Elias Ross
 * @version 1.0
 */
public class XmlReader {

    /**
	 * Does the reading.
	 */
    private XmlScanner scanner;

    /**
	 * Set if in a entity scan.
	 */
    private boolean inEntityScan;

    /**
	 * cbufSize should be sufficiently large for finding tag matches.
	 */
    private static final int cbufSize = 64;

    char cbuf[] = new char[cbufSize];

    /**
	 * Stores the document type description (Dtd) information.  May be
	 * created ahead of time, or read from file.
	 */
    private Dtd dtd;

    /**
	 * Stores the current element rule state.
	 */
    private RuleStack eruleStack;

    /**
	 * Stores the attribute rule state.
	 */
    private ElementRule.AttributeRuleState aruleState;

    /**
	 * Stores attribute value data.
	 * Also used in {@link #Nmtoken} function.
	 */
    private XmlCharArrayWriter attValue = new XmlCharArrayWriter(64);

    /**
	 * Holds the last resolved name.
	 */
    private NamespaceImpl namespaceElement = new NamespaceImpl();

    /**
	 * Holds the last resolved for an attribute.
	 */
    private NamespaceImpl namespaceAttr = new NamespaceImpl();

    /**
	 * A SystemLiteralResolver is used to parse system (Dtd) files.
	 */
    private SystemLiteralResolver resolver;

    /**
	 * Construct a new reader with a pre-defined Dtd and specific resolver.
	 * @param dtd existing Dtd
	 * @param resolver specific resolver
	 */
    public XmlReader(Reader reader, Dtd dtd, SystemLiteralResolver resolver) {
        this.dtd = dtd;
        this.resolver = resolver;
        this.eruleStack = new RuleStack();
        this.aruleState = new ElementRule.AttributeRuleState();
        scanner = createXmlScanner(reader);
        setupStringPool();
    }

    private void setupStringPool() {
        scanner.getStringPool().add(XmlTags.XMLNS);
        scanner.getStringPool().addAll(dtd.getKnownElements().keySet());
        Iterator i = dtd.getKnownElements().values().iterator();
        while (i.hasNext()) {
            ElementRule er = (ElementRule) i.next();
            List l = er.getAttributeRules();
            if (l != null) {
                for (int j = 0; j < l.size(); j++) {
                    String name = ((AttributeRule) l.get(j)).getName();
                    scanner.getStringPool().add(name);
                }
            }
        }
    }

    private static XmlScanner createXmlScanner(Reader reader) {
        return new XmlScanner(reader, 1024 * 4, cbufSize);
    }

    /**
	 * Construct a new reader with a pre-defined Dtd and null resolver.
	 * @param dtd existing Dtd
	 */
    public XmlReader(Reader reader, Dtd dtd) {
        this(reader, dtd, NullResolver.getInstance());
    }

    /**
	 * Construct a new reader with a default Dtd and null resolver.
	 */
    public XmlReader(Reader reader) {
        this(reader, new Dtd());
    }

    /**
	 * Construct a reader that reads a string.
	 * @see #setReadString
	 */
    public XmlReader(String s) {
        this();
        setReadString(s);
    }

    /**
	 * Construct a dummy reader.
	 * @see #setReader
	 * @see #setReadString
	 */
    public XmlReader() {
        this(NullReader.getInstance());
    }

    /**
	 * Reuse this reader on another input stream.
	 */
    public void setReader(Reader reader) {
        scanner.setReader(reader);
    }

    /**
	 * Reuse this reader on string input.
	 */
    public void setReadString(String xml) {
        scanner = new XmlScanner(xml);
    }

    /**
	 * Returns the underlying Dtd.
	 */
    public Dtd getDtd() {
        return dtd;
    }

    /**
	 * Returns the underlying scanner.
	 */
    public XmlScanner getScanner() {
        return scanner;
    }

    /**
	 * Returns the underlying string pool.
	 * The string pool can be used to add pre-existing canonical strings to
	 * the pool, so that all element tags of the same value are the same
	 * object.
	 */
    public StringPool getStringPool() {
        return scanner.getStringPool();
    }

    /**
	 * Sets the SystemLiteralResolver for this XmlReader.
	 */
    public void setResolver(SystemLiteralResolver resolver) {
        this.resolver = resolver;
    }

    /**
	 * Returns the SystemLiteralResolver for this XmlReader.
	 */
    public SystemLiteralResolver getResolver() {
        return resolver;
    }

    /**
	 * Tests if no more bytes can be read from the stream, by trying to
	 * read a character and pushing it back if it was read.  This
	 * indicates the entire document was read.
	 * @return false if no more bytes can be read
	 */
    public boolean hasMoreData() throws IOException {
        int c = scanner.peek();
        if (c == -1) return false;
        return true;
    }

    /**
	 * Closes the underlying input stream.  Once this method is called,
	 * do not attempt to parse any more data.
	 * @throws IOException if closing failed for some reason
	 */
    public void close() throws IOException {
        scanner.close();
    }

    /**
	 * Reads in XML document.
	 * By specification, all <i>Misc</i> data should be read in at the end of
	 * reading a document.  However, to support streaming this is not done.
	 * Call <code>while (Misc(document));</code> to remove this data.
	 * <p><code>{01} document</code></p>
	 * @see #Misc
	 * @return document 
	 */
    public Document document() throws IOException, XmlException {
        Document d = new Document();
        Prolog(d);
        Element e = element();
        if (e == null) {
            throw new XmlException("Could not find root object");
        }
        String docname = dtd.getName();
        if (docname != null && !docname.equals(e.getName())) {
            throw new XmlException("Root element name does not match doctype " + docname);
        }
        d.appendChild(e);
        return d;
    }

    /**
	 * Check if valid XML character.  This is not used for performance
	 * reasons.
	 * <p><code>{02} Char (Character) {15.1} {16.3} {20.1} {65.1} {S37.1}</code></p>
	 * @return is valid XML character
	 */
    public static boolean Char(int i) {
        if (i >= 0x20 && i <= 0xD7FF) return true;
        if (i == 0x09 || i == 0x0a || i == 0x0d) return true;
        if (i >= 0xE000 && i <= 0xFFFD) return true;
        if (i >= 0x10000 && i <= 0x10FFFF) return true;
        return false;
    }

    /**
	 * @return true if character is a space
	 */
    boolean isS(int c) {
        return (c == 0x20 || c == 0x0a || c == 0x0d || c == 0x09);
    }

    /**
	 * Parse white space.  This is a little bit different than the java
	 * idea of whitespace.  This is optimized to read in just a little
	 * bit of space at a time.
	 * <p><code>{03} S (Space) {06.2} ... :</code></p>
	 * @return true if space was found, false if EOF or no spaces
	 */
    public boolean S() throws IOException {
        boolean found = false;
        while (true) {
            if (!isS(scanner.peek())) return found;
            scanner.read();
            found = true;
        }
    }

    /**
	 * Determines if the given character is a valid <code>NameChar</code>.
	 * Note:  Does not conform to exact XML spec, since I have no idea
	 * what a CombiningChar or Extender is.
	 * <p><code>{04} NameChar (Name Character) {05.2 .3 .4}
	 * {07.1}</code></p>
	 */
    public static boolean NameChar(final char c) {
        if ((c >= 0x61 && c <= 0x7a) || (c >= 0x41 && c <= 0x5a) || (c >= 0x30 && c <= 0x3a) || c == '.' || c == '-' || c == '_') return true;
        if (c < 128) return false;
        return Character.isLetterOrDigit(c);
    }

    /**
	 * Not a builtin rule.
	 */
    static boolean FirstNameChar(final char c) {
        if ((c >= 0x61 && c <= 0x7a) || (c >= 0x41 && c <= 0x5a) || c == ':' || c == '_') return true;
        if (c < 128) return false;
        return Character.isLetter(c);
    }

    /**
	 * Parses a Name.
	 * <p><code>{05} Name {06.1 .3} {17.1} {28.2} {40.1} {42.1} {44.1}
	 * {45.2} {48.1} {51.6} {52.2} {53.2} {58.3 .6} {59.1 .3 .4 .6}
	 * {68.1} {71.2} {72.3}</code></p>
	 * @return null if not a name, or a String containing the
	 * name.
	 */
    public String Name() throws XmlException, IOException {
        String name = scanner.getName();
        return name;
    }

    /**
	 * Parses multiple names.  Not implemented.
	 * <p>
	 * <code>{06} Names (not part of any other rule - however,
	 * TokenizedType{56} refers to 'IDREFS' content, which is of type Names):
	 * </code>
	 * </p>
	 * @return null
	 */
    public List<String> Names() throws XmlException, IOException {
        List<String> l = new ArrayList<String>();
        while (true) {
            String s = Name();
            if (s == null) break;
            l.add(s);
            S();
        }
        return l;
    }

    /**
	 * Parse Nmtoken.
	 * <p><code>{07} Nmtoken (Name token) {08.1 .3} {59.2 .5}
	 * </code></p>
	 */
    public String Nmtoken() throws XmlException, IOException {
        int c = scanner.peek();
        if (!NameChar((char) c)) {
            return null;
        }
        int len = 0;
        attValue.reset();
        while (true) {
            c = scanner.read();
            if (!NameChar((char) c)) {
                if (c > 0) scanner.unread(c);
                return attValue.toString();
            }
            attValue.write((char) c);
            if (len++ > XmlReaderPrefs.MAX_NAME_LEN) {
                throw new XmlException("Exceeded MAX_NAME_LEN, read to " + attValue);
            }
        }
    }

    /**
	 * Parse Nmtokens.
	 * <p><code>{08} Nmtokens (Name tokens) 
	 * (not part of any other rule - however,
	 * TokenizedType{56} refers to 'NMTOKENS' content):
	 * </code></p>
	 * @return a list of String instances
	 */
    public List<String> Nmtokens() throws XmlException, IOException {
        List<String> l = new ArrayList<String>();
        while (true) {
            String s = Nmtoken();
            if (s == null) break;
            l.add(s);
            S();
        }
        return l;
    }

    /**
	 * Parse Entity Value.  Calls the same code as AttValue(), though
	 * it parses PEReference as well as References.
	 * <p><code>{09} EntityValue {73.1} {74.1}
	 * </code></p>
	 * @return entity value without quotes, or null if not an entity
	 */
    public String EntityValue() throws IOException, XmlException {
        int q = scanner.peek();
        if (q != '"' && q != '\'' && q != '%') throw new XmlException("Expected EntityValue quote or %, got " + (char) q);
        if (q == '%') {
            Entity e = PEReference();
            return e.resolveAll(this);
        }
        return AttValue(true);
    }

    /** 
	 * @param resolvePE true to resolve and append parameter entities
	 * @see #AttValue()
	 */
    private String AttValue(boolean resolvePE) throws IOException, XmlException {
        int q = scanner.read();
        if (q != '"' && q != '\'') throw new XmlException("Expected AttValue quote, got " + (char) q);
        attValue.reset();
        int c;
        while (true) {
            c = scanner.peek();
            if (c == -1) throw new XmlException("EOF in AttValue");
            if (c == '>' && !resolvePE) throw new XmlException("Must not have > in AttValue");
            if (c == '&') {
                if (CharRef()) {
                    attValue.write(scanner.read());
                    continue;
                } else if (!resolvePE) {
                    Entity ent = Reference();
                    if (ent != null) {
                        if (ent.isExternal()) throw new XmlException("No external entity in att value");
                        attValue.write(ent.resolveAll(this));
                    } else {
                        attValue.write((char) scanner.read());
                    }
                    continue;
                } else {
                    checkEntityReference(attValue);
                }
            }
            if (c == '%' && resolvePE) {
                Entity e = PEReference();
                attValue.write(e.resolveAll(this));
                continue;
            }
            c = scanner.read();
            if (q == c) return attValue.toString();
            attValue.write(c);
            if (attValue.size() > XmlReaderPrefs.MAX_ATTRIBUTE_LEN) {
                throw new XmlException("Exceeded MAX_ATTRIBUTE_LEN, read to " + attValue);
            }
        }
    }

    /**
	 * Parse attribute value.
	 * <p><code>{10} AttValue (Attribute Value) {41.1}
	 * {60.2}</code></p>
	 * @return attribute value
	 * @throws XmlException if bad attribute value
	 */
    public String AttValue() throws IOException, XmlException {
        return AttValue(false);
    }

    /**
	 * Parse system literal value.
	 * <p><code>{11} SystemLiteral {75.2 .6}</code></p>
	 * @return literal value
	 * @throws XmlException if bad system literal
	 */
    public String SystemLiteral() throws IOException, XmlException {
        int q = scanner.read();
        if (q != '"' && q != '\'') throw new XmlException("Expected SystemLiteral quote, got " + q);
        attValue.reset();
        int c;
        int len = 0;
        while (true) {
            c = scanner.read();
            if (q == c) return attValue.toString();
            if (len++ > XmlReaderPrefs.MAX_SYSTEM_LITERAL_LEN) throw new XmlException("Exceeded MAX_SYSTEM_LITERAL_LEN, read to " + attValue);
            if (c == -1) {
                throw new XmlException("Expecting closing quote, read to " + attValue);
            }
            attValue.write((char) c);
        }
    }

    /**
	 * Parse a public identifier value.
	 * <p><code>{12} PubidLiteral {75.4 83.2}</code></p>
	 * @return public identifier value
	 * @throws XmlException if bad public literal
	 */
    public String PubidLiteral() throws IOException, XmlException {
        String s = SystemLiteral();
        for (int i = 0; i < s.length(); i++) {
            if (!PubidChar(s.charAt(i))) throw new XmlException("Bad PubidChar in " + s);
        }
        return s;
    }

    /**
	 * Check if valid PubidChar.
	 * <p><code>{13} PubidChar {12.1 .2}</code></p>
	 * @return is valid Pubid character
	 */
    public static boolean PubidChar(char c) {
        if (c == '\"' || c == '&' || c == '\'' || c == '<' || c == '>') return false;
        if (c >= ' ' && c <= 'Z') return true;
        if (c >= 'a' && c <= 'z') return true;
        if (c == '_') return true;
        return false;
    }

    /**
	 * Parse char data, until &lt; or &amp; character.
	 * <p><code>{14} CharData {43.2}</code></p>
	 * @param w writer to copy CharData to
	 * @throws XmlException if bad character data
	 */
    public void CharData(Writer w) throws IOException, XmlException {
        scanner.copyUntil(w, '<', '&');
    }

    /**
	 * Parse comment.
	 * <p><code>{15} Comment {27.1} {29.6} {43.6}</code></p>
	 * @throws XmlException if premature EOF or bad comment data
	 * @param skip if true, skips over a comment;  if false,
	 * returns a new Comment instance
	 * @return null if comment was not found.
	 */
    public Comment comment(boolean skip) throws IOException, XmlException {
        if (!matches(XmlEvent.COMMENT, 4)) return null;
        Comment c = null;
        if (skip) {
            copyUntil(NullWriter.getInstance(), XmlTags.COMMENT_DASH);
        } else {
            c = new Comment();
            copyUntil(c.getWriter(), XmlTags.COMMENT_DASH);
        }
        if (scanner.read() != '>') throw new XmlException("Cannot have -- in comment");
        return c;
    }

    /**
	 * Parse processing instruction.
	 * <p><code>{16} PI (Processing Instruction) {27.1} {29.5}
	 * {43.5}</code></p>
	 * @return false, if PI was not found.
	 * @throws XmlException if premature EOF or bad PI data
	 * @param skip false to return this processing instruction as
	 * as PI;  if true, skips this processing instruction
	 * and returns null
	 */
    public PI pi(boolean skip) throws IOException, XmlException {
        if (!matches(XmlEvent.PI, 2)) return null;
        String target = PITarget();
        S();
        if (skip) {
            copyUntil(NullWriter.getInstance(), XmlTags.PI_END);
        } else {
            PI pi = new PI(target);
            copyUntil(pi.getWriter(), XmlTags.PI_END);
            return pi;
        }
        return null;
    }

    /**
	 * Parse processing instruction target.
	 * <p><code>{17} PITarget (Processing Instruction Target)
	 * {16.1}</code></p>
	 * @throws XmlException if premature EOF or bad target data
	 */
    public String PITarget() throws IOException, XmlException {
        String name = Name();
        if (name == null) throw new XmlException("Expected a Name following <? in Processing Instruction");
        if (name.toLowerCase().equals("xml")) throw new XmlException("PITarget cannot be xml");
        return name;
    }

    /**
	 * Parse CDSect.
	 * <p><code>{18} CDSect (Character Data Section) {43.4}</code></p>
	 * <p><code>{19} CDStart (Character Data Start) {18.1}</code></p>
	 * <p><code>{20} CData (Character Data) {18.1}</code></p>
	 * <p><code>{21} CDEnd (Character Data End) {18.3}</code></p>
	 * @param Writer write contents to this object
	 * @return null, if CDSect was not found.
	 * @throws XmlException if premature EOF or bad CDSect data
	 */
    public boolean CDSect(Writer w) throws IOException, XmlException {
        boolean match = matches(XmlTags.CDATA_BEGIN);
        if (!match) return false;
        copyUntil(w, XmlTags.CDATA_END);
        return true;
    }

    /**
	 * Parse prolog.
	 * <p><code>{22} Prolog {01.1}</code></p>
	 */
    public void Prolog(Document d) throws IOException, XmlException {
        XmlDecl();
        while (Misc(d)) ;
        boolean found = doctypedecl();
        if (found) while (Misc(d)) ;
    }

    /**
	 * Parse an XmlDecl or TextDecl.
	 * @param allowSA allow standalone
	 */
    boolean XmlDecl(boolean allowSA) throws IOException, XmlException {
        if (!matches(XmlTags.XML_DECL)) return false;
        VersionInfo();
        EncodingDecl();
        if (allowSA) SDDecl();
        S();
        if (!matches(XmlTags.PI_END)) throw new XmlException("Expected closing ?> for XmlDecl");
        return true;
    }

    /**
	 * Parse XML declaration.
	 * <p><code>{23} XmlDecl (XML Declaration) {22.1}</code></p>
	 * @return false, if XmlDecl was not found.
	 * @throws XmlException if premature EOF or bad PI data
	 */
    public boolean XmlDecl() throws IOException, XmlException {
        return XmlDecl(true);
    }

    /**
	 * Parse version info.
	 * <p><code>{24} VersionInfo {23.1}</code></p>
	 */
    public void VersionInfo() throws IOException, XmlException {
        if (!S()) throw new XmlException("Expected space after XML");
        if (!matches(XmlTags.VERSION_BEGIN)) {
            throw new XmlException("Expected 'version'");
        }
        Eq();
        String version = SystemLiteral();
        if (!VersionNum(version)) throw new XmlException("Version contains invalid characters: " + version);
    }

    /**
	 * Parse equals.
	 * <p><code>{25} Eq (Equals) {24.2} {32.2} {41.2} {80.2}</code></p>
	 */
    public void Eq() throws IOException, XmlException {
        S();
        if (scanner.read() != '=') throw new XmlException("Expected =");
        S();
    }

    /**
	 * Parse version number.
	 * <p><code>{26} VersionNum {24.3 .4}</code></p>
	 * @param c character to check
	 * @return if valid character
	 */
    public boolean VersionNum(String ver) throws IOException, XmlException {
        int len = ver.length();
        for (int i = 0; i < len; i++) {
            char c = ver.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '.')) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
	 * Parse misc stuff.
	 * <p><code>{27} Misc (Miscellaneous) {01.3} {22.2 .4}</code></p>
	 * @param d a document to store comments and processing instructions
	 * in; can be null if no comments or processing instructions are to
	 * be stored
	 * @return true if any miscellaneous text was found
	 * @see #comment
	 * @see #pi
	 * @see #S
	 */
    public boolean Misc(Document d) throws IOException, XmlException {
        if (S()) return true;
        Comment c = comment(false);
        if (c != null) {
            if (d != null) d.appendChild(c);
            return true;
        }
        PI pi = pi(false);
        if (pi != null) {
            if (d != null) d.appendChild(pi);
            return true;
        }
        return false;
    }

    /**
	 * Parse doctypedecl.
	 * <p><code>{28} doctypedecl {22.3}</code></p>
	 * @return true if doctypedecl exists 
	 */
    public boolean doctypedecl() throws IOException, XmlException {
        if (!matches(XmlTags.DOCTYPE_BEGIN)) return false;
        if (!S()) throw new XmlException("Expected space after doctypedecl.");
        String docname = Name();
        dtd.setName(docname);
        boolean space = S();
        Entity dtd = null;
        if (space) {
            dtd = ExternalID();
            if (dtd != null) S();
        }
        int c = scanner.peek();
        if (c == '[') {
            scanner.read();
            while (true) {
                S();
                if (markupdecl()) {
                    continue;
                }
                if (scanner.peek() == '%') {
                    peScan(PEReference());
                    continue;
                }
                c = scanner.read();
                if (c != ']') throw new XmlException("expected ] to end doctypedecl");
                break;
            }
            S();
        }
        c = scanner.read();
        if (c != '>') throw new XmlException("Expected > at end of doctypedecl, not " + (char) c);
        if (dtd != null) {
            resolveExtSubset(dtd);
        }
        return true;
    }

    /**
	 * Parse markup declaration.
	 * <p><code>{29} markupdecl {28.6} {31.1}</code></p>
	 * @return false if no element found
	 * @throws XmlException 
	 */
    public boolean markupdecl() throws IOException, XmlException {
        switch(scanner.peekEvent()) {
            case XmlEvent.ELEMENT_DECL:
                if (!elementdecl()) throw new XmlException("Expected elementdecl");
                break;
            case XmlEvent.ATTLIST_DECL:
                if (!AttlistDecl()) throw new XmlException("Expected AttlistDecl");
                break;
            case XmlEvent.ENTITY_DECL:
                if (!EntityDecl()) throw new XmlException("Expected EntityDecl");
                break;
            case XmlEvent.NOTATATION_DECL:
                if (!NotationDecl()) throw new XmlException("Expected NotationDecl");
                break;
            case XmlEvent.PI:
                if (pi(false) == null) throw new XmlException("Expected PI");
                break;
            case XmlEvent.COMMENT:
                if (comment(false) == null) throw new XmlException("Expected Comment");
                break;
            default:
                return false;
        }
        return true;
    }

    /**
	 * Parse an extSubset.
	 * <p><code>{30} extSubset: the Dtd file</code></p>
	 * @throws XmlException if data found in bad form
	 */
    public void extSubset() throws XmlException, IOException {
        TextDecl();
        extSubsetDecl();
    }

    /**
	 * Begins scanning using an parameter entity.
	 */
    private void peScan(Entity entity) throws IOException, XmlException {
        XmlScanner newScanner;
        XmlScanner oldScanner = scanner;
        if (entity.getValue() == null) {
            Reader r = resolver.resolve(entity.getSystemID());
            newScanner = createXmlScanner(r);
        } else {
            newScanner = new XmlScanner(entity.getValue());
        }
        scanner = newScanner;
        extSubsetDecl();
        if (hasMoreData()) throw new XmlException("Incomplete parameter entity content " + entity);
        scanner = oldScanner;
    }

    /**
	 * Parse external subset declaration.
	 * <p><code>{31} extSubsetDecl {30.2} {62.3} {79.2}</code></p>
	 * @return when done reading
	 * @throws XmlException 
	 */
    public void extSubsetDecl() throws IOException, XmlException {
        while (true) {
            S();
            switch(scanner.peekEvent()) {
                case XmlEvent.ELEMENT_DECL:
                case XmlEvent.ATTLIST_DECL:
                case XmlEvent.ENTITY_DECL:
                case XmlEvent.NOTATATION_DECL:
                case XmlEvent.PI:
                case XmlEvent.COMMENT:
                    markupdecl();
                    break;
                case XmlEvent.CONDITIONAL_SECT:
                    conditionalSect();
                    break;
                default:
                    if (scanner.peek() == '%') {
                        peScan(PEReference());
                    } else {
                        return;
                    }
            }
        }
    }

    /**
	 * Parse an standalone document declaration.
	 * <p><code>{32} SDDecl {23.3}</code></p>
	 * @throws XmlException if encoding incorrect
	 * @return yes, no, or null
	 */
    public String SDDecl() throws XmlException, IOException {
        boolean space = S();
        if (matches(XmlTags.STANDALONE_BEGIN)) {
            if (!space) throw new XmlException("Expected space before standalone declaration");
            Eq();
            String yesNo = AttValue();
            if (!yesNo.equals("yes") && !yesNo.equals("no")) throw new XmlException("Expected yes or no for standalone value");
            return yesNo;
        }
        return null;
    }

    /**
	 * Parse element.
	 * <p><code>{39} element {01.2} {43.1}</code></p>
	 * @return false if no element found
	 * @throws XmlException 
	 */
    public Element element() throws IOException, XmlException {
        Element e = STag();
        if (e == null) {
            return null;
        }
        if (e.isOpen()) {
            content(e);
            if (!ETag(e)) {
                throw new XmlException("Expected tag </" + e.getName() + ">");
            }
        }
        return e;
    }

    /**
	 * Creates and returns either a Element or ElementNS; also configures namespaces.
	 * @throws XmlException if something is wrong with the namespace
	 */
    private Element makeElement(NamespaceImpl namespace, List<Attribute> attr, boolean open) throws XmlException {
        UriMap m = scanner.getUriMap();
        boolean useNS = namespace.hasNamespace();
        if (attr != null) {
            for (int i = 0; i < attr.size(); i++) {
                Attribute a = attr.get(i);
                if (a.getPrefix() == XmlTags.XMLNS) {
                    String ln = a.getLocalName();
                    String v = a.getValue();
                    m.put(ln, v);
                }
                if (a.getName() == XmlTags.XMLNS) {
                    String v = a.getValue();
                    if (v.length() != 0) v = null;
                    namespace.setNamespaceURI(v);
                    useNS = true;
                }
            }
        }
        if (useNS) return new ElementNS(namespace, attr, open); else return new Element(namespace.getName(), attr, open);
    }

    /**
	 * Parse STag.  Also parses EmptyElemTag.
	 * <p><code>{40} STag {39.2}</code></p>
	 * <p><code>{44} EmptyElemTag {39.1}</code></p>
	 * @return Element, which will indicate open or closed.  
	 * @throws XmlException if bad STag
	 */
    public Element STag() throws IOException, XmlException {
        if (!matches(XmlEvent.STAG, 1)) return null;
        scanner.readNamespace(namespaceElement);
        ElementRule rule = dtd.getElementRule(namespaceElement.getName());
        if (rule != null) aruleState.clear();
        List<Attribute> attr = null;
        int c;
        while (true) {
            boolean space = S();
            c = scanner.read();
            if (c == '/') {
                c = scanner.read();
                if (c != '>') throw new XmlException("Expected > for empty element");
                if (rule != null) attr = rule.encounterEnd(aruleState, attr);
                return makeElement(namespaceElement, attr, false);
            }
            if (c == '>') {
                if (rule != null) attr = rule.encounterEnd(aruleState, attr);
                return makeElement(namespaceElement, attr, true);
            }
            scanner.unread(c);
            if (!space) throw new XmlException("Expected whitespace after name or attribute");
            Attribute a = Attribute();
            if (rule != null) rule.encounterAttribute(a, aruleState);
            if (attr == null) attr = new ArrayList<Attribute>();
            attr.add(a);
        }
    }

    /**
	 * Parse Attribute.  Returns either a regular
	 * {@link Attribute} or a {@link AttributeNS} depending
	 * on if namespace information is available or not.
	 * <p><code>{41} Attribute {40.3} {44.3}</code></p>
	 *
	 * @return an attribute
	 * @throws XmlException if no or an invalid attribute was found
	 */
    public Attribute Attribute() throws IOException, XmlException {
        scanner.readNamespace(namespaceAttr);
        if (namespaceAttr.isClear()) throw new XmlException("Expected Attribute name");
        Eq();
        String value = AttValue();
        if (namespaceAttr.hasNamespace()) return new AttributeNS(namespaceAttr, value); else return new Attribute(namespaceAttr.getName(), value);
    }

    /**
	 * Parse ETag. 
	 * <p><code>{42} ETag (End Tag) {39.4}</code></p>
	 * @return an element, if exists
	 */
    public Element ETag() throws IOException, XmlException {
        if (!matches(XmlEvent.ETAG, 2)) return null;
        String name = Name();
        if (name == null) throw new XmlException("Expected name for ETag");
        S();
        int c = scanner.read();
        if (c != '>') throw new XmlException("Expected > for ETag " + name);
        return new Element(name);
    }

    /**
	 * Parse ETag. 
	 * <p><code>{42} ETag (End Tag) {39.4}</code></p>
	 * @param match beginning element to match
	 * @return true, if exists
	 * @throws XmlException if etag does not match "match"
	 */
    public boolean ETag(Element match) throws IOException, XmlException {
        if (!matches(XmlEvent.ETAG, 2)) return false;
        String name = Name();
        if (name == null) throw new XmlException("Expected name for ETag");
        if (name != match.getName() && !name.equals(match.getName())) throw new XmlException("Expected </" + match.getName() + "> got </" + name + ">");
        S();
        int c = scanner.read();
        if (c != '>') throw new XmlException("Expected > for ETag " + name);
        return true;
    }

    /**
	 * Begins scanning using an entity that appears within a document.
	 */
    private void entityScan(Element e, Entity entity) throws IOException, XmlException {
        XmlScanner newScanner;
        XmlScanner oldScanner = scanner;
        boolean oldInEntityScan = inEntityScan;
        if (entity.getValue() == null) {
            Reader r = resolver.resolve(entity.getSystemID());
            newScanner = createXmlScanner(r);
        } else {
            newScanner = new XmlScanner(entity.getValue());
        }
        newScanner.setStringPool(oldScanner.getStringPool());
        if (entity.isResolving()) throw new XmlException("Circular entity evaluation for " + entity);
        entity.setResolving(true);
        inEntityScan = true;
        scanner = newScanner;
        content(e);
        if (hasMoreData()) throw new XmlException("Unmatched end tag in entity content " + entity);
        if (!oldInEntityScan) inEntityScan = false;
        scanner = oldScanner;
        entity.setResolving(false);
    }

    /**
	 * Parse content.
	 * <p><code>{43} content {39.3} {78.2}</code></p>
	 * @param e Element to add content to
	 * @throws XmlException if bad content
	 */
    public void content(Element e) throws IOException, XmlException {
        ElementRule rule = dtd.getElementRule(e.getName());
        ElementRuleState eruleState = null;
        boolean pcdata = true;
        if (rule != null) {
            pcdata = rule.isPCDataAllowed();
            if (inEntityScan) eruleState = eruleStack.state(); else eruleState = eruleStack.startElement();
            if (!inEntityScan) eruleState.clear();
        }
        Writer w = NullWriter.getInstance();
        CharacterData cd = null;
        whileLoop: while (true) {
            if (!pcdata) S();
            switch(scanner.peekEvent()) {
                case XmlEvent.ETAG:
                    if (cd != null) e.appendChild(cd);
                    break whileLoop;
                case XmlEvent.STAG:
                    if (cd != null) e.appendChild(cd);
                    Element child = element();
                    if (rule != null) rule.encounterElement(child, eruleState);
                    e.appendChild(child);
                    break;
                case XmlEvent.COMMENT:
                    if (cd != null) e.appendChild(cd);
                    Comment c = comment(false);
                    if (c != null) e.appendChild(c);
                    break;
                case XmlEvent.PI:
                    if (cd != null) e.appendChild(cd);
                    PI pi = pi(false);
                    if (pi != null) e.appendChild(pi);
                    break;
                case XmlEvent.CHARDATA:
                    if (!pcdata) throw new ElementRuleException("Not allowed to have PCDATA section: " + e, rule);
                    if (cd == null) {
                        cd = new CharacterData();
                        w = cd.getWriter();
                    }
                    CharData(w);
                    break;
                case XmlEvent.CDSECT:
                    if (!pcdata) throw new ElementRuleException("Not allowed to have PCDATA section: " + e, rule);
                    if (cd == null) {
                        cd = new CharacterData();
                        w = cd.getWriter();
                    }
                    if (!CDSect(w)) {
                        throw new XmlException("Bad CDSect tag found");
                    }
                    break;
                case XmlEvent.REFERENCE:
                    Entity entity = Reference();
                    if (entity != null) {
                        entityScan(e, entity);
                    } else {
                        if (!pcdata) throw new ElementRuleException("Not allowed to have PCDATA section: " + e, rule);
                        if (cd == null) {
                            cd = new CharacterData();
                            w = cd.getWriter();
                        }
                        w.write((char) scanner.read());
                    }
                    break;
                case XmlEvent.NONE:
                    throw new XmlException("Illegal content for element");
                case XmlEvent.EOD:
                    if (inEntityScan) {
                        if (cd != null) e.appendChild(cd);
                        break whileLoop;
                    }
                    throw new XmlException("EOF in scanning");
                default:
                    throw new XmlException("Unknown content for element " + e);
            }
        }
        if (rule != null) {
            if (!inEntityScan) {
                rule.encounterEnd(eruleState);
                eruleStack.endElement();
            }
        }
    }

    /**
	 * Parse element declaration.  Adds entity rule, if found.
	 * <p><code>{45} elementdecl {29.1}</code></p>
	 * @throws XmlException if bad element declaration or duplicate found
	 * @return false if an elementdecl was not found.
	 */
    public boolean elementdecl() throws IOException, XmlException {
        if (!matches(XmlTags.ELEMENT_DECL_BEGIN)) return false;
        if (!S()) throw new XmlException("Expected space after elementdecl");
        String name = Name();
        if (name == null) throw new XmlException("Expected name after elementdecl: " + this);
        if (!S()) throw new XmlException("Expected space after elementdecl name");
        ElementReq req = contentspec();
        S();
        int c = scanner.read();
        if (c != '>') throw new XmlException("Expected > at end of elementdecl");
        ElementRule rule = dtd.getElementRule(name);
        if (rule == null) {
            dtd.addElementRule(name, new ElementRule(req, null));
        } else {
            rule.setRootReq(req);
        }
        return true;
    }

    /**
	 * Parse content specification.
	 * <p><code>{46} contentspec (content specification)
	 * {45.4}</code></p>
	 * @throws XmlException if bad content specification
	 * @return ElementRule rule containing allowable elements
	 */
    public ElementReq contentspec() throws IOException, XmlException {
        ElementReq req;
        if (matches(XmlTags.CONTENTSPEC_EMPTY)) {
            req = new ElementReq();
        } else if (matches(XmlTags.CONTENTSPEC_ANY)) {
            req = new ElementReq();
            req.setANY();
        } else {
            req = Mixed();
            if (req == null) throw new XmlException("Expected Mixed or children in contentspec");
        }
        return req;
    }

    /**
	 * Parse children.
	 * <p><code>{47} children {46.2}</code></p>
	 * @throws XmlException if bad children
	 */
    public ElementReq children() throws IOException, XmlException {
        ElementReq req = choiceSeq(true);
        int c = scanner.read();
        if (c == '>' || isS(c)) {
            scanner.unread(c);
        } else if (c != '?' && c != '*' && c != '+') {
            throw new XmlException("Expected *, ?, or +");
        }
        req.setRepeating(c);
        return req;
    }

    /**
	 * Parse content particle.
	 * <p><code>{48} cp {49.2 .5} {50.2 .5}</code></p>
	 * @throws XmlException if bad children
	 */
    public ElementReq cp() throws IOException, XmlException {
        ElementReq req;
        String name = Name();
        if (name != null) {
            req = new ElementReq(name);
        } else {
            req = choiceSeq();
        }
        int c = scanner.peek();
        if (c == '?' || c == '*' || c == '+') scanner.read();
        req.setRepeating(c);
        return req;
    }

    /**
	 * Parse choice or sequence.
	 * <p><code>{49} choice {47.1} {48.2}</code></p>
	 * <p><code>{50} seq (sequence) {47.2} {48.3}</code></p>
	 * @throws XmlException if bad children
	 */
    public ElementReq choiceSeq() throws IOException, XmlException {
        return choiceSeq(false);
    }

    private ElementReq choiceSeq(boolean initial) throws IOException, XmlException {
        boolean isSeq = false;
        boolean isChoice = false;
        ElementReq req = new ElementReq();
        if (!initial) {
            if (!matches(XmlTags.PAREN_BEGIN)) throw new XmlException("Expected ( in choiceSeq" + this);
        }
        while (true) {
            S();
            req.add(cp());
            S();
            int c = scanner.read();
            if (c == ',') {
                if (isChoice) throw new XmlException("Expect | not , in choice");
                isSeq = true;
                continue;
            }
            if (c == '|') {
                if (isSeq) throw new XmlException("Expect , not | in sequence");
                isChoice = true;
                continue;
            }
            if (c == ')') {
                if (isSeq) req.setSequence(); else req.setChoice();
                return req;
            }
            throw new XmlException("Expect ) to end choiceSeq");
        }
    }

    void fillMixed(ElementReq req) throws IOException, XmlException {
        while (true) {
            S();
            if (unreadPEReference()) S();
            if (matches(XmlTags.PAREN_END_STAR)) {
                req.setChoice();
                req.setStar();
                return;
            }
            if (matches(XmlTags.PAREN_END)) {
                if (req.size() > 0) throw new XmlException("Expected )* not )");
                return;
            }
            if (!matches(XmlTags.BAR)) throw new XmlException("Expected | or )*");
            S();
            String name = Name();
            if (name == null) throw new XmlException("Expected element name");
            req.add(new ElementReq(name));
        }
    }

    /**
	 * Parse content specification's mixed or children content.
	 * <p><code>{51} Mixed (mixed content) {46.1}</code></p>
	 * <p><code>{47} children {46.2}</code></p>
	 * @throws XmlException if bad specification
	 * @return null if not a specification or rule containing allowable elements
	 */
    public ElementReq Mixed() throws IOException, XmlException {
        if (!matches(XmlTags.PAREN_BEGIN)) throw new XmlException("Expected (");
        S();
        if (matches(XmlTags.PCDATA_TAG)) {
            ElementReq req = new ElementReq();
            req.setPCDATA();
            fillMixed(req);
            return req;
        } else {
            return children();
        }
    }

    /**
	 * Parse attribute declaration.  Affects entity rule, if found.
	 * <p><code>{52} AttlistDecl {29.2}</code></p>
	 * @throws XmlException if bad element declaration
	 * @return false if an attribute declaration was not found.
	 */
    public boolean AttlistDecl() throws IOException, XmlException {
        if (!matches(XmlTags.ATTLIST_DECL_BEGIN)) return false;
        boolean space = S();
        if (unreadPEReference()) {
            space = S();
        }
        if (!space) throw new XmlException("Expected space after AttlistDecl");
        String name = Name();
        if (name == null) throw new XmlException("Expected name after AttlistDecl");
        ElementRule rule = dtd.getElementRule(name);
        if (rule == null) {
            rule = new ElementRule(null);
            dtd.addElementRule(name, rule);
        }
        while (true) {
            AttDef(rule);
            int c = scanner.peek();
            if (c == '>') break;
        }
        scanner.read();
        return true;
    }

    /**
	 * Parse attribute declaration.
	 * <p><code>{53} AttDef (Attribute Definition) {52.4}</code></p>
	 * @param erule a rule in which to store the name to AttributeRule mappings
	 * @throws XmlException if bad element declaration
	 */
    public void AttDef(ElementRule erule) throws IOException, XmlException {
        boolean space = S();
        if (unreadPEReference()) space = S();
        String name = Name();
        if (name == null) {
            return;
        }
        if (space == false) throw new XmlException("Expected space before AttDef name");
        if (!S()) throw new XmlException("Expected space after AttDef name");
        AttributeRule arule = AttType();
        arule.setName(name);
        unreadPEReference();
        if (!S()) throw new XmlException("Expected space after AttDef name");
        DefaultDecl(arule);
        erule.addAttributeRule(arule);
    }

    /**
	 * Parse an attribute type.  Returns a new rule.
	 * <p><code>{53} AttType (Attribute Type) {53.4}</code></p>
	 * <p><code>{54} StringType {54.1}</code></p>
	 * <p><code>{55} TokenizedType {54.2}</code></p>
	 * <p><code>{56} EnumeratedType {54.3}</code></p>
	 * @throws XmlException if unknown or bad attribute type
	 */
    public AttributeRule AttType() throws IOException, XmlException {
        String type = Name();
        if (type != null) {
            if (type.equals(XmlTags.ST_CDATA)) return new AttributeRule(AttributeValueType.CDATA);
            if (type.equals(XmlTags.TT_ID)) return new AttributeRule(AttributeValueType.ID);
            if (type.equals(XmlTags.TT_IDREF)) return new AttributeRule(AttributeValueType.IDREF);
            if (type.equals(XmlTags.TT_IDREFS)) return new AttributeRule(AttributeValueType.IDREFS);
            if (type.equals(XmlTags.TT_ENTITY)) return new AttributeRule(AttributeValueType.ENTITY);
            if (type.equals(XmlTags.TT_ENTITIES)) return new AttributeRule(AttributeValueType.ENTITIES);
            if (type.equals(XmlTags.TT_NMTOKEN)) return new AttributeRule(AttributeValueType.NMTOKEN);
            if (type.equals(XmlTags.TT_NMTOKENS)) return new AttributeRule(AttributeValueType.NMTOKENS);
            if (type.equals(XmlTags.ET_NOTATION)) {
                if (!S()) throw new XmlException("Expected space after NOTATION");
                return new AttributeRule(AttributeValueType.NOTATION, Enumeration());
            }
            throw new XmlException("Unknown AttType " + type);
        }
        return new AttributeRule(AttributeValueType.NAME_GROUP, Enumeration());
    }

    /**
	 * Parses an attribute enumeration.  Returns a Collection of String instances.
	 * <p><code>{59} Enumeration {57.2}</code></p>
	 * @throws XmlException if bad enumeration
	 */
    public Collection<String> Enumeration() throws IOException, XmlException {
        if (!matches(XmlTags.PAREN_BEGIN)) throw new XmlException("Expected ( in Enumeration");
        Collection<String> list = new ArrayList<String>();
        while (true) {
            S();
            String nmtoken = Nmtoken();
            list.add(nmtoken);
            S();
            int c = scanner.read();
            if (c == ')') break;
            if (c != '|') throw new XmlException("Expected | in Enumeration: " + (char) c);
        }
        return list;
    }

    /**
	 * Parses a default declaration.
	 * <p><code>{60} DefaultDecl {53.6}</code></p>
	 * @param rule sets either the fixed or default value for this object
	 * @throws XmlException if bad declaration
	 */
    public void DefaultDecl(AttributeRule rule) throws IOException, XmlException {
        unreadPEReference();
        if (matches(XmlTags.REQUIRED_BEGIN)) {
            rule.setRequired();
        } else if (matches(XmlTags.IMPLIED_BEGIN)) {
        } else if (matches(XmlTags.FIXED_BEGIN)) {
            S();
            String v = AttValue();
            rule.setFixed(v);
        } else {
            S();
            String v = AttValue();
            rule.setDefault(v);
        }
    }

    /**
	 * Parses a condition section.
	 * <p><code>{61} conditionalSect {31.2}</code></p>
	 * @throws XmlException if bad conditional section
	 */
    public void conditionalSect() throws IOException, XmlException {
        if (!matches(XmlEvent.CONDITIONAL_SECT, 3)) return;
        S();
        unreadPEReference();
        S();
        String name = Name();
        if (name == null) throw new XmlException("Expected INCLUDE or IGNORE");
        S();
        if (scanner.read() != '[') throw new XmlException("Expected [ after " + name);
        if (name.equals(XmlTags.IGNORE_BEGIN)) ignoreSect(); else if (name.equals(XmlTags.INCLUDE_BEGIN)) includeSect(); else throw new XmlException("Expected INCLUDE or IGNORE, not " + name);
    }

    /**
	 * Parses an includeSect, past the &lt;![INCLUDE[ tag.
	 * <p><code>{62} includeSect {61.1}</code></p>
	 */
    public void includeSect() throws IOException, XmlException {
        extSubsetDecl();
        if (!matches(XmlTags.CDATA_END)) throw new XmlException("Expected ]]> at end of includeSect");
    }

    /**
	 * Parses an ignoreSect, past the &lt;![IGNORE[ tag.
	 * <p><code>{63} ignoreSect {61.2}</code></p>
	 */
    public void ignoreSect() throws IOException, XmlException {
        ignoreSectContents();
    }

    /**
	 * Parses ignore section contents.
	 * <p><code>{64} ignoreSectContents {63.3} {64.2} </code></p>
	 */
    public void ignoreSectContents() throws IOException, XmlException {
        while (true) {
            int c = scanner.skipUntil('<', ']');
            if (c == '<' && matches(XmlTags.CONDITIONAL_BEGIN)) {
                ignoreSectContents();
            }
            if (c == ']' && matches(XmlTags.CDATA_END)) {
                break;
            }
            if (c == -1) throw new XmlException("EOF in ignoreSectContents");
            scanner.read();
        }
    }

    /**
	 * Parses ignore contents.
	 * <p><code>{65} Ignore {64.1 .3} </code></p>
	 */
    public void Ignore() {
    }

    /**
	 * Parse character reference.  Once read, is put back on the stream.
	 * <p><code>{66} CharRef {67.2}</code></p>
	 * @throws XmlException if bad character reference
	 * @return true if a CharRef was found
	 */
    public boolean CharRef() throws IOException, XmlException {
        return scanner.charRef();
    }

    /**
	 * Checks a reference for valid syntax, and if valid appends
	 * it to the writer.
	 */
    private void checkEntityReference(Writer writer) throws IOException, XmlException {
        scanner.read();
        String name = Name();
        if (name == null) throw new XmlException("Expected name after & in entity");
        if (scanner.read() != ';') throw new XmlException("Expected ; after " + name + " in entity");
        writer.write('&');
        writer.write(name);
        writer.write(';');
    }

    /**
	 * Parse a reference, starting with &amp; character.
	 * <p><code>{67} Reference {09.2 .4} {10.1 .2} {43.3}</code></p>
	 * @return Entity value, if significant entity exists 
	 * @throws XmlException if bad reference
	 */
    public Entity Reference() throws IOException, XmlException {
        if (scanner.peek() != '&') return null;
        if (scanner.translateReference()) return null;
        if (CharRef()) return null;
        scanner.read();
        String name = Name();
        if (name == null) throw new XmlException("Expected name after &");
        if (scanner.read() != ';') throw new XmlException("Expected ; after " + name);
        Entity entity = dtd.getEntity(name);
        if (entity == null) throw new XmlException("Unknown entity " + name);
        return entity;
    }

    boolean unreadPEReference() throws IOException, XmlException {
        Entity e = PEReference();
        if (e != null) {
            scanner.unread(' ');
            scanner.unread(e.resolveAll(this));
            scanner.unread(' ');
            return true;
        }
        return false;
    }

    /**
	 * Parse an parameter entity reference.
	 * <p><code>{69} PEReference {09.1 .3} {28.7} {31.3}</code></p>
	 * @return Entity instance
	 * @throws XmlException if PEReference is bad
	 */
    public Entity PEReference() throws IOException, XmlException {
        if (scanner.peek() != '%') return null;
        scanner.read();
        String pe = Name();
        if (pe == null) throw new XmlException("Expected name after %");
        if (scanner.read() != ';') throw new XmlException("Expected ; after " + pe);
        Entity entity = dtd.getParameterEntity(pe);
        if (entity == null) throw new XmlException("Unknown parameter entity " + pe);
        return entity;
    }

    /**
	 * Parse an entity declaration.  Adds its value to the Dtd.
	 * <p><code>{70} EntityDecl {29.3}</code></p>
	 * @return false if not found
	 * @throws XmlException if entity decl is bad
	 */
    public boolean EntityDecl() throws IOException, XmlException {
        if (!matches(XmlTags.ENTITY_DECL_BEGIN)) return false;
        if (!S()) throw new XmlException("Expected space after <!ENTITY in entity declaration");
        String name = null;
        boolean pe = false;
        if (scanner.peek() == '%') {
            scanner.read();
            if (S()) {
                pe = true;
            } else {
                scanner.unread('%');
                unreadPEReference();
            }
        }
        name = Name();
        if (name == null) throw new XmlException("Expected Name in entity declaration");
        if (!S()) throw new XmlException("Expected space after name in entity declaration");
        if (pe) {
            Entity value = PEDef();
            dtd.addParameterEntity(name, value);
        } else {
            Entity value = EntityDef();
            dtd.addEntity(name, value);
        }
        S();
        if (scanner.read() != '>') throw new XmlException("Expected > after for entity declaration");
        return true;
    }

    /**
	 * Parse a general entity declaration.
	 * <p><code>{71} GEDecl {70.1}</code></p>
	 */
    public void GEDecl() throws IOException, XmlException {
        EntityDecl();
    }

    /**
	 * Parse an parameter entity declaration.
	 * <p><code>{72} PEDecl {70.2}</code></p>
	 */
    public void PEDecl() throws IOException, XmlException {
        EntityDecl();
    }

    /**
	 * Parse an entity definition.
	 * <p><code>{73} EntityDef {71.4}</code></p>
	 * @throws XmlException if entity definition is bad
	 * @return value of this entity
	 */
    public Entity EntityDef() throws IOException, XmlException {
        Entity entity = ExternalID();
        if (entity != null) {
            NDataDecl();
        } else {
            String value = EntityValue();
            entity = new Entity(value);
        }
        return entity;
    }

    /**
	 * Parse an parameter entity definition.
	 * <p><code>{74} PEDef {72.5}</code></p>
	 * @throws XmlException if parameter entity definition is bad
	 * @return value of this parameter entity
	 */
    public Entity PEDef() throws IOException, XmlException {
        Entity entity = ExternalID();
        if (entity == null) {
            String value = EntityValue();
            entity = new Entity(value);
        }
        return entity;
    }

    private Entity ExternalID(boolean reqSystemLiteral) throws IOException, XmlException {
        if (matches(XmlTags.PUBLIC_BEGIN)) {
            if (!S()) throw new XmlException("Expected space after PUBLIC");
            String pubid = PubidLiteral();
            Entity entity;
            if (S()) {
                String sys = SystemLiteral();
                entity = new Entity(pubid, sys);
            } else {
                if (reqSystemLiteral) {
                    throw new XmlException("Expected SystemLiteral");
                }
                entity = new Entity(pubid, null);
            }
            return entity;
        }
        if (matches(XmlTags.SYSTEM_BEGIN)) {
            if (!S()) throw new XmlException("Expected space after SYSTEM");
            String sys = SystemLiteral();
            Entity entity = new Entity(null, sys);
            return entity;
        }
        return null;
    }

    /**
	 * Parse an ExternalID.
	 * <p><code>{75} ExternalID {04.1} {05.1}</code></p>
	 * @return the external identifier, with public and system id's
	 */
    public Entity ExternalID() throws XmlException, IOException {
        return ExternalID(true);
    }

    /**
	 * Parse notational data declaration.
	 * <p><code>{76} NDataDecl {73.2}</code></p>
	 * @return name of the notation system if found
	 * @throws XmlException if NDATA found but bad name
	 */
    public String NDataDecl() throws XmlException, IOException {
        boolean spaced = S();
        if (matches(XmlTags.NDATA_BEGIN)) {
            if (!spaced) throw new XmlException("Expected space before NDATA");
            if (!S()) throw new XmlException("Expected space after NDATA");
            String name = Name();
            if (name == null) throw new XmlException("Expected name after NDATA");
            return name;
        }
        return null;
    }

    /**
	 * Parse XML text declaration.
	 * <p><code>{77} TextDecl (Text Declaration) {30.1} {78.1} {79.1}</code></p>
	 * @return false, if TextDecl was not found.
	 * @throws XmlException if premature EOF or bad PI data
	 */
    public boolean TextDecl() throws IOException, XmlException {
        return XmlDecl(false);
    }

    /**
	 * Parse an external parsed entity.
	 * <p><code>{79} extParsedEnt</code></p>
	 * @throws XmlException if data found in bad form
	 * @param e an element to add content to
	 */
    public void extParsedEnt(Element e) throws XmlException, IOException {
        TextDecl();
        inEntityScan = true;
        content(e);
        inEntityScan = false;
    }

    /**
	 * Parse an extPE.
	 * <p><code>{79} extPE</code></p>
	 * @throws XmlException if data found in bad form
	 */
    public void extPE() throws XmlException, IOException {
        TextDecl();
        extSubsetDecl();
    }

    /**
	 * Parse an encoding.
	 * <p><code>{80} EncodingDecl: {23.2} {77.2}</code></p>
	 * @throws XmlException if encoding incorrect
	 * @return the encoding or null
	 */
    public String EncodingDecl() throws XmlException, IOException {
        boolean space = S();
        if (matches(XmlTags.ENCODING_BEGIN)) {
            if (!space) throw new XmlException("Expected space before encoding declaration");
            Eq();
            String enc = AttValue(false);
            EncName(enc);
            return enc;
        } else {
            if (space) scanner.unread(' ');
        }
        return null;
    }

    /**
	 * Parse an encoding name.
	 * <p><code>{81} EncName: {80.3 .4}</code></p>
	 * @throws XmlException if encoding name is bad
	 * @return the encoding name
	 */
    public void EncName(String name) throws XmlException, IOException {
        int len = name.length();
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '-' || c == '_' || c == '.') {
                continue;
            }
            throw new XmlException("Encoding name invalid " + name);
        }
    }

    /**
	 * Parse notational declaration.  Adds its value to the Dtd.
	 * <p><code>{82} NotationDecl {29.4}</code></p>
	 * @return true if notation declaration found
	 * @throws XmlException if data found in bad form
	 */
    public boolean NotationDecl() throws XmlException, IOException {
        if (!matches(XmlTags.NOTATION_DECL_BEGIN)) {
            return false;
        }
        if (!S()) throw new XmlException("Expected space after NotationDecl");
        if (unreadPEReference()) S();
        String name = Name();
        if (name == null) throw new XmlException("Expected name after NotationDecl");
        if (!S()) throw new XmlException("Expected space after NotationDecl name");
        Entity entity = PublicID();
        Notation n = new Notation(entity.getPublicID(), entity.getSystemID());
        dtd.addNotation(name, n);
        S();
        if (scanner.read() != '>') throw new XmlException("Expected > at end of NotationDecl");
        return true;
    }

    /**
	 * Parse a PublicID.
	 * <p><code>{83} PublicID {82.5}</code></p>
	 * @return the public literal
	 */
    public Entity PublicID() throws XmlException, IOException {
        return ExternalID(false);
    }

    /**
	 * Checks if a character is a valid XML letter or not.
	 * <p><code>{84} Letter {04.1} {05.1}</code></p>
	 * @return true if is a XML letter
	 */
    public static boolean Letter(char l) {
        return Character.isLetter(l);
    }

    /**
	 * Attempts to match an event, if so, skips ahead n characters.
	 */
    private boolean matches(int event, int skip) throws XmlException, IOException {
        if (scanner.peekEvent() == event) {
            scanner.skip(skip);
            return true;
        }
        return false;
    }

    /**
	 * Retrives the next a.length characters from the input stream.  If
	 * these do not match the contents of the array, they are put back.
	 * If they do match, they are thrown away and true is returned.
	 * This is used to test for beginnings of tags and such.
	 * @param a characters to find
	 * @return true if matches
	 */
    boolean matches(final char a[]) throws IOException {
        int num = scanner.peek(cbuf, 0, a.length);
        if (num < a.length) return false;
        for (int i = 0; i < a.length; i++) if (cbuf[i] != a[i]) {
            return false;
        }
        scanner.skip(a.length);
        return true;
    }

    /**
	 * This method copies until it finds a pattern of characters in the
	 * input stream.  This pattern of characters is not copied.
	 * If EOF is found prematurely, an exception is raised.
	 * @param w output stream to copy to
	 * @param a character pattern to find
	 * @throws XmlException if premature EOF is found
	 */
    void copyUntil(Writer w, char[] a) throws IOException, XmlException {
        int num;
        int alen = a.length;
        int alen_ = a.length - 1;
        while (true) {
            scanner.copyUntil(w, a[0], a[0]);
            num = scanner.read(cbuf, 0, alen);
            if (num < alen) throw new XmlException("Premature EOF looking for " + new String(a));
            boolean match = true;
            for (int i = 1; i < alen; i++) {
                if (cbuf[i] != a[i]) {
                    match = false;
                }
            }
            if (!match) {
                w.write(a[0]);
                scanner.unread(cbuf, 1, alen_);
            } else {
                return;
            }
        }
    }

    /**
	 * Returns the ObjectPool for element rules.
	 */
    RuleStack getRuleStack() {
        return eruleStack;
    }

    /**
	 * Used internally to read in a system literal.
	 */
    private void resolveExtSubset(Entity entity) throws IOException, XmlException {
        try {
            Reader r = resolver.resolve(entity.getSystemID());
            XmlReader xr = new XmlReader(r, getDtd());
            xr.setResolver(resolver);
            xr.extSubset();
            if (xr.hasMoreData()) throw new XmlException("Unknown tags or data found in external subset");
            xr.close();
        } catch (XmlException r) {
            throw new ResolverException("Could not resolveExtSubset " + entity, r);
        }
    }

    /**
	 * Returns a string containing the next 16 characters to be read
	 * from the XmlScanner.  These characters are put back after
	 * reading.
	 * @return a string for debugging purposes
	 */
    @Override
    public String toString() {
        try {
            int num = scanner.peek(cbuf, 0, 16);
            if (num == -1) return "XmlReader [ EOF ]";
            String s = new String(cbuf, 0, num);
            return "XmlReader [" + s + "] (" + num + ") ";
        } catch (IOException ioe) {
            return super.toString() + " " + ioe.toString();
        }
    }
}
