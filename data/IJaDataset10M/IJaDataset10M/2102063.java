package org.coode.cloud.view;

import org.coode.cloud.model.AbstractClassCloudModel;
import org.coode.cloud.model.OWLCloudModel;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: candidasa
 * Date: Sep 4, 2006
 * Time: 4:04:16 PM
 * Calculates the Interval Rank of all classes in an ontology
 * Algorithm: from Ralph Freese (2004) automated Lattice Drawing in Concept Lattices, ICFCA 04, LNAI v. 2961, pp. 112-127
 * <p/>
 * rank(a) = height(a) - depth (a) + M
 */
public class IntervalRank extends AbstractClassCloudView {

    private static final long serialVersionUID = 2413080726115916638L;

    protected OWLCloudModel createModel() {
        return new IntervalRankModel(getOWLModelManager());
    }

    class IntervalRankModel extends AbstractClassCloudModel {

        private int m;

        private int maxRank = 0;

        protected OWLObjectHierarchyProvider provider;

        protected HashMap<OWLClass, Integer> heights;

        protected HashMap<OWLClass, Integer> depths;

        protected HashMap<OWLClass, Integer> intervalRanks;

        LinkedList<OWLClass>[] reverseIntervalRanks;

        HashSet<OWLClass> alreadyReturned = new HashSet<OWLClass>();

        protected IntervalRankModel(OWLModelManager mngr) {
            super(mngr);
        }

        public Set<OWLClass> getEntities() {
            return intervalRanks.keySet();
        }

        public void activeOntologiesChanged(Set<OWLOntology> ontologies) throws OWLException {
            provider = getOWLModelManager().getOWLHierarchyManager().getOWLClassHierarchyProvider();
            heights = new HashMap<OWLClass, Integer>();
            depths = new HashMap<OWLClass, Integer>();
            intervalRanks = new HashMap<OWLClass, Integer>();
            computeHeights(getOWLModelManager().getActiveOntology());
            computeIntervalRank();
        }

        protected int getValueForEntity(OWLClass entity) throws OWLException {
            return intervalRanks.get(entity);
        }

        /**
         * recursively descend into the class hierarchy, counting height as we go down (and recording it as we descend)
         * also, as we return, return the maximal number of subclasses (depth) as we go up. Not the maximal height = M.
         */
        private void computeHeights(OWLOntology ontology) throws OWLException {
            OWLClass thing = getOWLModelManager().getOWLDataFactory().getOWLThing();
            recursiveHeights(thing, -1);
        }

        private int recursiveHeights(OWLClass cls, int height) {
            int currentHeight = height + 1;
            if (currentHeight > m) m = currentHeight;
            int currentDepth = 0;
            Set<OWLClass> children = provider.getChildren(cls);
            if (children.size() > 0) {
                for (OWLClass childClass : children) {
                    int depth = recursiveHeights(childClass, currentHeight);
                    if (depth > currentDepth) currentDepth = depth;
                }
                currentDepth++;
            }
            heights.put(cls, currentHeight);
            depths.put(cls, currentDepth);
            return currentDepth;
        }

        /**
         * Does the simple calculation to generate the IntervalRanks for each class in the ontology
         */
        private void computeIntervalRank() {
            Iterator<OWLClass> recordedClassesIterator = heights.keySet().iterator();
            while (recordedClassesIterator.hasNext()) {
                OWLClass checkClass = recordedClassesIterator.next();
                Integer rank = heights.get(checkClass) - depths.get(checkClass) + m;
                if (rank > maxRank) maxRank = rank;
                intervalRanks.put(checkClass, rank);
            }
            reverseIntervalRanks = new LinkedList[maxRank + 1];
            for (int i = 0; i < reverseIntervalRanks.length; i++) {
                reverseIntervalRanks[i] = new LinkedList<OWLClass>();
            }
            recordedClassesIterator = heights.keySet().iterator();
            while (recordedClassesIterator.hasNext()) {
                OWLClass owlClass = recordedClassesIterator.next();
                reverseIntervalRanks[intervalRanks.get(owlClass)].add(owlClass);
            }
        }

        /**
         * returns the maximum intervalRank of the current ontology
         */
        public int getMaximumRank() {
            return maxRank;
        }

        /**
         * returns a listing of all the different rank levels and how many classes there are in each.
         * This can later be used to draw an Excel graph of the distribution profile of each evaluated ontology.
         */
        public int[] getOntologyRankDistributionProfile() {
            int[] rankDistribution = new int[reverseIntervalRanks.length];
            for (int i = 0; i < reverseIntervalRanks.length; i++) {
                LinkedList<OWLClass> reverseIntervalRank = reverseIntervalRanks[i];
                rankDistribution[i] = reverseIntervalRank.size();
            }
            return rankDistribution;
        }

        /**
         * Returns the first class with the given rank or lower, once a class has been returned once it is not
         * returned again until the resetNextClassCount method is called.
         * Returns null if such a class does not exist.
         */
        public OWLClass getNextClassByMaximumRank(int maximumRank) {
            OWLClass returnedClass = null;
            maximumRank++;
            while (returnedClass == null && maximumRank > 0) {
                maximumRank--;
                Iterator<OWLClass> currentRankIterator = reverseIntervalRanks[maximumRank].iterator();
                while (currentRankIterator.hasNext()) {
                    returnedClass = currentRankIterator.next();
                    if (!alreadyReturned.contains(returnedClass)) break;
                    returnedClass = null;
                }
            }
            if (returnedClass != null) alreadyReturned.add(returnedClass);
            return returnedClass;
        }

        /**
         * resets the count of classes already returned by getNextClassByMaximumRank for a different simulated user
         */
        public void resetNextClassCount() {
            alreadyReturned.clear();
        }
    }
}
