package eu.medsea.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MagicMimeEntry {

    public static final int STRING_TYPE = 1;

    public static final int BELONG_TYPE = 2;

    public static final int SHORT_TYPE = 3;

    public static final int LELONG_TYPE = 4;

    public static final int BESHORT_TYPE = 5;

    public static final int LESHORT_TYPE = 6;

    public static final int BYTE_TYPE = 7;

    public static final int UNKNOWN_TYPE = 20;

    private ArrayList<MagicMimeEntry> subEntries = new ArrayList<MagicMimeEntry>();

    int checkBytesFrom;

    int type;

    String typeStr;

    String content;

    String mimeType;

    String mimeEnc;

    MagicMimeEntry parent;

    boolean isBetween;

    public MagicMimeEntry(List<String> entries) throws InvalidMagicMimeEntryException {
        this(0, null, entries);
    }

    private MagicMimeEntry(int level, MagicMimeEntry parent, List<String> entries) throws InvalidMagicMimeEntryException {
        if (entries == null || entries.size() == 0) {
            return;
        }
        try {
            addEntry((String) entries.get(0));
        } catch (Exception e) {
            throw new InvalidMagicMimeEntryException(entries);
        }
        entries.remove(0);
        this.parent = parent;
        if (parent != null) {
            parent.subEntries.add(this);
        }
        while (entries.size() > 0) {
            int thisLevel = howManyGreaterThans((String) entries.get(0));
            if (thisLevel > level) {
                new MagicMimeEntry(thisLevel, this, entries);
            } else {
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "MimeMagicType: " + checkBytesFrom + ", " + type + ", " + content + ", " + mimeType + ", " + mimeEnc;
    }

    public void traverseAndPrint(String tabs) {
        System.out.println(tabs + toString());
        int len = subEntries.size();
        for (int i = 0; i < len; i++) {
            MagicMimeEntry me = (MagicMimeEntry) subEntries.get(i);
            me.traverseAndPrint(tabs + "\t");
        }
    }

    private int howManyGreaterThans(String aLine) {
        if (aLine == null) {
            return -1;
        }
        int i = 0;
        int len = aLine.length();
        while (i < len) {
            if (aLine.charAt(i) == '>') {
                i++;
            } else {
                break;
            }
        }
        return i;
    }

    void addEntry(String aLine) {
        String trimmed = aLine.replaceAll("^>*", "");
        String[] tokens = trimmed.split("\t");
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < tokens.length; i++) {
            if (!"".equals(tokens[i])) {
                v.add(tokens[i]);
            }
        }
        tokens = new String[v.size()];
        tokens = (String[]) v.toArray((String[]) tokens);
        if (tokens.length > 0) {
            String tok = tokens[0].trim();
            try {
                if (tok.startsWith("0x")) {
                    checkBytesFrom = Integer.parseInt(tok.substring(2), 16);
                } else {
                    checkBytesFrom = Integer.parseInt(tok);
                }
            } catch (NumberFormatException e) {
                addEntry(trimmed.replaceAll("  ", "\t"));
                return;
            }
        }
        if (tokens.length > 1) {
            typeStr = tokens[1].trim();
            type = getType(typeStr);
        }
        if (tokens.length > 2) {
            content = ltrim(tokens[2]);
            content = stringWithEscapeSubstitutions(content);
        }
        if (tokens.length > 3) {
            mimeType = tokens[3].trim();
        }
        if (tokens.length > 4) {
            mimeEnc = tokens[4].trim();
        }
    }

    private String ltrim(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                return s.substring(i);
            }
        }
        return s;
    }

    private int getType(String tok) {
        if (tok.startsWith("string")) {
            return STRING_TYPE;
        } else if (tok.startsWith("belong")) {
            return BELONG_TYPE;
        } else if (tok.equals("short")) {
            return SHORT_TYPE;
        } else if (tok.startsWith("lelong")) {
            return LELONG_TYPE;
        } else if (tok.startsWith("beshort")) {
            return BESHORT_TYPE;
        } else if (tok.startsWith("leshort")) {
            return LESHORT_TYPE;
        } else if (tok.equals("byte")) {
            return BYTE_TYPE;
        }
        return UNKNOWN_TYPE;
    }

    public int getCheckBytesFrom() {
        return checkBytesFrom;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getMatch(byte[] content) throws IOException {
        ByteBuffer buf = readBuffer(content);
        if (buf == null) return null;
        buf.position(0);
        boolean matches = match(buf);
        if (matches) {
            int subLen = subEntries.size();
            String myMimeType = getMimeType();
            if (subLen > 0) {
                String mtype = null;
                for (int k = 0; k < subLen; k++) {
                    MagicMimeEntry me = (MagicMimeEntry) subEntries.get(k);
                    mtype = me.getMatch(content);
                    if (mtype != null) {
                        return mtype;
                    }
                }
                if (myMimeType != null) {
                    return myMimeType;
                }
            } else {
                return myMimeType;
            }
        }
        return null;
    }

    public String getMatch(RandomAccessFile raf) throws IOException {
        ByteBuffer buf = readBuffer(raf);
        if (buf == null) return null;
        boolean matches = match(buf);
        if (matches) {
            String myMimeType = getMimeType();
            if (subEntries.size() > 0) {
                String mtype = null;
                for (int i = 0; i < subEntries.size(); i++) {
                    MagicMimeEntry me = (MagicMimeEntry) subEntries.get(i);
                    mtype = me.getMatch(raf);
                    if (mtype != null) {
                        return mtype;
                    }
                }
                if (myMimeType != null) {
                    return myMimeType;
                }
            } else {
                return myMimeType;
            }
        }
        return null;
    }

    private ByteBuffer readBuffer(byte[] content) throws IOException {
        int startPos = getCheckBytesFrom();
        if (startPos > content.length) return null;
        ByteBuffer buf;
        switch(getType()) {
            case MagicMimeEntry.STRING_TYPE:
                {
                    int len = getContent().length();
                    buf = ByteBuffer.allocate(len + 1);
                    buf.put(content, startPos, len);
                    break;
                }
            case MagicMimeEntry.SHORT_TYPE:
            case MagicMimeEntry.LESHORT_TYPE:
            case MagicMimeEntry.BESHORT_TYPE:
                {
                    buf = ByteBuffer.allocate(2);
                    buf.put(content, startPos, 2);
                    break;
                }
            case MagicMimeEntry.LELONG_TYPE:
            case MagicMimeEntry.BELONG_TYPE:
                {
                    buf = ByteBuffer.allocate(4);
                    buf.put(content, startPos, 4);
                    break;
                }
            case MagicMimeEntry.BYTE_TYPE:
                {
                    buf = ByteBuffer.allocate(1);
                    buf.put(buf.array(), startPos, 1);
                }
            default:
                {
                    buf = null;
                    break;
                }
        }
        return buf;
    }

    private ByteBuffer readBuffer(RandomAccessFile raf) throws IOException {
        int startPos = getCheckBytesFrom();
        if (startPos > raf.length()) return null;
        raf.seek(startPos);
        ByteBuffer buf;
        switch(getType()) {
            case MagicMimeEntry.STRING_TYPE:
                {
                    int len = 0;
                    int index = typeStr.indexOf(">");
                    if (index != -1) {
                        len = Integer.parseInt(typeStr.substring(index + 1, typeStr.length() - 1));
                        isBetween = true;
                    } else {
                        len = getContent().length();
                    }
                    buf = ByteBuffer.allocate(len + 1);
                    raf.read(buf.array(), 0, len);
                    break;
                }
            case MagicMimeEntry.SHORT_TYPE:
            case MagicMimeEntry.LESHORT_TYPE:
            case MagicMimeEntry.BESHORT_TYPE:
                {
                    buf = ByteBuffer.allocate(2);
                    raf.read(buf.array(), 0, 2);
                    break;
                }
            case MagicMimeEntry.LELONG_TYPE:
            case MagicMimeEntry.BELONG_TYPE:
                {
                    buf = ByteBuffer.allocate(4);
                    raf.read(buf.array(), 0, 4);
                    break;
                }
            case MagicMimeEntry.BYTE_TYPE:
                {
                    buf = ByteBuffer.allocate(1);
                    raf.read(buf.array(), 0, 1);
                }
            default:
                {
                    buf = null;
                    break;
                }
        }
        return buf;
    }

    private boolean match(ByteBuffer buf) throws IOException {
        boolean matches = true;
        switch(getType()) {
            case MagicMimeEntry.STRING_TYPE:
                {
                    matches = matchString(buf);
                    break;
                }
            case MagicMimeEntry.SHORT_TYPE:
                {
                    matches = matchShort(buf, ByteOrder.BIG_ENDIAN, false, (short) 0xFF);
                    break;
                }
            case MagicMimeEntry.LESHORT_TYPE:
            case MagicMimeEntry.BESHORT_TYPE:
                {
                    ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
                    if (getType() == MagicMimeEntry.LESHORT_TYPE) {
                        byteOrder = ByteOrder.LITTLE_ENDIAN;
                    }
                    boolean needMask = false;
                    short sMask = 0xFF;
                    int indx = typeStr.indexOf('&');
                    if (indx >= 0) {
                        sMask = (short) Integer.parseInt(typeStr.substring(indx + 3), 16);
                        needMask = true;
                    } else if (getContent().startsWith("&")) {
                        sMask = (short) Integer.parseInt(getContent().substring(3), 16);
                        needMask = true;
                    }
                    matches = matchShort(buf, byteOrder, needMask, sMask);
                    break;
                }
            case MagicMimeEntry.LELONG_TYPE:
            case MagicMimeEntry.BELONG_TYPE:
                {
                    ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
                    if (getType() == MagicMimeEntry.LELONG_TYPE) {
                        byteOrder = ByteOrder.LITTLE_ENDIAN;
                    }
                    boolean needMask = false;
                    long lMask = 0xFFFFFFFF;
                    int indx = typeStr.indexOf('&');
                    if (indx >= 0) {
                        lMask = Long.parseLong(typeStr.substring(indx + 3), 16);
                        needMask = true;
                    } else if (getContent().startsWith("&")) {
                        lMask = Long.parseLong(getContent().substring(3), 16);
                        needMask = true;
                    }
                    matches = matchLong(buf, byteOrder, needMask, lMask);
                    break;
                }
            case MagicMimeEntry.BYTE_TYPE:
                {
                    matches = matchByte(buf);
                }
            default:
                {
                    matches = false;
                    break;
                }
        }
        return matches;
    }

    private boolean matchString(ByteBuffer bbuf) throws IOException {
        if (isBetween) {
            String buffer = new String(bbuf.array());
            if (buffer.contains(getContent())) {
                return true;
            }
            return false;
        }
        int read = getContent().length();
        for (int j = 0; j < read; j++) {
            if ((bbuf.get(j) & 0xFF) != getContent().charAt(j)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchByte(ByteBuffer bbuf) throws IOException {
        byte b = bbuf.get(0);
        return b == getContent().charAt(0);
    }

    private boolean matchShort(ByteBuffer bbuf, ByteOrder bo, boolean needMask, short sMask) throws IOException {
        bbuf.order(bo);
        short got;
        String testContent = getContent();
        if (testContent.startsWith("0x")) {
            got = (short) Integer.parseInt(testContent.substring(2), 16);
        } else if (testContent.startsWith("&")) {
            got = (short) Integer.parseInt(testContent.substring(3), 16);
        } else {
            got = (short) Integer.parseInt(testContent);
        }
        short found = bbuf.getShort();
        if (needMask) {
            found = (short) (found & sMask);
        }
        if (got != found) {
            return false;
        }
        return true;
    }

    private boolean matchLong(ByteBuffer bbuf, ByteOrder bo, boolean needMask, long lMask) throws IOException {
        bbuf.order(bo);
        long got;
        String testContent = getContent();
        if (testContent.startsWith("0x")) {
            got = Long.parseLong(testContent.substring(2), 16);
        } else if (testContent.startsWith("&")) {
            got = Long.parseLong(testContent.substring(3), 16);
        } else {
            got = Long.parseLong(testContent);
        }
        long found = bbuf.getInt();
        if (needMask) {
            found = (short) (found & lMask);
        }
        if (got != found) {
            return false;
        }
        return true;
    }

    private static String stringWithEscapeSubstitutions(String s) {
        StringBuffer ret = new StringBuffer();
        int len = s.length();
        int indx = 0;
        int c;
        while (indx < len) {
            c = s.charAt(indx);
            if (c == '\n') {
                break;
            }
            if (c == '\\') {
                indx++;
                if (indx >= len) {
                    ret.append((char) c);
                    break;
                }
                int cn = s.charAt(indx);
                if (cn == '\\') {
                    ret.append('\\');
                } else if (cn == ' ') {
                    ret.append(' ');
                } else if (cn == 't') {
                    ret.append('\t');
                } else if (cn == 'n') {
                    ret.append('\n');
                } else if (cn == 'r') {
                    ret.append('\r');
                } else if (cn >= '\60' && cn <= '\67') {
                    int escape = cn - '0';
                    indx++;
                    if (indx >= len) {
                        ret.append((char) escape);
                        break;
                    }
                    cn = s.charAt(indx);
                    if (cn >= '\60' && cn <= '\67') {
                        escape = escape << 3;
                        escape = escape | (cn - '0');
                        indx++;
                        if (indx >= len) {
                            ret.append((char) escape);
                            break;
                        }
                        cn = s.charAt(indx);
                        if (cn >= '\60' && cn <= '\67') {
                            escape = escape << 3;
                            escape = escape | (cn - '0');
                        } else {
                            indx--;
                        }
                    } else {
                        indx--;
                    }
                    ret.append((char) escape);
                } else {
                    ret.append((char) cn);
                }
            } else {
                ret.append((char) c);
            }
            indx++;
        }
        return new String(ret);
    }
}
