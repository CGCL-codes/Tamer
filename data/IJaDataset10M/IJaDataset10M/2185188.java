package org.vexi.plat;

import java.io.InputStream;
import org.ibex.util.Hash;
import org.vexi.graphics.Picture;

/** common superclass for all platforms that use GCJ to compile a native binary */
public abstract class GCJ extends Platform {

    protected native InputStream _getBuiltinInputStream();

    protected native void _decodeJPEG(InputStream is, Picture p);

    public static final class Retainer {

        private static Hash table = new Hash();

        private static final class Key {

            private Object o;

            public Key(Object o) {
                this.o = o;
            }

            public int hashCode() {
                return o == null ? 0 : o.hashCode();
            }

            public boolean equals(Object o2) {
                return (o2 instanceof Key) && ((Key) o2).o == o;
            }
        }

        private static final class Entry {

            private int refCount;

            public Entry() {
                inc();
            }

            public void inc() {
                refCount++;
            }

            public boolean dec() {
                return --refCount == 0;
            }
        }

        public static synchronized void retain(Object o) {
            Key k = new Key(o);
            Entry r = (Entry) table.get(k);
            if (r == null) table.put(k, new Entry()); else r.inc();
        }

        public static synchronized void release(Object o) {
            Key k = new Key(o);
            Entry e = (Entry) table.get(k);
            if (e == null) throw new Error("Retainer::Release on unknown object");
            if (e.dec()) {
                table.remove(k);
            }
        }
    }
}
