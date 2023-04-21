package com.phloc.holiday.parser;

import javax.annotation.Nonnull;
import com.phloc.holiday.HolidayMap;
import com.phloc.holiday.config.Holidays;

/**
 * @author Sven Diedrichsen
 * @author philip
 */
public interface IHolidayParser {

    void parse(int nYear, @Nonnull HolidayMap aHolidayMap, @Nonnull Holidays aConfig);
}
