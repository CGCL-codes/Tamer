package com.voxdei.voxcontentSE.DAO.vdPermRole;

import java.util.Comparator;

/**
 * Comparator class is used to sort the VdPermRole objects.
 * @author Michael Salgado
 * @company VoxDei
 */
public class VdPermRoleComparator implements Comparator<Object> {

    /**
     * Holds the field on which the comparison is performed.
     */
    private int iType;

    /**
     * Value that will contain the information about the order of the sort: normal or reversal.
     */
    private boolean bReverse;

    /**
     * Constructor class for VdPermRoleComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdPermRoleComparator(VdPermRoleDAO.ID_TYPE, bReverse));<code>
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdPermRoleDAO.ID_TYPE</li>
     *   <li>VdPermRoleDAO.ID_ID_TYPE</li>
     *   <li>VdPermRoleDAO.ID_ID_ROLE</li>
     * </ul>
     */
    public VdPermRoleComparator(int iType) {
        this(iType, false);
    }

    /**
     * Constructor class for VdPermRoleComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdPermRoleComparator(VdPermRoleDAO.ID_TYPE, bReverse));<code>
     *
     * @param iType the field from which you want to sort.
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdPermRoleDAO._TYPE</li>
     *   <li>VdPermRoleDAO._ID_TYPE</li>
     *   <li>VdPermRoleDAO._ID_ROLE</li>
     * </ul>
     *
     * @param bReverse set this value to true, if you want to reverse the sorting results
     */
    public VdPermRoleComparator(int iType, boolean bReverse) {
        this.iType = iType;
        this.bReverse = bReverse;
    }

    /**
     * Implementation of the compare method.
     */
    public int compare(Object pObj1, Object pObj2) {
        VdPermRole b1 = (VdPermRole) pObj1;
        VdPermRole b2 = (VdPermRole) pObj2;
        int iReturn = 0;
        switch(iType) {
            case VdPermRoleDAO._TYPE:
                if (b1.getType() == null && b2.getType() != null) {
                    iReturn = -1;
                } else if (b1.getType() == null && b2.getType() == null) {
                    iReturn = 0;
                } else if (b1.getType() != null && b2.getType() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getType().compareTo(b2.getType());
                }
                break;
            case VdPermRoleDAO._ID_TYPE:
                if (b1.getIdType() == null && b2.getIdType() != null) {
                    iReturn = -1;
                } else if (b1.getIdType() == null && b2.getIdType() == null) {
                    iReturn = 0;
                } else if (b1.getIdType() != null && b2.getIdType() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getIdType().compareTo(b2.getIdType());
                }
                break;
            case VdPermRoleDAO._ID_ROLE:
                if (b1.getIdRole() == null && b2.getIdRole() != null) {
                    iReturn = -1;
                } else if (b1.getIdRole() == null && b2.getIdRole() == null) {
                    iReturn = 0;
                } else if (b1.getIdRole() != null && b2.getIdRole() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getIdRole().compareTo(b2.getIdRole());
                }
                break;
            default:
                throw new IllegalArgumentException("Type passed for the field is not supported");
        }
        return bReverse ? (-1 * iReturn) : iReturn;
    }
}
