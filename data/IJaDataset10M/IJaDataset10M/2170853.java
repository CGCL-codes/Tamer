package org.apache.fop.fonts;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Reads a TrueType file or a TrueType Collection.
 * The TrueType spec can be found at the Microsoft
 * Typography site: http://www.microsoft.com/truetype/
 */
public class TTFFile {

    static final byte NTABS = 24;

    static final int NMACGLYPHS = 258;

    static final int MAX_CHAR_CODE = 255;

    static final int ENC_BUF_SIZE = 1024;

    static String encoding = "WinAnsiEncoding";

    short firstChar = 0;

    boolean is_embeddable = true;

    boolean hasSerifs = true;

    HashMap dirTabs;

    HashMap kerningTab;

    HashMap ansiKerningTab;

    ArrayList cmaps;

    ArrayList unicodeMapping;

    int upem;

    int nhmtx;

    int post_format;

    int loca_format;

    long lastLoca = 0;

    int nglyphs;

    int nmglyphs;

    TTFMtxEntry mtx_tab[];

    int[] mtx_encoded = null;

    String fontName = "";

    String fullName = "";

    String notice = "";

    String familyName = "";

    String subFamilyName = "";

    long italicAngle = 0;

    long isFixedPitch = 0;

    int fontBBox1 = 0;

    int fontBBox2 = 0;

    int fontBBox3 = 0;

    int fontBBox4 = 0;

    int capHeight = 0;

    int underlinePosition = 0;

    int underlineThickness = 0;

    int xHeight = 0;

    int ascender = 0;

    int descender = 0;

    short lastChar = 0;

    int ansiWidth[];

    HashMap ansiIndex;

    private TTFDirTabEntry currentDirTab;

    /**
     * Position inputstream to position indicated
     * in the dirtab offset + offset
     */
    void seek_tab(FontFileReader in, String name, long offset) throws IOException {
        TTFDirTabEntry dt = (TTFDirTabEntry) dirTabs.get(name);
        if (dt == null) {
            System.out.println("Dirtab " + name + " not found.");
        } else {
            in.seek_set(dt.offset + offset);
            this.currentDirTab = dt;
        }
    }

    /**
     * Convert from truetype unit to pdf unit based on the
     * unitsPerEm field in the "head" table
     * @param n truetype unit
     * @return pdf unit
     */
    int get_ttf_funit(int n) {
        int ret;
        if (n < 0) {
            long rest1 = n % upem;
            long storrest = 1000 * rest1;
            long ledd2 = rest1 / storrest;
            ret = -((-1000 * n) / upem - (int) ledd2);
        } else {
            ret = (n / upem) * 1000 + ((n % upem) * 1000) / upem;
        }
        return ret;
    }

    /**
     * Read the cmap table,
     * return false if the table is not present or only unsupported
     * tables are present. Currently only unicode cmaps are supported.
     * Set the unicodeIndex in the TTFMtxEntries and fills in the
     * cmaps ArrayList.
     */
    private boolean readCMAP(FontFileReader in) throws IOException {
        unicodeMapping = new ArrayList();
        int mtxPtr = 0;
        seek_tab(in, "cmap", 2);
        int num_cmap = in.readTTFUShort();
        long cmap_unioffset = 0;
        for (int i = 0; i < num_cmap; i++) {
            int cmap_pid = in.readTTFUShort();
            int cmap_eid = in.readTTFUShort();
            long cmap_offset = in.readTTFULong();
            if (cmap_pid == 3 && cmap_eid == 1) {
                cmap_unioffset = cmap_offset;
            }
        }
        if (cmap_unioffset <= 0) {
            System.out.println("Unicode cmap table not present");
            return false;
        }
        seek_tab(in, "cmap", cmap_unioffset);
        int cmap_format = in.readTTFUShort();
        int cmap_length = in.readTTFUShort();
        if (cmap_format == 4) {
            in.skip(2);
            int cmap_segCountX2 = in.readTTFUShort();
            int cmap_searchRange = in.readTTFUShort();
            int cmap_entrySelector = in.readTTFUShort();
            int cmap_rangeShift = in.readTTFUShort();
            int cmap_endCounts[] = new int[cmap_segCountX2 / 2];
            int cmap_startCounts[] = new int[cmap_segCountX2 / 2];
            int cmap_deltas[] = new int[cmap_segCountX2 / 2];
            int cmap_rangeOffsets[] = new int[cmap_segCountX2 / 2];
            for (int i = 0; i < (cmap_segCountX2 / 2); i++) {
                cmap_endCounts[i] = in.readTTFUShort();
            }
            in.skip(2);
            for (int i = 0; i < (cmap_segCountX2 / 2); i++) {
                cmap_startCounts[i] = in.readTTFUShort();
            }
            for (int i = 0; i < (cmap_segCountX2 / 2); i++) {
                cmap_deltas[i] = in.readTTFShort();
            }
            for (int i = 0; i < (cmap_segCountX2 / 2); i++) {
                cmap_rangeOffsets[i] = in.readTTFUShort();
            }
            int glyphIdArrayOffset = in.getCurrentPos();
            for (int i = 0; i < cmap_startCounts.length; i++) {
                for (int j = cmap_startCounts[i]; j <= cmap_endCounts[i]; j++) {
                    if (j < 256 && j > lastChar) {
                        lastChar = (short) j;
                    }
                    if (mtxPtr < mtx_tab.length) {
                        int glyphIdx;
                        if (cmap_rangeOffsets[i] != 0 && j != 65535) {
                            int glyphOffset = glyphIdArrayOffset + ((cmap_rangeOffsets[i] / 2) + (j - cmap_startCounts[i]) + (i) - cmap_segCountX2 / 2) * 2;
                            in.seek_set(glyphOffset);
                            glyphIdx = (in.readTTFUShort() + cmap_deltas[i]) & 0xffff;
                            unicodeMapping.add(new UnicodeMapping(glyphIdx, j));
                            mtx_tab[glyphIdx].unicodeIndex.add(new Integer(j));
                            ArrayList v = (ArrayList) ansiIndex.get(new Integer(j));
                            if (v != null) {
                                for (int k = 0; k < v.size(); k++) {
                                    Integer aIdx = (Integer) v.get(k);
                                    ansiWidth[aIdx.intValue()] = mtx_tab[glyphIdx].wx;
                                }
                            }
                        } else {
                            glyphIdx = (j + cmap_deltas[i]) & 0xffff;
                            if (glyphIdx < mtx_tab.length) {
                                mtx_tab[glyphIdx].unicodeIndex.add(new Integer(j));
                            } else {
                                System.out.println("Glyph " + glyphIdx + " out of range: " + mtx_tab.length);
                            }
                            unicodeMapping.add(new UnicodeMapping(glyphIdx, j));
                            if (glyphIdx < mtx_tab.length) {
                                mtx_tab[glyphIdx].unicodeIndex.add(new Integer(j));
                            } else {
                                System.out.println("Glyph " + glyphIdx + " out of range: " + mtx_tab.length);
                            }
                            ArrayList v = (ArrayList) ansiIndex.get(new Integer(j));
                            if (v != null) {
                                for (int k = 0; k < v.size(); k++) {
                                    Integer aIdx = (Integer) v.get(k);
                                    ansiWidth[aIdx.intValue()] = mtx_tab[glyphIdx].wx;
                                }
                            }
                        }
                        if (glyphIdx < mtx_tab.length) {
                            if (mtx_tab[glyphIdx].unicodeIndex.size() < 2) {
                                mtxPtr++;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Print first char/last char
     */
    private void print_max_min() {
        int min = 255;
        int max = 0;
        for (int i = 0; i < mtx_tab.length; i++) {
            if (mtx_tab[i].index < min) min = mtx_tab[i].index;
            if (mtx_tab[i].index > max) max = mtx_tab[i].index;
        }
        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
    }

    public void readFont(FontFileReader in) throws IOException {
        readFont(in, (String) null);
    }

    /**
     * initialize the ansiWidths array (for winAnsiEncoding)
     * and fill with the missingwidth
     */
    private void initAnsiWidths() {
        ansiWidth = new int[256];
        for (int i = 0; i < 256; i++) {
            ansiWidth[i] = mtx_tab[0].wx;
        }
        ansiIndex = new HashMap();
        for (int i = 32; i < Glyphs.winAnsiEncoding.length; i++) {
            Integer ansi = new Integer(i);
            Integer uni = new Integer((int) Glyphs.winAnsiEncoding[i]);
            ArrayList v = (ArrayList) ansiIndex.get(uni);
            if (v == null) {
                v = new ArrayList();
                ansiIndex.put(uni, v);
            }
            v.add(ansi);
        }
    }

    /**
     * Read the font data
     * If the fontfile is a TrueType Collection (.ttc file)
     * The name of the font to read data for must be supplied,
     * else the name is ignored
     */
    public void readFont(FontFileReader in, String name) throws IOException {
        if (!checkTTC(in, name, true)) {
            if (name == null) {
                throw new IllegalArgumentException("For TrueType collection you must specify which font " + "to select (-ttcname)");
            } else {
                throw new IOException("Name does not exist in the TrueType collection: " + name);
            }
        }
        readDirTabs(in);
        readFontHeader(in);
        getNumGlyphs(in);
        System.out.println("Number of glyphs in font: " + nglyphs);
        readHorizontalHeader(in);
        readHorizontalMetrics(in);
        initAnsiWidths();
        readPostscript(in);
        readOS2(in);
        readIndexToLocation(in);
        readGlyf(in);
        readName(in);
        readPCLT(in);
        readCMAP(in);
        createCMaps();
        readKerning(in);
    }

    private void createCMaps() {
        cmaps = new ArrayList();
        TTFCmapEntry tce = new TTFCmapEntry();
        UnicodeMapping um = (UnicodeMapping) unicodeMapping.get(0);
        UnicodeMapping lastMapping = um;
        tce.unicodeStart = um.uIdx;
        tce.glyphStartIndex = um.gIdx;
        for (int i = 1; i < unicodeMapping.size(); i++) {
            um = (UnicodeMapping) unicodeMapping.get(i);
            if (((lastMapping.uIdx + 1) != um.uIdx) || ((lastMapping.gIdx + 1) != um.gIdx)) {
                tce.unicodeEnd = lastMapping.uIdx;
                cmaps.add(tce);
                tce = new TTFCmapEntry();
                tce.unicodeStart = um.uIdx;
                tce.glyphStartIndex = um.gIdx;
            }
            lastMapping = um;
        }
        tce.unicodeEnd = um.uIdx;
        cmaps.add(tce);
    }

    public void printStuff() {
        System.out.println("Font name: " + fontName);
        System.out.println("Full name: " + fullName);
        System.out.println("Family name: " + familyName);
        System.out.println("Subfamily name: " + subFamilyName);
        System.out.println("Notice:    " + notice);
        System.out.println("xHeight:   " + (int) get_ttf_funit(xHeight));
        System.out.println("capheight: " + (int) get_ttf_funit(capHeight));
        int italic = (int) (italicAngle >> 16);
        System.out.println("Italic: " + italic);
        System.out.print("ItalicAngle: " + (short) (italicAngle / 0x10000));
        if ((italicAngle % 0x10000) > 0) {
            System.out.print("." + (short) ((italicAngle % 0x10000) * 1000) / 0x10000);
        }
        System.out.println();
        System.out.println("Ascender:    " + get_ttf_funit(ascender));
        System.out.println("Descender:   " + get_ttf_funit(descender));
        System.out.println("FontBBox:    [" + (int) get_ttf_funit(fontBBox1) + " " + (int) get_ttf_funit(fontBBox2) + " " + (int) get_ttf_funit(fontBBox3) + " " + (int) get_ttf_funit(fontBBox4) + "]");
    }

    public static void main(String[] args) {
        try {
            TTFFile ttfFile = new TTFFile();
            FontFileReader reader = new FontFileReader(args[0]);
            String name = null;
            if (args.length >= 2) {
                name = args[1];
            }
            ttfFile.readFont(reader, name);
            ttfFile.printStuff();
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        }
    }

    public String getWindowsName() {
        return new String(familyName + "," + subFamilyName);
    }

    public String getPostscriptName() {
        if ("Regular".equals(subFamilyName) || "Roman".equals(subFamilyName)) {
            return familyName;
        } else {
            return familyName + "," + subFamilyName;
        }
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getCharSetName() {
        return encoding;
    }

    public int getCapHeight() {
        return (int) get_ttf_funit(capHeight);
    }

    public int getXHeight() {
        return (int) get_ttf_funit(xHeight);
    }

    public int getFlags() {
        int flags = 32;
        if (italicAngle != 0) {
            flags = flags | 64;
        }
        if (isFixedPitch != 0) {
            flags = flags | 2;
        }
        if (hasSerifs) {
            flags = flags | 1;
        }
        return flags;
    }

    public String getStemV() {
        return "0";
    }

    public String getItalicAngle() {
        String ia = Short.toString((short) (italicAngle / 0x10000));
        return ia;
    }

    public int[] getFontBBox() {
        int[] fbb = new int[4];
        fbb[0] = (int) get_ttf_funit(fontBBox1);
        fbb[1] = (int) get_ttf_funit(fontBBox2);
        fbb[2] = (int) get_ttf_funit(fontBBox3);
        fbb[3] = (int) get_ttf_funit(fontBBox4);
        return fbb;
    }

    public int getLowerCaseAscent() {
        return (int) get_ttf_funit(ascender);
    }

    public int getLowerCaseDescent() {
        return (int) get_ttf_funit(descender);
    }

    public short getLastChar() {
        return lastChar;
    }

    public short getFirstChar() {
        return firstChar;
    }

    public int[] getWidths() {
        int[] wx = new int[mtx_tab.length];
        for (int i = 0; i < wx.length; i++) {
            wx[i] = (int) get_ttf_funit(mtx_tab[i].wx);
        }
        return wx;
    }

    public int getCharWidth(int idx) {
        return (int) get_ttf_funit(ansiWidth[idx]);
    }

    public HashMap getKerning() {
        return kerningTab;
    }

    public HashMap getAnsiKerning() {
        return ansiKerningTab;
    }

    public boolean isEmbeddable() {
        return is_embeddable;
    }

    /**
     * Read Table Directory from the current position in the
     * FontFileReader and fill the global HashMap dirTabs
     * with the table name (String) as key and a TTFDirTabEntry
     * as value.
     */
    protected void readDirTabs(FontFileReader in) throws IOException {
        in.skip(4);
        int ntabs = in.readTTFUShort();
        in.skip(6);
        dirTabs = new HashMap();
        TTFDirTabEntry[] pd = new TTFDirTabEntry[ntabs];
        for (int i = 0; i < ntabs; i++) {
            pd[i] = new TTFDirTabEntry();
            dirTabs.put(pd[i].read(in), pd[i]);
        }
    }

    /**
     * Read the "head" table, this reads the bounding box and
     * sets the upem (unitsPerEM) variable
     */
    protected void readFontHeader(FontFileReader in) throws IOException {
        seek_tab(in, "head", 2 * 4 + 2 * 4 + 2);
        upem = in.readTTFUShort();
        in.skip(16);
        fontBBox1 = in.readTTFShort();
        fontBBox2 = in.readTTFShort();
        fontBBox3 = in.readTTFShort();
        fontBBox4 = in.readTTFShort();
        in.skip(2 + 2 + 2);
        loca_format = in.readTTFShort();
    }

    /**
     * Read the number of glyphs from the "maxp" table
     */
    protected void getNumGlyphs(FontFileReader in) throws IOException {
        seek_tab(in, "maxp", 4);
        nglyphs = in.readTTFUShort();
    }

    /**
     * Read the "hhea" table to find the ascender and descender and
     * size of "hmtx" table, i.e. a fixed size font might have only
     * one width
     */
    protected void readHorizontalHeader(FontFileReader in) throws IOException {
        seek_tab(in, "hhea", 4);
        ascender = in.readTTFShort();
        descender = in.readTTFShort();
        in.skip(2 + 2 + 3 * 2 + 8 * 2);
        nhmtx = in.readTTFUShort();
        if (ascender == 0 || descender == 0) {
            seek_tab(in, "OS/2", 68);
            if (this.currentDirTab.length >= 78) {
                ascender = in.readTTFShort();
                descender = in.readTTFShort();
            }
        }
    }

    /**
     * Read "hmtx" table and put the horizontal metrics
     * in the mtx_tab array. If the number of metrics is less
     * than the number of glyphs (eg fixed size fonts), extend
     * the mtx_tab array and fill in the missing widths
     */
    protected void readHorizontalMetrics(FontFileReader in) throws IOException {
        seek_tab(in, "hmtx", 0);
        int mtx_size = (nglyphs > nhmtx) ? nglyphs : nhmtx;
        mtx_tab = new TTFMtxEntry[mtx_size];
        for (int i = 0; i < mtx_size; i++) {
            mtx_tab[i] = new TTFMtxEntry();
        }
        for (int i = 0; i < nhmtx; i++) {
            mtx_tab[i].wx = in.readTTFUShort();
            mtx_tab[i].lsb = in.readTTFShort();
        }
        if (nhmtx < mtx_size) {
            int lastWidth = mtx_tab[nhmtx - 1].wx;
            for (int i = nhmtx; i < mtx_size; i++) {
                mtx_tab[i].wx = lastWidth;
                mtx_tab[i].lsb = in.readTTFShort();
            }
        }
    }

    /**
     * Read the "post" table
     * containing the postscript names of the glyphs.
     */
    private final void readPostscript(FontFileReader in) throws IOException {
        String[] ps_glyphs_buf;
        int i, k, l;
        seek_tab(in, "post", 0);
        post_format = in.readTTFLong();
        italicAngle = in.readTTFULong();
        underlinePosition = in.readTTFShort();
        underlineThickness = in.readTTFShort();
        isFixedPitch = in.readTTFULong();
        in.skip(4 * 4);
        switch(post_format) {
            case 0x00010000:
                for (i = 0; i < Glyphs.mac_glyph_names.length; i++) {
                    mtx_tab[i].name = Glyphs.mac_glyph_names[i];
                }
                break;
            case 0x00020000:
                int numGlyphStrings = 0;
                l = in.readTTFUShort();
                for (i = 0; i < l; i++) {
                    mtx_tab[i].index = in.readTTFUShort();
                    if (mtx_tab[i].index > 257) numGlyphStrings++;
                }
                ps_glyphs_buf = new String[numGlyphStrings];
                for (i = 0; i < ps_glyphs_buf.length; i++) {
                    ps_glyphs_buf[i] = in.readTTFString(in.readTTFUByte());
                }
                for (i = 0; i < l; i++) {
                    if (mtx_tab[i].index < NMACGLYPHS) {
                        mtx_tab[i].name = Glyphs.mac_glyph_names[mtx_tab[i].index];
                    } else {
                        if (!mtx_tab[i].isIndexReserved()) {
                            k = mtx_tab[i].index - NMACGLYPHS;
                            mtx_tab[i].name = ps_glyphs_buf[k];
                        }
                    }
                }
                break;
            case 0x00030000:
                System.out.println("Postscript format 3");
                break;
            default:
                System.out.println("Unknown Postscript format : " + post_format);
        }
    }

    /**
     * Read the "OS/2" table
     */
    private final void readOS2(FontFileReader in) throws IOException {
        if (dirTabs.get("OS/2") != null) {
            seek_tab(in, "OS/2", 2 * 4);
            int fsType = in.readTTFUShort();
            if (fsType == 2) {
                is_embeddable = false;
            } else {
                is_embeddable = true;
            }
        } else {
            is_embeddable = true;
        }
    }

    /**
     * Read the "loca" table
     */
    protected final void readIndexToLocation(FontFileReader in) throws IOException {
        seek_tab(in, "loca", 0);
        for (int i = 0; i < nglyphs; i++) {
            mtx_tab[i].offset = (loca_format == 1 ? in.readTTFULong() : (in.readTTFUShort() << 1));
        }
        lastLoca = (loca_format == 1 ? in.readTTFULong() : (in.readTTFUShort() << 1));
    }

    /**
     * Read the "glyf" table to find the bounding boxes
     */
    private final void readGlyf(FontFileReader in) throws IOException {
        TTFDirTabEntry dirTab = (TTFDirTabEntry) dirTabs.get("glyf");
        for (int i = 0; i < (nglyphs - 1); i++) {
            if (mtx_tab[i].offset != mtx_tab[i + 1].offset) {
                in.seek_set(dirTab.offset + mtx_tab[i].offset);
                in.skip(2);
                mtx_tab[i].bbox[0] = in.readTTFShort();
                mtx_tab[i].bbox[1] = in.readTTFShort();
                mtx_tab[i].bbox[2] = in.readTTFShort();
                mtx_tab[i].bbox[3] = in.readTTFShort();
            } else {
                mtx_tab[i].bbox[0] = mtx_tab[0].bbox[0];
                mtx_tab[i].bbox[1] = mtx_tab[0].bbox[1];
                mtx_tab[i].bbox[2] = mtx_tab[0].bbox[2];
                mtx_tab[i].bbox[3] = mtx_tab[0].bbox[3];
            }
        }
        long n = ((TTFDirTabEntry) dirTabs.get("glyf")).offset;
        for (int i = 0; i < nglyphs; i++) {
            if ((i + 1) >= mtx_tab.length || mtx_tab[i].offset != mtx_tab[i + 1].offset) {
                in.seek_set(n + mtx_tab[i].offset);
                in.skip(2);
                mtx_tab[i].bbox[0] = in.readTTFShort();
                mtx_tab[i].bbox[1] = in.readTTFShort();
                mtx_tab[i].bbox[2] = in.readTTFShort();
                mtx_tab[i].bbox[3] = in.readTTFShort();
            } else {
                mtx_tab[i].bbox[0] = mtx_tab[0].bbox[0];
                mtx_tab[i].bbox[1] = mtx_tab[0].bbox[0];
                mtx_tab[i].bbox[2] = mtx_tab[0].bbox[0];
                mtx_tab[i].bbox[3] = mtx_tab[0].bbox[0];
            }
        }
    }

    /**
     * Read the "name" table
     */
    private final void readName(FontFileReader in) throws IOException {
        seek_tab(in, "name", 2);
        int i = in.getCurrentPos();
        int n = in.readTTFUShort();
        int j = in.readTTFUShort() + i - 2;
        i += 2 * 2;
        while (n-- > 0) {
            in.seek_set(i);
            int platform_id = in.readTTFUShort();
            int encoding_id = in.readTTFUShort();
            int language_id = in.readTTFUShort();
            int k = in.readTTFUShort();
            int l = in.readTTFUShort();
            if (((platform_id == 1 || platform_id == 3) && (encoding_id == 0 || encoding_id == 1)) && (k == 1 || k == 2 || k == 0 || k == 4 || k == 6)) {
                in.seek_set(j + in.readTTFUShort());
                String txt = in.readTTFString(l);
                switch(k) {
                    case 0:
                        notice = txt;
                        break;
                    case 1:
                        familyName = txt;
                        break;
                    case 2:
                        subFamilyName = txt;
                        break;
                    case 4:
                        fullName = txt;
                        break;
                    case 6:
                        fontName = txt;
                        break;
                }
                if (!notice.equals("") && !fullName.equals("") && !fontName.equals("") && !familyName.equals("") && !subFamilyName.equals("")) {
                    break;
                }
            }
            i += 6 * 2;
        }
    }

    /**
     * Read the "PCLT" table to find xHeight and capHeight
     */
    private final void readPCLT(FontFileReader in) throws IOException {
        TTFDirTabEntry dirTab = (TTFDirTabEntry) dirTabs.get("PCLT");
        if (dirTab != null) {
            in.seek_set(dirTab.offset + 4 + 4 + 2);
            xHeight = in.readTTFUShort();
            in.skip(2 * 2);
            capHeight = in.readTTFUShort();
            in.skip(2 + 16 + 8 + 6 + 1 + 1);
            int serifStyle = in.readTTFUByte();
            serifStyle = serifStyle >> 6;
            serifStyle = serifStyle & 3;
            if (serifStyle == 1) {
                hasSerifs = false;
            } else {
                hasSerifs = true;
            }
        } else {
            for (int i = 0; i < mtx_tab.length; i++) {
                if ("H".equals(mtx_tab[i].name)) {
                    capHeight = mtx_tab[i].bbox[3] - mtx_tab[i].bbox[1];
                }
            }
        }
    }

    /**
     * Read the kerning table, create a table for both CIDs and
     * winAnsiEncoding
     */
    private final void readKerning(FontFileReader in) throws IOException {
        kerningTab = new HashMap();
        ansiKerningTab = new HashMap();
        TTFDirTabEntry dirTab = (TTFDirTabEntry) dirTabs.get("kern");
        if (dirTab != null) {
            seek_tab(in, "kern", 2);
            for (int n = in.readTTFUShort(); n > 0; n--) {
                in.skip(2 * 2);
                int k = in.readTTFUShort();
                if (!((k & 1) != 0) || (k & 2) != 0 || (k & 4) != 0) {
                    return;
                }
                if ((k >> 8) != 0) {
                    continue;
                }
                k = in.readTTFUShort();
                in.skip(3 * 2);
                while (k-- > 0) {
                    int i = in.readTTFUShort();
                    int j = in.readTTFUShort();
                    int kpx = in.readTTFShort();
                    if (kpx != 0) {
                        Integer iObj = new Integer(i);
                        HashMap adjTab = (HashMap) kerningTab.get(iObj);
                        if (adjTab == null) {
                            adjTab = new HashMap();
                        }
                        adjTab.put(new Integer(j), new Integer((int) get_ttf_funit(kpx)));
                        kerningTab.put(iObj, adjTab);
                    }
                }
            }
            for (Iterator ae = kerningTab.keySet().iterator(); ae.hasNext(); ) {
                Integer cidKey = (Integer) ae.next();
                HashMap akpx = new HashMap();
                HashMap ckpx = (HashMap) kerningTab.get(cidKey);
                for (Iterator aee = ckpx.keySet().iterator(); aee.hasNext(); ) {
                    Integer cidKey2 = (Integer) aee.next();
                    Integer kern = (Integer) ckpx.get(cidKey2);
                    ArrayList unicodeIndex = mtx_tab[cidKey2.intValue()].unicodeIndex;
                    for (int i = 0; i < unicodeIndex.size(); i++) {
                        Integer unicodeKey = (Integer) unicodeIndex.get(i);
                        Integer[] ansiKeys = unicodeToWinAnsi(unicodeKey.intValue());
                        for (int u = 0; u < ansiKeys.length; u++) {
                            akpx.put(ansiKeys[u], kern);
                        }
                    }
                }
                if (akpx.size() > 0) {
                    ArrayList unicodeIndex = mtx_tab[cidKey.intValue()].unicodeIndex;
                    for (int i = 0; i < unicodeIndex.size(); i++) {
                        Integer unicodeKey = (Integer) unicodeIndex.get(i);
                        Integer[] ansiKeys = unicodeToWinAnsi(unicodeKey.intValue());
                        for (int u = 0; u < ansiKeys.length; u++) {
                            ansiKerningTab.put(ansiKeys[u], akpx);
                        }
                    }
                }
            }
        }
    }

    /**
     * Return a ArrayList with TTFCmapEntry
     */
    public ArrayList getCMaps() {
        return cmaps;
    }

    /**
     * Check if this is a TrueType collection and that the given
     * name exists in the collection.
     * If it does, set offset in fontfile to the beginning of
     * the Table Directory for that font
     * @return true if not collection or font name present, false
     * otherwise
     */
    protected final boolean checkTTC(FontFileReader in, String name, boolean verbose) throws IOException {
        String tag = in.readTTFString(4);
        if ("ttcf".equals(tag)) {
            in.skip(4);
            int numDirectories = (int) in.readTTFULong();
            long[] dirOffsets = new long[numDirectories];
            for (int i = 0; i < numDirectories; i++) {
                dirOffsets[i] = in.readTTFULong();
            }
            if (verbose) {
                System.out.println("This is a TrueType collection file with " + numDirectories + " fonts");
                System.out.println("Containing the following fonts: ");
            }
            boolean found = false;
            long dirTabOffset = 0;
            for (int i = 0; (i < numDirectories); i++) {
                in.seek_set(dirOffsets[i]);
                readDirTabs(in);
                readName(in);
                if (fullName.equals(name)) {
                    found = true;
                    dirTabOffset = dirOffsets[i];
                    if (verbose) {
                        System.out.println("* " + fullName);
                    }
                } else {
                    if (verbose) {
                        System.out.println(fullName);
                    }
                }
                notice = "";
                fullName = "";
                familyName = "";
                fontName = "";
                subFamilyName = "";
            }
            in.seek_set(dirTabOffset);
            return found;
        } else {
            in.seek_set(0);
            return true;
        }
    }

    private Integer[] unicodeToWinAnsi(int unicode) {
        ArrayList ret = new ArrayList();
        for (int i = 32; i < Glyphs.winAnsiEncoding.length; i++) {
            if (unicode == Glyphs.winAnsiEncoding[i]) {
                ret.add(new Integer(i));
            }
        }
        Integer[] itg = new Integer[ret.size()];
        ret.toArray(itg);
        return itg;
    }
}

/**
 * Key-value helper class
 */
class UnicodeMapping {

    int uIdx;

    int gIdx;

    UnicodeMapping(int gIdx, int uIdx) {
        this.uIdx = uIdx;
        this.gIdx = gIdx;
    }
}
