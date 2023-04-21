package com.consciouscode.alex.reader;

/**
 * The internal representation of Alex code.
 */
public class AList implements AItem {

    private AItem[] myContents;

    /**
     * Creates a new zero-length AList.
     */
    public AList() {
    }

    /**
     * @param contents may be <code>null</code>.
     */
    public AList(AItem[] contents) {
        myContents = contents;
    }

    /**
     * @return the length of this list.
     */
    public int length() {
        return (myContents == null ? 0 : myContents.length);
    }

    /**
     * @param i must be less than {@link #length()}.
     * @return the item at the given index.
     */
    public AItem get(int index) {
        return myContents[index];
    }
}
