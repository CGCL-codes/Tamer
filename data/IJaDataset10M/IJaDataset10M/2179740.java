package com.voxdei.voxcontentSE.DAO;

import java.util.Comparator;

/**
 * Comparator class is used to sort the VdModuleType objects.
 * @author Michael Salgado
 * @company VoxDei
 */
public class VdModuleTypeComparator implements Comparator<Object> {

    /**
     * Holds the field on which the comparison is performed.
     */
    private int iType;

    /**
     * Value that will contain the information about the order of the sort: normal or reversal.
     */
    private boolean bReverse;

    /**
     * Constructor class for VdModuleTypeComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdModuleTypeComparator(VdModuleTypeDAO.ID_URL, bReverse));<code>
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdModuleTypeDAO.ID_URL</li>
     *   <li>VdModuleTypeDAO.ID_EMAIL</li>
     *   <li>VdModuleTypeDAO.ID_AUTHOR</li>
     *   <li>VdModuleTypeDAO.ID_TITLE</li>
     *   <li>VdModuleTypeDAO.ID_DESCRIPTION</li>
     *   <li>VdModuleTypeDAO.ID_MODULETYPE</li>
     *   <li>VdModuleTypeDAO.ID_CLAZZ</li>
     *   <li>VdModuleTypeDAO.ID_ADMIN_LINK</li>
     *   <li>VdModuleTypeDAO.ID_ADMIN_CLASS</li>
     *   <li>VdModuleTypeDAO.ID_ID</li>
     * </ul>
     */
    public VdModuleTypeComparator(int iType) {
        this(iType, false);
    }

    /**
     * Constructor class for VdModuleTypeComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdModuleTypeComparator(VdModuleTypeDAO.ID_URL, bReverse));<code>
     *
     * @param iType the field from which you want to sort.
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdModuleTypeDAO._URL</li>
     *   <li>VdModuleTypeDAO._EMAIL</li>
     *   <li>VdModuleTypeDAO._AUTHOR</li>
     *   <li>VdModuleTypeDAO._TITLE</li>
     *   <li>VdModuleTypeDAO._DESCRIPTION</li>
     *   <li>VdModuleTypeDAO._MODULETYPE</li>
     *   <li>VdModuleTypeDAO._CLAZZ</li>
     *   <li>VdModuleTypeDAO._ADMIN_LINK</li>
     *   <li>VdModuleTypeDAO._ADMIN_CLASS</li>
     *   <li>VdModuleTypeDAO._ID</li>
     * </ul>
     *
     * @param bReverse set this value to true, if you want to reverse the sorting results
     */
    public VdModuleTypeComparator(int iType, boolean bReverse) {
        this.iType = iType;
        this.bReverse = bReverse;
    }

    /**
     * Implementation of the compare method.
     */
    public int compare(Object pObj1, Object pObj2) {
        VdModuleType b1 = (VdModuleType) pObj1;
        VdModuleType b2 = (VdModuleType) pObj2;
        int iReturn = 0;
        switch(iType) {
            case VdModuleTypeDAO._URL:
                if (b1.getUrl() == null && b2.getUrl() != null) {
                    iReturn = -1;
                } else if (b1.getUrl() == null && b2.getUrl() == null) {
                    iReturn = 0;
                } else if (b1.getUrl() != null && b2.getUrl() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getUrl().compareTo(b2.getUrl());
                }
                break;
            case VdModuleTypeDAO._EMAIL:
                if (b1.getEmail() == null && b2.getEmail() != null) {
                    iReturn = -1;
                } else if (b1.getEmail() == null && b2.getEmail() == null) {
                    iReturn = 0;
                } else if (b1.getEmail() != null && b2.getEmail() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getEmail().compareTo(b2.getEmail());
                }
                break;
            case VdModuleTypeDAO._AUTHOR:
                if (b1.getAuthor() == null && b2.getAuthor() != null) {
                    iReturn = -1;
                } else if (b1.getAuthor() == null && b2.getAuthor() == null) {
                    iReturn = 0;
                } else if (b1.getAuthor() != null && b2.getAuthor() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getAuthor().compareTo(b2.getAuthor());
                }
                break;
            case VdModuleTypeDAO._TITLE:
                if (b1.getTitle() == null && b2.getTitle() != null) {
                    iReturn = -1;
                } else if (b1.getTitle() == null && b2.getTitle() == null) {
                    iReturn = 0;
                } else if (b1.getTitle() != null && b2.getTitle() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getTitle().compareTo(b2.getTitle());
                }
                break;
            case VdModuleTypeDAO._DESCRIPTION:
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
            case VdModuleTypeDAO._MODULETYPE:
                if (b1.getModuletype() == null && b2.getModuletype() != null) {
                    iReturn = -1;
                } else if (b1.getModuletype() == null && b2.getModuletype() == null) {
                    iReturn = 0;
                } else if (b1.getModuletype() != null && b2.getModuletype() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getModuletype().compareTo(b2.getModuletype());
                }
                break;
            case VdModuleTypeDAO._CLAZZ:
                if (b1.getClazz() == null && b2.getClazz() != null) {
                    iReturn = -1;
                } else if (b1.getClazz() == null && b2.getClazz() == null) {
                    iReturn = 0;
                } else if (b1.getClazz() != null && b2.getClazz() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getClazz().compareTo(b2.getClazz());
                }
                break;
            case VdModuleTypeDAO._ADMIN_LINK:
                if (b1.getAdminLink() == null && b2.getAdminLink() != null) {
                    iReturn = -1;
                } else if (b1.getAdminLink() == null && b2.getAdminLink() == null) {
                    iReturn = 0;
                } else if (b1.getAdminLink() != null && b2.getAdminLink() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getAdminLink().compareTo(b2.getAdminLink());
                }
                break;
            case VdModuleTypeDAO._ADMIN_CLASS:
                if (b1.getAdminClass() == null && b2.getAdminClass() != null) {
                    iReturn = -1;
                } else if (b1.getAdminClass() == null && b2.getAdminClass() == null) {
                    iReturn = 0;
                } else if (b1.getAdminClass() != null && b2.getAdminClass() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getAdminClass().compareTo(b2.getAdminClass());
                }
                break;
            case VdModuleTypeDAO._ID:
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
