package org.icepdf.core.pobjects.fonts.ofont;

import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import org.icepdf.core.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The purpose of the class is to parse a CMap file.  A CMap specifies the
 * mapping from character codes to character selectors.  A CMap file defines
 * the relationship between a character code and the character description
 * <p/>
 * Character selectors are always CIDs in a CIDFont. A CMap serves a function
 * analogous to the Encoding dictionary for a simple font. The CMap does not
 * refer directly to a specific CIDFont; instead, it is combined with it as part
 * of a CIDkeyed font, represented in PDF as a Type 0 font dictionary.   Within
 * the CMap, the character mappings refer to the associated CIDFont by font
 * number, which in PDF is always 0.
 *
 * @since 1.0
 */
class CMap extends Dictionary implements org.icepdf.core.pobjects.fonts.CMap {

    private static final Logger logger = Logger.getLogger(CMap.class.toString());

    ;

    /**
     * Dictionary containing entries that define the character collection  for
     * the CIDFont or CIDFonts associate with the CMap.  Specifically the
     * character collections registry, ordering and supplement is defined.
     */
    private Map cIdSystemInfo;

    /**
     * PostScript name of the CMap.
     */
    private String cMapName;

    /**
     * defines changes to the internal organization of CMap files or the
     * semantics of CMap operators. The CMapType of CMaps described in
     * this document.
     * cMapType = 2 - indicates a ToUnicode cmap
     * cMapType = 1 - indicates a CMap object
     * cMapType = 0 - not sure yet, maybe CMap with external CMap reference
     */
    private float cMapType;

    /**
     * The name of a predefined CMap, or a stream containing a CMap, that
     * is to be used as the base for this CMap. This allows the CMap to
     * be defined differentially, specifying only the character mappings
     * that differ from the base CMap.
     */
    private Object useCMap;

    /**
     * The WMode dictionary entry controls whether the CID-keyed font writes
     * horizontally or vertically. It indicates which set of metrics will be
     * used when a base font is shown. An entry of 0 defines horizontal
     * writing from left to right; an entry of 1 defines vertical writing
     * from top to bottom.
     */
    private int wMode;

    /**
     * Defines the source character code range.  Source CMap references must
     * be in this range.
     */
    private int[][] codeSpaceRange;

    /**
     * Defines mappings from character codes to Unicode characters in the
     * associated font. Expressed in UTF-16BE encoding.
     */
    private Map<Integer, Integer> bfChars;

    /**
     * Defines mappings from character codes to Unicode character ranges.
     * Expressed in UTF-16BE encoding.
     */
    private CMapRange[] bfRange;

    /**
     * Define mappings of individual input character codes to CIDS in the
     * associated CIDFont.
     */
    private Map cIdChars;

    /**
     * Similar to cIdChars but defines ranges of input codes.
     */
    private Map cIdRange;

    /**
     * Define mappings if the normal mapping produces a CID for which no glyph
     * in the associated CIDFont
     */
    private Hashtable notDefChars;

    /**
     * Similar to notDefChars but defines ranges of input codes.
     */
    private Hashtable notDefRange;

    /**
     * Stream containing the embbeded CMap
     */
    private Stream cMapStream;

    private InputStream cMapInputStream;

    /**
     * Create a new CMap instance.  If the CMap is created from a named object
     * the dictionary property will be populated with values for the keys
     * Type, CMapName and CIDSystemInfo which are also repeated in the CMap
     * file itself. If the CMap file was created from a Font object then they
     * previously mentioned keys values must be parsed from the CMap file.
     *
     * @param library pointer to default library containing all document objects
     * @param entries hashtable containing all of the dictionary properties associated
     *                with this object.  The hashtable will be empty if this object
     *                was created via a Font objects ToUnicode key.
     * @param cMapStream stream containing CMap data.
     */
    public CMap(Library library, Hashtable entries, Stream cMapStream) {
        super(library, entries);
        this.cMapStream = cMapStream;
    }

    public CMap(Library l, Hashtable h, InputStream cMapInputStream) {
        super(l, h);
        this.cMapInputStream = cMapInputStream;
    }

    /**
     * Start the parsing of the CMap file.  Once completed, all necessary data
     * should be captured from the CMap file.
     * <p/>
     * Simple CMap
     * /CIDInit /ProcSet findresource
     * begin
     * 12 dict begin
     * begincmap
     * /CIDSystemInfo <<
     * /Registry (Adobe)
     * /Ordering (UCS)
     * /Supplement 0
     * >> def
     * /CMapName /Adobe-Identity-UCS def
     * /CMapType 2 def
     * 1 begincodespacerange
     * <00> <FF>
     * endcodespacerange
     * 7 beginbfchar
     * <01> <0054>
     * <02> <0065>
     * <03> <0073>
     * <04> <0074>
     * <05> <0069>
     * <06> <006E>
     * <07> <0067>
     * endbfchar
     * 2 beginbfrange
     * <0000> <005E> <0020>
     * <005F> <0061>[<00660066> <0066069> <00660066006C>]
     * endbfrange
     * endcmap
     * CMapName currentdict /CMap defineresource pop
     * end
     * end
     */
    public void init() {
        try {
            if (cMapInputStream == null) {
                cMapInputStream = cMapStream.getInputStreamForDecodedStreamBytes();
            }
            Parser parser = new Parser(cMapInputStream);
            Object previousToken = null;
            while (true) {
                Object token = parser.getStreamObject();
                if (token == null) {
                    break;
                }
                String nameString = token.toString();
                if (nameString.toLowerCase().indexOf("cidsysteminfo") >= 0) {
                    token = parser.getStreamObject();
                    cIdSystemInfo = (Hashtable) token;
                    token = parser.getStreamObject();
                }
                if (token instanceof Name) {
                    nameString = token.toString();
                    if (nameString.toLowerCase().indexOf("cmapname") >= 0) {
                        token = parser.getStreamObject();
                        cMapName = token.toString();
                        token = parser.getStreamObject();
                    }
                    if (nameString.toLowerCase().indexOf("cmaptype") >= 0) {
                        token = parser.getStreamObject();
                        cMapType = Float.parseFloat(token.toString());
                        token = parser.getStreamObject();
                    }
                    if (nameString.toLowerCase().indexOf("usemap") >= 0) {
                    }
                }
                if (token instanceof String) {
                    String stringToken = (String) token;
                    if (stringToken.equalsIgnoreCase("begincodespacerange")) {
                        int numberOfRanges = (int) Float.parseFloat(previousToken.toString());
                        codeSpaceRange = new int[numberOfRanges][2];
                        for (int i = 0; i < numberOfRanges; i++) {
                            token = parser.getStreamObject();
                            StringObject hexToken = (StringObject) token;
                            int startRange = hexToken.getUnsignedInt(0, hexToken.getLength());
                            token = parser.getStreamObject();
                            hexToken = (StringObject) token;
                            int endRange = hexToken.getUnsignedInt(0, hexToken.getLength());
                            codeSpaceRange[i][0] = startRange;
                            codeSpaceRange[i][1] = endRange;
                        }
                    }
                    if (stringToken.equalsIgnoreCase("beginbfchar")) {
                        int numberOfbfChar = (int) Float.parseFloat(previousToken.toString());
                        bfChars = new HashMap<Integer, Integer>(numberOfbfChar);
                        for (int i = 0; i < numberOfbfChar; i++) {
                            token = parser.getStreamObject();
                            StringObject hexToken = (StringObject) token;
                            Integer key = hexToken.getUnsignedInt(0, hexToken.getLength());
                            token = parser.getStreamObject();
                            hexToken = (StringObject) token;
                            Integer value = null;
                            try {
                                value = hexToken.getUnsignedInt(0, hexToken.getLength());
                            } catch (NumberFormatException e) {
                                logger.log(Level.FINE, "CMAP: ", e);
                            }
                            bfChars.put(key, value);
                        }
                    }
                    if (stringToken.equalsIgnoreCase("beginbfrange")) {
                        int numberOfbfRanges = (int) Float.parseFloat(previousToken.toString());
                        bfRange = new CMapRange[numberOfbfRanges];
                        for (int i = 0; i < numberOfbfRanges; i++) {
                            token = parser.getStreamObject();
                            StringObject hexToken = (StringObject) token;
                            Integer startRange = hexToken.getUnsignedInt(0, hexToken.getLength());
                            token = parser.getStreamObject();
                            hexToken = (StringObject) token;
                            Integer endRange = hexToken.getUnsignedInt(0, hexToken.getLength());
                            token = parser.getStreamObject();
                            if (token instanceof Vector) {
                                bfRange[i] = new CMapRange(startRange, endRange, (Vector) token);
                            } else {
                                hexToken = (StringObject) token;
                                Integer offset = hexToken.getUnsignedInt(0, hexToken.getLength());
                                bfRange[i] = new CMapRange(startRange, endRange, offset);
                            }
                        }
                    }
                    if (stringToken.equalsIgnoreCase("begincidchar")) {
                    }
                    if (stringToken.equalsIgnoreCase("begincidrange")) {
                    }
                    if (stringToken.equalsIgnoreCase("beginnotdefchar")) {
                    }
                    if (stringToken.equalsIgnoreCase("beginnotdefrange")) {
                    }
                }
                previousToken = token;
            }
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.FINE, "CMap parsing error", e);
        } catch (IOException e) {
        }
        if (cMapInputStream != null) {
            try {
                cMapInputStream.close();
            } catch (IOException e) {
                logger.log(Level.FINE, "Error clossing cmap stream", e);
            }
        }
    }

    /**
     * The method is called when ever a character code is incounter that has a
     * FontDescriptor that defines a ToUnicode CMap.  The <code>charMap</code>
     * is mapped according to the CMap rules and a mapped character code is
     * returned.
     *
     * @param charMap value to map against the ToUnicode CMap
     * @return mapped character value.
     */
    public char toSelector(char charMap) {
        if (cIdSystemInfo != null) {
            if (bfChars != null) {
                Integer tmp = bfChars.get(new Integer(charMap));
                if (tmp != null) {
                    return (char) tmp.intValue();
                }
            }
            if (bfRange != null) {
                for (CMapRange aBfRange : bfRange) {
                    if (aBfRange.inRange(charMap)) {
                        return (char) aBfRange.getCMapValue(charMap);
                    }
                }
            }
        }
        return charMap;
    }

    /**
     * Help class to store data for a CMap bfrange value.  CMap bfranges come
     * in two flavours but there both share a start and end range value.
     * Characters that fall in this range are mapped with wither the offset
     * value or to an offset vector.
     * <p/>
     * Basic offset Mapping
     * <0000> <005E> <0020>  -  values that are between <0000> and <005E> are
     * offset by <0020> ie  <0001> maps to <0021>, <004f> maps to <006f> and
     * <0006F> would not be mapped by this range.
     * <p/>
     * Vector offset Mapping
     * <005F> <0061>[<00660066> <0066069> <00660066006C>] - values that are
     * between <005f> and <0067> are mapped directly to an offset index in the
     * array.  ie <005f> maps to <00660066> and <0060> maps to <0066069> and
     * finally <0061> maps to <00660066006C>.
     */
    class CMapRange {

        int startRange = 0;

        int endRange = 0;

        int offsetValue = 0;

        Vector offsetVecor = null;

        /**
         * Create a new instance of a CMapRange, when it is a simple range
         * mapping with an offset value.
         *
         * @param startRange  start range of mapping
         * @param endRange    end range of mapping
         * @param offsetValue value to offset a mapping by
         */
        public CMapRange(int startRange, int endRange, int offsetValue) {
            this.startRange = startRange;
            this.endRange = endRange;
            this.offsetValue = offsetValue;
        }

        /**
         * Creat new instance of a CMapRange, when it is a more vector range
         * mapping.  Each valid number in the range maps the a corresponding
         * value in the vector based on the numbers offset from the start range.
         *
         * @param startRange  start range of mapping
         * @param endRange    end range of the mapping
         * @param offsetVecor offset mappped vector
         */
        public CMapRange(int startRange, int endRange, Vector offsetVecor) {
            this.startRange = startRange;
            this.endRange = endRange;
            this.offsetVecor = offsetVecor;
        }

        /**
         * Checks if a <code>value</code> is in the CMap bfrange.
         *
         * @param value value to check for containment
         * @return true if the cmap falls inside one of the bfranges, false
         *         otherwise.
         */
        public boolean inRange(int value) {
            return (value >= startRange && value <= endRange);
        }

        /**
         * Get the mapped value of <code>value</code>.  It is assumed that
         * inRange is called before this method is called.  If the
         * <code>value</code> is not in the range then a value of -1 is returned
         *
         * @param value value to find corresponding CMap for
         * @return the mapped CMap value for <code>value</code>, -1 if the
         *         <code>value</code> can not be mapped.
         */
        public int getCMapValue(int value) {
            int tmp = -1;
            if (offsetVecor == null) {
                tmp = offsetValue + (value - startRange);
            } else {
                String offset = offsetVecor.elementAt(value - startRange).toString();
                tmp = Integer.parseInt(offset);
            }
            return tmp;
        }
    }
}
