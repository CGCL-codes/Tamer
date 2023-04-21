package net.sf.cclearly.ui.widgets.date.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import net.sf.cclearly.resources.Icons;

class DaySelectionStatePanel extends DateSelectionStatePanel {

    private static final long serialVersionUID = 1L;

    private JPanel headerPanel;

    private final DateSelectionModel panelDate;

    protected DaySelectionStatePanel(DateSelectionModel panelDate, DateWindowController controller) {
        super(controller);
        this.panelDate = panelDate;
    }

    class DayPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private Calendar day;

        private JLabel dayLabel;

        public DayPanel(Calendar currentDay, boolean isSelected) {
            day = (Calendar) currentDay.clone();
            setPreferredSize(new Dimension(17, 15));
            setMinimumSize(new Dimension(17, 15));
            setMaximumSize(new Dimension(17, 15));
            setLayout(new BorderLayout());
            dayLabel = new JLabel(Integer.toString(day.get(Calendar.DAY_OF_MONTH)));
            if (isSelected) {
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
            } else {
                setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            }
            add(dayLabel, BorderLayout.LINE_END);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {

                Color oldColor;

                public void mouseClicked(MouseEvent e) {
                    panelDate.setDate(day.getTime());
                    getDateController().getDateModel().setDate(panelDate.getDate().getTime());
                    getDateController().acceptDate();
                }

                public void mouseEntered(MouseEvent e) {
                    oldColor = getBackground();
                    setBackground(Color.decode("#88dddd"));
                }

                public void mouseExited(MouseEvent e) {
                    setBackground(oldColor);
                }
            });
        }

        public void setForegroundColor(Color color) {
            dayLabel.setForeground(color);
        }
    }

    void constructPanel() {
        constructHeaderPanel();
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints cst = new GridBagConstraints();
        cst.gridx = 0;
        cst.gridy = 0;
        cst.gridwidth = 9;
        cst.fill = GridBagConstraints.HORIZONTAL;
        cst.anchor = GridBagConstraints.NORTH;
        cst.weightx = 9;
        add(headerPanel, cst);
        cst.fill = GridBagConstraints.HORIZONTAL;
        cst.weightx = 1;
        cst.gridy = 1;
        cst.gridwidth = 1;
        JLabel leftSpacerLabel = new JLabel("");
        leftSpacerLabel.setPreferredSize(new Dimension(14, 16));
        leftSpacerLabel.setMinimumSize(new Dimension(14, 16));
        add(leftSpacerLabel, cst);
        cst.gridx++;
        for (int day = 0; day < 7; day++) {
            String dayName = "";
            switch(day) {
                case 0:
                    dayName = "M";
                    break;
                case 1:
                    dayName = "T";
                    break;
                case 2:
                    dayName = "W";
                    break;
                case 3:
                    dayName = "T";
                    break;
                case 4:
                    dayName = "F";
                    break;
                case 5:
                    dayName = "S";
                    break;
                case 6:
                    dayName = "S";
                    break;
            }
            JPanel dayHeadPanel = new JPanel(new BorderLayout());
            dayHeadPanel.add(new JLabel(dayName), BorderLayout.LINE_END);
            dayHeadPanel.setBackground(Color.WHITE);
            dayHeadPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
            add(dayHeadPanel, cst);
            cst.gridx++;
        }
        JLabel rightSpacerLabel = new JLabel("");
        rightSpacerLabel.setPreferredSize(new Dimension(14, 16));
        rightSpacerLabel.setMinimumSize(new Dimension(14, 16));
        add(rightSpacerLabel, cst);
        cst.gridx++;
        Calendar nowDate = getDateController().getCalendar();
        GregorianCalendar startOfMonth = (GregorianCalendar) getDateController().getCalendar();
        startOfMonth.set(panelDate.getDate().get(Calendar.YEAR), panelDate.getDate().get(Calendar.MONTH), 1);
        cst.anchor = GridBagConstraints.CENTER;
        cst.weighty = 1;
        cst.fill = GridBagConstraints.BOTH;
        Calendar idxDate = getDateController().getCalendar();
        idxDate.setTime(panelDate.getDate().getTime());
        idxDate.set(Calendar.DAY_OF_MONTH, 1);
        if (idxDate.get(Calendar.DAY_OF_WEEK) == idxDate.getFirstDayOfWeek()) {
            idxDate.add(Calendar.WEEK_OF_YEAR, -1);
        }
        idxDate.set(Calendar.DAY_OF_WEEK, idxDate.getFirstDayOfWeek());
        Calendar endDate = getDateController().getCalendar();
        endDate.setTime(panelDate.getDate().getTime());
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        Calendar selectedDate = getDateController().getDateModel().getDate();
        int cnt = 0;
        while (idxDate.before(endDate) || idxDate.equals(endDate)) {
            cst.gridx = 1;
            cst.gridy++;
            for (int day = 1; day <= 7; day++) {
                boolean selected = false;
                if ((selectedDate.get(Calendar.YEAR) == idxDate.get(Calendar.YEAR)) && (selectedDate.get(Calendar.MONTH) == idxDate.get(Calendar.MONTH)) && (selectedDate.get(Calendar.DAY_OF_MONTH) == idxDate.get(Calendar.DAY_OF_MONTH))) {
                    selected = true;
                }
                DayPanel dayPanel = new DayPanel(idxDate, selected);
                if ((nowDate.get(Calendar.YEAR) == idxDate.get(Calendar.YEAR)) && (nowDate.get(Calendar.MONTH) == idxDate.get(Calendar.MONTH)) && (nowDate.get(Calendar.DAY_OF_MONTH) == idxDate.get(Calendar.DAY_OF_MONTH))) {
                    dayPanel.setBackground(Color.decode("#F5E68B"));
                } else {
                    dayPanel.setBackground(Color.WHITE);
                }
                if (idxDate.get(Calendar.MONTH) != endDate.get(Calendar.MONTH)) {
                    dayPanel.setForegroundColor(Color.LIGHT_GRAY);
                }
                add(dayPanel, cst);
                idxDate.add(Calendar.DAY_OF_YEAR, 1);
                cst.gridx++;
            }
            cnt++;
        }
        cst.gridx = 1;
        cst.gridy++;
        cst.gridwidth = 7;
        JPanel todayPanel = new JPanel();
        todayPanel.setBackground(Color.WHITE);
        todayPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        JButton button = new JButton("Today");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                panelDate.setDate(getDateController().getCalendar().getTime());
                getDateController().getDateModel().setDate(panelDate.getDate().getTime());
                getDateController().acceptDate();
            }
        });
        todayPanel.add(button);
        add(todayPanel, cst);
    }

    private void constructHeaderPanel() {
        GregorianCalendar currentDate = (GregorianCalendar) panelDate.getDate();
        headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#D8D7E5"));
        headerPanel.setLayout(new GridBagLayout());
        GridBagConstraints cst = new GridBagConstraints();
        cst.gridx = 0;
        cst.gridy = 0;
        cst.weightx = 0;
        cst.anchor = GridBagConstraints.WEST;
        MouseAdapter linkMouseAdapter = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setForeground(Color.BLACK);
            }
        };
        JLabel backLabel = new JLabel(Icons.ICON_LEFT);
        backLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        backLabel.addMouseListener(linkMouseAdapter);
        backLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                panelDate.rollMonth(false);
                getDateController().setCurrentStatePanel(new DaySelectionStatePanel(panelDate, getDateController()));
            }
        });
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        headerPanel.add(backLabel, cst);
        cst.gridx++;
        cst.weightx = 1;
        cst.anchor = GridBagConstraints.EAST;
        JLabel monthLabel = new JLabel(panelDate.getMonthString() + " ");
        monthLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        monthLabel.addMouseListener(linkMouseAdapter);
        monthLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                getDateController().setCurrentStatePanel(new MonthSelectionStatePanel(panelDate, getDateController()));
            }
        });
        headerPanel.add(monthLabel, cst);
        cst.gridx++;
        cst.weightx = 1;
        cst.anchor = GridBagConstraints.WEST;
        JLabel yearLabel = new JLabel(" " + currentDate.get(Calendar.YEAR));
        yearLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        yearLabel.addMouseListener(linkMouseAdapter);
        yearLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                getDateController().setCurrentStatePanel(new YearSelectionStatePanel(true, panelDate, getDateController()));
            }
        });
        headerPanel.add(yearLabel, cst);
        cst.gridx++;
        cst.weightx = 0;
        cst.anchor = GridBagConstraints.EAST;
        JLabel nextLabel = new JLabel(Icons.ICON_RIGHT);
        nextLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        nextLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextLabel.addMouseListener(linkMouseAdapter);
        nextLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                panelDate.rollMonth(true);
                getDateController().setCurrentStatePanel(new DaySelectionStatePanel(panelDate, getDateController()));
            }
        });
        headerPanel.add(nextLabel, cst);
    }
}
