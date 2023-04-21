package uk.ac.gla.terrier.structures;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import uk.ac.gla.terrier.utility.ApplicationSetup;
import uk.ac.gla.terrier.utility.Files;

/**
 * This class implements an output stream for the lexicon structure.
 * @author Vassilis Plachouras
 * @version $Revision: 1.29 $
 */
public class LexiconOutputStream implements Closeable {

    /** The logger used */
    private static Logger logger = Logger.getRootLogger();

    /** A zero buffer for writing to the file.*/
    protected final byte[] zeroBuffer = new byte[ApplicationSetup.STRING_BYTE_LENGTH];

    /** The term represented as an array of bytes.*/
    protected final byte[] termCharacters = new byte[ApplicationSetup.STRING_BYTE_LENGTH];

    /** The term represented as a string.*/
    protected String term;

    /** An integer representing the id of the term.*/
    protected int termId;

    /** The document frequency of the term.*/
    protected int documentFrequency;

    /** The term frequency of the term.*/
    protected int termFrequency;

    /** The offset in bytes in the inverted file of the term.*/
    protected long endOffset;

    /** The offset in bits in the starting byte in the inverted file.*/
    protected byte endBitOffset;

    /** A data input stream to read from the bufferInput.*/
    protected DataOutput lexiconStream = null;

    /** Pointer written - the sum of the Nts */
    protected long numPointersWritten = 0;

    /** collection length - the sum of the TFs */
    protected long numTokensWritten = 0;

    protected int numTermsWritten = 0;

    /** A constructor for child classes that doesnt open the file */
    protected LexiconOutputStream(long a, long b, long c) {
    }

    /**
	 * A default constructor.
	 */
    public LexiconOutputStream() {
        try {
            lexiconStream = new DataOutputStream(Files.writeFileStream(ApplicationSetup.LEXICON_FILENAME));
        } catch (IOException ioe) {
            logger.fatal("I/O error occured while opening the lexicon file. Stack trace follows.", ioe);
        }
    }

    /** Create a lexicon using the specified data stream */
    public LexiconOutputStream(DataOutput out) {
        lexiconStream = out;
    }

    /**
	 * A constructor given the filename.
	 * @param filename java.lang.String the name of the lexicon file.
	 */
    public LexiconOutputStream(String filename) {
        try {
            lexiconStream = new DataOutputStream(Files.writeFileStream(filename));
        } catch (IOException ioe) {
            logger.fatal("I/O error occured while opening the lexicon file. Stack trace follows.", ioe);
        }
    }

    /**
	 * A constructor given the filename.
	 * @param file java.io.File the name of the lexicon file.
	 */
    public LexiconOutputStream(File file) {
        try {
            lexiconStream = new DataOutputStream(Files.writeFileStream(file));
        } catch (IOException ioe) {
            logger.fatal("I/O error occured while opening the lexicon file. Stack trace follows.", ioe);
        }
    }

    /** A constructor for a LexiconOutputStream given the index path and prefix
	  * @param path String the path to the index
	  * @param prefix String the prefix of the filenames in the index
	  */
    public LexiconOutputStream(String path, String prefix) {
        this(path + ApplicationSetup.FILE_SEPARATOR + prefix + ApplicationSetup.LEXICONSUFFIX);
    }

    /**
	 * Closes the lexicon stream.
	 * @throws IOException if an I/O error occurs while closing the stream.
	 */
    public void close() {
        try {
            if (lexiconStream instanceof java.io.Closeable) ((java.io.Closeable) lexiconStream).close();
        } catch (IOException ioe) {
        }
    }

    /**
	 * Writes a lexicon entry.
	 * @return the number of bytes written to the file. 
	 * @throws java.io.IOException if an I/O error occurs
	 * @param _term the string representation of the term
	 * @param _termId the terms integer identifier
	 * @param _documentFrequency the term's document frequency in the collection
	 * @param _termFrequency the term's frequency in the collection
	 * @param _endOffset the term's ending byte offset in the inverted file
	 * @param _endBitOffset the term's ending byte bit-offset in the inverted file
	 */
    public int writeNextEntry(String _term, int _termId, int _documentFrequency, int _termFrequency, long _endOffset, byte _endBitOffset) throws IOException {
        byte[] tmpBytes = _term.getBytes();
        final int length = tmpBytes.length;
        numPointersWritten += _documentFrequency;
        numTokensWritten += _termFrequency;
        numTermsWritten++;
        lexiconStream.write(tmpBytes, 0, length);
        lexiconStream.write(zeroBuffer, 0, ApplicationSetup.STRING_BYTE_LENGTH - length);
        lexiconStream.writeInt(_termId);
        lexiconStream.writeInt(_documentFrequency);
        lexiconStream.writeInt(_termFrequency);
        lexiconStream.writeLong(_endOffset);
        lexiconStream.writeByte(_endBitOffset);
        return Lexicon.lexiconEntryLength;
    }

    /**
	 * Writes a lexicon entry.
	 * @return the number of bytes written.
	 * @throws java.io.IOException if an I/O error occurs
	 * @param _term the byte[] representation of the term. Using this format means that
	 * the term does not have to be decoded and recoded every time.
	 * @param _termId the terms integer identifier
	 * @param _documentFrequency the term's document frequency in the collection
	 * @param _termFrequency the term's frequency in the collection
	 * @param _endOffset the term's ending byte offset in the inverted file
	 * @param _endBitOffset the term's ending byte bit-offset in the inverted file
	 */
    public int writeNextEntry(byte[] _term, int _termId, int _documentFrequency, int _termFrequency, long _endOffset, byte _endBitOffset) throws IOException {
        final int length = _term.length;
        numPointersWritten += _documentFrequency;
        numTokensWritten += _termFrequency;
        numTermsWritten++;
        lexiconStream.write(_term, 0, _term.length);
        lexiconStream.write(zeroBuffer, 0, ApplicationSetup.STRING_BYTE_LENGTH - length);
        lexiconStream.writeInt(_termId);
        lexiconStream.writeInt(_documentFrequency);
        lexiconStream.writeInt(_termFrequency);
        lexiconStream.writeLong(_endOffset);
        lexiconStream.writeByte(_endBitOffset);
        return Lexicon.lexiconEntryLength;
    }

    /** Returns the number of pointers there would be in an inverted index built using this lexicon (thus far).
	  * This is equal to the sum of the Nts written to this lexicon output stream. */
    public long getNumberOfPointersWritten() {
        return numPointersWritten;
    }

    /** Returns the number of tokens there are in the entire collection represented by this lexicon (thus far).
	  * This is equal to the sum of the TFs written to this lexicon output stream. */
    public long getNumberOfTokensWritten() {
        return numTokensWritten;
    }

    /** Returns the number of terms written so far by this LexiconInputStream */
    public int getNumberOfTermsWritten() {
        return numTermsWritten;
    }

    /**
	 * Sets the bit offset in the last byte of the term's entry in the inverted file.
	 * @param _endBitOffset byte the bit offset in the last byte of the 
	 *		term's entry in the inverted file.
	 * @deprecated
	 */
    public void setEndBitOffset(byte _endBitOffset) {
        endBitOffset = _endBitOffset;
    }

    /**
	 * Sets the ending offset of the term's entry in the inverted file.
	 * @param _endOffset long The ending byte of the term's 
	 *		entry in the inverted file.
	 * @deprecated
	 */
    public void setEndOffset(long _endOffset) {
        endOffset = _endOffset;
    }

    /**
	 * Sets the document frequency for the given term.
	 * @param _Nt int The document frequency for the given term.
	 * @deprecated
	 */
    public void setNt(int _Nt) {
        documentFrequency = _Nt;
    }

    /**
	 * Sets the string representation of the term.
	 * @param _term java.lang.String The string representation of 
	 *		the seeked term.
	 * @deprecated
	 */
    public void setTerm(String _term) {
        term = _term;
    }

    /**
	 * Sets the term's id.
	 * @param _termId int the term's identifier.
	 * @deprecated
	 */
    public void setTermId(int _termId) {
        termId = _termId;
    }

    /**
	 * Sets the term frequency for the already found term.
	 * @param _termFrequency int The term frequency in the collection.
 	 * @deprecated
	 */
    public void setTF(int _termFrequency) {
        termFrequency = _termFrequency;
    }
}
