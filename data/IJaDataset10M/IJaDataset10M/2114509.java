package org.asam.ods;

/**
 *	Generated from IDL interface "SMatLink"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public interface SMatLinkOperations {

    org.asam.ods.BuildUpFunction getLinkType() throws org.asam.ods.AoException;

    int getOrdinalNumber() throws org.asam.ods.AoException;

    org.asam.ods.SubMatrix getSMat1() throws org.asam.ods.AoException;

    org.asam.ods.Column[] getSMat1Columns() throws org.asam.ods.AoException;

    org.asam.ods.SubMatrix getSMat2() throws org.asam.ods.AoException;

    org.asam.ods.Column[] getSMat2Columns() throws org.asam.ods.AoException;

    void setLinkType(org.asam.ods.BuildUpFunction linkType) throws org.asam.ods.AoException;

    void setOrdinalNumber(int ordinalNumber) throws org.asam.ods.AoException;

    void setSMat1(org.asam.ods.SubMatrix subMat1) throws org.asam.ods.AoException;

    void setSMat1Columns(org.asam.ods.Column[] columns) throws org.asam.ods.AoException;

    void setSMat2(org.asam.ods.SubMatrix subMat2) throws org.asam.ods.AoException;

    void setSMat2Columns(org.asam.ods.Column[] columns) throws org.asam.ods.AoException;
}
