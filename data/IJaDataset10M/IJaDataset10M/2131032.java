package de.internnetz.eaf.calendar.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.EventListenerList;
import com.toedter.calendar.JCalendar;
import de.internnetz.eaf.calendar.CalendarTool;
import de.internnetz.eaf.calendar.events.DateChangedEvent;
import de.internnetz.eaf.calendar.events.DateChangedListener;
import de.internnetz.eaf.calendar.i18n.Messages;
import de.internnetz.eaf.core.preferences.LanguagePreferences;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class DateChooser extends javax.swing.JFrame {

    private static final long serialVersionUID = -8724063121245713306L;

    /**
	 * Auto-generated main method to display this JFrame
	 */
    public static void main(String[] args) {
        DateChooser inst = new DateChooser();
        inst.setVisible(true);
    }

    private JCalendar jCalendar;

    private JPanel jPanelControls;

    private JButton jButtonAbbrechen;

    private JButton jButtonOk;

    private Locale locale;

    private EventListenerList dateListenerList;

    public DateChooser() {
        super();
        locale = LanguagePreferences.getLanguage().locale();
        initGUI();
        dateListenerList = new EventListenerList();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setTitle(Messages.getString("DateChooser.0"));
            {
                jPanelControls = new JPanel();
                getContentPane().add(jPanelControls, BorderLayout.SOUTH);
                FlowLayout jPanelControlsLayout = new FlowLayout();
                jPanelControlsLayout.setAlignment(FlowLayout.RIGHT);
                jPanelControls.setLayout(jPanelControlsLayout);
                {
                    jButtonOk = new JButton();
                    jPanelControls.add(jButtonOk);
                    jButtonOk.setText(Messages.getString("DateChooser.1"));
                    jButtonOk.setFont(new java.awt.Font("Arial", 0, 12));
                    jButtonOk.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            jButtonOkActionPerformed(evt);
                        }
                    });
                }
                {
                    jButtonAbbrechen = new JButton();
                    jPanelControls.add(jButtonAbbrechen);
                    jButtonAbbrechen.setText(Messages.getString("DateChooser.3"));
                    jButtonAbbrechen.setFont(new java.awt.Font("Arial", 0, 12));
                    jButtonAbbrechen.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            jButtonAbbrechenActionPerformed(evt);
                        }
                    });
                }
            }
            {
                jCalendar = new JCalendar(locale);
                getContentPane().add(jCalendar, BorderLayout.CENTER);
                jCalendar.setBorder(BorderFactory.createEmptyBorder(5, 1, 1, 1));
            }
            pack();
            this.setSize(287, 255);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jButtonAbbrechenActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

    private void jButtonOkActionPerformed(ActionEvent evt) {
        fireDateChangedEvent(new DateChangedEvent(this, getSelectedDate()));
        setVisible(false);
    }

    public void setVisible(boolean isVisible) {
        if (isVisible) {
            jCalendar.setDate(CalendarTool.getDate());
        }
        super.setVisible(isVisible);
    }

    public Date getSelectedDate() {
        return jCalendar.getDate();
    }

    private void fireDateChangedEvent(DateChangedEvent evt) {
        Object[] listeners = dateListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == DateChangedListener.class) {
                ((DateChangedListener) listeners[i + 1]).DateChanged(evt);
            }
        }
    }

    public void registerDateChangedListener(DateChangedListener listener) {
        dateListenerList.add(DateChangedListener.class, listener);
    }

    public void setDate(Date d) {
        if (d != null) {
            jCalendar.setDate(d);
            fireDateChangedEvent(new DateChangedEvent(this, d));
        }
    }
}
