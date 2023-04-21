package jaxlib.prefs;

import java.io.EOFException;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;
import java.util.ConcurrentModificationException;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import jaxlib.col.HashXMap;
import jaxlib.col.OrderedHashMap;
import jaxlib.col.XMap;
import jaxlib.col.XSet;
import jaxlib.io.file.Files;
import jaxlib.io.stream.BufferedXInputStream;
import jaxlib.io.stream.BufferedXReader;
import jaxlib.io.stream.CharBufferWriter;
import jaxlib.io.stream.InputStreamXReader;
import jaxlib.io.stream.OutputStreamXWriter;
import jaxlib.util.StringBuilders;

/**
 * The {@code PropertiesFile} class represents a persistent set of properties. The properties can be saved 
 * and loaded to streams. Each key and its corresponding value in the property map is a string.
 * <p>
 * This class behaves similar to the {@link java.util.Properties} class. It supports additional file 
 * formats and association of a comment with each property, as well as a file header and footer comment. 
 * Additionaly {@code PropertiesFile} keeps the order of properties like they were loaded from the source 
 * stream or added to the map. Thus users may modify the file using a text editor and the application may 
 * modify the file also without loosing comments.
 * </p><p>
 * Properties may be reordered using the methods provided by the list views of this map: 
 * {@link #keyList()}, {@link #entryList()}, {@link #valueList()}.
 * </p><p>
 * When properties are loaded from a stream then {@code PropertiesFile} automatically 
 * {@link String#intern() internalizes} the property keys, but neither the values nor the comments.
 * </p><p>
 * Unlike {@code java.util.Properties} instances of this class are not threadsafe.
 * </p>
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: PropertiesFile.java 1480 2005-11-23 17:46:17Z joerg_wassmer $
 */
public class PropertiesFile extends OrderedHashMap<String, String> implements Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    /** 
   * A table of hex digits 
   */
    static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
   * Used to wrap lines when saving plain properties files.
   */
    static final char[] splitChars = { ':', ';', ',', '|' };

    /**
   * Convert a nibble to a hex character
   * @param	nibble	the nibble to convert.
   */
    static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    static void writePropertyString(final Writer out, String s, int fromIndex, int toIndex, final boolean escapeSpace, final boolean canSplit, final int prefixLen, final boolean iniFileKey) throws IOException {
        if (!escapeSpace) {
            while (fromIndex < toIndex) {
                char c = s.charAt(fromIndex);
                if ((c == '\n') || (c == '\r') || !Character.isWhitespace(c)) break; else fromIndex++;
            }
            while (fromIndex < toIndex) {
                char c = s.charAt(toIndex - 1);
                if ((c == '\n') || (c == '\r') || !Character.isWhitespace(c)) break; else toIndex--;
            }
        }
        if (canSplit && (s.length() + prefixLen > 80)) {
            char splitChar = 0;
            int splitCount = 0;
            for (char c : PropertiesFile.splitChars) {
                int a = 0;
                for (int i = fromIndex; i < toIndex; i++) {
                    if (s.charAt(i) == c) a++;
                }
                if (a > splitCount) {
                    splitCount = a;
                    splitChar = c;
                }
            }
            s = s.substring(fromIndex, toIndex);
            toIndex -= fromIndex;
            fromIndex = 0;
            out.write("\\\n  ");
            while (true) {
                int nextIndex;
                if (splitChar > 0) {
                    nextIndex = s.indexOf(splitChar, fromIndex);
                    if (nextIndex <= fromIndex) nextIndex = Math.min(toIndex, fromIndex + 80); else nextIndex++;
                } else {
                    nextIndex = Math.min(toIndex, fromIndex + 80);
                }
                if (toIndex - nextIndex < 4) nextIndex = toIndex;
                while (nextIndex < toIndex) {
                    if (Character.isWhitespace(s.charAt(nextIndex))) nextIndex++; else break;
                }
                writePropertyString(out, s, fromIndex, nextIndex, escapeSpace, false, -1, iniFileKey);
                fromIndex = nextIndex;
                if (fromIndex == toIndex) {
                    break;
                } else {
                    out.write("\\\n  ");
                }
            }
        }
        while (fromIndex < toIndex) {
            int c = s.charAt(fromIndex++);
            if ((c > 61) && (c < 127)) {
                if (c == '\\') {
                    out.write('\\');
                    out.write('\\');
                    continue;
                } else if (iniFileKey && ((c == '[') || (c == ']'))) {
                    out.write('\\');
                    out.write(c);
                } else {
                    out.write(c);
                    continue;
                }
            } else {
                switch(c) {
                    case ' ':
                        if (escapeSpace) out.write('\\');
                        out.write(' ');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '=':
                    case ':':
                    case '#':
                    case '!':
                        out.write('\\');
                        out.write(c);
                        break;
                    default:
                        if ((c < 0x0020) || (c > 0x007e)) {
                            out.write('\\');
                            out.write('u');
                            out.write(toHex((c >> 12) & 0xF));
                            out.write(toHex((c >> 8) & 0xF));
                            out.write(toHex((c >> 4) & 0xF));
                            out.write(toHex(c & 0xF));
                        } else {
                            out.write(c);
                        }
                        break;
                }
            }
        }
    }

    private transient String footerComment;

    private transient String headerComment;

    /**
   * True while reading files, false otherwise.
   */
    transient boolean internKeys = false;

    /**
   * Creates a new {@code PropertiesFile} with capacity {@code 16} and load factor {@code 0.75}.
   *
   * @since JaXLib 1.0
   */
    public PropertiesFile() {
        super();
    }

    /**
   * Creates a new {@code PropertiesFile} with load factor {@code 0.75} and a capacity sufficient to hold
   * the mappings of the specified source map.
   * <p>
   * If the specified map is instance of {@code PropertiesFile} then new one will also contain the comments
   * of the source.
   * </p>
   *
   * @throws NullPointerException
   *  if the specified map is {@code null} or contains a {@code null} key or value.
   * @throws IllegalArgumentException
   *  if the specified map contains a key of zero length.
   * @throws ConcurrentModificationException
   *  if this or the specified map gets modified concurrently by another thread.
   *
   * @since JaXLib 1.0
   */
    public PropertiesFile(Map<? extends String, ? extends String> source) {
        super(source.size());
        putAll(source);
    }

    /**
   * Creates a new {@code PropertiesFile} with the specified initial capacity and load factor {@code 0.75}.
   *
   * @throws IllegalArgumentException
   *  if {@code initialCapacity < 0}.
   *
   * @since JaXLib 1.0
   */
    public PropertiesFile(int initialCapacity) {
        super(initialCapacity);
    }

    /**
   * Creates a new {@code PropertiesFile} with the specified initial capacity and load factor.
   *
   * @throws IllegalArgumentException
   *  if {@code (initialCapacity < 0) || !(loadFactor > 0)}.
   *
   * @since JaXLib 1.0
   */
    public PropertiesFile(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        this.headerComment = (String) in.readObject();
        for (PropertiesFile.Entry e : propertySet()) e.comment = (String) in.readObject();
        this.footerComment = (String) in.readObject();
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.headerComment);
        for (PropertiesFile.Entry e : propertySet()) out.writeObject(e.comment);
        out.writeObject(this.footerComment);
    }

    @Override
    protected PropertiesFile.Entry createEntry(String key, int keyHashCode, String value) {
        return new PropertiesFile.Entry(this, key, keyHashCode, value);
    }

    /**
   * Returns the comment associated with the specified key.
   *
   * @return 
   *  the comment; {@code null} if there is none associated with the specified key or the comment is set
   *  to {@code null}.
   *
   * @param key
   *  the key to search for.
   *
   * @since JaXLib 1.0
   */
    public String getComment(String key) {
        PropertiesFile.Entry e = (PropertiesFile.Entry) super.getEntry(key);
        return (e != null) ? e.getComment() : null;
    }

    /**
   * Returns the entry associated with the specified key.
   *
   * @return 
   *  the entry; {@code null} if there is none associated with the specified key.
   *
   * @param key
   *  the key to search for.
   *
   * @since JaXLib 1.0
   */
    public PropertiesFile.Entry getEntry(String key) {
        return (PropertiesFile.Entry) super.getEntry(key);
    }

    /**
   * The comment in the file footer. May be {@code null}.
   *
   * @since JaXLib 1.0
   */
    public String getFooterComment() {
        return this.footerComment;
    }

    /**
   * The comment in the file header. May be {@code null}.
   *
   * @since JaXLib 1.0
   */
    public String getHeaderComment() {
        return this.headerComment;
    }

    /**
   * Associates the specified value with the specified key and comment.
   *
   * @return
   *  the value associated with the specified key prior to this call.
   *
   * @param key
   *  the key to associates the value with.
   * @param value
   *  the value to associates with the key.
   * @param comment
   *  the optional comment for the entry.
   *
   * @throws NullPointerException
   *  if the specified key or the value is {@code null}.
   * @throws IllegalArgumentException
   *  if the specified key is of zero length.
   *
   * @since JaXLib 1.0
   */
    public String put(String key, String value, String comment) {
        value = super.put(key, value);
        if ((value != null) || (comment != null)) {
            PropertiesFile.Entry e = (PropertiesFile.Entry) super.getEntry(key);
            if (e != null) {
                e.setComment(comment);
                return value;
            } else {
                throw new ConcurrentModificationException();
            }
        }
        return value;
    }

    public String putIfAbsent(String key, String value, String comment) {
        value = super.putIfAbsent(key, value);
        if ((value != null) && (comment != null)) {
            PropertiesFile.Entry e = (PropertiesFile.Entry) super.getEntry(key);
            if (e != null) {
                e.setComment(comment);
                return value;
            } else {
                throw new ConcurrentModificationException();
            }
        }
        return value;
    }

    /**
   * This method works similar to {@link XMap#putAll(Map) Map.putAll(src)} but also stores the comments if
   * the specified map is instance of {@code PropertiesFile}.
   *
   * @throws NullPointerException
   *  if the specified map is {@code null} or contains a {@code null} key or value.
   * @throws IllegalArgumentException
   *  if the specified map contains a key of zero length.
   * @throws ConcurrentModificationException
   *  if this or the specified map gets modified concurrently by another thread.
   *
   * @since JDK 1.2, JaXLib 1.0
   */
    @Override
    public void putAll(Map<? extends String, ? extends String> src) {
        if (src == this) {
            return;
        } else if (src instanceof PropertiesFile) {
            PropertiesFile sp = (PropertiesFile) src;
            if (isEmpty()) {
                super.putAll(sp);
                Iterator<? extends PropertiesFile.Entry> itSrc = sp.propertySet().iterator();
                Iterator<? extends PropertiesFile.Entry> itDst = propertySet().iterator();
                try {
                    while (itSrc.hasNext()) itDst.next().setComment(itSrc.next().getComment());
                } catch (NoSuchElementException ex) {
                    throw (ConcurrentModificationException) new ConcurrentModificationException().initCause(ex);
                }
            } else {
                for (PropertiesFile.Entry e : sp.propertySet()) put(e.getKey(), e.getValue(), e.getComment());
            }
        } else {
            super.putAll(src);
        }
    }

    /**
   * Returns the set of properties in this map.
   * The returned set is the same instance as returned by {@link #entrySet()}.
   *
   * @since JaXLib 1.0
   */
    public XSet<? extends PropertiesFile.Entry> propertySet() {
        return (XSet<? extends PropertiesFile.Entry>) (XSet<? extends Map.Entry<String, String>>) super.entrySet();
    }

    /**
   * Set all comments to {@code null}.
   *
   * @since JaXLib 1.0
   */
    public void removeComments() {
        for (PropertiesFile.Entry e : (Set<PropertiesFile.Entry>) (Set) entrySet()) e.setComment(null);
    }

    /**
   * Set the comment for the file footer. 
   *
   * @param comment
   *  the comment; may be {@code null}.
   *
   * @since JaXLib 1.0
   */
    public void setFooterComment(String comment) {
        this.footerComment = comment;
    }

    /**
   * Set the comment for the file header. 
   *
   * @param comment
   *  the comment; may be {@code null}.
   *
   * @since JaXLib 1.0
   */
    public void setHeaderComment(String comment) {
        this.headerComment = comment;
    }

    @Override
    public String toString() {
        CharBufferWriter out = new CharBufferWriter(size() * 32);
        try {
            writeProperties(out, true);
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
        return out.toString();
    }

    public PropertiesFile.Format read(final File file, final boolean includeComments) throws IOException, InvalidPropertiesFormatException {
        final InputStream in = Files.createLockedInputStream(file, 0, TimeUnit.NANOSECONDS);
        try {
            final PropertiesFile.Format format = read(in, includeComments);
            in.close();
            return format;
        } catch (IOException ex) {
            try {
                in.close();
            } finally {
                throw ex;
            }
        }
    }

    /**
   * Reads properties from the specified stream and puts them into this map.
   * Detects and returns the file format automatically.
   * <p>
   * If the file format is not XML then this method uses the {@code ISO-8859-1} 
   * {@link java.nio.charset.Charset}.
   * </p>
   *
   * @return
   *  the detected file format.
   *
   * @param in
   *  the stream to read from.
   * @param includeComments
   *  If {@code true} this call will read all comments from the specified stream. If {@code false} then
   *  all comments will be skipped.
   *
   * @throws IOException
   *  if an I/O error occurs.
   * @throws InvalidPropertiesFormatException
   *  if the specified stream contains a malformed property file.
   * @throws NullPointerException
   *  if {@code in == null}.
   *
   * @since JaXLib 1.0
   */
    public PropertiesFile.Format read(InputStream in, boolean includeComments) throws IOException, InvalidPropertiesFormatException {
        for (int c; true; ) {
            c = in.read();
            if (c < 0) return Format.PROPERTIES; else if (c == '<') return readXML(c, in, includeComments); else if ((c == '#') || (c == '!') || !Character.isWhitespace((char) c)) return readProperties0(c, in, includeComments);
        }
    }

    /**
   * Reads properties from the specified stream and puts them into this map.
   * The file must be {@link Format#PROPERTIES plain text} formatted.
   * <p>
   * Please note this class provides no method to read another format from a {@link Reader} instance. This
   * is because it is a bad idea to read XML from character streams since character decoding may not be
   * handled correctly.
   * </p>
   *
   * @param in
   *  the stream to read from.
   * @param includeComments
   *  If {@code true} this call will read all comments from the specified stream. If {@code false} then
   *  all comments will be skipped.
   *
   * @throws IOException
   *  if an I/O error occurs.
   * @throws InvalidPropertiesFormatException
   *  if the specified stream contains a malformed property file.
   * @throws NullPointerException
   *  if {@code in == null}.
   *
   * @since JaXLib 1.0
   */
    public void readProperties(Reader in, boolean includeComments) throws IOException {
        this.internKeys = true;
        try {
            new PropertiesInput(new BufferedXReader(in, 2048), !includeComments).run();
        } finally {
            this.internKeys = false;
        }
    }

    private PropertiesFile.Format readProperties0(int firstChar, InputStream in, boolean includeComments) throws IOException {
        InputStreamXReader reader0 = new InputStreamXReader(in);
        reader0.setByteBufferCapacity(2048);
        BufferedXReader reader = new BufferedXReader(reader0, 2048);
        if (firstChar >= 0) reader.unread(firstChar);
        this.internKeys = true;
        try {
            new PropertiesInput(reader, !includeComments).run();
            return Format.PROPERTIES;
        } finally {
            this.internKeys = false;
        }
    }

    private PropertiesFile.Format readXML(int firstChar, InputStream in, boolean includeComments) throws IOException {
        BufferedXInputStream in2 = (in instanceof BufferedXInputStream) ? (BufferedXInputStream) in : new BufferedXInputStream(in, 2048);
        in2.unread(firstChar);
        this.internKeys = true;
        try {
            PropertiesFile.XMLInput xmlInput = new XMLInput(in2, !includeComments);
            in2 = null;
            in = null;
            return xmlInput.run();
        } finally {
            this.internKeys = false;
        }
    }

    /**
   * Writes this properties file to the specified stream.
   * This method uses the {@code ISO-8859-1} charset for the {@link Format#PROPERTIES plain text format} and
   * the {@code UTF-8} charset for XML formats.
   *
   * @param out
   *  the destination stream.
   * @param format
   *  the file format to use.
   * @param includeComments
   *  {@code True} to include all comments. {@code False} to exclude all comments.
   *
   * @throws IOException
   *  if an I/O error occurs.
   * @throws NullPointerException
   *  if {@code (out == null) || (format == null)}.
   *
   * @since JaXLib 1.0
   */
    public void write(OutputStream out, PropertiesFile.Format format, boolean includeComments) throws IOException {
        switch(format) {
            case PROPERTIES:
                writeProperties(out, includeComments);
                break;
            case XML_PROPERTIES:
                writeXMLProperties(out, includeComments);
                break;
            case XML_PREFERENCES_MAP:
                writeXMLPreferences(out, includeComments);
                break;
            default:
                throw new AssertionError(format);
        }
    }

    private void writeProperties(OutputStream out, boolean includeComments) throws IOException {
        OutputStreamXWriter writer = new OutputStreamXWriter(out, "ISO-8859-1");
        writer.setByteBufferCapacity(2048);
        writer.setCharBufferCapacity(2048);
        writeProperties(writer, includeComments);
        writer.closeInstance();
    }

    /**
   * Writes this properties file to the specified stream.
   * This method uses the {@link Format#PROPERTIES plain text format}.
   * <p>
   * Please note this class provides no method to write another format to a {@link Writer} instance. This
   * is because it is a bad idea to write XML to character streams since character encoding may not be
   * handled correctly.
   * </p>
   *
   * @param out
   *  the destination stream.
   * @param includeComments
   *  {@code True} to include all comments. {@code False} to exclude all comments.
   *
   * @throws IOException
   *  if an I/O error occurs.
   * @throws NullPointerException
   *  if {@code (out == null) || (format == null)}.
   *
   * @since JaXLib 1.0
   */
    public void writeProperties(Writer out, boolean includeComments) throws IOException {
        new PropertiesFile.PropertiesOutput(out, !includeComments).run();
    }

    private void writeXMLPreferences(OutputStream out, boolean includeComments) throws IOException {
        new PropertiesFile.XMLPreferencesOutput(out, !includeComments).run();
    }

    private void writeXMLProperties(OutputStream out, boolean includeComments) throws IOException {
        new PropertiesFile.XMLPropertiesOutput(out, !includeComments).run();
    }

    /**
   * Called by IniFile
   */
    final void writeIniFileProperties(Writer out, boolean includeComments, String sectionName) throws IOException {
        new PropertiesFile.PropertiesOutput(out, !includeComments, sectionName).run();
    }

    public static class Entry extends OrderedHashMap.Entry<String, String> {

        private static String checkKey(PropertiesFile map, String key) {
            if (key != null) {
                if (key.length() > 0) return map.internKeys ? key.intern() : key; else throw new IllegalArgumentException("empty key");
            } else {
                throw new NullPointerException("key");
            }
        }

        String comment;

        protected Entry(PropertiesFile map, String key, int hashCode, String value) {
            super(map, (key = checkKey(map, key)), hashCode, value);
            if (value == null) throw new NullPointerException("value");
        }

        public String getComment() {
            return this.comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        @Override
        public String setValue(String newValue) {
            if (newValue != null) return super.setValue(newValue); else throw new NullPointerException("newValue");
        }
    }

    /**
   * File formats supported by {@link PropertiesFile}.
   * Please note future versions of this enum may define additional formats.
   *
   * @since JaXlib 1.0
   */
    public static enum Format implements Serializable {

        PROPERTIES, XML_PROPERTIES, XML_PREFERENCES_MAP;

        /**
     * @since JaXLib 1.0
     */
        private static final long serialVersionUID = 1L;

        /**
     * Returns the common name suffix for files of this type.
     */
        public String getFilenameSuffix() {
            switch(this) {
                case PROPERTIES:
                    return ".properties";
                case XML_PROPERTIES:
                case XML_PREFERENCES_MAP:
                    return ".xml";
                default:
                    throw new AssertionError(this);
            }
        }
    }

    private final class PropertiesInput extends Object {

        private final BufferedXReader in;

        private final boolean quotes = false;

        private final boolean skipComments;

        private final StringBuilder sb = new StringBuilder(128);

        private int lineNumber = 0;

        private boolean keyHasValue;

        PropertiesInput(BufferedXReader in, boolean skipComments) {
            super();
            this.in = in;
            this.skipComments = skipComments;
        }

        private void afterBackslash() throws IOException {
            int c = read();
            switch(c) {
                case 'f':
                    this.sb.append('\f');
                    return;
                case 'n':
                    this.sb.append('\n');
                    return;
                case 'r':
                    this.sb.append('\r');
                    return;
                case 't':
                    this.sb.append('\t');
                    return;
                case 'u':
                    {
                        int value = 0;
                        for (int i = 0; i < 4; i++) {
                            c = read();
                            if ((c >= '0') && (c <= '9')) value = (value << 4) + c - '0'; else if ((c >= 'a') && (c <= 'f')) value = (value << 4) + 10 + c - 'a'; else if ((c >= 'A') && (c <= 'F')) value = (value << 4) + 10 + c - 'A'; else {
                                throw new InvalidPropertiesFormatException("Malformed \\uxxxx encoding at line " + this.lineNumber);
                            }
                        }
                        this.sb.append((char) value);
                    }
                    return;
                default:
                    {
                        if (Character.isWhitespace(c)) {
                            final int startLength = this.sb.length();
                            while (true) {
                                if ((c < 0) || (c == '\n')) {
                                    this.sb.setLength(startLength);
                                    skipWhitespaces();
                                    return;
                                } else if (Character.isWhitespace((char) c)) {
                                    this.sb.append((char) c);
                                } else {
                                    this.in.unread(c);
                                    return;
                                }
                                c = read();
                            }
                        } else {
                            this.sb.append((char) c);
                        }
                    }
                    return;
            }
        }

        private String nextComment() throws IOException {
            this.sb.setLength(0);
            skipWhitespaces();
            int c = read();
            if ((c == '#') || (c == '!')) {
                READ: while (true) {
                    c = read();
                    switch(c) {
                        case -1:
                            break READ;
                        case '\n':
                            {
                                if (!this.skipComments) {
                                    if (!StringBuilders.endsWith(this.sb, '\n')) StringBuilders.trimRight(this.sb);
                                    this.sb.append('\n');
                                }
                                skipSpaces();
                                c = read();
                                if ((c == '#') || (c == '!')) {
                                    continue READ;
                                } else if (c == '\n') {
                                    this.in.unread(c);
                                    continue READ;
                                } else {
                                    if (c >= 0) this.in.unread(c);
                                    break READ;
                                }
                            }
                        default:
                            if (!this.skipComments) this.sb.append((char) c);
                            continue READ;
                    }
                }
                if (this.skipComments) {
                    return null;
                } else {
                    StringBuilders.trimRight(this.sb);
                    return (this.sb.length() > 0) ? this.sb.toString() : "";
                }
            } else {
                if (c >= 0) this.in.unread(c);
                return null;
            }
        }

        private String nextKey() throws IOException {
            this.keyHasValue = false;
            skipWhitespaces();
            this.sb.setLength(0);
            int min = -1;
            READ: while (true) {
                int c = read();
                switch(c) {
                    case -1:
                        {
                            StringBuilders.trimRight(this.sb);
                            if (this.sb.length() == 0) return null; else break READ;
                        }
                    case '\n':
                        skipWhitespaces();
                        break READ;
                    case '=':
                    case ':':
                        this.keyHasValue = true;
                        break READ;
                    case '\\':
                        afterBackslash();
                        min = this.sb.length();
                        continue READ;
                    default:
                        {
                            if (Character.isWhitespace((char) c)) {
                                while (true) {
                                    c = read();
                                    switch(c) {
                                        case -1:
                                            break READ;
                                        case '\n':
                                            break READ;
                                        case '=':
                                        case ':':
                                            this.keyHasValue = true;
                                            break READ;
                                        default:
                                            if (!Character.isWhitespace((char) c)) {
                                                this.in.unread(c);
                                                this.keyHasValue = true;
                                                break READ;
                                            }
                                            break;
                                    }
                                }
                            } else {
                                this.sb.append((char) c);
                                continue READ;
                            }
                        }
                }
            }
            for (int i = this.sb.length(); --i > min; ) {
                if (Character.isWhitespace(this.sb.charAt(i))) this.sb.setLength(i); else break;
            }
            if (this.sb.length() > 0) return this.sb.toString(); else throw new InvalidPropertiesFormatException("Empty property name at line " + this.lineNumber);
        }

        private String nextValue() throws IOException {
            if (!this.keyHasValue) return "";
            this.sb.setLength(0);
            int min = -1;
            READ: while (true) {
                int c = read();
                switch(c) {
                    case -1:
                        break READ;
                    case '\n':
                        break READ;
                    case '\\':
                        afterBackslash();
                        min = this.sb.length();
                        break;
                    default:
                        if ((this.sb.length() > 0) || !Character.isWhitespace((char) c)) this.sb.append((char) c);
                        break;
                }
            }
            for (int i = this.sb.length(); --i > min; ) {
                if (Character.isWhitespace(this.sb.charAt(i))) this.sb.setLength(i); else break;
            }
            if (this.sb.length() > 0) return this.sb.toString(); else return "";
        }

        private int read() throws IOException {
            int c = this.in.read();
            if (c == '\n') {
                this.lineNumber++;
                return '\n';
            } else if (c == '\r') {
                this.lineNumber++;
                c = this.in.read();
                if ((c >= 0) && (c != '\n')) this.in.unread(c);
                return '\n';
            } else if (c < 0) {
                return -1;
            } else {
                return c;
            }
        }

        void run() throws IOException {
            boolean header = true;
            READ: while (true) {
                int c = read();
                if (c < 0) {
                    break;
                } else {
                    if (c != '\n') this.in.unread(c);
                    String comment = nextComment();
                    if (header) {
                        header = false;
                        if (!this.skipComments) PropertiesFile.this.setHeaderComment(comment);
                        comment = null;
                    }
                    String key = nextKey();
                    if (key == null) {
                        if (!this.skipComments) PropertiesFile.this.setFooterComment(comment);
                    } else {
                        String value = nextValue();
                        if (this.skipComments) PropertiesFile.this.put(key, value); else PropertiesFile.this.put(key, value, comment);
                    }
                }
            }
        }

        private void skipSpaces() throws IOException {
            while (true) {
                int c = read();
                if (c < 0) {
                    return;
                } else if ((c == '\n') || !Character.isWhitespace((char) c)) {
                    this.in.unread(c);
                    return;
                }
            }
        }

        private void skipWhitespaces() throws IOException {
            while (true) {
                int c = read();
                if (c < 0) {
                    return;
                } else if (!Character.isWhitespace((char) c)) {
                    this.in.unread(c);
                    return;
                }
            }
        }
    }

    final class PropertiesOutput extends Object {

        private final Writer out;

        private final boolean skipComments;

        private final String iniFileSectionName;

        PropertiesOutput(Writer out, boolean skipComments) {
            super();
            this.out = out;
            this.skipComments = skipComments;
            this.iniFileSectionName = null;
        }

        PropertiesOutput(Writer out, boolean skipComments, String iniFileSectionName) {
            super();
            this.out = out;
            this.skipComments = skipComments;
            this.iniFileSectionName = (iniFileSectionName == null) ? "null" : iniFileSectionName;
        }

        void run() throws IOException {
            writeln();
            if (this.skipComments) {
                if (this.iniFileSectionName != null) {
                    writeln();
                    write('[');
                    PropertiesFile.writePropertyString(this.out, this.iniFileSectionName, 0, this.iniFileSectionName.length(), true, false, 0, true);
                    write(']');
                    writeln();
                }
            } else {
                String comment = PropertiesFile.this.getHeaderComment();
                if (comment == null) {
                    if (this.iniFileSectionName != null) {
                        writeln();
                        write('[');
                        PropertiesFile.writePropertyString(this.out, this.iniFileSectionName, 0, this.iniFileSectionName.length(), true, false, 0, true);
                        write(']');
                        writeln();
                    }
                } else {
                    writeComment(comment);
                    if (this.iniFileSectionName == null) {
                        writeln();
                        writeln();
                        writeln();
                    } else {
                        write('[');
                        PropertiesFile.writePropertyString(this.out, this.iniFileSectionName, 0, this.iniFileSectionName.length(), true, false, 0, true);
                        write(']');
                        writeln();
                    }
                }
            }
            boolean hadComment = false;
            for (PropertiesFile.Entry e : (Set<PropertiesFile.Entry>) (Set) PropertiesFile.this.entrySet()) {
                String key = e.getKey();
                if (!this.skipComments) {
                    String comment = e.getComment();
                    if (hadComment || (comment != null)) {
                        writeln();
                        writeln();
                    }
                    if (comment == null) {
                        hadComment = false;
                    } else {
                        hadComment = true;
                        writeComment(comment);
                    }
                }
                writeString(key, 0, key.length(), true, false, -1);
                write(" = ");
                String val = e.getValue();
                writeString(val, 0, val.length(), false, true, key.length() + 3);
                writeln();
            }
            if (!this.skipComments) {
                String comment = PropertiesFile.this.getFooterComment();
                if (comment != null) writeComment(comment);
            }
            writeln();
        }

        private void write(final int c) throws IOException {
            this.out.write(c);
        }

        private void write(final String s) throws IOException {
            this.out.write(s);
        }

        private void writeComment(String s) throws IOException {
            int fromIndex = 0;
            int toIndex = s.length();
            write('#');
            while (fromIndex < toIndex) {
                int c = s.charAt(fromIndex++);
                if ((c == '\n') || (c == '\r')) {
                    if ((c == '\r') && (fromIndex < toIndex) && (s.charAt(fromIndex) == '\n')) fromIndex++;
                    while (fromIndex < toIndex) {
                        c = s.charAt(fromIndex);
                        if ((c == '\n') || (c == '\r') || !Character.isWhitespace(c)) break; else fromIndex++;
                    }
                    if (fromIndex < toIndex) {
                        writeln();
                        write('#');
                    }
                } else {
                    write(c);
                }
            }
            writeln();
        }

        private void writeln() throws IOException {
            this.out.write('\n');
        }

        private void writeString(final String s, final int fromIndex, final int toIndex, final boolean escapeSpace, final boolean canSplit, final int prefixLen) throws IOException {
            PropertiesFile.writePropertyString(this.out, s, fromIndex, toIndex, escapeSpace, canSplit, prefixLen, false);
        }
    }

    private final class XMLInput extends Object {

        final Document doc;

        final boolean skipComments;

        XMLInput(InputStream in, boolean skipComments) throws IOException {
            super();
            this.doc = XMLUtil.loadDocument(in, skipComments);
            this.skipComments = skipComments;
        }

        private Format getFormat() throws IOException {
            DocumentType docType = this.doc.getDoctype();
            if (docType == null) throw new InvalidPropertiesFormatException("XML file has no document type");
            String sid = docType.getSystemId();
            if (sid == null) throw new InvalidPropertiesFormatException("document type has no system id");
            Element root = this.doc.getDocumentElement();
            String rootName = root.getNodeName();
            if (sid.equals(XMLUtil.PROPS_DTD_URI)) {
                if (rootName.equals("properties")) return Format.XML_PROPERTIES;
            } else if (sid.equals(PreferencesUtil.PREFS_DTD_URI)) {
                if (rootName.equals("map")) return Format.XML_PREFERENCES_MAP;
            } else {
                throw new InvalidPropertiesFormatException("unsupported document type: " + sid);
            }
            throw new InvalidPropertiesFormatException("unsupported document root '" + rootName + "' for document type '" + sid + "'");
        }

        Format run() throws IOException {
            Format format = getFormat();
            switch(format) {
                case XML_PREFERENCES_MAP:
                    readXMLPreferences();
                    break;
                case XML_PROPERTIES:
                    readXMLProperties();
                    break;
                default:
                    throw new AssertionError(format);
            }
            return format;
        }

        /**
     * Extra method to avoid classloading.
     */
        private void readXMLPreferences() throws IOException {
            new XMLPreferencesReader().run();
        }

        /**
     * Extra method to avoid classloading.
     */
        private void readXMLProperties() throws IOException {
            new XMLPropertiesReader().run();
        }

        private final class XMLPreferencesReader extends Object {

            XMLPreferencesReader() {
                super();
            }

            private Element getMapElement() throws IOException {
                Element mapElement = XMLInput.this.doc.getDocumentElement();
                return mapElement;
            }

            private void parseHeaderComment() throws IOException {
                if (!XMLInput.this.skipComments) {
                    NodeList nodes = XMLInput.this.doc.getChildNodes();
                    for (int i = 0, hi = nodes.getLength(); i < hi; i++) {
                        Node node = nodes.item(i);
                        if (node instanceof Comment) {
                            String s = node.getNodeValue().trim();
                            if (s.length() > 0) {
                                PropertiesFile.this.setHeaderComment(s);
                                break;
                            }
                        }
                    }
                }
            }

            void run() throws IOException {
                Element mapElement = getMapElement();
                parseHeaderComment();
                NodeList nodes = mapElement.getChildNodes();
                Node comment = null;
                for (int i = 0, hi = nodes.getLength(); i < hi; i++) {
                    Node node = nodes.item(i);
                    if (node instanceof Comment) {
                        if (!XMLInput.this.skipComments) comment = node;
                    } else {
                        if (node.getNodeName().equals("entry")) {
                            Element entry = (Element) node;
                            String key = entry.getAttribute("key");
                            String val = entry.getAttribute("value");
                            String commentStr = ((XMLInput.this.skipComments) || (comment == null)) ? null : comment.getNodeValue();
                            if (XMLInput.this.skipComments) PropertiesFile.this.put(key, val); else PropertiesFile.this.put(key, val, commentStr);
                        }
                        comment = null;
                    }
                }
                if (comment != null) {
                    String s = comment.getNodeValue().trim();
                    if (s.length() > 0) PropertiesFile.this.setFooterComment(s);
                }
            }
        }

        private final class XMLPropertiesReader extends Object {

            XMLPropertiesReader() {
                super();
            }

            private Element getPropertiesElement() throws IOException {
                Element propertiesElement = XMLInput.this.doc.getDocumentElement();
                return propertiesElement;
            }

            private void parseHeaderComment() throws IOException {
                if (!XMLInput.this.skipComments) {
                    NodeList nodes = XMLInput.this.doc.getChildNodes();
                    for (int i = 0, hi = nodes.getLength(); i < hi; i++) {
                        Node node = nodes.item(i);
                        if (node instanceof Comment) {
                            String s = node.getNodeValue().trim();
                            if (s.length() > 0) {
                                PropertiesFile.this.setHeaderComment(s);
                                break;
                            }
                        }
                    }
                }
            }

            void run() throws IOException {
                Element propertiesElement = getPropertiesElement();
                parseHeaderComment();
                NodeList nodes = propertiesElement.getChildNodes();
                boolean haveHeaderComment = XMLInput.this.skipComments;
                Node comment = null;
                for (int i = 0, hi = nodes.getLength(); i < hi; i++) {
                    Node node = nodes.item(i);
                    if (node instanceof Comment) {
                        if (!XMLInput.this.skipComments) comment = node;
                    } else {
                        if (!haveHeaderComment && node.getNodeName().equals("comment")) {
                            String s = node.getNodeValue().trim();
                            if (s.length() > 0) {
                                PropertiesFile.this.setHeaderComment(s);
                                haveHeaderComment = true;
                            }
                        } else {
                            Element entry = (Element) node;
                            if (entry.hasAttribute("key")) {
                                Node n = entry.getFirstChild();
                                String val = (n == null) ? "" : n.getNodeValue();
                                String key = entry.getAttribute("key");
                                String commentStr = ((XMLInput.this.skipComments) || (comment == null)) ? null : comment.getNodeValue();
                                if (XMLInput.this.skipComments) PropertiesFile.this.put(key, val); else PropertiesFile.this.put(key, val, commentStr);
                            }
                        }
                        comment = null;
                    }
                }
                if (comment != null) {
                    String s = comment.getNodeValue().trim();
                    if (s.length() > 0) PropertiesFile.this.setFooterComment(s);
                }
            }
        }
    }

    private abstract class XMLOutput extends Object {

        final Document doc;

        final OutputStream out;

        final boolean skipComments;

        XMLOutput(OutputStream out, boolean skipComments) {
            super();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                this.doc = dbf.newDocumentBuilder().newDocument();
            } catch (ParserConfigurationException ex) {
                throw new Error(ex);
            }
            this.out = out;
            this.skipComments = skipComments;
        }

        abstract String getDtdUri();

        abstract void writeProperty(String key, String val, String comment);

        void generateDoc() {
            for (Map.Entry<?, ?> e : PropertiesFile.this.entrySet()) {
                Object keyObj = e.getKey();
                if (keyObj == null) continue;
                String key = keyObj.toString();
                Object valObj = e.getValue();
                String val = (valObj != null) ? valObj.toString() : "";
                String comment = this.skipComments ? null : PropertiesFile.this.getComment(key);
                writeProperty(key, val, comment);
            }
        }

        final void run() throws IOException {
            try {
                generateDoc();
            } catch (DOMException ex) {
                throw (IOException) new IOException(ex.getMessage()).initCause(ex);
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t;
            try {
                t = tf.newTransformer();
            } catch (TransformerConfigurationException ex) {
                throw new Error(ex);
            }
            t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, getDtdUri());
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.VERSION, "1.0");
            DOMSource doms = new DOMSource(this.doc);
            StreamResult sr = new StreamResult(this.out);
            try {
                t.transform(doms, sr);
            } catch (TransformerException ex) {
                throw (IOException) new IOException(ex.getMessage()).initCause(ex);
            }
        }
    }

    private final class XMLPreferencesOutput extends XMLOutput {

        private Element mapElement;

        XMLPreferencesOutput(OutputStream out, boolean skipComments) {
            super(out, skipComments);
        }

        @Override
        void generateDoc() {
            if (!this.skipComments) {
                String comment = PropertiesFile.this.getHeaderComment();
                if (comment != null) {
                    Comment commentNode = doc.createComment(comment);
                    this.doc.appendChild(commentNode);
                }
            }
            this.mapElement = this.doc.createElement("map");
            this.mapElement.setAttribute("MAP_XML_VERSION", "1.0");
            this.doc.appendChild(this.mapElement);
            super.generateDoc();
        }

        @Override
        String getDtdUri() {
            return PreferencesUtil.PREFS_DTD_URI;
        }

        @Override
        void writeProperty(String key, String val, String comment) {
            if (comment != null) {
                Comment commentNode = doc.createComment(comment);
                this.mapElement.appendChild(commentNode);
            }
            Element entry = doc.createElement("entry");
            this.mapElement.appendChild(entry);
            entry.setAttribute("key", key);
            entry.setAttribute("value", val);
        }
    }

    private final class XMLPropertiesOutput extends XMLOutput {

        private Element propertiesElement;

        XMLPropertiesOutput(OutputStream out, boolean skipComments) {
            super(out, skipComments);
        }

        @Override
        void generateDoc() {
            this.propertiesElement = this.doc.createElement("properties");
            this.doc.appendChild(this.propertiesElement);
            if (!this.skipComments) {
                String comment = PropertiesFile.this.getHeaderComment();
                if (comment != null) {
                    Element e = doc.createElement("comment");
                    this.propertiesElement.appendChild(e);
                    e.appendChild(doc.createTextNode(comment));
                }
            }
            super.generateDoc();
        }

        @Override
        String getDtdUri() {
            return XMLUtil.PROPS_DTD_URI;
        }

        @Override
        void writeProperty(String key, String val, String comment) {
            if (comment != null) {
                Comment commentNode = doc.createComment(comment);
                this.propertiesElement.appendChild(commentNode);
            }
            Element entry = doc.createElement("entry");
            this.propertiesElement.appendChild(entry);
            entry.setAttribute("key", key);
            entry.appendChild(doc.createTextNode(val));
        }
    }

    private static final class XMLUtil extends Object {

        static final String PROPS_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<!-- DTD for properties -->" + "<!ELEMENT properties ( comment?, entry* ) >" + "<!ATTLIST properties" + " version CDATA #FIXED \"1.0\">" + "<!ELEMENT comment (#PCDATA) >" + "<!ELEMENT entry (#PCDATA) >" + "<!ATTLIST entry " + " key CDATA #REQUIRED>";

        static final String PROPS_DTD_URI = "http://java.sun.com/dtd/properties.dtd";

        private static DocumentBuilderFactory documentBuilderFactoryIncludingComments;

        private static DocumentBuilderFactory documentBuilderFactorySkippingComments;

        private static final AtomicReference<DocumentBuilder> documentBuilderIncludingComments = new AtomicReference<DocumentBuilder>();

        private static final AtomicReference<DocumentBuilder> documentBuilderSkippingComments = new AtomicReference<DocumentBuilder>();

        private XMLUtil() {
        }

        private static DocumentBuilder getDocumentBuilder(boolean skipComments) {
            DocumentBuilder db = skipComments ? XMLUtil.documentBuilderSkippingComments.getAndSet(null) : XMLUtil.documentBuilderIncludingComments.getAndSet(null);
            if (db == null) {
                DocumentBuilderFactory dbf = skipComments ? XMLUtil.documentBuilderFactorySkippingComments : XMLUtil.documentBuilderFactoryIncludingComments;
                if (dbf == null) {
                    dbf = DocumentBuilderFactory.newInstance();
                    dbf.setCoalescing(true);
                    dbf.setIgnoringComments(skipComments);
                    dbf.setIgnoringElementContentWhitespace(true);
                    dbf.setValidating(true);
                    if (skipComments) XMLUtil.documentBuilderFactorySkippingComments = dbf; else XMLUtil.documentBuilderFactoryIncludingComments = dbf;
                }
                try {
                    db = dbf.newDocumentBuilder();
                } catch (ParserConfigurationException ex) {
                    throw new Error(ex);
                }
                db.setEntityResolver(XMLUtil.HandlerImpl.instance);
                db.setErrorHandler(XMLUtil.HandlerImpl.instance);
            }
            return db;
        }

        static Document loadDocument(InputStream in, boolean skipComments) throws IOException {
            DocumentBuilder db = XMLUtil.getDocumentBuilder(skipComments);
            InputSource is = new InputSource(in);
            Document doc;
            try {
                doc = db.parse(is);
            } catch (SAXException ex) {
                throw (IOException) new IOException(ex.getMessage()).initCause(ex);
            }
            if (skipComments) XMLUtil.documentBuilderSkippingComments.set(db); else XMLUtil.documentBuilderIncludingComments.set(db);
            return doc;
        }

        private static final class HandlerImpl extends Object implements EntityResolver, ErrorHandler {

            static final HandlerImpl instance = new HandlerImpl();

            private HandlerImpl() {
                super();
            }

            public void error(SAXParseException x) throws SAXException {
                throw x;
            }

            public void fatalError(SAXParseException x) throws SAXException {
                throw x;
            }

            public void warning(SAXParseException x) throws SAXException {
                throw x;
            }

            public InputSource resolveEntity(String pid, String sid) throws SAXException {
                if (sid.equals(PROPS_DTD_URI)) {
                    InputSource is = new InputSource(new StringReader(PROPS_DTD));
                    is.setSystemId(PROPS_DTD_URI);
                    return is;
                } else if (sid.equals(PreferencesUtil.PREFS_DTD_URI)) {
                    InputSource is = new InputSource(new StringReader(PreferencesUtil.PREFS_DTD));
                    is.setSystemId(PreferencesUtil.PREFS_DTD_URI);
                    return is;
                } else {
                    throw new SAXException("Invalid system identifier: " + sid);
                }
            }
        }
    }
}
