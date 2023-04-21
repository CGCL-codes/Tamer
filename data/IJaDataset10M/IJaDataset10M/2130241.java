package sun.management.counter.perf;

import sun.management.counter.*;
import java.nio.*;
import java.util.*;
import java.util.regex.*;

public class PerfInstrumentation {

    private ByteBuffer buffer;

    private Prologue prologue;

    private long lastModificationTime;

    private long lastUsed;

    private int nextEntry;

    private SortedMap<String, Counter> map;

    public PerfInstrumentation(ByteBuffer b) {
        prologue = new Prologue(b);
        buffer = b;
        buffer.order(prologue.getByteOrder());
        int major = getMajorVersion();
        int minor = getMinorVersion();
        if (major < 2) {
            throw new InstrumentationException("Unsupported version: " + major + "." + minor);
        }
        rewind();
    }

    public int getMajorVersion() {
        return prologue.getMajorVersion();
    }

    public int getMinorVersion() {
        return prologue.getMinorVersion();
    }

    public long getModificationTimeStamp() {
        return prologue.getModificationTimeStamp();
    }

    void rewind() {
        buffer.rewind();
        buffer.position(prologue.getEntryOffset());
        nextEntry = buffer.position();
        map = new TreeMap<String, Counter>();
    }

    boolean hasNext() {
        return (nextEntry < prologue.getUsed());
    }

    Counter getNextCounter() {
        if (!hasNext()) {
            return null;
        }
        if ((nextEntry % 4) != 0) {
            throw new InstrumentationException("Entry index not properly aligned: " + nextEntry);
        }
        if (nextEntry < 0 || nextEntry > buffer.limit()) {
            throw new InstrumentationException("Entry index out of bounds: nextEntry = " + nextEntry + ", limit = " + buffer.limit());
        }
        buffer.position(nextEntry);
        PerfDataEntry entry = new PerfDataEntry(buffer);
        nextEntry = nextEntry + entry.size();
        Counter counter = null;
        PerfDataType type = entry.type();
        if (type == PerfDataType.BYTE) {
            if (entry.units() == Units.STRING && entry.vectorLength() > 0) {
                counter = new PerfStringCounter(entry.name(), entry.variability(), entry.flags(), entry.vectorLength(), entry.byteData());
            } else if (entry.vectorLength() > 0) {
                counter = new PerfByteArrayCounter(entry.name(), entry.units(), entry.variability(), entry.flags(), entry.vectorLength(), entry.byteData());
            } else {
                assert false;
            }
        } else if (type == PerfDataType.LONG) {
            if (entry.vectorLength() == 0) {
                counter = new PerfLongCounter(entry.name(), entry.units(), entry.variability(), entry.flags(), entry.longData());
            } else {
                counter = new PerfLongArrayCounter(entry.name(), entry.units(), entry.variability(), entry.flags(), entry.vectorLength(), entry.longData());
            }
        } else {
            assert false;
        }
        return counter;
    }

    public synchronized List<Counter> getAllCounters() {
        while (hasNext()) {
            Counter c = getNextCounter();
            if (c != null) {
                map.put(c.getName(), c);
            }
        }
        return new ArrayList<Counter>(map.values());
    }

    public synchronized List<Counter> findByPattern(String patternString) {
        while (hasNext()) {
            Counter c = getNextCounter();
            if (c != null) {
                map.put(c.getName(), c);
            }
        }
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher("");
        List<Counter> matches = new ArrayList<Counter>();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry me = (Map.Entry) iter.next();
            String name = (String) me.getKey();
            matcher.reset(name);
            if (matcher.lookingAt()) {
                matches.add((Counter) me.getValue());
            }
        }
        return matches;
    }
}
