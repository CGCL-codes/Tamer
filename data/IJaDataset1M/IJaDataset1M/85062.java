package org.jcvi.fasta.fastq;

import org.jcvi.glyph.nuc.NucleotideEncodedGlyphs;

public abstract class AbstractFastQFileVisitor<T extends FastQRecord> implements FastQFileVisitor {

    private boolean initialized = false;

    protected void checkNotYetInitialized() {
        if (initialized) {
            throw new IllegalStateException("already initialized, can not visit more records");
        }
    }

    @Override
    public void visitEndOfFile() {
        checkNotYetInitialized();
        initialized = true;
    }

    @Override
    public void visitLine(String line) {
        checkNotYetInitialized();
    }

    @Override
    public void visitEncodedQualities(String encodedQualities) {
    }

    @Override
    public void visitEndBlock() {
    }

    @Override
    public void visitNucleotides(NucleotideEncodedGlyphs nucleotides) {
    }

    @Override
    public void visitFile() {
    }
}
