package net.sf.jmimemagic;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.apache.oro.text.perl.Perl5Util;

/**
 * This class represents a single match test
 * 
 * @author $Author: andyscukanec $
 * @version $Revision: 5919 $
 */
public class MagicMatcher {

    private Logger log;

    private ArrayList subMatchers = new ArrayList(0);

    private MagicMatch match;

    /**
     * constructor
     */
    public MagicMatcher() {
        log = Logger.getLogger("net.sf.jmimemagic");
        log.debug("MagicMatcher: instantiated");
    }

    public void setMatch(MagicMatch newMatch) {
        log.debug("MagicMatcher: setMatch()");
        match = newMatch;
    }

    public MagicMatch getMatch() {
        log.debug("MagicMatcher: getMatch()");
        return match;
    }

    /**
     * test to see if everything is in order for this match
     * 
     * @return whether or not this match has enough data to be valid
     */
    public boolean isValid() {
        log.debug("MagicMatcher: isValid()");
        if ((match == null) || (match.getTest() == null)) {
            return false;
        }
        String type = new String(match.getTest().array());
        char comparator = match.getComparator();
        String description = match.getDescription();
        String test = new String(match.getTest().array());
        if ((type != null) && !type.equals("") && (comparator != '\0') && (comparator == '=' || comparator == '!' || comparator == '>' || comparator == '<') && (description != null) && !description.equals("") && (test != null) && !test.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * add a submatch to this magic match
     * 
     * @param m
     *            a magic match
     */
    public void addSubMatcher(MagicMatcher m) {
        log.debug("MagicMatcher: addSubMatcher()");
        subMatchers.add(m);
    }

    /**
     * set all submatches
     * 
     * @param a
     *            a collection of submatches
     */
    public void setSubMatchers(Collection a) {
        log.debug("MagicMatcher: setSubMatchers(): for match '" + match.getDescription() + "'");
        subMatchers.clear();
        subMatchers.addAll(a);
    }

    /**
     * get all submatches for this magic match
     * 
     * @return a collection of submatches
     */
    public Collection getSubMatchers() {
        log.debug("MagicMatcher: getSubMatchers()");
        return subMatchers;
    }

    /**
     * test to see if this match or any submatches match
     * 
     * @param f
     *            the file that should be used to test the match
     * @return the deepest magic match object that matched
     * @throws IOException
     * @throws UnsupportedTypeException
     */
    public MagicMatch test(File f) throws IOException, UnsupportedTypeException {
        log.debug("MagicMatcher: test(File)");
        int offset = match.getOffset();
        String description = match.getDescription();
        String type = match.getType();
        log.debug("MagicMatcher: test(File): testing '" + f.getName() + "' for '" + description + "'");
        log.debug("MagicMatcher: test(File): \n=== BEGIN MATCH INFO ==");
        log.debug(match.toString());
        log.debug("MagicMatcher: test(File): \n=== END MATCH INFO ====\n");
        RandomAccessFile file = null;
        file = new RandomAccessFile(f, "r");
        try {
            int length = 0;
            if (type.equals("byte")) {
                length = 1;
            } else if (type.equals("short") || type.equals("leshort") || type.equals("beshort")) {
                length = 4;
            } else if (type.equals("long") || type.equals("lelong") || type.equals("belong")) {
                length = 8;
            } else if (type.equals("string")) {
                length = match.getTest().capacity();
            } else if (type.equals("regex")) {
                length = (int) file.length() - offset;
                if (length < 0) {
                    length = 0;
                }
            } else {
                throw new UnsupportedTypeException("unsupported test type '" + type + "'");
            }
            if (length > file.length() - offset) {
                return null;
            }
            byte[] buf = new byte[length];
            file.seek(offset);
            int bytesRead = 0;
            int size = 0;
            boolean done = false;
            while (!done) {
                size = file.read(buf, 0, length - bytesRead);
                if (size == -1) {
                    throw new IOException("reached end of file before all bytes were read");
                }
                bytesRead += size;
                if (bytesRead == length) {
                    done = true;
                }
            }
            log.debug("MagicMatcher: test(File): stream size is '" + buf.length + "'");
            MagicMatch match = null;
            MagicMatch submatch = null;
            if (testInternal(buf)) {
                match = getMatch();
                log.debug("MagicMatcher: test(File): testing matched '" + description + "'");
                if ((subMatchers != null) && (subMatchers.size() > 0)) {
                    log.debug("MagicMatcher: test(File): testing " + subMatchers.size() + " submatches for '" + description + "'");
                    for (int i = 0; i < subMatchers.size(); i++) {
                        log.debug("MagicMatcher: test(File): testing submatch " + i);
                        MagicMatcher m = (MagicMatcher) subMatchers.get(i);
                        if ((submatch = m.test(f)) != null) {
                            log.debug("MagicMatcher: test(File): submatch " + i + " matched with '" + submatch.getDescription() + "'");
                            match.addSubMatch(submatch);
                        } else {
                            log.debug("MagicMatcher: test(File): submatch " + i + " doesn't match");
                        }
                    }
                }
            }
            return match;
        } finally {
            try {
                file.close();
            } catch (Exception fce) {
            }
        }
    }

    /**
     * test to see if this match or any submatches match
     * 
     * @param data
     *            the data that should be used to test the match
     * @return the deepest magic match object that matched
     * @throws IOException
     * @throws UnsupportedTypeException
     */
    public MagicMatch test(byte[] data) throws IOException, UnsupportedTypeException {
        log.debug("MagicMatcher: test(byte[])");
        int offset = match.getOffset();
        String description = match.getDescription();
        String type = match.getType();
        log.debug("MagicMatcher: test(byte[]): testing byte[] data for '" + description + "'");
        log.debug("MagicMatcher: test(byte[]): \n=== BEGIN MATCH INFO ==");
        log.debug(match.toString());
        log.debug("MagicMatcher: test(byte[]): \n=== END MATCH INFO ====\n");
        int length = 0;
        if (type.equals("byte")) {
            length = 1;
        } else if (type.equals("short") || type.equals("leshort") || type.equals("beshort")) {
            length = 4;
        } else if (type.equals("long") || type.equals("lelong") || type.equals("belong")) {
            length = 8;
        } else if (type.equals("string")) {
            length = match.getTest().capacity();
        } else if (type.equals("regex")) {
            length = data.length - offset - 1;
            if (length < 0) {
                length = 0;
            }
        } else {
            throw new UnsupportedTypeException("unsupported test type " + type);
        }
        byte[] buf = new byte[length];
        log.debug("MagicMatcher: test(byte[]): offset=" + offset + ",length=" + length + ",data length=" + data.length);
        if (offset + length < data.length) {
            System.arraycopy(data, offset, buf, 0, length);
            log.debug("MagicMatcher: test(byte[]): stream size is '" + buf.length + "'");
            MagicMatch match = null;
            MagicMatch submatch = null;
            if (testInternal(buf)) {
                match = getMatch();
                log.debug("MagicMatcher: test(byte[]): testing matched '" + description + "'");
                if ((subMatchers != null) && (subMatchers.size() > 0)) {
                    log.debug("MagicMatcher: test(byte[]): testing " + subMatchers.size() + " submatches for '" + description + "'");
                    for (int i = 0; i < subMatchers.size(); i++) {
                        log.debug("MagicMatcher: test(byte[]): testing submatch " + i);
                        MagicMatcher m = (MagicMatcher) subMatchers.get(i);
                        if ((submatch = m.test(data)) != null) {
                            log.debug("MagicMatcher: test(byte[]): submatch " + i + " matched with '" + submatch.getDescription() + "'");
                            match.addSubMatch(submatch);
                        } else {
                            log.debug("MagicMatcher: test(byte[]): submatch " + i + " doesn't match");
                        }
                    }
                }
            }
            return match;
        }
        return null;
    }

    private boolean testInternal(byte[] data) {
        log.debug("MagicMatcher: testInternal(byte[])");
        if (data.length == 0) {
            return false;
        }
        String type = match.getType();
        String test = new String(match.getTest().array());
        String mimeType = match.getMimeType();
        String description = match.getDescription();
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        if ((type != null) && (test != null) && (test.length() > 0)) {
            if (type.equals("string")) {
                buffer = buffer.put(data);
                return testString(buffer);
            } else if (type.equals("byte")) {
                buffer = buffer.put(data);
                return testByte(buffer);
            } else if (type.equals("short")) {
                buffer = buffer.put(data);
                return testShort(buffer);
            } else if (type.equals("leshort")) {
                buffer = buffer.put(data);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                return testShort(buffer);
            } else if (type.equals("beshort")) {
                buffer = buffer.put(data);
                buffer.order(ByteOrder.BIG_ENDIAN);
                return testShort(buffer);
            } else if (type.equals("long")) {
                buffer = buffer.put(data);
                return testLong(buffer);
            } else if (type.equals("lelong")) {
                buffer = buffer.put(data);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                return testLong(buffer);
            } else if (type.equals("belong")) {
                buffer = buffer.put(data);
                buffer.order(ByteOrder.BIG_ENDIAN);
                return testLong(buffer);
            } else if (type.equals("regex")) {
                return testRegex(new String(data));
            } else {
                log.error("MagicMatcher: testInternal(byte[]): invalid test type '" + type + "'");
            }
        } else {
            log.error("MagicMatcher: testInternal(byte[]): type or test is empty for '" + mimeType + " - " + description + "'");
        }
        return false;
    }

    /**
     * test the data against the test byte
     * 
     * @param data
     *            the data we are testing
     * @return if we have a match
     */
    private boolean testByte(ByteBuffer data) {
        log.debug("MagicMatcher: testByte()");
        String test = new String(match.getTest().array());
        char comparator = match.getComparator();
        long bitmask = match.getBitmask();
        byte b = data.get(0);
        b = (byte) (b & bitmask);
        log.debug("MagicMatcher: testByte(): decoding '" + test + "' to byte");
        int tst = Integer.decode(test).byteValue();
        byte t = (byte) (tst & 0xff);
        log.debug("MagicMatcher: testByte(): applying bitmask '" + bitmask + "' to '" + tst + "', result is '" + t + "'");
        log.debug("MagicMatcher: testByte(): comparing byte '" + b + "' to '" + t + "'");
        switch(comparator) {
            case '=':
                return t == b;
            case '!':
                return t != b;
            case '>':
                return t > b;
            case '<':
                return t < b;
        }
        return false;
    }

    /**
     * test the data against the byte array
     * 
     * @param data
     *            the data we are testing
     * @return if we have a match
     */
    private boolean testString(ByteBuffer data) {
        log.debug("MagicMatcher: testString()");
        ByteBuffer test = match.getTest();
        char comparator = match.getComparator();
        byte[] b = data.array();
        byte[] t = test.array();
        boolean diff = false;
        int i = 0;
        for (i = 0; i < t.length; i++) {
            log.debug("testing byte '" + b[i] + "' from '" + new String(data.array()) + "' against byte '" + t[i] + "' from '" + new String(test.array()) + "'");
            if (t[i] != b[i]) {
                diff = true;
                break;
            }
        }
        switch(comparator) {
            case '=':
                return !diff;
            case '!':
                return diff;
            case '>':
                return t[i] > b[i];
            case '<':
                return t[i] < b[i];
        }
        return false;
    }

    /**
     * test the data against a short
     * 
     * @param data
     *            the data we are testing
     * @return if we have a match
     */
    private boolean testShort(ByteBuffer data) {
        log.debug("MagicMatcher: testShort()");
        short val = 0;
        String test = new String(match.getTest().array());
        char comparator = match.getComparator();
        long bitmask = match.getBitmask();
        val = byteArrayToShort(data);
        val = (short) (val & (short) bitmask);
        short tst = 0;
        try {
            tst = Integer.decode(test).shortValue();
        } catch (NumberFormatException e) {
            log.error("MagicMatcher: testShort(): " + e);
            return false;
        }
        log.debug("MagicMatcher: testShort(): testing '" + Long.toHexString(val) + "' against '" + Long.toHexString(tst) + "'");
        switch(comparator) {
            case '=':
                return val == tst;
            case '!':
                return val != tst;
            case '>':
                return val > tst;
            case '<':
                return val < tst;
        }
        return false;
    }

    /**
     * test the data against a long
     * 
     * @param data
     *            the data we are testing
     * @return if we have a match
     */
    private boolean testLong(ByteBuffer data) {
        log.debug("MagicMatcher: testLong()");
        long val = 0;
        String test = new String(match.getTest().array());
        char comparator = match.getComparator();
        long bitmask = match.getBitmask();
        val = byteArrayToLong(data);
        val = val & bitmask;
        long tst = Long.decode(test).longValue();
        log.debug("MagicMatcher: testLong(): testing '" + Long.toHexString(val) + "' against '" + test + "' => '" + Long.toHexString(tst) + "'");
        switch(comparator) {
            case '=':
                return val == tst;
            case '!':
                return val != tst;
            case '>':
                return val > tst;
            case '<':
                return val < tst;
        }
        return false;
    }

    /**
     * test the data against a regex
     * 
     * @param data
     *            the data we are testing
     * @return if we have a match
     */
    private boolean testRegex(String text) {
        log.debug("MagicMatcher: testRegex()");
        String test = new String(match.getTest().array());
        char comparator = match.getComparator();
        Perl5Util utility = new Perl5Util();
        log.debug("MagicMatcher: testRegex(): searching for '" + test + "'");
        if (comparator == '=') {
            if (utility.match(test, text)) {
                return true;
            }
            return false;
        } else if (comparator == '!') {
            if (utility.match(test, text)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * convert a byte array to a short
     * 
     * @param data
     *            buffer of byte data
     * @return byte array converted to a short
     */
    private short byteArrayToShort(ByteBuffer data) {
        return data.getShort(0);
    }

    /**
     * convert a byte array to a long
     * 
     * @param data
     *            buffer of byte data
     * @return byte arrays (high and low bytes) converted to a long value
     */
    private long byteArrayToLong(ByteBuffer data) {
        return data.getInt(0);
    }

    public String toString() {
        String result = "name: " + getMatch() == null ? "(null)" : getMatch().getDescription();
        result += "children: ";
        for (int ctr = 0; ctr < subMatchers.size(); ctr++) {
            result += subMatchers.get(ctr).toString() + "  ";
        }
        return result;
    }
}
