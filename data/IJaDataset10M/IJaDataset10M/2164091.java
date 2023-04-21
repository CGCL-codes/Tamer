package de.miij.ui.comp;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

public class MVisualCalendar extends JDialog {

    private JPanel controlMonth = null;

    private JButton controlMonthFirst = null;

    private JButton controlMonthLast = null;

    private JButton controlMonthPrevious = null;

    private JButton controlMonthNext = null;

    private JLabel controlMonthLabel = null;

    private JPanel controlYear = null;

    private JButton controlYearFirst = null;

    private JButton controlYearLast = null;

    private JButton controlYearPrevious = null;

    private JButton controlYearNext = null;

    private JLabel controlYearLabel = null;

    private JPanel controlDays = null;

    private int date = 10;

    private int month = 9;

    private int year = 2005;

    private JTextField txtWhereToSetDate = null;

    private JFrame ParentWindow = null;

    private JLabel aktLabel = new JLabel();

    public MVisualCalendar(JTextField txtWhereToSetDate, JFrame parentWindow) {
        super(parentWindow);
        try {
            this.txtWhereToSetDate = txtWhereToSetDate;
            this.initialize();
            this.setActDate();
            this.setDays();
            this.ParentWindow = parentWindow;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.ParentWindow, ex.getMessage(), "Fehler bei der Initialisierung", JOptionPane.ERROR_MESSAGE);
        }
    }

    public MVisualCalendar(int year, int month) {
        try {
            this.month = month;
            this.year = year;
            this.initialize();
            this.setDays();
            this.setMonth(month - 1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.ParentWindow, ex.getMessage(), "Fehler bei der Initialisierung", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void initialize() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(300, 240);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setBackground(Color.decode("#EEEEEE"));
        this.setTitle("Visual Calendar");
        this.setModal(true);
        this.addYearPanel();
        this.addMonthPanel();
    }

    public void setMonth(int month) {
        if (month <= 0) this.month = 0; else if (month >= 11) this.month = 11; else this.month = month;
        if (this.month + 1 <= 9) this.controlMonthLabel.setText("0" + (this.month + 1)); else this.controlMonthLabel.setText("" + (this.month + 1));
        this.setDays();
    }

    public void setYear(int year) {
        if (year <= 1970) this.year = 1970; else this.year = year;
        this.controlYearLabel.setText("" + this.year);
        this.setDays();
    }

    /**
	 * Diese Methode f�gt dem VisualCalendar das ControlPanel zum Ausw�hlen des
	 * Jahres hinzu.
	 */
    public void addYearPanel() {
        this.controlYear = new JPanel();
        this.controlYear.setLayout(null);
        this.controlYearFirst = new JButton("<<");
        this.controlYearFirst.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MVisualCalendar.this.setYear((MVisualCalendar.this.year = MVisualCalendar.this.year - 10));
            }
        });
        this.controlYearFirst.setBounds(0, 0, 50, 25);
        this.controlYear.add(this.controlYearFirst);
        this.controlYearPrevious = new JButton("<");
        this.controlYearPrevious.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MVisualCalendar.this.setYear(--MVisualCalendar.this.year);
            }
        });
        this.controlYearPrevious.setBounds(60, 0, 50, 25);
        this.controlYear.add(this.controlYearPrevious);
        this.controlYearLabel = new JLabel("" + this.year);
        this.controlYearLabel.setBounds(120, 0, 40, 25);
        this.controlYearLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
        this.controlYear.add(this.controlYearLabel);
        this.controlYearNext = new JButton(">");
        this.controlYearNext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MVisualCalendar.this.setYear(++MVisualCalendar.this.year);
            }
        });
        this.controlYearNext.setBounds(165, 0, 50, 25);
        this.controlYear.add(this.controlYearNext);
        this.controlYearLast = new JButton(">>");
        this.controlYearLast.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MVisualCalendar.this.setYear((MVisualCalendar.this.year = MVisualCalendar.this.year + 10));
            }
        });
        this.controlYearLast.setBounds(225, 0, 50, 25);
        this.controlYear.add(this.controlYearLast);
        this.controlYear.setBounds(10, 10, 300, 25);
        this.getContentPane().add(controlYear);
    }

    /**
	 * Diese Methode f�gt dem VisualCalendar das ControlPanel zum Ausw�hlen des
	 * Monats hinzu.
	 */
    public void addMonthPanel() {
        this.controlMonth = new JPanel();
        this.controlMonth.setLayout(null);
        this.controlMonthFirst = new JButton("<<");
        this.controlMonthFirst.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MVisualCalendar.this.setMonth((MVisualCalendar.this.month = 0));
            }
        });
        this.controlMonthFirst.setBounds(0, 0, 50, 25);
        this.controlMonth.add(this.controlMonthFirst);
        this.controlMonthPrevious = new JButton("<");
        this.controlMonthPrevious.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MVisualCalendar.this.setMonth(--MVisualCalendar.this.month);
            }
        });
        this.controlMonthPrevious.setBounds(60, 0, 50, 25);
        this.controlMonth.add(this.controlMonthPrevious);
        this.controlMonthLabel = new JLabel("" + this.month);
        this.controlMonthLabel.setBounds(128, 0, 40, 25);
        this.controlMonthLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
        this.controlMonth.add(this.controlMonthLabel);
        this.controlMonthNext = new JButton(">");
        this.controlMonthNext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MVisualCalendar.this.setMonth(++MVisualCalendar.this.month);
            }
        });
        this.controlMonthNext.setBounds(165, 0, 50, 25);
        this.controlMonth.add(this.controlMonthNext);
        this.controlMonthLast = new JButton(">>");
        this.controlMonthLast.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MVisualCalendar.this.setMonth((MVisualCalendar.this.month = 11));
            }
        });
        this.controlMonthLast.setBounds(225, 0, 50, 25);
        this.controlMonth.add(this.controlMonthLast);
        this.controlMonth.setBounds(10, 35, 300, 25);
        this.getContentPane().add(controlMonth);
    }

    public void setDays() {
        try {
            this.controlDays.removeAll();
            this.remove(this.controlDays);
        } catch (Exception ex) {
        }
        this.controlDays = new JPanel();
        this.controlDays.setLayout(null);
        for (int i = 0; i < 7; i++) {
            JLabel label = this.getLabel(false);
            label.setForeground(Color.decode("#990000"));
            switch(i) {
                case 6:
                    label.setText("So");
                    break;
                case 0:
                    label.setText("Mo");
                    break;
                case 1:
                    label.setText("Di");
                    break;
                case 2:
                    label.setText("Mi");
                    break;
                case 3:
                    label.setText("Do");
                    break;
                case 4:
                    label.setText("Fr");
                    break;
                case 5:
                    label.setText("Sa");
                    break;
            }
            label.setBounds(30 * (i + 1), 0, 20, 20);
            this.controlDays.add(label);
        }
        GregorianCalendar cal = new GregorianCalendar(this.year, this.month, this.date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int weekOfYear = (this.month == 0) ? 1 : cal.get(Calendar.WEEK_OF_YEAR);
        int weekOfYear2 = cal.get(Calendar.WEEK_OF_YEAR);
        int addY = 0;
        for (int i = 1; this.month == cal.get(Calendar.MONTH); i++) {
            JLabel week = this.getLabel(false);
            week.setForeground(Color.decode("#990000"));
            week.setText("" + cal.get(Calendar.WEEK_OF_YEAR));
            week.setBounds(10, 20 * (cal.get(Calendar.WEEK_OF_YEAR) - weekOfYear) + 20 + addY, 20, 20);
            if (cal.get(Calendar.WEEK_OF_YEAR) < weekOfYear) {
                week.setBounds(10, 10 + 20 * (cal.get(Calendar.WEEK_OF_YEAR) + 52 - weekOfYear) + 20 + addY, 20, 20);
            }
            if (this.month == 0 && cal.get(Calendar.WEEK_OF_YEAR) > 50) {
                week.setBounds(10, 20 * (cal.get(Calendar.WEEK_OF_YEAR) - weekOfYear2) + 20, 20, 20);
                addY = 20;
            }
            this.controlDays.add(week);
            JLabel day = this.getLabel(true);
            GregorianCalendar c2 = new GregorianCalendar();
            if (i == c2.get(Calendar.DATE) && cal.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                day.setForeground(Color.BLUE);
            }
            day.setText("" + cal.get(Calendar.DATE));
            day.setBounds(30 * ((cal.get(Calendar.DAY_OF_WEEK) - 1 == 0) ? 7 : cal.get(Calendar.DAY_OF_WEEK) - 1), 20 * (cal.get(Calendar.WEEK_OF_YEAR) - weekOfYear) + 20 + addY, 20, 20);
            if (cal.get(Calendar.WEEK_OF_YEAR) < weekOfYear) {
                day.setBounds(30 * ((cal.get(Calendar.DAY_OF_WEEK) - 1 == 0) ? 7 : cal.get(Calendar.DAY_OF_WEEK) - 1), 20 * (cal.get(Calendar.WEEK_OF_YEAR) + 52 - weekOfYear) + 20 + addY, 20, 20);
            }
            if (this.month == 0 && cal.get(Calendar.WEEK_OF_YEAR) > 50) {
                day.setBounds(30 * ((cal.get(Calendar.DAY_OF_WEEK) - 1 == 0) ? 7 : cal.get(Calendar.DAY_OF_WEEK) - 1), 20 * (cal.get(Calendar.WEEK_OF_YEAR) - weekOfYear2) + 20, 20, 20);
            }
            this.controlDays.add(day);
            cal.add(Calendar.DATE, 1);
        }
        this.controlDays.setBounds(20, 60, 300, 300);
        this.getContentPane().add(this.controlDays);
        this.repaint();
    }

    private JLabel getLabel(boolean isDay) {
        JLabel label = new JLabel();
        label.setSize(20, 20);
        label.setHorizontalAlignment(JLabel.CENTER);
        if (isDay) {
            label.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    MVisualCalendar.this.aktLabel.setBorder(null);
                    MVisualCalendar.this.aktLabel = (JLabel) e.getSource();
                    MVisualCalendar.this.aktLabel.setBorder(new LineBorder(Color.BLACK, 1, true));
                    String actDate = ((JLabel) e.getComponent()).getText() + "." + (MVisualCalendar.this.month + 1) + "." + MVisualCalendar.this.year;
                    MVisualCalendar.this.txtWhereToSetDate.setText(actDate);
                    if (e.getClickCount() >= 2) {
                        MVisualCalendar.this.setVisible(false);
                    }
                }
            });
        }
        return label;
    }

    public void setActDate() {
        GregorianCalendar cal = new GregorianCalendar();
        this.date = cal.get(Calendar.DATE);
        this.month = cal.get(Calendar.MONTH);
        this.year = cal.get(Calendar.YEAR);
        this.setMonth(this.month);
        this.setYear(this.year);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new MVisualCalendar(2006, 10).setVisible(true);
    }
}
