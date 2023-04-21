package lpp.citytrans.server.model;

import java.util.Set;

public class Enums {

    public enum DayType {

        WORKDAY("workday"), SATURDAY("saturday"), SUNDAY("sunday"), FREEDAY("freeday");

        private String name;

        private DayType(String name) {
            this.name = name;
        }

        public static DayType getByName(String name) {
            for (DayType dt : values()) if (dt.name.equals(name)) return dt;
            return null;
        }

        public static DayType getByDay(int day, Set<DayType> availableDayTypes) {
            if (day < 5 && availableDayTypes.contains(WORKDAY)) return WORKDAY; else if (day == 5 && availableDayTypes.contains(SATURDAY)) return SATURDAY; else if (day == 6 && availableDayTypes.contains(SUNDAY)) return SUNDAY; else if ((day == 5 || day == 6) && availableDayTypes.contains(FREEDAY)) return FREEDAY;
            return WORKDAY;
        }

        public static DayType getByDay(int day) {
            if (day < 5) return WORKDAY; else if (day == 5) return SATURDAY; else if (day == 6) return SUNDAY;
            return WORKDAY;
        }
    }

    ;

    public enum TransportType {

        Bus, Tram, Foot
    }

    ;
}
