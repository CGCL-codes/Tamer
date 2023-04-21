package de.d3web.we.testcase.kdom;

import java.util.ArrayList;
import java.util.List;
import de.knowwe.core.kdom.AbstractType;
import de.knowwe.core.kdom.Type;
import de.knowwe.core.kdom.parsing.Section;
import de.knowwe.core.kdom.sectionFinder.SectionFinder;
import de.knowwe.core.kdom.sectionFinder.SectionFinderResult;

public class StateRating extends AbstractType {

    public StateRating() {
        this.sectionFinder = new StateRatingSectionFinder();
    }

    class StateRatingSectionFinder implements SectionFinder {

        @Override
        public List<SectionFinderResult> lookForSections(String text, Section<?> father, Type type) {
            List<SectionFinderResult> result = new ArrayList<SectionFinderResult>();
            int start;
            int end;
            if (text.contains(":")) {
                start = text.indexOf(":") + 1;
            } else {
                start = text.indexOf("(") + 1;
            }
            end = text.lastIndexOf(")");
            while (start < text.length() && text.charAt(start) == ' ') {
                start++;
            }
            while (end - 1 > 0 && text.charAt(end - 1) == ' ') {
                end--;
            }
            if (end < start) end = start;
            SectionFinderResult s = new SectionFinderResult(start, end);
            result.add(s);
            return result;
        }
    }
}
