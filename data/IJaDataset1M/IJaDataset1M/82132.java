package de.outofbounds.kinderactive.gui.learn.time;

import de.outofbounds.kinderactive.gui.ButtonFactory;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import de.outofbounds.kinderactive.gui.CategoryTab;
import de.outofbounds.kinderactive.gui.Palette;

/**
 *
 * @author root
 */
public class YearCategoryTab extends CategoryTab {

    static final Color monthColor = new Color(192, 64, 64);

    static final Color monthTraceColor = Palette.RED;

    static final Color dayColor = new Color(64, 192, 64);

    static final Color dayTraceColor = Palette.GREEN;

    static final Color[] monthColors = { new Color(0, 0, 192), new Color(0, 0, 224), new Color(0, 128, 0), new Color(0, 192, 0), new Color(0, 255, 0), new Color(160, 0, 0), new Color(192, 0, 0), new Color(224, 0, 0), new Color(102, 80, 16), new Color(144, 112, 48), new Color(176, 144, 80), new Color(0, 0, 160) };

    static String[] monthNames = { "Januar", "Februar", "M�rz", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember" };

    static String[] seasonNames = { "Fr�hling", "Sommer", "Herbst", "Winter" };

    JPanel seasonsPanel;

    JTextField dateField;

    int speed = 1;

    GregorianCalendar calendar;

    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    /** Creates a new instance of YearCategoryTab */
    public YearCategoryTab() {
        super("intro-year", null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = createTabConstraints();
        gbc.weighty = 0.05;
        add(buildSpeakerPanel(), gbc);
        gbc.gridy++;
        gbc.weighty = 1.0;
        seasonsPanel = buildSeasonsPanel();
        add(seasonsPanel, gbc);
        gbc.gridy++;
        gbc.weighty = 0.05;
        add(buildActionPanel(), gbc);
        setCurrentTime();
        refresh();
    }

    public JPanel buildButtonPanel() {
        buttonPanel = new JPanel();
        return buttonPanel;
    }

    public JPanel buildActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = ButtonFactory.createButtonConstraints();
        JButton decMonthButton = new JButton("<");
        decMonthButton.setBackground(monthTraceColor);
        decMonthButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.MONTH, -1);
                refresh();
            }
        });
        panel.add(decMonthButton, gbc);
        gbc.gridx++;
        JButton decDayButton = new JButton("<");
        decDayButton.setBackground(dayTraceColor);
        decDayButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                refresh();
            }
        });
        panel.add(decDayButton, gbc);
        gbc.gridx++;
        dateField = new JTextField("01.01.1970", 10);
        dateField.setHorizontalAlignment(JTextField.CENTER);
        dateField.setEditable(false);
        panel.add(dateField, gbc);
        gbc.gridx++;
        JButton incDayButton = new JButton(">");
        incDayButton.setBackground(dayTraceColor);
        incDayButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                refresh();
            }
        });
        panel.add(incDayButton, gbc);
        gbc.gridx++;
        JButton incMonthButton = new JButton(">");
        incMonthButton.setBackground(monthTraceColor);
        incMonthButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.MONTH, 1);
                refresh();
            }
        });
        panel.add(incMonthButton, gbc);
        return panel;
    }

    public JPanel buildSpeakerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = ButtonFactory.createButtonConstraints();
        AbstractButton speedButton;
        speedButton = new JButton("x1000");
        speedButton.setBackground(Palette.BLUE.darker());
        speedButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                speed = 1000;
            }
        });
        panel.add(speedButton, gbc);
        gbc.gridx++;
        speedButton = new JButton("x100");
        speedButton.setBackground(Palette.BLUE);
        speedButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                speed = 100;
            }
        });
        panel.add(speedButton, gbc);
        gbc.gridx++;
        speedButton = new JButton("x10");
        speedButton.setBackground(Palette.BLUE.brighter());
        speedButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                speed = 10;
            }
        });
        panel.add(speedButton, gbc);
        gbc.gridx++;
        speedButton = new JButton("x1");
        speedButton.setBackground(Palette.BLUE.brighter().brighter());
        speedButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                speed = 1;
            }
        });
        panel.add(speedButton, gbc);
        gbc.gridx++;
        AbstractButton speakButton = ButtonFactory.createControlButton(ButtonFactory.Type.SPEAK);
        speakButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        panel.add(speakButton, gbc);
        gbc.gridx++;
        AbstractButton currentTimeButton = ButtonFactory.createControlButton(ButtonFactory.Type.CLEAR);
        currentTimeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setCurrentTime();
                speed = 1;
                refresh();
            }
        });
        panel.add(currentTimeButton, gbc);
        return panel;
    }

    public JPanel buildSeasonsPanel() {
        JPanel panel = new JPanel() {

            final Stroke dateHandStroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                int xbase = w / 2;
                int ybase = h / 2;
                int fontSize = w / 45;
                Font font = new Font("Arial", Font.BOLD, fontSize);
                int r = Math.min(w, h) / 2;
                int r1 = (int) (0.95 * r);
                int r2 = (int) (0.75 * r);
                int r3 = (int) (0.05 * r);
                g2.setColor(Color.BLACK);
                for (int i = 0; i < 12; ++i) {
                    g2.setColor(monthColors[i]);
                    g2.fillArc(xbase - r1, ybase - r1, 2 * r1, 2 * r1, (i + 3) * 30, 30);
                }
                AffineTransform t1 = g2.getTransform();
                g2.setColor(Color.BLACK);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                for (int i = 0; i < 12; ++i) {
                    int x = xbase + (int) (r1 * Math.sin(2 * Math.PI * i / 12));
                    int y = ybase - (int) (r1 * Math.cos(2 * Math.PI * i / 12));
                    g2.drawLine(xbase, ybase, x, y);
                    AffineTransform t2 = new AffineTransform(t1);
                    t2.rotate(-2 * Math.PI * (2 * i + 1) / 24, xbase, ybase);
                    g2.setTransform(t2);
                    String monthName = monthNames[i];
                    int stringWidth = fm.stringWidth(monthName);
                    int height = fm.getHeight();
                    g2.drawString(monthName, (int) (xbase - stringWidth / 2), (ybase - (r1 + r2) / 2 + height / 2));
                    g2.setTransform(t1);
                }
                g2.setColor(Color.WHITE);
                g2.fillOval(xbase - r2, ybase - r2, 2 * r2, 2 * r2);
                g2.setColor(Color.BLACK);
                g2.drawOval(xbase - r1, ybase - r1, 2 * r1, 2 * r1);
                g2.drawOval(xbase - r2, ybase - r2, 2 * r2, 2 * r2);
                for (int i = 0; i < 4; ++i) {
                }
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DATE, 1);
                calendar.add(Calendar.DATE, -1);
                int days = calendar.get(Calendar.DATE);
                calendar.set(Calendar.DATE, day);
                int x = xbase - (int) (r * Math.sin(2 * Math.PI * (month + (double) day / days) / 12));
                int y = ybase - (int) (r * Math.cos(2 * Math.PI * (month + (double) day / days) / 12));
                g2.fillOval(xbase - r3, ybase - r3, 2 * r3, 2 * r3);
                g2.setColor(Color.RED);
                g2.setStroke(dateHandStroke);
                g2.drawLine(xbase, ybase, x, y);
            }
        };
        panel.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                int w = getWidth();
                int h = getHeight();
                int xbase = w / 2;
                int ybase = h / 2;
                int dx = e.getX() - xbase;
                int dy = e.getY() - ybase;
                double angle = -Math.atan((double) dx / dy) + (dy >= 0 ? Math.PI : 0);
                if (e.getButton() == e.BUTTON1) {
                    refresh();
                } else if (e.getButton() == e.BUTTON2) {
                } else if (e.getButton() == e.BUTTON3) {
                    refresh();
                }
            }
        });
        return panel;
    }

    public void refresh() {
        seasonsPanel.repaint();
        dateField.setText(dateFormat.format(calendar.getTime()));
    }

    public void setCurrentTime() {
        calendar = new GregorianCalendar();
    }
}
