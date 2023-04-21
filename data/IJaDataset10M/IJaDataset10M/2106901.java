package de.humanfork.treemerge.message;

import de.humanfork.treemerge.merge.mergelist.MergeEntry;

/**
 * The Class HangonSequencingConflict.
 */
public class HangonSequencingConflict extends ConflictImpl {

    /**
     * ID's for possible answers.
     */
    public enum AnswerID {

        /** t1 tree hangons first. */
        firstT1, /** t2 tree hangons first. */
        firstT2
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
        answers.add(new AnswerImpl(AnswerID.firstT1, "T1 first", "First the nodes from t1, afterwards from t2", ""));
        answers.add(new AnswerImpl(AnswerID.firstT2, "T2 first", "First the nodes from t2, afterwards from t1", ""));
    }

    /**
     * Get Answer which prefert t1.
     * @return t1 answer
     */
    public static Answer getAnswerT1() {
        return answers.get(0);
    }

    /**
     * Get Answer which prefert t2.
     * @return t2 answer
     */
    public static Answer getAnswerT2() {
        return answers.get(1);
    }

    /**
     * The Constructor.
     *
     * @param entry1 the entry1
     * @param entry2 the entry2
     */
    public HangonSequencingConflict(final MergeEntry entry1, final MergeEntry entry2) {
        super("Insertion sequencing conflict", "Sequencing conflict between:\n" + entry1.hangonsNiceToString() + "\n after\n" + entry1.nodeNiceToString() + "\n and\n" + entry2.hangonsNiceToString() + "\n after\n" + entry1.nodeNiceToString(), new PositionImpl(entry1.getHangonNodes(), entry2.getHangonNodes()), new HangonSequencingConflictID(entry1.getFirstHangonNode(), entry2.getFirstHangonNode()), HangonSequencingConflict.answers, HangonSequencingConflict.getAnswerT1(), "Select the order");
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
