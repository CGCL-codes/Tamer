package org.dcm4che2.audit.message;

/**
 * This message describes the event of a person or process accessing a log 
 * of audit trail information.
 * 
 * <blockquote>
 * Note: For example, an implementation that maintains a local cache of
 * audit information that has not been transferred to a central collection
 * point might generate this message if its local cache were accessed by a user.
 * </blockquote>
 *
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision: 5685 $ $Date: 2008-01-15 15:05:18 -0500 (Tue, 15 Jan 2008) $
 * @since Nov 21, 2006
 * @see <a href="ftp://medical.nema.org/medical/dicom/supps/sup95_fz.pdf">
 * DICOM Supp 95: Audit Trail Messages, A.1.3.2 Audit Log Used</a>
 */
public class AuditLogUsedMessage extends AuditMessage {

    public AuditLogUsedMessage() {
        super(new AuditEvent(AuditEvent.ID.AUDIT_LOG_USED, AuditEvent.ActionCode.READ));
    }

    public ActiveParticipant addUserPerson(String userID, String altUserID, String userName, String hostname) {
        return addActiveParticipant(ActiveParticipant.createActivePerson(userID, altUserID, userName, hostname, true));
    }

    public ActiveParticipant addUserProcess(String processID, String[] aets, String processName, String hostname) {
        return addActiveParticipant(ActiveParticipant.createActiveProcess(processID, aets, processName, hostname, true));
    }

    public ParticipantObject addSecurityAuditLog(String uri) {
        ParticipantObject obj = ParticipantObject.createSecurityAuditLog(uri);
        addParticipantObject(obj);
        return obj;
    }

    @Override
    public void validate() {
        super.validate();
        ActiveParticipant user = getRequestingActiveParticipants();
        if (user == null) {
            throw new IllegalStateException("No Requesting User");
        }
        ParticipantObject auditLog = null;
        for (ParticipantObject po : participantObjects) {
            if (ParticipantObject.TypeCodeRole.SECURITY_RESOURCE == po.getParticipantObjectTypeCodeRole()) {
                if (auditLog != null) {
                    throw new IllegalStateException("Multiple Audit Log identification");
                }
                auditLog = po;
            }
        }
        if (auditLog == null) {
            throw new IllegalStateException("No Audit Log identification");
        }
    }
}
