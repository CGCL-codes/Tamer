package Timer;

import java.awt.Panel;
import java.awt.MenuBar;
import java.awt.Menu;
import java.awt.MenuItem;
import java.lang.Thread;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.lang.Runnable;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

/**Diese Klasse stellt den Container der die beide beiden Komponenten<br>
Datum und Zeit beinhaltet dar.*/
public class CinemaTimer extends Panel implements Runnable {

    TimerPanel tP;

    DatePanel dP;

    FaktorPanel fP;

    Calc calculiere;

    CinemaDate cD;

    /**Konstruktor der Klasse CinemaTimer*/
    public CinemaTimer() {
        setBackground(Color.lightGray);
        setSize(250, 120);
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints con = new GridBagConstraints();
        setLayout(gb);
        tP = new TimerPanel();
        dP = new DatePanel();
        cD = new CinemaDate(0, 0, 0, 0, 0, 0);
        calculiere = new Calc(dP, tP, cD);
        fP = new FaktorPanel(calculiere, cD);
        con.gridwidth = 1;
        con.gridheight = 1;
        con.insets = new Insets(0, 0, 5, 5);
        gb.setConstraints(tP, con);
        add(tP);
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.gridheight = 1;
        gb.setConstraints(dP, con);
        add(dP);
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.gridheight = GridBagConstraints.REMAINDER;
        con.insets = new Insets(2, 1, 5, 5);
        con.fill = GridBagConstraints.HORIZONTAL;
        gb.setConstraints(fP, con);
        add(fP);
        new Thread(this).start();
    }

    /**liefert ein Objekt von CinemaDate zur�ck */
    public CinemaDate getCinemaDate() {
        return cD;
    }

    /**Methode des Threads ; wird aufgerufen wenn der Thread gestartet wird<br>
	l�uft in einer Endlosschleife*/
    public void run() {
        int tempSec = 61;
        Date date;
        while (true) {
            date = new Date();
            int realSec = (int) Integer.parseInt(date.toString().substring(17, 19));
            try {
                Thread.sleep(5);
                if (realSec != tempSec) {
                    calculiere.setSignal();
                    tempSec = realSec;
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /** n�chsten Tag ermitteln in Form day.month. curDate in Form day.month.*/
    public String getNextDate(String curDate) {
        String date = curDate.substring(3);
        StringTokenizer st = new StringTokenizer(date, ".");
        String day = st.nextToken();
        String month = st.nextToken();
        CinemaDate cd = getCinemaDate();
        CinemaDate cDate = new CinemaDate(new Integer(day).intValue(), new Integer(month).intValue(), cd.getYear(), cd.getHour(), cd.getMinute(), cd.getSecond());
        String[] str = cDate.getNextDates(1);
        st = new StringTokenizer(str[0], ".");
        day = st.nextToken();
        month = st.nextToken();
        String year = st.nextToken();
        cDate = new CinemaDate(new Integer(day).intValue(), new Integer(month).intValue(), new Integer(year).intValue(), 0, 0, 0);
        String nextDate = cDate.getWeekday() + "," + day + "." + month + ".";
        return nextDate;
    }

    /** liefert Daten f�r Programmverwaltung*/
    public String[] getCinemaGuideDates() {
        CinemaDate cd = getCinemaDate();
        String[] s = cd.getNextDates(14);
        String[] cgDates = new String[14];
        for (int i = 0; i < 14; i++) {
            StringTokenizer st = new StringTokenizer(s[i], ".");
            String day = st.nextToken();
            String month = st.nextToken();
            String year = st.nextToken();
            CinemaDate cDate = new CinemaDate(new Integer(day).intValue(), new Integer(month).intValue(), new Integer(year).intValue(), 0, 0, 0);
            String date = cDate.getWeekday() + "," + day + "." + month + ".";
            cgDates[i] = date;
        }
        return cgDates;
    }

    /**pr�ft, ob �bergebene Zeit bereits vergangen wenn ja -> return true*/
    public boolean isLast(int day, int month, int hours, int minutes) {
        boolean isLast = false;
        CinemaDate cd = getCinemaDate();
        int d = cd.getDay();
        int mo = cd.getMonth();
        int h = cd.getHour();
        int m = cd.getMinute();
        System.out.println("akt. Zeit : " + h + ":" + m);
        if (month < mo) isLast = true; else {
            if (month == mo) if (day < d) isLast = true;
        }
        if ((day == d) && (month == mo)) {
            if (hours < h) isLast = true;
            {
                if (hours == h) {
                    if (minutes <= m) isLast = true;
                }
            }
        }
        return isLast;
    }

    /** entfernt reservierte Karten (1/2 Stunden vor Vorstellungsbegin)*/
    public boolean deleteReservedTickets(int day, int month, int hours, int minutes) {
        boolean isDelete = false;
        CinemaDate cd = getCinemaDate();
        int d = cd.getDay();
        int mo = cd.getMonth();
        int h = cd.getHour();
        int m = cd.getMinute();
        System.out.println("akt. Zeit : " + h + ":" + m);
        if (month < mo) isDelete = true; else {
            if (month == mo) if (day < d) isDelete = true;
        }
        if ((day == d) && (month == mo)) {
            int x1 = hours * 60 + minutes;
            int x2 = h * 60 + m;
            if (x1 <= x2) isDelete = true; else {
                if (x1 - x2 <= 30) isDelete = true;
            }
        }
        return isDelete;
    }

    /** liefert String des heutigen Tages Form: Wochentag,Tag.Monat.*/
    public String getCurDateString() {
        CinemaDate cd = getCinemaDate();
        StringTokenizer st = new StringTokenizer(cd.getDateString(), ".");
        String day = st.nextToken();
        String month = st.nextToken();
        String year = st.nextToken();
        CinemaDate cDate = new CinemaDate(new Integer(day).intValue(), new Integer(month).intValue(), new Integer(year).intValue(), 0, 0, 0);
        String date = cDate.getWeekday() + "," + day + "." + month + ".";
        return date;
    }
}
