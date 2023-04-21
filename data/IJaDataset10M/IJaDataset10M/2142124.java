package net.mogray.infinitypfm.core.data;

import java.io.Serializable;
import java.sql.*;

/**
 * @author wayne
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParamDateRange implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Timestamp startDate;

    private Timestamp endDate;

    int yr = 0;

    int mth = 0;

    public int getYr() {
        return yr;
    }

    public void setYr(int yr) {
        this.yr = yr;
    }

    public int getMth() {
        return mth;
    }

    public void setMth(int mth) {
        this.mth = mth;
    }

    /**
	 * 
	 */
    public ParamDateRange() {
        super();
    }

    /**
	 * @return Returns the endDate.
	 */
    public Timestamp getEndDate() {
        return endDate;
    }

    /**
	 * @param endDate The endDate to set.
	 */
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    /**
	 * @return Returns the startDate.
	 */
    public Timestamp getStartDate() {
        return startDate;
    }

    /**
	 * @param startDate The startDate to set.
	 */
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
}
