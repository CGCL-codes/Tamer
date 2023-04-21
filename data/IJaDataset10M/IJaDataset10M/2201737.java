package org.aigebi.rbac.to;

import org.aigebi.rbac.hibernate.dao.AgbLtoken;
import org.aigebi.rbac.hibernate.dao.AgbOtoken;

/**Transfer object for operation.
 * @author Ligong Xu
 * @version $Id: Operation.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class Operation extends BaseTO implements OperationTO {

    private String mName;

    private String mDescription;

    private Long otokenId;

    private Long ltokenId;

    private LtokenTO ltoken;

    private OtokenTO otoken;

    /**
	 * @return the description
	 */
    public String getDescription() {
        return mDescription;
    }

    /**
	 * @param pDescription the description to set
	 */
    public void setDescription(String pDescription) {
        mDescription = pDescription;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return mName;
    }

    /**
	 * @param pName the name to set
	 */
    public void setName(String pName) {
        mName = pName;
    }

    public Operation() {
        super();
    }

    public Operation(Long pId) {
        super(pId);
    }

    public Operation(Long pId, String pName, String pDescription) {
        super(pId);
        mName = pName;
        mDescription = pDescription;
    }

    public Long getLtokenId() {
        return ltokenId;
    }

    public void setLtokenId(Long pLtokenId) {
        ltokenId = pLtokenId;
    }

    public Long getOtokenId() {
        return otokenId;
    }

    public void setOtokenId(Long pOtokenId) {
        otokenId = pOtokenId;
    }

    public LtokenTO getLtoken() {
        return ltoken;
    }

    public void setLtoken(LtokenTO pLtoken) {
        ltoken = pLtoken;
    }

    public OtokenTO getOtoken() {
        return otoken;
    }

    public void setOtoken(OtokenTO pOtoken) {
        otoken = pOtoken;
    }
}
