package org.rapla.gui.internal.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.rapla.entities.User;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Repeating;
import org.rapla.entities.domain.Reservation;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;

public class ReservationInfoUI extends ClassificationInfoUI {

    public ReservationInfoUI(RaplaContext sm) throws RaplaException {
        super(sm);
    }

    private void addRestriction(Reservation reservation, Allocatable allocatable, StringBuffer buf) {
        Appointment[] appointments = reservation.getRestriction(allocatable);
        if (appointments.length == 0) return;
        buf.append("<small>");
        buf.append(" (");
        for (int i = 0; i < appointments.length; i++) {
            if (i > 0) buf.append(", ");
            encode(getAppointmentFormater().getShortSummary(appointments[i]), buf);
        }
        buf.append(")");
        buf.append("</small>");
    }

    private String allocatableList(Reservation reservation, Allocatable[] allocatables, User user, LinkController controller) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < allocatables.length; i++) {
            Allocatable allocatable = allocatables[i];
            if (user != null && !allocatable.canRead(user)) continue;
            if (controller != null) controller.createLink(allocatable, getName(allocatable), buf); else encode(getName(allocatable), buf);
            addRestriction(reservation, allocatable, buf);
            if (i < allocatables.length - 1) {
                buf.append(",");
            }
        }
        return buf.toString();
    }

    protected String getTooltip(Object object) {
        Reservation reservation = (Reservation) object;
        StringBuffer buf = new StringBuffer();
        insertModificationRow(reservation, buf);
        insertClassificationTitle(reservation, buf);
        createTable(getAttributes(reservation, null, null, true), buf, false);
        return buf.toString();
    }

    protected String createHTMLAndFillLinks(Object object, LinkController controller) {
        Reservation reservation = (Reservation) object;
        StringBuffer buf = new StringBuffer();
        insertModificationRow(reservation, buf);
        insertClassificationTitle(reservation, buf);
        createTable(getAttributes(reservation, controller, null, false), buf, false);
        this.insertAllAppointments(reservation, buf);
        return buf.toString();
    }

    public List getAttributes(Reservation reservation, LinkController controller, User user, boolean excludeAdditionalInfos) {
        ArrayList att = new ArrayList();
        att.addAll(getClassificationAttributes(reservation, excludeAdditionalInfos));
        User owner = reservation.getOwner();
        final Locale locale = getLocale();
        final String ownerName = owner.getName(locale);
        String ownerText = encode(ownerName);
        if (controller != null) ownerText = controller.createLink(owner, ownerName);
        att.add(new Row(getString("reservation.owner"), ownerText));
        User lastChangeBy = reservation.getLastChangedBy();
        if (lastChangeBy != null && (owner == null || !lastChangeBy.equals(owner))) {
            final String lastChangedName = lastChangeBy.getName(locale);
            String lastChangeByText = encode(lastChangedName);
            if (controller != null) lastChangeByText = controller.createLink(lastChangeBy, lastChangedName);
            att.add(new Row(getString("last_changed_by"), lastChangeByText));
        }
        Allocatable[] resources = reservation.getResources();
        String resourceList = allocatableList(reservation, resources, user, controller);
        if (resourceList.length() > 0) {
            att.add(new Row(getString("resources"), resourceList));
        }
        Allocatable[] persons = reservation.getPersons();
        String personList = allocatableList(reservation, persons, user, controller);
        if (personList.length() > 0) {
            att.add(new Row(getString("persons"), personList));
        }
        return att;
    }

    void insertAllAppointments(Reservation reservation, StringBuffer buf) {
        buf.append("<table cellpadding=\"2\">");
        buf.append("<tr>\n");
        buf.append("<td colspan=\"2\" class=\"label\">");
        String appointmentLabel = getString("appointments");
        encode(appointmentLabel, buf);
        buf.append(":");
        buf.append("</td>\n");
        buf.append("</tr>\n");
        Appointment[] appointments = reservation.getAppointments();
        for (int i = 0; i < appointments.length; i++) {
            buf.append("<tr>\n");
            buf.append("<td valign=\"top\">\n");
            if (appointments[i].getRepeating() != null) {
                buf.append("<img width=\"16\" height=\"16\" src=\"org/rapla/gui/images/repeating.png\">");
            } else {
                buf.append("<img width=\"16\" height=\"16\" src=\"org/rapla/gui/images/single.png\">");
            }
            buf.append("</td>\n");
            buf.append("<td>\n");
            String appointmentSummary = getAppointmentFormater().getSummary(appointments[i]);
            encode(appointmentSummary, buf);
            Repeating repeating = appointments[i].getRepeating();
            if (repeating != null) {
                buf.append("<br>");
                buf.append("<small>");
                List periods = getPeriodModel().getPeriodsFor(appointments[i].getStart());
                String repeatingSummary = getAppointmentFormater().getSummary(repeating, periods);
                encode(repeatingSummary, buf);
                if (repeating.hasExceptions()) {
                    buf.append("<br>");
                    buf.append(getAppointmentFormater().getExceptionSummary(repeating));
                }
                buf.append("</small>");
            }
            buf.append("</td>\n");
            buf.append("<td></td>");
            buf.append("</tr>\n");
        }
        buf.append("</table>\n");
    }
}
