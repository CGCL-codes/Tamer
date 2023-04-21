package net.flysource.client.util;

import net.flysource.client.gui.flyeditor.FlyPatternModel;
import java.util.Comparator;

public class PatternNameComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        String s1 = ((FlyPatternModel) o1).getName().toLowerCase();
        String s2 = ((FlyPatternModel) o2).getName().toLowerCase();
        s1 = s1 == null ? "" : s1;
        s2 = s2 == null ? "" : s2;
        return s1.compareTo(s2);
    }
}
