package org.myjerry.evenstar.constants;

import java.util.ArrayList;
import java.util.List;

public class DateHeaderConstants {

    public static final List<String> dateFormats = new ArrayList<String>();

    static {
        dateFormats.add("MMM d, yyyy");
        dateFormats.add("MMMM d, yyyy");
        dateFormats.add("EEEE, MMMM d, yyyy");
        dateFormats.add("MM/dd/yy");
        dateFormats.add("MM/dd/yyyy");
        dateFormats.add("MM.dd.yyyy");
        dateFormats.add("yyyyMMdd");
        dateFormats.add("yyyy/MM/dd");
        dateFormats.add("yyyy-MM-dd");
        dateFormats.add("dd.MM.yy");
        dateFormats.add("dd.MM.yyyy");
        dateFormats.add("dd/MM/yyyy");
        dateFormats.add("EEEE, MMMM dd");
        dateFormats.add("dd MMMM yyyy");
        dateFormats.add("dd MMMM, yyyy");
    }
}
