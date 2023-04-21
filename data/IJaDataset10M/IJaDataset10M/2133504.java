package com.knowgate.crm;

import java.math.BigDecimal;
import java.sql.SQLException;
import com.knowgate.jdc.JDCConnection;
import com.knowgate.dataobjs.DB;
import com.knowgate.dataobjs.DBPersist;
import com.knowgate.misc.Gadgets;

/**
 * <p>Send and Received Phone Calls</p>
 * @author Sergio Montoro Ten
 * @version 2.1
 */
public class PhoneCall extends DBPersist {

    public PhoneCall() {
        super(DB.k_phone_calls, "PhoneCall");
    }

    public boolean store(JDCConnection oConn) throws SQLException {
        if (!AllVals.containsKey(DB.gu_phonecall)) put(DB.gu_phonecall, Gadgets.generateUUID());
        if (!AllVals.containsKey(DB.id_status)) {
            if (oConn.getDataBaseProduct() == JDCConnection.DBMS_ORACLE) put(DB.id_status, new BigDecimal(0)); else put(DB.id_status, (short) 0);
        }
        return super.store(oConn);
    }

    public static final short ClassId = 22;
}
