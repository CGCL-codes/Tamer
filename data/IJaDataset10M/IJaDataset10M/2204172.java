package org.apache.myfaces.trinidadinternal.share.nls;

/**
 * The DateFormatContext class contains all date format parameters.
 *
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/share/nls/DateFormatContext.java#0 $) $Date: 10-nov-2005.19:00:01 $
 */
public abstract class DateFormatContext implements Cloneable {

    /**
   * Returns the year offset for parsing years with only two digits.
   */
    public abstract int getTwoDigitYearStart();

    /**
   * Override of Object.hashCode().
   */
    @Override
    public int hashCode() {
        int twoDigitYearStart = getTwoDigitYearStart();
        return twoDigitYearStart;
    }

    /**
   * Override of Object.equals().
   */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DateFormatContext)) return false;
        DateFormatContext dfc = (DateFormatContext) obj;
        int thisTwoDigitYearStart = getTwoDigitYearStart();
        int thatTwoDigitYearStart = dfc.getTwoDigitYearStart();
        return (thisTwoDigitYearStart == thatTwoDigitYearStart);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("DateFormatContext is not cloneable!");
        }
    }

    /**
   * Override of Object.toString().
   */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(super.toString());
        buffer.append(", twoDigitYearStart=");
        buffer.append(getTwoDigitYearStart());
        return buffer.toString();
    }
}
