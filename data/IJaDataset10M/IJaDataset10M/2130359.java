package com.psycho.rtb.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.psycho.rtb.AlreadyDeclaredException;
import com.psycho.rtb.RtbConstants;

/**
 * 
 * @author psycho
 * @version 1.0
 */
public abstract class DataEntry implements RtbConstants, Comparable<DataEntry> {

    static final Log LOG = LogFactory.getLog(DataEntry.class);

    public static final int NO_INDEX = -1;

    public int index = NO_INDEX;

    int selfConnect = NO_INDEX;

    public final String name;

    Set<Integer> registeredGroups = new HashSet<Integer>();

    public int[] groups = null;

    Stream stream = null;

    public DataEntry(String n) {
        name = n;
    }

    public int getIndex() {
        return index;
    }

    public int compareTo(DataEntry o) {
        return index - o.index;
    }

    public void setIndex(int i) {
        index = i;
    }

    public String getName() {
        return name;
    }

    public boolean isSelfConnected() {
        return selfConnect != NO_INDEX;
    }

    public void setSelfConnection(int i) {
        selfConnect = i;
    }

    public int getSelfConnection() {
        return selfConnect;
    }

    public boolean equalsSubType(DataEntry other) {
        return true;
    }

    public void checkType(Class<?> st, Object i) throws AlreadyDeclaredException {
    }

    public void addGroup(int group) {
        registeredGroups.add(group);
    }

    static final List<int[]> groupPool = new ArrayList<int[]>();

    public void close(Stream parent) {
        if (registeredGroups == null) {
            return;
        }
        stream = parent;
        groups = new int[registeredGroups.size()];
        int index = 0;
        for (Integer group : registeredGroups) {
            groups[index] = group;
            index++;
        }
        synchronized (groupPool) {
            boolean found = false;
            for (int[] existing : groupPool) {
                if (Arrays.equals(groups, existing)) {
                    groups = existing;
                    found = true;
                    break;
                }
            }
            if (!found) {
                groupPool.add(groups);
            }
        }
    }

    public boolean isInGroup(int p) {
        return registeredGroups.contains(p);
    }

    public abstract void encodeEntry(DataOutputStream out) throws IOException;

    protected void encodeName(DataOutputStream os) throws IOException {
        os.writeUTF(name);
    }

    protected static String decodeName(DataInputStream is) throws IOException {
        return is.readUTF();
    }

    public abstract DataEntry getInstance(String n, Object i, Class<?> st);

    public abstract void init(Stream stream, DataEntry[] entryList);

    public abstract void encodeValues(Stream values, int[] indexes, DataOutputStream out) throws IOException;

    public abstract void decodeValues(Stream values, int[] indexes, DataInputStream in) throws IOException;

    public DataEntry decodeEntry(DataInputStream in) throws IOException {
        return decodeEntry(in, decodeName(in));
    }

    abstract DataEntry decodeEntry(DataInputStream in, String name) throws IOException;

    public abstract void copy(int[] fromIndexes, Stream sStream, int[] toIndexes, Stream rStream);

    public abstract void encodeArray(Object value, DataOutputStream out) throws IOException;

    public abstract Object decodeArray(Object value, int length, DataInputStream in) throws IOException;

    /**
     * Do something TODO.
     * <p>Details of the function.</p>
     *
     * @param b
     */
    public void update(Object b) {
        if (updateValue(b)) {
            stream.notifyChange(groups);
        }
    }

    /**
     * Do something TODO.
     * <p>Details of the function.</p>
     *
     * @param b
     */
    abstract boolean updateValue(Object b);
}
