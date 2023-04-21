package de.humanfork.treemerge.message;

import de.humanfork.treemerge.merge.mergelist.MergeEntry;
import de.humanfork.treemerge.merge.mergelist.MergeList;

/**
 * The Class SequencingConflict.
 */
public class SequencingConflict extends ConflictImpl {

    /**
     * ID's for possible answers.
     */
    public enum AnswerID {

        /** consider only the oder in t1. */
        STRICT_T1, /** consider only the oder in t2. */
        STRICT_T2
    }

    ;

    /** The entry1. */
    private MergeEntry entry1;

    /** The entry2. */
    private MergeEntry entry2;

    /** Possible answers, created at load time. */
    private static Answers answers;

    static {
        answers = new Answers();
        answers.add(new AnswerImpl(AnswerID.STRICT_T1, "strict order from t1", "consider only the order from t1", ""));
        answers.add(new AnswerImpl(AnswerID.STRICT_T2, "strict order from t2", "consider only the order from t2", ""));
    }

    /**
     * Gets the answer_ t1.
     *
     * @return the answer_ t1
     */
    public static Answer getAnswer_T1() {
        return answers.get(0);
    }

    /**
     * Gets the answer_ t2.
     *
     * @return the answer_ t2
     */
    public static Answer getAnswer_T2() {
        return answers.get(1);
    }

    /**
     * The Constructor.
     *
     * @param mergeList2 the merge list2
     * @param entry1 the entry1
     * @param mergeList1 the merge list1
     * @param entry2 the entry2
     */
    public SequencingConflict(MergeEntry entry1, MergeEntry entry2, MergeList mergeList1, MergeList mergeList2) {
        super("Sequencing conflict", "Sequencing conflict between " + entry1.getNode().niceToString() + " follwed by " + mergeList1.getSucceeding(entry1).nodeNiceToString() + " and " + entry2.getNode().niceToString() + " follwed by " + mergeList2.getSucceeding(entry2).nodeNiceToString(), new PositionImpl(mergeList1.getAllNodes(), mergeList2.getAllNodes()), new SequencingConflictID(entry1, entry2), SequencingConflict.answers, SequencingConflict.answers.get(0), "Select the order");
        assert (mergeList1.isRightLocked(entry1));
        assert (mergeList2.isRightLocked(entry2));
        assert (mergeList1.isLeftLocked(mergeList1.getSucceeding(entry1)));
        assert (mergeList2.isLeftLocked(mergeList2.getSucceeding(entry2)));
        this.entry1 = entry1;
        this.entry2 = entry2;
    }

    /**
     * Gets the entry1.
     *
     * @return Returns the entry1.
     */
    public MergeEntry getEntry1() {
        return entry1;
    }

    /**
     * Gets the entry2.
     *
     * @return Returns the entry2.
     */
    public MergeEntry getEntry2() {
        return entry2;
    }
}
