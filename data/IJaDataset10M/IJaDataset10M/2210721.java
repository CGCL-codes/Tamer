package net.sourceforge.mords.appt;

import net.sourceforge.mords.appt.obj.Appointment;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author  david
 */
public class ApptListPanel extends javax.swing.JPanel {

    private Vector<Appointment> appts;

    private Date start;

    private Date end;

    private int interval;

    private Vector<Appointment> listing;

    private Vector<ChangeListener> listeners;

    private AppointmentPanel ap;

    private Date selTime = new Date();

    private Vector<ItemListener> itemListeners;

    private int val;

    /** Creates new form ApptListPanel */
    public ApptListPanel() {
        initComponents();
        ap = new AppointmentPanel();
        add(ap);
        listeners = new Vector<ChangeListener>();
        itemListeners = new Vector<ItemListener>();
        list.setCellRenderer(new AppointmentListCellRenderer());
    }

    public void addItemListener(ItemListener itl) {
        itemListeners.add(itl);
    }

    public void setList(String title, Vector<Appointment> appts, Date start, Date end, int interval) {
        jScrollPane1.setBorder(BorderFactory.createTitledBorder(title));
        this.appts = appts;
        this.start = start;
        this.end = end;
        this.interval = interval;
        merge();
        list.setListData(listing);
    }

    public void setList(String title, Vector<Appointment> appts) {
        jScrollPane1.setBorder(BorderFactory.createTitledBorder(title));
        this.appts = appts;
        list.setListData(appts);
    }

    public Appointment getAppointment() {
        return (Appointment) list.getSelectedValue();
    }

    public Date getSelectedDateTime() {
        return selTime;
    }

    public Vector<Appointment> getMergedList() {
        return listing;
    }

    public void merge() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        listing = new Vector<Appointment>();
        while (cal.getTime().compareTo(end) <= 0) {
            Appointment a = new Appointment();
            a.setDateTime(cal.getTime());
            a.setDuration(interval);
            a.setStatus("OPEN");
            listing.add(a);
            cal.add(Calendar.MINUTE, interval);
        }
        System.out.println("Created a blank listing of " + listing.size() + " appointments.");
        listing.addAll(appts);
        Collections.sort(listing);
    }

    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    private void fireItemSelected() {
        ChangeEvent e = new ChangeEvent(this);
        Iterator<ChangeListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().stateChanged(e);
        }
        Appointment ap = (Appointment) list.getSelectedValue();
        selTime = ap.getDateTime();
    }

    private void fireDoubleClick() {
        Iterator<ItemListener> it = itemListeners.iterator();
        while (it.hasNext()) {
            it.next().itemStateChanged(null);
        }
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        setLayout(new java.awt.GridLayout());
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Appointment List"));
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(list);
        add(jScrollPane1);
    }

    private void listMouseClicked(java.awt.event.MouseEvent evt) {
        if (val == list.getSelectedIndex()) {
            fireDoubleClick();
        }
        val = list.getSelectedIndex();
        ap.setAppointment((Appointment) list.getSelectedValue());
        fireItemSelected();
    }

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JList list;
}
