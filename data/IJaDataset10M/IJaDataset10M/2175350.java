package Plan58.provider;

import java.util.*;
import java.text.*;

/**
 * The Class datums.
 * 
 * @author Sören Haag -660553
 * 
 * Diese Klasse liest das Systemdatum und prüft die Korrektheit eines übergebenen Datums
 */
public class datums {

    /**
	 * Die Funktion getdatums bestimmt das aktuelle Systemdatum und gibt dieses als formatierten String
	 * zurueck.
	 * 
	 * @return formatiertes Datum - Typ String
	 */
    public String getdatums() {
        String ret = null;
        Date dt = new Date();
        SimpleDateFormat dfd = new SimpleDateFormat("dd.MM.yyyy");
        dfd.setTimeZone(TimeZone.getDefault());
        ret = dfd.format(dt);
        return ret;
    }

    /**
	 * Die Funktion gettime bestimmt die aktuelle Systemzeit und gibt diese als formatierten String
	 * zurueck.
	 * 
	 * @return formatierte Zeit - Typ String
	 */
    public String gettime() {
        String ret = null;
        Date dt = new Date();
        SimpleDateFormat dft = new SimpleDateFormat("HH:mm:ss");
        dft.setTimeZone(TimeZone.getDefault());
        ret = dft.format(dt);
        return ret;
    }

    /**
	 * Checks if is valid date.
	 * 
	 * @param str the str
	 * 
	 * @return true, if is valid date
	 */
    public boolean isValidDate(String str) {
        try {
            DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);
            formatter.setLenient(false);
            @SuppressWarnings("unused") java.util.Date date = formatter.parse(str);
        } catch (java.text.ParseException e) {
            return false;
        }
        return true;
    }

    /**
	 * Nextweek.
	 * 
	 * @param date the date
	 */
    public void nextweek(String date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
    }
}
