package de.ibk.ods.implementation;

import org.asam.ods.AoException;
import org.asam.ods.BuildUpFunction;
import org.asam.ods.Column;
import org.asam.ods.SMatLinkPOA;
import org.asam.ods.SubMatrix;
import org.omg.PortableServer.POA;

/**
 * @author Reinhard Kessler, Ingenieurbüro Kessler
 * @version 5.0.0
 */
public class SMatLinkImpl extends SMatLinkPOA {

    /**
	 * 
	 */
    public SMatLinkImpl(byte[] oid, POA adapter) {
        super();
    }

    public BuildUpFunction getLinkType() throws AoException {
        return null;
    }

    public int getOrdinalNumber() throws AoException {
        return 0;
    }

    public SubMatrix getSMat1() throws AoException {
        return null;
    }

    public Column[] getSMat1Columns() throws AoException {
        return null;
    }

    public SubMatrix getSMat2() throws AoException {
        return null;
    }

    public Column[] getSMat2Columns() throws AoException {
        return null;
    }

    public void setLinkType(BuildUpFunction linkType) throws AoException {
    }

    public void setOrdinalNumber(int ordinalNumber) throws AoException {
    }

    public void setSMat1(SubMatrix subMat1) throws AoException {
    }

    public void setSMat1Columns(Column[] columns) throws AoException {
    }

    public void setSMat2(SubMatrix subMat2) throws AoException {
    }

    public void setSMat2Columns(Column[] columns) throws AoException {
    }
}
