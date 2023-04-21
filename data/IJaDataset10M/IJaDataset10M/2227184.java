package br.org.databasetools.core.view.fields;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import br.org.databasetools.core.images.ColorManager;
import br.org.databasetools.core.images.FontManager;

public class TCalendarPopup extends JPopupMenu {

    private static final long serialVersionUID = 1L;

    protected Color selectedBackground;

    protected Color selectedBorder;

    protected Color background;

    protected Color foreground;

    protected JLabel monthLabel;

    protected SimpleDateFormat dateFormat;

    protected JPanel days = null;

    protected SimpleDateFormat monthFormat = new SimpleDateFormat("MMM   yyyy");

    protected Calendar setupCalendar = Calendar.getInstance();

    protected Calendar calendar = Calendar.getInstance();

    private TDateInternalField field;

    public TCalendarPopup(TDateInternalField field) {
        super();
        this.field = field;
        background = ColorManager.getInstance().getCalendarBackground();
        selectedBackground = ColorManager.getInstance().getCalendarSelectedBackground();
        selectedBorder = ColorManager.getInstance().getCalendarSelectedBorder();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        initializePopup();
        updatePopup();
    }

    public TCalendarPopup(TDateInternalField field, Date date) {
        this(field);
        setupCalendar.setTime(date);
        calendar.setTime(date);
        updatePopup();
    }

    protected void initializePopup() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(background);
        header.setOpaque(true);
        JLabel label;
        label = createUpdateButton(Calendar.YEAR, -1);
        label.setIcon(new ImageIcon(this.getClass().getResource("/images/16/previous_year.gif")));
        label.setPreferredSize(new Dimension(15, 10));
        label.setToolTipText("Ano Anterior");
        header.add(Box.createHorizontalStrut(1));
        header.add(label);
        header.add(Box.createHorizontalStrut(1));
        label = createUpdateButton(Calendar.MONTH, -1);
        label.setIcon(new ImageIcon(this.getClass().getResource("/images/16/previous.gif")));
        label.setPreferredSize(new Dimension(15, 10));
        label.setToolTipText("Mes Anterior");
        header.add(label);
        monthLabel = new JLabel("", JLabel.CENTER);
        monthLabel.setForeground(foreground);
        monthLabel.setFont(FontManager.getCalendarFontToday());
        monthLabel.setPreferredSize(new Dimension(65, 1));
        header.add(Box.createHorizontalGlue());
        header.add(monthLabel);
        header.add(Box.createHorizontalGlue());
        label = createUpdateButton(Calendar.MONTH, 1);
        label.setIcon(new ImageIcon(this.getClass().getResource("/images/16/next.gif")));
        label.setPreferredSize(new Dimension(15, 10));
        label.setToolTipText("Proximo Mes");
        header.add(label);
        label = createUpdateButton(Calendar.YEAR, 1);
        label.setIcon(new ImageIcon(this.getClass().getResource("/images/16/next_year.gif")));
        label.setPreferredSize(new Dimension(15, 10));
        label.setToolTipText("Proximo Ano");
        header.add(Box.createHorizontalStrut(1));
        header.add(label);
        header.add(Box.createHorizontalStrut(1));
        this.setBorder(BorderFactory.createLineBorder(selectedBorder));
        this.setLayout(new BorderLayout());
        JPanel monthPanel = new JPanel();
        monthPanel.setBackground(background);
        monthPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, selectedBorder));
        monthPanel.setPreferredSize(new Dimension(150, 20));
        monthPanel.add(header);
        this.add(BorderLayout.NORTH, monthPanel);
    }

    protected void updatePopup() {
        monthLabel.setText((monthFormat.format(calendar.getTime())).toUpperCase());
        if (days != null) {
            remove(days);
        }
        days = new JPanel();
        days.setLayout(new GridLayout(0, 7));
        days.setBackground(background);
        days.setOpaque(true);
        setupCalendar.setFirstDayOfWeek(1);
        setupCalendar.set(Calendar.DAY_OF_WEEK, setupCalendar.getFirstDayOfWeek());
        for (int i = 0; i < 7; i++) {
            int dayInt = setupCalendar.get(Calendar.DAY_OF_WEEK);
            JLabel label = new JLabel();
            label.setFocusable(true);
            label.setFont(FontManager.getCalendarFontToday());
            label.setPreferredSize(new Dimension(1, 14));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setForeground(Color.BLUE);
            if (dayInt == Calendar.SUNDAY) {
                label.setText("D");
                label.setForeground(Color.RED);
            } else if (dayInt == Calendar.MONDAY) {
                label.setText("S");
            } else if (dayInt == Calendar.TUESDAY) {
                label.setText("T");
            } else if (dayInt == Calendar.WEDNESDAY) {
                label.setText("Q");
            } else if (dayInt == Calendar.THURSDAY) {
                label.setText("Q");
            } else if (dayInt == Calendar.FRIDAY) {
                label.setText("S");
            } else if (dayInt == Calendar.SATURDAY) {
                label.setText("S");
            }
            days.add(label);
            setupCalendar.roll(Calendar.DAY_OF_WEEK, true);
        }
        setupCalendar = (Calendar) calendar.clone();
        setupCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int first = setupCalendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < (first - 1); i++) {
            days.add(new JLabel(""));
        }
        for (int i = 1; i <= setupCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            final int day = i;
            final JLabel label = new JLabel(String.valueOf(day));
            label.setFont(FontManager.getCalendarFont());
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFocusable(true);
            Calendar dayWeek = Calendar.getInstance();
            dayWeek.set(setupCalendar.get(Calendar.YEAR), setupCalendar.get(Calendar.MONTH), day);
            setupCalendar.setFirstDayOfWeek(1);
            if (dayWeek.get(Calendar.DAY_OF_WEEK) == setupCalendar.getFirstDayOfWeek()) {
                label.setForeground(Color.RED);
            }
            if (dayWeek.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                label.setBorder(BorderFactory.createLineBorder(Color.RED));
                label.setFont(FontManager.getCalendarFontToday());
            }
            label.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    System.out.println("label recebeu foco!!!");
                    label.setBackground(selectedBackground);
                    if (Integer.parseInt(label.getText()) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        label.setBorder(BorderFactory.createLineBorder(Color.RED));
                    } else {
                        label.setBorder(BorderFactory.createLineBorder(selectedBorder));
                    }
                }

                public void focusLost(FocusEvent e) {
                    label.setBackground(background);
                    label.setBorder(BorderFactory.createEmptyBorder());
                    System.out.println("label perdeu foco!!!");
                    if (Integer.parseInt(label.getText()) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        label.setBorder(BorderFactory.createLineBorder(Color.RED));
                    }
                }
            });
            label.addMouseListener(new MouseListener() {

                public void mousePressed(MouseEvent e) {
                }

                public void mouseClicked(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                    setVisible(false);
                    setupCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(label.getText()));
                    try {
                        field.setFieldValue(setupCalendar.getTime());
                        field.notifyFieldListeners(field, TCalendarPopup.this);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }

                public void mouseEntered(MouseEvent e) {
                    label.setOpaque(true);
                    label.setBackground(selectedBackground);
                    if (Integer.parseInt(label.getText()) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        label.setBorder(BorderFactory.createLineBorder(Color.RED));
                    } else {
                        label.setBorder(BorderFactory.createLineBorder(selectedBorder));
                    }
                }

                public void mouseExited(MouseEvent e) {
                    label.setOpaque(false);
                    label.setBackground(background);
                    label.setBorder(BorderFactory.createEmptyBorder());
                    if (Integer.parseInt(label.getText()) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        label.setBorder(BorderFactory.createLineBorder(Color.RED));
                    }
                }
            });
            days.add(label);
            if (day == setupCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                label.requestFocus();
                label.requestFocusInWindow();
            }
        }
        this.add(BorderLayout.SOUTH, days);
        this.pack();
    }

    protected JLabel createUpdateButton(final int field, final int amount) {
        final JLabel label = new JLabel();
        label.setForeground(foreground);
        label.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                calendar.add(field, amount);
                updatePopup();
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        return label;
    }
}
