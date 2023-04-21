package jdbm.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import jdbm.RecordManager;
import jdbm.btree.BPage;
import jdbm.btree.BTree;
import org.CognitiveWeb.extser.AbstractExtensibleSerializer;
import org.CognitiveWeb.extser.IExtensibleSerializer;
import org.CognitiveWeb.extser.ISerializer;
import org.CognitiveWeb.extser.LongPacker;

/**
 * Concrete class knows how to maintain its state against a
 * {@link RecordManager}.
 * 
 * @see ExtensibleSerializerSingleton
 * 
 * @author thompsonbry
 */
public class ExtensibleSerializer extends AbstractExtensibleSerializer {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * A reference to the {@link RecordManager}.
     */
    private transient RecordManager m_recman;

    /**
     * The recid of this serializer.
     */
    private transient long m_recid;

    /**
     * Return the record manager.
     */
    public RecordManager getRecordManager() {
        return m_recman;
    }

    /**
     * Return the logical row id of this serializer.
     */
    public long getRecid() {
        return m_recid;
    }

    /**
     * Deserialization constructor.
     */
    public ExtensibleSerializer() {
        super();
    }

    /**
     * Create a new instance an insert it into the store.
     * 
     * @param recman The record manager.
     * @return The new instance.
     * @throws IOException
     */
    public static ExtensibleSerializer createInstance(RecordManager recman) throws IOException {
        ExtensibleSerializer ser = new ExtensibleSerializer();
        ser.m_recman = recman;
        ser.m_recid = recman.insert(ser, DefaultSerializer.INSTANCE);
        ser.registerSerializers();
        return ser;
    }

    /**
     * Load an existing instance from the store.
     * 
     * @param recman
     * @param recid
     * @return
     * @throws IOException
     */
    public static ExtensibleSerializer load(RecordManager recman, long recid) throws IOException {
        ExtensibleSerializer ser = (ExtensibleSerializer) recman.fetch(recid, DefaultSerializer.INSTANCE);
        ser.m_recman = recman;
        ser.m_recid = recid;
        return ser;
    }

    /**
     * <p>Note: The {@link DefaultSerializer} is used to insert, fetch
     * and update instances of this class.</p>
     */
    protected synchronized void update() {
        try {
            m_recman.update(m_recid, this, DefaultSerializer.INSTANCE);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ISerializer getSerializer(long serializerId) {
        try {
            return (ISerializer) getRecordManager().fetch(serializerId);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Extends the default behavior to also register serializers for the various
     * jdbm classes with persistent state: {@link BTree}, {@link BPage},
     * etc.
     */
    protected void setupSerializers() {
        super.setupSerializers();
        _registerClass(jdbm.btree.BTree.class, jdbm.btree.BTree.Serializer0.class, (short) 0);
        _registerClass(jdbm.btree.BPage.class, jdbm.btree.BPage.Serializer0.class, (short) 0);
        _registerClass(jdbm.htree.HashDirectory.class, jdbm.htree.HashDirectory.Serializer0.class, (short) 0);
        _registerClass(jdbm.htree.HashBucket.class, jdbm.htree.HashBucket.Serializer0.class, (short) 0);
        _registerClass(jdbm.strings.StringTable.class, jdbm.strings.StringTable.Serializer0.class, (short) 0);
        _registerClass(HashMap.class);
    }

    public DataOutputStream getDataOutputStream(long recid, ByteArrayOutputStream baos) throws IOException {
        return new MyDataOutputStream(recid, this, baos);
    }

    public DataInputStream getDataInputStream(long recid, ByteArrayInputStream bais) throws IOException {
        return new MyDataInputStream(recid, this, bais);
    }

    public static class MyDataOutputStream extends DataOutputStream {

        protected MyDataOutputStream(long recid, IExtensibleSerializer serializer, ByteArrayOutputStream out) throws IOException {
            super(recid, serializer, out);
        }

        public int writePackedOId(long oid) throws IOException {
            return LongPacker.packLong(this, oid);
        }
    }

    public static class MyDataInputStream extends DataInputStream {

        protected MyDataInputStream(long recid, IExtensibleSerializer serializer, ByteArrayInputStream is) throws IOException {
            super(recid, serializer, is);
        }

        public long readPackedOId() throws IOException {
            return LongPacker.unpackLong(this);
        }
    }
}
