package Bus;

import utils.RegexFinder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Raimon Bosch
 */
public class Business {

    private String date;

    /** Creates a new instance of Business */
    public Business(String date) {
        this.setDate(date);
    }

    public String dayOfWeek(int code) {
        switch(code) {
            case Calendar.MONDAY:
                return "Lunes";
            case Calendar.TUESDAY:
                return "Martes";
            case Calendar.WEDNESDAY:
                return "Miercoles";
            case Calendar.THURSDAY:
                return "Jueves";
            case Calendar.FRIDAY:
                return "Viernes";
            case Calendar.SATURDAY:
                return "Sabado";
            case Calendar.SUNDAY:
                return "Domingo";
            case 0:
                return "Sabado";
        }
        return "Monday";
    }

    public String dayOfMonth(int code) {
        switch(code) {
            case Calendar.JANUARY:
                return "Enero";
            case Calendar.FEBRUARY:
                return "Febrero";
            case Calendar.MARCH:
                return "Marzo";
            case Calendar.APRIL:
                return "Abril";
            case Calendar.MAY:
                return "Mayo";
            case Calendar.JUNE:
                return "Junio";
            case Calendar.JULY:
                return "Julio";
            case Calendar.AUGUST:
                return "Agosto";
            case Calendar.SEPTEMBER:
                return "Setiembre";
            case Calendar.OCTOBER:
                return "Octubre";
            case Calendar.NOVEMBER:
                return "Noviembre";
            case Calendar.DECEMBER:
                return "Diciembre";
        }
        return "Enero";
    }

    public String getDateExternal() {
        return getDateExternal(false);
    }

    public String getDateExternal(String type) {
        return getDateExternalByType(type, false);
    }

    public String getDateExternalStrong() {
        return getDateExternal(true);
    }

    public String getDateExternalStrong(String type) {
        return getDateExternalByType(type, true);
    }

    public String getDateExternalByType(String type, boolean strong) {
        if (type.equals("fiesta")) return "<strong>Fiesta</strong> programada para el " + getDateExternal(strong); else if (type.equals("concierto")) return "<strong>Concierto</strong> programado para el " + getDateExternal(strong); else return "<strong>Evento</strong> programado para el " + getDateExternal(strong);
    }

    public String getRealDateExternal(boolean strong, boolean see_hour) {
        String s = getDateExternal(strong);
        if (!see_hour) s = RegexFinder.regexReplace("a las [0-9\\:]*", "", s).trim();
        return RegexFinder.regexReplace("\\/[0-9]*", "", s);
    }

    private String getDateExternal(boolean strong) {
        Date dt = null;
        SimpleDateFormat dt_formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str_strong_init = "<strong>", str_strong_end = "</strong>";
        if (!strong) {
            str_strong_init = "";
            str_strong_end = "";
        }
        try {
            dt = dt_formatter.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        String minutes = "" + dt.getMinutes();
        int i_minutes = 0;
        try {
            i_minutes = Integer.parseInt(minutes);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        if (i_minutes < 10) minutes = "0" + minutes;
        int day = dt.getDay() + 1;
        if (dt.getHours() < 6) {
            int day_after = (day - 1) % 7;
            int num_day_after = dt.getDate() - 1;
            if (num_day_after != 0) {
                return str_strong_init + "" + dayOfWeek(day_after) + " " + num_day_after + "/" + dt.getDate() + " de " + dayOfMonth(dt.getMonth()) + " del " + (dt.getYear() + 1900) + "" + str_strong_end + " a las " + dt.getHours() + ":" + minutes;
            }
        }
        return str_strong_init + "" + dayOfWeek(day) + " " + dt.getDate() + " de " + dayOfMonth(dt.getMonth()) + " del " + (dt.getYear() + 1900) + "" + str_strong_end + " a las " + dt.getHours() + ":" + minutes;
    }

    public String getDate() {
        if (this.date == null) {
            System.out.println("Se devolvio la fecha actual puesto que el campo fecha estaba a null.");
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return date_format.format(new Date());
        }
        return date.replace("'", "&#39;").replace("\n", "").replace("\r", "");
    }

    public void setDate(String date) {
        this.date = date;
    }
}
