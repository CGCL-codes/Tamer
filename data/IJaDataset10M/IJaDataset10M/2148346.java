package jalds.alds.al.sorting.comparisonsort;

import jalds.alds.SortableObject;

/**
 * Every programmer knows that quicksort is the best in-memory sort in the world. Look inside the
 * sort function of almost any library and you'll find quicksort. Another thing that every
 * programmer knows is that bubblesort, the "hello world" of sorts, is one of the worst in the
 * world. But what most programmers don't know is that a simple modification to bubble sort turns it
 * into combsort, a remarkably simple sort that's nearly as fast as quicksort.
 * <p>
 * The basic idea is to eliminate turtles, or small values near the end of the list, since in a
 * bubble sort these slow the sorting down tremendously. In bubble sort, when any two elements are
 * compared, they always have a gap (distance from each other) of 1. The basic idea of comb sort is
 * that the gap can be much more than one. The gap starts out as the length of the list being sorted
 * divided by the shrink factor (generally 1.3), and the list is sorted with that value (rounded
 * down to an integer if needed) for the gap. Then the gap is divided by the shrink factor again,
 * the list is sorted with this new gap, and the process repeats until the gap is 1. At this point,
 * comb sort continues using a gap of 1 until the list is fully sorted. The final stage of the sort
 * is thus equivalent to a bubble sort, but by this time most turtles have been dealt with, so a
 * bubble sort will be efficient. This is a diminishing increment sort.
 * <p>
 * Runs in approximately <em>O(N log N)<em> time
 * <p>
 * Source http://www.yagni.com/combsort/index.php
 * <p>
 * @see BubbleSort
 * 
 * @author Devender Gollapally
 *
 */
final class CombSort extends AbstractComparisonSort {

    private static final float SHRINK_FACTOR = (float) 1.3;

    public SortableObject[] sort(SortableObject[] unSortedList) {
        int gap = unSortedList.length;
        boolean swapped = false;
        do {
            swapped = false;
            gap = (new Float(gap / SHRINK_FACTOR)).intValue();
            if (gap == 10 || gap == 9) {
                gap = 11;
            } else if (gap < 1) {
                gap = 1;
            }
            for (int i = 0; (i < unSortedList.length) && (i + gap < unSortedList.length); i++) {
                if (unSortedList[i].getValue() > unSortedList[i + gap].getValue()) {
                    swapped = true;
                    swap(i, i + gap, unSortedList);
                }
            }
        } while (swapped || gap > 1);
        return unSortedList;
    }
}
