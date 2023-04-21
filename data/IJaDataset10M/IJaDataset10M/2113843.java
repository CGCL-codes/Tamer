package at.rc.tacos.client.controller;

import java.util.Calendar;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Duplicates the transport
 * 
 * @author b.thek
 */
public class CopyTransportAction extends Action implements IProgramStatus {

    private TableViewer viewer;

    /**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
    public CopyTransportAction(TableViewer viewer) {
        this.viewer = viewer;
        setText("Kopie erstellen");
        setToolTipText("Dupliziert den ausgew�hlten Transport");
    }

    @Override
    public void run() {
        ISelection selection = viewer.getSelection();
        Transport t1 = (Transport) ((IStructuredSelection) selection).getFirstElement();
        Transport t2 = new Transport();
        t2.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
        t2.setTransportId(0);
        t2.setTransportNumber(0);
        t2.clearVehicleDetail();
        t2.setCreationTime(Calendar.getInstance().getTimeInMillis());
        if (t1.getProgramStatus() == PROGRAM_STATUS_PREBOOKING) t2.setProgramStatus(PROGRAM_STATUS_PREBOOKING);
        if (t1.getProgramStatus() == PROGRAM_STATUS_OUTSTANDING || t1.getProgramStatus() == PROGRAM_STATUS_UNDERWAY) t2.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
        t2.setTransportPriority(t1.getTransportPriority());
        t2.getStatusMessages().clear();
        t2.setYear(Calendar.getInstance().get(Calendar.YEAR));
        t2.setDateOfTransport(t1.getDateOfTransport());
        t2.setAppointmentTimeAtDestination(t1.getAppointmentTimeAtDestination());
        t2.setPlannedStartOfTransport(t1.getPlannedStartOfTransport());
        t2.setPlannedTimeAtPatient(t1.getPlannedTimeAtPatient());
        t2.setHelicopterAlarming(t1.isHelicopterAlarming());
        t2.setPoliceAlarming(t1.isPoliceAlarming());
        t2.setAssistantPerson(t1.isAssistantPerson());
        t2.setBackTransport(t1.isBackTransport());
        t2.setBlueLightToGoal(t1.isBlueLightToGoal());
        t2.setBrkdtAlarming(t1.isBrkdtAlarming());
        t2.setFirebrigadeAlarming(t1.isFirebrigadeAlarming());
        t2.setDfAlarming(t1.isDfAlarming());
        t2.setEmergencyDoctorAlarming(t1.isEmergencyDoctorAlarming());
        t2.setEmergencyPhone(t1.isEmergencyPhone());
        t2.setLongDistanceTrip(t1.isLongDistanceTrip());
        t2.setMountainRescueServiceAlarming(t1.isMountainRescueServiceAlarming());
        if (t1.getKindOfIllness() != null) t2.setKindOfIllness(t1.getKindOfIllness());
        if (t1.getKindOfTransport() != null) t2.setKindOfTransport(t1.getKindOfTransport());
        if (t1.getCallerDetail() != null) t2.setCallerDetail(t1.getCallerDetail());
        if (t1.getFeedback() != null) t2.setFeedback(t1.getFeedback());
        t2.setPlanedLocation(t1.getPlanedLocation());
        t2.setPatient(t1.getPatient());
        t2.setDirection(t1.getDirection());
        t2.setFromCity(t1.getFromCity());
        t2.setFromStreet(t1.getFromStreet());
        t2.setToCity(t1.getToCity());
        t2.setToStreet(t1.getToStreet());
        NetWrapper.getDefault().sendAddMessage(Transport.ID, t2);
    }
}
