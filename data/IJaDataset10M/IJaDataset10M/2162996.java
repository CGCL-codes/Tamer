package edu.mit.lcs.haystack.lucene.index;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.OutputStream;
import org.apache.lucene.util.StringHelper;
import java.io.IOException;

/** This stores a monotonically increasing set of <Term, TermInfo> pairs in a
  Directory.  A TermInfos can be written once, in order.  */
final class TermInfosWriter {

    /** The file format version, a negative number. */
    public static final int FORMAT = -2;

    private FieldInfos fieldInfos;

    private OutputStream output;

    private Term lastTerm = new Term("", "");

    private TermInfo lastTi = new TermInfo();

    private long size = 0;

    /** Expert: The fraction of terms in the "dictionary" which should be stored
   * in RAM.  Smaller values use more memory, but make searching slightly
   * faster, while larger values use less memory and make searching slightly
   * slower.  Searching is typically not dominated by dictionary lookup, so
   * tweaking this is rarely useful.*/
    int indexInterval = 128;

    /** Expert: The fraction of {@link TermDocs} entries stored in skip tables,
   * used to accellerate {@link TermDocs#skipTo(int)}.  Larger values result in
   * smaller indexes, greater acceleration, but fewer accelerable cases, while
   * smaller values result in bigger indexes, less acceleration and more
   * accelerable cases. More detailed experiments would be useful here. */
    int skipInterval = 16;

    private long lastIndexPointer = 0;

    private boolean isIndex = false;

    private TermInfosWriter other = null;

    TermInfosWriter(Directory directory, String segment, FieldInfos fis) throws IOException {
        initialize(directory, segment, fis, false);
        other = new TermInfosWriter(directory, segment, fis, true);
        other.other = this;
    }

    private TermInfosWriter(Directory directory, String segment, FieldInfos fis, boolean isIndex) throws IOException {
        initialize(directory, segment, fis, isIndex);
    }

    private void initialize(Directory directory, String segment, FieldInfos fis, boolean isi) throws IOException {
        fieldInfos = fis;
        isIndex = isi;
        output = directory.createFile(segment + (isIndex ? ".tii" : ".tis"));
        output.writeInt(FORMAT);
        output.writeLong(0);
        output.writeInt(indexInterval);
        output.writeInt(skipInterval);
    }

    /** Adds a new <Term, TermInfo> pair to the set.
    Term must be lexicographically greater than all previous Terms added.
    TermInfo pointers must be positive and greater than all previous.*/
    final void add(Term term, TermInfo ti) throws IOException {
        if (!isIndex && term.compareTo(lastTerm) <= 0) throw new IOException("term out of order");
        if (ti.freqPointer < lastTi.freqPointer) throw new IOException("freqPointer out of order");
        if (ti.proxPointer < lastTi.proxPointer) throw new IOException("proxPointer out of order");
        if (!isIndex && size % indexInterval == 0) other.add(lastTerm, lastTi);
        writeTerm(term);
        output.writeVInt(ti.docFreq);
        output.writeVLong(ti.freqPointer - lastTi.freqPointer);
        output.writeVLong(ti.proxPointer - lastTi.proxPointer);
        if (ti.docFreq >= skipInterval) {
            output.writeVInt(ti.skipOffset);
        }
        if (isIndex) {
            output.writeVLong(other.output.getFilePointer() - lastIndexPointer);
            lastIndexPointer = other.output.getFilePointer();
        }
        lastTi.set(ti);
        size++;
    }

    private final void writeTerm(Term term) throws IOException {
        int start = StringHelper.stringDifference(lastTerm.text(), term.text());
        int length = term.text().length() - start;
        output.writeVInt(start);
        output.writeVInt(length);
        output.writeChars(term.text(), start, length);
        output.writeVInt(fieldInfos.fieldNumber(term.field()));
        lastTerm = term;
    }

    /** Called to complete TermInfos creation. */
    final void close() throws IOException {
        output.seek(4);
        output.writeLong(size);
        output.close();
        if (!isIndex) other.close();
    }
}
