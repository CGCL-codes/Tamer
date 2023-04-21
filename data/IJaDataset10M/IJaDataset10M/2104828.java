package it.southdown.avana.metadata;

import java.util.Arrays;
import it.southdown.avana.alignment.*;
import it.southdown.avana.util.TextUtilities;

/**
 * This class' job is to take an aligment whose sequences have an id header 
 * and change the header to contain various metadata values.
 * The sequences are also sorted if necessary.
 *
 */
public class AlignmentMetadataAnnotator {

    private static final String[] SORTING_FIELDS = { "Year", "Subtype", "Host", "Country", "Isolate" };

    /**
	 * Perform the metadata annotation (change the header to descriptive metadata text)
	 * Also sorts according to the default sort criteria.
	 * 
	 * @param alignment
	 * @param meta
	 * @param includeId indicates whether the sequence id should be included when the metadata is added
	 * @return
	 */
    public Alignment annotateAlignment(Alignment alignment, Metadata meta, boolean includeId) {
        return annotateAlignment(alignment, meta, true, includeId);
    }

    /**
	 * Perform the metadata annotation (change the header to descriptive metadata text)
	 * Sorts according to the default sort criteria if the sort flag is set.
	 * 
	 * @param alignment
	 * @param meta
	 * @param sort
	 * @param includeId indicates whether the sequence id should be included when the metadata is added
	 * @return
	 */
    public Alignment annotateAlignment(Alignment alignment, Metadata meta, boolean sort, boolean includeId) {
        Sequence[] sequences = alignment.getSequences();
        MetaRichSequence[] newSequences = new MetaRichSequence[sequences.length];
        for (int i = 0; i < sequences.length; i++) {
            Sequence s = sequences[i];
            newSequences[i] = new MetaRichSequence(s, meta, includeId);
        }
        if (sort) {
            Arrays.sort(newSequences);
        }
        return new SimpleAlignment(newSequences, alignment.getName() + " - ANNOTATED");
    }

    private static class MetaRichSequence extends Sequence implements Comparable<MetaRichSequence> {

        private Comparable[] sortingValues;

        public MetaRichSequence(Sequence original, Metadata meta, boolean retainId) {
            super(makeMetaId(original, meta, retainId), original.getData());
            sortingValues = new Comparable[SORTING_FIELDS.length];
            String id = original.getId();
            String[] values = meta.getValues(id);
            for (int i = 0; i < SORTING_FIELDS.length; i++) {
                int idx = meta.getColumnIndex(SORTING_FIELDS[i]);
                sortingValues[i] = (idx >= 0) ? values[idx] : "";
            }
        }

        private static String makeMetaId(Sequence original, Metadata meta, boolean retainId) {
            String id = original.getId();
            String[] values = meta.getValues(id);
            String newId = id;
            if (values != null) {
                newId = TextUtilities.stringArrayToString(values);
                if (retainId) {
                    newId = id + ',' + newId;
                }
            }
            return newId;
        }

        /**
    	 * Compare all fields in turn, until we find one that is not equal.
    	 */
        @SuppressWarnings("unchecked")
        public int compareTo(MetaRichSequence otherRec) {
            for (int i = 0; i < sortingValues.length; i++) {
                Comparable thisVal = sortingValues[i];
                Comparable otherVal = otherRec.sortingValues[i];
                if (!thisVal.equals(otherVal)) {
                    return (thisVal.compareTo(otherVal));
                }
            }
            return 0;
        }
    }
}
