package com.angel.architecture.flex.date;

import java.util.Calendar;
import java.util.Date;
import com.angel.architecture.flex.annotations.internal.MatchParameterType;
import com.angel.architecture.flex.annotations.serialization.Serialization;
import com.angel.architecture.flex.serialization.SerializationType;

/**
 * <p>Un objeto de clase <tt>LocalDate</tt> representa una fecha en el cliente tal como es mostrada por pantalla.
 * Del lado cliente, este objeto act�a como wrapper de un objeto Date de manera tal de serializar
 * los valores de las fechas tal y cual como los ve el usuario</p>
 * <p>La idea es que los objetos de este tipo sean transparentes al desarrollo y por eso se realiza una conversi�n
 * de forma tal de poder trabajar con objetos <tt>java.util.Date</tt> del lado servidor</p>
 *
 * @author Juan Isern
 */
@Serialization(type = SerializationType.FIELD)
@MatchParameterType(Date.class)
public class LocalDate {

    private int fullYear;

    private int month;

    private int day;

    private int hours;

    private int minutes;

    private int seconds;

    public LocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.fullYear = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.hours = calendar.get(Calendar.HOUR_OF_DAY);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.seconds = calendar.get(Calendar.SECOND);
    }

    int getDay() {
        return day;
    }

    int getFullYear() {
        return fullYear;
    }

    int getHours() {
        return hours;
    }

    int getMinutes() {
        return minutes;
    }

    int getMonth() {
        return month;
    }

    int getSeconds() {
        return seconds;
    }

    public Date buildDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, fullYear);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public LocalDate() {
    }

    void setFullYear(int fullYear) {
        this.fullYear = fullYear;
    }

    void setMonth(int month) {
        this.month = month;
    }

    void setDay(int day) {
        this.day = day;
    }

    void setHours(int hours) {
        this.hours = hours;
    }

    void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
