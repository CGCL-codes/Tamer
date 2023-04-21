package edu.kds.circuit.misc;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

public class SeqRange extends Range {

    private static final long serialVersionUID = -8557331469134400204L;

    final List<Clause> clauses = new LinkedList<Clause>();

    private final Pattern clausePattern;

    private final Pattern clauseListPattern;

    public SeqRange() {
        String num = "\\d+";
        String seq = num + "\\.\\." + num;
        String seqDiv = seq + "/" + num;
        String clause = "\\p{Blank}*(" + num + "|" + seq + "|" + seqDiv + ")\\p{Blank}*";
        String multClause = "(" + clause + "\\p{Blank}*\\*\\p{Blank}*" + num + "\\p{Blank}*|" + clause + ")";
        String clauseList = "(" + multClause + ",)*(" + multClause + ")";
        clausePattern = Pattern.compile(multClause);
        clauseListPattern = Pattern.compile(clauseList);
    }

    public SeqRange(int i) {
        this();
        addIndex(i);
    }

    public SeqRange(int start, int end) {
        this();
        clauses.add(new Sequence(start, end));
    }

    public SeqRange(String clauses) {
        this();
        if (!clauseListPattern.matcher(clauses).matches()) throw new Error("Clause list is ill-formed (" + clauses + ")");
        String[] clauseArr = clauses.split(",");
        for (String c : clauseArr) addClause(c);
    }

    public Iterator<Integer> iterator() {
        List<Integer> iList = new LinkedList<Integer>();
        for (Clause c : clauses) for (Integer i : c) iList.add(i);
        return iList.iterator();
    }

    @Override
    public int getSize() {
        int ret = 0;
        for (Clause c : clauses) ret += c.size();
        return ret;
    }

    public void addIndex(int i) {
        clauses.add(new SingleBit(i));
        fireStateChanged(INDEX_CHANGE_EVENT);
    }

    public void remIndex(int i) {
        Clause remMe = null;
        for (Clause ii : clauses) {
            if (ii instanceof SingleBit && ((SingleBit) ii).bit == i) {
                remMe = ii;
                break;
            }
        }
        clauses.remove(remMe);
        fireStateChanged(INDEX_CHANGE_EVENT);
    }

    public void addClause(String clauseStr) {
        if (!clausePattern.matcher(clauseStr).matches()) throw new Error("Clause is ill-formed (" + clauseStr + ")");
        int mPos = clauseStr.indexOf('*');
        int fact = 1;
        if (mPos > 0) {
            try {
                fact = Integer.parseInt(clauseStr.substring(mPos + 1).trim());
            } catch (NumberFormatException exc) {
                exc.printStackTrace();
            }
            clauseStr = clauseStr.substring(0, mPos).trim();
        }
        Clause addThis = null;
        int pos1 = clauseStr.indexOf("..");
        int pos2 = clauseStr.indexOf('/');
        if (pos1 >= 0) {
            String sIndex = clauseStr.substring(0, pos1).trim();
            String eIndex = clauseStr.substring(pos1 + 2, (pos2 > 0) ? pos2 : clauseStr.length()).trim();
            if (pos2 > 0) {
                String div = clauseStr.substring(pos2 + 1).trim();
                try {
                    int s = Integer.parseInt(sIndex);
                    int e = Integer.parseInt(eIndex);
                    int d = Integer.parseInt(div);
                    addThis = new SequenceDiv(s, e, d);
                } catch (NumberFormatException exc) {
                    exc.printStackTrace();
                }
            } else {
                try {
                    int s = Integer.parseInt(sIndex);
                    int e = Integer.parseInt(eIndex);
                    addThis = new Sequence(s, e);
                } catch (NumberFormatException exc) {
                    exc.printStackTrace();
                }
            }
        } else {
            try {
                int b = Integer.parseInt(clauseStr.trim());
                addThis = new SingleBit(b);
            } catch (NumberFormatException exc) {
                exc.printStackTrace();
            }
        }
        if (fact > 1) {
            clauses.add(new MultClause(addThis, fact));
        } else {
            clauses.add(addThis);
        }
        super.fireStateChanged(INDEX_CHANGE_EVENT);
    }

    public void adaptClausesFrom(SeqRange r) {
        clearRange();
        for (Clause c : r.clauses) {
            clauses.add(c);
        }
        super.fireStateChanged(INDEX_CHANGE_EVENT);
    }

    @Override
    public boolean overlaps(Range r) {
        for (Clause c : clauses) {
            if (c.overlaps(r)) return true;
        }
        return false;
    }

    @Override
    public boolean overlaps(int i) {
        for (Clause c : clauses) {
            if (c.overlaps(i)) return true;
        }
        return false;
    }

    public void compress() {
        SingleBit firstBit = null;
        ;
        SingleBit prevBit = null;
        List<Clause> tmp = new LinkedList<Clause>(clauses);
        for (Clause c : tmp) {
            if (c instanceof SingleBit) {
                if (firstBit == null) {
                    firstBit = prevBit = (SingleBit) c;
                    continue;
                }
                if (prevBit.bit == ((SingleBit) c).bit - 1) {
                    clauses.remove(prevBit);
                    prevBit = (SingleBit) c;
                    continue;
                }
                if (firstBit != prevBit) {
                    int p = clauses.indexOf(prevBit);
                    clauses.add(p, new Sequence(firstBit.bit, prevBit.bit));
                    clauses.remove(prevBit);
                }
                firstBit = prevBit = null;
            }
        }
        if (firstBit != prevBit) {
            int p = clauses.indexOf(prevBit);
            clauses.add(p, new Sequence(firstBit.bit, prevBit.bit));
            clauses.remove(prevBit);
        }
    }

    public void trim(int size) {
        int excess = getSize() - size;
        if (excess <= 0) return;
        for (int i = clauses.size() - 1; i >= 0; i--) {
            Clause c = clauses.get(i);
            int cSize = c.size();
            if (cSize <= excess) {
                clauses.remove(c);
                excess -= cSize;
                if (excess <= 0) break;
            } else {
                c.trim(excess);
                break;
            }
        }
        super.fireStateChanged(INDEX_CHANGE_EVENT);
    }

    public boolean truncate(int maxIndex) {
        List<Clause> tmp = new LinkedList<Clause>(clauses);
        for (Clause c : tmp) {
            if (c.truncate(maxIndex)) clauses.remove(c);
        }
        super.fireStateChanged(INDEX_CHANGE_EVENT);
        return clauses.isEmpty();
    }

    public void clearRange() {
        clauses.clear();
    }

    public String toString() {
        String ret = "[";
        for (Clause c : clauses) {
            ret += c + ",";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret + "]";
    }

    @Override
    public String toCompactString() {
        return toString();
    }

    public String toExtensiveString() {
        return toString();
    }

    public Range clone() {
        SeqRange ret = new SeqRange();
        for (Clause c : clauses) {
            ret.clauses.add(c.clone());
        }
        return ret;
    }

    public static final int INDEX_CHANGE_EVENT = c++;

    /**
	 * Implementing methods are required to be immutable due to the 
	 * choice of clone in SeqRange.
	 * @author Rasmus
	 */
    private static interface Clause extends Iterable<Integer> {

        boolean overlaps(Range r);

        boolean overlaps(int bit);

        Iterator<Integer> iterator();

        int size();

        void trim(int amt);

        /**
		 * @return true if this entire clause should be deleted due 
		 * to the truncate, false otherwise.
		 */
        boolean truncate(int maxIndex);

        public Clause clone();
    }

    private static class SingleBit implements Clause, Serializable {

        private static final long serialVersionUID = 5186798807746031189L;

        final int bit;

        SingleBit(int bit) {
            this.bit = bit;
        }

        public boolean overlaps(Range r) {
            return r.overlaps(bit);
        }

        public boolean overlaps(int bit) {
            return this.bit == bit;
        }

        public Iterator<Integer> iterator() {
            List<Integer> l = new ArrayList<Integer>(1);
            l.add(bit);
            return l.iterator();
        }

        public int size() {
            return 1;
        }

        public void trim(int amt) {
            throw new Error("SingleBit.trim(" + amt + ") should not be possible");
        }

        public boolean truncate(int maxIndex) {
            return bit > maxIndex;
        }

        public String toString() {
            return bit + "";
        }

        public SingleBit clone() {
            return new SingleBit(bit);
        }
    }

    private static class SequenceDiv implements Clause, Serializable {

        private static final long serialVersionUID = 3149023696528489324L;

        int startIndex, endIndex, div;

        SequenceDiv(int startIndex, int endIndex, int div) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.div = div;
        }

        public boolean overlaps(Range r) {
            for (Integer b : r) {
                if (overlaps(b)) return true;
            }
            return false;
        }

        public boolean overlaps(int bit) {
            int max = Math.max(startIndex, endIndex);
            int min = Math.min(startIndex, endIndex);
            return (bit - startIndex) % div == 0 && bit >= min && bit <= max;
        }

        public Iterator<Integer> iterator() {
            List<Integer> l = new ArrayList<Integer>(1);
            for (int i = startIndex; (startIndex > endIndex) ? i >= endIndex : i <= endIndex; i += (startIndex > endIndex) ? -div : div) l.add(i);
            return l.iterator();
        }

        public int size() {
            return (int) Math.floor(Math.abs(endIndex - startIndex) / div + 1);
        }

        public void trim(int amt) {
            assert (size() > amt);
            if (startIndex > endIndex) {
                endIndex += amt * div;
            } else {
                endIndex -= amt * div;
            }
        }

        public boolean truncate(int maxIndex) {
            if (Math.min(startIndex, endIndex) > maxIndex) return true;
            if (Math.max(startIndex, endIndex) <= maxIndex) return false;
            if (startIndex > maxIndex) {
                startIndex = maxIndex;
                return false;
            }
            if (endIndex > maxIndex) {
                endIndex = maxIndex;
                return false;
            }
            throw new Error("This code should be unreachable");
        }

        public String toString() {
            return startIndex + ".." + endIndex + "/" + div;
        }

        public SequenceDiv clone() {
            return new SequenceDiv(startIndex, endIndex, div);
        }
    }

    private static class Sequence extends SequenceDiv {

        private static final long serialVersionUID = -1102372677868199384L;

        Sequence(int startIndex, int endIndex) {
            super(startIndex, endIndex, 1);
        }

        public String toString() {
            return startIndex + ".." + endIndex;
        }

        public Sequence clone() {
            return new Sequence(startIndex, endIndex);
        }
    }

    private static class MultClause implements Clause {

        private final Clause clause;

        private int fact;

        MultClause(Clause c, int fact) {
            assert fact > 0;
            this.clause = c;
            this.fact = fact;
        }

        public String toString() {
            return clause + "*" + fact;
        }

        public MultClause clone() {
            return new MultClause(clause.clone(), fact);
        }

        public Iterator<Integer> iterator() {
            List<Integer> l = new ArrayList<Integer>(clause.size() * fact);
            for (int f = 0; f < fact; f++) {
                for (Integer i : clause) {
                    l.add(i);
                }
            }
            return l.iterator();
        }

        public boolean overlaps(Range r) {
            return clause.overlaps(r);
        }

        public boolean overlaps(int bit) {
            return clause.overlaps(bit);
        }

        public int size() {
            return clause.size() * fact;
        }

        public void trim(int amt) {
            int cSize = clause.size();
            while (amt > 0 && fact > 1) {
                fact--;
                amt -= cSize;
            }
            if (amt > 0) {
                clause.trim(amt);
            }
        }

        public boolean truncate(int maxIndex) {
            return clause.truncate(maxIndex);
        }
    }
}
