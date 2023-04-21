package org.adempierelbr.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.CLogger;

/**
 *	MProcessLink
 *
 *	Model for X_LBR_ProcessLink
 *	
 *	@author Alvaro Montenegro (Kenos, www.kenos.com.br)
 *	@version $Id: MProcessLink.java, 03/02/2009 14:10:00 amontenegro
 */
public class MProcessLink extends X_LBR_ProcessLink {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**	Logger			*/
    public static CLogger log = CLogger.getCLogger(MProcessLink.class);

    /**************************************************************************
	 *  Default Constructor
	 *  @param Properties ctx
	 *  @param int ID (0 create new)
	 *  @param String trx
	 */
    public MProcessLink(Properties ctx, int ID, String trx) {
        super(ctx, ID, trx);
    }

    /**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
    public MProcessLink(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
