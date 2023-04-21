package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import java.util.Arrays;

/**
 * Builder lass to manipulate and generate a trie.
 * This is useful for ICU data in primitive types.
 * Provides a compact way to store information that is indexed by Unicode 
 * values, such as character properties, types, keyboard values, etc. This is 
 * very useful when you have a block of Unicode data that contains significant 
 * values while the rest of the Unicode data is unused in the application or 
 * when you have a lot of redundance, such as where all 21,000 Han ideographs 
 * have the same value.  However, lookup is much faster than a hash table.
 * A trie of any primitive data type serves two purposes:
 * <UL type = round>
 *     <LI>Fast access of the indexed values.
 *     <LI>Smaller memory footprint.
 * </UL>
 * This is a direct port from the ICU4C version
 * @version            $Revision: 1.3 $
 * @author             Syn Wee Quek
 */
public class IntTrieBuilder extends TrieBuilder {

    /**
	 * Copy constructor
	 */
    public IntTrieBuilder(IntTrieBuilder table) {
        super(table);
        m_data_ = new int[m_dataCapacity_];
        System.arraycopy(table.m_data_, 0, m_data_, 0, m_dataLength_);
        m_initialValue_ = table.m_initialValue_;
    }

    /**
     * Constructs a build table
     * @param aliasdata data to be filled into table
     * @param maxdatalength maximum data length allowed in table
     * @param initialvalue inital data value
     * @param latin1linear is latin 1 to be linear
     * @return updated table
     */
    public IntTrieBuilder(int aliasdata[], int maxdatalength, int initialvalue, boolean latin1linear) {
        super();
        if (maxdatalength < DATA_BLOCK_LENGTH_ || (latin1linear && maxdatalength < 1024)) {
            throw new IllegalArgumentException("Argument maxdatalength is too small");
        }
        if (aliasdata != null) {
            m_data_ = aliasdata;
        } else {
            m_data_ = new int[maxdatalength];
        }
        int j = DATA_BLOCK_LENGTH_;
        if (latin1linear) {
            int i = 0;
            do {
                m_index_[i++] = j;
                j += DATA_BLOCK_LENGTH_;
            } while (i < (256 >> SHIFT_));
        }
        m_dataLength_ = j;
        Arrays.fill(m_data_, 0, m_dataLength_, initialvalue);
        m_initialValue_ = initialvalue;
        m_dataCapacity_ = maxdatalength;
        m_isLatin1Linear_ = latin1linear;
        m_isCompacted_ = false;
    }

    /**
     * Gets a 32 bit data from the table data
     * @param ch codepoint which data is to be retrieved
     * @return the 32 bit data
     */
    public int getValue(int ch) {
        if (m_isCompacted_ || ch > UCharacter.MAX_VALUE || ch < 0) {
            return 0;
        }
        int block = m_index_[ch >> SHIFT_];
        return m_data_[Math.abs(block) + (ch & MASK_)];
    }

    /**
     * Sets a 32 bit data in the table data
     * @param ch codepoint which data is to be set
     * @param value to set
     * @return true if the set is successful, otherwise 
     *              if the table has been compacted return false
     */
    public boolean setValue(int ch, int value) {
        if (m_isCompacted_ || ch > UCharacter.MAX_VALUE || ch < 0) {
            return false;
        }
        int block = getDataBlock(ch);
        if (block < 0) {
            return false;
        }
        m_data_[block + (ch & MASK_)] = value;
        return true;
    }

    /**
     * Serializes the build table with 32 bit data
     * @param datamanipulate builder raw fold method implementation
     * @param triedatamanipulate result trie fold method
     * @return a new trie
     */
    public IntTrie serialize(TrieBuilder.DataManipulate datamanipulate, Trie.DataManipulate triedatamanipulate) {
        if (datamanipulate == null) {
            throw new IllegalArgumentException("Parameters can not be null");
        }
        if (!m_isCompacted_) {
            compact(false);
            fold(datamanipulate);
            compact(true);
            m_isCompacted_ = true;
        }
        if (m_dataLength_ >= MAX_DATA_LENGTH_) {
            throw new ArrayIndexOutOfBoundsException("Data length too small");
        }
        char index[] = new char[m_indexLength_];
        int data[] = new int[m_dataLength_];
        for (int i = 0; i < m_indexLength_; i++) {
            index[i] = (char) (m_index_[i] >>> INDEX_SHIFT_);
        }
        System.arraycopy(m_data_, 0, data, 0, m_dataLength_);
        int options = SHIFT_ | (INDEX_SHIFT_ << OPTIONS_INDEX_SHIFT_);
        options |= OPTIONS_DATA_IS_32_BIT_;
        if (m_isLatin1Linear_) {
            options |= OPTIONS_LATIN1_IS_LINEAR_;
        }
        return new IntTrie(index, data, m_initialValue_, options, triedatamanipulate);
    }

    protected int m_data_[];

    protected int m_initialValue_;

    /**
     * No error checking for illegal arguments.
     * @param ch codepoint to look for
     * @return -1 if no new data block available (out of memory in data array)
     */
    private int getDataBlock(int ch) {
        ch >>= SHIFT_;
        int indexValue = m_index_[ch];
        if (indexValue > 0) {
            return indexValue;
        }
        int newBlock = m_dataLength_;
        int newTop = newBlock + DATA_BLOCK_LENGTH_;
        if (newTop > m_dataCapacity_) {
            return -1;
        }
        m_dataLength_ = newTop;
        m_index_[ch] = newBlock;
        Arrays.fill(m_data_, newBlock, newBlock + DATA_BLOCK_LENGTH_, m_initialValue_);
        return newBlock;
    }

    /**
     * Compact a folded build-time trie.
     * The compaction
     * - removes blocks that are identical with earlier ones
     * - overlaps adjacent blocks as much as possible (if overlap == true)
     * - moves blocks in steps of the data granularity
     *
     * It does not
     * - try to move and overlap blocks that are not already adjacent
     * - try to move and overlap blocks that overlap with multiple values in 
     * the overlap region
     * @param overlap flag
     */
    private void compact(boolean overlap) {
        if (m_isCompacted_) {
            return;
        }
        findUnusedBlocks();
        int overlapStart = DATA_BLOCK_LENGTH_;
        if (m_isLatin1Linear_ && SHIFT_ <= 8) {
            overlapStart += 256;
        }
        int newStart = DATA_BLOCK_LENGTH_;
        int prevEnd = newStart - 1;
        for (int start = newStart; start < m_dataLength_; ) {
            if (m_map_[start >> SHIFT_] < 0) {
                start += DATA_BLOCK_LENGTH_;
                continue;
            }
            if (start >= overlapStart) {
                int i = findSameDataBlock(m_data_, newStart, start, overlap ? DATA_GRANULARITY_ : DATA_BLOCK_LENGTH_);
                if (i >= 0) {
                    m_map_[start >> SHIFT_] = i;
                    start += DATA_BLOCK_LENGTH_;
                    continue;
                }
            }
            int x = m_data_[start];
            int i = 0;
            if (x == m_data_[prevEnd] && overlap && start >= overlapStart) {
                for (i = 1; i < DATA_BLOCK_LENGTH_ && x == m_data_[start + i] && x == m_data_[prevEnd - i]; ++i) {
                }
                i &= ~(DATA_GRANULARITY_ - 1);
            }
            if (i > 0) {
                m_map_[start >> SHIFT_] = newStart - i;
                start += i;
                for (i = DATA_BLOCK_LENGTH_ - i; i > 0; --i) {
                    m_data_[newStart++] = m_data_[start++];
                }
            } else if (newStart < start) {
                m_map_[start >> SHIFT_] = newStart;
                for (i = DATA_BLOCK_LENGTH_; i > 0; --i) {
                    m_data_[newStart++] = m_data_[start++];
                }
            } else {
                m_map_[start >> SHIFT_] = start;
                newStart += DATA_BLOCK_LENGTH_;
                start = newStart;
            }
            prevEnd = newStart - 1;
        }
        for (int i = 0; i < m_indexLength_; ++i) {
            m_index_[i] = m_map_[m_index_[i] >>> SHIFT_];
        }
        m_dataLength_ = newStart;
    }

    /**
     * Find the same data block
     * @param data array
     * @param dataLength
     * @param otherBlock
     * @param step
     */
    private static final int findSameDataBlock(int data[], int dataLength, int otherBlock, int step) {
        dataLength -= DATA_BLOCK_LENGTH_;
        for (int block = 0; block <= dataLength; block += step) {
            int i = 0;
            for (i = 0; i < DATA_BLOCK_LENGTH_; ++i) {
                if (data[block + i] != data[otherBlock + i]) {
                    break;
                }
            }
            if (i == DATA_BLOCK_LENGTH_) {
                return block;
            }
        }
        return -1;
    }

    /**
     * Fold the normalization data for supplementary code points into
     * a compact area on top of the BMP-part of the trie index,
     * with the lead surrogates indexing this compact area.
     *
     * Duplicate the index values for lead surrogates:
     * From inside the BMP area, where some may be overridden with folded values,
     * to just after the BMP area, where they can be retrieved for
     * code point lookups.
     * @param manipulate fold implementation
     */
    private final void fold(DataManipulate manipulate) {
        int leadIndexes[] = new int[SURROGATE_BLOCK_COUNT_];
        int index[] = m_index_;
        System.arraycopy(index, 0xd800 >> SHIFT_, leadIndexes, 0, SURROGATE_BLOCK_COUNT_);
        for (char c = 0xd800; c <= 0xdbff; ++c) {
            int block = index[c >> SHIFT_];
            if (block > 0) {
                index[c >> SHIFT_] = -block;
            }
        }
        int indexLength = BMP_INDEX_LENGTH_;
        for (int c = 0x10000; c < 0x110000; ) {
            if (index[c >> SHIFT_] != 0) {
                c &= ~0x3ff;
                int block = findSameIndexBlock(index, indexLength, c >> SHIFT_);
                int value = manipulate.getFoldedValue(c, block + SURROGATE_BLOCK_COUNT_);
                if (value != 0) {
                    if (!setValue(0xd7c0 + (c >> 10), value)) {
                        throw new ArrayIndexOutOfBoundsException("Data table overflow");
                    }
                    if (block == indexLength) {
                        System.arraycopy(index, c >> SHIFT_, index, indexLength, SURROGATE_BLOCK_COUNT_);
                        indexLength += SURROGATE_BLOCK_COUNT_;
                    }
                }
                c += 0x400;
            } else {
                c += DATA_BLOCK_LENGTH_;
            }
        }
        if (indexLength >= MAX_INDEX_LENGTH_) {
            throw new ArrayIndexOutOfBoundsException("Index table overflow");
        }
        System.arraycopy(index, BMP_INDEX_LENGTH_, index, BMP_INDEX_LENGTH_ + SURROGATE_BLOCK_COUNT_, indexLength - BMP_INDEX_LENGTH_);
        System.arraycopy(leadIndexes, 0, index, BMP_INDEX_LENGTH_, SURROGATE_BLOCK_COUNT_);
        indexLength += SURROGATE_BLOCK_COUNT_;
        m_indexLength_ = indexLength;
    }
}
