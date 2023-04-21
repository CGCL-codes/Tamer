package org.mobicents.protocols.ss7.cap.api.primitives;

/**
*
ExtensionField ::= SEQUENCE {
	type EXTENSION.&id ({SupportedExtensions}),
	-- shall identify the value of an EXTENSION type
	criticality CriticalityType DEFAULT ignore,
	value [1] EXTENSION.&ExtensionType ({SupportedExtensions}{@type}),
	...
}
-- This parameter indicates an extension of an argument data type.
-- Its content is network operator specific
 
CriticalityType  ::= ENUMERATED { 
        ignore    (0), 
        abort     (1) 
        } 
Code ::= CHOICE {local   INTEGER, 
                 global  OBJECT IDENTIFIER} 
* 
* @author sergey vetyutnev
* 
*/
public interface ExtensionField {

    public Integer getLocalCode();

    public long[] getGlobalCode();

    public CriticalityType getCriticalityType();

    /**
	 * 
	 * @return Encoded field parameter without tag and length fields
	 */
    public byte[] getData();

    public void setLocalCode(Integer localCode);

    public void setGlobalCode(long[] globalCode);

    public void setCriticalityType(CriticalityType criticalityType);

    /**
	 * @param data
	 *            Encoded field parameter without tag and length fields
	 */
    public void setData(byte[] data);
}
