package com.columboid.protocol.syncml.deviceinfo;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:element xmlns:ns=&quot;http://protocol.columboid.com/syncml/deviceinfo&quot; xmlns:xs=&quot;http://www.w3.org/2001/XMLSchema&quot; name=&quot;Rx-Pref&quot;&gt;
 *   &lt;xs:complexType&gt;
 *     &lt;xs:sequence&gt;
 *       &lt;xs:element ref=&quot;ns:CTType&quot;/&gt;
 *       &lt;xs:element ref=&quot;ns:VerCT&quot;/&gt;
 *     &lt;/xs:sequence&gt;
 *   &lt;/xs:complexType&gt;
 * &lt;/xs:element&gt;
 * </pre>
 */
public class RxPref {

    private CTType CTType;

    private VerCT verCT;

    /**
	 * Get the 'CTType' element value.
	 * 
	 * @return value
	 */
    public CTType getCTType() {
        return CTType;
    }

    /**
	 * Set the 'CTType' element value.
	 * 
	 * @param CTType
	 */
    public void setCTType(CTType CTType) {
        this.CTType = CTType;
    }

    /**
	 * Get the 'VerCT' element value.
	 * 
	 * @return value
	 */
    public VerCT getVerCT() {
        return verCT;
    }

    /**
	 * Set the 'VerCT' element value.
	 * 
	 * @param verCT
	 */
    public void setVerCT(VerCT verCT) {
        this.verCT = verCT;
    }
}
