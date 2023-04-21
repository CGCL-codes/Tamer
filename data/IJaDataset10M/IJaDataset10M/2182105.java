package com.voxdei.voxcontentSE.DAO;

import java.util.Comparator;

/**
 * Comparator class is used to sort the VdTemplates objects.
 * @author Michael Salgado
 * @company VoxDei
 */
public class VdTemplatesComparator implements Comparator<Object> {

    /**
     * Holds the field on which the comparison is performed.
     */
    private int iType;

    /**
     * Value that will contain the information about the order of the sort: normal or reversal.
     */
    private boolean bReverse;

    /**
     * Constructor class for VdTemplatesComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdTemplatesComparator(VdTemplatesDAO.ID_PARAMETERS, bReverse));<code>
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdTemplatesDAO.ID_PARAMETERS</li>
     *   <li>VdTemplatesDAO.ID_CLAZZ</li>
     *   <li>VdTemplatesDAO.ID_ADMIN_LINK</li>
     *   <li>VdTemplatesDAO.ID_ADMIN_CLASS</li>
     *   <li>VdTemplatesDAO.ID_TYPE</li>
     *   <li>VdTemplatesDAO.ID_IMAGE</li>
     *   <li>VdTemplatesDAO.ID_COMPANY</li>
     *   <li>VdTemplatesDAO.ID_AUTHOR</li>
     *   <li>VdTemplatesDAO.ID_EMAIL</li>
     *   <li>VdTemplatesDAO.ID_DESCRIPTION</li>
     *   <li>VdTemplatesDAO.ID_NAME</li>
     *   <li>VdTemplatesDAO.ID_ID</li>
     * </ul>
     */
    public VdTemplatesComparator(int iType) {
        this(iType, false);
    }

    /**
     * Constructor class for VdTemplatesComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdTemplatesComparator(VdTemplatesDAO.ID_PARAMETERS, bReverse));<code>
     *
     * @param iType the field from which you want to sort.
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdTemplatesDAO._PARAMETERS</li>
     *   <li>VdTemplatesDAO._CLAZZ</li>
     *   <li>VdTemplatesDAO._ADMIN_LINK</li>
     *   <li>VdTemplatesDAO._ADMIN_CLASS</li>
     *   <li>VdTemplatesDAO._TYPE</li>
     *   <li>VdTemplatesDAO._IS_DEFAULT</li>
     *   <li>VdTemplatesDAO._ENABLED</li>
     *   <li>VdTemplatesDAO._IMAGE</li>
     *   <li>VdTemplatesDAO._COMPANY</li>
     *   <li>VdTemplatesDAO._AUTHOR</li>
     *   <li>VdTemplatesDAO._EMAIL</li>
     *   <li>VdTemplatesDAO._DESCRIPTION</li>
     *   <li>VdTemplatesDAO._NAME</li>
     *   <li>VdTemplatesDAO._ID</li>
     * </ul>
     *
     * @param bReverse set this value to true, if you want to reverse the sorting results
     */
    public VdTemplatesComparator(int iType, boolean bReverse) {
        this.iType = iType;
        this.bReverse = bReverse;
    }

    /**
     * Implementation of the compare method.
     */
    public int compare(Object pObj1, Object pObj2) {
        VdTemplates b1 = (VdTemplates) pObj1;
        VdTemplates b2 = (VdTemplates) pObj2;
        int iReturn = 0;
        switch(iType) {
            case VdTemplatesDAO._PARAMETERS:
                if (b1.getParameters() == null && b2.getParameters() != null) {
                    iReturn = -1;
                } else if (b1.getParameters() == null && b2.getParameters() == null) {
                    iReturn = 0;
                } else if (b1.getParameters() != null && b2.getParameters() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getParameters().compareTo(b2.getParameters());
                }
                break;
            case VdTemplatesDAO._CLAZZ:
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
            case VdTemplatesDAO._ADMIN_LINK:
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
            case VdTemplatesDAO._ADMIN_CLASS:
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
            case VdTemplatesDAO._TYPE:
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
            case VdTemplatesDAO._IMAGE:
                if (b1.getImage() == null && b2.getImage() != null) {
                    iReturn = -1;
                } else if (b1.getImage() == null && b2.getImage() == null) {
                    iReturn = 0;
                } else if (b1.getImage() != null && b2.getImage() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getImage().compareTo(b2.getImage());
                }
                break;
            case VdTemplatesDAO._COMPANY:
                if (b1.getCompany() == null && b2.getCompany() != null) {
                    iReturn = -1;
                } else if (b1.getCompany() == null && b2.getCompany() == null) {
                    iReturn = 0;
                } else if (b1.getCompany() != null && b2.getCompany() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getCompany().compareTo(b2.getCompany());
                }
                break;
            case VdTemplatesDAO._AUTHOR:
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
            case VdTemplatesDAO._EMAIL:
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
            case VdTemplatesDAO._DESCRIPTION:
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
            case VdTemplatesDAO._NAME:
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
            case VdTemplatesDAO._ID:
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
