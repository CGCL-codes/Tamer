package com.yerihyo.yeritools.text;

import java.util.List;
import java.util.ArrayList;

public class LineExtractor {

    public static List<String> linearLineChooser(List<String> lines, int interpolation) {
        ArrayList<String> returnList = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            if (i % interpolation == 0) {
                returnList.add(lines.get(i));
            }
        }
        return returnList;
    }

    public static List<String> logLineChooser(List<String> lines, double base) {
        ArrayList<String> returnList = new ArrayList<String>();
        long nextIndex = 0;
        int nextMultiplier = 1;
        for (int i = 0; i < lines.size(); i++) {
            if (i == nextIndex) {
                returnList.add(lines.get(i));
                nextIndex = Math.round(Math.pow(base, nextMultiplier++)) - 1;
            }
        }
        return returnList;
    }
}
