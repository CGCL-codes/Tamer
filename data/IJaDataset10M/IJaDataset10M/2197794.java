package org.ozoneDB.core.storage.experimental.wizardStore;

import java.io.*;
import org.ozoneDB.DxLib.*;
import org.ozoneDB.core.ObjectID;

/**
 * This extension of the DxDiskHashNodeLeaf class assumes that the key and data
 * member of the stored DxKayData pairs are ObjectIDs. Thus is casts and writes
 * them directly to the stream for better performance.
 */
class IDTableNodeLeaf extends DxDiskHashNodeLeaf implements Externalizable {

    static final long serialVersionUID = 1L;

    public IDTableNodeLeaf(DxDiskHashMap _grandParent) {
        super(_grandParent);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        byte c = 0;
        for (DxKeyData elem = element; elem != null; elem = elem.next) {
            c++;
        }
        out.writeByte(c);
        DxKeyData elem = element;
        while (elem != null) {
            ((ObjectID) elem.key).writeExternal(out);
            ((ObjectID) elem.data).writeExternal(out);
            elem = elem.next;
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        byte c = in.readByte();
        ObjectID key = ((IDTable) grandParent).newObjectID();
        key.readExternal(in);
        org.ozoneDB.core.storage.ClusterID data = ((IDTable) grandParent).newClusterID();
        data.readExternal(in);
        element = grandParent.newKeyData();
        element.set(key, data);
        DxKeyData elem = element;
        for (int i = 1; i < c; i++) {
            key = ((IDTable) grandParent).newObjectID();
            key.readExternal(in);
            data = ((IDTable) grandParent).newClusterID();
            data.readExternal(in);
            elem.next = grandParent.newKeyData();
            elem.next.set(key, data);
            elem = elem.next;
        }
    }
}
