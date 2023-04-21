package uk.ac.gla.terrier.structures.indexing;

import gnu.trove.*;
import java.util.Arrays;
import uk.ac.gla.terrier.sorting.HeapSortInt;
import uk.ac.gla.terrier.utility.TermCodes;

/** Represents the postings of one document, and saves block (term position) information. Uses HashMaps internally.
  * <p>
  * <b>Properties:</b><br>
  * <ul><li><tt>indexing.avg.unique.terms.per.doc</tt> - number of unique terms per doc on average, used to tune the initial
  * size of the haashmaps used in this class.</li></ul>
  * @see uk.ac.gla.terrier.structures.indexing.DocumentPostingList
  */
public class BlockDocumentPostingList extends DocumentPostingList {

    /** mapping term to blockids in this document */
    protected final THashMap<String, TIntHashSet> term_blocks = new THashMap<String, TIntHashSet>(AVG_DOCUMENT_UNIQUE_TERMS);

    /** number of blocks in this document. usually equal to document length, but perhaps less */
    protected int blockCount = 0;

    /** Instantiate a new block document posting list. Saves block information, but no fields */
    public BlockDocumentPostingList() {
        super();
    }

    /** Instantiate a new block document posting list. Saves block information, with the specified number of fields */
    public BlockDocumentPostingList(int fieldCount) {
        super(fieldCount);
    }

    /** Insert a term into this document, occurs at given block id */
    public void insert(String t, int blockId) {
        insert(t);
        TIntHashSet blockids = null;
        if ((blockids = term_blocks.get(t)) == null) {
            term_blocks.put(t, blockids = new TIntHashSet());
        }
        blockids.add(blockId);
        blockCount++;
    }

    /** Insert a term into this document, occurs at given block id, and in the given field */
    public void insert(String t, int fieldId, int blockId) {
        super.insert(t, fieldId);
        TIntHashSet blockids = null;
        if ((blockids = term_blocks.get(t)) == null) {
            term_blocks.put(t, blockids = new TIntHashSet());
        }
        blockids.add(blockId);
        blockCount++;
    }

    /** Insert a term into this document, occurs at given block id, and in the given fields */
    public void insert(String t, int[] fieldIds, int blockId) {
        super.insert(t, fieldIds);
        TIntHashSet blockids = null;
        if ((blockids = term_blocks.get(t)) == null) {
            term_blocks.put(t, blockids = new TIntHashSet());
        }
        blockids.add(blockId);
        blockCount++;
    }

    /** Insert a term into this document tf times, occurs at given block id, and in the given fields */
    public void insert(int tf, String t, int[] fieldIds, int blockId) {
        super.insert(tf, t, fieldIds);
        TIntHashSet blockids = null;
        if ((blockids = term_blocks.get(t)) == null) {
            term_blocks.put(t, blockids = new TIntHashSet());
        }
        blockids.add(blockId);
        blockCount++;
    }

    public int[] getBlocks(String term) {
        int[] rtr = term_blocks.get(term).toArray();
        if (rtr == null) return new int[0];
        Arrays.sort(rtr);
        return rtr;
    }

    /** returns the postings suitable to be written into the block direct index */
    public int[][] getPostings() {
        final int termCount = occurrences.size();
        final int[] termids = new int[termCount];
        final int[] tfs = new int[termCount];
        final int[] fields = new int[termCount];
        final int[] blockfreqs = new int[termCount];
        final TIntObjectHashMap<int[]> term2blockids = new TIntObjectHashMap<int[]>();
        int blockTotal = 0;
        if (fieldCount > 0) {
            class PostingVisitor implements TObjectIntProcedure<String> {

                int blockTotal = 0;

                int i = 0;

                public boolean execute(final String a, final int b) {
                    termids[i] = TermCodes.getCode(a);
                    tfs[i] = b;
                    fields[i] = term_fields.get(a);
                    final TIntHashSet ids = term_blocks.get(a);
                    blockfreqs[i] = ids.size();
                    this.blockTotal += ids.size();
                    final int[] bids = ids.toArray();
                    Arrays.sort(bids);
                    term2blockids.put(termids[i], bids);
                    i++;
                    return true;
                }
            }
            ;
            PostingVisitor proc = new PostingVisitor();
            occurrences.forEachEntry(proc);
            blockTotal = proc.blockTotal;
            HeapSortInt.ascendingHeapSort(termids, tfs, fields, blockfreqs);
        } else {
            class PostingVisitor implements TObjectIntProcedure<String> {

                int i = 0;

                int blockTotal = 0;

                public boolean execute(final String a, final int b) {
                    termids[i] = TermCodes.getCode(a);
                    tfs[i] = b;
                    final TIntHashSet ids = term_blocks.get(a);
                    blockfreqs[i] = ids.size();
                    blockTotal += ids.size();
                    final int[] bids = ids.toArray();
                    Arrays.sort(bids);
                    term2blockids.put(termids[i], bids);
                    i++;
                    return true;
                }
            }
            ;
            PostingVisitor proc = new PostingVisitor();
            occurrences.forEachEntry(proc);
            blockTotal = proc.blockTotal;
            HeapSortInt.ascendingHeapSort(termids, tfs, blockfreqs);
        }
        final int[] blockids = new int[blockTotal];
        int offset = 0;
        for (int termid : termids) {
            final int[] src = term2blockids.get(termid);
            final int src_l = src.length;
            System.arraycopy(src, 0, blockids, offset, src_l);
            offset += src_l;
        }
        return new int[][] { termids, tfs, fields, blockfreqs, blockids };
    }
}
