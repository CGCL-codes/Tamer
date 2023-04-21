package org.gnf.seqtracs.seq;

import java.io.*;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.io.*;

/**
 * <p>Title: DNASequence</p>
 * <p>Description: Adapter for biojava Sequence</p>
 * <p>Copyright: Copyright (c) 2002 GNF</p>
 * <p>Company: GNF</p>
 * @author Christian M. Zmasek (czmasek@gnf.org)
 * @version 1.0
 */
public final class DNASequence extends Seq {

    public DNASequence(final Sequence seq) throws Exception {
        if (seq.getAlphabet().getName().toUpperCase().indexOf("DNA") < 0) {
            throw new Exception("Sequence appears to be not DNA.");
        }
        initialize(seq);
    }

    private DNASequence() {
    }

    public static DNASequence getInstance(final File fasta_dna_file) throws Exception {
        SequenceIterator si = createDNASeqIterator(fasta_dna_file);
        if (!si.hasNext()) {
            throw new Exception("Failure to read in DNA sequence from FASTA file.");
        }
        DNASequence dna = new DNASequence(si.nextSequence());
        if (si.hasNext()) {
            throw new Exception("DNA FASTA file contains more than one sequence.");
        }
        return dna;
    }

    public double getPercentNoCall(int first, int last) {
        char[] s = getSequenceAsCharArray();
        int acgt = 0;
        if (first < 0) {
            first = 0;
        }
        if (last > s.length - 1) {
            last = s.length - 1;
        }
        if (last <= first) {
            return 0.0;
        }
        for (int i = first; i <= last; ++i) {
            if (s[i] == 'a' || s[i] == 'c' || s[i] == 'g' || s[i] == 't') {
                acgt++;
            }
        }
        int le = last - first + 1;
        return (((double) (le - acgt) / le) * 100.0);
    }

    public char[] getSequenceAsCharArray() {
        return (getSequence().seqString().toLowerCase().toCharArray());
    }

    public static SequenceIterator createDNASeqIterator(File fasta_file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fasta_file));
        SequenceIterator stream = SeqIOTools.readFastaDNA(br);
        return stream;
    }
}
