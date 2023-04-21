package org.expasy.jpl.io.mol.fasta;

import java.util.List;

/**
 * This class defines a fasta entry that is constituted of an header and a
 * sequence.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public final class FastaEntry {

    private static int COLUMN_MAX = 60;

    /** the header */
    private final FastaHeader header;

    /** the sequence */
    private String sequence;

    /**
	 * The default constructor.
	 * 
	 * @param header the header.
	 * @param sequence the sequence.
	 */
    public FastaEntry(final FastaHeader header, final String sequence) {
        this.header = header;
        if ((sequence.length() > COLUMN_MAX) && sequence.contains("\n")) {
            this.sequence = block2Line(sequence);
        } else {
            this.sequence = sequence;
        }
    }

    /**
	 * @return the header line.
	 */
    public final FastaHeader getHeader() {
        return header;
    }

    /**
	 * @return the sequence string.
	 */
    public final String getSequence() {
        return sequence;
    }

    static String list2Line(final List<String> list) {
        final StringBuffer sb = new StringBuffer();
        for (final String line : list) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
	 * Get the given entry sequence.
	 * 
	 * @param fastaEntry to get sequence from.
	 * @return entry sequence.
	 */
    static String block2Line(final String block, final int from) {
        final String[] lines = block.split("\n");
        final StringBuffer sb = new StringBuffer();
        for (int i = from; i < lines.length; i++) {
            sb.append(lines[i]);
        }
        return sb.toString();
    }

    static String block2Line(final String block) {
        return block2Line(block, 0);
    }

    static String line2Block(final String line) {
        final StringBuilder block = new StringBuilder();
        int i = 0;
        while (i < line.length() - COLUMN_MAX) {
            block.append(line.substring(i, i + COLUMN_MAX));
            block.append("\n");
            i += COLUMN_MAX;
        }
        block.append(line.substring(i));
        return block.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(header);
        sb.append("\n");
        sb.append(line2Block(sequence));
        return sb.toString();
    }
}
