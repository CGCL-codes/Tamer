package org.compiere.process;

/**
 * 	Validate Info Window SQL
 *	
 *  @author Jorg Janke
 *  @version $Id: InfoWindowValidate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class InfoWindowValidate extends SvrProcess {

    /**	Info Window			*/
    private int p_AD_InfoWindow_ID = 0;

    /**
	 * 	Prepare
	 */
    protected void prepare() {
        p_AD_InfoWindow_ID = getRecord_ID();
    }

    /**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
    protected String doIt() throws Exception {
        return null;
    }
}
