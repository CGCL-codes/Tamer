package net.saim.algorithms;

/** This consists exclusively of static fields and methods that operate on or return learners.
 *  @author Konrad Höffner */
public class Learners {

    /** An array containing all implementations of learners defined in this package.
	 * This can be used to let a user select a learner of his choice along with {@link getNames()}.*/
    public static final Learner[] LEARNERS = {};

    /** returns the names of all lerners in the field {@link LEARNERS}.*/
    public static String[] getNames() {
        String[] names = new String[LEARNERS.length];
        for (int i = 0; i < LEARNERS.length; i++) {
            names[i] = LEARNERS[i].getName();
        }
        return names;
    }
}
