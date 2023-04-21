package br.ufmg.ubicomp.droidguide.context;

import java.util.Calendar;

/**
	 *	 Contexto Logico do DroideGuide:
	 * 	================================
	 * 
	 * 	Aqui abordaremos as seguintes quest�es 
	 * 	que se relacionam com o contexto logico 
	 * 	da aplicacao:
	 * 		+ Data
	 * 		+ Hora
	 * 	Funcoes para pegar valor.



	/**
	 * -- Computacao Ubiqua -- Prof. Loureiro -- Leticia e Pollyanna
	 * @author Leticia Decker de Sousa
	 * @version 1.0
	 * @since Agosto de 2008
	 * @date 10/19/2008
	 */
public class DataFormated {

    protected String dayOfWeek;

    protected int dayOfMonth;

    protected String monthOfYear;

    protected int yearCurrent;

    protected int hour;

    protected int minute;

    protected int second;

    protected Calendar myCalendar;

    /**
		 * Construtor da classe
		 */
    public DataFormated() {
    }

    /**
		 * Pega a data corrente.
		 */
    public String getDate() {
        String date;
        this.myCalendar = Calendar.getInstance();
        switch(Calendar.DAY_OF_WEEK) {
            case Calendar.SUNDAY:
                this.dayOfWeek = "Sunday, ";
                break;
            case Calendar.MONDAY:
                this.dayOfWeek = "Monday, ";
                break;
            case Calendar.TUESDAY:
                this.dayOfWeek = "Tuesday, ";
                break;
            case Calendar.WEDNESDAY:
                this.dayOfWeek = "Wednesday, ";
                break;
            case Calendar.THURSDAY:
                this.dayOfWeek = "Thursday, ";
                break;
            case Calendar.FRIDAY:
                this.dayOfWeek = "Friday, ";
                break;
            case Calendar.SATURDAY:
                this.dayOfWeek = "Saturday, ";
                break;
            default:
                this.dayOfWeek = " ";
        }
        this.dayOfMonth = Calendar.DAY_OF_MONTH;
        switch(Calendar.MONTH) {
            case Calendar.JANUARY:
                this.monthOfYear = "January ";
                break;
            case Calendar.FEBRUARY:
                this.monthOfYear = "February ";
                break;
            case Calendar.MARCH:
                this.monthOfYear = "March ";
                break;
            case Calendar.APRIL:
                this.monthOfYear = "April ";
                break;
            case Calendar.MAY:
                this.monthOfYear = "May ";
                break;
            case Calendar.JUNE:
                this.monthOfYear = "June ";
                break;
            case Calendar.JULY:
                this.monthOfYear = "July ";
                break;
            case Calendar.AUGUST:
                this.monthOfYear = "August ";
                break;
            case Calendar.SEPTEMBER:
                this.monthOfYear = "September ";
                break;
            case Calendar.OCTOBER:
                this.monthOfYear = "October ";
                break;
            case Calendar.NOVEMBER:
                this.monthOfYear = "November ";
                break;
            case Calendar.DECEMBER:
                this.monthOfYear = "December ";
                break;
            default:
                this.monthOfYear = " ";
        }
        yearCurrent = Calendar.YEAR;
        date = dayOfWeek + monthOfYear + dayOfMonth + "," + yearCurrent;
        return date;
    }

    /**
		 * Pega a hora em hora, minutos e segundos
		 */
    public String getHour() {
        String complhr;
        this.myCalendar = Calendar.getInstance();
        this.hour = Calendar.HOUR_OF_DAY;
        this.minute = Calendar.MINUTE;
        this.second = Calendar.SECOND;
        complhr = hour + ":" + minute + ":" + second;
        return complhr;
    }
}
