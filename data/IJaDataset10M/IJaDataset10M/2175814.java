package com.ivata.mask.web.tag.webgui.list;

import org.apache.log4j.Logger;
import java.util.Comparator;

/**
 * <p>
 * Compare two column values to decide the sort order of a sorted column. The
 * sorting can be either numeric or alpha-numeric, the default
 * </p>
 *
 * @since ivata masks 0.4 (2002-11-03)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class ListColumnComparator implements Comparator {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ListColumnComparator.class);

    /**
     * <p>
     * By default, if the list is sorted, then it is sorted using the column
     * specified in ascending order. By setting this value <code>false</code>,
     * you can override this default behavior.
     * </p>
     */
    private boolean sortAscending = true;

    /**
     * <p>
     * Stores whether the column will be sorted numerically, or
     * alpha-numerically (the default). Set to <code>true</code> to sort
     * numerically. In this case all string values which cannot be evaluated as
     * an integer will be mapped to <code>0</code>.
     * </p>
     */
    private boolean sortNumerically = false;

    /**
     * <p>
     * Construct a new list column comparator.
     * </p>
     *
     * @param sortAscendingParam Refer to {@link
     * #setSortAscendingsetSortAscending}.
     */
    public ListColumnComparator(final boolean sortAscendingParam) {
        this.sortAscending = sortAscendingParam;
    }

    /**
     * <p>
     * Compares the elements, which must all be <code>String</code> instances
     * either numerically or alpha-numerically (default). This is dependent on
     * the value set in {@link #sortNumericallysortNumerically}.
     * </p>
     *
     * @param arg0  first argument to be compared.
     * @param arg1 second argument to be compared.
     * @return positive if <code>arg1</code> is greater than <code>arg0</code>,
     * <code>0</code> if they are equal, otherwise negative.
     */
    public final int compare(final Object arg0, final Object arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("compare(Object arg0 = " + arg0 + ", Object arg1 = " + arg1 + ") - start");
        }
        String string0 = (String) arg0;
        String string1 = (String) arg1;
        int result;
        if (sortNumerically) {
            Long long0, long1;
            try {
                long0 = new Long(string0);
            } catch (Exception e) {
                logger.error("compare(Object, Object)", e);
                long0 = new Long(0);
            }
            try {
                long1 = new Long(string1);
            } catch (Exception e) {
                logger.error("compare(Object, Object)", e);
                long1 = new Long(0);
            }
            result = long0.compareTo(long1);
        } else {
            result = string0.compareTo(string1);
        }
        if (result == 0) {
            result = -1;
        }
        if (sortAscending) {
            if (logger.isDebugEnabled()) {
                logger.debug("compare(Object, Object) - end - return value = " + result);
            }
            return result;
        } else {
            int returnint = -result;
            if (logger.isDebugEnabled()) {
                logger.debug("compare(Object, Object) - end - return value = " + returnint);
            }
            return returnint;
        }
    }

    /**
     * <p>
     * By default, if the list is sorted, then it is sorted using the column
     * specified in ascending order. By setting this value <code>false</code>,
     * you can override this default behavior.
     * </p>
     *
     * @return the current value of sortAscending.
     */
    public final boolean getSortAscending() {
        if (logger.isDebugEnabled()) {
            logger.debug("getSortAscending() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getSortAscending() - end - return value = " + sortAscending);
        }
        return sortAscending;
    }

    /**
     * <p>
     * Get whether the column will be sorted numerically, or alpha-numerically
     * (the default).
     * </p>
     *
     * @return <code>true</code> if the sort will be performed numerically. In
     *         this case all string values which cannot be evaluated as an
     *         integer will be mapped to <code>0</code>. Otherwise,
     *         <code>false</code> indicates an alpha-numeric sort is
     *         performed.
     */
    public final boolean getSortNumerically() {
        if (logger.isDebugEnabled()) {
            logger.debug("getSortNumerically() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getSortNumerically() - end - return value = " + sortNumerically);
        }
        return sortNumerically;
    }

    /**
     * <p>
     * By default, if the list is sorted, then it is sorted using the column
     * specified in ascending order. By setting this value <code>false</code>,
     * you can override this default behavior.
     * </p>
     *
     * @param sortAscendingParam
     *            the new default value of sortAscending.
     */
    public final void setSortAscending(final boolean sortAscendingParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setSortAscending(boolean sortAscendingParam = " + sortAscendingParam + ") - start");
        }
        this.sortAscending = sortAscendingParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setSortAscending(boolean) - end");
        }
    }

    /**
     * <p>
     * Set whether the column will be sorted numerically, or alpha-numerically
     * (the default).
     * </p>
     *
     * @param sortNumericallyParam Set to <code>true</code> to sort numerically.
     * In this case all string values which cannot be evaluated as an integer
     * will be mapped to <code>0</code>. Otherwise, set to <code>false</code>
     * to sort alpha-numerically.
     */
    public final void setSortNumerically(final boolean sortNumericallyParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setSortNumerically(boolean sortNumericallyParam = " + sortNumericallyParam + ") - start");
        }
        this.sortNumerically = sortNumericallyParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setSortNumerically(boolean) - end");
        }
    }
}
