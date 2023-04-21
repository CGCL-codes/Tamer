package com.fh.auge.core;

import com.domainlanguage.money.Money;
import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.CalendarInterval;

public interface IPerformance {

    public Gain getGain(CalendarInterval interval);

    public Money getValue(CalendarDate date);
}
