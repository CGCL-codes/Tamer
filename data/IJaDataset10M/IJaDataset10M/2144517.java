package com.calipso.reportgenerator.common.dateresolver;

import com.calipso.reportgenerator.common.DateExpressionResolver;
import java.util.Calendar;

/**
 * Define el comportamiento para poder averiguar el valor de la a o
 */
public class ResolveYear extends DateExpressionResolver {

    public ResolveYear() {
        super();
    }

    /**
   * Devuelve el a o de la fecha previamente seteada
   * @return String
   */
    public String doGetValue() {
        return String.valueOf(getDateFormatter().getCalendar().get(Calendar.YEAR));
    }
}
