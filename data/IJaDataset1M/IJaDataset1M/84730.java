package com.bluebrim.font.impl.server.truetype;

import java.io.*;
import java.util.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.font.impl.server.util.*;

/**
 * The core TTF parser. This class takes care of all the messy details of the TTF file, parsing it into some
 * kind of intermediary format which can be transformed into something more useful by the corresponding 
 * FileInfoExctractor class.
 * Creation date: (2001-04-06 11:06:00)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoTrueTypeFileParser {

    /** Simple comparator class, used to compare the placement in the TTF file of tables. A table is
* "smaller" than another, if it if placed before the other in the file.
*/
    protected class TableDirOffsetComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            if (!(o1 instanceof CoTrueTypeTableEntry)) {
                throw new ClassCastException();
            }
            if (!(o2 instanceof CoTrueTypeTableEntry)) {
                throw new ClassCastException();
            }
            CoTrueTypeTableEntry entry1 = (CoTrueTypeTableEntry) o1;
            CoTrueTypeTableEntry entry2 = (CoTrueTypeTableEntry) o2;
            if (entry1.getOffset() == entry2.getOffset()) {
                return 0;
            }
            if (entry1.getOffset() < entry2.getOffset()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    ;

    private int m_numTables;

    private List m_tableDir;

    private int m_unitsPerEm;

    private static final byte[] TTF_VERSION = new byte[] { 0, 1, 0, 0 };

    private static final byte[] APPLE_TYPE = new byte[] { 0x74, 0x72, 0x75, 0x65 };

    private static final Set REQUIRED_TABLES = new HashSet(Arrays.asList(new String[] { "cmap", "head", "hmtx", "hhea", "maxp", "name", "post", "OS/2", "loca", "glyf" }));

    private int m_weightValue;

    private int m_widthValue;

    private int m_strikeoutThickness;

    private int m_strikeoutPosition;

    private int m_fsSelection;

    private int m_ascent;

    private int m_descent;

    private int m_linegap;

    private int m_numHMetrics;

    private double m_fontRevision;

    private int m_bBoxXMin;

    private int m_bBoxXMax;

    private int m_bBoxYMin;

    private int m_bBoxYMax;

    private int m_locaTableFormat;

    private int[] m_internalKernPairLeftChar;

    private int[] m_internalKernPairRightChar;

    private short[] m_internalKernPairDistance;

    private byte[] m_internalHmtxTable;

    private byte[] m_internalLocaTable;

    private int[] m_glyphAdvances;

    private int[] m_glyphLocations;

    private double m_italicAngle;

    private short m_underlinePosition;

    private short m_underlineThickness;

    private boolean m_isFixedPitch;

    private long m_minMemoryType42;

    private long m_maxMemoryType42;

    private CoTrueTypeNameRecord[] m_nameRecords;

    private byte[] m_nameData;

    private Map m_glyphMapping = new HashMap();

    private PrintWriter m_warnings;

    protected void parseNameTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
        int format = CoNativeDataUtil.readUint16(ttStream);
        if (format != 0) {
            m_warnings.println("Warning: Name table format is not 0. Still trying to parse table, though.");
        }
        int nameRecordCount = CoNativeDataUtil.readUint16(ttStream);
        int nominalHeaderSize = CoNativeDataUtil.readUint16(ttStream);
        m_nameRecords = new CoTrueTypeNameRecord[nameRecordCount];
        for (int i = 0; i < nameRecordCount; i++) {
            m_nameRecords[i] = new CoTrueTypeNameRecord(ttStream);
        }
        int filePos = 6 + nameRecordCount * 12;
        if (nominalHeaderSize != filePos) {
            m_warnings.println("Warning: Actual and specified start of name string data is not identical. (Actual: " + filePos + ", specified: " + nominalHeaderSize + "). Ignoring specified size.");
        }
        int nameDataSize = (int) entry.getLength() - filePos;
        m_nameData = new byte[nameDataSize];
        ttStream.read(m_nameData);
        for (int i = 0; i < nameRecordCount; i++) {
            m_nameRecords[i].setNameDataArea(m_nameData);
        }
    }

    protected void parseHeadTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
        assertTableLength(54, entry);
        ttStream.skip(4);
        m_fontRevision = CoNativeDataUtil.readFixed32(ttStream);
        ttStream.skip(10);
        m_unitsPerEm = CoNativeDataUtil.readUint16(ttStream);
        ttStream.skip(16);
        m_bBoxXMin = CoNativeDataUtil.readShort16(ttStream);
        m_bBoxYMin = CoNativeDataUtil.readShort16(ttStream);
        m_bBoxXMax = CoNativeDataUtil.readShort16(ttStream);
        m_bBoxYMax = CoNativeDataUtil.readShort16(ttStream);
        ttStream.skip(6);
        m_locaTableFormat = CoNativeDataUtil.readUint16(ttStream);
        ttStream.skip(2);
    }

    protected void parsePostTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
        if (entry.getLength() < 24) {
            throw new com.bluebrim.font.shared.CoFontException("Error in TTF file: table " + entry.getName() + " is shorter than expected (" + entry.getLength() + " when it should be at least 24).");
        }
        ttStream.skip(4);
        m_italicAngle = CoNativeDataUtil.readFixed32(ttStream);
        m_underlinePosition = CoNativeDataUtil.readShort16(ttStream);
        m_underlineThickness = CoNativeDataUtil.readShort16(ttStream);
        m_isFixedPitch = (CoNativeDataUtil.readUint16(ttStream) != 0);
        ttStream.skip(2);
        m_minMemoryType42 = CoNativeDataUtil.readUint32(ttStream);
        m_maxMemoryType42 = CoNativeDataUtil.readUint32(ttStream);
        int filePos = 24;
        ttStream.skip(entry.getLength() - filePos);
    }

    protected void parseMaxpTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
        skipTable(ttStream, entry);
    }

    protected void parseCmapTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
        int version = CoNativeDataUtil.readUint16(ttStream);
        if (version > 1) {
            m_warnings.println("Warning: cmap table version number is " + version + ", and not 0 or 1 as expected.");
        }
        int subTableCount = CoNativeDataUtil.readUint16(ttStream);
        int[] platform = new int[subTableCount];
        int[] encoding = new int[subTableCount];
        long[] offset = new long[subTableCount];
        int encodingTablesStart = 4 + (8 * subTableCount);
        int unicodeEncodingSubtable = -1;
        for (int i = 0; i < subTableCount; i++) {
            platform[i] = CoNativeDataUtil.readUint16(ttStream);
            encoding[i] = CoNativeDataUtil.readUint16(ttStream);
            offset[i] = CoNativeDataUtil.readUint32(ttStream) - encodingTablesStart;
            if (platform[i] == 3 && encoding[i] == 1) {
                unicodeEncodingSubtable = i;
            }
        }
        if (unicodeEncodingSubtable == -1) {
            throw new com.bluebrim.font.shared.CoFontException("Unicode encoding not found in true type file. Can't map font.");
        }
        ttStream.skip(offset[unicodeEncodingSubtable]);
        long filePos = encodingTablesStart + offset[unicodeEncodingSubtable];
        int subTableFormat = CoNativeDataUtil.readUint16(ttStream);
        int subTableLength = CoNativeDataUtil.readUint16(ttStream);
        if (subTableFormat != 4) {
            throw new com.bluebrim.font.shared.CoFontException("Unicode encoding table in cmap is in unknown format (is: " + subTableFormat + ", should be: 4). Can't read font.");
        }
        readCmapSubTableFormat4(ttStream, subTableLength - 4);
        filePos += subTableLength;
        ttStream.skip(entry.getLength() - filePos);
        checkActualTableLength(filePos, entry);
    }

    protected void parseHheaTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
        assertTableLength(36, entry);
        ttStream.skip(34);
        m_numHMetrics = CoNativeDataUtil.readUint16(ttStream);
        ttStream.skip(entry.getLength() - 36);
    }

    protected void parseHmtxTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
        m_internalHmtxTable = new byte[(int) entry.getLength()];
        ttStream.read(m_internalHmtxTable);
    }

    protected void parseOS_2Table(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
        if (entry.getLength() > 86) {
            m_warnings.println("Warning: table " + entry.getName() + " is longer than expected (" + entry.getLength() + " instead of " + 86 + "). Ignoring additional information.");
        }
        if (entry.getLength() < 68) {
            throw new com.bluebrim.font.shared.CoFontException("Error in TTF file: table " + entry.getName() + " is shorter than expected (" + entry.getLength() + " instead of " + 68 + ").");
        }
        ttStream.skip(4);
        m_weightValue = CoNativeDataUtil.readUint16(ttStream);
        m_widthValue = CoNativeDataUtil.readUint16(ttStream);
        ttStream.skip(18);
        m_strikeoutThickness = CoNativeDataUtil.readUint16(ttStream);
        m_strikeoutPosition = CoNativeDataUtil.readUint16(ttStream);
        ttStream.skip(32);
        m_fsSelection = CoNativeDataUtil.readUint16(ttStream);
        ttStream.skip(4);
        long filePos = 68;
        if (entry.getLength() >= 78) {
            m_ascent = CoNativeDataUtil.readUint16(ttStream);
            m_descent = Math.abs(CoNativeDataUtil.readShort16(ttStream));
            m_linegap = CoNativeDataUtil.readUint16(ttStream);
            filePos += 6;
        } else {
            m_warnings.println("Warning: No sTypoAscent/Descent/LineGap information in OS/2 table.");
        }
        ttStream.skip((entry.getLength() - filePos));
    }

    protected void parseKernTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
        int tableVersion = CoNativeDataUtil.readUint16(ttStream);
        int filePos = 2;
        if (tableVersion != 0) {
            ttStream.skip((entry.getLength() - filePos));
            m_warnings.println("Warning: Incorrect version number (only version 0 accepted) in kern table (pair kerning data).");
            return;
        }
        int subTableCount = CoNativeDataUtil.readUint16(ttStream);
        filePos += 2;
        for (int i = 0; i < subTableCount; i++) {
            int subTableHeaderSize = 6;
            int subTableVersion = CoNativeDataUtil.readUint16(ttStream);
            int subTableLength = CoNativeDataUtil.readUint16(ttStream) - subTableHeaderSize;
            int coverage = CoNativeDataUtil.readUint16(ttStream);
            filePos += subTableHeaderSize;
            if ((coverage == 1) || (coverage == 9)) {
                int kernPairCount = CoNativeDataUtil.readUint16(ttStream);
                ttStream.skip(6);
                m_internalKernPairLeftChar = new int[kernPairCount];
                m_internalKernPairRightChar = new int[kernPairCount];
                m_internalKernPairDistance = new short[kernPairCount];
                for (int j = 0; j < kernPairCount; j++) {
                    m_internalKernPairLeftChar[j] = CoNativeDataUtil.readUint16(ttStream);
                    m_internalKernPairRightChar[j] = CoNativeDataUtil.readUint16(ttStream);
                    m_internalKernPairDistance[j] = CoNativeDataUtil.readShort16(ttStream);
                }
                if (subTableLength != (8 + (6 * kernPairCount))) {
                    throw new com.bluebrim.font.shared.CoFontException("Pair kerning table subtable type 0 is of incorrect length");
                }
            } else {
                ttStream.skip(subTableLength);
            }
            filePos += subTableLength;
        }
        checkActualTableLength(filePos, entry);
        ttStream.skip(entry.getLength() - filePos);
    }

    protected void parseGlyfTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
        skipTable(ttStream, entry);
    }

    protected void parseLocaTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
        m_internalLocaTable = new byte[(int) entry.getLength()];
        ttStream.read(m_internalLocaTable);
    }

    protected void skipTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
        ttStream.skip(entry.getLength());
    }

    public boolean canHandleFile(InputStream file) {
        byte[] identifier = new byte[4];
        try {
            file.read(identifier, 0, 4);
            if (Arrays.equals(identifier, TTF_VERSION) || Arrays.equals(identifier, APPLE_TYPE)) {
                return true;
            }
        } catch (IOException ignored) {
        }
        return false;
    }

    protected long readOffsetSubtable(InputStream ttStream) throws com.bluebrim.font.shared.CoFontException {
        byte[] identifier = new byte[4];
        byte[] buffer = new byte[2];
        try {
            ttStream.read(identifier, 0, 4);
            if (!Arrays.equals(identifier, TTF_VERSION) && !Arrays.equals(identifier, APPLE_TYPE)) {
                throw new com.bluebrim.font.shared.CoFontException("Not a TrueType file: " + ttStream);
            }
            m_numTables = CoNativeDataUtil.readUint16(ttStream);
            ttStream.skip(3 * 2);
        } catch (IOException e) {
            throw new com.bluebrim.font.shared.CoFontException("Error reading from TTF File (" + ttStream + ").", e);
        }
        return 12;
    }

    protected long readTableDirectory(InputStream ttStream) throws com.bluebrim.font.shared.CoFontException {
        m_tableDir = new ArrayList(m_numTables);
        for (int i = 0; i < m_numTables; i++) {
            CoTrueTypeTableEntry entry = new CoTrueTypeTableEntry(ttStream);
            m_tableDir.add(entry);
        }
        return (m_numTables * CoTrueTypeTableEntry.LENGTH);
    }

    protected void parseTTFile(InputStream ttStream) throws com.bluebrim.font.shared.CoFontException {
        Set requiredTablesLeft = new HashSet(REQUIRED_TABLES);
        long filePos = 0;
        filePos += readOffsetSubtable(ttStream);
        filePos += readTableDirectory(ttStream);
        try {
            Collections.sort(m_tableDir, new TableDirOffsetComparator());
            Iterator entries = m_tableDir.iterator();
            while (entries.hasNext()) {
                CoTrueTypeTableEntry entry = (CoTrueTypeTableEntry) entries.next();
                if (entry.getOffset() < filePos) {
                    throw new com.bluebrim.font.shared.CoFontException("Invalid TTF file: table " + entry.getName() + " starts at overlapping address.");
                }
                if (entry.getOffset() > filePos) {
                    if (!((entry.getOffset() % 4 == 0) && (entry.getOffset() - filePos <= 3))) {
                        m_warnings.println("Warning: TTF file contains strangely aligned tables. There is " + (entry.getOffset() - filePos) + " bytes of junk data before table " + entry.getName());
                    }
                    ttStream.skip(entry.getOffset() - filePos);
                    filePos = entry.getOffset();
                }
                String name = entry.getName();
                requiredTablesLeft.remove(name);
                if (name.equals("name")) {
                    parseNameTable(ttStream, entry);
                } else if (name.equals("head")) {
                    parseHeadTable(ttStream, entry);
                } else if (name.equals("post")) {
                    parsePostTable(ttStream, entry);
                } else if (name.equals("maxp")) {
                    parseMaxpTable(ttStream, entry);
                } else if (name.equals("cmap")) {
                    parseCmapTable(ttStream, entry);
                } else if (name.equals("hhea")) {
                    parseHheaTable(ttStream, entry);
                } else if (name.equals("hmtx")) {
                    parseHmtxTable(ttStream, entry);
                } else if (name.equals("OS/2")) {
                    parseOS_2Table(ttStream, entry);
                } else if (name.equals("glyf")) {
                    parseGlyfTable(ttStream, entry);
                } else if (name.equals("loca")) {
                    parseLocaTable(ttStream, entry);
                } else if (name.equals("kern")) {
                    parseKernTable(ttStream, entry);
                } else {
                    skipTable(ttStream, entry);
                }
                filePos += entry.getLength();
            }
            if (ttStream.available() != 0) {
                m_warnings.println("Warning: TTF file contains data beyond last specified table.");
            }
            if (!requiredTablesLeft.isEmpty()) {
                throw new com.bluebrim.font.shared.CoFontException("Not all required tables was found in TTF file (" + ttStream + "). The following table(s) were missing: " + requiredTablesLeft);
            }
            handleHmtxTable();
            handleLocaTable();
        } catch (IOException e) {
            throw new com.bluebrim.font.shared.CoFontException("Error reading from TTF File (" + ttStream + ")", e);
        }
    }

    protected void assertTableLength(long expected, CoTrueTypeTableEntry entry) throws com.bluebrim.font.shared.CoFontException {
        if (entry.getLength() > expected) {
            m_warnings.println("Warning: table " + entry.getName() + " is longer than expected (" + entry.getLength() + " instead of " + expected + "). Ignoring additional information.");
        }
        if (entry.getLength() < expected) {
            throw new com.bluebrim.font.shared.CoFontException("Error in TTF file: table " + entry.getName() + " is shorter than expected (" + entry.getLength() + " instead of " + expected + ").");
        }
    }

    /**
 * Check if a file is in a format that this class can handle. This is just done by simple heuristics
 * (like 'magic numbers'), so a successful return from this method is not a guarantee that the file
 * will be successfully parsed.
 * Creation date: (2001-04-20 14:36:20)
 * @return True iff this file is of a format that can be handled.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
    public static boolean canHandle(File fontFile) {
        try {
            FileInputStream in = new FileInputStream(fontFile);
            byte[] identifier = new byte[4];
            in.read(identifier, 0, 4);
            if (Arrays.equals(identifier, TTF_VERSION) || Arrays.equals(identifier, APPLE_TYPE)) {
                return true;
            }
        } catch (IOException ignored) {
        }
        return false;
    }

    protected void checkActualTableLength(long actual, CoTrueTypeTableEntry entry) throws com.bluebrim.font.shared.CoFontException {
        if (actual < entry.getLength()) {
            m_warnings.println("Warning: table " + entry.getName() + " was actually shorter than declared (" + actual + " instead of " + entry.getLength() + "). Ignoring additional information.");
        }
        if (actual > entry.getLength()) {
            throw new com.bluebrim.font.shared.CoFontException("Error in TTF file: table " + entry.getName() + " was actually longer than declared (" + actual + " instead of " + entry.getLength() + ").");
        }
    }

    protected String getBestMatchingName(int nameType) {
        List records = new LinkedList();
        for (int i = 0; i < m_nameRecords.length; i++) {
            CoTrueTypeNameRecord record = m_nameRecords[i];
            if (record.getNameType() == nameType) {
                records.add(record);
            }
        }
        Iterator i = records.iterator();
        while (i.hasNext()) {
            CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
            if (record.getPlatform() == 0) {
                return record.getStringRepresentation();
            }
        }
        i = records.iterator();
        while (i.hasNext()) {
            CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
            if (record.getPlatform() == 3 && record.getLanguage() == 0x0409) {
                return record.getStringRepresentation();
            }
        }
        i = records.iterator();
        while (i.hasNext()) {
            CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
            if (record.getPlatform() == 3 && record.getLanguage() == 0x0809) {
                return record.getStringRepresentation();
            }
        }
        i = records.iterator();
        while (i.hasNext()) {
            CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
            if (record.getPlatform() == 1 && record.getEncoding() == 0 && record.getLanguage() == 0) {
                return record.getStringRepresentation();
            }
        }
        i = records.iterator();
        while (i.hasNext()) {
            CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
            if (record.getPlatform() == 3) {
                return record.getStringRepresentation();
            }
        }
        i = records.iterator();
        while (i.hasNext()) {
            CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
            if (record.getPlatform() == 1) {
                return record.getStringRepresentation();
            }
        }
        return null;
    }

    /**
 * fsSelection is a flag byte, most important is bit 0 which specifices if the font is italic or not (1=italic).
 * Creation date: (2001-05-28 11:15:36)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
    public int getFsSelection() {
        return m_fsSelection;
    }

    public int[] getGlyphAdvances() {
        return m_glyphAdvances;
    }

    public Map getGlyphMapping() {
        return m_glyphMapping;
    }

    public short[] getInternalKernPairDistance() {
        return m_internalKernPairDistance;
    }

    public int[] getInternalKernPairLeftChar() {
        return m_internalKernPairLeftChar;
    }

    public int[] getInternalKernPairRightChar() {
        return m_internalKernPairRightChar;
    }

    public double getItalicAngle() {
        return m_italicAngle;
    }

    public int getLinegap() {
        return m_linegap;
    }

    public int getStrikeoutPosition() {
        return m_strikeoutPosition;
    }

    public int getStrikeoutThickness() {
        return m_strikeoutThickness;
    }

    public short getUnderlineThickness() {
        return m_underlineThickness;
    }

    /**
 * Size of em square in TrueType units. Divide by this number to normalize metrics to em square.
 * Creation date: (2001-05-23 15:12:25)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
    public int getUnitsPerEm() {
        return m_unitsPerEm;
    }

    protected void handleHmtxTable() {
        m_glyphAdvances = new int[m_numHMetrics];
        for (int i = 0; i < m_numHMetrics; i++) {
            m_glyphAdvances[i] = CoNativeDataUtil.read2BytesAsInt(m_internalHmtxTable, i * 4);
        }
        m_internalHmtxTable = null;
    }

    protected void readCmapSubTableFormat4(InputStream ttStream, int length) throws IOException {
        int version = CoNativeDataUtil.readUint16(ttStream);
        if (version != 0) {
            m_warnings.println("Warning: cmap subtable (type 4) version number is " + version + ", and not 0 as expected.");
        }
        int segmentCount = CoNativeDataUtil.readUint16(ttStream) / 2;
        ttStream.skip(6);
        int[] startChar = new int[segmentCount];
        int[] endChar = new int[segmentCount];
        short[] delta = new short[segmentCount];
        int[] offset = new int[segmentCount];
        for (int i = 0; i < segmentCount; i++) {
            endChar[i] = CoNativeDataUtil.readUint16(ttStream);
        }
        ttStream.skip(2);
        for (int i = 0; i < segmentCount; i++) {
            startChar[i] = CoNativeDataUtil.readUint16(ttStream);
        }
        for (int i = 0; i < segmentCount; i++) {
            delta[i] = CoNativeDataUtil.readShort16(ttStream);
        }
        for (int i = 0; i < segmentCount; i++) {
            offset[i] = CoNativeDataUtil.readUint16(ttStream);
        }
        int glyphIndexArrayLength = (length - 12 - (8 * segmentCount)) / 2;
        int[] glyphIndexArray = new int[glyphIndexArrayLength];
        for (int i = 0; i < glyphIndexArrayLength; i++) {
            glyphIndexArray[i] = CoNativeDataUtil.readUint16(ttStream);
        }
        for (int segment = 0; segment < segmentCount; segment++) {
            if (offset[segment] == 0) {
                for (int i = startChar[segment]; i <= endChar[segment]; i++) {
                    registerMapping((char) i, (short) (i + delta[segment]));
                }
            } else {
                for (int i = startChar[segment]; i <= endChar[segment]; i++) {
                    int glyphIndexOffset = (i - startChar[segment]) + (offset[segment] / 2) + (segment - segmentCount);
                    if (glyphIndexOffset >= glyphIndexArrayLength) {
                        m_warnings.println("Warning: Glyph index out of array bounds in cmap. Char value: " + i);
                    } else {
                        int glyphIndex = glyphIndexArray[glyphIndexOffset];
                        if (glyphIndex != 0) {
                            glyphIndex = (short) (glyphIndex + delta[segment]);
                            registerMapping((char) i, glyphIndex);
                        }
                    }
                }
            }
        }
    }

    protected void registerMapping(char ch, int glyphIndex) {
        if (glyphIndex != 0) {
            Integer glyphIntWrapper = new Integer(glyphIndex);
            Set unicodeSet = (Set) m_glyphMapping.get(glyphIntWrapper);
            if (unicodeSet == null) {
                unicodeSet = new HashSet();
                m_glyphMapping.put(glyphIntWrapper, unicodeSet);
            }
            unicodeSet.add(new Character(ch));
        }
    }

    public CoTrueTypeFileParser(CoMessageLogger warningLog, InputStream ttFile) throws com.bluebrim.font.shared.CoFontException {
        super();
        m_warnings = warningLog.getWriter();
        parseTTFile(ttFile);
    }

    public int getAscent() {
        return m_ascent;
    }

    public int getDescent() {
        return m_descent;
    }

    public short getUnderlinePosition() {
        return m_underlinePosition;
    }

    public int getWeightValue() {
        return m_weightValue;
    }

    public int getWidthValue() {
        return m_widthValue;
    }

    public int getBBoxXMax() {
        return m_bBoxXMax;
    }

    public int getBBoxXMin() {
        return m_bBoxXMin;
    }

    public int getBBoxYMax() {
        return m_bBoxYMax;
    }

    public int getBBoxYMin() {
        return m_bBoxYMin;
    }

    public double getFontRevision() {
        return m_fontRevision;
    }

    public int[] getGlyphLocations() {
        return m_glyphLocations;
    }

    public long getMaxMemoryType42() {
        return m_maxMemoryType42;
    }

    public long getMinMemoryType42() {
        return m_minMemoryType42;
    }

    public int getNumTables() {
        return m_numTables;
    }

    public List getTableDir() {
        return m_tableDir;
    }

    protected void handleLocaTable() {
        if (m_locaTableFormat == 0) {
            int locaTableLength = m_internalLocaTable.length / 2;
            m_glyphLocations = new int[locaTableLength];
            for (int i = 0; i < locaTableLength; i++) {
                m_glyphLocations[i] = CoNativeDataUtil.read2BytesAsInt(m_internalLocaTable, i * 2) * 2;
            }
        } else {
            int locaTableLength = m_internalLocaTable.length / 4;
            m_glyphLocations = new int[locaTableLength];
            for (int i = 0; i < locaTableLength; i++) {
                m_glyphLocations[i] = CoNativeDataUtil.read2BytesAsInt(m_internalLocaTable, i * 4);
            }
        }
    }
}
