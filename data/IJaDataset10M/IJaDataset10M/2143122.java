package org.compiere.model;

import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 *	Package Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPackageLine.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MPackageLine extends X_M_PackageLine {

    /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_PackageLine_ID id
	 *	@param trxName transaction
	 */
    public MPackageLine(Properties ctx, int M_PackageLine_ID, String trxName) {
        super(ctx, M_PackageLine_ID, trxName);
        if (M_PackageLine_ID == 0) {
            setQty(Env.ZERO);
        }
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public MPackageLine(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * 	Parent Constructor
	 *	@param parent header
	 */
    public MPackageLine(MPackage parent) {
        this(parent.getCtx(), 0, parent.get_TrxName());
        setClientOrg(parent);
        setM_Package_ID(parent.getM_Package_ID());
    }

    /**
	 * 	Set Shipment Line
	 *	@param line line
	 */
    public void setInOutLine(MInOutLine line) {
        setM_InOutLine_ID(line.getM_InOutLine_ID());
        setQty(line.getMovementQty());
    }
}
