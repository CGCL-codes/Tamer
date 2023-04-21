package com.googlecode.contraildb.core.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import com.googlecode.contraildb.core.Identifier;
import com.googlecode.contraildb.core.Item;
import com.googlecode.contraildb.core.impl.btree.BPlusTree;
import com.googlecode.contraildb.core.impl.btree.BTree;
import com.googlecode.contraildb.core.impl.btree.InnerNode;
import com.googlecode.contraildb.core.impl.btree.Node;
import com.googlecode.contraildb.core.storage.CommitMarker;
import com.googlecode.contraildb.core.storage.Entity;
import com.googlecode.contraildb.core.storage.LockFolder;
import com.googlecode.contraildb.core.storage.RevisionFolder;
import com.googlecode.contraildb.core.storage.RevisionJournal;
import com.googlecode.contraildb.core.storage.RootFolder;

/**
 * For performance reasons Contrail uses its own externalization scheme to 
 * persist objects.
 * 
 * Use the ExternalizationManager.write(DataOutput, Object) method to 
 * write objects to storage and the ExternalizationManager.read(DataInput) 
 * method to read objects.
 * 
 * Before a particular object type can be read or written a serializer 
 * for the object type must be registered.  Use the
 * ExternalizationManager.registerSerializer(Serializer) to register a 
 * serializer for a type.
 * 
 */
public class ExternalizationManager {

    public static interface Serializer<T> {

        void writeExternal(DataOutput out, T object) throws IOException;

        T readExternal(DataInput out) throws IOException;

        void readExternal(DataInput out, T object) throws IOException;

        int typeCode();
    }

    private static final HashMap<Integer, Serializer<?>> __serializers = new HashMap<Integer, Serializer<?>>();

    public static void registerSerializer(Serializer<?> serializer) {
        __serializers.put(serializer.typeCode(), serializer);
    }

    @SuppressWarnings("unchecked")
    public static <T> void writeExternal(DataOutput out, T object) throws IOException {
        if (object == null) {
            out.writeInt(0);
        } else {
            Integer type = object.getClass().getName().hashCode();
            Serializer<T> serializer = (Serializer<T>) __serializers.get(type);
            if (serializer == null) throw new IOException("No serializer available for type: " + type);
            out.writeInt(type);
            serializer.writeExternal(out, object);
        }
    }

    public static <T> void writeExternal(DataOutput out, T object, Serializer<T> serializer) throws IOException {
        if (object == null) {
            out.writeInt(0);
        } else {
            out.writeInt(serializer.typeCode());
            serializer.writeExternal(out, object);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readExternal(DataInput in) throws IOException {
        Integer type = in.readInt();
        if (type == 0) return null;
        Serializer<T> serializer = (Serializer<T>) __serializers.get(type);
        if (serializer == null) throw new IOException("No serializer available for type#: " + type);
        return serializer.readExternal(in);
    }

    public static final Serializer<String> StringSerializer = new Serializer<String>() {

        private final int typeCode = String.class.getName().hashCode();

        public String readExternal(DataInput in) throws IOException {
            return in.readUTF();
        }

        public void writeExternal(DataOutput out, String object) throws IOException {
            out.writeUTF(object);
        }

        public void readExternal(DataInput arg0, String arg1) throws IOException {
            throw new UnsupportedOperationException();
        }

        public int typeCode() {
            return typeCode;
        }
    };

    public static final Serializer<Long> LongSerializer = new Serializer<Long>() {

        private final int typeCode = Long.class.getName().hashCode();

        public Long readExternal(DataInput in) throws IOException {
            return in.readLong();
        }

        public void writeExternal(DataOutput out, Long object) throws IOException {
            out.writeLong((Long) object);
        }

        public void readExternal(DataInput arg0, Long arg1) throws IOException {
            throw new UnsupportedOperationException();
        }

        public int typeCode() {
            return typeCode;
        }
    };

    public static final Serializer<Integer> IntegerSerializer = new Serializer<Integer>() {

        private final int typeCode = Integer.class.getName().hashCode();

        public Integer readExternal(DataInput in) throws IOException {
            return in.readInt();
        }

        public void writeExternal(DataOutput out, Integer object) throws IOException {
            out.writeInt(object);
        }

        public void readExternal(DataInput arg0, Integer arg1) throws IOException {
            throw new UnsupportedOperationException();
        }

        public int typeCode() {
            return typeCode;
        }
    };

    static {
        registerSerializer(StringSerializer);
        registerSerializer(LongSerializer);
        registerSerializer(IntegerSerializer);
        registerSerializer(Identifier.SERIALIZER);
        registerSerializer(Entity.SERIALIZER);
        registerSerializer(LockFolder.SERIALIZER);
        registerSerializer(LockFolder.Lock.SERIALIZER);
        registerSerializer(RevisionFolder.SERIALIZER);
        registerSerializer(RevisionJournal.SERIALIZER);
        registerSerializer(RootFolder.SERIALIZER);
        registerSerializer(Node.SERIALIZER);
        registerSerializer(InnerNode.SERIALIZER);
        registerSerializer(CommitMarker.SERIALIZER);
        registerSerializer(BPlusTree.SERIALIZER);
        registerSerializer(BTree.SERIALIZER);
        registerSerializer(Item.SERIALIZER);
    }

    public static <T> T readExternal(DataInput in, Serializer<T> serializer) throws IOException {
        int type = in.readInt();
        if (type == 0) return null;
        if (type != serializer.typeCode()) throw new IOException("Unexpected type code.  Expected " + serializer.typeCode() + ", read " + type + ".\nPossibly corrupted data.");
        return serializer.readExternal(in);
    }
}
