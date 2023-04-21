package mx.unam.ecologia.gye.model;

import cern.colt.list.LongArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory for {@link Sequence} instances.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class SequenceFactory {

    private static Map<String, Repeat> c_RepeatCache = new HashMap<String, Repeat>();

    public static SequenceUnit createSequenceUnit(String str) {
        final int len = str.length();
        if (len == 1) {
            return Bases.fromCharSym(str.charAt(0));
        } else {
            Repeat r = c_RepeatCache.get(str);
            if (r == null) {
                r = new Repeat(len);
                for (int i = 0; i < len; i++) {
                    r.add(Bases.fromCharSym(str.charAt(i)));
                }
                r.setAssembled();
                c_RepeatCache.put(str, r);
            }
            return r;
        }
    }

    public static Sequence createRepeatSequence(String str) {
        int idx = str.indexOf('(');
        if (idx > 0) {
            final int times = Integer.parseInt(str.substring(idx + 1, str.indexOf(')')));
            SequenceImpl seq = new SequenceImpl(idx);
            SequenceUnit su = createSequenceUnit(str.substring(0, idx));
            for (int i = 0; i < times; i++) {
                seq.add(su);
            }
            return seq;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Sequence createSequence(String str) {
        if (str.contains("(")) {
            return createRepeatSequence(str);
        } else {
            SequenceImpl seq = new SequenceImpl(1);
            final int len = str.length();
            for (int i = 0; i < len; i++) {
                seq.add(Bases.fromCharSym(str.charAt(i)));
            }
            return seq;
        }
    }

    public static CompoundSequence createCompoundSequence(String str) {
        if (str.startsWith("$")) {
            CompoundSequenceImpl cseq = new CompoundSequenceImpl();
            String[] sqs = str.substring(1).split("-");
            for (int i = 0; i < sqs.length; i++) {
                String[] strs = sqs[i].split(";");
                SequenceImpl s = (SequenceImpl) createSequence(strs[0]);
                if (strs.length == 2) {
                    s.setTracingChanges(true);
                    LongArrayList lal = s.getChangeTrace();
                    String[] muts = strs[1].split(",");
                    for (int j = 0; j < muts.length; j++) {
                        lal.add(Long.parseLong(muts[j]));
                    }
                }
                cseq.add(s);
            }
            return cseq;
        } else {
            if (str.indexOf('-') > 0) {
                CompoundSequenceImpl cseq = new CompoundSequenceImpl();
                String[] reps = str.split("-");
                for (int i = 0; i < reps.length; i++) {
                    cseq.add(createSequence(reps[i]));
                }
                return cseq;
            } else {
                CompoundSequenceImpl cseq = new CompoundSequenceImpl();
                cseq.add(createSequence(str));
                return cseq;
            }
        }
    }
}
