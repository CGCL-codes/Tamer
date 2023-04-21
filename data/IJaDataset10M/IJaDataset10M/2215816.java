package es.usc.citius.servando.android.medim.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * @author Ángel Piñeiro
 * 
 */
public class TimeUtils {

    private static GregorianCalendar absoluteStart = new GregorianCalendar(2012, Calendar.FEBRUARY, 15);

    /**
	 * TODO IMPLEMENTAR CORRECTAMENTE
	 * 
	 * @param driverAcquisitionStartDate
	 * @param currentSample
	 * @return
	 */
    public static long samplesFromProtocolStart(Date driverAcquisitionStartDate, long currentSample) {
        return ((driverAcquisitionStartDate.getTime() - absoluteStart.getTimeInMillis()) / 4) + currentSample;
    }

    /**
	 * 
	 * @param tiempo
	 * @return
	 */
    public static GregorianCalendar calendarFromSample(Date driverAcquisitionStartDate, long sample) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(driverAcquisitionStartDate);
        cal.add(Calendar.MILLISECOND, (int) (sample * 4));
        return cal;
    }

    /**
	 * Dado un tempo en milisegundos devolve un array con tres campos correspondentes ó numero de horas, minutos e
	 * segundos
	 * 
	 * @param millis
	 * @return
	 */
    public static long[] splitTimeFields(long millis) {
        return new long[] { millis / (1000 * 60 * 60), (millis % (1000 * 60 * 60)) / (1000 * 60), ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000 };
    }
}
