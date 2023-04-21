package net.sf.borg.ui.calendar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.TextAttribute;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;
import net.sf.borg.common.Errmsg;
import net.sf.borg.common.PrefName;
import net.sf.borg.common.Prefs;
import net.sf.borg.common.Resource;
import net.sf.borg.model.AppointmentModel;
import net.sf.borg.model.Day;
import net.sf.borg.model.Model;
import net.sf.borg.model.TaskModel;
import net.sf.borg.model.beans.Appointment;
import net.sf.borg.model.beans.CalendarBean;
import net.sf.borg.ui.NavPanel;
import net.sf.borg.ui.Navigator;

public class WeekPanel extends JPanel implements Printable {

    private class WeekSubPanel extends ApptBoxPanel implements Navigator, Prefs.Listener, Model.Listener, Printable, MouseWheelListener {

        private Date beg_ = null;

        private double colwidth = 0;

        private float dash1[] = { 1.0f, 3.0f };

        private BasicStroke dashed = new BasicStroke(0.02f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 3.0f, dash1, 0.0f);

        private int date_;

        private int month_;

        private double timecolwidth = 0;

        private int year_;

        private boolean needLoad = true;

        public WeekSubPanel(int month, int year, int date) {
            year_ = year;
            month_ = month;
            date_ = date;
            clearData();
            Prefs.addListener(this);
            addMouseWheelListener(this);
            AppointmentModel.getReference().addListener(this);
            TaskModel.getReference().addListener(this);
        }

        public void clearData() {
            clearBoxes();
            needLoad = true;
            setToolTipText(null);
        }

        public Date getDateForCoord(double x, double y) {
            double col = (x - timecolwidth) / colwidth;
            if (col > 6) col = 6;
            Calendar cal = new GregorianCalendar();
            cal.setTime(beg_);
            cal.add(Calendar.DATE, (int) col);
            return cal.getTime();
        }

        public String getNavLabel() {
            GregorianCalendar cal = new GregorianCalendar(year_, month_, date_, 23, 59);
            int fdow = Prefs.getIntPref(PrefName.FIRSTDOW);
            cal.setFirstDayOfWeek(fdow);
            int offset = cal.get(Calendar.DAY_OF_WEEK) - fdow;
            if (offset == -1) offset = 6;
            cal.add(Calendar.DATE, -1 * offset);
            Date beg = cal.getTime();
            cal.add(Calendar.DATE, 6);
            Date end = cal.getTime();
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
            return df.format(beg) + " " + Resource.getResourceString("__through__") + " " + df.format(end);
        }

        public void goTo(Calendar cal) {
            year_ = cal.get(Calendar.YEAR);
            month_ = cal.get(Calendar.MONTH);
            date_ = cal.get(Calendar.DATE);
            clearData();
            repaint();
        }

        public void next() {
            GregorianCalendar cal = new GregorianCalendar(year_, month_, date_, 23, 59);
            cal.add(Calendar.DATE, 7);
            year_ = cal.get(Calendar.YEAR);
            month_ = cal.get(Calendar.MONTH);
            date_ = cal.get(Calendar.DATE);
            clearData();
            repaint();
        }

        public void prefsChanged() {
            clearData();
            repaint();
        }

        public void prev() {
            GregorianCalendar cal = new GregorianCalendar(year_, month_, date_, 23, 59);
            cal.add(Calendar.DATE, -7);
            year_ = cal.get(Calendar.YEAR);
            month_ = cal.get(Calendar.MONTH);
            date_ = cal.get(Calendar.DATE);
            clearData();
            repaint();
        }

        public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Font sm_font = Font.decode(Prefs.getPref(PrefName.MONTHVIEWFONT));
            clearData();
            int ret = drawIt(g, pageFormat.getWidth(), pageFormat.getHeight(), pageFormat.getImageableWidth(), pageFormat.getImageableHeight(), pageFormat.getImageableX(), pageFormat.getImageableY(), sm_font);
            refresh();
            return ret;
        }

        public void refresh() {
            clearData();
            repaint();
        }

        public void remove() {
        }

        public void today() {
            GregorianCalendar cal = new GregorianCalendar();
            year_ = cal.get(Calendar.YEAR);
            month_ = cal.get(Calendar.MONTH);
            date_ = cal.get(Calendar.DATE);
            clearData();
            repaint();
        }

        private int drawIt(Graphics g, double width, double height, double pageWidth, double pageHeight, double pagex, double pagey, Font sm_font) {
            boolean showpub = false;
            boolean showpriv = false;
            String sp = Prefs.getPref(PrefName.SHOWPUBLIC);
            if (sp.equals("true")) showpub = true;
            sp = Prefs.getPref(PrefName.SHOWPRIVATE);
            if (sp.equals("true")) showpriv = true;
            Graphics2D g2 = (Graphics2D) g;
            Map<TextAttribute, Serializable> stmap = new HashMap<TextAttribute, Serializable>();
            stmap.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            stmap.put(TextAttribute.FONT, sm_font);
            g2.setColor(Color.white);
            g2.fillRect(0, 0, (int) width, (int) height);
            g2.setColor(Color.black);
            int fontHeight = g2.getFontMetrics().getHeight();
            int fontDesent = g2.getFontMetrics().getDescent();
            g2.translate(pagex, pagey);
            Shape s = g2.getClip();
            GregorianCalendar cal = new GregorianCalendar(year_, month_, date_, 23, 59);
            int fdow = Prefs.getIntPref(PrefName.FIRSTDOW);
            cal.setFirstDayOfWeek(fdow);
            int offset = cal.get(Calendar.DAY_OF_WEEK) - fdow;
            if (offset == -1) offset = 6;
            cal.add(Calendar.DATE, -1 * offset);
            beg_ = cal.getTime();
            int caltop = fontHeight + fontDesent;
            int daytop = caltop + fontHeight + fontDesent;
            double rowheight = pageHeight - daytop;
            timecolwidth = pageWidth / 21;
            double aptop = daytop + rowheight / 6;
            colwidth = (pageWidth - timecolwidth) / 7;
            int calbot = (int) rowheight + daytop;
            setResizeBounds((int) aptop, calbot, (int) timecolwidth, (int) (pageWidth));
            setDragBounds(daytop, calbot, (int) timecolwidth, (int) (pageWidth));
            String shr = Prefs.getPref(PrefName.WKSTARTHOUR);
            String ehr = Prefs.getPref(PrefName.WKENDHOUR);
            int starthr = 7;
            int endhr = 22;
            try {
                starthr = Integer.parseInt(shr);
                endhr = Integer.parseInt(ehr);
            } catch (Exception e) {
                Errmsg.errmsg(e);
            }
            int numhalfhours = (endhr - starthr) * 2;
            double tickheight = (calbot - aptop) / numhalfhours;
            SimpleDateFormat dfw = new SimpleDateFormat("EEE dd");
            g2.setColor(this.getBackground());
            g2.fillRect(0, caltop, (int) timecolwidth, calbot - caltop);
            g2.setColor(Color.BLACK);
            g2.setColor(new Color(Prefs.getIntPref(PrefName.UCS_DEFAULT)));
            g2.fillRect((int) timecolwidth, daytop, (int) (pageWidth - timecolwidth), (int) pageHeight - daytop);
            g2.setColor(Color.BLACK);
            Stroke defstroke = g2.getStroke();
            g2.setStroke(dashed);
            for (int row = 0; row < numhalfhours; row++) {
                int rowtop = (int) ((row * tickheight) + aptop);
                g2.drawLine((int) timecolwidth, rowtop, (int) pageWidth, rowtop);
            }
            g2.setStroke(defstroke);
            cal.setTime(beg_);
            g2.setFont(sm_font);
            int smfontHeight = g2.getFontMetrics().getHeight();
            for (int col = 0; col < 7; col++) {
                int colleft = (int) (timecolwidth + col * colwidth);
                if (needLoad) {
                    addDateZone(cal.getTime(), starthr * 60, endhr * 60, new Rectangle(colleft, 0, (int) colwidth, calbot));
                    try {
                        startmin = starthr * 60;
                        endmin = endhr * 60;
                        Day di = Day.getDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), showpub, showpriv, true);
                        Iterator<CalendarBean> it = di.getItems().iterator();
                        int notey = daytop;
                        while (it.hasNext()) {
                            CalendarBean appt = it.next();
                            Date d = appt.getDate();
                            if (d == null) continue;
                            GregorianCalendar acal = new GregorianCalendar();
                            acal.setTime(d);
                            double apstartmin = 60 * acal.get(Calendar.HOUR_OF_DAY) + acal.get(Calendar.MINUTE);
                            int dur = 0;
                            Integer duri = appt.getDuration();
                            if (duri != null) {
                                dur = duri.intValue();
                            }
                            double apendmin = apstartmin + dur;
                            if (!(appt instanceof Appointment) || AppointmentModel.isNote((Appointment) appt) || apendmin < startmin || apstartmin >= endmin - 4 || appt.getDuration() == null || appt.getDuration().intValue() == 0) {
                                if (addNoteBox(cal.getTime(), appt, new Rectangle(colleft + 2, notey, (int) (colwidth - 4), smfontHeight), new Rectangle(colleft, caltop, (int) colwidth, (int) (aptop - caltop))) != null) {
                                    notey += smfontHeight;
                                }
                            } else {
                                addApptBox(cal.getTime(), (Appointment) appt, new Rectangle(colleft + 4, (int) aptop, (int) colwidth - 8, (int) (calbot - aptop)), new Rectangle(colleft, (int) aptop, (int) colwidth, (int) (calbot - aptop)));
                            }
                        }
                    } catch (Exception e) {
                        Errmsg.errmsg(e);
                    }
                    Collection<ApptBox> layoutlist = new ArrayList<ApptBox>();
                    Iterator<Object> bit = boxes.iterator();
                    while (bit.hasNext()) {
                        Box b = (Box) bit.next();
                        if (!(b instanceof ApptBox)) continue;
                        ApptBox ab = (ApptBox) b;
                        Calendar c = new GregorianCalendar();
                        c.setTime(ab.getDate());
                        if (c.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)) layoutlist.add(ab);
                    }
                    ApptBox.layoutBoxes(layoutlist, starthr, endhr);
                }
                g2.setClip(s);
                g2.setColor(this.getBackground());
                g2.fillRect(colleft, caltop, (int) (colwidth), daytop - caltop);
                Calendar today = new GregorianCalendar();
                if (today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && today.get(Calendar.DATE) == cal.get(Calendar.DATE)) {
                    g2.setColor(new Color(Prefs.getIntPref(PrefName.UCS_TODAY)));
                    g2.fillRect(colleft, caltop, (int) (colwidth), daytop - caltop);
                }
                g2.setColor(Color.black);
                cal.add(Calendar.DATE, 1);
            }
            needLoad = false;
            drawBoxes(g2);
            cal.setTime(beg_);
            for (int col = 0; col < 7; col++) {
                int colleft = (int) (timecolwidth + (col * colwidth));
                String dayofweek = dfw.format(cal.getTime());
                int swidth = g2.getFontMetrics().stringWidth(dayofweek);
                g2.drawString(dayofweek, (int) (colleft + (colwidth - swidth) / 2), caltop + fontHeight);
                cal.add(Calendar.DATE, 1);
            }
            g2.drawLine(0, caltop, (int) pageWidth, caltop);
            g2.drawLine(0, daytop, (int) pageWidth, daytop);
            g2.drawLine(0, (int) aptop, (int) pageWidth, (int) aptop);
            g2.drawLine(0, calbot, (int) pageWidth, calbot);
            g2.drawLine(0, caltop, 0, calbot);
            g2.setFont(sm_font);
            boolean mt = false;
            String mil = Prefs.getPref(PrefName.MILTIME);
            if (mil.equals("true")) mt = true;
            for (int row = 1; row < endhr - starthr; row++) {
                int y = (int) ((row * tickheight * 2) + aptop);
                int hr = row + starthr;
                if (!mt && hr > 12) hr = hr - 12;
                String tmlabel = Integer.toString(hr) + ":00";
                g2.drawString(tmlabel, 2, y + smfontHeight / 2);
            }
            for (int col = 0; col < 8; col++) {
                int colleft = (int) (timecolwidth + (col * colwidth));
                g2.drawLine(colleft, caltop, colleft, calbot);
            }
            return Printable.PAGE_EXISTS;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                Font sm_font = Font.decode(Prefs.getPref(PrefName.WEEKVIEWFONT));
                drawIt(g, getWidth(), getHeight(), getWidth() - 20, getHeight() - 20, 10, 10, sm_font);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getWheelRotation() > 0) {
                next();
                nav.setLabel(getNavLabel());
            } else if (e.getWheelRotation() < 0) {
                prev();
                nav.setLabel(getNavLabel());
            }
        }

        public Date startDate() {
            Calendar startCal = new GregorianCalendar();
            startCal.set(Calendar.YEAR, year_);
            startCal.set(Calendar.MONTH, month_);
            startCal.set(Calendar.DAY_OF_MONTH, date_);
            int startOffset;
            if (Prefs.getIntPref(PrefName.FIRSTDOW) == Calendar.SUNDAY) {
                startOffset = startCal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
                startCal.add(Calendar.DAY_OF_MONTH, -1 * startOffset);
            } else if (Prefs.getIntPref(PrefName.FIRSTDOW) == Calendar.MONDAY) {
                startOffset = startCal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
                startCal.add(Calendar.DAY_OF_MONTH, -1 * startOffset);
            }
            Date startDate = startCal.getTime();
            return startDate;
        }

        public Date endDate() {
            Calendar endCal = new GregorianCalendar();
            endCal.set(Calendar.MONTH, month_);
            endCal.set(Calendar.YEAR, year_);
            endCal.set(Calendar.DAY_OF_MONTH, date_);
            int endOffset;
            endOffset = (Prefs.getIntPref(PrefName.FIRSTDOW) + 6) - endCal.get(Calendar.DAY_OF_WEEK);
            endCal.add(Calendar.DAY_OF_MONTH, endOffset);
            Date endDate = endCal.getTime();
            return endDate;
        }
    }

    private NavPanel nav = null;

    private WeekSubPanel wp_ = null;

    public WeekPanel(int month, int year, int date) {
        wp_ = new WeekSubPanel(month, year, date);
        nav = new NavPanel(wp_);
        setLayout(new java.awt.GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        cons.fill = java.awt.GridBagConstraints.BOTH;
        add(nav, cons);
        cons.gridy = 1;
        cons.weightx = 1.0;
        cons.weighty = 1.0;
        add(wp_, cons);
    }

    public void goTo(Calendar cal) {
        wp_.goTo(cal);
        nav.setLabel(wp_.getNavLabel());
    }

    public int print(Graphics arg0, PageFormat arg1, int arg2) throws PrinterException {
        return wp_.print(arg0, arg1, arg2);
    }

    public void refresh() {
        wp_.refresh();
    }
}
