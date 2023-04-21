package net.stickycode.scheduled.configuration;

import java.util.concurrent.TimeUnit;
import net.stickycode.scheduled.Schedule;
import org.junit.Test;

public class ScheduleParserTest {

    private final class NoopScheduleParser extends AbstractScheduleParser {

        @Override
        public boolean matches(String specification) {
            return false;
        }

        @Override
        public Schedule parse(String specification) {
            return null;
        }
    }

    @Test(expected = UnsupportedUnitForSchedulingException.class)
    public void undefinedUnit() {
        parseUnit("rubbish");
    }

    @Test
    public void upperCaseUnit() {
        parseUnit("MINUTE");
    }

    @Test
    public void lowerCaseUnit() {
        parseUnit("day");
    }

    @Test
    public void mixedCaseUnit() {
        parseUnit("Day");
    }

    @Test
    public void allUnits() {
        for (TimeUnit unit : TimeUnit.values()) {
            parseUnit(unit.name().substring(0, unit.name().length() - 1));
        }
    }

    private TimeUnit parseUnit(String string) {
        return new NoopScheduleParser().parseTimeUnit(string);
    }
}
