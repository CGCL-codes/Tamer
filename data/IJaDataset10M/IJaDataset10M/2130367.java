package org.xBaseJ.indexes;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;
import org.xBaseJ.DBF;
import org.xBaseJ.Util;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.Field;

public abstract class Index {

    public int top_Node;

    public int next_available;

    public int reserved_02;

    public short key_length;

    public short key_per_Node;

    public char keyType;

    public short key_entry_size;

    public byte reserved_01;

    public byte reserved_03;

    public byte reserved_04;

    public byte unique_key;

    public byte key_definition[];

    public Vector keyControl;

    public NodeKey activeKey;

    public int record;

    public Node topNode = null;

    public Node workNode;

    public File file;

    public RandomAccessFile nfile;

    public FileChannel channel;

    public ByteBuffer bytebuffer;

    public String dosname;

    public DBF database;

    public String stringKey;

    public static final int findFirstMatchingKey = -1;

    public static final int findAnyKey = -2;

    public static final int keyNotFound = -3;

    public static final int foundMatchingKeyButNotRecord = -4;

    public boolean foundExact = false;

    public Index() {
        key_definition = new byte[488];
        keyControl = new Vector();
        dosname = new String("");
        activeKey = null;
    }

    public boolean compareKey(String keyToCompare) throws xBaseJException, IOException {
        NodeKey tempKey;
        if (keyType == 'F') tempKey = new NodeKey(new NodeFloat(Double.valueOf(keyToCompare).doubleValue())); else if (keyType == 'N') {
            Double d = new Double(keyToCompare);
            tempKey = new NodeKey(d);
        } else tempKey = new NodeKey(keyToCompare);
        return (activeKey.compareKey(tempKey) == 0);
    }

    public abstract int add_entry(NodeKey key, int recno) throws xBaseJException, IOException;

    public int add_entry(int recno) throws xBaseJException, IOException {
        NodeKey newkey = build_key();
        return add_entry(newkey, recno);
    }

    public abstract int find_entry(NodeKey key) throws xBaseJException, IOException;

    public abstract int find_entry(NodeKey key, int recno) throws xBaseJException, IOException;

    public int find_entry(String key) throws xBaseJException, IOException {
        if (keyType == 'F') return find_entry(new NodeKey(new NodeFloat(Double.valueOf(key).doubleValue())));
        if (keyType == 'N') {
            double d = 0.0;
            Field f;
            f = (Field) keyControl.elementAt(0);
            if (f.getType() == 'D') d = Util.doubleDate(key); else d = Double.valueOf(key).doubleValue();
            return find_entry(new NodeKey(new Double(d)));
        }
        return find_entry(new NodeKey(key));
    }

    public int find_entry(String key, int recno) throws xBaseJException, IOException {
        if (keyType == 'F') record = find_entry(new NodeKey(new NodeFloat(Double.valueOf(key).doubleValue())), recno); else if (keyType == 'N') record = find_entry(new NodeKey(new Double(key)), recno); else record = find_entry(new NodeKey(key), recno);
        return record;
    }

    public abstract int get_next_key() throws xBaseJException, IOException;

    public abstract int get_prev_key() throws xBaseJException, IOException;

    public abstract void del_entry(Node inNode) throws IOException, xBaseJException;

    public abstract void reIndex() throws xBaseJException, IOException;

    public void check_for_duplicates(int count) throws xBaseJException, IOException {
        if (topNode == null) return;
        if (unique_key == 0) return;
        int ret = find_entry(build_key(), findFirstMatchingKey);
        if (ret == keyNotFound) return;
        if (count == findFirstMatchingKey) if (ret == count) return;
        if (count > 0) {
            if (ret == count) {
                return;
            }
        }
        if (ret > 0) throw new xBaseJException("Duplicate key error");
    }

    public String getName() {
        return dosname.trim();
    }

    public String getKeyFields() {
        return stringKey;
    }

    public String buildKey() throws xBaseJException {
        return build_key().toString();
    }

    public NodeKey build_key() throws xBaseJException {
        NodeKey dataptr;
        int i;
        Field f;
        double doubleer = 0.0;
        switch(keyType) {
            case 'F':
                for (i = 0; i < keyControl.size(); i++) {
                    f = (Field) keyControl.elementAt(i);
                    if (f.get() == null) ; else if (f.get().length() == 0) ; else if (f.getType() == 'D') doubleer += Util.doubleDate(f.get()); else doubleer += Double.valueOf(f.get()).doubleValue();
                }
                dataptr = new NodeKey(new NodeFloat(doubleer));
                break;
            case 'N':
                for (i = 0; i < keyControl.size(); i++) {
                    f = (Field) keyControl.elementAt(i);
                    if (f.get() == null) ; else if (f.get().length() == 0) ; else if (f.getType() == 'D') doubleer += Util.doubleDate(f.get()); else doubleer += Double.valueOf(f.get()).doubleValue();
                }
                dataptr = new NodeKey(new Double(doubleer));
                break;
            default:
                StringBuffer sb = new StringBuffer();
                for (i = 0; i < keyControl.size(); i++) {
                    f = (Field) keyControl.elementAt(i);
                    sb.append(f.get());
                }
                dataptr = new NodeKey(new String(sb));
                break;
        }
        return dataptr;
    }

    public boolean is_unique_key() {
        return (unique_key != 0);
    }

    public void set_active_key(NodeKey key) {
        activeKey = key;
    }

    public NodeKey get_active_key() {
        return activeKey;
    }

    public void set_key_definition(String definition) {
        byte kd[];
        try {
            kd = definition.getBytes(DBF.encodedType);
        } catch (UnsupportedEncodingException UEE) {
            kd = definition.getBytes();
        }
        for (int x = 0; x < kd.length; x++) key_definition[x] = kd[x];
    }

    public void update(int recno) throws xBaseJException, IOException {
        NodeKey bkey = build_key();
        record = find_entry(activeKey, recno);
        if (record == recno) {
            if (bkey.compareKey(activeKey) != 0) {
                del_entry(workNode);
                add_entry(recno);
            }
        } else add_entry(recno);
    }

    public void position_at_first() throws xBaseJException, IOException {
        String startKey;
        if (keyType == 'N') startKey = new NodeKey(new Double(-1.78e308)).toString(); else if (keyType == 'F') startKey = new NodeKey(new NodeFloat(-1.78e308)).toString(); else startKey = new NodeKey(new String(" ")).toString();
        find_entry(startKey, -1);
        workNode.set_pos(-1);
    }

    public void position_at_last() throws xBaseJException, IOException {
        String startKey;
        if (keyType == 'N') startKey = new NodeKey(new Double(1.78e308)).toString(); else if (keyType == 'F') startKey = new NodeKey(new NodeFloat(1.78e308)).toString(); else startKey = new NodeKey(new String("￿")).toString();
        find_entry(startKey, -1);
    }

    /** return to return if the find_entry function found the exact key requested
 * @return boolean
*/
    public boolean didFindFindExact() {
        return foundExact;
    }
}
