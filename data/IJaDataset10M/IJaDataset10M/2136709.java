package pogvue.datamodel;

import pogvue.analysis.AAFrequency;
import pogvue.util.Comparison;
import pogvue.util.QuickSort;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.*;

/** Data structure to hold and manipulate a multiple sequence alignment
 */
public final class Alignment implements AlignmentI {

    private SequenceRegion sequenceRegion;

    private final Vector sequences;

    private Vector groups = new Vector();

    public Hashtable[] cons;

    private String gapCharacter = "-";

    private Vector aaFrequency;

    private boolean aaFrequencyValid = false;

    private int maxLength = -1;

    public Alignment() {
        sequences = new Vector();
    }

    public Alignment(SequenceI[] seqs) {
        sequences = new Vector();
        for (SequenceI seq : seqs) {
            sequences.addElement(seq);
        }
        groups.addElement(new SequenceGroup());
        int i = 0;
        while (i < seqs.length) {
            addToGroup((SequenceGroup) groups.elementAt(0), seqs[i]);
            i++;
        }
        maxLength = -1;
        getWidth();
    }

    public Vector getSequences() {
        return sequences;
    }

    public Sequence[] getSequenceArray() {
        return (Sequence[]) (sequences.toArray(new Sequence[sequences.size()]));
    }

    public Sequence[] getFilledSequenceArray(int start, int end, int frac) {
        Sequence[] tmp = (Sequence[]) (sequences.toArray(new Sequence[sequences.size()]));
        int i = 0;
        Vector newseq = new Vector();
        while (i < tmp.length) {
            if (tmp[i].getSequence().length() > 0) {
                String str = tmp[i].getSequence(start, end);
                Pattern p = Pattern.compile("-");
                Matcher m = p.matcher(str);
                int count = 0;
                while (m.find()) {
                    count++;
                }
                System.out.println("Count " + tmp[i].getName() + " " + count);
                if (count / str.length() < 0.1) {
                    newseq.addElement(new Sequence(tmp[i].getName(), str, 1, str.length()));
                }
            }
            i++;
        }
        return (Sequence[]) (newseq.toArray(new Sequence[newseq.size()]));
    }

    public SequenceI getSequenceAt(int i) {
        if (i < sequences.size()) {
            return (SequenceI) sequences.elementAt(i);
        }
        return null;
    }

    public void insertSequenceAt(SequenceI seq, int pos) {
        if (pos >= 0 && pos < getHeight()) {
            sequences.insertElementAt(seq, pos);
        }
    }

    /** Adds a sequence to the alignment.  Recalculates maxLength and size.
   * Should put the new sequence in a sequence group!!!
   * 
   * @param snew 
   */
    public void addSequence(SequenceI snew) {
        sequences.addElement(snew);
    }

    public void addSequence(SequenceI[] seq) {
        for (SequenceI aSeq : seq) {
            addSequence(aSeq);
        }
    }

    /** Adds a sequence to the alignment.  Recalculates maxLength and size.
   * Should put the new sequence in a sequence group!!!
   * 
   * @param snew 
   */
    public void setSequenceAt(int i, SequenceI snew) {
        SequenceI oldseq = getSequenceAt(i);
        deleteSequence(oldseq);
        sequences.setElementAt(snew, i);
        ((SequenceGroup) groups.lastElement()).addSequence(snew);
    }

    public Vector getGroups() {
        return groups;
    }

    /** Sorts the sequences by sequence group size - largest to smallest.
   * Uses QuickSort.
   */
    public void sortGroups() {
        float[] arr = new float[groups.size()];
        Object[] s = new Object[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            arr[i] = ((SequenceGroup) groups.elementAt(i)).sequences.size();
            s[i] = groups.elementAt(i);
        }
        QuickSort.sort(arr, s);
        Vector newg = new Vector(groups.size());
        for (int i = groups.size() - 1; i >= 0; i--) {
            newg.addElement(s[i]);
        }
        groups = newg;
    }

    /** Takes out columns consisting entirely of gaps (-,.," ")
   */
    public void removeGaps() {
        for (int i = 0; i < getWidth(); i++) {
            boolean flag = true;
            for (int j = 0; j < getHeight(); j++) {
                if (getSequenceAt(j).getLength() > i) {
                    if (!(getSequenceAt(j).getSequence().substring(i, i + 1).equals("-")) && !(getSequenceAt(j).getSequence().substring(i, i + 1).equals(".")) && !(getSequenceAt(j).getSequence().substring(i, i + 1).equals(" "))) {
                        flag = false;
                    }
                }
            }
            if (flag) {
                System.out.println("Deleting column " + i);
                deleteColumns(i + 1, i + 1);
            }
        }
    }

    /** Returns an array of Sequences containing columns
   * start to end (inclusive) only.
   * 
   * @param start start column to fetch
   * @param end end column to fetch
   * @return Array of Sequences, ready to put into a new Alignment
   */
    public SequenceI[] getColumns(int start, int end) {
        return getColumns(0, getHeight() - 1, start, end);
    }

    /** Removes a range of columns (start to end inclusive).
   * 
   * @param start Start column in the alignment
   * @param end End column in the alignment
   */
    public void deleteColumns(int start, int end) {
        deleteColumns(0, getHeight() - 1, start, end);
    }

    public void deleteColumns(int seq1, int seq2, int start, int end) {
        for (int i = 0; i <= (end - start); i++) {
            for (int j = seq1; j <= seq2; j++) {
                getSequenceAt(j).deleteCharAt(start);
            }
        }
    }

    public void insertColumns(SequenceI[] seqs, int pos) {
        if (seqs.length == getHeight()) {
            for (int i = 0; i < getHeight(); i++) {
                String tmp = getSequenceAt(i).getSequence();
                getSequenceAt(i).setSequence(tmp.substring(0, pos) + seqs[i].getSequence() + tmp.substring(pos));
            }
        }
    }

    public SequenceI[] getColumns(int seq1, int seq2, int start, int end) {
        SequenceI[] seqs = new Sequence[(seq2 - seq1) + 1];
        for (int i = seq1; i <= seq2; i++) {
            seqs[i] = new Sequence(getSequenceAt(i).getName(), getSequenceAt(i).getSequence().substring(start, end), getSequenceAt(i).findPosition(start), getSequenceAt(i).findPosition(end));
        }
        return seqs;
    }

    public void trimLeft(int i) {
        for (int j = 0; j < getHeight(); j++) {
            SequenceI s = getSequenceAt(j);
            int newstart = s.findPosition(i);
            s.setStart(newstart);
            s.setSequence(s.getSequence().substring(i));
        }
    }

    public void trimRight(int i) {
        for (int j = 0; j < getHeight(); j++) {
            SequenceI s = getSequenceAt(j);
            int newend = s.findPosition(i);
            s.setEnd(newend);
            s.setSequence(s.getSequence().substring(0, i + 1));
        }
    }

    public void deleteSequence(SequenceI s) {
        for (int i = 0; i < getHeight(); i++) {
            if (getSequenceAt(i) == s) {
                deleteSequence(i);
            }
        }
    }

    public void deleteSequence(int i) {
        sequences.removeElementAt(i);
    }

    public Vector removeRedundancy(float threshold, Vector sel) {
        Vector del = new Vector();
        for (int i = 1; i < sel.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (!del.contains(sel.elementAt(i)) || !del.contains(sel.elementAt(j))) {
                    float pid = Comparison.compare((SequenceI) sel.elementAt(j), (SequenceI) sel.elementAt(i));
                    if (pid >= threshold) {
                        if (((SequenceI) sel.elementAt(j)).getSequence().length() > ((SequenceI) sel.elementAt(i)).getSequence().length()) {
                            del.addElement(sel.elementAt(i));
                            System.out.println("Deleting sequence " + ((SequenceI) sel.elementAt(i)).getName());
                        } else {
                            del.addElement(sel.elementAt(i));
                            System.out.println("Deleting sequence " + ((SequenceI) sel.elementAt(i)).getName());
                        }
                    }
                }
            }
        }
        for (int i = 0; i < del.size(); i++) {
            System.out.println("Deleting sequence " + ((SequenceI) del.elementAt(i)).getName());
            deleteSequence((SequenceI) del.elementAt(i));
        }
        return del;
    }

    public void sortByPID(SequenceI s) {
        float scores[] = new float[getHeight()];
        SequenceI seqs[] = new SequenceI[getHeight()];
        for (int i = 0; i < getHeight(); i++) {
            scores[i] = Comparison.compare(getSequenceAt(i), s);
            seqs[i] = getSequenceAt(i);
        }
        QuickSort.sort(scores, 0, scores.length - 1, seqs);
        int len;
        if (getHeight() % 2 == 0) {
            len = getHeight() / 2;
        } else {
            len = (getHeight() + 1) / 2;
        }
        for (int i = 0; i < len; i++) {
            SequenceI tmp = seqs[i];
            sequences.setElementAt(seqs[getHeight() - i - 1], i);
            sequences.setElementAt(tmp, getHeight() - i - 1);
        }
    }

    public void sortByID() {
        String ids[] = new String[getHeight()];
        SequenceI seqs[] = new SequenceI[getHeight()];
        for (int i = 0; i < getHeight(); i++) {
            ids[i] = getSequenceAt(i).getName();
            seqs[i] = getSequenceAt(i);
        }
        QuickSort.sort(ids, seqs);
        int len;
        if (getHeight() % 2 == 0) {
            len = getHeight() / 2;
        } else {
            len = (getHeight() + 1) / 2;
            System.out.println("Sort len is odd = " + len);
        }
        for (int i = 0; i < len; i++) {
            System.out.println("Swapping " + seqs[i].getName() + " and " + seqs[getHeight() - i - 1].getName());
            SequenceI tmp = seqs[i];
            sequences.setElementAt(seqs[getHeight() - i - 1], i);
            sequences.setElementAt(tmp, getHeight() - i - 1);
        }
    }

    /**    */
    public SequenceGroup findGroup(int i) {
        return findGroup(getSequenceAt(i));
    }

    /**    */
    public SequenceGroup findGroup(SequenceI s) {
        for (int i = 0; i < this.groups.size(); i++) {
            SequenceGroup sg = (SequenceGroup) groups.elementAt(i);
            if (sg.sequences.contains(s)) {
                return sg;
            }
        }
        return null;
    }

    /**    */
    public void addToGroup(SequenceGroup g, SequenceI s) {
        if (!(g.sequences.contains(s))) {
            g.sequences.addElement(s);
        }
    }

    /**    */
    public void removeFromGroup(SequenceGroup g, SequenceI s) {
        if (g != null && g.sequences != null) {
            if (g.sequences.contains(s)) {
                g.sequences.removeElement(s);
                if (g.sequences.size() == 0) {
                    groups.removeElement(g);
                }
            }
        }
    }

    /**    */
    public void addGroup(SequenceGroup sg) {
        groups.addElement(sg);
    }

    /**    */
    public SequenceGroup addGroup() {
        SequenceGroup sg = new SequenceGroup();
        groups.addElement(sg);
        return sg;
    }

    /**    */
    public void deleteGroup(SequenceGroup g) {
        if (groups.contains(g)) {
            groups.removeElement(g);
        }
    }

    /**    */
    public SequenceI findName(String name) {
        int i = 0;
        while (i < sequences.size()) {
            SequenceI s = getSequenceAt(i);
            if (s.getName().equals(name)) {
                return s;
            }
            i++;
        }
        return null;
    }

    /**    */
    public int findIndex(SequenceI s) {
        int i = 0;
        while (i < sequences.size()) {
            if (s == getSequenceAt(i)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int getHeight() {
        return sequences.size();
    }

    public Hashtable getNameHash() {
        Hashtable hash = new Hashtable();
        for (int i = 0; i < sequences.size(); i++) {
            String name = getSequenceAt(i).getName();
            hash.put(name, getSequenceAt(i));
        }
        return hash;
    }

    public int getWidth() {
        if (maxLength == -1) {
            for (int i = 0; i < sequences.size(); i++) {
                if (getSequenceAt(i).getLength() > maxLength) {
                    maxLength = getSequenceAt(i).getLength();
                }
            }
        }
        return maxLength;
    }

    public int getMaxIdLength() {
        int max = 0;
        int i = 0;
        while (i < sequences.size()) {
            SequenceI seq = getSequenceAt(i);
            String tmp = seq.getName() + "/" + seq.getStart() + "-" + seq.getEnd();
            if (tmp.length() > max) {
                max = tmp.length();
            }
            i++;
        }
        return max;
    }

    public void setGapCharacter(String gc) {
        gapCharacter = gc;
    }

    public String getGapCharacter() {
        return gapCharacter;
    }

    public Vector getAAFrequency() {
        if (aaFrequency == null || !aaFrequencyValid) {
            aaFrequency = AAFrequency.calculate(sequences, 1, getWidth());
            aaFrequencyValid = true;
        }
        return aaFrequency;
    }

    public void setSequenceRegion(SequenceRegion r) {
        this.sequenceRegion = r;
        maxLength = -1;
        getWidth();
    }

    public SequenceRegion getSequenceRegion() {
        return sequenceRegion;
    }

    public void addSequences(Vector feat) {
        for (int i = 0; i < feat.size(); i++) {
            Sequence seq = (Sequence) feat.elementAt(i);
            addSequence(seq);
        }
    }

    public void addSequences(Vector feat, int space) {
        for (int i = 0; i < feat.size(); i++) {
            Sequence seq = (Sequence) feat.elementAt(i);
            for (int j = 0; j < space; j++) {
                GFF gff = new GFF(seq.getName(), "", 1, 2);
                addSequence(gff);
                j++;
            }
            addSequence(seq);
        }
    }

    public static Alignment getDummyAlignment(String name, String chr, int start, int end) {
        SequenceI[] s = new SequenceI[1];
        StringBuffer tmpseq = new StringBuffer();
        int i = 0;
        while (i < end - start + 1) {
            tmpseq.append('X');
            i++;
        }
        s[0] = new Sequence(name, tmpseq.toString(), 1, end - start + 1);
        Alignment al = new Alignment(s);
        SequenceRegion r = new SequenceRegion(chr, start, end);
        al.setSequenceRegion(r);
        return al;
    }
}
