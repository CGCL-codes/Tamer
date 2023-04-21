package org.skins.web.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class StrutsValidatorUtil {

    /**
	 * Esegue un controllo formale sulla data. Sul file di properties di struts
	 * per la visualizzazione dei messaggi occorre indicare le suguenti
	 * tipologie field.mandatory --> per il campo obbligatorio data.errata -->
	 * errore generico della data data.nonvalida --> Data inserita non valida
	 * 
	 * @param errors
	 *            oggetto struts contenete gli errori
	 * @param property
	 *            nome della property dell' ActionForm
	 * @param date
	 *            data da controllare
	 * @param mandatory
	 *            se true si tratta di un campo obbligatorio
	 */
    public static boolean checkDate(ActionMessages errors, String property, String date, boolean mandatory) {
        if (date.trim().length() == 0 && !mandatory) {
            return true;
        }
        if (date.trim().length() == 0 && mandatory) {
            errors.add(property, new ActionMessage("field.mandatory"));
            return false;
        }
        StringTokenizer tokenDate = new StringTokenizer(date, "/");
        if (date.length() > 10) {
            errors.add(property, new ActionMessage("data.errata"));
            return false;
        }
        List listDate = new ArrayList();
        while (tokenDate.hasMoreTokens()) {
            String s = tokenDate.nextToken();
            listDate.add(s);
        }
        if (listDate.size() != 3) {
            errors.add(property, new ActionMessage("data.errata"));
            return false;
        }
        String giorno = (String) listDate.get(0);
        String mese = (String) listDate.get(1);
        String anno = (String) listDate.get(2);
        if (!StringUtils.isNumeric(giorno) || !StringUtils.isNumeric(mese) || !StringUtils.isNumeric(anno)) {
            errors.add(property, new ActionMessage("data.errata"));
            return false;
        }
        if (giorno.length() != 2 || mese.length() != 2 || anno.length() != 4) {
            errors.add(property, new ActionMessage("data.errata"));
            return false;
        }
        if (Integer.parseInt(mese) > 12 || Integer.parseInt(mese) < 1) {
            errors.add(property, new ActionMessage("data.nonvalida"));
            return false;
        }
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.MONTH, Integer.parseInt(mese) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(anno));
        int iGiorno = Integer.parseInt(giorno);
        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (iGiorno > maxDayOfMonth) {
            errors.add(property, new ActionMessage("data.nonvalida"));
            return false;
        }
        return true;
    }

    public static Calendar getDateFromString(String data) throws ParseException {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.ITALIAN);
        Date date = dateFormat.parse(data);
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance(Locale.ITALIAN);
        calendar.setTime(date);
        return calendar;
    }

    public static String formatCurrency(String value) {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.ITALIAN);
        format.applyPattern("#####.##");
        format.setMinimumFractionDigits(2);
        Double d = new Double(value);
        String res = format.format(d);
        return res;
    }

    public static String getToday() {
        Calendar calendar = getNow();
        String giorno = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        giorno = "00".substring(giorno.length()) + giorno;
        String mese = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        mese = "00".substring(mese.length()) + mese;
        String anno = Integer.toString(calendar.get(Calendar.YEAR));
        StringBuffer sb = new StringBuffer(giorno).append("/").append(mese).append("/").append(anno);
        return sb.toString();
    }

    public static Calendar getNow() {
        Calendar calendar = GregorianCalendar.getInstance(Locale.ITALIAN);
        calendar.setTime(new Date(System.currentTimeMillis()));
        return calendar;
    }

    public static boolean isIntervalloTempoOK(String dateFrom, String dateTo) {
        SimpleDateFormat dfDal = (SimpleDateFormat) SimpleDateFormat.getDateInstance(DateFormat.DATE_FIELD);
        SimpleDateFormat dfAl = (SimpleDateFormat) SimpleDateFormat.getDateInstance(DateFormat.DATE_FIELD);
        try {
            Date dal = dfDal.parse(dateFrom);
            Date al = dfAl.parse(dateTo);
            return al.compareTo(dal) >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
