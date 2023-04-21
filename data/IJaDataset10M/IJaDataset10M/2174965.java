package uk.ac.lkl.migen.system.ai.collaboration;

import java.util.*;

/**
 * A pair maker gets a set of students and constructions, 
 * calculates similarities among them, and then returns either
 * the groups of students, or a matrix with the similarities, 
 * or both. 
 * 
 * @author sergut
 *
 */
public interface PairMaker {

    /**
     * Returns the matrix of distances among students.
     * 
     * @return the matrix of distances among students.
     */
    public StudentDistanceMatrix getStudentDistanceMatrix();

    /**
     * Returns the groups of students.
     * 
     * @param strictPairs if true, return only pairs (this will leave one student out if their number is odd); if false, return a triad with the pairs
     *  
     * @return the groups of students. 
     */
    public List<Set<String>> getGroups(boolean strictPairs);
}
