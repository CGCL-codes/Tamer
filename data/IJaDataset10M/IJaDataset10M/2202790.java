package com.antlersoft.odb.diralloc;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import com.antlersoft.util.NetByte;
import com.antlersoft.odb.DiskAllocatorException;
import com.antlersoft.odb.ObjectKey;

class ClassIterator implements Iterator<ObjectKey> {

    private StreamPair pair;

    private Iterator<Integer> allocatorIterator;

    ClassIterator(StreamPair pair) throws IOException {
        this.pair = pair;
        pair.enterProtected();
        try {
            allocatorIterator = pair.allocator.iterator();
        } finally {
            pair.leaveProtected();
        }
    }

    public boolean hasNext() {
        return allocatorIterator.hasNext();
    }

    public ObjectKey next() throws NoSuchElementException {
        pair.enterProtected();
        try {
            int offset = ((Integer) allocatorIterator.next()).intValue();
            byte[] prefix;
            try {
                prefix = pair.allocator.read(offset, 8);
            } catch (IOException ioe) {
                throw new IllegalStateException("ClassIterator: Allocator I/O error");
            } catch (DiskAllocatorException dae) {
                throw new IllegalStateException("ClassIterator: Allocator error");
            }
            return new DAKey(NetByte.quadToInt(prefix, 0), NetByte.quadToInt(prefix, 4));
        } finally {
            pair.leaveProtected();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
