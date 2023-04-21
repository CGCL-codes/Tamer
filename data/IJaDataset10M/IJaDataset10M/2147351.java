package org.archive.io.warc.v10;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;
import org.archive.io.warc.WARCConstants;
import org.archive.util.LongWrapper;
import org.archive.util.anvl.ANVLRecord;

/**
 * A WARC file Record.
 *
 * @author stack
 */
public class WARCRecord extends ArchiveRecord implements WARCConstants {

    /**
     * Header-Line pattern;
     * I heart http://www.fileformat.info/tool/regex.htm
     */
    private static final Pattern HEADER_LINE = Pattern.compile("^WARC/([0-9]+\\.[0-9]+(?:\\.[0-9]+)?)" + "[\\t ]+" + "([0-9]+)" + "[\\t ]+" + "(request|response|warcinfo|resource|metadata|" + "revisit|conversion)" + "[\\t ]+" + "([^\\t ]+)" + "[\\t ]+" + "([0-9]{14})" + "[\\t ]+" + "([^\\t ]+)" + "[\\t ]+" + "(.+)$");

    private Pattern WHITESPACE = Pattern.compile("\\s");

    /**
     * Constructor.
     *
     * @param in Stream cue'd up to be at the start of the record this instance
     * is to represent.
     * @throws IOException
     */
    public WARCRecord(InputStream in, final String identifier, final long offset) throws IOException {
        this(in, identifier, offset, true, false);
    }

    /**
     * Constructor.
     * @param in Stream cue'd up just past Header Line and Named Fields.
     * @param headers Header Line and ANVL Named fields.
     * @throws IOException
     */
    public WARCRecord(InputStream in, ArchiveRecordHeader headers) throws IOException {
        super(in, headers, 0, true, false);
    }

    /**
     * Constructor.
     *
     * @param in Stream cue'd up to be at the start of the record this instance
     * is to represent or, if <code>headers</code> is not null, just past the
     * Header Line and Named Fields.
     * @param identifier Identifier for this the hosting Reader.
     * @param offset Current offset into <code>in</code> (Used to keep
     * <code>position</code> properly aligned).  Usually 0.
     * @param digest True if we're to calculate digest for this record.  Not
     * digesting saves about ~15% of cpu during parse.
     * @param strict Be strict parsing (Parsing stops if file inproperly
     * formatted).
     * @throws IOException
     */
    public WARCRecord(final InputStream in, final String identifier, final long offset, boolean digest, boolean strict) throws IOException {
        super(in, null, 0, digest, strict);
        setHeader(parseHeaders(in, identifier, offset, strict));
    }

    /**
     * Parse WARC Header Line and Named Fields.
     * @param in Stream to read.
     * @param identifier Identifier for the hosting Reader.
     * @param offset Absolute offset into Reader.
     * @param strict Whether to be loose parsing or not.
     * @return An ArchiveRecordHeader.
     * @throws IOException 
     */
    protected ArchiveRecordHeader parseHeaders(final InputStream in, final String identifier, final long offset, final boolean strict) throws IOException {
        final Map<Object, Object> m = new HashMap<Object, Object>();
        m.put(ABSOLUTE_OFFSET_KEY, new Long(offset));
        m.put(READER_IDENTIFIER_FIELD_KEY, identifier);
        int headLineLength = parseHeaderLine(in, m, strict);
        final LongWrapper anvlParseLength = new LongWrapper(0);
        InputStream countingStream = new InputStream() {

            @Override
            public int read() throws IOException {
                int c = in.read();
                if (c != -1) {
                    anvlParseLength.longValue++;
                }
                return c;
            }
        };
        parseNamedFields(countingStream, m);
        final int contentOffset = (int) (headLineLength + anvlParseLength.longValue);
        incrementPosition(contentOffset);
        return new ArchiveRecordHeader() {

            private Map<Object, Object> fields = m;

            private int contentBegin = contentOffset;

            public String getDate() {
                return (String) this.fields.get(DATE_FIELD_KEY);
            }

            public String getDigest() {
                return (String) this.fields.get(NAMED_FIELD_CHECKSUM_LABEL);
            }

            public String getReaderIdentifier() {
                return (String) this.fields.get(READER_IDENTIFIER_FIELD_KEY);
            }

            public Set getHeaderFieldKeys() {
                return this.fields.keySet();
            }

            public Map getHeaderFields() {
                return this.fields;
            }

            public Object getHeaderValue(String key) {
                return this.fields.get(key);
            }

            public long getLength() {
                Object o = this.fields.get(LENGTH_FIELD_KEY);
                if (o == null) {
                    return -1;
                }
                return ((Long) o).longValue();
            }

            public String getMimetype() {
                return (String) this.fields.get(MIMETYPE_FIELD_KEY);
            }

            public long getOffset() {
                Object o = this.fields.get(ABSOLUTE_OFFSET_KEY);
                if (o == null) {
                    return -1;
                }
                return ((Long) o).longValue();
            }

            public String getRecordIdentifier() {
                return (String) this.fields.get(RECORD_IDENTIFIER_FIELD_KEY);
            }

            public String getUrl() {
                return (String) this.fields.get(URL_FIELD_KEY);
            }

            public String getVersion() {
                return (String) this.fields.get(VERSION_FIELD_KEY);
            }

            public int getContentBegin() {
                return this.contentBegin;
            }

            @Override
            public String toString() {
                return this.fields.toString();
            }
        };
    }

    protected int parseHeaderLine(final InputStream in, final Map<Object, Object> fields, final boolean strict) throws IOException {
        byte[] line = readLine(in, strict);
        if (line.length <= 2) {
            throw new IOException("No Header Line found");
        }
        String headerLine = new String(line, 0, line.length - 2, HEADER_LINE_ENCODING);
        Matcher m = HEADER_LINE.matcher(headerLine);
        if (!m.matches()) {
            throw new IOException("Failed parse of Header Line: " + headerLine);
        }
        for (int i = 0; i < HEADER_FIELD_KEYS.length; i++) {
            if (i == 1) {
                fields.put(HEADER_FIELD_KEYS[i], Long.parseLong(m.group(i + 1)));
                continue;
            }
            fields.put(HEADER_FIELD_KEYS[i], m.group(i + 1));
        }
        return line.length;
    }

    /**
     * Read a line.
     * A 'line' in this context ends in CRLF and contains ascii-only and no
     * control-characters.
     * @param in InputStream to read.
     * @param strict Strict parsing (If false, we'll eat whitespace before the
     * record.
     * @return All bytes in line including terminating CRLF.
     * @throws IOException
     */
    protected byte[] readLine(final InputStream in, final boolean strict) throws IOException {
        boolean done = false;
        boolean recordStart = strict;
        int read = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        for (int c = -1, previousCharacter; !done; ) {
            if (read++ >= MAX_LINE_LENGTH) {
                throw new IOException("Read " + MAX_LINE_LENGTH + " bytes without finding CRLF");
            }
            previousCharacter = c;
            c = in.read();
            if (c == -1) {
                throw new IOException("End-Of-Stream before CRLF:\n" + new String(baos.toByteArray()));
            }
            if (isLF((char) c) && isCR((char) previousCharacter)) {
                done = true;
            } else if (!recordStart && Character.isWhitespace(c)) {
                continue;
            } else {
                if (isCR((char) previousCharacter)) {
                    throw new IOException("CR in middle of Header:\n" + new String(baos.toByteArray()));
                }
                if (!recordStart) {
                    recordStart = true;
                }
            }
            baos.write(c);
        }
        return baos.toByteArray();
    }

    protected void parseNamedFields(final InputStream in, final Map<Object, Object> fields) throws IOException {
        ANVLRecord r = ANVLRecord.load(in);
        fields.putAll(r.asMap());
    }

    public static boolean isCROrLF(final char c) {
        return isCR(c) || isLF(c);
    }

    public static boolean isCR(final char c) {
        return c == CRLF.charAt(0);
    }

    public static boolean isLF(final char c) {
        return c == CRLF.charAt(1);
    }

    @Override
    protected String getMimetype4Cdx(ArchiveRecordHeader h) {
        final String m = super.getMimetype4Cdx(h);
        Matcher matcher = WHITESPACE.matcher(m);
        return matcher.replaceAll("");
    }
}
