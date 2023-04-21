package edu.mit.lcs.haystack.lucene.index;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.OutputStream;
import org.apache.lucene.util.StringHelper;
import java.io.IOException;
import java.util.Vector;

/**
 * Writer works by opening a document and then opening the fields within the document and then
 * writing out the vectors for each field.
 * 
 * Rough usage:
 *
 <CODE>
 for each document
 {
 writer.openDocument();
 for each field on the document
 {
 writer.openField(field);
 for all of the terms
 {
 writer.addTerm(...)
 }
 writer.closeField
 }
 writer.closeDocument()    
 }
 </CODE>
 */
final class TermVectorsWriter {

    public static final int FORMAT_VERSION = 1;

    public static final int FORMAT_SIZE = 4;

    public static final String TVX_EXTENSION = ".tvx";

    public static final String TVD_EXTENSION = ".tvd";

    public static final String TVF_EXTENSION = ".tvf";

    private OutputStream tvx = null, tvd = null, tvf = null;

    private Vector fields = null;

    private Vector terms = null;

    private FieldInfos fieldInfos;

    private TVField currentField = null;

    private long currentDocPointer = -1;

    /** Create term vectors writer for the specified segment in specified
   *  directory.  A new TermVectorsWriter should be created for each
   *  segment. The parameter <code>maxFields</code> indicates how many total
   *  fields are found in this document. Not all of these fields may require
   *  termvectors to be stored, so the number of calls to
   *  <code>openField</code> is less or equal to this number.
   */
    public TermVectorsWriter(Directory directory, String segment, FieldInfos fieldInfos) throws IOException {
        tvx = directory.createFile(segment + TVX_EXTENSION);
        tvx.writeInt(FORMAT_VERSION);
        tvd = directory.createFile(segment + TVD_EXTENSION);
        tvd.writeInt(FORMAT_VERSION);
        tvf = directory.createFile(segment + TVF_EXTENSION);
        tvf.writeInt(FORMAT_VERSION);
        this.fieldInfos = fieldInfos;
        fields = new Vector(fieldInfos.size());
        terms = new Vector();
    }

    public final void openDocument() throws IOException {
        closeDocument();
        currentDocPointer = tvd.getFilePointer();
    }

    public final void closeDocument() throws IOException {
        if (isDocumentOpen()) {
            closeField();
            writeDoc();
            fields.clear();
            currentDocPointer = -1;
        }
    }

    public final boolean isDocumentOpen() {
        return currentDocPointer != -1;
    }

    /** Start processing a field. This can be followed by a number of calls to
   *  addTerm, and a final call to closeField to indicate the end of
   *  processing of this field. If a field was previously open, it is
   *  closed automatically.
   */
    public final void openField(String field) throws IOException {
        if (!isDocumentOpen()) throw new IllegalStateException("Cannot open field when no document is open.");
        closeField();
        currentField = new TVField(fieldInfos.fieldNumber(field));
    }

    /** Finished processing current field. This should be followed by a call to
   *  openField before future calls to addTerm.
   */
    public final void closeField() throws IOException {
        if (isFieldOpen()) {
            writeField();
            fields.add(currentField);
            terms.clear();
            currentField = null;
        }
    }

    /** Return true if a field is currently open. */
    public final boolean isFieldOpen() {
        return currentField != null;
    }

    /** Add term to the field's term vector. Field must already be open
   *  of NullPointerException is thrown. Terms should be added in
   *  increasing order of terms, one call per unique termNum. ProxPointer
   *  is a pointer into the TermPosition file (prx). Freq is the number of
   *  times this term appears in this field, in this document.
   */
    public final void addTerm(String termText, int freq) {
        if (!isDocumentOpen()) throw new IllegalStateException("Cannot add terms when document is not open");
        if (!isFieldOpen()) throw new IllegalStateException("Cannot add terms when field is not open");
        addTermInternal(termText, freq);
    }

    private final void addTermInternal(String termText, int freq) {
        currentField.length += freq;
        TVTerm term = new TVTerm();
        term.termText = termText;
        term.freq = freq;
        terms.add(term);
    }

    /** Add specified vectors to the document.
   */
    public final void addVectors(TermFreqVector[] vectors) throws IOException {
        if (!isDocumentOpen()) throw new IllegalStateException("Cannot add term vectors when document is not open");
        if (isFieldOpen()) throw new IllegalStateException("Cannot add term vectors when field is open");
        for (int i = 0; i < vectors.length; i++) {
            addTermFreqVector(vectors[i]);
        }
    }

    /** Add specified vector to the document. Document must be open but no field
   *  should be open or exception is thrown. The same document can have <code>addTerm</code>
   *  and <code>addVectors</code> calls mixed, however a given field must either be
   *  populated with <code>addTerm</code> or with <code>addVector</code>.     *
   */
    public final void addTermFreqVector(TermFreqVector vector) throws IOException {
        if (!isDocumentOpen()) throw new IllegalStateException("Cannot add term vector when document is not open");
        if (isFieldOpen()) throw new IllegalStateException("Cannot add term vector when field is open");
        addTermFreqVectorInternal(vector);
    }

    private final void addTermFreqVectorInternal(TermFreqVector vector) throws IOException {
        openField(vector.getField());
        for (int i = 0; i < vector.size(); i++) {
            addTermInternal(vector.getTerms()[i], vector.getTermFrequencies()[i]);
        }
        closeField();
    }

    /** Close all streams. */
    final void close() throws IOException {
        try {
            closeDocument();
        } finally {
            IOException keep = null;
            if (tvx != null) try {
                tvx.close();
            } catch (IOException e) {
                if (keep == null) keep = e;
            }
            if (tvd != null) try {
                tvd.close();
            } catch (IOException e) {
                if (keep == null) keep = e;
            }
            if (tvf != null) try {
                tvf.close();
            } catch (IOException e) {
                if (keep == null) keep = e;
            }
            if (keep != null) throw (IOException) keep.fillInStackTrace();
        }
    }

    private void writeField() throws IOException {
        currentField.tvfPointer = tvf.getFilePointer();
        final int size;
        tvf.writeVInt(size = terms.size());
        tvf.writeVInt(currentField.length - size);
        String lastTermText = "";
        for (int i = 0; i < size; i++) {
            TVTerm term = (TVTerm) terms.elementAt(i);
            int start = StringHelper.stringDifference(lastTermText, term.termText);
            int length = term.termText.length() - start;
            tvf.writeVInt(start);
            tvf.writeVInt(length);
            tvf.writeChars(term.termText, start, length);
            tvf.writeVInt(term.freq);
            lastTermText = term.termText;
        }
    }

    private void writeDoc() throws IOException {
        if (isFieldOpen()) throw new IllegalStateException("Field is still open while writing document");
        tvx.writeLong(currentDocPointer);
        final int size;
        tvd.writeVInt(size = fields.size());
        int lastFieldNumber = 0;
        for (int i = 0; i < size; i++) {
            TVField field = (TVField) fields.elementAt(i);
            tvd.writeVInt(field.number - lastFieldNumber);
            lastFieldNumber = field.number;
        }
        long lastFieldPointer = 0;
        for (int i = 0; i < size; i++) {
            TVField field = (TVField) fields.elementAt(i);
            tvd.writeVLong(field.tvfPointer - lastFieldPointer);
            lastFieldPointer = field.tvfPointer;
        }
    }

    private static class TVField {

        int number;

        long tvfPointer = 0;

        int length = 0;

        TVField(int number) {
            this.number = number;
        }
    }

    private static class TVTerm {

        String termText;

        int freq = 0;
    }
}
