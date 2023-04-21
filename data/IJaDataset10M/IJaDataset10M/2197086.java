package edu.colorado.emml.construction;

/**
 * Created by: Sam
 * Sep 27, 2008 at 3:02:43 PM
 */
public class IncrementalIterator implements ModelLibraryIterator {

    private int startIndex;

    private int endIndex;

    private int indexIncrement;

    private Integer currentIndex;

    public IncrementalIterator(int startIndex, int endIndex, int indexIncrement) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.indexIncrement = indexIncrement;
        this.currentIndex = startIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getIndexIncrement() {
        return indexIncrement;
    }

    public Integer nextIndex() {
        if (currentIndex == null) {
            return null;
        } else {
            Integer indexToReturn = currentIndex;
            updateIndex();
            return indexToReturn;
        }
    }

    private void updateIndex() {
        currentIndex += indexIncrement;
        if (indexIncrement > 0 && currentIndex > endIndex) {
            currentIndex = null;
        } else if (indexIncrement < 0 && currentIndex < endIndex) {
            currentIndex = null;
        }
    }

    public static void main(String[] args) {
        IncrementalIterator ii = new IncrementalIterator(12, 22, 1);
        for (Integer a = ii.nextIndex(); a != null; a = ii.nextIndex()) {
            System.out.println("a = " + a);
        }
    }
}
