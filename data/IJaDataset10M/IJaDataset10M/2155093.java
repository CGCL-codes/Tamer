package com.skt.cls.domain.entity.base;

/**
 * The Class BaseEntity.
 */
public class BaseEntity {

    /** The start page idx. */
    private int startPageIdx;

    /** The end page idx. */
    private int endPageIdx;

    /**
	 * Gets the start page idx.
	 * 
	 * @return the start page idx
	 */
    public final int getStartPageIdx() {
        return startPageIdx;
    }

    /**
	 * Sets the start page idx.
	 * 
	 * @param startPageIdx the new start page idx
	 */
    public final void setStartPageIdx(int startPageIdx) {
        this.startPageIdx = startPageIdx;
    }

    /**
	 * Gets the end page idx.
	 * 
	 * @return the end page idx
	 */
    public final int getEndPageIdx() {
        return endPageIdx;
    }

    /**
	 * Sets the end page idx.
	 * 
	 * @param endPageIdx the new end page idx
	 */
    public void setEndPageIdx(int endPageIdx) {
        this.endPageIdx = endPageIdx;
    }
}
