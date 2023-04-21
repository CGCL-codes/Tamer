package edu.upmc.opi.caBIG.caTIES.client.vr.desktop.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Aesthetics;
import edu.upmc.opi.caBIG.caTIES.client.vr.utils.actions.HoverBorderAction;
import edu.upmc.opi.caBIG.caTIES.client.vr.utils.widgets.DateField;

/**
 * Date Selector Task
 */
public class CaTIES_DateTask extends JPanel {

    /**
     * The listeners.
     */
    private Vector listeners = new Vector();

    /**
     * Adds the change listener.
     * 
     * @param listener the listener
     */
    public void addChangeListener(ChangeListener listener) {
        if (listeners.contains(listener)) return;
        listeners.addElement(listener);
    }

    /**
     * Removes the change listener.
     * 
     * @param listener the listener
     */
    public void removeChangeListener(ChangeListener listener) {
        listeners.removeElement(listener);
    }

    /**
     * Fire change event.
     */
    private void fireChangeEvent() {
        Vector vtemp = (Vector) listeners.clone();
        for (int x = 0; x < vtemp.size(); x++) {
            ChangeListener target = (ChangeListener) vtemp.elementAt(x);
            target.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * The date.
     */
    public DateField date = new DateField();

    /**
     * The neverexpires.
     */
    protected JCheckBox neverexpires = new JCheckBox("Never Expires");

    /**
     * The Constant DATEFORMAT.
     */
    public static final SimpleDateFormat YEARFORMAT = new SimpleDateFormat("yyyy");

    public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("MM / dd / yyyy");

    public static final SimpleDateFormat DATETIMEFORMAT = new SimpleDateFormat("MM / dd / yyyy hh:mm aa");

    /**
     * The mesg.
     */
    private JLabel mesg = new JLabel("");

    /**
     * The Constructor.
     */
    public CaTIES_DateTask() {
        date.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                Date d = date.getValueAsDate();
                if (d != null) updateMessage(d);
                fireChangeEvent();
            }
        });
        neverexpires.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (neverexpires.isSelected()) {
                    date.setVisible(false);
                    mesg.setVisible(false);
                } else {
                    if (date.getValueAsDate() == null) date.setValue(new Date());
                    updateMessage(date.getValueAsDate());
                    date.setVisible(true);
                    mesg.setVisible(true);
                }
                fireChangeEvent();
            }
        });
        initGUI();
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public Date getDate() {
        if (neverexpires.isSelected()) return null; else {
            return date.getValueAsDate();
        }
    }

    /**
     * Sets the date.
     * 
     * @param d the d
     */
    public void setDate(Date d) {
        if (d == null) {
            date.setVisible(false);
            mesg.setVisible(false);
            neverexpires.setSelected(true);
        } else {
            neverexpires.setSelected(false);
            date.setVisible(true);
            mesg.setVisible(true);
        }
        date.setValue(d);
        updateMessage(d);
    }

    /**
     * Never expires.
     * 
     * @return true, if never expires
     */
    public boolean neverExpires() {
        return neverexpires.isSelected();
    }

    /**
     * Update message.
     * 
     * @param d the d
     */
    private void updateMessage(Date d) {
        if (d == null) {
            mesg.setText("invalid date");
            return;
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(d);
        GregorianCalendar today = new GregorianCalendar();
        if (today.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
            if (today.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                if (today.get(Calendar.DATE) == c.get(Calendar.DATE)) {
                    mesg.setText("expires today");
                    return;
                } else if (c.get(Calendar.DATE) - today.get(Calendar.DATE) == 1) {
                    mesg.setText("expires tommorow");
                    return;
                } else if (today.get(Calendar.DATE) > c.get(Calendar.DATE)) {
                    mesg.setText("expired");
                    return;
                }
            } else if (today.get(Calendar.MONTH) > c.get(Calendar.MONTH)) {
                mesg.setText("expired");
                return;
            }
        } else if (today.get(Calendar.YEAR) > c.get(Calendar.YEAR)) {
            mesg.setText("expired");
            return;
        }
        mesg.setText("");
    }

    /**
     * Inits the GUI.
     */
    protected void initGUI() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 0);
        p.add(neverexpires, c);
        p.add(date, c);
        p.add(mesg, c);
        mesg.setForeground(Color.red);
        date.getButton().setIcon(CaTIES_Aesthetics.ICON_CALENDAR);
        date.getButton().setFocusPainted(false);
        date.getButton().setOpaque(false);
        date.getButton().setBorder(BorderFactory.createEmptyBorder());
        new HoverBorderAction(date.getButton());
        date.setTextBoxSize(75, (new JComboBox()).getPreferredSize().height);
        this.setLayout(new BorderLayout());
        this.add(p, BorderLayout.WEST);
        p.setOpaque(false);
        neverexpires.setOpaque(false);
        this.setOpaque(false);
    }
}
