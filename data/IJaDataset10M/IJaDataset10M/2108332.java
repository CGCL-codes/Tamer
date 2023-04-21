package org.uguess.birt.report.engine.spreadsheet.model;

/**
 * This interface represents a merge block object.
 */
public interface MergeBlock {

    /**
	 * Returns the merge block starting row index.
	 * 
	 * @return
	 */
    int getStartRow();

    /**
	 * Returns the merge block ending row index
	 * 
	 * @return
	 */
    int getEndRow();

    /**
	 * Returns the merge block starting column index.
	 * 
	 * @return
	 */
    int getStartColumn();

    /**
	 * Returns the merge block ending column index.
	 * 
	 * @return
	 */
    int getEndColumn();

    /**
	 * Retrns the row span of the merge block. If no row merging, it should
	 * return 0.
	 * 
	 * @return
	 */
    int getRowSpan();

    /**
	 * Returns the column span of the merge block. If no column merging, it
	 * should return 0.
	 * 
	 * @return
	 */
    int getColumnSpan();

    /**
	 * Returns if current merge block crosses another merge block.
	 * 
	 * @param merge
	 * @return
	 */
    boolean cross(MergeBlock merge);

    /**
	 * Returns if current merge block includes another merge block.
	 * 
	 * @param merge
	 * @return
	 */
    boolean include(MergeBlock merge);

    /**
	 * Returns if current merge block includes a cell in given row, column.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
    boolean include(int row, int col);

    /**
	 * Returns if current merge block is empty.
	 * 
	 * @return
	 */
    boolean isEmpty();
}
