package com.voxdei.voxcontentSE.DAO;

import java.util.Comparator;

/**
 * Comparator class is used to sort the VdGroupContent objects.
 * @author Michael Salgado
 * @company VoxDei
 */
public class VdGroupContentComparator implements Comparator<Object> {

    /**
     * Holds the field on which the comparison is performed.
     */
    private int iType;

    /**
     * Value that will contain the information about the order of the sort: normal or reversal.
     */
    private boolean bReverse;

    /**
     * Constructor class for VdGroupContentComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdGroupContentComparator(VdGroupContentDAO.ID_ID_CONTENT, bReverse));<code>
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdGroupContentDAO.ID_ID_CONTENT</li>
     *   <li>VdGroupContentDAO.ID_ID_GROUP</li>
     * </ul>
     */
    public VdGroupContentComparator(int iType) {
        this(iType, false);
    }

    /**
     * Constructor class for VdGroupContentComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdGroupContentComparator(VdGroupContentDAO.ID_ID_CONTENT, bReverse));<code>
     *
     * @param iType the field from which you want to sort.
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdGroupContentDAO._ID_CONTENT</li>
     *   <li>VdGroupContentDAO._ID_GROUP</li>
     * </ul>
     *
     * @param bReverse set this value to true, if you want to reverse the sorting results
     */
    public VdGroupContentComparator(int iType, boolean bReverse) {
        this.iType = iType;
        this.bReverse = bReverse;
    }

    /**
     * Implementation of the compare method.
     */
    public int compare(Object pObj1, Object pObj2) {
        VdGroupContent b1 = (VdGroupContent) pObj1;
        VdGroupContent b2 = (VdGroupContent) pObj2;
        int iReturn = 0;
        switch(iType) {
            case VdGroupContentDAO._ID_CONTENT:
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
            case VdGroupContentDAO._ID_GROUP:
                if (b1.getIdGroup() == null && b2.getIdGroup() != null) {
                    iReturn = -1;
                } else if (b1.getIdGroup() == null && b2.getIdGroup() == null) {
                    iReturn = 0;
                } else if (b1.getIdGroup() != null && b2.getIdGroup() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getIdGroup().compareTo(b2.getIdGroup());
                }
                break;
            default:
                throw new IllegalArgumentException("Type passed for the field is not supported");
        }
        return bReverse ? (-1 * iReturn) : iReturn;
    }
}
