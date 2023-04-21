package jsslib.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Dieses Objekt repräsentiert einen Zeitraum
 * @author Robert Schuster
 */
public class TimePeriod implements Serializable {

    private static final long serialVersionUID = 814416355886171850L;

    public GregorianCalendar from;

    public GregorianCalendar to;

    public static final int TODAY = 0;

    public static final int YESTERDAY = 1;

    public static final int THIS_WEEK = 2;

    public static final int LAST_WEEK = 3;

    public static final int THIS_MONTH = 4;

    public static final int LAST_MONTH = 5;

    public static final int THIS_JEAR = 6;

    public static final int LAST_JEAR = 7;

    public static final int EVER = 8;

    public int typ = -1;

    public int from_typ = -1;

    public int to_typ = -1;

    public static final long ONE_DAY = 86400000L;

    public static final long ONE_HOUR = 3600000L;

    public static final long ONE_MIN = 60000L;

    /**
     * Ohne einen Parameter wird von und bis auf jetzt gesetzt
     */
    public TimePeriod() {
        from = new GregorianCalendar();
        to = new GregorianCalendar();
    }

    /**
     * Von und bis werden auf einen Zeitraum gesetzt, der durch den typ bestimmt ist
     * 
     * @param typ siehe Konstanten!
     */
    public TimePeriod(int typ) {
        this.typ = typ;
        this.from_typ = typ;
        this.to_typ = typ;
        setFromTyp(typ);
        setToTyp(typ);
    }

    /**
     * Setzt den von-Wert auf einen der Standard-Typen
     * @param neuertyp
     */
    public void setFromTyp(int neuertyp) {
        this.from_typ = neuertyp;
        if (from_typ != to_typ) typ = -1; else typ = neuertyp;
        switch(neuertyp) {
            case TODAY:
                from = new GregorianCalendar();
                setMidnight(from);
                break;
            case YESTERDAY:
                from = new GregorianCalendar();
                setMidnight(from);
                from.setTimeInMillis(from.getTimeInMillis() - ONE_DAY);
                break;
            case THIS_WEEK:
                from = new GregorianCalendar();
                setMidnight(from);
                int day_of_week = from.get(Calendar.DAY_OF_WEEK);
                int day_offset_von;
                if (day_of_week == 1) {
                    day_offset_von = -6;
                } else {
                    day_offset_von = 2 - day_of_week;
                }
                from.setTimeInMillis(from.getTimeInMillis() + ONE_DAY * day_offset_von);
                break;
            case LAST_WEEK:
                from = new GregorianCalendar();
                setMidnight(from);
                int day_of_week2 = from.get(Calendar.DAY_OF_WEEK);
                int day_offset_von2;
                if (day_of_week2 == 1) {
                    day_offset_von2 = -13;
                } else {
                    day_offset_von2 = -5 - day_of_week2;
                }
                from.setTimeInMillis(from.getTimeInMillis() + ONE_DAY * day_offset_von2);
                break;
            case THIS_MONTH:
                from = new GregorianCalendar();
                setMidnight(from);
                from.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case LAST_MONTH:
                from = new GregorianCalendar();
                setMidnight(from);
                from.set(Calendar.DAY_OF_MONTH, 1);
                from.setTimeInMillis(from.getTimeInMillis() - ONE_DAY);
                while (from.get(Calendar.DAY_OF_MONTH) != 1) from.setTimeInMillis(from.getTimeInMillis() - ONE_DAY);
                break;
            case THIS_JEAR:
                from = new GregorianCalendar();
                setMidnight(from);
                from.set(Calendar.DAY_OF_MONTH, 1);
                from.set(Calendar.MONTH, 0);
                break;
            case LAST_JEAR:
                from = new GregorianCalendar();
                int jahr = from.get(Calendar.YEAR);
                jahr--;
                setMidnight(from);
                from.set(Calendar.DAY_OF_MONTH, 1);
                from.set(Calendar.MONTH, 0);
                from.set(Calendar.YEAR, jahr);
                break;
            case EVER:
                from = new GregorianCalendar();
                int jahr2 = from.get(Calendar.YEAR);
                setMidnight(from);
                from.set(2000, 0, 1, 0, 0, 0);
                break;
        }
        if (to != null) if (to.before(from)) {
            to.setTimeInMillis(from.getTimeInMillis());
            set2359(to);
            if (from_typ == TODAY) to_typ = TODAY;
        }
    }

    /**
     * Setzt den bis-Wert auf einen der Standard-Typen
     * @param neuertyp
     */
    public void setToTyp(int neuertyp) {
        this.to_typ = neuertyp;
        if (from_typ != to_typ) typ = -1; else typ = neuertyp;
        switch(neuertyp) {
            case TODAY:
                to = new GregorianCalendar();
                set2359(to);
                break;
            case YESTERDAY:
                to = new GregorianCalendar();
                set2359(to);
                to.setTimeInMillis(to.getTimeInMillis() - ONE_DAY);
                break;
            case THIS_WEEK:
                to = new GregorianCalendar();
                int day_of_week = to.get(Calendar.DAY_OF_WEEK);
                int day_offset_von;
                if (day_of_week == 1) {
                    day_offset_von = -6;
                } else {
                    day_offset_von = 2 - day_of_week;
                }
                int day_offset_bis = day_offset_von + 6;
                set2359(to);
                to.setTimeInMillis(to.getTimeInMillis() + ONE_DAY * day_offset_bis);
                break;
            case LAST_WEEK:
                to = new GregorianCalendar();
                int day_of_week2 = to.get(Calendar.DAY_OF_WEEK);
                int day_offset_von2;
                if (day_of_week2 == 1) {
                    day_offset_von2 = -13;
                } else {
                    day_offset_von2 = -5 - day_of_week2;
                }
                int day_offset_bis2 = day_offset_von2 + 6;
                set2359(to);
                to.setTimeInMillis(to.getTimeInMillis() + ONE_DAY * day_offset_bis2);
                break;
            case THIS_MONTH:
                GregorianCalendar temp = new GregorianCalendar();
                setMidnight(temp);
                temp.set(Calendar.DAY_OF_MONTH, 1);
                to = new GregorianCalendar();
                set2359(to);
                while (to.get(Calendar.MONTH) == temp.get(Calendar.MONTH)) to.setTimeInMillis(to.getTimeInMillis() + ONE_DAY);
                to.setTimeInMillis(to.getTimeInMillis() - ONE_DAY);
                break;
            case LAST_MONTH:
                GregorianCalendar temp2 = new GregorianCalendar();
                setMidnight(temp2);
                temp2.set(Calendar.DAY_OF_MONTH, 1);
                temp2.setTimeInMillis(temp2.getTimeInMillis() - ONE_DAY);
                to = new GregorianCalendar();
                to.setTimeInMillis(temp2.getTimeInMillis());
                set2359(to);
                break;
            case THIS_JEAR:
                to = new GregorianCalendar();
                set2359(to);
                to.set(Calendar.MONTH, 11);
                to.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case LAST_JEAR:
                to = new GregorianCalendar();
                int jahr = to.get(Calendar.YEAR);
                jahr--;
                set2359(to);
                to.set(Calendar.MONTH, 11);
                to.set(Calendar.DAY_OF_MONTH, 31);
                to.set(Calendar.YEAR, jahr);
                break;
            case EVER:
                to = new GregorianCalendar();
                int jahr2 = to.get(Calendar.YEAR);
                set2359(to);
                to.set(Calendar.MONTH, 11);
                to.set(Calendar.DAY_OF_MONTH, 31);
                to.set(Calendar.YEAR, jahr2);
                break;
        }
        if (from != null) if (to.before(from)) {
            from.setTimeInMillis(to.getTimeInMillis());
            setMidnight(from);
            from_typ = to_typ;
        }
    }

    /**
     * Erniedrigt den Von-Wert um einen Tag und prüft, ob es sich dabei dann
     * um heute handelt.
     */
    public void setFromMinusOneDay() {
        from.setTimeInMillis(from.getTimeInMillis() - ONE_DAY);
        GregorianCalendar heute = new GregorianCalendar();
        setMidnight(heute);
        if (from.getTimeInMillis() == heute.getTimeInMillis()) from_typ = TODAY; else from_typ = -1;
        if (to.before(from)) {
            to.setTimeInMillis(from.getTimeInMillis());
            set2359(to);
            to_typ = from_typ;
        }
    }

    /**
     * Erhöht den Von-Wert um einen Tag und prüft, ob es sich dabei dann
     * um heute handelt.
     */
    public void setFromPlusOneDay() {
        from.setTimeInMillis(from.getTimeInMillis() + ONE_DAY);
        GregorianCalendar heute = new GregorianCalendar();
        setMidnight(heute);
        if (from.getTimeInMillis() == heute.getTimeInMillis()) from_typ = TODAY; else from_typ = -1;
        if (to.before(from)) {
            to.setTimeInMillis(from.getTimeInMillis());
            set2359(to);
            to_typ = from_typ;
        }
    }

    /**
     * Erniedrigt den Von-Wert um einen Tag und prüft, ob es sich dabei dann
     * um heute handelt.
     */
    public void setToMinusOneDay() {
        to.setTimeInMillis(to.getTimeInMillis() - ONE_DAY);
        GregorianCalendar heute = new GregorianCalendar();
        set2359(heute);
        if (to.getTimeInMillis() == heute.getTimeInMillis()) to_typ = TODAY; else to_typ = -1;
        if (to.before(from)) {
            from.setTimeInMillis(to.getTimeInMillis());
            setMidnight(from);
            from_typ = to_typ;
        }
    }

    /**
     * Erhöht den Von-Wert um einen Tag und prüft, ob es sich dabei dann
     * um heute handelt.
     */
    public void setToPlusOneDay() {
        to.setTimeInMillis(to.getTimeInMillis() + ONE_DAY);
        GregorianCalendar heute = new GregorianCalendar();
        set2359(heute);
        if (to.getTimeInMillis() == heute.getTimeInMillis()) to_typ = TODAY; else to_typ = -1;
        if (to.before(from)) {
            from.setTimeInMillis(to.getTimeInMillis());
            setMidnight(from);
        }
    }

    /**
     * Setzt die Uhr des Übergebenen Kalenders auf 00:00:00:000
     * @param cal
     */
    public void setMidnight(GregorianCalendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Setzt die Uhr des Übergebenen Kalenders auf 23:59:59:999
     * @param cal
     */
    public void set2359(GregorianCalendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
    }

    /**
     * Gibt das Datum des von-Objekts als String zurück
     * @return Format: 
     */
    public String getFromString() {
        if (from == null) return "null";
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return formater.format(from.getTime());
    }

    /**
     * Gibt das Datum des von-Objekts als String zurück
     * @return Format: 
     */
    public String getToString() {
        if (to == null) return "null";
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return formater.format(to.getTime());
    }

    public String getFromToTimeOfDayString() {
        if (to == null || from == null) return "null";
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        return formater.format(from.getTime()) + " - " + formater.format(to.getTime());
    }

    /**
     * Gibt das Von-Datum in der Formatierung für die Datenbank zurück
     * @return 'yyyy-MM-dd HH:mm:ss'
     */
    public String getDatabaseFromString() {
        if (from == null) return "0000-01-01 00:00:00";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "'" + formater.format(from.getTime()) + "'";
    }

    /**
     * Gibt das Bis-Datum in der Formatierung für die Datenbank zurück
     * @return 'yyyy-MM-dd HH:mm:ss'
     */
    public String getDatabaseToString() {
        if (to == null) return "0000-01-01 00:00:00";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "'" + formater.format(to.getTime()) + "'";
    }

    /**
     * Gibt die den Zeitabstand zwischen von und bis in Millisekunden zurück
     * @return
     */
    public long getDurationInMillis() {
        return to.getTimeInMillis() - from.getTimeInMillis();
    }

    /**
     * Gibt die den Zeitabstand zwischen von und bis in Minuten zurück
     * @return
     */
    public int getDurationInMinutes() {
        return (int) ((to.getTimeInMillis() - from.getTimeInMillis()) / 60000);
    }

    public int getDurationInDays() {
        int ergebnis = 0;
        int bistag = (int) (to.getTimeInMillis() / ONE_DAY);
        int vontag = (int) (from.getTimeInMillis() / ONE_DAY);
        ergebnis = bistag - vontag + 1;
        return ergebnis;
    }

    /**
     * Kürzt den zeitraum, so das nur ganze Monate übrig bleiben
     */
    public void setOnlyCompleteMonth() {
        if (from.get(Calendar.DAY_OF_MONTH) != 1) {
            while (from.get(Calendar.DAY_OF_MONTH) != 1) {
                from.setTimeInMillis(from.getTimeInMillis() + ONE_DAY);
            }
            setMidnight(from);
        }
        GregorianCalendar temp = new GregorianCalendar();
        temp.setTimeInMillis(to.getTimeInMillis() + ONE_DAY);
        if (temp.get(Calendar.MONTH) == to.get(Calendar.MONTH)) {
            while (to.get(Calendar.MONTH) == temp.get(Calendar.MONTH)) {
                to.setTimeInMillis(to.getTimeInMillis() - ONE_DAY);
            }
            set2359(to);
        }
    }

    /**
     * Gibt die Anzahl der Monate zurück, dabei wird aufgerundet
     * @return anzahl der angefangenen Monate, also die Monate, die einen Anteil an diesem Zeitraum haben
     */
    public int getDurationInMonth() {
        int ergebnis = 0;
        int startmonat = from.get(GregorianCalendar.MONTH);
        int startjahr = from.get(GregorianCalendar.YEAR);
        int endmonat = to.get(GregorianCalendar.MONTH);
        int endjahr = to.get(GregorianCalendar.YEAR);
        int start = startjahr * 12 + startmonat;
        int ende = endjahr * 12 + endmonat;
        ergebnis = ende - start + 1;
        return ergebnis;
    }

    /**
     * gibt von_bis-Objekte zurück, die die an diesem Zeitraum beteiligten Monate
     * enthalten
     * @return
     */
    public ArrayList<TimePeriod> getMonth() {
        ArrayList<TimePeriod> ergebnis = new ArrayList<TimePeriod>();
        int anzahl = getDurationInMonth();
        int startmonat = from.get(GregorianCalendar.MONTH);
        int startjahr = from.get(GregorianCalendar.YEAR);
        for (int i = 0; i < anzahl; i++) {
            TimePeriod temp = new TimePeriod();
            temp.setMidnight(temp.from);
            temp.from.set(Calendar.MONTH, startmonat);
            temp.from.set(Calendar.YEAR, startjahr);
            temp.from.set(Calendar.DAY_OF_MONTH, 1);
            temp.to.set(Calendar.MONTH, startmonat);
            temp.to.set(Calendar.YEAR, startjahr);
            temp.to.set(Calendar.DAY_OF_MONTH, 28);
            while (temp.to.get(Calendar.MONTH) == temp.from.get(Calendar.MONTH)) temp.to.setTimeInMillis(temp.to.getTimeInMillis() + ONE_DAY);
            temp.to.setTimeInMillis(temp.to.getTimeInMillis() - ONE_DAY);
            temp.set2359(temp.to);
            ergebnis.add(temp);
            startmonat++;
            if (startmonat == 12) {
                startmonat = 0;
                startjahr++;
            }
        }
        return ergebnis;
    }

    /**
     * Gibt zurück, wie oft der übergebene Wochentag im Zeitraum vorkommt.
     * @param wochentag Sonntag = 1
     * @return
     */
    public int getNumberOfWeekdays(int wochentag) {
        int ergebnis = 0;
        GregorianCalendar temp = new GregorianCalendar();
        temp.setTime(from.getTime());
        int aktuellerTag;
        while (temp.before(to)) {
            aktuellerTag = temp.get(Calendar.DAY_OF_WEEK);
            if (aktuellerTag == wochentag) ergebnis++;
            temp.setTimeInMillis(temp.getTimeInMillis() + ONE_DAY);
        }
        return ergebnis;
    }
}
