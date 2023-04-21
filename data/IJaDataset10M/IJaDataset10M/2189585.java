package org.mobicents.protocols.ss7.map.api.primitives;

import java.util.ArrayList;

/**
 * @author sergey vetyutnev
 */
public interface MAPExtensionContainer {

    /**
	 * Get the PrivateExtension list
	 * 
	 * @return
	 */
    public ArrayList<MAPPrivateExtension> getPrivateExtensionList();

    /**
	 * Set the PrivateExtension list
	 * 
	 * @param privateExtensionList
	 */
    public void setPrivateExtensionList(ArrayList<MAPPrivateExtension> privateExtensionList);

    /**
	 * Get the Pcs-Extensions - ASN.1 encoded byte array
	 * 
	 * @return
	 */
    public byte[] getPcsExtensions();

    /**
	 * Set the Pcs-Extensions - ASN.1 encoded byte array
	 * 
	 * @param pcsExtensions
	 */
    public void setPcsExtensions(byte[] pcsExtensions);
}
