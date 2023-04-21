package org.gnf.seqtracs.seq;

import org.gnf.seqtracs.seq.*;
import org.gnf.seqtracs.software.Software;
import java.io.*;
import java.sql.Date;

/**
 * <p>Title: PhredOutput</p>
 * <p>Description: Object to capture the output of phred</p>
 * <p>Copyright: Copyright (c) 2002 GNF</p>
 * <p>Company: GNF</p>
 * @author Christian M. Zmasek (czmasek@gnf.org)
 * @version 1.0
 */
public class PhredOutput {

    public PhredOutput(final File fasta_dna_file, final File fasta_qual_file, final Software phred, final String phred_options) throws Exception {
        _dna = DNASequence.getInstance(fasta_dna_file);
        _qual = QualSequence.getInstance(fasta_qual_file);
        _phred = phred;
        _options = phred_options;
    }

    private PhredOutput() {
        _dna = null;
        _qual = null;
        _phred = null;
        _options = null;
    }

    public DNASequence getDNASequence() {
        return _dna;
    }

    public QualSequence getQualSequence() {
        return _qual;
    }

    public String getPhredOptions() {
        return _options;
    }

    public Software getPhred() {
        return _phred;
    }

    public String getSeqName() {
        return getQualSequence().getName();
    }

    public String getSeqDescription() {
        return getQualSequence().getDescription();
    }

    public int getSeqLength() {
        return getQualSequence().getSeqLength();
    }

    public SequenceFeature calculateHSR(final int threshold, final int match_score, final int mismatch_score) {
        return (getQualSequence().calculateHSR(threshold, match_score, mismatch_score));
    }

    public void writeDNASequence(final OutputStream os) throws Exception {
        getDNASequence().writeSequence(os);
    }

    public void writeQualSequence(final OutputStream os) throws Exception {
        getQualSequence().writeSequence(os);
    }

    public void writeDNASequence(final File f) throws Exception {
        getDNASequence().writeSequence(f);
    }

    public void writeDNASequenceAsFasta(final File f, final String fasta_annotation) throws Exception {
        getDNASequence().writeSequenceAsFasta(f, fasta_annotation);
    }

    public void writeQualSequenceAsFasta(final File f, final String fasta_annotation) throws Exception {
        getQualSequence().writeSequenceAsFasta(f, fasta_annotation);
    }

    public void writeQualSequence(final File f) throws Exception {
        getQualSequence().writeSequence(f);
    }

    public String getDNASequenceAsString() {
        return getDNASequence().getSequenceAsString();
    }

    public String getQualSequenceAsString() {
        return getQualSequence().getSequenceAsString();
    }

    public int[] getQualSequenceAsIntArray() {
        return getQualSequence().getSequenceAsIntArray();
    }

    public char[] getDNASequencesCharArray() {
        return this.getDNASequence().getSequenceAsCharArray();
    }

    public String toString() {
        String s = "DNA:\n" + getDNASequenceAsString() + "\n" + "Qual:\n" + getQualSequenceAsString() + "\n" + "Description:\n" + getSeqDescription();
        return s;
    }

    private final DNASequence _dna;

    private final QualSequence _qual;

    private final Software _phred;

    private final String _options;
}
