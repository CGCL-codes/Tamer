package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	RfQ Topic Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQTopic.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MRfQTopic extends X_C_RfQ_Topic {

    /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQ_Topic_ID id
	 *	@param trxName transaction
	 */
    public MRfQTopic(Properties ctx, int C_RfQ_Topic_ID, String trxName) {
        super(ctx, C_RfQ_Topic_ID, trxName);
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public MRfQTopic(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * 	Get Current Topic Subscribers
	 *	@return array subscribers
	 */
    public MRfQTopicSubscriber[] getSubscribers() {
        ArrayList<MRfQTopicSubscriber> list = new ArrayList<MRfQTopicSubscriber>();
        String sql = "SELECT * FROM C_RfQ_TopicSubscriber " + "WHERE C_RfQ_Topic_ID=? AND IsActive='Y'";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getC_RfQ_Topic_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(new MRfQTopicSubscriber(getCtx(), rs, get_TrxName()));
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getSubscribers", e);
        }
        try {
            if (pstmt != null) pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        MRfQTopicSubscriber[] retValue = new MRfQTopicSubscriber[list.size()];
        list.toArray(retValue);
        return retValue;
    }
}
