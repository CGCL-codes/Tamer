package ie.omk.jest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.*;
import ie.omk.smpp.message.*;
import ie.omk.debug.Debug;

public class DateDialog extends javax.swing.JDialog implements java.awt.event.MouseListener {

    JFrame fparent;

    SimpleDateFormat timeForm = new SimpleDateFormat("HH:mm:ss:S");

    SimpleDateFormat dateForm = new SimpleDateFormat("dd/MM/yyyy");

    JButton ok;

    JLabel dLabel, tLabel;

    JTextField f_date;

    JTextField f_time;

    public DateDialog(JFrame fparent) {
        this(fparent, new Date());
    }

    public DateDialog(JFrame fparent, Date d) {
        super(fparent);
        if (fparent == null) throw new NullPointerException();
        this.fparent = fparent;
        setTitle("Date Dialog");
        if (d == null) d = new Date();
        f_date = new JTextField();
        f_time = new JTextField();
        dLabel = new JLabel("Date");
        tLabel = new JLabel("Time");
        f_date.setText(dateForm.format(d));
        f_time.setText(timeForm.format(d));
        ok = new JButton("Ok");
        ok.addMouseListener(this);
        JPanel p1 = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc1 = new GridBagConstraints();
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc1.fill = gbc1.HORIZONTAL;
        gbc1.weightx = 0.0;
        gbc1.gridwidth = gbc1.RELATIVE;
        gbc2.fill = gbc2.HORIZONTAL;
        gbc2.weightx = 1.0;
        gbc2.gridwidth = gbc2.REMAINDER;
        gb.setConstraints(dLabel, gbc1);
        gb.setConstraints(f_date, gbc2);
        gb.setConstraints(tLabel, gbc1);
        gb.setConstraints(f_time, gbc2);
        p1.setLayout(gb);
        p1.add(tLabel);
        p1.add(f_time);
        p1.add(dLabel);
        p1.add(f_date);
        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add("South", ok);
        Container cpane = getContentPane();
        cpane.setLayout(new BorderLayout());
        cpane.add("Center", p1);
        cpane.add("East", p2);
        f_time.requestFocus();
        f_time.selectAll();
    }

    /** Get the java.util.Date that the current settings reflect.
	  * @return The Date representation of the text fields, or null if there
	  * is an error in the text form.
	  */
    public Date getDate() {
        SimpleDateFormat allForm = new SimpleDateFormat("HH:mm:ss:S dd/MM/yyyy");
        String s = new String(f_time.getText() + " " + f_date.getText());
        ParsePosition pos = new ParsePosition(0);
        return (allForm.parse(s, pos));
    }

    public void setVisible(boolean b) {
        Cursor c;
        if (b) {
            c = new Cursor(Cursor.WAIT_CURSOR);
        } else {
            c = new Cursor(Cursor.DEFAULT_CURSOR);
        }
        fparent.setCursor(c);
        super.setVisible(b);
    }

    public void mouseClicked(MouseEvent e) {
        this.setVisible(false);
    }

    public void mousePressed(MouseEvent e) {
        return;
    }

    public void mouseReleased(MouseEvent e) {
        return;
    }

    public void mouseEntered(MouseEvent e) {
        return;
    }

    public void mouseExited(MouseEvent e) {
        return;
    }
}
