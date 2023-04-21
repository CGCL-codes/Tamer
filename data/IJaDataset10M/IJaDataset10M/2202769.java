package org.archive.util.anvl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.archive.io.UTF8Bytes;

/**
 * List of {@link ANVLRecord}s.
 * @author stack
 * @version $Date: 2006-08-05 01:15:47 +0000 (Sat, 05 Aug 2006) $ $Version$
 */
public class ANVLRecords extends ArrayList<ANVLRecord> implements UTF8Bytes {

    private static final long serialVersionUID = 5361551920550106113L;

    public ANVLRecords() {
        super();
    }

    public ANVLRecords(int initialCapacity) {
        super(initialCapacity);
    }

    public ANVLRecords(Collection<ANVLRecord> c) {
        super(c);
    }

    public byte[] getUTF8Bytes() throws UnsupportedEncodingException {
        return toString().getBytes(UTF8);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (final Iterator i = iterator(); i.hasNext(); ) {
            sb.append(i.next().toString());
        }
        return super.toString();
    }
}
