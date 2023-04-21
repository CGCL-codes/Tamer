package de.bea.domingo.groupware.repeat;

import de.bea.domingo.map.DirectMapper;

/**
 * Mapper for {@link MonthlyByDay} repeat options.
 *
 * @see de.bea.domingo.groupware.repeat.MonthlyByDay
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class MonthlyByDayMapper extends RepeatIntervalMapper {

    /**
     * Constructor.
     */
    public MonthlyByDayMapper() {
        super(MonthlyByDay.class);
        add(new DirectMapper("RepeatInterval", "Interval", String.class).withTextItem());
    }
}
