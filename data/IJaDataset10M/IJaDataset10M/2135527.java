package org.dcm4cheri.auditlog;

import org.dcm4che.auditlog.Patient;
import org.dcm4che.auditlog.User;

/**
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 3922 $ $Date: 2005-10-05 12:26:16 -0400 (Wed, 05 Oct 2005) $
 * @since 12.10.2004
 *
 */
public class ProcedureRecord implements IHEYr4.Message {

    private final String action;

    private final String placerOrderNumber;

    private final String fillerOrderNumber;

    private final String suid;

    private final String accessionNumber;

    private final Patient patient;

    private final User user;

    private final String desc;

    public ProcedureRecord(String action, String placerOrderNumber, String fillerOrderNumber, String suid, String accessionNumber, Patient patient, User user, String desc) {
        this.action = action;
        this.placerOrderNumber = placerOrderNumber;
        this.fillerOrderNumber = fillerOrderNumber;
        this.suid = suid;
        this.accessionNumber = accessionNumber;
        this.patient = patient;
        this.user = user;
        this.desc = desc != null && desc.length() != 0 ? desc : null;
    }

    public void writeTo(StringBuffer sb) {
        sb.append("<ProcedureRecord><ObjectAction>").append(action).append("</ObjectAction>").append("<PlacerOrderNumber><![CDATA[").append(placerOrderNumber).append("]]></PlacerOrderNumber>").append("<FillerOrderNumber><![CDATA[").append(fillerOrderNumber).append("]]></FillerOrderNumber>").append("<SUID>").append(suid).append("</SUID>");
        if (accessionNumber != null) sb.append("<AccessionNumber><![CDATA[").append(accessionNumber).append("]]></AccessionNumber>");
        patient.writeTo(sb);
        user.writeTo(sb);
        if (desc != null) sb.append("<Description><![CDATA[").append(desc).append("]]></Description>");
        sb.append("</ProcedureRecord>");
    }
}
