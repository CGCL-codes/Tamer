package org.compiere.model;

import java.sql.*;
import java.util.*;

/**
 * 	Tax Category Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTaxCategory.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MTaxCategory extends X_C_TaxCategory {

    /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_TaxCategory_ID id
	 *	@param trxName trx
	 */
    public MTaxCategory(Properties ctx, int C_TaxCategory_ID, String trxName) {
        super(ctx, C_TaxCategory_ID, trxName);
        if (C_TaxCategory_ID == 0) {
            setIsDefault(false);
        }
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs resukt set
	 *	@param trxName trx
	 */
    public MTaxCategory(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
