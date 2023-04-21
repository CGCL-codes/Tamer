package jaxlib.io.stream;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: ObjectIO.java 2730 2009-04-21 01:12:29Z joerg_wassmer $
 */
public class ObjectIO extends DataIO {

    protected ObjectIO() throws InstantiationException {
        throw new InstantiationException();
    }

    public static Object[] readArraySkipNonSerializables(final ObjectInput in) throws IOException, ClassNotFoundException {
        int remaining = in.readInt();
        if (remaining < 0) return null;
        final Object[] a = (Object[]) Array.newInstance(((Class) in.readObject()).getComponentType(), remaining);
        int i = 0;
        while (--remaining >= 0) {
            final Object e;
            try {
                e = in.readObject();
            } catch (final ObjectStreamException ex) {
                continue;
            }
            try {
                a[i] = e;
            } catch (final ArrayStoreException ex) {
                continue;
            }
            i++;
        }
        return (i < a.length) ? Arrays.copyOf(a, i) : a;
    }

    public static void writeArraySkipNonSerializables(final ObjectOutput out, Object[] a) throws IOException {
        if (a == null) {
            out.writeInt(-1);
        } else {
            a = a.clone();
            int count = a.length;
            for (int i = count; --i >= 0; ) {
                final Object e = a[i];
                if ((e != null) && !(e instanceof Serializable)) {
                    count--;
                    a[i] = a;
                }
            }
            out.writeInt(count);
            out.writeObject(a.getClass());
            if (count > 0) {
                int written = 0;
                for (int i = 0, hi = a.length; i < hi; i++) {
                    final Object e = a[i];
                    if (e != a) {
                        a[i] = null;
                        try {
                            out.writeObject(e);
                        } catch (final ObjectStreamException ex) {
                        }
                        if (++written == count) break;
                    }
                }
            }
        }
    }

    public static int writeEachObject(final Iterable<?> src, final ObjectOutput out) throws IOException {
        int count = 0;
        for (final Iterator<?> it = src.iterator(); it.hasNext(); count++) out.writeObject(it.next());
        return count;
    }
}
