package org.jtr.transliterate;

import java.util.List;

/**
 * <p>Holds an ordered list of characters and character ranges. This is the
 * basic data structure for finding transliterations to perform.</p>
 *
 * <p><b>Usage:</b></p>
 *
 * <p>This class is normally created and populated directly from the
 * {@link CharacterParser} class. CharacterList objects are immutable, so
 * no additional patterns can be added once the object has been constructed.
 * This allows CharacterList objects to be used by multiple threads
 * simultaneously.</p>
 *
 * <p>Once a character list has been created, it will normally be passed
 * into a {@link CharacterReplacer} constructor for processing. However,
 * character lists may also be cached, since they hold no state information
 * during the parsing itself. This would remove compile-time costs of creating
 * the list when performance is critical.</p>
 *
 * @author  <a href="mailto:run2000@users.sourceforge.net">Nicholas Cull</a>
 * @version $Id: CharacterList.java,v 1.7 2005/02/16 12:42:38 run2000 Exp $
 */
public final class CharacterList {

    /**
     * The last character in the list, if the list is non-empty, or -1 if
     * the list is empty.
     */
    private int m_nLastChar;

    /** The logical number of characters in the list. */
    private int m_nLength;

    /** The list data structure itself, along with the number of entries. */
    private ListEntry[] m_cCharacterArray;

    private int m_nArraySize;

    /** Creates an empty CharacterList */
    public CharacterList() {
        m_nLastChar = -1;
    }

    /** Creates new CharacterList, using the given list of {@link ListEntry}
     * objects.
     *
     * @param cCharList a <code>List</code> of {@link ListEntry} objects to
     * be stored in the array
     * @throws NullPointerException if cCharList is null
     */
    public CharacterList(List cCharList) {
        makeListEntryArray(cCharList);
    }

    /**
     * Used by the <code>List</code> constructor to create the underlying
     * array for maximum performance.
     *
     * @param cCharList a <code>List</code> of {@link ListEntry} objects to
     * be stored in the array
     * @throws NullPointerException if cCharList is null
     */
    private void makeListEntryArray(List cCharList) {
        ListEntry cEntry;
        m_nArraySize = cCharList.size();
        m_cCharacterArray = new ListEntry[m_nArraySize];
        for (int iEntry = 0; iEntry < m_nArraySize; iEntry++) {
            cEntry = (ListEntry) cCharList.get(iEntry);
            m_cCharacterArray[iEntry] = cEntry;
            m_nLength += cEntry.m_nEndOffset + 1;
        }
        if (m_nArraySize > 0) {
            cEntry = m_cCharacterArray[m_nArraySize - 1];
            m_nLastChar = cEntry.m_nStartPos + cEntry.m_nEndOffset;
        } else {
            m_nLastChar = -1;
        }
    }

    /**
     * Determines whether a given character exists in the list. If it does,
     * returns the <em>first</em> occurrence of that character in the list.
     *
     * @param nChar the character to be matched
     * @param bComplement determines behaviour when the COMPLEMENT_MASK flag
     * is set&mdash;if false, the list is scanned normally, otherwise the
     * complement of the list is used
     * @return the logical position in the list where the character was found,
     * or -1 if the character could not be found
     */
    public int getMatch(char nChar, boolean bComplement) {
        ListEntry cRange;
        int iPos = 0, nPos = 0;
        while (iPos < m_nArraySize) {
            cRange = (ListEntry) m_cCharacterArray[iPos];
            if ((nChar >= cRange.m_nStartPos) && (nChar <= cRange.m_nStartPos + cRange.m_nEndOffset)) {
                if (bComplement) {
                    return -1;
                } else {
                    return (nPos + (nChar - cRange.m_nStartPos));
                }
            }
            nPos += cRange.m_nEndOffset + 1;
            iPos++;
        }
        if (bComplement) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Finds the character corresponding to the given position in the list.
     *
     * @param nPosition the position where the resulting character should be
     * found in the list
     * @param bDeleteUnreplaceables determines behaviour when the
     * DELETE_UNREPLACEABLES_MASK is set&mdash;if false, the last character
     * in the list will be returned, otherwise -1 is returned
     * @return the character corresponding to the position in the list,
     * or -1 if the character could not be found
     */
    public int findCharAtPosition(int nPosition, boolean bDeleteUnreplaceables) {
        if (nPosition >= m_nLength) {
            if (bDeleteUnreplaceables) {
                return -1;
            } else {
                return m_nLastChar;
            }
        }
        ListEntry cRange;
        int iPos = 0, nPos = 0;
        while (iPos < m_nArraySize) {
            cRange = m_cCharacterArray[iPos];
            if (nPosition <= nPos + cRange.m_nEndOffset) {
                return cRange.m_nStartPos + (nPosition - nPos);
            }
            nPos += cRange.m_nEndOffset + 1;
            iPos++;
        }
        if (bDeleteUnreplaceables) {
            return -1;
        } else {
            return m_nLastChar;
        }
    }

    /**
     * Determines the total number of characters in the list&mdash;not just
     * the number of entries in the underlying array.
     *
     * @return the total number of characters in the pattern
     */
    public int size() {
        return m_nLength;
    }
}
