package net.sf.jannot;

import java.util.BitSet;

public class Alignment {

    private String name;

    private Sequence alignment;

    private Sequence reference;

    private int refGapCount;

    private int[] mapping = null;

    public Alignment(String name, Sequence sequence, Sequence reference) {
        this.name = name;
        this.alignment = sequence;
        this.reference = reference;
        refGapCount = countGaps(reference);
        aligned = new BitSet();
        mapping = new int[reference.size()];
        int index = 0;
        int pos = 0;
        for (int i = 0; i < reference.size(); i++) {
            pos++;
            if (reference.getNucleotide(i + 1) != '-') {
                mapping[index++] = pos;
            }
        }
        for (int i = index; i < reference.size(); i++) {
            mapping[i] = pos + 1;
        }
        for (int i = 1; i < this.refLength(); i++) {
            char inf = getNucleotide(i);
            char ref = getReferenceNucleotide(i);
            if (inf != '-' && ref != '-' && Character.toLowerCase(inf) == Character.toLowerCase(ref)) aligned.set(i);
        }
    }

    private int countGaps(Sequence referenceMapping2) {
        int gaps = 0;
        for (int i = 1; i <= referenceMapping2.size(); i++) {
            if (referenceMapping2.getNucleotide(i) == '-') gaps++;
        }
        return gaps;
    }

    /**
	 * Returns the name of this alignment
	 * 
	 * @return
	 */
    public String name() {
        return name;
    }

    /**
	 * Returns the alignment position if you know the position in the reference
	 * sequence. Suppose you have the following reference sequence
	 * 
	 * <pre>
	 * AAAATTTT
	 * </pre>
	 * 
	 * The corresponding alignment to some other sequence of this reference is
	 * 
	 * <pre>
	 * AAAA----TTTT
	 * </pre>
	 * 
	 * Then this method allows you the query which position in the original
	 * reference sequence corresponds to which position in the reference
	 * alignment.
	 * 
	 * For example 5 in the reference sequence maps to 9 in the reference
	 * alignment. And 4 maps to 4.
	 * 
	 * Both coordinates are 1 based!
	 * 
	 */
    private int ref2aln(int position) {
        if (mapping[position - 1] == 0) System.out.println("ref2aln: " + (position - 1) + "\t" + mapping[position - 1]);
        return mapping[position - 1];
    }

    /**
	 * Gives the nucleotide that appears at the given position in the alignment.
	 * The coordinates are in the reference sequence.
	 * 
	 * @param pos
	 *            position the get nucleotide
	 * @return nucleotide at provided position
	 */
    public char getNucleotide(int pos) {
        return alignment.getNucleotide(ref2aln(pos));
    }

    /**
	 * Gives the nucleotide that appears at the given position in the reference
	 * sequence. The coordinates are in the reference sequence.
	 * 
	 * @param pos
	 *            position the get nucleotide
	 * @return nucleotide at provided position
	 */
    public char getReferenceNucleotide(int pos) {
        return reference.getNucleotide(ref2aln(pos));
    }

    private BitSet aligned;

    /**
	 * Returns whether the supplied position is aligned. Two sequences are
	 * aligned in a position if the share the same nucleotide.
	 * 
	 * @param pos
	 * @return
	 */
    public boolean isAligned(int pos) {
        return aligned.get(pos);
    }

    public String getSubSequence(int start, int end) {
        return alignment.getSubSequence(ref2aln(start), ref2aln(end));
    }

    /**
	 * Returns the length of the reference sequence. This is the length of the
	 * reference alignment minus all gaps.
	 * 
	 * @return
	 */
    public int refLength() {
        return reference.size() - refGapCount;
    }
}
