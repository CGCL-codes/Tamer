package org.jaffa.components.finder;

/**
 * This is the base class for all the output DTOs used by the Finder components.
 */
public class FinderOutDto {

    /** Indicates if there are more records than what are being returned by a query. */
    private Boolean moreRecordsExist = Boolean.FALSE;

    /** Indicates if there are more records than what are being returned by a query. */
    private Integer totalRecords;

    /** Getter for property moreRecordsExist.
     * @return Value of property moreRecordsExist.
     */
    public Boolean getMoreRecordsExist() {
        return moreRecordsExist;
    }

    /** Setter for property moreRecordsExist.
     * @param moreRecordsExist New value of property moreRecordsExist.
     */
    public void setMoreRecordsExist(Boolean moreRecordsExist) {
        this.moreRecordsExist = moreRecordsExist;
    }

    /** Getter for property totalRecords.
     * @return Value of property totalRecords.
     */
    public Integer getTotalRecords() {
        return totalRecords;
    }

    /** Setter for property totalRecords.
     * @param totalRecords New value of property totalRecords.
     */
    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    /** Returns diagnostic information.
     * @return diagnostic information.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (moreRecordsExist != null) buf.append("<moreRecordsExist>").append(moreRecordsExist).append("</moreRecordsExist>");
        if (totalRecords != null) buf.append("<totalRecords>").append(totalRecords).append("</totalRecords>");
        return buf.toString();
    }
}
