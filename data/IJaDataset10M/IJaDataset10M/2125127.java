package com.shenming.sms.dc.sql2java;

import java.util.Comparator;

/**
 * Comparator class is used to sort the SmTbAccountGroupBean objects.
 */
public class SmTbAccountGroupComparator implements Comparator {

    /**
     * Holds the field on which the comparison is performed.
     */
    private int iType;

    /**
     * Value that will contain the information about the order of the sort: normal or reversal.
     */
    private boolean bReverse;

    /**
     * Constructor class for SmTbAccountGroupComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new SmTbAccountGroupComparator(SmTbAccountGroupManager.ROLE_TYPE, bReverse));<code>
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>SmTbAccountGroupManager.ID_ROLE_TYPE
     *   <li>SmTbAccountGroupManager.ID_USER_ID
     *   <li>SmTbAccountGroupManager.ID_DESCRIPTION
     * </ul>
     */
    public SmTbAccountGroupComparator(int iType) {
        this(iType, false);
    }

    /**
     * Constructor class for SmTbAccountGroupComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new SmTbAccountGroupComparator(SmTbAccountGroupManager.ROLE_TYPE, bReverse));<code>
     *
     * @param iType the field from which you want to sort.
     * <br>
     * Possible values are:
     * <ul>
     *   <li>SmTbAccountGroupManager.ID_ROLE_TYPE
     *   <li>SmTbAccountGroupManager.ID_USER_ID
     *   <li>SmTbAccountGroupManager.ID_DESCRIPTION
     * </ul>
     *
     * @param bReverse set this value to true, if you want to reverse the sorting results
     */
    public SmTbAccountGroupComparator(int iType, boolean bReverse) {
        this.iType = iType;
        this.bReverse = bReverse;
    }

    /**
     * Implementation of the compare method.
     */
    public int compare(Object pObj1, Object pObj2) {
        SmTbAccountGroupBean b1 = (SmTbAccountGroupBean) pObj1;
        SmTbAccountGroupBean b2 = (SmTbAccountGroupBean) pObj2;
        int iReturn = 0;
        switch(iType) {
            case SmTbAccountGroupManager.ID_ROLE_TYPE:
                if (b1.getRoleType() == null && b2.getRoleType() != null) {
                    iReturn = -1;
                } else if (b1.getRoleType() == null && b2.getRoleType() == null) {
                    iReturn = 0;
                } else if (b1.getRoleType() != null && b2.getRoleType() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getRoleType().compareTo(b2.getRoleType());
                }
                break;
            case SmTbAccountGroupManager.ID_USER_ID:
                if (b1.getUserId() == null && b2.getUserId() != null) {
                    iReturn = -1;
                } else if (b1.getUserId() == null && b2.getUserId() == null) {
                    iReturn = 0;
                } else if (b1.getUserId() != null && b2.getUserId() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getUserId().compareTo(b2.getUserId());
                }
                break;
            case SmTbAccountGroupManager.ID_DESCRIPTION:
                if (b1.getDescription() == null && b2.getDescription() != null) {
                    iReturn = -1;
                } else if (b1.getDescription() == null && b2.getDescription() == null) {
                    iReturn = 0;
                } else if (b1.getDescription() != null && b2.getDescription() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getDescription().compareTo(b2.getDescription());
                }
                break;
            default:
                throw new IllegalArgumentException("Type passed for the field is not supported");
        }
        return bReverse ? (-1 * iReturn) : iReturn;
    }
}
