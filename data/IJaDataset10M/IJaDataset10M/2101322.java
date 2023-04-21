package masc.Model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import mas.common.Meeting;
import mas.common.MeetingDocument;
import mas.common.MeetingLogic;
import masc.Controller.MeetingChangeListener;

/**
 * Die Datenhaltung auf Clientseite
 * @author hypersquirrel
 */
public class MeetingModel implements ListModel, ListSelectionListener {

    private Vector<ListDataListener> datalisteners = new Vector<ListDataListener>();

    private Vector<MeetingChangeListener> meetinglisteners = new Vector<MeetingChangeListener>();

    private MeetingLogic meetinglogic;

    private Date from;

    private Date to;

    /**
	 * @param meeting
	 * @see mas.common.MeetingLogic#createMeeting(mas.common.Meeting)
	 */
    public void createMeeting(Meeting meeting) {
        meetinglogic.createMeeting(meeting);
        update();
    }

    /**
	 * Ändert das aktuelle Meeting
	 * @param newMeeting Meeting mit den neuen Eigenschaften
	 */
    public void changeMeeting(Meeting newMeeting) {
        Meeting oldMeeting = this.getCurrentMeeting();
        meetinglogic.setLabel(oldMeeting.getId(), newMeeting.getLabel());
        meetinglogic.setDescription(oldMeeting.getId(), newMeeting.getDescription());
        meetinglogic.setLocation(oldMeeting.getId(), newMeeting.getLocation());
        meetinglogic.setDate(oldMeeting.getId(), newMeeting.getDate());
        meetinglogic.removeMembers(oldMeeting.getId(), oldMeeting.getUser());
        meetinglogic.addMembers(oldMeeting.getId(), newMeeting.getUser());
        meetinglogic.setModerator(oldMeeting.getId(), newMeeting.getModerator().getUserName());
        meetinglogic.setRecorder(oldMeeting.getId(), newMeeting.getRecorder().getUserName());
        meetings.set(selectedIndex, newMeeting);
        fireContentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, selectedIndex, selectedIndex));
        fireMeetingChanged();
    }

    private Vector<Meeting> meetings;

    private int selectedIndex;

    /**
	 * Erzeugt ein neues MeetingModel
	 * @param meetinglogic
	 */
    public MeetingModel(MeetingLogic meetinglogic) {
        this.meetinglogic = meetinglogic;
    }

    /**
	 * DataListener Interface
	 */
    @Override
    public void addListDataListener(ListDataListener l) {
        datalisteners.add(l);
    }

    /**
	 * Registriert MeetingChangeListener
	 * @param l MeetingChangeListener
	 */
    public void addMeetingChangeListener(MeetingChangeListener l) {
        meetinglisteners.add(l);
    }

    /**
	 * Informiert alle Listener über Änderungen
	 * @param lde
	 */
    private void fireContentsChanged(ListDataEvent lde) {
        for (ListDataListener listener : datalisteners) listener.contentsChanged(lde);
    }

    /**
	 * Informiert alle Listener über Änderungen
	 */
    private void fireMeetingChanged() {
        for (MeetingChangeListener listener : meetinglisteners) listener.meetingChanged();
    }

    /**
	 * 
	 * @return das aktuell ausgewählte Meeting
	 */
    public Meeting getCurrentMeeting() {
        return meetings.elementAt(selectedIndex);
    }

    /**
	 * Holt zum aktuell ausgewählten Meeting das zugehörige MeetingDocument
	 * @return MeetingDocument des aktuellen Termines
	 */
    public MeetingDocument getCurrentMeetingDocument() {
        return meetinglogic.getMeetingDoc(this.getCurrentMeeting().getId());
    }

    /**
	 * Speichert ein MeetingDocument in der Datenbank
	 * @param doc das zu speichernde MeetingDocument
	 */
    public void saveDocument(MeetingDocument doc) {
        meetinglogic.setMeetingDoc(doc.getId(), doc);
    }

    /**
	 * Ist der/die aktuelle BenutzerIn ProtokollantIn?
	 * @return ja oder nein
	 */
    public boolean isRecorder() {
        return meetinglogic.isRecorder(this.getCurrentMeeting().getId());
    }

    /**
	 * Ist der/die aktuelle BenutzerIn ModeratorIn?
	 * @return ja oder nein
	 */
    public boolean isModerator() {
        return meetinglogic.isModerator(this.getCurrentMeeting().getId());
    }

    /**
	 * ListModel Interface
	 */
    @Override
    public Object getElementAt(int index) {
        Date d = meetings.elementAt(index).getDate();
        DateFormat dateformat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        return dateformat.format(d) + " - " + meetings.elementAt(index).getLabel();
    }

    /**
	 * ListModel Interface
	 */
    @Override
    public int getSize() {
        return meetings.size();
    }

    /**
	 * DataListener Interface
	 */
    @Override
    public void removeListDataListener(ListDataListener l) {
        datalisteners.remove(l);
    }

    /**
	 * Löscht einen MeetingChangeListener
	 * @param l der zu löschende Listener
	 */
    public void removeMeetingChangeListener(MeetingChangeListener l) {
        meetinglisteners.remove(l);
    }

    /**
	 * Setzt das Datums-Interval der anzuzeigenden Meetings
	 * @param from Anfangsdatum
	 * @param to Enddatum
	 */
    public void setDateInterval(Date from, Date to) {
        this.from = from;
        this.to = to;
        meetings = meetinglogic.getMeetings(new Timestamp(from.getTime()), new Timestamp(to.getTime()));
        Collections.sort(meetings, new Comparator<Meeting>() {

            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        fireContentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
    }

    public void update() {
        setDateInterval(from, to);
    }

    /**
	 * ListModel Interface
	 */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() instanceof JList) {
            selectedIndex = ((JList) e.getSource()).getSelectedIndex();
            fireMeetingChanged();
        }
    }
}
