package com.makotan.util.datetime;

import java.util.Calendar;

public interface NendoCalc {

    Calendar createNendoStartDate(Calendar cal);

    Calendar createNendEndDate(Calendar calender);

    long calcYearAge(Calendar from, Calendar to);

    void setStartMonth(int startMonth);

    void setStartDay(int startDay);
}
