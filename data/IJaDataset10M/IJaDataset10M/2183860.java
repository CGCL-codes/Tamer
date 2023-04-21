package org.plenaquest.client.ihm.util;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import com.toedter.calendar.JDateChooser;

public class DateChooser extends JPanel {

    private static final long serialVersionUID = 6718571348329729027L;

    private JDateChooser chooser = new JDateChooser();

    private JLabel labelHour = new JLabel("Heure");

    private JComboBox boxHour = new JComboBox();

    private JLabel labelMinuts = new JLabel("Minutes");

    private JComboBox boxMinuts = new JComboBox();

    public DateChooser() {
        super();
        for (int i = 0; i <= 23; i++) boxHour.addItem(Integer.toString(i));
        for (int i = 0; i < 60; i++) {
            boxMinuts.addItem(Integer.toString(i));
        }
        chooser.setDateFormatString("dd/MM/yy");
        this.add(chooser);
        this.add(labelHour);
        this.add(boxHour);
        this.add(boxMinuts);
        this.add(labelMinuts);
    }

    public void setMinSelectableDate(Date date) {
        chooser.setMinSelectableDate(date);
    }

    public void setDate(Date date) {
        if (date != null) {
            chooser.setDate(date);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            boxHour.setSelectedIndex(c.get(Calendar.HOUR_OF_DAY));
            boxMinuts.setSelectedIndex(c.get(Calendar.MINUTE));
        }
    }

    public Date getDate() {
        Date res = chooser.getDate();
        Calendar c = Calendar.getInstance();
        int hour = (Integer) Integer.parseInt((String) boxHour.getSelectedItem());
        int minuts = (Integer) Integer.parseInt((String) boxMinuts.getSelectedItem());
        c.setTime(res);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minuts);
        return new Date(c.getTimeInMillis());
    }

    public void setEnabled(boolean enabled) {
        chooser.setEnabled(enabled);
        boxHour.setEnabled(enabled);
        boxMinuts.setEnabled(enabled);
    }

    public static void main(String[] args) {
        JFrame test = new JFrame();
        final DateChooser dc = new DateChooser();
        JButton bouton = new JButton("ok");
        bouton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println(dc.getDate());
            }
        });
        test.getContentPane().setLayout(new FlowLayout());
        test.getContentPane().add(dc);
        test.getContentPane().add(bouton);
        test.pack();
        test.setVisible(true);
        test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
