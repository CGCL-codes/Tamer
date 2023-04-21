package bravo.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import bravo.calendar.Appointment;

public class AppointmentPanel extends JPanel {

    private static final long serialVersionUID = 0xDEADBEEF;

    private Appointment appt;

    AppointmentPanel() {
        super();
    }

    public void setAppointment(Appointment a) {
        appt = a;
        repaint();
    }

    public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        int largeTextHeight;
        int width, height;
        String apptName = (appt == null ? "No Selection" : appt.getName());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        largeTextHeight = CalendarViewSupport.getFontSize(g, CalendarViewSupport.largeFont);
        width = getWidth();
        height = getHeight();
        g.setColor(CalendarViewSupport.COLOR_APPT_PANEL);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        int curHeight = 1;
        curHeight += CalendarViewSupport.drawText(g, CalendarViewSupport.font, apptName, 1, curHeight, width - 2, largeTextHeight + 1, true);
        curHeight += 1;
        g.drawLine(0, curHeight, width, curHeight);
        if (appt == null) {
            curHeight += CalendarViewSupport.drawText(g, CalendarViewSupport.smallFont, "You do not currently have an appointment selected. The details of an appointment will be displayed when you select one.", 2, curHeight, width - 4, height - curHeight - 1, true) + 1;
            return;
        }
        curHeight += 1;
        Font smallBoldFont = CalendarViewSupport.smallFont.deriveFont(Font.BOLD);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E. MMMM d, yyyy @ h:mm a");
        curHeight += CalendarViewSupport.drawText(g, smallBoldFont, "From:", 2, curHeight, width - 4, height - curHeight - 1) + 1;
        curHeight += CalendarViewSupport.drawText(g, CalendarViewSupport.smallFont, dateFormat.format(appt.getStart().getTime()), 2, curHeight, width - 4, height - curHeight - 1) + 1;
        curHeight += CalendarViewSupport.drawText(g, smallBoldFont, "Until:", 2, curHeight, width - 4, height - curHeight - 1) + 1;
        curHeight += CalendarViewSupport.drawText(g, CalendarViewSupport.smallFont, dateFormat.format(appt.getEnd().getTime()), 2, curHeight, width - 4, height - curHeight - 1) + 1;
        curHeight += CalendarViewSupport.drawText(g, smallBoldFont, "Description:", 2, curHeight, width - 4, height - curHeight - 1) + 1;
        curHeight += CalendarViewSupport.drawText(g, CalendarViewSupport.smallFont, appt.getDesc(), 2, curHeight, width - 4, height - curHeight - 1);
    }
}
