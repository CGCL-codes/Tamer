package de.d3web.we.kdom.sectionFinder;

import java.util.List;
import de.d3web.we.kdom.Section;
import de.d3web.we.kdom.Type;

public abstract class AbstractSingleResultFinder implements SectionFinder {

    @Override
    public List<SectionFinderResult> lookForSections(String text, Section<?> father, Type type) {
        return SectionFinderResult.createSingleItemList(lookForSection(text, father, type));
    }

    public abstract SectionFinderResult lookForSection(String text, Section<?> father, Type type);
}
