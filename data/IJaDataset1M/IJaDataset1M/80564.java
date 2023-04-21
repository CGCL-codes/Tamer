package org.openspp.command;

import org.openspp.dao.RouteRecordDAO;
import org.openspp.dto.ObjectKey;
import org.openspp.dto.RouteRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
public class DelRteRecCommand implements SpppCommand<RouteRecord>, CommandResultMessages {

    private RouteRecord rteRec = null;

    private RouteRecordDAO dao;

    public RouteRecordDAO getDao() {
        return dao;
    }

    @Autowired
    public void setDao(RouteRecordDAO dao) {
        this.dao = dao;
    }

    public OverallResult execute() {
        OverallResult result = new OverallResult();
        ObjectKey objKey = new ObjectKey();
        objKey.setObjectName(this.getRteRec().getRouteRecordName());
        objKey.setRegistrantId(this.getRteRec().getOrganizationId());
        objKey.setRegistrantName(this.getRteRec().getOrganizationName());
        try {
            dao.delRouteRecord(objKey);
            result = new OverallResult();
            result.setResultCode(CommandResultCodes.REQUESTSUCCEEDED.getResultCode());
            result.setStrMessage(succeededMsgStr);
        } catch (DuplicateKeyException e) {
            ObjectLevelResult<RouteRecord> objResult = new ObjectLevelResult<RouteRecord>();
            objResult.setResultCode(CommandResultCodes.ATTRIBUTEVALUEINVALID.getResultCode());
            objResult.setStrMessage(registryDataAccessError);
            objResult.setAttributeName("rant dgName");
            objResult.setAttributeValue(this.getRteRec());
            result = objResult;
        } catch (DataAccessException e) {
            result = new OverallResult();
            result.setResultCode(CommandResultCodes.UNEXPECTEDSYETEMERROR.getResultCode());
            result.setStrMessage(registryDataAccessError);
        }
        return result;
    }

    private RouteRecord getRteRec() {
        return rteRec;
    }

    private void setRteRec(RouteRecord rteRec) {
        this.rteRec = rteRec;
    }

    public void setDto(RouteRecord dto) {
        this.setRteRec((RouteRecord) (dto));
    }
}
