package uk.ac.gla.terrier.structures;

import gnu.trove.TIntArrayList;
import java.io.IOException;
import uk.ac.gla.terrier.compression.BitIn;
import uk.ac.gla.terrier.utility.FieldScore;

/**
 * This class implements the block field inverted 
 * index for performing retrieval.
 * @author Douglas Johnson
 * @version $Revision: 1.32 $
 */
public class BlockInvertedIndex extends InvertedIndex implements IndexConfigurable {

    protected int DocumentBlockCountDelta = 1;

    protected BlockInvertedIndex() {
    }

    /**
	 * Creates an instance of the BlockInvertedIndex class 
	 * using the given lexicon.
	 * @param lexicon The lexicon used for retrieval
	 */
    public BlockInvertedIndex(Lexicon lexicon) {
        super(lexicon);
    }

    public BlockInvertedIndex(Lexicon lexicon, String path, String prefix) {
        super(lexicon, path, prefix);
    }

    /**
	 * Creates an instance of the BlockInvertedIndex class 
	 * using the given lexicon.
	 * @param lexicon The lexicon used for retrieval
	 * @param filename the name of the inverted file
	 */
    public BlockInvertedIndex(Lexicon lexicon, String filename) {
        super(lexicon, filename);
    }

    /** let it know which index to use */
    public void setIndex(Index i) {
        DocumentBlockCountDelta = i.getIntIndexProperty("blocks.invertedindex.countdelta", 1);
    }

    /**
	 * Prints out the block inverted index file.
	 */
    public void print() {
        for (int i = 0; i < lexicon.getNumberOfLexiconEntries(); i++) {
            lexicon.findTerm(i);
            System.out.print("Term (" + lexicon.getTerm() + "," + i + ") : ");
            int[][] documents = getDocuments(i);
            int blockindex = 0;
            for (int j = 0; j < documents[0].length; j++) {
                System.out.print("(" + documents[0][j] + ", " + documents[1][j] + ", ");
                if (FieldScore.USE_FIELD_INFORMATION) {
                    System.out.print(documents[2][j] + ", ");
                }
                System.out.print(documents[3][j]);
                for (int k = 0; k < documents[3][j]; k++) {
                    System.out.print(", B" + documents[4][blockindex]);
                    blockindex++;
                }
                System.out.print(")");
            }
            System.out.println();
        }
    }

    /**
	 * Returns a 2D array containing the document ids, 
	 * the term frequencies, the field scores the block frequencies and 
	 * the block ids for the given documents. 
	 * @return int[][] the five dimensional [5][] array containing 
	 *				 the document ids, frequencies, field scores and block 
	 *				 frequencies, while the last vector contains the 
	 *				 block identifiers and it has a different length from 
	 *				 the document identifiers.
	 * @param startOffset start byte of the postings in the inverted file
	 * @param startBitOffset start bit of the postings in the inverted file
	 * @param endOffset end byte of the postings in the inverted file
	 * @param endBitOffset end bit of the postings in the inverted file
	 * @param df the number of postings to expect 
	 */
    public int[][] getDocuments(final long startOffset, final byte startBitOffset, final long endOffset, final byte endBitOffset, final int df) {
        final int fieldCount = FieldScore.FIELDS_COUNT;
        final boolean loadTagInformation = FieldScore.USE_FIELD_INFORMATION;
        final int[][] documentTerms = new int[5][];
        documentTerms[0] = new int[df];
        documentTerms[1] = new int[df];
        documentTerms[2] = new int[df];
        documentTerms[3] = new int[df];
        final TIntArrayList blockids = new TIntArrayList(df);
        try {
            final BitIn file = this.file.readReset(startOffset, startBitOffset, endOffset, endBitOffset);
            if (loadTagInformation) {
                documentTerms[0][0] = file.readGamma() - 1;
                documentTerms[1][0] = file.readUnary();
                documentTerms[2][0] = file.readBinary(fieldCount);
                int blockfreq = documentTerms[3][0] = file.readUnary() - DocumentBlockCountDelta;
                int tmpBlocks[] = new int[blockfreq];
                int previousBlockId = -1;
                for (int j = 0; j < blockfreq; j++) {
                    tmpBlocks[j] = previousBlockId = file.readGamma() + previousBlockId;
                }
                blockids.add(tmpBlocks);
                for (int i = 1; i < df; i++) {
                    documentTerms[0][i] = file.readGamma() + documentTerms[0][i - 1];
                    documentTerms[1][i] = file.readUnary();
                    documentTerms[2][i] = file.readBinary(fieldCount);
                    blockfreq = documentTerms[3][i] = file.readUnary() - DocumentBlockCountDelta;
                    tmpBlocks = new int[blockfreq];
                    previousBlockId = -1;
                    for (int j = 0; j < blockfreq; j++) {
                        tmpBlocks[j] = previousBlockId = file.readGamma() + previousBlockId;
                    }
                    blockids.add(tmpBlocks);
                }
            } else {
                documentTerms[0][0] = file.readGamma() - 1;
                documentTerms[1][0] = file.readUnary();
                int blockfreq = documentTerms[3][0] = file.readUnary() - DocumentBlockCountDelta;
                int tmpBlocks[] = new int[blockfreq];
                int previousBlockId = -1;
                for (int j = 0; j < blockfreq; j++) {
                    tmpBlocks[j] = previousBlockId = file.readGamma() + previousBlockId;
                }
                blockids.add(tmpBlocks);
                for (int i = 1; i < df; i++) {
                    documentTerms[0][i] = file.readGamma() + documentTerms[0][i - 1];
                    documentTerms[1][i] = file.readUnary();
                    blockfreq = documentTerms[3][i] = file.readUnary() - DocumentBlockCountDelta;
                    tmpBlocks = new int[blockfreq];
                    previousBlockId = -1;
                    for (int j = 0; j < blockfreq; j++) {
                        tmpBlocks[j] = previousBlockId = file.readGamma() + previousBlockId;
                    }
                    blockids.add(tmpBlocks);
                }
            }
            documentTerms[4] = blockids.toNativeArray();
            return documentTerms;
        } catch (IOException ioe) {
            logger.error("Problem reading block inverted index", ioe);
            return null;
        }
    }

    public int[][] getDocuments(int termid) {
        LexiconEntry lEntry = lexicon.getLexiconEntry(termid);
        if (lEntry == null) return null;
        return getDocuments(lEntry.startOffset, lEntry.startBitOffset, lEntry.endOffset, lEntry.endBitOffset, lEntry.n_t);
    }

    public int[][] getDocumentsWithoutBlocks(int termid) {
        LexiconEntry lEntry = lexicon.getLexiconEntry(termid);
        if (lEntry == null) return null;
        return getDocumentsWithoutBlocks(lEntry.startOffset, lEntry.startBitOffset, lEntry.endOffset, lEntry.endBitOffset, lEntry.n_t);
    }

    public int[][] getDocumentsWithoutBlocks(LexiconEntry lEntry) {
        return getDocumentsWithoutBlocks(lEntry.startOffset, lEntry.startBitOffset, lEntry.endOffset, lEntry.endBitOffset, lEntry.n_t);
    }

    public int[][] getDocumentsWithoutBlocks(long startOffset, byte startBitOffset, long endOffset, byte endBitOffset, int df) {
        int[][] documentTerms = null;
        try {
            final BitIn file = this.file.readReset(startOffset, startBitOffset, endOffset, endBitOffset);
            final int fieldCount = FieldScore.FIELDS_COUNT;
            final boolean loadTagInformation = FieldScore.USE_FIELD_INFORMATION;
            if (loadTagInformation) {
                documentTerms = new int[3][df];
                documentTerms[0][0] = file.readGamma() - 1;
                documentTerms[1][0] = file.readUnary();
                documentTerms[2][0] = file.readBinary(fieldCount);
                int blockfreq = file.readUnary() - DocumentBlockCountDelta;
                for (int j = 0; j < blockfreq; j++) {
                    file.readGamma();
                }
                for (int i = 1; i < df; i++) {
                    documentTerms[0][i] = file.readGamma() + documentTerms[0][i - 1];
                    documentTerms[1][i] = file.readUnary();
                    documentTerms[2][i] = file.readBinary(fieldCount);
                    blockfreq = file.readUnary() - DocumentBlockCountDelta;
                    for (int j = 0; j < blockfreq; j++) {
                        file.readGamma();
                    }
                }
            } else {
                documentTerms = new int[2][df];
                documentTerms[0][0] = file.readGamma() - 1;
                documentTerms[1][0] = file.readUnary();
                int blockfreq = file.readUnary() - DocumentBlockCountDelta;
                for (int j = 0; j < blockfreq; j++) {
                    file.readGamma();
                }
                for (int i = 1; i < df; i++) {
                    documentTerms[0][i] = file.readGamma() + documentTerms[0][i - 1];
                    documentTerms[1][i] = file.readUnary();
                    blockfreq = file.readUnary() - DocumentBlockCountDelta;
                    for (int j = 0; j < blockfreq; j++) {
                        file.readGamma();
                    }
                }
            }
            return documentTerms;
        } catch (IOException ioe) {
            logger.error("Problem reading inverted index", ioe);
            return null;
        }
    }
}
