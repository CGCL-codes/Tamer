package org.colimas.services.ws;

import org.colimas.services.I_Service;

/**
 * <h3>AbstractWebService.java</h3>
 *
 * <P>
 * Function:<BR />
 * Do action with web service. @see execute method
 * </P>
 * @author zhao lei
 * @version 1.1
 *
 * Modification History:
 * <PRE>
 * SEQ DATE       ORDER DEVELOPER      DESCRIPTION
 * --- ---------- ----- -------------- -----------------------------
 * 001 2005/12/24          zhao lei       INIT
 * 002 2005/12/30          zhao lei       change the return type
 * </PRE>
 */
public abstract class AbstractWebService implements I_Service {

    /**
	 *<p>constructor</p>
	 */
    public AbstractWebService() {
        super();
    }

    /**
	 *<p>
	 * execute the action:
	 * deploy
	 * undeploy
	 * checkout web service list from a web service server</p>
	 * @param params
	 * @return result
	 * @throws WebServiceException
	 */
    public abstract Object execute(Object[] params) throws WebServiceException;

    protected String[] toStrings(Object[] objects) {
        if (objects == null) return null;
        String[] strs = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            strs[i] = (String) objects[i];
        }
        return strs;
    }
}
