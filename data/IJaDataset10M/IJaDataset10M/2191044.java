package org.velma.data.msa;

import java.io.Serializable;
import org.velma.data.bank.KeyHolder;
import org.velma.data.msa.SortCountByPosition.CountData;

/**
 * Centralized data type to be used by VELMA and to allow custom parsers to work
 * For alignments, the key is its alignment name
 * 
 * @author Hyun Kyu Shim
 *
 */
public interface MSA extends Serializable, KeyHolder {

    public int getSeqCount();

    public int getPosCount();

    /** @return type of file, see Constants */
    public abstract byte getType();

    /**
	 * @param row
	 * @param column
	 * @return encoded value at given row and index
	 */
    public abstract byte get(int row, int column);

    /**
	 * @param rowIndex
	 * @return name of the sequence at the index
	 */
    public abstract String getName(int rowIndex);

    /**
	 * @param seqName
	 * @return index of the seqName
	 */
    public abstract int getIndexOfSequence(String seqName);

    /**
	 * @param rowIndex
	 * @return sequence at the index
	 */
    public abstract Sequence getSequence(int rowIndex);

    /**
	 * @param name of the sequence
	 * @return sequence associated with the name
	 */
    public abstract Sequence getSequenceByName(String name);

    /**
	 * @return counts matrix, first index is the position,
	 * second index is the code
	 */
    public abstract float[][] getCounts();

    /**
	 * @return counts matrix sorted at each position
	 * its count
	 */
    public abstract CountData[][] getSortedCounts();
}
