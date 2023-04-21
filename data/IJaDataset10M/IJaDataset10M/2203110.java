package com.voxdei.voxcontentSE.DAO;

import java.util.Comparator;

/**
 * Comparator class is used to sort the VdFrontpage objects.
 * @author Michael Salgado
 * @company VoxDei
 */
public class VdFrontpageComparator implements Comparator<Object> {

    /**
     * Holds the field on which the comparison is performed.
     */
    private int iType;

    /**
     * Value that will contain the information about the order of the sort: normal or reversal.
     */
    private boolean bReverse;

    /**
     * Constructor class for VdFrontpageComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdFrontpageComparator(VdFrontpageDAO.ID_ORDERNUM, bReverse));<code>
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdFrontpageDAO.ID_ORDERNUM</li>
     *   <li>VdFrontpageDAO.ID_ID_CONTENT</li>
     * </ul>
     */
    public VdFrontpageComparator(int iType) {
        this(iType, false);
    }

    /**
     * Constructor class for VdFrontpageComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdFrontpageComparator(VdFrontpageDAO.ID_ORDERNUM, bReverse));<code>
     *
     * @param iType the field from which you want to sort.
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdFrontpageDAO._ORDERNUM</li>
     *   <li>VdFrontpageDAO._ID_CONTENT</li>
     * </ul>
     *
     * @param bReverse set this value to true, if you want to reverse the sorting results
     */
    public VdFrontpageComparator(int iType, boolean bReverse) {
        this.iType = iType;
        this.bReverse = bReverse;
    }

    /**
     * Implementation of the compare method.
     */
    public int compare(Object pObj1, Object pObj2) {
        VdFrontpage b1 = (VdFrontpage) pObj1;
        VdFrontpage b2 = (VdFrontpage) pObj2;
        int iReturn = 0;
        switch(iType) {
            case VdFrontpageDAO._ORDERNUM:
                if (b1.getOrdernum() == null && b2.getOrdernum() != null) {
                    iReturn = -1;
                } else if (b1.getOrdernum() == null && b2.getOrdernum() == null) {
                    iReturn = 0;
                } else if (b1.getOrdernum() != null && b2.getOrdernum() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getOrdernum().compareTo(b2.getOrdernum());
                }
                break;
            case VdFrontpageDAO._ID_CONTENT:
                if (b1.getIdContent() == null && b2.getIdContent() != null) {
                    iReturn = -1;
                } else if (b1.getIdContent() == null && b2.getIdContent() == null) {
                    iReturn = 0;
                } else if (b1.getIdContent() != null && b2.getIdContent() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getIdContent().compareTo(b2.getIdContent());
                }
                break;
            default:
                throw new IllegalArgumentException("Type passed for the field is not supported");
        }
        return bReverse ? (-1 * iReturn) : iReturn;
    }
}
