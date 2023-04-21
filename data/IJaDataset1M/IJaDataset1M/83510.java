package com.idna.common.dao.impl;

import java.util.Map;
import javax.sql.DataSource;
import com.idna.common.dao.sp.CommunicationLogInsert;
import com.idna.common.dao.sp.GetCommsLogByFeatureIdAndTpCodeProcedure;
import com.idna.common.domain.CommsLog;
import com.idna.common.dao.CommsLogDao;

/**
 * A class used as a DAO
 * 
 * @author  Raymond Chan
 * @version     %I%, %G%
 * @since       1.0
 */
public class CommsLogDaoImpl implements CommsLogDao {

    private DataSource dataSource;

    private GetCommsLogByFeatureIdAndTpCodeProcedure getCommsLogByFeatureIdAndTpCodeProcedure;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    public String recordCommunicationCall(CommsLog commsLog) {
        CommunicationLogInsert communicationLogInsert = new CommunicationLogInsert(dataSource);
        Map map = communicationLogInsert.execute(commsLog);
        if (map.size() > 0) {
            return map.get("CommunicationLogID").toString();
        } else {
            return null;
        }
    }

    public CommsLog getCommsLog(String futureId, String tpCode) {
        return getCommsLogByFeatureIdAndTpCodeProcedure.execute(futureId, tpCode);
    }

    public void setGetCommsLogByFeatureIdAndTpCodeProcedure(GetCommsLogByFeatureIdAndTpCodeProcedure getCommsLogByFeatureIdAndTpCodeProcedure) {
        this.getCommsLogByFeatureIdAndTpCodeProcedure = getCommsLogByFeatureIdAndTpCodeProcedure;
    }
}
