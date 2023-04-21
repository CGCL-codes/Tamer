package net.innig.collect;

import java.util.*;

/**
    Finds the difference between two ordered lists.  Given a pair of lists,
    ListDiff provides a sequence of steps which will mutate the first list
    into the second. These steps implement {@link ListDiff.Step}, and include
    insert, move, and remove.  You can ask for the steps using {@link #getSteps()},
    or call {@link #applySteps(ListMutator) applySteps} to have the ListDiff walk
    a {@link ListMutator} through the steps.
    <p>
    ListDiff is useful for situations where some structure (such as a user interface)
    is shadowing a changing list, and you don't want to rebuild the structure from
    scratch every time the list changes -- perhaps beacuse the cost of recreating
    the structure is high, or because you don't want to lose state information about
    existing elements whose position has changed in the list.
    <p>
    The algorithm ListDiff uses for generating the steps is quite simple.  It does
    not guarantee the shortest possible sequence of steps, but usually comes quite
    close.

    <p align="center">
    <table cellpadding=4 cellspacing=2 border=0 bgcolor="#338833" width="90%"><tr><td bgcolor="#EEEEEE">
        <b>Maturity:</b>
        This is a mature API, and a stable implementation.
        It performs well in informal testing, but has not undergone
        methodical or real-world testing.
    </td></tr><tr><td bgcolor="#EEEEEE">
        <b>Plans:</b>
        There are no current plans to expand or revise this class's functionality.
        It would be worth researching alternative algorithms for generating the steps.
    </td></tr></table>
    
    @author Paul Cantrell
    @version [Development version]
*/
public class ListDiff extends CollectionDiff {

    /** For testing */
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println();
            System.out.println();
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            List a = new LinkedList(), b = new LinkedList();
            System.out.print("Old: ");
            StringTokenizer st = new StringTokenizer(in.readLine());
            while (st.hasMoreTokens()) a.add(st.nextToken());
            System.out.print("New: ");
            st = new StringTokenizer(in.readLine());
            while (st.hasMoreTokens()) b.add(st.nextToken());
            for (Iterator i = (new ListDiff(a, b)).getSteps(); i.hasNext(); ) System.out.println(i.next());
        }
    }

    /** Prepares a comparison of two lists. */
    public ListDiff(List oldStuff, List newStuff) {
        super(oldStuff, newStuff, true);
        this.oldStuff = oldStuff;
        this.newStuff = newStuff;
        calculateSteps();
    }

    /** Returns a sequence of steps which transform the old list into the new.
     *  The Iterator returns {@link ListDiff.Step} objects. */
    public Iterator getSteps() {
        return steps.iterator();
    }

    /** Walks a ListMutator through the sequence of steps which transform the old
     *  list into the new. */
    public void applySteps(ListMutator mutator) {
        for (Iterator i = getSteps(); i.hasNext(); ) {
            Step step = (Step) i.next();
            if (step instanceof Insert) mutator.insert(step.getObject(), step.getIndex()); else if (step instanceof Remove) mutator.remove(step.getObject(), step.getIndex()); else if (step instanceof Move) mutator.move(step.getObject(), step.getIndex(), ((Move) step).getToIndex());
        }
    }

    /** Returns true if the other object is a list diff comparing equal lists. */
    public boolean equals(Object other) {
        ListDiff otherdiff = (ListDiff) other;
        return otherdiff.oldStuff.equals(oldStuff) && otherdiff.newStuff.equals(newStuff);
    }

    /** A step in a list transformation. */
    public abstract class Step {

        Step(Object object, int index) {
            this.index = index;
            this.object = object;
        }

        public Object getObject() {
            return object;
        }

        public int getIndex() {
            return index;
        }

        private Object object;

        private int index;
    }

    /** A list transformation step: insert an object at a given position.
     *  After the step, the length of the list has increased by one, and the
     *  item which was at <code>index</code> is now at <code>index+1</code>. */
    public final class Insert extends Step {

        public Insert(Object object, int index) {
            super(object, index);
        }

        public String toString() {
            return "Insert " + getObject() + " at position " + getIndex();
        }
    }

    /** A list transformation step: remove an object at a given position.
     *  After the step, the length of the list has decreased by one. */
    public final class Remove extends Step {

        public Remove(Object object, int index) {
            super(object, index);
        }

        public String toString() {
            return "Remove " + getObject() + " at position " + getIndex();
        }
    }

    /** A list transformation step: move an object from one position to another.
     *  Before the move, <code>object</code> is at <code>index</code>;
     *  after the move, <code>object</code> is at <code>toIndex</code>.
     *  Items in the list between <code>index</code> and <code>toIndex</code> shift
     *  over one position to accomodate the move. */
    public final class Move extends Step {

        public Move(Object object, int index, int toIndex) {
            super(object, index);
            this.toIndex = toIndex;
        }

        public int getToIndex() {
            return toIndex;
        }

        public String toString() {
            return "Insert " + getObject() + " from position " + getIndex() + " to " + getToIndex();
        }

        private int toIndex;
    }

    private void calculateSteps() {
        steps = new LinkedList();
        List oldWork = new ArrayList(oldStuff), newWork = new ArrayList(newStuff);
        int curPos;
        for (curPos = 0; curPos < oldWork.size() && curPos < newWork.size(); curPos++) {
            Object nextObject = newWork.get(curPos);
            int oldPos = -1;
            for (int j = curPos; j < oldWork.size(); j++) if (oldWork.get(j).equals(nextObject)) {
                oldPos = j;
                break;
            }
            if (oldPos == -1) {
                steps.add(new Insert(nextObject, curPos));
                oldWork.add(curPos, nextObject);
            } else if (curPos != oldPos) {
                steps.add(new Move(nextObject, oldPos, curPos));
                oldWork.remove(oldPos);
                oldWork.add(curPos, nextObject);
            }
        }
        for (; curPos < newWork.size(); curPos++) steps.add(new Insert(newWork.get(curPos), curPos));
        for (int j = curPos; j < oldWork.size(); j++) steps.add(new Remove(oldWork.get(curPos), curPos));
    }

    private List oldStuff, newStuff, steps;
}
