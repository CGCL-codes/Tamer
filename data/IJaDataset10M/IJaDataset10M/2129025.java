package com.qallme.framework.tools.presentation;

import java.util.Comparator;
import java.util.List;
import com.qallme.framework.output.xml.*;

public class AnswerListSorter implements Comparator<List<AnswerFragment>> {

    public int compare(List<AnswerFragment> o1, List<AnswerFragment> o2) {
        for (int l = 0; l < o1.size(); l++) {
            int i = o1.get(l).getText().compareTo(o2.get(l).getText());
            if (i != 0) return i;
        }
        return 0;
    }
}
