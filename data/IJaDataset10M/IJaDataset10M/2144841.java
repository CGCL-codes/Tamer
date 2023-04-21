package src.lib.ioInterfaces;

import java.util.LinkedList;
import java.util.Vector;
import src.lib.objects.AlignedRead;

/**
 * Special iterator wrapper for times when reads must be pushed back into the iterator for later use.
 * @author VSRAP
 * @version $Revision: 2876 $
 *
 */
public class PushBackIteratorWrapper {

    private LinkedList<AlignedRead> buffer_ahead;

    private Generic_AlignRead_Iterator it;

    public PushBackIteratorWrapper(Generic_AlignRead_Iterator it) {
        this.it = it;
        buffer_ahead = new LinkedList<AlignedRead>();
    }

    public boolean has_next() {
        return it.hasNext() || buffer_ahead.size() > 0;
    }

    public AlignedRead next() {
        if (buffer_ahead.size() > 0) {
            return buffer_ahead.remove(0);
        } else {
            return it.next();
        }
    }

    public void add_back(AlignedRead ar) {
        buffer_ahead.add(ar);
    }

    public void add_back(Vector<AlignedRead> buffer) {
        for (int r = 0; r < buffer.size(); r++) {
            buffer_ahead.add(buffer.get(r));
        }
    }

    public void push_back(AlignedRead ar) {
        buffer_ahead.addFirst(ar);
    }

    public void push_back(LinkedList<AlignedRead> buffer) {
        while (buffer.size() > 0) {
            buffer_ahead.addFirst(buffer.removeLast());
        }
    }
}
