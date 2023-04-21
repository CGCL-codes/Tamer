package org.openxml4j.document.word;

/**
 * kind of merge of word doc
 * @author    CNDR
 */
public class MergeStyle implements Comparable {

    private static int nextOrdinal = 0;

    private final int ordinal = nextOrdinal++;

    private MergeStyle() {
    }

    public int compareTo(Object o) {
        return ordinal - ((MergeStyle) o).ordinal;
    }

    /**
	 * no change in the merged doc
	 */
    public static final MergeStyle KEEP_DOC_AS_IS = new MergeStyle();

    /**
	 * the added doc will be fully read only
	 */
    public static final MergeStyle MERGE_AS_READ_ONLY = new MergeStyle();
}
