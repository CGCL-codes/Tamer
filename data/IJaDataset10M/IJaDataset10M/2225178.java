package com.ojt.ui;

/**
 * Descriptor for multi list components.
 * @author R�mi "DwarfConan" Guitreau
 * @since 8 avr. 08 : Cr�ation.
 */
public interface MultiListSlidable {

    /**
	 * @return The number of lists managed.
	 */
    int getNbLists();

    /**
	 * Get the height of a list.
	 * @param listIdx The list index.
	 * @return The height of the list
	 */
    int getListHeight(final int listIdx);

    int getListElementHeight();

    void memberMoved(final int srcList, final int destList);

    boolean listCanGrow(final int listIdx);

    boolean listCanDiminue(final int listIdx);

    boolean isListIgnored(final int listIdx);

    int getNbElements(final int listIdx);
}
