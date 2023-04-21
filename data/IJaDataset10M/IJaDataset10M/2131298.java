package freeboogie.vcgen;

import java.util.TreeSet;
import com.google.common.collect.Sets;
import freeboogie.ast.Command;

/**
  Computes the read index and the write index using the
  algorithm in ESC/Java.

  We have:
    w(n) = 1 + r(n)   if n writes to X
    w(n) = r(n)       otherwise
    r(n) = w(m)       m BEFORE n and all w(m) equal
    r(n) = 1 + max_{m BEFORE n} w(m) otherwise

  @author rgrig
 */
public class Passificator extends AbstractPassivator {

    int computeReadIndex(Command c) {
        Integer alreadyComputed = readIndexCache().get(c);
        if (alreadyComputed != null) return alreadyComputed;
        int ri = 0;
        TreeSet<Integer> wm = Sets.newTreeSet();
        for (Command m : parents(c)) wm.add(computeWriteIndex(m));
        if (!wm.isEmpty()) {
            ri = wm.last();
            if (wm.size() > 1) ++ri;
        }
        readIndexCache().put(c, ri);
        return ri;
    }

    int computeWriteIndex(Command c) {
        Integer alreadyComputed = writeIndexCache().get(c);
        if (alreadyComputed != null) return alreadyComputed;
        int wi = computeReadIndex(c);
        if (writes(c)) ++wi;
        writeIndexCache().put(c, wi);
        return wi;
    }
}
