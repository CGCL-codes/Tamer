package com.voxdei.voxcontentSE.DAO;

import java.util.Comparator;

/**
 * Comparator class is used to sort the VdContentImage objects.
 * @author Michael Salgado
 * @company VoxDei
 */
public class VdContentImageComparator implements Comparator<Object> {

    /**
     * Holds the field on which the comparison is performed.
     */
    private int iType;

    /**
     * Value that will contain the information about the order of the sort: normal or reversal.
     */
    private boolean bReverse;

    /**
     * Constructor class for VdContentImageComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdContentImageComparator(VdContentImageDAO.ID_FOOTPAGE, bReverse));<code>
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdContentImageDAO.ID_FOOTPAGE</li>
     *   <li>VdContentImageDAO.ID_ID_CONTENT</li>
     *   <li>VdContentImageDAO.ID_URL</li>
     *   <li>VdContentImageDAO.ID_TITLE</li>
     *   <li>VdContentImageDAO.ID_ID</li>
     * </ul>
     */
    public VdContentImageComparator(int iType) {
        this(iType, false);
    }

    /**
     * Constructor class for VdContentImageComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new VdContentImageComparator(VdContentImageDAO.ID_FOOTPAGE, bReverse));<code>
     *
     * @param iType the field from which you want to sort.
     * <br>
     * Possible values are:
     * <ul>
     *   <li>VdContentImageDAO._FOOTPAGE</li>
     *   <li>VdContentImageDAO._ID_CONTENT</li>
     *   <li>VdContentImageDAO._URL</li>
     *   <li>VdContentImageDAO._TITLE</li>
     *   <li>VdContentImageDAO._ID</li>
     * </ul>
     *
     * @param bReverse set this value to true, if you want to reverse the sorting results
     */
    public VdContentImageComparator(int iType, boolean bReverse) {
        this.iType = iType;
        this.bReverse = bReverse;
    }

    /**
     * Implementation of the compare method.
     */
    public int compare(Object pObj1, Object pObj2) {
        VdContentImage b1 = (VdContentImage) pObj1;
        VdContentImage b2 = (VdContentImage) pObj2;
        int iReturn = 0;
        switch(iType) {
            case VdContentImageDAO._FOOTPAGE:
                if (b1.getFootpage() == null && b2.getFootpage() != null) {
                    iReturn = -1;
                } else if (b1.getFootpage() == null && b2.getFootpage() == null) {
                    iReturn = 0;
                } else if (b1.getFootpage() != null && b2.getFootpage() == null) {
                    iReturn = 1;
                } else {
                    iReturn = b1.getFootpage().compareTo(b2.getFootpage());
                }
                break;
            case VdContentImageDAO._ID_CONTENT:
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
            case VdContentImageDAO._URL:
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
            case VdContentImageDAO._TITLE:
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
            case VdContentImageDAO._ID:
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
