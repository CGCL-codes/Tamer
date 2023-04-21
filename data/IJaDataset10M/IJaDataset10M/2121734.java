package socialnetwork;

/** 
 * @author Terje Br�dland
 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EventRoleImpl implements IEventRole {

    /** 
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Boolean participant;

    public Boolean getParticipant() {
        return participant;
    }

    public void setParticipant(Boolean theParticipant) {
        participant = theParticipant;
    }

    /** 
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Boolean organizer;

    public Boolean getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Boolean theOrganizer) {
        organizer = theOrganizer;
    }

    /** 
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private IStudent student;

    public IStudent getStudent() {
        return student;
    }

    public void setStudent(IStudent theStudent) {
        student = theStudent;
    }

    /** 
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private IEvent event;

    public IEvent getEvent() {
        return event;
    }

    public void setEvent(IEvent theEvent) {
        event = theEvent;
    }
}
