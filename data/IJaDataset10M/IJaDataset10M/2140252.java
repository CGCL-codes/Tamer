package com.dcivision.alert.core;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import com.dcivision.alert.InvitationFacade;
import com.dcivision.alert.bean.UpdateAlertType;
import com.dcivision.audit.bean.AuditTrail;
import com.dcivision.audit.dao.AuditTrailDAObject;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.framework.Utility;

public class InvitationDocumentView implements InvitationFacade {

    private SessionContainer sessionContainer;

    private Connection dbConn;

    /*****************
	 * 
	 * @param sessionContainer
	 * @param dbConn
	 */
    public InvitationDocumentView(SessionContainer sessionContainer, Connection dbConn) {
        this.sessionContainer = sessionContainer;
        this.dbConn = dbConn;
    }

    /****************************
	 *  @param docID
	 *  @param updateAlertType
	 *  @param allInvitees
	 *  @author barbin
	 *  @exception ApplicationException
	 *  @return readUserNameList
	 */
    public List getReadDocmentUser(String docID, UpdateAlertType updateAlertType, List allInvitees) throws ApplicationException {
        List readUserNameList = new ArrayList();
        if (Utility.isEmpty(allInvitees)) {
            return readUserNameList;
        }
        AuditTrailDAObject auditTrailDAObject = new AuditTrailDAObject(sessionContainer, dbConn);
        List result = auditTrailDAObject.getListByDocumentID(docID);
        List userId = new ArrayList();
        if (!Utility.isEmpty(result)) {
            for (int i = 0; i < result.size(); i++) {
                AuditTrail tmpAuditTrail = (AuditTrail) result.get(i);
                if (tmpAuditTrail.getAccessDate().after(updateAlertType.getCreateDate())) {
                    userId.add(UserInfoFactory.getUserFullName(tmpAuditTrail.getAccessorID()));
                }
            }
        }
        for (int l = 0; l < userId.size(); l++) {
            if (allInvitees.contains(userId.get(l)) && !readUserNameList.contains(userId.get(l))) {
                readUserNameList.add(userId.get(l));
            }
        }
        return readUserNameList;
    }
}
