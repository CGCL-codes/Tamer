package net.taylor.fitnesse;

import java.util.Arrays;
import fitnesse.slim.Converter;
import fitnesse.slim.SlimError;

public class LongArrayConverter implements Converter {

    public String toString(Object o) {
        if (o == null) return "null";
        Long[] integers = (Long[]) o;
        return Arrays.asList(integers).toString();
    }

    public Object fromString(String arg) {
        String[] strings = fromStringToArrayOfStrings(arg);
        Long[] integers = new Long[strings.length];
        for (int i = 0; i < strings.length; i++) {
            try {
                integers[i] = Long.parseLong(strings[i]);
            } catch (NumberFormatException e) {
                throw new SlimError("message:<<CANT_CONVERT_TO_LONG_LIST>>");
            }
        }
        return integers;
    }

    static String[] fromStringToArrayOfStrings(String arg) {
        if (arg.startsWith("[")) arg = arg.substring(1);
        if (arg.endsWith("]")) arg = arg.substring(0, arg.length() - 1);
        String[] strings = arg.split(",");
        for (int i = 0; i < strings.length; i++) strings[i] = strings[i].trim();
        return strings;
    }
}
