package ivia.italo.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    public static Date criaData(int mes, int ano) {
        Calendar c = new GregorianCalendar();
        c.set(ano, mes, 1);
        return c.getTime();
    }

    public static int pegaMaximoMes(Date data) {
        Calendar c = new GregorianCalendar();
        c.setTime(data);
        return c.getMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int pegaDiaMes(Date data) {
        Calendar c = new GregorianCalendar();
        c.setTime(data);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static String diaSemana(int dia) {
        switch(dia) {
            case 1:
                return "Domingo";
            case 2:
                return "Segunda";
            case 3:
                return "Terca";
            case 4:
                return "Quarta";
            case 5:
                return "Quinta";
            case 6:
                return "Sexta";
            case 7:
                return "Sabado";
            default:
                return "Erro de dia";
        }
    }
}
