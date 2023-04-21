package edu.mit.lcs.haystack.lucene.index;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ForwardReader {

    InputStream fwd;

    InputStream index;

    InputStream uri;

    long size;

    String primaryField;

    public ForwardReader(Directory d, String segment) throws IOException, SecurityException {
        fwd = null;
        index = null;
        uri = null;
        try {
            fwd = d.openFile(segment + ".fwd");
            index = d.openFile(segment + ".fwi");
            uri = d.openFile(segment + ".fwu");
        } catch (FileNotFoundException fne) {
            throw new RuntimeException("not a forward index reader");
        } catch (IOException io) {
            throw new RuntimeException("not a forward index reader");
        }
        if ((fwd == null) || (index == null) || (uri == null)) {
            throw new RuntimeException("not a forward index reader");
        }
        size = index.length() / 8L;
        primaryField = fwd.readString();
    }

    public String getPrimaryField() {
        return primaryField;
    }

    private class IndexPair {

        private long loc;

        private long docnum;

        public IndexPair(long loc, long docnum) {
            this.loc = loc;
            this.docnum = docnum;
        }

        public long getFilePosition() {
            return loc;
        }

        public long getDocNum() {
            return docnum;
        }
    }

    protected Map hashUnique;

    protected void readURIIndex() throws IOException {
        hashUnique = new Hashtable();
        try {
            if (uri != null) {
                while (true) {
                    String uniqueID = uri.readString();
                    long fp = uri.readLong();
                    long docNum = uri.readLong();
                    hashUnique.put(uniqueID, new IndexPair(fp, docNum));
                }
            }
        } catch (IOException io) {
        }
    }

    public IndexPair getDocumentInfo(Object uniqueID) throws IOException {
        if (hashUnique == null) {
            readURIIndex();
        }
        IndexPair ip = (IndexPair) hashUnique.get(uniqueID);
        if (ip == null) throw new IllegalArgumentException("could not find " + uniqueID.toString());
        return ip;
    }

    public long getDocumentNumber(Object uniqueID) throws IOException {
        IndexPair ip = getDocumentInfo(uniqueID);
        return ip.getDocNum();
    }

    public FrequencyMap getFrequencyMap(Object uniqueID) throws IOException {
        IndexPair ip = getDocumentInfo(uniqueID);
        Map m = getFieldHash(ip.getFilePosition());
        return new FrequencyMap(m, uniqueID);
    }

    protected Map getFieldHash(long filepos) throws IOException {
        fwd.seek(filepos);
        int numFields = fwd.readInt();
        Map hashFields = new Hashtable();
        for (int f = 0; f < numFields; f++) {
            String fieldName = fwd.readString();
            int numWords = fwd.readInt();
            Hashtable hashWords = new Hashtable();
            for (int w = 0; w < numWords; w++) {
                String word = fwd.readString();
                int frequency = fwd.readInt();
                hashWords.put(word, new Integer(frequency));
            }
            hashFields.put(fieldName, hashWords);
        }
        return hashFields;
    }

    public FrequencyMap getFrequencyMap(int i) throws IOException {
        index.seek(i * 8L);
        long fwdPos = index.readLong();
        Map m = getFieldHash(fwdPos);
        String primaryFieldValue = null;
        Map mt = (Map) m.get(primaryField);
        if (mt != null) {
            if (mt.size() == 1) {
                Set s = mt.keySet();
                Iterator iter = s.iterator();
                if (iter.hasNext()) {
                    primaryFieldValue = (String) iter.next();
                }
            }
        }
        if (primaryFieldValue == null) throw new RuntimeException("primaryFieldValue == null");
        return new FrequencyMap(m, primaryFieldValue);
    }

    public long size() {
        return size;
    }

    public void close() throws IOException {
        fwd.close();
        index.close();
        uri.close();
    }
}
