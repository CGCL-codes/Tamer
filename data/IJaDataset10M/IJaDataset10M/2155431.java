package com.voxdei.voxcontentSE.DAO.vdRole;

import java.util.Comparator;

/**
 * Comparator class is used to sort the VdRole objects.
 * @author Michael Salgado
 * @company VoxDei
 */
public class VdRoleComparator implements Comparator<Object> {

    /**
     * Holds the field on which the comparison is performed.
     */
    private int iType;

    /**
     * Value that will contain the information about the order of the sort: normal or reversal.
     */
    private boolean bReverse;

    /**
     * Constructor class for VdRoleComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdRoleComparator(VdRoleDAO.ID_SEE, bReverse));<code>
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdRoleDAO.ID_PARENT_ROLE</li>
     *   <li>VdRoleDAO.ID_DESCRIPTION</li>
     *   <li>VdRoleDAO.ID_NAME</li>
     *   <li>VdRoleDAO.ID_ALIAS</li>
     *   <li>VdRoleDAO.ID_ID</li>
     * </ul>
     */
    public VdRoleComparator(int iType) {
        this(iType, false);
    }

    /**
     * Constructor class for VdRoleComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdRoleComparator(VdRoleDAO.ID_SEE, bReverse));<code>
     *
     * @param iType the field from which you want to sort.
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdRoleDAO._SEE</li>
     *   <li>VdRoleDAO._REMOVE</li>
     *   <li>VdRoleDAO._EDIT</li>
     *   <li>VdRoleDAO._ADDNEW</li>
     *   <li>VdRoleDAO._PARENT_ROLE</li>
     *   <li>VdRoleDAO._DESCRIPTION</li>
     *   <li>VdRoleDAO._NAME</li>
     *   <li>VdRoleDAO._ALIAS</li>
     *   <li>VdRoleDAO._ID</li>
     * </ul>
     *
     * @param bReverse set this value to true, if you want to reverse the sorting results
     */
    public VdRoleComparator(int iType, boolean bReverse) {
        this.iType = iType;
        this.bReverse = bReverse;
    }

    /**
     * Implementation of the compare method.
     */
    public int compare(Object pObj1, Object pObj2) {
        VdRole b1 = (VdRole) pObj1;
        VdRole b2 = (VdRole) pObj2;
        int iReturn = 0;
        switch(iType) {
            case VdRoleDAO._PARENT_ROLE:
                if (b1.getParentRole() == null && b2.getParentRole() != null) {
                    iReturn = -1;
                } else if (b1.getParentRole() == null && b2.getParentRole() == null) {
                    iReturn = 0;
                } else if (b1.getParentRole() != null && b2.getParentRole() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getParentRole().compareTo(b2.getParentRole());
                }
                break;
            case VdRoleDAO._DESCRIPTION:
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
            case VdRoleDAO._NAME:
                if (b1.getName() == null && b2.getName() != null) {
                    iReturn = -1;
                } else if (b1.getName() == null && b2.getName() == null) {
                    iReturn = 0;
                } else if (b1.getName() != null && b2.getName() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getName().compareTo(b2.getName());
                }
                break;
            case VdRoleDAO._ALIAS:
                if (b1.getAlias() == null && b2.getAlias() != null) {
                    iReturn = -1;
                } else if (b1.getAlias() == null && b2.getAlias() == null) {
                    iReturn = 0;
                } else if (b1.getAlias() != null && b2.getAlias() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getAlias().compareTo(b2.getAlias());
                }
                break;
            case VdRoleDAO._ID:
                if (b1.getId() == null && b2.getId() != null) {
                    iReturn = -1;
                } else if (b1.getId() == null && b2.getId() == null) {
                    iReturn = 0;
                } else if (b1.getId() != null && b2.getId() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getId().compareTo(b2.getId());
                }
                break;
            default:
                throw new IllegalArgumentException("Type passed for the field is not supported");
        }
        return bReverse ? (-1 * iReturn) : iReturn;
    }
}
