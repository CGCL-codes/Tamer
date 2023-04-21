package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import at.rc.tacos.model.Transport;

public class TransportViewFilter extends ViewerFilter {

    private String trNr;

    private String from;

    private String patient;

    private String to;

    private String location;

    private String priority;

    private String vehicle;

    private String disease;

    /**
	 * Default class constructor for the address filter.
	 * 
	 * @param value
	 *            the street or the city to filter
	 */
    public TransportViewFilter(String trNr, String from, String patient, String to, String location, String priority, String vehicle, String disease) {
        this.trNr = trNr;
        this.from = from;
        this.patient = patient;
        this.to = to;
        this.location = location;
        this.priority = priority;
        this.vehicle = vehicle;
        this.disease = disease;
    }

    /**
	 * Returns whether or not the object should be filtered or not.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param parentElement
	 *            the parent element
	 * @param element
	 *            the element to check
	 */
    @Override
    public boolean select(Viewer arg0, Object parentElement, Object element) {
        Transport transport = (Transport) element;
        if (trNr != null & !trNr.trim().isEmpty()) {
            if (!String.valueOf(transport.getTransportNumber()).toLowerCase().contains(trNr.toLowerCase()) & !String.valueOf(transport.getTransportNumber()).toLowerCase().startsWith(trNr.toLowerCase())) return false;
        }
        if (from != null & !from.trim().isEmpty()) {
            if (!transport.getFromCity().toLowerCase().contains(from.toLowerCase()) & !transport.getFromCity().toLowerCase().startsWith(from.toLowerCase()) & !transport.getFromStreet().toLowerCase().contains(from.toLowerCase()) & !transport.getFromStreet().toLowerCase().startsWith(from.toLowerCase())) return false;
        }
        if (patient != null & !patient.trim().isEmpty()) {
            if (!transport.getPatient().getLastname().toLowerCase().contains(patient.toLowerCase()) & !transport.getPatient().getLastname().toLowerCase().startsWith(patient.toLowerCase()) & !transport.getPatient().getFirstname().toLowerCase().contains(patient.toLowerCase()) & !transport.getPatient().getFirstname().toLowerCase().startsWith(patient.toLowerCase())) return false;
        }
        if (to != null & !to.trim().isEmpty()) {
            if (!transport.getToStreet().toLowerCase().contains(to.toLowerCase()) & !transport.getToStreet().toLowerCase().startsWith(to.toLowerCase()) & !transport.getToCity().toLowerCase().contains(to.toLowerCase()) & !transport.getToCity().toLowerCase().startsWith(to.toLowerCase())) return false;
        }
        if (location != null & !location.trim().isEmpty()) {
            if (transport.getVehicleDetail() != null) {
                if (transport.getVehicleDetail().getCurrentStation() != null) {
                    String locationToCheck = this.getLocationToCheck(transport.getVehicleDetail().getCurrentStation().getLocationName());
                    if (!locationToCheck.toLowerCase().contains(location.toLowerCase()) & !locationToCheck.toLowerCase().startsWith(location.toLowerCase())) return false;
                }
            }
        }
        if (priority != null & !priority.trim().isEmpty()) {
            String priorityToCheck = this.getPriorityToCheck(transport.getTransportPriority());
            if (!priorityToCheck.toLowerCase().contains(priority.toLowerCase()) & !priorityToCheck.toLowerCase().startsWith(priority.toLowerCase())) return false;
        }
        if (vehicle != null & !vehicle.trim().isEmpty()) {
            if (transport.getVehicleDetail() != null) {
                if (!transport.getVehicleDetail().getVehicleName().toLowerCase().contains(vehicle.toLowerCase()) & !transport.getVehicleDetail().getVehicleName().toLowerCase().startsWith(vehicle.toLowerCase())) return false;
            }
        }
        if (disease != null & !disease.trim().isEmpty()) {
            if (transport.getKindOfIllness() != null) {
                if (!transport.getKindOfIllness().getDiseaseName().toLowerCase().contains(disease.toLowerCase()) & !transport.getKindOfIllness().getDiseaseName().toLowerCase().startsWith(disease.toLowerCase())) return false;
            }
        }
        return true;
    }

    private String getLocationToCheck(String location) {
        if (location.equalsIgnoreCase("Kapfenberg")) return "KA"; else if (location.equalsIgnoreCase("Bruck an der Mur")) return "BM"; else if (location.equalsIgnoreCase("St. Marein")) return "MA"; else if (location.equalsIgnoreCase("Breitenau")) return "BR"; else if (location.equalsIgnoreCase("Th�rl") || location.equalsIgnoreCase("Th?rl")) return "TH"; else if (location.equalsIgnoreCase("Turnau")) return "TU"; else if (location.equalsIgnoreCase("Bezirk: Bruck - Kapfenberg")) return "BE";
        return "BE";
    }

    private String getPriorityToCheck(String priority) {
        if (priority.equalsIgnoreCase("A")) return "1"; else if (priority.equalsIgnoreCase("B")) return "2"; else if (priority.equalsIgnoreCase("C")) return "3"; else if (priority.equalsIgnoreCase("D")) return "4"; else if (priority.equalsIgnoreCase("E")) return "5"; else if (priority.equalsIgnoreCase("F")) return "6"; else if (priority.equalsIgnoreCase("G")) return "7";
        return "3";
    }
}
