package uk.ac.gla.terrier.structures;

import java.io.DataInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;
import uk.ac.gla.terrier.utility.ApplicationSetup;
import uk.ac.gla.terrier.utility.Files;

/**
 * A document index class which reads the .docid file and keeps its contents
 * in a array of bytes in memory. This class reduces the memory overhead 
 * introduced when we use the class DocumentIndexInMemory, by decoding the
 * information on the fly.
 * @author Vassilis Plachouras, Craig Macdonald
 * @version $Revision: 1.33 $
 */
public class DocumentIndexEncoded extends DocumentIndex {

    /** The logger used */
    private static Logger logger = Logger.getRootLogger();

    /** The entry length minus 4 bytes for the docid.*/
    private int shortEntryLength = this.entryLength - 4;

    /** Set the length of docnos in the index file */
    public void setDocnoEntryLength(int l) {
        logger.warn("docno.byte.length changed, " + this.getClass().getName() + " reloading");
        this.DOCNO_BYTE_LENGTH = l;
        this.entryLength = (8 + DOCNO_BYTE_LENGTH + 9);
        this.shortEntryLength = this.entryLength - 4;
        this.buffer = new byte[DOCNO_BYTE_LENGTH];
        this.zeroBuffer = new byte[DOCNO_BYTE_LENGTH];
        try {
            DataInputStream dis = new DataInputStream(Files.openFileStream(filename));
            numberOfDocumentIndexEntries = (int) Files.length(filename) / this.entryLength;
            loadIntoMemory(dis, numberOfDocumentIndexEntries);
            dis.close();
        } catch (IOException ioe) {
            logger.fatal("Input/Output exception during opening the document index file. Stack trace follows: ", ioe);
        }
    }

    /** 
	 * The index of the last retrieved entry. When there is a request
	 * for the information about a document, given its document id, 
	 * the document id is compared with the index. If they are equal,
	 * then we have the information about the document readily available,
	 * otherwise we need to decode it and update the index.
	 */
    private int index = -1;

    /** The byte array that holds the contents of the .docid file.*/
    protected byte[] bytes;

    /**
	 * The default constructor for DocumentIndexInMemory. Opens the document index file
	 * and reads its contents into memory.
	 */
    public DocumentIndexEncoded(String path, String prefix) {
        this(path + ApplicationSetup.FILE_SEPARATOR + prefix + ApplicationSetup.DOC_INDEX_SUFFIX);
    }

    public DocumentIndexEncoded() {
        this(ApplicationSetup.DOCUMENT_INDEX_FILENAME);
    }

    /**
	 * A constructor for DocumentIndexInMemory that specifies the file to open.
	 * Opens the document index file and reads its contents into memory.
	 * For the document pointers file we replace the extension of the 
	 * document index file with the right default extension. 
	 * @param filename String The filename of the document index file.
	 */
    public DocumentIndexEncoded(String filename) {
        super(filename);
        try {
            DataInputStream dis = new DataInputStream(Files.openFileStream(filename));
            numberOfDocumentIndexEntries = (int) Files.length(filename) / this.entryLength;
            loadIntoMemory(dis, numberOfDocumentIndexEntries);
            dis.close();
        } catch (IOException ioe) {
            logger.fatal("Input/Output exception during opening the document index file. Stack trace follows: ", ioe);
        }
    }

    /**
	 * Prints to the standard error the document index structure, 
	 * which is loaded into memory.
	 */
    public void print() {
        for (int i = 0; i < numberOfDocumentIndexEntries; i++) {
            System.out.println("" + (i * entryLength) + ", " + i + ", " + getDocumentLength(i) + ", " + getDocumentNumber(i) + ", " + getEndOffset() + ", " + getEndBitOffset());
        }
    }

    /**
	 * Returns the id of a document with a given document number.
	 * @return int The document's id, or a negative number if a document with the given number doesn't exist.
	 * @param docno java.lang.String The document's number
	 */
    public int getDocumentId(String docno) {
        if (!seek(docno)) return -1;
        return index;
    }

    /**
	 * Returns the length of a document with a given id.
	 * @return int The document's length
	 * @param docid the document's id
	 */
    public int getDocumentLength(int docid) {
        if (!seek(docid)) return -1;
        int i = index * shortEntryLength;
        docLength = (int) (bytes[i++] & 0xff);
        docLength = docLength << 8 | (int) (bytes[i++] & 0xff);
        docLength = docLength << 8 | (int) (bytes[i++] & 0xff);
        docLength = docLength << 8 | (int) (bytes[i++] & 0xff);
        return docLength;
    }

    /**
	 * Returns the document length of the document with a given document number .
	 * @return int The document's length
	 * @param docno java.lang.String The document's number
	 */
    public int getDocumentLength(String docno) {
        if (!seek(docno)) return -1;
        int i = index * shortEntryLength;
        docLength = (int) (bytes[i++] & 0xff);
        for (int j = 0; j < 3; j++) docLength = docLength << 8 | (int) (bytes[i++] & 0xff);
        return docLength;
    }

    /**
	 * Returns the number of a document with a given id.
	 * @return java.lang.String The documents number
	 * @param docid int The documents id
	 */
    public String getDocumentNumber(int docid) {
        if (!seek(docid)) return null;
        System.arraycopy(bytes, (index * shortEntryLength) + 4, buffer, 0, DOCNO_BYTE_LENGTH);
        docno = (new String(buffer)).trim();
        return docno;
    }

    /**
	 * Returns the ending bit offset.
	 * @return byte
	 */
    protected byte getEndBitOffset() {
        return bytes[index * shortEntryLength + 12 + DOCNO_BYTE_LENGTH];
    }

    /**
	 * Returns the ending byte offset of the entry in the direct index.
	 * @return long the ending byte of the entry in the direct index.
	 */
    protected long getEndOffset() {
        int i = index * shortEntryLength + 4 + DOCNO_BYTE_LENGTH;
        endOffset = (int) (bytes[i++] & 0xff);
        for (int j = 0; j < 7; j++) endOffset = endOffset << 8 | (int) (bytes[i++] & 0xff);
        return endOffset;
    }

    /**
	 * Returns the ending offset of the current document's
	 * entry in the direct index.
	 * @return FilePosition an offset in the direct index.
	 */
    public FilePosition getDirectIndexEndOffset() {
        return new FilePosition(getEndOffset(), getEndBitOffset());
    }

    /**
	 * Returns the number of documents in the document index.
	 * @return int the number of documents in the document index.
	 */
    public int getNumberOfDocuments() {
        return numberOfDocumentIndexEntries;
    }

    /**
	 * Returns the bit offset in the starting yte of the 
	 * document's entry in the direct file.
	 * @return byte the bit offset in the starting byte of
	 *			  the documents entry in the direct file.
	 */
    protected byte getStartBitOffset() {
        if (index == 0) return 0;
        startBitOffset = (byte) (bytes[index * shortEntryLength - 1] + 1);
        if (startBitOffset > 7) startBitOffset = 0;
        return startBitOffset;
    }

    /**
	 * Returns the starting byte of the corresponding 
	 * entry in the direct file.
	 * @return long the starting byte of the direct index entry.
	 */
    protected long getStartOffset() {
        if (index == 0) return 0;
        int i = index * shortEntryLength - 9;
        startOffset = (int) (bytes[i++] & 0xff);
        for (int j = 0; j < 7; j++) startOffset = startOffset << 8 | (int) (bytes[i++] & 0xff);
        if (bytes[index * shortEntryLength - 1] == 7) startOffset++;
        return startOffset;
    }

    /**
	 * Returns the starting offset of the current document's
	 * entry in the direct index.
	 * @return FilePosition an offset in the direct index.
	 */
    public FilePosition getDirectIndexStartOffset() {
        return new FilePosition(getStartOffset(), getStartBitOffset());
    }

    /**
	 * Loads the data from the file into memory.
	 * @param dis java.io.DataInputStream The input stream from 
	 *			which the data are read
	 * @param numOfEntries int The number of entries to read
	 * @exception java.io.IOException An input/output exception is 
	 *			thrown if there any error while reading from disk.
	 */
    public void loadIntoMemory(DataInputStream dis, int numOfEntries) throws java.io.IOException {
        final int shortEntryLength = this.entryLength - 4;
        final int numOfBytes = numOfEntries * shortEntryLength;
        bytes = new byte[numOfBytes];
        for (int i = 0; i < numOfBytes; i += shortEntryLength) {
            dis.skipBytes(4);
            dis.readFully(bytes, i, shortEntryLength);
        }
    }

    /** 
	 * Overrides the seek(int docid) method of
	 * the DocumentIndex class.
	 * @param i the docid of the document we are looking for.
	 */
    public boolean seek(int i) {
        if (i == index) return true;
        if (i >= numberOfDocumentIndexEntries) return false;
        index = i;
        return true;
    }

    /**
	 * Overrides the seek(String s) method of 
	 * the super class.
	 * @param docno String the document number of the document we are seeking.
	 * @return Returns false if the given docno could not be found in the DocumentIndex
	 */
    public boolean seek(final String docno) {
        long low = 0;
        long high = numberOfDocumentIndexEntries - 1;
        long i = 0;
        int compareResult = 0;
        final int DOCNOLENGTH = DOCNO_BYTE_LENGTH;
        while (high >= low) {
            i = (long) (high + low) / 2;
            System.arraycopy(bytes, (int) i * shortEntryLength + 4, buffer, 0, DOCNOLENGTH);
            compareResult = sComp.compare(docno, new String(buffer).trim());
            if (compareResult == 0) break;
            if (compareResult < 1) high = i - 1; else low = i + 1;
        }
        if (compareResult == 0) return seek((int) i); else return false;
    }

    /**
	 * A main method for testing the DocumentIndexEncoded class.
	 * <br>
	 * The first command line argument corresponds to the filename 
	 * of the document index file. This is followed by one of the options
	 * specified below:
	 * <ul>
	 * <li>--print : prints the contents of the document index to the 
	 *	   standard error stream.</li>
	 * <li>--docid <document number> : returns the document id of the document
	 *	   with the given document number.</li>
	 * <li>--docno <document id> : returns the document number of the document
	 *	   with the given document id.</li>
	 * </ul>
	 * For example, we can write:<br>
	 * <tt>java -cp ... uk.ac.gla.terrier.structures.DocumentIndexEncoded filename --docno 1023</tt><br>
	 * This will return the document number of the document with id 1023.
	 * @param args java.lang.String[] the command line parameters
	 */
    public static void main(java.lang.String[] args) {
        DocumentIndex docIndexEncoded = new DocumentIndexEncoded(args[0]);
        if (args[1].equals("--print")) docIndexEncoded.print(); else if (args[1].equals("--docid")) System.out.println(docIndexEncoded.getDocumentId(args[2])); else if (args[1].equals("--docno")) System.out.println(docIndexEncoded.getDocumentNumber(Integer.parseInt(args[2])));
    }
}
