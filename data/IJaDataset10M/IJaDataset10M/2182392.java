package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
	 * This is the updated TimePanel class. The old class used pulldowns for date and time selection, 
	 * which was not very easy, along with being incorporated into AggregateWindow.
	 * Instead, it now uses a JTextField that updates on every change within it. The parsing style 
	 * is configurable through radio buttons, and the TimePanel adopts whatever style is currently selected
	 * upon creation.
	 * This class now represents a refactoring out of AggregateWindow such that both Aggregate and Query can
	 * share the same user interface elements. Be sure any changes here work in both menus.
	 * 
	 * @author rzeszotj
	 *
	 */
public class TimePanel extends JPanel implements DocumentListener {

    private boolean isInterval;

    private SimpleDateFormat NADate_1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private SimpleDateFormat NADate_2 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    private SimpleDateFormat EDate_1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private SimpleDateFormat EDate_2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private SimpleDateFormat Hours_1 = new SimpleDateFormat("HH:mm:ss");

    private JRadioButton eurDateRadio, naDateRadio;

    private Date currentTime;

    private JTextField timeField;

    private ParsePosition p = new ParsePosition(0);

    private final Color ERROR = new Color(255, 128, 128);

    private final Color GOOD = Color.WHITE;

    public TimePanel(String name, Calendar init, boolean interval, JRadioButton NA, JRadioButton EU) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
        isInterval = interval;
        eurDateRadio = EU;
        naDateRadio = NA;
        JLabel label = new JLabel(name);
        if (isInterval) {
            timeField = new JTextField(getTimeString(init), 9);
        } else {
            timeField = new JTextField(getTimeString(init), 19);
        }
        timeField.getDocument().addDocumentListener(this);
        label.setPreferredSize(new Dimension(70, 20));
        add(label);
        add(timeField);
        updateDate();
    }

    /**
		 * Formats a calendar into a string using whatever style is selected
		 * @param c The calendar to format
		 */
    private String getTimeString(Calendar c) {
        String s = "";
        Date cur = c.getTime();
        if (isInterval) s = Hours_1.format(cur); else if (naDateRadio.isSelected()) {
            s = NADate_1.format(cur);
        } else if (eurDateRadio.isSelected()) {
            s = EDate_1.format(cur);
        }
        return s;
    }

    /**
		 * Formats the current entry into a string using whatever style is selected
		 */
    public String getTimeString() {
        updateDate();
        return NADate_1.format(currentTime);
    }

    /**
		 * Updates currentTime with text in timeField, changing background
		 */
    private void updateDate() {
        String cur = null;
        try {
            cur = timeField.getText();
        } catch (NullPointerException e) {
            timeField.setBackground(ERROR);
            currentTime = null;
            return;
        }
        p.setIndex(0);
        if (cur == null) {
            timeField.setBackground(ERROR);
            return;
        }
        if (isInterval) {
            currentTime = Hours_1.parse(cur, p);
        } else if (naDateRadio.isSelected()) {
            currentTime = NADate_1.parse(cur, p);
            if (currentTime == null) {
                p.setIndex(0);
                currentTime = NADate_2.parse(cur, p);
            }
        } else if (eurDateRadio.isSelected()) {
            currentTime = EDate_1.parse(cur, p);
            if (currentTime == null) {
                p.setIndex(0);
                currentTime = EDate_2.parse(cur, p);
            }
        } else timeField.setBackground(ERROR);
        if (currentTime == null) {
            timeField.setBackground(ERROR);
        } else {
            timeField.setBackground(GOOD);
        }
    }

    /**
		 * Returns true if an unparsable entry is present
		 */
    public boolean isBad() {
        if (currentTime == null) return true; else return false;
    }

    /**
		 * Returns a calendar object based on the string in textField
		 */
    public GregorianCalendar getDate() {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(currentTime);
        return c;
    }

    public void insertUpdate(DocumentEvent ev) {
        updateDate();
    }

    public void removeUpdate(DocumentEvent ev) {
        updateDate();
    }

    public void changedUpdate(DocumentEvent ev) {
    }
}
