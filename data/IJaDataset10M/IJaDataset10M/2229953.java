package fi.helsinki.cs.kaisei;

import java.util.LinkedHashMap;

public class Weekday {

    public static enum Day {

        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    ;

    public static LinkedHashMap<Integer, Day> intToEnumMap = new LinkedHashMap<Integer, Day>();

    public static LinkedHashMap<Day, Integer> enumToIntMap = new LinkedHashMap<Day, Integer>();

    public static LinkedHashMap<Day, String> longNameMap = new LinkedHashMap<Day, String>();

    static {
        intToEnumMap.put(1, Day.MON);
        intToEnumMap.put(2, Day.TUE);
        intToEnumMap.put(3, Day.WED);
        intToEnumMap.put(4, Day.THU);
        intToEnumMap.put(5, Day.FRI);
        intToEnumMap.put(6, Day.SAT);
        intToEnumMap.put(7, Day.SUN);
        enumToIntMap.put(Day.MON, 1);
        enumToIntMap.put(Day.TUE, 2);
        enumToIntMap.put(Day.WED, 3);
        enumToIntMap.put(Day.THU, 4);
        enumToIntMap.put(Day.FRI, 5);
        enumToIntMap.put(Day.SAT, 6);
        enumToIntMap.put(Day.SUN, 7);
        longNameMap.put(Day.MON, "Monday");
        longNameMap.put(Day.TUE, "Tuesday");
        longNameMap.put(Day.WED, "Wednesday");
        longNameMap.put(Day.THU, "Thursday");
        longNameMap.put(Day.FRI, "Friday");
        longNameMap.put(Day.SAT, "Saturday");
        longNameMap.put(Day.SUN, "Sunday");
    }
}
